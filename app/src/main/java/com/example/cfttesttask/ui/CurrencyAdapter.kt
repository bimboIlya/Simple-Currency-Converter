package com.example.cfttesttask.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cfttesttask.data.Currency
import com.example.cfttesttask.databinding.ItemCurrencyBinding
import com.example.cfttesttask.ui.util.clearFocusAndHideKeyboard
import java.util.*

class CurrencyAdapter(
    private val viewModel: CurrencyViewModel
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {
    private var dataList = listOf<Currency>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurrencyBinding.inflate(inflater, parent, false)

        return CurrencyViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun updateList(newList: List<Currency>) {
        dataList = newList
        notifyDataSetChanged()
    }


    class CurrencyViewHolder(
        private val binding: ItemCurrencyBinding,
        private val viewmodel: CurrencyViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currency: Currency) {
            val format = "%1$,.2f"

            with(binding) {
                this.currency = currency
                this.format = format

                setUpConversion(foreignCurrInput, rubCurrInput)

                executePendingBindings()
            }
        }

        private fun setUpConversion(foreign: EditText, rub: EditText) {
            val actionHideKeyboardListener = getActionHideKeyboardListener()

            rub.setOnEditorActionListener(actionHideKeyboardListener)
            rub.addTextChangedListener(getConverterTextWatcher(rub, foreign, false))

            foreign.setOnEditorActionListener(actionHideKeyboardListener)
            foreign.addTextChangedListener(getConverterTextWatcher(foreign, rub, true))
        }

        private fun getActionHideKeyboardListener(): TextView.OnEditorActionListener? {
            return TextView.OnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    view.clearFocusAndHideKeyboard()
                    true
                } else {
                    false
                }
            }
        }

        private fun getConverterTextWatcher(
            viewToAttach: EditText,
            viewToDeliver: EditText,
            toRubles: Boolean
        ): TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // VERY IMPORTANT CHECK; otherwise would cause infinite cycle and stack overflow
                if (viewToAttach.isFocused) {

                    val input = s.toString()
                    if (input.isBlank()) {
                        viewToDeliver.setText("")
                        return
                    }

                    val currency = binding.currency!!
                    val format = binding.format!!
                    val amount = input.toDouble()

                    val convertedNum =
                        getConvertedCurrencyAndSaveItsState(toRubles, amount, currency)

                    // set other edittext's text to converted number
                    val convertedString =
                        String.format(Locale.getDefault(), format, convertedNum)
                    viewToDeliver.setText(convertedString)
                }
            }
        }

        private fun getConvertedCurrencyAndSaveItsState(
            toRubles: Boolean,
            amount: Double,
            currency: Currency
        ): Double =
            if (toRubles) {
                Currency.convertToRubles(amount, currency)
                    .also { viewmodel.currencyUpdated(adapterPosition, amount, it) }
            } else {
                Currency.convertFromRubles(amount, currency)
                    .also { viewmodel.currencyUpdated(adapterPosition, it, amount) }
            }
    }
}
