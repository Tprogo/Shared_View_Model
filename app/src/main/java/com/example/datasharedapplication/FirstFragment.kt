package com.example.datasharedapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.datasharedapplication.databinding.FragmentFirstBinding
import kotlinx.coroutines.launch
import java.security.Permission


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val viewModelm by activityViewModels<SharedViewModel>()

//    private val permis = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissionMap ->
//        if (permissionMap.containsValue(false)) {
//            imagePicker.launch("image/*")
//        } else {
//            Toast.makeText(
//                context, "Permission is needed to open gallery", Toast.LENGTH_LONG
//            ).show()
//        }
//    }

    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) {

        if (it != null) {
            binding.imageView1.setImageURI(it)
        } else {
            Toast.makeText(requireContext(), "No media is selected", Toast.LENGTH_SHORT).show()

        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->

            if ( isGranted[Manifest.permission.ACCESS_COARSE_LOCATION]==true && isGranted[Manifest.permission.READ_CONTACTS]==true && isGranted[Manifest.permission.CAMERA]==true ) {
                // Permission is granted. Continue the action or workflow in your
                // app.

                imagePicker.launch("image/*")
            } else if ( isGranted[Manifest.permission.ACCESS_COARSE_LOCATION]==false || isGranted[Manifest.permission.READ_CONTACTS]==false|| isGranted[Manifest.permission.CAMERA]==false){
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.

                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()


            }
        }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        viewModelm.lvData.observe(viewLifecycleOwner, {
            binding.textView1.text = it

        })

        //navigate to second fragment using navcontroller

        binding.button1.setOnClickListener {

            val txInput = binding.editText1.text.toString()

            viewLifecycleOwner.lifecycleScope.launch { viewModelm.addData(txInput) }

            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }

//       select image from gallery using registerForActivityResult and getcontent

        //        binding.selectimagebtn.setOnClickListener {
//                imagePicker.launch("image/*")
//            }

//         runtime permission using registerForActivity
        binding.selectimagebtn.setOnClickListener {

         checkSelPermission()
//



        }





        return binding.root
    }

    private fun accessGallery(){
        requestPermissionLauncher.launch(arrayOf( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA ))
    }

    private fun showCustomDialog() {



        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Permission is Needed")
        builder.setMessage("We need permission to access the gallery.")
        builder.setPositiveButton("OK") { _, _ ->
            accessGallery()
        }
        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()

        // Add margins to the dialog's window
//        dialog.window?.attributes?.apply {
//            width = ViewGroup.LayoutParams.WRAP_CONTENT
//            height = ViewGroup.LayoutParams.WRAP_CONTENT
//
//        }

        dialog.show()
    }

    private fun checkSelPermission(){
        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED) &&  (ContextCompat.checkSelfPermission(
                requireContext()

                , Manifest.permission.ACCESS_COARSE_LOCATION

            ) ==
                    PackageManager.PERMISSION_GRANTED) &&  (ContextCompat.checkSelfPermission(
                requireContext()

                , Manifest.permission.CAMERA

            ) ==
                    PackageManager.PERMISSION_GRANTED) )  {
                // You can use the API that requires the permission.
               imagePicker.launch("image/*")
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.READ_CONTACTS) ||


                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)  || ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)

                ) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                showCustomDialog()

            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                accessGallery()


            }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}