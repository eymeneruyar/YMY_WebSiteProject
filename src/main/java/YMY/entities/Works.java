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

    @Column(length = 5)
    @NotNull(message = "Miktar alanı boş olamaz!")
    @Comment("Yapılan işin miktar")
    private Integer quantity;

    @Column(length = 16)
    @NotNull(message = "Birim fiyat alanı boş olamaz!")
    @Comment("Yapılan işin birim fiyatı")
    private Float unitPrice;

    @Column(length = 16)
    @Comment("miktar * birim fiyat")
    private Float total;

}
