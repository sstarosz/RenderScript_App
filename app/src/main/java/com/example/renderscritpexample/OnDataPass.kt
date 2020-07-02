package com.example.renderscritpexample

interface OnDataPass {
    fun onContrastPass(value: Int)
    fun onBlurPass(value: Int)
    fun onSharpenPass(value: Int)
    fun onBrightPass(value: Int)

    fun invert()
    fun greyscale()
    fun addNoise()
    fun flip()
    fun mosaic()
    fun oilpaint()
    fun vignette()
    fun reflief()
}