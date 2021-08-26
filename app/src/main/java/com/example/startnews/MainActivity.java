package com.example.startnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements News_Adapter.OnClickedListener {
    private RecyclerView recyclerView;
    private  List<News> news=new ArrayList<>();
    private News_Adapter mAdapter; 
    EditText Search;
    private final String apiKey="557a0b2d242849519c045970d8fa80b6"/*"46d028500ef8473799d6d2df8a0f5582"*/;
    private String url="https://newsapi.org/v2/top-headlines?country=in&pageSize=90&apiKey="+apiKey;
    private SwipeRefreshLayout swipe_refresh;
    Button SearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView=findViewById(R.id.recyclerView);
        swipe_refresh=findViewById(R.id.swipe_refresh);
        Search=findViewById(R.id.Search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadNews();
        mAdapter =new News_Adapter(news,this,this);
        recyclerView.setAdapter(mAdapter);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews();
                mAdapter.updateNews(news);
                mAdapter =new News_Adapter(news,MainActivity.this,MainActivity.this);
                recyclerView.setAdapter(mAdapter);
                swipe_refresh.setRefreshing(false);
            }
        });



    }


    private void loadNews(){
        RequestQueue requestQueue;
        requestQueue= Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray newsJsonArray=response.getJSONArray("articles");


                            for(int i=0;i<newsJsonArray.length();i++){
                                JSONObject newsJsonObject=newsJsonArray.getJSONObject(i);
                                String Image= newsJsonObject.getString("urlToImage");
                                String Author=newsJsonObject.getString("author");

                                String title=newsJsonObject.getString("title");
                                String description= newsJsonObject.getString("description");
                                String url= newsJsonObject.getString("url");

                                news.add(new News(Image,Author,title,description,url));

                            }
                            mAdapter.notifyDataSetChanged();
//                        mAdapter.updateNews(news);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, error.toString()+"ewwkmvfmdvmklrxjngrnjgjr", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("User-Agent", "Mozilla/5.0");
                return map;
            }


        };
        requestQueue.add(jsonObjectRequest);
    }
     
    @Override
    public void onNoteClicked(int position) {
        String url=news.get(position).getUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
    public void Visibility(View view){
        Search.setVisibility(View.VISIBLE);
        Search.requestFocus();
//        to automatically show the keyboard
        ((InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE)).showSoftInput(Search,InputMethodManager.SHOW_FORCED);
        if(!Search.getText().toString().toLowerCase().trim().isEmpty()){
            Search.onEditorAction(EditorInfo.IME_ACTION_DONE);
            SearchOnclick(SearchButton);

            Search.clearFocus();
        }

    }

    public void SearchOnclick(View view) {

        String a=Search.getText().toString().toLowerCase().trim();
        if((a.equals("business")) ||(a.equals("entertainment"))||(a.equals("general"))||(a.equals("health"))||(a.equals("science"))||(a.equals("sports"))||(a.equals("technology"))){
        String HalfUrl=url.substring(0,47);
        String remainUrl=url.substring(47);
        String Category="&category="+Search.getText().toString().toLowerCase().trim();

        String newUrl=HalfUrl+Category+remainUrl;
        url=newUrl;
        loadNews();
        mAdapter.updateNews(news);
        mAdapter =new News_Adapter(news,this,this);
        recyclerView.setAdapter(mAdapter);
        Search.clearFocus();

        }else{
            Search.setError("Enter the correct Category\n1.business\n2.entertainment\n3.general\n4.health\n5.science\n6.sports\n7.technology");
            Search.requestFocus();
            return;
    }

    }
}