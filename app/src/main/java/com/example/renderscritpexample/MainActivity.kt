package com.example.renderscritpexample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.renderscript.*
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnDataPass {

    private lateinit var rs: RenderScript

    private lateinit var scriptcInvert: ScriptC_invert
    private lateinit var scriptGreyscale : ScriptC_grayscale
    private lateinit var scriptContrast : ScriptC_contrast
    private lateinit var scriptcNoise: ScriptC_noise
    private lateinit var scriptcBright: ScriptC_bright
    private lateinit var scriptFlip : ScriptC_flip
    private lateinit var scriptMosaic : ScriptC_mosaicFilter
    private lateinit var scriptcOilpaint: ScriptC_oilpaint
    private lateinit var scriptcRelieffilter: ScriptC_reliefFilter
    private lateinit var scriptcVignettefilter: ScriptC_vignetteFilter

    private lateinit var orignalImage:Bitmap
    private lateinit var imageDisp: ImageView
    private lateinit var actualBitmap : Bitmap
    lateinit var viewPager : ViewPager2
    lateinit var pagerAdapter : PageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)

        pagerAdapter = PageAdapter(this,3)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout,viewPager){ tab, position ->
            when(position){
                0->tab.text = "Adjusments"
                1->tab.text = "Filters"
                2->tab.text = "Options"
            }
            viewPager.setCurrentItem(tab.position,true)
        }.attach()

        imageDisp = findViewById(R.id.imageView)
        orignalImage = BitmapFactory.decodeResource(this.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(orignalImage)
        actualBitmap = (imageDisp.drawable as BitmapDrawable).bitmap

        rs = RenderScript.create(this);
        scriptcInvert  = ScriptC_invert(rs)
        scriptGreyscale = ScriptC_grayscale(rs)
        scriptContrast = ScriptC_contrast(rs)
        scriptcNoise = ScriptC_noise(rs)
        scriptcBright = ScriptC_bright(rs)
        scriptFlip = ScriptC_flip(rs)
        scriptMosaic = ScriptC_mosaicFilter(rs)
        scriptcOilpaint = ScriptC_oilpaint(rs)
        scriptcRelieffilter = ScriptC_reliefFilter(rs)
        scriptcVignettefilter = ScriptC_vignetteFilter(rs)
    }

    fun onClickReset(view: View) {
        orignalImage = BitmapFactory.decodeResource(this.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(orignalImage)
    }
    override fun onContrastPass(value: Int) {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(ContrastImage(img,value.toFloat()))
    }
    override fun onSharpenPass(value: Int) {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(SharpImage(img,value))
    }
    override fun onBrightPass(value: Int) {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(BrightImage(img,value))
    }
    override fun invert() {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(invertBitmap(img))
    }
    override fun onBlurPass(value: Int) {
        if (value != 0){
            var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
            imageDisp.setImageBitmap(BlurImage(img,value))
        }
    }
    override fun greyscale() {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(greyScaleImage(img))
    }
    override fun addNoise() {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(Noise(img))
    }

    override fun flip() {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(FlipImage(img))
    }

    override fun mosaic() {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(MosaicImage(img))
    }

    override fun oilpaint() {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(OilpaintImage(img))
    }

    override fun vignette() {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(VignetteImage(img))
    }

    override fun reflief() {
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(RefliefImage(img))
    }


    private fun greyScaleImage(bitmap: Bitmap) : Bitmap{
        val inputAllocation: Allocation = Allocation.createFromBitmapResource(
            rs,
            resources,
            R.drawable.kwiaty
        )
        val outputAllocation: Allocation = Allocation.createTyped(
            rs,
            inputAllocation.type,
            Allocation.USAGE_SCRIPT
        )

        scriptGreyscale.invoke_process(inputAllocation, outputAllocation)

        outputAllocation.copyTo(bitmap)
        return bitmap
    }

    private fun Noise(bitmap: Bitmap) : Bitmap{
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        tmpIn.copyFrom(bitmap)
        scriptcNoise.forEach_Noise(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)
        return  outputBitmap
    }

    private fun invertBitmap(bitmap: Bitmap): Bitmap{
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        tmpIn.copyFrom(bitmap)
        scriptcInvert.forEach_invert(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)
        return  outputBitmap
    }

    private fun SharpImage(bitmap: Bitmap,sharpFactor:Int):Bitmap{
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        var sharp : ScriptIntrinsicConvolve3x3 = ScriptIntrinsicConvolve3x3.create(rs,Element.U8_4(rs))

        val matrixSharpen = floatArrayOf(
            0.0f, -sharpFactor.toFloat(), 0.0f,
            -sharpFactor.toFloat(), 1 + 4 * sharpFactor.toFloat(), - sharpFactor.toFloat(),
            0.0f,-sharpFactor.toFloat(),0.0f)

        sharp.setInput(tmpIn)
        sharp.setCoefficients(matrixSharpen)
        sharp.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        tmpIn.destroy()
        tmpOut.destroy()
        return outputBitmap
    }

    private fun BlurImage(bitmap: Bitmap,blurFactor:Int): Bitmap {

        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        var blur : ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        blur.setRadius(blurFactor.toFloat() / 4.0f)
        blur.setInput(tmpIn)
        blur.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        return outputBitmap
    }

    private fun ContrastImage(bitmap: Bitmap, contrast: Float) : Bitmap{
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)
        scriptContrast._contrastFactor = contrast

        tmpIn.copyFrom(bitmap)
        scriptContrast.forEach_contrast(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)

        return  outputBitmap
    }

    private fun BrightImage(bitmap: Bitmap,brightFactor:Int) : Bitmap{
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        scriptcBright.invoke_setBright(brightFactor.toFloat())
        scriptcBright.forEach_exposure(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)

        tmpIn.destroy()
        tmpOut.destroy()
        return  outputBitmap
    }

    private fun FlipImage(bitmap: Bitmap) :Bitmap{
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        scriptFlip.invoke_flip_setup(tmpIn,tmpOut,1)
        scriptFlip.forEach_flip(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)

        tmpIn.destroy()
        tmpOut.destroy()
        return  outputBitmap
    }

    private fun MosaicImage(bitmap: Bitmap): Bitmap {
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        scriptMosaic._gIn = tmpIn;
        scriptMosaic._gIn = tmpOut;
        scriptMosaic.invoke_setup_mosaic()
        scriptMosaic.forEach_mosaic(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)

        tmpIn.destroy()
        tmpOut.destroy()
        return  outputBitmap
    }

    private fun VignetteImage(bitmap: Bitmap): Bitmap {
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        scriptcVignettefilter.invoke_setup_vignette(tmpIn,tmpOut)
        scriptcVignettefilter.forEach_vignetteFilter(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)

        tmpIn.destroy()
        tmpOut.destroy()

        return  outputBitmap
    }

    private fun OilpaintImage(bitmap: Bitmap): Bitmap {
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        scriptcOilpaint._gIn = tmpIn
        scriptcOilpaint._gOut = tmpOut
        scriptcOilpaint.invoke_setup_oilpaint()
        scriptcOilpaint.forEach_oilpaint(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)

        tmpIn.destroy()
        tmpOut.destroy()
        return  outputBitmap
    }

    private fun RefliefImage(bitmap: Bitmap): Bitmap {
        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        scriptcRelieffilter._gIn = tmpIn
        scriptcRelieffilter._gOut = tmpOut
        scriptcRelieffilter.invoke_setup_relief()
        scriptcRelieffilter.forEach_relief(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)

        tmpIn.destroy()
        tmpOut.destroy()
        return  outputBitmap
    }

}
