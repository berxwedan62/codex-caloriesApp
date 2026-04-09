# Lokma

Lokma is a full-stack calorie tracking and meal journaling platform designed to help users log food quickly, monitor nutrition, and build healthy eating habits over time.

This README is intended to give a developer a fast, practical understanding of the project direction, technical design, and how to run and extend it.

---

## 1) Project Overview

### Goal
Lokma aims to make daily nutrition tracking simple and consistent by combining:
- fast food logging,
- clear daily/weekly progress views,
- intelligent suggestions,
- and (later) premium features for advanced analytics and coaching.

### Core Product Vision
The product is built around one core loop:
1. User logs meals/snacks.
2. System calculates calories and macro estimates.
3. User views progress vs targets.
4. User gets nudges and insights to improve consistency.

### Target Users
- People trying to lose, gain, or maintain weight.
- Users who want a lightweight daily food log without complexity.
- Users who want advanced insights later via a premium tier.

---

## 2) Tech Stack

> **Note:** The project is currently at an early scaffold stage. The stack below defines the intended/standardized implementation for Lokma.

### Frontend
- **React + TypeScript** for a strongly-typed UI.
- **Vite** for fast local development and builds.
- **Tailwind CSS** (or equivalent utility styling) for rapid interface iteration.

### Backend
- **Node.js + Express (TypeScript)** for API services.
- **REST API** as initial integration layer (GraphQL can be evaluated later).
- **JWT-based authentication** for secure user sessions.

### Data & Storage
- **PostgreSQL** as primary relational database.
- **Prisma ORM** for schema management and type-safe data access.
- Optional **Redis** for caching and rate-limiting support as scale grows.

### Infrastructure / DevOps
- **Docker + Docker Compose** for local parity and reproducible environments.
- **GitHub Actions** for CI (lint, test, build).
- Cloud deployment target: container-friendly platform (e.g., Render, Fly.io, AWS ECS, or similar).

---

## 3) Architecture

Lokma follows a modular client-server architecture.

### High-Level Flow
- **Client (Web App)** handles UI, input, and visualization.
- **API Layer** handles auth, validation, business rules.
- **Data Layer** persists users, meals, goals, and history.
- **Analytics Layer (planned)** aggregates trends and generates actionable insights.

### Logical Components
1. **Auth Module**
   - Signup/login/logout
   - Token issuance/refresh
   - Access control middleware

2. **Nutrition Module**
   - Food entry management
   - Meal/day calorie aggregation
   - Macro calculations (protein/carbs/fat)

3. **Goal Module**
   - Calorie target setup
   - Daily and weekly completion logic
   - Streak/consistency indicators

4. **Insight Module (planned)**
   - Trend analysis
   - Habit suggestions
   - Premium intelligence features

### Design Principles
- Clear separation between domain logic and transport/API handlers.
- Type-safe contracts between frontend and backend.
- Extensible modules to support premium without reworking core flows.

---

## 4) Folder Structure

Because the repository is currently minimal, this is the **recommended canonical structure** for implementation:

```text
lokma/
├─ README.md
├─ .env.example
├─ docker-compose.yml
├─ package.json
├─ apps/
│  ├─ web/                 # Frontend app (React + TS)
│  │  ├─ src/
│  │  │  ├─ components/
│  │  │  ├─ pages/
│  │  │  ├─ hooks/
│  │  │  ├─ services/
│  │  │  └─ types/
│  │  └─ ...
│  └─ api/                 # Backend app (Node + Express + TS)
│     ├─ src/
│     │  ├─ modules/
│     │  │  ├─ auth/
│     │  │  ├─ nutrition/
│     │  │  ├─ goals/
│     │  │  └─ insights/
│     │  ├─ middleware/
│     │  ├─ utils/
│     │  └─ server.ts
│     ├─ prisma/
│     │  ├─ schema.prisma
│     │  └─ migrations/
│     └─ ...
├─ packages/
│  ├─ shared-types/        # Shared DTOs/interfaces
│  └─ ui/                  # Shared UI primitives (optional)
└─ docs/
   ├─ architecture.md
   ├─ api-contracts.md
   └─ roadmap.md
```

