package backend.web.rest;

import static backend.domain.PaymentAsserts.*;
import static backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import backend.IntegrationTest;
import backend.domain.Payment;
import backend.repository.EntityManager;
import backend.repository.PaymentRepository;
import backend.service.dto.PaymentDTO;
import backend.service.mapper.PaymentMapper;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaymentResourceIT {

    private static final Long DEFAULT_ID_CART = 1L;
    private static final Long UPDATED_ID_CART = 2L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment().idCart(DEFAULT_ID_CART).date(DEFAULT_DATE);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment().idCart(UPDATED_ID_CART).date(UPDATED_DATE);
        return payment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Payment.class).block();
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
        payment = createEntity(em);
    }

    @Test
    void createPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        var returnedPaymentDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(PaymentDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Payment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPayment = paymentMapper.toEntity(returnedPaymentDTO);
        assertPaymentUpdatableFieldsEquals(returnedPayment, getPersistedPayment(returnedPayment));
    }

    @Test
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPaymentsAsStream() {
        // Initialize the database
        paymentRepository.save(payment).block();

        List<Payment> paymentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PaymentDTO.class)
            .getResponseBody()
            .map(paymentMapper::toEntity)
            .filter(payment::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(paymentList).isNotNull();
        assertThat(paymentList).hasSize(1);
        Payment testPayment = paymentList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertPaymentAllPropertiesEquals(payment, testPayment);
        assertPaymentUpdatableFieldsEquals(payment, testPayment);
    }

    @Test
    void getAllPayments() {
        // Initialize the database
        paymentRepository.save(payment).block();

        // Get all the paymentList
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
            .value(hasItem(payment.getId().intValue()))
            .jsonPath("$.[*].idCart")
            .value(hasItem(DEFAULT_ID_CART.intValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()));
    }

    @Test
    void getPayment() {
        // Initialize the database
        paymentRepository.save(payment).block();

        // Get the payment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, payment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(payment.getId().intValue()))
            .jsonPath("$.idCart")
            .value(is(DEFAULT_ID_CART.intValue()))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()));
    }

    @Test
    void getNonExistingPayment() {
        // Get the payment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPayment() throws Exception {
        // Initialize the database
        paymentRepository.save(payment).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).block();
        updatedPayment.idCart(UPDATED_ID_CART).date(UPDATED_DATE);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentToMatchAllProperties(updatedPayment);
    }

    @Test
    void putNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.save(payment).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment.date(UPDATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPayment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayment, payment), getPersistedPayment(payment));
    }

    @Test
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.save(payment).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment.idCart(UPDATED_ID_CART).date(UPDATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPayment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(partialUpdatedPayment, getPersistedPayment(partialUpdatedPayment));
    }

    @Test
    void patchNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, paymentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(paymentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePayment() {
        // Initialize the database
        paymentRepository.save(payment).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, payment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentRepository.count().block();
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

    protected Payment getPersistedPayment(Payment payment) {
        return paymentRepository.findById(payment.getId()).block();
    }

    protected void assertPersistedPaymentToMatchAllProperties(Payment expectedPayment) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPaymentAllPropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
        assertPaymentUpdatableFieldsEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }

    protected void assertPersistedPaymentToMatchUpdatableProperties(Payment expectedPayment) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPaymentAllUpdatablePropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
        assertPaymentUpdatableFieldsEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }
}
