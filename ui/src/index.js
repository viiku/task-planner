// src/index.js - Complete refactored frontend
import './styles.css';

// =============================================================================
// API CLIENT
// =============================================================================
class ApiClient {
  constructor() {
    this.baseUrl = '';
  }

  async request(url, options = {}) {
    const config = {
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      },
      ...options
    };

    try {
      const response = await fetch(this.baseUrl + url, config);
      const data = await this.handleResponse(response);
      return data;
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  }

  async handleResponse(response) {
    let data;
    try {
      data = await response.json();
    } catch (e) {
      data = {};
    }

    if (!response.ok) {
      if (response.status === 401) {
        // Unauthorized - trigger logout
        AppState.logout();
        throw new Error('Session expired. Please login again.');
      }
      throw new Error(data.message || `HTTP ${response.status}`);
    }

    return data;
  }

  // Auth endpoints
  signup = (data) => this.request('/auth/signup', { method: 'POST', body: JSON.stringify(data) });
  login = (data) => this.request('/auth/login', { method: 'POST', body: JSON.stringify(data) });
  logout = () => this.request('/auth/logout', { method: 'POST' });
  logoutAll = () => this.request('/auth/logout-all-devices', { method: 'POST' });
  refreshToken = () => this.request('/auth/refresh-token', { method: 'POST' });

  // Task endpoints  
  getTasks = () => this.request('/api/v1/tasks');
  getTasksByType = (type) => this.request(`/api/v1/tasks/${encodeURIComponent(type)}`);
  createTask = (data) => this.request('/api/v1/tasks', { method: 'POST', body: JSON.stringify(data) });
  updateTask = (id, data) => this.request(`/api/v1/tasks/${encodeURIComponent(id)}`, { method: 'PUT', body: JSON.stringify(data) });
  deleteTask = (id) => this.request(`/api/v1/tasks/${encodeURIComponent(id)}`, { method: 'DELETE' });
}

const api = new ApiClient();

// =============================================================================
// APPLICATION STATE
// =============================================================================
class ApplicationState {
  constructor() {
    this.state = {
      // Auth state
      isAuthenticated: false,
      user: null,
      
      // UI state
      currentView: 'auth', // 'auth', 'dashboard', 'tasks', 'profile'
      loading: false,
      
      // Data state
      tasks: [],
      filteredTasks: [],
      searchQuery: '',
      statusFilter: '',
      
      // Notifications
      notifications: []
    };
    
    this.listeners = [];
  }

  // State management
  setState(updates) {
    this.state = { ...this.state, ...updates };
    this.notifyListeners();
  }

  getState() {
    return { ...this.state };
  }

  subscribe(listener) {
    this.listeners.push(listener);
    return () => {
      this.listeners = this.listeners.filter(l => l !== listener);
    };
  }

  notifyListeners() {
    this.listeners.forEach(listener => listener(this.state));
  }

  // Auth actions
  async login(email, password) {
    try {
      this.setState({ loading: true });
      const result = await api.login({ email, password });
      
      this.setState({
        isAuthenticated: true,
        user: { email },
        currentView: 'dashboard',
        loading: false
      });
      
      await this.loadTasks();
      this.showNotification('Login successful!', 'success');
      return result;
    } catch (error) {
      this.setState({ loading: false });
      this.showNotification(error.message, 'error');
      throw error;
    }
  }

  async signup(email, password) {
    try {
      this.setState({ loading: true });
      await api.signup({ email, password });
      this.setState({ loading: false });
      this.showNotification('Account created! Please login.', 'success');
    } catch (error) {
      this.setState({ loading: false });
      this.showNotification(error.message, 'error');
      throw error;
    }
  }

  async logout() {
    try {
      await api.logout();
    } catch (error) {
      console.warn('Logout request failed:', error);
    } finally {
      this.setState({
        isAuthenticated: false,
        user: null,
        currentView: 'auth',
        tasks: [],
        filteredTasks: []
      });
      this.showNotification('Logged out successfully', 'info');
    }
  }

  async logoutAllDevices() {
    try {
      await api.logoutAll();
      this.setState({
        isAuthenticated: false,
        user: null,
        currentView: 'auth',
        tasks: [],
        filteredTasks: []
      });
      this.showNotification('Logged out from all devices', 'info');
    } catch (error) {
      this.showNotification(error.message, 'error');
    }
  }

