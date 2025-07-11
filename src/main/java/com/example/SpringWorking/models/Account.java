package com.example.SpringWorking.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long account_id;

    @NotEmpty(message = "enter password")
    private String password;

    @Email(message = "enter valid email")
    @NotEmpty(message = "enter an email")
    private String email;

    @NotEmpty(message = "enter username")
    private String username;
 
    @Min(value = 18)
    @Max(value = 100)
    private int age;

    private String photo;


    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_of_birth;


    private String role;

    @Column(name="token")
    private String password_reset_token;
    private LocalDateTime password_reset_token_expiry;

    // MAPPING RELATION BETWEEN ACCOUNT AND POST 
    // 1 ACCOUNT->MULTIPLE POSTS
    @OneToMany(mappedBy = "account")
    List<Post>posts;

    
    // MAPPING RELATION BETWEEN ACCOUNT AND AUTHORITY
    @ManyToMany
    @JoinTable(
        name="account_authority",
        joinColumns = {@JoinColumn(name="account_id",referencedColumnName = "account_id")},
        inverseJoinColumns = {@JoinColumn(name="authority_id",referencedColumnName = "id")}
    )
    private Set<Authority> authorities=new HashSet<>();

}
