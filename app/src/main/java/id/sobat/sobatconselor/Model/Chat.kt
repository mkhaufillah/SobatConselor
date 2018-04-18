package id.sobat.sobatconselor.Model

import java.util.Date

open class Chat {
    var idUser: String? = null
    var idCons: String? = null
    var date: Date? = null
    var name: String? = null
    var nickname: String? = null
    var lastChat: String? = null
    var photo: String? = null
    var avatar: Int? = null
    var unreadUser: Int? = null
    var unreadCons: Int? = null
}