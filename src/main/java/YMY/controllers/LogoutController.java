package YMY.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cikis_yap")
public class LogoutController {

    @GetMapping("")
    public String logout(){
        return "redirect:/giris_yap";
    }

}
