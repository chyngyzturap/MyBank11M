package com.cht.mybank11m.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cht.mybank11m.R
import com.cht.mybank11m.data.model.Account
import com.cht.mybank11m.databinding.ActivityMainBinding
import com.cht.mybank11m.ui.adapter.AccountAdapter
import com.cht.mybank11m.ui.viewmodel.AccountsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AccountAdapter
    private val viewModel: AccountsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        initClicks()
        subscribeToLiveData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }

    private fun initAdapter() = with(binding) {
        adapter = AccountAdapter(
            onDelete = { id ->
                viewModel.deleteAccount(id)
            },
            onEdit = { account ->
                showEditDialog(account)
            },
            onStatusToggle = { id, isChecked ->
                viewModel.patchAccountStatus(id, isChecked)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    private fun initClicks() = with(binding) {
        btnAdd.setOnClickListener { showAddDialog() }
    }

    private fun subscribeToLiveData(){
        viewModel.accounts.observe(this) {
            adapter.submitList(it)
        }

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_account, null)
        with(dialogView){
            val nameInput = findViewById<EditText>(R.id.etName)
            val balanceInput = findViewById<EditText>(R.id.etBalance)
            val currencyInput = findViewById<EditText>(R.id.etCurrency)

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Добавить счет")
                .setView(this)
                .setPositiveButton("Создать счет"){_,_, ->
                    val account = Account(
                        name = nameInput.text.toString(),
                        balance = balanceInput.text.toString().toInt(),
                        currency = currencyInput.text.toString(),
                        isActive = true
                    )
                    viewModel.addAccount(account)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun showEditDialog(account: Account){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_account, null)
        with(dialogView){
            val nameInput = findViewById<EditText>(R.id.etName)
            val balanceInput = findViewById<EditText>(R.id.etBalance)
            val currencyInput = findViewById<EditText>(R.id.etCurrency)

            nameInput.setText(account.name)
            balanceInput.setText(account.balance.toString())
            currencyInput.setText(account.currency)

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Редактировать счет")
                .setView(this)
                .setPositiveButton(getString(R.string.edit_account)){_,_, ->

                    val updatedAccount = account.copy(
                        name = nameInput.text.toString(),
                        balance = balanceInput.text.toString().toInt(),
                        currency = currencyInput.text.toString(),
                    )
                    viewModel.updateAccountFully(updatedAccount)
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }


}