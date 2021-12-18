package YMY.dto;

import YMY.entities.User;
import YMY.repositories.CompanyRepository;
import YMY.repositories.InvoiceRepository;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CompanyDetailDto {

    final CompanyRepository companyRepository;
    final InvoiceRepository invoiceRepository;
    final UserService userService;
    public CompanyDetailDto(CompanyRepository companyRepository, InvoiceRepository invoiceRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
    }

    //All invoice list DESC by selected company
    public Map<Check,Object> listAllInvoiceBySelectedCompany(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try{
            if(user.getId() != null){
                int id = Integer.parseInt(stId);
                hm.put(Check.status,true);
                hm.put(Check.message,"Seçilen firmaya ait fatura bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndCompany_IdEqualsOrderByIdDesc(true, user.getId(), id));
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Seçilen firmaya ait fatura bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,CompanyDetailDto.class);
        }
        return hm;
    }

    //List of invoice by filtered date, debt status and billing status
    public Map<Check,Object> listInvoiceByFilter(String stId,String date,String debtStatus,String billingStatus){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        String[] dateArr = date.replaceAll(" to ",",").split(",");
        String startDate = dateArr[0];
        String endDate = dateArr[1];
        try{
            if(user.getId() != null){
                int id = Integer.parseInt(stId);
                int billingStatusId = Integer.parseInt(billingStatus);
                int debtStatusId = Integer.parseInt(debtStatus);
                if(debtStatusId == 2 && billingStatusId == 2){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true, user.getId(), id,startDate,endDate));
                }else if(debtStatusId == 2 && billingStatusId == 1){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndBillingStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true, user.getId(), billingStatus,id,startDate,endDate));
                }else if(debtStatusId == 2 && billingStatusId == 0){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndBillingStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true, user.getId(), billingStatus,id,startDate,endDate));
                }else if(debtStatusId == 1 && billingStatusId == 2){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true,user.getId(),false,id,startDate,endDate));
                }else if(debtStatusId == 1 && billingStatusId == 1){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndBillingStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true, user.getId(),false,billingStatus,id,startDate,endDate));
                }else if(debtStatusId == 1 && billingStatusId == 0){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndBillingStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true, user.getId(),false,billingStatus,id,startDate,endDate));
                }else if(debtStatusId == 0 && billingStatusId == 2){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true,user.getId(),true,id,startDate,endDate));
                }else if(debtStatusId == 0 && billingStatusId == 1){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndBillingStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true, user.getId(),true,billingStatus,id,startDate,endDate));
                }else if(debtStatusId == 0 && billingStatusId == 0){
                    hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndBillingStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(true, user.getId(),true,billingStatus,id,startDate,endDate));
                }else{
                    hm.put(Check.result,null);
                }
                hm.put(Check.status,true);
                hm.put(Check.message,"Seçilen firmaya ait fatura bilgileri başarılı bir şekilde getirildi!");
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Seçilen firmaya ait fatura bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,CompanyDetailDto.class);
        }
        return hm;
    }

}
