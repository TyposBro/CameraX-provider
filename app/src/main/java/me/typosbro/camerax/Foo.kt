//package me.typosbro.camerax
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.view.Surface
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import androidx.media3.common.Player
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
//import androidx.media3.ui.PlayerView
//import java.util.concurrent.Executors
//
//class MainActivity : ComponentActivity() {
//    private lateinit var cameraExecutor: ExecutorService
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        cameraExecutor = Executors.newSingleThreadExecutor()
//        setContent {
//            CameraPreviewWithExoPlayer()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraExecutor.shutdown()
//    }
//
//    @Composable
//    fun CameraPreviewWithExoPlayer() {
//        val context = LocalContext.current
//        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
//
//        var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
//        val preview = remember { Preview.Builder().build() }
//        val cameraSelector = remember { CameraSelector.DEFAULT_BACK_CAMERA }
//
//        DisposableEffect(context) {
//            exoPlayer = ExoPlayer.Builder(context)
//                .setMediaSourceFactory(DefaultMediaSourceFactory(context))
//                .build()
//            exoPlayer?.playWhenReady = true
//            exoPlayer?.repeatMode = Player.REPEAT_MODE_OFF
//            exoPlayer?.prepare()
//
//            onDispose {
//                exoPlayer?.release()
//            }
//        }
//
//        LaunchedEffect(preview) {
//            val cameraProvider = ProcessCameraProvider.getInstance(context).get()
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    lifecycleOwner,
//                    cameraSelector,
//                    preview
//                )
//            } catch (e: Exception) {
//                Toast.makeText(context, "Failed to start camera", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        AndroidView(
//            factory = { ctx ->
//                PlayerView(ctx).apply {
//                    player = exoPlayer
//                    useController = false
//                    preview.setSurfaceProvider { surface ->
//                        exoPlayer?.setVideoSurface(surface)
//                        Surface(surface)
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//
//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
//    }
//
//    companion object {
//        private const val REQUEST_CODE_PERMISSIONS = 10
//        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//    }
//}