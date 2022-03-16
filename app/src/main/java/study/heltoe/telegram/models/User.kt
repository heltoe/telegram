package study.heltoe.telegram.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val id: String, val phone: String, val username: String) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "phone" to phone,
            "username" to username,
        )
    }
}