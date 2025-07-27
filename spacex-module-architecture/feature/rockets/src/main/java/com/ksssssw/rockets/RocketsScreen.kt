package com.ksssssw.rockets

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ksssssw.spacex.model.Rocket
import com.ksssssw.ui.preview.RocketsPreviewParameterProvider
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun RocketsScreen(
    modifier: Modifier = Modifier,
    viewModel: RocketsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RocketsScreen(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
internal fun RocketsScreen(
    uiState: RocketsUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = uiState.rockets,
                key = { it.id }
            ) { rocket ->
                RocketItem(
                    name = rocket.name,
                    description = rocket.description,
                    imageUrl = rocket.flickrImages.first()
                )
            }
        }
    }
}

@Composable
fun RocketItem(
    name: String,
    description: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = imageUrl,
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(imageUrl)
//                .setHeader("User-Agent", "Mozilla/5.0")
//                .build(),
            contentDescription = name,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
            error = painterResource(id = android.R.drawable.ic_menu_gallery),
            onLoading = {
                Log.d("AsyncImage", "Loading image: $imageUrl")
            },
            onSuccess = {
                Log.d("AsyncImage", "Successfully loaded image: $imageUrl")
            },
            onError = { error ->
                Log.e("AsyncImage", "Failed to load image: $imageUrl", error.result.throwable)
            }
        )

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RocketItemPreview() {
    RocketItem(
        name = "Falcon 1",
        description = "Falcon 9 is a two-stage rocket designed and manufactured by SpaceX for the reliable and safe transport of satellites and the Dragon spacecraft into orbit.",
        imageUrl = "https://farm5.staticflickr.com/4599/38583829295_581f34dd84_b.jpg"
    )
}

@Preview(showBackground = true)
@Composable
fun RocketsScreenPreview(
    @PreviewParameter(RocketsPreviewParameterProvider::class)
    rockets: List<Rocket>
) {
    RocketsScreen(
        uiState = RocketsUiState(
            rockets = rockets
        )
    )
}