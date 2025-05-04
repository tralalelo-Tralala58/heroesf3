//com.fasterxml.jackson.core:jackson-databind:jar:2.15.2
//com.fasterxml.jackson.dataformat:jackson-dataformat-xml:jar:2.15.2
import Game.saves.Player;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class proga {
    public static void main(String[] args) {
        names.add("Костя");
        names.add("Настя");
        names.add("Ксюша");
        QueReady queReady = new QueReady();
        Printer printer = new Printer();
        queReady.start();
        printer.start();

        try {
            queReady.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            printer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Все люди обслужены");
    }

    static List<String> list = Collections.synchronizedList(new ArrayList<>());
    static List<String> names = new ArrayList<>();
    static volatile boolean allNamesProcessed = false;

    static class Printer extends Thread {
        @Override
        public void run() {
            while (!allNamesProcessed || !list.isEmpty()) {
                synchronized (list) {
                    while (list.isEmpty()) {
                        if (allNamesProcessed) {
                            return; // Завершаем, если все имена обработаны и список пуст
                        }
                        try {
                            list.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("list elem - " + list.remove(0));
                }
            }
        }
    }

    static class QueReady extends Thread {
        @Override
        public void run() {
            for (String name : names) {
                synchronized (list) {
                    list.add(name);
                    list.notify();
                }

                try {
                    Thread.sleep(500); // Небольшая задержка между добавлениями
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Помечаем, что все имена добавлены
            allNamesProcessed = true;
            synchronized (list) {
                list.notify(); // Будим Printer на случай, если он ждет
            }
        }
    }
}



