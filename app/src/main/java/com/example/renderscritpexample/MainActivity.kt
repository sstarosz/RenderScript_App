package com.example.renderscritpexample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.*
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var rs: RenderScript
    private lateinit var script: ScriptC_invert
    private lateinit var  orignalImage:Bitmap
    private lateinit var imageDisp: ImageView

    private lateinit var seekBarContrast: SeekBar
    private lateinit var seekBarSharp: SeekBar
    private lateinit var seekBarBlur: SeekBar

    private lateinit var contrastValueTextView: TextView
    private lateinit var sharpValueTextView: TextView
    private lateinit var blurValueTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contrastValueTextView = findViewById(R.id.ContrastValueText)
        sharpValueTextView = findViewById(R.id.SharpValueText)
        blurValueTextView = findViewById(R.id.BlurValueText)

        seekBarContrast = findViewById(R.id.seekBar)
        seekBarSharp = findViewById(R.id.seekBarSharpen)
        seekBarBlur = findViewById(R.id.seekBarBlur)

        SetSeekBars(seekBarContrast,seekBarSharp, seekBarBlur)

        imageDisp = findViewById(R.id.imageView)
        orignalImage = BitmapFactory.decodeResource(this.resources,R.drawable.kwiaty)
        imageDisp.setImageBitmap(orignalImage)

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

    fun SharpImage(bitmap: Bitmap,sharpFactor:Int):Bitmap{
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


    fun BlurImage(bitmap: Bitmap,blurFactor:Int): Bitmap {

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

    fun contras(bitmap: Bitmap,contrast: Float) : Bitmap{
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


    fun SetSeekBars(seekBarContrast: SeekBar,seekBarSharp: SeekBar,seekBarBlur: SeekBar){
        seekBarContrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                contrastValueTextView.text = progress.toString()

                var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)

                imageDisp.setImageBitmap(contras(img,progress.toFloat()))

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        }
        )

        seekBarBlur.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
               blurValueTextView.text = progress.toString()

                var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)

                if(progress != 0){
                    imageDisp.setImageBitmap(BlurImage(img,progress))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        }
        )

        seekBarSharp.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                sharpValueTextView.text = progress.toString()

                var img: Bitmap = BitmapFactory.decodeResource(this@MainActivity.resources,R.drawable.kwiaty)

                if(progress != 0){
                    imageDisp.setImageBitmap(SharpImage(img,(progress / 10)))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        }
        )
    }
}
