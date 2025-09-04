package com.example.famchat.extensions

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.famchat.R
import com.example.famchat.databinding.FcCustomToastErrorBinding
import com.example.famchat.databinding.FcCustomToastSuccessBinding
import com.example.famchat.viewmodel.GlobalValue
import java.lang.reflect.Method
import kotlin.math.max
import kotlin.math.min

@SuppressLint("ClickableViewAccessibility")
fun View.autoHideKeyboard(activity: Activity, viewException: MutableList<View>) {
    if (this !is EditText) {
        this.setOnTouchListener { _: View?, _: MotionEvent? ->
            activity.hideSoftKeyboard()
            false
        }
    }
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val innerView = this.getChildAt(i)
            for (aViewException in viewException) {
                if (aViewException.id != innerView.id) {
                    innerView.autoHideKeyboard(activity)
                }
            }
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.autoHideKeyboard(activity: Activity) {
    //Set up touch listener for non-text box views to hide keyboard.
    if (this !is EditText) {
        this.setOnTouchListener { _: View?, _: MotionEvent? ->
            activity.hideSoftKeyboard()
            false
        }
    }

    //If a layout container, iterate over children and seed recursion.
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val innerView = this.getChildAt(i)
            innerView.autoHideKeyboard(activity)
        }
    }
}

class SafeClickListener(
    private var defaultInterval: Long = 500L,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom
) {
    if (layoutParams is MarginLayoutParams) {
        val p = layoutParams as MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        requestLayout()
    }
}

fun View.asRectF() = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
fun View.asRect() = Rect(left, top, right, bottom)

fun Spinner.setupAdapterSelect(
    lstData: MutableList<String>,
    dataDefault: String = "",
    itemClick: (Int) -> Unit
) {
    val adapter: ArrayAdapter<String> = ArrayAdapter(
        this.context,
        android.R.layout.simple_spinner_item,
        lstData
    )
    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
    this.adapter = adapter
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            itemClick(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // onNothingSelected
        }
    }

    val indexFirst = lstData.indexOfFirst { it == dataDefault }
    this.setSelection(max(indexFirst, 0))
}

