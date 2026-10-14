import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VendingMachineItemTest {

	private static final double DELTA = 0.0001;

	private VendingMachineItem item;

	@BeforeEach
	void setUp() throws VendingMachineException {
		item = new VendingMachineItem("Chips", 1.25);
	}

	@AfterEach
	void tearDown() {
		item = null;
	}

	// ---------- Validation: constructor / getters ----------

	@Test
	void testConstructor_ValidArgsSetsName() {
		// Arrange (done in setUp)
		// Act
		String name = item.getName();
		// Assert
		assertEquals("Chips", name);
	}

	@Test
	void testConstructor_ValidArgsSetsPrice() {
		// Act
		double price = item.getPrice();
		// Assert
		assertEquals(1.25, price, DELTA);
	}

	@Test
	void testGetName_CalledRepeatedlyReturnsSameValue() {
		// Act
		String first = item.getName();
		String second = item.getName();
		// Assert
		assertEquals(first, second);
		assertEquals("Chips", second);
	}

	@Test
	void testGetPrice_CalledRepeatedlyReturnsSameValue() {
		// Act
		double first = item.getPrice();
		double second = item.getPrice();
		// Assert
		assertEquals(first, second, DELTA);
		assertEquals(1.25, second, DELTA);
	}

	@ParameterizedTest
	@ValueSource(doubles = { 0.01, 0.5, 1.0, 2.75, 100.0, 1000000.0, Double.MAX_VALUE })
	void testConstructor_PositivePricesStoresPrice(double price) throws VendingMachineException {
		// Arrange / Act
		VendingMachineItem created = new VendingMachineItem("Candy", price);
		// Assert
		assertEquals(price, created.getPrice(), DELTA);
		assertEquals("Candy", created.getName());
	}

	// ---------- Boundary cases ----------

	@Test
	void testConstructor_PriceExactlyZeroIsAllowed() throws VendingMachineException {
		// Arrange / Act
		VendingMachineItem free = new VendingMachineItem("Sample", 0);
		// Assert
		assertEquals(0.0, free.getPrice(), DELTA);
		assertEquals("Sample", free.getName());
	}

	@Test
	void testConstructor_PriceJustAboveZeroIsAllowed() throws VendingMachineException {
		// Arrange / Act
		VendingMachineItem cheap = new VendingMachineItem("Gum", 0.01);
		// Assert
		assertEquals(0.01, cheap.getPrice(), DELTA);
	}

	@Test
	void testConstructor_PriceJustBelowZeroThrowsException() {
		// Arrange / Act / Assert
		assertThrows(VendingMachineException.class,
				() -> new VendingMachineItem("Gum", -0.01));
	}

	// ---------- Exception / invalid cases ----------

	@ParameterizedTest
	@ValueSource(doubles = { -0.01, -1.0, -50.5, -1000000.0, Double.NEGATIVE_INFINITY })
	void testConstructor_NegativePriceThrowsException(double price) {
		// Arrange / Act / Assert
		assertThrows(VendingMachineException.class,
				() -> new VendingMachineItem("Bad", price));
	}
}

