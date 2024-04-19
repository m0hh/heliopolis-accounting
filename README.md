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

- I've coded the jwt logic myself instead of relying on ouath2 because I wanted to familiarize myself with underlying architecture

- Because this is a closed system all endpoints require authentication except login. to authenticate a request send an Authorization Header with token directly without prefixing with token or bearer

## User Module
- username: the username of the user
- password: the password of the user
- email: the email
- roles: roles like Admin or Manager
- hourlyRate: an Integer describing how much money does he make an hour
- hoursToWork: an Integer of his total hours of supposed work in a month

## SignUp

because a only the admin can signup new users the signup must be sent with a token of an admin. the admin himself must be inserted manually in the database.
Send a POST request to /user/saveUser
body :
```json
{
"username": "test",
"password": "test4321",
"email": "example@gmail.com",
"roles": ["Manager"],
"hourlyRate": 50,
"hoursToWork": 186
}
```

response:
```
User with id '8' saved succssfully!
```
### Login

Send a POST request to  /user/loginUser

body:

```json
{
"username": "test",
"password": "test4321"
}
```

response:

```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiI3ODkwIiwic3ViIjoibW9oYW1lZCIsImlzcyI6IkFCQ19MdGQiLCJhdWQiOiJYWVpfTHRkIiwiaWF0IjoxNzEzNTU3MjQxLCJleHAiOjE3MTM1NjA4NDF9.ZLaw4YjbIyWy0nqjLohsj-F839F1kvsD6lgjdcwGgil_ww4GT8zM_wyEcQev0-Jbhnvi_cei7l8M_KfuA2Zusg",
    "message": "Token generated successfully!"
}
```

### Adding Firbase Token

send a POST request to /user/fb_token with the token in headers
body:

```json
{
    "firebaseToken": "adcacdsdcvsda"
}
```

response:
```
Saved
```

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
userOpen has its getter overriden to return only the username 

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

#### Closing a shift
By closing a shift you calculate all the orders created, and you subtract all the expenses and credits of these shifts, the calculation happens inside the database
defined by the method closeShift in the ShiftRepo.

URL POST : /api/v1/shift/close/{shift ID}
Needs to be sent with a JWT token

You will receive a response of 1  and a status code of 200 ok of all goes well 

if the token belonged to a user who didn't create the shift you will receive

```json
{
    "message": "only the user who created the shift can close it",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-24T14:25:33.192314754Z"
}
```

if the shift is already closed you will receive

```json
{
    "message": "This Shift is closed modify it first",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-24T14:29:12.448661138Z"
}
```

if the shift does not exist you will receive

```json
{
    "message": "No shift is by that id",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-24T14:29:57.610722213Z"
}
```

### Reopening a shift

if you want to reopen a closed shift

URL PUT : /api/v1/shift/reopen/{shift ID}
 

```json
{
    "id": 15,
    "userOpen": "mohamed",
    "expenses": [],
    "credits": [],
    "orders": [],
    "totalOrders": 0.00,
    "totalCredits": 0.00,
    "totalExpenses": 0.00,
    "totalShift": 0.00,
    "createdAt": "2024-01-18T13:46:57.121793",
    "closed_at": "2024-01-24T16:26:22.106137",
    "closed": false
}
```

If there is any open shift already

```json
{
    "message": "There is an open shift you need to close that first",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-27T12:10:40.459505402Z"
}
```

If the user who sent the request is not the one who opened it in the first place

```json
{
    "message": "The user who opened the shift must reopen it",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-27T12:12:32.389278004Z"
}
```

If the ID sent does not exist

```json
{
    "message": "There is no shift by that ID",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-27T12:13:23.723868268Z"
}
```

### Retrieving a shift 

Retrieving a shift will return the shift information with all it's orders, expenses and credits

