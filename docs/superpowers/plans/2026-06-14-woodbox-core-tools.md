# WoodBox 核心工具实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在现有 Android 项目中落地 15 个核心工具（日常/测量/图像/查询四类）

**Architecture:** 传统 XML 布局 + 多 Activity 架构，所有 Activity 继承 `BaseActivity`，工具首页用 RecyclerView Grid 展示，数据源内嵌 assets

**Tech Stack:** Kotlin, Android SDK 35, minSdk 24, ZXing(二维码), CameraX(扫码), Markwon(Markdown渲染), Gson(JSON)

---

### Task 1: 项目脚手架 — 依赖注入 + BaseActivity + MainActivity + 数据提取

**Files:**
- Modify: `app/build.gradle.kts`
- Create: `app/src/main/java/com/example/myapplication/base/BaseActivity.kt`
- Create: `app/src/main/java/com/example/myapplication/main/MainActivity.kt`
- Create: `app/src/main/res/layout/activity_main.xml`
- Create: `app/src/main/java/com/example/myapplication/main/ToolItem.kt`
- Modify: `app/src/main/AndroidManifest.xml`

- [ ] **Step 1: 添加依赖**

`app/build.gradle.kts` dependencies 块末尾追加：

```kotlin
implementation("com.google.zxing:core:3.5.3")
implementation("androidx.camera:camera-camera2:1.3.1")
implementation("androidx.camera:camera-lifecycle:1.3.1")
implementation("androidx.camera:camera-view:1.3.1")
implementation("io.noties.markwon:core:4.6.2")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.google.code.gson:gson:2.10.1")
```

- [ ] **Step 2: 创建数据模型 ToolItem**

```kotlin
package com.example.myapplication.main

import androidx.annotation.DrawableRes

data class ToolItem(
    val id: Int,
    val name: String,
    @DrawableRes val iconResId: Int,
    val activityClass: Class<*>
)
```

- [ ] **Step 3: 创建 BaseActivity**

```kotlin
package com.example.myapplication.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
    }

    protected open fun initView() {}
}
```

- [ ] **Step 4: 创建工具首页布局 `res/layout/activity_main.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="@color/purple_500" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/grid_tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:clipToPadding="false"
        android:padding="12dp" />

</LinearLayout>
```

- [ ] **Step 5: 创建 MainActivity**

```kotlin
package com.example.myapplication.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.tools.daily.TomatoClockActivity
import com.example.myapplication.tools.daily.CountdownActivity
import com.example.myapplication.tools.daily.RandomNumberActivity
import com.example.myapplication.tools.daily.UnitConverterActivity
import com.example.myapplication.tools.daily.TranslateActivity
import com.example.myapplication.tools.measure.RulerActivity
import com.example.myapplication.tools.measure.CompassActivity
import com.example.myapplication.tools.measure.SpiritLevelActivity
import com.example.myapplication.tools.measure.NoiseActivity
import com.example.myapplication.tools.image.QRCodeActivity
import com.example.myapplication.tools.image.QRCodeScanActivity
import com.example.myapplication.tools.image.ColorPickerActivity
import com.example.myapplication.tools.reference.ChineseColorsActivity
import com.example.myapplication.tools.reference.LinuxCommandActivity
import com.example.myapplication.tools.reference.TodayInHistoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ToolAdapter

    private val tools = listOf(
        ToolItem(1, "番茄时钟", R.drawable.ic_tomato, TomatoClockActivity::class.java),
        ToolItem(2, "倒计时", R.drawable.ic_countdown, CountdownActivity::class.java),
        ToolItem(3, "随机数生成", R.drawable.ic_random, RandomNumberActivity::class.java),
        ToolItem(4, "单位换算", R.drawable.ic_converter, UnitConverterActivity::class.java),
        ToolItem(5, "翻译", R.drawable.ic_translate, TranslateActivity::class.java),
        ToolItem(6, "尺子", R.drawable.ic_ruler, RulerActivity::class.java),
        ToolItem(7, "指南针", R.drawable.ic_compass, CompassActivity::class.java),
        ToolItem(8, "水平仪", R.drawable.ic_level, SpiritLevelActivity::class.java),
        ToolItem(9, "分贝计", R.drawable.ic_noise, NoiseActivity::class.java),
        ToolItem(10, "二维码生成", R.drawable.ic_qrcode, QRCodeActivity::class.java),
        ToolItem(11, "二维码扫描", R.drawable.ic_scan, QRCodeScanActivity::class.java),
        ToolItem(12, "取色器", R.drawable.ic_color, ColorPickerActivity::class.java),
        ToolItem(13, "中国传统色", R.drawable.ic_palette, ChineseColorsActivity::class.java),
        ToolItem(14, "Linux命令", R.drawable.ic_terminal, LinuxCommandActivity::class.java),
        ToolItem(15, "历史上的今天", R.drawable.ic_history, TodayInHistoryActivity::class.java),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "WoodBox Tools"

        recyclerView = findViewById(R.id.grid_tools)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter = ToolAdapter(tools) { item ->
            startActivity(Intent(this, item.activityClass))
        }
        recyclerView.adapter = adapter
    }
}
```

- [ ] **Step 6: 创建 ToolAdapter**

```kotlin
package com.example.myapplication.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class ToolAdapter(
    private val items: List<ToolItem>,
    private val onClick: (ToolItem) -> Unit
) : RecyclerView.Adapter<ToolAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.tool_icon)
        val name: TextView = view.findViewById(R.id.tool_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tool, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconResId)
        holder.name.text = item.name
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
```

- [ ] **Step 7: 创建 item 布局 `res/layout/item_tool.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="12dp">

    <ImageView
        android:id="@+id/tool_icon"
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:contentDescription="@null" />

    <TextView
        android:id="@+id/tool_name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="6dp"
        android:gravity="center"
        android:maxLines="1"
        android:textSize="12sp" />

</LinearLayout>
```

- [ ] **Step 8: 更新 AndroidManifest.xml**

将所有 Activity 注册到 manifest，在 `<application>` 内追加：

