package study.heltoe.telegram.ui.fragments

import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_enter_code.*
import study.heltoe.telegram.MainActivity
import study.heltoe.telegram.R
import study.heltoe.telegram.activities.RegisterActivity
import study.heltoe.telegram.models.User
import study.heltoe.telegram.utilits.*

class EnterCodeFragment(val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {
    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) enterCode()
        })
    }

    private fun updateUserInfo (user: User) {
        REF_DB_ROOT
            .child(NODE_USERS)
            .child(user.id)
            .updateChildren(user.toMap())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Добро пожаловать")
                    (activity as RegisterActivity).replaceActivity(MainActivity())
                } else showToast(it.exception?.message.toString())
            }
            .addOnFailureListener { showToast(it.message.toString()) }
    }

    private fun enterCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH
            .signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val uid = AUTH.currentUser?.uid.toString()
                    val user = User(uid, phoneNumber, uid)
                    updateUserInfo(user)
                } else showToast(it.exception?.message.toString())
            }
            .addOnFailureListener { showToast(it.message.toString()) }
    }
}