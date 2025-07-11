package com.example.SpringWorking.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

     @NotBlank(message = "enter title")
    private String title;
    @NotBlank(message = "enter body")
@Column(columnDefinition = "TEXT")
    private String body;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // DEFINING RELATION BETWEEN ACCOUNT AND POST CLASS.
    @ManyToOne()
    @JoinColumn(name = "account_id" ,referencedColumnName = "account_id",nullable = true)
     private Account account;

}
