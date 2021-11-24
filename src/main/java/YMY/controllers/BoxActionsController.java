package YMY.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("kasa_haraketleri")
public class BoxActionsController {

    @GetMapping("")
    public String boxActions(){
        return "boxActions";
    }

}
