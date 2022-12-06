package kr.ac.kumoh.s20200158.w1401customlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20200158.w1401customlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: SongViewModel
    private val songAdapter = SongAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[SongViewModel::class.java]

        binding.list.apply {
            layoutManager = LinearLayoutManager(applicationContext) // 반드시 필요
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = songAdapter // 반드시 필요
        }

        model.list.observe(this) {
            songAdapter.notifyItemRangeInserted(0, model.list.value?.size ?: 0)
        }

        model.requestSong()
    }

    inner class SongAdapter: RecyclerView.Adapter<SongAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            //val txTitle = itemView.findViewById<TextView>(android.R.id.text1)
            //val txSinger = itemView.findViewById<TextView>(android.R.id.text2)
            val txTitle = itemView.findViewById<TextView>(R.id.text1)
            val txSinger = itemView.findViewById<TextView>(R.id.text2)
            val niImage: NetworkImageView = itemView.findViewById((R.id.image))

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            //val view = layoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false) // 만들자마자 붙일거냐를 false로 한 것임
            val view = layoutInflater.inflate(R.layout.item_song, parent, false) // 만들자마자 붙일거냐를 false로 한 것임
            return ViewHolder(view)
        }

        // 값 세팅
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txTitle.text = model.list.value?.get(position)?.title
            holder.txSinger.text = model.list.value?.get(position)?.singer
            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
        }

        override fun getItemCount() = model.list.value?.size ?: 0
        //override fun getItemCount() = model.list.value!!.size => 이렇게 해도 가능

    }
}