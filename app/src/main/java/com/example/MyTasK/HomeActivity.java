package com.example.MyTasK;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyTasK.model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;




public class HomeActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;

    private Toolbar toolbar;
private FloatingActionButton fabBtn;

    //Update input field..

    private EditText titleUp;
    private EditText noteUp;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    //Variable

    private String title;
    private String note;
    private String post_key;

//Firebase

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    //Recycler..

   private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(HomeActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Notes");

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uId=mUser.getUid();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uId);

        mDatabase.keepSynced(true);

        //Recycler..


        recyclerView=(RecyclerView)findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        //If anyone click floating button to write.....
        fabBtn=findViewById(R.id.fab_btn);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder myDialog=new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);
                View myview=inflater.inflate(R.layout.custominputfoeld,null);


                myDialog.setView(myview);
                final AlertDialog dialog=myDialog.create();

                final EditText title=myview.findViewById(R.id.edt_title);
                final EditText note=myview.findViewById(R.id.edt_note);

                //FOR SAVE BUTTON
                Button btnSave=myview.findViewById(R.id.btn_save);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String mTitle=title.getText().toString().trim();
                        String mNote=note.getText().toString().trim();

                        if (TextUtils.isEmpty(mTitle)){
                            title.setError("Required Field..");
                            return;
                        }
                        if (TextUtils.isEmpty(mNote)){
                            note.setError("Required Field..");
                            return;
                        }

                        String id=mDatabase.push().getKey();
                        String datee= DateFormat.getDateInstance().format(new Date());

                        //MODEL class data
                        Data data=new Data(mTitle,mNote,datee,id);
                        mDatabase.child(id).setValue(data);
                        Toast.makeText(getApplicationContext(),"Data Insert",Toast.LENGTH_SHORT).show();

                        dialog.dismiss();


                    }
                });



                dialog.show();

            }
        });

    }

   //Here data is retreving from FireBase
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.item_data,
                        MyViewHolder.class,
                        mDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Data model, final int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setNote(model.getNote());
                viewHolder.setDate(model.getDate());

                //this code will call updateData() function jokhn app e ekta text er upor click kora hobe
                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post_key=getRef(position).getKey();  //HERE POST_KEY= position jeta populateViewHolder er argument
                        title=model.getTitle();
                        note=model.getNote();
                    updateData();

                }
            });

        }
    };
        recyclerView.setAdapter(adapter);
    }

    //implement viewholder class
    //This part will show "saved notes" in home page
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder(View itemView) {
            super(itemView);
            myview=itemView;
        }

        public void setTitle(String title){
            TextView mTitle=myview.findViewById(R.id.title);
            mTitle.setText(title);
        }

        public void setNote(String note){
            TextView mNote=myview.findViewById(R.id.note);
            mNote.setText(note);
        }

        public void setDate(String date){
            TextView mDate=myview.findViewById(R.id.date);
            mDate.setText(date);
        }


    }

  //UPDATE FIELD
    //This part will call update_input_field XML
    public void updateData(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(HomeActivity.this);
        //Inflater is callinghomeactivity means layout er ekta copy create kora.
        // may be home layout e update ta use hobe tai
        LayoutInflater inflater=LayoutInflater.from(HomeActivity.this);

        View myview=inflater.inflate(R.layout.update_input_field,null);
        mydialog.setView(myview);



        final AlertDialog dialog=mydialog.create();

     //UPDATE and DELETE  from FIREBASE

        titleUp=myview.findViewById(R.id.edt_title_upd);
        noteUp=myview.findViewById(R.id.edt_note_upd);

        titleUp.setText(title);
        titleUp.setSelection(title.length());

        noteUp.setText(note);
        noteUp.setSelection(note.length());


        btnDeleteUp=myview.findViewById(R.id.btn_delete_upd);
        btnUpdateUp=myview.findViewById(R.id.btn_update_upd);

 //UPDATE BUTTON CLICK

        btnUpdateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title=titleUp.getText().toString().trim();
                note=noteUp.getText().toString().trim();

                String mDate=DateFormat.getDateInstance().format(new Date());

                Data data=new Data(title,note,mDate,post_key);
                mDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

    //DELETE BUTTON CLICK

        btnDeleteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();

    }


    //This part is for calling mainmenu XML where logout is implementated,..
    // ..actually jekono XML start howar jonno onCreate lage
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    //if signout is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                mAuth.signOut(); //mAuth signout mane firebase thekeo signout
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}


