package YMY.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("fatura")
public class InvoiceController {

    @GetMapping("")
    public String invoice(){
        return "invoice";
    }

}
