package com.example.mydiarytest.UI

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mydiarytest.R

// dùng webview với https://developer.mozilla.org/en-US/docs/Web/HTML/Element/ul hỗ trợ
// ko đổi đc bullet
// có add file audio mp3, add video mp4
// có thay đôỉ font, bg trên từng kí tự
/* -> nếu bullet là icon -> làm như cách ban đầu
   -> nếu bullet là imageView ->
* */

class RichEditorActivity : AppCompatActivity() {

    private lateinit var mEditor: com.example.mydiarytest.Utils.RichEditor
    private lateinit var mPreview: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rich_editor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        mEditor = findViewById(R.id.editor)
        mEditor.setEditorHeight(200)
        mEditor.setEditorFontSize(22)
        mEditor.setEditorFontColor(Color.BLACK)
        mEditor.setPadding(10, 10, 10, 10)
        mEditor.setPlaceholder("Insert text here...")

        mPreview = findViewById(R.id.preview)
        mEditor.setOnTextChangeListener { text -> mPreview.text = text }

        findViewById<View>(R.id.action_undo).setOnClickListener { mEditor.undo() }
        findViewById<View>(R.id.action_redo).setOnClickListener { mEditor.redo() }
        findViewById<View>(R.id.action_bold).setOnClickListener { mEditor.setBold() }
        findViewById<View>(R.id.action_italic).setOnClickListener { mEditor.setItalic() }
        findViewById<View>(R.id.action_subscript).setOnClickListener { mEditor.setSubscript() }
        findViewById<View>(R.id.action_superscript).setOnClickListener { mEditor.setSuperscript() }
        findViewById<View>(R.id.action_strikethrough).setOnClickListener { mEditor.setStrikeThrough() }
        findViewById<View>(R.id.action_underline).setOnClickListener { mEditor.setUnderline() }
        findViewById<View>(R.id.action_heading1).setOnClickListener { mEditor.setHeading(1) }
        findViewById<View>(R.id.action_heading2).setOnClickListener { mEditor.setHeading(2) }
        findViewById<View>(R.id.action_heading3).setOnClickListener { mEditor.setHeading(3) }
        findViewById<View>(R.id.action_heading4).setOnClickListener { mEditor.setHeading(4) }
        findViewById<View>(R.id.action_heading5).setOnClickListener { mEditor.setHeading(5) }
        findViewById<View>(R.id.action_heading6).setOnClickListener { mEditor.setHeading(6) }

        findViewById<View>(R.id.action_txt_color).setOnClickListener(object : View.OnClickListener {
            var isChanged = false
            override fun onClick(v: View) {
                mEditor.setTextColor(if (isChanged) Color.BLACK else Color.RED)
                isChanged = !isChanged
            }
        })

        findViewById<View>(R.id.action_bg_color).setOnClickListener(object : View.OnClickListener {
            var isChanged = false
            override fun onClick(v: View) {
                mEditor.setTextBackgroundColor(if (isChanged) Color.TRANSPARENT else Color.YELLOW)
                isChanged = !isChanged
            }
        })

        findViewById<View>(R.id.action_indent).setOnClickListener { mEditor.setIndent() }
        findViewById<View>(R.id.action_outdent).setOnClickListener { mEditor.setOutdent() }
        findViewById<View>(R.id.action_align_left).setOnClickListener { mEditor.setAlignLeft() }
        findViewById<View>(R.id.action_align_center).setOnClickListener { mEditor.setAlignCenter() }
        findViewById<View>(R.id.action_align_right).setOnClickListener { mEditor.setAlignRight() }
        findViewById<View>(R.id.action_blockquote).setOnClickListener { mEditor.setBlockquote() }
        findViewById<View>(R.id.action_insert_bullets).setOnClickListener {
            mEditor.setBullets()
            mEditor.setBulletsBackground("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDWaYnMiphRaf3ErObtuqOhN3ls7dZezZ5Iw&s")
        }

        findViewById<View>(R.id.action_insert_image).setOnClickListener {
            mEditor.insertImage(
                "https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                "dachshund", 320
            )
        }


        findViewById<View>(R.id.action_insert_audio).setOnClickListener {
            mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3")
        }

        findViewById<View>(R.id.action_insert_video).setOnClickListener {
            mEditor.insertVideo(
                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4",
                360
            )
        }



    }
}