```json
{
  "id": 10,
  "userOpen": "mohamed",
  "expenses": [
    {
      "id": 16,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:10:31.833369"
    },
    {
      "id": 17,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:10:32.901406"
    },
    {
      "id": 18,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:10:33.800461"
    },
    {
      "id": 19,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:11:35.881764"
    },
    {
      "id": 20,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:12:34.404652"
    },
    {
      "id": 21,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:13:23.721623"
    },
    {
      "id": 22,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:14:27.579661"
    },
    {
      "id": 23,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:27:50.127319"
    },
    {
      "id": 24,
      "user": "mohamed",
      "amount": 12.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:28:54.58553"
    }
  ],
  "credits": [
    {
      "amount": 13.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:10:46.472687",
      "id": 17,
      "user": "mohamed"
    },
    {
      "amount": 13.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:10:47.564461",
      "id": 18,
      "user": "mohamed"
    },
    {
      "amount": 13.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:10:48.477856",
      "id": 19,
      "user": "mohamed"
    },
    {
      "amount": 13.50,
      "description": "adfadcad",
      "createdAt": "2024-01-15T09:10:49.414302",
      "id": 20,
      "user": "mohamed"
    }
  ],
  "orders": [
    {
      "id": 617,
      "orderId": 2391,
      "createdAt": "2023-12-19T22:56:43",
      "amount": 20.00
    },
    {
      "id": 618,
      "orderId": 2390,
      "createdAt": "2023-12-19T22:55:59",
      "amount": 75.00
    },
    {
      "id": 619,
      "orderId": 2389,
      "createdAt": "2023-12-19T22:55:28",
      "amount": 35.00
    },
    {
      "id": 620,
      "orderId": 2388,
      "createdAt": "2023-12-19T22:34:30",
      "amount": 25.00
    },
    {
      "id": 621,
      "orderId": 2387,
      "createdAt": "2023-12-19T20:35:43",
      "amount": 10.00
    },
    {
      "id": 622,
      "orderId": 2386,
      "createdAt": "2023-12-19T20:16:31",
      "amount": 15.00
    },
    {
      "id": 623,
      "orderId": 2385,
      "createdAt": "2023-12-19T20:12:22",
      "amount": 30.00
    },
    {
      "id": 624,
      "orderId": 2384,
      "createdAt": "2023-12-19T20:11:31",
      "amount": 20.00
    },
    {
      "id": 625,
      "orderId": 2383,
      "createdAt": "2023-12-19T19:29:23",
      "amount": 10.00
    },
    {
      "id": 626,
      "orderId": 2382,
      "createdAt": "2023-12-19T19:26:51",
      "amount": 65.00
    },
    {
      "id": 627,
      "orderId": 2381,
      "createdAt": "2023-12-19T19:23:34",
      "amount": 10.00
    },
    {
      "id": 628,
      "orderId": 2380,
      "createdAt": "2023-12-19T19:17:55",
      "amount": 40.00
    },
    {
      "id": 629,
      "orderId": 2379,
      "createdAt": "2023-12-19T19:11:42",
      "amount": 10.00
    },
    {
      "id": 630,
      "orderId": 2378,
      "createdAt": "2023-12-19T19:00:55",
      "amount": 15.00
    },
    {
      "id": 631,
      "orderId": 2377,
      "createdAt": "2023-12-19T18:59:14",
      "amount": 10.00
    },
    {
      "id": 632,
      "orderId": 2376,
      "createdAt": "2023-12-19T18:51:10",
      "amount": 30.00
    },
    {
      "id": 633,
      "orderId": 2375,
      "createdAt": "2023-12-19T18:49:28",
      "amount": 10.00
    },
    {
      "id": 634,
      "orderId": 2374,
      "createdAt": "2023-12-19T18:48:55",
      "amount": 30.00
    },
    {
      "id": 635,
      "orderId": 2373,
      "createdAt": "2023-12-19T18:28:50",
      "amount": 25.00
    },
    {
      "id": 636,
      "orderId": 2372,
      "createdAt": "2023-12-19T18:27:57",
      "amount": 5.00
    },
    {
      "id": 637,
      "orderId": 2371,
      "createdAt": "2023-12-19T18:14:40",
      "amount": 10.00
    },
    {
      "id": 638,
      "orderId": 2370,
      "createdAt": "2023-12-19T18:12:00",
      "amount": 50.00
    },
    {
      "id": 639,
      "orderId": 2369,
      "createdAt": "2023-12-19T17:54:28",
      "amount": 20.00
    },
    {
      "id": 640,
      "orderId": 2368,
      "createdAt": "2023-12-19T17:53:01",
      "amount": 110.00
    },
    {
      "id": 641,
      "orderId": 2367,
      "createdAt": "2023-12-19T17:02:11",
      "amount": 10.00
    },
    {
      "id": 642,
      "orderId": 2366,
      "createdAt": "2023-12-19T16:48:52",
      "amount": 10.00
    },
    {
      "id": 643,
      "orderId": 2365,
      "createdAt": "2023-12-19T16:47:18",
      "amount": 10.00
    },
    {
      "id": 644,
      "orderId": 2364,
      "createdAt": "2023-12-19T16:46:24",
      "amount": 25.00
    },
    {
      "id": 645,
      "orderId": 2363,
      "createdAt": "2023-12-19T16:26:26",
      "amount": 10.00
    },
    {
      "id": 646,
      "orderId": 2362,
      "createdAt": "2023-12-19T16:25:25",
      "amount": 20.00
    },
    {
      "id": 647,
      "orderId": 2361,
      "createdAt": "2023-12-19T16:24:22",
      "amount": 10.00
    },
    {
      "id": 648,
      "orderId": 2360,
      "createdAt": "2023-12-19T16:23:54",
      "amount": 45.00
    },
    {
      "id": 649,
      "orderId": 2359,
      "createdAt": "2023-12-19T16:22:35",
      "amount": 25.00
    },
    {
      "id": 650,
      "orderId": 2358,
      "createdAt": "2023-12-19T15:20:19",
      "amount": 60.00
    },
    {
      "id": 651,
      "orderId": 2357,
      "createdAt": "2023-12-19T15:03:42",
      "amount": 10.00
    },
    {
      "id": 652,
      "orderId": 2356,
      "createdAt": "2023-12-19T14:32:50",
      "amount": 20.00
    },
    {
      "id": 653,
      "orderId": 2355,
      "createdAt": "2023-12-19T14:00:07",
      "amount": 10.00
    },
    {
      "id": 654,
      "orderId": 2354,
      "createdAt": "2023-12-19T13:49:55",
      "amount": 15.00
    },
    {
      "id": 655,
      "orderId": 2353,
      "createdAt": "2023-12-19T13:49:53",
      "amount": 10.00
    },
    {
      "id": 656,
      "orderId": 2352,
      "createdAt": "2023-12-19T13:49:23",
      "amount": 20.00
    },
    {
      "id": 657,
      "orderId": 2351,
      "createdAt": "2023-12-19T13:46:33",
      "amount": 10.00
    },
    {
      "id": 658,
      "orderId": 2350,
      "createdAt": "2023-12-19T13:25:02",
      "amount": 5.00
    },
    {
      "id": 659,
      "orderId": 2349,
      "createdAt": "2023-12-19T13:20:25",
      "amount": 10.00
    },
    {
      "id": 660,
      "orderId": 2348,
      "createdAt": "2023-12-19T13:15:12",
      "amount": 10.00
    },
    {
      "id": 661,
      "orderId": 2347,
      "createdAt": "2023-12-19T13:05:30",
      "amount": 25.00
    },
    {
      "id": 662,
      "orderId": 2346,
      "createdAt": "2023-12-19T12:56:39",
      "amount": 25.00
    },
    {
      "id": 663,
      "orderId": 2345,
      "createdAt": "2023-12-19T12:54:38",
      "amount": 10.00
    },
    {
      "id": 664,
      "orderId": 2344,
      "createdAt": "2023-12-19T12:48:02",
      "amount": 10.00
    },
    {
      "id": 665,
      "orderId": 2343,
      "createdAt": "2023-12-19T12:22:27",
      "amount": 10.00
    },
    {
      "id": 666,
      "orderId": 2342,
      "createdAt": "2023-12-19T12:14:38",
      "amount": 10.00
    },
    {
      "id": 667,
      "orderId": 2341,
      "createdAt": "2023-12-19T11:33:34",
      "amount": 10.00
    },
    {
      "id": 668,
      "orderId": 2340,
      "createdAt": "2023-12-19T10:58:56",
      "amount": 30.00
    },
    {
      "id": 669,
      "orderId": 2339,
      "createdAt": "2023-12-19T09:50:49",
      "amount": 15.00
    },
    {
      "id": 670,
      "orderId": 2338,
      "createdAt": "2023-12-19T08:33:00",
      "amount": 30.00
    },
    {
      "id": 671,
      "orderId": 2337,
      "createdAt": "2023-12-19T07:30:45",
      "amount": 20.00
    },
    {
      "id": 672,
      "orderId": 2336,
      "createdAt": "2023-12-19T06:53:43",
      "amount": 20.00
    }
  ],
  "totalOrders": 1240.00,
  "totalCredits": 54.00,
  "totalExpenses": 112.50,
  "totalShift": 1073.50,
  "createdAt": "2024-01-15T09:09:01.784149",
  "closed_at": "2024-01-15T13:25:58.107403",
  "closed": true
}
```

