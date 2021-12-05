package YMY.controllers;

import YMY.dto.InvoicePrintDto;
import YMY.entities.Invoice;
import YMY.repositories.InvoiceRepository;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/fatura_yazdir")
public class InvoicePrintController {

    final InvoiceRepository invoiceRepository;
    final InvoicePrintDto invoicePrintDto;
    public InvoicePrintController(InvoiceRepository invoiceRepository, InvoicePrintDto invoicePrintDto) {
        this.invoiceRepository = invoiceRepository;
        this.invoicePrintDto = invoicePrintDto;
    }

    @GetMapping("/{stId}")
    public String invoicePrint(@PathVariable String stId, Model model){
        try {
            Integer id = Integer.parseInt(stId);
            List<Float> result = new ArrayList<>();
            Float subTotal = 0f;
            Float discount = 0f;
            Float kdv = 0f;
            Float total = 0f;
            String[] dateArr;
            String date;
            Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
            if(optionalInvoice.isPresent()){
                Invoice invoice = optionalInvoice.get();
                invoice.getWorkses().forEach(item -> {
                    result.add(item.getTotal());
                });
                total = (float) result.stream().mapToDouble(Float::floatValue).sum();
                discount = total * (invoice.getDiscount()/100f);
                kdv = (total-discount) * (invoice.getVat()/100f);
                subTotal = total - discount;
                dateArr = invoice.getDate().split("-");
                date = dateArr[2] + "-" + dateArr[1] + "-" + dateArr[0];
                model.addAttribute("invoice",invoice);
                model.addAttribute("total",total);
                model.addAttribute("subTotal",subTotal);
                model.addAttribute("discount",discount);
                model.addAttribute("kdv",kdv);
                model.addAttribute("date",date);
            }else {
                return "redirect:/yonetim";
            }
        } catch (Exception e) {
            String error = "Fatura yazdırma işlemi yapılırken bir hata oluştu!";
            Util.logger(error + " " + e, Invoice.class);
            return "redirect:/yonetim";
        }
        return "invoicePrint";
    }

    /*@GetMapping("/exportToExcel/{stId}")
    public void exportToExcel(@PathVariable String stId, HttpServletResponse response){
        invoicePrintDto.exportToExcel(stId,response);
    }*/

    @ResponseBody
    @GetMapping("/exportToExcel/{stId}")
    public Map<Check,Object> exportToExcel(@PathVariable String stId,HttpServletResponse response){
        return invoicePrintDto.exportToExcel(stId,response);
    }

}
