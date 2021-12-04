package YMY.controllers;

import YMY.entities.Invoice;
import YMY.repositories.InvoiceRepository;
import YMY.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/fatura")
public class InvoicePreviewController {

    final InvoiceRepository invoiceRepository;
    public InvoicePreviewController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping("/{stId}")
    public String invoice(@PathVariable String stId, Model model){
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
            String error = "Fatura ön izleme yapılırken bir hata oluştu!";
            Util.logger(error + " " + e, Invoice.class);
            return "redirect:/yonetim";
        }
        return "invoicePreview";
    }

}
