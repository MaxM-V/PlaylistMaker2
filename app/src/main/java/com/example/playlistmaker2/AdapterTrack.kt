import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker2.R
import com.example.playlistmaker2.Track

class AdapterTrack(
    private var trackL: List<Track>,
    private val onClick:( Track) -> Unit
) : RecyclerView.Adapter<AdapterTrack.PersonViewHolder>() {
    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTrack: TextView = itemView.findViewById(R.id.trackname)
        val nameArtists: TextView = itemView.findViewById(R.id.artistname)
        val timeTrack: TextView = itemView.findViewById(R.id.trackTime)
        val trackImage: ImageView = itemView.findViewById(R.id.icon_group)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_search, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val track = trackL[position]
        holder.nameTrack.text = track.trackName
        holder.nameArtists.text = track.artistName
        holder.timeTrack.text = track.trackTime
        val radiusDp = 2f
        val scale = holder.itemView.context.resources.displayMetrics.density
        val radiusPx = (radiusDp * scale).toInt()

        Glide.with(holder.itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .transform(RoundedCorners(radiusPx))
            .into(holder.trackImage)

        holder.itemView.setOnClickListener{onClick(track)}
    }

    override fun getItemCount(): Int = trackL.size

    fun updateList(newList: List<Track>){
        trackL= newList
        notifyDataSetChanged()
    }
}