package tech11.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column( name = "username", nullable = false, updatable = false, unique = true)
    private String username;

    @Column( name = "email", nullable = false, updatable = false, unique = true)
    private String email;

    @Column(name= "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthday", nullable = false)
    private String birthday;

    @Column(name = "phone")
    private String phone;

    @Column(name = "dob", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "modified")
    private LocalDateTime modified;

    @PrePersist
    public void updateCreated() {
        this.modified = LocalDateTime.now();
        this.created = LocalDateTime.now();
    }

    @PreUpdate
    public void updateModified () {
        this.modified = LocalDateTime.now();
    }
}


//todo abstract this class into a base entity