package com.innovations.hosa.rickhansen.hosa_app;

import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.SurfaceView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.innovations.hosa.rickhansen.hosa_app.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;

    AHBottomNavigation bottomNavigation;
    AHBottomNavigationItem profile;
    AHBottomNavigationItem diagnostics;
    AHBottomNavigationItem imaging;
    AHBottomNavigationItem network;

    private static final int VIEW_MODE_PROFILE = 0;
    private static final int VIEW_MODE_DIAGNOSTICS = 1;
    private static final int VIEW_MODE_IMAGING = 2;
    private static final int VIEW_MODE_NETWORK = 3;
    private int mViewMode = 0;

    static JavaCamera2View camera2View;


    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("KELP: Health");

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        setupBottomNavBehaviors();
        setupBottomNavStyle();

        addBottomNavigationItems();
        bottomNavigation.setCurrentItem(0);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        mViewMode = VIEW_MODE_PROFILE;
                        break;
                    case 1:
                        mViewMode = VIEW_MODE_DIAGNOSTICS;
                        break;
                    case 2:
                        mViewMode = VIEW_MODE_IMAGING;
                        break;
                    case 3:
                        mViewMode = VIEW_MODE_NETWORK;
                        break;
                    default:
                        mViewMode = VIEW_MODE_PROFILE;
                        break;
                }
                return true;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        navigationView.setItemIconTintList(null);
    }

    @Override
    public void onPause()
    {
        super.onPause();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();

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

        if (id == R.id.profile) {
            mViewMode = VIEW_MODE_PROFILE;
            bottomNavigation.setCurrentItem(0);
            //Start a Class
            Intent launchNewActivity = new Intent(MainActivity.this, UserProfile.class);
            startActivity(launchNewActivity);


        } else if (id == R.id.diagnostics) {
            //Start a Class
            /*Intent launchNewActivity = new Intent(MainActivity.this, class1.class);
            startActivityForResult(launchNewActivity, 0);
            */
            mViewMode = VIEW_MODE_DIAGNOSTICS;
            bottomNavigation.setCurrentItem(1);

        } else if (id == R.id.imaging) {
            mViewMode = VIEW_MODE_IMAGING;
            bottomNavigation.setCurrentItem(2);
        } else if (id == R.id.network) {
            //Start a class
            /*Intent launchNewActivity = new Intent(MainActivity.this, class1.class);
            startActivity(launchNewActivity);
*/
            mViewMode = VIEW_MODE_NETWORK;
            bottomNavigation.setCurrentItem(3);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCameraViewStarted(int width, int height) {

    }

    public void onCameraViewStopped() {
        //mRgba.release();
    }

    public boolean onTouch(View v, MotionEvent event) {


        return false; // don't need subsequent touch events
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        //mRgba = inputFrame.rgba();

        return new Mat();
    }

    /**
     * Adds styling properties to {@link AHBottomNavigation}
     */
    private void setupBottomNavStyle() {
        /*
        Set Bottom Navigation colors. Accent color for active item,
        Inactive color when its view is disabled.
        Will not be visible if setColored(true) and default current item is set.
         */
        bottomNavigation.setDefaultBackgroundColor(fetchColor(R.color.red));
        bottomNavigation.setAccentColor(fetchColor(R.color.black));
        bottomNavigation.setInactiveColor(fetchColor(R.color.transparent_green_color));

        // Colors for selected (active) and non-selected items.
        bottomNavigation.setColoredModeColors(Color.WHITE,
                fetchColor(R.color.brightBlue));

        //  Displays item Title always (for selected and non-selected items)
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }

    /**
     * Adds (items) {@link AHBottomNavigationItem} to {@link AHBottomNavigation}
     * Also assigns a distinct color to each Bottom Navigation item, used for the color ripple.
     */
    private void addBottomNavigationItems(){
        profile = new AHBottomNavigationItem("Profile", R.drawable.rsz_profile);
        diagnostics = new AHBottomNavigationItem("Diagnostics", R.drawable.rsz_data);
        imaging = new AHBottomNavigationItem("Imaging", R.drawable.rsz_otoscope);
        network = new AHBottomNavigationItem("Network", R.drawable.rsz_network);

        bottomNavigation.addItem(profile);
        bottomNavigation.addItem(diagnostics);
        bottomNavigation.addItem(imaging);
        bottomNavigation.addItem(network);
    }

    public void setupBottomNavBehaviors() {

        bottomNavigation.setTranslucentNavigationEnabled(false);
    }

    /**
     * Simple facade to fetch color resource, so I avoid writing a huge line every time.
     *
     * @param color to fetch
     * @return int color value.
     */
    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

}