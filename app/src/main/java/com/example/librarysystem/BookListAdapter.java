package com.example.librarysystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {


    public ArrayList<BookList> bookLists;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context adaptercontext;

    // data is passed into the constructor
    public BookListAdapter(Context context, ArrayList<BookList> bookListArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.bookLists = bookListArrayList;
        this.adaptercontext = context;

    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mylist, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookList bookList = bookLists.get(position);

        holder.titleText.setText(bookList.getTitle());
        holder.categoryText.setText(bookList.getCategory());
        holder.authorText.setText(bookList.getAuthor());

        if (bookList.getImage() == "") {

            holder.profileImg.setImageResource(R.drawable.image_not_available);

        } else {

            Picasso.get().load(bookList.getImage())
                    .placeholder(R.drawable.ic_search)
                    .error(R.drawable.home_image)
                    .into(holder.profileImg);

        }


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return bookLists.size();
    }

    // convenience method for getting data at click position
    public BookList getItem(int id) {
        return bookLists.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClickBookList(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profileImg;
        TextView titleText;
        TextView categoryText;
        TextView authorText;

        ViewHolder(View itemView) {
            super(itemView);
            profileImg = (ImageView) itemView.findViewById(R.id.icon);
            titleText = (TextView) itemView.findViewById(R.id.title);
            categoryText = (TextView) itemView.findViewById(R.id.category);
            authorText = (TextView) itemView.findViewById(R.id.author_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClickBookList(view, getAdapterPosition());

        }
    }

}
