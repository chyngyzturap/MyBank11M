package com.cht.mybank11m.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.cht.mybank11m.R
import com.cht.mybank11m.data.model.Account

class AccountAdapter(
    val onDelete: (String) -> Unit,
    val onEdit: (Account) -> Unit,
    val onStatusToggle: (String, Boolean) -> Unit
): RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private val items = mutableListOf<Account>()

    fun submitList(data: List<Account>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class AccountViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(account: Account) = with(itemView) {
            findViewById<TextView>(R.id.tv_name).text = account.name
            findViewById<TextView>(R.id.tv_balance).text = "${account.balance} ${account.currency}"
            findViewById<Button>(R.id.btn_edit).setOnClickListener {
                onEdit(account)
            }
            findViewById<Button>(R.id.btn_delete).setOnClickListener {
                    account.id?.let { onDelete(it) }
            }
            findViewById<SwitchCompat>(R.id.switch_active).apply {
                isChecked = account.isActive
                setOnCheckedChangeListener { buttonView, isChecked ->
                    account.id?.let{onStatusToggle(it, isChecked)}
                }
            }
        }
    }
}