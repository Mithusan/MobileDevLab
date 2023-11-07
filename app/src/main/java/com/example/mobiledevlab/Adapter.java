package com.example.mobiledevlab;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private Context context;
    private List<Note> notes;

    Adapter(Context context,List<Note> notes){
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_list_view, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String title = notes.get(position).getTitle();
        String description = notes.get(position).getDescription();
        String colour = notes.get(position).getColour();
        long id = notes.get(position).getId();

        holder.titleOutput.setText(title);
        holder.descriptionOutput.setText(description);
        if(colour.equals("Yellow")){
            holder.noteColour.setBackgroundResource(R.color.yellow);
        }else if(colour.equals("Blue")){
            holder.noteColour.setBackgroundResource(R.color.blue);
        }else if(colour.equals("Orange")){
            holder.noteColour.setBackgroundResource(R.color.orange);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleOutput;
        TextView descriptionOutput;
        ConstraintLayout noteColour;
        ImageButton editNote;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.nTitle);
            descriptionOutput = itemView.findViewById(R.id.nDescription);
            noteColour = itemView.findViewById(R.id.noteContainer);
            editNote = itemView.findViewById(R.id.editBtn);

            editNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), Edit.class);
                    i.putExtra("ID",notes.get(getAdapterPosition()).getId());
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
