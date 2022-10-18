package com.example.cyclopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_report_screen.*

class NewReportScreen : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var hazChoices:MutableList<String> = ArrayList()
    private val NEW_SPINNER_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Hides the top bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_new_report_screen)

        //Janky way of setting default text to spinner
        hazChoices.add(0,"Type of hazard")
        //Add rest of options - move this to a function...
        hazChoices.add("Tree")
        hazChoices.add("Rock")
        hazChoices.add("Stick")
        hazChoices.add("Crash")

        homeBtn.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }


        val aa = ArrayAdapter(this,R.layout.spinner_layout_as_button,hazChoices)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(hazTypeSpin) {
            adapter = aa
            !aa.isEnabled(0)
            setSelection(0,false)
            onItemSelectedListener = this@NewReportScreen
            prompt = "Select hazard type"
            gravity = Gravity.CENTER
        }

        val spinner = Spinner(this)
        spinner.id = NEW_SPINNER_ID
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this@NewReportScreen,"Nothing selected",Toast.LENGTH_SHORT).show()
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (view?.id) {
            1 -> Toast.makeText(this@NewReportScreen,"Spinner 2 Position: ${position} amd hazard: ${hazChoices[position]}",Toast.LENGTH_SHORT).show()
            else -> {
                Toast.makeText(this@NewReportScreen,"Spinner 1 Position: ${position} amd hazard: ${hazChoices[position]}",Toast.LENGTH_SHORT).show()
            }
        }
    }
}