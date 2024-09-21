package com.haseeb.measuremate.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import com.haseeb.measuremate.domain.model.BodyPartValue
import com.haseeb.measuremate.ui.util.roundToDecimal
import java.time.LocalDate

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    bodyPartValues: List<BodyPartValue>,
    pathAndCircleWidth : Float = 5f,
    helperLinesColor : Color = MaterialTheme.colorScheme.surfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    
    val dataPointValues = bodyPartValues.asReversed().map {
        it.value
    }

    val textMeasurer = rememberTextMeasurer()
    val highestValue = dataPointValues.maxOrNull() ?: 0f
    val lowestValue = dataPointValues.minOrNull() ?: 0f
    val noOfParts = 3
    val difference = (highestValue - lowestValue) / noOfParts
    val valuesList = listOf(
        highestValue.roundToDecimal(1),
        (highestValue - difference).roundToDecimal(1),
        (lowestValue + difference).roundToDecimal(1),
        lowestValue.roundToDecimal(1)
    )

    Canvas(modifier = modifier){
        val graphWidth = size.width
        val graphHeight = size.height

        valuesList.forEachIndexed { index, fl ->
            val graph80Percent = graphHeight * 0.8f
            val xPosition = 0f
            val yPosition = (graph80Percent/noOfParts) * index
            val graph5PercentHeight = graphHeight * 0.05f
            val graph10PercentWidth = graphWidth * 0.1f
            drawText(
                textMeasurer = textMeasurer,
                text = "$fl",
                topLeft = Offset(x = xPosition, y = yPosition),
                style = textStyle,
            )

            drawLine(
                color = helperLinesColor,
                strokeWidth = pathAndCircleWidth,
                start = Offset(x = graph10PercentWidth, y = yPosition+graph5PercentHeight),
                end = Offset(x = graphWidth, y = yPosition+graph5PercentHeight),
            )
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LineGraphPreview() {
    val dummyBodyPartValues = listOf(
        BodyPartValue(value = 72.0f, date = LocalDate.of(2023, 5, 10)),
        BodyPartValue(value = 76.84865145f, date = LocalDate.of(2023, 5, 1)),
        BodyPartValue(value = 74.0f, date = LocalDate.of(2023, 4, 20)),
        BodyPartValue(value = 75.1f, date = LocalDate.of(2023, 4, 5)),
        BodyPartValue(value = 66.3f, date = LocalDate.of(2023, 3, 15)),
        BodyPartValue(value = 67.2f, date = LocalDate.of(2023, 3, 10)),
        BodyPartValue(value = 73.5f, date = LocalDate.of(2023, 3, 1)),
        BodyPartValue(value = 69.8f, date = LocalDate.of(2023, 2, 18)),
        BodyPartValue(value = 68.4f, date = LocalDate.of(2023, 2, 1)),
        BodyPartValue(value = 72.0f, date = LocalDate.of(2023, 1, 22)),
        BodyPartValue(value = 70.5f, date = LocalDate.of(2023, 1, 14))
    )
    LineGraph(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 2 / 1f),
        bodyPartValues = dummyBodyPartValues)
}