```xml
<activity android:name=".main.MainActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<!-- 日常工具 -->
<activity android:name=".tools.daily.TomatoClockActivity" />
<activity android:name=".tools.daily.CountdownActivity" />
<activity android:name=".tools.daily.RandomNumberActivity" />
<activity android:name=".tools.daily.UnitConverterActivity" />
<activity android:name=".tools.daily.TranslateActivity" />

<!-- 测量工具 -->
<activity android:name=".tools.measure.RulerActivity" />
<activity android:name=".tools.measure.CompassActivity" />
<activity android:name=".tools.measure.SpiritLevelActivity" />
<activity android:name=".tools.measure.NoiseActivity" />

<!-- 图像工具 -->
<activity android:name=".tools.image.QRCodeActivity" />
<activity android:name=".tools.image.QRCodeScanActivity" />
<activity android:name=".tools.image.ColorPickerActivity" />

<!-- 查询工具 -->
<activity android:name=".tools.reference.ChineseColorsActivity" />
<activity android:name=".tools.reference.LinuxCommandActivity" />
<activity android:name=".tools.reference.TodayInHistoryActivity" />
```

- [ ] **Step 9: 提取 assets**

从原 APK 提取数据到项目 `app/src/main/assets/`：

```bash
# 解压 chinesecolors.json
unzip -p "/Users/mac/Desktop/7.17.20.apk" "assets/chinesecolors.json" > "/Users/mac/AndroidStudioProjects/untitled folder/MyApplication2/app/src/main/assets/chinesecolors.json"

# 解压 linux-command 目录
unzip -o "/Users/mac/Desktop/7.17.20.apk" "assets/linux-command/*" -d "/tmp/woodbox_extract"
cp -r "/tmp/woodbox_extract/assets/linux-command" "/Users/mac/AndroidStudioProjects/untitled folder/MyApplication2/app/src/main/assets/linux-command"

# 解压 cncity.txt
unzip -p "/Users/mac/Desktop/7.17.20.apk" "assets/cncity.txt" > "/Users/mac/AndroidStudioProjects/untitled folder/MyApplication2/app/src/main/assets/cncity.txt"
```

- [ ] **Step 10: 生成占位图标**

创建简单的 vector drawable 作工具图标（每个工具一个），放在 `res/drawable/` 下，命名为 `ic_tomato.xml`, `ic_countdown.xml` 等。

每个 icon 用 Material Design 风格，不同形状/颜色区分。

---

### Task 2: 日常工具 — 番茄时钟 + 倒计时 + 随机数生成

**Files:**
- Create: `app/src/main/java/com/example/myapplication/tools/daily/TomatoClockActivity.kt`
- Create: `app/src/main/res/layout/activity_tomato_clock.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/daily/CountdownActivity.kt`
- Create: `app/src/main/res/layout/activity_countdown.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/daily/RandomNumberActivity.kt`
- Create: `app/src/main/res/layout/activity_random_number.xml`

- [ ] **Step 1: 番茄时钟布局 `activity_tomato_clock.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:id="@+id/tv_timer"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="25:00"
        android:textSize="72sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/tv_status"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="工作中"
        android:textSize="18sp" />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        android:orientation="horizontal">

        <Button
            android:id="@+id/btn_start"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="开始" />

        <Button
            android:id="@+id/btn_pause"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="12dp"
            android:text="暂停" />

        <Button
            android:id="@+id/btn_reset"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="12dp"
            android:text="重置" />

    </LinearLayout>

</LinearLayout>
```

- [ ] **Step 2: 番茄时钟 Activity**

```kotlin
package com.example.myapplication.tools.daily

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class TomatoClockActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var timer: CountDownTimer? = null
    private var isWorkTime = true
    private var timeLeftMs = WORK_DURATION_MS
    private var isRunning = false

    companion object {
        private const val WORK_DURATION_MS = 25 * 60 * 1000L
        private const val BREAK_DURATION_MS = 5 * 60 * 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomato_clock)

        tvTimer = findViewById(R.id.tv_timer)
        tvStatus = findViewById(R.id.tv_status)
        btnStart = findViewById(R.id.btn_start)
        btnPause = findViewById(R.id.btn_pause)
        btnReset = findViewById(R.id.btn_reset)

        updateDisplay()

        btnStart.setOnClickListener { startTimer() }
        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { resetTimer() }
    }

    private fun startTimer() {
        if (isRunning) return
        isRunning = true
        timer = object : CountDownTimer(timeLeftMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMs = millisUntilFinished
                updateDisplay()
            }

            override fun onFinish() {
                isWorkTime = !isWorkTime
                timeLeftMs = if (isWorkTime) WORK_DURATION_MS else BREAK_DURATION_MS
                isRunning = false
                tvStatus.text = if (isWorkTime) "工作中" else "休息中"
                updateDisplay()
            }
        }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
    }

    private fun resetTimer() {
        timer?.cancel()
        isRunning = false
        isWorkTime = true
        timeLeftMs = WORK_DURATION_MS
        tvStatus.text = "工作中"
        updateDisplay()
    }

    private fun updateDisplay() {
        val totalSec = timeLeftMs / 1000
        val min = totalSec / 60
        val sec = totalSec % 60
        tvTimer.text = String.format("%02d:%02d", min, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
```

- [ ] **Step 3: 倒计时布局 `activity_countdown.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="24dp">

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <EditText
            android:id="@+id/et_hours"
            android:layout_width="60dp"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:hint="时"
            android:inputType="number" />

        <EditText
            android:id="@+id/et_minutes"
            android:layout_width="60dp"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:hint="分"
            android:inputType="number" />

        <EditText
            android:id="@+id/et_seconds"
            android:layout_width="60dp"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:hint="秒"
            android:inputType="number" />

    </LinearLayout>

    <TextView
        android:id="@+id/tv_countdown"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        android:text="00:00:00"
        android:textSize="64sp"
        android:textStyle="bold" />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:orientation="horizontal">

        <Button
            android:id="@+id/btn_start"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="开始" />

        <Button
            android:id="@+id/btn_pause"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="12dp"
            android:text="暂停" />

        <Button
            android:id="@+id/btn_reset"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="12dp"
            android:text="重置" />

    </LinearLayout>

</LinearLayout>
```

- [ ] **Step 4: 倒计时 Activity (逻辑与番茄时钟类似，但可自定义时长)**

