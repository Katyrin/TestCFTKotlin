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
            if (fragment is OnUpdateDataListener) listener = fragment
        }
    }

    private lateinit var listener: OnUpdateDataListener
    private lateinit var binding: ActivityMainBinding
    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (application as App).appComponent
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) replaceCurrenciesFragment()
        if (!isWorkScheduled()) setPeriodicDataUpdate()
    }

    private fun replaceCurrenciesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CurrenciesFragment.newInstance())
            .commitNow()
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
        val periodicWorkRequest = getPeriodicWorkRequest()
        val instance = WorkManager.getInstance(this)
        instance.enqueueUniquePeriodicWork(
            TAG_REFRESH_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    private fun getPeriodicWorkRequest() =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            .let { constraints ->
                PeriodicWorkRequest.Builder(
                    UpdateData::class.java,
                    REPEAT_INTERVAL, TimeUnit.MINUTES,
                    FIX_INTERVAL, TimeUnit.MINUTES
                )
                    .setConstraints(constraints)
                    .build()
            }


    private fun isWorkScheduled(): Boolean {
        val statuses = WorkManager.getInstance(this).getWorkInfosByTag(TAG_REFRESH_WORK)
        return try {
            var running = false
            for (workInfo in statuses.get()) {
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
        private const val FIX_INTERVAL = 25L
        private const val REPEAT_INTERVAL = 30L
        private const val TAG_REFRESH_WORK = "REFRESH_WORK"
    }
}