package caatsoft.studio.com.desafioandroidmobillskotlin.despesas_model

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class DespesasModel : Comparable<DespesasModel> {
    var id: String? = null
    var valor = 0
    var descricao: String? = null
    var data: String? = null
    var isBooleana = false
    override fun compareTo(o: DespesasModel): Int {
        return o.valor - valor
    }
}