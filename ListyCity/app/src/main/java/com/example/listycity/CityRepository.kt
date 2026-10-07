package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {

    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")
    init {
        citiesRef.addSnapshotListener {snapshot, error->
            if (error!=null){
                return@addSnapshotListener
            }
            _cities.clear()

            snapshot?.documents?.forEach {document ->
                val city=document.toObject( City::class.java)
                if (city!=null){
                    _cities.add(city)
                }
            }

        }
    }
    private val _cities = mutableStateListOf(
        City("Edmonton", "AB"),
        City("Vancouver", "BC"),
        City("Toronto", "ON")
    )

    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        _cities.add(city)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        citiesRef.document(oldCity.name).set(updatedCity)
        val index = _cities.indexOf(oldCity)
        if (index != -1) {
            _cities[index] = updatedCity
        }
    }
    fun deleteCity (city: City) {
        citiesRef.document(city.name).delete()

        }
    }
