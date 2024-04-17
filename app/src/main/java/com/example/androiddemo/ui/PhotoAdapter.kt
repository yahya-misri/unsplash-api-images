package com.example.androiddemo.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.DisplayMetrics
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.R
import com.example.androiddemo.databinding.ItemHomeLayoutBinding
import com.example.androiddemo.model.ResponseMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class PhotoAdapter(var context: Context) : PagingDataAdapter<ResponseMain, PhotoAdapter.PhotoHolder>(
    diffCallback
) {
    var widthPixels = 0
    private lateinit var imageLoader: ImageLoader

    init {
        val metrics = DisplayMetrics()
        (context as HomeActivity).windowManager.defaultDisplay.getMetrics(metrics)
        widthPixels = metrics.widthPixels
        imageLoader = ImageLoader(context)

    }
    private val cache: LruCache<String, Bitmap> =
        object : LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }

    class PhotoHolder(var itemHomeLayoutBinding: ItemHomeLayoutBinding) :
        RecyclerView.ViewHolder(itemHomeLayoutBinding.root)

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        var w = ((widthPixels) / 2)
        val params = FrameLayout.LayoutParams(
            w,
            w
        )
        holder.itemHomeLayoutBinding.cardView.layoutParams = params
        holder.itemHomeLayoutBinding.image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_launcher_background))

      /*  var item = getItem(position)
        val bitmap = cache.get(item?.urls?.regular)
        if (bitmap != null) {
            holder.itemHomeLayoutBinding.image.setImageBitmap(bitmap)
        } else {
            holder.itemHomeLayoutBinding.image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_launcher_background))
            DownloadImageTask(holder.itemHomeLayoutBinding.image).execute(item?.urls?.regular)
        }*/

       GlobalScope.launch(Dispatchers.IO) {
            var item = getItem(holder.bindingAdapterPosition)
            holder.itemHomeLayoutBinding.image.tag = item?.urls?.small
            var bm = item?.urls?.small?.let { imageLoader.loadImage(it) }
            item?.urls?.small?.let { Log.e("ADAPTER", "$position : "+it) }
            // var bm= allData[position].urls?.regular?.let { imageLoader.loadImage(it) }
            withContext(Dispatchers.Main)
            {
                holder.itemHomeLayoutBinding.image.setImageBitmap(bm)

//                if(holder.itemHomeLayoutBinding.image.tag.equals(item?.urls?.small))
//                {
//
//                }
//                else{
//                    holder.itemHomeLayoutBinding.image.setImageBitmap(null)
//                }



            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        var rowPhotoLayoutBinding = DataBindingUtil.inflate<ItemHomeLayoutBinding>(
            LayoutInflater.from(parent.context), R.layout.item_home_layout, parent, false
        )
        return PhotoHolder(rowPhotoLayoutBinding)
    }

    object diffCallback : DiffUtil.ItemCallback<ResponseMain>() {
        override fun areItemsTheSame(oldItem: ResponseMain, newItem: ResponseMain) =
            oldItem.id.equals( newItem.id)

        override fun areContentsTheSame(oldItem: ResponseMain, newItem: ResponseMain) =
            oldItem == newItem
    }
    private inner class DownloadImageTask(var imageView: ImageView) : AsyncTask<String, Void, Bitmap>() {
        private var imageUrl: String? = null

        override fun doInBackground(vararg params: String): Bitmap? {
            imageUrl = params[0]
            var bitmap: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                bitmap = BitmapFactory.decodeStream(input)
                cache.put(imageUrl!!, bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                // Update UI if the image is downloaded successfully
                //imageView.setImageBitmap(result)
                notifyDataSetChanged()
            } else {
                // Handle error or set a placeholder image
            }
        }
    }
}