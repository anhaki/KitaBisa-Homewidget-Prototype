package com.kitapacak.wecan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kitapacak.wecan.SharedPref.CityModel
import com.kitapacak.wecan.SharedPref.CityPreference
import com.kitapacak.wecan.databinding.ActivitySettingsBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var cityModel: CityModel

    private lateinit var cityPreference: CityPreference

    //Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var cityName = ""
    private var country = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnGetLokasi.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //request location permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQ_CODE
                )
            } else {
                //if permission has already granted
                getLocation()
            }
        }

        //sharedPref
        cityPreference = CityPreference(this)
        showExistingPref()
    }

    private fun showExistingPref() {
        cityModel = cityPreference.getCity()
        populateView(cityModel)
    }

    private fun populateView(cityModel: CityModel) {
        binding.apply {
            tvCity.text = if (cityModel.city.toString().isEmpty()) "Set Your" else cityModel.city
            tvCountryName.text = if (cityModel.country.toString().isEmpty()) "Location" else cityModel.country
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
            getCityName(location.latitude, location.longitude)
            binding.tvCity.text = cityName
            binding.tvCountryName.text = country

            saveCity(cityName, country)

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, cityModel)

            setResult(RESULT_CODE, resultIntent)
        }
    }

    //Location
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

    //Location
    private fun getCityName(lat: Double, long: Double) {
        try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val address = geoCoder.getFromLocation(lat, long, 3)
            if (address != null) {
                country = address[0].countryName
                cityName = address[0].subAdminArea
            }
        } catch (e: Exception) {
            Toast.makeText(this, "loading city", Toast.LENGTH_SHORT).show()
        }
    }

    //Location
    private fun saveCity(city: String, country: String) {
        val cityPreference = CityPreference(this)

        cityModel.city = city
        cityModel.country = country

        cityPreference.setCity(cityModel)
        Toast.makeText(this, "location saved!", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        const val LOCATION_PERMISSION_REQ_CODE = 1001
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101
    }
}