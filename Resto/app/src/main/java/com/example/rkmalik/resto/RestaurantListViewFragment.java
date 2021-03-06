package com.example.rkmalik.resto;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rkmalik.data.Restaurant;
import com.example.rkmalik.model.DBHelper;
import com.example.rkmalik.model.RestaurantModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RestaurantListViewFragment extends ListFragment {
    private List<Rest_ListViewItem_Holder> mItems;        // ListView items list
    private SQLiteDatabase database = null;
    private List<Restaurant> restaurantListFromDB;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(this.getActivity().getApplicationContext());
        //database = dbHelper.openDatabase();
       // dbHelper.onUpgrade(database, 3,4);
        database = dbHelper.openDatabase();
        restaurantListFromDB = RestaurantModel.getRestList(database);
        database.close();

        setListAdapter(new RestaurantListAdapter(getActivity(), restaurantListFromDB));

        // initialize and set the list adapter
       // setListAdapter(new RestaurantListAdapter(getActivity(), restaurantListFromDB));
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }
 
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
       // Rest_ListViewItem_Holder item = mItems.get(position);

        Restaurant selectedRestaurant = restaurantListFromDB.get(position);
        //Bundle data;
        //data.pu

        Activity activity = getActivity();
        //activity.setTitle("Sub");
        // do something
       // Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
        Intent foodItemsIntent = new Intent(getActivity(), FoodItems.class);
        foodItemsIntent.putExtra("restId", selectedRestaurant.getId());
       // foodItemsIntent.putExtra("restName", selectedRestaurant.getName());
        //foodItemsIntent.putParcelableArrayListExtra("restaurant", selectedRestaurant);
        startActivity(foodItemsIntent);
    }
}
