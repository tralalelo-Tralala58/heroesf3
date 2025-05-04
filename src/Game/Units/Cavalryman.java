package Game.Units;

//Кавалерист – Юнит 4 уровня.
public class Cavalryman extends Unit {
    public Cavalryman(int count, boolean isDamageUP, boolean isSalonUp, boolean isHotelUp){
        this.price = 15;
        this.count = count;
        if(isDamageUP){
            this.HP = 14; //   Средний показатель ХП.
            this.damage = 10; //  Высокий показатель атаки.
        }else{
            this.HP = 12; //   Средний показатель ХП.
            this.damage = 9; //  Высокий показатель атаки.
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
        this.move = 4; //  Высокий показатель перемещения.
        this.design = "%";
    }
}