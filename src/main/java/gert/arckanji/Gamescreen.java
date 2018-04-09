package gert.arckanji;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Gamescreen extends Activity
{
    private Excercise[] excercises;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    MediaPlayer correctplayer;
    MediaPlayer wrongplayer;
    EditText textbox;
    private boolean fastmode;
    private SharedPreferences defaultSharedPreferences;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private int score = 0;
    private int excercisenumber = 0;
    private final Runnable mHidePart2Runnable = new Runnable()
    {
        @SuppressLint("InlinedApi")
        @Override
        public void run()
        {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable()
    {
        @Override
        public void run()
        {
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        score = 0;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        fastmode = defaultSharedPreferences.getBoolean("fastmode", false);
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gamescreen);
        excercises = ChapterManager.FillExcerciseList(this);
        TextView kanji = findViewById(R.id.excercisetarget);
        kanji.setText(excercises[excercisenumber].kanjiword);
        wrongplayer = MediaPlayer.create(this,R.raw.wong);
        correctplayer = MediaPlayer.create(this,R.raw.correct);
        TextView scoretarget = findViewById(R.id.Score);
        scoretarget.setText(score + " / "+ excercises.length);
        mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        //mContentView = findViewById(R.id.fullscreen_content);

        textbox = (EditText)findViewById(R.id.Textfield);
        InsertPretext(textbox, excercises[excercisenumber]);

        if(fastmode)
        {
            textbox.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {

                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    Checkresult(s.toString());
                }
            });
        }
        else
        {
            Button submitbtn = findViewById(R.id.button2);
            submitbtn.setText("Submit");
            submitbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!Checkresult(textbox.getText().toString()))
                {
                    SkipExcercise(null);
                }
            }
        });
        }
    }

    private boolean Checkresult(String input) //handles correct
    {
        boolean result = false;
        if (input.equals(excercises[excercisenumber].hiraganaword))
        {
            if (defaultSharedPreferences.getBoolean("use_sound", false))
                correctplayer.start();
            score++;
            result = true;
            TextView scoretarget = findViewById(R.id.Score);
            scoretarget.setText(score + " / " + excercises.length);

            excercisenumber++;
            if (excercisenumber < excercises.length)
            {
                TextView kanji = findViewById(R.id.excercisetarget);
                kanji.setText(excercises[excercisenumber].kanjiword);
                textbox.setText("");
                InsertPretext(textbox, excercises[excercisenumber]);
            } else
            {
                excercisenumber--;
                ShowMessage("やった!", score + " / " + excercises.length, true);
                TextView kanji = findViewById(R.id.excercisetarget);
                kanji.setText("Done !");
            }
        }
        return result;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @SuppressLint("InlinedApi")

    public void GoHome(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    public void ShowMeaning(View view)
    {
        ShowMessage("Meaning",excercises[excercisenumber].english);
    }
    public void SkipExcercise(View view)
    {
        if(defaultSharedPreferences.getBoolean("use_sound", false))
            wrongplayer.start();
        ShowMessage("バカ!",excercises[excercisenumber].kanjiword + System.lineSeparator()+ " = "+ System.lineSeparator()+ excercises[excercisenumber].hiraganaword, false);
        excercisenumber++;
        EditText textbox = (EditText)findViewById(R.id.Textfield);
        if(excercisenumber < excercises.length)
        {
            TextView kanji = findViewById(R.id.excercisetarget);
            kanji.setText(excercises[excercisenumber].kanjiword);
            textbox.setText("");
            InsertPretext(textbox, excercises[excercisenumber]);
        }
    }

    private void InsertPretext(EditText textbox, Excercise excercise)
    {
        String[] arr = excercise.pretext.split("1");
        if (arr.length>1)
        {
            if ( arr[0] == null) arr[0] = "";
            if ( arr[1] == null) arr[1] = "";
            textbox.setText(arr[0] + arr[1]);
            textbox.setSelection(arr[0].length());
        }
        if(arr.length == 1)
        {
            textbox.setText(arr[0]);
            textbox.setSelection(arr[0].length());
        }
    }

    public void ShowMessage(String title, String message,boolean isfinished)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        if(!isfinished)
        {
            dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(excercisenumber >= excercises.length)
                        {
                            excercisenumber--;
                            ShowMessage("やった!",score + " / "+ excercises.length, true);
                            TextView kanji = findViewById(R.id.excercisetarget);
                            kanji.setText("Done !");
                        }
                    }
                });
        }
        else
        {
            dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        GoHome(null);
                    }
                });
        }
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    public void ShowMessage(String title, String message)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("ok",null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        wrongplayer.release();
        correctplayer.release();
    }
}
