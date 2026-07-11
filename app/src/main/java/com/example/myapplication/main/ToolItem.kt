package com.example.myapplication.main

import androidx.annotation.DrawableRes

data class ToolItem(
    val id: Int,
    val name: String,
    @DrawableRes val iconResId: Int,
    val activityClass: Class<*>
)
