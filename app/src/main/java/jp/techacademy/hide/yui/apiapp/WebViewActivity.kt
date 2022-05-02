package jp.techacademy.hide.yui.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity: AppCompatActivity() {

    // onCreate()メソッド内で呼び出されている上記メソッドで、WebViewで指定されたURLを読み込み表示しています。
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView.loadUrl(intent.getStringExtra(KEY_URL).toString())
    }

    companion object {
        private const val KEY_URL = "key_url"

        // このstart()メソッドでは、WebViewActivityの遷移処理を行っています。
        fun start(activity: Activity, url: String) {
            // 画面遷移を行うには、ActivityクラスにあるstartActivity()メソッドを呼び出します。
            // その際の引数にURL情報を付加したIntentオブジェクトを入れ、渡します。
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, url))
        }
    }
}