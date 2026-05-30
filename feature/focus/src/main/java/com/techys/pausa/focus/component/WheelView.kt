package com.techys.pausa.focus.component

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.compose.ui.graphics.toArgb
import com.techys.designsystem.theme.Dimen
import com.techys.designsystem.theme.NeonBlue
import java.lang.Integer.parseInt


class WheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    val TAG: String = WheelView::class.java.getSimpleName()
    val OFF_SET_DEFAULT = 1
    var offset = OFF_SET_DEFAULT


    var displayItemCount = 0

    var selectedIndex = 1

    val textSize = 50f
    abstract class OnWheelViewListener {
        abstract fun onSelected(selectedIndex: Int, item: String?)
    }

    private var views: LinearLayout? = null

    init {
        overScrollMode = OVER_SCROLL_NEVER
        init(context)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //Catch touch event if parent also listens to touch events(e.g. bottom sheet)
        parent.requestDisallowInterceptTouchEvent(true);
        return true
    }

    //    String[] items;
    var mitems: MutableList<String?>? = null
    private fun getItems(): List<String?>? {
        return mitems
    }

    fun setItems(list: List<String?>?) {
        if (null == mitems) {
            mitems = ArrayList()
        }
        mitems!!.clear()
        mitems!!.addAll(list!!)

        if (views != null) {
            views!!.removeAllViews()
        }


        for (i in 0 until offset) {
            mitems!!.add(0, "")
            mitems!!.add("")
        }
        initData()
    }





    private fun init(context: Context) {

        offset = 2
        this.isVerticalScrollBarEnabled = false
        views = LinearLayout(context)
        views!!.orientation = LinearLayout.VERTICAL
        this.addView(views)
        scrollerTask = Runnable {
            val newY = scrollY
            if (initialY - newY == 0) { // stopped
                val remainder = initialY % itemHeight
                val divided = initialY / itemHeight
                //                    Log.d(TAG, "initialY: " + initialY);
//                    Log.d(TAG, "remainder: " + remainder + ", divided: " + divided);
                if (remainder == 0) {
                    selectedIndex = divided + offset
                    onSeletedCallBack()
                } else {
                    if (remainder > itemHeight / 2) {
                        this.post(Runnable {
                            this.smoothScrollTo(0, initialY - remainder + itemHeight)
                            selectedIndex = divided + offset + 1
                            onSeletedCallBack()
                        })
                    } else {
                        this.post(Runnable {
                            this.smoothScrollTo(0, initialY - remainder)
                            selectedIndex = divided + offset
                            onSeletedCallBack()
                        })
                    }
                }
            } else {
                initialY = scrollY
                this.postDelayed(scrollerTask, newCheck.toLong())
            }
        }
    }

    var initialY = 0

    var scrollerTask: Runnable? = null
    var newCheck = 50

    fun startScrollerTask() {
        initialY = scrollY
        postDelayed(scrollerTask, newCheck.toLong())
    }

    private fun initData() {
        displayItemCount = offset * 2 + 1
        for (item in mitems!!) {
            views!!.addView(createView(item))
        }
        val params = this.layoutParams
        params.height = itemHeight * displayItemCount
        views?.clipChildren = true
        views?.clipToPadding = true
        this.layoutParams = params
        refreshItemView(0)
    }

    var itemHeight = 0

    private fun createView(item: String?): TextView? {
        val tv = TextView(context)
        tv.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        tv.isSingleLine = true
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

        try {
            parseInt(item!!)
            tv.text = String.format("%02d", item)

        } catch (e: Exception) {
            tv.text = item
        }

        tv.gravity = this.foregroundGravity
        val padding = dip2px(context!!, 15f)
        tv.setPadding(padding, padding / 2, padding, padding / 2)


        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv)
//            val wid = (context as Activity)!!.windowManager.defaultDisplay.width

            views!!.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, itemHeight * displayItemCount)

//            this.layoutParams = LinearLayout.LayoutParams(wid/3, itemHeight * displayItemCount)
        }
        return tv
    }


    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

//        Log.d(TAG, "l: " + l + ", t: " + t + ", oldl: " + oldl + ", oldt: " + oldt);

