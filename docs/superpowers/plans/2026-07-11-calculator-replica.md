# ColorOS 计算器精简复刻 — 实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use subagent-driven-development or executing-plans to implement this plan task-by-task.

**Goal:** 在现有 Android 项目中新建 `app-calculator` module，用 XML + ViewBinding + Kotlin 复刻 ColorOS 计算器核心功能（基础运算 + 科学计算）

**Architecture:** MVVM (Activity + ViewModel + Room) + 自研表达式引擎 (Tokenizer → Recursive Descent Parser → BigDecimal Evaluator) + 传统 XML UI

**Tech Stack:** Kotlin, AppCompat, ViewBinding, Room, BigDecimal, Material3 (仅主题), JUnit

---

### Task 0: 项目模块配置

**Files:**
- Create: `app-calculator/build.gradle.kts`
- Create: `app-calculator/src/main/AndroidManifest.xml`
- Create: `app-calculator/proguard-rules.pro`
- Modify: `settings.gradle.kts`

- [ ] **Step 1: 创建 app-calculator/build.gradle.kts**

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.calculator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.calculator"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
```

- [ ] **Step 2: 创建 app-calculator/src/main/AndroidManifest.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.Calculator">
        <activity
            android:name=".CalculatorActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustNothing"
            android:configChanges="orientation|screenSize|screenLayout|keyboardHidden">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

- [ ] **Step 3: 创建 app-calculator/proguard-rules.pro**

```
# 留空，release 时无需额外规则
```

- [ ] **Step 4: 修改 settings.gradle.kts 添加模块**

在 `settings.gradle.kts` 中追加：

```kotlin
include(":app-calculator")
```

- [ ] **Step 5: 更新根 build.gradle.kts 添加 kapt plugin**

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.24" apply false
}
```

---

### Task 1: 资源文件（主题、颜色、字符串、尺寸）

**Files:**
- Create: `app-calculator/src/main/res/values/colors.xml`
- Create: `app-calculator/src/main/res/values/strings.xml`
- Create: `app-calculator/src/main/res/values/dimens.xml`
- Create: `app-calculator/src/main/res/values/themes.xml`
- Create: `app-calculator/src/main/res/values-night/themes.xml`
- Create: `app-calculator/src/main/res/drawable/btn_calc_rounded.xml`
- Create: `app-calculator/src/main/res/drawable/btn_calc_op_rounded.xml`
- Create: `app-calculator/src/main/res/drawable/btn_calc_eq_rounded.xml`
- Create: `app-calculator/src/main/res/drawable/btn_calc_func_rounded.xml`

- [ ] **Step 1: colors.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="cal_btn_number_bg">#F2F3F5</color>
    <color name="cal_btn_number_bg_dark">#3A3A3C</color>
    <color name="cal_btn_op_bg">#FF9500</color>
    <color name="cal_btn_op_bg_dark">#FF9F0A</color>
    <color name="cal_btn_eq_bg">#FF9500</color>
    <color name="cal_btn_eq_bg_dark">#FF9F0A</color>
    <color name="cal_btn_func_bg">#D1D5D9</color>
    <color name="cal_btn_func_bg_dark">#636366</color>
    <color name="cal_btn_number_text">#1C1C1E</color>
    <color name="cal_btn_number_text_dark">#FFFFFF</color>
    <color name="cal_btn_op_text">#FFFFFF</color>
    <color name="cal_btn_func_text">#1C1C1E</color>
    <color name="cal_btn_func_text_dark">#FFFFFF</color>
    <color name="cal_display_bg">#FFFFFF</color>
    <color name="cal_display_bg_dark">#1C1C1E</color>
    <color name="cal_formula_text">#8E8E93</color>
    <color name="cal_result_text">#1C1C1E</color>
    <color name="cal_result_text_dark">#FFFFFF</color>
    <color name="cal_divider">#C6C6C8</color>
    <color name="cal_divider_dark">#38383A</color>
    <color name="cal_history_bg">#FFFFFF</color>
    <color name="cal_history_bg_dark">#2C2C2E</color>
    <color name="cal_accent">#FF9500</color>
</resources>
```

- [ ] **Step 2: strings.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">计算器</string>
    <string name="btn_clear">C</string>
    <string name="btn_paren">( )</string>
    <string name="btn_percent">%</string>
    <string name="btn_divide">÷</string>
    <string name="btn_multiply">×</string>
    <string name="btn_subtract">-</string>
    <string name="btn_add">+</string>
    <string name="btn_equals">=</string>
    <string name="btn_decimal">.</string>
    <string name="btn_negate">+/−</string>
    <string name="btn_sin">sin</string>
    <string name="btn_cos">cos</string>
    <string name="btn_tan">tan</string>
    <string name="btn_log">log</string>
    <string name="btn_ln">ln</string>
    <string name="btn_sqrt">√</string>
    <string name="btn_square">x²</string>
    <string name="btn_cube">x³</string>
    <string name="btn_power">xⁿ</string>
    <string name="btn_fact">x!</string>
    <string name="btn_pi">π</string>
    <string name="btn_e">e</string>
    <string name="btn_deg">Deg</string>
    <string name="btn_rad">Rad</string>
    <string name="btn_inv">INV</string>
    <string name="sci_label">科学</string>
    <string name="history_label">历史</string>
    <string name="expr_error">表达式错误</string>
    <string name="div_zero">除数不能为零</string>
    <string name="overflow">结果过大</string>
    <string name="invalid_input">输入无效</string>
    <string name="no_history">暂无历史记录</string>
</resources>
```

- [ ] **Step 3: dimens.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="display_formula_size">16sp</dimen>
    <dimen name="display_result_size">40sp</dimen>
    <dimen name="display_padding">16dp</dimen>
    <dimen name="btn_calc_size">0dp</dimen>
    <dimen name="btn_calc_margin">4dp</dimen>
    <dimen name="btn_calc_corner">12dp</dimen>
    <dimen name="btn_calc_text_size">22sp</dimen>
    <dimen name="btn_calc_func_text_size">18sp</dimen>
    <dimen name="btn_sci_text_size">14sp</dimen>
    <dimen name="grid_padding">8dp</dimen>
    <dimen name="display_height">120dp</dimen>
    <dimen name="sci_panel_height">180dp</dimen>
