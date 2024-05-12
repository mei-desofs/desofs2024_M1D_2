package com.isep.desofs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_role")
    private String nameRole;

    @JsonIgnoreProperties(value = { "userId", "roleId" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "roleId")
    private RoleUser roleUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameRole() {
        return this.nameRole;
    }

    public Role nameRole(String nameRole) {
        this.setNameRole(nameRole);
        return this;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }

    public RoleUser getRoleUser() {
        return this.roleUser;
    }

    public void setRoleUser(RoleUser roleUser) {
        if (this.roleUser != null) {
            this.roleUser.setRoleId(null);
        }
        if (roleUser != null) {
            roleUser.setRoleId(this);
        }
        this.roleUser = roleUser;
    }

    public Role roleUser(RoleUser roleUser) {
        this.setRoleUser(roleUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return getId() != null && getId().equals(((Role) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", nameRole='" + getNameRole() + "'" +
            "}";
    }
}