  async refreshToken() {
    try {
      await api.refreshToken();
      this.showNotification('Session refreshed', 'success');
    } catch (error) {
      this.showNotification(error.message, 'error');
      this.logout();
    }
  }

  // Task actions
  async loadTasks() {
    try {
      this.setState({ loading: true });
      const tasks = await api.getTasks();
      this.setState({ 
        tasks: Array.isArray(tasks) ? tasks : [],
        loading: false 
      });
      this.applyFilters();
    } catch (error) {
      this.setState({ loading: false, tasks: [] });
      this.showNotification('Failed to load tasks', 'error');
    }
  }

  async createTask(taskData) {
    try {
      await api.createTask(taskData);
      await this.loadTasks();
      this.showNotification('Task created successfully!', 'success');
    } catch (error) {
      this.showNotification(error.message, 'error');
    }
  }

  async updateTask(id, taskData) {
    try {
      await api.updateTask(id, taskData);
      await this.loadTasks();
      this.showNotification('Task updated successfully!', 'success');
    } catch (error) {
      this.showNotification(error.message, 'error');
    }
  }

  async deleteTask(id) {
    try {
      await api.deleteTask(id);
      await this.loadTasks();
      this.showNotification('Task deleted successfully!', 'success');
    } catch (error) {
      this.showNotification(error.message, 'error');
    }
  }

  // Filtering and search
  setSearchQuery(query) {
    this.setState({ searchQuery: query });
    this.applyFilters();
  }

  setStatusFilter(status) {
    this.setState({ statusFilter: status });
    this.applyFilters();
  }

  applyFilters() {
    const { tasks, searchQuery, statusFilter } = this.state;
    let filtered = [...tasks];

    if (searchQuery) {
      const query = searchQuery.toLowerCase();
      filtered = filtered.filter(task => 
        (task.title && task.title.toLowerCase().includes(query)) ||
        (task.type && task.type.toLowerCase().includes(query)) ||
        (task.description && task.description.toLowerCase().includes(query))
      );
    }

    if (statusFilter) {
      filtered = filtered.filter(task => task.status === statusFilter);
    }

    this.setState({ filteredTasks: filtered });
  }

  // UI actions
  setView(view) {
    this.setState({ currentView: view });
  }

  showNotification(message, type = 'info') {
    const id = Date.now();
    const notification = { id, message, type };
    
    this.setState({
      notifications: [...this.state.notifications, notification]
    });

    setTimeout(() => {
      this.setState({
        notifications: this.state.notifications.filter(n => n.id !== id)
      });
    }, 5000);
  }

  // Initialize app
  async init() {
    try {
      // Try to restore session
      await api.refreshToken();
      this.setState({
        isAuthenticated: true,
        user: { email: 'User' },
        currentView: 'dashboard'
      });
      await this.loadTasks();
    } catch (error) {
      // No valid session, stay on auth page
      console.log('No valid session found');
    }
  }
}

const AppState = new ApplicationState();

// =============================================================================
// UI COMPONENTS
// =============================================================================

// Helper function to create elements
function createElement(tag, attributes = {}, ...children) {
  const element = document.createElement(tag);
  
  Object.entries(attributes).forEach(([key, value]) => {
    if (key.startsWith('on') && typeof value === 'function') {
      element.addEventListener(key.substring(2).toLowerCase(), value);
    } else if (key === 'className') {
      element.className = value;
    } else {
      element.setAttribute(key, value);
    }
  });
  
  children.forEach(child => {
    if (typeof child === 'string' || typeof child === 'number') {
      element.appendChild(document.createTextNode(child));
    } else if (child && child.nodeType) {
      element.appendChild(child);
    }
  });
  
  return element;
}

// Navigation Component
function createNavigation(state) {
  if (!state.isAuthenticated) return null;

  const nav = createElement('nav', { className: 'navigation' });
  
  const navItems = [
    { id: 'dashboard', label: 'Dashboard', icon: '📊' },
    { id: 'tasks', label: 'Tasks', icon: '✓' },
    { id: 'profile', label: 'Profile', icon: '👤' }
  ];

  navItems.forEach(item => {
    const button = createElement('button', {
      className: `nav-btn ${state.currentView === item.id ? 'active' : ''}`,
      onClick: () => AppState.setView(item.id)
    }, `${item.icon} ${item.label}`);
    nav.appendChild(button);
  });

  const logoutBtn = createElement('button', {
    className: 'nav-btn logout-btn',
    onClick: () => {
      if (confirm('Are you sure you want to logout?')) {
        AppState.logout();
      }
    }
  }, '🚪 Logout');
  
  nav.appendChild(logoutBtn);
  return nav;
}

