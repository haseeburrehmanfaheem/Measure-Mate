package com.haseeb.measuremate.domain.model

import java.time.LocalDate

data class BodyPartValue(
    val value : Float,
    val date : LocalDate,
    val bodyPartId: String? = null,
    val bodyPartValueId: String? = null
)
