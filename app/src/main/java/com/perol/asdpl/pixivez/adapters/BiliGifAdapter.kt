package com.perol.asdpl.pixivez.adapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.ybq.android.spinkit.SpinKitView
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.responses.IllustBean
import com.perol.asdpl.pixivez.responses.UgoiraMetadataResponse
import com.perol.asdpl.pixivez.services.GlideApp
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.Exception

class BiliGifAdapter(layoutResId: Int,files:List<File>, var illustBean: IllustBean, var ugoria: UgoiraMetadataResponse) : BaseQuickAdapter<File, BaseViewHolder>(layoutResId,files) {
    override fun convert(helper: BaseViewHolder, item: File) {
//        val spinKitView = helper.getView<SpinKitView>(R.id.progressbar_loadingx)
//        spinKitView.visibility = View.GONE
//        val path = mContext.cacheDir.path + "/" + illustBean.id + "/" + illustBean.id + ".gif"
//        val height = illustBean.height;
//        val width = illustBean.width;
//        val burstLinker = BurstLinker()
//        val listFiles = item.listFiles()
//        val a=  Observable.create<File> {
//            burstLinker.init(width, height, path)
//            for (i in ugoria.ugoira_metadata.frames.indices) {
//                val futureTarget = GlideApp.with(mContext).asBitmap().load(listFiles[i]).submit()
//                val colorBitmap = futureTarget.get()
//                burstLinker.connect(colorBitmap, BurstLinker.OCTREE_QUANTIZER,
//                        BurstLinker.NO_DITHER, 0, 0, ugoria.ugoira_metadata.frames[0].delay)
//            }
//            it.onNext(File(path))
//        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({},{
//            burstLinker.release()
//            it.printStackTrace()
//        },{
//            burstLinker.release()
//            val imageView = helper.getView<ImageView>(R.id.imageview_gifx)
//            GlideApp.with(imageView.context).load(File(path)).into(imageView)
//        })

    }


}