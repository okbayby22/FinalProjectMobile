package com.example.outsidedb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private ArrayList<Post> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        textView = findViewById(R.id.textView2);
        /*
         * Build retrofit from web server
         */
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(GsonConverterFactory.create()).build();
        JSonPlaceHolderAPI placeHolderAPI = retrofit.create(JSonPlaceHolderAPI.class);
        Call<List<Post>> call = placeHolderAPI.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    textView.setText("Code: "+response.code());
                }
                List<Post> posts = response.body();
                /*
                 * Set content for text view to show data in DB
                 */
                for (Post post : posts){
                    String content = "";
                    content += "ID: " + post.getId()+"\n";
                    content += "User ID: " + post.getUserId()+"\n";
                    content += "Title: " + post.getTitle()+"\n";
                    content += "Text: " + post.getText()+"\n";
                    content += "-------------------------------------------------------------------------------------";
                    content += "\n\n";
                    textView.append(content); //append text view when have data

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
}