If there is no shift by that ID

```json
{
    "message": "No shift by that Id",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-27T12:17:56.851261953Z"
}
```


## Credit module
A Credit is money that customers will pay at later time .
A credit has:
- user: A FK Indicating who the user who created the credit is, and he will be responsible for it
- amount: a BigDecimal of the amount of the credit.
- description: a String indicating any other information about the credit
- createdAt: a LocalDateTime  indicating the exact time the credit was created at.
- shift: a FK indicating which shift this credit belongs to.

All the upcoming requests must be sent with jwt token in Authorization Headers
user has its getter overriden to return only the username 
shift is annotated with @JsonBackReference and fetched lazily because we don't want to access the shift information from this side

## Adding a new Credit

URL POST /api/v1/credit/add

Body:

```json
{
    "amount":13.5,
    "description":"new credit",
    "shift":{
        "id": 15
    }
}
```

Response:

```json
{
    "amount": 13.5,
    "description": "new credit",
    "createdAt": "2024-01-29T21:15:13.727030007",
    "id": 25,
    "user": "mohamed"
}
```

If the request is sent with a shift id that is closed

```json
{
    "message": "This Shift is closed open it first and then modify",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:17:33.31082058Z"
}
```

If the shift does not exist

```json
{
    "message": "No Shift with that Id",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:23:15.932147641Z"
}
```

