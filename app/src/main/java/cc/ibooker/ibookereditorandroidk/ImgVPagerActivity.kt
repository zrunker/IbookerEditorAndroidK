package cc.ibooker.ibookereditorandroidk

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cc.ibooker.ibookereditorklib.IbookerEditorScaleImageView
import com.squareup.picasso.Picasso
import java.util.*

/**
 * 图片预览Activity
 *
 *
 * Created by 邹峰立 on 2018/3/13 0013.
 */
class ImgVPagerActivity : AppCompatActivity(), View.OnClickListener {
    private var currentPath: String? = null
    private var currentPosition: Int = 0
    private var imgAllPathList: ArrayList<String>? = null

    private var mViewPager: ViewPager? = null
    private var mAdapter: ImgVPagerAdapter? = null
    private var indicatorTv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imgvpger)

        // 获取上一个界面传值
        currentPath = intent.getStringExtra("currentPath")
        currentPosition = intent.getIntExtra("position", 0)
        imgAllPathList = intent.getStringArrayListExtra("imgAllPathList")

        // 初始化
        if (imgAllPathList != null && imgAllPathList!!.size > 0)
            init()
    }

    // 初始化
    private fun init() {
        mViewPager = findViewById(R.id.id_viewpager)
        val shareImg = findViewById<ImageView>(R.id.img_share)
        shareImg.setOnClickListener(this)
        val leftImg = findViewById<ImageView>(R.id.img_left)
        leftImg.setOnClickListener(this)
        val rightImg = findViewById<ImageView>(R.id.img_right)
        rightImg.setOnClickListener(this)
        indicatorTv = findViewById(R.id.tv_indicator)

        // 初始化数据
        initVpData()
        mViewPager!!.currentItem = currentPosition
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                updateIndicatorTv(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        updateIndicatorTv(currentPosition)
    }

    // 格式化indicatorTv
    private fun updateIndicatorTv(currentPosition: Int) {
        val indicatorTip = (currentPosition + 1).toString() + "/" + imgAllPathList!!.size
        indicatorTv!!.text = indicatorTip
    }

    // 初始化ViewPager数据
    private fun initVpData() {
        if (imgAllPathList != null && imgAllPathList!!.size > 0) {
            val imageViews = ArrayList<IbookerEditorScaleImageView>()
            // 获取图片资源，并保存到imageViews中
            for (i in imgAllPathList!!.indices) {
                val imageView = IbookerEditorScaleImageView(this)
                imageView.setOnMyClickListener(object : IbookerEditorScaleImageView.OnMyClickListener {
                    override fun onMyClick(v: View) {// 点击事件
                        Toast.makeText(this@ImgVPagerActivity, "图片点击事件：" + i, Toast.LENGTH_LONG).show()
                        finish()
                    }
                })
                imageView.setOnMyLongClickListener(object : IbookerEditorScaleImageView.OnMyLongClickListener {
                    override fun onMyLongClick(v: View) {// 长按事件
                        Toast.makeText(this@ImgVPagerActivity, "图片长按事件：" + i, Toast.LENGTH_LONG).show()
                    }
                })
                val imgPath = imgAllPathList!![i]
                Picasso.get().load(imgPath).into(imageView)
                imageViews.add(imageView)
            }
            // 刷新数据
            setAdapter(imageViews)
        }
    }

    // 自定义setAdapter
    private fun setAdapter(list: ArrayList<IbookerEditorScaleImageView>) {
        if (mAdapter == null) {
            mAdapter = ImgVPagerAdapter(list)
            mViewPager!!.adapter = mAdapter
        } else {
            mAdapter!!.reflashData(list)
        }
    }

    // 点击事件监听
    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_share// 分享
            -> Toast.makeText(this@ImgVPagerActivity, "执行分享", Toast.LENGTH_LONG).show()
            R.id.img_left// 左移图片
            -> mViewPager!!.currentItem = if (currentPosition == 0) imgAllPathList!!.size - 1 else currentPosition - 1
            R.id.img_right// 右移事件
            -> mViewPager!!.currentItem = if (currentPosition == imgAllPathList!!.size - 1) 0 else currentPosition + 1
        }
    }
}
