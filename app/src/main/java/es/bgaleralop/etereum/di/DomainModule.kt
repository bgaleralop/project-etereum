package es.bgaleralop.etereum.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import es.bgaleralop.etereum.domain.images.repository.ImageRepository
import es.bgaleralop.etereum.domain.images.services.AndroidImageFormatter
import es.bgaleralop.etereum.domain.images.services.ImageFormatter
import es.bgaleralop.etereum.domain.images.usecases.OpenImageUseCase
import es.bgaleralop.etereum.domain.images.usecases.SaveImageUseCase
import es.bgaleralop.etereum.domain.images.usecases.TransformImageUseCase
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

@Module
@InstallIn(ViewModelComponent::class)
object DomainViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideOpenImageUseCase(repository: ImageRepository): OpenImageUseCase {
        return OpenImageUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSaveImageUseCase(repository: ImageRepository): SaveImageUseCase {
        return SaveImageUseCase(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideTransformImageUseCase(formatter: ImageFormatter): TransformImageUseCase {
        return TransformImageUseCase(formatter)
    }
}