If any of the required fields are missing from the body

```json
{
    "message": "amount cannot bel blank or null, You must enter a Shift id",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:27:28.260770024Z"
}
```

### Retrieving a credit

URL GET /api/v1/credit/retrieve/{credit id}

````json
{
    "amount": 13.50,
    "description": "new credit",
    "createdAt": "2024-01-29T21:15:13.72703",
    "id": 25,
    "user": "mohamed"
}
````

if the credit does not exist

```json
{
  "message": "No Credit with that ID",
  "httpStatus": "BAD_REQUEST",
  "timestamp": "2024-01-29T19:28:36.966672487Z"
}
```


### Listing credits

url GET /api/v1/credit/list?page={page number}

response:
```json
[
    {
        "id": 17,
        "amount": 13.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:10:46.472687"
    },
    {
        "id": 18,
        "amount": 13.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:10:47.564461"
    },
    {
        "id": 19,
        "amount": 13.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:10:48.477856"
    },
    {
        "id": 20,
        "amount": 13.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:10:49.414302"
    },
    {
        "id": 21,
        "amount": 13.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T13:45:33.873531"
    },
    {
        "id": 22,
        "amount": 13.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T13:45:35.455034"
    },
    {
        "id": 23,
        "amount": 13.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T13:45:36.390855"
    },
    {
        "id": 24,
        "amount": 13.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T13:45:37.324064"
    },
    {
        "id": 25,
        "amount": 13.50,
        "description": "new credit",
        "createdAt": "2024-01-29T21:15:13.72703"
    }
]
```

