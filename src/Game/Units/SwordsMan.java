package Game.Units;

// Мечник – Юнит 3 уровня
public class SwordsMan extends Unit {
    public SwordsMan(int count, boolean isDamageUp, boolean isSalonUp, boolean isHotelUp){
        this.price = 10;
        this.count = count;
        if(isDamageUp){
            this.HP = 16; //   Высокий показатель ХП.
            this.damage = 8; // Средний показатель атаки.
        }else{
            this.HP = 15; //   Высокий показатель ХП.
            this.damage = 7; // Средний показатель атаки.
        }
        if(isSalonUp){
            this.HP += 1;
        }
        if(isHotelUp){
            this.damage += 1;
        }
        this.totalHP = this.HP * this.count;
        this.totalDamage = this.damage * this.count;
        this.distance = 1; // Низкий показатель дистанции атаки.
        this.move = 3; //  Средний показатель перемещения.
        this.design = "⚔";
    }
}
