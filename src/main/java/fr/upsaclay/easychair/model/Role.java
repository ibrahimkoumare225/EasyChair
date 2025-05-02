package fr.upsaclay.easychair.model;


import fr.upsaclay.easychair.model.enumates.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.lang.Contract;

@Entity
@Table(name = "roles")
//A revoir avec singleTable , TablePERCLASS impossible avec generation identity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

   /* @ManyToOne
    @JoinColumn(name="conference_id",nullable = false)
    private Conference conference;*/



}
