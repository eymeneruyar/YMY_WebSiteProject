package YMY.restControllers;

import YMY.entities.User;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserActivityRestController {

    final UserService userService;
    public UserActivityRestController(UserService userService) {
        this.userService = userService;
    }

    //User Information
    @GetMapping("/userActivity")
    public Map<Check,Object> userActivity(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        Map<Object,Object> info = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            info.put("email",user.getEmail());
            info.put("name",user.getName());
            info.put("surname",user.getSurname());
            info.put("profileImage",user.getProfileImage());
            info.put("roles",user.getRoles().get(0).getName());
            hm.put(Check.status,true);
            hm.put(Check.message,"Giriş yapan kullanıcı bilgileri başarılı bir şekilde getirildi!");
            hm.put(Check.result,info);
        } catch (Exception e) {
            String error = "Giriş yapan kullanıcı bilgileri getirilirken bir sorun oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, User.class);
        }
        return hm;
    }

}
