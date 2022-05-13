package uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.screens.main

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.mobilebankingapp.R
import uz.targetsoftwaredevelopment.mobilebankingapp.data.entities.ContactData
import uz.targetsoftwaredevelopment.mobilebankingapp.databinding.ScreenContactsBinding
import uz.targetsoftwaredevelopment.mobilebankingapp.presentation.ui.adapter.ContactsAdapter
import uz.targetsoftwaredevelopment.mobilebankingapp.utils.scope
import java.lang.Boolean
import kotlin.String
import kotlin.getValue
import kotlin.lazy

@AndroidEntryPoint
class ContactsScreen : Fragment(R.layout.screen_contacts) {
    private val binding by viewBinding(ScreenContactsBinding::bind)
    private val contactsList = ArrayList<ContactData>()
    private val adapter by lazy { ContactsAdapter(contactsList) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)
        getContacts()

        rvContacts.adapter = adapter
        rvContacts.layoutManager = LinearLayoutManager(requireContext())

        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val cursor: Cursor = requireActivity().contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )!!
        while (cursor.moveToNext()) {
            val contactId: String = cursor.getString(
                cursor.getColumnIndex(
                    ContactsContract.Contacts._ID
                )
            )
            val hasPhone: String =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
            if (Boolean.parseBoolean(hasPhone)) {
                // You know it has a number so now query it like this
                val phones: Cursor = requireActivity().contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null
                )!!
                while (phones.moveToNext()) {
                    val fullName: String =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                    val phoneNumber: String =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    contactsList.add(ContactData(fullName, phoneNumber))
                }
                adapter.notifyDataSetChanged()
                phones.close()
            }
            val emails: Cursor = requireActivity().contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
                null,
                null
            )!!
            while (emails.moveToNext()) {
                // This would allow you get several email addresses
                val emailAddress: String = emails.getString(
                    emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                )
            }
            emails.close()
        }
        cursor.close()
    }
}