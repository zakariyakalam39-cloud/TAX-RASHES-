package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.TaxBluePrimary
import com.example.ui.theme.TaxBorder
import com.example.ui.theme.TaxCyanAccent
import com.example.ui.theme.TaxDanger
import com.example.ui.theme.TaxGreenSuccess
import com.example.ui.theme.TaxPanelDark
import com.example.ui.theme.TaxPanelElevated
import com.example.ui.theme.TaxTextMuted
import com.example.ui.theme.TaxTextPrimary
import com.example.ui.theme.TaxTextSecondary

/**
 * Professional Admin Login Dialog with password authentication,
 * error state handling, visibility toggle, and default demo auto-fill.
 */
@Composable
fun AdminLoginDialog(
    onDismiss: () -> Unit,
    onSubmitLogin: (password: String) -> Unit,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val adminEmail = "admin@taxrashes.com"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(22.dp))
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(listOf(TaxCyanAccent, TaxBluePrimary)),
                    shape = RoundedCornerShape(22.dp)
                )
                .testTag("admin_login_dialog"),
            color = TaxPanelDark,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header with Lock Icon and Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(TaxCyanAccent.copy(alpha = 0.15f))
                                .border(1.dp, TaxCyanAccent.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Admin Security",
                                tint = TaxCyanAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "ADMIN AUTHENTICATION",
                                color = TaxTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Restricted Console Access",
                                color = TaxCyanAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("admin_login_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TaxTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter master administrator credentials to access tournament moderation, prize distributions, and room management.",
                    color = TaxTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Admin Account Display (Read-only / Predefined)
                OutlinedTextField(
                    value = adminEmail,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Admin Account") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = TaxCyanAccent
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_email_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TaxPanelElevated,
                        unfocusedContainerColor = TaxPanelElevated,
                        focusedBorderColor = TaxBorder,
                        unfocusedBorderColor = TaxBorder,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextMuted
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Password Input Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Admin Password") },
                    placeholder = { Text("Enter administrator password", color = TaxTextMuted) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (errorMessage != null) TaxDanger else TaxCyanAccent
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.testTag("admin_password_toggle")
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = TaxTextSecondary
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (password.isNotBlank()) {
                                onSubmitLogin(password)
                            }
                        }
                    ),
                    isError = errorMessage != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TaxPanelElevated,
                        unfocusedContainerColor = TaxPanelElevated,
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        errorBorderColor = TaxDanger,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary,
                        focusedLabelColor = TaxCyanAccent,
                        unfocusedLabelColor = TaxTextMuted
                    )
                )

                // Error message
                AnimatedVisibility(visible = errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = TaxDanger,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(top = 6.dp, start = 4.dp)
                            .testTag("admin_login_error_text")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Demo Autofill Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(TaxPanelElevated)
                        .border(1.dp, TaxBorder, RoundedCornerShape(10.dp))
                        .clickable { password = "admin123" }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("admin_quick_fill_pill"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = TaxCyanAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Default Password: ",
                            color = TaxTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "admin123",
                            color = TaxCyanAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Auto Fill",
                        color = TaxCyanAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("admin_login_cancel_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                    ) {
                        Text(
                            text = "Cancel",
                            color = TaxTextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { onSubmitLogin(password) },
                        enabled = password.isNotBlank(),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("admin_login_submit_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TaxCyanAccent,
                            disabledContainerColor = TaxPanelElevated
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = if (password.isNotBlank()) Color(0xFF050914) else TaxTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Login as Admin",
                            color = if (password.isNotBlank()) Color(0xFF050914) else TaxTextMuted,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dialog enabling administrators to change the current login password.
 */
@Composable
fun ChangeAdminPasswordDialog(
    onDismiss: () -> Unit,
    onSubmitChange: (currentPass: String, newPass: String, confirmPass: String) -> Unit,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPasswords by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(22.dp))
                .border(1.5.dp, TaxBorder, RoundedCornerShape(22.dp))
                .testTag("change_admin_password_dialog"),
            color = TaxPanelDark,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHANGE ADMIN PASSWORD",
                        color = TaxCyanAccent,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TaxTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    singleLine = true,
                    visualTransformation = if (showPasswords) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("current_admin_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TaxPanelElevated,
                        unfocusedContainerColor = TaxPanelElevated,
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password (min 4 chars)") },
                    singleLine = true,
                    visualTransformation = if (showPasswords) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_admin_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TaxPanelElevated,
                        unfocusedContainerColor = TaxPanelElevated,
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm New Password") },
                    singleLine = true,
                    visualTransformation = if (showPasswords) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("confirm_admin_password_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TaxPanelElevated,
                        unfocusedContainerColor = TaxPanelElevated,
                        focusedBorderColor = TaxCyanAccent,
                        unfocusedBorderColor = TaxBorder,
                        focusedTextColor = TaxTextPrimary,
                        unfocusedTextColor = TaxTextPrimary
                    )
                )

                AnimatedVisibility(visible = errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = TaxDanger,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TaxBorder)
                    ) {
                        Text("Cancel", color = TaxTextSecondary)
                    }

                    Button(
                        onClick = { onSubmitChange(currentPassword, newPassword, confirmPassword) },
                        enabled = currentPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank(),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_admin_password_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TaxGreenSuccess)
                    ) {
                        Text("Save Password", color = Color(0xFF00150E), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
