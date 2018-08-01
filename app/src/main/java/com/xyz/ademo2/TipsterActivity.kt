package com.xyz.ademo2

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*


class TipsterActivity : Activity(){

    private lateinit var txtAmount: EditText

    private lateinit var txtPeople : EditText

    private lateinit var txtTipOther : EditText

    private lateinit var rdoGroupTips: RadioGroup

    private lateinit var btnCalculate : Button

    private lateinit var btnReset : Button

    private lateinit var txtTipAmount : TextView

    private lateinit var txtTotalToPay : TextView

    private lateinit var txtTipPerPerson : TextView

    private var radioChechedId : Int = -1

    private lateinit var mLogic : NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        txtAmount = findViewById(R.id.txtAmount)
        txtAmount.requestFocus()

        txtPeople = findViewById(R.id.txtPeople)
        txtTipOther = findViewById(R.id.txtTipOther)

        rdoGroupTips = findViewById(R.id.RedioGroupTips)

        btnCalculate = findViewById(R.id.btnCalculate)
        btnCalculate.isEnabled = false

        btnReset = findViewById(R.id.btnReset)

        txtTipAmount = findViewById(R.id.txtTipAmount)
        txtTotalToPay = findViewById(R.id.txtTotalToPay)
        txtTipPerPerson = findViewById(R.id.txtTipPerPerson)

        txtTipOther.isEnabled = false

        rdoGroupTips.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.radioFifteen||checkedId == R.id.radioTwenty){
                txtTipOther.isEnabled = false
                btnCalculate.isEnabled = txtAmount.text.length > 0 && txtPeople.text.length > 0
            }
            if(checkedId == R.id.radioOther){
                txtTipOther.isEnabled = true

                txtTipOther.requestFocus()
                btnCalculate.isEnabled = txtAmount.text.length > 0 &&txtPeople.text.length >0 && txtTipOther.text.length > 0
            }
            radioChechedId = checkedId
        }

        val mKeyListener = View.OnKeyListener { v, keyCode, event ->
            when (v.id) {
                R.id.txtAmount,
                R.id.txtPeople -> btnCalculate.isEnabled = txtAmount.text.length > 0 && txtPeople.text.length > 0
                R.id.txtTipOther -> btnCalculate.isEnabled = (txtAmount.text.length > 0
                        && txtPeople.text.length > 0
                        && txtTipOther.text.length > 0)
            }
            false }

        val mClickListener = View.OnClickListener { v -> if(v.id == R.id.btnCalculate){
            calculate()
        }else{
            reset()
        } }

        txtAmount.setOnKeyListener(mKeyListener)
        txtPeople.setOnKeyListener(mKeyListener)
        txtTipOther.setOnKeyListener(mKeyListener)

        btnCalculate.setOnClickListener(mClickListener)
        btnReset.setOnClickListener(mClickListener)


    }

    private fun reset() {
        txtTipAmount.text = ""
        txtTotalToPay.text = ""
        txtTipPerPerson.text = ""
        txtAmount.setText("")
        txtPeople.setText("")
        txtTipOther.setText("")
        rdoGroupTips.clearCheck()
        rdoGroupTips.check(R.id.radioFifteen)
        txtAmount.requestFocus()
    }

    private fun calculate() {
        val billAmount = txtAmount.text.toString().toDouble()
        val totalPeople = txtPeople.text.toString().toDouble()
        var percentage : Double = 0.0
        var isError = false
        if(billAmount < 1){
            showErrorAlert("总账单错误，无法计算",R.id.txtAmount)
            isError = true
        }
        if(totalPeople <1){
            showErrorAlert("人数错误，无法计算",R.id.txtPeople)
            isError = true
        }
        when(radioChechedId){
            -1 -> radioChechedId = rdoGroupTips.checkedRadioButtonId
            R.id.radioFifteen -> percentage = 15.00
            R.id.radioTwenty -> percentage = 20.00
            R.id.radioOther -> {percentage = txtTipOther.text.toString().toDouble()
                if (percentage < 0.0){
                    showErrorAlert("百分比输入错误！",R.id.radioOther)
                    isError = true
                }
            }
        }

        if(!isError){
            val tipAmount = (billAmount*percentage)/100
            val totalToPay = billAmount + tipAmount
            val perPersonToPay = totalToPay / totalPeople
            println("1: $tipAmount  2：$totalToPay  3：$perPersonToPay")
            txtTipAmount.text = tipAmount.toString()
            txtTotalToPay.text = totalToPay.toString()
            txtTipPerPerson.text = perPersonToPay.toString()
        }
    }

    private fun showErrorAlert(errorMsg : String, id: Int) {
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(errorMsg)
                .setNeutralButton("Close",DialogInterface.OnClickListener { dialog, which -> findViewById<View>(id).requestFocus() })
                .show()
    }

}