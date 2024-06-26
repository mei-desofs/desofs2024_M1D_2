package backend.service.mapper;

import backend.domain.Cart;
import backend.domain.Photo;
import backend.domain.Portfolio;
import backend.service.dto.CartDTO;
import backend.service.dto.PhotoDTO;
import backend.service.dto.PortfolioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {
    @Mapping(target = "portfolio", source = "portfolio", qualifiedByName = "portfolioId")
    @Mapping(target = "cart", source = "cart", qualifiedByName = "cartId")
    PhotoDTO toDto(Photo s);

    @Named("portfolioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PortfolioDTO toDtoPortfolioId(Portfolio portfolio);

    @Named("cartId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CartDTO toDtoCartId(Cart cart);
}
