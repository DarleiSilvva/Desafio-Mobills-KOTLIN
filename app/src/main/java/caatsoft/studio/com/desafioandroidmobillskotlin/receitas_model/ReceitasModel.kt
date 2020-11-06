package caatsoft.studio.com.desafioandroidmobillskotlin.receitas_model

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class ReceitasModel : Comparable<ReceitasModel> {
    var id: String? = null
    var valor = 0
    var descricao: String? = null
    var data: String? = null
    var isBooleana = false
    override fun compareTo(o: ReceitasModel): Int {
        return o.valor - valor
    }
}