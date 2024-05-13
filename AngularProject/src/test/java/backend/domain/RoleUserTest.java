package backend.domain;

import static backend.domain.RoleTestSamples.*;
import static backend.domain.RoleUserTestSamples.*;
import static backend.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import backend.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleUser.class);
        RoleUser roleUser1 = getRoleUserSample1();
        RoleUser roleUser2 = new RoleUser();
        assertThat(roleUser1).isNotEqualTo(roleUser2);

        roleUser2.setId(roleUser1.getId());
        assertThat(roleUser1).isEqualTo(roleUser2);

        roleUser2 = getRoleUserSample2();
        assertThat(roleUser1).isNotEqualTo(roleUser2);
    }

    @Test
    void userIdTest() throws Exception {
        RoleUser roleUser = getRoleUserRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        roleUser.setUserId(userBack);
        assertThat(roleUser.getUserId()).isEqualTo(userBack);

        roleUser.userId(null);
        assertThat(roleUser.getUserId()).isNull();
    }

    @Test
    void roleIdTest() throws Exception {
        RoleUser roleUser = getRoleUserRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        roleUser.setRoleId(roleBack);
        assertThat(roleUser.getRoleId()).isEqualTo(roleBack);

        roleUser.roleId(null);
        assertThat(roleUser.getRoleId()).isNull();
    }
}
