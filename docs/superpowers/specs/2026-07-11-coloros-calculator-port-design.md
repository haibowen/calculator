# ColorOS Calculator → Standard Android Port: Design Specification

**Date:** 2026-07-11  
**Status:** Approved for Implementation Planning  
**Target:** Standard Android (AOSP/Pixel), minSdk 24, targetSdk 34

---

## 1. Project Overview

Port the OPPO/ColorOS Calculator (`com.coloros.calculator`, v16.4.2) to a clean, buildable Android Studio project targeting standard Android, with **1:1 animation fidelity** to the original app.

### Core Principles
- **Zero third-party dependencies** — only AndroidX, Material3, Kotlin stdlib
- **Semantic package structure** — `com.calculator.{core,converter,mortgage,ui,data,util}`
- **Incremental migration** — each module tested in isolation before integration
- **Animation parity** — every transition, spring, ripple, stagger matched frame-for-frame

---

## 2. Architecture

```
calculator-app/
├── app/                          # Android 应用模块
│   ├── src/main/
│   │   ├── java/com/calculator/
│   │   │   ├── core/             # 表达式解析、计算引擎 (纯 Kotlin)
│   │   │   ├── converter/        # 单位/货币/进制换算 (纯 Kotlin)
│   │   │   ├── mortgage/         # 房贷计算 (纯 Kotlin)
│   │   │   ├── ui/               # Android UI 层
│   │   │   │   ├── main/         # CalculatorActivity + Fragment
│   │   │   │   ├── history/      # HistoryDrawerFragment
│   │   │   │   ├── converter/    # 换算器界面
│   │   │   │   ├── mortgage/     # 房贷界面
│   │   │   │   ├── settings/     # 设置界面
│   │   │   │   └── widget/       # 自定义 View
│   │   │   ├── data/             # Repository、Room、Preferences
│   │   │   └── util/             # 扩展函数、工具类
│   │   ├── res/                  # 资源 (布局、值、动画、Drawable)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── core/                         # 可选：独立发布的纯 Kotlin 库
│   ├── calculator-engine/
│   ├── unit-converter/
│   ├── currency-converter/
│   └── mortgage-calculator/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

### Tech Stack
| Layer | Choice | Version |
|-------|--------|---------|
| Build | Gradle KTS | 8.5+ |
| Language | Kotlin | 2.0+ |
| Min SDK | Android 7.0 | 24 |
| Target SDK | Android 14 | 34 |
| UI | XML + View + Material3 | 1.12+ |
| Arch | MVVM + Repository + StateFlow | - |
| DI | Manual / ServiceLocator | - |
| DB | Room | 2.6+ |
| Async | Coroutines + Flow | - |
| Animation | ViewPropertyAnimator + AnimationSet + spring physics | - |

---

## 3. Module Specifications

### 3.1 calculator-engine (Pure Kotlin)

**Responsibility:** Parse & evaluate mathematical expressions.

**Public API:**
```kotlin
interface CalculatorEngine {
    fun evaluate(expression: String): Result<CalculationResult>
    fun evaluateAsync(expression: String): Flow<CalculationResult>
}

data class CalculationResult(
    val value: Double,
    val formatted: String,
    val isError: Boolean,
    val errorMessage: String?
)
```

**Supported Operations:**
- Arithmetic: `+ - * / %`
- Power: `^` (右结合)
- Factorial: `!` (后缀)
- Functions: `sin, cos, tan, asin, acos, atan, sinh, cosh, tanh, log, ln, sqrt, cbrt, exp, abs, floor, ceil, round`
- Constants: `π (pi), e`
- Parentheses: `( )` 嵌套任意深度
- Implicit multiplication: `2π`, `3sin(30)`
- Angle modes: DEG / RAD / GRAD
- Inverse toggle (2nd key): `sin ↔ asin`, `log ↔ 10^x`, `ln ↔ e^x`, `sqrt ↔ x²`

**Algorithm:** Shunting-yard (Dijkstra) → RPN evaluation  
**Precision:** `Double` (IEEE 754), display rounded to 12 significant digits  
**Error Handling:** Division by zero, overflow, domain errors → `CalculationResult(isError=true)`

**Tests:** 200+ unit cases covering edge cases, operator precedence, angle modes, inverse toggle.

---

### 3.2 unit-converter (Pure Kotlin)

**Categories (9 total):**
| Category | Base Unit | Units Count |
|----------|-----------|-------------|
| Length | meter | 18 |
| Area | m² | 12 |
| Volume | m³ | 14 |
| Weight | kilogram | 14 |
| Temperature | celsius | 5 (special: offset-based) |
| Power | watt | 8 |
| Speed | m/s | 10 |
| Pressure | pascal | 11 |
| Base (Numeral) | decimal | 4 (bin/oct/dec/hex) |

**API:**
```kotlin
interface UnitConverter {
    fun convert(value: Double, from: Unit, to: Unit): Double
    fun getCategories(): List<UnitCategory>
    fun getUnits(category: UnitCategory): List<Unit>
}

