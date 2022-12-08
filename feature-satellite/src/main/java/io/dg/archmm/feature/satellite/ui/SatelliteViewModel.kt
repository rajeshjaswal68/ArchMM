/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dg.archmm.feature.satellite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import io.dg.archmm.core.data.SatelliteRepository
import io.dg.archmm.feature.satellite.ui.SatelliteUiState.Error
import io.dg.archmm.feature.satellite.ui.SatelliteUiState.Loading
import io.dg.archmm.feature.satellite.ui.SatelliteUiState.Success
import javax.inject.Inject

@HiltViewModel
class SatelliteViewModel @Inject constructor(
    private val satelliteRepository: SatelliteRepository
) : ViewModel() {

    val uiState: StateFlow<SatelliteUiState> = satelliteRepository
        .satellites.map { Success(data = it) }
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addSatellite(name: String) {
        viewModelScope.launch {
            satelliteRepository.add(name)
        }
    }
}

sealed interface SatelliteUiState {
    object Loading : SatelliteUiState
    data class Error(val throwable: Throwable) : SatelliteUiState
    data class Success(val data: List<String>) : SatelliteUiState
}
