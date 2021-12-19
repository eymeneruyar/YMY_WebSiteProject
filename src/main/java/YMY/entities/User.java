package YMY.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(length = 100)
    @NotNull(message = "Kullanıcı ismi boş olamaz!")
    @NotEmpty(message = "Kullanıcı ismi boş olamaz!")
    @Comment(value = "Kullanıcı ismi")
    private String name;

    @Column(length = 50)
    @NotNull(message = "Kullanıcı soyismi boş olamaz!")
    @NotEmpty(message = "Kullanıcı soyismi boş olamaz!")
    @Comment(value = "Kullanıcı soyismi")
    private String surname;

    @Column(unique = true,length = 100)
    @NotNull(message = "Kullanıcı email boş olamaz!")
    @NotEmpty(message = "Kullanıcı email boş olamaz!")
    @Comment(value = "Kullanıcı email")
    private String email;

    @Column(unique = true,length = 75)
    @NotNull(message = "Kullanıcı firması boş olamaz!")
    @NotEmpty(message = "Kullanıcı firması boş olamaz!")
    @Comment(value = "Kullanıcı firması")
    private String companyName;

    private String password;

    private String profileImage;

    private boolean enabled;

    private boolean tokenExpired;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private List<Role> roles;


}
