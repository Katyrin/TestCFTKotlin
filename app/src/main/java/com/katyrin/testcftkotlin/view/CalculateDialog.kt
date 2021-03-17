package com.katyrin.testcftkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.katyrin.testcftkotlin.R
import com.katyrin.testcftkotlin.databinding.DialogFragmentBinding
import com.katyrin.testcftkotlin.model.Currency

class CalculateDialog(private val currency: Currency): BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentBinding

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
        binding.negativeButtonDialog.setOnClickListener {
            dismiss()
        }
        binding.positiveButtonDialog.setOnClickListener {
            if (binding.amountInRubles.text.toString() != "") {
                val amountInRubles = binding.amountInRubles.text.toString().toDouble()
                val result = amountInRubles * currency.nominal / currency.value
                val roundResult = String.format("%.2f", result).toDouble()
                val stringResult = "$roundResult ${currency.charCode}"
                binding.result.text =stringResult
            } else {
                binding.amountInRubles.error = getString(R.string.enter_amount)
            }

        }
    }
}