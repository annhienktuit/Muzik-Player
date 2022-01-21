package com.annhienktuit.muzikplayer.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.annhienktuit.muzikplayer.R
import com.annhienktuit.muzikplayer.utils.CacheUtils.Companion.simpleMusicCache
import com.annhienktuit.muzikplayer.utils.MuzikUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

class MusicService : Service() {
    private lateinit var exoPlayer: ExoPlayer
    private var listID = ArrayList<String>()
    private var listURL = ArrayList<String>()
    private var listArtwork = ArrayList<String>()
    private var listTitle = ArrayList<String>()
    private var listArtist = ArrayList<String>()
    private var listMediaSources = ArrayList<MediaSource>()

    companion object {
        const val MEDIA_SESSION_TAG = "media_session"
        const val notificationID = 123
        const val channelID = "com.annhienktuit.muzikplayer"
        private var currentIndex = 0
    }

    override fun onBind(intent: Intent?): IBinder {
        exoPlayer.playWhenReady = true
        getDataFromBundle(intent)
        initializePlayer()
        initializeNotification()
        return PlayerServiceBinder()
    }

    override fun onCreate() {
        super.onCreate()
        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(8 * 1024, 32 * 1024, 1024, 1024)
            .build()
        exoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(this))
            .setLoadControl(loadControl)
            .setTrackSelector(DefaultTrackSelector(this))
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        getDataFromBundle(intent)
        return START_STICKY
    }

    private fun getDataFromBundle(intent: Intent?) {
        val extras = intent?.extras
        if (extras != null) {
            listID = extras.getStringArrayList("listID") as ArrayList<String>
            listURL = extras.getStringArrayList("listURL") as ArrayList<String>
            listArtwork = extras.getStringArrayList("listArtwork") as ArrayList<String>
            listArtist = extras.getStringArrayList("listArtist") as ArrayList<String>
            listTitle = extras.getStringArrayList("listTitle") as ArrayList<String>
            currentIndex = extras.getInt("Index")
        }
    }

    private fun initializePlayer() {
        val concatenatingMediaSource: ConcatenatingMediaSource = initializeMedia()
        exoPlayer.playWhenReady = true
        exoPlayer.setMediaSource(concatenatingMediaSource)
        exoPlayer.prepare()
        exoPlayer.seekTo(currentIndex, 0)
    }

    private fun initializeMedia(): ConcatenatingMediaSource {
        val httpDataSourceFactory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleMusicCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        for (idx in 0 until listURL.size) {
            val mediaItem = MediaItem.Builder()
                .setUri(Uri.parse(listURL[idx]))
                .build()
            if (MuzikUtils.isOnlinePath(mediaItem.localConfiguration!!.uri.toString())) {
                listMediaSources.add(
                    ProgressiveMediaSource
                        .Factory(cacheDataSourceFactory)
                        .createMediaSource(mediaItem)
                )
            } else {
                listMediaSources.add(
                    ProgressiveMediaSource
                        .Factory(DefaultDataSource.Factory(this))
                        .createMediaSource(mediaItem)
                )
            }
        }

        val concatenatingMediaSource = ConcatenatingMediaSource()
        concatenatingMediaSource.addMediaSources(listMediaSources)
        return concatenatingMediaSource
    }

    private fun initializeNotification() {
        createNotificationChannel()
        val mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG)
        val mediaController = MediaControllerCompat(this, mediaSession.sessionToken)
        mediaSession.isActive = true
        val mediaSessionConnector = MediaSessionConnector(mediaSession)

        val timelineQueueNavigator = object : TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(
                player: Player,
                windowIndex: Int
            ): MediaDescriptionCompat {
                player.let { safePlayer ->
                    return MediaDescriptionCompat.Builder().apply {
                        setTitle(listTitle[exoPlayer.currentMediaItemIndex])
                    }.build()
                }
                return MediaDescriptionCompat.Builder().build()
            }
        }

        mediaSessionConnector.setQueueNavigator(timelineQueueNavigator)
        mediaSessionConnector.setPlayer(exoPlayer)

        val notificationListener = object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationPosted(
                notificationId: Int,
                notification: Notification,
                ongoing: Boolean
            ) {
                super.onNotificationPosted(notificationId, notification, ongoing)
                startForeground(notificationId, notification)
            }
        }

        val playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            notificationID,
            channelID
        )
            .setMediaDescriptionAdapter(
                DescriptionAdapter(
                    this,
                    mediaController,
                    listArtwork,
                    listArtist
                )
            )
            .setSmallIconResourceId(R.drawable.ic_logo)
            .setNotificationListener(notificationListener)
            .build()

        playerNotificationManager.apply {
            setUseNextAction(true)
            setMediaSessionToken(mediaSession.sessionToken)
            setPlayer(exoPlayer)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TITLE"
            val descriptionText = "DESC"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    inner class PlayerServiceBinder : Binder() {
        fun getExoPlayerInstance() = exoPlayer
        fun getCurrentIndex() = exoPlayer.currentMediaItemIndex
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        val restartServicePendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val alarmService = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000] =
            restartServicePendingIntent
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        Toast.makeText(this, "service destroyed", Toast.LENGTH_SHORT).show()
    }

}

class DescriptionAdapter(
    val context: Context, private val controller: MediaControllerCompat,
    private var listArtwork: ArrayList<String>,
    private var listArtist: ArrayList<String>
) :
    PlayerNotificationManager.MediaDescriptionAdapter {
    private var mContext: Context = context
    override fun getCurrentContentTitle(player: Player): String {
        val window = player.currentMediaItemIndex
        return window.toString()
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return controller.sessionActivity
    }

    override fun getCurrentContentText(player: Player): String? {
        return listArtist[player.currentMediaItemIndex]
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        loadBitmap(listArtwork[player.currentMediaItemIndex], callback)
        return getBitmapFromVectorDrawable(mContext, R.drawable.ic_logo)
    }

    private fun loadBitmap(url: String, callback: PlayerNotificationManager.BitmapCallback?) {
        Glide.with(context.applicationContext)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    callback?.onBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun getBitmapFromVectorDrawable(
        context: Context,
        @DrawableRes drawableId: Int
    ): Bitmap? {
        return ContextCompat.getDrawable(context, drawableId)?.let {
            val drawable = DrawableCompat.wrap(it).mutate()
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

}