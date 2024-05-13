package backend.web.rest;

import static backend.domain.UserAsserts.*;
import static backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import backend.IntegrationTest;
import backend.domain.User;
import backend.repository.EntityManager;
import backend.repository.UserRepository;
import backend.service.dto.UserDTO;
import backend.service.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
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
 * Integration tests for the {@link UserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PROFILE_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private User user;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User()
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD)
            .address(DEFAULT_ADDRESS)
            .contact(DEFAULT_CONTACT)
            .profilePhoto(DEFAULT_PROFILE_PHOTO)
            .profilePhotoContentType(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE);
        return user;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createUpdatedEntity(EntityManager em) {
        User user = new User()
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .address(UPDATED_ADDRESS)
            .contact(UPDATED_CONTACT)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        return user;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(User.class).block();
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
        user = createEntity(em);
    }

    @Test
    void createUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the User
        UserDTO userDTO = userMapper.toDto(user);
        var returnedUserDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(UserDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the User in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUser = userMapper.toEntity(returnedUserDTO);
        assertUserUpdatableFieldsEquals(returnedUser, getPersistedUser(returnedUser));
    }

    @Test
    void createUserWithExistingId() throws Exception {
        // Create the User with an existing ID
        user.setId(1L);
        UserDTO userDTO = userMapper.toDto(user);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllUsersAsStream() {
        // Initialize the database
        userRepository.save(user).block();

        List<User> userList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UserDTO.class)
            .getResponseBody()
            .map(userMapper::toEntity)
            .filter(user::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(userList).isNotNull();
        assertThat(userList).hasSize(1);
        User testUser = userList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertUserAllPropertiesEquals(user, testUser);
        assertUserUpdatableFieldsEquals(user, testUser);
    }

    @Test
    void getAllUsers() {
        // Initialize the database
        userRepository.save(user).block();

        // Get all the userList
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
            .value(hasItem(user.getId().intValue()))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].password")
            .value(hasItem(DEFAULT_PASSWORD))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS))
            .jsonPath("$.[*].contact")
            .value(hasItem(DEFAULT_CONTACT))
            .jsonPath("$.[*].profilePhotoContentType")
            .value(hasItem(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].profilePhoto")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_PHOTO)));
    }

    @Test
    void getUser() {
        // Initialize the database
        userRepository.save(user).block();

        // Get the user
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, user.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(user.getId().intValue()))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.password")
            .value(is(DEFAULT_PASSWORD))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS))
            .jsonPath("$.contact")
            .value(is(DEFAULT_CONTACT))
            .jsonPath("$.profilePhotoContentType")
            .value(is(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE))
            .jsonPath("$.profilePhoto")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_PHOTO)));
    }

    @Test
    void getNonExistingUser() {
        // Get the user
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUser() throws Exception {
        // Initialize the database
        userRepository.save(user).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user
        User updatedUser = userRepository.findById(user.getId()).block();
        updatedUser
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .address(UPDATED_ADDRESS)
            .contact(UPDATED_CONTACT)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        UserDTO userDTO = userMapper.toDto(updatedUser);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserToMatchAllProperties(updatedUser);
    }

    @Test
    void putNonExistingUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserWithPatch() throws Exception {
        // Initialize the database
        userRepository.save(user).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user using partial update
        User partialUpdatedUser = new User();
        partialUpdatedUser.setId(user.getId());

        partialUpdatedUser.email(UPDATED_EMAIL).password(UPDATED_PASSWORD);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the User in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUser, user), getPersistedUser(user));
    }

    @Test
    void fullUpdateUserWithPatch() throws Exception {
        // Initialize the database
        userRepository.save(user).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the user using partial update
        User partialUpdatedUser = new User();
        partialUpdatedUser.setId(user.getId());

        partialUpdatedUser
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD)
            .address(UPDATED_ADDRESS)
            .contact(UPDATED_CONTACT)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the User in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserUpdatableFieldsEquals(partialUpdatedUser, getPersistedUser(partialUpdatedUser));
    }

    @Test
    void patchNonExistingUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        user.setId(longCount.incrementAndGet());

        // Create the User
        UserDTO userDTO = userMapper.toDto(user);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(userDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the User in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUser() {
        // Initialize the database
        userRepository.save(user).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the user
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, user.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userRepository.count().block();
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

    protected User getPersistedUser(User user) {
        return userRepository.findById(user.getId()).block();
    }

    protected void assertPersistedUserToMatchAllProperties(User expectedUser) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserAllPropertiesEquals(expectedUser, getPersistedUser(expectedUser));
        assertUserUpdatableFieldsEquals(expectedUser, getPersistedUser(expectedUser));
    }

    protected void assertPersistedUserToMatchUpdatableProperties(User expectedUser) {
        // Test fails because reactive api returns an empty object instead of null
        // assertUserAllUpdatablePropertiesEquals(expectedUser, getPersistedUser(expectedUser));
        assertUserUpdatableFieldsEquals(expectedUser, getPersistedUser(expectedUser));
    }
}
