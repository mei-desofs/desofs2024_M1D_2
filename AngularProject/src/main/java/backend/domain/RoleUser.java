package backend.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A RoleUser.
 */
@Table("role_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoleUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Transient
    private User userId;

    @Transient
    private Role roleId;

    @Column("user_id_id")
    private Long userIdId;

    @Column("role_id_id")
    private Long roleIdId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RoleUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return this.userId;
    }

    public void setUserId(User user) {
        this.userId = user;
        this.userIdId = user != null ? user.getId() : null;
    }

    public RoleUser userId(User user) {
        this.setUserId(user);
        return this;
    }

    public Role getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Role role) {
        this.roleId = role;
        this.roleIdId = role != null ? role.getId() : null;
    }

    public RoleUser roleId(Role role) {
        this.setRoleId(role);
        return this;
    }

    public Long getUserIdId() {
        return this.userIdId;
    }

    public void setUserIdId(Long user) {
        this.userIdId = user;
    }

    public Long getRoleIdId() {
        return this.roleIdId;
    }

    public void setRoleIdId(Long role) {
        this.roleIdId = role;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleUser)) {
            return false;
        }
        return getId() != null && getId().equals(((RoleUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleUser{" +
            "id=" + getId() +
            "}";
    }
}
