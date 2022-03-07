package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Объекты класса Logs хранят данные о каждой игре
public class Logs {

    private int WinPLayer; // Кто победил ("0" - 1 игрок, "1" - 2 игрок, "2" - ничья)
    private String Field;  // Поле игры в формате строки
    Date date; // Время окончания игры

    public Logs(int WinPlayer, String Field, Date date) {
        this.WinPLayer = WinPlayer;
        this.Field = Field;
        this.date = date;
    }


    // Запись результатов в файл. Каждый раз копируются данные из старого файла Result.txt,
    // после файл перезаписывается с учетом данных о новых играх.

    public static void WriteFile(ArrayList<Logs> GameList) {

        //Массив, необходимый для записи в файл строк старых данных об играх. Так как мы не знаем
        //сколько строк (старых игр) у нас в файле, реализовал это через ArrayList
        ArrayList<String> detail = new ArrayList<>();
        //Массив int, вспомогательный, для подсчета общего рейтинга
        int[] number = new int[]{0, 0, 0};
        //Вспомогательные массив строк, необходимый для записи в файл строк старых данных об играх.
        String line[] = new String[4];
        File file1 = new File("Result.txt");

        try {
            // Если файл Result.txt не создан или пустой, создаем и добавляем в него начальные данные
            if (!file1.exists() || file1.length() == 0) {
                BufferedWriter resultWrite1 = new BufferedWriter(new FileWriter("Result.txt", StandardCharsets.UTF_8));
                resultWrite1.write("Число побед игрока 1: 0\r\n" +
                        "Число побед игрока 2: 0\r\n" +
                        "Число ничьих: 0\r\n\r\n");
                resultWrite1.flush();
                resultWrite1.close();
            }

            BufferedReader resultRead = new BufferedReader(new FileReader("Result.txt", StandardCharsets.UTF_8));

            // Считываем первые 3 строки файла, это всегда общий рейтинг
            line[0] = resultRead.readLine();
            line[1] = resultRead.readLine();
            line[2] = resultRead.readLine();

            // Добавляем в массив detail (ArrayList) подробные данные о старых играх
            while ((line[3] = resultRead.readLine()) != null) {
                detail.add(line[3]);
            }

            // Добавляем в массив detail (ArrayList) подробные данные о новых играх.
            // Проходим весь массив GameList хранящий данные о новых играх, и переносим эти данные в detail и number
            // для дальнейшей записи в файл
            for (int i = 0; i < GameList.size(); i++) {
                if (GameList.get(i).WinPLayer == 0) {
                    number[0]++;
                    detail.add("Игрок 1 - победа - " + GameList.get(i).date);
                } else if (GameList.get(i).WinPLayer == 1) {
                    number[1]++;
                    detail.add("Игрок 2 - победа - " + GameList.get(i).date);
                } else {
                    number[2]++;
                    detail.add("Ничья - " + GameList.get(i).date);
                }
                detail.add(GameList.get(i).Field + "\r\n");
            }

            // Складываем старые данные общего рейтинга с новыми (индекс строки line, с которого
            // располагаются числа побед и ничьих в общем рейтинге (первые 3 строки Result.txt) всегда статичен)
            // поэтому считываем line с этого индекса для преобразования в int
            number[0] = number[0] + Integer.parseInt(line[0].substring(22));
            number[1] = number[1] + Integer.parseInt(line[1].substring(22));
            number[2] = number[2] + Integer.parseInt(line[2].substring(14));

            // Перезаписываем файл Result.txt с учетом новых игр
            BufferedWriter resultWrite1 = new BufferedWriter(new FileWriter("Result.txt", StandardCharsets.UTF_8));
            //В первые 3 строки записываем общий рейтинг
            resultWrite1.write("Число побед игрока 1: " + number[0] + "\r\n" +
                    "Число побед игрока 2: " + number[1] + "\r\n" +
                    "Число ничьих: " + number[2] + "\r\n");
            // Далее проходим весь массив detail, в котором у нас построчно хранятся подробные данные сначала
            // о старых играх, а затем о новых.
            for (int i = 0; i < detail.size(); i++) {
                resultWrite1.write(detail.get(i) + "\r\n");
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

