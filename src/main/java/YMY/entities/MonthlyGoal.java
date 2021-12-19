package YMY.entities;

import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class MonthlyGoal extends BaseStructure{

    @Column(length = 50)
    @Comment(value = "Aylık hedef anahtarı")
    @NotNull(message = "Aylık hedef anahtarınızı girin!")
    @NotEmpty(message = "Aylık hedef anahtarınızı girin!")
    private String key;

    @Comment(value = "Aylık hedef miktarı")
    @NotNull(message = "Aylık hedef miktarınızı girin!")
    private Float goal;


}
