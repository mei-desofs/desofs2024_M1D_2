package com.isep.desofs.web.rest;

import com.isep.desofs.repository.PhotoRepository;
import com.isep.desofs.service.PhotoService;
import com.isep.desofs.service.dto.PhotoDTO;
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
 * REST controller for managing {@link com.isep.desofs.domain.Photo}.
 */
@RestController
@RequestMapping("/api/photos")
public class PhotoResource {

    private final Logger log = LoggerFactory.getLogger(PhotoResource.class);

    private static final String ENTITY_NAME = "photo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhotoService photoService;

    private final PhotoRepository photoRepository;

    public PhotoResource(PhotoService photoService, PhotoRepository photoRepository) {
        this.photoService = photoService;
        this.photoRepository = photoRepository;
    }

    /**
     * {@code POST  /photos} : Create a new photo.
     *
     * @param photoDTO the photoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new photoDTO, or with status {@code 400 (Bad Request)} if the photo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PhotoDTO> createPhoto(@RequestBody PhotoDTO photoDTO) throws URISyntaxException {
        log.debug("REST request to save Photo : {}", photoDTO);
        if (photoDTO.getId() != null) {
            throw new BadRequestAlertException("A new photo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        photoDTO = photoService.save(photoDTO);
        return ResponseEntity.created(new URI("/api/photos/" + photoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, photoDTO.getId().toString()))
            .body(photoDTO);
    }

    /**
     * {@code PUT  /photos/:id} : Updates an existing photo.
     *
     * @param id the id of the photoDTO to save.
     * @param photoDTO the photoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photoDTO,
     * or with status {@code 400 (Bad Request)} if the photoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the photoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PhotoDTO> updatePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhotoDTO photoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Photo : {}, {}", id, photoDTO);
        if (photoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, photoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!photoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        photoDTO = photoService.update(photoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, photoDTO.getId().toString()))
            .body(photoDTO);
    }

    /**
     * {@code PATCH  /photos/:id} : Partial updates given fields of an existing photo, field will ignore if it is null
     *
     * @param id the id of the photoDTO to save.
     * @param photoDTO the photoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated photoDTO,
     * or with status {@code 400 (Bad Request)} if the photoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the photoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the photoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhotoDTO> partialUpdatePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhotoDTO photoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Photo partially : {}, {}", id, photoDTO);
        if (photoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, photoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!photoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhotoDTO> result = photoService.partialUpdate(photoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, photoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /photos} : get all the photos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of photos in body.
     */
    @GetMapping("")
    public List<PhotoDTO> getAllPhotos() {
        log.debug("REST request to get all Photos");
        return photoService.findAll();
    }

    /**
     * {@code GET  /photos/:id} : get the "id" photo.
     *
     * @param id the id of the photoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the photoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PhotoDTO> getPhoto(@PathVariable("id") Long id) {
        log.debug("REST request to get Photo : {}", id);
        Optional<PhotoDTO> photoDTO = photoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(photoDTO);
    }

    /**
     * {@code DELETE  /photos/:id} : delete the "id" photo.
     *
     * @param id the id of the photoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable("id") Long id) {
        log.debug("REST request to delete Photo : {}", id);
        photoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
