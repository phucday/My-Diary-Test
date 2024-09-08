package com.example.mydiarytest.UI

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.size
import com.example.mydiarytest.R
import com.example.mydiarytest.StickerView.StickerImageView
import com.example.mydiarytest.databinding.ActivityMainBinding
import com.example.mydiarytest.databinding.PopupWindowLayoutBinding
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var modeEyeProtection = false
    private var stickToTop = false
    private var gravityFamily = Gravity.LEFT
    private var listEditText = ArrayList<EditText>()
    private var listImageView = ArrayList<ImageView>()
    private var idCurEditText: Int ?= 0


    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(this@MainActivity, "Please give us permission", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val inputStream = contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                Log.d("checkSaveImgBitmapToByteArray: ", bitmapToByteArray(bitmap).toString())
                Log.d(
                    "checkSaveImgByteArraytoBitMap: ",
                    byteArrayToBitmap(bitmapToByteArray(bitmap)).toString()
                )
//                addNewImageView(bitmap)
                getCurEdtByIdAndSplitByImage(bitmap)

                addEdiText()
            }
        }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        clickOptionsDiary()
        clickEyeProtection()

        clickInsertImage()
        addStickerImage()

//        binding.btnAddBulletEdt.setOnClickListener{ addBulletEditText() }
    }


    private fun addStickerImage() {
        val canvas: FrameLayout = binding.layoutSticker
        binding.btnAddSticker.setOnClickListener {
            val ivSticker = StickerImageView(this)
            ivSticker.setImageResource(R.drawable.panda)
            canvas.addView(ivSticker)

            // wait for 'canvas.addView(ivSticker)' done
            ivSticker.post {
                ivSticker.x = 400f
                ivSticker.y = binding.layoutNote.scrollY + 500f
            }

            Log.e("ThoNH", "${ivSticker.top}")

            Log.d("takeIndex: ", "onCreate: ${binding.layoutTextAndImage.size}")

            for(i in listEditText){
                Log.d(
                    "NHPindexOfChild",
                    "addStickerImage: ${binding.layoutTextAndImage.indexOfChild(i)}" +
                            " and id: ${i.id}, text: ${i.text}"
                )
            }
            for(i in listImageView){
                Log.d(
                    "NHPindexOfChild",
                    "addStickerImage: ${binding.layoutTextAndImage.indexOfChild(i)}" +
                            " and id: ${i.id}, bitmap: ${(i.drawable as BitmapDrawable).bitmap.toString()}"
                )
            }

        }
    }

    private fun clickInsertImage() {
        addEdiText()
        clickToOpenGallery()
    }
    private fun clickToOpenGallery(){
        binding.btnInsertImage.setOnClickListener {

            if (!checkReadImagePermission(this)) {
                requestReadPermission()
            } else {
                openGallery()
            }
        }
    }

    private fun checkReadImagePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) context.checkSelfPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        else context.checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private fun roleEdt(edt: EditText){
        edt.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                Log.d("EditTextContents", "ID: ${edt.id}, Text: ${edt.text}")

                // add EditText
//                if (!listEditText.contains(addEditText)) {
//                    listEditText.add(addEditText)
//                }
//                for (i in listEditText) {
//                    Log.d("ListEditTextContents", "ID: ${i.id}, Text: ${i.text}")
//                }

            }else{
                idCurEditText = edt.id
                Log.d("idCurEditText: ", "ID: $idCurEditText")
                Log.d("curPo: ", ": ${edt.selectionStart}")

                if (!listEditText.contains(edt)) {
                    listEditText.add(edt)
                }
                for (i in listEditText) {
                    Log.d("ListEditTextContents", "ID: ${i.id}, Text: ${i.text}")
                }

            }
        }

        binding.btnAddBulletEdt.setOnClickListener {
            // Lấy EditText hiện tại dựa trên idCurEditText
            val currentEditText = listEditText.find { it.id == idCurEditText }
            currentEditText?.let {
                val cursorPosition = it.selectionStart
                Log.d("NHPcursorPosition", "cursorPosition: $cursorPosition")

                val layout = it.layout
                if (layout != null) {
                    val line = layout.getLineForOffset(cursorPosition)
                    val lineStart = layout.getLineStart(line)

                    // Thêm bullet nếu không có
                    if (!it.text.startsWith("• ", lineStart)) {
                        it.text.insert(lineStart, "• ")
                        it.setSelection(cursorPosition + 2)  // Dịch con trỏ về sau ký tự "• "
                    } else {

                        // Chèn bullet vào đầu dòng hiện tại
                        it.text.replace(lineStart, lineStart + 2, "")

                        it.setSelection(cursorPosition - 2)
                    }
                }
            }
        }

        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {


                edt.setOnKeyListener { _, keyCode, event ->
                    if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                        val cursorPosition = edt.selectionStart
                        Log.d("curPoafterTextChanged: ", ": ${edt.selectionStart}")
                        val layout = edt.layout

                        if (layout != null) {
                            val currentLine = layout.getLineForOffset(cursorPosition)

                            val lineStart = layout.getLineStart(currentLine)
                            val lineEnd = layout.getLineEnd(currentLine)

                            val currentLineText =
                                edt.text?.substring(lineStart, lineEnd) ?: ""

                            if (currentLineText.trim().startsWith("•")) {
                                val bullet = "\n• "
                                val currentText = edt.text.toString()

                                edt.setText(
                                    currentText.substring(
                                        0,
                                        cursorPosition
                                    ) + bullet + currentText.substring(cursorPosition)
                                )

                                edt.setSelection(cursorPosition + bullet.length)
                                return@setOnKeyListener true
                            }
                        }
                    }
                    false
                }
            }
        })
    }

    private fun addEdiText() {
        val addEditText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            hint = "continue type something\uD83D\uDE06\uD83D\uDE06\uD83D\uDE06\uD83D\uDE06"
//            setBackgroundResource(R.color.white)
            gravity = gravityFamily
            backgroundTintList =
                ContextCompat.getColorStateList(context, android.R.color.transparent)
            id = View.generateViewId()
            Log.d("checkID: ", "addEdiText: $id")
        }
        binding.layoutTextAndImage.addView(addEditText)
        listEditText.add(addEditText)
        roleEdt(addEditText)
    }


    private fun getCurEdtByIdAndSplitByImage(bitmap: Bitmap) {
        val currentEditText = listEditText.find { it.id == idCurEditText }
        currentEditText?.let { editText ->
            val cursorPosition = editText.selectionStart
            val text = editText.text.toString()

            val textBeforeCursor = text.substring(0, cursorPosition)
            val textAfterCursor = text.substring(cursorPosition)

            // Remove current EditText
            binding.layoutTextAndImage.removeView(editText)
            listEditText.remove(editText)

            // Create new EditText for text before cursor
            val beforeEditText = EditText(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                id = View.generateViewId()
                Log.d("NHPcheckIDbeforeEditText: ", "$id")
                backgroundTintList =
                    ContextCompat.getColorStateList(context, android.R.color.transparent)
                setText(textBeforeCursor)
            }

            roleEdt(beforeEditText)

            // Create new EditText for text after cursor
            val afterEditText = EditText(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                id = View.generateViewId()
                Log.d("NHPcheckIDafterEditText: ", "$id")
                backgroundTintList =
                    ContextCompat.getColorStateList(context, android.R.color.transparent)
                setText(textAfterCursor)
            }
            roleEdt(afterEditText)
            // Add new EditTexts to layout
            binding.layoutTextAndImage.addView(beforeEditText)

            // Add ImageView between the EditTexts
            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                id = View.generateViewId()
                setImageBitmap(bitmap)
            }
            binding.layoutTextAndImage.addView(imageView)
            listImageView.add(imageView)

            // Finally, add the afterEditText
            binding.layoutTextAndImage.addView(afterEditText)
            idCurEditText = afterEditText.id
        }
    }

    private fun addNewImageView(bitmap: Bitmap) {
        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setImageBitmap(bitmap)
        }
        binding.layoutTextAndImage.addView(imageView)
    }

    private fun clickEyeProtection() {
        binding.eyeProtection.setOnClickListener {
            if (modeEyeProtection) {
                binding.viewEyeProtection.visibility = View.GONE
                binding.eyeProtection.alpha = 1f
                binding.eyeProtection.setImageResource(R.drawable.icon_eye_protection)

            } else {
                binding.viewEyeProtection.visibility = View.VISIBLE
                binding.viewEyeProtection.setBackgroundColor(getColor(R.color.white))
                binding.eyeProtection.alpha = 0.5f
                binding.eyeProtection.setImageResource(R.drawable.icon_eye_protection)
            }
            modeEyeProtection = !modeEyeProtection
        }
    }

    private fun clickOptionsDiary() {
        binding.optionsDiary.setOnClickListener { showMenuPopUp(binding.optionsDiary) }
    }

    private fun showMenuPopUp(anchorView: View) {
        val binding = PopupWindowLayoutBinding.inflate(layoutInflater)
        binding.itemSwitch.isChecked = stickToTop

        val popupWindow = PopupWindow(
            binding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        // Show PopupWindow beside anchorView
        popupWindow.showAsDropDown(anchorView)

        binding.deleteDiaryLayout.setOnClickListener {
            Toast.makeText(this, "Delete Diary", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        binding.shareDiaryLayout.setOnClickListener {
            Toast.makeText(this, "Share Diary", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        binding.exportPdfLayout.setOnClickListener {
            Toast.makeText(this, "Export PDF", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        // Handle Switch toggle
        binding.itemSwitch.setOnCheckedChangeListener { _, isChecked ->
            stickToTop = isChecked
            if (isChecked) {
                Toast.makeText(this, "Stick to Top Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Stick to Top Disabled", Toast.LENGTH_SHORT).show()
            }
            popupWindow.dismiss()
        }
        binding.stickToTopLayout.setOnClickListener {
            binding.itemSwitch.isChecked = !binding.itemSwitch.isChecked
            stickToTop = binding.itemSwitch.isChecked
            if (binding.itemSwitch.isChecked) {
                Toast.makeText(this, "OnClick Stick to Top Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "OnClick Stick to Top Disabled", Toast.LENGTH_SHORT).show()
            }
            popupWindow.dismiss()
        }
    }
}