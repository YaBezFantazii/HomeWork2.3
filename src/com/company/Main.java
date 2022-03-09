package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String NickName1, NickName2;
        Scanner console = new Scanner(System.in);
        String[] FieldGame;
        String CheckNew = "";
        String Player;
        Boolean CheckWin;
        int number, DeadHeat;
        //Массив с результатами игр. Элементами массива являются объекты класса Logs.
        ArrayList<Logs> GameList = new ArrayList<>();

        // Правила
        System.out.println("Для выбора ячейки куда хотите поставить крестик введите через консоль" +
                " соответствующее число ");
        System.out.println(" | 1 | 2 | 3 | \n" +
                           " | 4 | 5 | 6 | \n" +
                           " | 7 | 8 | 9 | ");
        System.out.println("Запись результатов файл происходит после полного завершения игр через команду 'n' в консоли.\n" +
                "Так же команда 'r' в консоли приводит к архивации старого рейтинга (перезаписи его в другой файл и \n" +
                "удалению текущего рейтинга\n");
        // Вводим имя игроков.
        System.out.println("Введите имя 1 игрока");
        NickName1 = console.nextLine();
        System.out.println("Введите имя 2 игрока");
        NickName2 = console.nextLine();
        GameList.add(new Logs(NickName1,0,0,0));
        GameList.add(new Logs(NickName2,0,0,0));

        // Проверка на то, хотим ли мы начать новую игру(y), закончить (n) или архивировать и обнулить рейтинг (r)
        while (!CheckNew.equals("n")){

            System.out.println("Для новой игры введите 'y'\nДля завершения игр и записи результатов в файл введите 'n'\n" +
                    "Для архивации и удаления старого рейтинга введите 'r'");
            CheckNew = console.nextLine();

            if (CheckNew.equals("y")) {
                // Обнуляем поле игры
                DeadHeat = 0;
                FieldGame = new String[] {"-", "-", "-", "-", "-", "-", "-", "-", "-"};
                Player = "X";
                CheckWin = false;
                // Играем
                while (!CheckWin) {

                    if (Player.equals("X")){
                        System.out.println("Ходит игрок "+NickName1+". Введите число, куда хотите поставить " + Player);
                    } else {
                        System.out.println("Ходит игрок "+NickName2+". Введите число, куда хотите поставить " + Player);
                    }

                    number = console.nextInt()-1;

                    // Проверка на корректность введенного числа и заполнености ячейки
                    if (number>8 || number<0 || FieldGame[number].equals("X") || FieldGame[number].equals("0")) {
                        System.out.println("Ячейка уже занята или введено неккоректное число (больше 9 или меньше 1)");
                    // Если все в порядке, записываем Х или 0 в поле,производим проверку на победу, меняем игрока
                    } else {
                        // Записываем Х или 0 в ячейку поля
                        FieldGame[number] = Player;
                        // Меняем текущего игрока
                        if (Player.equals("X")) {Player = "0";} else {Player = "X";}
                        console.nextLine();
                        //Печататем текущее поле в консоль
                        System.out.println(PrintField(FieldGame));
                        // Проверка на победу
                        CheckWin = Check.Check(FieldGame);
                        // Если DeadHeat станет больше 8, то ничья, так как все ячейки будут заполненые и проверку на победу не пройдена.
                        DeadHeat++;
                    }
                    // Пишем как закончилась игра, и добавляем новый элемент с результатом в массив GameList для дальнейшей записи в файл
                    if (CheckWin & Player.equals("0")) {
                        System.out.println("!!!Победил игрок "+NickName1+"!!!\n");
                        GameList.get(0).setWin( GameList.get(0).getWin()+1 );
                        GameList.get(1).setLose( GameList.get(1).getLose()+1 );
                    } else if (CheckWin & Player.equals("X")) {
                        System.out.println("!!!Победил игрок "+NickName2+"!!!\n");
                        GameList.get(0).setLose( GameList.get(0).getLose()+1 );
                        GameList.get(1).setWin( GameList.get(1).getWin()+1 );
                    } else if (DeadHeat>8) {
                        System.out.println("!!!Ничья!!!\n");
                        GameList.get(0).setDeadHeat( GameList.get(0).getDeadHeat()+1 );
                        GameList.get(1).setDeadHeat( GameList.get(1).getDeadHeat()+1 );
                        CheckWin = true;
                    }
                }

            // Конец игр. Записываем данные о прошедших играх в рейтинг.
            } else if (CheckNew.equals("n")) {
                System.out.println("Конец игры. Вся статистика записана в файл Result.txt");
                if ((GameList.get(0).getWin()!=0)||(GameList.get(0).getLose()!=0)||(GameList.get(0).getDeadHeat()!=0)){
                    Logs.WriteFile(GameList);
                }
            // Архивируем и удаляем старый рейтинг. Рейтинг из текущей сессии сохранятся в архивном файле
            } else if (CheckNew.equals("r")){
                if ((GameList.get(0).getWin()!=0)||(GameList.get(0).getLose()!=0)||(GameList.get(0).getDeadHeat()!=0)){
                    Logs.WriteFile(GameList);
                }
                //Архивируем рейтинг
                Logs.Archive();
                // Очищаем текущую сессию от данных
                GameList.clear();
                // Создаем игроков снова, чтобы они могли продолжать играть в этой сессии
                GameList.add(new Logs(NickName1,0,0,0));
                GameList.add(new Logs(NickName2,0,0,0));
                System.out.println("Рейтинг заархивирован и result.txt обнулен.");
            // Можно вводить только "y" "n" "r"
            } else {
                System.out.println("Введен неккоректный символ");
            }
        }




    }


    // Метод, возвращающий поле текущей игры в формате строки
    public static String PrintField(String[] field){
        return ( " | "+field[0]+" | "+field[1]+" | "+field[2]+" | \r\n" +
                 " | "+field[3]+" | "+field[4]+" | "+field[5]+" | \r\n" +
                 " | "+field[6]+" | "+field[7]+" | "+field[8]+" |");
    }
}
