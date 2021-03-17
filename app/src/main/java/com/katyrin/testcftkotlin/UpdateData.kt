package com.katyrin.testcftkotlin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.katyrin.testcftkotlin.model.CurrenciesDTO
import com.katyrin.testcftkotlin.repository.*
import com.katyrin.testcftkotlin.utils.convertCurrenciesDTOToModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateData(private val context: Context, workerParameters: WorkerParameters)
    : Worker(context, workerParameters) {

    private val currencyRemoteRepository: CurrencyRepository =
        CurrencyRepositoryImpl(RemoteDataSource())
    private val currencyLocalRepository: LocalRepository =
        LocalRepositoryImpl(App.getCurrenciesDao())

    override fun doWork(): Result {
        return try {
            updateCurrenciesInDataBase()
            showNotification(
                context.getString(R.string.update_data),
                context.getString(R.string.update_data_message))
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun updateCurrenciesInDataBase() {
        currencyRemoteRepository.getCurrenciesFromServer(object : Callback<CurrenciesDTO> {
            override fun onResponse(call: Call<CurrenciesDTO>, response: Response<CurrenciesDTO>) {
                val serverResponse: CurrenciesDTO? = response.body()
                if (serverResponse != null) {
                    convertCurrenciesDTOToModel(serverResponse).map {
                        Thread {
                            currencyLocalRepository.saveEntity(it)
                        }.start()
                    }
                }
            }
            override fun onFailure(call: Call<CurrenciesDTO>, t: Throwable) {}
        })
    }

    private fun showNotification(title: String, message: String) {
        val notificationBuilder =
            context.let {
                NotificationCompat.Builder(it, CHANNEL_ID).apply {
                    setSmallIcon(R.drawable.ic_baseline_refresh_24)
                    setContentTitle(title)
                    setContentText(message)
                    setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    priority = NotificationCompat.PRIORITY_HIGH
                }
            }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = context.getString(R.string.update_data)
        val descriptionText = context.getString(R.string.update_data_message)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "Update Data ID"
        private const val NOTIFICATION_ID = 24
    }
}