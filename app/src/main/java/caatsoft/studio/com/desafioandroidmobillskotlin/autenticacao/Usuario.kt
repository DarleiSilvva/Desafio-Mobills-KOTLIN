package caatsoft.studio.com.desafioandroidmobillskotlin.autenticacao

/**
 * Darlei Silva 05/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 */
class Usuario {
    var uuid: String? = null
        private set
    var nomeDoUsuario: String? = null
        private set

    constructor() {}
    constructor(uuid: String?, nomeDoUsuario: String?) {
        this.uuid = uuid
        this.nomeDoUsuario = nomeDoUsuario
    }
}