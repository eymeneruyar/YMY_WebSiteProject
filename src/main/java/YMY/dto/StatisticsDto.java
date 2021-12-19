package YMY.dto;

import YMY.entities.*;
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
    final YearlyGoalRepository yearlyGoalRepository;
    final UserService userService;
    public StatisticsDto(CompanyRepository companyRepository, CustomerRepository customerRepository, InvoiceRepository invoiceRepository, BoxActionsRepository boxActionsRepository, YearlyGoalRepository yearlyGoalRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.boxActionsRepository = boxActionsRepository;
        this.yearlyGoalRepository = yearlyGoalRepository;
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
        Map<String,Object> goalOverview = new LinkedHashMap<>();
        User user = userService.userInfo();
        List<Float> earningList = new ArrayList<>();
        List<Float> expenseList = new ArrayList<>();
        float earning = 0;
        float expense = 0;
        float profit = 0;
        float percentage = 0;
        float yearlyGoal = 0;
        String startYear = Util.generateDate().split("-")[0];
        String endYear = String.valueOf(Integer.parseInt(startYear) + 1);
        try{
            if(user.getId() != null){
                List<BoxActions> boxActionsListEarning = boxActionsRepository.findByStatusEqualsAndUserIdEqualsAndDescriptionEqualsAndTransactionDateBetween(true,user.getId(),1,startYear,endYear); //Kasa girişi
                List<BoxActions> boxActionsListExpense = boxActionsRepository.findByStatusEqualsAndUserIdEqualsAndDescriptionEqualsAndTransactionDateBetween(true,user.getId(),0,startYear,endYear); //Kasa çıkışı
                boxActionsListEarning.forEach(item->{
                    earningList.add(item.getAmount());
                });
                boxActionsListExpense.forEach(item -> {
                    expenseList.add(item.getAmount());
                });
                earning = (float) earningList.stream().mapToDouble(Float::floatValue).sum();
                expense = (float) expenseList.stream().mapToDouble(Float::floatValue).sum();
                profit = earning - expense;

                //Yearly Goal Calculation
                Optional<YearlyGoal> optionalYearlyGoal = yearlyGoalRepository.findByStatusEqualsAndUserIdEqualsAndKeyEquals(true, user.getId(), Util.generateDate().split("-")[0]);
                if(optionalYearlyGoal.isPresent()){
                    YearlyGoal goal = optionalYearlyGoal.get();
                    yearlyGoal = goal.getGoal();
                }else{
                    yearlyGoal = 750000; //Default value
                }

                percentage = (profit/yearlyGoal) * 100;
                goalOverview.put("yearlyGoal",yearlyGoal);
                goalOverview.put("profit",profit);
                goalOverview.put("percentage",Math.ceil(percentage));

                hm.put(Check.status,true);
                hm.put(Check.message,"Yıllık hedef bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,goalOverview);
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Yıllık hedef bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,StatisticsDto.class);
        }
        return hm;
    }

    //Revenue Report
    public Map<Check,Object> infoRevenueReport(String startYear){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        String endYear = String.valueOf(Integer.parseInt(startYear) + 1);
        try{
            if(user.getId() != null){
                hm.put(Check.status,true);
                hm.put(Check.message,"Hasılat bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,boxActionsRepository.findByStatusEqualsAndUserIdEqualsAndTransactionDateBetween(true, user.getId(),startYear,endYear));
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Hasılat bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,StatisticsDto.class);
        }
        return hm;
    }

}
