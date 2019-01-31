package com.phoenixoverlord.eventapp.utils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.design.button.MaterialButton
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.Checkable
import com.phoenixoverlord.eventapp.R

class LikeButton(context : Context, attributes: AttributeSet) : MaterialButton(context, attributes), Checkable {

    var likeCount : Int = 0
    set(value) {
        field = value
        text = "$field LIKES"
    }
    private var checked : Boolean = false
    private var onCheckedChange : ((likeButton : LikeButton, checked: Boolean) -> Unit)? = null

    override fun isChecked() = checked

    override fun setChecked(checked: Boolean) {
        this.checked = checked

        val colorAccent = ContextCompat.getColor(context, R.color.colorAccent)

        if (checked) {
            background.setColorFilter(colorAccent, PorterDuff.Mode.SRC)
            setTextColor(Color.WHITE)
        }
        else {
            background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC)
            setTextColor(colorAccent)
        }

        onCheckedChange?.invoke(this, checked)
    }

    override fun toggle() {
        // Do not change into property access syntax
        setChecked(!checked)
    }

    fun setOnCheckedChangedListener(onCheckedChange : (likeButton : LikeButton, checked : Boolean) -> Unit) {
        this.onCheckedChange = onCheckedChange
        super.setOnClickListener { toggle() }
    }
}

