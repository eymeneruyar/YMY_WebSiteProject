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
                        int quantity = Integer.parseInt(it.getQuantity());
                        float unitPrice = Float.parseFloat(it.getUnitPrice());
                        float total = (float) quantity * unitPrice;
                        result.add(total);
                        it.setStatus(true);
                        it.setDate(Util.generateDate());
                        it.setUserId(user.getId());
                        it.setTotal(String.valueOf(total));
                    });
                    //Works save process
                    debt = (float) result.stream().mapToDouble(Float::floatValue).sum();
                    discount = Float.parseFloat(invoice.getDiscount());
                    //Düzenlenmesi gerek
                    if(invoice.getVat() == "18" && discount > 0){
                        debt = debt - (debt*(discount/100));
                        debt = (float) (debt + (debt * 0.18));
                        System.out.println("KDV ve iskonto var: " + debt);
                    }
                    else if(invoice.getVat() == "18" && discount <= 0){
                        debt = (float) (debt + (debt * 0.18));
                        System.out.println("KDV var ama iskonto yok: " + debt);
                    }
                    else if(invoice.getVat() == "0" && discount > 0){
                        debt = debt - (debt*(discount/100));
                        System.out.println("KDV yok ama iskonto var: " + debt);
                    }
                    //Düzenlenmesi gerek
                    invoice.setUserId(user.getId());
                    invoice.setStatus(true);
                    invoice.setPaidStatus(false);
                    invoice.setDate(Util.generateDate());
                    invoice.setDebt(String.valueOf(debt));
                    invoice.setPaid("0");
                    invoice.setRemainingDebt("0");
                    invoiceRepository.saveAndFlush(invoice);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Fatura kayıt işlemi başarıyla tamamlandı!");
                    hm.put(Check.result,invoice);
                    //cacheService.cacheRefresh("customerList"); //Refresh cache
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
                startDate = "01" + "-" + date[1] + "-" + date[2];
                endDate = Util.generateDate();
                System.out.println("Start Data: " + startDate);
                System.out.println("End Date: " + endDate);
                long workNumber = invoiceRepository.countByDateBetweenAndStatusEqualsAndUserIdEqualsAndDateContains(startDate,endDate,true,user.getId(),date[1]);
                System.out.println("Work number: " + workNumber);
                if(String.valueOf(workNumber).length() == 1){
                    code = "SN36" + date[2] + date[1] + "0" + (workNumber+1); //İş sayısı 10'dan küçükse başına 0 ekleyerek yazar.
                }else {
                    code = "SN36" + date[2] + date[1] + (workNumber+1); //İş sayısı 10'dan büyükse direk yazar.
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
