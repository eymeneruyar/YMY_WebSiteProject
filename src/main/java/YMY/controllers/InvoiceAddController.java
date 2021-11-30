package YMY.controllers;

import YMY.dto.InvoiceAddDto;
import YMY.entities.Invoice;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @PostMapping("/saveOrUpdate")
    public Map<Check,Object> invoiceSaveOrUpdate(@RequestBody @Valid Invoice invoice, BindingResult bindingResult){
        return invoiceAddDto.invoiceSaveOrUpdate(invoice,bindingResult);
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

    @ResponseBody
    @GetMapping("/generateInvoiceCode")
    public Map<Check,Object> generateInvoiceCode(){
        return invoiceAddDto.generateInvoiceCode();
    }

}
