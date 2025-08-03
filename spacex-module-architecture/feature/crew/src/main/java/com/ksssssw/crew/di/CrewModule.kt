package com.ksssssw.crew.di

import com.ksssssw.crew.CrewViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val crewModule = module {
    viewModel {
        CrewViewModel(
            crewRepository = get()
        )
    }
}