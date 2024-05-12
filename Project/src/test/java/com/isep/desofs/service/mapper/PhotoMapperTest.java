package com.isep.desofs.service.mapper;

import static com.isep.desofs.domain.PhotoAsserts.*;
import static com.isep.desofs.domain.PhotoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhotoMapperTest {

    private PhotoMapper photoMapper;

    @BeforeEach
    void setUp() {
        photoMapper = new PhotoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPhotoSample1();
        var actual = photoMapper.toEntity(photoMapper.toDto(expected));
        assertPhotoAllPropertiesEquals(expected, actual);
    }
}
