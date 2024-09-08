package com.example.mydiarytest.bullet;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.mydiarytest.R;

public class CustomEditTextWithBullets extends androidx.appcompat.widget.AppCompatEditText {

    Context mContext;
    Typeface mTypeface;

    public CustomEditTextWithBullets(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public CustomEditTextWithBullets(Context context, AttributeSet attrs) {
        this(context, attrs,  R.attr.editTextStyle);
    }

    public CustomEditTextWithBullets(Context context) {
        this(context, null,  R.attr.editTextStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (lengthAfter > lengthBefore) {
            if (text.toString().length() == 1) {
                text = "\uD83D " + text;
                setText(text);
                setSelection(getText().length());
            }
            if (text.toString().endsWith("\n")) {
                text = text.toString().replace("\n", "\n• ");
                text = text.toString().replace("• •", "•");
                setText(text);
                setSelection(getText().length());
            }
        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

//        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
//
//        List<ItemText> xx = new List();
//
//        String bulletMark = "߷";
//        String fullContent = "sfsdfjsdjfkdf\n߷fsjflsjdfksdjf\n߷sfsdfsdkfjkkj";
//        List<String> rows = Arrays.asList(fullContent.split("\n"));
//        rows.forEach(s -> {
//            if (s.startsWith(bulletMark)){
//                String normalizeContent = s.replace(bulletMark, "");
//                SpannableString string = new SpannableString(normalizeContent);
//                string.setSpan(new ImageSpan(this, R.mipmap.ic_launcher), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                spannableBuilder.append(string);
//            }else {
//                spannableBuilder.append(s);
//            }
//        });
//
//        EditText editText = new EditText();
//        editText.setText(spannableBuilder.toString());

    }
}
