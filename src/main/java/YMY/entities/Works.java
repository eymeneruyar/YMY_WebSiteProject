package YMY.entities;

import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Works extends BaseStructure{

    @Column(columnDefinition = "TEXT")
    @NotNull(message = "Yapılan iş alanı boş olamaz!")
    @NotEmpty(message = "Yapılan iş alanı boş olamaz!")
    @Comment("Yapılan iş")
    private String work;

    @Column(length = 100)
    @NotNull(message = "Miktar alanı boş olamaz!")
    @NotEmpty(message = "Miktar alanı boş olamaz!")
    @Comment("Yapılan işin miktar")
    private String quantity;

    @Column(length = 100)
    @NotNull(message = "Birim fiyat alanı boş olamaz!")
    @NotEmpty(message = "Birim fiyat alanı boş olamaz!")
    @Comment("Yapılan işin birim fiyatı")
    private String unitPrice;

    @Column(length = 100)
    @Comment("miktar * birim fiyat")
    private String total;

}
