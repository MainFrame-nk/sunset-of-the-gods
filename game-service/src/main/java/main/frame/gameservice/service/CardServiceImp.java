package main.frame.gameservice.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import main.frame.gameservice.model.Card;
import main.frame.gameservice.model.Player;
import main.frame.gameservice.model.RestrictedCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CardServiceImp implements CardService {

    @PersistenceContext
    private EntityManager entityManager;

//    // Проверка, может ли персонаж использовать карту
//    public boolean canUseCard(Player player, RestrictedCard card) {
//        // Если у карты есть разрешенные классы, проверим, подходит ли класс персонажа
//        Set<String> allowedClasses = card.getAllowedClasses();
//        if (allowedClasses.isEmpty()) {
//            // Карта доступна для всех классов
//            return true;
//        }
//
//        // Проверим, есть ли у персонажа хотя бы один класс, который подходит для карты
//        for (String playerClass : player.getClasses()) {
//            if (allowedClasses.contains(playerClass)) {
//                return true; // Персонаж может использовать карту
//            }
//        }
//
//        // Если классы не совпадают, персонаж не может использовать карту
//        return false;
//    }

//    public Optional<Role> findById(int id) {
//        return Optional.ofNullable(entityManager.find(Role.class, id));
//    }
//
//    public Optional<Role> findByName(String name) {
//        return entityManager.createQuery("SELECT r FROM Card r WHERE r.name = :name", Role.class)
//                .setParameter("name", name)
//                .getResultStream()
//                .findFirst();
//    }
//
//    @Override
//    @Transactional
//    public List<Role> getAllRoles() {
//        return entityManager.createQuery("SELECT r FROM Card r", Role.class)
//                .getResultList();
//    }

//    public RoleDTO getRoleById(Long id) {
//        Role role = entityManager.find(Role.class, id); // Получаем сущность Role по ID
//        if (role == null) {
//            throw new RuntimeException("Role not found"); // Обработка случая, когда роль не найдена
//        }
//        return role.toRoleDTO(); // Преобразуем сущность в DTO
//    }

    // Преобразование RoleDTO в сущность Role для сохранения в базе данных
//    public Role convertToEntity(RoleDTO roleDTO) {
//        Role role = new Role();
//        role.setId(roleDTO.getId());
//        role.setName(roleDTO.getName());
//        return role;
//    }

//    // Пример метода для сохранения роли
//    public RoleDTO saveRole(RoleDTO roleDTO) {
//        Role role = convertToEntity(roleDTO);  // Преобразуем DTO в сущность
//        Role savedRole = roleRepository.save(role);  // Сохраняем роль в базе данных
//        return savedRole.toRoleDTO();  // Возвращаем сохранённую роль в виде DTO
//    }
}
