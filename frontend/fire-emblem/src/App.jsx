import { useEffect, useState } from 'react';
import './App.css';
import BattleMap from './components/BattleMap';
import { useGame } from './hooks/useGame';
import { gameApi } from './services/api';

function App() {
  const { sessionId, setSessionId, gameState, startNewGame, refreshState, loading } = useGame();
  const [sessions, setSessions] = useState([]);

  useEffect(() => {
    const fetchSessions = async () => {
      try {
        const res = await gameApi.getAllSessions();
        setSessions(res.data);
      } catch (error) {
        console.error("Failed to load sessions:", error);
      }
    };

    if (!sessionId) {
      fetchSessions();
    }
  }, [sessionId]);

  const handleJoin = async (id) => {
    try {
      setSessionId(id);
      await refreshState(id);
    } catch (error) {
      console.error("Join failed:", error);
      alert("Could not load this session!");
    }
  };

  const handleDelete = async (e, id) => {
    e.stopPropagation();
    try {
      await gameApi.deleteSession(id);
      setSessions(prev => prev.filter(s => s.id !== id));
    } catch (error) {
      console.error("Delete failed:", error);
    }
  };

  // LOBBY VIEW
  if (!sessionId) {
    return (
      <div className="lobby-container">
        <h1 className="lobby-title">TACTICAL LOBBY</h1>

        <div className="session-grid">
          {sessions.map(s => {

            return (
              <div key={s.id} className="session-card" onClick={() => handleJoin(s.id)}>
                <div className="session-header">
                  <span className="id-tag">BATTLE #{s.id}</span>
                  <span className="phase-tag">{s.currentPhase}</span>
                </div>
                <div className="session-body">
                  <p>Turn: {s.turnNumber}</p>
                  <p>Units: {s.characters?.filter(u => u.alive).length || 0}</p>
                </div>
                <button className="delete-btn" onClick={(e) => handleDelete(e, s.id)}>
                  DELETE SAVE
                </button>
              </div>
            );
          })}


          <button className="new-game-card" onClick={startNewGame} disabled={loading}>
            {loading ? 'CREATING...' : '+ NEW BATTLE'}
          </button>
        </div>
      </div >
    );
  }

  // GAME VIEW
  return (
    <div className="game-screen">
      <header className="game-status-bar">
        <button className="back-btn" onClick={() => setSessionId(null)}>← LOBBY</button>
        <div className="phase-display">{gameState?.currentPhase} - TURN {gameState?.turnNumber}</div>
        <div className={`status - pill ${gameState?.status?.toLowerCase()}`}>{gameState?.status}</div>
      </header>

      <BattleMap />
    </div>
  );
}

export default App;
