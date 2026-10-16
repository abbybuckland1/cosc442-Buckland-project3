Baseline coverage: 69.39%

Final coverage: 100%

Description of tests added: Going into it, I already had all of the remove tests. But, I added add() tests that tested adding items to empty slots and trying to add an item when the inventory was full. These tests covered the loop that looks for an empty slot and testing full inventory also covered what happens when there is no empty slots. Then, I tested isFull() checking when the inventory was full and not full. Testing the full and partially full inventory covered both the true and false results of the condition that checks whether a slot contains an item. Also, I tested contains() to check if an item was in the inventory or not. Testing an item that exists and one that doesn't covered both the outcomes of the if statement. Next, I tested get() to check that items were in the right slots and getItems() was added to improve coverage to check if an array contained the right items. 

Remaining uncovered behavior: none