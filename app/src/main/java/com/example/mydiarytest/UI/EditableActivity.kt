package com.example.mydiarytest.UI

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.example.mydiarytest.R
import com.example.mydiarytest.databinding.ActivityEditableBinding

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

//        binding.insertImageButton.setOnClickListener { insertIconDemo() }
        insertIconDemo()
        binding.changeFont.setOnClickListener { changeFont() }
        binding.pushSizeText.setOnClickListener {
            binding.editText.textSize += 20f
        }

        binding.editText.doOnTextChanged { text, start, before, count ->
            Log.e("ThoNH", "$text >>> $start $before $count")
        }
    }

    private fun changeFont() {
        binding.editText.typeface = ResourcesCompat.getFont(this, R.font.nerko_one_regular)
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun insertIconDemo() {
        binding.insertImageButton.setOnClickListener {
            val cursorPosition = binding.editText.selectionStart
            val layout = binding.editText.layout
            val currentLine = layout.getLineForOffset(cursorPosition)
            val lineStart = layout.getLineStart(currentLine)
            val lineEnd = layout.getLineEnd(currentLine)
            val spannable = SpannableStringBuilder(binding.editText.text)

            // Tìm tất cả ImageSpan trong dòng hiện tại
            val imageSpans = spannable.getSpans(lineStart, lineEnd, ImageSpan::class.java)

            if (imageSpans.isNotEmpty() && spannable.getSpanStart(imageSpans.first()) == lineStart) {

                for (imageSpan in imageSpans) {
                    spannable.removeSpan(imageSpan)
                }

                if (spannable.toString().substring(lineStart, lineStart + 1) == "߷") {
                    spannable.delete(lineStart, lineStart + 1)
                }
                binding.editText.text = spannable
                binding.editText.setSelection(cursorPosition.coerceAtMost(spannable.length))
            } else {
                spannable.insert(lineStart, "߷")
                spannable.setSpan(
                    ImageSpan(this@EditableActivity, R.drawable.icon_edit),
                    lineStart,
                    lineStart + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.editText.text = spannable
                Log.d("PNCheck: ", spannable.toString())
                binding.editText.setSelection(cursorPosition.coerceAtMost(spannable.length))
            }
        }

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.editText.setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        val cursorPosition = binding.editText.selectionStart
                        val layout = binding.editText.layout
                        val currentLine = layout.getLineForOffset(cursorPosition)
                        val lineStart = layout.getLineStart(currentLine)
                        val lineEnd = layout.getLineEnd(currentLine)
                        val spannable = SpannableStringBuilder(binding.editText.text)

                        // Tìm tất cả ImageSpan trong dòng hiện tại
                        val imageSpans = spannable.getSpans(lineStart, lineEnd, ImageSpan::class.java)

                        // Nếu có ImageSpan ở đầu dòng hiện tại, chèn image vào đầu dòng mới
                        if (imageSpans.isNotEmpty() && spannable.getSpanStart(imageSpans.first()) == lineStart) {
                            // Chèn image vào đầu dòng mới
                            spannable.insert(cursorPosition, "\n߷")
                            spannable.setSpan(
                                ImageSpan(this@EditableActivity, R.drawable.icon_edit),
                                cursorPosition + 1,
                                cursorPosition + 2,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )

                            binding.editText.text = spannable
                            binding.editText.setSelection(cursorPosition + 2) // Đặt con trỏ sau image mới
                            return@setOnKeyListener true // Ngăn hệ thống tự động thêm Enter
                        }
                    }
                    false
                }
            }
        })

        binding.editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE) {
                val x = event.x.toInt()
                val y = event.y.toInt()
                val layout = binding.editText.layout
                val line = layout.getLineForVertical(y)
                val offset = layout.getOffsetForHorizontal(line, x.toFloat())

                // Tìm tất cả ImageSpan trong dòng hiện tại
                val imageSpans = (binding.editText.text as Spannable).getSpans(
                    layout.getLineStart(line),
                    layout.getLineEnd(line),
                    ImageSpan::class.java
                )

                if (imageSpans.isNotEmpty()) {
                    // Nếu con trỏ nằm trước hoặc vào một ImageSpan, điều chỉnh vị trí
                    for (imageSpan in imageSpans) {
                        val spanStart = (binding.editText.text as Spannable).getSpanStart(imageSpan)
                        if (offset <= spanStart) {
                            // Đặt con trỏ sau hình ảnh
                            binding.editText.setSelection(spanStart + 1)
                            return@setOnTouchListener true // Ngăn chặn hành động mặc định
                        }
                    }
                }
            }
            false
        }
    }