```kotlin
package com.example.myapplication.tools.daily

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class CountdownActivity : AppCompatActivity() {

    private lateinit var etHours: EditText
    private lateinit var etMinutes: EditText
    private lateinit var etSeconds: EditText
    private lateinit var tvCountdown: TextView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    private var timer: CountDownTimer? = null
    private var timeLeftMs = 0L
    private var totalMs = 0L
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        etHours = findViewById(R.id.et_hours)
        etMinutes = findViewById(R.id.et_minutes)
        etSeconds = findViewById(R.id.et_seconds)
        tvCountdown = findViewById(R.id.tv_countdown)
        btnStart = findViewById(R.id.btn_start)
        btnPause = findViewById(R.id.btn_pause)
        btnReset = findViewById(R.id.btn_reset)

        btnStart.setOnClickListener { startTimer() }
        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { resetTimer() }
    }

    private fun startTimer() {
        if (isRunning) return
        if (timeLeftMs <= 0) {
            val h = etHours.text.toString().toIntOrNull() ?: 0
            val m = etMinutes.text.toString().toIntOrNull() ?: 0
            val s = etSeconds.text.toString().toIntOrNull() ?: 0
            totalMs = (h * 3600L + m * 60L + s) * 1000L
            timeLeftMs = totalMs
        }
        if (timeLeftMs <= 0) return
        isRunning = true
        timer = object : CountDownTimer(timeLeftMs, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMs = millisUntilFinished
                updateDisplay()
            }

            override fun onFinish() {
                isRunning = false
                tvCountdown.text = "00:00:00"
            }
        }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isRunning = false
    }

    private fun resetTimer() {
        timer?.cancel()
        isRunning = false
        timeLeftMs = 0
        tvCountdown.text = "00:00:00"
    }

    private fun updateDisplay() {
        val totalSec = timeLeftMs / 1000
        val h = totalSec / 3600
        val m = (totalSec % 3600) / 60
        val s = totalSec % 60
        tvCountdown.text = String.format("%02d:%02d:%02d", h, m, s)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
```

- [ ] **Step 5: 随机数生成布局 `activity_random_number.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="最小值"
        android:textSize="16sp" />

    <EditText
        android:id="@+id/et_min"
        android:layout_width="120dp"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:inputType="number"
        android:text="1" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="最大值"
        android:textSize="16sp" />

    <EditText
        android:id="@+id/et_max"
        android:layout_width="120dp"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:inputType="number"
        android:text="100" />

    <CheckBox
        android:id="@+id/cb_unique"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="不重复" />

    <Button
        android:id="@+id/btn_generate"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="生成" />

    <TextView
        android:id="@+id/tv_result"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:textSize="24sp" />

</LinearLayout>
```

- [ ] **Step 6: 随机数生成 Activity**

```kotlin
package com.example.myapplication.tools.daily

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class RandomNumberActivity : AppCompatActivity() {

    private lateinit var etMin: EditText
    private lateinit var etMax: EditText
    private lateinit var cbUnique: CheckBox
    private lateinit var btnGenerate: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_number)

        etMin = findViewById(R.id.et_min)
        etMax = findViewById(R.id.et_max)
        cbUnique = findViewById(R.id.cb_unique)
        btnGenerate = findViewById(R.id.btn_generate)
        tvResult = findViewById(R.id.tv_result)

        btnGenerate.setOnClickListener { generate() }
    }

    private fun generate() {
        val min = etMin.text.toString().toIntOrNull() ?: return
        val max = etMax.text.toString().toIntOrNull() ?: return
        if (min >= max) {
            tvResult.text = "最大值需大于最小值"
            return
        }
        val result = if (cbUnique.isChecked) {
            (min..max).toList().shuffled().take(1).joinToString()
        } else {
            (min..max).random().toString()
        }
        tvResult.text = result
    }
}
```

---

### Task 3: 日常工具 — 单位换算 + 翻译

**Files:**
- Create: `app/src/main/java/com/example/myapplication/tools/daily/UnitConverterActivity.kt`
- Create: `app/src/main/res/layout/activity_unit_converter.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/daily/TranslateActivity.kt`
- Create: `app/src/main/res/layout/activity_translate.xml`

- [ ] **Step 1: 单位换算布局 `activity_unit_converter.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <Spinner
        android:id="@+id/spinner_category"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:entries="@array/converter_categories" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:orientation="horizontal">

        <EditText
            android:id="@+id/et_input"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:hint="输入数值"
            android:inputType="numberDecimal" />

        <Spinner
            android:id="@+id/spinner_from"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1" />

    </LinearLayout>

    <TextView
        android:id="@+id/tv_result"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:gravity="center"
        android:textSize="32sp"
        android:textStyle="bold" />

    <Spinner
        android:id="@+id/spinner_to"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp" />

</LinearLayout>
```

- [ ] **Step 2: 单位换算数据**

`res/values/arrays.xml` 中定义分类和单位：

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="converter_categories">
        <item>长度</item>
        <item>重量</item>
        <item>温度</item>
        <item>面积</item>
        <item>体积</item>
        <item>速度</item>
    </string-array>
</resources>
```

- [ ] **Step 3: 单位换算 Activity**

```kotlin
package com.example.myapplication.tools.daily

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.text.DecimalFormat

class UnitConverterActivity : AppCompatActivity() {

    private lateinit var spinnerCategory: Spinner
    private lateinit var etInput: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var tvResult: TextView

    private val lengthUnits = arrayOf("毫米", "厘米", "分米", "米", "千米", "英寸", "英尺")
    private val weightUnits = arrayOf("毫克", "克", "千克", "吨", "磅", "盎司")
    private val tempUnits = arrayOf("摄氏度", "华氏度", "开尔文")
    private val areaUnits = arrayOf("平方毫米", "平方厘米", "平方米", "公顷", "平方千米")
    private val volumeUnits = arrayOf("毫升", "升", "立方米", "加仑")
    private val speedUnits = arrayOf("米/秒", "千米/时", "英里/时", "节")

