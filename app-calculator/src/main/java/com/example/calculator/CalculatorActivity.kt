package com.example.calculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculator.adapter.HistoryAdapter
import com.example.calculator.databinding.ActivityCalculatorBinding
import com.example.calculator.model.HistoryEntry

class CalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var viewModel: CalculatorViewModel
    private lateinit var historyAdapter: HistoryAdapter
    private var isSciPanelVisible = false
    private var isHistoryVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]

        setupGridButtons()
        setupScientificButtons()
        setupToolbar()
        setupHistory()
        setupObservers()
    }

    private fun setupGridButtons() {
        binding.panelBasic.btn0.setOnClickListener { viewModel.onDigit("0") }
        binding.panelBasic.btn1.setOnClickListener { viewModel.onDigit("1") }
        binding.panelBasic.btn2.setOnClickListener { viewModel.onDigit("2") }
        binding.panelBasic.btn3.setOnClickListener { viewModel.onDigit("3") }
        binding.panelBasic.btn4.setOnClickListener { viewModel.onDigit("4") }
        binding.panelBasic.btn5.setOnClickListener { viewModel.onDigit("5") }
        binding.panelBasic.btn6.setOnClickListener { viewModel.onDigit("6") }
        binding.panelBasic.btn7.setOnClickListener { viewModel.onDigit("7") }
        binding.panelBasic.btn8.setOnClickListener { viewModel.onDigit("8") }
        binding.panelBasic.btn9.setOnClickListener { viewModel.onDigit("9") }

        binding.panelBasic.btnAdd.setOnClickListener { viewModel.onOperator("+") }
        binding.panelBasic.btnSubtract.setOnClickListener { viewModel.onOperator("-") }
        binding.panelBasic.btnMultiply.setOnClickListener { viewModel.onOperator("×") }
        binding.panelBasic.btnDivide.setOnClickListener { viewModel.onOperator("÷") }

        binding.panelBasic.btnClear.setOnClickListener { viewModel.onClear() }
        binding.panelBasic.btnEquals.setOnClickListener { viewModel.onEquals() }
        binding.panelBasic.btnDecimal.setOnClickListener { viewModel.onDecimal() }
        binding.panelBasic.btnNegate.setOnClickListener { viewModel.onOperator("-") }
        binding.panelBasic.btnPercent.setOnClickListener { viewModel.onPercent() }
        binding.panelBasic.btnParen.setOnClickListener { viewModel.onParentheses() }
    }

    private fun setupScientificButtons() {
        with(binding.panelScientific) {
            btnSin.setOnClickListener { viewModel.onFunction("sin") }
            btnCos.setOnClickListener { viewModel.onFunction("cos") }
            btnTan.setOnClickListener { viewModel.onFunction("tan") }
            btnLog.setOnClickListener { viewModel.onFunction("log") }
            btnLn.setOnClickListener { viewModel.onFunction("ln") }
            btnSqrt.setOnClickListener { viewModel.onFunction("sqrt") }
            btnSquare.setOnClickListener { viewModel.onFunction("square") }
            btnCube.setOnClickListener { viewModel.onFunction("cube") }
            btnPower.setOnClickListener { viewModel.onPower() }
            btnFact.setOnClickListener { viewModel.onFactorial() }
            btnPi.setOnClickListener { viewModel.onConstant("π") }
            btnE.setOnClickListener { viewModel.onConstant("e") }
            btnDeg.setOnClickListener {
                viewModel.toggleAngleMode()
                updateAngleButton()
            }
            btnInv.setOnClickListener {
                btnSin.text = if (btnSin.text == "sin") "asin" else "sin"
                btnCos.text = if (btnCos.text == "cos") "acos" else "cos"
                btnTan.text = if (btnTan.text == "tan") "atan" else "tan"
            }
            btnParen.setOnClickListener { viewModel.onParentheses() }
        }
    }

    private fun setupToolbar() {
        binding.btnToggleSci.setOnClickListener {
            isSciPanelVisible = !isSciPanelVisible
            binding.panelScientific.root.visibility = if (isSciPanelVisible) View.VISIBLE else View.GONE
        }

        binding.btnToggleHistory.setOnClickListener {
            isHistoryVisible = !isHistoryVisible
            binding.panelHistory.root.visibility = if (isHistoryVisible) View.VISIBLE else View.GONE
        }

        binding.panelHistory.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun setupHistory() {
        historyAdapter = HistoryAdapter { entry ->
            viewModel.loadFromHistory(entry)
            isHistoryVisible = false
            binding.panelHistory.root.visibility = View.GONE
        }
        binding.panelHistory.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@CalculatorActivity)
            adapter = historyAdapter
        }
    }

    private fun setupObservers() {
        viewModel.expressionText.observe(this, Observer { text ->
            binding.tvFormula.text = text
        })
        viewModel.resultText.observe(this, Observer { text ->
            binding.tvResult.text = text
        })
        viewModel.errorText.observe(this, Observer { error ->
            if (error != null) {
                binding.tvError.text = error
                binding.tvError.visibility = View.VISIBLE
                binding.tvResult.text = ""
            } else {
                binding.tvError.visibility = View.GONE
            }
        })
        viewModel.history.observe(this, Observer { list ->
            historyAdapter.submitList(list)
            binding.panelHistory.tvEmptyHistory.visibility =
                if (list.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun updateAngleButton() {
        binding.panelScientific.btnDeg.text =
            if (viewModel.isDegreeMode()) "Deg" else "Rad"
    }
}
