package backend.service.impl;

import backend.repository.CartRepository;
import backend.service.CartService;
import backend.service.dto.CartDTO;
import backend.service.mapper.CartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link backend.domain.Cart}.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public Mono<CartDTO> save(CartDTO cartDTO) {
        log.debug("Request to save Cart : {}", cartDTO);
        return cartRepository.save(cartMapper.toEntity(cartDTO)).map(cartMapper::toDto);
    }

    @Override
    public Mono<CartDTO> update(CartDTO cartDTO) {
        log.debug("Request to update Cart : {}", cartDTO);
        return cartRepository.save(cartMapper.toEntity(cartDTO)).map(cartMapper::toDto);
    }

    @Override
    public Mono<CartDTO> partialUpdate(CartDTO cartDTO) {
        log.debug("Request to partially update Cart : {}", cartDTO);

        return cartRepository
            .findById(cartDTO.getId())
            .map(existingCart -> {
                cartMapper.partialUpdate(existingCart, cartDTO);

                return existingCart;
            })
            .flatMap(cartRepository::save)
            .map(cartMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CartDTO> findAll() {
        log.debug("Request to get all Carts");
        return cartRepository.findAll().map(cartMapper::toDto);
    }

    public Mono<Long> countAll() {
        return cartRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CartDTO> findOne(Long id) {
        log.debug("Request to get Cart : {}", id);
        return cartRepository.findById(id).map(cartMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Cart : {}", id);
        return cartRepository.deleteById(id);
    }
}