//        try {
//            Field field = ScrollView.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            OverScroller mScroller = (OverScroller) field.get(this);
//
//
//            if(mScroller.isFinished()){
//                Log.d(TAG, "isFinished...");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        refreshItemView(t)
        scrollDirection = if (t > oldt) {
            SCROLL_DIRECTION_DOWN
        } else {
            SCROLL_DIRECTION_UP
        }
    }

    private fun refreshItemView(y: Int) {
        var position = y / itemHeight + offset
        val remainder = y % itemHeight
        val divided = y / itemHeight
        if (remainder == 0) {
            position = divided + offset
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1
            }

//            if(remainder > itemHeight / 2){
//                if(scrollDirection == SCROLL_DIRECTION_DOWN){
//                    position = divided + offset;
//                    Log.d(TAG, ">down...position: " + position);
//                }else if(scrollDirection == SCROLL_DIRECTION_UP){
//                    position = divided + offset + 1;
//                    Log.d(TAG, ">up...position: " + position);
//                }
//            }else{
////                position = y / itemHeight + offset;
//                if(scrollDirection == SCROLL_DIRECTION_DOWN){
//                    position = divided + offset;
//                    Log.d(TAG, "<down...position: " + position);
//                }else if(scrollDirection == SCROLL_DIRECTION_UP){
//                    position = divided + offset + 1;
//                    Log.d(TAG, "<up...position: " + position);
//                }
//            }
//        }

//        if(scrollDirection == SCROLL_DIRECTION_DOWN){
//            position = divided + offset;
//        }else if(scrollDirection == SCROLL_DIRECTION_UP){
//            position = divided + offset + 1;
        }
        val childSize = views!!.childCount
        for (i in 0 until childSize) {
            val itemView = views!!.getChildAt(i) as TextView ?: return
            if (position == i) {
                itemView.setTextColor(NeonBlue.toArgb())
            } else {
                itemView.setTextColor(Color.GRAY)

            }
        }
    }


    var selectedAreaBorder: IntArray? = null

    private fun obtainSelectedAreaBorder(): IntArray {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = IntArray(2)
            selectedAreaBorder!![0] = itemHeight * offset
            selectedAreaBorder!![1] = itemHeight * (offset + 1)
        }
        return selectedAreaBorder as IntArray
    }


    private var scrollDirection = -1
    private val SCROLL_DIRECTION_UP = 0
    private val SCROLL_DIRECTION_DOWN = 1

    var paint: Paint? = null
    var viewWidth = 0

    override fun setBackgroundDrawable(background: Drawable?) {
        var background = background
        if (viewWidth == 0) {
//            viewWidth = (context as Activity?)!!.windowManager.defaultDisplay.width
            viewWidth = measuredWidth
            Log.d(TAG, "viewWidth: $viewWidth")
        }
        if (null == paint) {
            paint = Paint()
            paint!!.color = NeonBlue.copy(alpha = 0.1f).toArgb()
            paint!!.strokeWidth = dip2px(context, 1f).toFloat()
        }
        background = object : Drawable() {
            override fun draw(canvas: Canvas) {
                canvas.drawRect(
                    0f,
                    obtainSelectedAreaBorder()[0].toFloat(), viewWidth.toFloat(),
                    obtainSelectedAreaBorder()[1].toFloat(), paint!!
                )
            }

            override fun setAlpha(alpha: Int) {}
            override fun setColorFilter(cf: ColorFilter?) {}
            override fun getOpacity(): Int {
                return PixelFormat.UNKNOWN
            }
        }
        super.setBackgroundDrawable(background)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "w: $w, h: $h, oldw: $oldw, oldh: $oldh")
        viewWidth = w
        setBackgroundDrawable(null)
    }


    private fun onSeletedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener!!.onSelected(getSeletedIndex(), mitems!![selectedIndex])
        }
    }

    fun setSelection(position: Int) {
        selectedIndex = position + offset
        post { this.smoothScrollTo(0, position * itemHeight) }
    }

    fun getSeletedItem(): String? {
        return mitems!![selectedIndex]
    }

    fun getSeletedIndex(): Int {
        return selectedIndex - offset
    }


    override fun fling(velocityY: Int) {
        super.fling(velocityY / 3)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            startScrollerTask()
        }
        return super.onTouchEvent(ev)
    }

    private var onWheelViewListener: OnWheelViewListener? = null

    fun getOnWheelViewListener(): OnWheelViewListener? {
        return onWheelViewListener
    }

    fun setOnWheelViewListener(onWheelViewListener: OnWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener
    }


    private fun getViewMeasuredHeight(view: View): Int {
        val width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val expandSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        view.measure(width, expandSpec)
        return view.measuredHeight
    }

    var appSoundEnabled = false

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun update(){
        invalidate()
    }

}