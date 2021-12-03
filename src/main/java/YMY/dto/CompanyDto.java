package YMY.dto;

import YMY.entities.Cities;
import YMY.entities.Company;
import YMY.entities.Towns;
import YMY.entities.User;
import YMY.repositories.CitiesRepository;
import YMY.repositories.CompanyRepository;
import YMY.repositories.TownsRepository;
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
public class CompanyDto {

    final CompanyRepository companyRepository;
    final UserService userService;
    final CacheService cacheService;
    final CitiesRepository citiesRepository;
    final TownsRepository townsRepository;
    public CompanyDto(CompanyRepository companyRepository, UserService userService, CacheService cacheService, CitiesRepository citiesRepository, TownsRepository townsRepository) {
        this.companyRepository = companyRepository;
        this.userService = userService;
        this.cacheService = cacheService;
        this.citiesRepository = citiesRepository;
        this.townsRepository = townsRepository;
    }

    //Save or update company information
    public Map<Check,Object> companySaveOrUpdate(Company company, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    company.setUserId(user.getId());
                    company.setStatus(true);
                    company.setDate(Util.generateDate());
                    //System.out.println(company);
                    companyRepository.saveAndFlush(company);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Firma kayıt işlemi başarıyla tamamlandı!");
                    hm.put(Check.result,company);
                    cacheService.cacheRefresh("companyList"); //Refresh cache
                    cacheService.cacheRefresh("invoiceAddListCompanyByUserId"); //Refresh cache
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Firma kayıt işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            if (e.toString().contains("constraint")){
                hm.put(Check.message,"Bu telefon numarasına kayıtlı firma zaten mevcut!");
            }else {
                hm.put(Check.message,error);
                Util.logger(error + " " + e,Company.class);
            }

        }
        return hm;
    }

    //List the company with status and user id in system
    public Map<Check,Object> listCompany(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(user.getId() != null ){
                hm.put(Check.status,true);
                hm.put(Check.message,"Firma listeleme işlemi başarılı!");
                hm.put(Check.result,companyRepository.findByStatusEqualsAndUserIdEqualsOrderByIdAsc(true,user.getId()));
                //cacheService.cacheRefresh("companyList");
            }else{
                String error = "Lütfen hesabınıza giriş yapıp tekrar deneyiniz!";
                hm.put(Check.status,false);
                hm.put(Check.message,error);
                Util.logger(error,Company.class);
            }
        } catch (Exception e) {
            String error = "Listeleme işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Company.class);
            System.err.println(e);
        }
        return hm;
    }

    //Delete selected company
    public Map<Check,Object> deleteCompany(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if(optionalCompany.isPresent()){
                Company company = optionalCompany.get();
                company.setStatus(false);
                companyRepository.saveAndFlush(company);
                hm.put(Check.status,true);
                hm.put(Check.message,"Silme işlemi başarılı!");
                cacheService.cacheRefresh("companyList"); //Refresh cache
            }else {
                hm.put(Check.status,false);
                hm.put(Check.message,"Seçilen firmaya ait herhangi bir bilgi bulunmamaktadır!");
                Util.logger("Seçilen firmaya ait herhangi bir bilgi bulunmamaktadır!",Company.class);
            }
        } catch (Exception e) {
            String error = "Silme işlemi sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Company.class);
        }
        return hm;
    }

    //List of cities
    public Map<Check,Object> listCities(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            hm.put(Check.status,true);
            hm.put(Check.message,"İl sıralama işlemi başarılı!");
            hm.put(Check.result,citiesRepository.findAll());
        } catch (Exception e) {
            String error = "İller sıralanırken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Cities.class);
            System.err.println(e);
        }
        return hm;
    }

    //Listing towns by selected city
    public Map<Check,Object> listTownsBySelectedCity(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            hm.put(Check.status,true);
            hm.put(Check.message,"İlçe sıralama işlemi başarılı!");
            hm.put(Check.result,townsRepository.findByTownCityKeyOrderByIdAsc(id));
        } catch (Exception e) {
            String error = "İlçeler sıralanırken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Towns.class);
            System.err.println(e);
        }
        return hm;
    }

    //Choose the city by selected city key no
    public Map<Check,Object> getInfoCityByCityKey(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Cities> optionalCities = citiesRepository.findByCityKey(id);
            if(optionalCities.isPresent()){
                hm.put(Check.status,true);
                hm.put(Check.message,"Şehir bilgisi başarıyla getirildi!");
                hm.put(Check.result,optionalCities.get());
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Verilen id değerine karşılık bir şehir bulunmamaktadır!");
            }
        } catch (Exception e) {
            String error = "Şehir bilgisi getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Cities.class);
            System.err.println(e);
        }
        return hm;
    }

    public Map<Check,Object> detailCompany(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if(optionalCompany.isPresent()){
                hm.put(Check.status,true);
                hm.put(Check.message,"Firma bilgisi başarıyla getirildi!");
                hm.put(Check.result,optionalCompany.get());
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Verilen id değerine karşılık bir firma bulunmamaktadır!");
            }
        } catch (Exception e) {
            String error = "Firma bilgisi getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Company.class);
            System.err.println(e);
        }
        return hm;
    }

}