    private val allUnits = arrayOf(lengthUnits, weightUnits, tempUnits, areaUnits, volumeUnits, speedUnits)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_converter)

        spinnerCategory = findViewById(R.id.spinner_category)
        etInput = findViewById(R.id.et_input)
        spinnerFrom = findViewById(R.id.spinner_from)
        spinnerTo = findViewById(R.id.spinner_to)
        tvResult = findViewById(R.id.tv_result)

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, pos: Int, id: Long) {
                val adapter = ArrayAdapter(this@UnitConverterActivity, android.R.layout.simple_spinner_item, allUnits[pos])
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerFrom.adapter = adapter
                spinnerTo.adapter = ArrayAdapter(this@UnitConverterActivity, android.R.layout.simple_spinner_item, allUnits[pos])
                convert()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        etInput.setOnEditorActionListener { _, _, _ -> convert(); false }

        val fromListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: android.view.View?, p2: Int, id: Long) { convert() }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
        spinnerFrom.onItemSelectedListener = fromListener
        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: android.view.View?, p2: Int, id: Long) { convert() }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    private fun convert() {
        val input = etInput.text.toString().toDoubleOrNull() ?: return
        val cat = spinnerCategory.selectedItemPosition
        val fromIdx = spinnerFrom.selectedItemPosition
        val toIdx = spinnerTo.selectedItemPosition
        val result = when (cat) {
            0 -> convertLength(input, fromIdx, toIdx)
            1 -> convertWeight(input, fromIdx, toIdx)
            2 -> convertTemp(input, fromIdx, toIdx)
            3 -> convertArea(input, fromIdx, toIdx)
            4 -> convertVolume(input, fromIdx, toIdx)
            5 -> convertSpeed(input, fromIdx, toIdx)
            else -> input
        }
        tvResult.text = DecimalFormat("#.####").format(result)
    }

    private fun convertLength(v: Double, from: Int, to: Int): Double {
        val toMm = doubleArrayOf(1.0, 10.0, 100.0, 1000.0, 1e6, 25.4, 304.8)
        return v * toMm[from] / toMm[to]
    }

    private fun convertWeight(v: Double, from: Int, to: Int): Double {
        val toMg = doubleArrayOf(1.0, 1000.0, 1e6, 1e9, 453592.37, 28349.5)
        return v * toMg[from] / toMg[to]
    }

    private fun convertTemp(v: Double, from: Int, to: Int): Double {
        val inC = when (from) { 0 -> v; 1 -> (v - 32) * 5 / 9; else -> v - 273.15 }
        return when (to) { 0 -> inC; 1 -> inC * 9 / 5 + 32; else -> inC + 273.15 }
    }

    private fun convertArea(v: Double, from: Int, to: Int): Double {
        val toSqMm = doubleArrayOf(1.0, 100.0, 1e6, 1e10, 1e12)
        return v * toSqMm[from] / toSqMm[to]
    }

    private fun convertVolume(v: Double, from: Int, to: Int): Double {
        val toMl = doubleArrayOf(1.0, 1000.0, 1e6, 3785.41)
        return v * toMl[from] / toMl[to]
    }

    private fun convertSpeed(v: Double, from: Int, to: Int): Double {
        val toMps = doubleArrayOf(1.0, 0.277778, 0.44704, 0.514444)
        return v * toMps[from] / toMps[to]
    }
}
```

- [ ] **Step 4: 翻译布局 `activity_translate.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <Spinner
        android:id="@+id/spinner_from_lang"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <EditText
        android:id="@+id/et_source"
        android:layout_width="match_parent"
        android:layout_height="120dp"
        android:gravity="top"
        android:hint="输入要翻译的文本"
        android:inputType="textMultiLine" />

    <Button
        android:id="@+id/btn_translate"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="翻译" />

    <Spinner
        android:id="@+id/spinner_to_lang"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <TextView
        android:id="@+id/tv_result"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:background="@android:color/darker_gray"
        android:minHeight="80dp"
        android:padding="12dp"
        android:textSize="16sp" />

</LinearLayout>
```

- [ ] **Step 5: 翻译 Activity (使用 mymemory API)**

```kotlin
package com.example.myapplication.tools.daily

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class TranslateActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var etSource: EditText
    private lateinit var btnTranslate: Button
    private lateinit var tvResult: TextView

    private val languages = arrayOf(
        "自动检测", "中文", "英语", "日语", "韩语", "法语", "德语", "俄语", "西班牙语"
    )
    private val langCodes = arrayOf("", "zh", "en", "ja", "ko", "fr", "de", "ru", "es")
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        spinnerFrom = findViewById(R.id.spinner_from_lang)
        spinnerTo = findViewById(R.id.spinner_to_lang)
        etSource = findViewById(R.id.et_source)
        btnTranslate = findViewById(R.id.btn_translate)
        tvResult = findViewById(R.id.tv_result)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter
        spinnerTo.setSelection(1)

        btnTranslate.setOnClickListener { doTranslate() }
    }

    private fun doTranslate() {
        val text = etSource.text.toString()
        if (text.isBlank()) return
        val from = langCodes[spinnerFrom.selectedItemPosition]
        val to = langCodes[spinnerTo.selectedItemPosition]
        tvResult.text = "翻译中..."

        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val url = "https://api.mymemory.translated.net/get?q=${java.net.URLEncoder.encode(text, "UTF-8")}&langpair=${from}|$to"
                    val json = URL(url).readText()
                    JSONObject(json).getJSONObject("responseData").getString("translatedText")
                }
                tvResult.text = result
            } catch (e: Exception) {
                tvResult.text = "翻译失败: ${e.message}"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
```

---

### Task 4: 测量工具 — 尺子 + 指南针 + 水平仪 + 分贝计

**Files:**
- Create: `app/src/main/java/com/example/myapplication/tools/measure/RulerActivity.kt`
- Create: `app/src/main/res/layout/activity_ruler.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/measure/CompassActivity.kt`
- Create: `app/src/main/res/layout/activity_compass.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/measure/SpiritLevelActivity.kt`
- Create: `app/src/main/res/layout/activity_spirit_level.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/measure/NoiseActivity.kt`
- Create: `app/src/main/res/layout/activity_noise.xml`

- [ ] **Step 1: 尺子布局 `activity_ruler.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="16dp">

    <HorizontalScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <LinearLayout
            android:id="@+id/ruler_container"
            android:layout_width="wrap_content"
            android:layout_height="80dp"
            android:orientation="horizontal" />

    </HorizontalScrollView>

    <TextView
        android:id="@+id/tv_measurement"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="0.0 cm"
        android:textSize="24sp"
        android:textStyle="bold" />

    <ToggleButton
        android:id="@+id/toggle_unit"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:textOff="厘米"
        android:textOn="英寸" />

    <Button
        android:id="@+id/btn_touch_ruler"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:gravity="center"
        android:text="触摸尺子\n滑动测量"
        android:textSize="18sp" />

</LinearLayout>
```

- [ ] **Step 2: 尺子 Activity (屏幕像素密度校准)**

```kotlin
package com.example.myapplication.tools.measure

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class RulerActivity : AppCompatActivity() {

    private lateinit var rulerContainer: LinearLayout
    private lateinit var tvMeasurement: TextView
    private lateinit var toggleUnit: ToggleButton
    private lateinit var btnTouchRuler: Button

    private var isCm = true
    private var dpi = 160f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ruler)

        dpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
        rulerContainer = findViewById(R.id.ruler_container)
        tvMeasurement = findViewById(R.id.tv_measurement)
        toggleUnit = findViewById(R.id.toggle_unit)
        btnTouchRuler = findViewById(R.id.btn_touch_ruler)

        drawRuler()

        toggleUnit.setOnCheckedChangeListener { _, isChecked ->
            isCm = !isChecked
            drawRuler()
        }

        btnTouchRuler.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
                val cmPerPixel = 2.54f / dpi
                val cm = event.x * cmPerPixel
                tvMeasurement.text = if (isCm) String.format("%.1f cm", cm) else String.format("%.1f in", cm / 2.54f)
            }
            true
        }
    }

    private fun drawRuler() {
        rulerContainer.removeAllViews()
        val paint = Paint().apply { color = Color.BLACK; strokeWidth = 2f }
        val cmWidth = (dpi / 2.54f).toInt()

        for (i in 0 until 30) {
            val view = object : android.view.View(this) {
                override fun onDraw(canvas: Canvas) {
                    super.onDraw(canvas)
                    canvas.drawLine(0f, 0f, 0f, height.toFloat(), paint)
                    canvas.drawText("$i", 4f, height - 4f, Paint().apply { textSize = 24f })
                    for (j in 1..9) {
                        val x = cmWidth * j / 10f
                        val h = if (j == 5) height * 0.6f else height * 0.3f
                        canvas.drawLine(x, 0f, x, h, paint)
                    }
                }
            }
            view.layoutParams = LinearLayout.LayoutParams(cmWidth, 120)
            rulerContainer.addView(view)
        }
    }
}
```

- [ ] **Step 3: 指南针 Activity (传感器)**

```kotlin
package com.example.myapplication.tools.measure

