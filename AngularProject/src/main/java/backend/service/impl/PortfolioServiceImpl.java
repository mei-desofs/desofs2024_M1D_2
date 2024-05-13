package backend.service.impl;

import backend.repository.PortfolioRepository;
import backend.service.PortfolioService;
import backend.service.dto.PortfolioDTO;
import backend.service.mapper.PortfolioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link backend.domain.Portfolio}.
 */
@Service
@Transactional
public class PortfolioServiceImpl implements PortfolioService {

    private final Logger log = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    private final PortfolioRepository portfolioRepository;

    private final PortfolioMapper portfolioMapper;

    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.portfolioMapper = portfolioMapper;
    }

    @Override
    public Mono<PortfolioDTO> save(PortfolioDTO portfolioDTO) {
        log.debug("Request to save Portfolio : {}", portfolioDTO);
        return portfolioRepository.save(portfolioMapper.toEntity(portfolioDTO)).map(portfolioMapper::toDto);
    }

    @Override
    public Mono<PortfolioDTO> update(PortfolioDTO portfolioDTO) {
        log.debug("Request to update Portfolio : {}", portfolioDTO);
        return portfolioRepository.save(portfolioMapper.toEntity(portfolioDTO)).map(portfolioMapper::toDto);
    }

    @Override
    public Mono<PortfolioDTO> partialUpdate(PortfolioDTO portfolioDTO) {
        log.debug("Request to partially update Portfolio : {}", portfolioDTO);

        return portfolioRepository
            .findById(portfolioDTO.getId())
            .map(existingPortfolio -> {
                portfolioMapper.partialUpdate(existingPortfolio, portfolioDTO);

                return existingPortfolio;
            })
            .flatMap(portfolioRepository::save)
            .map(portfolioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PortfolioDTO> findAll() {
        log.debug("Request to get all Portfolios");
        return portfolioRepository.findAll().map(portfolioMapper::toDto);
    }

    public Mono<Long> countAll() {
        return portfolioRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PortfolioDTO> findOne(Long id) {
        log.debug("Request to get Portfolio : {}", id);
        return portfolioRepository.findById(id).map(portfolioMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Portfolio : {}", id);
        return portfolioRepository.deleteById(id);
    }
}
