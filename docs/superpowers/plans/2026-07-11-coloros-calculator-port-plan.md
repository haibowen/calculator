# ColorOS Calculator Port Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a clean, buildable Android Studio project that replicates the ColorOS Calculator (v16.4.2) core functionality with **1:1 animation fidelity** on standard Android (AOSP/Pixel), minSdk 24.

**Architecture:** Incremental module-by-module migration: Gradle setup → Resources → Pure-Kotlin core libraries (calculator-engine, unit-converter, currency-converter, mortgage-calculator) → Custom Views → MVVM UI (CalculatorFragment + ViewModel) → History (Room) → Converter/Mortgage/Settings screens → Polish & Animation parity.

**Tech Stack:** Kotlin 2.0, Gradle KTS, AndroidX, Material3, Room, Coroutines/Flow, ViewBinding, JUnit5 + Mockito + Espresso.

---

## File Structure Map

```
calculator-app/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradle/libs.versions.toml
├── core/
│   ├── calculator-engine/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/calculator/core/engine/
│   ├── unit-converter/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/calculator/core/converter/
│   ├── currency-converter/
│   │   ├── build.gradle.kts
│   │   └── src/main/kotlin/com/calculator/core/currency/
│   └── mortgage-calculator/
│       ├── build.gradle.kts
│       └── src/main/kotlin/com/calculator/core/mortgage/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/calculator/
│       │   ├── CalculatorApplication.kt
│       │   ├── data/
│       │   │   ├── repository/
│       │   │   ├── local/
│       │   │   │   ├── dao/
│       │   │   │   ├── entity/
│       │   │   │   └── CalculatorDatabase.kt
│       │   │   └── remote/
│       │   ├── ui/
│       │   │   ├── main/
│       │   │   │   ├── CalculatorFragment.kt
│       │   │   │   ├── CalculatorViewModel.kt
│       │   │   │   └── CalculatorActivity.kt
│       │   │   ├── history/
│       │   │   │   ├── HistoryDrawerFragment.kt
│       │   │   │   └── HistoryAdapter.kt
│       │   │   ├── converter/
│       │   │   ├── mortgage/
│       │   │   ├── settings/
│       │   │   └── widget/
│       │   │       ├── CalculatorGrid.kt
│       │   │       ├── CalculatorButton.kt
│       │   │       ├── FormulaView.kt
│       │   │       ├── ResultView.kt
│       │   │       └── HistoryDrawerLayout.kt
│       │   └── util/
│       └── res/
│           ├── layout/
│           ├── values/
│           ├── drawable/
│           ├── anim/
│           └── xml/
└── docs/
```

---

## Phase 0: Project Skeleton & Gradle (Tasks 0.1–0.8)

### Task 0.1: Create settings.gradle.kts

**Files:**
- Create: `settings.gradle.kts`

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.5.0" apply false
        id("org.jetbrains.kotlin.android") version "2.0.0" apply false
        id("com.google.devtools.ksp") version "2.0.0-1.0.13" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "Calculator"
include(":app")
include(":core:calculator-engine")
include(":core:unit-converter")
include(":core:currency-converter")
include(":core:mortgage-calculator")
```

- [ ] **Step 1:** Write file
- [ ] **Step 2:** `cat settings.gradle.kts` → verify syntax
- [ ] **Step 3:** `git add settings.gradle.kts && git commit -m "chore: add settings.gradle.kts with module includes"`

---

### Task 0.2: Create gradle/libs.versions.toml

**Files:**
- Create: `gradle/libs.versions.toml`

```toml
[versions]
agp = "8.5.0"
kotlin = "2.0.0"
ksp = "2.0.0-1.0.13"
coreKtx = "1.13.1"
appcompat = "1.7.0"
material = "1.12.0"
constraintlayout = "2.1.4"
recyclerview = "1.3.2"
fragment = "1.8.0"
activity = "1.9.0"
lifecycle = "2.8.3"
room = "2.6.1"
coroutines = "1.8.1"
datastore = "1.1.1
window = "1.3.0"
junit = "5.10.2"
mockito = "5.11.0"
espresso = "3.5.1"
gson = "2.10.1"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material3 = { group = "com.google.android.material", name = "material", version.ref = "material" }
constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
recyclerview = { group = "androidx.recyclerview", name = "recyclerview", version.ref = "recyclerview" }
fragment-ktx = { group = "androidx.fragment", name = "fragment-ktx", version.ref = "fragment" }
activity-ktx = { group = "androidx.activity", name = "activity-ktx", version.ref = "activity" }
lifecycle-viewmodel-ktx = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycle" }
lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
coroutines-flow = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-flow", version.ref = "coroutines" }
datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
window = { group = "androidx.window", name = "window", version.ref = "window" }
junit-jupiter = { group = "org.junit.jupiter", name = "junit-jupiter", version.ref = "junit" }
mockito-core = { group = "org.mockito", name = "mockito-core", version.ref = "mockito" }
mockito-kotlin = { group = "org.mockito", name = "mockito-kotlin", version = "5.1.1" }
espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espresso" }
ext-junit = { group = "androidx.test.ext", name = "junit", version = "1.2.1" }
gson = { group = "com.google.code.gson", name = "gson", version.ref = "gson" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```

- [ ] **Step 1:** Write file
- [ ] **Step 2:** `cat gradle/libs.versions.toml` → verify
- [ ] **Step 3:** `git add gradle/libs.versions.toml && git commit -m "chore: add version catalog"`

---

### Task 0.3: Create Root build.gradle.kts

**Files:**
- Create: `build.gradle.kts`

```kotlin
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.13" apply false
}

allprojects {
    group = "com.calculator"
    version = "1.0.0"
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
```

- [ ] **Step 1:** Write file
- [ ] **Step 2:** `./gradlew --version` → verify Gradle wrapper exists (run `gradle wrapper` if not)
- [ ] **Step 3:** `git add build.gradle.kts && git commit -m "chore: add root build.gradle.kts"`

---

### Task 0.4: Create gradle.properties

**Files:**
- Create: `gradle.properties`

```properties
# Gradle
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true

# Android
android.nonTransitiveRClass=true
android.enableArtifactTransformCache=true
android.enableBuildCache=true

# Kotlin
kotlin.code.style=official
kotlin.incremental.compilation=true
kotlin.incremental.android=true

# KSP
ksp.incremental=true
ksp.useWorkers=true
```

- [ ] **Step 1:** Write file
- [ ] **Step 2:** `git add gradle.properties && git commit -m "chore: add gradle.properties"`

---

### Task 0.5: Create Core Module build.gradle.kts (Shared Template)

**Files:**
- Create: `core/calculator-engine/build.gradle.kts`
- Create: `core/unit-converter/build.gradle.kts`
- Create: `core/currency-converter/build.gradle.kts`
- Create: `core/mortgage-calculator/build.gradle.kts`

```kotlin
// core/calculator-engine/build.gradle.kts
plugins {
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.calculator.core.engine"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }

    buildFeatures {
        viewBinding = false
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.flow)
}
```

**Note:** For `unit-converter`, `currency-converter`, `mortgage-calculator`: change `namespace` to `com.calculator.core.converter`, `com.calculator.core.currency`, `com.calculator.core.mortgage` respectively.

- [ ] **Step 1:** Write all 4 files
- [ ] **Step 2:** `./gradlew :core:calculator-engine:assemble --dry-run` → verify config
- [ ] **Step 3:** `git add core/ && git commit -m "chore: add core module build files"`

---

### Task 0.6: Create App Module build.gradle.kts

**Files:**
- Create: `app/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.calculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.calculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=androidx.lifecycle.LifecycleKt.ExperimentalLifecycleKotlinAPI"
        )
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    packagingOptions {
        resources.excludes += "/META-INF/*.kotlin_module"
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material3)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.fragment.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.window)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Coroutines
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.flow)

    // DataStore
    implementation(libs.datastore.preferences)

    // JSON
    implementation(libs.gson)

    // Core modules
    implementation(project(":core:calculator-engine"))
    implementation(project(":core:unit-converter"))
    implementation(project(":core:currency-converter"))
    implementation(project(":core:mortgage-calculator"))

    // Test
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
```

- [ ] **Step 1:** Write file
- [ ] **Step 2:** `./gradlew :app:assembleDebug --dry-run` → verify
- [ ] **Step 3:** `git add app/build.gradle.kts && git commit -m "chore: add app module build.gradle.kts"`

---

### Task 0.7: Create ProGuard Rules

**Files:**
- Create: `app/proguard-rules.pro`

```pro
# Keep data classes for Room
-keepclassmembers class * {
    @androidx.room.Entity *;
    @androidx.room.Dao *;
    @androidx.room.Database *;
}

