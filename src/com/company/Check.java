package com.company;

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
}
