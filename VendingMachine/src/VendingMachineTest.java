import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class VendingMachineTest {

    VendingMachine machine;
    VendingMachineItem item;

    @BeforeEach  //arrange
    void setUp(){
        machine = new VendingMachine();
        item = new VendingMachineItem("Chips", 1.00);
    }

    @AfterEach 
    void tearDown(){
        machine = null;
        item=null;
    }

//testing the inital slots
    @Test 
    void testInitialSlotsEmpty(){
        assertNull(machine.getItem("A"));
        assertNull(machine.getItem("B"));
        assertNull(machine.getItem("C"));
        assertNull(machine.getItem("D"));
}

//ADD ITEM TESTS
    @Test
    void testAddItem() {
        //act: adding the item to slot A
        machine.addItem(item, "A");
        //assert:checkong that slot A now contains the item that was added
        assertEquals(item, machine.getItem("A"));
    }

    @Test 
    void testAddItemInvalidCode(){

        //act+assert: adding an item to invalid slot E should throw exception
        assertThrows(VendingMachineException.class, ()-> machine.addItem(item,"E"));
    }


    @Test 
    void testAddItemOccupiedSlot(){
        //arrange: creating second item
        VendingMachineItem item2 = new VendingMachineItem("Soda",1.25);
        machine.addItem(item, "A"); //adding the chips to slot A

        //act+assert: adding item2 to slot A that is occupied (should throw exception)
        assertThrows(VendingMachineException.class, ()-> machine.addItem(item2,"A"));
    }

//GET BALANCE TESTS
    @Test
    void testGetBalance() {
        //act+assert (balance should start with 0)
        assertEquals(0.0,machine.getBalance(),0.001);
    }

    @Test
    void testGetBalanceAfterInsert() {
        //act
        machine.insertMoney(5);
        //assert
        assertEquals(5,machine.getBalance(),0.001);

    }

//GET ITEM TESTS
    @Test
    void testGetItem() {
        //arrange: adding item to slot A
        machine.addItem(item,"A");

        //Act+Assert: getting the item from slot A and checking that the same item was given
        assertEquals(item,machine.getItem("A"));
    }

    @Test 
    void testGetItemInvalidCode(){
        //act+assert: getting an item from invalid slot E should throw exception
        assertThrows(VendingMachineException.class, ()-> machine.getItem("E"));
    }

    @Test
    void testGetItemEmptySlot() {   
        //act+assert: getting an item from an empty valid slot should return null
        assertNull(machine.getItem("A"));
}

//INSERT MONEY TESTS
    @ParameterizedTest 
    @ValueSource(doubles={0.0,0.01,0.50,1.00,5.00,100.00,1000.00})
    void testInsertMoney(double amount) {
        //act
        machine.insertMoney(amount);
        //assert
        assertEquals(amount,machine.getBalance(),0.001);
    }

    @Test
    void testInsertMoneyAddsToBalance() {
        //act: start with 5 dollars
        machine.insertMoney(5.00);

        //act: insert another 2 dollars
        machine.insertMoney(2.00);

        //assert: previous balance plus amount should equal 7
        assertEquals(7.00, machine.getBalance(), 0.001);
    }

    @Test
    void testInsertMoneyNegative() {
        
          //act+assert: inserting money <0 should throw exception
        assertThrows(VendingMachineException.class, ()-> machine.insertMoney(-0.01));
    }

//MAKE PURCHASE TESTS
    @Test
    void testMakePurchase() {
        //arrange
        machine.addItem(item,"A");
        machine.insertMoney(2.50);
        //act+assert
        assertTrue(machine.makePurchase("A"));
        assertEquals(1.50, machine.getBalance(),0.001); //item price subtracted from balance
    }

        @Test
    void testMakePurchaseMultipleItems() {
        //arrange
        VendingMachineItem item2 = new VendingMachineItem("Candy", 0.75);
        machine.addItem(item,"A");
        machine.addItem(item2,"B");
        machine.insertMoney(1.75);
        //act+assert
        assertTrue(machine.makePurchase("A"));
        assertEquals(0.75, machine.getBalance(),0.001); //item price subtracted from balance
        assertTrue(machine.makePurchase("B"));
        assertEquals(0.00, machine.getBalance(),0.001);
    }


    @Test
    void testMakePurchaseExactBalance() {
        //arrange: item costs exactly 1 dollar
        machine.addItem(item, "A");
        machine.insertMoney(1.00);

        //act+assert: purchase should succeed with exact balance
        assertTrue(machine.makePurchase("A"));

        //assert: entire balance should be used
        assertEquals(0.0, machine.getBalance(), 0.001);
        assertNull(machine.getItem("A"));
    }

 
@Test
    void testPurchaseWithMultipleInserts(){
        VendingMachineItem item = new VendingMachineItem("Candy", 0.75);
        machine.addItem(item, "A");
        machine.insertMoney(0.10);
        machine.insertMoney(0.10);
        machine.insertMoney(0.25);
        machine.insertMoney(0.25);
        machine.insertMoney(0.05);
        //act+assert
        assertTrue(machine.makePurchase("A"));
    }

     @Test
    void testMakePurchaseNotEnoughMoney() {
        //arrange: add 1.00 item but only insert 0.50, not enough money
        machine.addItem(item,"A");
        machine.insertMoney(0.50);
        //act+assert
        assertFalse(machine.makePurchase("A"));
    }


     @Test
    void testMakePurchaseEmptySlot() {
        //act+assert: purchase from empty slot
        assertFalse(machine.makePurchase("B"));
    }


//REMOVE ITEM TESTS
    @Test
    void testRemoveItem() {
        //arrange
        machine.addItem(item,"A");
        //act+assert: remove item from slot A and check it was the correct item
        assertEquals(item,machine.removeItem("A"));
        assertNull(machine.getItem("A"));
    }

    @Test 
    void testRemoveItemEmptySlot(){
          //act+assert: removing an item from empty slot D should throw exception
        assertThrows(VendingMachineException.class, ()-> machine.removeItem("D"));
    }

    @Test 
    void testRemoveItemInvalidCode(){
          //act+assert: removing an item from invalid slot E should throw exception
        assertThrows(VendingMachineException.class, ()-> machine.removeItem("E"));
    }

//RETURN CHANGE TESTS
    @Test
    void testReturnChange() {
        //arrange
        machine.insertMoney(5.00);
        //act+assert
        assertEquals(5.00, machine.returnChange(), 0.001);
        //assert
        assertEquals(0.0,machine.getBalance(),0.001);
    }

    @Test
    void testReturnChangeZeroBalance() {
        //assert+act
        assertEquals(0.0,machine.returnChange(),0.001);

    }
}
