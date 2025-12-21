package org.myatdental.dentistoptions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myatdental.authoption.useroptions.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dentists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dentist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dentist_id")
    private Long id;

    @Column(length = 10, nullable = false, unique = true)
    private String code;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30)
    private String specialization;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @PrePersist
    protected void onCreate() {

        if (this.joinDate == null) {
            this.joinDate = LocalDate.now();
        }

    }
}