package com.haseeb.measuremate.presentation.details

import com.haseeb.measuremate.domain.model.BodyPart
import com.haseeb.measuremate.domain.model.BodyPartValue
import com.haseeb.measuremate.domain.model.MeasuringUnit
import com.haseeb.measuremate.domain.model.TimeRange
import java.time.LocalDate

data class DetailsState(
    val bodyPart: BodyPart? = null,
    val textFieldValue: String = "",
    val recentlyDeletedBodyPartValue: BodyPartValue? = null,
    val date: LocalDate = LocalDate.now(),
    val timeRange: TimeRange = TimeRange.LAST7DAYS,
    val allBodyPartValues: List<BodyPartValue> = emptyList(),
    val graphBodyPartValues: List<BodyPartValue> = emptyList()
)
