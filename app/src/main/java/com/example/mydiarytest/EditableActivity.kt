package com.example.mydiarytest

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mydiarytest.databinding.ActivityEditableBinding

private const val TAG = "EditableActivity"

class EditableActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditableBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.insertImageButton.setOnClickListener { insertImage() }
        binding.changeFont.setOnClickListener { changeFont() }
        binding.pushSizeText.setOnClickListener {
            binding.editText.textSize += 20f
        }
    }

    private fun changeFont() {
        binding.editText.typeface = ResourcesCompat.getFont(this,R.font.nerko_one_regular)
    }

    private fun insertImage() {
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground)
        drawable?.let {
            val bitmap = drawableToBitmap(it)
            val imageSpan = ImageSpan(this, bitmap)
            val spannable = SpannableStringBuilder(binding.editText.text)
            // Đảm bảo vị trí con trỏ hợp lệ
            val cursorPosition = binding.editText.selectionStart.coerceAtLeast(0).coerceAtMost(spannable.length)

            // Thêm một ký tự tạm thời nếu con trỏ ở cuối văn bản
            if (cursorPosition == spannable.length) {
                spannable.append("{image}")
            }

            // Chèn ImageSpan tại vị trí con trỏ
            spannable.setSpan(
                imageSpan,
                cursorPosition,
                cursorPosition + 7,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
//            binding.editText.
            Log.d(TAG, "insertImage: $spannable")
            binding.editText.text = spannable

            // Di chuyển con trỏ sau ImageSpan
            binding.editText.setSelection(cursorPosition + 7)

        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        // For other drawable types (including VectorDrawable)
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}