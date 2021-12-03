package YMY.dto;

import YMY.entities.*;
import YMY.repositories.CompanyRepository;
import YMY.repositories.CustomerRepository;
import YMY.repositories.InvoiceRepository;
import YMY.repositories.WorksRepository;
import YMY.services.CacheService;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class InvoiceAddDto {

    final CompanyRepository companyRepository;
    final CustomerRepository customerRepository;
    final InvoiceRepository invoiceRepository;
    final WorksRepository worksRepository;
    final UserService userService;
    final CacheService cacheService;
    public InvoiceAddDto(CompanyRepository companyRepository, CustomerRepository customerRepository, InvoiceRepository invoiceRepository, WorksRepository worksRepository, UserService userService, CacheService cacheService) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.worksRepository = worksRepository;
        this.userService = userService;
        this.cacheService = cacheService;
    }

    //SaveOrUpdate Invoice
    public Map<Check,Object> invoiceSaveOrUpdate(Invoice invoice, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        List<Float> result = new ArrayList<>();
        float debt = 0;
        float discount = 0;
        float kdv = 0;
        try {
            //System.out.println("workses " + invoice.getWorkses());
            //System.out.println("workses size " + invoice.getWorkses().size());
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    //Works save process
                    invoice.getWorkses().forEach(it ->{
                        Integer quantity = it.getQuantity();
                        Float unitPrice = it.getUnitPrice();
                        Float total = quantity * unitPrice;
                        result.add(total);
                        it.setStatus(true);
                        it.setDate(Util.generateDate());
                        it.setUserId(user.getId());
                        it.setTotal(total);
                    });
                    //Works save process
                    debt = (float) result.stream().mapToDouble(Float::floatValue).sum();
                    discount = invoice.getDiscount();
                    kdv = invoice.getVat();
                    //Düzenlenmesi gerek
                    if(kdv == 18 && discount > 0){
                        debt = debt - (debt*(discount/100));
                        debt = debt + (debt * (kdv/100));
                        System.out.println("KDV ve iskonto var: " + debt);
                    }
                    else if(kdv == 18 && discount <= 0){
                        debt = debt + (debt * (kdv/100));
                        System.out.println("KDV var ama iskonto yok: " + debt);
                    }
                    else if(kdv == 0 && discount > 0){
                        debt = debt - (debt*(discount/100));
                        System.out.println("KDV yok ama iskonto var: " + debt);
                    }
                    //Düzenlenmesi gerek
                    invoice.setUserId(user.getId());
                    invoice.setStatus(true);
                    invoice.setPaidStatus(false);
                    invoice.setDate(Util.generateDate());
                    invoice.setDebt(debt);
                    invoice.setPaid(0f);
                    invoice.setRemainingDebt(0f);
                    invoiceRepository.saveAndFlush(invoice);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Fatura kayıt işlemi başarıyla tamamlandı!");
                    hm.put(Check.result,invoice);
                    cacheService.cacheRefresh("invoiceAddListInvoiceThisMonth"); //Refresh cache
                    cacheService.cacheRefresh("invoiceAddlistFilteredInvoice"); //Refresh cache
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Fatura kayıt işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Invoice.class);
        }
        return hm;
    }

    //Listing invoice (Start of Month - Today)
    public Map<Check,Object> listInvoiceThisMonth(){
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
                hm.put(Check.message,"İş emirleri başarılı bir şekilde listelendi!");
                hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndDateBetweenOrderByInvoiceCodeDesc(true,user.getId(),startDate,endDate));
            }else{
                String error = "Lütfen hesabınıza giriş yapıp tekrar deneyiniz!";
                hm.put(Check.status,false);
                hm.put(Check.message,error);
                Util.logger(error, Invoice.class);
            }
        } catch (Exception e) {
            String error = "İş emirleri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Invoice.class);
        }
        return hm;
    }

    //Listing invoice (Date range - Company - Billing Status)
    public Map<Check,Object> listFilteredInvoice(String date,String companyId,String billingStatus){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        String[] dateArr = date.replaceAll(" to ",",").split(",");
        String startDate = dateArr[0];
        String endDate = dateArr[1];
        try {
            Integer id = Integer.parseInt(companyId);
            Integer billingStatusId = Integer.parseInt(billingStatus);
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if(user.getId() != null){
                if(optionalCompany.isPresent()){
                    hm.put(Check.status,true);
                    hm.put(Check.message,"İş emirleri başarılı bir şekilde listelendi!");
                    if(billingStatusId == 0){ //Fatura Kesilmeyecek
                        hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsAndBillingStatusEqualsOrderByInvoiceCodeDesc(true,user.getId(),startDate,endDate,id,billingStatus));
                    }else if(billingStatusId == 1){ //Fatura kesilecek
                        hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsAndBillingStatusEqualsOrderByInvoiceCodeDesc(true,user.getId(),startDate,endDate,id,billingStatus));
                    }else if(billingStatusId == 2){ //Tümü
                        hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsOrderByInvoiceCodeDesc(true,user.getId(),startDate,endDate,id));
                    }else{
                        hm.put(Check.result,null);
                    }
                }else{
                    String error = "Seçilen firma bulunamadı!";
                    hm.put(Check.status,false);
                    hm.put(Check.message,error);
                }
            }else{
                String error = "Lütfen hesabınıza giriş yapıp tekrar deneyiniz!";
                hm.put(Check.status,false);
                hm.put(Check.message,error);
                Util.logger(error, Invoice.class);
            }
        } catch (Exception e) {
            String error = "İş emirleri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Invoice.class);
        }
        return hm;
    }

    //Delete invoice by selected id
    public Map<Check,Object> deleteInvoice(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            Integer id = Integer.parseInt(stId);
            Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
            if(optionalInvoice.isPresent()){
                Invoice invoice = optionalInvoice.get();
                invoice.setStatus(false);
                invoice.getWorkses().forEach(item -> {
                    item.setStatus(false);
                });
                invoiceRepository.saveAndFlush(invoice);
                hm.put(Check.status,true);
                hm.put(Check.message,"Fatura silme işlemi başarıyla gerçekleştirildi!");
                cacheService.cacheRefresh("invoiceAddListInvoiceThisMonth"); //Refresh cache
                cacheService.cacheRefresh("invoiceAddlistFilteredInvoice"); //Refresh cache
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Silinmek istenen fatura bulunamadı!");
            }
        } catch (Exception e) {
            String error = "Silme işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Invoice.class);
        }
        return hm;
    }

    //Listing company by user in system
    public Map<Check,Object> listCompanyByUserId(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(user.getId() != null){
                hm.put(Check.status,true);
                hm.put(Check.message,"Firmalar başarılı bir şekilde listelendi!");
                hm.put(Check.result,companyRepository.findByStatusEqualsAndUserIdEqualsOrderByIdAsc(true,user.getId()));
            }else{
                String error = "Lütfen hesabınıza giriş yapıp tekrar deneyiniz!";
                hm.put(Check.status,false);
                hm.put(Check.message,error);
                Util.logger(error, Invoice.class);
            }
        } catch (Exception e) {
            String error = "Firmalar getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Invoice.class);
        }
        return hm;
    }

    //Listing customer by selected company
    public Map<Check,Object> listCustomersBySelectedCompany(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(user.getId() != null ){
                int id = Integer.parseInt(stId);
                hm.put(Check.status,true);
                hm.put(Check.message,"Seçilen firmaya ait müşteri sıralama işlemi başarılı!");
                hm.put(Check.result,customerRepository.findByStatusAndUserIdAndCompany_Id(true, user.getId(),id));
            }else{
                String error = "Lütfen hesabınıza giriş yapıp tekrar deneyiniz!";
                hm.put(Check.status,false);
                hm.put(Check.message,error);
                Util.logger(error, Invoice.class);
            }
        } catch (NumberFormatException e) {
            String error = "Seçilen firmaya ait müşteri sıralama işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Invoice.class);
        }
        return hm;
    }

    //Generate Invoice Code
    public Map<Check,Object> generateInvoiceCode(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        String startDate = "";
        String endDate = "";
        String code = "";
        try {
            if(user.getId() != null){
                String[] date = Util.generateDate().split("-");
                startDate = date[0] + "-" + date[1] + "-" + "01";
                endDate = Util.generateDate();
                System.out.println("Start Data: " + startDate);
                System.out.println("End Date: " + endDate);
                long workNumber = invoiceRepository.countByStatusEqualsAndUserIdEqualsAndDateBetween(true,user.getId(),startDate,endDate);
                System.out.println("Work number: " + workNumber);
                if(String.valueOf(workNumber).length() == 1){
                    code = "SN36" + date[0] + date[1] + "0" + (workNumber+1); //İş sayısı 10'dan küçükse başına 0 ekleyerek yazar.
                }else {
                    code = "SN36" + date[0] + date[1] + (workNumber+1); //İş sayısı 10'dan büyükse direk yazar.
                }
                hm.put(Check.status,true);
                hm.put(Check.message,"Fatura kodu başarılı bir şekilde oluşturuldu!");
                hm.put(Check.result,code);
            }else{
                String error = "Lütfen hesabınıza giriş yapıp tekrar deneyiniz!";
                hm.put(Check.status,false);
                hm.put(Check.message,error);
                Util.logger(error, Invoice.class);
            }
        } catch (Exception e) {
            String error = "Fatura kodu oluşturulurken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Invoice.class);
        }
        return hm;
    }

}
