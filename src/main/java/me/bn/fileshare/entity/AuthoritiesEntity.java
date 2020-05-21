package me.bn.fileshare.entity;

import javax.persistence.*;
import java.util.Objects;

/*@Entity
@Table(name = "authorities", schema = "file_share", catalog = "")*/
public class AuthoritiesEntity {
    private String authority;
    private UsersEntity usersByUsername;

    @Basic
    @Column(name = "authority")
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthoritiesEntity that = (AuthoritiesEntity) o;
        return Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority);
    }

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UsersEntity getUsersByUsername() {
        return usersByUsername;
    }

    public void setUsersByUsername(UsersEntity usersByUsername) {
        this.usersByUsername = usersByUsername;
    }
}
