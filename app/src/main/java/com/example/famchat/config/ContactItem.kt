package com.example.famchat.config

import com.example.famchat.activity.main.model.Contact

sealed class ContactItem {
    data class Header(val letter: String) : ContactItem()
    data class Person(val contact: Contact) : ContactItem()
}

enum class CallType {
    INCOMING,   // Gọi đến
    OUTGOING,   // Gọi đi
    MISSED      // Cuộc gọi nhỡ
}