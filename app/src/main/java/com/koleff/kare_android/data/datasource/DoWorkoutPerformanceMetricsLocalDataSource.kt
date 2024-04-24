package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.response.DoWorkoutPerformanceMetricsListResponse
import com.koleff.kare_android.data.model.response.DoWorkoutPerformanceMetricsResponse
import com.koleff.kare_android.data.model.response.UserResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsListWrapper
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.UserWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class DoWorkoutPerformanceMetricsLocalDataSource(
    private val doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao,
    private val doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao
) : DoWorkoutPerformanceMetricsDataSource {
    override suspend fun saveDoWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            doWorkoutPerformanceMetricsDao.insertWorkoutPerformanceMetrics(performanceMetrics.toEntity())

            val result = ServerResponseData(
                BaseResponse()
            )
            emit(ResultWrapper.Success(result))
        }

    override suspend fun getAllDoWorkoutPerformanceMetricsById(
        id: Int,
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)
        try {
            val data =
                doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetricsById(id, start, end)

            val result = DoWorkoutPerformanceMetricsListWrapper(
                DoWorkoutPerformanceMetricsListResponse(
                    data.map { it.toDto() }
                )
            )

            emit(ResultWrapper.Success(result))
        } catch (e: NoSuchElementException) {
            emit(
                ResultWrapper.ApiError(
                    error = KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND
                )
            )
        }
    }

    override suspend fun getDoWorkoutPerformanceMetricsById(id: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = doWorkoutPerformanceMetricsDao.getWorkoutPerformanceMetricsById(id)
                ?: run {

                    //Performance metrics not found...
                    emit(
                        ResultWrapper.ApiError(
                            error = KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND
                        )
                    )
                    return@flow
                }

            val result = DoWorkoutPerformanceMetricsWrapper(
                DoWorkoutPerformanceMetricsResponse(
                    data.toDto()
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getDoWorkoutPerformanceMetricsByWorkoutId(workoutId: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val data =
                    doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetricsByWorkoutId(
                        workoutId
                    )

                if(data.isNotEmpty()) throw NoSuchElementException()

                val result = DoWorkoutPerformanceMetricsListWrapper(
                    DoWorkoutPerformanceMetricsListResponse(
                        data.map { it.toDto() }
                    )
                )

                emit(ResultWrapper.Success(result))
            } catch (e: NoSuchElementException) {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND
                    )
                )
            }
        }

    override suspend fun getAllDoWorkoutPerformanceMetricsByWorkoutId(
        workoutId: Int,
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        try {
            val data =
                doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetricsByWorkoutId(
                    workoutId,
                    start,
                    end
                )

            val result = DoWorkoutPerformanceMetricsListWrapper(
                DoWorkoutPerformanceMetricsListResponse(
                    data.map { it.toDto() }
                )
            )

            emit(ResultWrapper.Success(result))
        } catch (e: NoSuchElementException) {
            emit(
                ResultWrapper.ApiError(
                    error = KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND
                )
            )
        }
    }

    override suspend fun getAllDoWorkoutPerformanceMetrics(): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val data =
                    doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics()

                val result = DoWorkoutPerformanceMetricsListWrapper(
                    DoWorkoutPerformanceMetricsListResponse(
                        data.map { it.toDto() }
                    )
                )

                emit(ResultWrapper.Success(result))
            } catch (e: NoSuchElementException) {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND
                    )
                )
            }
        }

    override suspend fun getAllDoWorkoutPerformanceMetrics(
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val data =
                    doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics(
                        start,
                        end
                    )

                val result = DoWorkoutPerformanceMetricsListWrapper(
                    DoWorkoutPerformanceMetricsListResponse(
                        data.map { it.toDto() }
                    )
                )

                emit(ResultWrapper.Success(result))
            } catch (e: NoSuchElementException) {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND
                    )
                )
            }
        }

    override suspend fun deleteDoWorkoutPerformanceMetrics(id: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            doWorkoutPerformanceMetricsDao.deleteWorkoutPerformanceMetricsById(id)

            //Delete DoWorkoutExerciseSets for the DoWorkoutPerformanceMetrics
            val exerciseSets = doWorkoutExerciseSetDao.findSetByPerformanceMetricsId(id)
            exerciseSets.forEach { exerciseSet ->
                doWorkoutExerciseSetDao.deleteSet(exerciseSet)
            }

            val result = ServerResponseData(
                BaseResponse()
            )
            emit(ResultWrapper.Success(result))
        }

    override suspend fun updateDoWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Update each DoWorkoutExerciseSet
            val exerciseSets = performanceMetrics.doWorkoutExerciseSets
            exerciseSets.forEach { exerciseSet ->
                val exerciseSetDto = exerciseSet.toEntity()

                //No entry in DB -> insert
                if (doWorkoutExerciseSetDao.updateSet(exerciseSetDto) == 0) {
                    doWorkoutExerciseSetDao.insertSet(exerciseSetDto)
                }
            }

            //Update performance metrics
            doWorkoutPerformanceMetricsDao.updateDoWorkoutPerformanceMetrics(performanceMetrics.toEntity())

            val result = DoWorkoutPerformanceMetricsWrapper(
                DoWorkoutPerformanceMetricsResponse(
                    performanceMetrics
                )
            )
            emit(ResultWrapper.Success(result))
        }

    override suspend fun saveDoWorkoutExerciseSet(exerciseSet: DoWorkoutExerciseSetDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            doWorkoutExerciseSetDao.insertSet(exerciseSet.toEntity())

            val result = ServerResponseData(
                BaseResponse()
            )
            emit(ResultWrapper.Success(result))
        }

    override suspend fun saveAllDoWorkoutExerciseSets(exerciseSets: List<DoWorkoutExerciseSetDto>): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            doWorkoutExerciseSetDao.insertAllSets(
                exerciseSets.map { it.toEntity() }
            )

            val result = ServerResponseData(
                BaseResponse()
            )
            emit(ResultWrapper.Success(result))
        }
}