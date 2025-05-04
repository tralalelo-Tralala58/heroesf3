package Game.Units;

// Копейщик – Юнит 1 уровня.
public class SpearMan extends Unit {
    public SpearMan(int count, boolean isDamageUp, boolean isSalonUp, boolean isHotelUp){
        this.price = 5;
        this.count = count;
        if(isDamageUp){
            this.HP = 8; //  Средний показатель ХП.
            this.damage = 4; // Низкий показатель атаки.
        }else{
            this.HP = 7; //  Средний показатель ХП.
            this.damage = 3; // Низкий показатель атаки.
        }
        if(isSalonUp){
            this.HP += 1;
        }
        if(isHotelUp){
            this.damage += 1;
        }
        this.totalHP = this.HP * this.count;
        this.totalDamage = this.damage * this.count;
        this.distance = 1; //  Низкий показатель дистанции атаки.
        this.move = 2; //  Низкий показатель перемещения.
        this.design = "➝";
    }
}