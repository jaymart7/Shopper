package ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.NetworkImage
import model.Product
import util.ViewState
import util.getRandomImageUrl

@Composable
internal fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()

    HomeContent(
        onEvent = { component.handleEvent(it) },
        model = model,
        modifier = modifier
    )
}

@Composable
private fun HomeContent(
    onEvent: (HomeEvent) -> Unit,
    model: HomeComponent.Model,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyGridState()

    model.scrollTo?.let {
        LaunchedEffect(it) {
            lazyListState.animateScrollToItem(it)
            onEvent(HomeEvent.ClearScroll)
        }
    }

    Column(modifier = modifier) {
        ProductContent(
            lazyListState = lazyListState,
            onItemClick = { onEvent(HomeEvent.ProductClick(it)) },
            onRefresh = { onEvent(HomeEvent.RefreshProduct) },
            productsState = model.productsState,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ProductContent(
    lazyListState: LazyGridState,
    onItemClick: (Product) -> Unit,
    onRefresh: () -> Unit,
    productsState: ViewState<List<Product>>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        when (productsState) {
            is ViewState.Error -> ErrorContent(
                onRefresh = onRefresh,
                errorMessage = productsState.error.message.orEmpty()
            )

            is ViewState.Loading -> Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }

            is ViewState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(
                        productsState.data,
                        key = { it.id },
                        itemContent = {
                            ProductItem(
                                modifier = Modifier.animateItem(),
                                onClick = { onItemClick(it) },
                                product = it
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    onRefresh: () -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            errorMessage,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRefresh,
            content = {
                Text("Refresh")
            }
        )
    }
}

@Composable
private fun ProductItem(
    onClick: () -> Unit,
    product: Product,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.clickable { onClick() },
            contentAlignment = Alignment.BottomEnd
        ) {
            NetworkImage(
                url = getRandomImageUrl(product.id),
                modifier = Modifier.fillMaxSize()
            )
            Text(
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(topStart = 8.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                text = product.title,
            )
        }
    }
}