import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class CompassActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var compassView: CompassView
    private lateinit var tvDegree: TextView
    private lateinit var tvDirection: TextView

    private var currentAzimuth = 0f

    inner class CompassView(context: Context) : android.view.View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 40f; textAlign = Paint.Align.CENTER; color = Color.BLACK }

        override fun onDraw(canvas: Canvas) {
            val cx = width / 2f
            val cy = height / 2f
            val radius = minOf(cx, cy) * 0.7f

            paint.style = Paint.Style.STROKE; paint.strokeWidth = 4f
            canvas.drawCircle(cx, cy, radius, paint)

            for (i in 0 until 360 step 30) {
                val rad = Math.toRadians((i - 90).toDouble())
                val isMajor = i % 90 == 0
                val inner = if (isMajor) radius * 0.85f else radius * 0.92f
                val outer = radius
                canvas.drawLine(
                    cx + inner * Math.cos(rad).toFloat(), cy + inner * Math.sin(rad).toFloat(),
                    cx + outer * Math.cos(rad).toFloat(), cy + outer * Math.sin(rad).toFloat(),
                    paint
                )
            }

            // Draw needle
            val angle = Math.toRadians((-currentAzimuth).toDouble())
            paint.style = Paint.Style.FILL
            paint.color = Color.RED
            canvas.drawLine(cx, cy, cx + radius * 0.6f * Math.cos(angle).toFloat(), cy + radius * 0.6f * Math.sin(angle).toFloat(), paint)
            paint.color = Color.GRAY
            canvas.drawLine(cx, cy, cx - radius * 0.6f * Math.cos(angle).toFloat(), cy - radius * 0.6f * Math.sin(angle).toFloat(), paint)

            canvas.drawText("N", cx, cy - radius + 50f, textPaint)
            canvas.drawText("S", cx, cy + radius - 20f, textPaint)
            canvas.drawText("E", cx + radius - 30f, cy + 15f, textPaint)
            canvas.drawText("W", cx - radius + 30f, cy + 15f, textPaint)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        compassView = CompassView(this)
        tvDegree = TextView(this).apply { textSize = 20f; gravity = android.view.Gravity.CENTER }
        tvDirection = TextView(this).apply { textSize = 24f; gravity = android.view.Gravity.CENTER; textStyle = android.graphics.Typeface.BOLD }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(compassView, android.widget.LinearLayout.LayoutParams(-1, 0, 1f))
            addView(tvDegree)
            addView(tvDirection)
        }
        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        currentAzimuth = event.values[0]
        compassView.invalidate()
        tvDegree.text = String.format("%.0f°", currentAzimuth)
        tvDirection.text = when {
            currentAzimuth < 22.5f || currentAzimuth >= 337.5f -> "北"
            currentAzimuth < 67.5f -> "东北"
            currentAzimuth < 112.5f -> "东"
            currentAzimuth < 157.5f -> "东南"
            currentAzimuth < 202.5f -> "南"
            currentAzimuth < 247.5f -> "西南"
            currentAzimuth < 292.5f -> "西"
            else -> "西北"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
```

- [ ] **Step 4: 水平仪 Activity (加速度传感器)**

```kotlin
package com.example.myapplication.tools.measure

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class SpiritLevelActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var levelView: LevelView
    private lateinit var tvX: TextView
    private lateinit var tvY: TextView

    private var pitch = 0f
    private var roll = 0f

    inner class LevelView(context: Context) : android.view.View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun onDraw(canvas: Canvas) {
            val cx = width / 2f
            val cy = height / 2f
            val radius = minOf(cx, cy) * 0.4f

            paint.style = Paint.Style.STROKE; paint.strokeWidth = 4f; paint.color = Color.DKGRAY
            canvas.drawCircle(cx, cy, radius, paint)

            paint.style = Paint.Style.FILL; paint.color = Color.argb(80, 0, 200, 0)
            val maxAngle = 15f
            val bx = cx + (roll / maxAngle) * radius
            val by = cy + (pitch / maxAngle) * radius
            val bubbleR = radius * 0.15f
            canvas.drawCircle(bx, by, bubbleR, paint)

            paint.style = Paint.Style.STROKE; paint.strokeWidth = 2f; paint.color = Color.GRAY
            canvas.drawLine(cx - radius, cy, cx + radius, cy, paint)
            canvas.drawLine(cx, cy - radius, cx, cy + radius, paint)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        levelView = LevelView(this)
        tvX = TextView(this).apply { gravity = android.view.Gravity.CENTER }
        tvY = TextView(this).apply { gravity = android.view.Gravity.CENTER }

        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(levelView, android.widget.LinearLayout.LayoutParams(-1, 0, 1f))
            addView(tvX)
            addView(tvY)
        }
        setContentView(layout)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        pitch = event.values[1] * 90f / 9.81f
        roll = event.values[0] * 90f / 9.81f
        levelView.invalidate()
        tvX.text = String.format("左右倾斜: %.1f°", roll)
        tvY.text = String.format("前后倾斜: %.1f°", pitch)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
```

- [ ] **Step 5: 分贝计 Activity (使用 MediaRecorder)**

```kotlin
package com.example.myapplication.tools.measure

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class NoiseActivity : AppCompatActivity() {

    private lateinit var tvDecibel: TextView
    private lateinit var tvLevel: TextView
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noise)

        tvDecibel = findViewById(R.id.tv_decibel)
        tvLevel = findViewById(R.id.tv_level)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 100)
        } else {
            startMonitoring()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startMonitoring()
        }
    }

    private fun startMonitoring() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile("/dev/null")
            prepare()
            start()
            isRecording = true
        }
        Thread {
            while (isRecording) {
                try {
                    val amplitude = mediaRecorder?.maxAmplitude ?: 1
                    val db = 20 * Math.log10(amplitude.toDouble()).toFloat()
                    runOnUiThread {
                        tvDecibel.text = String.format("%.1f dB", db)
                        tvLevel.text = when {
                            db < 30 -> "非常安静"
                            db < 50 -> "安静"
                            db < 70 -> "正常交谈"
                            db < 85 -> "吵闹"
                            db < 100 -> "非常吵闹"
                            else -> "伤害性噪音"
                        }
                    }
                    Thread.sleep(500)
                } catch (e: Exception) { break }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRecording = false
        mediaRecorder?.apply { stop(); release() }
        mediaRecorder = null
    }
}
```

分贝计布局 `activity_noise.xml`：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:id="@+id/tv_decibel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="0.0 dB"
        android:textSize="64sp"
        android:textStyle="bold" />

    <TextView
        android:id="@+id/tv_level"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="等待检测..."
        android:textSize="20sp" />

</LinearLayout>
```

