package YMY.dto;

import YMY.entities.Customer;
import YMY.entities.Invoice;
import YMY.entities.User;
import YMY.entities.Works;
import YMY.repositories.InvoiceRepository;
import YMY.repositories.WorksRepository;
import YMY.services.CacheService;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
public class InvoiceEditDto {

    final InvoiceRepository invoiceRepository;
    final WorksRepository worksRepository;
    final UserService userService;
    final CacheService cacheService;
    public InvoiceEditDto(InvoiceRepository invoiceRepository, WorksRepository worksRepository, UserService userService, CacheService cacheService) {
        this.invoiceRepository = invoiceRepository;
        this.worksRepository = worksRepository;
        this.userService = userService;
        this.cacheService = cacheService;
    }

    //Update work
    public Map<Check,Object> updateWork(Invoice invoice, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        Invoice mainInvoice = new Invoice();
        Works works = new Works();
        Works mainWorks = new Works();
        List<Works> worksList = new ArrayList<>();
        List<Float> result = new ArrayList<>();
        float debt = 0;
        float discount = 0;
        float kdv = 0;
        float remainigDebt = 0;
        try{
            if(!bindingResult.hasErrors()){
                if(invoice.getId() != null){
                    mainInvoice = invoiceRepository.findById(invoice.getId()).get(); //DB'de olan değerler
                    mainWorks = worksRepository.findById(invoice.getWorkses().get(0).getId()).get(); //DB'de olan asıl değer
                    works = invoice.getWorkses().get(0); //Güncellenmek istenen değerler
                    mainInvoice.setBillingStatus(invoice.getBillingStatus());
                    mainInvoice.setDiscount(invoice.getDiscount());
                    mainInvoice.setVat(invoice.getVat());
                    mainWorks.setWork(works.getWork());
                    mainWorks.setQuantity(works.getQuantity());
                    mainWorks.setUnitPrice(works.getUnitPrice());
                    mainWorks.setTotal(works.getQuantity() * works.getUnitPrice());
                    //Ödeme Kısımlarının güncellenmesi - Start
                    mainInvoice.getWorkses().forEach(it ->{
                        Integer quantity = it.getQuantity();
                        Float unitPrice = it.getUnitPrice();
                        Float total = quantity * unitPrice;
                        result.add(total);
                    });
                    debt = (float) result.stream().mapToDouble(Float::floatValue).sum();
                    remainigDebt = debt - mainInvoice.getPaid();
                    discount = mainInvoice.getDiscount();
                    kdv = mainInvoice.getVat();
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
                    //Ödeme Kısımlarının güncellenmesi - End
                    mainInvoice.setDebt(debt);
                    mainInvoice.setRemainingDebt(remainigDebt);
                    worksRepository.saveAndFlush(mainWorks);
                    invoiceRepository.saveAndFlush(mainInvoice);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Güncelleme işlemi başarılı!");
                    hm.put(Check.result,mainInvoice);
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"İş emri güncelleme işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        }catch(Exception e){
            String error = "Güncelleme işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Works.class);
        }
        return hm;
    }

    //List works by selected invoice id
    public Map<Check,Object> listWorks(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(user.getId() != null ){
                int id = Integer.parseInt(stId);
                Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
                if(optionalInvoice.isPresent()){
                    hm.put(Check.status,true);
                    hm.put(Check.message,"İş emri koduna ait yapılan işler başarıyla sıralandı!");
                    hm.put(Check.result,optionalInvoice.get());
                }
            }else{
                String error = "Lütfen hesabınıza giriş yapıp tekrar deneyiniz!";
                hm.put(Check.status,false);
                hm.put(Check.message,error);
                Util.logger(error, Invoice.class);
            }
        } catch (NumberFormatException e) {
            String error = "İş emri koduna ait yapılan işler getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Invoice.class);
        }
        return hm;
    }

    //Delete work
    public Map<Check,Object> deleteWork(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Works> optionalWorks = worksRepository.findById(id);
            if(optionalWorks.isPresent()){
                hm.put(Check.status,true);
                hm.put(Check.message,"Silme işlemi başarılı!");
                Works works = optionalWorks.get();
                works.setStatus(false);
                worksRepository.saveAndFlush(works);
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Silinecek iş bulunamadı!");
            }
        }catch (Exception e){
            String error = "Silme işlemi sırasında bir hata oluştu";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Works.class);
        }
        return hm;
    }

}
