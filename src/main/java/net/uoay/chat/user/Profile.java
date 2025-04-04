package net.uoay.chat.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import net.uoay.chat.Utils;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name = "user_profile")
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = Utils.displayNamePattern)
    @Column(nullable = false)
    private String displayName;

    @Nullable
    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Nullable
    @Column
    private LocalDate birthday;

    public Profile() {}

    public Profile(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Optional<Sex> getSex() {
        return Optional.ofNullable(sex);
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Optional<LocalDate> getBirthday() {
        return Optional.ofNullable(birthday);
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

}
