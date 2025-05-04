package Game.Units;

//Паладин - Юнит 5 уровня.
public class Paladin extends Unit {
    public Paladin(int count, boolean isDamageUp, boolean isSalonUp, boolean isHotelUp){
        this.price = 25;
        this.count = count;
        if(isDamageUp){
            this.HP = 20; //   Высокий показатель ХП.
            this.damage = 12; //  Высокий показатель атаки.
        }else{
            this.HP = 18; //   Высокий показатель ХП.
            this.damage = 10; //  Высокий показатель атаки.
        }
        if(isSalonUp){
            this.HP += 1;
        }
        if(isHotelUp){
            this.damage += 1;
        }
        this.totalHP = this.HP * this.count;
        this.totalDamage = this.damage * this.count;
        this.distance = 2; // Средний показатель дистанции атаки.
        this.move = 5; //  Высокий показатель перемещения.
        this.design = "P";
    }
}
