package com.example.tabbed.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.MainActivity
import com.example.tabbed.Model.MenuModel
import com.example.tabbed.R
import com.example.tabbed.Response.DefaultResponse
import com.example.tabbed.Util.ApiError
import com.example.tabbed.Util.ClickListenerGetView
import com.example.tabbed.Util.MenuRecyclerViewClickListener
import kotlinx.android.synthetic.main.fragment_frag_addeditmenu.*
import kotlinx.android.synthetic.main.list_menu.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class FragAddEditMenu(private val listener: MenuRecyclerViewClickListener) : Fragment() {
    private val TAG = "FragAddEditMenu"
    private var id_menu = -1
    lateinit var bitmap: Bitmap
    lateinit var bitmap2: Bitmap
    private val CODE_GALLERY_REQUEST = 999
    private var list_of_types = arrayOf("-- Please select type of food --", "MainCourse", "Salad", "Dessert", "Drink")

    private var inputMenuName: EditText? = null
    private var inputType: Spinner? = null
    private var inputPrice: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_addeditmenu, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.arguments?.clear()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupInterface()
        argumentCheck()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)!!.hideFloatingActionButton()
        setupButton()
    }

    private fun setupInterface(){
        inputMenuName = view?.findViewById(R.id.editTxtMenuName)
        inputPrice = view?.findViewById(R.id.editTxtPrice)
        inputType = view?.findViewById(R.id.spinnerMenuType)
    }

    private fun argumentCheck(){
        if (arguments?.getString("menu_name") != null) {
            id_menu = arguments!!.getInt("id_menu", -1)
            txtTitleEditMenu.text = "EDIT MENU"
            editTxtMenuName.setText(arguments?.getString("menu_name"))
            arguments!!.getString("type")?.let { setupSpinner(it) }
            editTxtPrice.setText(arguments?.getInt("price").toString())

            btnAddNewMenu.visibility = View.INVISIBLE
            btnUpdateMenu.visibility = View.VISIBLE
            btnDeleteMenu.visibility = View.VISIBLE

            var image_file = arguments?.getString("image_file")
            Glide.with(this)
                .asBitmap()
                .load(image_file)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bitmap2 = resource
                        imageFoodAddEdit.setImageBitmap(resource)
                        imageFoodAddEdit.alpha = 1.0F
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })
        }
        else {
            txtTitleEditMenu.text = "ADD NEW MENU"
            editTxtMenuName.text.clear()
            editTxtPrice.text.clear()
            setupSpinner("")
            btnAddNewMenu.visibility = View.VISIBLE
            btnUpdateMenu.visibility = View.INVISIBLE
            btnDeleteMenu.visibility = View.INVISIBLE
        }

    }

    private fun setupSpinner(type: String){
        if (!type.isNullOrEmpty()) {
            for (i in list_of_types.indices) {
                if (spinnerMenuType.adapter.getItem(i).toString().contains(type)) {
                    spinnerMenuType.setSelection(i)
                    return
                }
            }
        }else {
            spinnerMenuType.setSelection(0)
        }
        //spinnerMenuType.adapter
    }

    private fun setupButton(){
        imageFoodAddEdit.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                CODE_GALLERY_REQUEST
            )
        }

        btnAddNewMenu.setOnClickListener {
            if (::bitmap.isInitialized) {
                if (foodtypeCheck()) {
                    val imageData = imageToBase64(bitmap)
                    imageData.let {
                        addMenu(imageData)
                    }
                } else { Toast.makeText(requireContext(), "Please select type of food.", Toast.LENGTH_LONG).show() }
            }
            else {
                Toast.makeText(requireContext(), "Please select an image.", Toast.LENGTH_LONG).show()
            }
        }

        btnUpdateMenu.setOnClickListener {
            var imageData: String
            if(::bitmap.isInitialized){
                imageData = checkImgAndType(bitmap)
            } else {
                imageData = checkImgAndType(bitmap2)
            }
            if (!imageData.isNullOrEmpty()) {
                updateMenu(imageData, id_menu)
            }
        }

        btnDeleteMenu.setOnClickListener {
            if(id_menu !=- 1){
                deleteMenu(id_menu)
            }else{
                Toast.makeText(requireContext(), "Error deleting menu.", Toast.LENGTH_LONG).show()
            }
        }

        imgBackButtonEditMenu.setOnClickListener {
            listener.menuOnClick(imgBackButtonEditMenu, MenuModel(-1, "", "", -1, "", ""), -1)
        }
    }

    private fun checkImgAndType(bitmapToConvert: Bitmap): String{
        if (foodtypeCheck()) {
            val imageData = imageToBase64(bitmapToConvert)
            return imageData
        } else {
            Toast.makeText(requireContext(), "Please select type of food.", Toast.LENGTH_LONG).show()
            return ""
        }
    }

    private fun foodtypeCheck(): Boolean{
        return spinnerMenuType?.selectedItem.toString().trim() != list_of_types[0]
    }

    private fun addMenu(image_file: String){
        val menu_name = editTxtMenuName?.text.toString().trim()
        val type = spinnerMenuType?.selectedItem.toString().trim()
        val price = editTxtPrice?.text.toString().trim()

        RetrofitClient.instance.addMenuAPI(menu_name, image_file, type, price)
            .enqueue(object: Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG Error adding menu.", t.message)
                }
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if(response.isSuccessful){
                        Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                    }
                    else{ //response.errorBody()?.string()

                    }
                }
            })
    }

    private fun updateMenu(image_file: String, id_menu: Int){
        val menu_name = editTxtMenuName?.text.toString().trim()
        val type = spinnerMenuType?.selectedItem.toString().trim()
        val price = editTxtPrice?.text.toString().trim()

        RetrofitClient.instance.updateMenuAPI(id_menu, menu_name, image_file, type, price)
            .enqueue(object: Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG Error updating menu.", t.message)
                }
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if(response.isSuccessful){
                        Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                    }
                    else{ //response.errorBody()?.string()
                        Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun deleteMenu(id_menu: Int){
        RetrofitClient.instance.deleteMenuAPI(id_menu)
            .enqueue(object: Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG Error deleting menu.", t.message)
                }
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if(response.isSuccessful){
                        Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                    }
                    else{ //response.errorBody()?.string()
                        Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }




    private fun imageToBase64(bitmap: Bitmap) : String{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        val encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return encodedImage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            val filepath: Uri = data.data!!
            try {
                val inputStream: InputStream? = activity!!.contentResolver.openInputStream(filepath)
                bitmap = BitmapFactory.decodeStream(inputStream)
                imageFoodAddEdit.setImageBitmap(bitmap)
                imageFoodAddEdit.alpha = 1.0F
            } catch (e: FileNotFoundException){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK)
                intent.setType("image/*")
                startActivityForResult(Intent.createChooser(intent, "Select an รmage."), CODE_GALLERY_REQUEST)
            } else {
                Toast.makeText(requireContext(), "You don't have permission to access gallery.", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}