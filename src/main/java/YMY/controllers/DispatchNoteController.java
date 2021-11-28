package YMY.controllers;

import YMY.dto.DispatchNoteDto;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("irsaliye")
public class DispatchNoteController {

    final DispatchNoteDto dispatchNoteDto;
    public DispatchNoteController(DispatchNoteDto dispatchNoteDto) {
        this.dispatchNoteDto = dispatchNoteDto;
    }

    @GetMapping("")
    public String dispatchNote(){
        return "dispatchNote";
    }

    @ResponseBody
    @GetMapping("/listCompanyByUserId")
    public Map<Check,Object> listCompanyByUserId(){
        return dispatchNoteDto.listCompanyByUserId();
    }

    @ResponseBody
    @GetMapping("/listCustomersBySelectedCompany/{stId}")
    public Map<Check,Object> listCustomersBySelectedCompany(@PathVariable String stId){
        return dispatchNoteDto.listCustomersBySelectedCompany(stId);
    }

}
