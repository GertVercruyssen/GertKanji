package gert.arckanji;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class KanjiBuilder extends Activity
{
    MediaPlayer correctplayer;
    MediaPlayer wrongplayer;
    Excercise[] excercises;
    String Kanji;
    String[] guesses;
    int sequencecount;
    int excercisecount;
    private int score;
    TextView scoretext;
    private SharedPreferences defaultSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_kanji_builder);
        //get excercises
        excercises = ChapterManager.FillExcerciseList(this);
        //get kanjilist current
        Kanji = ChapterManager.GetCurrentKanji(this);
        //Todo add more difficult kanji to guess;
        //set up screen
        wrongplayer = MediaPlayer.create(this,R.raw.wong);
        correctplayer = MediaPlayer.create(this,R.raw.correct);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        score = 0;
        scoretext = findViewById(R.id.score);
        scoretext.setText(score+" / "+excercises.length);
        sequencecount = 0;
        excercisecount = 0;
        SetupExcercise(excercises[excercisecount]);
        //set up clickhandlers

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Button btn = (Button)v;

                TextView antarget = findViewById(R.id.AnswerTarget);
                String text = antarget.getText().toString();
                if(text.contains("?"))
                    text.replace('?',btn.getText().charAt(0));
                else
                    text = text + btn.getText();
                antarget.setText(text);
                sequencecount++;
                //check if correct
                if(btn.getText() == excercises[excercisecount].kanji)
                {
                    //correct
                    score++;
                    scoretext.setText(score+" / "+excercises.length);
                    if (defaultSharedPreferences.getBoolean("use_sound", false))
                        correctplayer.start();
                    if (excercises[excercisecount] == excercises[excercises.length - 1])
                    {
                        ShowMessage("やった!", score + " / " + excercises.length, true);
                    }
                    else
                    {
                        excercisecount++;
                        SetupExcercise(excercises[excercisecount]);
                    }
                }
                else
                {
                    //incorrect
                    if (defaultSharedPreferences.getBoolean("use_sound", false))
                        wrongplayer.start();
                    ShowMessage("バカ",excercises[excercisecount].kanjiword+ System.lineSeparator()+"="+System.lineSeparator()+excercises[excercisecount].hiraganaword);

                    if (excercises[excercisecount] == excercises[excercises.length - 1])
                    {
                        ShowMessage("やった!", score + " / " + excercises.length, true);
                    }
                    else
                    {
                        excercisecount++;
                        SetupExcercise(excercises[excercisecount]);
                        if (defaultSharedPreferences.getBoolean("use_sound", false))
                            wrongplayer.start();
                    }
                }
            }
        };
        findViewById(R.id.Kanjibtn1).setOnClickListener(listener);
        findViewById(R.id.kanjibtn2).setOnClickListener(listener);
        findViewById(R.id.kanjibtn3).setOnClickListener(listener);
        findViewById(R.id.kanjibtn4).setOnClickListener(listener);
        findViewById(R.id.kanjibtn5).setOnClickListener(listener);
        findViewById(R.id.kanjibtn6).setOnClickListener(listener);
        findViewById(R.id.kanjibtn7).setOnClickListener(listener);
        findViewById(R.id.kanjibtn8).setOnClickListener(listener);
    }

    private void SetupExcercise(Excercise ex)
    {
        TextView extarget = findViewById(R.id.ExcerciseTarget);
        extarget.setText(ex.hiraganaword);
        TextView antarget = findViewById(R.id.AnswerTarget);
        antarget.setText(ex.kanjiword.replace(ex.kanji.charAt(0),'?'));
        guesses = new String[8];
        SetupButtons();
    }

    private void SetupButtons()
    {
        SetupGuesses(excercises[excercisecount].kanji);
        Button btn1 = findViewById(R.id.Kanjibtn1);
        btn1.setText(guesses[0]);
        Button btn2 = findViewById(R.id.kanjibtn2);
        btn2.setText(guesses[1]);
        Button btn3 = findViewById(R.id.kanjibtn3);
        btn3.setText(guesses[2]);
        Button btn4 = findViewById(R.id.kanjibtn4);
        btn4.setText(guesses[3]);
        Button btn5 = findViewById(R.id.kanjibtn5);
        btn5.setText(guesses[4]);
        Button btn6 = findViewById(R.id.kanjibtn6);
        btn6.setText(guesses[5]);
        Button btn7 = findViewById(R.id.kanjibtn7);
        btn7.setText(guesses[6]);
        Button btn8 = findViewById(R.id.kanjibtn8);
        btn8.setText(guesses[7]);
    }

    private void SetupGuesses(String CurrentKanji)
    {
        String[] possiblekanji = Kanji.split("(?!^)");
        boolean addedcorrect=false;
        ChapterManager.shuffleArray(possiblekanji);
        for (int i = 0; i<8 && i<possiblekanji.length;i++)
        {
            guesses[i] = possiblekanji[i];
            if(possiblekanji[i].equals(CurrentKanji))
                addedcorrect = true;
        }
        if(!addedcorrect)
            guesses[7] = CurrentKanji;
        ChapterManager.shuffleArray(guesses);
    }
    public void ShowMessage(String title, String message, final boolean isfinished)
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
                            if(excercisecount >= excercises.length)
                            {
                                excercisecount--;
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

    public void ShowMeaning(View view)
    {
        ShowMessage("Meaning",excercises[excercisecount].english);
    }
    public void GoHome(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
