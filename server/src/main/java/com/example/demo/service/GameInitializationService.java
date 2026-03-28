package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.model.Character;
import com.example.demo.model.Character.Allegiance;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.GameSessionRepository;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameInitializationService {
    private final CharacterRepository characterRepository;
    private final GameSessionRepository sessionRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long createNewGame() {
        GameSession session = new GameSession();
        sessionRepository.save(session);

        createUnitWithWeapon(session, "Marth", UnitClass.MYRMIDON, Allegiance.PLAYER, 1, 0, 0, true,
                new Weapon("Iron Sword", 1, Rarity.COMMON, 5, 40, WeaponType.SWORD));

        createUnitWithWeapon(session, "Sain", UnitClass.CAVALIER, Allegiance.PLAYER, 1, 0, 1, false,
                new Weapon("Iron Lance", 1, Rarity.COMMON, 7, 35, WeaponType.LANCE));

        createUnitWithWeapon(session, "Lena", UnitClass.CLERIC, Allegiance.PLAYER, 1, 1, 0, false,
                new Weapon("Heal", 1, Rarity.COMMON, 10, 30, WeaponType.HEAL_STAFF));

        createUnitWithWeapon(session, "Bandit", UnitClass.FIGHTER, Allegiance.ENEMY, 2, 4, 4, false,
                new Weapon("Iron Axe", 1, Rarity.COMMON, 8, 30, WeaponType.AXE));

        createUnitWithWeapon(session, "Dark Mage", UnitClass.MAGE, Allegiance.ENEMY, 2, 7, 8, false,
                new Weapon("Fire Tome", 1, Rarity.COMMON, 8, 30, WeaponType.FIRE_TOME));

        createUnitWithWeapon(session, "Batta the Beast", UnitClass.MYRMIDON, Allegiance.ENEMY, 5, 9, 9, false,
                new Weapon("Steel Sword", 1, Rarity.UNCOMMON, 11, 25, WeaponType.SWORD));

        return session.getId();
    }

    private void createUnitWithWeapon(GameSession s, String name, UnitClass cls, Allegiance alg, int lv, int x, int y, boolean isLord, Weapon w) {
        Character c = new Character(name, cls, alg, lv);
        c.setGameSession(s);
        c.setPosX(x);
        c.setPosY(y);
        c.setLord(isLord);

        c.addToInventory(w);
        w.setEquipped(true);

        characterRepository.save(c);
    }
}
