package YMY.controllers;

import YMY.dto.SettingsDto;
import YMY.entities.MonthlyGoal;
import YMY.entities.User;
import YMY.entities.YearlyGoal;
import YMY.properties.ChangePassword;
import YMY.properties.UserInfo;
import YMY.services.UserService;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/ayarlar")
public class SettingsController {

    final SettingsDto settingsDto;
    final UserService userService;
    public SettingsController(SettingsDto settingsDto, UserService userService) {
        this.settingsDto = settingsDto;
        this.userService = userService;
    }

    @GetMapping("")
    public String settings(Model model){
        User user = userService.userInfo();
        model.addAttribute("user",user);
        return "settings";
    }

    @ResponseBody
    @PostMapping("/update")
    public Map<Check, Object> update(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult){
        return settingsDto.update(userInfo,bindingResult);
    }

    @ResponseBody
    @PostMapping("/changePassword")
    public Map<Check, Object> changePassword(@RequestBody @Valid ChangePassword changePassword, BindingResult bindingResult){
        return settingsDto.changePassword(changePassword,bindingResult);
    }

    @ResponseBody
    @PostMapping("/uploadImage")
    public Map<Check, Object> uploadImage(@RequestParam("image") MultipartFile file){
        return settingsDto.uploadImage(file);
    }

    @ResponseBody
    @GetMapping("/showImage")
    public Map<Check, Object> getImage() throws IOException{
        return settingsDto.getImage();
    }

    @ResponseBody
    @PostMapping("/saveOrUpdateMonthlyGoal")
    public Map<Check, Object> saveOrUpdateMonthlyGoal(@RequestBody @Valid MonthlyGoal monthlyGoal, BindingResult bindingResult){
        return settingsDto.saveOrUpdateMonthlyGoal(monthlyGoal,bindingResult);
    }

    @ResponseBody
    @PostMapping("/saveOrUpdateYearlyGoal")
    public Map<Check, Object> saveOrUpdateYearlyGoal(@RequestBody @Valid YearlyGoal yearlyGoal, BindingResult bindingResult){
        return settingsDto.saveOrUpdateYearlyGoal(yearlyGoal,bindingResult);
    }

}
