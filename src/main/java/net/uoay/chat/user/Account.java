package net.uoay.chat.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uoay.chat.friend.Friendship;
import net.uoay.chat.group.ChatGroup;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_account", indexes = { @Index(columnList = "username") })
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
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
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @Column
    @Setter(AccessLevel.NONE)
    private Set<Friendship> friendships;

    @ManyToMany
    @Column
    @Setter(AccessLevel.NONE)
    private Set<ChatGroup> groups;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            return Objects.equals(username, ((Account)obj).username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    public Account(String username, String password, Profile profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
        groups = new HashSet<>();
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

    @Override
    public String getPassword() {
        return password;
    }

    public void setProfile(Profile profile) {
        this.profile.setDisplayName(profile.getDisplayName());
        profile.getSex().ifPresent(sex -> this.profile.setSex(sex));
        profile.getBirthday().ifPresent(birthday -> this.profile.setBirthday(birthday));
    }

    public Set<String> getFriends() {
        return friendships
            .stream()
            .map(friendship -> friendship.getAnother(username).orElseThrow())
            .collect(Collectors.toSet());
    }

    public void addFriend(Friendship friendship) {
        friendships.add(friendship);
    }

    public boolean removeFriend(Friendship friendship) {
        return friendships.remove(friendship);
    }

    public Set<String> getGroups() {
        return groups
            .stream()
            .map(ChatGroup::getSearchId)
            .collect(Collectors.toSet());
    }

    public boolean joinGroup(ChatGroup group) {
        return groups.add(group);
    }

    public boolean leaveGroup(ChatGroup group) {
        return groups.remove(group);
    }
}
