package com.example.sportal.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reset_password_links")
@Getter
@Setter
public class ResetPasswordLink {
    @Id
    private String URI;
    @Column(nullable = false)
    private Calendar expiresAt;
    @OneToOne(cascade = CascadeType.PERSIST)
    private User intendedFor;
}
