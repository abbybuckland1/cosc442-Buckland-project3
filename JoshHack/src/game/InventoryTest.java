package game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
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

    @Test
    void testAddValidEmptySlot(){
        //arrange/act
        myInventory.add(one);
        myInventory.add(two);
        myInventory.add(three);
        //assert
        assertEquals(one, myInventory.get(0));
        assertEquals(two, myInventory.get(1));
        assertEquals(three, myInventory.get(2));
    }

    @Test 
    void testAddInventoryFull(){
        Item four = new Item('!', Color.BLUE, "test", null);
        myInventory.add(one);
        myInventory.add(two);
        myInventory.add(three);
        myInventory.add(four);

        assertEquals(one, myInventory.get(0));
        assertEquals(two, myInventory.get(1));
        assertEquals(three, myInventory.get(2));
        assertFalse(myInventory.contains(four));
    }

    @Test 
    void testIsFull(){
        myInventory.add(one);
        myInventory.add(two);
        myInventory.add(three);

        assertTrue(myInventory.isFull());
    }

    @Test 
    void testIsFullNot(){
        myInventory.add(one);

        assertFalse(myInventory.isFull());
    }

    @Test 
    void testGetItems(){
        myInventory.add(one);
        myInventory.add(two);

        Item[] items = myInventory.getItems();

        assertEquals(3, items.length);
        assertEquals(one, items[0]);
        assertEquals(two, items[1]);
        assertNull(items[2]);
    }

    @Test
    void testContainsValid() {
        myInventory.add(one);
        myInventory.add(two);

        assertTrue(myInventory.contains(two));
    }

    @Test
    void testContainsItemNotInInventoryInvalid() {
        myInventory.add(one);

        assertFalse(myInventory.contains(three));
    }
}