---

### Task 5: 图像工具 — 二维码生成 + 二维码扫描 + 取色器

**Files:**
- Create: `app/src/main/java/com/example/myapplication/tools/image/QRCodeActivity.kt`
- Create: `app/src/main/res/layout/activity_qrcode.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/image/QRCodeScanActivity.kt`
- Create: `app/src/main/res/layout/activity_qrcode_scan.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/image/ColorPickerActivity.kt`
- Create: `app/src/main/res/layout/activity_color_picker.xml`

- [ ] **Step 1: 二维码生成布局 `activity_qrcode.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/et_input"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="输入文本或URL" />

    <Button
        android:id="@+id/btn_generate"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="生成二维码" />

    <ImageView
        android:id="@+id/iv_qrcode"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:layout_marginTop="24dp"
        android:contentDescription="二维码" />

    <Button
        android:id="@+id/btn_save"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="保存到相册"
        android:visibility="gone" />

</LinearLayout>
```

- [ ] **Step 2: 二维码生成 Activity**

```kotlin
package com.example.myapplication.tools.image

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.FileOutputStream

class QRCodeActivity : AppCompatActivity() {

    private lateinit var etInput: EditText
    private lateinit var btnGenerate: Button
    private lateinit var ivQrcode: ImageView
    private lateinit var btnSave: Button
    private var currentBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        etInput = findViewById(R.id.et_input)
        btnGenerate = findViewById(R.id.btn_generate)
        ivQrcode = findViewById(R.id.iv_qrcode)
        btnSave = findViewById(R.id.btn_save)

        btnGenerate.setOnClickListener { generate() }
        btnSave.setOnClickListener { saveToGallery() }
    }

    private fun generate() {
        val text = etInput.text.toString()
        if (text.isBlank()) return

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        ivQrcode.setImageBitmap(bitmap)
        currentBitmap = bitmap
        btnSave.visibility = android.view.View.VISIBLE
    }

    private fun saveToGallery() {
        val bitmap = currentBitmap ?: return
        val filename = "QR_${System.currentTimeMillis()}.png"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                contentResolver.openOutputStream(it)?.use { os -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, os) }
            }
        }
        Toast.makeText(this, "已保存到相册", Toast.LENGTH_SHORT).show()
    }
}
```

- [ ] **Step 3: 二维码扫描布局 `activity_qrcode_scan.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.camera.view.PreviewView
        android:id="@+id/preview_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <TextView
        android:id="@+id/tv_result"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:background="#99000000"
        android:gravity="center"
        android:padding="16dp"
        android:text="将二维码放入框内"
        android:textColor="@android:color/white"
        android:textSize="16sp" />

</FrameLayout>
```

- [ ] **Step 4: 二维码扫描 Activity (CameraX + ZXing)**

```kotlin
package com.example.myapplication.tools.image

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer
import java.util.concurrent.Executors

class QRCodeScanActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_scan)

        previewView = findViewById(R.id.preview_view)
        tvResult = findViewById(R.id.tv_result)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 200)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        }
    }

    private fun startCamera() {
        val provider = ProcessCameraProvider.getInstance(this)
        provider.addListener({
            val cameraProvider = provider.get()
            val preview = Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider }

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            analyzer.setAnalyzer(Executors.newSingleThreadExecutor()) { image ->
                val buffer: ByteBuffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val source = com.google.zxing.PlanarYUVLuminanceSource(bytes, image.width, image.height, 0, 0, image.width, image.height)
                val binary = BinaryBitmap(HybridBinarizer(source))
                try {
                    val result = MultiFormatReader().decode(binary)
                    runOnUiThread {
                        tvResult.text = "扫描结果: ${result.text}"
                        Toast.makeText(this@QRCodeScanActivity, result.text, Toast.LENGTH_LONG).show()
                    }
                } catch (_: Exception) {}
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, analyzer)
        }, ContextCompat.getMainExecutor(this))
    }
}
```

- [ ] **Step 5: 取色器 Activity (色环 + 中国传统色)**

