package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TaxBgDark
import com.example.ui.theme.TaxBorder
import com.example.ui.theme.TaxCyanAccent
import com.example.ui.theme.TaxGreenSuccess
import com.example.ui.theme.TaxPanelElevated
import com.example.ui.theme.TaxTextPrimary
import java.util.Locale

@Composable
fun TaxRashesTopBar(
    walletBalance: Double,
    unreadNotifications: Int,
    onWalletClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAdminClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF03060E),
                        TaxBgDark
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = TaxTextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            letterSpacing = 1.5.sp
                        )
                    ) {
                        append("TAX ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = TaxCyanAccent,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            letterSpacing = 1.5.sp
                        )
                    ) {
                        append("RASHES")
                    }
                },
                modifier = Modifier.testTag("brand_logo_title")
            )

            Spacer(modifier = Modifier.weight(1f))

            // Quick Wallet Chip
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(TaxPanelElevated)
                    .border(1.dp, TaxBorder, RoundedCornerShape(20.dp))
                    .clickable { onWalletClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("topbar_wallet_chip"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(TaxGreenSuccess.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "৳",
                        color = TaxGreenSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = String.format(Locale.US, "৳%.2f", walletBalance),
                    color = TaxTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Notifications
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TaxPanelElevated)
                    .border(1.dp, TaxBorder, CircleShape)
                    .testTag("topbar_notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotifications > 0) {
                            Badge(
                                containerColor = TaxCyanAccent,
                                contentColor = Color(0xFF050914)
                            ) {
                                Text(
                                    text = "$unreadNotifications",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = TaxTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Admin / Moderator Demo Controls
            IconButton(
                onClick = onAdminClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TaxPanelElevated)
                    .border(1.dp, TaxBorder, CircleShape)
                    .testTag("topbar_admin_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AdminPanelSettings,
                    contentDescription = "Admin Panel",
                    tint = TaxCyanAccent,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}
