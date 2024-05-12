package com.isep.desofs.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PhotoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPhotoAllPropertiesEquals(Photo expected, Photo actual) {
        assertPhotoAutoGeneratedPropertiesEquals(expected, actual);
        assertPhotoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPhotoAllUpdatablePropertiesEquals(Photo expected, Photo actual) {
        assertPhotoUpdatableFieldsEquals(expected, actual);
        assertPhotoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPhotoAutoGeneratedPropertiesEquals(Photo expected, Photo actual) {
        assertThat(expected)
            .as("Verify Photo auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPhotoUpdatableFieldsEquals(Photo expected, Photo actual) {
        assertThat(expected)
            .as("Verify Photo relevant properties")
            .satisfies(e -> assertThat(e.getPhoto()).as("check photo").isEqualTo(actual.getPhoto()))
            .satisfies(e -> assertThat(e.getPhotoContentType()).as("check photo contenty type").isEqualTo(actual.getPhotoContentType()))
            .satisfies(e -> assertThat(e.getDate()).as("check date").isEqualTo(actual.getDate()))
            .satisfies(e -> assertThat(e.getState()).as("check state").isEqualTo(actual.getState()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPhotoUpdatableRelationshipsEquals(Photo expected, Photo actual) {
        assertThat(expected)
            .as("Verify Photo relationships")
            .satisfies(e -> assertThat(e.getPortfolio()).as("check portfolio").isEqualTo(actual.getPortfolio()))
            .satisfies(e -> assertThat(e.getCart()).as("check cart").isEqualTo(actual.getCart()));
    }
}
