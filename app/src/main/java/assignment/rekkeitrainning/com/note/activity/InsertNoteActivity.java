package assignment.rekkeitrainning.com.note.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import assignment.rekkeitrainning.com.note.R;
import assignment.rekkeitrainning.com.note.constants.Constants;
import assignment.rekkeitrainning.com.note.db.DBNote;
import assignment.rekkeitrainning.com.note.model.Note;
import assignment.rekkeitrainning.com.note.notification.SetupNotification;

public class InsertNoteActivity extends AppCompatActivity implements View.OnClickListener {
    final int RESULT_LOAD_IMAGE = 1;
    final int RESULT_LOAD_CAMERA = 2;
    DBNote mDBNote;
    RelativeLayout rlt_insert;
    ImageView img_note;
    TextView tv_datetimenow;
    TextInputEditText et_title;
    TextInputEditText et_content;
    ImageButton ibtn_calendar;
    ImageButton ibtn_clock;
    TextView tv_calendar;
    TextView tv_clock;
    DatePickerDialog mDatePicker;
    BottomNavigationView btNavigation;
    Toolbar mToolbar;
    Note mNote = null;
    String date_now;
    String time_now;
    Calendar mCalendar;
    int year;
    int month;
    int dayOfMonth;
    int hour;
    int minute;
    boolean isInsert = false;
    Bitmap bmpImage;
    String url_image;
    MenuItem mMenuNewNote;
    String timeAlaram = "";
    SetupNotification mSetupNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        initView();
        setBackGroundColor();
        getData();
        initListener();
        getTimeNow();
    }

    private void getData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mNote = (Note) mBundle.getParcelable(Constants.KEY_OBJECT_NOTE);
            et_title.setText(mNote.getTitle());
            et_content.setText(mNote.getContent());
            tv_calendar.setText(mNote.getAlaramDate());
            tv_clock.setText(mNote.getAlaramTime());
            img_note.setImageBitmap(Constants.StringToBitmap(mNote.getImage()));
            url_image = mNote.getImage();
            isInsert = false;
            btNavigation.setVisibility(View.VISIBLE);
        } else {
            isInsert = true;
            btNavigation.setVisibility(View.INVISIBLE);
        }
    }

    private void initView() {
        mDBNote = new DBNote(this);
        btNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        disableShiftMode(btNavigation);
        mToolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        tv_datetimenow = findViewById(R.id.tvDatetime);
        tv_calendar = findViewById(R.id.tvAlaramDate);
        tv_clock = findViewById(R.id.tvAlaramTime);
        ibtn_calendar = findViewById(R.id.ibnAlaramDate);
        ibtn_clock = findViewById(R.id.ibtnAlaramTime);
        et_content = findViewById(R.id.etContent);
        et_title = findViewById(R.id.etTitle);
        img_note = findViewById(R.id.imgNote);
        rlt_insert = findViewById(R.id.rltInsert);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSetupNotification = new SetupNotification(this);
    }

    private void getTimeNow() {
        mCalendar = Calendar.getInstance();
        date_now = mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR);
        time_now = mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + mCalendar.get(Calendar.MINUTE);
        tv_datetimenow.setText(date_now + " " + time_now);
    }

    private void initListener() {
        btNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        ibtn_calendar.setOnClickListener(this);
        ibtn_clock.setOnClickListener(this);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (mNote != null) {
                    mDBNote.deleteNote(mNote);
                    mSetupNotification.cancleAlaramManager(mNote);
                    mSetupNotification.cancleNotification(mNote);
                    Intent mIntent = new Intent(InsertNoteActivity.this, MainActivity.class);
                    startActivity(mIntent);
                } else {
                    Toast.makeText(this, "Bạn không xóa được", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_share:
                setActionShare();
                break;
            case R.id.action_back:
                Intent mIntent = new Intent(InsertNoteActivity.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.action_right:
                break;
        }
        return true;
    }

    private void setActionShare() {
        Intent mIntentShare = new Intent(Intent.ACTION_SEND);
        mIntentShare.setType("text/plain");
        mIntentShare.putExtra(Intent.EXTRA_SUBJECT, mNote.getTitle());
        mIntentShare.putExtra(Intent.EXTRA_TEXT, mNote.getContent());
        mIntentShare.putExtra(Intent.EXTRA_STREAM, Constants.StringToBitmap(mNote.getImage()));
        PackageManager mPackageManager = getPackageManager();
        List<ResolveInfo> mListActivity = mPackageManager.queryIntentActivities(mIntentShare, 0);
        for (ResolveInfo app : mListActivity) {
            if ((app.activityInfo.name).startsWith("com.facebook.katana") ||
                    (app.activityInfo.name).contains("android.gm") ||
                    (app.activityInfo.name).equals("com.twitter.android.PostActivity") ||
                    (app.activityInfo.name).contains("com.whatsapp")) {
                ActivityInfo activityInfo = app.activityInfo;
                ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                mIntentShare.addCategory(Intent.CATEGORY_LAUNCHER);
                mIntentShare.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                mIntentShare.setComponent(componentName);
                startActivity(mIntentShare);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        mMenuNewNote = menu.findItem(R.id.menu_new);
        if (mNote != null) {
            mMenuNewNote.setVisible(true);
        } else {
            mMenuNewNote.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_camera:
                showDialogChooseImage();
                break;
            case R.id.menu_change:
                showDialogChooseColor();
                break;
            case R.id.menu_choose:
                insertData();
                break;
            case R.id.menu_new:
                reloadNewNote();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void reloadNewNote() {
        finish();
        Intent mIntent = new Intent(this, InsertNoteActivity.class);
        startActivity(mIntent);
    }

    private long convertDatetoMilisecond() {
        long milisecond = 0;
        if (!TextUtils.isEmpty(tv_calendar.getText().toString()) && !TextUtils.isEmpty(tv_clock.getText().toString())) {
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            Date mDateAlaram = new Date();
            try {
                timeAlaram = tv_calendar.getText().toString() + " " + tv_clock.getText().toString() + "-00"; //"2018-01-17 23-24-00"
                timeAlaram = timeAlaram.replace(":", "-");
                mDateAlaram = mDateFormat.parse(timeAlaram);
                milisecond = mDateAlaram.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return milisecond;
    }

    private void insertData() {
        long milisecond = convertDatetoMilisecond();
        Log.d("TAG", "milisecon" + milisecond);
        Note mInsertNote = new Note();
        mInsertNote.setTitle(et_title.getText().toString());
        mInsertNote.setContent(et_content.getText().toString());
        mInsertNote.setDate(date_now);
        mInsertNote.setTime(time_now);
        mInsertNote.setImage(url_image);
        mInsertNote.setAlaramDate(tv_calendar.getText().toString());
        mInsertNote.setAlaramTime(tv_clock.getText().toString());
        if (isInsert) {
            if (!TextUtils.isEmpty(mInsertNote.getTitle()) && !TextUtils.isEmpty(mInsertNote.getContent())) {
                long insert = mDBNote.insertNote(mInsertNote);
                Note noteNew = mDBNote.getNoteNew();
                mSetupNotification.scheduleNotification(mSetupNotification.getNotification(noteNew), noteNew, milisecond);
                Intent mIntent = new Intent(InsertNoteActivity.this, MainActivity.class);
                startActivity(mIntent);
            } else {
                Toast.makeText(this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!TextUtils.isEmpty(mInsertNote.getTitle()) && !TextUtils.isEmpty(mInsertNote.getContent())) {
                mInsertNote.setId(mNote.getId());
                int update = mDBNote.updateNote(mInsertNote);
                mSetupNotification.scheduleNotification(mSetupNotification.getNotification(mInsertNote), mInsertNote, milisecond);
                Intent mIntent = new Intent(InsertNoteActivity.this, MainActivity.class);
                startActivity(mIntent);
            } else {
                Toast.makeText(this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialogChooseColor() {
        Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_color_background);
        Button btn_main = mDialog.findViewById(R.id.btnMain);
        Button btn_main1 = mDialog.findViewById(R.id.btnMain1);
        Button btn_main2 = mDialog.findViewById(R.id.btnMain2);
        Button btn_main3 = mDialog.findViewById(R.id.btnMain3);
        btn_main.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                saveColorBackground("Main");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rlt_insert.setBackgroundColor(getResources().getColor(R.color.bgMain, getResources().newTheme()));
                } else {
                    rlt_insert.setBackgroundColor(R.color.bgMain);
                }
                mDialog.dismiss();
            }
        });
        btn_main1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                saveColorBackground("Main1");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rlt_insert.setBackgroundColor(getResources().getColor(R.color.bgMain1, getResources().newTheme()));
                } else {
                    rlt_insert.setBackgroundColor(R.color.bgMain1);
                }
                mDialog.dismiss();
            }
        });
        btn_main2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                saveColorBackground("Main2");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rlt_insert.setBackgroundColor(getResources().getColor(R.color.bgMain2, getResources().newTheme()));
                } else {
                    rlt_insert.setBackgroundColor(R.color.bgMain2);
                }
                mDialog.dismiss();
            }
        });
        btn_main3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                saveColorBackground("Main3");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    rlt_insert.setBackgroundColor(getResources().getColor(R.color.bgMain3, getResources().newTheme()));
                } else {
                    rlt_insert.setBackgroundColor(R.color.bgMain3);
                }
                mDialog.dismiss();
            }
        });
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(mDialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.show();
        mDialog.getWindow().setAttributes(lWindowParams);
    }

    @SuppressLint("ResourceAsColor")
    private void setBackGroundColor() {
        String mKeyColor = getBackgroundColorSave();
        if (mKeyColor.equalsIgnoreCase("Main")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rlt_insert.setBackgroundColor(getResources().getColor(R.color.bgMain, getResources().newTheme()));
            } else {
                rlt_insert.setBackgroundColor(R.color.bgMain);
            }
        } else if (mKeyColor.equalsIgnoreCase("Main1")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rlt_insert.setBackgroundColor(getResources().getColor(R.color.bgMain1, getResources().newTheme()));
            } else {
                rlt_insert.setBackgroundColor(R.color.bgMain1);
            }
        } else if (mKeyColor.equalsIgnoreCase("Main2")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rlt_insert.setBackgroundColor(getResources().getColor(R.color.bgMain2, getResources().newTheme()));
            } else {
                rlt_insert.setBackgroundColor(R.color.bgMain2);
            }
        } else if (mKeyColor.equalsIgnoreCase("Main3")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rlt_insert.setBackgroundColor(getResources().getColor(R.color.bgMain3, getResources().newTheme()));
            } else {
                rlt_insert.setBackgroundColor(R.color.bgMain3);
            }
        }
    }

    private String getBackgroundColorSave() {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.KEY_PREFERENCES_COLOR, MODE_PRIVATE);
        return mSharedPreferences.getString(Constants.KEY_COLOR, "");
    }

    private void saveColorBackground(String color) {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.KEY_PREFERENCES_COLOR, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.KEY_COLOR, color);
        editor.commit();
    }

    private void showDialogChooseImage() {
        Dialog mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_picture);
        LinearLayout ln_takephoto = mDialog.findViewById(R.id.lnTakephoto);
        LinearLayout ln_choosephoto = mDialog.findViewById(R.id.lnChoosephoto);
        ln_choosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
                mDialog.dismiss();
            }
        });
        ln_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
                mDialog.dismiss();
            }
        });
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(mDialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.show();
        mDialog.getWindow().setAttributes(lWindowParams);
    }

    private void getImageFromCamera() {
        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoCaptureIntent, RESULT_LOAD_CAMERA);
    }

    private void getImageFromAlbum() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    img_note.setImageBitmap(selectedImage);
                    url_image = Constants.BitmapToString(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RESULT_LOAD_CAMERA) {
                bmpImage = (Bitmap) data.getExtras().get("data");
                img_note.setImageBitmap(bmpImage);
                url_image = Constants.BitmapToString(bmpImage);
            }
        } else {
        }
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

        } catch (IllegalAccessException e) {

        }
    }

    public void showDatePickerDialog() {
        mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker = new DatePickerDialog(InsertNoteActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tv_calendar.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, year, month, dayOfMonth);
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        mDatePicker.show();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ibtnAlaramTime:
                showTimePickerDialog();
                break;
            case R.id.ibnAlaramDate:
                showDatePickerDialog();
                break;
        }
    }

    private void showTimePickerDialog() {
        mCalendar = Calendar.getInstance();
        hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        minute = mCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(InsertNoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                tv_clock.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
