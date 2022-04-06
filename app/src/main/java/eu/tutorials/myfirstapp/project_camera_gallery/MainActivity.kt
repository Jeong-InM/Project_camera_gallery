package eu.tutorials.myfirstapp.project_camera_gallery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val GALLERY = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGallery = findViewById<Button>(R.id.btnGallery)
        btnGallery.setOnClickListener{
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,GALLERY)
        }
        var btnCamera = findViewById<Button>(R.id.btnCamera)
        btnCamera.setOnClickListener{
            if(checkPermission()){
                dispatchTakePictureIntent()
            }
            else{
                requestPermission()
            }
        }
    }


    @Override
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)

        val GImageView = findViewById<ImageView>(R.id.GImageView)
        if( resultCode == Activity.RESULT_OK){
            if( requestCode == GALLERY)
            {
                val ImnageData: Uri? =data?.data
                Toast.makeText(this, ImnageData.toString(), Toast.LENGTH_SHORT).show()
                try{
                    val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,ImnageData)
                    GImageView.setImageBitmap(bitmap)
                }
                catch(e:Exception)
                {
                    e.printStackTrace()
                }
            }
            else if( requestCode==REQUEST_IMAGE_CAPTURE)
            {
                val imageBitmap : Bitmap? = data?.extras?.get("data") as Bitmap
                GImageView.setImageBitmap(imageBitmap)
            }
        }
     }
    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA),1)
    }
    private fun checkPermission():Boolean{

        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

    }

    @Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 설정 OK", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "권한 허용 안됨", Toast.LENGTH_SHORT).show()
        }
    }

    private val REQUEST_IMAGE_CAPTURE=2
    private fun dispatchTakePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{ takePictureIntent->
            takePictureIntent.resolveActivity(packageManager)?.also{
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }



}