# TaskPlanner Frontend (Minimal)

Minimal webpack-based frontend that calls backend APIs listed by the user.

Quick start:

1. Install dependencies:

```bash
cd /home/vikku/workspace/java-projects/taskplanner-frontend
npm install
```

2. Start dev server (proxies /api to http://localhost:8080):

```bash
npm start
```

Notes:
- Adjust `webpack.config.js` proxy target if your backend runs on a different host/port.
- Endpoints used: `/auth/signup`, `/auth/login`, `/auth/refresh-token`, `/auth/logout`, `/auth/logout-all-devices`, and `/api/v1/tasks/{taskId}`.
