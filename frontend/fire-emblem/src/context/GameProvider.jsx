import { useState } from 'react';
import { gameApi } from '../services/api';
import { GameContext } from './GameContext';

export const GameProvider = ({ children }) => {
  const [sessionId, setSessionId] = useState(null);
  const [gameState, setGameState] = useState(null);
  const [selectedUnit, setSelectedUnit] = useState(null);
  const [loading, setLoading] = useState(false);

  const refreshState = async (id) => {
    const res = await gameApi.getGameState(id || sessionId);
    setGameState(res.data);
  };

  const startNewGame = async () => {
    setLoading(true);
    try {
      const res = await gameApi.startNewGame();
      setSessionId(res.data);
      await refreshState(res.data);
    } catch (err) {
      console.error("Failed to start game", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <GameContext.Provider value={{
      sessionId, setSessionId, gameState, selectedUnit, setSelectedUnit,
      startNewGame, refreshState, loading
    }}>
      {children}
    </GameContext.Provider>
  );
};
