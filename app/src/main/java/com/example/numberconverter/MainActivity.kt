package com.example.numberconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.numberconverter.ui.theme.NumberConverterTheme

enum class NumberBase {
    BIN, OCT, HEX
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NumberConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NumberConverterView()
                }
            }
        }
    }
}

@Composable
fun NumberConverterView(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    var selectedBase by remember { mutableStateOf(NumberBase.BIN) }
    var convertedText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxHeight(1f)) {
        HeaderThemePanel()
        ConverterChoicePanel(selectedBase) { base ->
            selectedBase = base
            convertedText = ""
        }
        TextField(
            value = inputText,
            onValueChange = { newValue: String ->
                inputText = newValue
                convertedText = ""
            },
            label = { Text(text = "Wprowadź liczbę do konwersji") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.95f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { convertedText = convertNumber(inputText, selectedBase) },
            enabled = inputText.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Text("Konwertuj")
        }

        if (convertedText != "" && convertedText != null && convertedText != "Wprowadziłeś złą liczbę.") {
            if (convertedText.isNotBlank()) {
                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = MaterialTheme.typography.headlineLarge.fontSize
                        )
                    ) {
                        append(convertedText)
                    }
                    append(" ")
                    withStyle(
                        style = SpanStyle(
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        )
                    ) {
                        append("(${selectedBase.name})")
                    }
                }

                Text(
                    text = annotatedString,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                /*Text(text = "$convertedText ($selectedBase)", modifier.padding(10.dp).fillMaxWidth(1f),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    textAlign = TextAlign.Center
                )*/
            } else if (convertedText == "Wprowadziłeś złą liczbę.") {
                Text(
                    text = "$convertedText", modifier.padding(10.dp).fillMaxWidth(1f),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

fun convertNumber(inputText: String, selectedBase: NumberBase): String {
    val number = inputText.toIntOrNull() ?: return "Wprowadziłeś złą liczbę."
    if (number < 0) {
        return "Wprowadziłeś złą liczbę."
    }
    return when (selectedBase) {
        NumberBase.BIN -> {
            Integer.toBinaryString(number)
        }
        NumberBase.OCT -> {
            Integer.toOctalString(number)
        }

        NumberBase.HEX -> {
            Integer.toHexString(number)
        }
    }
}

@Composable
fun HeaderThemePanel(modifier: Modifier = Modifier) {
    var isHelpRowVisible by remember { mutableStateOf(false) }
    Column{
        Row(modifier = Modifier
            .fillMaxHeight(0.2f)
            .fillMaxWidth(0.95f)
            .padding(start = 3.dp, top = 5.dp, end = 3.dp)
            .background(color = MaterialTheme.colorScheme.primary)
            /*horizontalArrangement = Arrangement.SpaceBetween,*/){
            Text(
                text = "Konwerter systemów liczbowych",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth(0.80f))
            IconButton(onClick = {
                isHelpRowVisible = !isHelpRowVisible
            },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor =  MaterialTheme.colorScheme.onPrimaryContainer,
                   disabledContainerColor = MaterialTheme.colorScheme.surfaceTint,
                   disabledContentColor = MaterialTheme.colorScheme.surfaceTint
                ),
                modifier = Modifier.fillMaxWidth(1f)) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Help button",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        if (isHelpRowVisible){
            Row(modifier = Modifier.background(
                color = MaterialTheme.colorScheme.primaryContainer)
            ){
                Text(
                    text="Konwersja między systemami liczbowymi jest możliwa" +
                            " wyłącznie dla liczb naturalnych od 0 do ${Int.MAX_VALUE}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                )

            }
        }
    }
}

@Composable
fun ConverterChoicePanel(selectedBase: NumberBase, onSelectedChange: (NumberBase) -> Unit) {
    Column{
        RadioGroup(selectedBase, onSelectedChange)
    }
}
@Composable
fun RadioGroup(selectedBase: NumberBase, onSelectedChange: (NumberBase) -> Unit) {
    val options = listOf(NumberBase.BIN, NumberBase.OCT, NumberBase.HEX)
    var selectedIndex by remember { mutableIntStateOf(options.indexOf(selectedBase)) }

    Column {
        options.forEachIndexed { index, base ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = index == selectedIndex,
                    onClick = {
                        selectedIndex = index
                        onSelectedChange(base)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = base.name)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NumberConverterTheme {
        NumberConverterView()
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview(){
    NumberConverterTheme {
        HeaderThemePanel()
    }

}