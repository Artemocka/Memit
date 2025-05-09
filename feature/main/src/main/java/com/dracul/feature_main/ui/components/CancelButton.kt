package com.dracul.feature_main.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dracul.common.aliases.CommonStrings

@Composable
fun CancelButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(stringResource(CommonStrings.action_cancel))
    }
}

@Preview
@Composable
private fun CancelButtonPreview() {
    CancelButton { }
}