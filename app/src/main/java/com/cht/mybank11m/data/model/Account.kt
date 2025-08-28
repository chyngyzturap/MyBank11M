package com.cht.mybank11m.data.model

data class Account (
    val id: String? = null,
    val name: String,
    val balance: Int,
    val currency: String,
    val isActive: Boolean
)