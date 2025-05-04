package Game.Map;
import Game.Que.*;
import Game.tests.Test1;
import Game.UI.UI;

import java.util.*;
import java.io.*;

public class GameMap implements Serializable{
    public UI ui;
    public String saveName;
    public String mapType;
    public boolean compIsAlive = true;
    public boolean isGamerWinner = false;
    public boolean inBuilding = false;
    public boolean basedGame;
    public boolean isGAmeOver = false;
    private final int height;
    private final int weight;
    public boolean isBattleStarted = false;

    public Hero[] heroes;
    public Assets[][] world;

    public BonusHunt[] bonus = {new BonusHunt("gold"),
            new BonusHunt("speedUp"),
            new BonusHunt("damageUp")};

    public Cafe cafe = new Cafe();
    public Salon salon = new Salon();
    public Hotel hotel = new Hotel();

    List<String> list = Collections.synchronizedList(new ArrayList<>());
    List<String> names = new ArrayList<>();

    public void createHeroes(String mapType){
        heroes = new Hero[2];

        for (int i = 0; i < heroes.length; i++) {
            heroes[i] = new Hero(100);
        }

        heroes[0].isGamerHero = true;
        heroes[0].isAvailable = true;

        heroes[0].army.put("SpearMan", 1);
        heroes[0].army.put("CrossBowMan", 10);

        heroes[1].isGamerHero = false;
        heroes[1].isAvailable = true;

        heroes[1].army.put("SpearMan", 6);
        heroes[1].army.put("CrossBowMan", 3);

        if(mapType.equals(Test1.CROSS_MAP)){
            heroes[0].heroY = height/2;
            heroes[0].heroX = 1;
            heroes[1].heroY = height/2;
            heroes[1].heroX = weight-2;
        }else {
            heroes[0].heroY = 1;
            heroes[0].heroX = 1;
            heroes[1].heroY = height-2;
            heroes[1].heroX = weight-2;
        }

        world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
        world[heroes[1].heroY][heroes[1].heroX] = heroes[1];
    }

