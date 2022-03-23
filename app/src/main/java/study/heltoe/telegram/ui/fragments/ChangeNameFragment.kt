package study.heltoe.telegram.ui.fragments

import android.view.*
import kotlinx.android.synthetic.main.fragment_change_name.*
import study.heltoe.telegram.R
import study.heltoe.telegram.utilits.*

class ChangeNameFragment : BaseFragment(R.layout.fragment_change_name) {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        val fullnameList = USER.fullname.split(" ")
        if (fullnameList.isNotEmpty()) {
            settings_input_name.setText(fullnameList[0])
            if (fullnameList.size > 1) {
                settings_input_surname.setText(fullnameList[1])
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> changeName()
        }
        return true
    }

    private fun changeName() {
        val name = settings_input_name.text.toString()
        val surname = settings_input_surname.text.toString()

        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullName = "$name $surname"
            REF_DB_ROOT
                .child(NODE_USERS)
                .child(UID)
                .child(CHILD_FULLNAME)
                .setValue(fullName)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.toast_data_update))
                        USER.fullname = fullName
                        fragmentManager?.popBackStack()
                    } else showToast(it.exception?.message.toString())
                }
        }
    }
}