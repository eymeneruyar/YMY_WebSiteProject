package YMY.dto;

import YMY.entities.Agenda;
import YMY.entities.User;
import YMY.repositories.AgendaRepository;
import YMY.services.UserService;
import YMY.utils.Check;
import YMY.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AgendaDto {

    final AgendaRepository agendaRepository;
    final UserService userService;
    public AgendaDto(AgendaRepository agendaRepository, UserService userService) {
        this.agendaRepository = agendaRepository;
        this.userService = userService;
    }

    //Save the note to agenda
    public Map<Check,Object> save(Agenda agenda, BindingResult bindingResult){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(!bindingResult.hasErrors()){
                if(user.getId() != null){
                    agenda.setStatus(true);
                    agenda.setUserId(user.getId());
                    agenda.setDate(Util.generateDate());
                    agendaRepository.saveAndFlush(agenda);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Ajandaya not kayıt işlemi başarıyla tamamlandı!");
                    hm.put(Check.result,agenda);
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Ajandaya not kayıt işlemi sırasında bir hata oluştu!");
                hm.put(Check.error,bindingResult.getAllErrors());
            }
        } catch (Exception e) {
            String error = "İşlem sırasında bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e,Agenda.class);
        }
        return hm;
    }

    //Delete note by selected
    public Map<Check,Object> deleteNote(String stId){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            int id = Integer.parseInt(stId);
            Optional<Agenda> optionalAgenda = agendaRepository.findById(id);
            if(user.getId() != null){
                if(optionalAgenda.isPresent()){
                    Agenda agenda = optionalAgenda.get();
                    agenda.setStatus(false);
                    agendaRepository.saveAndFlush(agenda);
                    hm.put(Check.status,true);
                    hm.put(Check.message,"Not silme işlemi başarıyla tamamlandı!");
                    hm.put(Check.result,agenda);
                }else{
                    hm.put(Check.status,false);
                    hm.put(Check.message,"Silinmek istenen not bulunamadı!");
                }
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapıp tekrar deneyin!");
            }
        }catch (Exception e){
            String error = "Not silinirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Agenda.class);
        }
        return hm;
    }

    //List all notes by status and userId
    public Map<Check,Object> listNote(){
        Map<Check,Object> hm = new LinkedHashMap<>();
        User user = userService.userInfo();
        try {
            if(user.getId() != null){
                hm.put(Check.status,true);
                hm.put(Check.message,"Not listeleme işlemi başarıyla tamamlandı!");
                hm.put(Check.result,agendaRepository.findByStatusEqualsAndUserIdEqualsOrderByIdDesc(true, user.getId()));
            }else{
                hm.put(Check.status,false);
                hm.put(Check.message,"Lütfen hesabınıza giriş yapıp tekrar deneyin!");
            }
        }catch (Exception e){
            String error = "Notlar listelenirken bir hata oluştu!";
            hm.put(Check.status,false);
            hm.put(Check.message,error);
            Util.logger(error + " " + e, Agenda.class);
        }
        return hm;
    }

}
