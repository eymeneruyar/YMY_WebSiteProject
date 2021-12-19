package YMY.entities;

import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class YearlyGoal extends BaseStructure{

    @Column(length = 50)
    @Comment(value = "Yıllık hedef anahtarı")
    @NotNull(message = "Yıllık hedef anahtarınızı girin!")
    @NotEmpty(message = "Yıllık hedef anahtarınızı girin!")
    private String key;

    @Comment(value = "Yıllık hedef miktarı")
    @NotNull(message = "Yıllık hedef miktarınızı girin!")
    private Float goal;

}
