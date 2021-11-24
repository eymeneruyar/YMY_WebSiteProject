package YMY.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("istatistikler")
public class StatisticsController {

    @GetMapping("")
    public String statistics(){
        return "statistics";
    }

}
