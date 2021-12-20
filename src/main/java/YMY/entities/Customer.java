package YMY.entities;

import lombok.Data;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Value;

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
    @Comment(value = "Müşteri kodu")
    private String code;

    @Value("-")
    @Column(length = 100)
    @Comment(value = "Müşteri İsmi")
    private String name;

    @Value("-")
    @Column(length = 50)
    @Comment(value = "Müşteri Soyismi")
    private String surname;

    @Value("-")
    @Column(length = 12)
    @Comment(value = "Müşteri telefon numarası")
    private String phone;

    @Value("-")
    @Column(length = 50)
    @Comment(value = "Müşteri email")
    private String email;

    @Value("-")
    @Column(length = 50)
    @Comment(value = "Müşteri araç markası")
    private String brand;

    @Value("-")
    @Column(length = 50)
    @Comment(value = "Müşteri araç modeli")
    private String model;

    @Value("-")
    @Column(length = 20)
    @Comment(value = "Müşteri araç plaka")
    private String plate;

    @Column(columnDefinition = "TEXT")
    @Comment(value = "Müşteri notu")
    private String note;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id")
    private Company company;

}
