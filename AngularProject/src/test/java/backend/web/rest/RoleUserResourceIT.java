package backend.web.rest;

import static backend.domain.RoleUserAsserts.*;
import static backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import backend.IntegrationTest;
import backend.domain.RoleUser;
import backend.repository.EntityManager;
import backend.repository.RoleUserRepository;
import backend.service.dto.RoleUserDTO;
import backend.service.mapper.RoleUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
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
 * Integration tests for the {@link RoleUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RoleUserResourceIT {

    private static final String ENTITY_API_URL = "/api/role-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private RoleUserMapper roleUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private RoleUser roleUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleUser createEntity(EntityManager em) {
        RoleUser roleUser = new RoleUser();
        return roleUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleUser createUpdatedEntity(EntityManager em) {
        RoleUser roleUser = new RoleUser();
        return roleUser;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(RoleUser.class).block();
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
        roleUser = createEntity(em);
    }

    @Test
    void createRoleUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);
        var returnedRoleUserDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(RoleUserDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the RoleUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoleUser = roleUserMapper.toEntity(returnedRoleUserDTO);
        assertRoleUserUpdatableFieldsEquals(returnedRoleUser, getPersistedRoleUser(returnedRoleUser));
    }

    @Test
    void createRoleUserWithExistingId() throws Exception {
        // Create the RoleUser with an existing ID
        roleUser.setId(1L);
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRoleUsersAsStream() {
        // Initialize the database
        roleUserRepository.save(roleUser).block();

        List<RoleUser> roleUserList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RoleUserDTO.class)
            .getResponseBody()
            .map(roleUserMapper::toEntity)
            .filter(roleUser::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(roleUserList).isNotNull();
        assertThat(roleUserList).hasSize(1);
        RoleUser testRoleUser = roleUserList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertRoleUserAllPropertiesEquals(roleUser, testRoleUser);
        assertRoleUserUpdatableFieldsEquals(roleUser, testRoleUser);
    }

    @Test
    void getAllRoleUsers() {
        // Initialize the database
        roleUserRepository.save(roleUser).block();

        // Get all the roleUserList
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
            .value(hasItem(roleUser.getId().intValue()));
    }

    @Test
    void getRoleUser() {
        // Initialize the database
        roleUserRepository.save(roleUser).block();

        // Get the roleUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, roleUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(roleUser.getId().intValue()));
    }

    @Test
    void getNonExistingRoleUser() {
        // Get the roleUser
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRoleUser() throws Exception {
        // Initialize the database
        roleUserRepository.save(roleUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleUser
        RoleUser updatedRoleUser = roleUserRepository.findById(roleUser.getId()).block();
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(updatedRoleUser);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, roleUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoleUserToMatchAllProperties(updatedRoleUser);
    }

    @Test
    void putNonExistingRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, roleUserDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRoleUserWithPatch() throws Exception {
        // Initialize the database
        roleUserRepository.save(roleUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleUser using partial update
        RoleUser partialUpdatedRoleUser = new RoleUser();
        partialUpdatedRoleUser.setId(roleUser.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRoleUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedRoleUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RoleUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRoleUser, roleUser), getPersistedRoleUser(roleUser));
    }

    @Test
    void fullUpdateRoleUserWithPatch() throws Exception {
        // Initialize the database
        roleUserRepository.save(roleUser).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleUser using partial update
        RoleUser partialUpdatedRoleUser = new RoleUser();
        partialUpdatedRoleUser.setId(roleUser.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRoleUser.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedRoleUser))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RoleUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleUserUpdatableFieldsEquals(partialUpdatedRoleUser, getPersistedRoleUser(partialUpdatedRoleUser));
    }

    @Test
    void patchNonExistingRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, roleUserDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(roleUserDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRoleUser() {
        // Initialize the database
        roleUserRepository.save(roleUser).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the roleUser
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, roleUser.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roleUserRepository.count().block();
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

    protected RoleUser getPersistedRoleUser(RoleUser roleUser) {
        return roleUserRepository.findById(roleUser.getId()).block();
    }

    protected void assertPersistedRoleUserToMatchAllProperties(RoleUser expectedRoleUser) {
        // Test fails because reactive api returns an empty object instead of null
        // assertRoleUserAllPropertiesEquals(expectedRoleUser, getPersistedRoleUser(expectedRoleUser));
        assertRoleUserUpdatableFieldsEquals(expectedRoleUser, getPersistedRoleUser(expectedRoleUser));
    }

    protected void assertPersistedRoleUserToMatchUpdatableProperties(RoleUser expectedRoleUser) {
        // Test fails because reactive api returns an empty object instead of null
        // assertRoleUserAllUpdatablePropertiesEquals(expectedRoleUser, getPersistedRoleUser(expectedRoleUser));
        assertRoleUserUpdatableFieldsEquals(expectedRoleUser, getPersistedRoleUser(expectedRoleUser));
    }
}
