package backend.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link backend.domain.User} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserDTO implements Serializable {

    private Long id;

    private String email;

    private String password;

    private String address;

    private String contact;

    @Lob
    private byte[] profilePhoto;

    private String profilePhotoContentType;

    private PortfolioDTO portfolio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhotoContentType() {
        return profilePhotoContentType;
    }

    public void setProfilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
    }

    public PortfolioDTO getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioDTO portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDTO)) {
            return false;
        }

        UserDTO userDTO = (UserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", address='" + getAddress() + "'" +
            ", contact='" + getContact() + "'" +
            ", profilePhoto='" + getProfilePhoto() + "'" +
            ", portfolio=" + getPortfolio() +
            "}";
    }
}
