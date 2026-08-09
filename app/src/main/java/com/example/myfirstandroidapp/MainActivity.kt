package com.example.myfirstandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF3F51B5))) {
                SemesterRegistrationScreen()
            }
        }
    }
}

// Matches the real paper forms used at registration:
// White = Government Scholarship, Yellow = Self-Finance, Pink = In-Service
enum class RegistrationCategory(val label: String, val sheetColor: Color) {
    GOVERNMENT_SCHOLARSHIP("Government", Color(0xFFFFFFFF)),
    SELF_FINANCE("Self-Finance", Color(0xFFFFF3B0)),
    IN_SERVICE("In-Service", Color(0xFFF6C6D9))
}

// small helper so a section has a consistent label style
@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
    )
}

// This screen only shows the layout (text fields, dropdown menus, checkbox, buttons).
// No submit/reset/cancel logic is implemented, as required by the task.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterRegistrationScreen() {

    // ---- student fields ----
    var studentName by remember { mutableStateOf("") }
    var studentEmail by remember { mutableStateOf("") }
    var studentPhone by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") } // free text, e.g. DD/MM/YYYY

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var selectedBloodGroup by remember { mutableStateOf(bloodGroups[0]) }
    var bloodGroupExpanded by remember { mutableStateOf(false) }

    // AS = Autumn Semester, SS = Spring Semester
    val semesterOptions = listOf("AS (Autumn Semester)", "SS (Spring Semester)")
    var selectedSemester by remember { mutableStateOf(semesterOptions[0]) }
    var semesterExpanded by remember { mutableStateOf(false) }

    val yearOptions = listOf("Year 1", "Year 2", "Year 3", "Year 4", "Year 5")
    var selectedYear by remember { mutableStateOf(yearOptions[0]) }
    var yearExpanded by remember { mutableStateOf(false) }

    var registerBackPaper by remember { mutableStateOf(false) }
    var backPaperModuleCode by remember { mutableStateOf("") }
    var backPaperModuleName by remember { mutableStateOf("") }

    // ---- parent / guardian fields ----
    var parentName by remember { mutableStateOf("") }
    var parentEmail by remember { mutableStateOf("") }
    var parentResidence by remember { mutableStateOf("") }

    // today's date, filled in automatically, read-only display
    val currentDate = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    // which "sheet" the student is registering under - just picks a color, no other logic
    var selectedCategory by remember { mutableStateOf(RegistrationCategory.GOVERNMENT_SCHOLARSHIP) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Semester Registration", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // date, filled automatically - shown as a small read-only field at the top
            OutlinedTextField(
                value = currentDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Date") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Registration category as chips, styled after the paper sheet colors
            Text(
                text = "REGISTRATION CATEGORY",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RegistrationCategory.values().forEach { category ->
                    FilterChip(
                        modifier = Modifier.weight(1f),
                        selected = (category == selectedCategory),
                        onClick = { selectedCategory = category },
                        label = { Text(category.label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = category.sheetColor,
                            selectedLabelColor = Color(0xFF3A3A3A)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Form card - background tint matches the selected sheet color
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = selectedCategory.sheetColor),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    // ---------- Student Information ----------
                    SectionLabel("STUDENT INFORMATION")

                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = studentEmail,
                        onValueChange = { studentEmail = it },
                        label = { Text("Student Email") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = studentPhone,
                        onValueChange = { studentPhone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        label = { Text("Date of Birth (DD/MM/YYYY)") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Blood Group dropdown
                    ExposedDropdownMenuBox(
                        expanded = bloodGroupExpanded,
                        onExpandedChange = { bloodGroupExpanded = !bloodGroupExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedBloodGroup,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Blood Group") },
                            shape = RoundedCornerShape(10.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodGroupExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = bloodGroupExpanded, onDismissRequest = { bloodGroupExpanded = false }) {
                            bloodGroups.forEach { option ->
                                DropdownMenuItem(text = { Text(option) }, onClick = {
                                    selectedBloodGroup = option
                                    bloodGroupExpanded = false
                                })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // Semester (AS/SS) and Year side by side
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ExposedDropdownMenuBox(
                            modifier = Modifier.weight(1f),
                            expanded = semesterExpanded,
                            onExpandedChange = { semesterExpanded = !semesterExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedSemester,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Semester") },
                                shape = RoundedCornerShape(10.dp),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = semesterExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(expanded = semesterExpanded, onDismissRequest = { semesterExpanded = false }) {
                                semesterOptions.forEach { option ->
                                    DropdownMenuItem(text = { Text(option) }, onClick = {
                                        selectedSemester = option
                                        semesterExpanded = false
                                    })
                                }
                            }
                        }

                        ExposedDropdownMenuBox(
                            modifier = Modifier.weight(1f),
                            expanded = yearExpanded,
                            onExpandedChange = { yearExpanded = !yearExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedYear,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Year") },
                                shape = RoundedCornerShape(10.dp),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(expanded = yearExpanded, onDismissRequest = { yearExpanded = false }) {
                                yearOptions.forEach { option ->
                                    DropdownMenuItem(text = { Text(option) }, onClick = {
                                        selectedYear = option
                                        yearExpanded = false
                                    })
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Register for back paper checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = registerBackPaper,
                            onCheckedChange = { registerBackPaper = it }
                        )
                        Text("Register for Back Paper", fontSize = 14.sp)
                    }

                    // Only shown when registering for a back paper
                    if (registerBackPaper) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = backPaperModuleCode,
                            onValueChange = { backPaperModuleCode = it },
                            label = { Text("Module Code") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = backPaperModuleName,
                            onValueChange = { backPaperModuleName = it },
                            label = { Text("Module Name") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    // ---------- Parent / Guardian Information ----------
                    SectionLabel("PARENT / GUARDIAN INFORMATION")

                    OutlinedTextField(
                        value = parentName,
                        onValueChange = { parentName = it },
                        label = { Text("Parent's Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = parentEmail,
                        onValueChange = { parentEmail = it },
                        label = { Text("Parent's Email") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = parentResidence,
                        onValueChange = { parentResidence = it },
                        label = { Text("Parent's Residence") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Buttons row: Submit, Reset, Cancel (no logic attached, just UI)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { /* no logic required */ },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Submit")
                }
                FilledTonalButton(
                    onClick = { /* no logic required */ },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
                OutlinedButton(
                    onClick = { /* no logic required */ },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}