package backend.service.mapper;

import backend.domain.Receipt;
import backend.service.dto.ReceiptDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Receipt} and its DTO {@link ReceiptDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReceiptMapper extends EntityMapper<ReceiptDTO, Receipt> {}
