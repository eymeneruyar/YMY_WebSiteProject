package YMY.controllers;

import YMY.dto.CompanyDetailDto;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/firma_detay")
public class CompanyDetail {

    final CompanyDetailDto companyDetailDto;
    public CompanyDetail(CompanyDetailDto companyDetailDto) {
        this.companyDetailDto = companyDetailDto;
    }

    @GetMapping("/{companyId}")
    public String companyDetail(@PathVariable String companyId){
        return "companyDetail";
    }

    @ResponseBody
    @GetMapping("/listAllInvoiceBySelectedCompany/{stId}")
    public Map<Check,Object> listAllInvoiceBySelectedCompany(@PathVariable String stId){
        return companyDetailDto.listAllInvoiceBySelectedCompany(stId);
    }

    @ResponseBody
    @GetMapping("/listInvoiceByFilter/{stId}/{date}/{debtStatus}/{billingStatus}")
    public Map<Check,Object> listInvoiceByFilter(@PathVariable String stId,@PathVariable String date,@PathVariable String debtStatus,@PathVariable String billingStatus){
        return companyDetailDto.listInvoiceByFilter(stId,date,debtStatus,billingStatus);
    }


}
