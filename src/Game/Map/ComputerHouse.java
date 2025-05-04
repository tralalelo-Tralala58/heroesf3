package Game.Map;

public class ComputerHouse extends Building {
    public ComputerHouse(){
        defence = new Hero(100);
        defence.isGamerHero = false;
        defence.army.put("CrossBowMan", 4);
        this.isGamerTower = false;
        this.design = "\u001B[31m" + "▥" + "\u001B[0m";
    }
}
