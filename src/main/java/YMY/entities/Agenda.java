package YMY.entities;

import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Agenda extends BaseStructure{

    @NotNull(message = "Not başlığı boş olamaz!")
    @NotEmpty(message = "Not başlığı boş olamaz!")
    @Comment(value = "Not başlığı")
    @Column(length = 100)
    private String title;

    @NotNull(message = "Not hatırlatma tarihi boş olamaz!")
    @NotEmpty(message = "Not hatırlatma tarihi boş olamaz!")
    @Comment(value = "Not hatırlatma tarihi")
    @Column(length = 20)
    private String reminderDate;

    @NotNull(message = "Not boş olamaz!")
    @NotEmpty(message = "Not boş olamaz!")
    @Comment(value = "Kullanıcı tarafından girilen not")
    @Column(columnDefinition = "TEXT")
    private String note;

}
