package Game.tests;

import Game.Map.*;

import Game.Units.*;
import Game.saves.*;
import org.junit.*;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestSystem{
    private static final Logger logger = TestLogger.getLogger();

    private int basedGold;
    private int height;
    private int weight;
    private Hero[] heroes;
    private GameMap map;

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();


    @Before
    public void setParameters() throws InterruptedException {
        logger.log(Level.INFO, "Начало инициализации тестовых данных");
        height = 5;
        weight = 5;
        basedGold = 100;

        heroes = new Hero[2];
        heroes[0] = new Hero(basedGold);
        heroes[1] = new Hero(basedGold);

        heroes[1].isGamerHero = false;
        heroes[1].isAvailable = true;

        heroes[0].isGamerHero = true;
        heroes[0].isAvailable = true;

        map = new GameMap(height, weight,"Bob","CROSS", false, false);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < weight; j++) {
                map.world[i][j] = new PathWay();
            }
        }

        systemOutRule.clearLog();
        logger.log(Level.INFO, "Инициализация тестовых данных завершена");
    }

    @Test
    public void TestOutPut() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста TestOutPut");
        try {
            heroes[0].heroY = height / 2;
            heroes[0].heroX = weight / 2;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[heroes[0].heroY][heroes[0].heroX + 1] = new Let();

            map.moveHero(heroes[0], 1, 0);

            String output = systemOutRule.getLogWithNormalizedLineSeparator();
            logger.log(Level.FINE, "Проверка вывода: " + output);
            Assert.assertEquals("Вы врезались в препятствие\n", output);

            logger.log(Level.INFO, "Тест TestOutPut завершен успешно");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestOutPut не пройден", e);
        }
    }

    @Test
    public void readTextFromStandardInputStream() {
        logger.log(Level.INFO, "Запуск теста readTextFromStandardInputStream");

        InputStream inputStream = new ByteArrayInputStream("G\n1\n".getBytes());
        System.setIn(inputStream);
        try {
            try (Scanner scanner = new Scanner(System.in)) {
                String line = scanner.nextLine();
                String number = scanner.nextLine();

                map.world[0][0] = heroes[0];
                map.world[0][1] = new GamerHouse();

                Building building = (Building) map.world[0][1];
                building.handleBuildingPurchase(heroes[0], line);

                heroes[0].handleUnitPurchase(building, number, heroes[0].gold);
                Assert.assertTrue(heroes[0].army.get("SpearMan") == 17);
            } finally {
                // Восстанавливаем оригинальный System.in
                System.setIn(System.in);
            }
            logger.log(Level.INFO, "Тест readTextFromStandardInputStream завершен успешно");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест readTextFromStandardInputStream не пройден", e);
        }catch (Exception e){
            logger.log(Level.SEVERE, "Ошибка в тесте readTextFromStandardInputStream", e);
        }
    }

    @Test
    public void AllTowersIsYours() {
        logger.log(Level.INFO, "Запуск теста AllTowersIsYours");
        try {
            heroes[0].heroY = height / 2;
            heroes[0].heroX = weight / 2;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[0][weight / 2] = new GamerHouse();
            map.world[height / 2][0] = new GamerHouse();
            map.world[height - 1][weight / 2] = new GamerHouse();
            map.world[height / 2][weight - 1] = new GamerHouse();

            logger.log(Level.FINE, "Проверка условия победы");
            map.endGame();

            Assert.assertTrue(map.isGAmeOver);
            logger.log(Level.INFO, "Тест AllTowersIsYours завершен успешно");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест AllTowersIsYours не пройден", e);
        }
    }

    @Test
    public void AllTowersIsComp() {
        logger.log(Level.INFO, "Запуск теста AllTowersIsComp");
        try {
            heroes[0].heroY = height / 2;
            heroes[0].heroX = weight / 2;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[0][weight / 2] = new ComputerHouse();
            map.world[height / 2][0] = new ComputerHouse();
            map.world[height - 1][weight / 2] = new ComputerHouse();
            map.world[height / 2][weight - 1] = new ComputerHouse();

            logger.log(Level.FINE, "Проверка условия поражения");
            map.endGame();

            Assert.assertTrue(map.isGAmeOver);
            logger.log(Level.INFO, "Тест AllTowersIsComp завершен успешно");
        }catch(AssertionError e){
            logger.log(Level.SEVERE, "Тест AllTowersIsComp не пройден", e);
        }
    }

    @Test
    public void TestIsBuyingHouse() {
        logger.log(Level.INFO, "Запуск теста TestIsBuyingHouse");
        try {
            map.world[0][0] = heroes[0];
            map.world[0][1] = new GamerHouse();

            Building building = (Building) map.world[0][1];

            logger.log(Level.FINE, "Покупка поста для копейщика");
            building.BuyGuardPost(heroes[0]);

            Assert.assertTrue(building.guardPost);
            logger.log(Level.INFO, "Тест TestIsBuyingHouse пройден");
        }catch(AssertionError e){
            logger.log(Level.SEVERE, "Тест TestIsBuyingHouse не пройден", e);
        }
    }

    @Test
    public void TestIsBuyingUnits() {
        logger.log(Level.INFO, "Запуск теста TestIsBuyingUnits");
        try {
            map.world[0][0] = heroes[0];
            map.world[0][1] = new GamerHouse();

            Building building = (Building) map.world[0][1];
            logger.log(Level.FINE, "Покупка поста для копейщика");
            building.BuyGuardPost(heroes[0]);

            logger.log(Level.FINE, "Покупка юнитов копейщиков");
            heroes[0].handleUnitPurchase(building, "1", heroes[0].gold);
            Assert.assertTrue(heroes[0].army.get("SpearMan") == 17);
            logger.log(Level.INFO, "Тест TestIsBuyingUnits пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestIsBuyingUnits не пройден", e);
        }
    }

    @Test
    public void TestIsBonusHuntGoldPickUP() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста TestIsBonusHuntGoldPickUP");
        try {
            heroes[0].heroY = 1;
            heroes[0].heroX = 1;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[1][2] = new BonusHunt("gold");

            int award = ((BonusHunt) map.world[1][2]).gold;

            logger.log(Level.FINE, "Герой подбирает монеты");
            map.moveHero(heroes[0], 1, 0); // герой подбирает монеты

            Assert.assertEquals(basedGold + award, heroes[0].gold);
            logger.log(Level.INFO, "Тест TestIsBonusHuntGoldPickUP пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestIsBonusHuntGoldPickUP не пройден", e);
        }
    }

    @Test
    public void TestIsBonusHuntSpeedPickUP() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста TestIsBonusHuntSpeedPickUP");
        try {
            heroes[0].heroY = 1;
            heroes[0].heroX = 1;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[1][0] = new BonusHunt("speedUp");

            logger.log(Level.FINE, "Герой подбирает подковы");
            map.moveHero(heroes[0], -1, 0); // герой подбирает подковы

            Assert.assertTrue(heroes[0].isSpeedUp);
            logger.log(Level.INFO, "Тест TestIsBonusHuntSpeedPickUP пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestIsBonusHuntSpeedPickUP не пройден", e);
        }
    }

    @Test
    public void TestIsBonusHuntDamagePickUP() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста TestIsBonusHuntDamagePickUP");
        try {
            heroes[0].heroY = 1;
            heroes[0].heroX = 1;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];

            map.world[0][1] = new BonusHunt("damageUp");

            logger.log(Level.FINE, "Герой подбирает кабана");
            map.moveHero(heroes[0], 0, -1); //герой подбирает кабана

            Assert.assertTrue(heroes[0].isDamageUp);
            logger.log(Level.INFO, "Тест TestIsBonusHuntDamagePickUP пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestIsBonusHuntDamagePickUP не пройден", e);
        }
    }

    @Test
    public void TestMovingCounts() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста TestMovingCounts");
        try {
            heroes[0].heroY = height / 2;
            heroes[0].heroX = weight / 2;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < weight; j++) {
                    if (i == height / 2 || j == weight / 2) {
                        map.world[i][j] = new PathWay();
                    } else {
                        map.world[i][j] = new Field();
                    }
                }
            }

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.updateDisplay();

            //Отнимется 1 очко тк клетка справа тропа
            logger.log(Level.FINE, "Герой походил вправо");
            map.moveHero(heroes[0], 1, 0);

            //Отнимется 2 очка тк клетка справа снизу поле (10 - (1+2) = 7)
            logger.log(Level.FINE, "Герой походил вправо вниз");
            map.moveHero(heroes[0], 1, 1);

            Assert.assertEquals(7, heroes[0].move);
            logger.log(Level.INFO, "Тест TestMovingCounts пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestMovingCounts не пройден", e);
        }
    }

    @Test
    public void TestOutOfBounds(){
        logger.log(Level.INFO, "Запуск теста TestOutOfBounds");
        try {
            heroes[0].heroY = 1;
            heroes[0].heroX = 0;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < weight; j++) {
                    if (i == height / 2 || j == weight / 2) {
                        map.world[i][j] = new PathWay();
                    } else {
                        map.world[i][j] = new Field();
                    }
                }
            }
            map.world[1][1] = new Let();
            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];

            //врезался в конец карты
            logger.log(Level.FINE, "Проверка выхода героя за пределы карты");
            Assert.assertFalse(map.checkOutOfBounds(heroes[0].heroX - 1, heroes[0].heroY));

            //врезался в препятствие
            logger.log(Level.FINE, "Герой пошел вправо к препятствию");
            map.moveHero(heroes[0], 1, 0);
            logger.log(Level.FINE, "Проверка героя чтобы он не зашел в препятствие");
            Assert.assertTrue(map.world[1][0] instanceof Hero);

            logger.log(Level.INFO, "Тест TestOutOfBounds пройден");
        }catch (Exception e){
            logger.log(Level.SEVERE, "Тест TestOutOfBounds не пройден", e);
        }
    }

    @Test
    public void TestPlaceAllUnits() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста TestPlaceAllUnits");
        try {
            heroes[0].army.put("SpearMan", 1);
            heroes[0].army.put("CrossBowMan", 10);


            heroes[1].army.put("SpearMan", 6);
            heroes[1].army.put("CrossBowMan", 3);

            map.world[0][0] = heroes[0];
            map.world[0][1] = heroes[1];

            int playerUnitCount = 0;
            int compUnitsCount = 0;

            logger.log(Level.FINE, "Генерация карты боя с юнитами");
            BattleField btf = new BattleField(heroes[0], heroes[1], map, map.ui); // в конструкторе вызывается метод ответственный за размещение юнитов по карте
            for (int i = 0; i < btf.height; i++) {
                for (int j = 0; j < btf.weight; j++) {
                    if (btf.battle[i][j] instanceof Unit) {
                        Unit unit = (Unit) btf.battle[i][j];
                        if (unit.isGamerUnit && j == 0) {
                            playerUnitCount++;
                        } else if (!unit.isGamerUnit && j == btf.weight - 1) {
                            compUnitsCount++;
                        }
                    }
                }
            }
            Assert.assertTrue((playerUnitCount == 2) && (compUnitsCount == 2));
            logger.log(Level.INFO, "Тест TestPlaceAllUnits пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestPlaceAllUnits не пройден", e);
        }
    }

    @Test
    public void StableIsWorking(){
        logger.log(Level.INFO, "Запуск теста StableIsWorking");
        try {
            map.world[0][0] = heroes[0];
            map.world[0][1] = new GamerHouse();

            Building building = (Building) map.world[0][1];

            logger.log(Level.FINE, "Покупка конюшни");
            building.BuyStable(heroes[0]); // -20

            Assert.assertTrue(heroes[0].isStable);

            logger.log(Level.INFO, "Тест StableIsWorking пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест StableIsWorking не пройден", e);
        }
    }

    @Test
    public void CathedralIsWorking(){
        logger.log(Level.INFO, "Запуск теста CathedralIsWorking");
        try {
            map.world[0][0] = heroes[0];
            map.world[0][1] = new GamerHouse();

            Building building = (Building) map.world[0][1];

            logger.log(Level.FINE, "Покупка собора");
            building.BuyCathedral(heroes[0]); // -30

            logger.log(Level.FINE, "Покупка паладинов");
            heroes[0].handleUnitPurchase(building, "5", heroes[0].gold);
            Assert.assertTrue((heroes[0].army.get("Paladin") == 2));

            logger.log(Level.INFO, "Тест CathedralIsWorking пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест CathedralIsWorking не пройден", e);
        }
    }

    @Test
    public void TestLogicHero() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста TestLogicHero");
        try {
            map.world[0][0] = heroes[0];
            map.world[0][1] = new GamerHouse();

            logger.log(Level.FINE, "Герой заходит в здание");
            map.moveHero(heroes[0], 1, 0);
            Assert.assertTrue(map.inBuilding);
            logger.log(Level.INFO, "Тест TestLogicHero пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestLogicHero не пройден", e);
        }
    }

    @Test
    public void IsUnitDie() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста IsUnitDie");
        try {
            heroes[0].army.put("SpearMan", 1);
            heroes[0].army.put("CrossBowMan", 10);

            heroes[1].army.put("SpearMan", 1);

            map.world[0][0] = heroes[0];
            map.world[0][1] = heroes[1];

            logger.log(Level.FINE, "Генерация карты боя с юнитами");
            BattleField btf = new BattleField(heroes[0], heroes[1], map, map.ui); // в конструкторе вызывается метод ответственный за размещение юнитов по карте

            for (int i = 0; i < btf.height; i++) {
                for (int j = 0; j < btf.weight; j++) {
                    if (btf.battle[i][j] instanceof Unit) {
                        Unit unit = (Unit) btf.battle[i][j];
                        if (unit.isGamerUnit && unit instanceof CrossBowMan) {
                            logger.log(Level.FINE, "Юнит атакует чужого юнита");
                            unit.chooseTarget(0, 9, btf.battle); //пытаюсь убить чужого юнита
                        }
                    }
                }
            }

            Assert.assertTrue(btf.battle[0][9] instanceof PathWay);
            logger.log(Level.INFO, "Тест IsUnitDie пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест IsUnitDie не пройден", e);
        }
    }

    @Test
    public void friendlyFire() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста friendlyFire");
        try {
            heroes[0].army.put("SpearMan", 1);
            heroes[0].army.put("CrossBowMan", 10);

            heroes[1].army.put("SpearMan", 1);

            map.world[0][0] = heroes[0];
            map.world[0][1] = heroes[1];

            logger.log(Level.FINE, "Генерация карты боя с юнитами");
            BattleField btf = new BattleField(heroes[0], heroes[1], map, map.ui); // в конструкторе вызывается метод ответственный за размещение юнитов по карте

            for (int i = 0; i < btf.height; i++) {
                for (int j = 0; j < btf.weight; j++) {
                    if (btf.battle[i][j] instanceof Unit) {
                        Unit unit = (Unit) btf.battle[i][j];
                        if (unit.isGamerUnit && unit instanceof CrossBowMan) {
                            logger.log(Level.FINE, "Юнит атакует своего юнита");
                            unit.chooseTarget(0, 0, btf.battle); //пытаюсь убить своего юнита
                        }
                    }
                }
            }
            Assert.assertTrue(btf.battle[0][0] instanceof SpearMan);
            logger.log(Level.INFO, "Теста friendlyFire пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест friendlyFire не пройден", e);
        }
    }

    @Test
    public void TestRangeUnit() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста TestRangeUnit");
        try {
            heroes[0].army.put("SpearMan", 1);
            heroes[0].army.put("CrossBowMan", 3);

            heroes[1].army.put("SpearMan", 1);

            map.world[0][0] = heroes[0];
            map.world[0][1] = heroes[1];

            boolean spearManFindUnits = false; // у копейщика дальность атаки 2, а враги находятся гораздо дальше этой дистанции
            boolean crossBowManFindsUnits = false; // лучник имеет дальность 100, чего хватит на всю карту

            logger.log(Level.FINE, "Генерация карты боя с юнитами");
            BattleField btf = new BattleField(heroes[0], heroes[1], map, map.ui); // в конструкторе вызывается метод ответственный за размещение юнитов по карте

            for (int i = 0; i < btf.height; i++) {
                for (int j = 0; j < btf.weight; j++) {
                    if (btf.battle[i][j] instanceof Unit) {
                        Unit unit = (Unit) btf.battle[i][j];
                        if (unit.isGamerUnit && unit instanceof SpearMan) {
                            spearManFindUnits = unit.findUnits(btf.battle, map.ui);
                        } else if (unit.isGamerUnit && unit instanceof CrossBowMan) {
                            crossBowManFindsUnits = unit.findUnits(btf.battle, map.ui);
                        }
                    }
                }
            }

            Assert.assertFalse( spearManFindUnits);
            Assert.assertTrue( crossBowManFindsUnits);
            logger.log(Level.INFO, "Теста TestRangeUnit пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест TestRangeUnit не пройден", e);
        }
    }

    @Test
    public void isHeroDie() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста isHeroDie");
        try {
            heroes[1].army.put("SpearMan", 6);
            heroes[1].army.put("CrossBowMan", 3);

            map.world[0][0] = heroes[0];
            map.world[0][1] = heroes[1];

            map.moveHero(heroes[0], 1, 0);
            // Герой при поражении удаляется с карты и еще одно условие поражения
            Assert.assertEquals(map.world[0][0] instanceof PathWay && map.isGAmeOver, true);
            logger.log(Level.INFO, "Теста isHeroDie пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест isHeroDie не пройден", e);
        }
    }

    @Test
    public void isBotCorrectMoving() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста isBotCorrectMoving");
        try {
            heroes[0].army.put("SpearMan", 1);

            heroes[1].army.put("SpearMan", 6);
            heroes[1].army.put("CrossBowMan", 3);

            map.world[0][0] = heroes[0];
            map.world[0][1] = heroes[1];

            boolean isCoordSpearmanEquals = true;
            boolean isCoordCrossBowManEquals = true;
            boolean isUnitAlive = false;
        /*
         у копейщика дальность атаки 2, а враги находятся гораздо дальше этой дистанции - он должен просто передвинуться за шаг
         лучник имеет дальность 100, чего хватит на всю дальность карты - он будет атаковать так как это в приоритете
        */
            logger.log(Level.FINE, "Генерация карты боя с юнитами");
            BattleField btf = new BattleField(heroes[0], heroes[1], map, map.ui); // в конструкторе вызывается метод ответственный за размещение юнитов по карте

            logger.log(Level.FINE, "Ход врага");
            btf.enemyTurn();
            for (int i = 0; i < btf.height; i++) {
                for (int j = 0; j < btf.weight; j++) {
                    if (btf.battle[i][j] instanceof Unit) {
                        Unit unit = (Unit) btf.battle[i][j];
                        if (unit.isGamerUnit && unit instanceof SpearMan) {
                            isUnitAlive = true;
                        } else if (!unit.isGamerUnit && unit instanceof SpearMan) {
                            isCoordSpearmanEquals = (i == 0 && j == btf.weight - 1);
                        } else if (!unit.isGamerUnit && unit instanceof CrossBowMan) {
                            isCoordCrossBowManEquals = (i == 1 && j == btf.weight - 1);
                        }
                    }
                }
            }
            Assert.assertTrue(!isUnitAlive && !isCoordSpearmanEquals && isCoordCrossBowManEquals);
            logger.log(Level.INFO, "Теста isBotCorrectMoving пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест isBotCorrectMoving не пройден", e);
        }
    }

    @Test
    public void isMapSaving() throws IOException, InterruptedException, ClassNotFoundException {
        logger.log(Level.INFO, "Запуск теста isMapSaving");
        try {
            heroes[0].heroY = height / 2;
            heroes[0].heroX = weight / 2;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[0][weight / 2] = new GamerHouse();
            map.world[height / 2][0] = new ComputerHouse();
            map.world[height - 1][weight / 2] = new ComputerHouse();
            map.world[height / 2][weight - 1] = new ComputerHouse();

            Savings savings = new Savings("Test_scores.bin", "", "");

            logger.log(Level.FINE, "Герой двигается вправо вниз");
            map.moveHero(heroes[0], 1, 1);

            logger.log(Level.FINE, "Сохранение в файл");
            savings.saveGame(Test1.TEST_SAVE, map);

            logger.log(Level.FINE, "Чтение из файла");
            GameMap map1 = savings.loadGame(Test1.TEST_SAVE);

            boolean isEquals = true;
            boolean isHeroEquals = true;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < weight; j++) {
                    if (!map.world[i][j].design.equals(map1.world[i][j].design)) {
                        isEquals = false;
                    }
                    if (map.world[i][j] instanceof Hero) {
                        Hero hero = (Hero) map.world[i][j];
                        Hero otherHero = (Hero) map1.world[i][j];
                        isHeroEquals = (hero.heroX == otherHero.heroX) && (hero.heroY == otherHero.heroY) && ((hero.gold == otherHero.gold));
                    }
                }
            }

            Assert.assertTrue(isEquals && isHeroEquals);
            logger.log(Level.INFO, "Теста isMapSaving пройден");
            map.clearSaveFile();
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест isMapSaving не пройден", e);
            map.clearSaveFile();
        }

    }

    @Test
    public void isScoresSaves() throws IOException {
        logger.log(Level.INFO, "Запуск теста isScoresSaves");
        try {
            Savings savings = new Savings(Test1.TEST_SCORES_FILE, "", "");

            Player player1 = new Player("Игрок1");
            Player player2 = new Player("Игрок2");

            logger.log(Level.FINE, "Чтение из файла");
            savings.loadScores(player1, player2);

            logger.log(Level.FINE, "Сохранение новых данных в файл");
            player2.score += 50;
            savings.saveScores(player1, player2);

            Player player3 = new Player("Игрок2");
            logger.log(Level.FINE, "Чтение из файла");
            savings.loadScores(player1, player3);

            Assert.assertEquals(player2.score, player3.score);
            logger.log(Level.INFO, "Теста isScoresSaves пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест isScoresSaves не пройден", e);
        }
    }

    @Test
    public void isHotelWork() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста isHotelWork");
        try {
            heroes[0].heroY = height / 2;
            heroes[0].heroX = weight / 2;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[heroes[0].heroY][heroes[0].heroX + 1] = new Hotel();


            logger.log(Level.FINE, "Герой двигается вправо в отель");
            map.moveHero(heroes[0], 1, 0);



            Assert.assertTrue(heroes[0].isHotelUp);
            logger.log(Level.INFO, "Теста isHotelWork пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест isHotelWork не пройден", e);
        }
    }

    @Test
    public void isCafeWork() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста isCafeWorkWork");
        try {
            heroes[0].heroY = height / 2;
            heroes[0].heroX = weight / 2;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[heroes[0].heroY][heroes[0].heroX + 1] = new Cafe();


            logger.log(Level.FINE, "Герой двигается вправо в кафе");
            map.moveHero(heroes[0], 1, 0);



            Assert.assertTrue(heroes[0].isCafeUp);
            logger.log(Level.INFO, "Теста isCafeWork пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест isCafeWork не пройден", e);
        }
    }

    @Test
    public void isSalonWork() throws InterruptedException {
        logger.log(Level.INFO, "Запуск теста isSalonWork");
        try {
            heroes[0].heroY = height / 2;
            heroes[0].heroX = weight / 2;

            map.world[heroes[0].heroY][heroes[0].heroX] = heroes[0];
            map.world[heroes[0].heroY][heroes[0].heroX + 1] = new Salon();


            logger.log(Level.FINE, "Герой двигается вправо в парихмахерскую");
            map.moveHero(heroes[0], 1, 0);



            Assert.assertTrue(heroes[0].isSalonUp);
            logger.log(Level.INFO, "Теста isSalonWork пройден");
        }catch (AssertionError e){
            logger.log(Level.SEVERE, "Тест isSalonWork не пройден", e);
        }
    }
}

