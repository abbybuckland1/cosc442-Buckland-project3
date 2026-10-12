***Initial and final coverage results***

|Source File| Initial(and final) Statement/Line Coverage|Initial (and final) Branch Coverage|
|---|---|---|
|VendingMachine.java|100%|100%|
|VendingMachineItem.java|100%|100%|

***Description of tests added to improve coverage***

I did not add any tests as my coverage was already at 100%

#***Condition Coverage Analysis***

if ((item != null) && (this.balance >= item.getPrice()))

C1: if ((item != null) 

True: testMakePurchaseExactBalance() >> adds an item to slot A with price of 1.00 then makes a purchase from Slot A which is not empty as an item was just added to it. 

False: testMakePurchaseEmptySlot() >> tries to make a purchase from slot B but there is no item added to slot B. So, the item is null making this condition false. 

C2: (this.balance >= item.getPrice()))

True: testMakePurchaseExactBalance() >> an item with price 1.00 is in slot A. Then 1.00 is inserted into balance. 1.00 = 1.00 which satisfies the condition. 

False: testMakePurchaseNotEnoughMoney() >> an item is added to slot A with a price of 1.00. Then only 0.50 is inserted into the balance. 0.50<1.00 which makes this condition false. 


***Test Sensitivity experiment results***

Inserted fault: insertMoney method 

Original line: this.balance+=amount;

Fault line: this.balance-=amount; 

-This makes the machine subtract the inserted money from the balance instead of adding it to the balance. 

Pass: 24 tests

Failed: 13 tests

Failed tests: 

testGetBalanceAfterInsert()

testInsertMoney(double)

testInsertMoneyAddsToBalance()

testMakePurchase()

testMakePurchaseExactBalance()

testMakePurchaseMultipleItems()

testPurchaseWithMultipleInserts()

testReturnChange()