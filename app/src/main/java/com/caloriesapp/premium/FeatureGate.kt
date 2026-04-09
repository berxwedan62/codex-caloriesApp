package com.caloriesapp.premium

import kotlinx.coroutines.flow.StateFlow

/**
 * Centralized read-only interface for feature access checks.
 *
 * All UI and domain layers should query this abstraction so a future
 * billing backend can be swapped in with minimal refactoring.
 */
interface FeatureGate {
    val tier: StateFlow<SubscriptionTier>

    fun isEnabled(feature: PremiumFeature): Boolean

    fun isLocked(feature: PremiumFeature): Boolean = !isEnabled(feature)
}
