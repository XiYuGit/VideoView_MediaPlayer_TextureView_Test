package com.example.xiyu_sx.videotest
import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v4.content.ContextCompat

class MainActivity : AppCompatActivity() ,View.OnClickListener {

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.bt_one->{
                val intent =Intent()
                intent.putExtra("message","one")
                intent.setClass(this, TextureView::class.java)
                startActivity(intent)
            }
            R.id.bt_two->{
                val intent =Intent()
                intent.putExtra("message","two")
                intent.setClass(this, SurfaceView::class.java)
                startActivity(intent)
            }
            R.id.bt_three->{
                val intent =Intent()
                intent.putExtra("message","three")
                intent.setClass(this,TextureView::class.java)
                startActivity(intent)
            }
            R.id.bt_four->{
                val intent =Intent()
                intent.putExtra("message","four")
                intent.setClass(this, SurfaceView::class.java)
                startActivity(intent)
            }
            R.id.bt_five->{
                val intent =Intent()
                intent.putExtra("message","five")
                intent.setClass(this,TextureView::class.java)
                startActivity(intent)
            }
            R.id.bt_six->{
                val intent =Intent()
                intent.putExtra("message","six")
                intent.setClass(this, SurfaceView::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
        }
        bt_one.setOnClickListener(this)
        bt_two.setOnClickListener(this)
        bt_three.setOnClickListener(this)
        bt_four.setOnClickListener(this)
        bt_five.setOnClickListener(this)
        bt_six.setOnClickListener(this)
        toolbar.setTitle("VideoTest")
        toolbar.setTitleTextColor(Color.WHITE)
    }

}