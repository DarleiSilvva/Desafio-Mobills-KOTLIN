package caatsoft.studio.com.desafioandroidmobillskotlin.repositorio

import androidx.lifecycle.MutableLiveData
import caatsoft.studio.com.desafioandroidmobillskotlin.despesas_model.DespesasModel
import caatsoft.studio.com.desafioandroidmobillskotlin.receitas_model.ReceitasModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Repositorio {
    var uid: String? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var repositorioInstance: Repositorio? = null
    var arrayDespesa = ArrayList<DespesasModel>()
    var arrayReceita = ArrayList<ReceitasModel>()

    fun repositorio(): Repositorio? {
        if (repositorioInstance == null) {
            repositorioInstance = Repositorio()
        }
        return repositorioInstance
    }

    fun getDespesaData(): ArrayList<DespesasModel> {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference
        uid = FirebaseAuth.getInstance().uid
        databaseReference!!.child("Despesa$uid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayDespesa.clear()
                var despesasModel: DespesasModel
                for (data in dataSnapshot.children) {
                    despesasModel = DespesasModel()
                    val descricao = data.child("descricao").value as String?
                    val boolena = data.child("pago").value as Boolean
                    despesasModel.id = data.child("id").value.toString()
                    despesasModel.valor = data.child("valor").value.toString().toInt()
                    despesasModel.descricao = descricao
                    despesasModel.data =
                        "${data.child("data").child("date").value.toString()}/${
                            data.child("data").child("month").value.toString()
                        } | ${
                            data.child("data").child("hours").value.toString()
                        }:${data.child("data").child("minutes").value.toString()}"
                    despesasModel.isBooleana = boolena
                    arrayDespesa.add(despesasModel)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
        return arrayDespesa
    }

    fun getReceitasData(): ArrayList<ReceitasModel> {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference
        uid = FirebaseAuth.getInstance().uid
        databaseReference!!.child("Receita$uid").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayReceita.clear()
                var receitasModel: ReceitasModel
                for (data in dataSnapshot.children) {
                    receitasModel = ReceitasModel()
                    val descricao = data.child("descricao").value as String?
                    val boolena = data.child("recebido").value as Boolean
                    receitasModel.id = data.child("id").value.toString()
                    receitasModel.valor = data.child("valor").value.toString().toInt()
                    receitasModel.descricao = descricao
                    receitasModel.data =
                        "${data.child("data").child("date").value.toString()}/${
                            data.child("data").child("month").value.toString()
                        } | ${
                            data.child("data").child("hours").value.toString()
                        }:${data.child("data").child("minutes").value.toString()}"
                    receitasModel.isBooleana = boolena
                    arrayReceita.add(receitasModel)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
        return arrayReceita
    }
}