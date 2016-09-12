package com.example.guest999.recycler_search;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    RecyclerView lv;
    String json;
    JSONParser jsonParser = new JSONParser();
    public static ArrayList<HashMap<String, String>> Movielist = null;
    public static final String URL_movie = "http://api.androidhive.info/json/movies.json";

    private static final String TAG_Movie = "title";
    private static final String TAG_image = "image";
    private static final String TAG_rating = "rating";
    private static final String TAG_releasing_year = "releaseYear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (RecyclerView) findViewById(R.id.recyclerview);
        new LoadMovies().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent intent=new Intent(MainActivity.this,Search.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class LoadMovies extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            Log.e("helloo", "2");
            json = jsonParser.makeServiceCall(URL_movie);
            Log.e("b_name JSON: ", "> " + json);
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            Movielist = new ArrayList<HashMap<String, String>>();
            Log.e("JSON", String.valueOf(Movielist));

            JSONArray b_name;
            if (json != null) {
                try {
                    b_name = new JSONArray(json);

                    if (b_name != null) {
                        // looping through All b_name
                        for (int i = 0; i < b_name.length(); i++) {
                            JSONObject c = b_name.getJSONObject(i);

                            // Storing each json item values in variable
                            String br_image = c.getString(TAG_image);
                            String br_name = c.getString(TAG_Movie);
                            String br_rating = c.getString(TAG_rating);
                            String br_year = c.getString(TAG_releasing_year);

                            Log.e("image", TAG_image);
                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            //map.put(TAG_Image, br_image);
                            map.put(TAG_Movie, br_name);
                            map.put(TAG_image, br_image);
                            map.put(TAG_rating, br_rating);
                            map.put(TAG_releasing_year, br_year);

                            // adding HashList to ArrayList
                            Movielist.add(map);
                        }

                    } else {
                        Log.d("b_name: ", "null");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            lv.setHasFixedSize(true);
            // use a linear layout manager
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            lv.setLayoutManager(mLayoutManager);
            // seting custom adapter
            lv.setAdapter(new MyAdapter(Movielist));
            Log.e("ARRAY", String.valueOf(Movielist));

        }

    }

    // adapter class for Fragment home
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<HashMap<String, String>> mDataset;

        public MyAdapter(ArrayList<HashMap<String, String>> BranchList) {
            mDataset = BranchList;
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private TextView rating, title, year;
            private ImageView imageView;


            public ViewHolder(View itemView) {
                super(itemView);
                rating = (TextView) itemView.findViewById(R.id.rating);
                year = (TextView) itemView.findViewById(R.id.year);
                title = (TextView) itemView.findViewById(R.id.m_name);
                imageView = (ImageView) itemView.findViewById(R.id.profile_image);


            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
            final String Branch_ID = mDataset.get(position).get(TAG_Movie);
            final String Rating = mDataset.get(position).get(TAG_rating);
            final String images = mDataset.get(position).get(TAG_image);
            final String year = mDataset.get(position).get(TAG_releasing_year);

            holder.title.setText(Branch_ID);
            holder.rating.setText("Rating:" + Rating);
            holder.year.setText("Year:" + year);
         /*   Picasso.with(MainActivity.this,images)
                    .load(URL)
                    .into(holder.imageView);*/

            Picasso.with(MainActivity.this)
                    .load(images)
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

}
