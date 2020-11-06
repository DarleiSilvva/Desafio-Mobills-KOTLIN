package caatsoft.studio.com.desafioandroidmobillskotlin.autenticacao

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import android.os.Bundle
import caatsoft.studio.com.desafioandroidmobillskotlin.R
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import android.content.Intent
import android.view.View
import android.widget.Button
import caatsoft.studio.com.desafioandroidmobillskotlin.MainActivity
import com.google.android.gms.tasks.OnFailureListener
import caatsoft.studio.com.desafioandroidmobillskotlin.autenticacao.CadastroActivity

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class LoginActivity : AppCompatActivity() {
    private var mEditEmail: EditText? = null
    private var mEditPassword: EditText? = null
    private var mBtnEnter: Button? = null
    private var mTxtAccount: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mEditEmail = findViewById(R.id.edit_email)
        mEditPassword = findViewById(R.id.edit_password)
        mBtnEnter = findViewById(R.id.btn_enter)
        mTxtAccount = findViewById(R.id.txt_account)
        mBtnEnter!!.setOnClickListener(View.OnClickListener {
            val email = mEditEmail!!.getText().toString()
            val password = mEditPassword!!.getText().toString()
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Nome, senha e email devem ser preenchidos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("FIXAR_CADASTRO", 1)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email ou senha inválido!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .addOnFailureListener { }
        })
        mTxtAccount!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, CadastroActivity::class.java)
            startActivity(intent)
        })
    }
}