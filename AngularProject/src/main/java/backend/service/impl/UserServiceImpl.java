package backend.service.impl;

import backend.repository.UserRepository;
import backend.service.UserService;
import backend.service.dto.UserDTO;
import backend.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link backend.domain.User}.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Mono<UserDTO> save(UserDTO userDTO) {
        log.debug("Request to save User : {}", userDTO);
        return userRepository.save(userMapper.toEntity(userDTO)).map(userMapper::toDto);
    }

    @Override
    public Mono<UserDTO> update(UserDTO userDTO) {
        log.debug("Request to update User : {}", userDTO);
        return userRepository.save(userMapper.toEntity(userDTO)).map(userMapper::toDto);
    }

    @Override
    public Mono<UserDTO> partialUpdate(UserDTO userDTO) {
        log.debug("Request to partially update User : {}", userDTO);

        return userRepository
            .findById(userDTO.getId())
            .map(existingUser -> {
                userMapper.partialUpdate(existingUser, userDTO);

                return existingUser;
            })
            .flatMap(userRepository::save)
            .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserDTO> findAll() {
        log.debug("Request to get all Users");
        return userRepository.findAll().map(userMapper::toDto);
    }

    /**
     *  Get all the users where RoleUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserDTO> findAllWhereRoleUserIsNull() {
        log.debug("Request to get all users where RoleUser is null");
        return userRepository.findAllWhereRoleUserIsNull().map(userMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserDTO> findOne(Long id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete User : {}", id);
        return userRepository.deleteById(id);
    }
}
