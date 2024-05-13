package backend.service.mapper;

import backend.domain.Cart;
import backend.domain.Payment;
import backend.service.dto.CartDTO;
import backend.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring")
public interface CartMapper extends EntityMapper<CartDTO, Cart> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    CartDTO toDto(Cart s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
