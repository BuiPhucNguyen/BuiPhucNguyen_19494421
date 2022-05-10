package com.example.buiphucnguyen_19494421;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    ImageButton imgBackHome, btnHome;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;
    private String key = "";
    private String task;
    private String note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        imgBackHome = findViewById(R.id.imgBackHome);
        imgBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,SignIn.class));
            }
        });
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Book").child(onlineUserID);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.add, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(true);

        final EditText task = myView.findViewById(R.id.task);
        final EditText note = myView.findViewById(R.id.note);
        Button cancel = myView.findViewById(R.id.cancelBtn);
        Button save = myView.findViewById(R.id.saveBtn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mtask = task.getText().toString().trim();
                String mnote = note.getText().toString().trim();
                String id = reference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                if(TextUtils.isEmpty(mtask)){
                    task.setError("Tên sách không được rỗng!");
                    return;
                }
                if(TextUtils.isEmpty(mnote)){
                    note.setError("Ghi chú không được rỗng!");
                    return;
                }else {

                    Book book = new Book(id, mtask, mnote, date);
                    reference.child(id).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HomeActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Book> options = new FirebaseRecyclerOptions.Builder<Book>()
                .setQuery(reference, Book.class)
                .build();

        FirebaseRecyclerAdapter<Book, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Book, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Book model) {
                holder.setTask(model.getName());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key = getRef(position).getKey();
                        task = model.getName();
                        note = model.getNote();

                        updateTask();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new MyViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update, null);
        myDialog.setView(view);

        final AlertDialog dialog =myDialog.create();
        final EditText mTask = view.findViewById(R.id.taskUD);
        final EditText mNote = view.findViewById(R.id.noteUD);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mNote.setText(note);
        mNote.setSelection(note.length());

        Button delButton = view.findViewById(R.id.dellBtn);
        Button updateButton = view.findViewById(R.id.updateBtn);

        Button cancel = view.findViewById(R.id.exitbtn);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = mTask.getText().toString().trim();
                note = mNote.getText().toString().trim();

                String date = DateFormat.getDateInstance().format(new Date());

                Book book = new Book(key, task, note, date);

                reference.child(key).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(HomeActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(HomeActivity.this, "Cập nhật thất bại!"+err, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.dismiss();

            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(HomeActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                        }else {
                            String err = task.getException().toString();
                            Toast.makeText(HomeActivity.this, "Xóa thất bại!"+ err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTask(String task) {
            TextView taskTextView = mView.findViewById(R.id.taskTV);
            taskTextView.setText(task);
        }

        public void setNote(String note) {
            TextView descTextView = mView.findViewById(R.id.noteTV);
            descTextView.setText(note);
        }

        public void setDate(String date) {
            TextView dateTextView = mView.findViewById(R.id.dateTV);
            dateTextView.setText(date);
        }
    }
}