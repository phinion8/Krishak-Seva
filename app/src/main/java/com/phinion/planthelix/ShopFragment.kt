package com.phinion.planthelix

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.adapters.ProductAdapter
import com.phinion.planthelix.adapters.ProductItemCallback
import com.phinion.planthelix.databinding.FragmentShopBinding
import com.phinion.planthelix.models.ProductItem
import com.phinion.planthelix.utils.WarningDialog
import com.phinion.planthelix.utils.isLocationEnabled
import com.phinion.planthelix.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale


class ShopFragment : Fragment(), ProductItemCallback {

    private lateinit var binding: FragmentShopBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var warningDialog: WarningDialog
    private var productList: ArrayList<ProductItem> = ArrayList()
    private lateinit var fertilizersAdapter: ProductAdapter
    private lateinit var pesticidesAdapter: ProductAdapter
    private var pesticidesList: ArrayList<ProductItem> = ArrayList()

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val MAX_LOCATION_RETRIES = 3
    private var locationRetries = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShopBinding.inflate(layoutInflater)

        database = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        warningDialog = WarningDialog(requireContext())

        val isLocationEnabled = isLocationEnabled(requireContext())
        fertilizersAdapter = ProductAdapter(requireContext(), productList, this)
        pesticidesAdapter = ProductAdapter(requireContext(), pesticidesList, this)

        if (isLocationEnabled) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted, you can proceed to get the location
                getCurrentLocation()
                dismissNoLocationView()
                showProductList()
            } else {
                showNoLocationView()
                dismissProductList()
            }
        } else {
            showNoLocationView()
            dismissProductList()
            requireContext().showToast("Please enable location from settings to continue.")

        }

        binding.okBtn.setOnClickListener {

            if (isLocationEnabled) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            } else {
                warningDialog.showSuccessDialog("Please enable location from settings to continue.", okOnClick = {
                    openLocationSettings(requireContext())
                    requireActivity().recreate()
                })
            }


        }

        binding.fertilizersList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@ShopFragment.fertilizersAdapter
        }

        binding.pesticidesList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@ShopFragment.pesticidesAdapter
        }

        if (isLocationEnabled) {
            database.collection("shopItems")
                .document("CYyShemrupV4kPaX0PAF")
                .collection("fertilizers")
                .addSnapshotListener { value, error ->

                    if (value != null) {
                        productList.clear()
                        for (snapshot in value.documents) {
                            val productItem = snapshot.toObject(ProductItem::class.java)
                            if (productItem != null) {
                                productList.add(productItem)
                            }
                        }
                        fertilizersAdapter.notifyDataSetChanged()

                    }
                }

            database.collection("shopItems")
                .document("Ajr7NEKevVN0bRLdMC1u")
                .collection("plantMedicines")
                .addSnapshotListener { value, error ->

                    if (value != null) {
                        pesticidesList.clear()
                        for (snapshot in value.documents) {
                            val productItem = snapshot.toObject(ProductItem::class.java)
                            if (productItem != null) {
                                pesticidesList.add(productItem)
                            }
                        }
                        pesticidesAdapter.notifyDataSetChanged()

                    }
                }
        }


        return binding.root
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed to get the location
                getCurrentLocation()
                dismissNoLocationView()
                showProductList()
            } else {
                showNoLocationView()
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }

    private fun getLocationWithRetry(fusedLocationClient: FusedLocationProviderClient) {
        CoroutineScope(Dispatchers.Main).launch {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requireContext().showToast("Please give location permission...")
                return@launch
            }
            fusedLocationClient.lastLocation.addOnSuccessListener {location->
                if (location != null){
                    getAddressFromLocation(location)
                }else{
                    if (locationRetries < MAX_LOCATION_RETRIES){
                        locationRetries++
                        getLocationWithRetry(fusedLocationClient)
                    }
                }
            }
        }

    }

    private fun getLocation() {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Attempt to get the location, retry up to MAX_LOCATION_RETRIES times
        getLocationWithRetry(fusedLocationClient)
    }

    private fun getCurrentLocation() {
        getLocation()
//        val fusedLocationClient: FusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(requireActivity())
//
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requireContext().showToast("Please give location permission...")
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location ->
//
//
//                if (location != null) {
//                    getAddressFromLocation(location)
//                } else {
//                    requireContext().showToast("location is null")
//                }
//
//
//            }
//            .addOnFailureListener {
//                requireContext().showToast(it.localizedMessage)
//            }
    }

    // Method to get city name from location
    private fun getAddressFromLocation(location: Location) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val cityName = addresses[0].locality
                    binding.cityText.text = cityName
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun showNoLocationView() {
        binding.coinAnim.visibility = View.VISIBLE
        binding.tvLocationPermission.visibility = View.VISIBLE
        binding.tvLocationPermissionDes.visibility = View.VISIBLE
        binding.okBtn.visibility = View.VISIBLE
    }

    fun dismissNoLocationView() {
        binding.coinAnim.visibility = View.GONE
        binding.tvLocationPermission.visibility = View.GONE
        binding.tvLocationPermissionDes.visibility = View.GONE
        binding.okBtn.visibility = View.GONE
    }

    fun showProductList() {
        binding.fertilizersList.visibility = View.VISIBLE
        binding.fertilizersText.visibility = View.VISIBLE
        binding.viewAllText.visibility = View.VISIBLE
        binding.pesticidesList.visibility = View.VISIBLE
        binding.pesticidesText.visibility = View.VISIBLE
        binding.pesticidesListViewAll.visibility = View.VISIBLE
    }

    fun dismissProductList() {
        binding.fertilizersList.visibility = View.GONE
        binding.fertilizersText.visibility = View.GONE
        binding.viewAllText.visibility = View.GONE
        binding.pesticidesList.visibility = View.GONE
        binding.pesticidesText.visibility = View.GONE
        binding.pesticidesListViewAll.visibility = View.GONE
    }

    override fun productItemOnClick(position: Int) {

    }

    fun openLocationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }


}