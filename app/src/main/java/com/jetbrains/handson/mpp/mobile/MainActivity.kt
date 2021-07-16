package com.jetbrains.handson.mpp.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), ApplicationContract.MainView {

    lateinit var departureStationText: EditText
    lateinit var arrivalStationText: EditText

    lateinit var presenter: ApplicationPresenter

    var originStationCRS = ""
    var destStationCRS = ""

    private val departureStationStart = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            departureStationText.setText(result.data!!.getStringExtra("Result"))
            originStationCRS = result.data!!.getStringExtra("ResultCRS")
        }
    }

    private val arrivalStationStart = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            arrivalStationText.setText(result.data!!.getStringExtra("Result"))
            destStationCRS = result.data!!.getStringExtra("ResultCRS")
        }
    }

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = ApplicationPresenter()

        departureStationText = findViewById(R.id.departureStationText)
        arrivalStationText = findViewById(R.id.arrivalStationText)
        departureStationText.setOnClickListener{
            departureStationStart.launch(Intent(this,SearchStationsActivity::class.java))
        }
        arrivalStationText.setOnClickListener{
            arrivalStationStart.launch(Intent(this,SearchStationsActivity::class.java))
        }

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener{presenter.getAndDisplayJourneysData(this, originStationCRS, destStationCRS)}
    }

    override fun displayJourneysInRecyclerView(journeysData: List<Journey>) {
        val rvTrains: RecyclerView = findViewById(R.id.rvTrains)
        if (journeysData.isEmpty()) {
            val noJourneysFoundText: TextView = findViewById(R.id.noJourneysFoundText)
            noJourneysFoundText.visibility = View.VISIBLE
            rvTrains.visibility = View.GONE
            return
        }
        val journeysAdapter = JourneysAdapter(journeysData)
        rvTrains.adapter = journeysAdapter
        rvTrains.layoutManager = LinearLayoutManager(this)
    }

}