```kotlin
package com.example.myapplication.tools.image

import android.graphics.*
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import org.json.JSONArray

class ColorPickerActivity : AppCompatActivity() {

    private lateinit var colorPreview: View
    private lateinit var tvHex: TextView
    private lateinit var tvRGB: TextView
    private lateinit var tvCMYK: TextView
    private lateinit var colorWheel: ColorWheelView
    private lateinit var etSearch: EditText
    private lateinit var lvColors: ListView

    private var chineseColors = mutableListOf<ChineseColor>()
    private val filteredColors = mutableListOf<ChineseColor>()

    data class ChineseColor(val name: String, val hex: String, val rgb: String, val cmyk: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        colorPreview = findViewById(R.id.color_preview)
        tvHex = findViewById(R.id.tv_hex)
        tvRGB = findViewById(R.id.tv_rgb)
        tvCMYK = findViewById(R.id.tv_cmyk)
        colorWheel = findViewById(R.id.color_wheel)
        etSearch = findViewById(R.id.et_search)
        lvColors = findViewById(R.id.lv_colors)

        colorWheel.onColorChanged = { color ->
            colorPreview.setBackgroundColor(color)
            tvHex.text = String.format("#%06X", 0xFFFFFF and color)
            tvRGB.text = String.format("RGB(%d,%d,%d)", Color.red(color), Color.green(color), Color.blue(color))
            tvCMYK.text = "CMYK: -"
        }

        loadChineseColors()
        lvColors.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredColors.map { "${it.name} (${it.hex})" })

        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filter(s.toString()) }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun loadChineseColors() {
        try {
            val json = assets.open("chinesecolors.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val hex = obj.getString("hex")
                val rgbArr = obj.getJSONArray("RGB")
                val cmykArr = obj.getJSONArray("CMYK")
                chineseColors.add(ChineseColor(
                    obj.getString("name"), hex,
                    "RGB(${rgbArr[0]},${rgbArr[1]},${rgbArr[2]})",
                    "CMYK(${cmykArr[0]},${cmykArr[1]},${cmykArr[2]},${cmykArr[3]})"
                ))
            }
            filteredColors.addAll(chineseColors)
        } catch (e: Exception) {
            Toast.makeText(this, "加载颜色数据失败", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filter(query: String) {
        filteredColors.clear()
        filteredColors.addAll(if (query.isBlank()) chineseColors else chineseColors.filter {
            it.name.contains(query) || it.hex.contains(query)
        })
        lvColors.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredColors.map { "${it.name} (${it.hex})" })
    }

    inner class ColorWheelView(context: android.content.Context) : android.view.View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        var onColorChanged: ((Int) -> Unit)? = null
        private val colors = intArrayOf(
            0xFFFF0000.toInt(), 0xFFFFFF00.toInt(), 0xFF00FF00.toInt(),
            0xFF00FFFF.toInt(), 0xFF0000FF.toInt(), 0xFFFF00FF.toInt(), 0xFFFF0000.toInt()
        )

        override fun onDraw(canvas: Canvas) {
            val cx = width / 2f; val cy = height / 2f; val r = minOf(cx, cy) * 0.8f
            val shader = SweepGradient(cx, cy, colors, null)
            paint.shader = shader
            canvas.drawCircle(cx, cy, r, paint)
            paint.shader = null; paint.style = Paint.Style.STROKE; paint.strokeWidth = 4f
            canvas.drawCircle(cx, cy, r, paint)
        }

        override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
            if (event.action == android.view.MotionEvent.ACTION_DOWN || event.action == android.view.MotionEvent.ACTION_MOVE) {
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bmp)
                onDraw(canvas)
                val x = event.x.toInt().coerceIn(0, width - 1)
                val y = event.y.toInt().coerceIn(0, height - 1)
                val pixel = bmp.getPixel(x, y)
                onColorChanged?.invoke(pixel)
                bmp.recycle()
            }
            return true
        }
    }
}
```

取色器布局 `activity_color_picker.xml`：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="8dp">

    <com.example.myapplication.tools.image.ColorPickerActivity$ColorWheelView
        android:id="@+id/color_wheel"
        android:layout_width="match_parent"
        android:layout_height="200dp" />

    <View
        android:id="@+id/color_preview"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:layout_marginTop="8dp" />

    <TextView
        android:id="@+id/tv_hex"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="4dp"
        android:text="HEX: #000000"
        android:textSize="16sp" />

    <TextView
        android:id="@+id/tv_rgb"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="RGB: (0,0,0)"
        android:textSize="16sp" />

    <TextView
        android:id="@+id/tv_cmyk"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="CMYK: -"
        android:textSize="16sp" />

    <EditText
        android:id="@+id/et_search"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:hint="搜索中国传统色" />

    <ListView
        android:id="@+id/lv_colors"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

</LinearLayout>
```

---

### Task 6: 查询工具 — 中国传统色 + Linux 命令 + 历史上的今天

**Files:**
- Create: `app/src/main/java/com/example/myapplication/tools/reference/ChineseColorsActivity.kt`
- Create: `app/src/main/res/layout/activity_chinese_colors.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/reference/LinuxCommandActivity.kt`
- Create: `app/src/main/res/layout/activity_linux_command.xml`
- Create: `app/src/main/java/com/example/myapplication/tools/reference/TodayInHistoryActivity.kt`
- Create: `app/src/main/res/layout/activity_today_in_history.xml`

- [ ] **Step 1: 中国传统色布局 `activity_chinese_colors.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="8dp">

    <EditText
        android:id="@+id/et_search"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="搜索颜色名或色值" />

    <GridView
        android:id="@+id/gv_colors"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="8dp"
        android:numColumns="4"
        android:verticalSpacing="8dp"
        android:horizontalSpacing="8dp"
        android:stretchMode="columnWidth" />

</LinearLayout>
```

- [ ] **Step 2: 中国传统色 Activity**

```kotlin
package com.example.myapplication.tools.reference

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import org.json.JSONArray

class ChineseColorsActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var gvColors: GridView
    private var allColors = mutableListOf<ColorItem>()
    private var displayColors = mutableListOf<ColorItem>()

    data class ColorItem(val name: String, val hex: String, val pinyin: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chinese_colors)

        etSearch = findViewById(R.id.et_search)
        gvColors = findViewById(R.id.gv_colors)

        loadColors()
        gvColors.adapter = ColorAdapter(displayColors)
        gvColors.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val c = displayColors[position]
            Toast.makeText(this, "${c.name} (${c.hex})", Toast.LENGTH_SHORT).show()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                displayColors.clear()
                displayColors.addAll(if (s.isNullOrBlank()) allColors else allColors.filter {
                    it.name.contains(s) || it.hex.contains(s) || it.pinyin.contains(s)
                })
                gvColors.adapter = ColorAdapter(displayColors)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadColors() {
        try {
            val json = assets.open("chinesecolors.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                allColors.add(ColorItem(
                    obj.getString("name"), obj.getString("hex"), obj.getString("pinyin")
                ))
            }
            displayColors.addAll(allColors)
        } catch (e: Exception) {
            Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class ColorAdapter(private val items: List<ColorItem>) : BaseAdapter() {
        override fun getCount() = items.size
        override fun getItem(pos: Int) = items[pos]
        override fun getItemId(pos: Int) = pos.toLong()

        override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(this@ChineseColorsActivity)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            val item = items[pos]
            view.setBackgroundColor(Color.parseColor(item.hex))
            val tv = view.findViewById<TextView>(android.R.id.text1)
            tv.text = item.name
            tv.textSize = 10f
            tv.gravity = android.view.Gravity.CENTER
            return view
        }
    }
}
```

- [ ] **Step 3: Linux 命令布局 `activity_linux_command.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="8dp">

    <EditText
        android:id="@+id/et_search"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="搜索命令" />

    <ListView
        android:id="@+id/lv_commands"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="8dp" />

