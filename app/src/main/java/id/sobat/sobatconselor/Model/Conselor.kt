package id.sobat.sobatconselor.Model

import com.google.firebase.firestore.GeoPoint
import java.util.Date

open class Conselor {
    var name: String? = null
    var point: Int? = null
    var photo: String? = null
    var cover: String? = null
    var client: Int? = null
    var description: String? = null
    var email: String? = null
    var location: GeoPoint? = null
    var birth: Date? = null
    var date: Date? = null
}