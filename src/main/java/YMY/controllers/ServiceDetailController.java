package YMY.controllers;

import YMY.entities.Services;
import YMY.repositories.ServicesRepositories;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/hizmetlerimiz")
public class ServiceDetailController {

    final ServicesRepositories servicesRepositories;
    public ServiceDetailController(ServicesRepositories servicesRepositories) {
        this.servicesRepositories = servicesRepositories;
    }

    @GetMapping("/{stId}")
    public String serviceDetails(@PathVariable String stId){
        return "serviceDetails";
    }

    @ResponseBody
    @PostMapping("/save")
    Map<Check,Object> save(@RequestBody Services services){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            servicesRepositories.saveAndFlush(services);
            hm.put(Check.status,true);
            hm.put(Check.message,"Servis hizmeti başarılı bir şekilde kaydedildi!");
        }catch (Exception e){
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,ServiceDetailController.class);
        }
        return hm;
    }

    @ResponseBody
    @GetMapping("/list")
    Map<Check,Object> list(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        try {
            hm.put(Check.status,true);
            hm.put(Check.message,"Servis hizmetleri başarılı bir şekilde getirildi!");
            hm.put(Check.result,servicesRepositories.findByStatusEquals(true));
        }catch (Exception e){
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,ServiceDetailController.class);
        }
        return hm;
    }

}
