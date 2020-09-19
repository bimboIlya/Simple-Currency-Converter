package com.example.cfttesttask

import android.app.Application
import androidx.work.*
import com.example.cfttesttask.di.AppInjector
import com.example.cfttesttask.di.WorkerUpdaterFactory
import com.example.cfttesttask.workers.DailyUpdateWorker
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyApp : Application(), Configuration.Provider, HasAndroidInjector {
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject lateinit var workerFactory: WorkerUpdaterFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        AppInjector.init(this)
        setUpDailyUpdateWorker()
    }

    private fun setUpDailyUpdateWorker() {
        val workManager = WorkManager.getInstance(this)

        val request = PeriodicWorkRequestBuilder<DailyUpdateWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(12, TimeUnit.HOURS)
            .setConstraints(getConstraints())
            .build()

        workManager.enqueueUniquePeriodicWork(
            "WorkerTag",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun getConstraints(): Constraints =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)  // potentially could be chosen thru sharedPref
            .build()

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}