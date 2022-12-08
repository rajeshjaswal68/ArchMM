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

package io.dg.archmm.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import io.dg.archmm.core.data.DefaultSatelliteRepository
import io.dg.archmm.core.database.Satellite
import io.dg.archmm.core.database.SatelliteDao

/**
 * Unit tests for [DefaultSatelliteRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultSatelliteRepositoryTest {

    @Test
    fun satellites_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultSatelliteRepository(FakeSatelliteDao())

        repository.add("Repository")

        assertEquals(repository.satellites.first().size, 1)
    }

}

private class FakeSatelliteDao : SatelliteDao {

    private val data = mutableListOf<Satellite>()

    override fun getSatellites(): Flow<List<Satellite>> = flow {
        emit(data)
    }

    override suspend fun insertSatellite(item: Satellite) {
        data.add(0, item)
    }
}