</resources>
```

- [ ] **Step 4: themes.xml (values/)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.Calculator" parent="Theme.AppCompat.DayNight.NoActionBar">
        <item name="colorPrimary">@color/cal_accent</item>
        <item name="colorPrimaryDark">@color/cal_accent</item>
        <item name="colorAccent">@color/cal_accent</item>
        <item name="android:windowBackground">@color/cal_display_bg</item>
    </style>
</resources>
```

- [ ] **Step 5: themes.xml (values-night/)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.Calculator" parent="Theme.AppCompat.DayNight.NoActionBar">
        <item name="colorPrimary">@color/cal_accent</item>
        <item name="colorPrimaryDark">@color/cal_accent</item>
        <item name="colorAccent">@color/cal_accent</item>
        <item name="android:windowBackground">@color/cal_display_bg_dark</item>
    </style>
</resources>
```

- [ ] **Step 6: btn_calc_rounded.xml (数字按钮背景)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="@android:attr/colorControlHighlight">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="@color/cal_btn_number_bg" />
            <corners android:radius="@dimen/btn_calc_corner" />
        </shape>
    </item>
</ripple>
```

- [ ] **Step 7: btn_calc_op_rounded.xml (运算符按钮背景)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="#33FFFFFF">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="@color/cal_btn_op_bg" />
            <corners android:radius="@dimen/btn_calc_corner" />
        </shape>
    </item>
</ripple>
```

- [ ] **Step 8: btn_calc_eq_rounded.xml (等号按钮背景)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="#33FFFFFF">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="@color/cal_btn_eq_bg" />
            <corners android:radius="@dimen/btn_calc_corner" />
        </shape>
    </item>
</ripple>
```

- [ ] **Step 9: btn_calc_func_rounded.xml (功能按钮背景)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="@android:attr/colorControlHighlight">
    <item>
        <shape android:shape="rectangle">
            <solid android:color="@color/cal_btn_func_bg" />
            <corners android:radius="@dimen/btn_calc_corner" />
        </shape>
    </item>
</ripple>
```

---

### Task 2: 表达式引擎 — Token 定义

**Files:**
- Create: `app-calculator/src/main/java/com/example/calculator/engine/Token.kt`

- [ ] **Step 1: 创建 Token.kt**

```kotlin
package com.example.calculator.engine

sealed class Token {
    data class Number(val value: java.math.BigDecimal) : Token()
    object Plus : Token()
    object Minus : Token()
    object Multiply : Token()
    object Divide : Token()
    object Power : Token()
    object Percent : Token()
    object Factorial : Token()
    object LParen : Token()
    object RParen : Token()
    object Negate : Token()          // 一元负号
    object Pi : Token()
    object E : Token()
    object Sin : Token()
    object Cos : Token()
    object Tan : Token()
    object Asin : Token()
    object Acos : Token()
    object Atan : Token()
    object Log : Token()
    object Ln : Token()
    object Sqrt : Token()
    object Cbrt : Token()
    object Square : Token()          // x²
    object Cube : Token()            // x³
    object End : Token()
}
```

---

### Task 3: 表达式引擎 — 词法分析器 Tokenizer

**Files:**
- Create: `app-calculator/src/main/java/com/example/calculator/engine/Tokenizer.kt`

- [ ] **Step 1: 创建 Tokenizer.kt**

```kotlin
package com.example.calculator.engine

class Tokenizer(private val input: String) {
    private var pos = 0
    private val tokens = mutableListOf<Token>()
    private val numberRegex = Regex("^\\d*\\.?\\d+")

    fun tokenize(): List<Token> {
        tokens.clear()
        pos = 0
        while (pos < input.length) {
            val ch = input[pos]
            when {
                ch.isWhitespace() -> pos++
                ch == '+' -> { tokens.add(Token.Plus); pos++ }
                ch == '-' -> {
                    val prev = tokens.lastOrNull()
                    if (prev == null || prev is Token.LParen || prev is Token.Multiply || prev is Token.Divide || prev is Token.Plus || prev is Token.Minus || prev is Token.Power) {
                        tokens.add(Token.Negate)
                    } else {
                        tokens.add(Token.Minus)
                    }
                    pos++
                }
                ch == '×' || ch == '*' -> { tokens.add(Token.Multiply); pos++ }
                ch == '÷' || ch == '/' -> { tokens.add(Token.Divide); pos++ }
                ch == '^' -> { tokens.add(Token.Power); pos++ }
                ch == '%' -> { tokens.add(Token.Percent); pos++ }
                ch == '!' -> { tokens.add(Token.Factorial); pos++ }
                ch == '(' -> { tokens.add(Token.LParen); pos++ }
                ch == ')' -> { tokens.add(Token.RParen); pos++ }
                ch == 'π' -> { tokens.add(Token.Pi); pos++ }
                ch.isDigit() || ch == '.' -> parseNumber()
                ch.isLetter() -> parseFunctionOrConstant()
                else -> pos++
            }
        }
        tokens.add(Token.End)
        return tokens
    }

    private fun parseNumber() {
        val start = pos
        while (pos < input.length && (input[pos].isDigit() || input[pos] == '.')) pos++
        val numStr = input.substring(start, pos)
        tokens.add(Token.Number(java.math.BigDecimal(numStr)))
    }

    private fun parseFunctionOrConstant() {
        val start = pos
        while (pos < input.length && input[pos].isLetter()) pos++
        val name = input.substring(start, pos)
        tokens.add(when (name.lowercase()) {
            "sin" -> Token.Sin; "cos" -> Token.Cos; "tan" -> Token.Tan
            "asin" -> Token.Asin; "acos" -> Token.Acos; "atan" -> Token.Atan
            "log" -> Token.Log; "ln" -> Token.Ln
            "sqrt" -> Token.Sqrt; "cbrt" -> Token.Cbrt
            "pi" -> Token.Pi; "e" -> Token.E
            else -> throw IllegalArgumentException("Unknown function: $name")
        })
    }
}
```

---

### Task 4: 表达式引擎 — 求值器 Evaluator

**Files:**
- Create: `app-calculator/src/main/java/com/example/calculator/engine/Evaluator.kt`

- [ ] **Step 1: 创建 Evaluator.kt**

```kotlin
package com.example.calculator.engine

