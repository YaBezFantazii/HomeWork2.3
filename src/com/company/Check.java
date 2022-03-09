package com.company;

import java.util.ArrayList;

public class Check {

    public static boolean Check(String[] GameField) {
        // Проверка на окончание партии (8 вариантов)
        // 3 одинаковые ( все "Х" или все "0" ) горизонтальные линии
        for (int i = 0; i < 9; i = i + 3) {
            if (GameField[i] != "-" & GameField[i].equals(GameField[i + 1]) & GameField[i + 1].equals(GameField[i + 2])) {
                return true;}}

        // 3 одинаковые ( все "Х" или все "0" ) вертикальные линии
        for (int i = 0; i < 3; i++) {
            if (GameField[i] != "-" & GameField[i].equals(GameField[i + 3]) & GameField[i + 3].equals(GameField[i + 6])) {
                return true;}}

        // 2 одинаковые диагональные линии ( все "Х" или все "0" )
        if (GameField[0] != "-" & GameField[0].equals(GameField[4]) & GameField[4].equals(GameField[8])) {
            return true;}
        if (GameField[2] != "-" & GameField[2].equals(GameField[4]) & GameField[4].equals(GameField[6])) {
            return true;}

        return false;
    }


    // Вспомогательный метод для перевода рейтинга из файла Result.txt
    // (они читаются из файла как String) в массив GameList
    public static int[] archive (String text){

        String tempText="";
        int check=0;
        // a[0]- кол-во побед, a[1]- кол-во поражений, a[2]- кол-во ничьих
        int[] a = new int[]{0,0,0};
        char[] array = text.toCharArray();

        // Посимвольно проходим строку формата int-int-int (победы-поражения-ничьи) и переводим ее в массив int
        for (int i = 0; i < array.length; i++)
        {

            if ((array[i] != '-')&(check==0)) {
                tempText = tempText+array[i];
                a[0]=Integer.parseInt(tempText);
            } else if (check==0){
                tempText="";
                check=1;
                continue;
            }

            if ((array[i] != '-')&(check==1)) {
                tempText = tempText+array[i];
                a[1]=Integer.parseInt(tempText);
            } else if (check==1) {
                tempText="";
                check=2;
                continue;
            }

            if ((array[i] != '-')&(check==2)) {
                tempText = tempText+array[i];
                a[2]=Integer.parseInt(tempText);
            } else if (check==2) {
                tempText="";
                check=3;
                continue;
            }
        }

        return a;
    }

}
