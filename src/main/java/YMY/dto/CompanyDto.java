package YMY.dto;

import YMY.entities.Company;
import YMY.entities.User;
import YMY.repositories.CompanyRepository;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CompanyDto {

    final CompanyRepository companyRepository;
    final UserService userService;
    public CompanyDto(CompanyRepository companyRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.userService = userService;
    }

    public Map<Check,Object> companySaveOrUpdate(Company company, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    company.setUserId(user.getId());
                    company.setPhone(company.getPhone().trim());
                    System.out.println(company);
                    companyRepository.saveAndFlush(company);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Firma kayıt işlemi başarıyla tamamlandı!");
                    hm.put(Check.result,company);
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Firma kayıt işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            hm.put(Check.status,false);
            if (e.toString().contains("constraint")){
                hm.put(Check.message,"Bu telefon numarasına kayıtlı firma zaten mevcut!");
            }else {
                hm.put(Check.message,"İşlem sırasında bir hata oluştu!");
            }

        }
        return hm;
    }

}
