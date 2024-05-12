package com.isep.desofs.service.mapper;

import com.isep.desofs.domain.Role;
import com.isep.desofs.domain.RoleUser;
import com.isep.desofs.domain.User;
import com.isep.desofs.service.dto.RoleDTO;
import com.isep.desofs.service.dto.RoleUserDTO;
import com.isep.desofs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoleUser} and its DTO {@link RoleUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleUserMapper extends EntityMapper<RoleUserDTO, RoleUser> {
    @Mapping(target = "userId", source = "userId", qualifiedByName = "userId")
    @Mapping(target = "roleId", source = "roleId", qualifiedByName = "roleId")
    RoleUserDTO toDto(RoleUser s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);
}
