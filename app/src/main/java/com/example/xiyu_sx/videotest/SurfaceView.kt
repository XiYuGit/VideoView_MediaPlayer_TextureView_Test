package com.example.xiyu_sx.videotest

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.RelativeLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.media_play.*
import kotlinx.android.synthetic.main.play_layout.*




/**
 * Created by xiyu_sx on 2017/7/25.
 */
class SurfaceView : AppCompatActivity(),View.OnClickListener , SeekBar.OnSeekBarChangeListener {

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
        var flag =1
        var flag2=1
        var flag_position=1
        var flag_seekbarProgress=0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_play)
        val chose=intent.getStringExtra("message")
        when (chose.toString()){
            "two"->{
                localSource()
            }
            "four"->{
                netSource()
            }
            "six"->{
                me3uSource()
            }
        }

        full_screen.setOnClickListener(this)
        surfaceView.setOnClickListener(this)
        play_pause.setOnClickListener(this)
        play_left.setOnClickListener(this)
        play_right.setOnClickListener(this)
        seekBar.setOnSeekBarChangeListener(this)
    }

    @SuppressLint("SwitchIntDef")
    override fun onClick(v: View?) {
        when(v?.getId()){
            R.id.surfaceView->{
                Log.e("surface","click!!!!")
                play_control()
            }
            R.id.full_screen-> {
                Log.e("getRequestedOrientation",getRequestedOrientation().toString()+ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE.toString()+ActivityInfo.SCREEN_ORIENTATION_PORTRAIT.toString())
                if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ) {
                    Log.e("横向转竖向", "right")
                        //切换竖屏\
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                }
                   else{
                        //切换横屏
                        Log.e("竖向转横向", "right")
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
        val resources = this.getResources()
        val resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android")
        val height = resources.getDimensionPixelSize(resourceId)
        if(getRequestedOrientation()==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ) {
            surfaceView.setLayoutParams(RelativeLayout.LayoutParams(windowManager.defaultDisplay.width + height, windowManager.defaultDisplay.height))

        }else {
            var temp:Int
            var temp1:Float
            temp1=200.toFloat()
            temp= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,temp1,getResources().getDisplayMetrics()).toInt()
            surfaceView.setLayoutParams(RelativeLayout.LayoutParams(windowManager.defaultDisplay.width,temp))
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions

    }
    fun localSource() {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(this ,Uri.parse("android.resource://com.example.xiyu_sx.videotest/"+R.raw.asddd))
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        val holder=surfaceView.holder
        holder.addCallback(MyCallBack())
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            init_play()
            seekBar.setMax(mediaPlayer.duration)
            val string=(mediaPlayer.duration/1000/60).toString()+":"+(mediaPlayer.duration/1000%60).toString()
            text2.setText(string)
        }
    }

    fun netSource(){
        val uri= Uri.parse("http://220.194.199.176/7/z/h/t/x/zhtxzqhavcpxefedqpmeujscdjwuqx/hc.yinyuetai.com/6899015D81C74332A7D4F0F81E9B8CF3.mp4?sc=a4aca31f34d67f5b")
        mediaPlayer.reset()
        mediaPlayer.setDataSource(this,uri)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        var holder=surfaceView.holder
        holder.addCallback(MyCallBack())
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            init_play()
            seekBar.setMax(mediaPlayer.duration)
            val string=(mediaPlayer.duration/1000/60).toString()+":"+(mediaPlayer.duration/1000%60).toString()
            text2.setText(string)
        }
    }

    fun me3uSource(){
        /*http://streaming.youku.com/live2play/klcd11.m3u8?auth_key=1527043875-0-0-ff81b5c5e9c04df7ab88b3f20ddba94e*/
        val uri=Uri.parse("http://streaming.youku.com/live2play/klcd11.m3u8?auth_key=1527043875-0-0-ff81b5c5e9c04df7ab88b3f20ddba94e")
        mediaPlayer.reset()
        mediaPlayer.setDataSource(this,uri)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        var holder=surfaceView.holder
        holder.addCallback(MyCallBack())
        mediaPlayer.prepare()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            init_play()
        }
    }

    fun init_play(){
        runOnUiThread {
            play_pause.setImageResource(R.drawable.pause)
        }
    }

    fun  play_control(){
        when(flag){
            1->{
                surface.setVisibility(View.GONE)
                flag=2
            }
            2->{
                surface.setVisibility(View.VISIBLE)
                flag=1
            }
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

    private inner class MyCallBack : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            mediaPlayer.setDisplay(holder)
            handler.postDelayed(runnable,1000)
        }
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

        }
        override fun surfaceDestroyed(holder: SurfaceHolder) {
                mediaPlayer.release()
                surfaceView.destroyDrawingCache()
                handler.removeCallbacks(runnable)
        }
    }
}
