package YMY.controllers;

import YMY.dto.InvoiceAddDto;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/fatura_ekle")
public class InvoiceAddController {

    final InvoiceAddDto invoiceAddDto;
    public InvoiceAddController(InvoiceAddDto invoiceAddDto) {
        this.invoiceAddDto = invoiceAddDto;
    }

    @GetMapping("")
    public String invoiceAdd(){
        return "invoiceAdd";
    }

    @ResponseBody
    @GetMapping("/listCompanyByUserId")
    public Map<Check,Object> listCompanyByUserId(){
        return invoiceAddDto.listCompanyByUserId();
    }

    @ResponseBody
    @GetMapping("/listCustomersBySelectedCompany/{stId}")
    public Map<Check,Object> listCustomersBySelectedCompany(@PathVariable String stId){
        return invoiceAddDto.listCustomersBySelectedCompany(stId);
    }

}