    public void createCrossMap() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.weight; j++) {
                if (i == height / 2 && j == 0) {
                    world[i][j] = new GamerHouse();
                } else if ((i == height / 2 && j == weight - 1 || (j == weight / 2 && (i == height - 1 || i == 0)))) {
                    world[i][j] = new ComputerHouse();
                } else if (i == height / 2 || j == weight / 2) {
                    world[i][j] = new PathWay();
                } else {
                    world[i][j] = new Field();
                }
            }
        }
    }

    public void createDiagonalMap() {
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.weight; j++) {
                if (i == 0 && j == 0) {
                    world[i][j] = new GamerHouse();
                } else if (i == 0 && j == weight-1 || i == height-1 && (j == 0 || j == weight-1)) {
                    world[i][j] = new ComputerHouse();
                } else if (i == j || j == height-i-1) {
                    world[i][j] = new PathWay();
                } else {
                    world[i][j] = new Field();
                }
            }
        }
    }

    public GameMap(int height, int weight, String name, String mapType, boolean basedGame, boolean moded) throws InterruptedException {
        this.height = height;
        this.weight = weight;
        this.saveName = name;
        this.mapType = mapType;
        this.basedGame = basedGame;
        this.ui = new UI();
        this.world = new Assets[this.height][this.weight];
        this.names.add("Костя");
        this.names.add("Настя");
        this.names.add("Ксюша");

        if(basedGame) {
            Random random = new Random();
            int letX;
            int letY;
            if(mapType.equals(Test1.CROSS_MAP)){
                createCrossMap();
            } else{
                createDiagonalMap();
            }

            createHeroes(mapType);

            //генерация препятствий
            if(moded){
                Scanner scanner = new Scanner(System.in);
                for (int i = 0; i < 5; ) {
                    ui.chooseXY();
                    letX = scanner.nextInt();
                    letY = scanner.nextInt();
                    if(checkOutOfBounds(letX, letY)) {
                        if (world[letY][letX] instanceof Field) {
                            world[letY][letX] = new Let();
                            i++;
                        }else {
                            ui.printLetIsWrong();
                        }
                    }else{
                        ui.printLetOutOfBounds();
                    }
                    updateDisplay();
                }
            }else{
                for (int i = 0; i < 10; ) {
                    letY = Math.abs(random.nextInt()) % this.height;
                    letX = Math.abs(random.nextInt()) % this.weight;
                    if (world[letY][letX] instanceof Field) {
                        world[letY][letX] = new Let();
                        i++;
                    }
                }
            }


            while (true){
                letY = Math.abs(random.nextInt()) % this.height;
                letX = Math.abs(random.nextInt()) % this.weight;
                if (world[letY][letX] instanceof Field) {
                    world[letY][letX] = cafe;
                    break;
                }
            }

            while (true){
                letY = Math.abs(random.nextInt()) % this.height;
                letX = Math.abs(random.nextInt()) % this.weight;
                if (world[letY][letX] instanceof Field) {
                    world[letY][letX] = salon;
                    break;
                }
            }

            while (true){
                letY = Math.abs(random.nextInt()) % this.height;
                letX = Math.abs(random.nextInt()) % this.weight;
                if (world[letY][letX] instanceof Field) {
                    world[letY][letX] = hotel;
                    break;
                }
            }

            //генерация подарков
            for (int i = 0; i < bonus.length; ) {
                letY = Math.abs(random.nextInt()) % this.height;
                letX = Math.abs(random.nextInt()) % this.weight;
                if (world[letY][letX] instanceof Field) {
                    world[letY][letX] = bonus[i++];
                }
            }
        }
    }

    public void updateDisplay() throws InterruptedException {
        ui.newMap(world, height, weight);
        endGame();
    }
    
    // Проверка границ карты
    public boolean checkOutOfBounds(int newX, int newY){
        return newX >= 0 && newX < this.weight && newY >= 0 && newY < this.height;
    }

    public void moveHero(Hero hero, int dx, int dy) throws InterruptedException {
        int newX = hero.heroX + dx;
        int newY = hero.heroY + dy;


        if (checkOutOfBounds(newX, newY)) {
            if (world[newY][newX] instanceof Building) {
                Building building = (Building) world[newY][newX];
                Scanner scanner = new Scanner(System.in);
                if (building.isGamerTower && hero.isGamerHero) {
                    inBuilding = true;
                    if (basedGame) {
                        while (inBuilding && hero.gold > 0) {
                            ui.printHeroGold(hero);
                            ui.printBuildings(building);

                            String choice = scanner.nextLine().toUpperCase();

                            if (choice.equals("Q")) {
                                inBuilding = false;
                            } else {
                                building.handleBuildingPurchase(hero, choice);
                            }

                            if (hero.gold > 0) {
                                ui.chooseInShop();

                                String buyUnitChoice = scanner.nextLine().toUpperCase();

                                if (buyUnitChoice.equals("Y")) {

                                    ui.printBuyingUnits();
                                    String unitChoice = scanner.nextLine().toUpperCase();

                                    ui.chooseGold();
                                    int gold = scanner.nextInt();
                                    scanner.nextLine(); // Очистка буфера после nextInt()
                                    if (gold > 0 && gold <= hero.gold) {
                                        hero.handleUnitPurchase(building, unitChoice, gold);
                                    } else {
                                        ui.needMoreMoney();
                                    }


                                } else {
                                    break;
                                }
                            }
                        }
                    } else {
                        ui.emulateShoping();
                    }
                } else {
                    isBattleStarted = true;
                    BattleField btf = new BattleField(hero, building.defence, this, ui);
                    btf.startBattle("Building");
                    isBattleStarted = false;
                    world = endBattle(hero, newY, newX);
                }
            }else if(world[newY][newX] instanceof Cafe) {
                if (cafe.isVisited) {
                    ui.coffeeWarning();
                }else if(hero.gold < 10){
                    ui.goOut(10);
                } else  {
                    cafe.isVisited = true;
                    hero.gold -= 10;

                    ui.waitQue();

                    list.clear();

                    AddQue addQue = new AddQue(list, names);
                    RemoveQue removeQue = new RemoveQue(list, this);

                    addQue.start();
                    removeQue.start();

                    addQue.join();
                    removeQue.join();

                    hero.isCafeUp = true;
                    updateDisplay();
                }
            } else if(world[newY][newX] instanceof Salon) {
                if (salon.isVisited) {
                    ui.salonWarning();
                } else if (hero.gold < 12) {
                    ui.goOut(12);
                } else {
                    salon.isVisited = true;
                    hero.gold -= 12;

                    ui.waitQue();

                    list.clear();

                    AddQue addQue = new AddQue(list, names);
                    RemoveQue removeQue = new RemoveQue(list, this);

                    addQue.start();
                    removeQue.start();

                    addQue.join();
                    removeQue.join();

                    hero.isSalonUp = true;
                    updateDisplay();
                }
            }else if(world[newY][newX] instanceof Hotel){
                if (hotel.isVisited) {
                    ui.hotelWarning();
                } else if (hero.gold < 15) {
                    ui.goOutFromHotel();
                } else {
                    hotel.isVisited = true;
                    hero.gold -= 15;
                    hero.isHotelUp = true;
                    ui.startSleeping();
                    SimpleQue simpleQue = new SimpleQue();

                    simpleQue.start();
                    simpleQue.join();

                    ui.isGoodNight();
                    Thread.sleep(1000);

                    hero.move = 0;
                }
            } else if (world[newY][newX] instanceof Road) {
                if ((world[newY][newX] instanceof PathWay && hero.move > 0) || (world[newY][newX] instanceof Field && hero.move > 1)) {
                    if (world[newY][newX] instanceof PathWay) {
                        hero.move -= new PathWay().path;

                        ui.printPath(new PathWay().path);

                    } else if (world[newY][newX] instanceof Field) {
                        hero.move -= new Field().path;

                        ui.printPath(new Field().path);
                    }

                    ui.movesRemaining(hero);

                    if(mapType.equals(Test1.CROSS_MAP)) {
                        if (hero.heroY == height / 2 || hero.heroX == weight / 2) {
                            world[hero.heroY][hero.heroX] = new PathWay();
                        } else {
                            world[hero.heroY][hero.heroX] = new Field();
                        }
                    }else{
                        if (hero.heroY == hero.heroX || hero.heroX == height-hero.heroY-1) {
                            world[hero.heroY][hero.heroX] = new PathWay();
                        } else {
                            world[hero.heroY][hero.heroX] = new Field();
                        }
                    }

                    hero.heroX = newX;
                    hero.heroY = newY;
                    world[hero.heroY][hero.heroX] = hero;

                    updateDisplay();
                } else {
                    ui.needMoreMoves();
                }
            }
            else if (world[newY][newX] instanceof Hero) {
                Hero otherHero = (Hero) world[newY][newX];
                hero.move = 0;
                otherHero.move = 0;
                isBattleStarted = true;
                BattleField btf = new BattleField(hero, otherHero, this, ui);
                btf.startBattle("Hero");
                isBattleStarted = false;
                if (hero.isGamerHero) {
                    world = endBattle(hero, otherHero);
                }else{
                    world = endBattle(otherHero, hero);
                }

            } else if (world[newY][newX] instanceof BonusHunt) {
                if(((BonusHunt)world[newY][newX]).choice.equals("gold")){
                    hero.gold += ((BonusHunt)world[newY][newX]).gold;
                    ui.printHeroGold(hero);
                }else if (((BonusHunt)world[newY][newX]).choice.equals("damageUp")){
                    hero.isDamageUp = true;
                    ui.printBonusDamageUp();
                }else {
                    ui.printBonusSpeedUp();
                    hero.isSpeedUp = true;
                }
                world[newY][newX] = new Field();
                updateDisplay();
            }else if(world[newY][newX] instanceof Let){
                ui.letIsHere();
            }
        } else {
            ui.letIsHere();
        }
    }

    public Assets[][] endBattle(Hero playerHero, int newY, int newX) throws InterruptedException{
        int reward = ((Building) world[newY][newX]).defence.gold;

        if (isGamerWinner) {
            // Победил атакующий
            if (playerHero.isGamerHero) {
                // Игрок захватил здание компьютера
                world[newY][newX] = new GamerHouse();
                playerHero.gold += reward;

                ui.printReward(reward);
            } else {
                // Компьютер проиграл при атаке здания игрока
                ui.defSuccess();
                world[playerHero.heroY][playerHero.heroX] = new PathWay();
                playerHero.isAvailable = false;
            }
        } else {
            // Победил защитник
            if (playerHero.isGamerHero) {
                // Игрок проиграл при атаке здания компьютера
                isGAmeOver = true;
                ui.printYouLose();
            } else {
                world[newY][newX] = new ComputerHouse();
                ui.enemyOnYourBase();
            }
        }

        // Восстанавливаем позицию героя
        if (playerHero.isGamerHero) {
            if (isGamerWinner || !isGAmeOver) {
                world[playerHero.heroY][playerHero.heroX] = playerHero;
            } else {
                world[playerHero.heroY][playerHero.heroX] = new PathWay();
            }
        } else {
            if (!isGamerWinner) {
                // Компьютерный герой победил и остается в захваченном здании
                world[newY][newX] = new ComputerHouse();
                compIsAlive = false;
            } else {
                // Компьютерный герой проиграл и исчезает
                world[playerHero.heroY][playerHero.heroX] = new PathWay();
                compIsAlive = false;
            }
        }

        updateDisplay();
        isBattleStarted = false;
        return world;
    }

    public Assets[][] endBattle(Hero playerHero, Hero computerHero) {
        if(isGamerWinner) {
            ui.printCompGold(computerHero);

            playerHero.gold += computerHero.gold;

            ui.printHeroGold(playerHero);

            computerHero.gold = 0;
            computerHero.isAvailable = false;

            this.world[computerHero.heroY][computerHero.heroX] = new PathWay();

            this.world[playerHero.heroY][playerHero.heroX] = playerHero;

            // Сбрасываем флаг начала боя
            isBattleStarted = false;
        }else{
            this.world[computerHero.heroY][computerHero.heroX] = computerHero;

            this.world[playerHero.heroY][playerHero.heroX] = new PathWay();
            isGAmeOver = true;
        }
        return this.world;
    }

    public void endRound(Hero[] heroes){
        for (int i = 0; i < heroes.length; i++) {
            if(heroes[i].isAvailable && !isBattleStarted){
                heroes[i].resetMoves();
            }
        }
    }

    public boolean isGamerWin(){
        if(mapType.equals(Test1.CROSS_MAP)) {
            return world[height / 2][0] instanceof GamerHouse && world[height / 2][weight - 1] instanceof GamerHouse
                    && world[height - 1][weight / 2] instanceof GamerHouse
                    && world[0][weight / 2] instanceof GamerHouse;
        }else{
            return world[0][0] instanceof GamerHouse && world[height-1][weight-1] instanceof GamerHouse
                    && world[height-1][0] instanceof GamerHouse
                    && world[0][weight-1] instanceof GamerHouse;
        }
    }

    public boolean isCompWin(){
        if(mapType.equals(Test1.CROSS_MAP)) {
            return world[height / 2][0] instanceof ComputerHouse && world[height / 2][weight - 1] instanceof ComputerHouse
                    && world[height - 1][weight / 2] instanceof ComputerHouse
                    && world[0][weight / 2] instanceof ComputerHouse;
        }else{
            return world[0][0] instanceof ComputerHouse && world[height-1][weight-1] instanceof ComputerHouse
                    && world[height-1][0] instanceof ComputerHouse
                    && world[0][weight-1] instanceof ComputerHouse;
        }
    }

    public void endGame(){
        if(isGamerWin()){
            isGAmeOver = true;
            ui.printYouWin();
            clearSaveFile();
        }
        if(isCompWin()){
            isGAmeOver = true;
            ui.printYouLose();
            clearSaveFile();
        }
    }

    public void clearSaveFile() {
        try {
            if (Objects.equals(saveName, "Игрок1")) {
                new FileOutputStream(Test1.PLAYER1_SAVE).close(); // Очистка файла
            }else if (Objects.equals(saveName, "Игрок2")){
                new FileOutputStream(Test1.PLAYER2_SAVE).close();
            }else{
                new FileOutputStream(Test1.TEST_SAVE).close();
            }
            ui.saveClear();
        } catch (IOException e) {
            ui.saveClearError();
        }
    }
}
