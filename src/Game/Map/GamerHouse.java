package Game.Map;

public class GamerHouse extends Building {
    public GamerHouse(){
        defence = new Hero(100);
        defence.isGamerHero = true;
        defence.army.put("CrossBowMan", 4);
        this.isGamerTower = true;
        this.design = "\u001B[34m" + "▥" + "\u001B[0m";
    }
}
