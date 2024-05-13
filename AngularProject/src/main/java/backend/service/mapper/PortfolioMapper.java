package backend.service.mapper;

import backend.domain.Portfolio;
import backend.service.dto.PortfolioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Portfolio} and its DTO {@link PortfolioDTO}.
 */
@Mapper(componentModel = "spring")
public interface PortfolioMapper extends EntityMapper<PortfolioDTO, Portfolio> {}
