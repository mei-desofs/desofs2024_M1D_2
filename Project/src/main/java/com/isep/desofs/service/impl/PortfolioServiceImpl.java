package com.isep.desofs.service.impl;

import com.isep.desofs.domain.Portfolio;
import com.isep.desofs.repository.PortfolioRepository;
import com.isep.desofs.service.PortfolioService;
import com.isep.desofs.service.dto.PortfolioDTO;
import com.isep.desofs.service.mapper.PortfolioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.isep.desofs.domain.Portfolio}.
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
    public PortfolioDTO save(PortfolioDTO portfolioDTO) {
        log.debug("Request to save Portfolio : {}", portfolioDTO);
        Portfolio portfolio = portfolioMapper.toEntity(portfolioDTO);
        portfolio = portfolioRepository.save(portfolio);
        return portfolioMapper.toDto(portfolio);
    }

    @Override
    public PortfolioDTO update(PortfolioDTO portfolioDTO) {
        log.debug("Request to update Portfolio : {}", portfolioDTO);
        Portfolio portfolio = portfolioMapper.toEntity(portfolioDTO);
        portfolio = portfolioRepository.save(portfolio);
        return portfolioMapper.toDto(portfolio);
    }

    @Override
    public Optional<PortfolioDTO> partialUpdate(PortfolioDTO portfolioDTO) {
        log.debug("Request to partially update Portfolio : {}", portfolioDTO);

        return portfolioRepository
            .findById(portfolioDTO.getId())
            .map(existingPortfolio -> {
                portfolioMapper.partialUpdate(existingPortfolio, portfolioDTO);

                return existingPortfolio;
            })
            .map(portfolioRepository::save)
            .map(portfolioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioDTO> findAll() {
        log.debug("Request to get all Portfolios");
        return portfolioRepository.findAll().stream().map(portfolioMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PortfolioDTO> findOne(Long id) {
        log.debug("Request to get Portfolio : {}", id);
        return portfolioRepository.findById(id).map(portfolioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Portfolio : {}", id);
        portfolioRepository.deleteById(id);
    }
}
