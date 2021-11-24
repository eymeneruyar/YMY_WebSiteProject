package YMY.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

public class Util {

    public static boolean isEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

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

}
