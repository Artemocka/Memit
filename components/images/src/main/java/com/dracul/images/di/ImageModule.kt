package com.dracul.images.di

import com.dracul.images.data.repository.DeleteImageByParentIdRepoImpl
import com.dracul.images.data.repository.DeleteImageRepoImpl
import com.dracul.images.data.repository.GetAllImagesByParentIdRepoImpl
import com.dracul.images.data.repository.GetImagesByIdRepoImpl
import com.dracul.images.data.repository.InsertImageRepoImpl
import com.dracul.images.data.repository.UpdateImageRepoImpl
import com.dracul.images.domain.repository.DeleteImageByParentIdRepo
import com.dracul.images.domain.repository.DeleteImageRepo
import com.dracul.images.domain.repository.GetAllImagesByParentIdRepo
import com.dracul.images.domain.repository.GetImageByIdRepo
import com.dracul.images.domain.repository.InsertImageRepo
import com.dracul.images.domain.repository.UpdateImageRepo
import com.dracul.images.domain.usecase.DeleteImageByIdUseCase
import com.dracul.images.domain.usecase.DeleteImageByParentIdImpl
import com.dracul.images.domain.usecase.DeleteImageImpl
import com.dracul.images.domain.usecase.DeleteImageUseCase
import com.dracul.images.domain.usecase.GetAllImagesByParentIdImpl
import com.dracul.images.domain.usecase.GetAllImagesByParentIdUseCase
import com.dracul.images.domain.usecase.GetImageByIdImpl
import com.dracul.images.domain.usecase.GetImageByIdUseCase
import com.dracul.images.domain.usecase.InsertImageImpl
import com.dracul.images.domain.usecase.InsertImageUseCase
import com.dracul.images.domain.usecase.UpdateImageImpl
import com.dracul.images.domain.usecase.UpdateImageUseCase
import org.koin.dsl.module

val imagesModule = module {
    //repository
    single<GetImageByIdRepo> {
        GetImagesByIdRepoImpl()
    }
    single<GetAllImagesByParentIdRepo> {
        GetAllImagesByParentIdRepoImpl()
    }
    single<DeleteImageRepo> {
        DeleteImageRepoImpl()
    }
    single<InsertImageRepo> {
        InsertImageRepoImpl()
    }
    single<UpdateImageRepo> {
        UpdateImageRepoImpl()
    }
    single<DeleteImageByParentIdRepo> {
        DeleteImageByParentIdRepoImpl()
    }

    //usecase
    single<GetImageByIdUseCase> {
        GetImageByIdImpl(get())
    }
    single<GetAllImagesByParentIdUseCase> {
        GetAllImagesByParentIdImpl(get())
    }
    single<DeleteImageUseCase> {
        DeleteImageImpl(get())
    }
    single<DeleteImageByIdUseCase> {
        DeleteImageByParentIdImpl(get())
    }
    single<InsertImageUseCase> {
        InsertImageImpl(get())
    }
    single<UpdateImageUseCase> {
        UpdateImageImpl(get())
    }
}