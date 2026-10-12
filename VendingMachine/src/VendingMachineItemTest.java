import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VendingMachineItemTest {
    VendingMachineItem item;

    @BeforeEach  //arrange
    void setUp(){
        item = new VendingMachineItem("Chips", 1.00);
    }

    @AfterEach 
    void tearDown(){
        item=null;
    }

    @Test 
    void testVendingMachineItemCreation(){
        //act
        VendingMachineItem item2 = new VendingMachineItem("Soda", 1.50);

        //assert: name and price should be set to values that are entered
        assertEquals("Soda", item2.getName());
        assertEquals(1.50,item2.getPrice(),0.001);
    }

    @Test
    void testVendingMachineItemZeroPrice() {
        //act: create item at lowest valid price
        VendingMachineItem item2 = new VendingMachineItem("Soda", 0.00);

        //assert: price of 0 should be accepted
        assertEquals(0.00, item2.getPrice(), 0.001);
    }

    @Test
    void testVendingMachineNegativePrice(){
        assertThrows(VendingMachineException.class,() -> new VendingMachineItem("Soda", -0.01));
    }

    @Test
    void testGetName() {
        assertEquals("Chips", item.getName());
    }

    @Test
    void testGetPrice() {
        assertEquals(1.00,item.getPrice(),0.001);
    }
}
