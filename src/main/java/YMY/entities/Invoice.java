package YMY.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Invoice extends BaseStructure{

    @Column(length = 15)
    @NotNull(message = "Fatura kodu boş olamaz!")
    @NotEmpty(message = "Fatura kodu boş olamaz!")
    @Comment("Fatura kodu")
    private String invoiceCode;

    @Column(length = 5)
    @NotNull(message = "KDV alanı boş olamaz!")
    @Comment("KDV")
    private Integer vat;

    @Column(length = 5)
    @NotNull(message = "İskonto alanı boş olamaz!")
    @Comment("İskonto")
    private Integer discount;

    @Comment("Fatura kesim tarihi")
    private String billingDate;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(length = 10)
    @Comment("Borç")
    private Float debt; //Borç

    @Column(length = 10)
    @Comment("Ödenen miktar")
    private Float paid; //Ödenen

    @Column(length = 10)
    @Comment("Kalan borç")
    private Float remainingDebt; //Kalan Borç

    @Comment("Fatura ödenme durumu: true = Ödenmiş, false = ödenmemiş")
    private boolean paidStatus;

    @Column(length = 10)
    @Comment("Fatura kesim durumu")
    private String billingStatus;

    @OneToOne(cascade = CascadeType.DETACH, orphanRemoval = true)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(cascade = CascadeType.DETACH, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinColumn(name = "ınvoice_id")
    private List<Works> workses = new ArrayList<>();

}
