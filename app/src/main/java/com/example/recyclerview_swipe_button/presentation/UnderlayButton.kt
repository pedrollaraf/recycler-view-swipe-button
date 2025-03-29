package com.example.recyclerview_swipe_button.presentation

import com.example.recyclerview_swipe_button.utils.dpToPx
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withTranslation

class UnderlayButton(
    private val text: String,
    @DrawableRes private val imageResId: Int = 0,
    private val backgroundColor: Int,
    private val iconColor: Int? = null,
    private val textColor: Int = Color.BLACK,
    private val textSize: Float = 12f,
    private val textStyle: Int = Typeface.NORMAL,
    private val fontFamily: String? = null,
    private val iconSize: Float = 24f,
    private val iconTextSpacing: Float = 4f,
    private val clickListener: () -> Unit
) {
    private var clickRegion: RectF? = null

    fun onClick(x: Float, y: Float): Boolean {
        return if (clickRegion?.contains(x, y) == true) {
            clickListener()
            true
        } else {
            false
        }
    }


    fun onDraw(c: Canvas, rect: RectF, context: Context) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundColor }
        c.drawRect(rect, paint)

        val margin = context.dpToPx(4).toFloat()
        val innerRect = RectF(
            rect.left + margin,
            rect.top + margin,
            rect.right - margin,
            rect.bottom - margin
        )

        // Configurações do texto
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor
            textSize = context.dpToPx(textSize).toFloat()
            typeface = createTypeface(context)
        }

        // Carrega o ícone
        val icon = if (imageResId != 0) {
            loadIcon(context, context.dpToPx(iconSize).toFloat())
        } else null

        // Configurações de padding e layout
        val horizontalPadding = context.dpToPx(4f)
        val availableTextWidth = (innerRect.width() - 2 * horizontalPadding).coerceAtLeast(0f)

        // Prepara o layout de texto
        val staticLayout = if (availableTextWidth > 0) {
            StaticLayout.Builder.obtain(text, 0, text.length, textPaint, availableTextWidth.toInt())
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setEllipsize(TextUtils.TruncateAt.END)
                .setMaxLines(2)
                .build()
        } else null

        // Calcula dimensões
        val iconHeight = icon?.height?.toFloat() ?: 0f
        val spacing : Float = (if (icon != null) context.dpToPx(iconTextSpacing).toFloat() else 0f)
        val textHeight = staticLayout?.height?.toFloat() ?: 0f
        val totalHeight = iconHeight + spacing + textHeight

        // Centraliza verticalmente
        val startY = innerRect.centerY() - (totalHeight / 2)

        // Desenha o ícone
        icon?.let {
            c.drawBitmap(
                it,
                innerRect.centerX() - it.width / 2,
                startY,
                null
            )
        }

        // Desenha o texto
        staticLayout?.let {
            c.withTranslation(
                innerRect.left + horizontalPadding,
                (startY + iconHeight + spacing).toFloat()
            ) {
                it.draw(this)
            }
        }

        clickRegion = rect
    }

    private fun createTypeface(context: Context): Typeface {
        return fontFamily?.let {
            ResourcesCompat.getFont(context, context.resources.getIdentifier(it, "font", context.packageName))
                ?: Typeface.defaultFromStyle(textStyle)
        } ?: Typeface.defaultFromStyle(textStyle)
    }

    private fun loadIcon(context: Context, sizePx: Float): Bitmap? {
        return ContextCompat.getDrawable(context, imageResId)?.run {
            iconColor?.let {
                setTint(it)
            }
            toBitmap(sizePx.toInt())
        }
    }

    private fun Drawable.toBitmap(size: Int): Bitmap {
        val bitmap = createBitmap(size, size)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, size, size)
        draw(canvas)
        return bitmap
    }
}