package com.cht.mybank11m.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cht.mybank11m.data.model.Account
import com.cht.mybank11m.data.model.AccountState
import com.cht.mybank11m.data.network.AccountApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountApi: AccountApi
): ViewModel() {

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadAccounts() {
        accountApi.getAccounts().handleResponse(
            onSuccess = { _accounts.value = it },
            onError = { errorMsg -> _errorMessage.value = errorMsg }
        )
    }

    fun addAccount(account: Account) {
        accountApi.addAccount(account).handleResponse(
            onSuccess = { loadAccounts() },
            onError = {errorMsg -> _errorMessage.value = errorMsg }
        )
    }

    fun deleteAccount(accountId: String) {
        accountApi.deleteAccount(accountId).handleResponse(
            onSuccess = { loadAccounts() },
            onError = {errorMsg -> _errorMessage.value = errorMsg }
        )
    }

    fun updateAccountFully(account: Account) {
        account.id?.let {
            accountApi.updateAccountFully(it, account).handleResponse(
                onSuccess = { loadAccounts() },
                onError = {errorMsg -> _errorMessage.value = errorMsg }
            )
        }
    }

    fun patchAccountStatus(accountId: String, isActive: Boolean) {
        accountApi.patchAccountStatus(accountId,AccountState(isActive)).handleResponse(
            onSuccess = { loadAccounts() },
            onError = {errorMsg -> _errorMessage.value = errorMsg }
        )
    }

    fun <T> Call <T>.handleResponse(
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        this.enqueue(object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val resultBody = response.body()
                if (response.isSuccessful && resultBody != null) {
                    onSuccess(resultBody)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onError(t.message.toString())
            }

        })
    }

}