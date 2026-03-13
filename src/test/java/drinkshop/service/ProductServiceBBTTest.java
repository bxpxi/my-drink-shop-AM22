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

    @Test
    @Tag("ECP")
    @DisplayName("ECP valid: addProduct adds a valid product")
    void addProduct_ECP_valid_shouldAddProduct() {
        // Arrange
        Product product = new Product(
                101,
                "Cola",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        // Act
        productService.addProduct(product);

        // Assert
        Product saved = productService.findById(101);

        assertAll(
                () -> assertNotNull(saved),
                () -> assertEquals("Cola", saved.getNume()),
                () -> assertEquals(10.0, saved.getPret()),
                () -> assertEquals(CategorieBautura.JUICE, saved.getCategorie()),
                () -> assertEquals(TipBautura.WATER_BASED, saved.getTip()),
                () -> assertEquals(1, productService.getAllProducts().size())
        );
    }

    @Test
    @Tag("BVA")
    @DisplayName("BVA invalid: addProduct rejects price equal to 0")
    void addProduct_BVA_invalid_priceZero_shouldThrowValidationException() {
        // Arrange
        Product product = new Product(
                102,
                "Sprite",
                0.0,
                CategorieBautura.JUICE,
                TipBautura.WATER_BASED
        );

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> productService.addProduct(product)
        );

        // Assert
        assertAll(
                () -> assertTrue(exception.getMessage().contains("Pret invalid!")),
                () -> assertTrue(productService.getAllProducts().isEmpty()),
                () -> assertNull(productService.findById(102))
        );
    }
}