package com.example.myapplication.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Dimension

@Composable
fun PictureScreen(
    modifier: Modifier = Modifier
){
    val viewModel: PicturesViewModel = viewModel(factory = PicturesViewModel.Factory)
    val uiState = viewModel.picturesUiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Pictures Retrieved: ${uiState.picturesList.size}")

        uiState.currentPicture?.let { picture ->
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(picture.download_url)
                        .size(200,200) // Set the target size to load the image at.
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            )
        }
        Row {
            ActionButton({ viewModel.rollPicture() },"Roll")
            ActionButton({ viewModel.applyBlur() },"Blur")
            ActionButton({ viewModel.applyGrayScale() },"Gray")
        }
    }
}

@Composable
fun ActionButton(actionFunction:() -> Unit, buttonMessage: String){
    Button(onClick = { actionFunction() }) {
        Text(text = buttonMessage)
    }
}




