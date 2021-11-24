package YMY.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("irsaliye")
public class DispatchNoteController {

    @GetMapping("")
    public String dispatchNote(){
        return "dispatchNote";
    }

}
