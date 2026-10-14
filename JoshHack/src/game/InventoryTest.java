package game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;


public class InventoryTest {
    Inventory myInventory;
    Item one;
    Item two;
    Item three;

    @BeforeEach 
    void setUp(){
        myInventory = new Inventory(3);
            one = new Item(',', Color.YELLOW, "rock", null);
            two = new Item('%', Color.RED, "apple", null);
            three = new Item('%', Color.YELLOW, "bread", null);
    }

    @AfterEach 
    void tearDown(){
        myInventory=null;
        one=null;
        two = null;
        three=null;
    }

    @Test
    void testRemoveValid() {
        //arrange
        myInventory.add(one); //adding rock to inventory
        myInventory.add(two);
        myInventory.add(three);
        //act
        myInventory.remove(one); //removing rock
        //assert
        assertFalse(myInventory.contains(one));
        assertTrue(myInventory.contains(two));
        assertTrue(myInventory.contains(three));
    }

    @Test
    void testRemoveItemNotInInventory() {
        //arrange
        myInventory.add(one);
        myInventory.add(two);
        //act
        myInventory.remove(three);
        //assert
        assertTrue(myInventory.contains(one));
        assertTrue(myInventory.contains(two));
        assertFalse(myInventory.contains(three));
    }

    @Test
    void testRemoveBoundaryRemoveFromLastSpot() {
        //arrange
        myInventory.add(one);
        myInventory.add(two);
        myInventory.add(three);
        //act
        myInventory.remove(three);
        //assert
        assertNull(myInventory.get(2));
        assertTrue(myInventory.contains(one));
        assertTrue(myInventory.contains(two));
    }

}
