package YMY.controllers;

import YMY.dto.BoxActionsDto;
import YMY.entities.BoxActions;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("kasa_haraketleri")
public class BoxActionsController {

    final BoxActionsDto boxActionsDto;
    public BoxActionsController(BoxActionsDto boxActionsDto) {
        this.boxActionsDto = boxActionsDto;
    }

    @GetMapping("")
    public String boxActions(){
        return "boxActions";
    }

    @ResponseBody
    @PostMapping("/save")
    public Map<Check,Object> save(@RequestBody @Valid BoxActions boxActions,BindingResult bindingResult){
        return boxActionsDto.save(boxActions,bindingResult);
    }

    @ResponseBody
    @PutMapping("/undoBoxActions/{stId}")
    public Map<Check,Object> undoBoxActions(@PathVariable String stId){
        return boxActionsDto.undoBoxActions(stId);
    }

    @ResponseBody
    @GetMapping("/listBoxActionsPaydayToToday")
    public Map<Check,Object> listBoxActionsPaydayToToday(){
        return boxActionsDto.listBoxActionsPaydayToToday();
    }

    @ResponseBody
    @GetMapping("/listBoxActionsByDescriptionAndDate/{description}/{date}")
    public Map<Check,Object> listBoxActionsByDescriptionAndDate(@PathVariable String description,@PathVariable String date){
        return boxActionsDto.listBoxActionsByDescriptionAndDate(description,date);
    }

    @ResponseBody
    @GetMapping("/listBoxActionsByDescriptionAndDateAndCompany/{description}/{date}/{company}")
    public Map<Check,Object> listBoxActionsByDescriptionAndDateAndCompany(@PathVariable String description,@PathVariable String date,@PathVariable String company){
        return boxActionsDto.listBoxActionsByDescriptionAndDateAndCompany(description,date,company);
    }

    @ResponseBody
    @GetMapping("/listCompany")
    public Map<Check,Object> listCompany(){
        return boxActionsDto.listCompany();
    }

    @ResponseBody
    @GetMapping("/listOfCustomerBySelectedCompany/{stId}")
    public Map<Check,Object> listOfCustomerBySelectedCompany(@PathVariable String stId){
        return boxActionsDto.listOfCustomerBySelectedCompany(stId);
    }

    @ResponseBody
    @GetMapping("/listOfInvoiceCodeBySelectedCustomer/{stId}")
    public Map<Check,Object> listOfInvoiceCodeBySelectedCustomer(@PathVariable String stId){
        return boxActionsDto.listOfInvoiceCodeBySelectedCustomer(stId);
    }

}
