package com.ahmadfma.intermediate_submission1.ui.add_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.custom.ProgressDialog
import com.ahmadfma.intermediate_submission1.databinding.ActivityAddStoryBinding
import com.ahmadfma.intermediate_submission1.helper.FileHelper
import com.ahmadfma.intermediate_submission1.helper.Validator
import com.ahmadfma.intermediate_submission1.viewmodel.StoryViewModel
import com.ahmadfma.intermediate_submission1.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import com.ahmadfma.intermediate_submission1.data.Result
import com.ahmadfma.intermediate_submission1.ui.maps.ChooseLocationActivity

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: StoryViewModel
    private lateinit var currentPhotoPath: String
    private var selectedImageFile: File? = null
    private val progressDialog = ProgressDialog(this)
    private var latitude : Float? = null
    private var longitude : Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[StoryViewModel::class.java]
    }

    private fun initListener() = with(binding) {
        addStoryToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        chooseFromGalleryBtn.setOnClickListener {
            startGallery()
        }
        chooseFromCameraBtn.setOnClickListener {
            startIntentCamera()
        }
        chooseLocationBtn.setOnClickListener {
            startIntentChooseLocation()
        }
        uploadBtn.setOnClickListener {
            checkInputs()
        }
    }

    private fun checkInputs() = with(binding) {
        var isValid = false
        descLayout.descInput.clearFocus()
        if(selectedImageFile == null) {
            Toast.makeText(this@AddStoryActivity, getString(R.string.error_select_image), Toast.LENGTH_SHORT).show()
        } else if(!Validator.isAllFormFilled(arrayOf(descLayout.descInput))) {
            Toast.makeText(this@AddStoryActivity, getString(R.string.error_form_not_filled), Toast.LENGTH_SHORT).show()
        } else  {
            isValid = true
        }

        if(isValid) {
            val description = descLayout.descInput.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = selectedImageFile!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                selectedImageFile!!.name,
                requestImageFile
            )
            uploadListener(imageMultipart, description)
        }
    }

    private fun uploadListener(imageMultipart: MultipartBody.Part, description: RequestBody) {
        viewModel.addNewStories(imageMultipart, description).observe(this) { result ->
            when(result) {
                is Result.Loading -> {
                    progressDialog.startLoadingDialog()
                }
                is Result.Error -> {
                    progressDialog.dismissDialog()
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    progressDialog.dismissDialog()
                    val response = result.data
                    if (response != null) {
                        if (!response.error) {
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun startIntentCamera() {
        if (!allCameraPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)
            FileHelper.createTempFile(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(this@AddStoryActivity, AUTHOR, it)
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startIntentChooseLocation() {
        if(!allLocationPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS_LOCATION, REQUEST_CODE_LOCATION)
        } else {
            val intent = Intent(this, ChooseLocationActivity::class.java)
            launcherChoosePosition.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!allCameraPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.no_permission), Toast.LENGTH_SHORT).show()
            }
        } else if(requestCode == REQUEST_CODE_LOCATION) {
            if(!allLocationPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.no_permission), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allCameraPermissionsGranted() = REQUIRED_PERMISSIONS_CAMERA.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun allLocationPermissionsGranted() = REQUIRED_PERMISSIONS_LOCATION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            selectedImageFile = FileHelper.reduceFileImage(FileHelper.uriToFile(selectedImg, this@AddStoryActivity))
            binding.storyImage.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            selectedImageFile = FileHelper.reduceFileImage(File(currentPhotoPath))
            if(selectedImageFile != null) {
                val result =  BitmapFactory.decodeFile(selectedImageFile!!.path)
                binding.storyImage.setImageBitmap(result)
            } else {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val launcherChoosePosition = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val data = it.data
            if(data != null) {
                latitude = data.getFloatExtra(ChooseLocationActivity.EXTRA_LATITUDE, 0f)
                longitude = data.getFloatExtra(ChooseLocationActivity.EXTRA_LONGITUDE, 0f)
                ViewCompat.setBackgroundTintList(binding.chooseLocationBtn, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blue)))
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS_CAMERA = arrayOf(Manifest.permission.CAMERA)
        private val REQUIRED_PERMISSIONS_LOCATION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        private const val REQUEST_CODE_CAMERA = 10
        private const val REQUEST_CODE_LOCATION = 11
        private const val AUTHOR = "com.ahmadfma.intermediate_submission1"
    }

}