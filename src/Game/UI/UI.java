package Game.UI;

import Game.Map.*;
import Game.Units.Unit;
import Game.saves.Player;

import java.io.Serializable;

public class UI implements Serializable {
    public void clearConsole() throws InterruptedException {
        Thread.sleep(500);
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    public void newMap(Assets[][] world, int height, int weight) throws InterruptedException {
        clearConsole();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < weight; j++) {
                System.out.print(world[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void printYouWin(){
        System.out.println("Вы победили!");
    }

    public void printYouLose(){
        System.out.println("Вы проиграли");
    }

    public void printHeroGold(Hero hero){
        System.out.println("Ваше золото: " + hero.gold);
    }

    public void printCompGold(Hero hero){
        System.out.println("Вы получили " + hero.gold + " золота от врага!");
    }

    public void printReward(int reward){
        System.out.println("Вы захватили здание и получили " + reward + " золота!");
    }

    public void chooseInShop(){
        System.out.println("Хотите купить юнитов? (Y/N)");
    }

    public void choosePlayer(){
        System.out.println("Выберите игрока (1 или 2):");
    }

    public void chooseMap(){
        System.out.println("Выберите тип карты:");
        System.out.println("1. Крестообразная");
        System.out.println("2. Диагональная");
    }

    public void saveSuccess(){
        System.out.println("Игра успешно загружена!");
    }

    public void saveClear(){
        System.out.println("Сохранение очищено");
    }

    public void saveClearError(){
        System.err.println("Ошибка очистки файла сохранения");
    }

    public void invalidInput(){
        System.out.println("Invalid input.");
    }

    public void yourStep(){
        System.out.print("Ваш ход: ");
    }
    public void compStep(){
        System.out.println("Ход компьютера...");
    }

    public void printScores(Player player1, Player player2){
        System.out.println("Игра окончена! Текущие очки:");
        System.out.println(player1.name + ": " + player1.score);
        System.out.println(player2.name + ": " + player2.score);
    }

    public void saveGame(){
        System.out.println("Игра сохранена!");
    }

    public void saveError(){
        System.out.println("Ошибка загрузки, начинаем новую игру");
    }

    public void chooseLoad(){
        System.out.println("1. Новая игра");
        System.out.println("2. Загрузить игру");
        System.out.print("Выберите вариант: ");
    }

    public void chooseGenerateMap(){
        System.out.println("Выберите хотите ли сами сгенерировать препятствия или рандом(mod/not)");
    }

    public void printExit(){
        System.out.println("Exiting game...");
    }

    public void chooseMove(){
        System.out.println("WASD - двигаться. Q - выйти. Save - сохранить");
    }

    public void chooseGold(){
        System.out.println("Введите количество золота для покупки:");
    }

    public void printBuyingUnits(){
        System.out.println("Выберите юнита для покупки (1 - Копейщик, 2 - Арбалетчик, 3 - Мечник, 4 - Кавалерист, 5 - Паладин):");
    }

    public void printUnitStats(Unit unit, int x, int y){
        System.out.println("\nХод юнита: " + unit.design + " в позиции (" + x + ", " + y + ")");
        System.out.println("Здоровье одного юнита - " + unit.HP);
        System.out.println("Урон одного юнита - " + unit.damage);
        System.out.println("Максимальное здоровье юнита " + unit.totalHP);
        System.out.println("Максимальная дистанция атаки юнита - " + unit.distance);
        System.out.println("Максимальная дистанция перемещения - " + unit.move);
        System.out.println("Максимальный урон юнита - " + unit.totalDamage);
    }

    public void printUnitDie(Unit target){
        System.out.println("Юнит " + target.design + " погиб");
    }

    public void printBonusDamageUp(){
        System.out.println("Это кабан! Теперь твои юниты полны сил");
    }

    public void printBonusSpeedUp(){
        System.out.println("Это подковы для лошади");
    }

    public void chooseForAttack(){
        System.out.println("Выберите цель для атаки:");
        System.out.print("Введите координаты цели (x, y): ");
    }

    public void printTarget(Unit target, int x, int y){
        System.out.println("Цель: (" + x + ", " + y + ") - " + target.design);
    }

    public void spawnUnit(String unit, int x, int y){
        System.out.println("Добавлен юнит: " + unit + " в позицию (" + y + ", " + x + ")");
    }

    public void unitMove(){
        System.out.print("Введите направление (WASD): ");
    }

    public void endFight(){
        System.out.println("Бой завершен!");
    }

    public void countUnits(boolean heroUnits, boolean compUnits){
        System.out.println("Игрок имеет юнитов: " + heroUnits);
        System.out.println("Компьютер имеет юнитов: " + compUnits);
    }

    public void fileNotFound(){
        System.out.println("Файл сохранения очков не найден или пуст. Используются значения по умолчанию.");
    }

    public void letIsHere(){
        System.out.println("Вы врезались в препятствие");
    }


    public void printBuildings(Building building){
        System.out.println("Доступные здания:");
        if (!building.tavern) {
            System.out.println("Купить таверну за 15 золота? (H)");
        } else {
            System.out.println("Таверна уже куплена.");
        }
        if (!building.stable) {
            System.out.println("Купить конюшню за 20 золота? (S)");
        } else {
            System.out.println("Конюшня уже куплена.");
        }
        if (!building.guardPost) {
            System.out.println("Купить сторожевой пост за 13 золота? (G)");
        } else {
            System.out.println("Сторожевой пост уже куплен.");
        }
        if (!building.towerOfCrossBowMen) {
            System.out.println("Купить башню арбалетчиков за 16 золота? (T)");
        } else {
            System.out.println("Башня арбалетчиков уже куплена.");
        }
        if (!building.armory) {
            System.out.println("Купить оружейную за 20 золота? (A)");
        } else {
            System.out.println("Оружейная уже куплена.");
        }
        if (!building.arena) {
            System.out.println("Купить арену за 25 золота? (R)");
        } else {
            System.out.println("Арена уже куплена.");
        }
        if (!building.cathedral) {
            System.out.println("Купить собор за 30 золота? (C)");
        } else {
            System.out.println("Собор уже куплен.");
        }
        System.out.println("Введите 'Q' для выхода");
    }

    public void printPath(int x){
        System.out.println("Поле отняло " + x + " очко");
    }

    public void movesRemaining(Hero hero){
        System.out.println("У героя осталось " + hero.move +" ходов");
    }

    public void needMoreMoves(){
        System.out.println("Недостаточно очков перемещения");
    }

    public void needMoreMoney(){
        System.out.println("Недостаточно золота для покупки юнитов.");
    }

    public void printBuyingUnits(int maxUnits){
        System.out.println("Куплено " + maxUnits + " юнитов.");
    }

    public void printResetMoves(Hero hero){
        System.out.println("Восстановили "+ hero.move +" ходов");
    }

    public void printMoveUnit(int newY, int newX){
        System.out.println("Юнит переместился на (" + newY + ", " + newX + ")");
    }

    public void printAttackUnit(Unit unit,Unit target){
        System.out.println("У " + target.design + " здоровья: " + target.totalHP);
        System.out.println(unit.design + " атакует " + target.design + "!");
        System.out.println("Нанесено урона: " + unit.totalDamage);
    }

    public void printDamageLeft(Unit target){
        System.out.println("У " + target.design + " осталось здоровья: " + target.totalHP);
    }

    public void printFriendlyFire(){
        System.out.println("Нельзя атаковать своих юнитов.");
    }

    public void wrongOneOrTwo(){
        System.out.println("Неверный выбор. Введите 1 или 2.");
    }

    public void chooseXY(){
        System.out.print("Введите координаты цели (x, y): ");
    }

    public void printLetIsWrong(){
        System.out.println("Препятствие наехало на важный участок");
    }

    public void printLetOutOfBounds(){
        System.out.println("Препятствие за границей мира");
    }

    public void emulateShoping(){
        System.out.println("Закупка зданий и юнитов");
    }

    public void enemyOnYourBase(){
        System.out.println("Враг захватил ваше здание!");
    }

    public void chooseAct(boolean target){
        if(target){
            System.out.println("1. Двигаться");
            System.out.println("2. Атаковать");
        }else{
            System.out.println("1. Двигаться");
        }
        System.out.print("Выберите действие: ");
    }

    public void targetIsNotUnit(){
        System.out.println("Цель не является юнитом.");
    }

    public void targetOutOfScope(){
        System.out.println("Цель вне зоны досягаемости.");
    }

    public void defSuccess(){
        System.out.println("Ваше здание отбило атаку!");
    }

    public void enemyAttackYourUnit(Unit unit){
        System.out.println("Враг атаковал вашего юнита " + unit.design);
    }

    public void coffeeWarning(){
        System.out.println("Хватит пить кофе, сердечный приступ не за горами");
    }

    public void goOut(int moreMoney){
        System.out.println("Проваливай пока не найдешь "+ moreMoney +" золотых");
    }

    public void salonWarning(){
        System.out.println("У тебя лысая бошка, стричь нечего");
    }

    public void waitQue(){
        System.out.println("Погоди, надо отстоять свою очередь");
    }

    public void hotelWarning(){
        System.out.println("Ты сюда играть пришел или спать?");
    }

    public void goOutFromHotel(){
        System.out.println("Сходи поспи на улице пока не найдешь 15 монет");
    }

    public void startSleeping(){
        System.out.println("Самое время поспать");
    }

    public void isGoodNight(){
        System.out.println("Хорошо поспал?");
    }

    public void printNPC(String number){
        System.out.println("Вышел нпс - " + number);
    }

    public void endOfQue(){
        System.out.println("Все люди обслужены");
    }
}
