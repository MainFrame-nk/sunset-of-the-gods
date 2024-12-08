package main.frame.gameservice.model.effects;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.model.cardconfig.BaseCard;
import main.frame.gameservice.model.player.LobbyPlayerCards;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name = "effects")
public class Effect extends BaseCard {

//    @Column(name = "target_type", nullable = false)
//    private String targetType; // Цель эффекта (PLAYER, MONSTER, COMPANION и т.д.)
// Ссылка на игрока, к которому применяется эффект
    @ManyToOne
    @JoinColumn(name = "player_id") // Ссылка на таблицу LobbyPlayerCards
    private LobbyPlayerCards player;  // Это поле будет использоваться для связи с игроком


    @Enumerated(EnumType.STRING) // Хранение enum в виде строки в БД
    @Column(name = "effect_type", nullable = false)
    private EffectType effectType; // Тип эффекта (например, увеличение урона)

    @Enumerated(EnumType.STRING)
    @Column(name = "effect_nature", nullable = false)
    private EffectNature effectNature; // Природа эффекта (BUFF или DEBUFF)

    @Column(name = "value")
    private int value; // Величина эффекта (например, +10 к урону)

    @Column(name = "duration")
    private Integer duration; // Длительность эффекта (в ходах). Null, если эффект постоянный.

    @ManyToMany(mappedBy = "effects")
    private Set<BaseCard> cards = new HashSet<>(); // Карты, которые используют этот эффект

    @Override
    public String getType() {
        return "Effect"; // Специфический тип для тела
    }
}