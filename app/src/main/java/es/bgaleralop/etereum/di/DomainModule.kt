package es.bgaleralop.etereum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.bgaleralop.etereum.domain.images.services.AndroidImageFormatter
import es.bgaleralop.etereum.domain.images.services.ImageFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideImageFormatter(): ImageFormatter {
        return AndroidImageFormatter()
    }
}