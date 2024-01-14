package com.ajith.dailyJobs.worker.entity;


import com.ajith.dailyJobs.token.Token;
import com.ajith.dailyJobs.worker.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name="_user")
public class Worker implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String profileImagePath;
    private  String phoneNumber;
    private Date joinDate;
    private String emailVerificationToken;
    private boolean isEmailVerified = false;
    private boolean isActive = true;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy ="worker")
    private List< Token > tokens;
    @Override
    public Collection < ? extends GrantedAuthority > getAuthorities ( ) {
        return List.of ( new SimpleGrantedAuthority ( role.name ( ) ) );
    }

    @Override
    public String getPassword ( ) {
        return password;
    }

    @Override
    public String getUsername ( ) {
        return email;
    }

    @Override
    public boolean isAccountNonExpired ( ) {
        return true;
    }

    @Override
    public boolean isAccountNonLocked ( ) {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired ( ) {
        return true;
    }

    @Override
    public boolean isEnabled ( ) {
        return true;
    }

    public boolean getIsActive() {

        return isActive;
    }

    public void setIsActive(boolean isActive) {

        this.isActive = isActive;
    }
}
