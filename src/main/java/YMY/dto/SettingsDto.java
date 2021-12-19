package YMY.dto;

import YMY.entities.MonthlyGoal;
import YMY.entities.User;
import YMY.entities.YearlyGoal;
import YMY.properties.ChangePassword;
import YMY.properties.UserInfo;
import YMY.repositories.MonthlyGoalRepository;
import YMY.repositories.UserRepository;
import YMY.repositories.YearlyGoalRepository;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SettingsDto {

    final UserService userService;
    final UserRepository userRepository;
    final MonthlyGoalRepository monthlyGoalRepository;
    final YearlyGoalRepository yearlyGoalRepository;
    public SettingsDto(UserService userService, UserRepository userRepository, MonthlyGoalRepository monthlyGoalRepository, YearlyGoalRepository yearlyGoalRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.monthlyGoalRepository = monthlyGoalRepository;
        this.yearlyGoalRepository = yearlyGoalRepository;
    }

    //Update User Information
    public Map<Check, Object> update(UserInfo userInfo, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    user.setName(userInfo.getName());
                    user.setSurname(userInfo.getSurname());
                    user.setEmail(userInfo.getEmail());
                    user.setCompanyName(userInfo.getCompanyName());
                    userRepository.saveAndFlush(user);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Kullanıcı bilgi güncelleme işlemi başarıyla tamamlandı!");
                    hm.put(Check.result,user);
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Kullanıcı bilgi güncelleme işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,User.class);
        }
        return hm;
    }

    //Change Password
    public Map<Check, Object> changePassword(ChangePassword changePassword,BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    if ( changePassword.getOldPassword().equals("") || changePassword.getNewPassword().equals("") || changePassword.getReNewPassword().equals("")){
                        hm.put(Check.status,false);
                        hm.put(Check.message,"Lütfen gerekli alanları doldurunuz!");
                    }else {
                        if(changePassword.getNewPassword().equals(changePassword.getReNewPassword())){
                            user.setPassword(userService.encoder().encode(changePassword.getNewPassword()));
                            userRepository.saveAndFlush(user);
                            hm.put(Check.status,true);
                            hm.put(Check.message,"Kullanıcı şifre güncelleme işlemi başarıyla tamamlandı!");
                            hm.put(Check.result,user);
                        }else {
                            hm.put(Check.status,false);
                            hm.put(Check.message,"Yeni şifre ve tekrarı aynı değil. Lütfen tekrar kontrol ediniz!");
                        }
                    }
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Kullanıcı şifre güncelleme işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,User.class);
        }
        return hm;
    }

    //Upload image
    public Map<Check, Object> uploadImage(MultipartFile file) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(auth.getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            File folderProfile = new File(Util.UPLOAD_DIR_PROFILE_IMAGES + user.getId());
            if(folderProfile.exists()){
                File fileOld = new File(Util.UPLOAD_DIR_PROFILE_IMAGES +user.getId() + "/" + user.getProfileImage());
                if(fileOld.delete()){
                    resultMap = Util.imageUpload(file, Util.UPLOAD_DIR_PROFILE_IMAGES + user.getId() + "/");
                }else{
                    resultMap.put(Check.status, false);
                    resultMap.put(Check.message, "Old image couldn't be deleted!");
                }
            }else{
                boolean status = folderProfile.mkdir();
                resultMap = Util.imageUpload(file, Util.UPLOAD_DIR_PROFILE_IMAGES + user.getId() + "/");
            }
            if((Boolean) resultMap.get(Check.status)){
                try{
                    user.setProfileImage(resultMap.get(Check.result).toString());
                    userRepository.saveAndFlush(user);
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.message, "Profile Image successfully saved!");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return resultMap;
    }

    //Show image
    public Map<Check, Object> getImage() throws IOException {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(auth.getName());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            String path = "";
            String profile_image = user.getProfileImage();
            if(!profile_image.equals("")){
                path = Util.UPLOAD_DIR_PROFILE_IMAGES + user.getId() + "/" + profile_image;
            }else{
                path = Util.UPLOAD_DIR_PROFILE_IMAGES + "default_profile_image.jpg";
            }
            byte[] fileContent = FileUtils.readFileToByteArray(new File(path));
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            if(encodedString != null){
                resultMap.put(Check.status, true);
                resultMap.put(Check.result, encodedString);
            }else{
                resultMap.put(Check.status, false);
            }

        }
        return resultMap;
    }

    //Monthly Goal Save or Update
    public Map<Check, Object> saveOrUpdateMonthlyGoal(MonthlyGoal monthlyGoal, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    Optional<MonthlyGoal> optionalMonthlyGoal = monthlyGoalRepository.findByStatusEqualsAndUserIdEqualsAndKeyEquals(true,user.getId(),monthlyGoal.getKey());
                    if(optionalMonthlyGoal.isPresent()){
                        MonthlyGoal mg = optionalMonthlyGoal.get();
                        mg.setGoal(monthlyGoal.getGoal());
                        monthlyGoalRepository.saveAndFlush(mg);
                        hm.put(Check.status,true);
                        hm.put(Check.message,"Aylık hedef belirleme işlemi başarıyla tamamlandı!");
                        hm.put(Check.result,mg);
                    }else{
                        monthlyGoal.setStatus(true);
                        monthlyGoal.setDate(Util.generateDate());
                        monthlyGoal.setUserId(user.getId());
                        monthlyGoalRepository.save(monthlyGoal);
                        hm.put(Check.status,true);
                        hm.put(Check.message,"Aylık hedef belirleme işlemi başarıyla tamamlandı!");
                        hm.put(Check.result,monthlyGoal);
                    }
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Aylık hedef belirleme işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,MonthlyGoal.class);
        }
        return hm;
    }

    //Yearly Goal Save or Update
    public Map<Check, Object> saveOrUpdateYearlyGoal(YearlyGoal yearlyGoal, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    Optional<YearlyGoal> optionalYearlyGoal = yearlyGoalRepository.findByStatusEqualsAndUserIdEqualsAndKeyEquals(true,user.getId(),yearlyGoal.getKey());
                    if(optionalYearlyGoal.isPresent()){
                        YearlyGoal yg = optionalYearlyGoal.get();
                        yg.setGoal(yearlyGoal.getGoal());
                        yearlyGoalRepository.saveAndFlush(yg);
                        hm.put(Check.status,true);
                        hm.put(Check.message,"Yıllık hedef belirleme işlemi başarıyla tamamlandı!");
                        hm.put(Check.result,yg);
                    }else{
                        yearlyGoal.setStatus(true);
                        yearlyGoal.setDate(Util.generateDate());
                        yearlyGoal.setUserId(user.getId());
                        yearlyGoalRepository.save(yearlyGoal);
                        hm.put(Check.status,true);
                        hm.put(Check.message,"Yıllık hedef belirleme işlemi başarıyla tamamlandı!");
                        hm.put(Check.result,yearlyGoal);
                    }
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Yıllık hedef belirleme işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,MonthlyGoal.class);
        }
        return hm;
    }

}
