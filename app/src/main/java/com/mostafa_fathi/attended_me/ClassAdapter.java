package com.mostafa_fathi.attended_me;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassItemViewHolder> {

    private List<ClassItem> classList;
    private Context context;
    private TextView emptyClasses;

    private OnItemClickLiscener liscener;

    public interface OnItemClickLiscener {
        void onClick(int position);
    }

    public void setLiscener(OnItemClickLiscener liscener) {
        this.liscener = liscener;
    }

    public ClassAdapter(List<ClassItem> classList, Context context,TextView message) {
        this.classList = classList;
        this.context = context;
        this.emptyClasses=message;
    }


    @NonNull
    @Override
    public ClassItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClassItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.classitem, parent, false),liscener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassItemViewHolder holder, int position) {

        emptyClasses.setVisibility(View.GONE);

        holder.className.setText(classList.get(position).getClassName());
        holder.subjectName.setText(classList.get(position).getSubjectName());

    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void setClassList(List<ClassItem> classList) {
        this.classList = classList;
        notifyDataSetChanged();
    }

    public void clearList() {
        classList.clear();

    }

    public class ClassItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView className,subjectName;
        public ClassItemViewHolder(@NonNull View itemView, OnItemClickLiscener liscener) {
            super(itemView);
            className=itemView.findViewById(R.id.class_tv);
            subjectName=itemView.findViewById(R.id.subject_tv);

            itemView.setOnClickListener(v->liscener.onClick(getAdapterPosition()));

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(getAdapterPosition(),0,0,"EDIT");
            contextMenu.add(getAdapterPosition(),1,0,"DELETE");

        }
    }
}
