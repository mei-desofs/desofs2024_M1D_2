package com.isep.desofs.service.impl;

import com.isep.desofs.domain.RoleUser;
import com.isep.desofs.repository.RoleUserRepository;
import com.isep.desofs.service.RoleUserService;
import com.isep.desofs.service.dto.RoleUserDTO;
import com.isep.desofs.service.mapper.RoleUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.isep.desofs.domain.RoleUser}.
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
    public RoleUserDTO save(RoleUserDTO roleUserDTO) {
        log.debug("Request to save RoleUser : {}", roleUserDTO);
        RoleUser roleUser = roleUserMapper.toEntity(roleUserDTO);
        roleUser = roleUserRepository.save(roleUser);
        return roleUserMapper.toDto(roleUser);
    }

    @Override
    public RoleUserDTO update(RoleUserDTO roleUserDTO) {
        log.debug("Request to update RoleUser : {}", roleUserDTO);
        RoleUser roleUser = roleUserMapper.toEntity(roleUserDTO);
        roleUser = roleUserRepository.save(roleUser);
        return roleUserMapper.toDto(roleUser);
    }

    @Override
    public Optional<RoleUserDTO> partialUpdate(RoleUserDTO roleUserDTO) {
        log.debug("Request to partially update RoleUser : {}", roleUserDTO);

        return roleUserRepository
            .findById(roleUserDTO.getId())
            .map(existingRoleUser -> {
                roleUserMapper.partialUpdate(existingRoleUser, roleUserDTO);

                return existingRoleUser;
            })
            .map(roleUserRepository::save)
            .map(roleUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleUserDTO> findAll() {
        log.debug("Request to get all RoleUsers");
        return roleUserRepository.findAll().stream().map(roleUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleUserDTO> findOne(Long id) {
        log.debug("Request to get RoleUser : {}", id);
        return roleUserRepository.findById(id).map(roleUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RoleUser : {}", id);
        roleUserRepository.deleteById(id);
    }
}
