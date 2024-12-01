package main.frame.gameservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.frame.gameservice.model.Card;
import main.frame.gameservice.model.effects.Effect;
import main.frame.gameservice.model.Player;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImp {
    private final CardService cardService;

    public boolean tryUseCard(Player player, Card card) {
        if (player.canUseCard(card, cardService)) {
            // Логика для использования карты
            return true;
        } else {
            // Логика для ошибки, если карта не может быть использована
            return false;
        }
    }

    public void applyEffect(Effect effect, Player player) {
        switch (effect.getEffectType()) {
            case INCREASE_DAMAGE:
                player.setDamage(player.getDamage() + effect.getValue());
                break;
            case HEAL:
                player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + effect.getValue()));
                break;
            case GAIN_LEVEL:
                player.setLevel(player.getLevel() + 1);
                break;
            case IMMUNITY:
                player.addStatus("IMMUNE");
                break;
            default:
                throw new IllegalArgumentException("Unknown effect type: " + effect.getEffectType());
        }
    }
   // effect.getEffectType().applyEffect(effect, player);

}
