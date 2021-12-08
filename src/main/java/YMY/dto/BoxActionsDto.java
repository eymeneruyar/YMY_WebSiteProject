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
                        hm.put(Check.message,"Ödeme (ilk) kayıt işlemi başarıyla tamamlandı!");
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
