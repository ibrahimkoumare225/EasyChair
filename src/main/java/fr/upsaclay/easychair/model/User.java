package fr.upsaclay.easychair.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    
    private String firstName;
    private String lastName;
    private String pseudo;
    private String password;
    private String email;
    private String photo;
    
    @Column(name = "birth_date")
    private LocalDateTime birthDate;
    
    @ElementCollection
    @CollectionTable(name = "user_keywords", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Role> roles = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
}
