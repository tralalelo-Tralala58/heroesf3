package Game.saves;

import Game.UI.UI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import Game.Map.GameMap;
import java.io.*;

public class Savings {

    UI ui = new UI();
    public String SCORES_FILE_JSON;
    public String SCORES_FILE;
    public String SCORES_FILE_XML;

    public Savings(String SCORES_FILE, String SCORES_FILE_JSON, String SCORES_FILE_XML){
        this.SCORES_FILE = SCORES_FILE;
        this.SCORES_FILE_JSON = SCORES_FILE_JSON;
        this.SCORES_FILE_XML = SCORES_FILE_XML;
    }

    public void saveScores(Player p1, Player p2) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORES_FILE))) {
            oos.writeObject(p1);
            oos.writeObject(p2);
        }
    }

    public void saveScoresInJson(Player p1, Player p2){
        try (FileWriter fw = new FileWriter(SCORES_FILE_JSON)){
            Player[] players = {p1, p2};
            Gson gson = new Gson();
            gson.toJson(players, fw);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadScoresInJson(Player p1, Player p2){
        File file = new File(SCORES_FILE_JSON);

        if (!file.exists() || file.length() == 0) {
            p1.name = "Player 1";
            p1.score = 0;
            p2.name = "Player 2";
            p2.score = 0;
            return;
        }

        try (FileReader fr = new FileReader(SCORES_FILE_JSON)){
            Gson gson = new Gson();
            Player[] myPlayers = gson.fromJson(fr, Player[].class);
            p1.name = myPlayers[0].name;
            p1.score = myPlayers[0].score;

            p2.name = myPlayers[1].name;
            p2.score = myPlayers[1].score;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void saveScoresInXML(Player p1, Player p2){
        ObjectMapper om = new XmlMapper();
        Player[] players = {p1, p2};

        try {
            om.writeValue(new File(SCORES_FILE_XML), players);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadScoresInXML(Player p1, Player p2){
        File file = new File(SCORES_FILE_XML);

        if (!file.exists() || file.length() == 0) {
            p1.name = "Player 1";
            p1.score = 0;
            p2.name = "Player 2";
            p2.score = 0;
            return;
        }

        ObjectMapper om = new XmlMapper();
        try {
            Player[] loadPlayer =  om.readValue(new File(SCORES_FILE_XML), Player[].class);

            p1.name = loadPlayer[0].name;
            p1.score = loadPlayer[0].score;

            p2.name = loadPlayer[1].name;
            p2.score = loadPlayer[1].score;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadScores(Player p1, Player p2) {
        File file = new File(SCORES_FILE);
        if (!file.exists() || file.length() == 0) {
            ui.fileNotFound();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Player savedP1 = (Player) ois.readObject();
            Player savedP2 = (Player) ois.readObject();

            p1.score = savedP1.score;
            p2.score = savedP2.score;
        } catch (EOFException e) {
            ui.fileNotFound();
        } catch (IOException | ClassNotFoundException e) {
            ui.fileNotFound();
        }
    }
    public void saveGame(String filename, GameMap game) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(game);
        }
    }

    public GameMap loadGame(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameMap) ois.readObject();
        }
    }
}
