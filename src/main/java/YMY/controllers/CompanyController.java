package YMY.controllers;

import YMY.dto.CompanyDto;
import YMY.entities.Company;
import YMY.utils.Check;
import org.springframework.cache.annotation.Cacheable;
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

    @ResponseBody
    @GetMapping("/list")
    @Cacheable("companyList")
    public Map<Check,Object> listCompany(){
        return companyDto.listCompany();
    }

    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check,Object> deleteCompany(@PathVariable String stId){
        return companyDto.deleteCompany(stId);
    }

    @ResponseBody
    @GetMapping("/listCities")
    @Cacheable("citiesList")
    public Map<Check,Object> listCities(){
        return companyDto.listCities();
    }

    @ResponseBody
    @GetMapping("/listTownsBySelectedCity/{stId}")
    @Cacheable("townsList")
    public Map<Check,Object> listTownsBySelectedCity(@PathVariable String stId){
        return companyDto.listTownsBySelectedCity(stId);
    }

    @ResponseBody
    @GetMapping("/getInfoCityByCityKey/{stId}")
    public Map<Check,Object> getInfoCityByCityKey(@PathVariable String stId){
        return companyDto.getInfoCityByCityKey(stId);
    }

    @ResponseBody
    @GetMapping("/detail/{stId}")
    public Map<Check,Object> detailCompany(@PathVariable String stId){
        return companyDto.detailCompany(stId);
    }

}
