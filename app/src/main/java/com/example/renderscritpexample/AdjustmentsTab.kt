package com.example.renderscritpexample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView

class AdjustmentsTab : Fragment() {

    private lateinit var seekBarContrast : SeekBar
    private lateinit var seekBarSharpen : SeekBar
    private lateinit var seekBarBlur : SeekBar
    private lateinit var seekBarBright : SeekBar


    private lateinit var contrastValueTextView: TextView
    private lateinit var sharpValueTextView: TextView
    private lateinit var blurValueTextView: TextView
    private lateinit var brightValueTextView: TextView


    lateinit var comm : OnDataPass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_adjustments_tab,container,false)
        comm = activity as OnDataPass

        seekBarContrast  = root.findViewById(R.id.seekBarContrast)
        seekBarSharpen  = root.findViewById(R.id.seekBarSharpen)
        seekBarBlur = root.findViewById(R.id.seekBarBlur)
        seekBarBright = root.findViewById(R.id.seekBarBrighter)

        contrastValueTextView = root.findViewById(R.id.textViewContrastValue)
        sharpValueTextView = root.findViewById(R.id.textViewSharpenValue)
        blurValueTextView = root.findViewById(R.id.textViewBlurValue)
        brightValueTextView = root.findViewById(R.id.textViewBrightnessValue)

        seekBarContrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                contrastValueTextView.text = progress.toString()
                comm.onContrastPass(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        seekBarSharpen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sharpValueTextView.text = progress.toString()
                comm.onSharpenPass(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekBarBlur.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blurValueTextView.text = progress.toString()
                comm.onBlurPass(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


        seekBarBright.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                brightValueTextView.text = progress.toString()
                comm.onBrightPass(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        return root
    }

}