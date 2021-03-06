package YMY.controllers;

import YMY.dto.CustomerDto;
import YMY.entities.Customer;
import YMY.utils.Check;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("musteri")
public class CustomerController {

    final CustomerDto customerDto;
    public CustomerController(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    @GetMapping("")
    public String customer(){
        return "customer";
    }

    @ResponseBody
    @PostMapping("/saveOrUpdate")
    public Map<Check,Object> customerSaveOrUpdate(@RequestBody @Valid Customer customer, BindingResult bindingResult){
        return customerDto.customerSaveOrUpdate(customer,bindingResult);
    }

    @ResponseBody
    @GetMapping("/list")
    public Map<Check,Object> listCustomer(){
        return customerDto.listCustomer();
    }

    @ResponseBody
    @GetMapping("/listBySelectedCompany/{stId}")
    public Map<Check,Object> listCustomersBySelectedCompany(@PathVariable String stId){
        return customerDto.listCustomersBySelectedCompany(stId);
    }

    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check,Object> deleteCustomer(@PathVariable String stId){
        return customerDto.deleteCustomer(stId);
    }

    @ResponseBody
    @GetMapping("/detail/{stId}")
    public Map<Check,Object> detailCustomer(@PathVariable String stId){
        return customerDto.detailCustomer(stId);
    }

    @ResponseBody
    @GetMapping("/listAllCompany")
    //@Cacheable("customerCompanyList")
    public Map<Check,Object> listAllCompany(){
        return customerDto.listAllCompany();
    }

}
