package Game.Que;

import Game.Map.GameMap;
import Game.UI.UI;

import java.util.List;

public class RemoveQue extends Thread {
    private final List<String> list;
    private final GameMap gameMap;
    private UI ui = new UI();

    public RemoveQue(List<String> list, GameMap gameMap) {
        this.list = list;
        this.gameMap = gameMap;
    }

    @Override
    public void run() {
        while (!list.isEmpty()) {
            synchronized (list) {
                if (!list.isEmpty()) {
                    ui.printNPC(list.remove(0));
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        ui.endOfQue();
    }
}