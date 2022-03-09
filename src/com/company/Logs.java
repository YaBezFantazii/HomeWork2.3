package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

// Объекты класса Logs хранят данные о результатах каждого игрока каждой игры
public class Logs {

    private String NickName; // Имя игрока
    private int Win; // Кол-во побед
    private int Lose; // Кол-во поражений
    private int DeadHeat; // Кол-во ничьих

    public Logs( String NickName,int Win, int Lose, int DeadHeat) {
        this.NickName = NickName;
        this.Win = Win;
        this.Lose = Lose;
        this.DeadHeat = DeadHeat;
    }

    // Геттеры и сеттеры
    public int getWin() {
        return Win;
    }

    public void setWin(int win) {
        Win = win;
    }

    public int getLose() {
        return Lose;
    }

    public void setLose(int lose) {
        Lose = lose;
    }

    public int getDeadHeat() {
        return DeadHeat;
    }

    public void setDeadHeat(int deadHeat) {
        DeadHeat = deadHeat;
    }


    public static void WriteFile(ArrayList<Logs> GameList) {

        String a1,a2,a3,a4;
        //Массив int, вспомогательный, для подсчета общего рейтинга
        int[] number = new int[]{0, 0, 0};
        //Вспомогательные массив строк, необходимый для записи в файл строк старых данных об играх.
        String line[] = new String[2];
        File file1 = new File("Result.txt");

        try {
            // Если файл Result.txt не создан, то создаем его
            if (!file1.exists() || file1.length() == 0) {
                BufferedWriter resultWrite1 = new BufferedWriter(new FileWriter("Result.txt", StandardCharsets.UTF_8));
                resultWrite1.flush();
                resultWrite1.close();
            }

            BufferedReader resultRead = new BufferedReader(new FileReader("Result.txt", StandardCharsets.UTF_8));
            resultRead.readLine();
            resultRead.readLine();

            // Берем данные о рейтинге из файла Result.txt и записываем их в массив GameList
            while ((line[0] = resultRead.readLine()) != null) {
                line[1] = resultRead.readLine();
                resultRead.readLine();
                a1 = line[0].substring(7);
                number = Check.archive(line[1]);
                GameList.add(new Logs(a1,number[0],number[1],number[2]));
            }


            // Объединяем старые и новые данные о рейтинге (если только что сыгравший игрок уже был в рейтинге)
            for (int i=0;i<GameList.size();i++){
                for (int g=i;g<GameList.size();g++){
                    if ((GameList.get(i).NickName.equals( GameList.get(g).NickName ))&(i!=g)){
                        GameList.get(i).Win = GameList.get(i).Win + GameList.get(g).Win;
                        GameList.get(i).Lose = GameList.get(i).Lose + GameList.get(g).Lose;
                        GameList.get(i).DeadHeat = GameList.get(i).DeadHeat + GameList.get(g).DeadHeat;
                        GameList.remove(g);
                    }
                }
            }



            //Сортировка игроков (производится в порядке убывания кол-ва побед)
            GameList.sort(new Comparator<Logs>() {
                public int compare(Logs o1, Logs o2) {
                    if (o1.Win == o2.Win) return 0;
                    else if (o1.Win< o2.Win) return 1;
                    else return -1;
                }
            });

            BufferedWriter resultWrite1 = new BufferedWriter(new FileWriter("Result.txt", StandardCharsets.UTF_8));

            // Записываем обновленный рейтинг в файл Result.txt
            resultWrite1.write("Побед - поражений - ничьих\r\n\r\n");
            for (int i = 0; i < GameList.size(); i++) {
                resultWrite1.write("Игрок: "+GameList.get(i).NickName + "\r\n");
                resultWrite1.write(GameList.get(i).Win+"-"+GameList.get(i).Lose+"-"+GameList.get(i).DeadHeat+"\r\n\r\n");
            }

            resultWrite1.flush();
            resultWrite1.close();
            resultRead.close();


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    // Метод, который копирует текущий рейтинг в файл с названием в
    // формате (день-месяц-год час-минута-секунда) (и создает этот файл),
    // после чего удаляет файл Result.txt (обнуляет рейтинг).
    public static void Archive() {
        Date date = new Date();
        DateFormat form = new SimpleDateFormat("dd-mm-yyyy hh.mm.ss"); // необходимо для перевода date в string
        String result;
        try {
            result = form.format(date)+".txt";
            Path path1 = FileSystems.getDefault().getPath("Result.txt");
            Path path2 = FileSystems.getDefault().getPath(result);
            Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(path1);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

