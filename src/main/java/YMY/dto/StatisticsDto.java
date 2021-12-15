package YMY.dto;

import YMY.entities.Company;
import YMY.entities.Invoice;
import YMY.entities.User;
import YMY.repositories.*;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class StatisticsDto {

    final CompanyRepository companyRepository;
    final CustomerRepository customerRepository;
    final InvoiceRepository invoiceRepository;
    final BoxActionsRepository boxActionsRepository;
    final UserService userService;
    public StatisticsDto(CompanyRepository companyRepository, CustomerRepository customerRepository, InvoiceRepository invoiceRepository, BoxActionsRepository boxActionsRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.boxActionsRepository = boxActionsRepository;
        this.userService = userService;
    }

    //Top Paying Company
    public Map<Check,Object> infoTopPayingCompany(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        HashMap<Integer,Float> temp = new LinkedHashMap<>();
        HashMap<Object,Object> result = new LinkedHashMap<>();
        User user = userService.userInfo();
        try{
            if(user.getId() != null){
                List<Company> companyList = companyRepository.findByStatusEqualsAndUserIdEqualsOrderByIdAsc(true,user.getId());
                for (int i = 0; i < companyList.size(); i++) {
                    List<Invoice> invoiceList = invoiceRepository.findByStatusEqualsAndUserIdEqualsAndCompany_IdEquals(true,user.getId(),companyList.get(i).getId());
                    float total = 0;
                    if(invoiceList.size() > 0){
                        for (int j = 0; j < invoiceList.size(); j++) {
                            total += invoiceList.get(j).getPaid();
                        }
                        temp.put(companyList.get(i).getId(),total);
                    }else{
                        temp.put(companyList.get(i).getId(),0f);
                    }
                }
                float maxValueInMap = Collections.max(temp.values());
                for (Map.Entry<Integer, Float> entry : temp.entrySet()) {  // Iterate through HashMap
                    if (entry.getValue()==maxValueInMap) {
                        Optional<Company> optionalCompany = companyRepository.findById(entry.getKey());
                        result.put("company",optionalCompany.get());
                        result.put("total",maxValueInMap);
                    }
                }
                hm.put(Check.status,true);
                hm.put(Check.message,"En çok kazandıran bilgisi başarılı bir şekilde getirildi!");
                hm.put(Check.result,result);
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "En çok kazandıran bilgisi getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,StatisticsDto.class);
        }
        return hm;
    }

    //Yearly Goal Overview
    public Map<Check,Object> infoYearlyGoalOverview(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        DecimalFormat df = new DecimalFormat();
        Map<String,Object> goalOverview = new LinkedHashMap<>();
        User user = userService.userInfo();
        float profit = 0;
        float percentage = 0;
        try{
            if(user.getId() != null){
                percentage = (profit/Util.monthlyGoal) * 100;
                df.setMinimumFractionDigits(2);
                goalOverview.put("monthlyGoal",Util.monthlyGoal);
                goalOverview.put("profit",profit);
                goalOverview.put("percentage",df.format(percentage));

                hm.put(Check.status,true);
                hm.put(Check.message,"Aylık hedef bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,goalOverview);
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Aylık hedef bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,DashboardDto.class);
        }
        return hm;
    }

}
