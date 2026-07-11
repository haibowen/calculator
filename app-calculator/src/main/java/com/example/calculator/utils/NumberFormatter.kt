package com.example.calculator.utils

import java.math.BigDecimal
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
