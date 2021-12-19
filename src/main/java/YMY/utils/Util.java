package YMY.utils;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    public static long maxFileUploadSize = 5120;
    public static final String UPLOAD_DIR_PROFILE_IMAGES  = "src/main/resources/images/profile_images/";

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

    //Add Image
    public static Map<Check, Object> imageUpload(MultipartFile file, String UPLOAD_DIR) {
        String fileName = "";
        String errorMessage = "";
        Map<Check, Object> hm = new LinkedHashMap<>();
        if (!file.isEmpty() ) {
            long fileSizeMB = file.getSize() / 1024;
            if ( fileSizeMB > maxFileUploadSize ) {
                errorMessage = "File should be max "+ (maxFileUploadSize / 1024) +"MB!";
            }else {
                fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                String ext = fileName.substring(fileName.length()-5);
                String uui = UUID.randomUUID().toString();
                fileName = uui + ext;
                try {
                    Path path = Paths.get(UPLOAD_DIR + fileName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    hm.put(Check.result, fileName);
                } catch ( IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            errorMessage = "Please select an image!";
        }

        if ( errorMessage.equals("") ) {
            hm.put(Check.status, true);
            hm.put(Check.message, fileName);
        }else {
            hm.put(Check.status, false);
            hm.put(Check.message, errorMessage);
        }

        return hm;
    }

}
