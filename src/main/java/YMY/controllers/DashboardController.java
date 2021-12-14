package YMY.controllers;

import YMY.dto.DashboardDto;
import YMY.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("yonetim")
public class DashboardController {

    final DashboardDto dashboardDto;
    public DashboardController(DashboardDto dashboardDto) {
        this.dashboardDto = dashboardDto;
    }

    @GetMapping("")
    public String dashboard(){
        return "dashboard";
    }

    @ResponseBody
    @GetMapping("/infoMontlyEarning")
    public Map<Check,Object> infoMontlyEarning(){
        return dashboardDto.infoMontlyEarning();
    }

    @ResponseBody
    @GetMapping("/infoGeneralStatistics")
    public Map<Check,Object> infoGeneralStatistics(){
        return dashboardDto.infoGeneralStatistics();
    }

    @ResponseBody
    @GetMapping("/listDebtorCompanies")
    public Map<Check,Object> listDebtorCompanies(){
        return dashboardDto.listDebtorCompanies();
    }

    @ResponseBody
    @GetMapping("/listDebtorCustomers")
    public Map<Check,Object> listDebtorCustomers(){
        return dashboardDto.listDebtorCustomers();
    }

    @ResponseBody
    @GetMapping("/infoAgendaNoteCard/{stPageNo}")
    public Map<Check,Object> infoAgendaNoteCard(@PathVariable String stPageNo){
        return dashboardDto.infoAgendaNoteCard(stPageNo);
    }

    @ResponseBody
    @GetMapping("/infoMonthlyGoalOverview")
    public Map<Check,Object> infoMonthlyGoalOverview(){
        return dashboardDto.infoMonthlyGoalOverview();
    }

}
