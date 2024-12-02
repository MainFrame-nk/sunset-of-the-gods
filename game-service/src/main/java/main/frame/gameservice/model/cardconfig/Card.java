package main.frame.gameservice.model.cardconfig;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;


public interface Card {
    String getName(); // Имя карты
    String getDescription(); // Описание карты
    String getType(); // Тип карты (например, оружие, броня, класс персонажа и т.д.)
    //String getRarity(); // Редкость карты (обычная, редкая и т.д.)
}
