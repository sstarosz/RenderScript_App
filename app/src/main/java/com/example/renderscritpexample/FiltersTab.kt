package com.example.renderscritpexample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class FiltersTab : Fragment() {

    private lateinit var comm : OnDataPass
    private lateinit var greyscaleButton: Button
    private lateinit var invertButton: Button
    private lateinit var noiseButton: Button
    private lateinit var flipButton: Button
    private lateinit var mosaicButton: Button
    private lateinit var oilpaintButton: Button
    private lateinit var vignetteButton: Button
    private lateinit var refliefButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_filters_tab,container,false)
        comm = activity as OnDataPass

        invertButton = root.findViewById(R.id.buttonInvert)
        noiseButton = root.findViewById(R.id.buttonNoise)
        greyscaleButton = root.findViewById(R.id.buttonGreyscale)
        flipButton = root.findViewById(R.id.buttonFilpH)
        mosaicButton = root.findViewById(R.id.buttonMosaic)
        oilpaintButton = root.findViewById(R.id.buttonOilPaint)
        vignetteButton = root.findViewById(R.id.buttonPixelate)
        refliefButton = root.findViewById(R.id.buttonRelief)

        greyscaleButton.setOnClickListener {
            comm.greyscale()
        }
        invertButton.setOnClickListener {
            comm.invert()
        }
        noiseButton.setOnClickListener {
            comm.addNoise()
        }

        flipButton.setOnClickListener {
            comm.flip()
        }

        mosaicButton.setOnClickListener {
            comm.mosaic()
        }

        oilpaintButton.setOnClickListener {
            comm.oilpaint()
        }

        vignetteButton.setOnClickListener {
            comm.vignette()
        }

        refliefButton.setOnClickListener {
            comm.reflief()
        }


        return root
    }

}