data class Unit(val id: String, val name: String, val symbol: String, val factor: Double)
// Temperature: factor = 1, offset = 273.15 (Kelvin base)
```

**Data Source:** Embedded JSON (`units.json`) loaded at init.

---

### 3.3 currency-converter (Pure Kotlin + Network)

**Features:**
- 160+ currencies (ISO 4217)
- Exchange rate caching (Room, TTL 1 hour)
- Offline fallback to last known rates
- Background refresh via WorkManager (periodic, 4h)

**API:**
```kotlin
interface CurrencyConverter {
    suspend fun convert(amount: Double, from: Currency, to: Currency): Result<Double>
    fun getCachedRate(from: Currency, to: Currency): Double?
    fun getAllCurrencies(): List<Currency>
    fun refreshRates(): Flow<Resource<Unit>>
}
```

---

### 3.4 mortgage-calculator (Pure Kotlin)

**Modes:** 等额本息、等额本金、组合贷 (商贷+公积金)  
**Outputs:** 月供、总利息、总还款、还款明细表 (List<RepaymentPeriod>)  
**Inputs:** 贷款总额、年利率、期限(年)、首付比例、公积金比例(组合贷)

---

### 3.5 UI Module (Android)

#### 3.5.1 Main Calculator Screen

**Layout Hierarchy:**
```
CalculatorActivity (singleTop)
└── FragmentContainerView (calculator_fragment)
    └── CalculatorFragment
        ├── CoordinatorLayout
        │   ├── AppBarLayout (collapsible)
        │   │   └── MaterialToolbar (centered title, menu items)
        │   ├── CalculatorFragmentContainer (FrameLayout)
        │   │   ├── FormulaView (TextView, 单行、右对齐、滚动)
        │   │   ├── ResultView (TextView, 大字号、右对齐、动画数字翻转)
        │   │   └── HistoryDrawerLayout (DrawerLayout, 从右侧滑入)
        │   │       ├── RecyclerView (历史记录列表, 带删除动画)
        │   │       └── EmptyView
        │   └── CalculatorGrid (自定义 GridLayout, 5列×7行/简易模式5×5)
        │       ├── CalculatorButton (MaterialButton 子类)
        │       │   ├── 文本/图标
        │       │   ├── 长按显示 tooltip
        │       │   ├── 按压波纹 (RippleDrawable)
        │       │   └── **关键动画**: 按下缩放 0.92x + 透明度 0.8 (80ms spring)
        │       └── FrameButton 包裹 (处理 2nd/Inv 切换时的淡入淡出)
