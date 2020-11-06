package caatsoft.studio.com.desafioandroidmobillskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import caatsoft.studio.com.desafioandroidmobillskotlin.R
import caatsoft.studio.com.desafioandroidmobillskotlin.InicioActivity.LogoLauncher
import android.content.Intent
import android.view.Window
import android.widget.ImageView
import caatsoft.studio.com.desafioandroidmobillskotlin.MainActivity

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class InicioActivity : AppCompatActivity() {
    private val SLEEP_TIMER = 3
    var cont = 0
    var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.inicio_do_app)
        supportActionBar!!.hide()
        val logoLauncher = LogoLauncher()
        logoLauncher.start()
    }

    private inner class LogoLauncher : Thread() {
        override fun run() {
            try {
                sleep(1000 * SLEEP_TIMER.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val intent = Intent(this@InicioActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}