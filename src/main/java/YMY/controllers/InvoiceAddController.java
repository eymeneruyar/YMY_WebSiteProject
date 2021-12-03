package YMY.controllers;

import YMY.dto.InvoiceAddDto;
import YMY.entities.Invoice;
import YMY.utils.Check;
import org.springframework.cache.annotation.Cacheable;
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
    @GetMapping("/listInvoiceThisMonth")
    @Cacheable("invoiceAddListInvoiceThisMonth")
    public Map<Check,Object> listInvoiceThisMonth(){
        return invoiceAddDto.listInvoiceThisMonth();
    }

    @ResponseBody
    @GetMapping("/listFilteredInvoice/{date}/{companyId}/{billingStatus}")
    @Cacheable("invoiceAddlistFilteredInvoice")
    public Map<Check,Object> listFilteredInvoice(@PathVariable String date,@PathVariable String companyId,@PathVariable String billingStatus){
        return invoiceAddDto.listFilteredInvoice(date,companyId,billingStatus);
    }

    @ResponseBody
    @DeleteMapping("/deleteInvoice/{stId}")
    public Map<Check,Object> deleteInvoice(@PathVariable String stId){
        return invoiceAddDto.deleteInvoice(stId);
    }

    @ResponseBody
    @GetMapping("/listCompanyByUserId")
    @Cacheable("invoiceAddListCompanyByUserId")
    public Map<Check,Object> listCompanyByUserId(){
        return invoiceAddDto.listCompanyByUserId();
    }

    @ResponseBody
    @GetMapping("/listCustomersBySelectedCompany/{stId}")
    @Cacheable("invoiceAddListCustomersBySelectedCompany")
    public Map<Check,Object> listCustomersBySelectedCompany(@PathVariable String stId){
        return invoiceAddDto.listCustomersBySelectedCompany(stId);
    }

    @ResponseBody
    @GetMapping("/generateInvoiceCode")
    public Map<Check,Object> generateInvoiceCode(){
        return invoiceAddDto.generateInvoiceCode();
    }

}
