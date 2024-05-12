package com.isep.desofs.service.mapper;

import static com.isep.desofs.domain.RoleUserAsserts.*;
import static com.isep.desofs.domain.RoleUserTestSamples.*;

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
