package gert.arckanji;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Gert on 20/01/2018.
 */

public final class ChapterManager
{
    public static Excercise[]  BuildExcercises(int count, String[] chapters)
    {
        Excercise[] testarr;
        int[] chapteraddedcount = new int[chapters.length];
        int prevadded = -1;
        int added=0;
        Excercise[][] chapterexarr = new Excercise[chapters.length][];
        int totalcount = 0;
        //init that shit
        for(int i = 0; i< chapters.length;i++)
        {
            testarr = GetChapterExcercise(chapters[i]);
            totalcount += testarr.length;
            shuffleArray(testarr);
            chapterexarr[i] = testarr;
            chapteraddedcount[i] = 0;
        }
        if(count == 99)
            count = totalcount;
        Excercise[] excercises = new Excercise[count];
        while(added != count && prevadded != added)
        {
            prevadded = added;
            for(int chapcnt = 0;chapcnt < chapters.length;chapcnt++)
            {
                if (added != count && chapteraddedcount[chapcnt] < chapterexarr[chapcnt].length)
                {
                    excercises[added] = chapterexarr[chapcnt][chapteraddedcount[chapcnt]];
                    //excercises[added] = RetreiveExercise(excercises, chapters[chapcnt]);
                    chapteraddedcount[chapcnt] = chapteraddedcount[chapcnt]+1;
                    added++;
                }
            }
        }

        excercises = TrimEmpty(excercises);
        shuffleArray(excercises);
        return excercises;
    }

    private static Excercise[] TrimEmpty(Excercise[] excercises)
    {
        int filledamount = 0;
        for (int i = 0; i < excercises.length; i++){
            if(excercises[i] != null)
                filledamount++;
        }
        Excercise[] filledarr = new Excercise[filledamount];

        filledamount=0;
        for (int i = 0; i < excercises.length; i++){
            if(excercises[i] != null)
            {
                filledarr[filledamount] = excercises[i];
                filledamount++;
            }
        }
        return filledarr;
    }

    private static void FillArrays(Excercise[] toFillarr, Excercise[] Contentarr)
    {
        int contentcounter = 0;
        for(int i = 0; (i < toFillarr.length) && (contentcounter < Contentarr.length); i++)
        {
            if(toFillarr[i] == null)
            {
                toFillarr[i] = Contentarr[contentcounter];
                contentcounter++;
            }
        }
    }
    static Excercise[] trimArray(Excercise[] ar, int newSize)
    {
        Excercise[] trimmedExcercises = new Excercise[newSize];
        for (int i = 0; i < newSize && i< ar.length; i++){
            trimmedExcercises[i] = ar[i];
        }
        return trimmedExcercises;
    }

