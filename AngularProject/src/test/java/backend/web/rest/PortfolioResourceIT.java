package backend.web.rest;

import static backend.domain.PortfolioAsserts.*;
import static backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import backend.IntegrationTest;
import backend.domain.Portfolio;
import backend.repository.EntityManager;
import backend.repository.PortfolioRepository;
import backend.service.dto.PortfolioDTO;
import backend.service.mapper.PortfolioMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PortfolioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PortfolioResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/portfolios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioMapper portfolioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Portfolio portfolio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Portfolio createEntity(EntityManager em) {
        Portfolio portfolio = new Portfolio().date(DEFAULT_DATE).name(DEFAULT_NAME);
        return portfolio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Portfolio createUpdatedEntity(EntityManager em) {
        Portfolio portfolio = new Portfolio().date(UPDATED_DATE).name(UPDATED_NAME);
        return portfolio;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Portfolio.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        portfolio = createEntity(em);
    }

    @Test
    void createPortfolio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);
        var returnedPortfolioDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PortfolioDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Portfolio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPortfolio = portfolioMapper.toEntity(returnedPortfolioDTO);
        assertPortfolioUpdatableFieldsEquals(returnedPortfolio, getPersistedPortfolio(returnedPortfolio));
    }

    @Test
    void createPortfolioWithExistingId() throws Exception {
        // Create the Portfolio with an existing ID
        portfolio.setId(1L);
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Portfolio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPortfoliosAsStream() {
        // Initialize the database
        portfolioRepository.save(portfolio).block();

        List<Portfolio> portfolioList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PortfolioDTO.class)
            .getResponseBody()
            .map(portfolioMapper::toEntity)
            .filter(portfolio::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(portfolioList).isNotNull();
        assertThat(portfolioList).hasSize(1);
        Portfolio testPortfolio = portfolioList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertPortfolioAllPropertiesEquals(portfolio, testPortfolio);
        assertPortfolioUpdatableFieldsEquals(portfolio, testPortfolio);
    }

    @Test
    void getAllPortfolios() {
        // Initialize the database
        portfolioRepository.save(portfolio).block();

        // Get all the portfolioList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(portfolio.getId().intValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getPortfolio() {
        // Initialize the database
        portfolioRepository.save(portfolio).block();

        // Get the portfolio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, portfolio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(portfolio.getId().intValue()))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingPortfolio() {
        // Get the portfolio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.save(portfolio).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the portfolio
        Portfolio updatedPortfolio = portfolioRepository.findById(portfolio.getId()).block();
        updatedPortfolio.date(UPDATED_DATE).name(UPDATED_NAME);
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(updatedPortfolio);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, portfolioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Portfolio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPortfolioToMatchAllProperties(updatedPortfolio);
    }

    @Test
    void putNonExistingPortfolio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        portfolio.setId(longCount.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, portfolioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Portfolio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPortfolio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        portfolio.setId(longCount.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Portfolio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPortfolio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        portfolio.setId(longCount.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Portfolio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePortfolioWithPatch() throws Exception {
        // Initialize the database
        portfolioRepository.save(portfolio).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the portfolio using partial update
        Portfolio partialUpdatedPortfolio = new Portfolio();
        partialUpdatedPortfolio.setId(portfolio.getId());

        partialUpdatedPortfolio.date(UPDATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPortfolio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPortfolio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Portfolio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPortfolioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPortfolio, portfolio),
            getPersistedPortfolio(portfolio)
        );
    }

    @Test
    void fullUpdatePortfolioWithPatch() throws Exception {
        // Initialize the database
        portfolioRepository.save(portfolio).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the portfolio using partial update
        Portfolio partialUpdatedPortfolio = new Portfolio();
        partialUpdatedPortfolio.setId(portfolio.getId());

        partialUpdatedPortfolio.date(UPDATED_DATE).name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPortfolio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPortfolio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Portfolio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPortfolioUpdatableFieldsEquals(partialUpdatedPortfolio, getPersistedPortfolio(partialUpdatedPortfolio));
    }

    @Test
    void patchNonExistingPortfolio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        portfolio.setId(longCount.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, portfolioDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Portfolio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPortfolio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        portfolio.setId(longCount.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Portfolio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPortfolio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        portfolio.setId(longCount.incrementAndGet());

        // Create the Portfolio
        PortfolioDTO portfolioDTO = portfolioMapper.toDto(portfolio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(portfolioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Portfolio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePortfolio() {
        // Initialize the database
        portfolioRepository.save(portfolio).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the portfolio
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, portfolio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return portfolioRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Portfolio getPersistedPortfolio(Portfolio portfolio) {
        return portfolioRepository.findById(portfolio.getId()).block();
    }

    protected void assertPersistedPortfolioToMatchAllProperties(Portfolio expectedPortfolio) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPortfolioAllPropertiesEquals(expectedPortfolio, getPersistedPortfolio(expectedPortfolio));
        assertPortfolioUpdatableFieldsEquals(expectedPortfolio, getPersistedPortfolio(expectedPortfolio));
    }

    protected void assertPersistedPortfolioToMatchUpdatableProperties(Portfolio expectedPortfolio) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPortfolioAllUpdatablePropertiesEquals(expectedPortfolio, getPersistedPortfolio(expectedPortfolio));
        assertPortfolioUpdatableFieldsEquals(expectedPortfolio, getPersistedPortfolio(expectedPortfolio));
    }
}
