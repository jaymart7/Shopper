package util

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.crossfade

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context)
        .crossfade(true)
//        .logger(DebugLogger()) Uncomment when needed
        .build()

fun getRandomImageUrl(id: Int) = "https://picsum.photos/id/$id/300"