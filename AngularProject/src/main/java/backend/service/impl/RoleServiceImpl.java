package backend.service.impl;

import backend.repository.RoleRepository;
import backend.service.RoleService;
import backend.service.dto.RoleDTO;
import backend.service.mapper.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link backend.domain.Role}.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public Mono<RoleDTO> save(RoleDTO roleDTO) {
        log.debug("Request to save Role : {}", roleDTO);
        return roleRepository.save(roleMapper.toEntity(roleDTO)).map(roleMapper::toDto);
    }

    @Override
    public Mono<RoleDTO> update(RoleDTO roleDTO) {
        log.debug("Request to update Role : {}", roleDTO);
        return roleRepository.save(roleMapper.toEntity(roleDTO)).map(roleMapper::toDto);
    }

    @Override
    public Mono<RoleDTO> partialUpdate(RoleDTO roleDTO) {
        log.debug("Request to partially update Role : {}", roleDTO);

        return roleRepository
            .findById(roleDTO.getId())
            .map(existingRole -> {
                roleMapper.partialUpdate(existingRole, roleDTO);

                return existingRole;
            })
            .flatMap(roleRepository::save)
            .map(roleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<RoleDTO> findAll() {
        log.debug("Request to get all Roles");
        return roleRepository.findAll().map(roleMapper::toDto);
    }

    /**
     *  Get all the roles where RoleUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RoleDTO> findAllWhereRoleUserIsNull() {
        log.debug("Request to get all roles where RoleUser is null");
        return roleRepository.findAllWhereRoleUserIsNull().map(roleMapper::toDto);
    }

    public Mono<Long> countAll() {
        return roleRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RoleDTO> findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        return roleRepository.findById(id).map(roleMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        return roleRepository.deleteById(id);
    }
}