```

**Key Animations (1:1 from Reference):**

| Trigger | Animation | Duration | Interpolator |
|---------|-----------|----------|--------------|
| Button press | `scaleX/Y: 1.0 → 0.92`, `alpha: 1.0 → 0.8` | 80ms | `SpringInterpolator(stiffness=300, damping=25)` |
| Button release | `scaleX/Y: 0.92 → 1.0`, `alpha: 0.8 → 1.0` | 120ms | `SpringInterpolator(stiffness=200, damping=20)` |
| 2nd/Inv toggle | Button cross-fade (old: `alpha 1→0, scale 1→0.9`; new: `alpha 0→1, scale 0.9→1`) | 150ms | `FastOutSlowInInterpolator` |
| History drawer open | `translationX: width → 0`, background `alpha: 0 → 0.32` | 250ms | `MaterialEasing.standard` |
| History drawer close | `translationX: 0 → width`, background `alpha: 0.32 → 0` | 200ms | `MaterialEasing.standardDecelerate` |
| History item delete | `translationX: 0 → width`, `alpha: 1 → 0`, neighbors slide up | 300ms | `MaterialEasing.emphasized` |
| Result update | `ResultView` 逐位数字翻转 (DigitFlipAnimation, 逐位 80ms 错开 30ms) | 300ms total | `OvershootInterpolator(1.2)` |
| Formula scroll | Horizontal auto-scroll to end on new input | 200ms | `LinearInterpolator` |
| Sci ↔ Simple mode switch | Grid 行/列动画重排 (GridLayout `rowCount` 变更 + `TransitionManager`) | 400ms | `MaterialEasing.emphasized` |
| Toolbar menu appear | `scale: 0.8→1.0`, `alpha: 0→1` 交错 (stagger 40ms/item) | 200ms | `MaterialEasing.standard` |

**Spring Physics Parameters (from original):**
```kotlin
// Button press spring
val pressSpring = SpringAnimation(view, DynamicAnimation.SCALE_X).apply {
    spring.stiffness = 1500f
    spring.dampingRatio = 0.75f
}
// History drawer
val drawerSpring = SpringAnimation(drawer, DynamicAnimation.TRANSLATION_X).apply {
    spring.stiffness = 800f
    spring.dampingRatio = 0.9f
}
```

#### 3.5.2 Converter Screens

- `ConverterActivity` + `ViewPager2` + `TabLayout` (9 tabs)
- Each tab: `ConverterFragment` with dual-column layout (from/to)
- **Unit picker:** BottomSheetDialogFragment, searchable, 最近使用置顶
- **Animation:** Tab switch = `FragmentFade` + `Slide` (300ms), picker = `BottomSheet` spring (stiffness=600)

#### 3.5.3 Mortgage Screen

- Form + Result card + Expandable repayment schedule (RecyclerView)
- **Animation:** Result card `slideUp` + `fadeIn` (400ms spring), schedule expand/collapse per item (200ms)

#### 3.5.4 Settings Screen

- `PreferenceFragmentCompat` + Material3 styling
- Options: Angle mode (DEG/RAD/GRAD), Digit grouping, Vibration, Theme (Light/Dark/System), History retention

#### 3.5.5 Custom Widgets (widget/)

| Widget | Extends | Key Features |
|--------|---------|--------------|
| `CalculatorGrid` | `GridLayout` | 动态行列、子视图插入/移除动画、自适应列宽 |
| `CalculatorButton` | `MaterialButton` | 图标/文本双模式、长按 tooltip、spring press、无障碍内容描述 |
| `FrameButton` | `FrameLayout` | 叠加两个 Button (正/反函数)、交叉淡入淡出动画 |
| `FormulaView` | `AppCompatTextView` | 单行、右对齐、自动水平滚动、运算符高亮着色 |
| `ResultView` | `AppCompatTextView` | 大字号、逐位翻转动画、错误状态红色闪烁 |
| `HistoryItemView` | `ConstraintLayout` | 左滑删除、撤销 Snackbar、进入/退出动画 |

---

## 4. Resources (Directly Ported from APK)

### 4.1 Colors (`colors.xml`)
- All `calculator_*` colors extracted from APK
- Material3 semantic mapping: `calculator_bg → surfaceContainerLowest`, `calculator_btn_primary → primaryContainer`, etc.
- Dark theme variants for every color

### 4.2 Dimens (`dimens.xml`)
- 200+ dimensions: `grid_padding_h`, `btn_height`, `toolbar_height`, `seekbar_*`, animation distances
- **All dp values preserved exactly**

### 4.3 Strings (`strings.xml`)
- Complete zh-CN + en-US (extracted from APK)
- Keys renamed to semantic: `fun_sin` → `button_function_sin`, `desc_fun_sin` → `content_desc_button_sin`

### 4.4 Drawables
- Vector assets for all icons (sin, cos, π, e, √, x², %, C, ⌫, ÷, ×, −, +, =, ., 0-9, 括号, 进制, 单位, 货币, 房贷, 设置, 历史, 科学/简易切换, 浮窗, 缩放)
- State lists for button backgrounds (normal, pressed, focused, disabled)
- Ripple drawables with custom colors

### 4.5 Animations (`anim/`, `animator/`)
| File | Purpose |
|------|---------|
| `button_press_spring.xml` | SpringAnimation via `AnimatorSet` |
| `button_release_spring.xml` |  |
| `history_drawer_enter.xml` |  |
| `history_drawer_exit.xml` |  |
| `history_item_delete.xml` |  |
| `result_digit_flip.xml` | 逐位翻转 `ObjectAnimator` (property: `text`) |
| `mode_switch_grid.xml` | `TransitionManager` + `ChangeBounds` |
| `toolbar_menu_stagger.xml` | 交错入场 |

---

## 5. Data Layer

### 5.1 Room Database (`CalculatorDatabase`)
```kotlin
@Entity(tableName = "history")
data class HistoryRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isError: Boolean = false
)

@Entity(tableName = "currency_rates")
data class CurrencyRate(
    @PrimaryKey val baseCurrency: String,
    val ratesJson: String,  // Map<String, Double> as JSON
    val updatedAt: Long
)

