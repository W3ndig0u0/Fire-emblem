import { useCallback, useMemo, useState } from 'react';
import { useGame } from '../hooks/useGame';
import { gameApi } from '../services/api';
import './../styles/BattleMap.css';
import ActionMenu from './ActionMenu';

const BattleMap = () => {
  const { gameState, selectedUnit, setSelectedUnit, refreshState } = useGame();
  const [menuPos, setMenuPos] = useState(null);
  const [validMoves, setValidMoves] = useState([]);
  const [validAttacks, setValidAttacks] = useState([]);
  const [isAttacking, setIsAttacking] = useState(false);
  const [hasMovedThisTurn, setHasMovedThisTurn] = useState(false);

  const units = gameState?.units || [];
  const mapWidth = gameState?.mapWidth || 0;
  const mapHeight = gameState?.mapHeight || 0;

  const getTilesInRange = useCallback((originX, originY, minRange, maxRange) => {
    const tiles = [];
    for (let x = 0; x < mapWidth; x++) {
      for (let y = 0; y < mapHeight; y++) {
        const dist = Math.abs(x - originX) + Math.abs(y - originY);
        if (dist >= minRange && dist <= maxRange) tiles.push({ x, y });
      }
    }
    return tiles;
  }, [mapWidth, mapHeight]);

  const enemiesInRange = useMemo(() => {
    if (!selectedUnit) return false;
    const { minRange = 1, maxRange = 1 } = selectedUnit.equippedWeapon || {};
    const reach = getTilesInRange(selectedUnit.posX, selectedUnit.posY, minRange, maxRange);
    return units.some(u =>
      u.alive && u.allegiance === 'ENEMY' &&
      reach.some(t => t.x === u.posX && t.y === u.posY)
    );
  }, [selectedUnit, units, getTilesInRange]);

  const resetSelection = () => {
    setMenuPos(null);
    setSelectedUnit(null);
    setValidMoves([]);
    setValidAttacks([]);
    setIsAttacking(false);
    setHasMovedThisTurn(false);
  };

  const handleAttackSelection = () => {
    if (!selectedUnit) return;
    const { minRange = 1, maxRange = 1 } = selectedUnit.equippedWeapon || {};
    setValidAttacks(getTilesInRange(selectedUnit.posX, selectedUnit.posY, minRange, maxRange).map(t => [t.x, t.y]));
    setValidMoves([]);
    setIsAttacking(true);
    setMenuPos(null);
  };

  const handleTileClick = async (e, unit, x, y) => {
    if (selectedUnit && isAttacking) {
      const isTarget = validAttacks.some(m => m[0] === x && m[1] === y);
      if (isTarget && unit && unit.allegiance === 'ENEMY') {
        try {
          await gameApi.attack(selectedUnit.id, unit.id);
          resetSelection();
          await refreshState();
        } catch (err) { console.error(err); }
        return;
      }
      setIsAttacking(false);
      setMenuPos({ x: e.clientX, y: e.clientY });
      return;
    }

    if (selectedUnit && hasMovedThisTurn) {
      if (unit && unit.id === selectedUnit.id) {
        setMenuPos({ x: e.clientX, y: e.clientY });
        const { minRange = 1, maxRange = 1 } = selectedUnit.equippedWeapon || {};
        setValidAttacks(getTilesInRange(x, y, minRange, maxRange).map(t => [t.x, t.y]));
        return;
      }
      resetSelection();
      return;
    }

    const isEnemy = unit && unit.allegiance === 'ENEMY';
    const isAttackTile = validAttacks.some(m => m[0] === x && m[1] === y);

    if (selectedUnit && isEnemy && isAttackTile) {
      const { minRange = 1, maxRange = 1 } = selectedUnit.equippedWeapon || {};
      const bestTile = validMoves.find(m => {
        const mx = m.posX ?? m.x ?? m[0];
        const my = m.posY ?? m.y ?? m[1];
        const dist = Math.abs(mx - x) + Math.abs(my - y);
        return dist >= minRange && dist <= maxRange;
      });

      if (bestTile) {
        const tx = bestTile.posX ?? bestTile.x ?? bestTile[0];
        const ty = bestTile.posY ?? bestTile.y ?? bestTile[1];
        try {
          if (selectedUnit.posX !== tx || selectedUnit.posY !== ty) {
            await gameApi.moveUnit(selectedUnit.id, tx, ty);
          }
          await gameApi.attack(selectedUnit.id, unit.id);
          resetSelection();
          await refreshState();
        } catch (err) { console.error(err); }
        return;
      }
    }

    const isMoveTarget = validMoves.some(m => (m.posX ?? m[0] ?? m.x) === x && (m.posY ?? m[1] ?? m.y) === y);
    if (isMoveTarget && selectedUnit) {
      try {
        if (selectedUnit.posX !== x || selectedUnit.posY !== y) {
          await gameApi.moveUnit(selectedUnit.id, x, y);
        }
        const latestState = await refreshState();
        const movedUnit = (latestState?.units || units).find(u => u.id === selectedUnit.id);
        if (movedUnit) {
          setSelectedUnit(movedUnit);
          setValidMoves([]);
          setHasMovedThisTurn(true);
          const { minRange = 1, maxRange = 1 } = movedUnit.equippedWeapon || {};
          setValidAttacks(getTilesInRange(x, y, minRange, maxRange).map(t => [t.x, t.y]));
          setMenuPos({ x: e.clientX, y: e.clientY });
        }
      } catch (err) { console.error(err); }
      return;
    }

    if (unit && unit.allegiance === 'PLAYER' && !unit.hasActed) {
      setSelectedUnit(unit);
      try {
        const response = await gameApi.getValidMoves(unit.id);
        const moveTiles = Array.isArray(response) ? response : (response.data || []);
        setValidMoves(moveTiles);
        const { minRange = 1, maxRange = 1 } = unit.equippedWeapon || {};
        const attackSet = new Set();
        moveTiles.forEach(tile => {
          const tx = tile.posX ?? tile.x ?? tile[0];
          const ty = tile.posY ?? tile.y ?? tile[1];
          getTilesInRange(tx, ty, minRange, maxRange).forEach(t => attackSet.add(`${t.x},${t.y}`));
        });
        setValidAttacks(Array.from(attackSet).map(s => s.split(',').map(Number)));
      } catch (err) { console.error(err); }
    } else {
      resetSelection();
    }
  };

  if (!gameState) return null;

  return (
    <div className="battle-map-container">
      <div className="grid-board" style={{ gridTemplateColumns: `repeat(${mapWidth}, var(--tile-size))` }}>
        {Array.from({ length: mapWidth * mapHeight }).map((_, i) => {
          const x = i % mapWidth;
          const y = Math.floor(i / mapWidth);
          const unit = units.find(u => u.posX === x && u.posY === y && u.alive);
          const isBlue = validMoves.some(m => (m.posX ?? m[0] ?? m.x) === x && (m.posY ?? m[1] ?? m.y) === y);
          const isRed = validAttacks.some(m => m[0] === x && m[1] === y);
          return (
            <div
              key={`${x}-${y}`}
              className={`grid-tile ${isBlue ? 'highlight-move' : isRed ? 'highlight-attack' : ''}`}
              onClick={(e) => handleTileClick(e, unit, x, y)}
            >
              {unit && (
                <div className={`unit-sprite ${unit.allegiance.toLowerCase()} ${unit.hasActed ? 'waited' : ''}`}>
                  <span>{unit.lord ? '👑' : unit.unitClass[0]}</span>
                </div>
              )}
            </div>
          );
        })}
      </div>

      <ActionMenu
        position={menuPos}
        unit={selectedUnit}
        hasMoved={hasMovedThisTurn}
        canAttack={enemiesInRange}
        onAttack={handleAttackSelection}
        onWait={async () => {
          resetSelection();
          await refreshState();
        }}
        onEndTurn={async () => {
          await gameApi.endTurn(gameState.sessionId);
          resetSelection();
          refreshState();
        }}
      />
    </div>
  );
};

export default BattleMap;