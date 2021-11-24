package YMY.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("fatura_yazdir")
public class InvoicePrintController {

    @GetMapping("")
    public String invoicePrint(){
        return "invoicePrint";
    }

}
