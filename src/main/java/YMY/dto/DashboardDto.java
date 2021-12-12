package YMY.dto;

import YMY.entities.BoxActions;
import YMY.entities.User;
import YMY.repositories.*;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardDto {

    final CompanyRepository companyRepository;
    final CustomerRepository customerRepository;
    final InvoiceRepository invoiceRepository;
    final BoxActionsRepository boxActionsRepository;
    final AgendaRepository agendaRepository;
    final UserService userService;
    public DashboardDto(CompanyRepository companyRepository, CustomerRepository customerRepository, InvoiceRepository invoiceRepository, BoxActionsRepository boxActionsRepository, AgendaRepository agendaRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.boxActionsRepository = boxActionsRepository;
        this.agendaRepository = agendaRepository;
        this.userService = userService;
    }

    //Monthly earnings
    public Map<Check,Object> infoMontlyEarning(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        Map<String,Object> earningCard = new LinkedHashMap<>();
        User user = userService.userInfo();
        List<Float> earningList = new ArrayList<>();
        List<Float> expenseList = new ArrayList<>();
        float earning = 0;
        float expense = 0;
        float profit = 0;
        String startDate = "";
        String endDate = "";
        try{
            if(user.getId() != null){
                String[] date = Util.generateDate().split("-");
                startDate = date[0] + "-" + date[1] + "-" + "01";
                endDate = Util.generateDate();
                List<BoxActions> boxActionsListEarning = boxActionsRepository.findByStatusEqualsAndUserIdEqualsAndDescriptionEqualsAndTransactionDateBetween(true,user.getId(),1,startDate,endDate); //Kasa girişi
                List<BoxActions> boxActionsListExpense = boxActionsRepository.findByStatusEqualsAndUserIdEqualsAndDescriptionEqualsAndTransactionDateBetween(true,user.getId(),0,startDate,endDate); //Kasa çıkışı
                boxActionsListEarning.forEach(item->{
                    earningList.add(item.getAmount());
                });
                boxActionsListExpense.forEach(item -> {
                    expenseList.add(item.getAmount());
                });
                earning = (float) earningList.stream().mapToDouble(Float::floatValue).sum();
                expense = (float) expenseList.stream().mapToDouble(Float::floatValue).sum();
                profit = earning - expense;
                earningCard.put("earning",earning);
                earningCard.put("expense",expense);
                earningCard.put("profit",profit);
                hm.put(Check.status,true);
                hm.put(Check.message,"Aylık kazanç bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,earningCard);
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Aylık kazanç bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,DashboardDto.class);
        }
        return hm;
    }

    //General Statistics
    public Map<Check,Object> infoGeneralStatistics(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        Map<String,Object> statisticsCard = new LinkedHashMap<>();
        User user = userService.userInfo();
        int totalCompany = 0;
        int totalCustomer = 0;
        int totalWork = 0;
        float totalRevenue = 0;
        try{
            if(user.getId() != null){
                totalCompany = companyRepository.countByStatusEqualsAndUserIdEquals(true, user.getId());
                totalCustomer = customerRepository.countByStatusEqualsAndUserIdEquals(true,user.getId());
                totalWork = invoiceRepository.countByStatusEqualsAndUserId(true,user.getId());
                statisticsCard.put("totalCompany",totalCompany);
                statisticsCard.put("totalCustomer",totalCustomer);
                statisticsCard.put("totalWork",totalWork);

                hm.put(Check.status,true);
                hm.put(Check.message,"Genel istatistik bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,statisticsCard);
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Genel istatistik bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,DashboardDto.class);
        }
        return hm;
    }

}