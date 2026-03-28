import './../styles/BattleMap.css';

const InventoryMenu = ({ unit, onEquip, onUse, onClose }) => {
  if (!unit) return null;

  return (
    <div className="inventory-overlay">
      <div className="inventory-modal">
        <header className="inventory-header">
          <h3>{unit.name}'s Items</h3>
          <button className="close-btn" onClick={onClose}>X</button>
        </header>
        <div className="item-list">
          {unit.inventory.map((item) => (
            <div key={item.id} className={`item-row ${item.equipped ? 'equipped' : ''}`}>
              <div className="item-info">
                <span className="item-name">{item.name}</span>
                <span className="item-durability">{item.durability}/{item.maxDurability}</span>
              </div>
              <div className="item-actions">
                {item.weaponType ? (
                  <button
                    className="item-btn equip"
                    onClick={() => onEquip(item.id)}
                    disabled={item.equipped}
                  >
                    {item.equipped ? 'Equipped' : 'Equip'}
                  </button>
                ) : (
                  <button className="item-btn use" onClick={() => onUse(item.id)}>Use</button>
                )}
              </div>
            </div>
          ))}
          {unit.inventory.length === 0 && <p className="empty-msg">No items carried.</p>}
        </div>
      </div>
    </div>
  );
};

export default InventoryMenu;
