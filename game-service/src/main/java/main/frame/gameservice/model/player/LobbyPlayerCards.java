package main.frame.gameservice.model.player;

import jakarta.persistence.*;
import lombok.*;
import main.frame.gameservice.dto.LobbyPlayerCardsDTO;
import main.frame.gameservice.model.cardconfig.BaseCard;
import main.frame.gameservice.model.characters.CharacterCard;
import main.frame.gameservice.model.characters.CompanionCard;
import main.frame.gameservice.model.effects.Ability;
import main.frame.gameservice.model.effects.Effect;
import main.frame.gameservice.model.equipment.BodyCard;
import main.frame.gameservice.model.equipment.HeadCard;
import main.frame.gameservice.model.equipment.LegCard;
import main.frame.gameservice.model.equipment.WeaponCard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "lobby_player_cards")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LobbyPlayerCards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lobby_id", nullable = false)
    private Long lobbyId; // ID лобби

    @Column(name = "player_id", nullable = false)
    private Long playerId; // ID игрока

    @Column(name = "level")
    private int level = 1;

    @Column(name = "classes_limit")
    private int classesLimit = 1; // По умолчанию лимит — 1 класс

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "player_character_cards",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "character_card_id"))
    private Set<CharacterCard> characterClasses = new HashSet<>(); // Текущие классы игрока

    @ManyToMany
    @JoinTable(
            name = "player_backpack",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private Set<BaseCard> backpack = new HashSet<>(); // Карты в рюкзаке игрока

    @Column(name = "damage")
    private int damage = 1;

    @Column(name = "companion_limit")
    private int companionLimit = 1; // Максимальное количество напарников, по умолчанию 1

    @ManyToMany
    @JoinTable(name = "player_companions",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "companion_id"))
    private Set<CompanionCard> companions = new HashSet<>(); // Текущие напарники игрока

    @Column(name = "weapons_limit")
    private int weaponsLimit = 2; // По умолчанию лимит — 2 оружия

    @ManyToMany
    @JoinTable(name = "player_weapons",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "weapon_id"))
    private Set<WeaponCard> weapons = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "head_slot_id", referencedColumnName = "id")
    private HeadCard headSlot;

    @OneToOne
    @JoinColumn(name = "body_slot_id", referencedColumnName = "id")
    private BodyCard bodySlot;

    @OneToOne
    @JoinColumn(name = "leg_slot_id", referencedColumnName = "id")
    private LegCard legSlot; // Слот для ног

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Effect> buffs = new ArrayList<>(); // Список положительных эффектов

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Effect> debuffs = new ArrayList<>(); // Список отрицательных эффектов

    @Column(name = "hand_limit", nullable = false)
    private int handLimit = 6;

    @ManyToMany
    @JoinTable(
            name = "player_hand",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private Set<BaseCard> hand = new HashSet<>(); // Карты в руке игрока

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "player_abilities",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "ability_id")
    )
    private Set<Ability> abilities = new HashSet<>();

//    public boolean addCharacterClass(CharacterCard characterCard) {
//        if (characterClasses.size() < classesLimit) {
//            characterClasses.add(characterCard);
//            return true; // Класс добавлен успешно
//        }
//        return false; // Лимит превышен
//    }
//
//    public boolean removeCharacterClass(CharacterCard characterCard) {
//        return characterClasses.remove(characterCard);
//    }

    // Методы для добавления и использования навыков, если это нужно в игре
//    public void addAbility(String ability) {
//        this.abilities.add(ability);
//    }
//
//    public void removeAbility(String ability) {
//        this.abilities.remove(ability);
//    }
//
//    public boolean hasAbility(String ability) {
//        return this.abilities.contains(ability);
//    }

    // Проверка, может ли персонаж использовать карту
//    public boolean canUseCard(Card card, CardService cardService) {
//        return cardService.canUseCard(this, card);
//    }
//
//    public String getAllRolesWithOutBrackets (Set<Role> roles){
//        return roles.stream().map(Role::getRole).map(x->x.substring(5)).collect(Collectors.joining(", "));
//    }

//    public UserDTO toUserDTO() {
//        return UserDTO.toUserDTO(this);
//    }
//
//    public static UserDTO toUserDTO(User user) {
//        return UserDTO.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .username(user.getUsername())
//                .phoneNumber(user.getPhoneNumber())
//                .active(user.isActive())
//                .dateOfCreated(user.getDateOfCreated())
//                .roles(user.getRoles().stream().map(Role::toRoleDto).collect(Collectors.toSet()))
//                .build();
//    }

    // Преобразование сущности User в UserDTO
    public LobbyPlayerCardsDTO toLobbyPlayerCardsDTO() {
        return new LobbyPlayerCardsDTO(
                this.id,
                this.lobbyId,
                this.playerId,
                this.level,
                this.characterClasses,
                this.classesLimit,
                this.backpack,
                this.damage,
                this.companionLimit,
                this.companions,
                this.weaponsLimit,
                this.weapons,
                this.buffs,
                this.debuffs,
                this.handLimit,
                this.hand,
                this.abilities,
                this.headSlot,
                this.bodySlot,
                this.legSlot
        );
    }
}
