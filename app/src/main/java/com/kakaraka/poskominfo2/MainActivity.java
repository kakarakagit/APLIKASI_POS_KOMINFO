package com.kakaraka.poskominfo2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.ContactsAdapterListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsAdapter mAdapter;

    boolean doubleBackToExitPressedOnce = false;

    private EditText myEditText;

    // url to fetch contacts json
    private static final String URL2 = "http://dev-blimana.com/profilingpos/api/";


    public static final String EXTRA_NAMA = "nama";
    public static final String EXTRA_NOIZIN = "noizin";
    public static final String EXTRA_KODEPOS = "kodepos";
    public static final String EXTRA_MASABERLAKU = "masaberlaku";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEditText = findViewById(R.id.edit_text);

        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapter(this, contactList, this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        //fetchContacts();
        ParseJSON();

        myEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAdapter.getFilter().filter(editable);
                // THI IS FINALLY WORKING
            }
        });

    }

    /**
     * fetches json by making http calls
     */

        private void ParseJSON()
        {

            JsonObjectRequest jRequest = new JsonObjectRequest(Request.Method.GET, URL2, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try
                    {
                        JSONArray jsonArray = response.getJSONArray("data");

                        for(int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String nama = jsonObject.getString("nama");
                            String noizin = jsonObject.getString("noizin");
                            String kodepos = jsonObject.getString("kodepos");
                            String masaberlaku = jsonObject.getString("masaberlaku");

                            contactList.add(new Contact(nama, noizin, kodepos, masaberlaku));
                        }
                        recyclerView.setAdapter(mAdapter);

                    } catch (JSONException e)
                    {
                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            MyApplication.getInstance().addToRequestQueue(jRequest);

        }




    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    @Override
    public void onContactSelected(Contact contact) {
        Intent detailIntent = new Intent(this, DetailActivity.class);

        detailIntent.putExtra(EXTRA_NAMA, contact.getName());
        detailIntent.putExtra(EXTRA_NOIZIN, contact.getNoizin());
        detailIntent.putExtra(EXTRA_KODEPOS, contact.getKodepos());
        detailIntent.putExtra(EXTRA_MASABERLAKU, contact.getMasaberlaku());

        //detailIntent.putExtra(EXTRA_GMAPS, contact.getGmaps());

        startActivity(detailIntent);


    }
}