    static void shuffleArray(Excercise[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = generateRandomInt(0,i+1);
            // Simple swap
            Excercise a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
    static void shuffleArray(String[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = generateRandomInt(0,i+1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    static int generateRandomInt(int lower, int upper)
    {
        int randint = 9999999;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            randint = ThreadLocalRandom.current().nextInt(lower, upper);
        }
        else
        {
            Random rnd = new Random();
            randint = rnd.nextInt(upper);
        }
        return randint;
    }

    private static boolean Contains(Excercise[] deja, Excercise chapterex)
    {
        for (int i = deja.length - 1; i > 0; i--)
        {
            if(deja[i] != null)
            {
                if (deja[i].equals(chapterex))
                    return true;
            }
        }
        return false;
    }

    public static Excercise[] FillExcerciseList(Context context)
    {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Excercise[] oefeningskes;
        String[] chapterarr = MakeChapterlist(defaultSharedPreferences);
        int excount = Integer.parseInt(defaultSharedPreferences.getString("excount","10"));
        oefeningskes = ChapterManager.BuildExcercises(excount, chapterarr);
        return oefeningskes;
    }

    public static String CheckChapterSetting(String chapter, String chapters, SharedPreferences prefs)
    {
        if(prefs.getBoolean(chapter,false))
            chapters += chapter+",";
        return chapters;
    }

    private static String[] MakeChapterlist(SharedPreferences prefs)
    {
        String Chapters = "";
        Chapters=CheckChapterSetting("N6",Chapters,prefs);
        Chapters=CheckChapterSetting("N5",Chapters,prefs);
        Chapters=CheckChapterSetting("N4",Chapters,prefs);
        Chapters=CheckChapterSetting("N3",Chapters,prefs);
        Chapters=CheckChapterSetting("N2",Chapters,prefs);
        Chapters=CheckChapterSetting("N1",Chapters,prefs);
        Chapters=CheckChapterSetting("x",Chapters,prefs);
        if(Chapters.endsWith(","))
            Chapters = Chapters.substring(0,Chapters.length()-1);
        String[] chapterarr = Chapters.split(",");
        if(chapterarr.length == 1 && chapterarr[0] == "")
            chapterarr = new String[] {"x"};
        return chapterarr;
    }

    public static String GetCurrentKanji(Context context)
    {
        String kanji = "";
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String[] chapterarr = MakeChapterlist(defaultSharedPreferences);
        for (int i = 0; i < chapterarr.length; i++)
        {
            kanji = kanji +KanjiFromChapter(chapterarr[i]);
        }
        return kanji;
    }

    private static String KanjiFromChapter(String Chapter)
    {
        String result = "";
        Excercise[] excs = GetChapterExcercise(Chapter);
        for (int i= 0; i < excs.length; i++)
        {
            if(!result.contains(excs[i].kanji))
                result = result + excs[i].kanji;
        }
        return result;
    }

    static Excercise[] GetChapterExcercise(String Chapter)
    {
        Excercise[] excercises = new Excercise[0];
        switch (Chapter)
        {
            case "N6":
            {
                Excercise[] temp1 = ExcerciseData.GenerateN4();
                Excercise[] temp2 = GetChapterExcercise("N3");
                Excercise[] temp3 = GetChapterExcercise("N2");
                Excercise[] temp4 = GetChapterExcercise("N1");
                Excercise[] copydestination = new Excercise[1000];
                int addedcount = 0;
                String s  ="家族兄弟姉妹私育部屋広低緑静近遠会社働作工場始終店客親切売当品便利使銀白黒紙朝晩昼夜前後午早荷送宅急速遅重軽住所様主番地号京都道府県市区村毎週映画図館公園夫妻特思料理有洋服衣短毛糸玉光";
                for(int i = 0; i < temp1.length;i++)
                {
                    if (s.contains(temp1[i].kanji))
                    {
                        copydestination[addedcount] = temp1[i];
                        addedcount++;
                    }
                }
                for(int i = 0; i < temp2.length;i++)
                {
                    if (s.contains(temp2[i].kanji))
                    {
                        copydestination[addedcount] = temp2[i];
                        addedcount++;
                    }
                }
                for(int i = 0; i < temp3.length;i++)
                {
                    if (s.contains(temp3[i].kanji))
                    {
                        copydestination[addedcount] = temp3[i];
                        addedcount++;
                    }
                }
                for(int i = 0; i < temp4.length;i++)
                {
                    if (s.contains(temp4[i].kanji))
                    {
                        copydestination[addedcount] = temp4[i];
                        addedcount++;
                    }
                }
                excercises = new Excercise[addedcount];
                for(int i = 0; i < addedcount;i++)
                {
                    excercises[i] = copydestination[i];
                }
                break;
            }
            case "N5":
            {
                excercises =ExcerciseData.GenerateN5();
                break;
            }
            case "N4":
            {
                excercises =ExcerciseData.GenerateN4();
                break;
            }
            case "N3":
            {
                Excercise[] e1 =ExcerciseData.GenerateN3Part1();
                excercises =ExcerciseData.GenerateN3Part2();
                System.arraycopy(e1,0,excercises,0,e1.length);
                break;
            }
            case "N2":
            {
                excercises =ExcerciseData.GenerateN2();
                break;
            }
            case "N1":
            {
                excercises =ExcerciseData.GenerateN1();
                break;
            }
            case "x":
            {
                excercises = new Excercise[10];
                excercises[0] = new Excercise(1,6,6,"啄","啄木鳥","きつつき",  "???", "");
                excercises[1] = new Excercise(2,6,6,"蒡","牛蒡","ごぼう",  "???", "");
                excercises[2] = new Excercise(3,6,6,"凝","混凝土","こんくりいと",  "???", "");
                excercises[3] = new Excercise(4,6,6,"髷","丁髷","ちょんまげ",  "???", "");
                excercises[4] = new Excercise(5,6,6,"躑","躑躅","つつじ",  "???", "");
                excercises[5] = new Excercise(6,6,6,"太","心太","ところてん",  "???", "");
                excercises[6] = new Excercise(7,6,6,"熊","熊猫","ぱんだ",  "???", "");
                excercises[7] = new Excercise(8,6,6,"贔","贔屓","ひいき",  "???", "");
                excercises[8] = new Excercise(9,6,6,"豌","豌豆","えだまめ",  "???", "");
                excercises[9] = new Excercise(10,6,6,"麗","瑰麗","かいれい",  "???", "");
                break;
            }
        }
        return excercises;
    }
}
//https://matome.naver.jp/odai/2144230372940075701