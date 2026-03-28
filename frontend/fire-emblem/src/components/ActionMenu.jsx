import './../styles/BattleMap.css';

const ActionMenu = ({ position, unit, onMove, onAttack, onWait, hasMoved, ...props }) => {
  if (!position || !unit) return null;

  return (
    <div className="action-menu" style={{ top: position.y, left: position.x }}>
      {/* Visa bara Move om enheten INTE har flyttat än */}
      {!hasMoved && (
        <button className="menu-btn" onClick={onMove}>Move</button>
      )}

      {/* Attack ska alltid vara tillgänglig så länge man inte agerat helt */}
      <button className="menu-btn" onClick={onAttack}>Attack</button>

      <button className="menu-btn" onClick={() => props.onOpenInventory()}>Items</button>
      <button className="menu-btn" onClick={onWait}>Wait</button>
    </div>
  );
};


export default ActionMenu;
