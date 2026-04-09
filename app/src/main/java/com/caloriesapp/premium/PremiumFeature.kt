package com.caloriesapp.premium

/**
 * Features that can be toggled by the entitlement layer.
 */
enum class PremiumFeature {
    CSV_EXPORT,
    CUSTOM_ADVANCED_TARGETS,
    UNLIMITED_FAVORITES,
    ADVANCED_WEEKLY_INSIGHTS,
}

/**
 * User subscription level.
 */
enum class SubscriptionTier {
    FREE,
    PREMIUM,
}
