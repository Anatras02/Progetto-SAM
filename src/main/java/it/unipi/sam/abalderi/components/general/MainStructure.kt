package it.unipi.sam.abalderi.components.general

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.unipi.sam.abalderi.ui.theme.AppAndroidTheme

@Composable
fun MainStructure(
    topBarText: String,
    modifier: Modifier = Modifier,
    floatingActionButton : @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    AppAndroidTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    TopAppBar {
                        Text(text = topBarText)
                    }
                }
            ) {
                Scaffold(floatingActionButton = floatingActionButton) {
                    Surface(modifier = modifier, content = content)
                }
            }
        }
    }
}
