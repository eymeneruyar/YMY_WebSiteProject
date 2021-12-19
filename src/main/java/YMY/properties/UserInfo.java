package YMY.properties;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserInfo {

    @NotNull(message = "Kullanıcı ismi boş olamaz!")
    @NotEmpty(message = "Kullanıcı ismi boş olamaz!")
    private String name;

    @NotNull(message = "Kullanıcı soyismi boş olamaz!")
    @NotEmpty(message = "Kullanıcı soyismi boş olamaz!")
    private String surname;

    @NotNull(message = "Kullanıcı email boş olamaz!")
    @NotEmpty(message = "Kullanıcı email boş olamaz!")
    private String email;

    @NotNull(message = "Kullanıcı firması boş olamaz!")
    @NotEmpty(message = "Kullanıcı firması boş olamaz!")
    private String companyName;

}
