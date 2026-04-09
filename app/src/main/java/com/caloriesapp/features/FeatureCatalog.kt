package com.caloriesapp.features

import com.caloriesapp.premium.PremiumFeature

/**
 * Metadata used by the UI to display feature labels and lock placeholders.
 */
data class FeatureDescriptor(
    val feature: PremiumFeature,
    val title: String,
    val description: String,
    val premiumBadge: String = "Premium",
)

object FeatureCatalog {
    val premiumFeatures: List<FeatureDescriptor> = listOf(
        FeatureDescriptor(
            feature = PremiumFeature.CSV_EXPORT,
            title = "CSV export",
            description = "Export your logs as CSV files for desktop analysis.",
        ),
        FeatureDescriptor(
            feature = PremiumFeature.CUSTOM_ADVANCED_TARGETS,
            title = "Custom advanced targets",
            description = "Create multi-metric targets and custom threshold rules.",
        ),
        FeatureDescriptor(
            feature = PremiumFeature.UNLIMITED_FAVORITES,
            title = "Unlimited favorites",
            description = "Pin unlimited foods, meals, and templates to favorites.",
        ),
        FeatureDescriptor(
            feature = PremiumFeature.ADVANCED_WEEKLY_INSIGHTS,
            title = "Advanced weekly insights",
            description = "Unlock trend explanations and deeper weekly summaries.",
        ),
    )
}
