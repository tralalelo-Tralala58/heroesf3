package Game.Map;

public class BonusHunt extends Assets {
    public int gold = 50;
    protected String choice;

    public BonusHunt(String choice){
        this.choice = choice;
        if (choice.equals("gold")){
            this.design = "\u001B[33m" + "$" + "\u001B[0m";
        }else if(choice.equals("damageUp")){
            this.design = "\u001B[31m" + "$" + "\u001B[0m";
        }else{
            this.design = "\u001B[34m" + "$" + "\u001B[0m";
        }
    }
}
