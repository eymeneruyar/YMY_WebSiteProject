package YMY.dto;

import YMY.entities.*;
import YMY.repositories.*;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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

    //Debtor Companies
    public Map<Check,Object> listDebtorCompanies(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        Map<Object,Object> result = new LinkedHashMap<>();
        User user = userService.userInfo();
        try{
            if(user.getId() != null){
                //Invoice repo kısmında seçilen şirkete ait ödenmemiş faturaları getiren sorgu yazıldı.
                //Company tarafından gelen şirketleri çift döngü yapılarak her firmaya ait toplam borç miktarı hesaplanacak
                List<Company> companyList = companyRepository.findByStatusEqualsAndUserIdEqualsOrderByIdAsc(true,user.getId());
                for (int i = 0; i < companyList.size(); i++) {
                    List<Invoice> invoiceList = invoiceRepository.findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndCompany_IdEquals(true,user.getId(),false,companyList.get(i).getId());
                    float total = 0;
                    if(invoiceList.size() > 0){
                        for (int j = 0; j < invoiceList.size(); j++) {
                            total += invoiceList.get(j).getRemainingDebt();
                        }
                        result.put(companyList.get(i).getId(),total);
                    }else{
                        result.put(companyList.get(i).getId(),0);
                    }
                }
                result.put("companyList",companyList);
                hm.put(Check.status,true);
                hm.put(Check.message,"Borçlu firma bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,result);
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Borçlu firma bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,DashboardDto.class);
        }
        return hm;
    }

    //Debtor customers
    public Map<Check,Object> listDebtorCustomers(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try{
            if(user.getId() != null){
                hm.put(Check.status,true);
                hm.put(Check.message,"Borçlu müşteri bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,invoiceRepository.findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsOrderByIdDesc(true, user.getId(), false));
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Borçlu müşteri bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,DashboardDto.class);
        }
        return hm;
    }

    //Agenda Note Card
    public Map<Check,Object> infoAgendaNoteCard(String stPageNo){
        Map<Check,Object> hm = new LinkedHashMap<>();
        Map<Object,Object> result = new LinkedHashMap<>();
        User user = userService.userInfo();
        try{
            if(user.getId() != null){
                int pageNo = Integer.parseInt(stPageNo);
                Pageable pageable = PageRequest.of(pageNo,1);
                Page<Agenda> agendaPage = agendaRepository.findByStatusEqualsAndUserIdEqualsAndReminderDateEqualsOrderByIdDesc(true, user.getId(), Util.generateDate(),pageable);
                result.put("totalPage",agendaPage.getTotalPages());
                result.put("agendaList",agendaPage.getContent());
                hm.put(Check.status,true);
                hm.put(Check.message,"Kişiye ait günlük not bilgileri başarılı bir şekilde getirildi!");
                hm.put(Check.result,result);
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapınız!");
            }
        }catch (Exception e){
            String error = "Kişiye ait günlük not bilgileri getirilirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,DashboardDto.class);
        }
        return hm;
    }

    //Monthly Goal Overview
    public Map<Check,Object> infoMonthlyGoalOverview(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        DecimalFormat df = new DecimalFormat();
        Map<Check,Object> infoMontlyEarning = infoMontlyEarning();
        Map<String,Object> goalOverview = new LinkedHashMap<>();
        User user = userService.userInfo();
        float profit = 0;
        float percentage = 0;
        try{
            if(user.getId() != null){
                Map<String,Object> map = (Map<String, Object>) infoMontlyEarning.get(Check.result);
                profit = (float) map.get("profit");
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