//    @SuppressLint("SetTextI18n")
//    private fun insertIconDemo(){
//        binding.insertImageButton.setOnClickListener {
//            val cursorPosition = binding.editText.selectionStart
//            val layout = binding.editText.layout
//            val currentLine = layout.getLineForOffset(cursorPosition)
//            Log.d("currentLine: ","currentLine: $currentLine")
//            val lineStart = layout.getLineStart(currentLine)
//            Log.d("lineStart: ","lineStart: $lineStart")
//            val lineEnd = layout.getLineEnd(currentLine)
//            Log.d("lineEnd: ","lineEnd: $lineEnd")
//            val currentLineText =
//                binding.editText.text?.substring(lineStart, lineEnd) ?: ""
//
//            val spannable = SpannableStringBuilder(binding.editText.text)
//            Log.d("SpannableStringBuilder: ","SpannableStringBuilder: $spannable")
//
//            val space = " "
//
//            if(spannable.isNotEmpty()){
//                val imageSpans = spannable.getSpans(lineStart, lineStart + 1, ImageSpan::class.java)
//
//                if (imageSpans.isNotEmpty()) {
//                    for (imageSpan in imageSpans) {
//                        spannable.removeSpan(imageSpan)
//                        Log.d("removeSpan", "removeSpan: comecome")
//                    }
//                    Log.d("PNinsertIconDemo", "ImageSpan removed at line: $currentLine")
//
//                } else {
//
//                    if(!currentLineText.startsWith(space)){
//                        spannable.insert(lineStart, space)
//                    }
//                    spannable.setSpan(
//                        ImageSpan(this@EditableActivity, R.drawable.icon_edit),
//                        lineStart,
//                        lineStart + 1,
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                    )
//
//                }
//                binding.editText.text = spannable
//                binding.editText.setSelection(currentLineText.length)
//
//            }else{
//                val textToInsert = " " // thêm space
//                spannable.insert(lineStart, textToInsert)
//                spannable.setSpan(
//                    ImageSpan(this@EditableActivity, R.drawable.icon_edit),
//                    lineStart,
//                    lineStart + 1,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                binding.editText.text = spannable
//                binding.editText.setSelection(cursorPosition+1)
//            }
//
//
//            // Áp dụng MovementMethod để chặn việc di chuyển con trỏ trước ImageSpan
////            binding.editText.movementMethod = NoSelectionBeforeImageSpanMovementMethod()
//        }
//
//        binding.editText.addTextChangedListener(object: TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            @SuppressLint("SetTextI18n")
//            override fun afterTextChanged(s: Editable?) {
//                binding.editText.setOnKeyListener { _, keyCode, event ->
//                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//
//                        val cursorPosition = binding.editText.selectionStart
//                        Log.d("curPoafterTextChanged: ", ": ${binding.editText.selectionStart}")
//                        val layout = binding.editText.layout
//
//                        if (layout != null) {
//                            val currentLine = layout.getLineForOffset(cursorPosition)
//                            Log.d("currentLineafterTextChanged: ", ": $currentLine")
//                            val lineStart = layout.getLineStart(currentLine)
//                            val lineEnd = layout.getLineEnd(currentLine)
//                            val spannable = SpannableStringBuilder(binding.editText.text)
//                            Log.d("spannableTextChanged: ", ": $spannable")
//                            val imageSpans = spannable.getSpans(lineStart, lineStart + 1, ImageSpan::class.java)
//                            Log.d("imageSpansTextChanged: ", ": $imageSpans")
//
//                            if (imageSpans.isNotEmpty()) {
//                                val newLineBullet = " "
//                                spannable.insert(cursorPosition, newLineBullet)
//                                val currentText = binding.editText.text.toString()
//                                Log.d("currentTextTextChanged: ", ": $currentText")
//                                binding.editText.setText(currentText.substring(0,cursorPosition) + newLineBullet + currentText.substring(cursorPosition))
//
//                                val newLineStart = cursorPosition + newLineBullet.length
//                                spannable.setSpan(
//                                    ImageSpan(this@EditableActivity, R.drawable.icon_edit),
//                                    newLineStart - 1, // Đặt ImageSpan vào khoảng trắng mới
//                                    newLineStart,
//                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                                )
//
//                                binding.editText.text = spannable
//
//                                binding.editText.setSelection(newLineStart-1)
////                                binding.editText.movementMethod = NoSelectionBeforeImageSpanMovementMethod()
//                            }
//                        }
//                    }
//                    false
//                }
//            }
//        })
////        val spannable = SpannableStringBuilder("߷"+binding.editText.text)
////        val cursorPosition = binding.editText.selectionStart.coerceAtLeast(0).coerceAtMost(spannable.length)
//////        spannable.append("߷")
////        if(binding.editText.text.startsWith("߷")){
////            val s = binding.editText.text.toString().replace("߷","")
////        }
////        spannable.setSpan(
////            ImageSpan(this, R.drawable.icon_edit),
////            0,
////            1,
////            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
////        )
////        binding.editText.text = spannable
////
////        Log.d("PNinsertIconDemo", "insertImage: $spannable")
////        binding.editText.setSelection(cursorPosition + 1)
//    }

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