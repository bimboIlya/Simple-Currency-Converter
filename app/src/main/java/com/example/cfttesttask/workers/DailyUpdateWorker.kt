package com.example.cfttesttask.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cfttesttask.data.sources.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber

class DailyUpdateWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val repo: CurrencyRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            repo.updateDataIfNecessary(forceUpdate = true)
            return@withContext Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < RETRY_AMOUNT) return@withContext Result.retry()
            return@withContext Result.failure()
        }
    }

    companion object {
        private const val RETRY_AMOUNT = 3
    }
}