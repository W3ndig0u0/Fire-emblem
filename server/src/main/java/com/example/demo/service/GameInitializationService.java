package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.Character;
import com.example.demo.model.Character.Allegiance;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameInitializationService {
    private final CharacterRepository characterRepository;
    private final GameSessionRepository sessionRepository;

    @Transactional
    public Long createNewGame() {
        GameSession session = new GameSession();
        sessionRepository.save(session);

        Character marth = new Character("Marth", UnitClass.MYRMIDON, Allegiance.PLAYER, 1);
        marth.setGameSession(session);
        marth.setPosX(0); marth.setPosY(0);
        marth.addToInventory(new Weapon("Iron Sword", 1, Rarity.COMMON, 5, 40, WeaponType.SWORD));
        characterRepository.save(marth);

        Character sain = new Character("Sain", UnitClass.CAVALIER, Allegiance.PLAYER, 1);
        sain.setGameSession(session);
        sain.setPosX(0); sain.setPosY(1);
        sain.addToInventory(new Weapon("Iron Lance", 1, Rarity.COMMON, 7, 35, WeaponType.LANCE));
        characterRepository.save(sain);

        Character lena = new Character("Lena", UnitClass.CLERIC, Allegiance.PLAYER, 1);
        lena.setGameSession(session);
        lena.setPosX(1); lena.setPosY(0);
        lena.addToInventory(new Weapon("Heal", 1, Rarity.COMMON, 10, 30, WeaponType.HEAL_STAFF));
        characterRepository.save(lena);

        Character bandit = new Character("Bandit", UnitClass.FIGHTER, Allegiance.ENEMY, 2);
        bandit.setGameSession(session);
        bandit.setPosX(4); bandit.setPosY(4);
        bandit.addToInventory(new Weapon("Iron Axe", 1, Rarity.COMMON, 8, 30, WeaponType.AXE));
        characterRepository.save(bandit);

        Character mage = new Character("Dark Mage", UnitClass.MAGE, Allegiance.ENEMY, 2);
        mage.setGameSession(session);
        mage.setPosX(7); bandit.setPosY(8);
        mage.addToInventory(new Weapon("Fire Tome", 1, Rarity.COMMON, 8, 30, WeaponType.FIRE_TOME));
        characterRepository.save(mage);

        Character boss = new Character("Batta the Beast", UnitClass.MYRMIDON, Allegiance.ENEMY, 5);
        boss.setGameSession(session);
        boss.setPosX(9); boss.setPosY(9);
        boss.addToInventory(new Weapon("Steel Sword", 1, Rarity.UNCOMMON, 11, 25, WeaponType.SWORD));
        characterRepository.save(boss);

        return session.getId();
    }
}
