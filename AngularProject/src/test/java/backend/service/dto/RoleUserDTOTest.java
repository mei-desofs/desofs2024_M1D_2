package backend.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleUserDTO.class);
        RoleUserDTO roleUserDTO1 = new RoleUserDTO();
        roleUserDTO1.setId(1L);
        RoleUserDTO roleUserDTO2 = new RoleUserDTO();
        assertThat(roleUserDTO1).isNotEqualTo(roleUserDTO2);
        roleUserDTO2.setId(roleUserDTO1.getId());
        assertThat(roleUserDTO1).isEqualTo(roleUserDTO2);
        roleUserDTO2.setId(2L);
        assertThat(roleUserDTO1).isNotEqualTo(roleUserDTO2);
        roleUserDTO1.setId(null);
        assertThat(roleUserDTO1).isNotEqualTo(roleUserDTO2);
    }
}
