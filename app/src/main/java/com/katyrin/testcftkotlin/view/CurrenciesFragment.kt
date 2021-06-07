package com.katyrin.testcftkotlin.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.katyrin.testcftkotlin.R
import com.katyrin.testcftkotlin.databinding.CurrenciesFragmentBinding
import com.katyrin.testcftkotlin.model.Currency
import com.katyrin.testcftkotlin.utils.createAndShow
import com.katyrin.testcftkotlin.viewmodel.AppState
import com.katyrin.testcftkotlin.viewmodel.CurrenciesViewModel
import javax.inject.Inject

class CurrenciesFragment : Fragment(), OnUpdateDataListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: CurrenciesViewModel by viewModels(factoryProducer = { factory })
    private lateinit var binding: CurrenciesFragmentBinding
    private var currencyList: List<Currency> = listOf()
    private var savedRecyclerLayoutState: Parcelable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrenciesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })

        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT)
            viewModel.getSaveStateLiveData()
        } else
            viewModel.getAllCurrencies()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessRemoteRequest -> {
                showRecycler()
                setData(appState.currencies)
                updateLocalData(appState.currencies)
                requireView().createAndShow(
                    getString(R.string.success_remote),
                    length = Snackbar.LENGTH_LONG
                )
            }
            is AppState.SuccessLocalRequest -> {
                showRecycler()
                setData(appState.currencies)
                requireView().createAndShow(
                    getString(R.string.success_local_db),
                    length = Snackbar.LENGTH_LONG
                )
            }
            is AppState.EmptyLocalList -> {
                showRecycler()
                viewModel.getCurrenciesFromRemoteSource()
            }
            is AppState.SuccessSaveData -> {
                showRecycler()
                setData(appState.currencies)
                requireView().createAndShow(
                    getString(R.string.success_local),
                    length = Snackbar.LENGTH_LONG
                )
            }
            is AppState.Loading -> {
                hideRecycler()
            }
            is AppState.Error -> {
                showRecycler()
                requireView().createAndShow(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getCurrenciesFromRemoteSource() },
                    Snackbar.LENGTH_INDEFINITE
                )
            }
            is AppState.SuccessInsert -> {
                requireView().createAndShow(
                    getString(R.string.success_insert),
                    length = Snackbar.LENGTH_LONG
                )
            }
        }
    }

    private fun showRecycler() {
        binding.loadingLayout.isVisible = false
        binding.currenciesRecyclerView.isVisible = true
    }

    private fun hideRecycler() {
        binding.loadingLayout.isVisible = true
        binding.currenciesRecyclerView.isVisible = false
    }

    private fun updateLocalData(currencies: List<Currency>) {
        currencies.map {
            viewModel.saveCurrencyToDB(it)
        }
    }

    private fun setData(currencies: List<Currency>) {
        val sortCurrency = currencies.sortedBy { it.name }
        currencyList = sortCurrency
        setRecyclerLayoutState()
        binding.currenciesRecyclerView.adapter =
            CurrenciesRecyclerViewAdapter(sortCurrency) { currency ->
                CalculateDialog.newInstance(currency, requireActivity().supportFragmentManager)
            }
    }

    private fun setRecyclerLayoutState() {
        if (savedRecyclerLayoutState != null) {
            binding.currenciesRecyclerView.layoutManager?.onRestoreInstanceState(
                savedRecyclerLayoutState
            )
        }
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

    companion object {
        fun newInstance() = CurrenciesFragment()
        private const val BUNDLE_RECYCLER_LAYOUT = "BUNDLE_RECYCLER_LAYOUT"
    }
}