package YMY.dto;

import YMY.entities.*;
import YMY.repositories.BoxActionsRepository;
import YMY.repositories.CompanyRepository;
import YMY.repositories.CustomerRepository;
import YMY.repositories.InvoiceRepository;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class BoxActionsDto {

    final CompanyRepository companyRepository;
    final CustomerRepository customerRepository;
    final InvoiceRepository invoiceRepository;
    final BoxActionsRepository boxActionsRepository;
    final UserService userService;

    public BoxActionsDto(CompanyRepository companyRepository, CustomerRepository customerRepository, InvoiceRepository invoiceRepository, BoxActionsRepository boxActionsRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.boxActionsRepository = boxActionsRepository;
        this.userService = userService;
    }

    //Save payment process
    public Map<Check,Object> save(BoxActions boxActions, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    //Kasa girişi ise
                    if(boxActions.getDescription() == 1 && boxActions.getInvoice() != null && boxActions.getCompany() != null && boxActions.getCustomer() != null){
                        //Faturanın bir miktarı ödenmiş ise
                        if(boxActionsRepository.existsByStatusEqualsAndUserIdEqualsAndInvoice_IdEquals(true,user.getId(),boxActions.getInvoice().getId())){
                            Optional<BoxActions> optionalBoxActions = boxActionsRepository.findByStatusEqualsAndUserIdEqualsAndInvoice_IdEquals(true, user.getId(), boxActions.getInvoice().getId());
                            if(optionalBoxActions.isPresent()){
                                BoxActions ba = optionalBoxActions.get(); //Tabloda ödemesi olan
                                float amount = boxActions.getAmount();
                                float totalPaid = ba.getInvoice().getPaid() + amount;
                                float debt = ba.getInvoice().getDebt();
                                if(totalPaid <= debt){
                                    ba.setAmount(totalPaid);
                                    boxActionsRepository.saveAndFlush(ba);
                                    invoiceRepository.updateRemainingDebtAndPaid(boxActions.getAmount(),true, user.getId(),boxActions.getInvoice().getId());
                                    invoiceRepository.updatePaidStatus(true,user.getId(),boxActions.getInvoice().getId());
                                    hm.put(Check.status,true);
                                    hm.put(Check.message,"Ödeme (mevcut) kayıt işlemi başarıyla tamamlandı!");
                                    hm.put(Check.result,ba);
                                }else{
                                    hm.put(Check.status,false);
                                    hm.put(Check.message,"Toplam ödenen miktar kayıtlı borcu aşmaktadır!");
                                }
                            }else{
                                hm.put(Check.status,false);
                                hm.put(Check.message,"Önceki ödeme işlemi bulunamadı!");
                            }
                        }else{ //Yeni ödeme işlemi ise
                            boxActions.setUserId(user.getId());
                            boxActions.setStatus(true);
                            boxActions.setDate(Util.generateDate());
                            invoiceRepository.updateRemainingDebtAndPaid(boxActions.getAmount(),true, user.getId(),boxActions.getInvoice().getId());
                            invoiceRepository.updatePaidStatus(true,user.getId(),boxActions.getInvoice().getId());
                            boxActionsRepository.saveAndFlush(boxActions);
                            hm.put(Check.status,true);
                            hm.put(Check.message,"Kasa girişi ödeme (ilk) kayıt işlemi başarıyla tamamlandı!");
                            hm.put(Check.result,boxActions);
                        }
                    }else{
                        //Kasa çıkış işlemi
                        boxActions.setUserId(user.getId());
                        boxActions.setStatus(true);
                        boxActions.setDate(Util.generateDate());
                        boxActionsRepository.saveAndFlush(boxActions);
                        hm.put(Check.status,true);
                        hm.put(Check.message,"Kasa çıkışı ödeme kayıt işlemi başarıyla tamamlandı!");
                        hm.put(Check.result,boxActions);
                    }
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Ödeme kayıt işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "Ödeme işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,BoxActionsDto.class);
        }
        return hm;
    }

    //List of box actions (Start of month - Today)
    public Map<Check,Object> listBoxActionsPaydayToToday(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        String startDate = "";
        String endDate = "";
        try {
            if(user.getId() != null){
                String[] date = Util.generateDate().split("-");
                startDate = date[0] + "-" + date[1] + "-" + "01";
                endDate = Util.generateDate();
                hm.put(Check.status,true);
                hm.put(Check.message,"Firmalar başarılı bir şekilde listelendi!");
                hm.put(Check.result,boxActionsRepository.findByStatusEqualsAndUserIdEqualsAndDateBetweenOrderByIdDesc(true,user.getId(),startDate,endDate));
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapıp tekrar deneyin!");
            }
        } catch (Exception e) {
            String error = "Kasa hareketleri listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, BoxActions.class);
        }
        return hm;
    }

    //List of box actions by selected description, start and end date
    public Map<Check,Object> listBoxActionsByDescriptionAndDate(String description,String date){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        String[] dateArr = date.replaceAll(" to ",",").split(",");
        String startDate = dateArr[0];
        String endDate = dateArr[1];
        try{
            int descId = Integer.parseInt(description);
            if(user.getId() != null){
                if(descId == 0 || descId == 1){
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Kasa işlemleri (giriş veya çıkış) başarılı bir şekilde listelendi!");
                    hm.put(Check.result,boxActionsRepository.findByUserIdEqualsAndStatusEqualsAndDescriptionEqualsAndTransactionDateBetween(user.getId(),true,descId,startDate,endDate));
                }else{
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Kasa işlemleri (tümü) başarılı bir şekilde listelendi!");
                    hm.put(Check.result,boxActionsRepository.findByUserIdEqualsAndStatusEqualsAndTransactionDateBetween(user.getId(),true,startDate,endDate));
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapıp tekrar deneyin!");
            }
        }catch (Exception e){
            String error = "Kasa işlemleri (kasa tanımı ve tarihe göre) listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, BoxActions.class);
        }
        return hm;
    }

    //List of box actions by description,date and company
    public Map<Check,Object> listBoxActionsByDescriptionAndDateAndCompany(String description,String date,String company){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        String[] dateArr = date.replaceAll(" to ",",").split(",");
        String startDate = dateArr[0];
        String endDate = dateArr[1];
        try {
            int descId = Integer.parseInt(description);
            int companyId = Integer.parseInt(company);
            if(user.getId() != null){
                if(descId == 1){ // Kasa tanımı giriş ise
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Kasa işlemleri (giriş) başarılı bir şekilde listelendi!");
                    hm.put(Check.result,boxActionsRepository.findByStatusEqualsAndUserIdEqualsAndDescriptionEqualsAndTransactionDateBetweenAndCompany_IdEquals(true, user.getId(),descId,startDate,endDate,companyId));
                }else{
                    hm.put(Check.status,false);
                    hm.put(Check.message,"Yalnızca kasa tanımı Giriş olan ifadeler firma ile birlikte filtrelenebilir!");
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapıp tekrar deneyin!");
            }
        } catch (Exception e) {
            String error = "Kasa işlemleri (kasa tanımı, tarih ve firmaya göre) listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, BoxActions.class);
        }
        return hm;
    }

    //List of companies
    public Map<Check,Object> listCompany(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(user.getId() != null){
                hm.put(Check.status,true);
                hm.put(Check.message,"Firmalar başarılı bir şekilde listelendi!");
                hm.put(Check.result,companyRepository.findByStatusEqualsAndUserIdEqualsOrderByIdAsc(true,user.getId()));
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapıp tekrar deneyin!");
            }
        } catch (Exception e) {
            String error = "Firmalar listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Company.class);
        }
        return hm;
    }

    //List of customers by selected company
    public Map<Check,Object> listOfCustomerBySelectedCompany(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            int id = Integer.parseInt(stId);
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if(user.getId() != null){
                if(optionalCompany.isPresent()){
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Seçilen firmaya ait müşteriler başarılı bir şekilde sıralandı!");
                    hm.put(Check.result,customerRepository.findByStatusAndUserIdAndCompany_Id(true, user.getId(),id));
                }else{
                    hm.put(Check.status,false);
                    hm.put(Check.message,"Seçilen firma sistemde bulunmamaktadır!");
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapıp tekrar deneyin!");
            }
        } catch (Exception e) {
            String error = "Seçilen firmaya ait müşteriler listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Customer.class);
        }
        return hm;
    }

    //List of invoice code by selected customer
    public Map<Check,Object> listOfInvoiceCodeBySelectedCustomer(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            int id = Integer.parseInt(stId);
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if(user.getId() != null){
                if(optionalCustomer.isPresent()){
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Seçilen müşteriye ait faturalar başarılı bir şekilde sıralandı!");
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndCustomer_IdEqualsOrderByIdDesc(true,user.getId(),id));
                }else{
                    hm.put(Check.status,false);
                    hm.put(Check.message,"Seçilen müşteri sistemde bulunmamaktadır!");
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapıp tekrar deneyin!");
            }
        } catch (Exception e) {
            String error = "Seçilen müşteriye ait faturalar listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Invoice.class);
        }
        return hm;
    }

}
