package backend.web.rest;

import backend.repository.UserRepository;
import backend.service.UserService;
import backend.service.dto.UserDTO;
import backend.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link backend.domain.User}.
 */
@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "user";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    public UserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /users} : Create a new user.
     *
     * @param userDTO the userDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDTO, or with status {@code 400 (Bad Request)} if the user has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserDTO>> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);
        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userService
            .save(userDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/users/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /users/:id} : Updates an existing user.
     *
     * @param id the id of the userDTO to save.
     * @param userDTO the userDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDTO,
     * or with status {@code 400 (Bad Request)} if the userDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDTO>> updateUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserDTO userDTO
    ) throws URISyntaxException {
        log.debug("REST request to update User : {}, {}", id, userDTO);
        if (userDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userService
                    .update(userDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /users/:id} : Partial updates given fields of an existing user, field will ignore if it is null
     *
     * @param id the id of the userDTO to save.
     * @param userDTO the userDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDTO,
     * or with status {@code 400 (Bad Request)} if the userDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserDTO>> partialUpdateUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserDTO userDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update User partially : {}, {}", id, userDTO);
        if (userDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserDTO> result = userService.partialUpdate(userDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /users} : get all the users.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<UserDTO>> getAllUsers(@RequestParam(name = "filter", required = false) String filter) {
        if ("roleuser-is-null".equals(filter)) {
            log.debug("REST request to get all Users where roleUser is null");
            return userService.findAllWhereRoleUserIsNull().collectList();
        }
        log.debug("REST request to get all Users");
        return userService.findAll().collectList();
    }

    /**
     * {@code GET  /users} : get all the users as a stream.
     * @return the {@link Flux} of users.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UserDTO> getAllUsersAsStream() {
        log.debug("REST request to get all Users as a stream");
        return userService.findAll();
    }

    /**
     * {@code GET  /users/:id} : get the "id" user.
     *
     * @param id the id of the userDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDTO>> getUser(@PathVariable("id") Long id) {
        log.debug("REST request to get User : {}", id);
        Mono<UserDTO> userDTO = userService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDTO);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" user.
     *
     * @param id the id of the userDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete User : {}", id);
        return userService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
