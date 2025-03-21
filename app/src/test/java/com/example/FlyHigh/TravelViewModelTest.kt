package com.example.FlyHigh

import com.example.FlyHigh.ui.viewmodel.TravelViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
//import org.mockito.Mockito.mock ---> no nos deja importar la libreria Mockito, entonces
//import org.mockito.Mockito.`when`    hemos preferido implementar los logs, y dan conflicto
//import org.mockito.Mockito.verify    con los tests.


class TravelViewModelTest {
    private lateinit var viewModel: TravelViewModel

    @Before
    fun initTravelViewModel() {
        viewModel = TravelViewModel()
    }

    @Test
    fun addTravelSuccessfully() {
        viewModel.addTravel("Trip to Paris", "A wonderful trip to France")
        assertEquals(1, viewModel.travels.size)
        assertEquals("Trip to Paris", viewModel.travels[0].name)
    }

    @Test
    fun updateExistingTravel() {
        viewModel.addTravel("Old Trip", "Description")
        val travel = viewModel.travels[0].copy(name = "New Trip")
        viewModel.updateTravel(travel)
        assertEquals("New Trip", viewModel.travels[0].name)
    }

    @Test
    fun deleteTravel() {
        viewModel.addTravel("Trip to Paris", "A wonderful trip to France")
        val travelId = viewModel.travels[0].id
        viewModel.deleteTravel(travelId)
        assertTrue(viewModel.travels.isEmpty())
    }

    @Test
    fun addItinerarySuccessfully() {
        viewModel.addTravel("Trip", "Description")
        val travelId = viewModel.travels[0].id
        viewModel.addItinerary(travelId, "Day 1", "Visit the museum")
        assertEquals(1, viewModel.travels[0].itineraries.size)
    }

    @Test
    fun updateItinerary() {
        viewModel.addTravel("Trip", "Description")
        val travelId = viewModel.travels[0].id
        viewModel.addItinerary(travelId, "Day 1", "Visit the museum")
        val updatedItinerary = viewModel.travels[0].itineraries[0].copy(name = "Updated Day 1")
        viewModel.updateItinerary(travelId, updatedItinerary)
        assertEquals("Updated Day 1", viewModel.travels[0].itineraries[0].name)
    }

    @Test
    fun deleteItinerary() {
        viewModel.addTravel("Trip", "Description")
        val travelId = viewModel.travels[0].id
        viewModel.addItinerary(travelId, "Day 1", "Visit the museum")
        val itineraryId = viewModel.travels[0].itineraries[0].id
        viewModel.deleteItinerary(travelId, itineraryId)
        assertTrue(viewModel.travels[0].itineraries.isEmpty())
    }
}
