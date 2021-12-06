package YMY.controllers;

import YMY.dto.BoxActionsDto;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("kasa_haraketleri")
public class BoxActionsController {

    final BoxActionsDto boxActionsDto;
    public BoxActionsController(BoxActionsDto boxActionsDto) {
        this.boxActionsDto = boxActionsDto;
    }

    @GetMapping("")
    public String boxActions(){
        return "boxActions";
    }

    @ResponseBody
    @GetMapping("/listCompany")
    public Map<Check,Object> listCompany(){
        return boxActionsDto.listCompany();
    }

    @ResponseBody
    @GetMapping("/listOfCustomerBySelectedCompany/{stId}")
    public Map<Check,Object> listOfCustomerBySelectedCompany(@PathVariable String stId){
        return boxActionsDto.listOfCustomerBySelectedCompany(stId);
    }

    @ResponseBody
    @GetMapping("/listOfInvoiceCodeBySelectedCustomer/{stId}")
    public Map<Check,Object> listOfInvoiceCodeBySelectedCustomer(@PathVariable String stId){
        return boxActionsDto.listOfInvoiceCodeBySelectedCustomer(stId);
    }

}