note that pagination here is required if the request is sent without the page query parameter

```json
{
    "message": "Specify the page",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:31:36.69374047Z"
}
```

there is also an optional start_date, end_date query parameters to filter credits based on dates and shift_id to filter based on a specific shift 

if sent with the wrong date time format

```json
{
    "message": "Wrong date format, the correct format is yyyy-MM-ddTHH:mm:ss",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T14:01:28.958102856Z"
}
```


### Updating a credit 

URL PUT /api/v1/credit/update

Body:

```json
{
    "id":12,
    "amount":444.5,
    "description":"updated",
    "shift":{
        "id": 15
    }
}
```

all the fields are optional so the user can update only the fields he requires

response

```json
{
    "amount": 444.5,
    "description": "updated",
    "createdAt": "2024-01-29T21:15:13.72703",
    "id": 25,
    "user": "mohamed"
}
```
If the credit does not exist

```json
{
    "message": "No Credit with that ID",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:48:11.360394174Z"
}
```

if the user trying to modify the credit is not the one who created it

```json
{
    "message": "Only the user who created the credit can modify it",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:49:17.90073539Z"
}
```

If the new shift is closed 

```json
{
    "message": "This Shift is closed open it first and then modify",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:50:07.590163355Z"
}
```

### Deleting a credit

URL DELETE /api/v1/credit/delete/{credit ID}

you will receive an empty response with a status of 204 No Content

if the user trying to delete is not the one who created the credit

```json
{
    "message": "Only the user who created the credit can delete it",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:52:30.98539982Z"
}
```

if the credit does not exist

```json
{
    "message": "No credit with that ID",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-29T19:53:20.789739619Z"
}
```


## Expense module
An Expense is money that the person responsible for shift has bought necessities with .
An expense has:
- user: A FK Indicating who the user who created the expense is, and he will be responsible for it
- amount: a BigDecimal of the amount of the expense.
- description: a String indicating any other information about the expense
- createdAt: a LocalDateTime  indicating the exact time the expense was created at.
- shift: a FK indicating which shift this expense belongs to.

All the upcoming requests must be sent with jwt token in Authorization Headers
user has its getter overriden to return only the username
shift is annotated with @JsonBackReference and fetched lazily because we don't want to access the shift information from this side

### Adding a new expense

URL POST /api/v1/expense/add

body

```json
{
    "amount":12.5,
    "description":"new expense",
    "shift":{
        "id": 15
    }
}
```

response

```json
{
    "id": 25,
    "user": "mohamed",
    "amount": 12.5,
    "description": "new expense",
    "createdAt": "2024-01-30T15:47:51.671672166"
}
```

if any of the required fields are not present in the body

```json
{
    "message": "You must enter a Shift id, amount cannot bel blank or null",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T13:48:58.522559849Z"
}
```

if the shift does not exist

```json
{
    "message": "No Shift with that Id",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T13:49:32.733505562Z"
}
```

if the shift is closed

```json
{
    "message": "This Shift is closed open it first and then modify",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T13:55:26.671760264Z"
}
```

### Listing expenses

URL GET /api/v1/expense/list?page=0

response:

