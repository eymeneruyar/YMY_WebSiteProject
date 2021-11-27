package YMY.dto;

import YMY.entities.Company;
import YMY.entities.Customer;
import YMY.entities.User;
import YMY.repositories.CompanyRepository;
import YMY.repositories.CustomerRepository;
import YMY.services.CacheService;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CustomerDto {

    final CompanyRepository companyRepository;
    final CustomerRepository customerRepository;
    final UserService userService;
    final CacheService cacheService;
    public CustomerDto(CompanyRepository companyRepository, CustomerRepository customerRepository, UserService userService, CacheService cacheService) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.cacheService = cacheService;
    }

    //Save or Update customer information
    public Map<Check,Object> customerSaveOrUpdate(Customer customer, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    customer.setUserId(user.getId());
                    customer.setStatus(true);
                    customer.setDate(Util.generateDate());
                    customerRepository.saveAndFlush(customer);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Müşteri kayıt işlemi başarıyla tamamlandı!");
                    hm.put(Check.result,customer);
                    cacheService.cacheRefresh("customerList"); //Refresh cache
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Müşteri kayıt işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Customer.class);
        }
        return hm;
    }

    //List of customers by status
    public Map<Check,Object> listCustomer(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(user.getId() != null ){
                hm.put(Check.status,true);
                hm.put(Check.message,"Müşteri listeleme işlemi başarılı!");
                hm.put(Check.result,customerRepository.findByStatusAndUserIdOrderByIdAsc(true, user.getId()));
            }else{
                String error = "Lütfen hesabınıza giriş yapıp tekrar deneyiniz!";
                hm.put(Check.status,false);
                hm.put(Check.message,error);
                Util.logger(error, Customer.class);
            }
        } catch (Exception e) {
            String error = "Listeleme işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Customer.class);
        }
        return hm;
    }

    //Listing all companies
    public Map<Check,Object> listAllCompany(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            hm.put(Check.status,true);
            hm.put(Check.message,"Firmalar başarılı bir şekilde listelendi!");
            hm.put(Check.result,companyRepository.findAll());
        } catch (Exception e) {
            String error = "Firmalar listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Customer.class);
        }
        return hm;
    }

}