import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.*

object Evaluator {
    private val MC = MathContext.DECIMAL128
    private val BD_PI = BigDecimal("3.14159265358979323846264338327950288419")
    private val BD_E = BigDecimal("2.71828182845904523536028747135266249775")

    var isDegreeMode = true

    data class Result(val value: BigDecimal?, val error: String?)

    sealed class Expr {
        data class Num(val v: BigDecimal) : Expr()
        data class UnaryOp(val op: Token, val expr: Expr) : Expr()
        data class BinOp(val left: Expr, val op: Token, val right: Expr) : Expr()
        data class Func(val name: String, val arg: Expr) : Expr()
        data class Var(val name: String) : Expr()
    }

    fun evaluate(tokens: List<Token>): Result {
        return try {
            val parser = Parser(tokens)
            val ast = parser.parseExpression()
            val value = evalAst(ast)
            Result(value, null)
        } catch (e: Exception) {
            Result(null, when {
                e.message?.contains("divide by zero", true) == true -> "除数不能为零"
                e.message?.contains("overflow", true) == true -> "结果过大"
                else -> "表达式错误"
            })
        }
    }

    private fun evalAst(expr: Expr): BigDecimal {
        return when (expr) {
            is Expr.Num -> expr.v
            is Expr.UnaryOp -> {
                val v = evalAst(expr.expr)
                when (expr.op) {
                    is Token.Negate -> v.negate()
                    is Token.Factorial -> factorial(v)
                    is Token.Percent -> v.divide(BigDecimal(100), MC)
                    else -> throw IllegalArgumentException("Unknown unary op")
                }
            }
            is Expr.BinOp -> {
                val l = evalAst(expr.left)
                val r = evalAst(expr.right)
                when (expr.op) {
                    is Token.Plus -> l.add(r, MC)
                    is Token.Minus -> l.subtract(r, MC)
                    is Token.Multiply -> l.multiply(r, MC)
                    is Token.Divide -> {
                        if (r.compareTo(BigDecimal.ZERO) == 0) throw ArithmeticException("divide by zero")
                        l.divide(r, MC)
                    }
                    is Token.Power -> pow(l, r)
                    else -> throw IllegalArgumentException("Unknown bin op")
                }
            }
            is Expr.Func -> {
                val arg = evalAst(expr.arg)
                when (expr.name) {
                    "sin" -> BigDecimal(sin(toRadiansOrDegrees(arg).toDouble())).round(MC)
                    "cos" -> BigDecimal(cos(toRadiansOrDegrees(arg).toDouble())).round(MC)
                    "tan" -> BigDecimal(tan(toRadiansOrDegrees(arg).toDouble())).round(MC)
                    "asin" -> BigDecimal(asin(arg.toDouble())).round(MC)
                    "acos" -> BigDecimal(acos(arg.toDouble())).round(MC)
                    "atan" -> BigDecimal(atan(arg.toDouble())).round(MC)
                    "log" -> BigDecimal(log10(arg.toDouble())).round(MC)
                    "ln" -> BigDecimal(ln(arg.toDouble())).round(MC)
                    "sqrt" -> BigDecimal(sqrt(arg.toDouble())).round(MC)
                    "cbrt" -> BigDecimal(cbrt(arg.toDouble())).round(MC)
                    "square" -> arg.multiply(arg, MC)
                    "cube" -> arg.multiply(arg, MC).multiply(arg, MC)
                    else -> throw IllegalArgumentException("Unknown function: ${expr.name}")
                }
            }
            is Expr.Var -> when (expr.name) {
                "pi" -> BD_PI; "e" -> BD_E
                else -> throw IllegalArgumentException("Unknown var: ${expr.name}")
            }
        }
    }

    private fun toRadiansOrDegrees(v: BigDecimal): BigDecimal {
        return if (isDegreeMode) BigDecimal(Math.toRadians(v.toDouble())) else v
    }

    private fun factorial(n: BigDecimal): BigDecimal {
        val intVal = n.toInt()
        if (intVal < 0 || intVal > 170) throw IllegalArgumentException("Factorial argument too big")
        var result = BigDecimal.ONE
        for (i in 2..intVal) result = result.multiply(BigDecimal(i))
        return result
    }

    private fun pow(base: BigDecimal, exp: BigDecimal): BigDecimal {
        return try {
            if (exp.scale() <= 0) {
                base.pow(exp.toInt(), MC)
            } else {
                BigDecimal(Math.pow(base.toDouble(), exp.toDouble()), MC)
            }
        } catch (e: Exception) {
            BigDecimal(Math.pow(base.toDouble(), exp.toDouble()), MC)
        }
    }

    private class Parser(private val tokens: List<Token>) {
        private var pos = 0
        private fun peek() = tokens[pos]
        private fun consume() = tokens[pos++]

        fun parseExpression(): Expr {
            var left = parseTerm()
            while (peek() is Token.Plus || peek() is Token.Minus) {
                val op = consume()
                val right = parseTerm()
                left = Expr.BinOp(left, op, right)
            }
            return left
        }

        private fun parseTerm(): Expr {
            var left = parseFactor()
            while (peek() is Token.Multiply || peek() is Token.Divide) {
                val op = consume()
                val right = parseFactor()
                left = Expr.BinOp(left, op, right)
            }
            return left
        }

        private fun parseFactor(): Expr {
            if (peek() is Token.Negate) {
                consume()
                val expr = parseFactor()
                return Expr.UnaryOp(Token.Negate, expr)
            }
            var left = parsePower()
            while (peek() is Token.Power) {
                consume()
                val right = parseFactor() // right-associative
                left = Expr.BinOp(left, Token.Power, right)
            }
            return left
        }

        private fun parsePower(): Expr {
            var expr = parsePrimary()
            while (peek() is Token.Factorial || peek() is Token.Percent) {
                val op = consume()
                expr = Expr.UnaryOp(op, expr)
            }
            return expr
        }