// Authentication Form Component
function createAuthForm(state) {
  const container = createElement('div', { className: 'auth-container' });
  const form = createElement('div', { className: 'auth-form' });
  
  const title = createElement('h2', {}, 'TaskPlanner');
  const subtitle = createElement('p', { className: 'auth-subtitle' }, 'Sign in to your account');
  
  const emailInput = createElement('input', {
    type: 'email',
    id: 'auth-email',
    placeholder: 'Email address',
    required: true
  });
  
  const passwordInput = createElement('input', {
    type: 'password',
    id: 'auth-password',
    placeholder: 'Password',
    required: true
  });

  const loginBtn = createElement('button', {
    className: 'primary-btn',
    onClick: async (e) => {
      e.preventDefault();
      const email = document.getElementById('auth-email').value.trim();
      const password = document.getElementById('auth-password').value;
      
      if (!email || !password) {
        AppState.showNotification('Please fill in all fields', 'error');
        return;
      }
      
      try {
        await AppState.login(email, password);
      } catch (error) {
        // Error already handled in AppState.login
      }
    }
  }, state.loading ? 'Signing in...' : 'Sign In');

  const signupBtn = createElement('button', {
    className: 'secondary-btn',
    onClick: async (e) => {
      e.preventDefault();
      const email = document.getElementById('auth-email').value.trim();
      const password = document.getElementById('auth-password').value;
      
      if (!email || !password) {
        AppState.showNotification('Please fill in all fields', 'error');
        return;
      }
      
      try {
        await AppState.signup(email, password);
        // Clear form after successful signup
        document.getElementById('auth-email').value = '';
        document.getElementById('auth-password').value = '';
      } catch (error) {
        // Error already handled in AppState.signup
      }
    }
  }, state.loading ? 'Creating...' : 'Create Account');

  if (state.loading) {
    loginBtn.disabled = true;
    signupBtn.disabled = true;
  }

  const buttonGroup = createElement('div', { className: 'button-group' }, loginBtn, signupBtn);
  
  form.appendChild(title);
  form.appendChild(subtitle);
  form.appendChild(emailInput);
  form.appendChild(passwordInput);
  form.appendChild(buttonGroup);
  
  container.appendChild(form);
  return container;
}

// Dashboard Component
function createDashboard(state) {
  const container = createElement('div', { className: 'dashboard' });
  
  const header = createElement('div', { className: 'dashboard-header' });
  const title = createElement('h1', {}, 'Dashboard');
  const welcomeMsg = createElement('p', { className: 'welcome-message' }, 
    `Welcome back${state.user?.email ? ', ' + state.user.email : ''}!`);
  
  header.appendChild(title);
  header.appendChild(welcomeMsg);

  // Stats cards
  const statsGrid = createElement('div', { className: 'stats-grid' });
  
  const totalTasks = state.tasks.length;
  const completedTasks = state.tasks.filter(t => t.status === 'completed' || t.status === 'COMPLETED').length;
  const pendingTasks = state.tasks.filter(t => t.status === 'pending' || t.status === 'PENDING' || !t.status).length;

  const statsData = [
    { title: 'Total Tasks', value: totalTasks, color: 'blue' },
    { title: 'Completed', value: completedTasks, color: 'green' },
    { title: 'Pending', value: pendingTasks, color: 'orange' }
  ];

  statsData.forEach(stat => {
    const card = createElement('div', { className: `stat-card ${stat.color}` });
    const statTitle = createElement('h3', {}, stat.title);
    const statValue = createElement('div', { className: 'stat-value' }, stat.value.toString());
    card.appendChild(statTitle);
    card.appendChild(statValue);
    statsGrid.appendChild(card);
  });

  // Recent tasks
  const recentSection = createElement('div', { className: 'recent-tasks-section' });
  const recentTitle = createElement('h2', {}, 'Recent Tasks');
  const recentList = createElement('div', { className: 'recent-tasks-list' });

  const recentTasks = state.tasks.slice(0, 5);
  if (recentTasks.length === 0) {
    recentList.appendChild(createElement('div', { className: 'empty-state' }, 
      'No tasks yet. Create your first task!'));
  } else {
    recentTasks.forEach(task => {
      const taskItem = createElement('div', { className: 'task-item-preview' });
      const taskTitle = createElement('span', { className: 'task-title' }, task.title || 'Untitled');
      const taskStatus = createElement('span', { 
        className: `task-status ${(task.status || 'pending').toLowerCase()}` 
      }, task.status || 'Pending');
      
      taskItem.appendChild(taskTitle);
      taskItem.appendChild(taskStatus);
      recentList.appendChild(taskItem);
    });
  }

  recentSection.appendChild(recentTitle);
  recentSection.appendChild(recentList);

  container.appendChild(header);
  container.appendChild(statsGrid);
  container.appendChild(recentSection);
  
  return container;
}

