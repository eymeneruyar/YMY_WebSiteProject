package YMY.utils;

import org.apache.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    public static float monthlyGoal = 150000;

    public static boolean isEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    //Logger
    public static void logger(String data,Class logClass){
        Logger.getLogger(logClass).error(data);
    }

    //Error messages editing
    public static List<Map<String,String>> errors(BindingResult bindingResult){

        List<Map<String,String>> list = new ArrayList<>();

        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String fieldMessage = error.getDefaultMessage();
            Map<String,String> map = new LinkedHashMap<>();
            map.put("fieldName",fieldName);
            map.put("fieldMessage",fieldMessage);
            list.add(map);
        });
        return list;
    }

    //Generate date
    public static String generateDate(){
        //String pattern = "dd-MM-yyyy HH:mm:ss";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

}
