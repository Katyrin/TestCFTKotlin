package com.katyrin.testcftkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.katyrin.testcftkotlin.R
import com.katyrin.testcftkotlin.databinding.DialogFragmentBinding
import com.katyrin.testcftkotlin.model.Currency

class CalculateDialog : BottomSheetDialogFragment() {

    companion object {
        private const val SELECTED_CURRENCY_RATE = "SELECTED_CURRENCY_RATE"
        private const val CALCULATE_DIALOG = "CALCULATE_DIALOG"

        fun newInstance(currency: Currency, fragmentManager: FragmentManager) =
            CalculateDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(SELECTED_CURRENCY_RATE, currency)
                }
                show(fragmentManager, CALCULATE_DIALOG)
            }
    }

    private lateinit var binding: DialogFragmentBinding
    private lateinit var currency: Currency

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currency = it.getParcelable(SELECTED_CURRENCY_RATE)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.result.text = currency.charCode
        binding.negativeButtonDialog.setOnClickListener { dismiss() }
        binding.positiveButtonDialog.setOnClickListener {
            if (binding.amountInRubles.text.toString() != "") {
                binding.result.text = countResult()
            } else {
                binding.amountInRubles.error = getString(R.string.enter_amount)
            }
        }
    }

    private fun countResult() : String {
        val amountInRubles = binding.amountInRubles.text.toString().toDouble()
        val result = amountInRubles * currency.nominal / currency.value
        val roundResult = String.format("%.2f", result).toDouble()
        return "$roundResult ${currency.charCode}"
    }
}