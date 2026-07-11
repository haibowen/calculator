# WoodBox 核心工具落地设计

## 概述
将「一个木函」(WoodBox) 的 15 个核心功能落地到当前 Android 项目中。使用传统 XML 布局 + 多 Activity 架构。

## 架构

### 整体结构
```
app/src/main/java/com/example/myapplication/
├── base/
│   └── BaseActivity.kt          # 所有 Activity 的基类
├── main/
│   └── MainActivity.kt          # 工具首页 Grid 列表
├── tools/                       # 按分类放置各工具
│   ├── daily/                   # 日常工具
│   │   ├── TomatoClockActivity.kt
│   │   ├── CountdownActivity.kt
│   │   ├── TranslateActivity.kt
│   │   ├── UnitConverterActivity.kt
│   │   └── RandomNumberActivity.kt
│   ├── measure/                 # 测量工具
│   │   ├── RulerActivity.kt
│   │   ├── CompassActivity.kt
│   │   ├── SpiritLevelActivity.kt
│   │   └── NoiseActivity.kt
│   ├── image/                   # 图像工具
│   │   ├── QRCodeActivity.kt
│   │   ├── QRCodeScanActivity.kt
│   │   └── ColorPickerActivity.kt
│   └── reference/               # 查询工具
│       ├── ChineseColorsActivity.kt
│       ├── LinuxCommandActivity.kt
│       └── TodayInHistoryActivity.kt
└── data/
    ├── ChineseColorsData.kt     # 中国传统色 JSON 数据
    ├── LinuxCommandLoader.kt    # Linux 命令数据加载
    └── TodayInHistoryData.kt    # 历史上的今天数据
```

### 布局文件
```
app/src/main/res/layout/
├── activity_main.xml              # 工具首页 Grid
├── activity_tomato_clock.xml
├── activity_countdown.xml
├── activity_translate.xml
├── activity_unit_converter.xml
├── activity_random_number.xml
├── activity_ruler.xml
├── activity_compass.xml
├── activity_spirit_level.xml
├── activity_noise.xml
├── activity_qrcode.xml
├── activity_qrcode_scan.xml
├── activity_color_picker.xml
├── activity_chinese_colors.xml
├── activity_linux_command.xml
└── activity_today_in_history.xml
```

## 功能详细设计

### 1. 工具首页 (MainActivity)
- `RecyclerView` + `GridLayoutManager(spanCount=4)`
- 每个 item 显示图标 + 名称
- 点击打开对应 Activity
- 顶部 Toolbar 显示 App 名称

### 2. 二维码生成器 (QRCodeActivity)
- 输入框输入文本/URL
- 点击生成按钮，调用 ZXing 生成二维码图片
- 显示生成的二维码
- 长按保存到相册

### 3. 二维码扫描 (QRCodeScanActivity)
- 使用 CameraX + ZXing 扫码
- 扫描成功后显示结果
- 支持复制结果、打开链接

### 4. 番茄时钟 (TomatoClockActivity)
- 25分钟工作 + 5分钟休息循环
- 圆形进度条显示剩余时间
- 开始/暂停/重置按钮
- 完成时播放通知声音

### 5. 倒计时 (CountdownActivity)
- 选择小时/分钟/秒
- 开始后实时显示剩余时间
- 暂停/继续/重置
- 结束时振动+通知

### 6. 翻译 (TranslateActivity)
- 源语言输入框 + 目标语言输出框
- 语言选择下拉框（中/英/日/韩/法/德等）
- 翻译按钮调用翻译 API（或离线词库）
- 复制结果、语音朗读

### 7. 单位换算 (UnitConverterActivity)
- 分类选择：长度、重量、温度、面积、体积、速度
- 输入数值自动换算
- 支持双向换算

### 8. 尺子 (RulerActivity)
- 屏幕标尺，根据屏幕 dpi 校准
- 支持厘米/英寸切换
- 量角器模式

### 9. 指南针 (CompassActivity)
- 使用 `SensorManager.getDefaultSensor(TYPE_GEOMAGNETIC_ROTATION_VECTOR)`
- 罗盘表盘 + 方向文字显示
- 需要 `CALIBRATION` 提示

### 10. 水平仪 (SpiritLevelActivity)
- 使用加速度传感器
- 十字气泡水平仪效果
- 两个轴向的角度数值显示

### 11. 取色器 (ColorPickerActivity)
- 从图片取色
- HSV 色环选择
- HEX/RGB/CMYK 值显示与复制
- 附带中国传统色对照

### 12. 随机数生成器 (RandomNumberActivity)
- 设置最小值/最大值
- 生成数量（1-10个）
- 不重复选项
- 结果列表展示

### 13. 分贝计 (NoiseActivity)
- 使用 `MediaRecorder` 或 `AudioRecord`
- 实时分贝数值显示
- 条形图动态展示
- 参考值对照（安静/正常/吵闹）

### 14. 中国传统色 (ChineseColorsActivity)
- 从 `chinesecolors.json` 加载 500+ 颜色
- 按颜色分类浏览（红/橙/黄/绿/蓝/紫/棕/白/灰/黑）
- 搜索（拼音/中文名）
- 点击显示大色块 + 详细值

### 15. Linux 命令查询 (LinuxCommandActivity)
- 内置 580 个 markdown 命令文档
- 搜索框搜索命令名/功能
- 按字母分类浏览
- 点击显示命令详情（Markdown 渲染）

### 16. 历史上的今天 (TodayInHistoryActivity)
- 显示当前日期发生的历史事件
- 日期选择器切换日期
- 事件列表展示（年份 + 事件描述）
- 点击查看详情

## 依赖库
```kotlin
// ZXing - 二维码
implementation("com.google.zxing:core:3.5.3")

// CameraX - 扫码相机
implementation("androidx.camera:camera-camera2:1.3.1")
implementation("androidx.camera:camera-lifecycle:1.3.1")
implementation("androidx.camera:camera-view:1.3.1")

// Markdown 渲染
implementation("io.noties.markwon:core:4.6.2")

// 网络请求（翻译）
implementation("com.squareup.okhttp3:okhttp:4.12.0")

// JSON 解析
implementation("com.google.code.gson:gson:2.10.1")
```

## 数据来源
- `chinesecolors.json` — 从原 APK assets 提取
- `assets/linux-command/*.md` — 580 个 markdown 文件，从原 APK 提取
- 历史上的今天 — 内嵌数据或免费 API
- 翻译 — 免费翻译 API（如 mtranslate 或离线词库）

## 非功能性需求
- 所有工具离线可用（翻译除外）
- minSdk = 24
- 中文界面
- 每个工具 Activity 独立，互不依赖
