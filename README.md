# Heliopolis Accounting Software

This is a and app that I did for a friend of mine who have a coffee shop called Heliopolis. He had a system for customers to register
orders and at the end of the day his system would output an Excel sheet that he would have to go through
manually to try and calculate his earnings. But problems arose because he didn't have  any
way to register his expenses or if someone asked him to pay the next day (credits).
So this system works simply by:
- Creating a Shift  
- Registering an Expense to that Shift
- Registering a Credit to that Shift
- At the end of the day Upload the Excel file with the day's orders 
- Close the shift and your earnings will be calculated

Additionally, we have a scheduler that runs every month calculating how many hours every user has worked which we get from shifts
and then calculate his pay and his deduction.

## General Information About the System
- Since this system is just for one small coffee shop the data won't be intensive ,so I opted to use Integer instead of
Long for all the IDs in our Entity's POJOs. The allocation size for all our IDs is 1 ,so it's unlikely that our Integer class 
will run out of numbers, so I used it since it's more memory efficient

- I relayed some of the system logic to the database by writing complex native queries the reason for that is I believe that SQL became underutilized 
and that it's more efficient performance wise to execute data intensive logic in the database, it's generally parallelized and optimised. The reason for writing the queries 
natively instead of HQL or JPQL is that first I want to get good at SQL since I am nowhere where I need to be right now and second because I believe the performance hit from translating the aforementioned
queries to native SQL queries is not worth it, since I don't plan to change my database of choice (PostgreSQL) mid-production, and I believe It's a rarity anyway.

## Shift module
A shift is the shift that all the expenses and credits and orders will be added to it and at the end of the day the 
totality of the shift will be calculated.
A shift have:
- userOpen: A FK Indicating who the user who opened the shift is, and he will be responsible for it
- expenses: which is a one-to-many relationship to all the expenses
- credits: which is a one-to-many relationship to all the credits
- orders: which is a one-to-many relationship to all the orders
- totalOrders: a BigDecimal indicating the total amount of orders
- totalCredits: a BigDecimal indicating the total amount of orders
- totalExpenses: a BigDecimal indicating the total amount of orders
- totalShift: a BigDecimal indicating the totality of the shift which orders - expenses - credits
- createdAt
- closed_at
- pay: a FK to the pay table this shift associated with
- closed: a boolean indicating is this shift is closed or not

All the upcoming requests must be sent with jwt token in Authorization Headers

### Creating a Shift

Send a POST request to /api/v1/shift/add
with an empty body

response:

```json
{
    "id": 15,
    "userOpen": "mohamed",
    "expenses": [],
    "credits": [],
    "orders": [],
    "totalOrders": null,
    "totalCredits": null,
    "totalExpenses": null,
    "totalShift": null,
    "createdAt": "2024-01-18T13:46:57.121793049",
    "closed_at": null,
    "closed": false
}
```

you can additionally send the createdAt and closed_at dates in the request body to create a past shift

if there is an open shift which is closed field is false in the database or in case you're creating a past shift and there is 
an overlap of dates with an existing shift

you will receive

```json
{
    "message": "There is an open shift or an overlapping shift, you need to close the open shift or modify the dates if overlapping",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-18T11:51:24.455185888Z"
}
```

### Listing Shifts

I've used a DTO here to avoid the N+1 cases in the many-to-one orders, expenses and credits, I thought of also 
excluding the user but since it's a FK and just fetching one row and it would be better to see who created the shift in 
listing I included it.

send a GET /api/v1/shift/list?page={page}

response

```json
[
    {
        "id": 15,
        "userOpen": "mohamed",
        "closed": false,
        "totalShift": null,
        "createdAt": "2024-01-18T13:46:57.121793",
        "closed_at": null
    },
    {
        "id": 10,
        "userOpen": "mohamed",
        "closed": true,
        "totalShift": 1073.50,
        "createdAt": "2024-01-15T09:09:01.784149",
        "closed_at": "2024-01-15T13:25:58.107403"
    },
    {
        "id": 11,
        "userOpen": "mohamed",
        "closed": true,
        "totalShift": null,
        "createdAt": "2024-01-15T13:32:14.438891",
        "closed_at": null
    },
    {
        "id": 12,
        "userOpen": "mohamed",
        "closed": true,
        "totalShift": null,
        "createdAt": "2024-01-15T13:32:57.565472",
        "closed_at": null
    },
    {
        "id": 13,
        "userOpen": "mohamed",
        "closed": true,
        "totalShift": -54.00,
        "createdAt": "2024-01-15T13:34:51.47735",
        "closed_at": "2024-01-15T14:27:23.845478"
    },
    {
        "id": 14,
        "userOpen": "ehab",
        "closed": true,
        "totalShift": 0.00,
        "createdAt": "2024-01-15T14:27:49.995",
        "closed_at": "2024-01-18T13:46:53.68093"
    }
]
```
the shifts will come back ordered by closed ascending and since there can't be more than one open shift the open shift will always come on top

pagination here is required you will receive this response if you didn't specify the page

```json
{
    "message": "Specify the page",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-18T15:07:43.34173248Z"
}
```

You can send an optional query params of start_date and end_date  which are the dates you filter shift by
the format of the dates is yyyy-MM-ddTHH:mm:ss

if you send a wrong date format you will receive 

```json
{
    "message": "Wrong date format, the correct format is yyyy-MM-ddTHH:mm:ss",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-19T07:53:00.051934871Z"
}
```

you can also send a query param to filter shifts by the user who created the request by specifying  get_user=true

