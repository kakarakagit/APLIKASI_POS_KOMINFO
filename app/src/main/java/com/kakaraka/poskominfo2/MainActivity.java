package com.kakaraka.poskominfo2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
    private SearchView searchView;

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

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // toolbar fancy stuff
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.toolbar_title);

        myEditText = findViewById(R.id.edit_text);

        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapter(this, contactList, this);

        // white background notification bar
        whiteNotificationBar(recyclerView);

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

                            //contactList.clear();
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



/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        return true;

    //=====================================================================================
    //========  ini search filter dengan action bar, sewaktu2 mungkin butuh??  ===========
    //=====================================================================================
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
    //=====================================================================================
    //========  ini search filter dengan action bar, sewaktu2 mungkin butuh??  ===========
    //=====================================================================================
    }

*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }



    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
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
