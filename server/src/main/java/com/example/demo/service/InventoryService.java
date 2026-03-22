package com.example.demo.service;

import com.example.demo.dto.UseItemResult;
import com.example.demo.model.*;
import com.example.demo.model.Character;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final ItemRepository itemRepository;
  private final CharacterRepository characterRepository;
  @Transactional
  public UseItemResult useItem(Long characterId, Long itemId, Long targetId) {
    Character user = characterRepository.findById(characterId).orElseThrow();
    Item item = itemRepository.findById(itemId).orElseThrow();
    Character target = characterRepository.findById(targetId).orElseThrow();

    if (item.getOwner() == null || !item.getOwner().getId().equals(characterId)) {
      throw new RuntimeException("You do not own this item!");
    }

    item.applyEffect(target);

    boolean wasConsumed = false;
    String message = item.getName() + " used on " + target.getName();

    if (item instanceof Weapon weapon) {
      if (weapon.getDurability() <= 0) {
        wasConsumed = true;
        message = "Your " + weapon.getName() + " broke!";
      }
    } else {
      wasConsumed = true;
    }

    if (wasConsumed) {
      user.getInventory().remove(item);
      itemRepository.delete(item);
    }

    return new UseItemResult(message, target.getHealth(), wasConsumed);
  }

}
