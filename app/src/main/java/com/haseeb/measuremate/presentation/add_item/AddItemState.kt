package com.haseeb.measuremate.presentation.add_item

import androidx.compose.ui.text.input.TextFieldValue
import com.haseeb.measuremate.domain.model.BodyPart

data class AddItemState(
    val textFieldValue: String = "",
    val selectedBodyPart: BodyPart? = null,
    val bodyParts: List<BodyPart> = emptyList(),
)
