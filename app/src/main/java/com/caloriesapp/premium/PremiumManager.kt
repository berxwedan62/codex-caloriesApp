package com.caloriesapp.premium

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Adapter contract for external billing providers.
 *
 * A future Google Play Billing implementation should only need to implement
 * this contract and call [PremiumManager.refreshEntitlements].
 */
interface BillingEntitlementSource {
    suspend fun fetchSubscriptionTier(): SubscriptionTier
}

/**
 * Premium + feature flag entry-point.
 *
 * Keeps current app logic detached from billing SDKs and purchase APIs.
 */
class PremiumManager(
    private val billingEntitlementSource: BillingEntitlementSource? = null,
) : FeatureGate {

    private val mutableTier = MutableStateFlow(SubscriptionTier.FREE)
    override val tier: StateFlow<SubscriptionTier> = mutableTier.asStateFlow()

    override fun isEnabled(feature: PremiumFeature): Boolean {
        val currentTier = tier.value
        return when (feature) {
            PremiumFeature.CSV_EXPORT,
            PremiumFeature.CUSTOM_ADVANCED_TARGETS,
            PremiumFeature.UNLIMITED_FAVORITES,
            PremiumFeature.ADVANCED_WEEKLY_INSIGHTS,
            -> currentTier == SubscriptionTier.PREMIUM
        }
    }

    fun favoritesLimit(): Int = when (tier.value) {
        SubscriptionTier.FREE -> FREE_FAVORITES_LIMIT
        SubscriptionTier.PREMIUM -> Int.MAX_VALUE
    }

    suspend fun refreshEntitlements() {
        val source = billingEntitlementSource ?: return
        mutableTier.value = source.fetchSubscriptionTier()
    }

    /**
     * Temporary local switch for QA/dev builds until billing is integrated.
     */
    fun setTierForDebug(newTier: SubscriptionTier) {
        mutableTier.value = newTier
    }

    companion object {
        const val FREE_FAVORITES_LIMIT = 10
    }
}
