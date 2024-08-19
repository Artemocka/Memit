package com.dracul.feature_main.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dracul.common.aliases.CommonStrings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ReminderBottomSheetWithDelete(
    onCreateReminder: (Calendar?) -> Unit, onDeleteReminder: () -> Unit
) {

    val currentTime = Calendar.getInstance()
    val notificationPermission = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )
    var showAlertPermissionDialog by remember {
        mutableStateOf(false)
    }
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )
    val date by remember {
        mutableStateOf(Calendar.getInstance())
    }
    var showTimePicker by remember {
        mutableStateOf(false)
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    val modalBottomSheetState = rememberModalBottomSheetState()
    var timeText by remember {
        mutableStateOf(normalizeTime(timePickerState.hour, timePickerState.minute))
    }
    val scope = rememberCoroutineScope()
    val formatter = SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
    var dateText by remember {
        mutableStateOf("Today")
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showAlertPermissionDialog = true
        }
    }

    if (showAlertPermissionDialog) {
        AlertPermissionDialog(context = LocalContext.current) {
            showAlertPermissionDialog = false
        }
    }

    if (showTimePicker) AdvancedTimePicker(onConfirm = {
        date.set(Calendar.HOUR_OF_DAY, it.hour)
        date.set(Calendar.MINUTE, it.minute)
        timeText = normalizeTime(it.hour, it.minute)
        showTimePicker = false
    }) {
        showTimePicker = false
    }

    if (showDatePicker) DatePickerModal(onDateSelected = {
        it?.let {
            currentTime.timeInMillis = it
            date.set(Calendar.YEAR, currentTime.get(Calendar.YEAR))
            date.set(Calendar.MONTH, currentTime.get(Calendar.MONTH))
            date.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH))
            dateText = formatter.format(date.time)
        }
    }) {
        showDatePicker = false
    }
    ModalBottomSheet(
        modifier = Modifier.windowInsetsPadding(WindowInsets(bottom = 0)),
        sheetState = modalBottomSheetState,
        onDismissRequest = {
            onCreateReminder(null)
        },
        windowInsets = WindowInsets(bottom = 0),
    ) {
        Column(
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            ReminderBottomSheetRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp),
                onClick = { showTimePicker = true },
                text = timeText,
                icon = Icons.Filled.Alarm
            )
            ReminderBottomSheetRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                onClick = { showDatePicker = true },
                text = dateText,
                icon = Icons.Filled.CalendarMonth
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp, top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }.invokeOnCompletion {
                        onDeleteReminder()
                    }
                }) {
                    Text(text = stringResource(id = CommonStrings.delete))
                }
                Button(onClick = {
                    if (!notificationPermission.status.isGranted) {
                        requestPermissionLauncher.launch(
                            notificationPermission.permission
                        )
                    } else {
                        scope.launch {
                            modalBottomSheetState.hide()
                        }.invokeOnCompletion {
                            onCreateReminder(date)
                        }
                    }
                }) {
                    Text(text = stringResource(id = CommonStrings.create))
                }
            }
        }
    }

}



