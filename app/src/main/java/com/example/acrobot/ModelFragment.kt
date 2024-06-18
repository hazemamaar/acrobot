package com.example.acrobot

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.acrobot.databinding.ActivityUploadImageBinding
import com.example.acrobot.databinding.FragmentAcrobotDialogBinding
import com.example.acrobot.databinding.FragmentModelBinding
import com.example.acrobot.ml.CnnModelDisease
import com.example.acrobot.ui.activities.AppActivity
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ModelFragment : Fragment() {

    private var _binding: FragmentModelBinding? = null
    private val binding get()  = _binding!!
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    lateinit var imageProcessor: ImageProcessor
    private var vFilename: String = ""
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private  var isAcromegaly:Float = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentModelBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        imageProcessor = ImageProcessor.Builder()
//            .add(TransformToGrayscaleOp()).add(NormalizeOp(0.0f,255.0f))
            .add(ResizeOp(200, 200, ResizeOp.ResizeMethod.BILINEAR)).build()



        binding.btnNext.setOnClickListener {
            Log.e("dksmgd", "onViewCreated: $isAcromegaly", )
            if (binding.imagePerson.visibility == View.VISIBLE){
                findNavController().navigate(ModelFragmentDirections.actionModelFragmentToAcrobotDialogFragment(isAcromegaly))
            }else{
                Toast.makeText(requireContext(),"Please Enter Your photo",Toast.LENGTH_LONG).show()
            }
        }
        binding.closeImage.setOnClickListener {
            binding.imagePerson.visibility = View.GONE
            binding.closeImage.visibility = View.GONE
            binding.pickFromGallery.visibility = View.VISIBLE
            binding.pickFromCamera.visibility = View.VISIBLE

        }
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    binding.imagePerson.setImageURI(uri)
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                    predict(bitmap)
                    Log.e("hazem", "onRequestPermissionsResult:$uri ")
                    binding.imagePerson.visibility = View.VISIBLE
                    binding.closeImage.visibility = View.VISIBLE
                    binding.pickFromGallery.visibility = View.GONE
                    binding.pickFromCamera.visibility = View.GONE
                }
            }

        // Set up button listeners
        binding.pickFromCamera.setOnClickListener {

            if (checkPermissions()) {
                openCamera()
            }

        }

        binding.pickFromGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }



    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val neededPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        return if (neededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                neededPermissions.toTypedArray(),
                PERMISSION_CODE
            )
            false
        } else {
            true
        }
    }

    private fun openCamera() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "New Picture")
            put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        vFilename = "FOTO_$timeStamp.jpg"

        val storageDir =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "niabsen")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val file = File(storageDir, vFilename)
        var imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireActivity().packageName}.provider",
            file
        )

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }

        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            val file = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "niabsen/$vFilename"
            )
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireActivity().packageName}.provider",
                    file
                )
                binding.imagePerson.setImageURI(uri)
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                predict(bitmap)
                binding.imagePerson.visibility = View.VISIBLE
                binding.closeImage.visibility = View.VISIBLE
                binding.pickFromGallery.visibility = View.GONE
                binding.pickFromCamera.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), "Image file Not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun predict(bitmap: Bitmap) {
        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)
        val model = CnnModelDisease.newInstance(requireContext())

// Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 200, 200, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

// Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputBuffer = outputs.outputFeature0AsTensorBuffer
        val resultArray = outputBuffer.floatArray
//            var maxIdx=0
        resultArray.forEachIndexed { index, fl ->
            Log.e("hazemmmmm", "onViewCreated: index ${resultArray[index]}")
        }
        if (resultArray[1].toDouble() == 1.0) {
            Log.e("hazemamaar", "predict: No Acromegaly")
        } else {
            Log.e("hazemamaar", "predict:Acromegaly")
        }

        isAcromegaly=resultArray[1]
    }
}