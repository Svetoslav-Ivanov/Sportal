package com.example.sportal.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "reset_password_links")
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordLink {
    @Id
    private String URI;
    @Column(nullable = false)
    private Calendar expiresAt;
    @OneToOne(cascade = CascadeType.PERSIST)
    private User intendedFor;
}