fun View.showPopupMenu(
    lstData: MutableList<String>,
    itemClick: (Int) -> Unit
) {
    val popupMenu = PopupMenu(this.context, this)
    lstData.forEach {
        popupMenu.menu.add(it)
    }
    popupMenu.setOnMenuItemClickListener { item ->
        val index = lstData.indexOf(item.title)
        itemClick(index)
        true
    }

    try {
        val popupWindowField = PopupMenu::class.java.getDeclaredField("mPopup")
        popupWindowField.isAccessible = true
        val popupWindow = popupWindowField.get(popupMenu) as PopupWindow
        popupWindow.width = this.width
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    popupMenu.show()
}

fun View.showPopupWindow(
    lstData: MutableList<String>,
    activity: Activity,
    itemClick: (Int) -> Unit
) {
    val popupWindow = PopupWindow(context)
    val listView = ListView(context)
    val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, lstData)
    listView.adapter = adapter

    listView.setOnItemClickListener { parent, view, position, id ->
        val index = position
        itemClick(index)
        popupWindow.dismiss()
    }

    val displayMetrics = DisplayMetrics()
    (activity.windowManager.defaultDisplay).getMetrics(displayMetrics)
    popupWindow.width = displayMetrics.widthPixels / 2
    if (lstData.size > 8) {
        popupWindow.height =
            displayMetrics.heightPixels / 2
    }


    try {
        val whiteBackgroundWithShadow = GradientDrawable().apply {
            setColor(Color.WHITE)
            cornerRadius = 8f
        }
        popupWindow.elevation = 18f
        popupWindow.setBackgroundDrawable(whiteBackgroundWithShadow)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    popupWindow.isOutsideTouchable = true
    popupWindow.isFocusable = true
    popupWindow.contentView = listView
    popupWindow.showAsDropDown(this)
}

fun View.showCustomPopupWindow(
    activity: Activity,
    layoutResId: Int,
    onBind: (View, PopupWindow) -> Unit
) {
    val inflater = LayoutInflater.from(context)
    val popupView = inflater.inflate(layoutResId, null)

    val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    popupWindow.width = displayMetrics.widthPixels / 2

    popupWindow.isOutsideTouchable = true
    popupWindow.isFocusable = true

    try {
        val background = GradientDrawable().apply {
            setColor(ContextCompat.getColor(context, R.color.color_card_view))
            cornerRadius = 16f
        }
        popupWindow.elevation = 16f
        popupWindow.setBackgroundDrawable(background)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    onBind(popupView, popupWindow)

    popupWindow.showAsDropDown(this)
}


inline fun <reified VB : ViewBinding> View.showCustomPopupWindowBinding(
    activity: Activity,
    popupWidth: Int? = null,
    crossinline onBind: (VB, PopupWindow) -> Unit
) {
    val inflateMethod: Method = VB::class.java.getMethod("inflate", LayoutInflater::class.java)
    val inflater = LayoutInflater.from(context)
    val binding = inflateMethod.invoke(null, inflater) as VB

    val popupWindow = PopupWindow(
        binding.root,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val screenHeight = displayMetrics.heightPixels

    popupWindow.width = popupWidth ?: (displayMetrics.widthPixels / 2)

    popupWindow.isOutsideTouchable = true
    popupWindow.isFocusable = true

    try {
        val background = GradientDrawable().apply {
            setColor(ContextCompat.getColor(context, R.color.color_card_view))
            cornerRadius = 32f
        }
        popupWindow.elevation = 32f
        popupWindow.setBackgroundDrawable(background)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    onBind(binding, popupWindow)

//    popupWindow.showAsDropDown(this, 0, activity.pxToDp(40))

    // Đo kích thước popup (bắt buộc để hiển thị đúng nếu cần show lên trên)
    binding.root.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    val popupHeight = binding.root.measuredHeight

    // Tính vị trí view
    val location = IntArray(2)
    getLocationOnScreen(location)
    val viewX = location[0]
    val viewY = location[1]

    // Nếu ở nửa dưới màn hình thì show lên trên
    if (viewY > screenHeight / 2) {

        popupWindow.height = min(popupHeight, viewY) - activity.pxToDp(30)

        popupWindow.showAtLocation(
            this,
            Gravity.NO_GRAVITY,
            viewX,
            viewY - popupHeight
        )
    } else {
        // Mặc định show dưới
        popupWindow.showAsDropDown(this, 0, activity.pxToDp(40))
    }
}


fun View.loadBitmapFromView(): Bitmap? {
    try {
        this.buildDrawingCache()
        val b1 = this.drawingCache
        b1?.let {
            val b = b1.copy(Bitmap.Config.ARGB_8888, false)
            this.destroyDrawingCache()
            return b
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return null
}

fun View.getBitmap(): Bitmap? {
    kotlin.runCatching {
        val bitmap = Bitmap.createBitmap(
            this.width, this.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        return bitmap
    }.onFailure {
        it.printStackTrace()
    }
    return null
}

fun View.showView(isTopView: Boolean, duration: Long = 300) {
    if (!this.isShown) {
        this.visibility = View.VISIBLE
        YoYo.with(if (isTopView) Techniques.SlideInDown else Techniques.SlideInUp)
            .duration(duration)
            .playOn(this)
    }
}

fun View.fadeIn(duration: Long = 300) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .setListener(null)
}

fun View.fadeOut(duration: Long = 300) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            visibility = View.GONE
        }
}



fun View.setEnableView(isEnable: Boolean) {
    this.isEnabled = isEnable
    this.alpha = if (isEnable) {
        1.0f
    } else {
        0.3f
    }
}

@SuppressLint("ClickableViewAccessibility")
fun View.showToastWindow(message: String) {
    val duration = GlobalValue.appConfigData?.timeAutoCloseToast?.seconds.safeParseInt(
        3
    ) * 1000
    val inflater = LayoutInflater.from(context)
    val contentLayout = inflater.inflate(
        R.layout.ca_custom_toast,
        null
    ) as RelativeLayout
    val popupWindow = PopupWindow(context)
    val textViewMessage =
        contentLayout.findViewById<TextView>(R.id.textViewMessage)
    textViewMessage.text = message

    popupWindow.isOutsideTouchable = true
    popupWindow.isFocusable = true
    popupWindow.contentView = contentLayout


    popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val handler = Handler()

    val dismissPopup = Runnable {
        popupWindow.dismiss()
    }

    kotlin.runCatching {
        popupWindow.showAtLocation(this, Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 200)
    }.onFailure {
        it.printStackTrace()
        return
    }

    handler.postDelayed(dismissPopup, duration.toLong()) // 5000 milliseconds = 5 seconds


    // Bắt sự kiện gạt sang trái hoặc phải để tắt popup window
    contentLayout.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_MOVE -> {
                val x = event.rawX
                val y = event.rawY
                val threshold =
                    resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)

                val popupLocation = IntArray(2)
                contentLayout.getLocationOnScreen(popupLocation)
                val popupLeft = popupLocation[0]
                val popupTop = popupLocation[1]
                val popupRight = popupLeft + contentLayout.width
                val popupBottom = popupTop + contentLayout.height

                if (x > popupLeft + threshold && x < popupRight - threshold &&
                    y > popupTop + threshold && y < popupBottom - threshold
                ) {
                    val translationX = x - contentLayout.width / 2
                    contentLayout.translationX = translationX
                }
                true
            }

            MotionEvent.ACTION_UP -> {
                val x = event.rawX
                val width = contentLayout.width
                val threshold = width / 3 // Số lượng pixel cần di chuyển để tắt popup window

                if (x < threshold || x > width - threshold) {
                    val translateAnimation = TranslateAnimation(
                        0f, if (x < threshold) -width.toFloat() else width.toFloat(),
                        0f, 0f
                    )
                    translateAnimation.duration = 300

                    val animationSet = AnimationSet(true)
                    animationSet.addAnimation(translateAnimation)
                    animationSet.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {}

                        override fun onAnimationEnd(animation: Animation?) {
                            popupWindow.dismiss()
                        }

                        override fun onAnimationRepeat(animation: Animation?) {}
                    })

                    contentLayout.startAnimation(animationSet)
                } else {
                    val initialX = 0 // Vị trí ban đầu của popupWindow theo trục X
                    val initialY = 200 // Vị trí ban đầu của popupWindow theo trục Y

                    popupWindow.update(initialX, initialY, popupWindow.width, popupWindow.height)
                    contentLayout.translationX = 0f
                }
                true
            }

            else -> false
        }
    }
}


