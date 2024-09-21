package com.haseeb.measuremate.domain.model

enum class TimeRange(
    val label : String,
) {
    LAST7DAYS("Last 7 Days"),
    LAST30DAYS("Last 30 Days"),
    AllTime("All Time")
}