```json
[
    {
        "id": 16,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:10:31.833369"
    },
    {
        "id": 17,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:10:32.901406"
    },
    {
        "id": 18,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:10:33.800461"
    },
    {
        "id": 19,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:11:35.881764"
    },
    {
        "id": 20,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:12:34.404652"
    },
    {
        "id": 21,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:13:23.721623"
    },
    {
        "id": 22,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:14:27.579661"
    },
    {
        "id": 23,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:27:50.127319"
    },
    {
        "id": 24,
        "amount": 12.50,
        "description": "adfadcad",
        "createdAt": "2024-01-15T09:28:54.58553"
    },
    {
        "id": 25,
        "amount": 12.50,
        "description": "new expense",
        "createdAt": "2024-01-30T15:47:51.671672"
    }
]
```

Pagination is mandatory if you sent the request without the page query parameter

```json
{
    "message": "Specify the page",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T13:57:30.635129952Z"
}
```

there is also an optional start_date, end_date query parameters to filter credits based on dates and shift_id to filter based on a specific shift 

if sent with the wrong date time format

```json
{
    "message": "Wrong date format, the correct format is yyyy-MM-ddTHH:mm:ss",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T14:01:28.958102856Z"
}
```

### Retrieving an expense

URL GET /api/v1/expense/retrieve/{expense ID}

response

```json
    "id": 25,
    "user": "mohamed",
    "amount": 12.50,
    "description": "new expense",
    "createdAt": "2024-01-30T15:47:51.671672"
}
```

if there is no expense with the specified ID

```json
{
    "message": "No Expense with that ID",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T14:17:25.251085624Z"
}
```

### Update an expense

URL PUT /api/v1/expense/update 

Body

```json
{
  "id": 25,
  "amount": 1444.50,
  "description": "updated",
  "createdAt": "2024-12-30T13:40:50.520532",
  "shift":{
    "id": 15
  }
}
```

all the fields are optional so the user can update only the fields he requires


response

```json
{
    "id": 25,
    "user": "mohamed",
    "amount": 1444.50,
    "description": "updated",
    "createdAt": "2024-01-30T15:47:51.671672"
}
```

Note that created at did not update

if there is no credit with that ID

```json
{
    "message": "No Expense with that ID",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T17:16:19.559175325Z"
}
```

if the user who sent the request is not the one who created it

```json
{
    "message": "Only the user who created the expense can modify it",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T17:17:32.346811873Z"
}
```

if the shift does not exist

```json
{
    "message": "No Shift by that ID",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T17:21:53.74320946Z"
}
```

if the shift is closed

```json
{
    "message": "This Shift is closed open it first and then modify",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T17:22:16.727909752Z"
}
```

### Deleting a expense

URL DELETE /api/v1/expense/delete/{expense ID}

response empty with a status code of http 204 no content


if no expense with that ID


```json
{
    "message": "No expense with that ID",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T17:23:38.46405758Z"
}
```


If the user who sent the request is not the user who created the expense
```json
{
    "message": "Only the user who created the expense can delete it",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-01-30T17:24:09.810565304Z"
}
```

## Order module
An Order is an item that a customer has purchases it comes in the form of an external excel sheet that comes from another system and is attached to a shift.
An Order has:
- orderId: an Integer indicating the ID of that order in the external system
- createdAt: a LocalDateTime indicating the time which this order was created at.
- amount: a BigDecimal Indicating the amount of the order 

All the upcoming requests must be sent with jwt token in Authorization Headers
shift is annotated with @JsonBackReference and fetched lazily because we don't want to access the shift information from this side


### Adding Orders From Excel

URL POST /api/v1/order/add

Body Form Data
file : excel file
shift_id: id of the shift 

Response:
Executed
status code:
200 Ok

The method to add data from the Excel file executes in a separate thread so the response will be immediate and when a successful operation is done a Fire Base Push Notification is sent to the user who created the request
also if there is any errors in the file, like missing values, a push notification will be sent as will. See the section about configuring Fire Base Push Notification.

If there is no file sent with the request
```json
{
    "message": "File is required in the request",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T10:59:49.785299919Z"
}
```

If there is no shift_id sent with request
```json
{
    "message": "Shift ID is required in the request",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T11:00:28.391588119Z"
}
```

