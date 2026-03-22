import com.example.demo.model.Item;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

  @Autowired private ItemRepository itemRepository;
  @Autowired private CharacterRepository characterRepository;

  @Transactional
  public String useItem(Long characterId, Long itemId, Long targetId) {
    Character user = characterRepository.findById(characterId).orElseThrow();
    Item item = itemRepository.findById(itemId).orElseThrow();
    Character target = characterRepository.findById(targetId).orElseThrow();

    StringBuilder resultMessage = new StringBuilder();

    if (item instanceof Weapon) {
      Weapon weapon = (Weapon) item;
      // Your logic: Reduce durability
      weapon.setDurability(weapon.getDurability() - 1);

      if (weapon.getDurability() <= 0) {
        user.getInventory().remove(weapon);
        itemRepository.delete(weapon);
        resultMessage.append("Your ").append(weapon.getName()).append(" broke! ");
      }
    } else {
      // Potions/Consumables
      item.use(target); // Calls your applyEffect logic
      user.getInventory().remove(item);
      itemRepository.delete(item);
    }

    characterRepository.save(user);
    return resultMessage.append("Item used successfully.").toString();
  }
}
