package com.example.demo.service;

import com.example.demo.dto.BattleReport;
import com.example.demo.model.*;
import com.example.demo.model.Character;
import com.example.demo.model.Character.Allegiance;
import com.example.demo.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private BattleService battleService;

    @Test
    void testExecuteAttack_Success() {
        Character hero = new Character("Marth", 20, 10, 10, 5, 5, 5);
        hero.setAllegiance(Allegiance.PLAYER);
        hero.setUnitClass(UnitClass.MYRMIDON);

        Weapon sword = new Weapon("Iron Sword", 1, Rarity.COMMON, 5, 40, WeaponType.SWORD);
        hero.addToInventory(sword);
        hero.setPosX(0);
        hero.setPosY(0);

        Character bandit = new Character("Bandit", 10, 0, 5, 5, 5, 5);
        bandit.setAllegiance(Allegiance.ENEMY);
        bandit.setUnitClass(UnitClass.FIGHTER);
        // Defenders need a weapon to avoid NullPointer in Counter-attack logic
        bandit.addToInventory(new Weapon("Axe", 1, Rarity.COMMON, 5, 40, WeaponType.AXE));
        bandit.setPosX(1);
        bandit.setPosY(0);

        BattleReport report = battleService.executeAttack(hero, bandit);

        assertEquals(12, report.damageDealt());
        assertTrue(report.defenderDied());
        assertTrue(hero.isHasActed());
    }

    @Test
    void testWeaponTriangle_SwordBeatsAxe() {
        Character hero = new Character("Marth", 20, 10, 10, 5, 5, 5);
        hero.setAllegiance(Allegiance.PLAYER);
        hero.setUnitClass(UnitClass.MYRMIDON);

        Weapon sword = new Weapon("Iron Sword", 1, Rarity.COMMON, 5, 40, WeaponType.SWORD);
        hero.addToInventory(sword);
        hero.setPosX(0);
        hero.setPosY(0);

        Character bandit = new Character("Axe Bandit", 20, 0, 5, 5, 5, 5);
        bandit.setAllegiance(Allegiance.ENEMY);
        bandit.setUnitClass(UnitClass.FIGHTER);
        Weapon axe = new Weapon("Iron Axe", 1, Rarity.COMMON, 5, 40, WeaponType.AXE);
        bandit.addToInventory(axe);
        bandit.setPosX(1);
        bandit.setPosY(0);

        BattleReport report = battleService.executeAttack(hero, bandit);

        // Triangle bonus (+2) applied: (10 + 5 + 2) - 5 = 12
        assertEquals(12, report.damageDealt());
        assertTrue(report.isBroken());
        assertFalse(report.canCounter());
    }

    @Test
    void testExecuteAttack_LethalDamage() {
        Character hero = new Character("Sigurd", 20, 10, 20, 5, 5, 5);
        hero.setAllegiance(Allegiance.PLAYER);
        hero.setUnitClass(UnitClass.CAVALIER);

        Weapon silverSword = new Weapon("Silver Sword", 1, Rarity.RARE, 10, 20, WeaponType.SWORD);
        hero.addToInventory(silverSword);
        hero.setPosX(0);
        hero.setPosY(0);

        Character weakDemon = new Character("Slime", 5, 0, 1, 1, 5, 5);
        weakDemon.setAllegiance(Allegiance.ENEMY);
        weakDemon.setUnitClass(UnitClass.SOLDIER);
        // Even slimes need a dummy weapon for range/type checks
        weakDemon.addToInventory(new Weapon("Slime", 1, Rarity.COMMON, 1, 10, WeaponType.LANCE));
        weakDemon.setPosX(1);
        weakDemon.setPosY(0);

        BattleReport report = battleService.executeAttack(hero, weakDemon);

        assertTrue(report.defenderDied());
        assertEquals(0, report.defenderRemainingHp());
    }
}