# Keep ViewBinding generated classes
-keep class com.calculator.databinding.** { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }

# Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }
```

- [ ] **Step 1:** Write file
- [ ] **Step 2:** `git add app/proguard-rules.pro && git commit -m "chore: add proguard rules"`

---

### Task 0.8: Verify Full Build

**Commands:**
```bash
./gradlew clean assembleDebug --no-daemon
```
- [ ] **Step 1:** Run command → expect BUILD SUCCESSFUL
- [ ] **Step 2:** `git add . && git commit -m "chore: verify full build passes"`

---

## Phase 1: Resources Migration (Tasks 1.1–1.5)

### Task 1.1: Copy Color Resources

**Source:** `~/tools/calculator_decompiled/res/values/colors.xml`
**Target:** `app/src/main/res/values/colors.xml`

- [ ] **Step 1:** Copy file, rename package references `com.coloros.calculator` → `com.calculator`
- [ ] **Step 2:** Add Material3 semantic colors (primary, secondary, surface, etc.) mapping to calculator colors
- [ ] **Step 3:** `git add ... && git commit -m "feat(res): import color resources"`

---

### Task 1.2: Copy Dimension Resources

**Source:** `~/tools/calculator_decompiled/res/values/dimens.xml`
**Target:** `app/src/main/res/values/dimens.xml`

- [ ] **Step 1:** Copy, fix references
- [ ] **Step 2:** Add `dimens-night.xml` for dark theme overrides
- [ ] **Step 3:** Commit

---

### Task 1.3: Copy String Resources

**Source:** `~/tools/calculator_decompiled/res/values/strings.xml`
**Target:** `app/src/main/res/values/strings.xml`

- [ ] **Step 1:** Copy all strings
- [ ] **Step 2:** Remove OPPO-specific strings (SAU, permissions, etc.)
- [ ] **Step 3:** Add missing Material3 strings (app_name, etc.)
- [ ] **Step 4:** Commit

---

### Task 1.4: Copy Layout Resources (Core Only)

**Source:** `~/tools/calculator_decompiled/res/layout/`
**Target:** `app/src/main/res/layout/`

**Copy only:**
- `main.xml` → `activity_calculator.xml`
- `include_calculator_and_history.xml` → `fragment_calculator.xml`
- `fragment_calculator_port.xml` (if exists) → adapt
- `calculator_grid` related layouts
- `calculator_button` related layouts

**Skip:** OPPO-specific (zoom, mini, fold, oslo, dragonfly, pantanal card, etc.)

- [ ] **Step 1:** Copy identified files
- [ ] **Step 2:** Refactor package names in XML (`com.android.calculator2` → `com.calculator`)
- [ ] **Step 3:** Replace `COUIToolbar` → `MaterialToolbar`, `COUISeekBar` → `Slider`, `COUIButtonPort` → `CalculatorButton`
- [ ] **Step 4:** Commit

---

### Task 1.5: Copy Drawable & Anim Resources

**Source:** `~/tools/calculator_decompiled/res/drawable*/`, `res/anim/`
**Target:** `app/src/main/res/drawable*/`, `res/anim/`

**Key animations to preserve (1:1):**
- Button press ripple (`event_down_20`, `event_down`)
- History drawer slide in/out
- Formula/result text transition
- Grid appear/disappear
- Scientific ↔ simple mode transition
- SeekBar thumb animation

- [ ] **Step 1:** Copy all drawable-* folders
- [ ] **Step 2:** Copy anim/ folder
- [ ] **Step 3:** Rename `coui_*` → `calculator_*`, `fold_*` → keep semantic names
- [ ] **Step 4:** Commit

---

## Phase 2: Calculator Engine (Pure Kotlin) (Tasks 2.1–2.10)

### Task 2.1: Create Token & AST Types

**Files:**
- Create: `core/calculator-engine/src/main/kotlin/com/calculator/core/engine/Token.kt`
- Create: `core/calculator-engine/src/main/kotlin/com/calculator/core/engine/Expression.kt`

```kotlin
// Token.kt
sealed interface Token {
    data class Number(val value: Double) : Token
    data class Operator(val op: Operator) : Token
    data class Function(val fn: Function) : Token
    data class Constant(val const: Constant) : Token
    object LeftParen : Token
    object RightParen : Token
    object End : Token
}

enum class Operator(val precedence: Int, val associativity: Associativity) {
    ADD(1, Associativity.LEFT), SUB(1, Associativity.LEFT),
    MUL(2, Associativity.LEFT), DIV(2, Associativity.LEFT),
    MOD(2, Associativity.LEFT),
    POW(3, Associativity.RIGHT),
    FACTORIAL(4, Associativity.LEFT),
    PERCENT(4, Associativity.LEFT);

    enum class Associativity { LEFT, RIGHT }
}

enum class Function(val argCount: Int) {
    SIN(1), COS(1), TAN(1),
    ASIN(1), ACOS(1), ATAN(1),
    SINH(1), COSH(1), TANH(1),
    LOG(1), LN(1), LOG10(1),
    SQRT(1), CBRT(1),
    EXP(1),
    ABS(1), FLOOR(1), CEIL(1), ROUND(1),
    DEG(1), RAD(1);
}

enum class Constant {
    PI(Math.PI), E(Math.E);
    val value: Double
}
```

```kotlin
// Expression.kt
sealed interface Expr {
    data class Number(val value: Double) : Expr
    data class Binary(val left: Expr, val op: Operator, val right: Expr) : Expr
    data class Unary(val op: Operator, val operand: Expr) : Expr
    data class FunctionCall(val fn: Function, val args: List<Expr>) : Expr
    data class Constant(val const: Constant) : Expr
    data class Percent(val operand: Expr) : Expr
    data class Factorial(val operand: Expr) : Expr
}
```

- [ ] **Step 1:** Write both files
- [ ] **Step 2:** `./gradlew :core:calculator-engine:compileKotlin` → verify
- [ ] **Step 3:** Commit

---

### Task 2.2: Implement Lexer (Tokenizer)

**Files:**
- Create: `core/calculator-engine/src/main/kotlin/com/calculator/core/engine/Lexer.kt`
- Create: `core/calculator-engine/src/test/kotlin/com/calculator/core/engine/LexerTest.kt`

```kotlin
// Lexer.kt
class Lexer(private val input: String) {
    private var pos = 0
    private val chars = input.toCharArray()

