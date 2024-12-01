package main.frame.gameservice.model.effects;

public enum EffectType {
    INCREASE_DAMAGE,      // Увеличение урона
    DECREASE_DAMAGE,      // Уменьшение урона
//    HEAL,                 // Восстановление здоровья
//    DAMAGE,               // Нанесение урона
//    INCREASE_ARMOR,       // Увеличение брони
//    DECREASE_ARMOR,       // Уменьшение брони
//    BOOST_HEALTH,         // Увеличение максимального здоровья
//    REDUCE_HEALTH,        // Уменьшение максимального здоровья
    GAIN_LEVEL,           // Получение уровня
    LOSE_LEVEL,           // Потеря уровня
//    ADD_ABILITY,          // Добавление навыка
//    REMOVE_ABILITY,       // Удаление навыка
    DOUBLE_REWARD,        // Удвоение награды
    SKIP_TURN,            // Пропуск хода
    IMMUNITY,             // Невосприимчивость к эффектам
    RESTRICTION,          // Наложение ограничений (например, запрет на использование карты)
//    SUMMON_COMPANION,     // Призыв напарника
    BUFF_COMPANION,       // Усиление напарника
    DEBUFF_COMPANION,     // Ослабление напарника
    DRAW_CARD,            // Вытянуть карту
    DISCARD_CARD,         // Сбросить карту
    GAIN_ITEM,            // Получение предмета
    LOSE_ITEM;            // Потеря предмета

//    INCREASE_DAMAGE {
//        @Override
//        public void applyEffect(Effect effect, Player player) {
//            player.setDamage(player.getDamage() + effect.getValue());
//        }
//    },
//    HEAL {
//        @Override
//        public void applyEffect(Effect effect, Player player) {
//            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + effect.getValue()));
//        }
//    };
//
//    public abstract void applyEffect(Effect effect, Player player);
}
