import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class VendingMachineTest {

	private static final double DELTA = 0.0001;

	private VendingMachine machine;
	private VendingMachineItem chips; // price 1.25
	private VendingMachineItem soda; // price 2.00
	private VendingMachineItem freebie; // price 0.00

	@BeforeEach
	void setUp() throws VendingMachineException {
		machine = new VendingMachine();
		chips = new VendingMachineItem("Chips", 1.25);
		soda = new VendingMachineItem("Soda", 2.00);
		freebie = new VendingMachineItem("Freebie", 0.0);
	}

	@AfterEach
	void tearDown() {
		machine = null;
		chips = null;
		soda = null;
		freebie = null;
	}

	// =====================================================================
	// Constructor / constants
	// =====================================================================

	@Test
	void testConstructor_NewMachineBalanceIsZero() {
		// Act
		double balance = machine.getBalance();
		// Assert
		assertEquals(0.0, balance, DELTA);
	}

	@ParameterizedTest
	@ValueSource(strings = { "A", "B", "C", "D" })
	void testConstructor_NewMachineAllSlotsEmpty(String code) throws VendingMachineException {
		// Act
		VendingMachineItem item = machine.getItem(code);
		// Assert
		assertNull(item);
	}

	@Test
	void testConstants_HaveExpectedValues() {
		// Assert
		assertEquals(4, VendingMachine.NUM_SLOTS);
		assertEquals("A", VendingMachine.A_CODE);
		assertEquals("B", VendingMachine.B_CODE);
		assertEquals("C", VendingMachine.C_CODE);
		assertEquals("D", VendingMachine.D_CODE);
	}

	// =====================================================================
	// addItem
	// =====================================================================

	@ParameterizedTest
	@ValueSource(strings = { "A", "B", "C", "D" })
	void testAddItem_ValidCodeEmptySlotItemPlacedInSlot(String code) throws VendingMachineException {
		// Act
		machine.addItem(chips, code);
		// Assert
		assertSame(chips, machine.getItem(code));
	}

	@Test
	void testAddItem_DifferentSlotsItemsStoredIndependently() throws VendingMachineException {
		// Act
		machine.addItem(chips, VendingMachine.A_CODE);
		machine.addItem(soda, VendingMachine.D_CODE);
		// Assert
		assertSame(chips, machine.getItem("A"));
		assertSame(soda, machine.getItem("D"));
		assertNull(machine.getItem("B"));
		assertNull(machine.getItem("C"));
	}

	@Test
	void testAddItem_AllFourSlotsAllSucceed() throws VendingMachineException {
		// Arrange
		VendingMachineItem fourth = new VendingMachineItem("Candy", 0.75);
		// Act
		machine.addItem(chips, "A");
		machine.addItem(soda, "B");
		machine.addItem(freebie, "C");
		machine.addItem(fourth, "D");
		// Assert
		assertSame(chips, machine.getItem("A"));
		assertSame(soda, machine.getItem("B"));
		assertSame(freebie, machine.getItem("C"));
		assertSame(fourth, machine.getItem("D"));
	}

	@Test
	void testAddItem_OccupiedSlotThrowsException() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		// Act / Assert
		assertThrows(VendingMachineException.class, () -> machine.addItem(soda, "A"));
	}

	@Test
	void testAddItem_OccupiedSlotOriginalItemRemains() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		// Act
		assertThrows(VendingMachineException.class, () -> machine.addItem(soda, "A"));
		// Assert
		assertSame(chips, machine.getItem("A"));
	}

	@Test
	void testAddItem_SlotEmptiedByRemoveCanBeReused() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "B");
		machine.removeItem("B");
		// Act
		machine.addItem(soda, "B");
		// Assert
		assertSame(soda, machine.getItem("B"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "E", "a", "b", "c", "d", "", " ", "AA", "1", "Z" })
	void testAddItem_InvalidCodeThrowsException(String code) {
		// Act / Assert
		assertThrows(VendingMachineException.class, () -> machine.addItem(chips, code));
	}

	// =====================================================================
	// getItem
	// =====================================================================

	@ParameterizedTest
	@ValueSource(strings = { "E", "a", "", "AB", "0" })
	void testGetItem_InvalidCodeThrowsException(String code) {
		// Act / Assert
		assertThrows(VendingMachineException.class, () -> machine.getItem(code));
	}

	@Test
	void testGetItem_DoesNotRemoveItem() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "C");
		// Act
		machine.getItem("C");
		// Assert
		assertSame(chips, machine.getItem("C"));
	}

	// =====================================================================
	// removeItem
	// =====================================================================

	@ParameterizedTest
	@ValueSource(strings = { "A", "B", "C", "D" })
	void testRemoveItem_OccupiedSlotReturnsItem(String code) throws VendingMachineException {
		// Arrange
		machine.addItem(chips, code);
		// Act
		VendingMachineItem removed = machine.removeItem(code);
		// Assert
		assertSame(chips, removed);
	}

	@Test
	void testRemoveItem_OccupiedSlotSlotBecomesEmpty() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		// Act
		machine.removeItem("A");
		// Assert
		assertNull(machine.getItem("A"));
	}

	@Test
	void testRemoveItem_OneSlotOtherSlotsUnaffected() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		machine.addItem(soda, "B");
		// Act
		machine.removeItem("A");
		// Assert
		assertNull(machine.getItem("A"));
		assertSame(soda, machine.getItem("B"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "A", "B", "C", "D" })
	void testRemoveItem_EmptySlotThrowsException(String code) {
		// Act / Assert
		assertThrows(VendingMachineException.class, () -> machine.removeItem(code));
	}

	@Test
	void testRemoveItem_SameSlotTwiceSecondCallThrowsException() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		machine.removeItem("A");
		// Act / Assert
		assertThrows(VendingMachineException.class, () -> machine.removeItem("A"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "E", "a", "", "AA", "5" })
	void testRemoveItem_InvalidCodeThrowsException(String code) {
		// Act / Assert
		assertThrows(VendingMachineException.class, () -> machine.removeItem(code));
	}

	// =====================================================================
	// insertMoney / getBalance
	// =====================================================================

	@Test
	void testInsertMoney_PositiveAmountIncreasesBalance() throws VendingMachineException {
		// Act
		machine.insertMoney(5.50);
		// Assert
		assertEquals(5.50, machine.getBalance(), DELTA);
	}

	@Test
	void testInsertMoney_MultipleInsertionsBalanceAccumulates() throws VendingMachineException {
		// Act
		machine.insertMoney(1.00);
		machine.insertMoney(0.25);
		machine.insertMoney(2.75);
		// Assert
		assertEquals(4.00, machine.getBalance(), DELTA);
	}

	@Test
	void testInsertMoney_ZeroAmountIsAllowedAndBalanceUnchanged() throws VendingMachineException {
		// Arrange
		machine.insertMoney(3.00);
		// Act
		machine.insertMoney(0);
		// Assert
		assertEquals(3.00, machine.getBalance(), DELTA);
	}

	@Test
	void testInsertMoney_ZeroAmountOnNewMachineBalanceStaysZero() throws VendingMachineException {
		// Act
		machine.insertMoney(0);
		// Assert
		assertEquals(0.0, machine.getBalance(), DELTA);
	}

	@Test
	void testInsertMoney_SmallPositiveAmountIsAllowed() throws VendingMachineException {
		// Act
		machine.insertMoney(0.01);
		// Assert
		assertEquals(0.01, machine.getBalance(), DELTA);
	}

	@ParameterizedTest
	@ValueSource(doubles = { -0.01, -1.0, -100.0, Double.NEGATIVE_INFINITY })
	void testInsertMoney_NegativeAmountThrowsException(double amount) {
		// Act / Assert
		assertThrows(VendingMachineException.class, () -> machine.insertMoney(amount));
	}

	@Test
	void testInsertMoney_NegativeAmountBalanceUnchanged() throws VendingMachineException {
		// Arrange
		machine.insertMoney(2.00);
		// Act
		assertThrows(VendingMachineException.class, () -> machine.insertMoney(-1.00));
		// Assert
		assertEquals(2.00, machine.getBalance(), DELTA);
	}

	@Test
	void testGetBalance_CalledRepeatedlyDoesNotChangeBalance() throws VendingMachineException {
		// Arrange
		machine.insertMoney(3.50);
		// Act
		double first = machine.getBalance();
		double second = machine.getBalance();
		// Assert
		assertEquals(3.50, first, DELTA);
		assertEquals(first, second, DELTA);
	}

	// =====================================================================
	// makePurchase
	// =====================================================================

	@Test
	void testMakePurchase_EnoughMoneyReturnsTrue() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		machine.insertMoney(2.00);
		// Act
		boolean result = machine.makePurchase("A");
		// Assert
		assertTrue(result);
	}

	@Test
	void testMakePurchase_EnoughMoneySubtractsPriceFromBalance() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		machine.insertMoney(2.00);
		// Act
		machine.makePurchase("A");
		// Assert
		assertEquals(0.75, machine.getBalance(), DELTA);
	}

	@Test
	void testMakePurchase_EnoughMoneySlotBecomesEmpty() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		machine.insertMoney(2.00);
		// Act
		machine.makePurchase("A");
		// Assert
		assertNull(machine.getItem("A"));
	}

	@Test
	void testMakePurchase_BalanceExactlyEqualsPriceReturnsTrueAndBalanceZero()
			throws VendingMachineException {
		// Arrange
		machine.addItem(soda, "B");
		machine.insertMoney(2.00);
		// Act
		boolean result = machine.makePurchase("B");
		// Assert
		assertTrue(result);
		assertEquals(0.0, machine.getBalance(), DELTA);
		assertNull(machine.getItem("B"));
	}

	@Test
	void testMakePurchase_BalanceJustBelowPriceReturnsFalse() throws VendingMachineException {
		// Arrange
		machine.addItem(soda, "B");
		machine.insertMoney(1.99);
		// Act
		boolean result = machine.makePurchase("B");
		// Assert
		assertFalse(result);
	}

	@Test
	void testMakePurchase_NotEnoughMoneyBalanceAndItemUnchanged() throws VendingMachineException {
		// Arrange
		machine.addItem(soda, "B");
		machine.insertMoney(1.00);
		// Act
		machine.makePurchase("B");
		// Assert
		assertEquals(1.00, machine.getBalance(), DELTA);
		assertSame(soda, machine.getItem("B"));
	}

	@Test
	void testMakePurchase_ZeroBalanceReturnsFalseForPricedItem() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "C");
		// Act
		boolean result = machine.makePurchase("C");
		// Assert
		assertFalse(result);
		assertSame(chips, machine.getItem("C"));
	}

	@Test
	void testMakePurchase_FreeItemWithZeroBalanceReturnsTrue() throws VendingMachineException {
		// Arrange
		machine.addItem(freebie, "D");
		// Act
		boolean result = machine.makePurchase("D");
		// Assert
		assertTrue(result);
		assertEquals(0.0, machine.getBalance(), DELTA);
		assertNull(machine.getItem("D"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "A", "B", "C", "D" })
	void testMakePurchase_EmptySlotReturnsFalse(String code) throws VendingMachineException {
		// Arrange
		machine.insertMoney(5.00);
		// Act
		boolean result = machine.makePurchase(code);
		// Assert
		assertFalse(result);
	}

	@Test
	void testMakePurchase_EmptySlotBalanceUnchanged() throws VendingMachineException {
		// Arrange
		machine.insertMoney(5.00);
		// Act
		machine.makePurchase("A");
		// Assert
		assertEquals(5.00, machine.getBalance(), DELTA);
	}

	@Test
	void testMakePurchase_SameSlotTwiceSecondPurchaseReturnsFalse() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		machine.insertMoney(5.00);
		// Act
		boolean first = machine.makePurchase("A");
		boolean second = machine.makePurchase("A");
		// Assert
		assertTrue(first);
		assertFalse(second);
		assertEquals(3.75, machine.getBalance(), DELTA);
	}

	@Test
	void testMakePurchase_MultipleItemsBalanceReducedByEachPrice() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		machine.addItem(soda, "B");
		machine.insertMoney(5.00);
		// Act
		boolean first = machine.makePurchase("A");
		boolean second = machine.makePurchase("B");
		// Assert
		assertTrue(first);
		assertTrue(second);
		assertEquals(1.75, machine.getBalance(), DELTA);
	}

	// =====================================================================
	// returnChange
	// =====================================================================

	@Test
	void testReturnChange_WithBalanceReturnsBalance() throws VendingMachineException {
		// Arrange
		machine.insertMoney(3.25);
		// Act
		double change = machine.returnChange();
		// Assert
		assertEquals(3.25, change, DELTA);
	}

	@Test
	void testReturnChange_WithBalanceSetsBalanceToZero() throws VendingMachineException {
		// Arrange
		machine.insertMoney(3.25);
		// Act
		machine.returnChange();
		// Assert
		assertEquals(0.0, machine.getBalance(), DELTA);
	}

	@Test
	void testReturnChange_ZeroBalanceReturnsZero() {
		// Act
		double change = machine.returnChange();
		// Assert
		assertEquals(0.0, change, DELTA);
	}

	@Test
	void testReturnChange_CalledTwiceSecondCallReturnsZero() throws VendingMachineException {
		// Arrange
		machine.insertMoney(2.00);
		// Act
		double first = machine.returnChange();
		double second = machine.returnChange();
		// Assert
		assertEquals(2.00, first, DELTA);
		assertEquals(0.0, second, DELTA);
	}

	@Test
	void testReturnChange_AfterPurchaseReturnsRemainingBalance() throws VendingMachineException {
		// Arrange
		machine.addItem(chips, "A");
		machine.insertMoney(2.00);
		machine.makePurchase("A");
		// Act
		double change = machine.returnChange();
		// Assert
		assertEquals(0.75, change, DELTA);
		assertEquals(0.0, machine.getBalance(), DELTA);
	}
}