# CharityTrades

A donation platform that mimics real brokerage/exchange mechanics.

## Finance Concepts (in lifecycle order)

| Finance Concept | CharityTrades Equivalent |
|-----------------|--------------------------|
| Brokerage | CharityTrades (manages accounts, validates, routes) |
| Buy Order (Bid) | Donation order from personal account |
| Sell Order (Ask) | Matching pledge from corporate account |
| Price | Match ratio (1:1, 2:1, etc.) |
| Credit Risk Manager | Validates order (amount >= minimum) |
| Order Router | Decides: route to CLOB or direct to GlobalGiving |
| Exchange / CLOB | Central Limit Order Book (bids = donations, asks = pledges) |
| Matching Engine | Pairs donations with corporate matchers |
| Clearing | Post-trade verification |
| Settlement | User clicks donation link, order marked SETTLED |

## Account Types

- **PERSONAL** - Can donate. Cannot create matching pledges.
- **CORPORATE** - Can donate. CAN create matching pledges (asks on CLOB).

## Order Lifecycle

```
NEW → VALIDATED → ROUTED → MATCHED/EXECUTED → CLEARING → SETTLED
 │        │          │            │               │          │
 │        │          │            │               │          └─ Complete
 │        │          │            │               └─ Post-trade verification
 │        │          │            └─ Paired with matcher (or direct)
 │        │          └─ Router picked CLOB or DIRECT
 │        └─ Credit Risk Manager (pre-trade check)
 └─ Order submitted
```

## How It Works

### Phase 1: Place Order

```
POST /api/orders { userId, projectId, amount: 50 }
```

This is NOT donating yet. This says: "I intend to donate $50"

System does:
- **NEW** → Order created
- **VALIDATED** → Credit Risk Manager checks amount >= minimum
- **ROUTED** → Router checks for matchers, picks CLOB or DIRECT
- **MATCHED** → Matching engine pairs with best corporate matcher

Response includes status, matchedAmount, totalImpact, and donationLink.

### Phase 2: Donate

User clicks donationLink, goes to GlobalGiving, donates with credit card.

### Phase 3: Settle

```
POST /api/orders/{id}/settle
```

System does:
- **CLEARING** → Post-trade verification
- **SETTLED** → Order complete

## Routing Logic

```
         Order comes in
               │
               ▼
    Any matchers for project?
               │
       ┌───────┴───────┐
       │               │
      YES              NO
       │               │
       ▼               ▼
   Route: CLOB     Route: DIRECT
       │               │
       ▼               ▼
    MATCHED         EXECUTED
```

## Tech Stack

- **Backend**: Java, Spring Boot
- **Database**: PostgreSQL
- **Frontend**: React (via CDN)
- **External API**: GlobalGiving

## Project Structure

```
src/main/java/com/charitytrades/
├── entity/
│   ├── User.java           # PERSONAL or CORPORATE account
│   ├── Order.java          # Donation order (bid)
│   ├── MatchingOrder.java  # Corporate pledge (ask)
│   ├── CentralBook.java    # CLOB with bids[] and asks[]
│   ├── OrderStatus.java    # NEW/VALIDATED/ROUTED/MATCHED/EXECUTED/CLEARING/SETTLED
│   └── RouteType.java      # CLOB or DIRECT
├── service/
│   ├── ExchangeService.java    # Core logic: validate → route → match → settle
│   └── OrderRouter.java        # Decides CLOB vs DIRECT
├── controller/
│   ├── OrderController.java           # POST /api/orders
│   └── MatchingOrderController.java   # POST /api/matching-orders
└── config/
    └── DataInitializer.java    # Loads projects from GlobalGiving on startup

frontend/
└── index.html    # React app (single file, CDN)
```

## Running

1. Start PostgreSQL
2. Create database: `createdb charitytrades`
3. Run backend: `./mvnw spring-boot:run`
4. Open frontend: `frontend/index.html` in browser

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/projects | List all projects |
| GET | /api/users | List all users |
| POST | /api/users | Create user |
| POST | /api/orders | Place donation order |
| POST | /api/orders/{id}/settle | Settle order after donating |
| POST | /api/matching-orders | Create corporate pledge |
| GET | /api/matching-orders | List all pledges |
