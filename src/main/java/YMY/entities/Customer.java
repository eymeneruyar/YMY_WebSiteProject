package YMY.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Entity
public class Customer extends BaseStructure{

    @NotNull(message = "Müşteri kodu boş olamaz!")
    @NotEmpty(message = "Müşteri kodu boş olamaz!")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String code;

    @NotNull(message = "Müşteri ismi boş olamaz!")
    @NotEmpty(message = "Müşteri ismi boş olamaz!")
    @Column(length = 100)
    private String name;

    @NotNull(message = "Müşteri soyismi boş olamaz!")
    @NotEmpty(message = "Müşteri soyismi boş olamaz!")
    @Column(length = 50)
    private String surname;

    @NotNull(message = "Müşteri telefonu boş olamaz!")
    @NotEmpty(message = "Müşteri telefonu boş olamaz!")
    @Column(length = 12)
    private String phone;

    @Column(length = 50)
    private String email;

    @Column(length = 50)
    private String brand;

    @Column(length = 50)
    private String model;

    @NotNull(message = "Araç plakan no boş olamaz!")
    @NotEmpty(message = "Araç plakan no boş olamaz!")
    @Column(length = 20)
    private String plate;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id")
    private Company company;

}
