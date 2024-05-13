package backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link backend.domain.Cart} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CartDTO implements Serializable {

    private Long id;

    private Integer total;

    private PaymentDTO payment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartDTO)) {
            return false;
        }

        CartDTO cartDTO = (CartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cartDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CartDTO{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            ", payment=" + getPayment() +
            "}";
    }
}
