package com.katyrin.testcftkotlin.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import com.katyrin.testcftkotlin.R
import com.katyrin.testcftkotlin.UpdateData
import com.katyrin.testcftkotlin.databinding.ActivityMainBinding
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    interface OnUpdateDataListener {
        fun updateData()
    }

    private lateinit var listener: OnUpdateDataListener
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CurrenciesFragment.newInstance())
                    .commitNow()
        }

        if (!isWorkScheduled(TAG_REFRESH_WORK))
            setPeriodicDataUpdate(TAG_REFRESH_WORK)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is OnUpdateDataListener) {
            listener = fragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.updateData -> {
                listener.updateData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setPeriodicDataUpdate(tag: String?) {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
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
            tag!!,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    private fun isWorkScheduled(tag: String): Boolean {
        val instance = WorkManager.getInstance(applicationContext)
        val statuses: ListenableFuture<List<WorkInfo>> = instance.getWorkInfosByTag(tag)
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