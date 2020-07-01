package com.example.renderscritpexample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.*
import android.view.View
import android.widget.*
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnDataPass {

    private lateinit var rs: RenderScript
    private lateinit var script: ScriptC_invert
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
            viewPager.setCurrentItem(tab.position,true)
        }.attach()

        imageDisp = findViewById(R.id.imageView)
        orignalImage = BitmapFactory.decodeResource(this.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(orignalImage)
        actualBitmap = (imageDisp.drawable as BitmapDrawable).bitmap

        rs = RenderScript.create(this);
        script  = ScriptC_invert(rs)
    }

    private fun invertBitmap(bitmap: Bitmap): Bitmap{

        val outputBitmap:Bitmap = Bitmap.createBitmap(bitmap)
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)

        tmpIn.copyFrom(bitmap)
        script.forEach_invert(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)
        return  outputBitmap
    }

    fun onInvertButtonClick(view: View) {
        var drawable: BitmapDrawable =  imageView.drawable as BitmapDrawable
        var img: Bitmap = drawable.bitmap

        imageDisp.setImageBitmap(invertBitmap(img))
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

        var  script2  = ScriptC_contrast(rs)
        script2.set_contrastFactor(contrast)


        tmpIn.copyFrom(bitmap)
        script2.forEach_contrast(tmpIn,tmpOut)
        tmpOut.copyTo(outputBitmap)

        return  outputBitmap
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

    override fun onBlurPass(value: Int) {
        if (value != 0){
        var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(BlurImage(img,value))
        }
    }



}
