package backend.domain;

import static backend.domain.RoleTestSamples.*;
import static backend.domain.RoleUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
        Role role1 = getRoleSample1();
        Role role2 = new Role();
        assertThat(role1).isNotEqualTo(role2);

        role2.setId(role1.getId());
        assertThat(role1).isEqualTo(role2);

        role2 = getRoleSample2();
        assertThat(role1).isNotEqualTo(role2);
    }

    @Test
    void roleUserTest() throws Exception {
        Role role = getRoleRandomSampleGenerator();
        RoleUser roleUserBack = getRoleUserRandomSampleGenerator();

        role.setRoleUser(roleUserBack);
        assertThat(role.getRoleUser()).isEqualTo(roleUserBack);
        assertThat(roleUserBack.getRoleId()).isEqualTo(role);

        role.roleUser(null);
        assertThat(role.getRoleUser()).isNull();
        assertThat(roleUserBack.getRoleId()).isNull();
    }
}
