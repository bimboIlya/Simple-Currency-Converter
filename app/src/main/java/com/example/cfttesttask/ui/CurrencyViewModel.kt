package com.example.cfttesttask.ui

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.cfttesttask.R
import com.example.cfttesttask.data.Currency
import com.example.cfttesttask.data.sources.CurrencyRepository
import com.example.cfttesttask.data.sources.DataUpdateException
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(
    private val repo: CurrencyRepository
) : ViewModel() {

    private val _currencyList = MutableLiveData<MutableList<Currency>>(mutableListOf())
    val currencyList: LiveData<List<Currency>>
        get() = _currencyList.map { it.toList() }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _messageResId = MutableLiveData<Int?>(null)
    val messageResId: LiveData<Int?> = _messageResId


    init {
        load(false)
    }

    private fun load(forceUpdate: Boolean) {
        viewModelScope.launch {
            try {
               loadingStarted()
                _currencyList.value = repo.updateDataIfNecessary(forceUpdate)
                    .sortedBy { it.name }
                    .toMutableList()
            } catch (ex: DataUpdateException) {
                showMessage(R.string.error_message)
            } finally {
                loadingEnded()
            }
        }
    }

    fun updateCurrencyListFromNetwork() {
        load(true)
    }

    /**
     * Sets [messageResId] to null so it won't be shown when View gets recreated
     */
    fun messageShown() {
        _messageResId.value = null
    }

    /**
     * Locally saves state of currency after conversion
     */
    fun currencyUpdated(positionInList: Int, foreignValue: Double, rubValue: Double) {
        val currency = _currencyList.value?.get(positionInList)
            ?: throw UninitializedPropertyAccessException("_currencyList.value is empty")
        _currencyList.value?.set(
            positionInList, Currency(
                currency.date,
                currency.id,
                currency.numCode,
                currency.charCode,
                foreignValue,
                currency.name,
                rubValue,
                currency.rubValue,
                currency.previousRubValue
            )
        )
    }

    private fun showMessage(@StringRes stringId: Int) {
        _messageResId.value = stringId
    }

    private fun loadingStarted() {
        _isLoading.value = true
    }

    private fun loadingEnded() {
        _isLoading.value = false
    }
}