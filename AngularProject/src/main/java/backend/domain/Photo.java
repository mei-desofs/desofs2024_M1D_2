package backend.domain;

import backend.domain.enumeration.PhotoState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Photo.
 */
@Table("photo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("photo")
    private byte[] photo;

    @Column("photo_content_type")
    private String photoContentType;

    @Column("date")
    private LocalDate date;

    @Column("state")
    private PhotoState state;

    @Transient
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Portfolio portfolio;

    @Transient
    @JsonIgnoreProperties(value = { "payment", "photos" }, allowSetters = true)
    private Cart cart;

    @Column("portfolio_id")
    private Long portfolioId;

    @Column("cart_id")
    private Long cartId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Photo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Photo photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Photo photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Photo date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PhotoState getState() {
        return this.state;
    }

    public Photo state(PhotoState state) {
        this.setState(state);
        return this;
    }

    public void setState(PhotoState state) {
        this.state = state;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.portfolioId = portfolio != null ? portfolio.getId() : null;
    }

    public Photo portfolio(Portfolio portfolio) {
        this.setPortfolio(portfolio);
        return this;
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        this.cartId = cart != null ? cart.getId() : null;
    }

    public Photo cart(Cart cart) {
        this.setCart(cart);
        return this;
    }

    public Long getPortfolioId() {
        return this.portfolioId;
    }

    public void setPortfolioId(Long portfolio) {
        this.portfolioId = portfolio;
    }

    public Long getCartId() {
        return this.cartId;
    }

    public void setCartId(Long cart) {
        this.cartId = cart;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }
        return getId() != null && getId().equals(((Photo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Photo{" +
            "id=" + getId() +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", date='" + getDate() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
