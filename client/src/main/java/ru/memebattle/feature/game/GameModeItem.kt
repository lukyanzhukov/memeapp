package ru.memebattle.feature.game

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_game_mode.view.*
import ru.memebattle.R

class GameModeItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val attrsTypedArray: TypedArray

    init {
        val layout = R.layout.item_game_mode
        val styleable = R.styleable.GameModeItemButton
        LayoutInflater.from(context).inflate(layout, this, true)
        attrsTypedArray = context.theme.obtainStyledAttributes(attrs, styleable, 0, 0)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        item_banner.setImageResource(
            attrsTypedArray.getResourceId(R.styleable.GameModeItemButton_banner, -1)
        )
        item_title.setTextColor(
            attrsTypedArray.getColor(
                R.styleable.GameModeItemButton_titleColor,
                DEFAULT_COLOR
            )
        )
        item_title.text = attrsTypedArray.getString(R.styleable.GameModeItemButton_title)
    }

    private companion object {
        const val DEFAULT_COLOR = 16744542
    }
}
