package YMY.controllers;

import YMY.dto.AgendaDto;
import YMY.entities.Agenda;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/ajandam")
public class AgendaController {

    final AgendaDto agendaDto;
    public AgendaController(AgendaDto agendaDto) {
        this.agendaDto = agendaDto;
    }

    @GetMapping("")
    public String agenda(){
        return "agenda";
    }

    @ResponseBody
    @PostMapping("/saveOrUpdate")
    public Map<Check,Object> save(@RequestBody @Valid Agenda agenda, BindingResult bindingResult){
        return agendaDto.save(agenda,bindingResult);
    }

    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check,Object> deleteNote(@PathVariable String stId){
        return agendaDto.deleteNote(stId);
    }

    @ResponseBody
    @GetMapping("/list")
    public Map<Check,Object> listNote(){
        return agendaDto.listNote();
    }

}