// Task Manager Component
function createTaskManager(state) {
  const container = createElement('div', { className: 'task-manager' });
  
  const header = createElement('div', { className: 'task-header' });
  const title = createElement('h1', {}, 'Task Manager');
  header.appendChild(title);

  // Create task form
  const createForm = createElement('div', { className: 'create-task-form' });
  const formTitle = createElement('h3', {}, 'Create New Task');
  
  const titleInput = createElement('input', {
    type: 'text',
    id: 'task-title',
    placeholder: 'Task title...',
    required: true
  });
  
  const typeInput = createElement('input', {
    type: 'text',
    id: 'task-type',
    placeholder: 'Task type/category...'
  });

  const descriptionInput = createElement('textarea', {
    id: 'task-description',
    placeholder: 'Task description (optional)...',
    rows: 3
  });

  const createBtn = createElement('button', {
    className: 'primary-btn',
    onClick: async () => {
      const title = document.getElementById('task-title').value.trim();
      const type = document.getElementById('task-type').value.trim();
      const description = document.getElementById('task-description').value.trim();
      
      if (!title) {
        AppState.showNotification('Task title is required', 'error');
        return;
      }
      
      const taskData = { title };
      if (type) taskData.type = type;
      if (description) taskData.description = description;
      
      await AppState.createTask(taskData);
      
      // Clear form
      document.getElementById('task-title').value = '';
      document.getElementById('task-type').value = '';
      document.getElementById('task-description').value = '';
    }
  }, 'Create Task');

  createForm.appendChild(formTitle);
  createForm.appendChild(titleInput);
  createForm.appendChild(typeInput);
  createForm.appendChild(descriptionInput);
  createForm.appendChild(createBtn);

  // Filters
  const filtersSection = createElement('div', { className: 'task-filters' });
  
  const searchInput = createElement('input', {
    type: 'text',
    placeholder: 'Search tasks...',
    value: state.searchQuery,
    onInput: (e) => AppState.setSearchQuery(e.target.value)
  });

  const statusSelect = createElement('select', {
    value: state.statusFilter,
    onChange: (e) => AppState.setStatusFilter(e.target.value)
  });
  
  const statusOptions = ['', 'pending', 'completed', 'in_progress'];
  statusOptions.forEach(status => {
    const option = createElement('option', { value: status }, 
      status === '' ? 'All Statuses' : status.charAt(0).toUpperCase() + status.slice(1).replace('_', ' '));
    statusSelect.appendChild(option);
  });

  filtersSection.appendChild(searchInput);
  filtersSection.appendChild(statusSelect);

  // Task list
  const tasksList = createElement('div', { className: 'tasks-list' });
  
  if (state.loading) {
    tasksList.appendChild(createElement('div', { className: 'loading' }, 'Loading tasks...'));
  } else if (state.filteredTasks.length === 0) {
    tasksList.appendChild(createElement('div', { className: 'empty-state' }, 
      state.searchQuery || state.statusFilter ? 'No tasks match your filters' : 'No tasks found'));
  } else {
    state.filteredTasks.forEach(task => {
      const taskItem = createElement('div', { className: 'task-item' });
      
      const taskHeader = createElement('div', { className: 'task-item-header' });
      const taskTitle = createElement('h4', {}, task.title || 'Untitled Task');
      const taskStatus = createElement('span', { 
        className: `status-badge ${(task.status || 'pending').toLowerCase()}` 
      }, task.status || 'Pending');
      
      taskHeader.appendChild(taskTitle);
      taskHeader.appendChild(taskStatus);

      const taskMeta = createElement('div', { className: 'task-meta' });
      if (task.type) {
        taskMeta.appendChild(createElement('span', { className: 'task-type' }, `Type: ${task.type}`));
      }
      if (task.id) {
        taskMeta.appendChild(createElement('span', { className: 'task-id' }, `ID: ${task.id}`));
      }

      const taskActions = createElement('div', { className: 'task-actions' });
      
      const editBtn = createElement('button', {
        className: 'small-btn',
        onClick: () => {
          const newTitle = prompt('Enter new title:', task.title || '');
          if (newTitle && newTitle.trim()) {
            AppState.updateTask(task.id, { ...task, title: newTitle.trim() });
          }
        }
      }, 'Edit');
      
      const deleteBtn = createElement('button', {
        className: 'small-btn danger',
        onClick: () => {
          if (confirm(`Are you sure you want to delete "${task.title || 'this task'}"?`)) {
            AppState.deleteTask(task.id);
          }
        }
      }, 'Delete');

      taskActions.appendChild(editBtn);
      taskActions.appendChild(deleteBtn);

      taskItem.appendChild(taskHeader);
      if (taskMeta.children.length > 0) {
        taskItem.appendChild(taskMeta);
      }
      if (task.description) {
        taskItem.appendChild(createElement('p', { className: 'task-description' }, task.description));
      }
      taskItem.appendChild(taskActions);
      
      tasksList.appendChild(taskItem);
    });
  }

  container.appendChild(header);
  container.appendChild(createForm);
  container.appendChild(filtersSection);
  container.appendChild(tasksList);
  
  return container;
}

