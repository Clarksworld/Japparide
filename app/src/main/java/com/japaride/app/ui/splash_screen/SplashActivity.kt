package com.japaride.app.ui.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.japaride.app.MainActivity
import com.japaride.app.R
import com.japaride.app.ui.auth.AuthenticationActivity
import com.japaride.app.ui.map.MapsActivity
import com.japaride.app.ui.webview.WebViewActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide();


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        val img1: ImageView = findViewById(R.id.imageView)
//        val txt1: ImageView = findViewById(R.id.imageView)
//
//        val slideAnim = AnimationUtils.loadAnimation(this, R.anim.slide_1)
//        val slideAnim2 = AnimationUtils.loadAnimation(this, R.anim.slide_2)
//
//        img1.startAnimation(slideAnim)
//        txt1.startAnimation(slideAnim2)




        Handler().postDelayed({

            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            finish()

//            val sharedPreferences = UserPreferences(this)
//
//            sharedPreferences.authToken.asLiveData().observe(this, Observer { token->
//                if (token!!.isEmpty()){
//
//
//                    val intent = Intent(this, AuthenticationActivity::class.java)
//                    startActivity(intent)
//                    finish()
//
//                }else{
//
//
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//
//                }
//
//            })

        },2000

        )
    }
}
