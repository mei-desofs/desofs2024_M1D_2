package com.isep.desofs.domain;

import static com.isep.desofs.domain.PortfolioTestSamples.*;
import static com.isep.desofs.domain.RoleUserTestSamples.*;
import static com.isep.desofs.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.isep.desofs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = getUserSample1();
        User user2 = new User();
        assertThat(user1).isNotEqualTo(user2);

        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);

        user2 = getUserSample2();
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void portfolioTest() throws Exception {
        User user = getUserRandomSampleGenerator();
        Portfolio portfolioBack = getPortfolioRandomSampleGenerator();

        user.setPortfolio(portfolioBack);
        assertThat(user.getPortfolio()).isEqualTo(portfolioBack);

        user.portfolio(null);
        assertThat(user.getPortfolio()).isNull();
    }

    @Test
    void roleUserTest() throws Exception {
        User user = getUserRandomSampleGenerator();
        RoleUser roleUserBack = getRoleUserRandomSampleGenerator();

        user.setRoleUser(roleUserBack);
        assertThat(user.getRoleUser()).isEqualTo(roleUserBack);
        assertThat(roleUserBack.getUserId()).isEqualTo(user);

        user.roleUser(null);
        assertThat(user.getRoleUser()).isNull();
        assertThat(roleUserBack.getUserId()).isNull();
    }
}
