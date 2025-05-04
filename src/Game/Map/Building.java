package Game.Map;

public class Building extends Assets {
    public Hero defence; // кто сидит в замке для защиты
    public boolean isGamerTower;
    public boolean tavern = false; // таверна - используется для найма Героев
    public boolean stable = false; // конюшня - увеличивает дальность перемещения для всех Героев
    public boolean guardPost = false; // Сторожевой пост – используется для найма Юнитов 1 уровня: копейщиков.
    public boolean towerOfCrossBowMen = false; // Башня арбалетчиков – используется для найма юнитов 2 уровня: арбалетчиков
    public boolean armory = false; // Оружейная – используется для найма юнитов 3 уровня: мечников
    public boolean arena = false; // Арена – используется для найма юнитов 4 уровня: кавалеристов
    public boolean cathedral = false; // Собор – используется для найма юнитов 5 уровня: паладинов


    public void handleBuildingPurchase(Hero hero, String choice) {
        switch (choice) {
            case "H":
                BuyTavern(hero);
                break;
            case "S":
                BuyStable(hero);
                break;
            case "G":
                BuyGuardPost(hero);
                break;
            case "T":
                BuyTowerOfCrossBowMan(hero);
                break;
            case "A":
                BuyArmory(hero);
                break;
            case "R":
                BuyArena(hero);
                break;
            case "C":
                BuyCathedral(hero);
                break;
            default:
                System.out.println("Неверный выбор.");
                break;
        }
    }

    public void BuyTavern(Hero hero){
        if(!tavern){
            if(hero.gold >= 15){
                hero.gold -= 15;
                this.tavern = true;
            }
        }
    }

    public void BuyStable(Hero hero){
        if(!stable){
            if(hero.gold >= 20){
                hero.gold -= 20;
                hero.isStable = true;
                this.stable = true;
            }
        }
    }

    public void BuyGuardPost(Hero hero){
        if(!guardPost){
            if(hero.gold >= 13){
                hero.gold -= 13;
                this.guardPost = true;
            }
        }
    }

    public void BuyTowerOfCrossBowMan(Hero hero){
        if(!towerOfCrossBowMen){
            if(hero.gold >= 16){
                hero.gold -= 16;
                this.towerOfCrossBowMen = true;
            }
        }
    }

    public void BuyArmory(Hero hero){
        if(!armory){
            if(hero.gold >= 20){
                hero.gold -= 20;
                this.armory = true;
            }
        }
    }

    public void BuyArena(Hero hero){
        if(!arena){
            if(hero.gold >= 25){
                hero.gold -= 25;
                this.arena = true;
            }
        }
    }

    public void BuyCathedral(Hero hero){
        if(!cathedral){
            if(hero.gold >= 30){
                hero.gold -= 30;
                this.cathedral = true;
            }
        }
    }
}
