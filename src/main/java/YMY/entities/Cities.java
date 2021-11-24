package YMY.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Cities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "sehir_title")
    private String name;

    @Column(name = "sehir_key")
    private int cityKey;

}
