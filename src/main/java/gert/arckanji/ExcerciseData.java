package gert.arckanji;

/**
 * Created by Gert on 03/03/2018.
 */

public final class ExcerciseData
{
    public static Excercise[] GenerateN5()
    {
        Excercise[] excercises = new Excercise[3];
        excercises[0] = new Excercise(1, 5, 5, "日", "日曜日", "にちようび", "Sunday", "1ようび");
        excercises[1] = new Excercise(1150, 5, 1, "雨", "雨具", "あまぐ", "rain gear", "1ぐ");
        excercises[2] = new Excercise(1151, 5, 1, "雨", "雨天", "うてん", "rainy weather", "1てん");
        return excercises;
    }
    public static Excercise[] GenerateN4()
    {
        Excercise[] excercises = new Excercise[3];
        excercises[0] = new Excercise(1, 4, 5, "会", "会う", "あう", "to meet", "1う");
        excercises[1] = new Excercise(1538, 4, 2, "漢", "漢和", "かんわ", "Chinese Character-Japanese (e.g. dictionary)", "1んわ");
        excercises[2] = new Excercise(1539, 4, 1, "漢", "漢語", "かんご", "Chinese word, Sino-Japanese word", "1んご");
        return excercises;
    }
    public static Excercise[] GenerateN3Part1()
    {
        Excercise[] excercises = new Excercise[3];
        excercises[0] = new Excercise(1, 3, 4, "政", "政治", "せいじ", "politics,government", "1じ");
        excercises[1] = new Excercise(1090, 3, 3, "球", "地球", "ちきゅう", "the earth", "ち1");
        excercises[2] = new Excercise(1091, 3, 1, "球", "球根", "きゅうこん", "(plant) bulb", "1こん");
        return excercises;
    }
    public static Excercise[] GenerateN3Part2()
    {
        Excercise[] excercises = new Excercise[1093];
        excercises[1091] = new Excercise(1092, 3, 3, "職", "職", "しょく", "employment", "1");
        excercises[1092] = new Excercise(2380, 3, 2, "幾", "幾分", "いくぶん", "somewhat", "1ぶん");
        excercises[1093] = new Excercise(2381, 3, 1, "幾", "幾多", "いくた", "many, numerous", "1た");
        return excercises;
    }
    public static Excercise[] GenerateN2()
    {
        Excercise[] excercises = new Excercise[3];
        excercises[0] = new Excercise(1, 2, 3, "党", "党", "とう", "party (political)", "1");
        excercises[1] = new Excercise(1048, 2, 5, "府", "府県", "ふけん", "prefecture", "1けん");
        excercises[2] = new Excercise(1049, 2, 5, "府", "府警", "ふけい", "prefectural police", "1けい");
        return excercises;
    }
    public static Excercise[] GenerateN1()
    {
        Excercise[] excercises = new Excercise[3];
        excercises[0] = new Excercise(1, 1, 3, "氏", "氏", "し", "family name,lineage,birth", "1");
        excercises[1] = new Excercise(1364, 1, 1, "鞠", "鞠", "まり", "ball", "1り");
        excercises[2] = new Excercise(1365, 1, 1, "濫", "濫用", "らんよう", "abuse, misuse, misappropriation, using to excess", "1よう");
        return excercises;
    }

}
