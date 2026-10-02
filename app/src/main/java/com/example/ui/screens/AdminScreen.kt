package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Category
import com.example.data.model.Difficulty
import com.example.data.model.LevelConfig
import com.example.data.model.Question
import com.example.data.model.QuestionType
import com.example.ui.theme.AmberPoints
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.VioletAccent
import com.example.ui.viewmodel.AdminStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    stats: AdminStats,
    categories: List<Category>,
    questions: List<Question>,
    levelConfigs: List<LevelConfig>,
    onAddQuestion: (String, String, QuestionType, String, String, String, String, String, String, String, Difficulty, Int) -> Unit,
    onUpdateQuestion: (Question) -> Unit,
    onDeleteQuestion: (Question) -> Unit,
    onToggleQuestionActive: (Question) -> Unit,
    onAddCategory: (String, String, String, String) -> Unit,
    onUpdateCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onToggleCategoryActive: (Category) -> Unit,
    onUpdateLevelConfig: (Int, String, Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBack()
    }

    var selectedTab by remember { mutableStateOf(0) } // 0: Overview, 1: Questions, 2: Categories, 3: Levels

    var showAddQuestionDialog by remember { mutableStateOf(false) }
    var editingQuestion by remember { mutableStateOf<Question?>(null) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }

    // Dialog for Add/Edit Question
    if (showAddQuestionDialog || editingQuestion != null) {
        QuestionFormDialog(
            categories = categories,
            initialQuestion = editingQuestion,
            onDismiss = {
                showAddQuestionDialog = false
                editingQuestion = null
            },
            onSave = { qCategoryId, qText, qType, qClue, qAns, optA, optB, optC, optD, qExpl, qDiff, qPts ->
                if (editingQuestion != null) {
                    onUpdateQuestion(
                        editingQuestion!!.copy(
                            categoryId = qCategoryId,
                            questionText = qText,
                            questionType = qType,
                            visualClue = qClue,
                            answer = qAns,
                            optionA = optA,
                            optionB = optB,
                            optionC = optC,
                            optionD = optD,
                            explanation = qExpl,
                            difficulty = qDiff,
                            points = qPts
                        )
                    )
                } else {
                    onAddQuestion(
                        qCategoryId, qText, qType, qClue, qAns, optA, optB, optC, optD, qExpl, qDiff, qPts
                    )
                }
                showAddQuestionDialog = false
                editingQuestion = null
            }
        )
    }

    // Dialog for Add/Edit Category
    if (showAddCategoryDialog || editingCategory != null) {
        CategoryFormDialog(
            initialCategory = editingCategory,
            onDismiss = {
                showAddCategoryDialog = false
                editingCategory = null
            },
            onSave = { catId, catName, catEmoji, catDesc ->
                if (editingCategory != null) {
                    onUpdateCategory(
                        editingCategory!!.copy(
                            name = catName,
                            iconEmoji = catEmoji,
                            description = catDesc
                        )
                    )
                } else {
                    onAddCategory(catId, catName, catEmoji, catDesc)
                }
                showAddCategoryDialog = false
                editingCategory = null
            }
        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_screen"),
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = {
                        editingQuestion = null
                        showAddQuestionDialog = true
                    },
                    containerColor = IndigoPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("admin_add_question_fab")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Question")
                }
            } else if (selectedTab == 2) {
                FloatingActionButton(
                    onClick = {
                        editingCategory = null
                        showAddCategoryDialog = true
                    },
                    containerColor = IndigoPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("admin_add_category_fab")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Category")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IndigoPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = IndigoPrimary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Overview", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    modifier = Modifier.testTag("admin_tab_overview")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Questions (${questions.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    modifier = Modifier.testTag("admin_tab_questions")
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Categories (${categories.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    modifier = Modifier.testTag("admin_tab_categories")
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Levels", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    modifier = Modifier.testTag("admin_tab_levels")
                )
            }

            when (selectedTab) {
                0 -> AdminOverviewTab(stats = stats, questionCount = questions.size, categoryCount = categories.size)
                1 -> AdminQuestionsTab(
                    questions = questions,
                    categories = categories,
                    onEditQuestion = { editingQuestion = it },
                    onDeleteQuestion = onDeleteQuestion,
                    onToggleActive = onToggleQuestionActive
                )
                2 -> AdminCategoriesTab(
                    categories = categories,
                    onEditCategory = { editingCategory = it },
                    onDeleteCategory = onDeleteCategory,
                    onToggleActive = onToggleCategoryActive
                )
                3 -> AdminLevelsTab(
                    levels = levelConfigs,
                    onUpdateLevel = onUpdateLevelConfig
                )
            }
        }
    }
}

@Composable
fun AdminOverviewTab(
    stats: AdminStats,
    questionCount: Int,
    categoryCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Platform Analytics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Total Users",
                value = "${stats.totalUsers}",
                icon = "👥",
                color = IndigoPrimary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Active Users",
                value = "${stats.activeUsers}",
                icon = "🟢",
                color = SuccessEmerald,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Games Played",
                value = "${stats.gamesPlayed}",
                icon = "🎮",
                color = AmberPoints,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Answered",
                value = "${stats.questionsAnswered}",
                icon = "❓",
                color = VioletAccent,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = "Total Questions",
                value = "$questionCount",
                icon = "📚",
                color = IndigoPrimary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Categories",
                value = "$categoryCount",
                icon = "🏷️",
                color = AmberPoints,
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Most Popular Category",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🔥 ${stats.mostPopularCategory}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Based on total rounds initiated by users today",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AdminQuestionsTab(
    questions: List<Question>,
    categories: List<Category>,
    onEditQuestion: (Question) -> Unit,
    onDeleteQuestion: (Question) -> Unit,
    onToggleActive: (Question) -> Unit
) {
    var searchFilter by remember { mutableStateOf("") }
    var selectedCatFilter by remember { mutableStateOf("ALL") }

    val filtered = remember(questions, searchFilter, selectedCatFilter) {
        questions.filter { q ->
            (selectedCatFilter == "ALL" || q.categoryId.equals(selectedCatFilter, ignoreCase = true)) &&
            (searchFilter.isBlank() || q.questionText.contains(searchFilter, ignoreCase = true) || q.answer.contains(searchFilter, ignoreCase = true))
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchFilter,
                onValueChange = { searchFilter = it },
                placeholder = { Text("Filter questions...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_question_search"),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(filtered) { question ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_question_item_${question.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (question.isActive) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(IndigoPrimary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = question.categoryId.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(AmberPoints.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = question.questionType.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberPoints
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onToggleActive(question) }, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    imageVector = if (question.isActive) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Active",
                                    tint = if (question.isActive) SuccessEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(onClick = { onEditQuestion(question) }, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = IndigoPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(onClick = { onDeleteQuestion(question) }, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = ErrorCrimson,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = question.questionText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Clue: ${question.visualClue}  |  Answer: ${question.answer}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SuccessEmerald,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun AdminCategoriesTab(
    categories: List<Category>,
    onEditCategory: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onToggleActive: (Category) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { cat ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_category_item_${cat.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (cat.isActive) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = cat.iconEmoji, fontSize = 26.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = cat.name,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                if (!cat.isActive) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(Disabled)",
                                        color = ErrorCrimson,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = cat.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = cat.isActive,
                            onCheckedChange = { onToggleActive(cat) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        IconButton(onClick = { onEditCategory(cat) }, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = IndigoPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = { onDeleteCategory(cat) }, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = ErrorCrimson,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminLevelsTab(
    levels: List<LevelConfig>,
    onUpdateLevel: (Int, String, Int) -> Unit
) {
    var editingLevel by remember { mutableStateOf<LevelConfig?>(null) }

    if (editingLevel != null) {
        var levelName by remember { mutableStateOf(editingLevel!!.name) }
        var minPts by remember { mutableStateOf(editingLevel!!.minPoints.toString()) }

        AlertDialog(
            onDismissRequest = { editingLevel = null },
            title = { Text("Edit Level ${editingLevel!!.level}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = levelName,
                        onValueChange = { levelName = it },
                        label = { Text("Level Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = minPts,
                        onValueChange = { minPts = it },
                        label = { Text("Minimum Points Required") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val pts = minPts.toIntOrNull() ?: editingLevel!!.minPoints
                    onUpdateLevel(editingLevel!!.level, levelName, pts)
                    editingLevel = null
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { editingLevel = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(levels) { lvl ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { editingLevel = lvl },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Level ${lvl.level}: ${lvl.name}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "${lvl.minPoints} points threshold",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { editingLevel = lvl }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = IndigoPrimary)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionFormDialog(
    categories: List<Category>,
    initialQuestion: Question?,
    onDismiss: () -> Unit,
    onSave: (String, String, QuestionType, String, String, String, String, String, String, String, Difficulty, Int) -> Unit
) {
    var categoryId by remember { mutableStateOf(initialQuestion?.categoryId ?: categories.firstOrNull()?.id ?: "countries") }
    var questionText by remember { mutableStateOf(initialQuestion?.questionText ?: "") }
    var questionType by remember { mutableStateOf(initialQuestion?.questionType ?: QuestionType.MULTIPLE_CHOICE) }
    var visualClue by remember { mutableStateOf(initialQuestion?.visualClue ?: "") }
    var answer by remember { mutableStateOf(initialQuestion?.answer ?: "") }
    var optionA by remember { mutableStateOf(initialQuestion?.optionA ?: "") }
    var optionB by remember { mutableStateOf(initialQuestion?.optionB ?: "") }
    var optionC by remember { mutableStateOf(initialQuestion?.optionC ?: "") }
    var optionD by remember { mutableStateOf(initialQuestion?.optionD ?: "") }
    var explanation by remember { mutableStateOf(initialQuestion?.explanation ?: "") }
    var difficulty by remember { mutableStateOf(initialQuestion?.difficulty ?: Difficulty.EASY) }
    var points by remember { mutableStateOf((initialQuestion?.points ?: 100).toString()) }

    var catMenuExpanded by remember { mutableStateOf(false) }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    var diffMenuExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialQuestion == null) "Add New Question" else "Edit Question") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Category dropdown
                ExposedDropdownMenuBox(
                    expanded = catMenuExpanded,
                    onExpandedChange = { catMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = categoryId,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catMenuExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = catMenuExpanded,
                        onDismissRequest = { catMenuExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.iconEmoji} ${cat.name}") },
                                onClick = {
                                    categoryId = cat.id
                                    catMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Question Text
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    label = { Text("Question Text") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Visual Clue & Type
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = typeMenuExpanded,
                        onExpandedChange = { typeMenuExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = questionType.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeMenuExpanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = typeMenuExpanded,
                            onDismissRequest = { typeMenuExpanded = false }
                        ) {
                            QuestionType.values().forEach { t ->
                                DropdownMenuItem(
                                    text = { Text(t.name) },
                                    onClick = {
                                        questionType = t
                                        typeMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = visualClue,
                        onValueChange = { visualClue = it },
                        label = { Text("Visual Clue (Emoji/URL)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // 4 Options
                OutlinedTextField(
                    value = optionA,
                    onValueChange = { optionA = it },
                    label = { Text("Option A") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = optionB,
                    onValueChange = { optionB = it },
                    label = { Text("Option B") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = optionC,
                    onValueChange = { optionC = it },
                    label = { Text("Option C") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = optionD,
                    onValueChange = { optionD = it },
                    label = { Text("Option D") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Correct Answer
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Correct Answer (Exact Match)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Difficulty & Points
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = diffMenuExpanded,
                        onExpandedChange = { diffMenuExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = difficulty.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Difficulty") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = diffMenuExpanded) },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = diffMenuExpanded,
                            onDismissRequest = { diffMenuExpanded = false }
                        ) {
                            Difficulty.values().forEach { d ->
                                DropdownMenuItem(
                                    text = { Text(d.name) },
                                    onClick = {
                                        difficulty = d
                                        diffMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = points,
                        onValueChange = { points = it },
                        label = { Text("Base Points") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Explanation
                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    label = { Text("Explanation") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (questionText.isNotBlank() && answer.isNotBlank()) {
                        val pts = points.toIntOrNull() ?: 100
                        onSave(
                            categoryId, questionText, questionType, visualClue, answer,
                            optionA.ifBlank { answer },
                            optionB.ifBlank { "Option B" },
                            optionC.ifBlank { "Option C" },
                            optionD.ifBlank { "Option D" },
                            explanation, difficulty, pts
                        )
                    }
                },
                modifier = Modifier.testTag("admin_save_question_button")
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CategoryFormDialog(
    initialCategory: Category?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var catId by remember { mutableStateOf(initialCategory?.id ?: "") }
    var catName by remember { mutableStateOf(initialCategory?.name ?: "") }
    var catEmoji by remember { mutableStateOf(initialCategory?.iconEmoji ?: "⭐") }
    var catDesc by remember { mutableStateOf(initialCategory?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialCategory == null) "Add Category" else "Edit Category") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (initialCategory == null) {
                    OutlinedTextField(
                        value = catId,
                        onValueChange = { catId = it },
                        label = { Text("Category ID (slug)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                OutlinedTextField(
                    value = catName,
                    onValueChange = { catName = it },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = catEmoji,
                    onValueChange = { catEmoji = it },
                    label = { Text("Emoji Icon (e.g. 🦁)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = catDesc,
                    onValueChange = { catDesc = it },
                    label = { Text("Short Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (catName.isNotBlank()) {
                    onSave(
                        catId.ifBlank { catName.lowercase().replace(" ", "_") },
                        catName,
                        catEmoji,
                        catDesc
                    )
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
