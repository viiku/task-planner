import 'whatwg-fetch';

const defaultHeaders = () => {
  const headers = { 'Content-Type': 'application/json' };
  const token = localStorage.getItem('token');
  const refreshToken = localStorage.getItem('refreshToken');
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  if (refreshToken) {
    headers['Refresh-Token'] = refreshToken;
  }
  return headers;
};

function handleJson(res) {
  if (!res.ok) return res.json().then(err => ({ error: true, status: res.status, body: err }));
  return res.json().catch(() => ({}));
}

function handleAuthResponse(res) {
  if (res.token) {
    localStorage.setItem('token', res.token);
  }
  if (res.refreshToken) {
    localStorage.setItem('refreshToken', res.refreshToken);
  }
  return res;
}

export const api = {
  signup: (data) => fetch('/api/auth/signup', { 
    method: 'POST', 
    headers: defaultHeaders(), 
    body: JSON.stringify(data)
  }).then(handleJson).then(handleAuthResponse),
  
  login: (data) => fetch('/api/auth/signin', { 
    method: 'POST', 
    headers: defaultHeaders(), 
    body: JSON.stringify(data)
  }).then(handleJson).then(handleAuthResponse),
  
  refreshToken: () => fetch('/api/auth/refreshtoken', { 
    method: 'POST', 
    headers: defaultHeaders()
  }).then(handleJson).then(handleAuthResponse),
  
  logout: () => fetch('/api/auth/logout', { 
    method: 'POST', 
    headers: defaultHeaders()
  }).then(handleJson).then(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
  }),
  
  logoutAll: () => fetch('/api/auth/logout-all-devices', { 
    method: 'POST', 
    headers: defaultHeaders()
  }).then(handleJson).then(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
  }),
  getTasksByType: (type) => fetch(`/api/v1/tasks/${encodeURIComponent(type)}`, { method: 'GET', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  listTasks: () => fetch('/api/v1/tasks', { method: 'GET', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  getTask: (taskId) => fetch(`/api/v1/tasks/${encodeURIComponent(taskId)}`, { method: 'GET', headers: defaultHeaders(), credentials: 'include' }).then(handleJson),
  createTask: (data) => fetch('/api/v1/tasks', { method: 'POST', headers: defaultHeaders(), body: JSON.stringify(data), credentials: 'include' }).then(handleJson),
  updateTask: (taskId, data) => fetch(`/api/v1/tasks/${encodeURIComponent(taskId)}`, { method: 'PUT', headers: defaultHeaders(), body: JSON.stringify(data), credentials: 'include' }).then(handleJson),
  deleteTask: (taskId) => fetch(`/api/v1/tasks/${encodeURIComponent(taskId)}`, { method: 'DELETE', headers: defaultHeaders(), credentials: 'include' }).then(handleJson)
};
