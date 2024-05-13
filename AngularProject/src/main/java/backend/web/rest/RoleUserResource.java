package backend.web.rest;

import backend.repository.RoleUserRepository;
import backend.service.RoleUserService;
import backend.service.dto.RoleUserDTO;
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
 * REST controller for managing {@link backend.domain.RoleUser}.
 */
@RestController
@RequestMapping("/api/role-users")
public class RoleUserResource {

    private final Logger log = LoggerFactory.getLogger(RoleUserResource.class);

    private static final String ENTITY_NAME = "roleUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleUserService roleUserService;

    private final RoleUserRepository roleUserRepository;

    public RoleUserResource(RoleUserService roleUserService, RoleUserRepository roleUserRepository) {
        this.roleUserService = roleUserService;
        this.roleUserRepository = roleUserRepository;
    }

    /**
     * {@code POST  /role-users} : Create a new roleUser.
     *
     * @param roleUserDTO the roleUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleUserDTO, or with status {@code 400 (Bad Request)} if the roleUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<RoleUserDTO>> createRoleUser(@RequestBody RoleUserDTO roleUserDTO) throws URISyntaxException {
        log.debug("REST request to save RoleUser : {}", roleUserDTO);
        if (roleUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new roleUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return roleUserService
            .save(roleUserDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/role-users/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /role-users/:id} : Updates an existing roleUser.
     *
     * @param id the id of the roleUserDTO to save.
     * @param roleUserDTO the roleUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleUserDTO,
     * or with status {@code 400 (Bad Request)} if the roleUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RoleUserDTO>> updateRoleUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleUserDTO roleUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RoleUser : {}, {}", id, roleUserDTO);
        if (roleUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return roleUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return roleUserService
                    .update(roleUserDTO)
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
     * {@code PATCH  /role-users/:id} : Partial updates given fields of an existing roleUser, field will ignore if it is null
     *
     * @param id the id of the roleUserDTO to save.
     * @param roleUserDTO the roleUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleUserDTO,
     * or with status {@code 400 (Bad Request)} if the roleUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roleUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RoleUserDTO>> partialUpdateRoleUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleUserDTO roleUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoleUser partially : {}, {}", id, roleUserDTO);
        if (roleUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return roleUserRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RoleUserDTO> result = roleUserService.partialUpdate(roleUserDTO);

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
     * {@code GET  /role-users} : get all the roleUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleUsers in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<RoleUserDTO>> getAllRoleUsers() {
        log.debug("REST request to get all RoleUsers");
        return roleUserService.findAll().collectList();
    }

    /**
     * {@code GET  /role-users} : get all the roleUsers as a stream.
     * @return the {@link Flux} of roleUsers.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<RoleUserDTO> getAllRoleUsersAsStream() {
        log.debug("REST request to get all RoleUsers as a stream");
        return roleUserService.findAll();
    }

    /**
     * {@code GET  /role-users/:id} : get the "id" roleUser.
     *
     * @param id the id of the roleUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RoleUserDTO>> getRoleUser(@PathVariable("id") Long id) {
        log.debug("REST request to get RoleUser : {}", id);
        Mono<RoleUserDTO> roleUserDTO = roleUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleUserDTO);
    }

    /**
     * {@code DELETE  /role-users/:id} : delete the "id" roleUser.
     *
     * @param id the id of the roleUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRoleUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete RoleUser : {}", id);
        return roleUserService
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
