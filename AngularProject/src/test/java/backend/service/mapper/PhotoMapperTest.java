package backend.service.mapper;

import static backend.domain.PhotoAsserts.*;
import static backend.domain.PhotoTestSamples.*;

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
