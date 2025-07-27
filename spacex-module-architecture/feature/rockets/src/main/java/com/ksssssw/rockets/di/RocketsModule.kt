package com.ksssssw.rockets.di

import com.ksssssw.rockets.RocketsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val rocketsModule = module {
    viewModel { RocketsViewModel(get()) }
}