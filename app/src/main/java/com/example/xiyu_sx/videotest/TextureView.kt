package com.example.xiyu_sx.videotest

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.texture_view.*
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Handler
import android.util.TypedValue
import android.view.*
import android.view.TextureView
import android.widget.RelativeLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.media_play.*

import kotlinx.android.synthetic.main.play_layout.*


/**
 * Created by xiyu_sx on 2017/7/28.
 */
class TextureView:AppCompatActivity(),TextureView.SurfaceTextureListener,View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    var mediaPlayer = MediaPlayer()
    var handler = Handler()
    var runnable: Runnable = object : Runnable {
        override fun run() {
            //要做的事情
            flag_position=mediaPlayer.currentPosition
            seekBar.setProgress(flag_position)
            if((mediaPlayer.currentPosition/1000%60)<10) {
                text1.setText((mediaPlayer.currentPosition / 1000 / 60).toString() + ":0" + (mediaPlayer.currentPosition / 1000 % 60).toString())
            }else{
                text1.setText((mediaPlayer.currentPosition / 1000 / 60).toString() + ":" + (mediaPlayer.currentPosition / 1000 % 60).toString())
            }
            handler.postDelayed(this, 1000)
        }
    }
    companion object {
        var flag=1
        var flag2=1
        var flag_position=1
        var flag_seekbarProgress=0
        var Style=0
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.texture_view)
        val chose=intent.getStringExtra("message")
        when (chose.toString()){
            "one"->{
                Style=1
            }
            "three"->{
                Style=2
            }
            "five"->{
                Style=3
            }
        }

        textureView.setSurfaceTextureListener(this)
        seekBar.setOnSeekBarChangeListener(this)
        full_screen.setOnClickListener(this)
        textureView.setOnClickListener(this)
        play_pause.setOnClickListener(this)
        play_left.setOnClickListener(this)
        play_right.setOnClickListener(this)

    }

    @SuppressLint("SwitchIntDef")
    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.textureView -> {
                Log.e("surface","click!!!!")
                play_control()
            }
            R.id.full_screen-> {
                Log.e("getRequestedOrientation",getRequestedOrientation().toString()+ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE.toString()+ActivityInfo.SCREEN_ORIENTATION_PORTRAIT.toString())
                if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ) {
                    //切换竖屏\
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                }
                else{
                    //切换横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                }
            }
            R.id.play_pause->{
                if(flag2==1) {
                    flag2=2
                    mediaPlayer.pause()
                    runOnUiThread {
                        play_pause.setImageResource(R.drawable.play)
                    }
                }else{
                    flag2=1
                    mediaPlayer.start()
                    runOnUiThread {
                        play_pause.setImageResource(R.drawable.pause)
                    }
                }
            }
            R.id.play_right-> {
                var seek = mediaPlayer.duration / 20 + mediaPlayer.getCurrentPosition()
                if (seek <= mediaPlayer.duration) {
                    mediaPlayer.seekTo(seek)
                } else {
                    mediaPlayer.seekTo((mediaPlayer.duration-1000))
                }
            }
            R.id.play_left->{
                var seek = mediaPlayer.duration / 20 - mediaPlayer.getCurrentPosition()
                if (seek>=0) {
                    mediaPlayer.seekTo(seek)
                } else {
                    mediaPlayer.seekTo(0)
                }
            }
        }

        }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        Log.e("走了这个方法","right")
        val resources = this.getResources()
        val resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ) {
            textureView.setLayoutParams(RelativeLayout.LayoutParams(windowManager.defaultDisplay.width + height, windowManager.defaultDisplay.height))

        }else {
            var temp:Int
            var temp1:Float
            temp1=200.toFloat()
            temp= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp1,getResources().getDisplayMetrics()).toInt()
            textureView.setLayoutParams(RelativeLayout.LayoutParams(windowManager.defaultDisplay.width,temp))
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions

    }
    fun  play_control(){
        when(flag){
            1->{

               texture.setVisibility(View.GONE)
                flag =2
            }
            2->{
                texture.setVisibility(View.VISIBLE)
                flag =1
            }
        }
    }



    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {

        System.out.println("onSurfaceTextureUpdated onSurfaceTextureUpdated")
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
        System.out.println("onSurfaceTextureUpdated onSurfaceTextureUpdated")
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
        var surface = Surface(p0)
        if(Style==3) {

            val uri = Uri.parse("http://220.170.49.115/2/e/o/m/i/eomitmivacsollthkkgpqxwvhdvaps/hc.yinyuetai.com/6ABC015EB9544161E5115F3347256EF4.mp4?sc=a3a6991aefcdf88f")
            mediaPlayer.reset()
            mediaPlayer.setDataSource(this, uri)
            mediaPlayer.setSurface(surface)
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                init_play()
                seekBar.setMax(mediaPlayer.duration)
                val string=(mediaPlayer.duration/1000/60).toString()+":"+(mediaPlayer.duration/1000%60).toString()
                text2.setText(string)

           }
        }
        if(Style==2){
            var uri=Uri.parse("http://220.170.49.115/2/e/o/m/i/eomitmivacsollthkkgpqxwvhdvaps/hc.yinyuetai.com/6ABC015EB9544161E5115F3347256EF4.mp4?sc=a3a6991aefcdf88f")
            mediaPlayer.reset()
            mediaPlayer.setDataSource(this,uri)
            mediaPlayer.setSurface(surface)
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                init_play()
                seekBar.setMax(mediaPlayer.duration)
                val string=(mediaPlayer.duration/1000/60).toString()+":"+(mediaPlayer.duration/1000%60).toString()
                text2.setText(string)
            }
        }
        if(Style==1){
            var uri=Uri.parse("android.resource://com.example.xiyu_sx.videotest/"+R.raw.asddd)
            mediaPlayer.reset()
            mediaPlayer.setDataSource(this,uri)
            mediaPlayer.setSurface(surface)
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
                init_play()
                seekBar.setMax(mediaPlayer.duration)
                val string=(mediaPlayer.duration/1000/60).toString()+":"+(mediaPlayer.duration/1000%60).toString()
                text2.setText(string)
                handler.postDelayed(runnable,1000)
            }
        }
    }
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        mediaPlayer.release()
        textureView.destroyDrawingCache()
        handler.removeCallbacks(runnable)
        return true
    }

    fun init_play(){
        runOnUiThread {
            play_pause.setImageResource(R.drawable.pause)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        flag_seekbarProgress=progress
        if(flag_position+2000<= flag_seekbarProgress){mediaPlayer.seekTo(flag_seekbarProgress)}
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        mediaPlayer.seekTo(flag_seekbarProgress)
    }


}


