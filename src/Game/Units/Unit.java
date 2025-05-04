package Game.Units;

import Game.Map.Assets;
import Game.Map.PathWay;
import Game.UI.UI;

import java.util.Scanner;

public class Unit extends Assets {
    UI ui = new UI();
    public int price;
    public int count;
    public int HP;
    public int damage;
    public int totalHP; // Суммарное здоровье группы
    public int totalDamage; // Суммарный урон группы
    public int distance;
    public int move;
    public int unitX;
    public int unitY;
    public boolean isMoved = false;
    public boolean isGamerUnit;

    public void takeDamage(int damage){
        int unitLost = damage / this.HP; // сколько юнитов точно умрут от этой атаки
        int remainDamage = damage % this.HP;// сколько хп останется у последнего юнита

        this.count = Math.max(0, this.count - unitLost);
        this.totalHP -= unitLost * this.HP;

        if (this.count > 0 && remainDamage > 0){
            this.totalHP -= remainDamage;
            if (this.totalHP <= 0) {
                this.count--; // Вся группа уничтожена
                this.totalHP = 0; // Здоровье группы равно 0
            }
        }

        update();
    }

    public void update(){
        this.totalDamage = this.count * this.damage;
        System.out.println();
    }


    public void act(Assets[][] battlefield, int x, int y, Scanner scanner, UI ui){
        while (true) {
            try {
                ui.printUnitStats(this, x, y);


                ui.chooseAct(findUnits(battlefield, ui));

                int choice = scanner.nextInt();
                scanner.nextLine(); // Очистка буфера

                switch (choice) {
                    case 1:
                        ui.unitMove();
                        String direction = scanner.nextLine().toUpperCase();
                        moveUnit(battlefield, direction, ui);
                        return; // Завершаем ход после успешного действия
                    case 2:
                        attackUnit(battlefield, scanner, ui);
                        return; // Завершаем ход после успешного действия
                    default:
                        ui.wrongOneOrTwo();
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                ui.wrongOneOrTwo();
                scanner.nextLine(); // Очистка буфера после некорректного ввода
            }
        }
    }

    private void moveUnit(Assets[][] battlefield, String direction, UI ui){
        if (this.isMoved) {
            return;
        }

        int newX = this.unitX;
        int newY = this.unitY;
        int stepsRemaining = this.move; // Оставшееся количество шагов

        while (stepsRemaining > 0) {
            int nextX = newX;
            int nextY = newY;

            // Определяем следующую клетку в зависимости от направления
            switch (direction.toUpperCase()) {
                case "W": // Вверх
                    nextY = Math.max(0, newY - 1);
                    break;
                case "A": // Влево
                    nextX = Math.max(0, newX - 1);
                    break;
                case "S": // Вниз
                    nextY = Math.min(battlefield.length - 1, newY + 1);
                    break;
                case "D": // Вправо
                    nextX = Math.min(battlefield[0].length - 1, newX + 1);
                    break;
                default:
                    ui.invalidInput();
                    return;
            }

            // Проверяем, можно ли переместиться на следующую клетку
            if (battlefield[nextY][nextX] instanceof PathWay) {
                // Освобождаем текущую клетку
                battlefield[newY][newX] = new PathWay();

                // Перемещаем юнита на следующую клетку
                newX = nextX;
                newY = nextY;
                battlefield[newY][newX] = this;

                stepsRemaining--; // Уменьшаем количество оставшихся шагов
            } else {
                // Если следующая клетка недоступна, прерываем перемещение
                break;
            }
        }

        // Обновляем координаты юнита
        this.unitX = newX;
        this.unitY = newY;
        this.isMoved = true;

        ui.printMoveUnit(newY, newX);
    }

    public boolean findUnits(Assets[][] battlefield, UI ui){
        boolean hasEnemies = false;
        for (int i = Math.max(0, this.unitY - this.distance); i <= Math.min(battlefield.length - 1, this.unitY + this.distance) ; i++) {
            for (int j = Math.max(0, this.unitX - this.distance); j <= Math.min(battlefield[0].length - 1, this.unitX + this.distance); j++) {
                if(battlefield[i][j] instanceof Unit && battlefield[i][j] != this){
                    Unit target = (Unit)battlefield[i][j];

                    if(!target.isGamerUnit) {
                        ui.printTarget(target, i, j);
                        hasEnemies = true;
                    }
                }
            }
        }
        return hasEnemies;
    }

    public void chooseTarget(int targetRow, int targetCol, Assets[][] battlefield){
        // Проверка, что цель находится в пределах досягаемости
        if (targetRow >= 0 && targetRow < battlefield.length && targetCol >= 0 && targetCol < battlefield[0].length) {
            if (Math.abs(this.unitY - targetRow) <= this.distance && Math.abs(this.unitX - targetCol) <= this.distance) {
                if (battlefield[targetRow][targetCol] instanceof Unit) {
                    Unit target = (Unit) battlefield[targetRow][targetCol];
                    if (!target.isGamerUnit) { // Проверяем, что цель не является юнитом игрока

                        ui.printAttackUnit(this, target);

                        target.takeDamage(this.totalDamage);

                        ui.printDamageLeft(target);

                        if (target.count <= 0) {
                            battlefield[targetRow][targetCol] = new PathWay();
                            ui.printUnitDie(target);
                        }
                    } else {
                        ui.printFriendlyFire();
                    }
                } else {
                    ui.targetIsNotUnit();
                }
            } else {
                ui.targetOutOfScope();
            }
        } else {
            ui.invalidInput();
        }
    }

    private void attackUnit(Assets[][] battlefield, Scanner scanner, UI ui){
        findUnits(battlefield, ui);
        ui.chooseForAttack();

        int targetRow = scanner.nextInt(); // Строка цели
        int targetCol = scanner.nextInt(); // Столбец цели
        scanner.nextLine(); // Очистка буфера

        chooseTarget(targetRow, targetCol, battlefield);
    }
}
