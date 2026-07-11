# ColorOS 计算器精简复刻 — 设计文档

## 目标

在现有 Android 项目中新建 `app-calculator` module，使用传统 XML + ViewBinding + Kotlin，复刻 ColorOS 计算器 v16.4.2 的核心计算功能（基础运算 + 科学计算），力求 UI 风格和交互体验 1:1 还原。

## 不包含功能

- 单位换算（长度/面积/体积/重量/温度/速度/压力/功率/进制）
- 汇率转换
- 房贷计算（商业贷/公积金/组合贷）
- 悬浮窗
- 锁屏卡片
- 插件系统
- 折叠屏适配

## 技术选型

| 层面 | 选择 |
|---|---|
| 语言 | Kotlin |
| UI | XML + ViewBinding |
| 架构 | MVVM (ViewModel + LiveData/Flow) |
| 表达式计算 | BigDecimal 自研引擎 |
| 本地存储 | Room (SQLite) — 计算历史 |
| 网络 | 不引入（精简版无需） |
| 主题 | AppCompat DayNight — 跟随系统亮暗 |

## 模块结构

```
app-calculator/
├── src/main/
│   ├── java/com/example/calculator/
│   │   ├── CalculatorActivity.kt        # 主 Activity
│   │   ├── CalculatorViewModel.kt        # MVVM ViewModel
│   │   ├── model/
│   │   │   └── HistoryEntry.kt           # Room 实体
│   │   ├── engine/
│   │   │   ├── ExpressionEngine.kt       # 表达式解析 + 求值
│   │   │   ├── Token.kt                  # 词法单元定义
│   │   │   └── MathConstants.kt          # 数学常量
│   │   ├── ui/
│   │   │   ├── CalculatorDisplay.kt      # 公式+结果双行显示 View
│   │   │   ├── CalculatorGrid.kt         # 按钮网格
│   │   │   ├── ScientificGrid.kt         # 科学计算面板
│   │   │   └── HistoryPanel.kt           # 历史记录面板
│   │   ├── adapter/
│   │   │   └── HistoryAdapter.kt         # 历史列表适配器
│   │   └── utils/
│   │       ├── NumberFormatter.kt        # 数字格式化
│   │       └── ThemeUtils.kt             # 主题工具
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_calculator.xml
│   │   │   ├── panel_display.xml
│   │   │   ├── panel_grid_basic.xml
│   │   │   ├── panel_grid_scientific.xml
│   │   │   └── panel_history.xml
│   │   ├── values/
│   │   │   ├── colors.xml
│   │   │   ├── strings.xml
│   │   │   ├── dimens.xml
│   │   │   └── themes.xml
│   │   ├── values-night/
│   │   │   └── themes.xml
│   │   └── drawable/
│   │       └── btn_calc_rounded.xml      # 按钮圆角背景
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## UI 布局设计

### 主界面（竖屏）

- **顶部状态区**: 历史下拉提示条（可选上滑展开）
- **公式显示行**: 灰色小字，显示完整输入表达式，左对齐，支持水平滚动
- **结果显示行**: 大号加粗字体，显示计算结果，右对齐
- **分隔线**: 浅灰分割线
- **按钮网格**: 4 列 x 6 行基础按钮布局
  - 颜色方案: 数字/小数点白色底、运算符橙色、功能键(C/%)浅灰
  - 圆角矩形按钮，按压有缩放反馈
- **底部工具栏**: 科学模式切换按钮 + 历史记录按钮

### 科学模式

基础网格上方插入 3 行科学函数按钮网格（4 列 x 3 行）：
- 三角函数: sin, cos, tan
- 反三角函数: asin, acos, atan
- 对数: log, ln, log₂
- 幂运算: x², x³, xⁿ, √, ∛
- 常量: π, e
- 阶乘: n!
- 角度模式切换: Deg / Rad

## 表达式引擎设计 (ExpressionEngine)

### 词法分析 (Tokenizer)

支持的 Token 类型：
- NUMBER (整数/小数)
- OPERATOR (+, -, ×, ÷)
- FUNCTION (sin, cos, tan, asin, acos, atan, log, ln, sqrt, cbrt, fact)
- PAREN_LEFT, PAREN_RIGHT
- CONSTANT (PI, E)
- PERCENT
- POWER (^)
- FACTORIAL (!)

### 语法分析 (Parser)

使用**递归下降解析器**，支持：
- 运算符优先级: `+ -` < `× ÷` < `^ !` < `functions`
- 括号嵌套
- 隐式乘法: `2π`, `3sin(30)`
- 后缀运算符: `!` (阶乘), `%` (百分比)
- 负号处理: `-5`, `3+-2`

### 求值器 (Evaluator)

- 使用 `BigDecimal` 保证精度
- 数学函数委托 `java.lang.Math`（三角函数通过弧度转换）
- 角度模式: Deg/Rad 影响三角函数输入

### 精度策略

- 中间计算: `MathContext.DECIMAL128` (34位)
- 最终结果: 去除尾部多余零，过长结果科学计数法

## 交互细节

### 按键反馈
- 点击: 缩放动画 (0.95x → 1.0x, 100ms)
- 长按退格: 连续删除
- 震动反馈（可选）

### 输入规则
- 禁止连续运算符 (如 `++`)
- 自动补全括号
- 数字千分位格式化显示
- 结果过长自动滚动

### 历史记录
- 上拉面板展示 Room 存储的历史
- 每条显示: 表达式 + 结果 + 时间
- 点击历史条目: 回填到当前计算器
- 清空历史

### 主题
- 跟随系统 DayNight
- 亮色: 白底深色字
- 暗色: 黑底浅色字
- 运算符按钮保持橙色系

## 数据流

```
用户点击按钮
    ↓
CalculatorActivity.onClick()
    ↓
CalculatorViewModel.onButtonPressed(token)
    ↓
ExpressionEngine.evaluate(expression) → result
    ↓
CalculatorViewModel.updateState(displayText, result)
    ↓
CalculatorDisplay 观察 LiveData 更新 UI
    ↓
ViewModel.saveToHistory(expression, result) → Room DB
```

## 错误处理

| 场景 | 显示 |
|---|---|
| 除以零 | "除数不能为零" |
| 无效表达式 | "表达式错误" |
| 数值溢出 | "结果过大" |
| 阶乘参数过大 | "参数超出范围" |
| 函数参数错误 | "输入无效" |

## 自检清单

- [x] 没有 TBD/占位符
- [x] 模块结构完整无矛盾
- [x] 范围聚焦于精简版核心功能
- [x] 每个需求描述明确无歧义
