package backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link backend.domain.Receipt} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReceiptDTO implements Serializable {

    private Long id;

    private Long idPayment;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(Long idPayment) {
        this.idPayment = idPayment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceiptDTO)) {
            return false;
        }

        ReceiptDTO receiptDTO = (ReceiptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, receiptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReceiptDTO{" +
            "id=" + getId() +
            ", idPayment=" + getIdPayment() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
