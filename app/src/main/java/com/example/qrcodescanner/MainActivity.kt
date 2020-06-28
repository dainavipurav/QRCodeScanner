package com.example.qrcodescanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.qrcodescanner.Models.HulkingModel
import com.example.qrcodescanner.Models.QRGeoModel
import com.example.qrcodescanner.Models.QRURLModel
import com.example.qrcodescanner.Models.QRVCardModel
import com.google.zxing.Result
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MainActivity : AppCompatActivity(), View.OnClickListener, ZXingScannerView.ResultHandler {

    private lateinit var mButton: Button
    private lateinit var mZxingScannerView: ZXingScannerView
    private lateinit var mTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mZxingScannerView = findViewById(R.id.zxingScannerView)
        mButton = findViewById(R.id.button)
        mTextView = findViewById(R.id.textView)



        Dexter.withContext(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    zxingScannerView.setResultHandler(this@MainActivity)
                    zxingScannerView.startCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }

            }).check()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> {

            }
        }
    }

    override fun handleResult(p0: Result?) {
        processRawResult(p0!!.text)
    }

    private fun processRawResult(text: String?) {
        if (text!!.startsWith("BEGIN:")){
            val tokens = text.split("\n".toRegex()).dropLastWhile( { it.isEmpty() }).toTypedArray()
            val qrvCardModel = QRVCardModel()
            for (i in tokens.indices){
                if (tokens[i].startsWith("BEGIN:"))
                    qrvCardModel.type= tokens[i].substring("BEGIN:".length )
                else if(tokens[i].startsWith("N:"))
                    qrvCardModel.name= tokens[i].substring("N:".length )
                else if(tokens[i].startsWith("ORG:"))
                    qrvCardModel.organization= tokens[i].substring("ORG:".length )
                else if(tokens[i].startsWith("TEL:"))
                    qrvCardModel.mobile= tokens[i].substring("TEL:".length )
                else if(tokens[i].startsWith("URL:"))
                    qrvCardModel.url= tokens[i].substring("URL:".length )
                else if(tokens[i].startsWith("EMAIL:"))
                    qrvCardModel.email= tokens[i].substring("EMAIL:".length )
                else if(tokens[i].startsWith("ADR:"))
                    qrvCardModel.address= tokens[i].substring("ADR:".length )
                else if(tokens[i].startsWith("NOTE:"))
                    qrvCardModel.note= tokens[i].substring("NOTE:".length )
                else if(tokens[i].startsWith("SUMMARY:"))
                    qrvCardModel.summary= tokens[i].substring("SUMMARY:".length )
                else if(tokens[i].startsWith("DTSTART:"))
                    qrvCardModel.dstart= tokens[i].substring("DTSTART:".length )
                else if(tokens[i].startsWith("DTEND:"))
                    qrvCardModel.dtend= tokens[i].substring("DTEND:".length )

                mTextView.text = qrvCardModel.type
            }
        }
        else if (text.startsWith("http://") || text.startsWith("https://") ||
            text.startsWith("www.")){
            val qrurlModel = QRURLModel()
            qrurlModel.url = text
            mTextView.text = qrurlModel.url
        }
        else if (text.startsWith("geo")){
            val qrGeoModel = QRGeoModel()
            val delims =  "[ , ?q= ]+"
            val tokens = text.split(delims.toRegex()).dropLastWhile( { it.isEmpty() }).toTypedArray()

            for (i in tokens.indices){
                if(tokens[i].startsWith("geo"))
                    qrGeoModel.lattitude = tokens[i].substring("geo".length  )
            }
            qrGeoModel.lattitude = tokens[0].substring("geo: ".length)
            qrGeoModel.longitude = tokens[1]
            qrGeoModel.geoPlace = tokens[2]

            mTextView.text = qrGeoModel.lattitude +" / "+qrGeoModel.longitude
        }
        else if (text.startsWith("UPI://")){
            val tokens = text.split("\n".toRegex()).dropLastWhile( { it.isEmpty() }).toTypedArray()
            val qrvCardModel = QRVCardModel()
            for (i in tokens.indices){
                if (tokens[i].startsWith("UPI:"))
                    qrvCardModel.type= tokens[i].substring("UPI:".length )
                else if(tokens[i].startsWith("PA:"))
                    qrvCardModel.id= tokens[i].substring("PA:".length )
                else if(tokens[i].startsWith("PN:"))
                    qrvCardModel.name= tokens[i].substring("PN:".length )
                else if(tokens[i].startsWith("ORG:"))
                    qrvCardModel.organization= tokens[i].substring("ORG:".length )
                else if(tokens[i].startsWith("TEL:"))
                    qrvCardModel.mobile= tokens[i].substring("TEL:".length )
                else if(tokens[i].startsWith("URL:"))
                    qrvCardModel.url= tokens[i].substring("URL:".length )
                else if(tokens[i].startsWith("EMAIL:"))
                    qrvCardModel.email= tokens[i].substring("EMAIL:".length )
                else if(tokens[i].startsWith("ADR:"))
                    qrvCardModel.address= tokens[i].substring("ADR:".length )
                else if(tokens[i].startsWith("NOTE:"))
                    qrvCardModel.note= tokens[i].substring("NOTE:".length )
                else if(tokens[i].startsWith("SUMMARY:"))
                    qrvCardModel.summary= tokens[i].substring("SUMMARY:".length )
                else if(tokens[i].startsWith("DTSTART:"))
                    qrvCardModel.dstart= tokens[i].substring("DTSTART:".length )
                else if(tokens[i].startsWith("DTEND:"))
                    qrvCardModel.dtend= tokens[i].substring("DTEND:".length )

                mTextView.text = "Id : ${qrvCardModel.id} \n Name : ${qrvCardModel.name}"
            }
        }
         else {
            mTextView.text = text
        }


/*
    fun capturePhoto() {
        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    requestPermissions(permissions, REQUEST_IMAGE_CAPTURE)
                } else {
                    openCamera()
                }
            } else {
                openCamera()
            }
        }
    }
*/

    }
}
