package com.iitbhu.KidsMagazine;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    Adapter adapter;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    DatabaseReference databaseReference;
    MyStory myStory;
    List<String> title_list,story_list,storyBeng_list,storyKnd_list;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        final int flag = i.getIntExtra("lang_key",0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // get the list of stories titles and contents in string array

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navi_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        databaseReference= FirebaseDatabase.getInstance().getReference("storybook");
        myStory=new MyStory();
        title_list=new ArrayList<>();
        story_list=new ArrayList<>();
        storyBeng_list=new ArrayList<>();
        storyKnd_list=new ArrayList<>();
        recyclerView = findViewById(R.id.storiesListsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        View hview=navigationView.getHeaderView(0);
        imageView=hview.findViewById(R.id.imageView1);
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(imageView);
        TextView textView=hview.findViewById(R.id.textView);
        TextView textView2=hview.findViewById(R.id.textView2);
        textView.setText(user.getDisplayName());
        textView2.setText(user.getEmail());
        Toast.makeText(this,"Welcome "+user.getDisplayName()+"!",Toast.LENGTH_SHORT).show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title_list.clear();
                story_list.clear();
                storyBeng_list.clear();
                storyKnd_list.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    myStory=ds.getValue(MyStory.class);
                    if (myStory != null) {
                        title_list.add(myStory.getTitle());
                    }
                    if (myStory != null) {
                        story_list.add(myStory.getStory());
                    }
                    if(myStory !=null) {
                        storyBeng_list.add(myStory.getStoryBeng());
                    }
                    if(myStory !=null) {
                        storyKnd_list.add(myStory.getStoryKnd());
                    }
                }
                String[] titles=new String[title_list.size()];
                titles=title_list.toArray(titles);
                String[] contents=new String[story_list.size()];
                contents=story_list.toArray(contents);
                String[] contentBeng=new String[storyBeng_list.size()];
                contentBeng=storyBeng_list.toArray(contentBeng);
                String[] contentKnd=new String[storyKnd_list.size()];
                contentKnd=storyKnd_list.toArray(contentKnd);
                adapter = new Adapter(HomeActivity.this,titles,contents,contentBeng,contentKnd,flag); // our adapter takes two string array
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.home){
            Toast.makeText(this, "Home Button Clicked.", Toast.LENGTH_SHORT).show();
        }
        if(menuItem.getItemId()==R.id.upload)
        {
            Toast.makeText(this,"First Choose, Then Upload",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),UploadActivity.class));
        }
        return true;
    }
}
