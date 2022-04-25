package com.ahmadfma.intermediate_submission1.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ahmadfma.intermediate_submission1.R
import com.ahmadfma.intermediate_submission1.helper.Validator

class CustomEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var errorButtonImage: Drawable
    constructor(context: Context) : super(context) {init()}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {init()}
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {init()}

    private fun init() {
        errorButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_error) as Drawable
        setOnTouchListener(this)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        hint = when(inputType-1) {
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> context.getString(R.string.input_email)
            InputType.TYPE_TEXT_VARIATION_PASSWORD -> context.getString(R.string.input_password)
            InputType.TYPE_TEXT_FLAG_MULTI_LINE -> context.getString(R.string.description)
            else -> context.getString(R.string.input)
        }

        setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                when(inputType-1) {
                    InputType.TYPE_TEXT_VARIATION_PASSWORD -> {
                        if(text != null && text!!.length < 6)
                            error = context.getString(R.string.error_password)
                    }
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS -> {
                        if(Validator.isEmailValid(text.toString()))
                            error = context.getString(R.string.error_invalid_email)
                    }
                    InputType.TYPE_TEXT_FLAG_MULTI_LINE -> {
                        if(text != null && text!!.isEmpty())
                            error = context.getString(R.string.error_description)
                    }
                }
            }
        }

    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

}