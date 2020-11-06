package caatsoft.studio.com.desafioandroidmobillskotlin.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import caatsoft.studio.com.desafioandroidmobillskotlin.despesas_model.DespesasModel
import caatsoft.studio.com.desafioandroidmobillskotlin.receitas_model.ReceitasModel
import caatsoft.studio.com.desafioandroidmobillskotlin.repositorio.DespesasDatabase
import caatsoft.studio.com.desafioandroidmobillskotlin.repositorio.Repositorio
import java.util.ArrayList

class MainViewModel: ViewModel () {

    val repositorio = Repositorio ()
    fun getLiveDataDespesasDatabase (): MutableLiveData<ArrayList<DespesasModel>> {
        val mutableData = MutableLiveData<ArrayList<DespesasModel>>()
        mutableData.value = repositorio.getDespesaData()
        return mutableData
    }
    fun getLiveDataReceitasDatabase (): MutableLiveData<ArrayList<ReceitasModel>> {
        val mutableData = MutableLiveData<ArrayList<ReceitasModel>>()
        mutableData.value =  repositorio.getReceitasData()
        return mutableData
    }
}