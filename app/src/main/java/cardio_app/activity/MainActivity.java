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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cardio_app.R;
import cardio_app.db.model.HealthParams;
import cardio_app.viewmodel.diary.HealthParamsViewModel;
import temporary_package.RandomParams;
import cardio_app.viewmodel.diary.TableHealthRecord;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<TableHealthRecord> rowList = new ArrayList<>();
    AppCompatActivity self = this;
    TableLayout tableLayout;

    private void addTableRecord(HealthParams hp, boolean showToast) {
        TableHealthRecord thr = new TableHealthRecord(self, new HealthParamsViewModel(hp));
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

        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.add_button);
        addBtn.setOnClickListener(view -> {
            HealthParams hp = RandomParams.getRandomParam();
            addTableRecord(hp, true);
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tableLayout = (TableLayout) findViewById(R.id.TableLayout);
        tableLayout.setPadding(0,0,15,0);
        for (HealthParams hp : RandomParams.makeParamList()) {
            addTableRecord(hp, false);
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
            // Handle the camera action
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

}