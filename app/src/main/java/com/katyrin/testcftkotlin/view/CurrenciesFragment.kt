package com.katyrin.testcftkotlin.view

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.katyrin.testcftkotlin.R
import com.katyrin.testcftkotlin.databinding.CurrenciesFragmentBinding
import com.katyrin.testcftkotlin.model.Currency
import com.katyrin.testcftkotlin.viewmodel.AppState
import com.katyrin.testcftkotlin.viewmodel.CurrenciesViewModel

class CurrenciesFragment : Fragment(), MainActivity.OnUpdateDataListener {

    companion object {
        fun newInstance() = CurrenciesFragment()
        private const val BUNDLE_RECYCLER_LAYOUT = "BUNDLE_RECYCLER_LAYOUT"
        private const val CALCULATE_DIALOG = "CALCULATE_DIALOG"
    }

    private val viewModel: CurrenciesViewModel by lazy {
        ViewModelProvider(this).get(CurrenciesViewModel::class.java)
    }
    private lateinit var binding: CurrenciesFragmentBinding
    private var currencyList: List<Currency> = listOf()
    private var savedRecyclerLayoutState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrenciesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })
        if (savedInstanceState != null) {
            savedRecyclerLayoutState =
                savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT)
            viewModel.getSaveStateLiveData()
        } else
            viewModel.getAllCurrencies()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessRemoteQuery -> {
                binding.loadingLayout.visibility = View.GONE
                binding.currenciesRecyclerView.visibility = View.VISIBLE
                setData(appState.currencies)
                updateLocalData(appState.currencies)
                currencyList = appState.currencies
                requireView().createAndShow(
                    getString(R.string.success_remote),
                    length = Snackbar.LENGTH_LONG)
            }
            is AppState.SuccessLocalQuery -> {
                binding.loadingLayout.visibility = View.GONE
                binding.currenciesRecyclerView.visibility = View.VISIBLE
                if (appState.currencies.isEmpty()) {
                    viewModel.getCurrenciesFromRemoteSource()
                } else {
                    setData(appState.currencies)
                    currencyList = appState.currencies
                    requireView().createAndShow(
                        getString(R.string.success_local_db),
                        length = Snackbar.LENGTH_LONG)
                }
            }
            is AppState.SuccessSaveData -> {
                binding.loadingLayout.visibility = View.GONE
                binding.currenciesRecyclerView.visibility = View.VISIBLE
                setData(appState.currencies)
                currencyList = appState.currencies
                requireView().createAndShow(
                    getString(R.string.success_local),
                    length = Snackbar.LENGTH_LONG)
            }
            is AppState.Loading -> {
                binding.currenciesRecyclerView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                requireView().createAndShow(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getCurrenciesFromRemoteSource() },
                    Snackbar.LENGTH_INDEFINITE
                )
            }
        }
    }

    private fun updateLocalData(currencies: List<Currency>) {
        currencies.map {
            viewModel.saveCurrencyToDB(it)
        }
    }

    private fun setData(currencies: List<Currency>) {
        val sortCurrency = currencies.sortedBy { it.name }
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.currenciesRecyclerView.layoutManager = layoutManager
        if (savedRecyclerLayoutState != null) {
            binding.currenciesRecyclerView.layoutManager
                ?.onRestoreInstanceState(savedRecyclerLayoutState)
        }
        binding.currenciesRecyclerView.adapter =
            CurrenciesRecyclerViewAdapter(sortCurrency, object : CurrencyOnClickListener {
                override fun onCurrencyClicked(currency: Currency) {
                    CalculateDialog(currency).show(
                        requireActivity().supportFragmentManager,
                        CALCULATE_DIALOG
                    )
                }
            })
    }

    private fun View.createAndShow(
        text: String, actionText: String = "",
        action: ((View) -> Unit)? = null,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length)
            .also {
                if (action != null) it.setAction(actionText, action)
            }.show()
    }

    override fun updateData() {
        viewModel.getCurrenciesFromRemoteSource()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            BUNDLE_RECYCLER_LAYOUT,
            binding.currenciesRecyclerView.layoutManager?.onSaveInstanceState()
        )
        viewModel.saveState(currencyList)
    }
}