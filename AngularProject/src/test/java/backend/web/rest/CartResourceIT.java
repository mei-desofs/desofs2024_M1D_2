package backend.web.rest;

import static backend.domain.CartAsserts.*;
import static backend.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import backend.IntegrationTest;
import backend.domain.Cart;
import backend.repository.CartRepository;
import backend.repository.EntityManager;
import backend.service.dto.CartDTO;
import backend.service.mapper.CartMapper;
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
 * Integration tests for the {@link CartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CartResourceIT {

    private static final Integer DEFAULT_TOTAL = 1;
    private static final Integer UPDATED_TOTAL = 2;

    private static final String ENTITY_API_URL = "/api/carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Cart cart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createEntity(EntityManager em) {
        Cart cart = new Cart().total(DEFAULT_TOTAL);
        return cart;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cart createUpdatedEntity(EntityManager em) {
        Cart cart = new Cart().total(UPDATED_TOTAL);
        return cart;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Cart.class).block();
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
        cart = createEntity(em);
    }

    @Test
    void createCart() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);
        var returnedCartDTO = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CartDTO.class)
            .returnResult()
            .getResponseBody();

        // Validate the Cart in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCart = cartMapper.toEntity(returnedCartDTO);
        assertCartUpdatableFieldsEquals(returnedCart, getPersistedCart(returnedCart));
    }

    @Test
    void createCartWithExistingId() throws Exception {
        // Create the Cart with an existing ID
        cart.setId(1L);
        CartDTO cartDTO = cartMapper.toDto(cart);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCartsAsStream() {
        // Initialize the database
        cartRepository.save(cart).block();

        List<Cart> cartList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CartDTO.class)
            .getResponseBody()
            .map(cartMapper::toEntity)
            .filter(cart::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(cartList).isNotNull();
        assertThat(cartList).hasSize(1);
        Cart testCart = cartList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertCartAllPropertiesEquals(cart, testCart);
        assertCartUpdatableFieldsEquals(cart, testCart);
    }

    @Test
    void getAllCarts() {
        // Initialize the database
        cartRepository.save(cart).block();

        // Get all the cartList
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
            .value(hasItem(cart.getId().intValue()))
            .jsonPath("$.[*].total")
            .value(hasItem(DEFAULT_TOTAL));
    }

    @Test
    void getCart() {
        // Initialize the database
        cartRepository.save(cart).block();

        // Get the cart
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cart.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cart.getId().intValue()))
            .jsonPath("$.total")
            .value(is(DEFAULT_TOTAL));
    }

    @Test
    void getNonExistingCart() {
        // Get the cart
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCart() throws Exception {
        // Initialize the database
        cartRepository.save(cart).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cart
        Cart updatedCart = cartRepository.findById(cart.getId()).block();
        updatedCart.total(UPDATED_TOTAL);
        CartDTO cartDTO = cartMapper.toDto(updatedCart);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cartDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCartToMatchAllProperties(updatedCart);
    }

    @Test
    void putNonExistingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cartDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCartWithPatch() throws Exception {
        // Initialize the database
        cartRepository.save(cart).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCart.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCart))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCartUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCart, cart), getPersistedCart(cart));
    }

    @Test
    void fullUpdateCartWithPatch() throws Exception {
        // Initialize the database
        cartRepository.save(cart).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cart using partial update
        Cart partialUpdatedCart = new Cart();
        partialUpdatedCart.setId(cart.getId());

        partialUpdatedCart.total(UPDATED_TOTAL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCart.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCart))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCartUpdatableFieldsEquals(partialUpdatedCart, getPersistedCart(partialUpdatedCart));
    }

    @Test
    void patchNonExistingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cartDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cart.setId(longCount.incrementAndGet());

        // Create the Cart
        CartDTO cartDTO = cartMapper.toDto(cart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cartDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCart() {
        // Initialize the database
        cartRepository.save(cart).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cart
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cart.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cartRepository.count().block();
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

    protected Cart getPersistedCart(Cart cart) {
        return cartRepository.findById(cart.getId()).block();
    }

    protected void assertPersistedCartToMatchAllProperties(Cart expectedCart) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCartAllPropertiesEquals(expectedCart, getPersistedCart(expectedCart));
        assertCartUpdatableFieldsEquals(expectedCart, getPersistedCart(expectedCart));
    }

    protected void assertPersistedCartToMatchUpdatableProperties(Cart expectedCart) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCartAllUpdatablePropertiesEquals(expectedCart, getPersistedCart(expectedCart));
        assertCartUpdatableFieldsEquals(expectedCart, getPersistedCart(expectedCart));
    }
}
