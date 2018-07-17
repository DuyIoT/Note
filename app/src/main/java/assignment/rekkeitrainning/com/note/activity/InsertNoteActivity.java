package assignment.rekkeitrainning.com.note.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Field;

import assignment.rekkeitrainning.com.note.R;
import assignment.rekkeitrainning.com.note.constants.Constants;
import assignment.rekkeitrainning.com.note.model.Note;

public class InsertNoteActivity extends AppCompatActivity {
    BottomNavigationView btNavigation;
    Toolbar mToolbar;
    Note mNote = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        initView();
        getData();
    }

    private void getData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null){
            mNote = (Note) mBundle.getParcelable(Constants.KEY_OBJECT_NOTE);
            Log.d("TAG", mNote.getTitle() + "AAA" +  mNote.getId());
        } else {
            Toast.makeText(this, "Bạn add", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        btNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        disableShiftMode(btNavigation);
        mToolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(mToolbar);
    }
    private void initListener() {
        btNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }
    private boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                break;

        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_camera:
                Toast.makeText(this,"HJJ", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("TAG", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("TAG", "Unable to change value of shift mode");
        }
    }
}
