package YMY.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Company extends BaseStructure{

    @NotNull(message = "Firma kodu boş olamaz!")
    @NotEmpty(message = "Firma kodu boş olamaz!")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String code;

    @NotNull(message = "Firma ismi boş olamaz!")
    @NotEmpty(message = "Firma ismi boş olamaz!")
    @Size(min = 1, max = 150, message = "Firma ismi minimum 1, maksimum 150 karakter olmalıdır!")
    @Column(length = 150)
    private String name;

    @Size(max = 150, message = "Firma yetkilisi maksimum 150 karakter olmalıdır!")
    @Column(length = 150)
    private String authorisedPerson;

    @NotNull(message = "Firma telefonu boş olamaz!")
    @NotEmpty(message = "Firma telefonu boş olamaz!")
    //@Pattern(regexp="(^$|[0-9]{20})",message = "Girilen telefon formatı (###-###-####) olmalıdır!")
    @Column(length = 12)
    private String phone;

    @Size(max=50)
    //@Pattern(regexp="(^(.+)@(.+)$)")
    @Column(length = 50)
    private String email;

    @Size(max = 50, message = "Vergi dairesi maksimum 50 karakter olmalıdır!")
    @Column(length = 50)
    private String taxOffice;

    @Size(max = 11, message = "Vergi dairesi numarası maksimum 11 karakter olmalıdır!")
    @Pattern(regexp="(^$|[0-9]{11})",message = "Vergi numarası 11 karakter olmalıdır!")
    @Column(length = 11)
    private String taxNumber;

    @Size(max = 50, message = "İl maksimum 50 karakter olmalıdır!")
    @Column(length = 50)
    private String city; //İl

    @Size(max = 50, message = "İlçe maksimum 50 karakter olmalıdır!")
    @Column(length = 75)
    private String town; //İlçe

    @Column(columnDefinition = "TEXT")
    private String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.DETACH, orphanRemoval = true)
    private List<Customer> customers = new ArrayList<>();

}
