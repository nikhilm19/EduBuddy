package com.example.nikmul19.edubuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventsAdapter  extends RecyclerView.Adapter<EventsAdapter.MyViewHolder>{
        private List<Events> eventsList;
        public EventsAdapter(List<Events> eventsList){
            this.eventsList=eventsList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            private TextView email,enroll;
            private CheckBox absent;

            public MyViewHolder(View view){
                super(view);
                email=view.findViewById(R.id.email);
                enroll=view.findViewById(R.id.enroll_no);
                absent=view.findViewById(R.id.absent);

            }


        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.events_layout, parent, false);

            return new MyViewHolder(itemView);
        }



        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Events event = eventsList.get(position);
          /*  holder.email.setText(event.getEmail());
            holder.enroll.setText(event.getEnroll());
            if(holder.absent.isChecked())event.setAbsent(true);
            holder.absent.setChecked(event.isAbsent());
            holder.absent.setOnCheckedChangeListener(null);
            holder.absent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    event.setAbsent(isChecked);
                }
            });*/


        }
        @Override
        public int getItemCount(){
            return eventsList.size();
        }

    }


