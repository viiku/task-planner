import 'whatwg-fetch';

const defaultHeaders = () => ({ 'Content-Type': 'application/json' });

function handleJson(res) {
  if (!res.ok) return res.json().then(err => ({ error: true, status: res.status, body: err }));
  return res.json().catch(() => ({}));
}

export const api = {
  signup: (data) => fetch('/auth/signup', { method: 'POST', headers: defaultHeaders(), body: JSON.stringify(data), credentials: 'include' }).then(handleJson),
  login: (data) => fetch('/auth/login', { method: 'POST', headers: defaultHeaders(), body: JSON.stringify(data), credentials: 'include' }).then(handleJson),
  refreshToken: () => fetch('/auth/refresh-token', { method: 'POST', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  logout: () => fetch('/auth/logout', { method: 'POST', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  logoutAll: () => fetch('/auth/logout-all-devices', { method: 'POST', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  getTasksByType: (type) => fetch(`/api/v1/tasks/${encodeURIComponent(type)}`, { method: 'GET', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  listTasks: () => fetch('/api/v1/tasks', { method: 'GET', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  getTask: (taskId) => fetch(`/api/v1/tasks/${encodeURIComponent(taskId)}`, { method: 'GET', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  createTask: (data) => fetch('/api/v1/tasks', { method: 'POST', headers: defaultHeaders(), body: JSON.stringify(data), credentials: 'include' }).then(handleJson),
  updateTask: (taskId, data) => fetch(`/api/v1/tasks/${encodeURIComponent(taskId)}`, { method: 'PUT', headers: defaultHeaders(), body: JSON.stringify(data), credentials: 'include' }).then(handleJson),
  deleteTask: (taskId) => fetch(`/api/v1/tasks/${encodeURIComponent(taskId)}`, { method: 'DELETE', headers: defaultHeaders(), credentials: 'include' }).then(handleJson)
};
