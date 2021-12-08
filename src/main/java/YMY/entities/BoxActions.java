package YMY.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class BoxActions extends BaseStructure{

    @Comment(value = "0 -> Çıkış, 1-> Giriş, null -> Seçim yapınız")
    @NotNull(message = "Kasa tanımı boş olamaz!")
    private Integer description;

    @Comment(value = "Ödeme başlığı")
    @Column(length = 75)
    @NotNull(message = "Başlık alanı boş olamaz!")
    @NotEmpty(message = "Başlık alanı boş olamaz!")
    private String title;

    @Comment(value = "Ödeme yöntemi")
    @Column(length = 50)
    @NotNull(message = "Ödeme yöntemi alanı boş olamaz!")
    @NotEmpty(message = "Ödeme yöntemi alanı boş olamaz!")
    private String paymentMethod;

    @Comment(value = "Ödeme yöntemi")
    @NotNull(message = "Tutar alanı boş olamaz!")
    private Float amount;

    @Comment(value = "İşlem tarihi")
    @Column(length = 15)
    @NotNull(message = "İşlem tarihi alanı boş olamaz!")
    @NotEmpty(message = "İşlem tarihi alanı boş olamaz!")
    private String transactionDate;

    @Comment(value = "Not")
    @Column(columnDefinition = "TEXT",length = 500)
    private String note;

    @OneToOne(cascade = CascadeType.DETACH, orphanRemoval = true)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(cascade = CascadeType.DETACH, orphanRemoval = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.DETACH, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

}
