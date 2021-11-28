package YMY.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class DispatchNote extends BaseStructure{

    @NotNull(message = "İrsaliye kodu boş olamaz!")
    @NotEmpty(message = "İrsaliye kodu boş olamaz!")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String code;

    @Column(length = 5)
    @NotNull(message = "KDV alanı boş olamaz!")
    @NotEmpty(message = "KDV alanı boş olamaz!")
    private String vat;

    @Column(length = 5)
    @NotNull(message = "İskonto alanı boş olamaz!")
    @NotEmpty(message = "İskonto alanı boş olamaz!")
    private String discount;

    @NotNull(message = "Fatura kodu boş olamaz!")
    @NotEmpty(message = "Fatura kodu boş olamaz!")
    private String invoiceCode;

    private String debt; //Borç

    private String paid; //Ödenen

    private String remainingDebt; //Kalan Borç

    @OneToOne(cascade = CascadeType.DETACH, orphanRemoval = true)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(cascade = CascadeType.DETACH, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.DETACH, orphanRemoval = true)
    @JoinColumn(name = "dispatch_note_id")
    private List<Works> workses = new ArrayList<>();


}
