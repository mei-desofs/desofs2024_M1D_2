package backend.web.rest;

import backend.repository.PhotoRepository;
import backend.service.PhotoService;
import backend.service.dto.PhotoDTO;
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
 * REST controller for managing {@link backend.domain.Photo}.
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
    public Mono<ResponseEntity<PhotoDTO>> createPhoto(@RequestBody PhotoDTO photoDTO) throws URISyntaxException {
        log.debug("REST request to save Photo : {}", photoDTO);
        if (photoDTO.getId() != null) {
            throw new BadRequestAlertException("A new photo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return photoService
            .save(photoDTO)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/photos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<PhotoDTO>> updatePhoto(
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

        return photoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return photoService
                    .update(photoDTO)
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
    public Mono<ResponseEntity<PhotoDTO>> partialUpdatePhoto(
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

        return photoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PhotoDTO> result = photoService.partialUpdate(photoDTO);

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
     * {@code GET  /photos} : get all the photos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of photos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<PhotoDTO>> getAllPhotos() {
        log.debug("REST request to get all Photos");
        return photoService.findAll().collectList();
    }

    /**
     * {@code GET  /photos} : get all the photos as a stream.
     * @return the {@link Flux} of photos.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PhotoDTO> getAllPhotosAsStream() {
        log.debug("REST request to get all Photos as a stream");
        return photoService.findAll();
    }

    /**
     * {@code GET  /photos/:id} : get the "id" photo.
     *
     * @param id the id of the photoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the photoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PhotoDTO>> getPhoto(@PathVariable("id") Long id) {
        log.debug("REST request to get Photo : {}", id);
        Mono<PhotoDTO> photoDTO = photoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(photoDTO);
    }

    /**
     * {@code DELETE  /photos/:id} : delete the "id" photo.
     *
     * @param id the id of the photoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePhoto(@PathVariable("id") Long id) {
        log.debug("REST request to delete Photo : {}", id);
        return photoService
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
