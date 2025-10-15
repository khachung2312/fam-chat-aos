package com.example.famchat.activity.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.famchat.R
import com.example.famchat.activity.main.adapter.ContactsAdapter
import com.example.famchat.activity.main.model.Contact
import com.example.famchat.config.ContactItem
import com.example.famchat.databinding.FcFragmentContactsBinding

class ContactsFragment : Fragment() {
    private var _binding: FcFragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FcFragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contacts = listOf(
            Contact(
                "An",
                "Online",
                "Life is beautiful",
                R.drawable.fc_ic_user, ""
            ),
            Contact("Anh", "Offline", "Be your own hero", R.drawable.fc_ic_user ),
            Contact("Binh", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Bao", "Online", "Make yourself proud", R.drawable.fc_ic_user),
            Contact("Chi", "Online", "Flowers are beautiful", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),
            Contact("Cuong", "Offline", "Keep working", R.drawable.fc_ic_user),


        )

        val items = buildContactListWithHeaders(contacts)
        binding.recyclerContacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ContactsAdapter(items)
        }
    }

    private fun buildContactListWithHeaders(contacts: List<Contact>): List<ContactItem> {
        val sorted = contacts.sortedBy { it.name.lowercase() }
        val result = mutableListOf<ContactItem>()
        var lastHeader: Char? = null

        for (c in sorted) {
            val firstLetter = c.name.first().uppercaseChar()
            if (firstLetter != lastHeader) {
                result.add(ContactItem.Header(firstLetter.toString()))
                lastHeader = firstLetter
            }
            result.add(ContactItem.Person(c))
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


