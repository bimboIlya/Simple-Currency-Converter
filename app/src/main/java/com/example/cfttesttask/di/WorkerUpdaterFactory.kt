package com.example.cfttesttask.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.cfttesttask.data.sources.CurrencyRepository
import com.example.cfttesttask.workers.DailyUpdateWorker
import timber.log.Timber
import javax.inject.Inject

class WorkerUpdaterFactory @Inject constructor(
    private val repo: CurrencyRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            DailyUpdateWorker::class.java.name -> DailyUpdateWorker(
                appContext,
                workerParameters,
                repo
            )
            else -> null
        }
    }
}