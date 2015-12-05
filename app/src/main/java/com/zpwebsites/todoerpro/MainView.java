package com.zpwebsites.todoerpro;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/*
* Copyright (C) 2015 Zachary Powell
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
public class MainView extends Activity {
    protected DbHelper db;
    List<Task> list;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        db = new DbHelper(this);
        list = db.getAllTasks();

        RecyclerView listTask = (RecyclerView) findViewById(R.id.listView1);
        mLayoutManager = new LinearLayoutManager(this);
        listTask.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(list);
        listTask.setAdapter(mAdapter);

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff5722")));
        bar.setDisplayShowHomeEnabled(false);
    }
    public void addTaskNow(View v) {
        EditText t = (EditText) findViewById(R.id.editText1);
        String s = t.getText().toString();
        if (s.equalsIgnoreCase("")) {
            Toast.makeText(this, "enter the task description first!!",
            Toast.LENGTH_LONG);
        } else {
            Task task = new Task(s, 0);
            t.setText("");
            addToList(task, list.size() + 1);
            mAdapter.notifyDataSetChanged();
        }
    }
    public void addToList(Task name, int position) {
        list.add(name);
        db.addTask(name);
        mAdapter.notifyItemInserted(position);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        List<Task> taskList = new ArrayList<Task>();

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public  class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mTextView;
            public TextView mTextViewDescription;
            public ViewHolder(LinearLayout v) {
                super(v);

                mTextView = (TextView) v.findViewById(R.id.textView);
                mTextViewDescription = (TextView) v.findViewById(R.id.textView2);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        removeItemFromList(getPosition());
                    }
                });

            }
        }

        public void removeItemFromList(int position) {
            Task tasktoremove = list.get(position);
            list.remove(position);
            db.deleteTask(tasktoremove);
            notifyItemRemoved(position);
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<Task> myDataset) {
            taskList = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);

            ViewHolder vh = new ViewHolder((LinearLayout) v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mTextView.setText(taskList.get(position).getTaskName());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
}