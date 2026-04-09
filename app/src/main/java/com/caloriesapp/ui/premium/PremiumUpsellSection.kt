package com.caloriesapp.ui.premium

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caloriesapp.features.FeatureCatalog
import com.caloriesapp.premium.FeatureGate

/**
 * Reusable section that shows placeholders only for features locked on free tier.
 */
@Composable
fun PremiumUpsellSection(
    gate: FeatureGate,
    onLearnMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val locked = FeatureCatalog.premiumFeatures.filter { gate.isLocked(it.feature) }
    if (locked.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Premium features",
            style = MaterialTheme.typography.titleLarge,
        )

        locked.forEach { descriptor ->
            LockedFeatureCard(
                descriptor = descriptor,
                onLearnMoreClick = onLearnMoreClick,
            )
        }
    }
}
