package cardio_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cardio_app.R;
import cardio_app.activity.drug.DrugsActivity;
import cardio_app.db.DbHelper;
import cardio_app.db.model.PressureData;
import cardio_app.viewmodel.pressure.PressureDataViewModel;
import temporary_package.RandomParams;
import cardio_app.viewmodel.pressure.TableRowPressureData;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<TableRowPressureData> rowList = new ArrayList<>();
    AppCompatActivity self = this;
    TableLayout tableLayout;
    private static final String TAG = DrugsActivity.class.getName();
    private DbHelper dbHelper;

    private void addTableRowToLayout(PressureData hp, boolean showToast) {
        TableRowPressureData thr = new TableRowPressureData(self, new PressureDataViewModel(hp));
        rowList.add(thr);
        tableLayout.addView(thr, 0); // always on the top

        if (showToast) {
            Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(R.string.new_record_added_msg)
                            + ", ID: " + String.valueOf(thr.getId()),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // floating button - to add new records
        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.add_button);
        addBtn.setOnClickListener(view -> {
            PressureData hp = RandomParams.getRandomPressureData();
            addTableRowToLayout(hp, true);
        });

        // table layout - for record list
        tableLayout = (TableLayout) findViewById(R.id.TableLayout);
        tableLayout.setPadding(0,0,15,0);

        try {
            Dao<PressureData, Integer> dao = getHelper().getDao(PressureData.class);
//            List<PressureData> listPressureData = dao.queryForAll();
            List<PressureData> listPressureData = RandomParams.makePressureDataList();
            for (PressureData hp : listPressureData) {
                addTableRowToLayout(hp, false);
            }
        } catch (SQLException e) {
            Log.e(TAG, "Can't get pressure data records from sql dao", e);
            throw new RuntimeException(e);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_diary) {

        } else if (id == R.id.menu_import_export) {

        } else if (id == R.id.menu_genre_report) {

        } else if (id == R.id.menu_advices) {

        } else if (id == R.id.menu_reminders) {
            Intent intent = new Intent(this, DrugsActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_questionnaire) {

        } else if (id == R.id.menu_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(this, DbHelper.class);
        }

        return dbHelper;
    }

}