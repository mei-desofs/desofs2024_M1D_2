package com.isep.desofs.web.rest;

import com.isep.desofs.repository.RoleUserRepository;
import com.isep.desofs.service.RoleUserService;
import com.isep.desofs.service.dto.RoleUserDTO;
import com.isep.desofs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.isep.desofs.domain.RoleUser}.
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
    public ResponseEntity<RoleUserDTO> createRoleUser(@RequestBody RoleUserDTO roleUserDTO) throws URISyntaxException {
        log.debug("REST request to save RoleUser : {}", roleUserDTO);
        if (roleUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new roleUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roleUserDTO = roleUserService.save(roleUserDTO);
        return ResponseEntity.created(new URI("/api/role-users/" + roleUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, roleUserDTO.getId().toString()))
            .body(roleUserDTO);
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
    public ResponseEntity<RoleUserDTO> updateRoleUser(
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

        if (!roleUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roleUserDTO = roleUserService.update(roleUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleUserDTO.getId().toString()))
            .body(roleUserDTO);
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
    public ResponseEntity<RoleUserDTO> partialUpdateRoleUser(
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

        if (!roleUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleUserDTO> result = roleUserService.partialUpdate(roleUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roleUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /role-users} : get all the roleUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleUsers in body.
     */
    @GetMapping("")
    public List<RoleUserDTO> getAllRoleUsers() {
        log.debug("REST request to get all RoleUsers");
        return roleUserService.findAll();
    }

    /**
     * {@code GET  /role-users/:id} : get the "id" roleUser.
     *
     * @param id the id of the roleUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleUserDTO> getRoleUser(@PathVariable("id") Long id) {
        log.debug("REST request to get RoleUser : {}", id);
        Optional<RoleUserDTO> roleUserDTO = roleUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleUserDTO);
    }

    /**
     * {@code DELETE  /role-users/:id} : delete the "id" roleUser.
     *
     * @param id the id of the roleUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete RoleUser : {}", id);
        roleUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
