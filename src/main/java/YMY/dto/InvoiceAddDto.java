package YMY.dto;

import YMY.entities.Invoice;
import YMY.entities.User;
import YMY.repositories.CompanyRepository;
import YMY.repositories.CustomerRepository;
import YMY.repositories.InvoiceRepository;
import YMY.repositories.WorksRepository;
import YMY.services.CacheService;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

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

}
