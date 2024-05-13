package backend.service.mapper;

import static backend.domain.RoleUserAsserts.*;
import static backend.domain.RoleUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleUserMapperTest {

    private RoleUserMapper roleUserMapper;

    @BeforeEach
    void setUp() {
        roleUserMapper = new RoleUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRoleUserSample1();
        var actual = roleUserMapper.toEntity(roleUserMapper.toDto(expected));
        assertRoleUserAllPropertiesEquals(expected, actual);
    }
}
