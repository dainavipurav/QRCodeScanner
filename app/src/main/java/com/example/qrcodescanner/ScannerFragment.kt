package com.example.qrcodescanner

import android.content.Context
import android.net.sip.SipSession
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maciejkozlowski.fragmentutils_kt.getListener
import com.maciejkozlowski.fragmentutils_kt.getListenerOrThrowException

class ScannerFragment : Fragment(R.layout.fragment_scanner) {
    companion object{
        fun newInstance() = ScannerFragment()
    }

    private lateinit var replaceFragmentListener : ReplaceWith

    override fun onAttach(context: Context) {
        super.onAttach(context)

        replaceFragmentListener = getListenerOrThrowException()

    }

}