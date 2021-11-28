package YMY.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Works extends BaseStructure{

    @Column(columnDefinition = "TEXT")
    @NotNull(message = "Yapılan iş alanı boş olamaz!")
    @NotEmpty(message = "Yapılan iş alanı boş olamaz!")
    private String work;

    @Column(length = 100)
    @NotNull(message = "Miktar alanı boş olamaz!")
    @NotEmpty(message = "Miktar alanı boş olamaz!")
    private String quantity;

    @Column(length = 100)
    @NotNull(message = "Fiyat alanı boş olamaz!")
    @NotEmpty(message = "Fiyat alanı boş olamaz!")
    private String price;

    @Column(length = 100)
    private String total;

}
