package com.kingkrooks.followup;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.facebook.FacebookSdk;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private boolean contactAlive = false;
    public boolean initial = false;


    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    private android.support.v7.widget.Toolbar toolbar;

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;
    private MainActivityFragment firstFrag = new MainActivityFragment();
    private fragment_two sec_frag = new fragment_two();
    private ResultFragment result_frag = new ResultFragment();
    private Contacts c_frag = new Contacts();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getResources().getString(R.string.twitter_key),
                getResources().getString(R.string.twitter_secret));

        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if (null == savedInstanceState) {
            mNavItemId = R.id.drawer_item_1;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().findItem(mNavItemId).setChecked(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,
                R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigate(mNavItemId);
    }

    private void navigate(final int itemId) {
        switch (itemId){
            case R.id.drawer_item_1:
                contactAlive = false;
                initial = false;
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, firstFrag, "home")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.drawer_item_2:
                contactAlive = false;
                initial = false;
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, sec_frag, "info")
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.drawer_item_3:
                contactAlive = true;
                initial = false;
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, c_frag, "contact")
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                break;

        }
    }

    public void goToLogin(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, sec_frag, "info")
                .addToBackStack(null)
                .commit();
    }

    public void useResult(String r){
        Log.i("tag", "Info made it alive");
        result_frag.setContent(r);
        Log.i("tag", "Information set");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, result_frag, "result")
                .addToBackStack(null)
                .commit();


    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else if(getFragmentManager().findFragmentByTag("result") != null){
            if(getFragmentManager().findFragmentByTag("result").equals(result_frag) && !contactAlive){
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, firstFrag, "home")
                        .addToBackStack(null)
                        .commit();
            } else if(getFragmentManager().findFragmentByTag("result").equals(result_frag) && contactAlive){
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, c_frag, "contact")
                        .addToBackStack(null)
                        .commit();
            }
        }
        else if(getFragmentManager().findFragmentByTag("info") != null){
            if(getFragmentManager().findFragmentByTag("info").equals(sec_frag) && initial){
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, firstFrag, "home")
                        .addToBackStack(null)
                        .commit();
                initial = false;
            } else if(getFragmentManager().findFragmentByTag("info").equals(sec_frag) && !initial){
                super.onBackPressed();
            }
        }
        else {
           super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sec_frag.onActivityResult(requestCode, resultCode, data);
        firstFrag.onActivityResult(requestCode, resultCode, data);

    }

    public void setTitle(String title){
        toolbar.setTitle(title);
    }


}