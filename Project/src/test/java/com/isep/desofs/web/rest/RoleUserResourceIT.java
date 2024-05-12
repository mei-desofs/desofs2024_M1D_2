package com.isep.desofs.web.rest;

import static com.isep.desofs.domain.RoleUserAsserts.*;
import static com.isep.desofs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.desofs.IntegrationTest;
import com.isep.desofs.domain.RoleUser;
import com.isep.desofs.repository.RoleUserRepository;
import com.isep.desofs.service.dto.RoleUserDTO;
import com.isep.desofs.service.mapper.RoleUserMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoleUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restRoleUserMockMvc;

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

    @BeforeEach
    public void initTest() {
        roleUser = createEntity(em);
    }

    @Test
    @Transactional
    void createRoleUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);
        var returnedRoleUserDTO = om.readValue(
            restRoleUserMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleUserDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RoleUserDTO.class
        );

        // Validate the RoleUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRoleUser = roleUserMapper.toEntity(returnedRoleUserDTO);
        assertRoleUserUpdatableFieldsEquals(returnedRoleUser, getPersistedRoleUser(returnedRoleUser));
    }

    @Test
    @Transactional
    void createRoleUserWithExistingId() throws Exception {
        // Create the RoleUser with an existing ID
        roleUser.setId(1L);
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleUserMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoleUsers() throws Exception {
        // Initialize the database
        roleUserRepository.saveAndFlush(roleUser);

        // Get all the roleUserList
        restRoleUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleUser.getId().intValue())));
    }

    @Test
    @Transactional
    void getRoleUser() throws Exception {
        // Initialize the database
        roleUserRepository.saveAndFlush(roleUser);

        // Get the roleUser
        restRoleUserMockMvc
            .perform(get(ENTITY_API_URL_ID, roleUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roleUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRoleUser() throws Exception {
        // Get the roleUser
        restRoleUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoleUser() throws Exception {
        // Initialize the database
        roleUserRepository.saveAndFlush(roleUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleUser
        RoleUser updatedRoleUser = roleUserRepository.findById(roleUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoleUser are not directly saved in db
        em.detach(updatedRoleUser);
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(updatedRoleUser);

        restRoleUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roleUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRoleUserToMatchAllProperties(updatedRoleUser);
    }

    @Test
    @Transactional
    void putNonExistingRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleUserDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roleUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(roleUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleUserMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(roleUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleUserWithPatch() throws Exception {
        // Initialize the database
        roleUserRepository.saveAndFlush(roleUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleUser using partial update
        RoleUser partialUpdatedRoleUser = new RoleUser();
        partialUpdatedRoleUser.setId(roleUser.getId());

        restRoleUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoleUser))
            )
            .andExpect(status().isOk());

        // Validate the RoleUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRoleUser, roleUser), getPersistedRoleUser(roleUser));
    }

    @Test
    @Transactional
    void fullUpdateRoleUserWithPatch() throws Exception {
        // Initialize the database
        roleUserRepository.saveAndFlush(roleUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the roleUser using partial update
        RoleUser partialUpdatedRoleUser = new RoleUser();
        partialUpdatedRoleUser.setId(roleUser.getId());

        restRoleUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRoleUser))
            )
            .andExpect(status().isOk());

        // Validate the RoleUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRoleUserUpdatableFieldsEquals(partialUpdatedRoleUser, getPersistedRoleUser(partialUpdatedRoleUser));
    }

    @Test
    @Transactional
    void patchNonExistingRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleUserDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roleUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(roleUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoleUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        roleUser.setId(longCount.incrementAndGet());

        // Create the RoleUser
        RoleUserDTO roleUserDTO = roleUserMapper.toDto(roleUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleUserMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(roleUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoleUser() throws Exception {
        // Initialize the database
        roleUserRepository.saveAndFlush(roleUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the roleUser
        restRoleUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, roleUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return roleUserRepository.count();
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
        return roleUserRepository.findById(roleUser.getId()).orElseThrow();
    }

    protected void assertPersistedRoleUserToMatchAllProperties(RoleUser expectedRoleUser) {
        assertRoleUserAllPropertiesEquals(expectedRoleUser, getPersistedRoleUser(expectedRoleUser));
    }

    protected void assertPersistedRoleUserToMatchUpdatableProperties(RoleUser expectedRoleUser) {
        assertRoleUserAllUpdatablePropertiesEquals(expectedRoleUser, getPersistedRoleUser(expectedRoleUser));
    }
}
