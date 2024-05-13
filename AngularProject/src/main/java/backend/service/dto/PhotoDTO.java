package backend.service.dto;

import backend.domain.enumeration.PhotoState;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link backend.domain.Photo} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhotoDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] photo;

    private String photoContentType;

    private LocalDate date;

    private PhotoState state;

    private PortfolioDTO portfolio;

    private CartDTO cart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PhotoState getState() {
        return state;
    }

    public void setState(PhotoState state) {
        this.state = state;
    }

    public PortfolioDTO getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioDTO portfolio) {
        this.portfolio = portfolio;
    }

    public CartDTO getCart() {
        return cart;
    }

    public void setCart(CartDTO cart) {
        this.cart = cart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhotoDTO)) {
            return false;
        }

        PhotoDTO photoDTO = (PhotoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, photoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhotoDTO{" +
            "id=" + getId() +
            ", photo='" + getPhoto() + "'" +
            ", date='" + getDate() + "'" +
            ", state='" + getState() + "'" +
            ", portfolio=" + getPortfolio() +
            ", cart=" + getCart() +
            "}";
    }
}
