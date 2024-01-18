# Heliopolis Accounting Software

This is a and app that I did for a friend of mine who have a coffe shop called Heliopolis. He had a system for customers to register
orders and at the end of the day his system would output an Excel sheet that he would have to go through
manually to try and calculate his earnings. But problems arose because he didn't have  any
way to register his expenses or if someone asked him to pay the next day (credits).
So this system works simply by:
- Creating a Shift  
- Registering an Expense to that Shift
- Registering a Credit to that Shift
- At the end of the day Upload the Excel file with the day's orders 
- Close the shift and you earnings will be calculated

Additionally we have a scheduler that runs every month calculating how many hours every user has worked which we get from shifts
and then calculate his pay and his deduction.