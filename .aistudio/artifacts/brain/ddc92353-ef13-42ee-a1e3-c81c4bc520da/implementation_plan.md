# TAX RASHES - Enhanced Main Dashboard UI

An upgraded, high-energy main dashboard UI for TAX RASHES built with Jetpack Compose. The revamped dashboard combines an esports hero banner, a minimalist gradient wallet & prize balance card with instant deposit actions, a hybrid tournament section (featured live match carousel + filter tabs for Live, Solo, Duo, Squad + one-tap register cards), and an in-dashboard top-up diamond shop showcase equipped with instant server region selection.

### User Review & Critical Decisions

> [!IMPORTANT]
> The following decisions were confirmed based on your preferences:
> - **Tournament Listings**: Hybrid design combining a featured match highlight carousel, quick category filter tabs (*All*, *Live Now*, *Solo*, *Duo*, *Squad*), and compact tournament cards with one-tap registration and live room access.
> - **Wallet Balance Display**: Minimalist cyber gradient balance card displaying Main Balance, Prize Winnings, a hide/show balance toggle, and an instant "+ Deposit" shortcut button.
> - **Top-Up Shop Section**: Full interactive in-dashboard shop showcase featuring an instant server region selector (*Bangladesh*, *India*, *Singapore*, *Global*), diamond package tiers with bonus tags, and 1-tap checkout.

---

### 1. Overview & Core Concept

- **What It Does**: Transforms the TAX RASHES home view into an all-in-one tournament and top-up dashboard where players can immediately monitor their esports funds, register for upcoming matches or join live rooms, and purchase Free Fire diamond packages without navigating away from the main screen.
- **Target Audience**: Mobile competitive esports gamers and tournament participants looking for fast access to scrim schedules, room credentials, and instant diamond top-ups.
- **Key Value**: Delivers the entire core player journey (Balance -> Scrims -> Diamonds) on a single responsive screen while retaining the existing 5-tab navigation for deep dives.

---

### 2. User Experience & Visual Design

#### Dashboard Layout Hierarchy
1. **Header & Minimalist Gradient Wallet Card**:
   - Sleek horizontal gradient card displaying the player's available ৳ wallet balance with an eye toggle to hide/show amounts.
   - Quick pill displaying accumulated prize winnings with an emerald glow.
   - High-contrast primary "+ Add Funds" button opening the bKash/Nagad/Rocket deposit modal.
2. **Live Match Highlights & Tournament Section**:
   - *Active Ticker / Live Carousel*: Prominent visual alert when matches are currently LIVE or rooms are open.
   - *Filter Pills*: Instant tabs for *All Matches*, *Live Now*, *Solo*, *Duo*, and *Squad*.
   - *Tournament Cards*: Display slots filled with dynamic progress bars, entry fee, prize pool in ৳, and clear action buttons (*Register* for upcoming matches, *Room Code* for live matches).
3. **Interactive Top-Up Shop Showcase**:
   - *Server Region Selector*: Interactive row (*Bangladesh*, *India*, *Singapore*, *Global*) dynamically filtering or pre-filling region metadata.
   - *Diamond Package Grid / Cards*: Diamond icons, package names (115, 240, 610, 1240, Weekly/Monthly passes), bonus badges (e.g. *+60 Free*), and direct price buttons triggering the instant UID purchase sheet.
4. **Recent Activity & Quick Links**:
   - Compact ledger snippet showing recent tournament entries and deposits with status pills.

#### Visual Polish & Aesthetics
- **Color Tokens**:
  - Dark Navy Background: `#050914`
  - Deep Cyber Panel: `#0B1426`
  - Elevated Container: `#101D35`
  - Electric Primary Blue: `#1769FF`
  - Cyber Cyan Accent: `#24A8FF`
  - Live Alert Crimson: `#FF5571`
  - Emerald Prize Green: `#20D58A`
- **Micro-Interactions**:
  - Smooth spring transitions between filter tabs.
  - One-tap clipboard copy feedback for room IDs and account numbers.
  - Linear slot progress animation.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Full In-Dashboard Shop vs. Separate Tab Only**
  - *Chosen Approach*: Embed an interactive top-up shop section directly inside the main dashboard while keeping the dedicated Top-Up bottom tab for extended package details.
  - *Why*: Allows players who need quick diamonds before a tournament to complete their purchase directly from the home feed with zero extra screen transitions.

- **Decision 2: Minimalist Gradient Wallet Card with Privacy Toggle**
  - *Chosen Approach*: Streamlined gradient balance pill with a hide/reveal toggle (showing `৳•••` or `৳350.00`) and quick deposit button.
  - *Why*: Reduces vertical clutter at the top of the dashboard while giving users privacy in public spaces or stream recordings.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────┐
│                   TAX RASHES Main Dashboard            │
│ ┌────────────────────────────────────────────────────┐ │
│ │ Minimalist Gradient Wallet Card (Balance + Deposit)│ │
│ └────────────────────────────────────────────────────┘ │
│ ┌────────────────────────────────────────────────────┐ │
│ │ Featured Live Scrims Carousel & Active Room Ticker │ │
│ └────────────────────────────────────────────────────┘ │
│ ┌────────────────────────────────────────────────────┐ │
│ │ Category Filter Tabs (All, Live, Solo, Duo, Squad) │ │
│ └────────────────────────────────────────────────────┘ │
│ ┌────────────────────────────────────────────────────┐ │
│ │ Tournament Cards with 1-Tap Registration & Rooms   │ │
│ └────────────────────────────────────────────────────┘ │
│ ┌────────────────────────────────────────────────────┐ │
│ │ Interactive Top-Up Shop (Server Selector + Bundles)│ │
│ └────────────────────────────────────────────────────┘ │
└───────────────────────────┬────────────────────────────┘
                            ▼
               TaxRashesViewModel & Repository
                            ▼
               Room SQLite Database (Local State)
```

#### State & Component Integration
- `HomeScreen.kt` will be updated with the new modular dashboard sections:
  - `MinimalistWalletCard`: State for balance visibility (`isBalanceVisible`), quick deposit trigger, and prize winnings pill.
  - `DashboardTournamentSection`: Filter state (`selectedCategory`), carousel of live/urgent matches, and list of filtered scrims.
  - `DashboardShopSection`: Server region selector (`selectedServer`) and product cards with bonus badges.
- All actions connect directly to existing `TaxRashesViewModel` flows (`openJoinTournament`, `openRoomDetails`, `openTopUpOrder`, `setDepositDialogVisible`).
