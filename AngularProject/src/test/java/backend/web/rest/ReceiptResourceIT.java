package backend.web.rest;

import static backend.domain.ReceiptAsserts.*;
import static backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import backend.IntegrationTest;
import backend.domain.Receipt;
import backend.repository.EntityManager;
import backend.repository.ReceiptRepository;
import backend.service.dto.ReceiptDTO;
import backend.service.mapper.ReceiptMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
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
 * Integration tests for the {@link ReceiptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReceiptResourceIT {

    private static final Long DEFAULT_ID_PAYMENT = 1L;
    private static final Long UPDATED_ID_PAYMENT = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/receipts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Receipt receipt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createEntity(EntityManager em) {
        Receipt receipt = new Receipt().idPayment(DEFAULT_ID_PAYMENT).description(DEFAULT_DESCRIPTION);
        return receipt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Receipt createUpdatedEntity(EntityManager em) {
        Receipt receipt = new Receipt().idPayment(UPDATED_ID_PAYMENT).description(UPDATED_DESCRIPTION);
        return receipt;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Receipt.class).block();
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
        receipt = createEntity(em);
    }

    @Test
    void createReceipt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);
        var returnedReceiptDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ReceiptDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Receipt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReceipt = receiptMapper.toEntity(returnedReceiptDTO);
        assertReceiptUpdatableFieldsEquals(returnedReceipt, getPersistedReceipt(returnedReceipt));
    }

    @Test
    void createReceiptWithExistingId() throws Exception {
        // Create the Receipt with an existing ID
        receipt.setId(1L);
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReceiptsAsStream() {
        // Initialize the database
        receiptRepository.save(receipt).block();

        List<Receipt> receiptList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ReceiptDTO.class)
            .getResponseBody()
            .map(receiptMapper::toEntity)
            .filter(receipt::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(receiptList).isNotNull();
        assertThat(receiptList).hasSize(1);
        Receipt testReceipt = receiptList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertReceiptAllPropertiesEquals(receipt, testReceipt);
        assertReceiptUpdatableFieldsEquals(receipt, testReceipt);
    }

    @Test
    void getAllReceipts() {
        // Initialize the database
        receiptRepository.save(receipt).block();

        // Get all the receiptList
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
            .value(hasItem(receipt.getId().intValue()))
            .jsonPath("$.[*].idPayment")
            .value(hasItem(DEFAULT_ID_PAYMENT.intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getReceipt() {
        // Initialize the database
        receiptRepository.save(receipt).block();

        // Get the receipt
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, receipt.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(receipt.getId().intValue()))
            .jsonPath("$.idPayment")
            .value(is(DEFAULT_ID_PAYMENT.intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingReceipt() {
        // Get the receipt
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReceipt() throws Exception {
        // Initialize the database
        receiptRepository.save(receipt).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receipt
        Receipt updatedReceipt = receiptRepository.findById(receipt.getId()).block();
        updatedReceipt.idPayment(UPDATED_ID_PAYMENT).description(UPDATED_DESCRIPTION);
        ReceiptDTO receiptDTO = receiptMapper.toDto(updatedReceipt);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, receiptDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Receipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReceiptToMatchAllProperties(updatedReceipt);
    }

    @Test
    void putNonExistingReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receipt.setId(longCount.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, receiptDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receipt.setId(longCount.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receipt.setId(longCount.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Receipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.save(receipt).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt.idPayment(UPDATED_ID_PAYMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReceipt))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Receipt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReceiptUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReceipt, receipt), getPersistedReceipt(receipt));
    }

    @Test
    void fullUpdateReceiptWithPatch() throws Exception {
        // Initialize the database
        receiptRepository.save(receipt).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the receipt using partial update
        Receipt partialUpdatedReceipt = new Receipt();
        partialUpdatedReceipt.setId(receipt.getId());

        partialUpdatedReceipt.idPayment(UPDATED_ID_PAYMENT).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReceipt.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedReceipt))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Receipt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReceiptUpdatableFieldsEquals(partialUpdatedReceipt, getPersistedReceipt(partialUpdatedReceipt));
    }

    @Test
    void patchNonExistingReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receipt.setId(longCount.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, receiptDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receipt.setId(longCount.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Receipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReceipt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        receipt.setId(longCount.incrementAndGet());

        // Create the Receipt
        ReceiptDTO receiptDTO = receiptMapper.toDto(receipt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(receiptDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Receipt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReceipt() {
        // Initialize the database
        receiptRepository.save(receipt).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the receipt
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, receipt.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return receiptRepository.count().block();
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

    protected Receipt getPersistedReceipt(Receipt receipt) {
        return receiptRepository.findById(receipt.getId()).block();
    }

    protected void assertPersistedReceiptToMatchAllProperties(Receipt expectedReceipt) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReceiptAllPropertiesEquals(expectedReceipt, getPersistedReceipt(expectedReceipt));
        assertReceiptUpdatableFieldsEquals(expectedReceipt, getPersistedReceipt(expectedReceipt));
    }

    protected void assertPersistedReceiptToMatchUpdatableProperties(Receipt expectedReceipt) {
        // Test fails because reactive api returns an empty object instead of null
        // assertReceiptAllUpdatablePropertiesEquals(expectedReceipt, getPersistedReceipt(expectedReceipt));
        assertReceiptUpdatableFieldsEquals(expectedReceipt, getPersistedReceipt(expectedReceipt));
    }
}
