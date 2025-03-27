package com.koleff.kare_android.ui.compose.dialogs

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview

/**
 * Used for preview
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateFilterWithDialog() {
    var isFilterDialogVisible by remember { mutableStateOf(false) }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var selectedTarget by remember { mutableStateOf<DatePickerTarget?>(null) }

    var fromDate by remember { mutableStateOf<LocalDate?>(null) }
    var toDate by remember { mutableStateOf<LocalDate?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

//        //Filter button
//        IconButton(
//            onClick = {
//                isFilterDialogVisible = true
//            }
//        ) {
//            Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
//        }

        //Filter dialog
        if (isFilterDialogVisible) {
            DateRangeDialog(
                fromDate = fromDate,
                toDate = toDate,
                onFromDateClick = {
                    selectedTarget = DatePickerTarget.FROM
                    isDatePickerVisible = true
                },
                onToDateClick = {
                    selectedTarget = DatePickerTarget.TO
                    isDatePickerVisible = true
                },
                onCancelClick = {
                    isFilterDialogVisible = false
                },
                onApplyClick = {
                    isFilterDialogVisible = false
                }
            )
        }

        //Calendar date picker
        if (isDatePickerVisible) {
            DatePickerModal(
                onDismissRequest = {
                    isDatePickerVisible = false
                    selectedTarget = null
                },
                onDateSelected = { selectedDate ->
                    when (selectedTarget) {
                        DatePickerTarget.FROM -> fromDate = selectedDate
                        DatePickerTarget.TO -> toDate = selectedDate
                        null -> {}
                    }
                    isDatePickerVisible = false
                    selectedTarget = null
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangeDialog(
    fromDate: LocalDate?,
    toDate: LocalDate?,
    onFromDateClick: () -> Unit,
    onToDateClick: () -> Unit,
    onCancelClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Dialog(onDismissRequest = onCancelClick) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Select Dates", style = MaterialTheme.typography.titleLarge)

                DateInputBox(
                    label = "From Date",
                    date = fromDate?.format(dateFormatter),
                    onClick = onFromDateClick
                )

                DateInputBox(
                    label = "To Date",
                    date = toDate?.format(dateFormatter),
                    onClick = onToDateClick
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onCancelClick) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onApplyClick) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
fun DateInputBox(label: String, date: String?, onClick: () -> Unit) {
    OutlinedTextField(
        value = date ?: "",
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        enabled = true,
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        onClick()
                    }
                }
            }
            .padding(vertical = 8.dp),
    )
}

//@Composable
//fun DateInputBox(label: String, date: String?, onClick: () -> Unit) {
//    TextField(
//        value = date ?: "",
//        onValueChange = {},
//        label = { Text(label) },
//        readOnly = true,
//        modifier = Modifier
//            .fillMaxWidth()
//            .pointerInput(Unit) {
//                awaitEachGesture {
//                    awaitFirstDown(pass = PointerEventPass.Initial)
//                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
//                    if (upEvent != null) {
//                        onClick()
//                    }
//                }
//            }
//            .padding(vertical = 8.dp),
//    )
//}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(onDismissRequest: () -> Unit, onDateSelected: (LocalDate) -> Unit) {
    val datePickerState = rememberDatePickerState()

    val labelTextColor = MaterialTheme.colorScheme.onSurface
    val labelTextStyle = MaterialTheme.typography.labelLarge.copy(
        color = labelTextColor
    )
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                val selectedMillis = datePickerState.selectedDateMillis
                if (selectedMillis != null) {
                    val selectedDate = Instant.ofEpochMilli(selectedMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onDateSelected(selectedDate)
                }
            }) {
                Text(text = "OK", style = labelTextStyle)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel", style = labelTextStyle)
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

enum class DatePickerTarget {
    FROM,
    TO
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DatePickerModalPreview() {
    DatePickerModal(onDismissRequest = {}, onDateSelected = {})
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DateFilterWithDialogPreview() {
    DateFilterWithDialog()
}