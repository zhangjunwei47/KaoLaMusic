package com.kaola.kaolamusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaola.kaolamusic.R

class MyFragment : Fragment() {

    companion object{
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_my, container, false)
        return view
    }
}