package com.katyrin.testcftkotlin.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import com.katyrin.testcftkotlin.App
import com.katyrin.testcftkotlin.R
import com.katyrin.testcftkotlin.UpdateData
import com.katyrin.testcftkotlin.databinding.ActivityMainBinding
import com.katyrin.testcftkotlin.di.AppComponent
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    init {
        supportFragmentManager.addFragmentOnAttachListener { _, fragment ->
            if (fragment is OnUpdateDataListener) {
                listener = fragment
            }
        }
    }

    interface OnUpdateDataListener {
        fun updateData()
    }

    private lateinit var listener: OnUpdateDataListener
    private lateinit var binding: ActivityMainBinding
    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {

        appComponent = (application as App).appComponent

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CurrenciesFragment.newInstance())
                .commitNow()
        }

        if (!isWorkScheduled())
            setPeriodicDataUpdate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.updateData -> {
                listener.updateData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setPeriodicDataUpdate() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest = PeriodicWorkRequest
            .Builder(
                UpdateData::class.java,
                30, TimeUnit.MINUTES,
                25, TimeUnit.MINUTES
            )
            .setConstraints(constraints)
            .build()
        val instance = WorkManager.getInstance(applicationContext)
        instance.enqueueUniquePeriodicWork(
            TAG_REFRESH_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    private fun isWorkScheduled(): Boolean {
        val instance = WorkManager.getInstance(applicationContext)
        val statuses: ListenableFuture<List<WorkInfo>> =
            instance.getWorkInfosByTag(TAG_REFRESH_WORK)
        return try {
            var running = false
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            running
        } catch (e: ExecutionException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        private const val TAG_REFRESH_WORK = "REFRESH_WORK"
    }
}