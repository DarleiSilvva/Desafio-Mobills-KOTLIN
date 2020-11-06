package caatsoft.studio.com.desafioandroidmobillskotlin.estatistica

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import caatsoft.studio.com.desafioandroidmobillskotlin.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class EstatisticaMensalActivity : AppCompatActivity() {
    var pieChart: PieChart? = null
    var colorClassArray = intArrayOf(
        Color.parseColor("#9F3C35"),
        Color.parseColor("#429A46")
    )
    var valorDes = 0
    var valorRes = 0
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var txtDespesa: TextView? = null
    var txtReceita: TextView? = null
    var dataVals = ArrayList<PieEntry>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estatistica)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "   Gráficos"
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))
        toolbar.setLogo(R.drawable.ic_logo)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.verde3))
        setSupportActionBar(toolbar)
        txtDespesa = findViewById<View>(R.id.txtDespesa) as TextView
        txtReceita = findViewById<View>(R.id.txtReceita) as TextView
        val textView = findViewById<View>(R.id.textView) as TextView
        textView.text = "Estatística com os valores do mês atual"
        //FirebaseApp.initializeApp(this)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference
        despesaClasse()
        receitaClasse()
    }

    fun pieChartFun() {
        pieChart = findViewById(R.id.pie_chart)
        val pieDataSet = PieDataSet(dataVals, "")
        pieDataSet.setColors(*colorClassArray)
        val pieData = PieData(pieDataSet)
        pieChart!!.setData(pieData)
        pieChart!!.invalidate()
        pieChart!!.getDescription().isEnabled = false
        pieChart!!.setBackgroundColor(ContextCompat.getColor(this, R.color.verde3))
        pieChart!!.setTransparentCircleRadius(35f)
        pieChart!!.setHoleRadius(30f)
        pieChart!!.setHoleColor(ContextCompat.getColor(this, R.color.verde3))
        pieDataSet.valueTextColor = ContextCompat.getColor(this, R.color.white)
        pieDataSet.valueTextSize = 12f
        val legendPie = pieChart!!.getLegend()
        legendPie.textSize = 20f
        legendPie.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legendPie.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legendPie.orientation = Legend.LegendOrientation.VERTICAL
        legendPie.setDrawInside(false)
        legendPie.textColor = ContextCompat.getColor(this, R.color.white)
    }

    fun despesaClasse() {
        val uid = FirebaseAuth.getInstance().uid
        databaseReference!!.child("Despesa$uid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var x = 0
                val date = Date()
                val cal = Calendar.getInstance()
                cal.time = date
                val month = cal[Calendar.MONTH]
                for (data in dataSnapshot.children) {
                    if (data.child("data").child("month").value.toString().toInt() == month) {
                        x += data.child("valor").value.toString().toInt()
                    }
                }
                txtDespesa!!.text = "$x R$"
                valorDes = x
                dataVals.add(PieEntry(valorDes.toFloat(), "Despesas"))
                pieChartFun()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }

    fun receitaClasse() {
        val uid = FirebaseAuth.getInstance().uid
        databaseReference!!.child("Receita$uid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var x = 0
                val date = Date()
                val cal = Calendar.getInstance()
                cal.time = date
                val month = cal[Calendar.MONTH]
                for (data in dataSnapshot.children) {
                    if (data.child("data").child("month").value.toString().toInt() == month) {
                        x += data.child("valor").value.toString().toInt()
                    }
                }
                txtReceita!!.text = "$x R$"
                valorRes = x
                dataVals.add(PieEntry(valorRes.toFloat(), "Receitas"))
                pieChartFun()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.grafico_gerais) {
            estatistica()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun estatistica() {
        val txtclose: TextView
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grafico_dados_popup)
        txtclose = dialog.findViewById<View>(R.id.txtclose) as TextView
        txtclose.setOnClickListener { dialog.dismiss() }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun estatisticaClick(view: View) {
        if (view.id == R.id.Saldocomtodososvalores) {
            val intent = Intent(applicationContext, EstatisticaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else if (view.id == R.id.Saldocomosvaloresnaopagos) {
            val intent = Intent(applicationContext, EstatisticaNaoPagoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else if (view.id == R.id.Saldocomosvaloresdomesatual) {
            val intent = Intent(applicationContext, EstatisticaMensalActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}