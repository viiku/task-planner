import './styles.css';
import { api } from './api';

const app = document.getElementById('app');

function el(tag, attrs = {}, ...children) {
  const e = document.createElement(tag);
  Object.entries(attrs).forEach(([k, v]) => { if (k === 'onclick') e.addEventListener('click', v); else e.setAttribute(k, v); });
  children.forEach(c => e.append(typeof c === 'string' ? document.createTextNode(c) : c));
  return e;
}

function renderAuth() {
  const email = el('input', { placeholder: 'Email', id: 'email' });
  const password = el('input', { placeholder: 'Password', id: 'password', type: 'password' });
  const signup = el('button', { onclick: async () => { await handleSignup(); } }, 'Sign Up');
  const login = el('button', { onclick: async () => { await handleLogin(); } }, 'Log In');
  const logout = el('button', { onclick: async () => { await handleLogout(); } }, 'Log Out');
  const logoutAll = el('button', { onclick: async () => { await handleLogoutAll(); } }, 'Log Out All Devices');
  const refresh = el('button', { onclick: async () => { await handleRefresh(); } }, 'Refresh Token');

  return el('div', { class: 'card' },
    el('h2', {}, 'Auth'),
    email, password,
    el('div', { class: 'row' }, signup, login, logout, refresh)
  );
}

function renderTasks() {
  const input = el('input', { placeholder: 'Task type or list id', id: 'taskType' });
  const btn = el('button', { onclick: async () => { await handleGetTasks(); } }, 'Get Tasks');
  const list = el('div', { id: 'tasksList' });

  return el('div', { class: 'card' }, el('h2', {}, 'Tasks'), input, btn, list);
}

function renderTaskManager() {
  const listBtn = el('button', { onclick: async () => { await handleListTasks(); } }, 'List All Tasks');
  const newTitle = el('input', { placeholder: 'Title', id: 'newTitle' });
  const newType = el('input', { placeholder: 'Type', id: 'newType' });
  const createBtn = el('button', { onclick: async () => { await handleCreateTask(); } }, 'Create Task');
  const editId = el('input', { placeholder: 'Task ID to update/delete', id: 'editId' });
  const updateBtn = el('button', { onclick: async () => { await handleUpdateTask(); } }, 'Update Task');
  const deleteBtn = el('button', { onclick: async () => { await handleDeleteTask(); } }, 'Delete Task');
  const list = el('div', { id: 'tasksListAll' });

  return el('div', { class: 'card' },
    el('h2', {}, 'Task Manager'),
    newTitle, newType, createBtn,
    el('div', { class: 'row' }, editId, updateBtn, deleteBtn),
    listBtn, list
  );
}

async function handleSignup() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  const res = await api.signup({ email, password });
  showResult(res);
  if (!res || res.error) return;
  if (window._onAuthSuccess) window._onAuthSuccess();
}

async function handleLogin() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  const res = await api.login({ email, password });
  showResult(res);
  if (!res || res.error) return;
  if (window._onAuthSuccess) window._onAuthSuccess();
}

async function handleRefresh() {
  const res = await api.refreshToken();
  showResult(res);
}

async function handleLogout() {
  const res = await api.logout();
  showResult(res);
}

async function handleLogoutAll() {
  const res = await api.logoutAll();
  showResult(res);
}

async function handleGetTasks() {
  const taskType = document.getElementById('taskType').value;
  const tasks = await api.getTasksByType(taskType);
  const container = document.getElementById('tasksList');
  container.innerHTML = '';
  if (!tasks || tasks.length === 0) container.appendChild(el('div', {}, 'No tasks'));
  else tasks.forEach(t => container.appendChild(el('div', { class: 'task' }, JSON.stringify(t))));
}

async function handleListTasks() {
  const tasks = await api.listTasks();
  const container = document.getElementById('tasksListAll');
  container.innerHTML = '';
  if (!tasks || tasks.length === 0) container.appendChild(el('div', {}, 'No tasks'));
  else tasks.forEach(t => {
    const row = el('div', { class: 'task' }, JSON.stringify(t));
    container.appendChild(row);
  });
}

async function handleCreateTask() {
  const title = document.getElementById('newTitle').value;
  const type = document.getElementById('newType').value;
  const res = await api.createTask({ title, type });
  showResult(res);
}

async function handleUpdateTask() {
  const id = document.getElementById('editId').value;
  const title = document.getElementById('newTitle').value;
  const type = document.getElementById('newType').value;
  if (!id) return showResult({ error: true, message: 'Provide task id in field' });
  const res = await api.updateTask(id, { title, type });
  showResult(res);
}

async function handleDeleteTask() {
  const id = document.getElementById('editId').value;
  if (!id) return showResult({ error: true, message: 'Provide task id in field' });
  const res = await api.deleteTask(id);
  showResult(res);
}

function showResult(r) {
  const out = document.getElementById('output') || el('pre', { id: 'output' });
  if (!document.getElementById('output')) document.body.appendChild(out);
  out.textContent = JSON.stringify(r, null, 2);
}

function mount() {
  const root = el('div', { id: 'root' }, renderAuth());
  app.appendChild(root);
  // Only show task manager after successful login
  window._onAuthSuccess = () => {
    const root = document.getElementById('root');
    root.innerHTML = '';
    root.appendChild(renderAuth());
    root.appendChild(renderTaskManager());
    root.appendChild(renderTasks());
  };
}

mount();
