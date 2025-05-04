package Game.tests;

import Game.Map.*;
import Game.UI.UI;
import Game.saves.*;

import java.io.*;
import java.util.Scanner;

public class Test1 {
    public static final String PLAYER1_SAVE = "C:"+ File.separator +"Users" + File.separator +"shura"+ File.separator +"IdeaProjects"+ File.separator +"untitled"+ File.separator +"src"+ File.separator +"Game"+ File.separator+ "saves"+ File.separator + "save1.bin";
    public static final String PLAYER2_SAVE = "C:"+ File.separator +"Users" + File.separator +"shura"+ File.separator +"IdeaProjects"+ File.separator +"untitled"+ File.separator +"src"+ File.separator +"Game"+ File.separator+ "saves"+ File.separator + "save2.bin";
    public static final String SCORES_FILE = "C:"+ File.separator +"Users" + File.separator +"shura"+ File.separator +"IdeaProjects"+ File.separator +"untitled"+ File.separator +"src"+ File.separator +"Game"+ File.separator+ "saves"+ File.separator + "scores.bin";
    public static final String SCORES_FILE_JSON = "C:"+ File.separator +"Users" + File.separator +"shura"+ File.separator +"IdeaProjects"+ File.separator +"untitled"+ File.separator +"src"+ File.separator +"Game"+ File.separator+ "saves"+ File.separator + "test.json";
    public static final String SCORES_FILE_XML = "C:"+ File.separator +"Users" + File.separator +"shura"+ File.separator +"IdeaProjects"+ File.separator +"untitled"+ File.separator +"src"+ File.separator +"Game"+ File.separator+ "saves"+ File.separator + "persons.xml";
    public static final String TEST_SCORES_FILE = "C:"+ File.separator +"Users" + File.separator +"shura"+ File.separator +"IdeaProjects"+ File.separator +"untitled"+ File.separator +"src"+ File.separator +"Game"+ File.separator+ "saves"+ File.separator + "Test_scores.bin";
    public static final String TEST_SAVE = "C:"+ File.separator +"Users" + File.separator +"shura"+ File.separator +"IdeaProjects"+ File.separator +"untitled"+ File.separator +"src"+ File.separator +"Game"+ File.separator+ "saves"+ File.separator + "testing_save.bin";


    public static final String CROSS_MAP = "CROSS";
    public static final String DIAGONAL_MAP = "DIAGONAL";