// Profile Component
function createProfile(state) {
  const container = createElement('div', { className: 'profile' });
  
  const header = createElement('h1', {}, 'Profile & Settings');
  
  const userSection = createElement('div', { className: 'profile-section' });
  const userTitle = createElement('h2', {}, 'Account Information');
  const userEmail = createElement('p', {}, `Email: ${state.user?.email || 'N/A'}`);
  
  userSection.appendChild(userTitle);
  userSection.appendChild(userEmail);

  const sessionSection = createElement('div', { className: 'profile-section' });
  const sessionTitle = createElement('h2', {}, 'Session Management');
  
  const refreshBtn = createElement('button', {
    className: 'secondary-btn',
    onClick: () => AppState.refreshToken()
  }, 'Refresh Token');
  
  const logoutAllBtn = createElement('button', {
    className: 'danger-btn',
    onClick: () => {
      if (confirm('This will log you out from all devices. Continue?')) {
        AppState.logoutAllDevices();
      }
    }
  }, 'Logout All Devices');

  const buttonGroup = createElement('div', { className: 'button-group' }, refreshBtn, logoutAllBtn);
  
  sessionSection.appendChild(sessionTitle);
  sessionSection.appendChild(buttonGroup);

  container.appendChild(header);
  container.appendChild(userSection);
  container.appendChild(sessionSection);
  
  return container;
}

// Notifications Component
function createNotifications(state) {
  const container = createElement('div', { className: 'notifications' });
  
  state.notifications.forEach(notification => {
    const notif = createElement('div', { 
      className: `notification ${notification.type}` 
    }, notification.message);
    container.appendChild(notif);
  });
  
  return container;
}

// Main App Component
function createApp(state) {
  const app = createElement('div', { className: 'app' });
  
  // Add notifications
  if (state.notifications.length > 0) {
    app.appendChild(createNotifications(state));
  }

  if (!state.isAuthenticated) {
    app.appendChild(createAuthForm(state));
    return app;
  }

  // Authenticated UI
  const nav = createNavigation(state);
  if (nav) app.appendChild(nav);

  const main = createElement('main', { className: 'main-content' });
  
  switch (state.currentView) {
    case 'dashboard':
      main.appendChild(createDashboard(state));
      break;
    case 'tasks':
      main.appendChild(createTaskManager(state));
      break;
    case 'profile':
      main.appendChild(createProfile(state));
      break;
    default:
      main.appendChild(createDashboard(state));
  }
  
  app.appendChild(main);
  return app;
}

// =============================================================================
// APP INITIALIZATION
// =============================================================================

let currentApp = null;

function render() {
  const state = AppState.getState();
  const newApp = createApp(state);
  
  const appContainer = document.getElementById('app');
  if (currentApp) {
    appContainer.removeChild(currentApp);
  }
  
  appContainer.appendChild(newApp);
  currentApp = newApp;
}

// Subscribe to state changes
AppState.subscribe(render);

// Initialize app
document.addEventListener('DOMContentLoaded', async () => {
  await AppState.init();
  render();
});