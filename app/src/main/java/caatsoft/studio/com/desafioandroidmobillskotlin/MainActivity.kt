package caatsoft.studio.com.desafioandroidmobillskotlin

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import caatsoft.studio.com.desafioandroidmobillskotlin.despesas_adapter.DespesasAdapter
import caatsoft.studio.com.desafioandroidmobillskotlin.receitas_adapter.ReceitasAdapter
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import caatsoft.studio.com.desafioandroidmobillskotlin.autenticacao.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.recyclerview.widget.LinearLayoutManager
import android.graphics.drawable.ColorDrawable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import caatsoft.studio.com.desafioandroidmobillskotlin.despesas_model.DespesasModel
import caatsoft.studio.com.desafioandroidmobillskotlin.repositorio.DespesasDatabase
import caatsoft.studio.com.desafioandroidmobillskotlin.repositorio.ReceitaDatabase
import caatsoft.studio.com.desafioandroidmobillskotlin.estatistica.EstatisticaActivity
import caatsoft.studio.com.desafioandroidmobillskotlin.estatistica.EstatisticaNaoPagoActivity
import caatsoft.studio.com.desafioandroidmobillskotlin.estatistica.EstatisticaMensalActivity
import caatsoft.studio.com.desafioandroidmobillskotlin.receitas_model.ReceitasModel
import caatsoft.studio.com.desafioandroidmobillskotlin.viewmodels.MainViewModel
import com.google.firebase.database.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class MainActivity : AppCompatActivity() {

    var floatingActionButton: FloatingActionButton? = null
    var dialog: Dialog? = null
    var editValor_text: EditText? = null
    var editDescricao_text: EditText? = null
    var editData_text: TextView? = null
    var imageViewBoolean: ImageView? = null

    //HolderDespesa holderDespesa = null;
    private lateinit var despesasAdapter: DespesasAdapter
    private lateinit var receitasAdapter: ReceitasAdapter
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java)}

    var valorInt = 0
    var descricaoString: String? = null
    var dataTimestamp: Timestamp? = null
    var pagoString = false
    var millisInString: String? = null
    var aBoolean = false
    var dateFormat: SimpleDateFormat? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var recyclerView: RecyclerView? = null
    var identificarTipo = false
    var arrayAdapterDespesa = ArrayList<DespesasModel>()
    var arrayAdapterReceita = ArrayList<ReceitasModel>()
    var b1: Button? = null
    var b2: Button? = null
    var button: Button? = null
    var x = 0
    var uid: String? = null
    var parafixarCadastro = 0
    var settings: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "   D. A. Mobills "
        toolbar.setTitleTextColor(Color.parseColor("#FF000000"))
        toolbar.setLogo(R.drawable.ic_logo)
        toolbar.setBackgroundResource(R.color.verde2)
        setSupportActionBar(toolbar)

        val fixarCadastro = intent.getIntExtra("FIXAR_CADASTRO", 0)
        settings = getSharedPreferences("fixar", MODE_PRIVATE)
        parafixarCadastro = settings!!.getInt("fixarCadastro", 0)
        parafixarCadastro += fixarCadastro
        val editor = settings!!.edit()
        editor.putInt("fixarCadastro", parafixarCadastro)
        editor.commit()
        if (parafixarCadastro == 0) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            finish()
            startActivity(intent)
        }
        dialog = Dialog(this)
        recyclerView = findViewById<View>(R.id.recycleView) as RecyclerView
        floatingActionButton = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton
        //FirebaseApp.initializeApp(this)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        millisInString = dateFormat!!.format(Date())
        dataTimestamp = Timestamp.valueOf(millisInString)
        uid = FirebaseAuth.getInstance().uid
        button = findViewById<View>(R.id.button_Despesas) as Button
        button!!.setTextColor(resources.getColor(R.color.white))
        button!!.setBackgroundResource(R.color.verde3)
        b2 = button
        identificarTipo = true
        adaptadorDespesaClasse()
        floatingActionButton!!.setOnClickListener {
            if (identificarTipo) {
                cadastro(true)
            } else {
                cadastro(false)
            }
        }
    }

    fun tabClick(v: View) {
        button = findViewById<View>(v.id) as Button
        if (v.id == R.id.button_Despesas) {
            adaptadorDespesaClasse()
            tabClickVerificacao(v)
            button!!.setTextColor(resources.getColor(R.color.white))
            button!!.setBackgroundResource(R.color.verde3)
            identificarTipo = true
        }
        if (v.id == R.id.button_Receitas) {
            adaptadorReceitaClasse()
            tabClickVerificacao(v)
            button!!.setTextColor(resources.getColor(R.color.white))
            button!!.setBackgroundResource(R.color.verde3)
            identificarTipo = false
        }
    }

    fun tabClickVerificacao(v: View) {
        if (x % 2 == 0) {
            x = 1
            b1 = findViewById<View>(v.id) as Button
            if (b2 != null) {
                b2!!.setTextColor(resources.getColor(R.color.black))
                b2!!.setBackgroundResource(R.color.verde2)
            }
        } else {
            x = 2
            b2 = findViewById<View>(v.id) as Button
            if (b1 != null) {
                b1!!.setTextColor(resources.getColor(R.color.black))
                b1!!.setBackgroundResource(R.color.verde2)
            }
            if (b1 != null && b2 != null) {
                if (b1!!.id == b2!!.id) {
                    v.setBackgroundResource(R.color.verde2)
                }
            }
        }
    }
    fun adaptadorDespesaClasse() {
        recyclerView!!.layoutManager = LinearLayoutManager(this@MainActivity)
        despesasAdapter = DespesasAdapter(viewModel.getLiveDataDespesasDatabase().value!!, this@MainActivity)
        recyclerView!!.adapter = despesasAdapter
        observeDataDatabase ()
    }

    fun adaptadorReceitaClasse() {
        recyclerView!!.layoutManager = LinearLayoutManager(this@MainActivity)
        receitasAdapter = ReceitasAdapter(viewModel.getLiveDataReceitasDatabase().value!!, this@MainActivity)
        recyclerView!!.adapter = receitasAdapter
        observeDataReceita ()
    }

    fun observeDataDatabase (){
        viewModel.getLiveDataDespesasDatabase().observe(this, Observer {
            despesasAdapter.notifyDataSetChanged()
        })
    }
    fun observeDataReceita (){
        viewModel.getLiveDataReceitasDatabase().observe(this, Observer {
            receitasAdapter.notifyDataSetChanged()
        })
    }

    fun cadastro(tipo: Boolean) {
        val txtclose: TextView
        val pago_nome: TextView
        val backgroundFundo: RelativeLayout
        dialog!!.setContentView(R.layout.inserir_dados_popup)
        txtclose = dialog!!.findViewById<View>(R.id.txtclose) as TextView
        pago_nome = dialog!!.findViewById<View>(R.id.pago_nome) as TextView
        backgroundFundo = dialog!!.findViewById<View>(R.id.backgroundFundo) as RelativeLayout
        editValor_text = dialog!!.findViewById<View>(R.id.valor_text) as EditText
        editDescricao_text = dialog!!.findViewById<View>(R.id.descricao_text) as EditText
        editData_text = dialog!!.findViewById<View>(R.id.data_text) as TextView
        imageViewBoolean = dialog!!.findViewById<View>(R.id.pago_boolean) as ImageView
        editData_text!!.text = millisInString
        if (!identificarTipo) {
            pago_nome.text = "Recebido:"
        }
        imageViewBoolean!!.setOnClickListener {
            if (aBoolean) {
                aBoolean = false
                imageViewBoolean!!.setImageResource(R.drawable.ic_check_errado)
            } else {
                aBoolean = true
                imageViewBoolean!!.setImageResource(R.drawable.ic_check_correto)
            }
        }
        if (tipo) {
            backgroundFundo.setBackgroundResource(R.drawable.fundo_despesa)
        } else {
            backgroundFundo.setBackgroundResource(R.drawable.fundo_receita)
        }
        txtclose.setOnClickListener { dialog!!.dismiss() }
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.show()
    }

    fun opcaoDeSelecaoClick(v: View?) {
        val valorS = editValor_text!!.text.toString()
        val descricaoS = editDescricao_text!!.text.toString()
        if (valorS.isEmpty() || valorS == null || descricaoS.isEmpty() || descricaoS == null) {
            Toast.makeText(baseContext, "Insira os valores", Toast.LENGTH_LONG).show()
        } else {
            valorInt = valorS.toInt()
            descricaoString = descricaoS
            dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            millisInString = dateFormat!!.format(Date())
            dataTimestamp = Timestamp.valueOf(millisInString)
            pagoString = aBoolean
            if (validacao()) {
                if (identificarTipo) {
                    val despesas = DespesasDatabase()
                    despesas.id = UUID.randomUUID().toString()
                    despesas.valor = valorInt
                    despesas.descricao = descricaoString
                    despesas.data = dataTimestamp
                    despesas.isPago = pagoString
                    databaseReference!!.child("Despesa$uid").child(despesas.id.toString())
                        .setValue(despesas)
                    adaptadorDespesaClasse()
                } else {
                    val receita = ReceitaDatabase()
                    receita.id = UUID.randomUUID().toString()
                    receita.valor = valorInt
                    receita.descricao = descricaoString
                    receita.data = dataTimestamp
                    receita.isRecebido = pagoString
                    databaseReference!!.child("Receita$uid").child(receita.id.toString())
                        .setValue(receita)
                    adaptadorReceitaClasse()
                }
                Toast.makeText(this, "Confirmado", Toast.LENGTH_LONG).show()
                limparDados()
                dialog!!.dismiss()
            }
        }
    }

    private fun validacao(): Boolean {
        var x = true
        if (valorInt <= 0) {
            editValor_text!!.error = "Insira um valor acima de 0"
            x = false
        }
        if (descricaoString!!.isEmpty()) {
            editDescricao_text!!.error = "Insira pelo menos um caractere"
            x = false
        }
        return x
    }

    fun limparDados() {
        editValor_text!!.setText("")
        editDescricao_text!!.setText("")
        editData_text!!.text = ""
        aBoolean = false
        imageViewBoolean!!.setImageResource(R.drawable.ic_check_errado)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.grafico_gerais) {
            estatistica()
            return true
        } else if (id == R.id.logout) {
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun estatistica() {
        val txtclose: TextView
        dialog!!.setContentView(R.layout.grafico_dados_popup)
        txtclose = dialog!!.findViewById<View>(R.id.txtclose) as TextView
        txtclose.setOnClickListener { dialog!!.dismiss() }
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.show()
    }

    fun estatisticaClick(view: View) {
        if (view.id == R.id.Saldocomtodososvalores) {
            val intent = Intent(applicationContext, EstatisticaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            dialog!!.dismiss()
            startActivity(intent)
        } else if (view.id == R.id.Saldocomosvaloresnaopagos) {
            val intent = Intent(applicationContext, EstatisticaNaoPagoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            dialog!!.dismiss()
            startActivity(intent)
        } else if (view.id == R.id.Saldocomosvaloresdomesatual) {
            val intent = Intent(applicationContext, EstatisticaMensalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            dialog!!.dismiss()
            startActivity(intent)
        }
    }

    fun logout() {
        val txtclose: TextView
        val logout_text: TextView
        dialog!!.setContentView(R.layout.log_out_dados_popup)
        txtclose = dialog!!.findViewById<View>(R.id.txtclose) as TextView
        logout_text = dialog!!.findViewById<View>(R.id.logout_text) as TextView
        logout_text.setOnClickListener {
            parafixarCadastro = settings!!.getInt("fixarCadastro", 0)
            parafixarCadastro = 0
            val editor = settings!!.edit()
            editor.putInt("fixarCadastro", parafixarCadastro)
            editor.commit()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            finish()
            startActivity(intent)
        }
        txtclose.setOnClickListener { dialog!!.dismiss() }
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.show()
    }
}