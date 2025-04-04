package net.uoay.chat.user;

import jakarta.persistence.*;
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

    @ManyToMany(cascade = CascadeType.ALL)
    @Column
    private Set<Friendship> friendships;

    @ManyToMany
    @Column
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

    public Account() {}

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

    public Integer getId() {
        return id;
    }

    public Set<String> getFriends() {
        return friendships
            .stream()
            .map(friendship -> {
                return friendship.getAnother(username).orElseThrow();
            })
            .collect(Collectors.toSet());
    }

    public void addFriend(Friendship friendship) {
        friendships.add(friendship);
    }

    public boolean removeFriend(Friendship friendship) {
        return friendships.remove(friendship);
    }

    public boolean hasFriend(String username) {
        return getFriends().contains(username);
    }

    public Set<ChatGroup> getGroups() {
        return groups;
    }

    public boolean joinGroup(ChatGroup group) {
        return groups.add(group);
    }

    public boolean leaveGroup(ChatGroup group) {
        return groups.remove(group);
    }

    public boolean isInGroup(ChatGroup group) {
        return groups.contains(group);
    }

}
