package YMY.dto;

import YMY.entities.Company;
import YMY.entities.Customer;
import YMY.entities.DispatchNote;
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
import java.util.Optional;

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

    //List of customer by selected company
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
                Util.logger(error, Customer.class);
            }
        } catch (NumberFormatException e) {
            String error = "Seçilen firmaya ait müşteri sıralama işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Customer.class);
        }
        return hm;
    }

    //Delete Customer
    public Map<Check,Object> deleteCustomer(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if(optionalCustomer.isPresent()){
                Customer customer = optionalCustomer.get();
                customer.setStatus(false);
                customerRepository.saveAndFlush(customer);
                hm.put(Check.status,true);
                hm.put(Check.message,"Müşteri silme işlemi başarıyla gerçekleştirildi!");
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Silinmek istenen müşteri bulunamadı!");
            }
        } catch (NumberFormatException e) {
            String error = "Silme işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Customer.class);
        }
        return hm;
    }

    //Details by selected customer
    public Map<Check,Object> detailCustomer(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            hm.put(Check.status,true);
            hm.put(Check.message,"Müşteri detayları başarılı bir şekilde getirildi!");
            hm.put(Check.result,customerRepository.findById(id).get());
        } catch (Exception e) {
            String error = "Müşteri detayları getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Customer.class);
        }
        return hm;
    }

    //Listing all companies
    public Map<Check,Object> listAllCompany(){
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
                Util.logger(error, Customer.class);
            }
        } catch (Exception e) {
            String error = "Firmalar listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Customer.class);
        }
        return hm;
    }

}
