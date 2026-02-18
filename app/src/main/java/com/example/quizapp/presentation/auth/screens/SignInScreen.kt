package com.example.quizapp.presentation.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizapp.R

val BgColor = Color(0xFFF2F3F7)
val PrimaryBlue = Color(0xFF4285F4)
val TextColorPrimary = Color(0xFF1D1D1D)
val TextColorSecondary = Color(0xFF7C8aa0)
val InputBgColor = Color.White

@Composable
fun SignInScreen(
    signInClick: (String, String) -> Unit = { _, _ -> },
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isRememberMeChecked by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BgColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextColorPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextColorSecondary
                )
            )

            Spacer(modifier = Modifier.height(48.dp))


            LabeledInput(
                label = stringResource(R.string.email_label),
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(R.string.email_placeholder),
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledInput(
                label = stringResource(R.string.password_label),
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(R.string.password_placeholder),
                keyboardType = KeyboardType.Password,
                isPassword = true,
                isPasswordVisible = isPasswordVisible,
                onVisibilityToggle = { isPasswordVisible = !isPasswordVisible }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isRememberMeChecked = !isRememberMeChecked }
                ) {
                    Checkbox(
                        checked = isRememberMeChecked,
                        onCheckedChange = { isRememberMeChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryBlue,
                            uncheckedColor = TextColorSecondary
                        )
                    )
                    Text(
                        text = stringResource(R.string.remember_me),
                        style = MaterialTheme.typography.bodySmall.copy(color = TextColorSecondary)
                    )
                }

                Text(
                    text = stringResource(R.string.forgot_password),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.clickable { onForgotPasswordClick() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { signInClick(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text(
                    text = stringResource(R.string.or_divider),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = TextColorSecondary
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SocialButton(
                    text = stringResource(R.string.google_login),
                    modifier = Modifier.weight(1f)
                )
                SocialButton(
                    text = stringResource(R.string.facebook_login),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.dont_have_account),
                    color = TextColorSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string.sign_up_action),
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}

@Composable
fun LabeledInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextColorPrimary
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(InputBgColor, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = InputBgColor,
                unfocusedContainerColor = InputBgColor
            ),
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = onVisibilityToggle) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Password",
                            tint = Color.Gray
                        )
                    }
                }
            } else null
        )
    }
}

@Composable
fun SocialButton(
    text: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {},
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier
                .size(24.dp)
                .background(Color.Gray, RoundedCornerShape(4.dp)))

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                color = TextColorPrimary,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignIn() {
    MaterialTheme {
        SignInScreen()
    }
}
