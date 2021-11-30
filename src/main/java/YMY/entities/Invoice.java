package YMY.entities;

import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Invoice extends BaseStructure{

    @Column(length = 15)
    @NotNull(message = "Fatura kodu boş olamaz!")
    @NotEmpty(message = "Fatura kodu boş olamaz!")
    @Comment("Fatura kodu")
    private String invoiceCode;

    @Column(length = 5)
    @NotNull(message = "KDV alanı boş olamaz!")
    @NotEmpty(message = "KDV alanı boş olamaz!")
    @Comment("KDV")
    private String vat;

    @Column(length = 5)
    @NotNull(message = "İskonto alanı boş olamaz!")
    @NotEmpty(message = "İskonto alanı boş olamaz!")
    @Comment("İskonto")
    private String discount;

    @Comment("Fatura kesim tarihi")
    private String billingDate;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(length = 10)
    @Comment("Borç")
    private String debt; //Borç

    @Column(length = 10)
    @Comment("Ödenen miktar")
    private String paid; //Ödenen

    @Column(length = 10)
    @Comment("Kalan borç")
    private String remainingDebt; //Kalan Borç

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ınvoice_id")
    private List<Works> workses = new ArrayList<>();

}
