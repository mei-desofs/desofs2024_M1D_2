package backend.domain;

import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Payment.
 */
@Table("payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("id_cart")
    private Long idCart;

    @Column("date")
    private LocalDate date;

    @Transient
    private Receipt receipt;

    @Transient
    private Cart cart;

    @Column("receipt_id")
    private Long receiptId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCart() {
        return this.idCart;
    }

    public Payment idCart(Long idCart) {
        this.setIdCart(idCart);
        return this;
    }

    public void setIdCart(Long idCart) {
        this.idCart = idCart;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Payment date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Receipt getReceipt() {
        return this.receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
        this.receiptId = receipt != null ? receipt.getId() : null;
    }

    public Payment receipt(Receipt receipt) {
        this.setReceipt(receipt);
        return this;
    }

    public Cart getCart() {
        return this.cart;
    }

    public void setCart(Cart cart) {
        if (this.cart != null) {
            this.cart.setPayment(null);
        }
        if (cart != null) {
            cart.setPayment(this);
        }
        this.cart = cart;
    }

    public Payment cart(Cart cart) {
        this.setCart(cart);
        return this;
    }

    public Long getReceiptId() {
        return this.receiptId;
    }

    public void setReceiptId(Long receipt) {
        this.receiptId = receipt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return getId() != null && getId().equals(((Payment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", idCart=" + getIdCart() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
