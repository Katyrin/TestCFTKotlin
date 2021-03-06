package com.katyrin.testcftkotlin.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.testcftkotlin.databinding.RvCurrencyItemBinding
import com.katyrin.testcftkotlin.model.Currency

class CurrenciesRecyclerViewAdapter(
    private val currencies: List<Currency>,
    private val onClickListener: (currency: Currency) -> Unit
) : RecyclerView.Adapter<CurrenciesRecyclerViewAdapter.CurrenciesViewHolder>() {

    inner class CurrenciesViewHolder(private val itemBinding: RvCurrencyItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(currency: Currency) {
            val title = "${currency.name} (${currency.numCode})"
            itemBinding.currencyName.text = title
            itemBinding.currencyCharCode.text = currency.charCode
            itemBinding.currencyValue.text = currency.value.toString()
            itemBinding.currencyNominal.text = currency.nominal.toString()
            itemBinding.root.setOnClickListener {
                onClickListener(currency)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = RvCurrencyItemBinding.inflate(layoutInflater, parent, false)
        return CurrenciesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    override fun getItemCount(): Int = currencies.size
}