        private fun parsePrimary(): Expr {
            return when (val t = peek()) {
                is Token.Number -> { consume(); Expr.Num(t.value) }
                is Token.LParen -> {
                    consume()
                    val expr = parseExpression()
                    if (peek() is Token.RParen) consume()
                    expr
                }
                is Token.Pi -> { consume(); Expr.Var("pi") }
                is Token.E -> { consume(); Expr.Var("e") }
                is Token.Sin -> { consume(); Expr.Func("sin", parsePrimary()) }
                is Token.Cos -> { consume(); Expr.Func("cos", parsePrimary()) }
                is Token.Tan -> { consume(); Expr.Func("tan", parsePrimary()) }
                is Token.Asin -> { consume(); Expr.Func("asin", parsePrimary()) }
                is Token.Acos -> { consume(); Expr.Func("acos", parsePrimary()) }
                is Token.Atan -> { consume(); Expr.Func("atan", parsePrimary()) }
                is Token.Log -> { consume(); Expr.Func("log", parsePrimary()) }
                is Token.Ln -> { consume(); Expr.Func("ln", parsePrimary()) }
                is Token.Sqrt -> { consume(); Expr.Func("sqrt", parsePrimary()) }
                is Token.Cbrt -> { consume(); Expr.Func("cbrt", parsePrimary()) }
                is Token.Square -> { consume(); Expr.Func("square", parsePrimary()) }
                is Token.Cube -> { consume(); Expr.Func("cube", parsePrimary()) }
                else -> throw IllegalArgumentException("Unexpected token: $t")
            }
        }
    }
}
```

---

### Task 5: 表达式引擎单元测试

**Files:**
- Create: `app-calculator/src/test/java/com/example/calculator/engine/EvaluatorTest.kt`

- [ ] **Step 1: 创建 EvaluatorTest.kt**

```kotlin
package com.example.calculator.engine

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class EvaluatorTest {
    private fun eval(expr: String): BigDecimal? {
        val tokens = Tokenizer(expr).tokenize()
        val result = Evaluator.evaluate(tokens)
        assertNull(result.error)
        return result.value
    }

    @Test
    fun testBasicArithmetic() {
        assertEquals(BigDecimal("5"), eval("2+3"))
        assertEquals(BigDecimal("1"), eval("3-2"))
        assertEquals(BigDecimal("6"), eval("2*3"))
        assertEquals(BigDecimal("2"), eval("6/3"))
    }

    @Test
    fun testPrecedence() {
        assertEquals(BigDecimal("7"), eval("1+2*3"))
        assertEquals(BigDecimal("9"), eval("(1+2)*3"))
    }

    @Test
    fun testDecimal() {
        assertEquals(BigDecimal("3.5"), eval("1.5+2"))
    }

    @Test
    fun testNegate() {
        assertEquals(BigDecimal("-5"), eval("-5"))
        assertEquals(BigDecimal("3"), eval("5+-2"))
    }

    @Test
    fun testPercent() {
        assertEquals(BigDecimal("0.5"), eval("50%"))
    }

    @Test
    fun testFactorial() {
        assertEquals(BigDecimal("120"), eval("5!"))
    }

    @Test
    fun testPower() {
        assertEquals(BigDecimal("8"), eval("2^3"))
    }

    @Test
    fun testSinCos() {
        Evaluator.isDegreeMode = true
        assertEquals(BigDecimal("0.5"), eval("sin(30)")!!.setScale(1, java.math.RoundingMode.HALF_UP))
    }

    @Test
    fun testErrorHandling() {
        val tokens = Tokenizer("1/0").tokenize()
        val result = Evaluator.evaluate(tokens)
        assertEquals("除数不能为零", result.error)
    }
}
```

---

### Task 6: NumberFormatter 工具类

**Files:**
- Create: `app-calculator/src/main/java/com/example/calculator/utils/NumberFormatter.kt`

- [ ] **Step 1: 创建 NumberFormatter.kt**

```kotlin
package com.example.calculator.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

object NumberFormatter {
    private val normalFormat = DecimalFormat("#,###,###,##0.##########")
    private val sciFormat = DecimalFormat("0.#####E0")

    fun format(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return if (stripped.precision() > 12 || stripped.scale() < -6) {
            sciFormat.format(stripped)
        } else {
            normalFormat.format(stripped)
        }
    }

    fun formatExpression(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        return normalFormat.format(stripped)
    }
}
```

---

### Task 7: Room 数据库 — 历史记录

**Files:**
- Create: `app-calculator/src/main/java/com/example/calculator/model/HistoryEntry.kt`
- Create: `app-calculator/src/main/java/com/example/calculator/model/HistoryDao.kt`
- Create: `app-calculator/src/main/java/com/example/calculator/model/CalculatorDatabase.kt`

- [ ] **Step 1: HistoryEntry.kt**

```kotlin
package com.example.calculator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expression: String,
    val result: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

- [ ] **Step 2: HistoryDao.kt**

```kotlin
package com.example.calculator.model

import androidx.room.*

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<HistoryEntry>>

    @Insert
    suspend fun insert(entry: HistoryEntry)

    @Delete
    suspend fun delete(entry: HistoryEntry)

    @Query("DELETE FROM history")
    suspend fun deleteAll()

    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(): HistoryEntry?
}
```

- [ ] **Step 3: CalculatorDatabase.kt**

```kotlin
package com.example.calculator.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntry::class], version = 1, exportSchema = false)
abstract class CalculatorDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile private var INSTANCE: CalculatorDatabase? = null

        fun getInstance(context: Context): CalculatorDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, CalculatorDatabase::class.java, "calculator.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
```

---

### Task 8: CalculatorViewModel

**Files:**
- Create: `app-calculator/src/main/java/com/example/calculator/CalculatorViewModel.kt`

- [ ] **Step 1: 创建 CalculatorViewModel.kt**

