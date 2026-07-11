package com.example.myapplication.main

import android.content.Intent
import com.example.myapplication.base.BaseActivity
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

class MainActivity : BaseActivity() {

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

    override fun getLayoutId() = R.layout.activity_main

    override fun initView() {
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
