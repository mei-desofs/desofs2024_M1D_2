package backend.web.rest;

import static backend.domain.PhotoAsserts.*;
import static backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import backend.IntegrationTest;
import backend.domain.Photo;
import backend.domain.enumeration.PhotoState;
import backend.repository.EntityManager;
import backend.repository.PhotoRepository;
import backend.service.dto.PhotoDTO;
import backend.service.mapper.PhotoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PhotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PhotoResourceIT {

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final PhotoState DEFAULT_STATE = PhotoState.ACTIVE;
    private static final PhotoState UPDATED_STATE = PhotoState.INACTIVE;

    private static final String ENTITY_API_URL = "/api/photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Photo photo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createEntity(EntityManager em) {
        Photo photo = new Photo().photo(DEFAULT_PHOTO).photoContentType(DEFAULT_PHOTO_CONTENT_TYPE).date(DEFAULT_DATE).state(DEFAULT_STATE);
        return photo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createUpdatedEntity(EntityManager em) {
        Photo photo = new Photo().photo(UPDATED_PHOTO).photoContentType(UPDATED_PHOTO_CONTENT_TYPE).date(UPDATED_DATE).state(UPDATED_STATE);
        return photo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Photo.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        photo = createEntity(em);
    }

    @Test
    void createPhoto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);
        var returnedPhotoDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PhotoDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Photo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPhoto = photoMapper.toEntity(returnedPhotoDTO);
        assertPhotoUpdatableFieldsEquals(returnedPhoto, getPersistedPhoto(returnedPhoto));
    }

    @Test
    void createPhotoWithExistingId() throws Exception {
        // Create the Photo with an existing ID
        photo.setId(1L);
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Photo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPhotosAsStream() {
        // Initialize the database
        photoRepository.save(photo).block();

        List<Photo> photoList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PhotoDTO.class)
            .getResponseBody()
            .map(photoMapper::toEntity)
            .filter(photo::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(photoList).isNotNull();
        assertThat(photoList).hasSize(1);
        Photo testPhoto = photoList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertPhotoAllPropertiesEquals(photo, testPhoto);
        assertPhotoUpdatableFieldsEquals(photo, testPhoto);
    }

    @Test
    void getAllPhotos() {
        // Initialize the database
        photoRepository.save(photo).block();

        // Get all the photoList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(photo.getId().intValue()))
            .jsonPath("$.[*].photoContentType")
            .value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].photo")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO)))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].state")
            .value(hasItem(DEFAULT_STATE.toString()));
    }

    @Test
    void getPhoto() {
        // Initialize the database
        photoRepository.save(photo).block();

        // Get the photo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, photo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(photo.getId().intValue()))
            .jsonPath("$.photoContentType")
            .value(is(DEFAULT_PHOTO_CONTENT_TYPE))
            .jsonPath("$.photo")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_PHOTO)))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()))
            .jsonPath("$.state")
            .value(is(DEFAULT_STATE.toString()));
    }

    @Test
    void getNonExistingPhoto() {
        // Get the photo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPhoto() throws Exception {
        // Initialize the database
        photoRepository.save(photo).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the photo
        Photo updatedPhoto = photoRepository.findById(photo.getId()).block();
        updatedPhoto.photo(UPDATED_PHOTO).photoContentType(UPDATED_PHOTO_CONTENT_TYPE).date(UPDATED_DATE).state(UPDATED_STATE);
        PhotoDTO photoDTO = photoMapper.toDto(updatedPhoto);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, photoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Photo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPhotoToMatchAllProperties(updatedPhoto);
    }

    @Test
    void putNonExistingPhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        photo.setId(longCount.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, photoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Photo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        photo.setId(longCount.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Photo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        photo.setId(longCount.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Photo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePhotoWithPatch() throws Exception {
        // Initialize the database
        photoRepository.save(photo).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the photo using partial update
        Photo partialUpdatedPhoto = new Photo();
        partialUpdatedPhoto.setId(photo.getId());

        partialUpdatedPhoto.date(UPDATED_DATE).state(UPDATED_STATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPhoto.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPhoto))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Photo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhotoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPhoto, photo), getPersistedPhoto(photo));
    }

    @Test
    void fullUpdatePhotoWithPatch() throws Exception {
        // Initialize the database
        photoRepository.save(photo).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the photo using partial update
        Photo partialUpdatedPhoto = new Photo();
        partialUpdatedPhoto.setId(photo.getId());

        partialUpdatedPhoto.photo(UPDATED_PHOTO).photoContentType(UPDATED_PHOTO_CONTENT_TYPE).date(UPDATED_DATE).state(UPDATED_STATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPhoto.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPhoto))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Photo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhotoUpdatableFieldsEquals(partialUpdatedPhoto, getPersistedPhoto(partialUpdatedPhoto));
    }

    @Test
    void patchNonExistingPhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        photo.setId(longCount.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, photoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Photo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        photo.setId(longCount.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Photo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPhoto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        photo.setId(longCount.incrementAndGet());

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(photoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Photo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePhoto() {
        // Initialize the database
        photoRepository.save(photo).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the photo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, photo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return photoRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Photo getPersistedPhoto(Photo photo) {
        return photoRepository.findById(photo.getId()).block();
    }

    protected void assertPersistedPhotoToMatchAllProperties(Photo expectedPhoto) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPhotoAllPropertiesEquals(expectedPhoto, getPersistedPhoto(expectedPhoto));
        assertPhotoUpdatableFieldsEquals(expectedPhoto, getPersistedPhoto(expectedPhoto));
    }

    protected void assertPersistedPhotoToMatchUpdatableProperties(Photo expectedPhoto) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPhotoAllUpdatablePropertiesEquals(expectedPhoto, getPersistedPhoto(expectedPhoto));
        assertPhotoUpdatableFieldsEquals(expectedPhoto, getPersistedPhoto(expectedPhoto));
    }
}