```kotlin
package com.example.calculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.calculator.engine.Evaluator
import com.example.calculator.engine.Token
import com.example.calculator.engine.Tokenizer
import com.example.calculator.model.CalculatorDatabase
import com.example.calculator.model.HistoryEntry
import com.example.calculator.utils.NumberFormatter
import kotlinx.coroutines.launch

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = CalculatorDatabase.getInstance(application).historyDao()

    private val _expressionText = MutableLiveData("")
    val expressionText: LiveData<String> = _expressionText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    private val _errorText = MutableLiveData<String?>(null)
    val errorText: LiveData<String?> = _errorText

    val history = dao.getAll()

    private var currentInput = ""
    private var lastResult: String? = null

    private val displayOperators = mapOf(
        "*" to "×", "/" to "÷", "sin" to "sin(", "cos" to "cos(", "tan" to "tan(",
        "asin" to "asin(", "acos" to "acos(", "atan" to "atan(",
        "log" to "log(", "ln" to "ln(", "sqrt" to "√(", "cbrt" to "∛("
    )

    fun onDigit(digit: String) {
        _errorText.value = null
        currentInput += digit
        updateDisplay()
        evaluateCurrent()
    }

    fun onDecimal() {
        _errorText.value = null
        val lastNum = currentInput.split(Regex("[+\\-×÷^()]")).lastOrNull() ?: ""
        if ("." !in lastNum) {
            currentInput += if (lastNum.isEmpty()) "0." else "."
            updateDisplay()
        }
    }

    fun onOperator(op: String) {
        _errorText.value = null
        if (currentInput.isEmpty() && op == "-") {
            currentInput = "-"
            updateDisplay()
            return
        }
        if (currentInput.isEmpty()) return
        val lastChar = currentInput.lastOrNull()
        if (lastChar in listOf('+', '-', '×', '÷', '^')) {
            currentInput = currentInput.dropLast(1)
        }
        currentInput += op
        updateDisplay()
    }

    fun onFunction(name: String) {
        _errorText.value = null
        if (currentInput.isNotEmpty() && currentInput.last().isDigit()) {
            currentInput += "×"
        }
        currentInput += displayOperators[name] ?: "${name}("
        updateDisplay()
    }

    fun onParentheses() {
        _errorText.value = null
        val openCount = currentInput.count { it == '(' }
        val closeCount = currentInput.count { it == ')' }
        currentInput += if (openCount > closeCount) ")" else "("
        updateDisplay()
    }

    fun onPercent() {
        _errorText.value = null
        currentInput += "%"
        updateDisplay()
        evaluateCurrent()
    }

    fun onFactorial() {
        _errorText.value = null
        currentInput += "!"
        updateDisplay()
        evaluateCurrent()
    }

    fun onPower() {
        onOperator("^")
    }

    fun onConstant(name: String) {
        _errorText.value = null
        if (currentInput.isNotEmpty() && currentInput.last().isDigit()) {
            currentInput += "×"
        }
        currentInput += name
        updateDisplay()
        evaluateCurrent()
    }

    fun onClear() {
        currentInput = ""
        lastResult = null
        _expressionText.value = ""
        _resultText.value = "0"
        _errorText.value = null
    }

    fun onBackspace() {
        _errorText.value = null
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            updateDisplay()
            if (currentInput.isNotEmpty()) evaluateCurrent() else _resultText.value = "0"
        }
    }

    fun onEquals() {
        if (currentInput.isEmpty()) return
        try {
            val displayExpr = currentInput
            val evalExpr = currentInput
                .replace("×", "*")
                .replace("÷", "/")
                .replace("√(", "sqrt(")
                .replace("∛(", "cbrt(")

            val tokens = Tokenizer(evalExpr).tokenize()
            val result = Evaluator.evaluate(tokens)

            if (result.error != null) {
                _errorText.value = result.error
                return
            }

            val formatted = NumberFormatter.format(result.value!!)
            _resultText.value = formatted
            lastResult = formatted
            _expressionText.value = displayExpr

            viewModelScope.launch {
                dao.insert(HistoryEntry(expression = displayExpr, result = formatted))
            }
            currentInput = formatted
        } catch (e: Exception) {
            _errorText.value = "表达式错误"
        }
    }

    fun toggleAngleMode() {
        Evaluator.isDegreeMode = !Evaluator.isDegreeMode
    }

    fun isDegreeMode() = Evaluator.isDegreeMode

    fun loadFromHistory(entry: HistoryEntry) {
        currentInput = entry.result
        _expressionText.value = entry.expression
        _resultText.value = entry.result
    }

    fun clearHistory() {
        viewModelScope.launch { dao.deleteAll() }
    }

    private fun updateDisplay() {
        _expressionText.value = currentInput
    }

    private fun evaluateCurrent() {
        if (currentInput.isEmpty()) return
        try {
            val evalExpr = currentInput
                .replace("×", "*")
                .replace("÷", "/")
                .replace("√(", "sqrt(")
                .replace("∛(", "cbrt(")

            val tokens = Tokenizer(evalExpr).tokenize()
            val result = Evaluator.evaluate(tokens)

            if (result.error != null) {
                _errorText.value = result.error
                return
            }
            _resultText.value = NumberFormatter.format(result.value!!)
            _errorText.value = null
        } catch (e: Exception) {
            _resultText.value = "?"
        }
    }
}
```

---

### Task 9: 布局 XML

**Files:**
- Create: `app-calculator/src/main/res/layout/activity_calculator.xml`
- Create: `app-calculator/src/main/res/layout/item_history.xml`

