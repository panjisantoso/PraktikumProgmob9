package com.example.progmobkelompok9.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log

import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.progmobkelompok9.DocumentDetailActivity
import com.example.progmobkelompok9.MainActivity
import com.example.progmobkelompok9.R
import com.example.progmobkelompok9.api.ApiClient
import com.example.progmobkelompok9.api.ApiService
import com.example.progmobkelompok9.model.Auth
import com.example.progmobkelompok9.model.Document
import com.example.progmobkelompok9.model.ResponseMessage

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import java.net.HttpURLConnection
import java.net.URL
import java.util.Random

import com.example.progmobkelompok9.util.StringFixed
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var notificationManager: NotificationManager? = null
    private lateinit var listDocument:List<Document>
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.e("data",remoteMessage!!.data["document"].toString())
        val gson = GsonBuilder().create()
        listDocument = gson.fromJson<ArrayList<Document>>(remoteMessage.data["document"], object : TypeToken<ArrayList<Document>>(){}.type)
        
        val notificationIntent = Intent(this, DocumentDetailActivity::class.java)
        notificationIntent.putExtra(StringFixed.KEY_ID_DOCUMENT, listDocument[0].idDocument)
        notificationIntent.putExtra(StringFixed.KEY_ID_USER, listDocument[0].idUser)
        notificationIntent.putExtra(StringFixed.KEY_NAMA_USER, listDocument[0].namaUser)
        notificationIntent.putExtra(StringFixed.KEY_NAMA_CATEGORY, listDocument[0].namaCategory)
        notificationIntent.putExtra(StringFixed.KEY_NAMA_DOCUMENT, listDocument[0].namaDocument)
        notificationIntent.putExtra(StringFixed.KEY_FILE_TYPE, listDocument[0].fileType)
        notificationIntent.putExtra(StringFixed.KEY_PENULIS, listDocument[0].penulis)
        notificationIntent.putExtra(StringFixed.KEY_PENERBIT, listDocument[0].penerbit)
        notificationIntent.putExtra(StringFixed.KEY_TAHUN_TERBIT, listDocument[0].tahunTerbit)
        notificationIntent.putExtra(StringFixed.KEY_DESKRIPSI, listDocument[0].deskripsi)
        notificationIntent.putExtra(StringFixed.KEY_PATH, listDocument[0].path)
        notificationIntent.putExtra(StringFixed.KEY_TOTAL_VIEW, listDocument[0].totalView)
        notificationIntent.putExtra(StringFixed.KEY_TOTAL_DOWNLOAD, listDocument[0].totalDownload)
        notificationIntent.putExtra(StringFixed.KEY_TGL_UPLOAD, listDocument[0].tglUpload)
        notificationIntent.putExtra(StringFixed.KEY_STATUS_AKTIF, listDocument[0].statusAktif)
        notificationIntent.putExtra(StringFixed.KEY_IMAGE_USER, listDocument[0].imageUser)
        notificationIntent.putExtra(StringFixed.KEY_IMAGE, listDocument[0].image)
        notificationIntent.putExtra(StringFixed.KEY_STATUS_LIKE, "0")
        
        if (MainActivity.isAppRunning) {
            //Some action
        } else {
            //Show notification as usual
        }

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT)

        //You should use an actual ID instead
        val notificationId = Random().nextInt(60000)
        remoteMessage?.let {
            val bitmap = getBitmapFromUrl(remoteMessage.data["image-url"])

            val likeIntent = Intent(this, LikeService::class.java)
            likeIntent.putExtra(NOTIFICATION_ID_EXTRA, notificationId)
            likeIntent.putExtra(IMAGE_URL_EXTRA, remoteMessage.data["image-url"])
            val likePendingIntent = PendingIntent.getService(this,
                    notificationId + 1, likeIntent, PendingIntent.FLAG_ONE_SHOT)

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels()
            }

            val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage!!.data["title"])
                    .setStyle(NotificationCompat.BigPictureStyle()
                            .setSummaryText(remoteMessage.data["body"])
                            .bigPicture(bitmap))/*Notification with Image*/
                    .setContentText(remoteMessage.data["body"])
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .addAction(R.drawable.ic_favorite_true,
                            getString(R.string.notification_add_to_cart_button), likePendingIntent)
                    .setContentIntent(pendingIntent)

            notificationManager?.notify(notificationId, notificationBuilder.build())
        }
    }

    override fun onNewToken(token: String?) {
        Log.d("TAG", "Refreshed token: $token")
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.edit().putString(StringFixed.KEY_FCM_TOKEN, token).apply()
        val idUser = preferences.getString(StringFixed.KEY_ID_USER,"")

        api(idUser,token!!)
    }

    private fun api(idUser:String, token:String){
        ApiClient.getClient()
                .create(ApiService::class.java)
                .updateFcmToken(idUser, token)
                .enqueue(object : Callback<ResponseMessage> {
                    override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                        try {
                            Log.e("update",response.message())
                        }
                        catch (e:Exception){
                            Log.e("update",e.message)
                        }

                    }

                    override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                        Log.e("update",t.message)
                    }
                })
    }

    private fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels() {
        val adminChannelName = getString(R.string.notifications_admin_channel_name)
        val adminChannelDescription = getString(R.string.notifications_admin_channel_description)

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)

    }

    companion object {

        private const val NOTIFICATION_ID_EXTRA = "notificationId"
        private const val IMAGE_URL_EXTRA = "imageUrl"
        private const val ADMIN_CHANNEL_ID = "admin_channel"
    }
}