</LinearLayout>
```

- [ ] **Step 4: Linux 命令 Activity (使用 Markwon 渲染)**

```kotlin
package com.example.myapplication.tools.reference

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import io.noties.markwon.Markwon
import java.io.File

class LinuxCommandActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var lvCommands: ListView
    private var commands = mutableListOf<CommandItem>()
    private var filteredCommands = mutableListOf<CommandItem>()
    private lateinit var markwon: Markwon

    data class CommandItem(val name: String, val filePath: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linux_command)

        markwon = Markwon.create(this)

        etSearch = findViewById(R.id.et_search)
        lvCommands = findViewById(R.id.lv_commands)

        loadCommands()

        lvCommands.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredCommands.map { it.name })
        lvCommands.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            showCommandDetail(filteredCommands[position])
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filter(s.toString()) }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun loadCommands() {
        try {
            val dir = File(filesDir, "linux-command")
            if (!dir.exists()) {
                // Copy from assets on first launch
                copyAssetsDir("linux-command")
            }
            dir.listFiles()?.filter { it.extension == "md" }?.forEach { file ->
                commands.add(CommandItem(file.nameWithoutExtension, file.absolutePath))
            }
            commands.sortBy { it.name }
            filteredCommands.addAll(commands)
        } catch (e: Exception) {
            Toast.makeText(this, "加载命令失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyAssetsDir(path: String) {
        val assetsDir = assets.open(path) ?: return
        // For simplicity, we'll read from assets directly in detail view
    }

    private fun filter(query: String) {
        filteredCommands.clear()
        filteredCommands.addAll(if (query.isBlank()) commands else commands.filter { it.name.contains(query, true) })
        lvCommands.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredCommands.map { it.name })
    }

    private fun showCommandDetail(cmd: CommandItem) {
        val content = try {
            assets.open("linux-command/${cmd.name}.md").bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            assets.open("linux-command/${cmd.name}.md.txt").bufferedReader().use { it.readText() }
        }

        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle(cmd.name)
            .setPositiveButton("关闭", null)
            .create()

        val scrollView = ScrollView(this)
        val tv = TextView(this).apply { setPadding(16, 16, 16, 16) }
        markwon.setMarkdown(tv, content)
        scrollView.addView(tv)
        dialog.setView(scrollView)
        dialog.show()
    }
}
```

- [ ] **Step 5: 历史上的今天布局 `activity_today_in_history.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="8dp">

    <DatePicker
        android:id="@+id/date_picker"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:datePickerMode="spinner" />

    <ListView
        android:id="@+id/lv_events"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_marginTop="8dp" />

</LinearLayout>
```

- [ ] **Step 6: 历史上的今天 (内嵌数据)**

```kotlin
package com.example.myapplication.tools.reference

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import org.json.JSONArray

class TodayInHistoryActivity : AppCompatActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var lvEvents: ListView
    private var historyData = mutableMapOf<String, List<HistoryEvent>>()

    data class HistoryEvent(val year: String, val title: String, val desc: String = "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_in_history)

        datePicker = findViewById(R.id.date_picker)
        lvEvents = findViewById(R.id.lv_events)

        loadTodayData()

        datePicker.init(datePicker.year, datePicker.month, datePicker.dayOfMonth) { _, _, _, _ ->
            loadEvents()
        }
    }

    private fun loadTodayData() {
        // 内嵌部分历史事件数据（简化版，仅包含每日几条代表性事件）
        // 实际应用中可使用完整数据文件
        historyData["1-1"] = listOf(
            HistoryEvent("前45年", "罗马共和国开始使用儒略历"),
            HistoryEvent("404年", "罗马角斗士最后一场比赛"),
            HistoryEvent("1981年", "中国颁布《中华人民共和国婚姻法》"),
            HistoryEvent("2006年", "中国废止农业税"),
        )
        historyData["6-14"] = listOf(
            HistoryEvent("1777年", "美国采用星条旗作为国旗"),
            HistoryEvent("1905年", "爱因斯坦提出相对论"),
            HistoryEvent("1945年", "中国共产党第七次全国代表大会在延安开幕"),
        )
        loadEvents()
    }

    private fun loadEvents() {
        val key = "${datePicker.month + 1}-${datePicker.dayOfMonth}"
        val events = historyData[key] ?: listOf(HistoryEvent("暂无数据", "这一天暂无历史事件"))
        lvEvents.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, events.map {
            "$it.year - $it.title"
        }.toTypedArray()) { _, view, value ->
            val tv1 = view.findViewById<TextView>(android.R.id.text1)
            val tv2 = view.findViewById<TextView>(android.R.id.text2)
            tv1.text = value
        }
    }
}
```

---

### Task 7: 最终集成 — 构建与验证

**Files:**
- Modify: `app/build.gradle.kts`
- Run: build

- [ ] **Step 1: 检查 build.gradle.kts 添加 Theme.AppCompat 支持**

确保 App 使用 AppCompat 主题（BaseActivity 继承 AppCompatActivity）：

在 `res/values/themes.xml` 中：

```xml
<resources>
    <style name="Theme.MyApplication" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">#6750A4</item>
        <item name="colorPrimaryVariant">#4F378B</item>
        <item name="colorOnPrimary">#FFFFFF</item>
        <item name="colorSecondary">#625B71</item>
    </style>
</resources>
```

- [ ] **Step 2: 构建项目**

```bash
./gradlew assembleDebug
```

- [ ] **Step 3: 修复构建错误**

如有编译错误，逐个修复每一处错误直到构建通过。

---

## 执行说明

每个 Task 都是独立的，可以按任意顺序执行。推荐顺序：
1. Task 1（脚手架）→ 2-6 任意顺序平行执行 → 7（集成验证）

Task 2-6 中的每个 Activity 都是自包含的，无需等待其他 Task 完成。
