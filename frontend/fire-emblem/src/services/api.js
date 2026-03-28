import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api/game',
});

export const gameApi = {
  startNewGame: () => API.post('/start'),

  getAllSessions: () => API.get('/sessions'),
  deleteSession: (id) => API.delete(`/session/${id}`),

  getGameState: (sessionId) => API.get(`/state/${sessionId}`),

  moveUnit: (unitId, x, y) => API.post('/move', null, {
    params: {
      unitId: Number(unitId),
      x: Number(x),
      y: Number(y)
    }
  }),

  attack: (attackerId, defenderId) => API.post('/attack', null, {
    params: {
      attackerId: Number(attackerId),
      defenderId: Number(defenderId)
    }
  }),

  getValidMoves: (unitId) => API.get(`/unit/${unitId}/valid-moves`),

  endTurn: (sessionId) => API.post('/end-turn', null, {
    params: { sessionId: Number(sessionId) }
  }),
};
