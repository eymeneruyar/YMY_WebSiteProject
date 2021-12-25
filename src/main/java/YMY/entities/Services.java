package YMY.entities;

import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Services{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean status; //true -> Available, false -> Deleted

    private String date;

    @Column(length = 150)
    @Comment(value = "Yapılan hizmetin başlığı")
    private String title;

    @Column(length = 150)
    @Comment(value = "Yapılan hizmetin kategorisi")
    private String category;

    @Column(columnDefinition = "TEXT")
    @Comment(value = "Yapılan hizmetin açıklaması")
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @Comment(value = "Yapılan hizmetin resimleri")
    private List<String> image;

}
