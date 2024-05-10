package com.example.datasharedapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.datasharedapplication.databinding.FragmentFirstBinding
import com.example.datasharedapplication.databinding.FragmentSecondBinding
import kotlinx.coroutines.launch


class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val mainModel by activityViewModels<SharedViewModel>()

    //always put registerActivtyForResult code before oncreate

    private val getImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        if (it !=null){
        binding.imageView2.setImageURI(it)}
        else{
            Toast.makeText(context, "No media is selected", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentSecondBinding.inflate(inflater, container, false)


//      set livedata as text view
        mainModel.lvData.observe(viewLifecycleOwner,{
            binding.textView2.text = it
        })

        //add edit text to mtable live data

        binding.button2.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {mainModel.addData(binding.editText2.text.toString())}
        }

        // pick image using launcher
        binding.selectimagebtn2.setOnClickListener {
            getImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}