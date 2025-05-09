package com.dracul.feature_main.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            ConfirmButton {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }
        },
        dismissButton = { CancelButton(onDismiss) }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview
@Composable
private fun DatePicker() {
    DatePickerModal({}) { }
}
