package com.mostafa_fathi.attended_me;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<StudentItem> studentList = new ArrayList<>();
    private ClassAdapter.OnItemClickLiscener liscener;
    private Context context;
    private TextView emptyMessage;


    public interface OnItemClickLiscener {
        void onClick(int position);
    }

    public void setLiscener(ClassAdapter.OnItemClickLiscener liscener) {
        this.liscener = liscener;
    }

    public StudentAdapter(List<StudentItem> studentList, Context context,TextView empty) {
        this.studentList = studentList;
        this.context=context;
        this.emptyMessage=empty;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false),liscener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

        emptyMessage.setVisibility(View.GONE);

        holder.roll.setText(""+studentList.get(position).getRoll());
        holder.name.setText(studentList.get(position).getName());
        holder.status.setText(studentList.get(position).getStatus());

        holder.cardView.setCardBackgroundColor(getColor(position));

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.name.setTextColor(Color.parseColor("#FFFFFFFF"));
//            }
//        });


    }

    private int getColor(int position) {
        String status=studentList.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.present)));
        else if (status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.absent)));

        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.white)));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setPostList(List<StudentItem> studentList) {
        this.studentList = studentList;
        notifyDataSetChanged();
    }

    public void clearList() {
        studentList.clear();

    }

    public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener  {
        TextView roll,name,status;
        CardView cardView;
        public StudentViewHolder(@NonNull View itemView, ClassAdapter.OnItemClickLiscener liscener) {
            super(itemView);
            roll=itemView.findViewById(R.id.roll);
            name=itemView.findViewById(R.id.name);
            status=itemView.findViewById(R.id.status);
            cardView=itemView.findViewById(R.id.cardView);
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
