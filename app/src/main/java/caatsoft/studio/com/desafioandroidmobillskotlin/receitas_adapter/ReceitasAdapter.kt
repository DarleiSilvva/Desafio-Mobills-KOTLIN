package caatsoft.studio.com.desafioandroidmobillskotlin.receitas_adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import caatsoft.studio.com.desafioandroidmobillskotlin.R
import caatsoft.studio.com.desafioandroidmobillskotlin.receitas_model.ReceitasModel
import caatsoft.studio.com.desafioandroidmobillskotlin.repositorio.ReceitaDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class ReceitasAdapter(
        private val receitas: ArrayList<ReceitasModel>,
        private val context: Context
) : RecyclerView.Adapter<ReceitasAdapter.ViewHolder>() {
    var excluir: TextView? = null
    var editar: TextView? = null
    var editValor_text: EditText? = null
    var editDescricao_text: EditText? = null
    var editData_text: TextView? = null
    var imageViewBoolean: ImageView? = null
    var millisInString: String? = null
    var aBoolean = false
    var dateFormat: SimpleDateFormat? = null
    var dataTimestamp: Timestamp? = null
    var valor: String? = null
    var descricao: String? = null
    var data: String? = null
    var id: String? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.modelo, parent, false)
        excluir = view.findViewById(R.id.excluir_nome)
        editar = view.findViewById(R.id.editar_nome)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fundo.setBackgroundResource(R.drawable.fundo_receita)
        holder.valor_text.text = java.lang.String.valueOf(receitas[position].valor) + " R$"
        holder.descricao_text.text = receitas[position].descricao
        holder.data_text.text = receitas[position].data
        holder.pago_nome.text = "Recebido"
        holder.pago_nome.textSize = 13.5f
        if (receitas[position].isBooleana) {
            holder.pago_boolean.setImageResource(R.drawable.ic_check_correto)
        } else {
            holder.pago_boolean.setImageResource(R.drawable.ic_check_errado)
        }
        excluir!!.setOnClickListener {
            id = receitas[position].id
            apagar()
        }
        editar!!.setOnClickListener {
            valor = java.lang.String.valueOf(receitas[position].valor)
            descricao = receitas[position].descricao
            data = receitas[position].data
            aBoolean = receitas[position].isBooleana
            id = receitas[position].id
            alterar()
        }
    }

    override fun getItemCount(): Int {
        return receitas.size
    }

    fun apagar() {
        val txtclose: TextView
        val apagar_text: TextView
        val backgroundFundo: RelativeLayout
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.apagar_dados_popup)
        backgroundFundo = dialog.findViewById<View>(R.id.backgroundFundo) as RelativeLayout
        txtclose = dialog.findViewById<View>(R.id.txtclose) as TextView
        apagar_text = dialog.findViewById<View>(R.id.apagar_text) as TextView
        backgroundFundo.setBackgroundResource(R.drawable.fundo_receita)
        apagar_text.setOnClickListener {
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase!!.reference
            val receitas = ReceitaDatabase()
            receitas.id = id
            val uid = FirebaseAuth.getInstance().uid
            databaseReference!!.child("Receita$uid").child(receitas.id.toString()).removeValue()
            Toast.makeText(context, "Excluído", Toast.LENGTH_LONG).show()
        }
        txtclose.setOnClickListener { dialog.dismiss() }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun alterar() {
        val dialog = Dialog(context)
        val txtclose: TextView
        val excluir: TextView
        val editar: TextView
        val pago_nome: TextView
        val backgroundFundo: RelativeLayout
        dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        millisInString = dateFormat!!.format(Date())
        dataTimestamp = Timestamp.valueOf(millisInString)
        dialog.setContentView(R.layout.dados_popup)
        pago_nome = dialog.findViewById<View>(R.id.pago_nome) as TextView
        txtclose = dialog.findViewById<View>(R.id.txtclose) as TextView
        backgroundFundo = dialog.findViewById<View>(R.id.backgroundFundo) as RelativeLayout
        editValor_text = dialog.findViewById<View>(R.id.valor_text) as EditText
        editDescricao_text = dialog.findViewById<View>(R.id.descricao_text) as EditText
        editData_text = dialog.findViewById<View>(R.id.data_text) as TextView
        imageViewBoolean = dialog.findViewById<View>(R.id.pago_boolean) as ImageView
        excluir = dialog.findViewById<View>(R.id.excluir_nome2) as TextView
        editar = dialog.findViewById<View>(R.id.editar_nome2) as TextView
        editValor_text!!.setText(valor)
        editDescricao_text!!.setText(descricao)
        editData_text!!.text = data
        pago_nome.text = "Recebido:"
        if (aBoolean) {
            imageViewBoolean!!.setImageResource(R.drawable.ic_check_correto)
        } else {
            imageViewBoolean!!.setImageResource(R.drawable.ic_check_errado)
        }
        editData_text!!.setOnLongClickListener {
            editData_text!!.text = millisInString
            true
        }
        excluir.setOnClickListener {
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase!!.reference
            val receitas = ReceitaDatabase()
            receitas.id = id
            val uid = FirebaseAuth.getInstance().uid
            databaseReference!!.child("Receita$uid").child(receitas.id.toString()).removeValue()
            Toast.makeText(context, "Excluído", Toast.LENGTH_LONG).show()
            dialog.dismiss()
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
        backgroundFundo.setBackgroundResource(R.drawable.fundo_receita)
        txtclose.setOnClickListener { dialog.dismiss() }
        editar.setOnClickListener {
            firebaseDatabase = FirebaseDatabase.getInstance()
            databaseReference = firebaseDatabase!!.reference
            val receitas = ReceitaDatabase()
            receitas.id = id
            receitas.valor = editValor_text!!.text.toString().toInt()
            receitas.descricao = editDescricao_text!!.text.toString()
            receitas.data = dataTimestamp
            receitas.isRecebido = aBoolean
            val uid = FirebaseAuth.getInstance().uid
            databaseReference!!.child("Receita$uid").child(receitas.id.toString())
                    .setValue(receitas)
            Toast.makeText(context, "Atualizado", Toast.LENGTH_LONG).show()
            //limpar dados
            editValor_text!!.setText("")
            editDescricao_text!!.setText("")
            editData_text!!.text = ""
            aBoolean = false
            imageViewBoolean!!.setImageResource(R.drawable.ic_check_errado)
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var valor_text: TextView
        var descricao_text: TextView
        var data_text: TextView
        var pago_nome: TextView
        var pago_boolean: ImageView
        var fundo: ConstraintLayout

        init {
            valor_text = itemView.findViewById(R.id.valor_text)
            descricao_text = itemView.findViewById(R.id.descricao_text)
            data_text = itemView.findViewById(R.id.data_text)
            pago_nome = itemView.findViewById(R.id.pago_nome)
            pago_boolean = itemView.findViewById(R.id.pago_boolean)
            fundo = itemView.findViewById(R.id.fundo)
        }
    }
}