    fun nextToken(): Token = when {
        pos >= chars.size -> Token.End
        chars[pos].isDigit() || chars[pos] == '.' -> readNumber()
        chars[pos].isLetter() -> readIdentifier()
        else -> readOperator()
    }

    private fun readNumber(): Token.Number { ... }
    private fun readIdentifier(): Token { ... } // sin, cos, pi, e, etc.
    private fun readOperator(): Token { ... }   // + - * / ^ % ( ) !
}
```

**Test cases (LexerTest.kt):**
```kotlin
@Test fun `tokenizes simple arithmetic`() {
    val tokens = Lexer("1+2*3").tokenizeAll()
    assertEquals(listOf(Number(1), Operator(ADD), Number(2), Operator(MUL), Number(3), End), tokens)
}
@Test fun `tokenizes functions and constants`() { ... }
@Test fun `handles decimals and scientific notation`() { ... }
@Test fun `handles unary minus`() { ... }
```

- [ ] **Step 1:** Write Lexer.kt
- [ ] **Step 2:** Write LexerTest.kt with 10+ cases
- [ ] **Step 3:** `./gradlew :core:calculator-engine:test --tests LexerTest` → all pass
- [ ] **Step 4:** Commit

---

### Task 2.3: Implement Parser (Shunting-Yard → AST)

**Files:**
- Create: `core/calculator-engine/src/main/kotlin/com/calculator/core/engine/Parser.kt`
- Create: `core/calculator-engine/src/test/kotlin/com/calculator/core/engine/ParserTest.kt`

```kotlin
// Parser.kt
class Parser(private val lexer: Lexer) {
    private var current: Token = lexer.nextToken()

    fun parse(): Expr = parseExpression(0)

    private fun parseExpression(minPrec: Int): Expr { ... } // precedence climbing
    private fun parsePrimary(): Expr { ... } // number, constant, function, (expr)
}
```

**Test cases:**
- Operator precedence: `1+2*3` = 7, not 9
- Right-associative pow: `2^3^2` = 512, not 64
- Parentheses: `(1+2)*3` = 9
- Functions: `sin(pi/2)` = 1
- Factorial: `5!` = 120
- Percent: `100+10%` = 110
- Unary minus: `-5^2` = -25 (not 25)
- Implicit multiplication: `2(3+4)` = 14

- [ ] **Step 1:** Write Parser.kt
- [ ] **Step 2:** Write ParserTest.kt (20+ cases)
- [ ] **Step 3:** Run tests → all pass
- [ ] **Step 4:** Commit

---

### Task 2.4: Implement Evaluator (AST Interpreter)

**Files:**
- Create: `core/calculator-engine/src/main/kotlin/com/calculator/core/engine/Evaluator.kt`
- Create: `core/calculator-engine/src/test/kotlin/com/calculator/core/engine/EvaluatorTest.kt`

```kotlin
// Evaluator.kt
object Evaluator {
    fun evaluate(expr: Expr): Result<Double> = try {
        Result.success(eval(expr))
    } catch (e: ArithmeticException) {
        Result.failure(e)
    } catch (e: IllegalArgumentException) {
        Result.failure(e)
    }

    private fun eval(expr: Expr): Double = when (expr) {
        is Expr.Number -> expr.value
        is Expr.Constant -> expr.const.value
        is Expr.Binary -> evalBinary(expr)
        is Expr.Unary -> evalUnary(expr)
        is Expr.FunctionCall -> evalFunction(expr)
        is Expr.Percent -> eval(expr.operand) / 100
        is Expr.Factorial -> factorial(expr.operand)
    }
}
```

**Test cases (100+ random expressions vs. reference):**
- Compare with `ScriptEngineManager("js").eval()` for random valid expressions
- Edge: division by zero → `ArithmeticException`
- Edge: `sqrt(-1)` → `NaN` or exception
- Edge: large factorial → `Double.POSITIVE_INFINITY`

- [ ] **Step 1:** Write Evaluator.kt
- [ ] **Step 2:** Write EvaluatorTest.kt with property-based tests (kotlinx-coroutines-test + random generation)
- [ ] **Step 3:** Run tests → all pass
- [ ] **Step 4:** Commit

---

### Task 2.5: Create Public Facade (CalculatorEngine)

**Files:**
- Create: `core/calculator-engine/src/main/kotlin/com/calculator/core/engine/CalculatorEngine.kt`

```kotlin
class CalculatorEngine {
    fun evaluate(input: String): EngineResult = try {
        val tokens = Lexer(input).tokenizeAll()
        val ast = Parser(Lexer(input)).parse()
        val value = Evaluator.evaluate(ast).getOrThrow()
        EngineResult.Success(value)
    } catch (e: ParseException) {
        EngineResult.Error(EngineError.SYNTAX_ERROR, e.message)
    } catch (e: ArithmeticException) {
        EngineResult.Error(EngineError.MATH_ERROR, e.message)
    }
}

sealed interface EngineResult {
    data class Success(val value: Double) : EngineResult
    data class Error(val code: EngineError, val message: String?) : EngineResult
}

enum class EngineError { SYNTAX_ERROR, MATH_ERROR, OVERFLOW, INVALID_INPUT }
```

- [ ] **Step 1:** Write file
- [ ] **Step 2:** Add integration tests in `CalculatorEngineTest.kt`
- [ ] **Step 3:** Run → pass
- [ ] **Step 4:** Commit

---

### Task 2.6: Angle Mode Support (DEG/RAD)

**Files:**
- Modify: `CalculatorEngine.kt` (add `angleMode` parameter)
- Create: `AngleMode.kt` (enum)

```kotlin
enum class AngleMode { DEGREE, RADIAN }

class CalculatorEngine {
    var angleMode: AngleMode = AngleMode.DEGREE
    
    fun evaluate(input: String): EngineResult { ... }
}
```
Evaluator converts trig inputs based on mode.

- [ ] **Step 1:** Add AngleMode enum
- [ ] **Step 2:** Modify Evaluator trig functions to respect mode
- [ ] **Step 3:** Add tests for DEG/RAD parity
- [ ] **Step 4:** Commit

---

### Task 2.7: Inverse & Hyperbolic Functions (INV Toggle)

**Files:**
- Modify: `Token.kt`, `Parser.kt`, `Evaluator.kt`
- Add `Function.ASIN`, `Function.SINH`, etc. (already in enum)
- Add `INVERSE` flag to engine state

```kotlin
class CalculatorEngine {
    var inverseMode = false
    // When inverseMode=true, SIN→ASIN, COS→ACOS, etc.
}
```

- [ ] **Step 1:** Extend Lexer to recognize `inv` prefix or toggle
- [ ] **Step 2:** Update Parser to map functions based on inverseMode
- [ ] **Step 3:** Test: `sin(30)`=0.5, `inv sin(0.5)`=30 (DEG)
- [ ] **Step 4:** Commit

---

### Task 2.8: History & Memory (Engine Level)

**Files:**
- Create: `CalculatorState.kt` (Serializable)

```kotlin
data class CalculatorState(
    val angleMode: AngleMode = AngleMode.DEGREE,
    val inverseMode: Boolean = false,
    val memory: Double = 0.0,
    val history: List<HistoryEntry> = emptyList()
)

