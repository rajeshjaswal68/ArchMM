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

package io.dg.archmm.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import io.dg.archmm.core.database.Satellite
import io.dg.archmm.core.database.SatelliteDao
import javax.inject.Inject

interface SatelliteRepository {
    val satellites: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultSatelliteRepository @Inject constructor(
    private val satelliteDao: SatelliteDao
) : SatelliteRepository {

    override val satellites: Flow<List<String>> =
        satelliteDao.getSatellites().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        satelliteDao.insertSatellite(Satellite(name = name))
    }
}
