package com.example.btap21_11_2025.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btap21_11_2025.R;
import com.example.btap21_11_2025.model.Category;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<Category> mList;

    public CategoryAdapter(Context context, List<Category> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setData(List<Category> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mList.get(position);
        if (category == null) return;
        holder.txtName.setText(category.getName());
       // lay anh dung picasso
        // Picasso.with(context).load("url").placeholder(...).error(...).into(imageView);
        Picasso.get()
                .load(category.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgCategory);
    }
    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgCategory;
        private TextView txtName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            txtName = itemView.findViewById(R.id.txtCategoryName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(itemView.getContext(), "Bạn đã chọn category : " + txtName.getText().toString(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }
}
