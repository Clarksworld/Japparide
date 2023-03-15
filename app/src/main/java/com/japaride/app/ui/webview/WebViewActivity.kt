package com.japaride.app.ui.webview

import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.japaride.app.databinding.ActivityWebViewBinding
import com.japaride.app.ui.map.MapsActivity
import com.japaride.app.visible

class WebViewActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWebViewBinding
    var url = "https://app.japparide.com/passenger/login"
    var alertDialog: AlertDialog? = null



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide();
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        binding.webViewPrivacy.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
//                view.loadUrl(url)
//
//                return true

//                if (url.contains("geo:")){
//
//                    val gmmIntentUri = Uri.parse(url)
//                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                    mapIntent.setPackage("com.google.android.apps.maps")
//                    if (mapIntent.resolveActivity(packageManager) != null){
//                        startActivity(mapIntent)
//                    }
//
//                    return true
//                }
//
//
                view.loadUrl(url)

                return true

//                if (url != null && url.startsWith("https://maps.google.com")){
//                    view.context.startActivity(
//                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
//
//                    )
//
//                    return true
//                }else{
//                    view.loadUrl(url)
//                    return false
//                }

//                url.startsWith("http://maps.google.com") {
//                    mWebView.getContext().startActivity(
//                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                    return true;
//                } else {
//                    view.loadUrl(url);
//                    return false;
//                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                binding.fingerPrintLoginProgress.visible(true)

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                binding.fingerPrintLoginProgress.visible(false)

            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

                binding.fingerPrintLoginProgress.visible(true)

            }


        }


        binding.refreshLayout.setOnRefreshListener {

            refreshPage()


        }

        binding.scrollViewLayout.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(
                v: View?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY > 5) {
                    binding.scrollViewLayout.setEnabled(false)
                } else {
                    binding.scrollViewLayout.setEnabled(true)
                }
            }
        })


        binding.webViewPrivacy.loadUrl(url)
        binding.webViewPrivacy.settings.javaScriptEnabled = true
        binding.webViewPrivacy.settings.allowContentAccess = true
        binding.webViewPrivacy.settings.domStorageEnabled = true
        binding.webViewPrivacy.settings.useWideViewPort = true
        binding.webViewPrivacy.settings.setAppCacheEnabled(true)
        binding.webViewPrivacy.settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        binding.webViewPrivacy.settings.setGeolocationEnabled(true)
        binding.webViewPrivacy.webChromeClient = object: WebChromeClient() {

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback
            ) {
                // Always grant permission since the app itself requires location
                // permission and the user has therefore already granted it
                callback.invoke(origin, true, false)


            }
        }


        binding.webViewPrivacy.setOnKeyListener { _, _, keyEvent ->
            if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && !binding.webViewPrivacy.canGoBack()) {
                false
            } else if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == MotionEvent.ACTION_UP) {
                binding.webViewPrivacy.goBack()
                true
            } else true
        }


        onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (binding.webViewPrivacy.canGoBack()){
                    binding.webViewPrivacy.goBack()
                }else{
//
                    createDialog()
//                    alertDialog?.show()

                }
            }
        })


    }


    fun createDialog() {

        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
        finish()

    }



    fun refreshPage(){

        binding.webViewPrivacy.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                view.loadUrl(url)

                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)

                binding.fingerPrintLoginProgress.visible(true)


            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                binding.fingerPrintLoginProgress.visible(false)
                binding.refreshLayout.isRefreshing = false


            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

//                val directions = SignUpFragmentDirections.
//                actionSignUpFragmentToErrorScreenFragment("four")
//                findNavController().navigate(directions)
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)

//                val directions = SignUpFragmentDirections.
//                actionSignUpFragmentToErrorScreenFragment("four")
//                findNavController().navigate(directions)

            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)

                handler?.cancel()
            }
        }



        binding.webViewPrivacy.loadUrl(binding.webViewPrivacy.url.toString())
        binding.webViewPrivacy.settings.javaScriptEnabled = true
        binding.webViewPrivacy.settings.allowContentAccess = true
        binding.webViewPrivacy.settings.domStorageEnabled = true
        binding.webViewPrivacy.settings.useWideViewPort = true
        binding.webViewPrivacy.webChromeClient = object: WebChromeClient(){

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback
            ) {
                // Always grant permission since the app itself requires location
                // permission and the user has therefore already granted it
                callback.invoke(origin, true, false)


            }
        }
    }





}