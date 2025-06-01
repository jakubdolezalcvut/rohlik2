package cz.rohlik.di

import androidx.lifecycle.SavedStateHandle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cz.rohlik.data.ArticleMapper
import cz.rohlik.domain.ArticleRepository
import cz.rohlik.data.Constants
import cz.rohlik.data.InstantDateDeserializer
import cz.rohlik.data.SpaceflightService
import cz.rohlik.ui.ArticleListViewModel
import cz.rohlik.ui.ArticleDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal val appModule = module {

    factoryOf(::ArticleRepository)
    factoryOf(::ArticleMapper)
    viewModel { (handle: SavedStateHandle) ->
        ArticleListViewModel(get(), handle)
    }
    viewModel { (handle: SavedStateHandle) ->
        ArticleDetailViewModel(
            articleRepository = get(),
            savedStateHandle = handle,
        )
    }
    factory<TimeZone> { TimeZone.currentSystemDefault() }
    factory<Dispatchers> { Dispatchers }

    single<OkHttpClient> {
        val interceptor = HttpLoggingInterceptor().apply {
            level = Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    factory<Gson> {
        GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantDateDeserializer)
            .create()
    }

    factory<SpaceflightService> {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()

        retrofit.create(SpaceflightService::class.java)
    }
}
