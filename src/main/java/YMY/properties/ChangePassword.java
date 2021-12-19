package YMY.properties;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChangePassword {

    @NotNull(message = "Eski şifre boş olamaz!")
    @NotEmpty(message = "Eski şifre boş olamaz!")
    private String oldPassword;

    @NotNull(message = "Yeni şifre boş olamaz!")
    @NotEmpty(message = "Yeni şifre boş olamaz!")
    private String newPassword;

    @NotNull(message = "Yeni şifre tekrarı boş olamaz!")
    @NotEmpty(message = "Yeni şifre tekrarı boş olamaz!")
    private String reNewPassword;

}
