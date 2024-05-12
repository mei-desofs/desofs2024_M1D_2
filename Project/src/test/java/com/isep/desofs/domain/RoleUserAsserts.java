package com.isep.desofs.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleUserAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleUserAllPropertiesEquals(RoleUser expected, RoleUser actual) {
        assertRoleUserAutoGeneratedPropertiesEquals(expected, actual);
        assertRoleUserAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleUserAllUpdatablePropertiesEquals(RoleUser expected, RoleUser actual) {
        assertRoleUserUpdatableFieldsEquals(expected, actual);
        assertRoleUserUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleUserAutoGeneratedPropertiesEquals(RoleUser expected, RoleUser actual) {
        assertThat(expected)
            .as("Verify RoleUser auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleUserUpdatableFieldsEquals(RoleUser expected, RoleUser actual) {}

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleUserUpdatableRelationshipsEquals(RoleUser expected, RoleUser actual) {
        assertThat(expected)
            .as("Verify RoleUser relationships")
            .satisfies(e -> assertThat(e.getUserId()).as("check userId").isEqualTo(actual.getUserId()))
            .satisfies(e -> assertThat(e.getRoleId()).as("check roleId").isEqualTo(actual.getRoleId()));
    }
}
