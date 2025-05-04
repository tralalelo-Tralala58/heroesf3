package Game.Map;

import Game.UI.UI;
import Game.Units.*;

import java.util.Map;
import java.util.Scanner;

public class BattleField {
    public final int height = 5;
    public final int weight = 10;
    public GameMap gameMap;
    public UI ui;

    public Assets[][] battle = new Assets[height][weight];

    public BattleField(Hero hero1, Hero hero2, GameMap gameMap, UI ui) throws InterruptedException {
        this.ui = ui;
        this.gameMap = gameMap;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < weight; j++) {
                battle[i][j] = new PathWay();
            }
        }

        if (hero1.isGamerHero) {
            placeUnits(hero1, 0);
            placeUnits(hero2, weight - 1);
        }else{
            placeUnits(hero2, 0);
            placeUnits(hero1, weight - 1);
        }

        updateDisplay();
    }

    private void placeUnits(Hero hero, int startX){
        int i = 0;
        for (Map.Entry<String, Integer> entry : hero.army.entrySet()) {
            String unit = entry.getKey(); // получаю воина игрока
            int count = entry.getValue(); // получаю количество воинов

            if (count > 0) {
                Unit newUnit = createUnit(unit, count, hero);
                newUnit.unitX = startX; // Устанавливаем координату X
                newUnit.unitY = i;     // Устанавливаем координату Y
                newUnit.isGamerUnit = hero.isGamerHero; // Помечаем юниты игрока
                battle[i][startX] = newUnit;
                ui.spawnUnit(unit, startX, i);
                i++;
            }
        }
    }

    public void startBattle(String isComp) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        while(true){
            playerTurn(scanner);
            updateDisplay();

            if(isBattleOver(isComp)){
                ui.endFight();
                break;
            }

            enemyTurn();
            updateDisplay();

            if(isBattleOver(isComp)){
                ui.endFight();
                break;
            }
        }
    }

    private void playerTurn(Scanner scanner) throws InterruptedException {
        ui.yourStep();
        resetMovedStatus();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < weight; j++) {
                if(battle[i][j] instanceof Unit && ((Unit)battle[i][j]).isGamerUnit){
                    Unit unit = (Unit) battle[i][j];
                    if (unit.count > 0 && !unit.isMoved) { // Проверяем, что юнит еще жив
                        unit.act(battle, i, j, scanner, ui);
                        updateDisplay();
                    }
                }
            }
        }
    }

    public void enemyTurn() throws InterruptedException {
        resetMovedStatus();
        ui.compStep();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < weight; j++) {
                if(battle[i][j] instanceof Unit && !((Unit)battle[i][j]).isGamerUnit){
                    Unit unit = (Unit)battle[i][j];
                    ui.printUnitStats(unit, i, j);
                    enemyAct(unit);
                }
            }
        }
        updateDisplay();
    }

    private void resetMovedStatus() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < weight; j++) {
                if (battle[i][j] instanceof Unit) {
                    Unit unit = (Unit) battle[i][j];
                    unit.isMoved = false; // Сбрасываем статус перемещения
                }
            }
        }
    }

    private void enemyAct(Unit unit){
        if (unit.isMoved) {
            return; // Если юнит уже двигался или атаковал, пропускаем его ход
        }

        Unit target = findPlayerUnitInRange(unit);

        if(target != null){
            target.takeDamage(unit.totalDamage);
            ui.printDamageLeft(target);
            if (target.totalHP <= 0){
                ui.printUnitDie(target);
                battle[target.unitY][target.unitX] = new PathWay();
            }
            unit.isMoved = true;
        }else{
            moveUnitTowardsPlayer(unit);
        }
    }

    // Поиск ближайшего юнита игрока в радиусе атаки
    private Unit findPlayerUnitInRange(Unit unit){
        for (int i = Math.max(0, unit.unitY - unit.distance); i <= Math.min(height - 1, unit.unitY + unit.distance) ; i++) {
            for (int j = Math.max(0, unit.unitX - unit.distance); j <= Math.min(weight - 1, unit.unitX + unit.distance); j++) {
                if(battle[i][j] instanceof Unit && ((Unit)battle[i][j]).isGamerUnit){
                    ui.enemyAttackYourUnit((Unit) battle[i][j]);
                    return (Unit)battle[i][j];
                }
            }
        }
        return null;
    }

    private void moveUnitTowardsPlayer(Unit unit){
        int newX = unit.unitX;
        int newY = unit.unitY;
        int stepsRemaining = unit.move; // Оставшееся количество шагов

        while (stepsRemaining > 0) {
            int nextX = newX - 1; // Двигаемся влево (к игроку)
            int nextY = newY;

            // Проверяем, можно ли переместиться на следующую клетку
            if (nextX >= 0 && battle[nextY][nextX] instanceof PathWay) {
                // Освобождаем текущую клетку
                battle[newY][newX] = new PathWay();

                // Перемещаем юнита на следующую клетку
                newX = nextX;
                newY = nextY;
                battle[newY][newX] = unit;

                stepsRemaining--; // Уменьшаем количество оставшихся шагов
            } else {
                // Если следующая клетка недоступна, прерываем перемещение
                break;
            }
        }

        // Обновляем координаты юнита
        unit.unitX = newX;
        unit.unitY = newY;
        unit.isMoved = true; // Помечаем, что юнит двигался

        ui.printMoveUnit(newY, newX);
    }

    private boolean isBattleOver(String isHero){
        boolean playerHasUnits = false;
        boolean compHasUnits = false;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < weight; j++) {
                if (battle[i][j] instanceof Unit) {
                    Unit unit = (Unit) battle[i][j];
                    if (unit.count > 0) { // Проверяем, что количество юнитов больше нуля
                        if (unit.isGamerUnit) {
                            playerHasUnits = true;
                        } else {
                            compHasUnits = true;
                        }
                    }
                }
            }
        }

        ui.countUnits(playerHasUnits, compHasUnits);

        if(!playerHasUnits){
            ui.printYouLose();
            gameMap.isGamerWinner = false;
        }
        if(!compHasUnits){
            gameMap.isGamerWinner = true;
            if(isHero.equals("Hero")) {
                gameMap.compIsAlive = false;
            }
        }

        return !playerHasUnits || !compHasUnits;
    }

    private void clearConsole() throws InterruptedException {
        Thread.sleep(500);
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    private void updateDisplay() throws InterruptedException {
        clearConsole();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < weight; j++) {
                System.out.print(battle[i][j] + " ");
            }
            System.out.println();
        }
    }

    private Unit createUnit(String unit, int count, Hero hero){
        switch (unit) {
            case "SpearMan":
                return new SpearMan(count, hero.isDamageUp, hero.isSalonUp, hero.isHotelUp);
            case "CrossBowMan":
                return new CrossBowMan(count, hero.isDamageUp, hero.isSalonUp, hero.isHotelUp);
            case "SwordsMan":
                return new SwordsMan(count, hero.isDamageUp, hero.isSalonUp, hero.isHotelUp);
            case "Cavalryman":
                return new Cavalryman(count, hero.isDamageUp, hero.isSalonUp, hero.isHotelUp);
            case "Paladin":
                return new Paladin(count, hero.isDamageUp, hero.isSalonUp, hero.isHotelUp);
            default:
                throw new IllegalArgumentException("Unknown unit type");
        }
    }
}