fun View.showToastError(message: String, iconResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    val inflater = LayoutInflater.from(context)
    val binding = FcCustomToastErrorBinding.inflate(inflater)

    binding.toastText.text = message
    binding.toastIcon.setImageDrawable(ContextCompat.getDrawable(context, iconResId))

    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels
    val targetWidth = (screenWidth * 0.9).toInt()

    val layoutParams = FrameLayout.LayoutParams(
        targetWidth,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    binding.root.layoutParams = layoutParams

    val container = FrameLayout(context)
    container.addView(binding.root)

    val toast = Toast(context)
    toast.duration = duration
    toast.view = container
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 50)
    toast.show()
}

fun View.showToastSuccess(message: String, iconResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    val inflater = LayoutInflater.from(context)
    val binding = FcCustomToastSuccessBinding.inflate(inflater)

    binding.toastText.text = message
    binding.toastIcon.setImageDrawable(ContextCompat.getDrawable(context, iconResId))

    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels
    val targetWidth = (screenWidth * 0.9).toInt()

    val layoutParams = FrameLayout.LayoutParams(
        targetWidth,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    binding.root.layoutParams = layoutParams

    val container = FrameLayout(context)
    container.addView(binding.root)

    val toast = Toast(context)
    toast.duration = duration
    toast.view = container
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 50)
    toast.show()
}




fun View.beGone() {
    this.visibility = View.GONE
}

fun View.beInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.beVisible() {
    this.visibility = View.VISIBLE
}

fun View.beGoneOrVisible(isGone: Boolean) {
    this.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun View.beInvisibleOrVisible(isGone: Boolean) {
    this.visibility = if (isGone) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

fun LinearLayout.setupIndicator(
    count: Int,
    @DrawableRes unselectedRes: Int = R.drawable.indicator_dot_unselected,
    spacing: Int = 8,
    size: Int = 20
) {
    this.removeAllViews()
    for (i in 0 until count) {
        val dot = ImageView(this.context).apply {
            setImageResource(unselectedRes)
            layoutParams = LinearLayout.LayoutParams(size, size).apply {
                setMargins(spacing, 0, spacing, 0)
            }
        }
        this.addView(dot)
    }
}

fun LinearLayout.updateIndicator(
    selectedPosition: Int,
    @DrawableRes selectedRes: Int = R.drawable.indicator_dot_selected,
    @DrawableRes unselectedRes: Int = R.drawable.indicator_dot_unselected
) {
    for (i in 0 until this.childCount) {
        val dot = this.getChildAt(i) as? ImageView ?: continue
        dot.setImageResource(if (i == selectedPosition) selectedRes else unselectedRes)
    }
}

fun LinearLayout.updateIndicatorAnimated(
    selectedPosition: Int,
    @DrawableRes selectedRes: Int = R.drawable.indicator_dot_selected,
    @DrawableRes unselectedRes: Int = R.drawable.indicator_dot_unselected
) {
    for (i in 0 until this.childCount) {
        val dot = this.getChildAt(i) as? ImageView ?: continue

        dot.setImageResource(if (i == selectedPosition) selectedRes else unselectedRes)

        val scale = if (i == selectedPosition) 1.5f else 1f
        dot.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(200)
            .start()
    }
}

fun ViewPager2.setupCarouselStyle(
    pageMarginPx: Int = context.resources.getDimensionPixelOffset(R.dimen._8sdp),
    offsetPx: Int = context.resources.getDimensionPixelOffset(R.dimen._24sdp)
) {
    offscreenPageLimit = 3
    clipToPadding = false
    clipChildren = false
    setPadding(offsetPx, 0, offsetPx, 0)

    val recyclerView = getChildAt(0) as RecyclerView
    if (recyclerView.itemDecorationCount == 0) {
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val itemCount = state.itemCount
                outRect.left = if (position == 0) 0 else pageMarginPx / 2
                outRect.right = if (position == itemCount - 1) 0 else pageMarginPx / 2
            }
        })
    }

    setPageTransformer { page, position ->
        val scale = 0.95f + (1 - kotlin.math.abs(position)) * 0.05f
        page.scaleY = scale
    }
}