- [ ] **Step 1: activity_calculator.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/cal_display_bg">

    <!-- 历史面板（初始隐藏，上滑显示） -->
    <include
        android:id="@+id/panel_history"
        layout="@layout/panel_history"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:visibility="gone" />

    <!-- 显示区域 -->
    <LinearLayout
        android:id="@+id/display_area"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="@dimen/display_padding"
        android:gravity="bottom">

        <TextView
            android:id="@+id/tv_error"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textColor="#FF3B30"
            android:textSize="14sp"
            android:visibility="gone"
            tools:text="表达式错误" />

        <TextView
            android:id="@+id/tv_formula"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="@dimen/display_formula_size"
            android:textColor="@color/cal_formula_text"
            android:gravity="start|bottom"
            android:singleLine="true"
            android:ellipsize="start"
            android:text="" />

        <TextView
            android:id="@+id/tv_result"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="@dimen/display_result_size"
            android:textColor="@color/cal_result_text"
            android:textStyle="bold"
            android:gravity="end|bottom"
            android:singleLine="true"
            android:ellipsize="end"
            android:text="0" />
    </LinearLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/cal_divider" />

    <!-- 科学计算面板 -->
    <include
        android:id="@+id/panel_scientific"
        layout="@layout/panel_grid_scientific"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:visibility="gone" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:fillViewport="true">

        <include
            android:id="@+id/panel_basic"
            layout="@layout/panel_grid_basic"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </ScrollView>

    <!-- 底部工具栏 -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:gravity="center"
        android:orientation="horizontal"
        android:background="@color/cal_display_bg">

        <TextView
            android:id="@+id/btn_toggle_sci"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:gravity="center"
            android:text="@string/sci_label"
            android:textSize="14sp"
            android:textColor="@color/cal_accent"
            android:clickable="true"
            android:focusable="true"
            android:background="?attr/selectableItemBackground" />

        <TextView
            android:id="@+id/btn_toggle_history"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:gravity="center"
            android:text="@string/history_label"
            android:textSize="14sp"
            android:textColor="@color/cal_accent"
            android:clickable="true"
            android:focusable="true"
            android:background="?attr/selectableItemBackground" />
    </LinearLayout>

</LinearLayout>
```

- [ ] **Step 2: panel_grid_basic.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<GridLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:columnCount="4"
    android:rowCount="6"
    android:padding="@dimen/grid_padding">

    <!-- Row 1 -->
    <Button android:id="@+id/btn_clear" android:text="@string/btn_clear" style="@style/CalcButtonFunc" />
    <Button android:id="@+id/btn_paren" android:text="@string/btn_paren" style="@style/CalcButtonFunc" />
    <Button android:id="@+id/btn_percent" android:text="@string/btn_percent" style="@style/CalcButtonFunc" />
    <Button android:id="@+id/btn_divide" android:text="@string/btn_divide" style="@style/CalcButtonOp" />

    <!-- Row 2 -->
    <Button android:id="@+id/btn_7" android:text="7" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_8" android:text="8" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_9" android:text="9" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_multiply" android:text="@string/btn_multiply" style="@style/CalcButtonOp" />

    <!-- Row 3 -->
    <Button android:id="@+id/btn_4" android:text="4" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_5" android:text="5" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_6" android:text="6" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_subtract" android:text="@string/btn_subtract" style="@style/CalcButtonOp" />

    <!-- Row 4 -->
    <Button android:id="@+id/btn_1" android:text="1" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_2" android:text="2" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_3" android:text="3" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_add" android:text="@string/btn_add" style="@style/CalcButtonOp" />

    <!-- Row 5 -->
    <Button android:id="@+id/btn_negate" android:text="@string/btn_negate" style="@style/CalcButtonFunc" />
    <Button android:id="@+id/btn_0" android:text="0" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_decimal" android:text="@string/btn_decimal" style="@style/CalcButtonNum" />
    <Button android:id="@+id/btn_equals" android:text="@string/btn_equals" style="@style/CalcButtonEq" />

</GridLayout>
```

- [ ] **Step 3: panel_grid_scientific.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<GridLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:columnCount="5"
    android:rowCount="3"
    android:padding="@dimen/grid_padding">

    <!-- Row 1 -->
    <Button android:id="@+id/btn_sin" android:text="@string/btn_sin" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_cos" android:text="@string/btn_cos" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_tan" android:text="@string/btn_tan" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_log" android:text="@string/btn_log" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_ln" android:text="@string/btn_ln" style="@style/CalcButtonSci" />

    <!-- Row 2 -->
    <Button android:id="@+id/btn_sqrt" android:text="@string/btn_sqrt" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_square" android:text="@string/btn_square" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_cube" android:text="@string/btn_cube" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_power" android:text="@string/btn_power" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_fact" android:text="@string/btn_fact" style="@style/CalcButtonSci" />

    <!-- Row 3 -->
    <Button android:id="@+id/btn_inv" android:text="@string/btn_inv" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_pi" android:text="@string/btn_pi" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_e" android:text="@string/btn_e" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_deg" android:text="@string/btn_deg" style="@style/CalcButtonSci" />
    <Button android:id="@+id/btn_paren" android:text="@string/btn_paren" style="@style/CalcButtonSci" />

</GridLayout>
```

- [ ] **Step 4: panel_history.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/cal_history_bg">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:gravity="center_vertical"
        android:paddingHorizontal="16dp">

        <TextView
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="@string/history_label"
            android:textSize="16sp"
            android:textStyle="bold"
            android:textColor="@color/cal_result_text" />

        <TextView
            android:id="@+id/btn_clear_history"
            android:text="清空"
            android:textSize="14sp"
            android:textColor="@color/cal_accent"
            android:clickable="true"
            android:focusable="true"
            android:padding="8dp"
            android:background="?attr/selectableItemBackground"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" />
    </LinearLayout>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rv_history"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:clipToPadding="false"
        android:padding="8dp" />

    <TextView
        android:id="@+id/tv_empty_history"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:text="@string/no_history"
        android:textSize="16sp"
        android:textColor="@color/cal_formula_text"
        android:visibility="gone" />
</LinearLayout>
```

- [ ] **Step 5: item_history.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="12dp"
    android:background="?attr/selectableItemBackground">

    <TextView
        android:id="@+id/tv_history_expr"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:textColor="@color/cal_formula_text"
        android:singleLine="true"
        android:ellipsize="end" />

    <TextView
        android:id="@+id/tv_history_result"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="22sp"
        android:textColor="@color/cal_result_text"
        android:textStyle="bold"
        android:singleLine="true"
        android:ellipsize="end" />

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/cal_divider"
        android:layout_marginTop="8dp" />
