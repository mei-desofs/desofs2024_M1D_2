package com.isep.desofs.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.isep.desofs.domain.RoleUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoleUserDTO implements Serializable {

    private Long id;

    private UserDTO userId;

    private RoleDTO roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUserId() {
        return userId;
    }

    public void setUserId(UserDTO userId) {
        this.userId = userId;
    }

    public RoleDTO getRoleId() {
        return roleId;
    }

    public void setRoleId(RoleDTO roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleUserDTO)) {
            return false;
        }

        RoleUserDTO roleUserDTO = (RoleUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roleUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleUserDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", roleId=" + getRoleId() +
            "}";
    }
}
