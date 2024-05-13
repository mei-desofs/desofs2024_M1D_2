package backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A User.
 */
@Table("user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("address")
    private String address;

    @Column("contact")
    private String contact;

    @Column("profile_photo")
    private byte[] profilePhoto;

    @Column("profile_photo_content_type")
    private String profilePhotoContentType;

    @Transient
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Portfolio portfolio;

    @Transient
    private RoleUser roleUser;

    @Column("portfolio_id")
    private Long portfolioId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public User id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public User email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public User password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return this.address;
    }

    public User address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return this.contact;
    }

    public User contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public byte[] getProfilePhoto() {
        return this.profilePhoto;
    }

    public User profilePhoto(byte[] profilePhoto) {
        this.setProfilePhoto(profilePhoto);
        return this;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhotoContentType() {
        return this.profilePhotoContentType;
    }

    public User profilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
        return this;
    }

    public void setProfilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.portfolioId = portfolio != null ? portfolio.getId() : null;
    }

    public User portfolio(Portfolio portfolio) {
        this.setPortfolio(portfolio);
        return this;
    }

    public RoleUser getRoleUser() {
        return this.roleUser;
    }

    public void setRoleUser(RoleUser roleUser) {
        if (this.roleUser != null) {
            this.roleUser.setUserId(null);
        }
        if (roleUser != null) {
            roleUser.setUserId(this);
        }
        this.roleUser = roleUser;
    }

    public User roleUser(RoleUser roleUser) {
        this.setRoleUser(roleUser);
        return this;
    }

    public Long getPortfolioId() {
        return this.portfolioId;
    }

    public void setPortfolioId(Long portfolio) {
        this.portfolioId = portfolio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return getId() != null && getId().equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", address='" + getAddress() + "'" +
            ", contact='" + getContact() + "'" +
            ", profilePhoto='" + getProfilePhoto() + "'" +
            ", profilePhotoContentType='" + getProfilePhotoContentType() + "'" +
            "}";
    }
}