data class HistoryEntry(
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

- [ ] **Step 1:** Create data classes
- [ ] **Step 2:** Add `saveState()` / `restoreState()` to engine
- [ ] **Step 3:** Test serialization round-trip
- [ ] **Step 4:** Commit

---

### Task 2.9: Performance Benchmarks

**Files:**
- Create: `core/calculator-engine/src/test/kotlin/com/calculator/core/engine/BenchmarkTest.kt`

```kotlin
@Test fun `evaluate 10000 expressions < 500ms`() {
    val expressions = List(10000) { "sin(${Random.nextDouble()}*pi)+cos(${Random.nextDouble()}*pi)*${Random.nextInt(100)}" }
    measureTimedValue { expressions.forEach { engine.evaluate(it) } } shouldBeLessThan 500_000_000L // ns
}
```

- [ ] **Step 1:** Write benchmark
- [ ] **Step 2:** Run → verify < 500ms for 10k ops
- [ ] **Step 3:** Commit

---

### Task 2.10: Publish Local & Verify App Dependency

**Commands:**
```bash
./gradlew :core:calculator-engine:publishToMavenLocal
./gradlew :app:dependencies --configuration debugCompileClasspath | grep calculator-engine
```

- [ ] **Step 1:** Publish
- [ ] **Step 2:** Verify appears in app deps
- [ ] **Step 3:** Commit

---

## Phase 3: Unit Converter (Pure Kotlin) (Tasks 3.1–3.6)

### Task 3.1: Define Unit System

**Files:**
- Create: `core/unit-converter/src/main/kotlin/com/calculator/core/converter/Unit.kt`
- Create: `core/unit-converter/src/main/kotlin/com/calculator/core/converter/UnitCategory.kt`

```kotlin
data class Unit(
    val id: String,
    val name: String,
    val symbol: String,
    val category: UnitCategory,
    val toBase: Double,        // multiplier to base unit
    val fromBase: Double,      // 1 / toBase
    val offset: Double = 0.0   // for temperature
)

enum class UnitCategory {
    LENGTH, AREA, VOLUME, WEIGHT, TEMPERATURE,
    POWER, SPEED, PRESSURE, ENERGY, TIME,
    DATA, ANGLE, BASE_NUMERIC  // for base conversion (bin/oct/hex)
}
```

- [ ] **Step 1:** Write data classes
- [ ] **Step 2:** Commit

---

### Task 3.2: Build Unit Registry (All Categories)

**Files:**
- Create: `core/unit-converter/src/main/kotlin/com/calculator/core/converter/UnitRegistry.kt`

```kotlin
object UnitRegistry {
    private val unitsByCategory = mutableMapOf<UnitCategory, List<Unit>>()
    
    fun getUnits(category: UnitCategory): List<Unit> = unitsByCategory[category] ?: emptyList()
    fun getUnit(category: UnitCategory, id: String): Unit? = getUnits(category).find { it.id == id }
    fun convert(value: Double, from: Unit, to: Unit): Double { ... }
    
    init {
        registerLength()
        registerArea()
        registerVolume()
        registerWeight()
        registerTemperature()
        registerPower()
        registerSpeed()
        registerPressure()
        registerBaseNumeric()
        // ... all categories from reference app
    }
}
```

**Data source:** Extract from `~/tools/calculator_jadx/sources/t3/*` converter classes or `strings.xml` arrays.

- [ ] **Step 1:** Write registry with all 10+ categories
- [ ] **Step 2:** Add temperature special handling (offset)
- [ ] **Step 3:** Commit

---

### Task 3.3: Implement UnitConverter Facade

**Files:**
- Create: `core/unit-converter/src/main/kotlin/com/calculator/core/converter/UnitConverter.kt`

```kotlin
class UnitConverter {
    fun convert(value: Double, fromId: String, toId: String, category: UnitCategory): Result<Double>
    fun getCategories(): List<UnitCategory>
    fun getUnits(category: UnitCategory): List<Unit>
    fun formatValue(value: Double, unit: Unit): String // locale-aware formatting
}
```

- [ ] **Step 1:** Write facade
- [ ] **Step 2:** Commit

---

### Task 3.4: Unit Converter Tests

**Files:**
- Create: `core/unit-converter/src/test/kotlin/com/calculator/core/converter/UnitConverterTest.kt`

```kotlin
@Test fun `length conversions match reference`() {
    assertEquals(1000.0, converter.convert(1.0, "km", "m", LENGTH).getOrThrow(), 0.001)
    assertEquals(39.3701, converter.convert(1.0, "m", "in", LENGTH).getOrThrow(), 0.001)
}
@Test fun `temperature conversions`() { ... }
@Test fun `base numeric (bin/oct/hex)`() { ... }
@Test fun `round-trip precision`() { ... }
```

- [ ] **Step 1:** Write 50+ test cases covering all categories
- [ ] **Step 2:** Run → all pass
- [ ] **Step 3:** Commit

---

### Task 3.5: Currency Converter (Skeleton)

**Files:**
- Create: `core/currency-converter/src/main/kotlin/com/calculator/core/currency/`
  - `Currency.kt`, `ExchangeRate.kt`, `CurrencyConverter.kt`, `RateProvider.kt`

```kotlin
interface RateProvider {
    suspend fun fetchRates(base: String): Result<Map<String, Double>>
}

class CurrencyConverter(
    private val rateProvider: RateProvider,
    private val cache: RateCache
) {
    suspend fun convert(amount: Double, from: String, to: String): Result<Double>
    suspend fun refreshRates(): Result<Unit>
}
```

- [ ] **Step 1:** Write interfaces + skeleton
- [ ] **Step 2:** Add in-memory cache implementation
- [ ] **Step 3:** Add mock provider for tests
- [ ] **Step 4:** Commit

---

### Task 3.6: Mortgage Calculator

**Files:**
- Create: `core/mortgage-calculator/src/main/kotlin/com/calculator/core/mortgage/`

```kotlin
enum class MortgageType { EQUAL_PRINCIPAL_INTEREST, EQUAL_PRINCIPAL, COMBINED }

data class MortgageInput(
    val totalAmount: Double,
    val downPayment: Double,
    val annualRate: Double,
    val years: Int,
    val type: MortgageType,
    val commercialRate: Double = 0.0,  // for combined
    val providentRate: Double = 0.0,
    val providentAmount: Double = 0.0
)

data class MortgageResult(
    val monthlyPayment: Double,
    val totalInterest: Double,
    val totalAmount: Double,
    val schedule: List<PaymentScheduleItem>
)

data class PaymentScheduleItem(
    val period: Int,
    val principal: Double,
    val interest: Double,
    val remainingPrincipal: Double
)
```

- [ ] **Step 1:** Write all data classes + calculator logic
- [ ] **Step 2:** Test against known amortization formulas
- [ ] **Step 3:** Commit

---

## Phase 4: Custom Views (Tasks 4.1–4.8)

### Task 4.1: CalculatorButton (Base)

**Files:**
- Create: `app/src/main/java/com/calculator/ui/widget/CalculatorButton.kt`

```kotlin
class CalculatorButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {
    
    enum class Style { PRIMARY, SECONDARY, ACCENT, FUNCTION, NUMBER }
    
    var buttonStyle: Style = Style.NUMBER
        set(value) { field = value; applyStyle() }
    
    private fun applyStyle() {
        when (buttonStyle) {
            Style.NUMBER -> setBackgroundResource(R.drawable.calculator_btn_number)
            Style.FUNCTION -> setBackgroundResource(R.drawable.calculator_btn_function)
            Style.ACCENT -> setBackgroundResource(R.drawable.calculator_btn_accent)
            // ...
        }
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> animatePress()
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> animateRelease()
        }
        return super.onTouchEvent(event)
    }
}
```

- [ ] **Step 1:** Write class with style enum
- [ ] **Step 2:** Create drawable resources for each style (press ripple, focus, disabled)
- [ ] **Step 3:** Add preview in `CalculatorButtonPreview.kt` (Compose preview or layout)
- [ ] **Step 4:** Commit

---

### Task 4.2: FormulaView (Expression Display)

**Files:**
- Create: `app/src/main/java/com/calculator/ui/widget/FormulaView.kt`

```kotlin
class FormulaView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    
    fun setExpression(expr: String, animated: Boolean = true) {
        if (animated) {
            animateTextChange(expr)
        } else {
            text = expr
        }
    }
    
    private fun animateTextChange(newText: String) {
        val fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply { duration = 120 }
        val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).apply { duration = 120 }
        val set = AnimatorSet().apply {
            playSequentially(fadeOut, fadeIn)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    text = newText
                }
            })
        }
        set.start()
    }
    
    fun setErrorState(isError: Boolean) {
        setTextColor(if (isError) ContextCompat.getColor(context, R.color.calculator_error) 
                            else ContextCompat.getColor(context, R.color.calculator_formula_text))
    }
}
```

- [ ] **Step 1:** Write class with animation
- [ ] **Step 2:** Match original animation timing (120ms fade)
- [ ] **Step 3:** Commit

---

### Task 4.3: ResultView (Result Display)

**Files:**
- Create: `app/src/main/java/com/calculator/ui/widget/ResultView.kt`

```kotlin
class ResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    
    fun setResult(value: String, animated: Boolean = true) { ... }
    fun setInverseMode(enabled: Boolean) { ... } // shows "INV" badge
    fun setAngleMode(mode: AngleMode) { ... } // shows "DEG"/"RAD"
}
```

- [ ] **Step 1:** Write class
- [ ] **Step 2:** Add angle mode indicator animation (slide in/out)
- [ ] **Step 3:** Commit

---

### Task 4.4: CalculatorGrid (Custom GridLayout)

**Files:**
- Create: `app/src/main/java/com/calculator/ui/widget/CalculatorGrid.kt`

```kotlin
class CalculatorGrid @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {
    
    var columnCount = 5
    var rowCount = 7
    var simpleColumnCount = 4
    var simpleRowCount = 6
    
    var isScientificMode = true
        set(value) {
            field = value
            reconfigureGrid()
        }
    
    private fun reconfigureGrid() {
        columnCount = if (isScientificMode) 5 else 4
        rowCount = if (isScientificMode) 7 else 6
        requestLayout()
    }
    
    fun addButton(button: CalculatorButton, column: Int, row: Int, 
                  colSpan: Int = 1, rowSpan: Int = 1) {
        val lp = LayoutParams().apply {
            columnSpec = GridLayout.spec(column, colSpan)
            rowSpec = GridLayout.spec(row, rowSpan)
            width = 0
            height = 0
            setGravity(Gravity.FILL)
        }
        addView(button, lp)
    }
}
```

- [ ] **Step 1:** Write class
- [ ] **Step 2:** Test scientific ↔ simple toggle animation
- [ ] **Step 3:** Commit

---

### Task 4.5: HistoryDrawerLayout (WrapperDrawerLayout)

**Files:**
- Create: `app/src/main/java/com/calculator/ui/widget/HistoryDrawerLayout.kt`

```kotlin
class HistoryDrawerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : DrawerLayout(context, attrs, defStyleAttr) {
    
    private var historyWidth = 0
    
    fun setHistoryWidth(width: Int) {
        historyWidth = width
        requestLayout()
    }
    
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        // Constrain history drawer width
    }
    
    fun openHistory(animated: Boolean = true) { ... }
    fun closeHistory(animated: Boolean = true) { ... }
}
```

- [ ] **Step 1:** Write class
- [ ] **Step 2:** Match original slide animation (300ms, decelerate interpolator)
- [ ] **Step 3:** Commit

---

### Task 4.6: FrameButton (Button Wrapper for Visual Effects)

**Files:**
- Create: `app/src/main/java/com/calculator/ui/widget/FrameButton.kt`

```kotlin
class FrameButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    private val button: CalculatorButton
    private val overlay: View? // for inverse/hyperbolic toggle
    
    init {
        button = CalculatorButton(context).apply { id = View.generateViewId() }
        addView(button, LayoutParams(MATCH_PARENT, MATCH_PARENT))
    }
    
    fun setPrimaryButton(@IdRes id: Int) { ... }
    fun setSecondaryButton(@IdRes id: Int) { ... }
    fun toggleInverse(animated: Boolean) { ... } // cross-fade between primary/secondary
}
```

- [ ] **Step 1:** Write class
- [ ] **Step 2:** Implement cross-fade animation (200ms) for INV toggle
- [ ] **Step 3:** Commit

---

### Task 4.7: Button Layout XML & Styles

**Files:**
- Create: `app/src/main/res/layout/fragment_calculator.xml` (uses CalculatorGrid + FrameButtons)
- Create: `app/src/main/res/values/styles_calculator.xml`

```xml
<style name="CalculatorButton.Number" parent="Widget.Material3.Button">
    <item name="android:background">@drawable/calculator_btn_number</item>
    <item name="android:textSize">28sp</item>
    <item name="android:textColor">@color/calculator_btn_number_text</item>
    <item name="android:insetTop">0dp</item>
    <item name="android:insetBottom">0dp</item>
</style>

<style name="CalculatorButton.Function" parent="CalculatorButton.Number">
    <item name="android:background">@drawable/calculator_btn_function</item>
    <item name="android:textColor">@color/calculator_btn_function_text</item>
</style>
```

- [ ] **Step 1:** Create layout with all 35+ buttons in correct positions
- [ ] **Step 2:** Define all styles
- [ ] **Step 3:** Preview in Android Studio
- [ ] **Step 4:** Commit

---

### Task 4.8: View Animation Parity Tests

**Files:**
- Create: `app/src/androidTest/java/com/calculator/ui/widget/AnimationParityTest.kt`

```kotl
@RunWith(AndroidJUnit4::class)
class AnimationParityTest {
    @Test fun `button press ripple duration matches reference`() {
        val button = activityRule.activity.findViewById<CalculatorButton>(R.id.digit_1)
        val start = SystemClock.uptimeMillis()
        button.performClick()
        val duration = SystemClock.uptimeMillis() - start
        assertEquals(150, duration, 20) // reference: 150ms
    }
    
    @Test fun `scientific-simple toggle animation duration`() { ... }
    @Test fun `history drawer slide animation duration`() { ... }
    @Test fun `formula text change fade duration`() { ... }
}
```

- [ ] **Step 1:** Write Espresso tests measuring animation durations
- [ ] **Step 2:** Run on device/emulator → verify timings match reference app
- [ ] **Step 3:** Commit

---

## Phase 5: Main Calculator UI (MVVM) (Tasks 5.1–5.9)

### Task 5.1: CalculatorViewModel

**Files:**
- Create: `app/src/main/java/com/calculator/ui/main/CalculatorViewModel.kt`

```kotlin
@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val engine: CalculatorEngine,
    private val historyRepository: HistoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(CalculatorUiState())
    val state: StateFlow<CalculatorUiState> = _state
    
    fun onButtonClick(button: CalculatorButtonType) {
        _state.update { current ->
            val newState = when (button) {
                is Digit -> current.appendDigit(button.value)
                is Operator -> current.appendOperator(button.op)
                is Function -> current.appendFunction(button.fn)
                Clear -> current.clear()
                Delete -> current.deleteLast()
                Equals -> current.evaluate()
                ToggleInverse -> current.copy(inverseMode = !current.inverseMode)
                ToggleAngleMode -> current.cycleAngleMode()
                // ...
            }
            engine.angleMode = newState.angleMode
            engine.inverseMode = newState.inverseMode
            newState
        }
    }
    
    private fun evaluate() { ... } // calls engine, saves to history
}
```

- [ ] **Step 1:** Write ViewModel with StateFlow
- [ ] **Step 2:** Define `CalculatorUiState` data class
- [ ] **Step 3:** Commit

---

### Task 5.2: CalculatorFragment

**Files:**
- Create: `app/src/main/java/com/calculator/ui/main/CalculatorFragment.kt`

```kotlin
class CalculatorFragment : Fragment() {
    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalculatorViewModel by viewModels()
    
    override fun onCreateView(inflater, container, savedInstanceState): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view, savedInstanceState) {
        setupButtonListeners()
        observeState()
    }
    
    private fun setupButtonListeners() {
        binding.calculatorGrid.allButtons.forEach { btn ->
            btn.setOnClickListener { viewModel.onButtonClick(btn.buttonType) }
        }
    }
    
    private fun observeState() {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.state.collect { state ->
                binding.formulaView.setExpression(state.formula)
                binding.resultView.setResult(state.result)
                binding.resultView.setAngleMode(state.angleMode)
                binding.resultView.setInverseMode(state.inverseMode)
                updateButtonVisibility(state)
            }
        }
    }
}
```

- [ ] **Step 1:** Write Fragment
- [ ] **Step 2:** Wire all 35+ buttons to ViewModel
- [ ] **Step 3:** Commit

---

### Task 5.3: CalculatorActivity

**Files:**
- Create: `app/src/main/java/com/calculator/ui/main/CalculatorActivity.kt`

```kotlin
class CalculatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CalculatorFragment())
                .commit()
        }
    }
}
```

- [ ] **Step 1:** Write Activity
- [ ] **Step 2:** Create `activity_calculator.xml` with toolbar + fragment container
- [ ] **Step 3:** Commit

---

### Task 5.4: History Repository (Room)

**Files:**
- Create: `app/src/main/java/com/calculator/data/local/entity/HistoryEntity.kt`
- Create: `app/src/main/java/com/calculator/data/local/dao/HistoryDao.kt`
- Create: `app/src/main/java/com/calculator/data/local/CalculatorDatabase.kt`

```kotlin
@Entity(tableName = "history", indices = [Index("timestamp")])
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis(),
    val angleMode: String, // DEG/RAD
    val isError: Boolean = false
)

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: HistoryEntity): Long
    
    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getHistory(limit: Int, offset: Int): List<HistoryEntity>
    
    @Query("DELETE FROM history")
    suspend fun clearAll()
    
    @Query("SELECT COUNT(*) FROM history")
    suspend fun count(): Int
}

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class CalculatorDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
```

- [ ] **Step 1:** Write entity, DAO, database
- [ ] **Step 2:** Add `HistoryRepository` wrapping DAO
- [ ] **Step 3:** Add `provideDatabase` in DI (manual or Hilt)
- [ ] **Step 4:** Commit

---

### Task 5.5: HistoryDrawerFragment

**Files:**
- Create: `app/src/main/java/com/calculator/ui/history/HistoryDrawerFragment.kt`
- Create: `app/src/main/java/com/calculator/ui/history/HistoryAdapter.kt`

```kotlin
class HistoryDrawerFragment : Fragment() {
    private var _binding: FragmentHistoryDrawerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalculatorViewModel by activityViewModels()
    
    override fun onCreateView(...) = FragmentHistoryDrawerBinding.inflate(...).root
    
    override fun onViewCreated(...) {
        binding.recyclerView.adapter = HistoryAdapter(onItemClick = { entry ->
            viewModel.onHistoryItemClick(entry)
            closeDrawer()
        })
        viewModel.historyFlow.collect { binding.recyclerView.adapter?.submitList(it) }
    }
}
```

- [ ] **Step 1:** Write fragment + adapter
- [ ] **Step 2:** Implement swipe-to-delete animation
- [ ] **Step 3:** "Clear all" dialog with confirmation
- [ ] **Step 4:** Commit

---

### Task 5.6: Settings Repository (DataStore)

**Files:**
- Create: `app/src/main/java/com/calculator/data/local/SettingsRepository.kt`

```kotlin
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val angleMode: Flow<AngleMode> = dataStore.data
        .map { it[ANGLE_MODE_KEY] ?: AngleMode.DEGREE }
    
    val inverseMode: Flow<Boolean> = dataStore.data
        .map { it[INVERSE_MODE_KEY] ?: false }
    
    val zoomTransparencyEnabled: Flow<Boolean> = ...
    
    suspend fun setAngleMode(mode: AngleMode) { ... }
    suspend fun setInverseMode(enabled: Boolean) { ... }
}
```

- [ ] **Step 1:** Write repository
- [ ] **Step 2:** Wire to ViewModel for persistence
- [ ] **Step 3:** Commit

---

### Task 5.7: Scientific ↔ Simple Mode Toggle

**Files:**
- Modify: `CalculatorViewModel`, `CalculatorFragment`, `CalculatorGrid`

```kotlin
// In ViewModel
fun toggleScientificMode() {
    _state.update { it.copy(isScientificMode = !it.isScientificMode) }
}

// In Fragment
private fun updateButtonVisibility(state: CalculatorUiState) {
    binding.calculatorGrid.isScientificMode = state.isScientificMode
    // Animate: cross-fade buttons, resize grid
    binding.calculatorGrid.animateModeChange(state.isScientificMode)
}
```

**Animation:** 300ms, grid resize + button fade + toolbar title change

- [ ] **Step 1:** Add state + toggle in ViewModel
- [ ] **Step 2:** Implement `CalculatorGrid.animateModeChange()`
- [ ] **Step 3:** Match reference timing exactly
- [ ] **Step 4:** Commit

---

### Task 5.8: Keyboard Input Support (Hardware/Bluetooth)

**Files:**
- Modify: `CalculatorFragment`, `CalculatorViewModel`

```kotlin
override fun onKeyDown(keyCode: KeyCode, event: KeyEvent): Boolean {
    return when (keyCode) {
        KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 -> { viewModel.onDigit(keyCode - KEYCODE_0); true }
        KeyEvent.KEYCODE_PLUS -> { viewModel.onOperator(ADD); true }
        KeyEvent.KEYCODE_MINUS -> { viewModel.onOperator(SUB); true }
        KeyEvent.KEYCODE_MULTIPLY -> { viewModel.onOperator(MUL); true }
        KeyEvent.KEYCODE_SLASH -> { viewModel.onOperator(DIV); true }
        KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER -> { viewModel.onEquals(); true }
        KeyEvent.KEYCODE_DEL -> { viewModel.onDelete(); true }
        KeyEvent.KEYCODE_ESCAPE -> { viewModel.onClear(); true }
        else -> super.onKeyDown(keyCode, event)
    }
}
```

- [ ] **Step 1:** Add key handling
- [ ] **Step 2:** Test with external keyboard
- [ ] **Step 3:** Commit

---

### Task 5.9: Main Calculator Integration Test

**Files:**
- Create: `app/src/androidTest/java/com/calculator/ui/main/CalculatorIntegrationTest.kt`

```kotlin
@RunWith(AndroidJUnit4::class)
class CalculatorIntegrationTest {
    @get:Rule val activityRule = ActivityScenarioRule(CalculatorActivity::class.java)
    
    @Test fun `basic arithmetic 2+3=5`() {
        onView(withId(R.id.digit_2)).perform(click())
        onView(withId(R.id.op_add)).perform(click())
        onView(withId(R.id.digit_3)).perform(click())
        onView(withId(R.id.eq)).perform(click())
        onView(withId(R.id.result_view)).check(matches(withText("5")))
    }
    
    @Test fun `scientific sin(30) in DEG = 0.5`() { ... }
    @Test fun `history saves and restores`() { ... }
    @Test fun `scientific-simple toggle works`() { ... }
}
```

- [ ] **Step 1:** Write 10+ integration tests
- [ ] **Step 2:** Run → all pass
- [ ] **Step 3:** Commit

---

## Phase 6: Converter Screens (Tasks 6.1–6.5)

### Task 6.1: ConverterActivity + Base Fragment

**Files:**
- Create: `app/src/main/java/com/calculator/ui/converter/ConverterActivity.kt`
- Create: `app/src/main/java/com/calculator/ui/converter/ConverterFragment.kt`

```kotlin
class ConverterFragment : Fragment() {
    private val viewModel: ConverterViewModel by viewModels()
    
    override fun onViewCreated(view, savedInstanceState) {
        // Category spinner (Length, Area, Volume, ...)
        // From/To unit spinners
        // Input EditText with live conversion
        // Swap button (animates)
        // Precision selector
    }
}
```

- [ ] **Step 1:** Create activity + fragment skeleton
- [ ] **Step 2:** Layout with Material3 components
- [ ] **Step 3:** Commit

---

### Task 6.2: ConverterViewModel

**Files:**
- Create: `app/src/main/java/com/calculator/ui/converter/ConverterViewModel.kt`

```kotlin
@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val unitConverter: UnitConverter,
    private val currencyConverter: CurrencyConverter
) : ViewModel() {
    
    private val _state = MutableStateFlow(ConverterUiState())
    val state: StateFlow<ConverterUiState> = _state
    
    fun onCategoryChanged(category: UnitCategory) { ... }
    fun onFromUnitChanged(unit: Unit) { ... }
    fun onToUnitChanged(unit: Unit) { ... }
    fun onInputChanged(text: String) { ... }
    fun onSwapClicked() { ... } // animate swap
}
```

- [ ] **Step 1:** Write ViewModel
- [ ] **Step 2:** Wire to UnitConverter facade
- [ ] **Step 3:** Commit

---

### Task 6.3: Currency Converter Tab

**Files:**
- Create: `CurrencyConverterFragment.kt`, `CurrencyConverterViewModel.kt`

```kotlin
// Adds: rate refresh button, last updated timestamp, offline indicator
// Uses CurrencyConverter (suspend functions)
```

- [ ] **Step 1:** Implement currency-specific UI
- [ ] **Step 2:** Add background rate refresh (WorkManager)
- [ ] **Step 3:** Commit

---

### Task 6.4: Base Numeric Converter (Bin/Oct/Hex/Dec)

**Files:**
- Create: `BaseConverterFragment.kt`

```kotlin
// 4 input fields (bin, oct, dec, hex) - editing one updates others live
// Bitwise operations (AND, OR, XOR, NOT, shifts)
// Programmer mode: 8/16/32/64 bit, signed/unsigned
```

- [ ] **Step 1:** Implement base conversion logic (reuse UnitCategory.BASE_NUMERIC)
- [ ] **Step 2:** Build UI with 4 synchronized fields
- [ ] **Step 3:** Commit

---

### Task 6.5: Converter Animation Parity

**Files:**
- Modify: All converter fragments

```kotlin
// Swap button: 300ms rotate + cross-fade
// Category change: 200ms fade + slide
// Input change: 100ms result update (no animation, instant)
```

- [ ] **Step 1:** Add animations matching reference
- [ ] **Step 2:** Measure with Espresso
- [ ] **Step 3:** Commit

---

## Phase 7: Mortgage Calculator (Tasks 7.1–7.3)

### Task 7.1: MortgageActivity + Fragment

**Files:**
- Create: `MortgageActivity.kt`, `MortgageFragment.kt`

```kotlin
// Inputs: total price, down payment, loan term, interest rate, loan type
// Types: Commercial, Provident, Combined
// Output: monthly payment, total interest, total repayment, amortization table
```

- [ ] **Step 1:** Create UI with Material3 form components
- [ ] **Step 2:** Commit

---

### Task 7.2: MortgageViewModel

**Files:**
- Create: `MortgageViewModel.kt`

```kotlin
@HiltViewModel
class MortgageViewModel @Inject constructor(
    private val mortgageCalculator: MortgageCalculator
) : ViewModel() {
    
    fun calculate(input: MortgageInput): MortgageResult = mortgageCalculator.calculate(input)
    fun generateSchedule(result: MortgageResult): List<PaymentScheduleItem> = ...
}
```

- [ ] **Step 1:** Wire to core mortgage-calculator module
- [ ] **Step 2:** Add amortization table RecyclerView
- [ ] **Step 3:** Commit

---

### Task 7.3: Mortgage Tests

**Files:**
- Create: `MortgageCalculatorTest.kt`, `MortgageIntegrationTest.kt`

```kotlin
@Test fun `equal principal interest matches formula`() { ... }
@Test fun `combined loan splits correctly`() { ... }
@Test fun `schedule sums to total`() { ... }
```

- [ ] **Step 1:** Write unit + integration tests
- [ ] **Step 2:** Run → pass
- [ ] **Step 3:** Commit

---

## Phase 8: Settings & Polish (Tasks 8.1–8.6)

### Task 8.1: SettingsActivity

**Files:**
- Create: `SettingsActivity.kt`, `SettingsFragment.kt`

```kotlin
// Preferences: Angle mode (DEG/RAD), Vibration, Sound, Theme (Light/Dark/System),
// History retention, Auto-clear clipboard, About/Licenses/Open Source
```

- [ ] **Step 1:** Use AndroidX Preference library
- [ ] **Step 2:** Wire to SettingsRepository (DataStore)
- [ ] **Step 3:** Commit

---

### Task 8.2: About / Licenses / Privacy

**Files:**
- Create: `AboutFragment.kt`, `LicensesFragment.kt`, `PrivacyFragment.kt`

```kotlin
// About: version, build date, device info
// Licenses: show all OSS licenses (from gradle-license-plugin)
// Privacy: static text
```

- [ ] **Step 1:** Generate licenses via `./gradlew generateLicenseReport`
- [ ] **Step 2:** Create UI
- [ ] **Step 3:** Commit

---

### Task 8.3: Theme & Dark Mode Support

**Files:**
- Modify: `app/src/main/res/values/themes.xml`, `values-night/themes.xml`

```xml
<style name="Theme.Calculator" parent="Theme.Material3.DynamicColors.DayNight">
    <item name="colorPrimary">@color/calculator_primary</item>
    <item name="colorSurface">@color/calculator_bg</item>
    <item name="calculatorFormulaTextColor">@color/calculator_formula_text</item>
    <!-- ... all semantic colors -->
</style>
```

- [ ] **Step 1:** Define all semantic color roles
- [ ] **Step 2:** Test Light/Dark/System switching
- [ ] **Step 3:** Commit

---

### Task 8.4: Accessibility (a11y)

**Files:**
- Modify: All custom views + layouts

```xml
<!-- Buttons -->
android:contentDescription="@string/desc_digit_1"
android:focusable="true"
android:clickable="true"

<!-- Formula/Result -->
android:contentDescription="@string/desc_formula"
android:accessibilityLiveRegion="polite"
```

- [ ] **Step 1:** Add content descriptions to all buttons
- [ ] **Step 2:** Add live region for result announcements
- [ ] **Step 3:** Test with TalkBack
- [ ] **Step 4:** Commit

---

### Task 8.5: Performance & Size Optimization

**Commands:**
```bash
./gradlew :app:assembleRelease
./gradlew :app:bundleRelease
ls -lh app/build/outputs/apk/release/app-release.apk
```

**Targets:**
- Release APK < 5 MB
- Startup < 800ms (cold)
- No ANR in monkey test (10,000 events)

- [ ] **Step 1:** Build release
- [ ] **Step 2:** Run `apkanalyzer` to check size breakdown
- [ ] **Step 3:** Enable R8 full mode, remove unused resources
- [ ] **Step 4:** Commit

---

### Task 8.6: Final Animation Audit (1:1 Parity)

**Process:**
1. Install reference app (calculator-16-4-2.apk) on same device
2. Install our app
3. Side-by-side record screen for each interaction:
   - Button press (all types)
   - Scientific ↔ Simple toggle
   - History drawer open/close
   - Formula text change
   - Result appear
   - Angle mode change
   - INV toggle
   - Converter swap
   - Category change
4. Use `adb shell screenrecord` + frame-by-frame comparison

**Files:**
- Create: `docs/animation-audit.md` with results table

| Interaction | Reference (ms) | Ours (ms) | Diff | Status |
|-------------|---------------|-----------|------|--------|
| Number press ripple | 150 | 152 | +2 | ✅ |
| Function press ripple | 150 | 148 | -2 | ✅ |
| Scientific→Simple | 300 | 305 | +5 | ✅ |
| History drawer open | 300 | 298 | -2 | ✅ |
| ... | ... | ... | ... | ... |

- [ ] **Step 1:** Record reference animations
- [ ] **Step 2:** Record our animations
- [ ] **Step 3:** Compare frame-by-frame
- [ ] **Step 4:** Fix any > 16ms (1 frame) differences
- [ ] **Step 5:** Document in audit file
- [ ] **Step 6:** Commit

---

## Phase 9: CI & Release Prep (Tasks 9.1–9.3)

### Task 9.1: GitHub Actions CI

**Files:**
- Create: `.github/workflows/ci.yml`

```yaml
name: CI
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: 'temurin', java-version: '17' }
      - name: Cache Gradle
        uses: gradle/actions/setup-gradle@v3
      - run: ./gradlew ktlintCheck detekt test assembleDebug --no-daemon
      - run: ./gradlew connectedAndroidTest --no-daemon
        uses: reactivecircus/android-emulator-runner@v2
        with: { api-level: 24, arch: x86_64 }
```

- [ ] **Step 1:** Write workflow
- [ ] **Step 2:** Push → verify CI passes
- [ ] **Step 3:** Commit

---

### Task 9.2: Release Signing Config

**Files:**
- Create: `app/keystore.properties` (gitignored)
- Modify: `app/build.gradle.kts` (signingConfigs)

```kotlin
signingConfigs {
    create("release") {
        val props = Properties().apply { load(FileInputStream("keystore.properties")) }
        storeFile = File(props["storeFile"] as String)
        storePassword = props["storePassword"] as String
        keyAlias = props["keyAlias"] as String
        keyPassword = props["keyPassword"] as String
    }
}
buildTypes {
    release { signingConfig = signingConfigs.getByName("release") }
}
```

- [ ] **Step 1:** Add signing config
- [ ] **Step 2:** Document keystore generation in README
- [ ] **Step 3:** Commit (without keystore)

---

### Task 9.3: Final Verification Checklist

**Manual verification:**

- [ ] Fresh clone → `./gradlew assembleRelease` succeeds
- [ ] Install release APK on API 24, 28, 31, 34 emulators
- [ ] All calculator functions work (100 random expressions)
- [ ] All converters work (each category, 5 conversions each)
- [ ] Mortgage calculator matches reference values
- [ ] History persists across app restart
- [ ] Settings persist across app restart
- [ ] Dark/Light/System theme works
- [ ] TalkBack reads all buttons correctly
- [ ] No crashes in 10-min monkey test
- [ ] APK size < 5 MB
- [ ] All animations within 1 frame of reference

---

## Execution Order Summary

| Phase | Tasks | Est. Time |
|-------|-------|-----------|
| 0: Skeleton | 0.1–0.8 | 2h |
| 1: Resources | 1.1–1.5 | 4h |
| 2: Engine | 2.1–2.10 | 12h |
| 3: Converters | 3.1–3.6 | 8h |
| 4: Custom Views | 4.1–4.8 | 10h |
| 5: Main UI | 5.1–5.9 | 10h |
| 6: Converters UI | 6.1–6.5 | 8h |
| 7: Mortgage | 7.1–7.3 | 4h |
| 8: Settings/Polish | 8.1–8.6 | 8h |
| 9: CI/Release | 9.1–9.3 | 3h |
| **Total** | **67 tasks** | **~69h** |

---

**Plan complete and saved to `docs/superpowers/plans/2026-07-11-coloros-calculator-port-plan.md`. Two execution options:**

1. **Subagent-Driven (recommended)** — I dispatch a fresh subagent per task, review between tasks, fast iteration. REQUIRED SUB-SKILL: `superpowers:subagent-driven-development`

2. **Inline Execution** — Execute tasks in this session using `executing-plans`, batch execution with checkpoints for review. REQUIRED SUB-SKILL: `superpowers:executing-plans`

**Which approach?**