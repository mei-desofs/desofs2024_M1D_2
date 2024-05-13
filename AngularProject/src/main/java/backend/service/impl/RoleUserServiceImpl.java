package backend.service.impl;

import backend.repository.RoleUserRepository;
import backend.service.RoleUserService;
import backend.service.dto.RoleUserDTO;
import backend.service.mapper.RoleUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link backend.domain.RoleUser}.
 */
@Service
@Transactional
public class RoleUserServiceImpl implements RoleUserService {

    private final Logger log = LoggerFactory.getLogger(RoleUserServiceImpl.class);

    private final RoleUserRepository roleUserRepository;

    private final RoleUserMapper roleUserMapper;

    public RoleUserServiceImpl(RoleUserRepository roleUserRepository, RoleUserMapper roleUserMapper) {
        this.roleUserRepository = roleUserRepository;
        this.roleUserMapper = roleUserMapper;
    }

    @Override
    public Mono<RoleUserDTO> save(RoleUserDTO roleUserDTO) {
        log.debug("Request to save RoleUser : {}", roleUserDTO);
        return roleUserRepository.save(roleUserMapper.toEntity(roleUserDTO)).map(roleUserMapper::toDto);
    }

    @Override
    public Mono<RoleUserDTO> update(RoleUserDTO roleUserDTO) {
        log.debug("Request to update RoleUser : {}", roleUserDTO);
        return roleUserRepository.save(roleUserMapper.toEntity(roleUserDTO)).map(roleUserMapper::toDto);
    }

    @Override
    public Mono<RoleUserDTO> partialUpdate(RoleUserDTO roleUserDTO) {
        log.debug("Request to partially update RoleUser : {}", roleUserDTO);

        return roleUserRepository
            .findById(roleUserDTO.getId())
            .map(existingRoleUser -> {
                roleUserMapper.partialUpdate(existingRoleUser, roleUserDTO);

                return existingRoleUser;
            })
            .flatMap(roleUserRepository::save)
            .map(roleUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<RoleUserDTO> findAll() {
        log.debug("Request to get all RoleUsers");
        return roleUserRepository.findAll().map(roleUserMapper::toDto);
    }

    public Mono<Long> countAll() {
        return roleUserRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RoleUserDTO> findOne(Long id) {
        log.debug("Request to get RoleUser : {}", id);
        return roleUserRepository.findById(id).map(roleUserMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete RoleUser : {}", id);
        return roleUserRepository.deleteById(id);
    }
}