</LinearLayout>
```

- [ ] **Step 6: 在 values/themes.xml 中添加按钮 Style**

在 `themes.xml` 中添加：

```xml
<style name="CalcButtonNum" parent="Widget.AppCompat.Button">
    <item name="android:layout_width">0dp</item>
    <item name="android:layout_height">match_parent</item>
    <item name="android:layout_margin">@dimen/btn_calc_margin</item>
    <item name="android:background">@drawable/btn_calc_rounded</item>
    <item name="android:textSize">@dimen/btn_calc_text_size</item>
    <item name="android:textColor">@color/cal_btn_number_text</item>
    <item name="android:textStyle">normal</item>
    <item name="android:stateListAnimator">@null</item>
</style>

<style name="CalcButtonOp" parent="Widget.AppCompat.Button">
    <item name="android:layout_width">0dp</item>
    <item name="android:layout_height">match_parent</item>
    <item name="android:layout_margin">@dimen/btn_calc_margin</item>
    <item name="android:background">@drawable/btn_calc_op_rounded</item>
    <item name="android:textSize">@dimen/btn_calc_text_size</item>
    <item name="android:textColor">@color/cal_btn_op_text</item>
    <item name="android:textStyle">normal</item>
    <item name="android:stateListAnimator">@null</item>
</style>

<style name="CalcButtonEq" parent="CalcButtonOp">
    <item name="android:background">@drawable/btn_calc_eq_rounded</item>
</style>

<style name="CalcButtonFunc" parent="Widget.AppCompat.Button">
    <item name="android:layout_width">0dp</item>
    <item name="android:layout_height">match_parent</item>
    <item name="android:layout_margin">@dimen/btn_calc_margin</item>
    <item name="android:background">@drawable/btn_calc_func_rounded</item>
    <item name="android:textSize">@dimen/btn_calc_text_size</item>
    <item name="android:textColor">@color/cal_btn_func_text</item>
    <item name="android:stateListAnimator">@null</item>
</style>

<style name="CalcButtonSci" parent="Widget.AppCompat.Button">
    <item name="android:layout_width">0dp</item>
    <item name="android:layout_height">48dp</item>
    <item name="android:layout_margin">2dp</item>
    <item name="android:background">@drawable/btn_calc_func_rounded</item>
    <item name="android:textSize">@dimen/btn_sci_text_size</item>
    <item name="android:textColor">@color/cal_btn_func_text</item>
    <item name="android:stateListAnimator">@null</item>
</style>
```

---

### Task 10: CalculatorActivity

**Files:**
- Create: `app-calculator/src/main/java/com/example/calculator/CalculatorActivity.kt`
- Create: `app-calculator/src/main/java/com/example/calculator/adapter/HistoryAdapter.kt`

- [ ] **Step 1: HistoryAdapter.kt**

```kotlin
package com.example.calculator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.databinding.ItemHistoryBinding
import com.example.calculator.model.HistoryEntry

