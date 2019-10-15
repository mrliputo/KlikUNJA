package id.ac.unja.klikunja;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.ac.unja.klikunja.models.Notices;

public class NoticesAdapter extends RecyclerView.Adapter<NoticesAdapter.MyViewHolder> {

    private List<Notices.Items> articles;
    private Context context;
    private NoticesAdapter.OnItemClickListener onItemClickListener;

    NoticesAdapter(List<Notices.Items> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notices_item, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticesAdapter.MyViewHolder holders, int position) {
        final NoticesAdapter.MyViewHolder holder = holders;
        Notices.Items model = articles.get(position);

        holder.title.setText(model.getTitle());
        holder.published_at.setText(Utils.DateFormat(model.getDate_published()));
        holder.timeAgo.setText(Utils.DateToTimeFormat(model.getDate_published()));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setOnItemClickListener(NoticesAdapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        TextView title, published_at, timeAgo;
        NoticesAdapter.OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, NoticesAdapter.OnItemClickListener onItemClickListener) {

            super(itemView);

            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            published_at = itemView.findViewById(R.id.publishedAt);
            timeAgo = itemView.findViewById(R.id.time_ago);

            this.onItemClickListener = onItemClickListener;

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void addItem(List<Notices.Items> mArticles) {
        for(Notices.Items ar : mArticles) {
            articles.add(ar);
        }

        notifyDataSetChanged();
    }
}
