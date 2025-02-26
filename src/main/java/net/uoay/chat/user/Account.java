package net.uoay.chat.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_account", indexes = { @Index(columnList = "username") })
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne(optional = false)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role = AccountRole.User;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    public Account() {}

    public Account(String username, String password, Profile profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile.setDisplayName(profile.getDisplayName());
        profile.getSex().ifPresent(sex -> this.profile.setSex(sex));
        profile.getBirthday().ifPresent(birthday -> this.profile.setBirthday(birthday));
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
