package backend.service.mapper;

import backend.domain.Role;
import backend.domain.RoleUser;
import backend.domain.User;
import backend.service.dto.RoleDTO;
import backend.service.dto.RoleUserDTO;
import backend.service.dto.UserDTO;
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
