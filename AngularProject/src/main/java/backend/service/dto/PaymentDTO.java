package backend.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link backend.domain.Payment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDTO implements Serializable {

    private Long id;

    private Long idCart;

    private LocalDate date;

    private ReceiptDTO receipt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCart() {
        return idCart;
    }

    public void setIdCart(Long idCart) {
        this.idCart = idCart;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ReceiptDTO getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptDTO receipt) {
        this.receipt = receipt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paymentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "id=" + getId() +
            ", idCart=" + getIdCart() +
            ", date='" + getDate() + "'" +
            ", receipt=" + getReceipt() +
            "}";
    }
}