If the file is not in excel format
```json
{
    "message": "The input file must be excel format",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T11:01:19.436810649Z"
}
```

If there is no shift by that ID
```json
{
    "message": "No shift is by that id",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T11:01:44.401779319Z"
}
```

If the shift is closed 
```json
{
    "message": "This Shift is closed modify it first",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T11:02:04.846839378Z"
}
```

### Listing Orders

URL GET /api/v1/order/list?page={Page number}

response

```json
[
    {
        "id": 627,
        "orderId": 2381,
        "createdAt": "2023-12-19T19:23:34",
        "amount": 10.00
    },
    {
        "id": 628,
        "orderId": 2380,
        "createdAt": "2023-12-19T19:17:55",
        "amount": 40.00
    },
    {
        "id": 629,
        "orderId": 2379,
        "createdAt": "2023-12-19T19:11:42",
        "amount": 10.00
    },
    {
        "id": 630,
        "orderId": 2378,
        "createdAt": "2023-12-19T19:00:55",
        "amount": 15.00
    },
    {
        "id": 631,
        "orderId": 2377,
        "createdAt": "2023-12-19T18:59:14",
        "amount": 10.00
    },
    {
        "id": 632,
        "orderId": 2376,
        "createdAt": "2023-12-19T18:51:10",
        "amount": 30.00
    },
    {
        "id": 633,
        "orderId": 2375,
        "createdAt": "2023-12-19T18:49:28",
        "amount": 10.00
    },
    {
        "id": 634,
        "orderId": 2374,
        "createdAt": "2023-12-19T18:48:55",
        "amount": 30.00
    },
    {
        "id": 635,
        "orderId": 2373,
        "createdAt": "2023-12-19T18:28:50",
        "amount": 25.00
    },
    {
        "id": 636,
        "orderId": 2372,
        "createdAt": "2023-12-19T18:27:57",
        "amount": 5.00
    }
]
```

Paging is mandatory otherwise you will receive this

```json
{
    "message": "Specify the page",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T11:15:20.56692394Z"
}
```

also there is start_date, end_date and shift_id optional query params to filter by

### Updating an order

There is no option to delete an order because that is money incoming so to limit foul play you can only update it and make it amount zero if it is deleted in the external system.

URL PUT 
Body:
```json
{
    "id": 635,
    "orderId": 2391,
    "createdAt": "2023-12-19T22:56:43",
    "amount": 24444.00
}
```

response:

```json
{
    "id": 635,
    "orderId": 2373,
    "createdAt": "2023-12-19T18:28:50",
    "amount": 24444.00
}
```

note that the createdAt and orderId does not change.

if there is no order with that ID

```json
{
    "message": "No Order with that ID",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T11:19:46.066796068Z"
}
```

if shift is closed

```json
{
    "message": "This Shift is closed open it first and then modify",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T14:54:19.434953268Z"
}
```

### Retrieve Order

URL GET /api/v1/order/retrieve/{order ID}

response:

```json
{
    "id": 638,
    "orderId": 2370,
    "createdAt": "2023-12-19T18:12:00",
    "amount": 50.00
}
```

if no order exist with that ID

```json
{
    "message": "No Order with that Id",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2024-02-06T14:57:24.901872602Z"
}
```

## Pay Module
A Pay is the monthly pay that the users receive it is calculated by adding all the hours a user has worked in his shifts and then multiply that by his hourly rate which we get from the user table it also calculates his total
deduction and the totality of the hours he worked at, all of that is calculated by a scheduler that runs once every month and alerts the users that their pay is calculated by a Fire Base push notification.
A Pay has:
- totalHours: an Integer indicating the totality of the hours he worked this month
- totalPay: a BigDecimal indicating his total pay.
- totalDeduction: a BigDecimal indicating his total deduction
- createdAt: a LocalDate indicating the date
- user: the user who his pay is being calculated
- shifts: a one-to-many relationship indicating the shifts he worked at








