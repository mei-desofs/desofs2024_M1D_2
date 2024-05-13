package backend.web.rest;

import static backend.domain.RoleAsserts.*;
import static backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import backend.IntegrationTest;
import backend.domain.Role;
import backend.repository.EntityManager;
import backend.repository.RoleRepository;
import backend.service.dto.RoleDTO;
import backend.service.mapper.RoleMapper;
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
 * Integration tests for the {@link RoleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RoleResourceIT {

    private static final String DEFAULT_NAME_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ROLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Role role;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role().nameRole(DEFAULT_NAME_ROLE);
        return role;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createUpdatedEntity(EntityManager em) {
        Role role = new Role().nameRole(UPDATED_NAME_ROLE);
        return role;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Role.class).block();
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
        role = createEntity(em);
    }

    @Test
    void createRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);
        var returnedRoleDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(RoleDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Role in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRole = roleMapper.toEntity(returnedRoleDTO);
        assertRoleUpdatableFieldsEquals(returnedRole, getPersistedRole(returnedRole));
    }

    @Test
    void createRoleWithExistingId() throws Exception {
        // Create the Role with an existing ID
        role.setId(1L);
        RoleDTO roleDTO = roleMapper.toDto(role);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRolesAsStream() {
        // Initialize the database
        roleRepository.save(role).block();

        List<Role> roleList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RoleDTO.class)
            .getResponseBody()
            .map(roleMapper::toEntity)
            .filter(role::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(roleList).isNotNull();
        assertThat(roleList).hasSize(1);
        Role testRole = roleList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertRoleAllPropertiesEquals(role, testRole);
        assertRoleUpdatableFieldsEquals(role, testRole);
    }

    @Test
    void getAllRoles() {
        // Initialize the database
        roleRepository.save(role).block();

        // Get all the roleList
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
            .value(hasItem(role.getId().intValue()))
            .jsonPath("$.[*].nameRole")
            .value(hasItem(DEFAULT_NAME_ROLE));
    }

    @Test
    void getRole() {
        // Initialize the database
        roleRepository.save(role).block();

        // Get the role
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, role.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(role.getId().intValue()))
            .jsonPath("$.nameRole")
            .value(is(DEFAULT_NAME_ROLE));
    }

    @Test
    void getNonExistingRole() {
        // Get the role
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRole() throws Exception {
        // Initialize the database
        roleRepository.save(role).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the role
        Role updatedRole = roleRepository.findById(role.getId()).block();
        updatedRole.nameRole(UPDATED_NAME_ROLE);
        RoleDTO roleDTO = roleMapper.toDto(updatedRole);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, roleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoleToMatchAllProperties(updatedRole);
    }

    @Test
    void putNonExistingRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, roleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.save(role).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.nameRole(UPDATED_NAME_ROLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Role in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRole, role), getPersistedRole(role));
    }

    @Test
    void fullUpdateRoleWithPatch() throws Exception {
        // Initialize the database
        roleRepository.save(role).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the role using partial update
        Role partialUpdatedRole = new Role();
        partialUpdatedRole.setId(role.getId());

        partialUpdatedRole.nameRole(UPDATED_NAME_ROLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRole.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedRole))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Role in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleUpdatableFieldsEquals(partialUpdatedRole, getPersistedRole(partialUpdatedRole));
    }

    @Test
    void patchNonExistingRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, roleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        role.setId(longCount.incrementAndGet());

        // Create the Role
        RoleDTO roleDTO = roleMapper.toDto(role);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(roleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Role in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRole() {
        // Initialize the database
        roleRepository.save(role).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the role
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, role.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roleRepository.count().block();
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

    protected Role getPersistedRole(Role role) {
        return roleRepository.findById(role.getId()).block();
    }

    protected void assertPersistedRoleToMatchAllProperties(Role expectedRole) {
        // Test fails because reactive api returns an empty object instead of null
        // assertRoleAllPropertiesEquals(expectedRole, getPersistedRole(expectedRole));
        assertRoleUpdatableFieldsEquals(expectedRole, getPersistedRole(expectedRole));
    }

    protected void assertPersistedRoleToMatchUpdatableProperties(Role expectedRole) {
        // Test fails because reactive api returns an empty object instead of null
        // assertRoleAllUpdatablePropertiesEquals(expectedRole, getPersistedRole(expectedRole));
        assertRoleUpdatableFieldsEquals(expectedRole, getPersistedRole(expectedRole));
    }
}
