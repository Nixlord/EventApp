package com.phoenixoverlord.eventapp.mechanisms

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import com.phoenixoverlord.eventapp.main.MainActivity
import com.phoenixoverlord.eventapp.utils.LoopingAtomicInteger


// Make lifecycle aware
class NotificationModule(context: Context) : ContextWrapper(context) {

    private val CHANNEL_ID_UPLOAD = "Upload"

    init {
        createNotificationChannel()
    }

    private val loopingAtomicInteger = LoopingAtomicInteger(1, 10000)

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID_UPLOAD
            val descriptionText = "Shubh Vivaah Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID_UPLOAD, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun createHandlerIntent(): PendingIntent? {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(this, 0, intent, 0)
    }

    fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(this).cancel(notificationId)
    }

    fun createNotification(title: String, text: String) : Int {
        val notificationId = loopingAtomicInteger.nextInt()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_UPLOAD)
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setContentText(text)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createHandlerIntent())
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, notificationBuilder.build())
        }

        return notificationId
    }

    fun createUploadProgressNotification(title: String, uploadTask: UploadTask) : Int {
        val notificationId = loopingAtomicInteger.nextInt()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_UPLOAD)
            .setContentTitle(title)
            .setContentText("Uploading")
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        NotificationManagerCompat.from(this).apply {

            // Start
            notificationBuilder.setProgress(100, 0, false)
            notify(notificationId, notificationBuilder.build())

            //Progress
            uploadTask.addOnProgressListener {
                val percent = (100.0 * it.bytesTransferred) / it.totalByteCount
                notificationBuilder.setProgress(100, percent.toInt(), false)
                notify(notificationId, notificationBuilder.build())

            }.addOnSuccessListener {

                notificationBuilder.setContentText("Upload complete")
                    .setProgress(0, 0, false)
                    .setContentIntent(createHandlerIntent())
                    .setAutoCancel(true)

                notify(notificationId, notificationBuilder.build())
            }
        }

        return notificationId
    }

    // Refactor Ugly Piece of Shit
    fun createDownloadProgressNotification(id: Int, title: String, fileDownloadTask: FileDownloadTask) {

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID_UPLOAD)
            .setContentTitle(title)
            .setContentText("Downloading")
            .setSmallIcon(android.R.drawable.ic_menu_upload)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        NotificationManagerCompat.from(this).apply {

            // Start
            notificationBuilder.setProgress(100, 0, false)
            notify(id, notificationBuilder.build())

            //Progress
            fileDownloadTask.addOnProgressListener {
                val percent = (100.0 * it.bytesTransferred) / it.totalByteCount
                notificationBuilder.setProgress(100, percent.toInt(), false)
                notify(id, notificationBuilder.build())

            }.addOnSuccessListener {

                notificationBuilder.setContentText("Download complete")
                    .setProgress(0, 0, false)
                    .setContentIntent(createHandlerIntent())
                    .setAutoCancel(true)
                notify(id, notificationBuilder.build())
            }
        }
    }
}