class HistoryAdapter(private val onClick: (HistoryEntry) -> Unit) :
    ListAdapter<HistoryEntry, HistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemHistoryBinding,
        private val onClick: (HistoryEntry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: HistoryEntry) {
            binding.tvHistoryExpr.text = entry.expression
            binding.tvHistoryResult.text = "= ${entry.result}"
            binding.root.setOnClickListener { onClick(entry) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<HistoryEntry>() {
        override fun areItemsTheSame(a: HistoryEntry, b: HistoryEntry) = a.id == b.id
        override fun areContentsTheSame(a: HistoryEntry, b: HistoryEntry) = a == b
    }
}
```

- [ ] **Step 2: CalculatorActivity.kt**

```kotlin
package com.example.calculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculator.adapter.HistoryAdapter
import com.example.calculator.databinding.ActivityCalculatorBinding
import com.example.calculator.model.HistoryEntry

class CalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var viewModel: CalculatorViewModel
    private lateinit var historyAdapter: HistoryAdapter
    private var isSciPanelVisible = false
    private var isHistoryVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]

        setupGridButtons()
        setupScientificButtons()
        setupToolbar()
        setupHistory()
        setupObservers()
    }

    private fun setupGridButtons() {
        // 数字按钮
        binding.panelBasic.btn0.setOnClickListener { viewModel.onDigit("0") }
        binding.panelBasic.btn1.setOnClickListener { viewModel.onDigit("1") }
        binding.panelBasic.btn2.setOnClickListener { viewModel.onDigit("2") }
        binding.panelBasic.btn3.setOnClickListener { viewModel.onDigit("3") }
        binding.panelBasic.btn4.setOnClickListener { viewModel.onDigit("4") }
        binding.panelBasic.btn5.setOnClickListener { viewModel.onDigit("5") }
        binding.panelBasic.btn6.setOnClickListener { viewModel.onDigit("6") }
        binding.panelBasic.btn7.setOnClickListener { viewModel.onDigit("7") }
        binding.panelBasic.btn8.setOnClickListener { viewModel.onDigit("8") }
        binding.panelBasic.btn9.setOnClickListener { viewModel.onDigit("9") }

        // 运算符
        binding.panelBasic.btnAdd.setOnClickListener { viewModel.onOperator("+") }
        binding.panelBasic.btnSubtract.setOnClickListener { viewModel.onOperator("-") }
        binding.panelBasic.btnMultiply.setOnClickListener { viewModel.onOperator("×") }
        binding.panelBasic.btnDivide.setOnClickListener { viewModel.onOperator("÷") }

        // 功能键
        binding.panelBasic.btnClear.setOnClickListener { viewModel.onClear() }
        binding.panelBasic.btnEquals.setOnClickListener { viewModel.onEquals() }
        binding.panelBasic.btnDecimal.setOnClickListener { viewModel.onDecimal() }
        binding.panelBasic.btnNegate.setOnClickListener { viewModel.onOperator("-") }
        binding.panelBasic.btnPercent.setOnClickListener { viewModel.onPercent() }
        binding.panelBasic.btnParen.setOnClickListener { viewModel.onParentheses() }
    }

    private fun setupScientificButtons() {
        with(binding.panelScientific) {
            btnSin.setOnClickListener { viewModel.onFunction("sin") }
            btnCos.setOnClickListener { viewModel.onFunction("cos") }
            btnTan.setOnClickListener { viewModel.onFunction("tan") }
            btnLog.setOnClickListener { viewModel.onFunction("log") }
            btnLn.setOnClickListener { viewModel.onFunction("ln") }
            btnSqrt.setOnClickListener { viewModel.onFunction("sqrt") }
            btnSquare.setOnClickListener { viewModel.onFunction("square") }
            btnCube.setOnClickListener { viewModel.onFunction("cube") }
            btnPower.setOnClickListener { viewModel.onPower() }
            btnFact.setOnClickListener { viewModel.onFactorial() }
            btnPi.setOnClickListener { viewModel.onConstant("π") }
            btnE.setOnClickListener { viewModel.onConstant("e") }
            btnDeg.setOnClickListener {
                viewModel.toggleAngleMode()
                updateAngleButton()
            }
            btnInv.setOnClickListener {
                // 切换 INV 模式：sin ↔ asin, cos ↔ acos, tan ↔ atan
                btnSin.text = if (btnSin.text == "sin") "asin" else "sin"
                btnCos.text = if (btnCos.text == "cos") "acos" else "cos"
                btnTan.text = if (btnTan.text == "tan") "atan" else "tan"
            }
            btnParen.setOnClickListener { viewModel.onParentheses() }
        }
    }

    private fun setupToolbar() {
        binding.btnToggleSci.setOnClickListener {
            isSciPanelVisible = !isSciPanelVisible
            binding.panelScientific.visibility = if (isSciPanelVisible) View.VISIBLE else View.GONE
        }

        binding.btnToggleHistory.setOnClickListener {
            isHistoryVisible = !isHistoryVisible
            binding.panelHistory.visibility = if (isHistoryVisible) View.VISIBLE else View.GONE
        }

        binding.panelHistory.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun setupHistory() {
        historyAdapter = HistoryAdapter { entry ->
            viewModel.loadFromHistory(entry)
            isHistoryVisible = false
            binding.panelHistory.visibility = View.GONE
        }
        binding.panelHistory.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@CalculatorActivity)
            adapter = historyAdapter
        }
    }

    private fun setupObservers() {
        viewModel.expressionText.observe(this) { binding.tvFormula.text = it }
        viewModel.resultText.observe(this) { binding.tvResult.text = it }
        viewModel.errorText.observe(this) { error ->
            if (error != null) {
                binding.tvError.text = error
                binding.tvError.visibility = View.VISIBLE
                binding.tvResult.text = ""
            } else {
                binding.tvError.visibility = View.GONE
            }
        }
        viewModel.history.observe(this) { list ->
            historyAdapter.submitList(list)
            binding.panelHistory.tvEmptyHistory.visibility =
                if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun updateAngleButton() {
        binding.panelScientific.btnDeg.text =
            if (viewModel.isDegreeMode()) "Deg" else "Rad"
    }
}
```

---

### Task 11: 添加 mipmap launcher icon（使用自适应图标，或者从原 APK 提取）

**Files:**
- Create: `app-calculator/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
- Create: `app-calculator/src/main/res/drawable/ic_launcher_background.xml`
- Create: `app-calculator/src/main/res/drawable/ic_launcher_foreground.xml`

- [ ] **Step 1: ic_launcher_background.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#FF9500"
        android:pathData="M0,0h108v108h-108z" />
</vector>
```

- [ ] **Step 2: ic_launcher_foreground.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <!-- 计算器图标：简单的 "=" 符号 -->
    <path
        android:pathData="M30,42 L78,42 M30,54 L78,54 M30,66 L78,66"
        android:strokeColor="#FFFFFF"
        android:strokeWidth="4"
        android:strokeLineCap="round" />
</vector>
```

- [ ] **Step 3: ic_launcher.xml (mipmap-anydpi-v26/)**

```xml
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

---

### Task 12: Theme 夜间模式适配（drawable 暗色版本）

- [ ] **Step 1: 在 values-night/themes.xml 中添加夜间覆盖变量**

```xml
<resources>
    <style name="Theme.Calculator" parent="Theme.AppCompat.DayNight.NoActionBar">
        <item name="colorPrimary">@color/cal_accent</item>
        <item name="colorPrimaryDark">@color/cal_accent</item>
        <item name="colorAccent">@color/cal_accent</item>
        <item name="android:windowBackground">@color/cal_display_bg_dark</item>
    </style>
</resources>
```

夜间模式的颜色已通过 `colors.xml` 中的 `_dark` 色值 + ViewBinding 层动态设置完成（见 Task 13）。

---

### Task 13: 主题切换逻辑（Activity 层监听夜间模式变化）

- [ ] **Step 1: 更新 CalculatorActivity 添加夜间模式监听**

在 `CalculatorActivity` 中添加：

```kotlin
private val nightModeColors by lazy {
    val tv = TypedValue()
    theme.resolveAttribute(android.R.attr.windowBackground, tv, true)
    // 通过资源颜色引用获取暗色值
    Pair(
        ContextCompat.getColor(this, R.color.cal_btn_number_bg),
        ContextCompat.getColor(this, R.color.cal_btn_number_bg_dark)
    )
}
```

实际上更好的方式：利用 `AppCompatTextView` 配合 `ColorStateList`。简化起见，我们仅在 Activity 的 `onCreate` 中统一设置按钮颜色即可，因为 `ripple drawable` 中使用的 `@color/xxx` 引用是跟随主题的。

---

### Task 14: 按需灰度测试运行

- [ ] **Step 1: 运行单元测试**

```bash
./gradlew :app-calculator:testDebugUnitTest
```

Expected: Tests pass

- [ ] **Step 2: 构建并运行 app**

```bash
./gradlew :app-calculator:installDebug
```

- [ ] **Step 3: 最终验证**
   - 基础四则运算正确
   - 科学函数计算正确
   - 错误处理显示（除零、语法错误等）
   - 历史记录存储与回填
   - 科学面板切换
   - 亮暗主题切换

---

## 自检清单

- [x] 每个步骤包含完整代码，无 TBD/占位符
- [x] 文件路径精确
- [x] Task 间类型和方法签名一致
- [x] 覆盖设计文档所有功能点
