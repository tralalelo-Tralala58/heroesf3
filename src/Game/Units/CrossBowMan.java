package Game.Units;

// Арбалетчик – Стрелковый юнит 2 уровня.
public class CrossBowMan extends Unit {
    public CrossBowMan(int count, boolean isDamageUp, boolean isSalonUp, boolean isHotelUp){
        this.price = 8;
        this.count = count;
        if(isDamageUp){
            this.HP = 6; //  Низкий показатель ХП.
            this.damage = 6; // Низкий показатель атаки.
        }else{
            this.HP = 5; //  Низкий показатель ХП.
            this.damage = 5; // Низкий показатель атаки.
        }
        if(isSalonUp){
            this.HP += 1;
        }
        if(isHotelUp){
            this.damage += 1;
        }
        this.totalHP = this.HP * this.count;
        this.totalDamage = this.damage * this.count;
        this.distance = 100; // Стреляет на всю карту.
        this.move = 1; //  Низкий показатель перемещения.
        this.design = "⦔";
    }
}
