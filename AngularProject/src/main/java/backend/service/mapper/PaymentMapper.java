package backend.service.mapper;

import backend.domain.Payment;
import backend.domain.Receipt;
import backend.service.dto.PaymentDTO;
import backend.service.dto.ReceiptDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "receipt", source = "receipt", qualifiedByName = "receiptId")
    PaymentDTO toDto(Payment s);

    @Named("receiptId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReceiptDTO toDtoReceiptId(Receipt receipt);
}
