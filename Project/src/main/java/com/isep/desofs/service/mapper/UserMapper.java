package com.isep.desofs.service.mapper;

import com.isep.desofs.domain.Portfolio;
import com.isep.desofs.domain.User;
import com.isep.desofs.service.dto.PortfolioDTO;
import com.isep.desofs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {
    @Mapping(target = "portfolio", source = "portfolio", qualifiedByName = "portfolioId")
    UserDTO toDto(User s);

    @Named("portfolioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PortfolioDTO toDtoPortfolioId(Portfolio portfolio);
}
