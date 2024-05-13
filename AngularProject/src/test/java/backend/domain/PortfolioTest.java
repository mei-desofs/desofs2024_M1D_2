package backend.domain;

import static backend.domain.PortfolioTestSamples.*;
import static backend.domain.UserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import backend.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PortfolioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Portfolio.class);
        Portfolio portfolio1 = getPortfolioSample1();
        Portfolio portfolio2 = new Portfolio();
        assertThat(portfolio1).isNotEqualTo(portfolio2);

        portfolio2.setId(portfolio1.getId());
        assertThat(portfolio1).isEqualTo(portfolio2);

        portfolio2 = getPortfolioSample2();
        assertThat(portfolio1).isNotEqualTo(portfolio2);
    }

    @Test
    void userTest() throws Exception {
        Portfolio portfolio = getPortfolioRandomSampleGenerator();
        User userBack = getUserRandomSampleGenerator();

        portfolio.addUser(userBack);
        assertThat(portfolio.getUsers()).containsOnly(userBack);
        assertThat(userBack.getPortfolio()).isEqualTo(portfolio);

        portfolio.removeUser(userBack);
        assertThat(portfolio.getUsers()).doesNotContain(userBack);
        assertThat(userBack.getPortfolio()).isNull();

        portfolio.users(new HashSet<>(Set.of(userBack)));
        assertThat(portfolio.getUsers()).containsOnly(userBack);
        assertThat(userBack.getPortfolio()).isEqualTo(portfolio);

        portfolio.setUsers(new HashSet<>());
        assertThat(portfolio.getUsers()).doesNotContain(userBack);
        assertThat(userBack.getPortfolio()).isNull();
    }
}
