package YMY.controllers;

import YMY.dto.CompanyDto;
import YMY.entities.Company;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("firma")
public class CompanyController {

    final CompanyDto companyDto;
    public CompanyController(CompanyDto companyDto) {
        this.companyDto = companyDto;
    }

    @GetMapping("")
    public String company(){
        return "company";
    }

    @ResponseBody
    @PostMapping("/saveOrUpdate")
    public Map<Check,Object> companySaveOrUpdate(@RequestBody @Valid Company company, BindingResult bindingResult){
        return companyDto.companySaveOrUpdate(company,bindingResult);
    }

}
