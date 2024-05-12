package com.isep.desofs.service.mapper;

import com.isep.desofs.domain.Portfolio;
import com.isep.desofs.service.dto.PortfolioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Portfolio} and its DTO {@link PortfolioDTO}.
 */
@Mapper(componentModel = "spring")
public interface PortfolioMapper extends EntityMapper<PortfolioDTO, Portfolio> {}
