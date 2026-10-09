package com.example.listycity

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {
    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")
    private val _cities = mutableStateListOf<City>()

    val cities: List<City>
        get() = _cities

    init {
        // Firestore snapshots keep the displayed list up to date.
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("CityRepository", "Could not load cities", error)
                return@addSnapshotListener
            }
            if (snapshot == null) return@addSnapshotListener

            val loadedCities = snapshot.documents.mapNotNull { document ->
                document.toObject(City::class.java)?.copy(id = document.id)
            }
            _cities.clear()
            _cities.addAll(loadedCities)
        }
    }

    fun addCity(city: City) {
        // Firestore creates an ID independent of the city's name.
        val data = mapOf("name" to city.name, "province" to city.province)
        citiesRef.add(data)
            .addOnFailureListener { error ->
                Log.e("CityRepository", "Could not add city", error)
            }
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        if (oldCity.id.isBlank()) {
            Log.e("CityRepository", "Cannot update a city without its document ID")
            return
        }

        // Update the fields while keeping the same document ID.
        citiesRef.document(oldCity.id)
            .update(mapOf("name" to updatedCity.name, "province" to updatedCity.province))
            .addOnFailureListener { error ->
                Log.e("CityRepository", "Could not update city", error)
            }
    }

    fun deleteCity(city: City) {
        if (city.id.isBlank()) {
            Log.e("CityRepository", "Cannot delete a city without its document ID")
            return
        }

        citiesRef.document(city.id).delete()
            .addOnFailureListener { error ->
                Log.e("CityRepository", "Could not delete city", error)
            }
    }
}
