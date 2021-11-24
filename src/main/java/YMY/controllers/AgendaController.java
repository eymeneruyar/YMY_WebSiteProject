package YMY.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notlarım")
public class AgendaController {

    @GetMapping("")
    public String agenda(){
        return "agenda";
    }

}
