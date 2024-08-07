package com.dracul.feature_main.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReminderBottomSheetRow(
    modifier: Modifier = Modifier, onClick: () -> Unit, text: String, icon: ImageVector
) {
    OutlinedIconButton(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, null
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp), text = text, fontSize = 16.sp
            )
        }
    }
}