package caatsoft.studio.com.desafioandroidmobillskotlin.repositorio

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class FirebaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}