    public static void main(String[] args){
        int height = 10;
        int weight = 10;
        GameMap map;
        Scanner scanner = new Scanner(System.in);
        UI ui = new UI();

        Player player1 = new Player("Игрок1");
        Player player2 = new Player("Игрок2");

        try{
            Savings savings = new Savings(SCORES_FILE, SCORES_FILE_JSON, SCORES_FILE_XML);
            //savings.loadScoresInXML(player1, player2);
            savings.loadScoresInJson(player1, player2);
            //savings.loadScores(player1, player2);

            ui.choosePlayer();

            int playerChoice = scanner.nextInt();
            scanner.nextLine();

            Player currentPlayer = (playerChoice == 1) ? player1 : player2;

            // Меню загрузки/новой игры
            ui.chooseLoad();

            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            if (choice == 2) {
                try {
                    map = savings.loadGame(currentPlayer == player1 ? PLAYER1_SAVE : PLAYER2_SAVE);
                    ui.saveSuccess();
                } catch (IOException | ClassNotFoundException e) {
                    ui.saveError();

                    ui.chooseMap();

                    int mapChoice = scanner.nextInt();
                    scanner.nextLine();

                    String mapType = (mapChoice == 1) ? CROSS_MAP : DIAGONAL_MAP;

                    map = new GameMap(height, weight, currentPlayer.name, mapType, true, false);
                    savings.saveGame(currentPlayer == player1 ? PLAYER1_SAVE : PLAYER2_SAVE, map);
                }
            } else {

                ui.chooseMap();
                int mapChoice = scanner.nextInt();
                scanner.nextLine();

                String mapType = (mapChoice == 1) ? CROSS_MAP : DIAGONAL_MAP;

                ui.chooseGenerateMap();

                String choose = scanner.nextLine().toLowerCase();
                if(choose.equals("mod")){
                    map = new GameMap(height, weight, currentPlayer.name, mapType, true, true);
                }else{
                    map = new GameMap(height, weight, currentPlayer.name, mapType, true, false);
                }

                savings.saveGame(currentPlayer == player1 ? PLAYER1_SAVE : PLAYER2_SAVE, map);
            }

            ui.chooseMove();

            while (true) {
                boolean playerMovesExhausted = false;

                while(!playerMovesExhausted) {
                    playerMovesExhausted = true;

                    if(map.isGAmeOver){
                        break;
                    }

                    if (map.heroes[0].move > 0 && canHeroMove(map.heroes[0], height, weight, map.mapType)) {
                        playerMovesExhausted = false;
                        ui.yourStep();
                        String input = scanner.nextLine().toLowerCase();

                        switch (input) {
                            case "w":
                                map.moveHero(map.heroes[0], 0, -1);
                                break;
                            case "wd", "dw":
                                map.moveHero(map.heroes[0], 1, -1);
                                break;
                            case "wa", "aw":
                                map.moveHero(map.heroes[0], -1, -1);
                                break;
                            case "as", "sa":
                                map.moveHero(map.heroes[0], -1, 1);
                                break;
                            case "sd", "ds":
                                map.moveHero(map.heroes[0], 1, 1);
                                break;
                            case "s":
                                map.moveHero(map.heroes[0], 0, 1);
                                break;
                            case "a":
                                map.moveHero(map.heroes[0], -1, 0);
                                break;
                            case "d":
                                map.moveHero(map.heroes[0], 1, 0);
                                break;
                            case "q":
                                ui.printExit();
                                return;
                            case "save":
                                savings.saveGame(currentPlayer == player1 ? PLAYER1_SAVE : PLAYER2_SAVE, map);
                                ui.saveGame();
                                break;
                            default:
                                ui.invalidInput();
                        }
                    }
                }

                // Ход компьютера (остаётся без изменений)
                boolean computerMovesExhausted = false;
                while (!computerMovesExhausted && !map.isBattleStarted && map.compIsAlive) {
                    if(map.isGAmeOver) break;
                    computerMovesExhausted = true;
                    if (map.heroes[1].move > 0 && canHeroMove(map.heroes[1], height, weight, map.mapType)) {
                        computerMovesExhausted = false;
                        ui.compStep();
                        if(map.mapType.equals(CROSS_MAP)){
                            map.moveHero(map.heroes[1], -1, 0);
                        }else{
                            map.moveHero(map.heroes[1], -1, -1);
                        }
                    }
                }

                if(map.isGAmeOver) {
                    currentPlayer.score += map.isGamerWinner ? 100 : 50;
                    savings.saveScoresInJson(player1, player2);
                    savings.saveScores(player1, player2);
                    savings.saveScoresInXML(player1, player2);

                    ui.printScores(player1, player2);
                    map.clearSaveFile();
                    break;
                }

                map.endRound(map.heroes);
                savings.saveGame(currentPlayer == player1 ? PLAYER1_SAVE : PLAYER2_SAVE, map);
                ui.saveGame();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean canHeroMove(Hero hero, int height, int weight, String mapType) {
        int currentX = hero.heroX;
        int currentY = hero.heroY;
        if (mapType.equals(CROSS_MAP)) {
            if ((currentY == height / 2 || currentX == weight / 2) && hero.move >= 1) {
                return true;
            } else if (hero.move >= 2) {
                return true;
            }
        }else {
            if((currentX == currentY || currentX == height - currentY - 1) && hero.move >= 1){
                return true;
            } else if (hero.move >= 2) {
                return true;
            }
        }
        return false;
    }
}
