package net.uoay.chat.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uoay.chat.util.ValidUtils;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_profile")
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Pattern(regexp = ValidUtils.displayNamePattern)
    @Column(nullable = false)
    private String displayName;

    @Nullable
    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Nullable
    @Column
    private LocalDate birthday;

    public Profile(String displayName) {
        this.displayName = displayName;
    }

    public Optional<Sex> getSex() {
        return Optional.ofNullable(sex);
    }

    public Optional<LocalDate> getBirthday() {
        return Optional.ofNullable(birthday);
    }
}