@Dao interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(record: HistoryRecord): Long
    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT 1000")
    fun getAll(): Flow<List<HistoryRecord>>
    @Query("DELETE FROM history WHERE id = :id")
    suspend fun delete(id: Long): Int
    @Query("DELETE FROM history")
    suspend fun clearAll(): Int
}
```

### 5.2 Preferences (`CalculatorPreferences`)
- DataStore (Preferences) for: angle_mode, digit_grouping, vibration_enabled, theme, history_retention_days, first_run_tips_shown

---

## 6. Removed OPPO/ColorOS Dependencies

| Original | Replacement / Removal |
|----------|----------------------|
| `COUIToolbar` | `MaterialToolbar` |
| `COUISeekBar` | `Slider` (Material) + custom thumb |
| `COUIButtonPort` | `CalculatorButton` (MaterialButton 子类) |
| `FlexibleWindowManager` | **Removed** |
| `OplusZoomWindow` | **Removed** |
| `SAU` (System App Update) | **Removed** |
| `oplus.*` permissions | **Removed** |
| `com.oplus.nearx.track` analytics | **Removed** |
| `com.customer.feedback.sdk` | **Removed** |
| `com.oplus.tingle`, `com.oplus.epona` | **Removed** |

**Permission Cleanup:** Only `VIBRATE`, `INTERNET`, `ACCESS_NETWORK_STATE`, `FOREGROUND_SERVICE` (for currency refresh) remain.

---

## 7. Animation Fidelity Checklist (1:1 Requirement)

> Every animation below **must** match original frame timing, easing, and visual feel.

- [ ] Button press spring (80ms, stiffness=1500, damping=0.75)
- [ ] Button release spring (120ms, stiffness=800, damping=0.9)
- [ ] 2nd/Inv cross-fade (150ms, FastOutSlowIn)
- [ ] History drawer enter (250ms, Material standard)
- [ ] History drawer exit (200ms, Material standard decelerate)
- [ ] History item delete + neighbor reflow (300ms, emphasized)
- [ ] Result digit flip (80ms/位, stagger 30ms, overshoot 1.2)
- [ ] Formula auto-scroll (200ms, linear)
- [ ] Sci/Simple grid reflow (400ms, ChangeBounds + TransitionManager)
- [ ] Toolbar menu stagger (40ms/item, 200ms total)
- [ ] Converter tab switch (300ms, fade+slide)
- [ ] Unit picker bottom sheet (spring, stiffness=600)
- [ ] Mortgage result card enter (400ms spring)
- [ ] Orientation change reflow (no jank, preserve state)
- [ ] Multi-window/foldable transitions (graceful degrade)

**Verification Method:** Side-by-side video comparison at 60fps, frame diff < 2px.

---

## 8. Testing Strategy

| Layer | Tool | Coverage Target |
|-------|------|-----------------|
| Unit (engine, converter, mortgage) | JUnit5 + KotlinTest | 95%+ |
| UI (Fragment, ViewModel) | Robolectric + Espresso | 80% |
| Animation | Screenshot testing (Paparazzi) + manual | 100% key flows |
| Integration | Compatibility | Device farm (API 24, 28, 31, 34) | All |

---

## 9. Build & Release

```kotlin
// build.gradle.kts (root)
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.13" apply false
}

// app/build.gradle.kts
android {
    namespace = "com.calculator"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.calculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = false
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.8.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-flow:1.8.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.window:window:1.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
}
```

---

## 10. Acceptance Criteria

1. **Builds cleanly** on fresh checkout (`./gradlew assembleDebug`)
2. **All unit tests pass** (`./gradlew test`)
3. **App launches** on API 24+ emulator/device
4. **Core calculator** evaluates expressions identically to reference (100 random expressions match)
5. **Animations** pass side-by-side visual diff (see §7)
6. **No OPPO/ColorOS code** remains (grep for `oplus`, `coloros`, `coui` → zero hits in app sources)
7. **APK size** < 8 MB (debug), < 5 MB (release, shrunk)
8. **No crashes** in monkey test (10,000 events)

---

## 11. Open Questions (Resolved)

| Question | Decision |
|----------|----------|
| Target platform | Standard Android (AOSP/Pixel) |
| Project type | Full buildable Android Studio project |
| Package restructuring | Semantic packages (`com.calculator.*`) |
| UI framework | XML + View + Material3 |
| OPPO features | All removed, core only |
| Migration strategy | Incremental (phased) |
| Animation fidelity | **1:1 replication required** |

---

**Next Step:** Invoke `writing-plans` skill to generate detailed implementation plan with task breakdown.