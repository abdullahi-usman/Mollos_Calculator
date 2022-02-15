package com.dahham.molloscalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.textButtonColors
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Popup
import androidx.lifecycle.ViewModelProvider
import com.dahham.molloscalculator.database.Country
import com.dahham.molloscalculator.ui.theme.MollosCalculatorTheme

class MainActivity : ComponentActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (::mainActivityViewModel.isInitialized.not()) {
            mainActivityViewModel = ViewModelProvider.AndroidViewModelFactory(application)
                .create(MainActivityViewModel::class.java)
        }

        setContent {
            MollosCalculatorTheme {
                // A surface container using the 'background' color from the theme

                Scaffold(
                    topBar = TopBar(),
                    bottomBar = BottomBar(),
                    modifier = Modifier.fillMaxSize(),
                ) {

                    val scrollState = rememberScrollState()

                    Column(modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxHeight(0.3f),
                            contentAlignment = Alignment.BottomCenter) {

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = MaterialTheme.colors.onSecondary)) {
                                        append("Currency Calculator")
                                    }

                                    withStyle(SpanStyle(color = MaterialTheme.colors.onPrimary,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 1.em)) {
                                        append(".")
                                    }

                                },
                                fontSize = (4 * (resources.displayMetrics.xdpi / 160)).em,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.ExtraBold,
                                overflow = TextOverflow.Visible,
                            )
                        }


                        // Wrap in a verticalScroll to enable scrolling on very small screens
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            ConvertSection()
                        }
                    }
                }

            }
        }
    }


    @Composable
    fun ConvertSection() {

        val fromCountry by mainActivityViewModel.fromCountry.observeAsState()
        val toCountry by mainActivityViewModel.toCountry.observeAsState()

        var fromCountryValue by remember {
            mutableStateOf("0.00")
        }

        var toCountryValue by remember {
            mutableStateOf("0.00")
        }

        var showCountriesList by remember {
            mutableStateOf(false)
        }

        //TODO: We should deal with this in a clean way
        var currentBtnClickFrom by remember {
            mutableStateOf(false)
        }

        if (showCountriesList) {
            ShowCountryList {
                showCountriesList = false
                if (currentBtnClickFrom) {
                    mainActivityViewModel.fromCountry.postValue(it)
                } else {
                    mainActivityViewModel.toCountry.postValue(it)
                }
            }
        }

        Box(modifier = Modifier.padding(vertical = 4.dp)) {

            TextField(value = TextFieldValue(buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colors.primaryVariant)) {
                    append(fromCountryValue)
                }
            }),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.onSurface,
                        shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp),
                onValueChange = {
                    fromCountryValue = it.text
                },
                placeholder = { Text(text = buildString { }) },
                trailingIcon = {
                    Text(text = fromCountry?.code!!, color = MaterialTheme.colors.secondaryVariant)
                })
        }

        Box(modifier = Modifier.padding(vertical = 4.dp)) {

            TextField(value = TextFieldValue(buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colors.primaryVariant)) {
                    append(toCountryValue)
                }
            }),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.onSurface,
                        shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp),
                onValueChange = {
                    toCountryValue = it.text
                },
                placeholder = { Text(text = buildString { }) },
                trailingIcon = {
                    Text(text = toCountry?.code!!, color = MaterialTheme.colors.secondaryVariant)
                })
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            TextButton(onClick = {
                currentBtnClickFrom = true
                showCountriesList = true
            }, modifier = Modifier
                .border(0.7.dp,
                    MaterialTheme.colors.secondaryVariant,
                    RoundedCornerShape(8.dp))

            ) {
                Row(modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = fromCountry?.code!!, color = MaterialTheme.colors.primaryVariant)

                    Icon(imageVector = ImageVector.vectorResource(
                        id = R.drawable.ic_expand_more_icon),
                        contentDescription = "", tint = MaterialTheme.colors.primaryVariant)
                }
            }

            Icon(modifier = Modifier.fillMaxWidth(0.1f),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_reverse_arrow),
                contentDescription = "",
                tint = MaterialTheme.colors.primaryVariant)


            TextButton(onClick = {
                currentBtnClickFrom = false
                showCountriesList = true
            }, modifier = Modifier
                .padding(horizontal = 8.dp)
                .border(0.7.dp,
                    MaterialTheme.colors.secondaryVariant,
                    RoundedCornerShape(8.dp))

            ) {
                Row(modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = toCountry?.code!!, color = MaterialTheme.colors.primaryVariant)

                    Icon(imageVector = ImageVector.vectorResource(
                        id = R.drawable.ic_expand_more_icon),
                        contentDescription = "", tint = MaterialTheme.colors.primaryVariant)
                }
            }

        }

        Button(onClick = {
            mainActivityViewModel.convertCurrency(fromCountryValue.toFloat()) {
                toCountryValue = it.toString()
            }
        },
            colors = textButtonColors(backgroundColor = MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)) {
            Text(text = "Convert", modifier = Modifier.padding(vertical = 4.dp))
        }
    }

    fun BottomBar() = @Composable {

    }

    @Composable
    fun ShowCountryList(callback: (country: Country) -> Unit) {

        val countries by mainActivityViewModel.getCountries(LocalContext.current).observeAsState()

        Popup(alignment = Alignment.Center, onDismissRequest = {
            //showPopUp = false
        }) {
            if (countries.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Processing Information Please Wait",
                        textAlign = TextAlign.Center)
                }
            } else {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(MaterialTheme.colors.surface),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    itemsIndexed(countries!!,
                        { index: Int, item: Country -> item.code },
                        { index: Int, item: Country ->
                            Text(text = item.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        //showPopUp = false
                                        callback(item)
                                    }
                                    .padding(vertical = 16.dp),
                                color = MaterialTheme.colors.primaryVariant)
                        })
                }
            }
            // }
        }
    }


    fun TopBar() = @Composable {
        TopAppBar(contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.secondaryVariant),
            elevation = 0.dp,
            title = { Text(text = "") },
            navigationIcon = {
                IconButton(modifier = Modifier.padding(horizontal = 16.dp), onClick = {

                }) {

                    Icon(ImageVector.vectorResource(id = R.drawable.ic_menu_icon),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "")
                }

            },
            actions = {
                IconButton(onClick = { }) {
                    Text(text = "Sign up",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colors.onPrimary,
                        fontFamily = Font(R.font.montserrat, FontWeight.Bold).toFontFamily())
                }
            })


    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MollosCalculatorTheme {
        Greeting("Android")
    }
}