package comcesar1287.github.www.recipesapp.view;

import android.app.ProgressDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import comcesar1287.github.www.recipesapp.R;
import comcesar1287.github.www.recipesapp.controller.domain.Recipe;
import comcesar1287.github.www.recipesapp.controller.fragment.RecipeFragment;
import comcesar1287.github.www.recipesapp.controller.util.Util;

public class ListViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    RequestQueue requestQueue;

    List<Recipe> recipes = new ArrayList<>();

    private ProgressDialog dialog;

    RecipeFragment frag;

    public static final String TAG = "ListViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        requestQueue = Volley.newRequestQueue(this);

        setupUI();

        dialog = ProgressDialog.show(this,"", getString(R.string.dialog_loading_data), true, false);

        loadingData();
    }

    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<Recipe> getRecipesList() {

        return recipes;
    }

    private void loadingData() {

        // Getting JSON Array node
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, Util.URL, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        Recipe recipeObject;
                        ArrayList<String> bookmarksArray, commentsArray;
                        HashMap<String,String> categoriesMap;

                        // looping through All Recipes
                        // display response
                        for (int i = 0; i < response.length(); i++) {

                            recipeObject = new Recipe();
                            bookmarksArray = new ArrayList<>();
                            commentsArray = new ArrayList<>();
                            categoriesMap = new HashMap<>();

                            JSONObject recipe;

                            JSONArray bookmarks;
                            JSONArray comments;
                            JSONArray categories;

                            try {
                                recipe = response.getJSONObject(i);

                                recipeObject.setId(recipe.getString("_id"));
                                recipeObject.setName(recipe.getString("name"));
                                recipeObject.setTime(recipe.getInt("time"));
                                recipeObject.setImage(recipe.getString("image"));


                                bookmarks = recipe.getJSONArray("bookmarks");
                                for(int j = 0; j < bookmarks.length(); j++){
                                    bookmarksArray.add(bookmarks.getString(j));
                                }

                                recipeObject.setBookmarks(bookmarksArray);

                                comments = recipe.getJSONArray("comments");
                                for(int j = 0; j < comments.length(); j++){
                                    commentsArray.add(comments.getString(j));
                                }

                                recipeObject.setComments(commentsArray);

                                categories = recipe.getJSONArray("categories");
                                for (int j = 0; j < categories.length(); j++) {
                                    JSONObject row_categories = categories.getJSONObject(j);
                                    categoriesMap.put("_id", row_categories.getString("_id"));
                                    categoriesMap.put("name", row_categories.getString("name"));
                                    categoriesMap.put("icon", row_categories.getString("icon"));
                                    categoriesMap.put("premium", row_categories.getString("premium"));
                                }

                                recipeObject.setCategories(categoriesMap);

                                recipes.add(recipeObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.i(TAG, String.valueOf(recipes.size()));

                        dialog.dismiss();

                        frag = (RecipeFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
                        if(frag == null) {
                            frag = new RecipeFragment();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.recipes_fragment_container, frag, "mainFrag");
                            ft.commit();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListViewActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }
        };

        requestQueue.add(getRequest);
    }
}
