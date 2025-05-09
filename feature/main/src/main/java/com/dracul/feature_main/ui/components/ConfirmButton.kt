package com.dracul.feature_main.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dracul.common.aliases.CommonStrings

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ConfirmButton(
    onClick: () -> Unit,
) {
    TextButton(onClick = onClick) {
        Text(stringResource(CommonStrings.action_ok))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun OkButtonPreview() {
    ConfirmButton {}
}