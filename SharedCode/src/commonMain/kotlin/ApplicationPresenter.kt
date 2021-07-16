package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlinx.serialization.json.JsonArray
import kotlin.coroutines.CoroutineContext


class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    private val scope = CoroutineScope(coroutineContext)

    override fun getAndDisplayJourneysData(view: ApplicationContract.MainView, arrivalStation: String, departureStation: String) {
        scope.launch { // launch a new coroutine and continue
            val journeysData: JsonArray = makeGetRequestForJourneysData(getAPIURLWithSelectedStations(arrivalStation, departureStation))
            view.displayJourneysInRecyclerView(journeysData.map{parseJSONElementToJourney(it)})
        }
    }

    override fun getAndListStationsData(view: ApplicationContract.SearchStationsView, url: String) {
        scope.launch { // launch a new coroutine and continue
            val stationsData: JsonArray = makeGetRequestForStationsData(url)
            view.listStationsInListView(stationsData.map{ parseJSONElementToStation(it) }
                    .filter{it.crs!="null"}.sortedBy { it.stationName })
        }
    }
}
