package backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Cart.
 */
@Table("cart")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("total")
    private Integer total;

    @Transient
    private Payment payment;

    @Transient
    @JsonIgnoreProperties(value = { "portfolio", "cart" }, allowSetters = true)
    private Set<Photo> photos = new HashSet<>();

    @Column("payment_id")
    private Long paymentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return this.total;
    }

    public Cart total(Integer total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
        this.paymentId = payment != null ? payment.getId() : null;
    }

    public Cart payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public Set<Photo> getPhotos() {
        return this.photos;
    }

    public void setPhotos(Set<Photo> photos) {
        if (this.photos != null) {
            this.photos.forEach(i -> i.setCart(null));
        }
        if (photos != null) {
            photos.forEach(i -> i.setCart(this));
        }
        this.photos = photos;
    }

    public Cart photos(Set<Photo> photos) {
        this.setPhotos(photos);
        return this;
    }

    public Cart addPhotos(Photo photo) {
        this.photos.add(photo);
        photo.setCart(this);
        return this;
    }

    public Cart removePhotos(Photo photo) {
        this.photos.remove(photo);
        photo.setCart(null);
        return this;
    }

    public Long getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(Long payment) {
        this.paymentId = payment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cart)) {
            return false;
        }
        return getId() != null && getId().equals(((Cart) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            "}";
    }
}
