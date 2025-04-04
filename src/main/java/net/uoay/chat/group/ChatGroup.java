package net.uoay.chat.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import net.uoay.chat.user.Account;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chat_group")
public class ChatGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String displayName;

    @ManyToMany
    @JsonIgnore
    private Set<Account> members;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Account owner;

    @ManyToMany
    @JsonIgnore
    private Set<Account> admins;

    @CreatedDate
    @Column(nullable = false)
    @JsonIgnore
    private LocalDateTime createdDate;

    public ChatGroup() {}

    public ChatGroup(Account owner, String displayName) {
        this.owner = owner;
        this.displayName = displayName;
        members = new HashSet<>();
        members.add(owner);
        admins = new HashSet<>();
    }

    public Integer getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public boolean addMember(Account account) {
        return members.add(account);
    }

    public boolean removeMember(Account account) {
        return members.remove(account);
    }

    public boolean setOwner(Account account) {
        if (members.contains(account)) {
            owner = account;
            return true;
        }
        return false;
    }

    public boolean setAdmin(Account account) {
        if (members.contains(account)) {
            return admins.add(account);
        }
        return false;
    }

    public boolean isMember(Account account) {
        return members.contains(account);
    }

    public boolean isOwner(Account account) {
        return owner.equals(account);
    }

    public boolean isAdmin(Account account) {
        return admins.contains(account);
    }

    public boolean contains(Account account) {
        return members.contains(account);
    }

    public Set<Account> getMembers() {
        return members;
    }

}
