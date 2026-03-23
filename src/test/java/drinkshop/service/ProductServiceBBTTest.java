package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceBBTTest {

    @TempDir
    Path tempDir;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        String testFile = tempDir.resolve("products_test.txt").toString();
        FileProductRepository repo = new FileProductRepository(testFile);
        productService = new ProductService(repo);
    }

    // -------------------- ECP --------------------

    @Test
    @Tag("ECP")
    @DisplayName("ECP valid: addProduct adds a valid product")
    void addProduct_ECP_valid_shouldAddProduct() {
        Product product = new Product(
                101,
                "Cola",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        productService.addProduct(product);

        Product saved = productService.findById(101);

        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals("Cola", saved.getNume()),
                () -> assertEquals(10.0, saved.getPret()),
                () -> assertEquals(1, productService.getAllProducts().size())
        );
    }

    @Test
    @Tag("ECP")
    @DisplayName("ECP invalid: null name should be rejected")
    void addProduct_ECP_invalid_nullName_shouldThrowException() {
        Product product = new Product(
                102,
                null,
                10.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        assertThrows(ValidationException.class,
                () -> productService.addProduct(product));

        assertTrue(productService.getAllProducts().isEmpty());
    }
    @Test
    @Tag("ECP")
    @DisplayName("ECP Invalid: empty name should be rejected")
    void addProduct_ECP_empty_name_shouldThrowException() {
        Product product = new Product(
                103,
                "",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        assertThrows(ValidationException.class,
                () -> productService.addProduct(product));

        assertTrue(productService.getAllProducts().isEmpty());
    }

    @Test
    @Tag("ECP")
    @DisplayName("ECP valid: add another valid product")
    void addProduct_ECP_anotherValid_shouldAddProduct() {
        Product product = new Product(
                104,
                "Fanta",
                8.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        productService.addProduct(product);

        Product saved = productService.findById(104);

        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals("Fanta", saved.getNume()),
                () -> assertEquals(8.0, saved.getPret()),
                () -> assertEquals(1, productService.getAllProducts().size())
        );
    }

    @Test
    @Tag("ECP")
    @DisplayName("ECP invalid: negative price should be rejected")
    void addProduct_ECP_invalid_negativePrice_shouldThrowException() {
        Product product = new Product(
                105,
                "Sprite",
                -5.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        assertThrows(ValidationException.class,
                () -> productService.addProduct(product));

        assertNull(productService.findById(105));
    }

    // -------------------- BVA --------------------
    @Test
    @Tag("BVA")
    @DisplayName("BVA invalid: price = 0")
    void addProduct_BVA_invalid_priceZero_shouldThrowValidationException() {
        Product product = new Product(
                106,
                "Cola",
                0.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        assertThrows(ValidationException.class,
                () -> productService.addProduct(product));
        assertTrue(productService.getAllProducts().isEmpty());
    }

    @Test
    @Tag("BVA")
    @DisplayName("BVA valid: price = 0.01")
    void addProduct_BVA_valid_priceJustAboveZero_shouldAddProduct() {
        Product product = new Product(
                107,
                "Sprite",
                0.01,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        productService.addProduct(product);

        Product saved = productService.findById(107);
        assertNotNull(saved);
        assertEquals("Sprite", saved.getNume());
        assertEquals(0.01, saved.getPret());
    }

    @Test
    @Tag("BVA")
    @DisplayName("BVA invalid: empty name")
    void addProduct_BVA_invalid_emptyName_shouldThrowValidationException() {
        Product product = new Product(
                108,
                "",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        assertThrows(ValidationException.class,
                () -> productService.addProduct(product));
        assertTrue(productService.getAllProducts().isEmpty());
    }

    @Test
    @Tag("BVA")
    @DisplayName("BVA valid: name = 'A'")
    void addProduct_BVA_valid_nameA_shouldAddProduct() {
        Product product = new Product(
                109,
                "A",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        productService.addProduct(product);

        Product saved = productService.findById(109);
        assertNotNull(saved);
        assertEquals("A", saved.getNume());
        assertEquals(10.0, saved.getPret());
    }
}