package assignment.rekkeitrainning.com.note.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import assignment.rekkeitrainning.com.note.R;
import assignment.rekkeitrainning.com.note.adapter.NoteAdapter;
import assignment.rekkeitrainning.com.note.constants.Constants;
import assignment.rekkeitrainning.com.note.db.DBNote;
import assignment.rekkeitrainning.com.note.model.Note;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClickListener {
    BottomNavigationView btNavigation;
    Toolbar mToolbar;
    List<Note> mListNote = null;
    DBNote mDbNote;
    NoteAdapter mNoteAdapter;
    RecyclerView rc_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
        initView();
        initListener();
    }

    private void initListener() {
        btNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent mIntent = new Intent(MainActivity.this, InsertNoteActivity.class);
                overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
                startActivity(mIntent);
                break;

        }
        return true;
    }

    private void initView() {
        rc_note = findViewById(R.id.rcNote);
        btNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        disableShiftMode(btNavigation);
        mToolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(mToolbar);
        mListNote = new ArrayList<>();
        mDbNote = new DBNote(this);
        mListNote = mDbNote.getAllNotes();
        rc_note.setLayoutManager(new GridLayoutManager(this, 2));
        mNoteAdapter = new NoteAdapter(this);
        mNoteAdapter.setmListNote(mListNote);
        mNoteAdapter.setOnItemClickListener(this);
        rc_note.setAdapter(mNoteAdapter);
        getSupportActionBar().setTitle("");

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

    @Override
    public void onItemClick(View view, int position) {
        Note mNote = new Note();
        mNote.setTitle(mListNote.get(position).getTitle());
        mNote.setContent(mListNote.get(position).getContent());
        mNote.setId(mListNote.get(position).getId());
        mNote.setDate(mListNote.get(position).getDate());
        mNote.setTime(mListNote.get(position).getTime());
        mNote.setAlaramTime(mListNote.get(position).getAlaramTime());
        mNote.setAlaramDate(mListNote.get(position).getAlaramDate());
        mNote.setImage(mListNote.get(position).getImage());
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(Constants.KEY_OBJECT_NOTE, mNote);
        Intent mIntent = new Intent(MainActivity.this, InsertNoteActivity.class);
        mIntent.putExtras(mBundle);
        overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
        startActivity(mIntent);
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
