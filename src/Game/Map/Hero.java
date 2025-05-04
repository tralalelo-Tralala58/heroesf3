package Game.Map;

import Game.UI.UI;
import Game.Units.*;

import java.util.HashMap;
import java.util.Map;

public class Hero extends Assets {
    public int gold;
    public int heroX;
    public int heroY;
    public Map<String, Integer> army;
    public boolean isStable = false;

    public boolean isCafeUp = false;
    public boolean isSalonUp = false;
    public boolean isHotelUp = false;

    public boolean isSpeedUp = false;
    public boolean isDamageUp = false;

    public boolean isGamerHero = false;
    public boolean isAvailable = false;
    public UI ui = new UI();
    public int move = 10;


    public Hero(int gold){
        this.gold = gold;
        army = new HashMap<>();
        this.design = "И";
    }

    public void resetMoves(){
        if(isStable && isSpeedUp){
            this.move = 17;
        }else if(isStable){
            this.move = 15;
        } else if (isSpeedUp) {
            this.move = 12;
        }else{
            this.move = 10;
        }
        if(isCafeUp){
            this.move += 3;
        }
        ui.printResetMoves(this);
    }

    public void handleUnitPurchase(Building house, String choice, int gold) {
        switch (choice) {
            case "1":
                BuySpearMan(gold, house);
                break;
            case "2":
                BuyCrossBowMan(gold, house);
                break;
            case "3":
                BuySwordsMan(gold, house);
                break;
            case "4":
                BuyCavalryman(gold, house);
                break;
            case "5":
                BuyPaladin(gold, house);
                break;
            default:
                ui.invalidInput();
                break;
        }
    }

    public void BuySpearMan(int gold, Building house){
        if(house.guardPost) {
            SpearMan guy = new SpearMan(0, this.isDamageUp, this.isSalonUp, this.isHotelUp);

            int maxUnits = gold / guy.price;
            if (maxUnits > 0) {

                this.gold -= maxUnits * guy.price;
                String varior = "SpearMan";
                this.army.put(varior, this.army.getOrDefault(varior, 0) + maxUnits);

                ui.printBuyingUnits(maxUnits);
            }else{
                ui.needMoreMoney();
            }
        }
    }

    public void BuyCrossBowMan(int gold, Building house){
        if(house.towerOfCrossBowMen) {
            CrossBowMan guy = new CrossBowMan(0, this.isDamageUp, this.isSalonUp, this.isHotelUp);

            int maxUnits = gold / guy.price;
            if (maxUnits > 0) {

                this.gold -= maxUnits * guy.price;
                String varior = "CrossBowMan";
                this.army.put(varior, army.getOrDefault(varior, 0) + maxUnits);

                ui.printBuyingUnits(maxUnits);
            }else{
                ui.needMoreMoney();
            }
        }
    }

    public void BuySwordsMan(int gold, Building house){
        if(house.armory) {
            SwordsMan guy = new SwordsMan(0, this.isDamageUp, this.isSalonUp, this.isHotelUp);

            int maxUnits = gold / guy.price;
            if (maxUnits > 0) {

                this.gold -= maxUnits * guy.price;
                String varior = "SwordsMan";
                this.army.put(varior, army.getOrDefault(varior, 0) + maxUnits);

                ui.printBuyingUnits(maxUnits);
            }else{
                ui.needMoreMoney();
            }
        }
    }

    public void BuyCavalryman(int gold, Building house){
        if(house.arena){
            Cavalryman guy = new Cavalryman(0, this.isDamageUp, this.isSalonUp, this.isHotelUp);

            int maxUnits = gold / guy.price;
            if (maxUnits > 0) {

                this.gold -= maxUnits * guy.price;
                String varior = "Cavalryman";
                this.army.put(varior, army.getOrDefault(varior, 0) + maxUnits);

                ui.printBuyingUnits(maxUnits);
            }else{
                ui.needMoreMoney();
            }

        }
    }

    public void BuyPaladin(int gold, Building house){
        if(house.cathedral){
            Paladin guy = new Paladin(0, this.isDamageUp, this.isSalonUp, this.isHotelUp);

            int maxUnits = gold / guy.price;
            if (maxUnits > 0) {

                this.gold -= maxUnits * guy.price;
                String varior = "Paladin";
                this.army.put(varior, army.getOrDefault(varior, 0) + maxUnits);

                ui.printBuyingUnits(maxUnits);
            }else{
                ui.needMoreMoney();
            }
        }
    }
}