---

## 5) How to Run (Developer Setup)

### Prerequisites
- Node.js 20+
- npm / pnpm / yarn
- Docker (optional but recommended)
- PostgreSQL (if not using Docker)

### Environment Variables
Create `.env` files from `.env.example` once available.

Typical variables:
- `DATABASE_URL`
- `JWT_SECRET`
- `PORT`
- `VITE_API_BASE_URL` (frontend)

### Local Run (Planned Standard Workflow)

```bash
# 1) Install dependencies
npm install

# 2) Start required services (db, cache) if using docker
docker compose up -d

# 3) Run database migrations
npm run db:migrate

# 4) Start backend + frontend (monorepo script)
npm run dev
```

### Suggested Scripts
- `npm run dev` → run web + api in watch mode
- `npm run build` → production build
- `npm run test` → execute test suites
- `npm run lint` → static checks

---

## 6) Main Features

### Implemented / Core Scope
- User account and authentication flow
- Daily food logging and meal management
- Calorie and macro calculation per meal/day
- Daily target tracking with visual progress

### Product UX Features
- Quick-add food entry workflow
- Clear dashboard for “today at a glance”
- Historical log for pattern review

### Quality Features
- Input validation and API error handling
- Type-safe payloads between client and server
- Modular code organization for maintainability

---

## 7) Future Roadmap

### Phase 1 — Foundation
- Stable auth and profile system
- Food log CRUD and target tracking
- Baseline dashboard and history pages

### Phase 2 — Intelligence
- Better nutrition suggestions
- Weekly trend analytics
- Streaks and behavioral nudges

### Phase 3 — Premium Platform
- Subscription billing integration
- Advanced analytics and personalization
- Optional coach-oriented or export workflows

### Phase 4 — Ecosystem
- Mobile app support
- Wearable / health platform integrations
- International food database expansion

---

## 8) Known Limitations

At the current repository state:
- The codebase is not fully scaffolded yet (this README defines the target structure).
- End-to-end workflows are not wired in this repository snapshot.
- Production deployment manifests and CI workflows are not yet committed.
- Seed data and realistic fixture sets are not yet available.

As development progresses, each limitation should be tracked via issues and milestones.

---

## 9) Premium Plan Strategy (How Premium Will Be Added)

Premium is designed to be added incrementally without destabilizing the free core experience.

### Monetization Model
- **Free Tier:** core logging + basic progress.
- **Premium Tier:** advanced analytics, deeper insights, personalization, and optional coaching features.

### Technical Rollout Plan
1. **Plan & Entitlements Model**
   - Add `plans`, `subscriptions`, and `entitlements` tables.
   - Represent capabilities as feature flags (e.g., `advanced_analytics`, `ai_insights`).

2. **Billing Integration**
   - Integrate with a payment provider (e.g., Stripe).
   - Handle checkout, subscription lifecycle webhooks, and status sync.

3. **Authorization Layer for Features**
   - Add server-side guards to enforce entitlements.
   - Add client-side gating to expose/lock premium UI sections cleanly.

4. **Graceful Degradation**
   - Premium-only endpoints return clear, structured upgrade responses.
   - Free users always retain access to baseline historical data.

5. **Observability & Experimentation**
   - Track conversion funnel events.
   - Run A/B tests for paywall placement and messaging.

### Premium Candidate Features
- Advanced macro/cycle insights
- Smart meal recommendations and pattern alerts
- Goal forecast projections
- Data export and enhanced reporting

---

## 10) Contributing Guidelines (Initial)

- Keep modules cohesive by domain (`auth`, `nutrition`, `goals`, etc.).
- Prefer explicit, typed contracts for requests/responses.
- Add tests for any business logic change.
- Update this README and/or `docs/` whenever architecture or workflows change.

---

## 11) Status

This repository currently contains initial scaffolding only. This document serves as the canonical project brief and implementation blueprint until full code modules are added.
