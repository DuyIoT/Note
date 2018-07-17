package assignment.rekkeitrainning.com.note.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import assignment.rekkeitrainning.com.note.R;
import assignment.rekkeitrainning.com.note.model.Note;

/**
 * Created by hoang on 7/17/2018.
 */

public class NoteAdapter extends  RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    ItemClickListener mClickListener;
    Context mContext;

    public void setmListNote(List<Note> mListNote) {
        this.mListNote = mListNote;
    }

    List<Note> mListNote;

    public NoteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rc_note, parent, false);
        return new NoteViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note mNote = mListNote.get(position);
        holder.tv_title.setText(mNote.getTitle());
        holder.tv_content.setText(mNote.getContent());
        holder.tv_date.setText(mNote.getDate());
        holder.tv_time.setText(mNote.getTime());
        if (!TextUtils.isEmpty(mNote.getAlaramDate()) && !TextUtils.isEmpty(mNote.getAlaramTime())){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.img_clock.setBackground(mContext.getResources().getDrawable(R.drawable.clock, mContext.getResources().newTheme()));
            } else {
                holder.img_clock.setImageResource(R.drawable.clock);
            }
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return mListNote!= null? mListNote.size(): 0;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_title;
        TextView tv_content;
        ImageView img_clock;
        TextView tv_date;
        TextView tv_time;
        @SuppressLint("WrongViewCast")
        public NoteViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tvTitle);
            tv_content = itemView.findViewById(R.id.tvContent);
            img_clock = itemView.findViewById(R.id.imgClock);
            tv_date = itemView.findViewById(R.id.tvDate);
            tv_time = itemView.findViewById(R.id.tvTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null){
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
    public void setOnItemClickListener(ItemClickListener mClickListener){
        this.mClickListener = mClickListener;
    }
    public interface ItemClickListener{
        public void onItemClick(View view, int position);
    }
}
