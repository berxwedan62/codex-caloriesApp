# Premium Feature Gating Architecture

This repository now includes a lightweight premium abstraction designed for future Google Play Billing integration.

## What was added

- `FeatureGate` interface: single read-only contract consumed by UI/domain code.
- `PremiumManager` implementation: stores current tier and evaluates premium flags.
- `BillingEntitlementSource` interface: seam for plugging Google Play Billing later.
- Premium feature catalog for:
  - CSV export
  - custom advanced targets
  - unlimited favorites
  - advanced weekly insights
- Compose UI placeholders (`LockedFeatureCard`, `PremiumUpsellSection`) for nicely locked states.

## Integration path for Google Play Billing

1. Implement `BillingEntitlementSource` with Play Billing purchase/subscription checks.
2. Inject implementation into `PremiumManager`.
3. Trigger `refreshEntitlements()` after app launch and purchase updates.
4. Keep all feature checks via `FeatureGate` (no direct SDK calls in UI/domain code).

Because all premium checks route through `FeatureGate`, swapping from debug/manual tiers to real billing should not require broad refactoring.
