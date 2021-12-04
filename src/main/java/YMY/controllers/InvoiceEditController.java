package YMY.controllers;

import YMY.dto.InvoiceEditDto;
import YMY.entities.Invoice;
import YMY.repositories.InvoiceRepository;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("fatura_duzenle")
public class InvoiceEditController {

    final InvoiceRepository invoiceRepository;
    final InvoiceEditDto invoiceEditDto;
    public InvoiceEditController(InvoiceRepository invoiceRepository, InvoiceEditDto invoiceEditDto) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceEditDto = invoiceEditDto;
    }

    @GetMapping("/{stId}")
    public String invoiceEdit(@PathVariable String stId, Model model){
        try {
            Integer id = Integer.parseInt(stId);
            Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
            if(!optionalInvoice.isPresent()){
                return "redirect:/yonetim";
            }
        } catch (Exception e) {
            String error = "Fatura yazdırma işlemi yapılırken bir hata oluştu!";
            Util.logger(error + " " + e, Invoice.class);
            return "redirect:/yonetim";
        }
        return "invoiceEdit";
    }

    @ResponseBody
    @PutMapping("/update")
    public Map<Check,Object> updateWork(@RequestBody @Valid Invoice invoice, BindingResult bindingResult){
        return invoiceEditDto.updateWork(invoice,bindingResult);
    }

    @ResponseBody
    @GetMapping("/listWorks/{stId}")
    public Map<Check,Object> listWorks(@PathVariable String stId){
        return invoiceEditDto.listWorks(stId);
    }

    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check,Object> deleteWork(@PathVariable String stId){
        return invoiceEditDto.deleteWork(stId);
    }

}
