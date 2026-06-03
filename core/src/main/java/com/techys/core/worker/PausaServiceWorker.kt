package com.techys.core.worker

//class PausaServiceWorker (context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
//    override fun doWork(): Result {
//        showNotification()
//        return Result.success()
//    }
//
//    private fun showNotification() {
//        val channelId = "periodic_channel"
//        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId, "Periodic Tasks", NotificationManager.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notification = NotificationCompat.Builder(applicationContext, channelId)
//            .setContentTitle("WorkManager Alert")
//            .setContentText("This is your periodic notification!")
//            .setSmallIcon(R.drawable.ic_notification) // Replace with your icon
//            .build()
//
//        notificationManager.notify(1, notification)
//    }
//}


//val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
//    15, TimeUnit.MINUTES // Minimum interval is 15 minutes
//).build()
//
//WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//"my_unique_notification_work",
//ExistingPeriodicWorkPolicy.KEEP, // Keep existing if already scheduled
//periodicWorkRequest
//)
//
//
//WorkManager.getInstance(context).cancelUniqueWork("my_unique_notification_work")