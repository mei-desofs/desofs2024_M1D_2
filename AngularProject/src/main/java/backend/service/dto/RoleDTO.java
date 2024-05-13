package backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link backend.domain.Role} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoleDTO implements Serializable {

    private Long id;

    private String nameRole;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDTO)) {
            return false;
        }

        RoleDTO roleDTO = (RoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleDTO{" +
            "id=" + getId() +
            ", nameRole='" + getNameRole() + "'" +
            "}";
    }
}
