# Task Planner

This project is a task planner application.

## API Endpoints

Below is a summary of the API endpoints (extracted from `postman_collection/postman_collection.json`). For each endpoint you'll find the HTTP method, path, a short description, and example headers/body when applicable.

| Service | Name | Method | Path | Description | Example Headers / Body |
|---|---:|---:|---|---|---|
| taskservice | createTask | POST | /api/v1/tasks | Create a new task. Request body: task fields (taskId, name, description, taskType, taskDifficulty, taskStatus, url, notes). | Header: Authorization: Bearer <token>\nBody (JSON): {"taskId":"string","name":"string",...} |
| taskservice | getTasksByTypeAndStatus | GET | /api/v1/tasks?type={type}&status={status} | Get tasks filtered by type and status. | Header: Authorization: Bearer <token> |
| taskservice | getListOfTaskByType | GET | /api/v1/tasks/type?type={type} | Get list of tasks for a given type. | Header: Authorization: Bearer <token> |
| taskservice | deleteTaskById | DELETE | /api/v1/tasks/{taskId} | Delete a task by id. | Header: Authorization: Bearer <token> |
| taskservice | updateTaskByTaskId | POST | /api/v1/tasks/{taskId} | Update a task by id (POST used in collection). | Header: Authorization: Bearer <token> |
| taskservice | getTaskById | GET | /api/v1/tasks | Get tasks (collection had GET /api/v1/tasks) | Header: Authorization: Bearer <token> |
| authservice | signup | POST | /api/auth/signup | Register a new user. Request body: username, email, password, firstName, lastName, phoneNumber. | Header: Content-Type: application/json\nBody (JSON): {"username":"aba","email":"aba@gmail.com",...} |
| authservice | signin | POST | /api/auth/signin | Sign in and receive tokens. Body: username, password. | Body (JSON): {"username":"abz","password":"abz"} |
| authservice | refresh-token | POST | /api/auth/refreshtoken | Refresh access token using refresh token. | Body (JSON): {"refreshToken":"<token>"} |
| authservice | logout | POST | /api/auth/logout | Logout current device/session (requires Authorization header). | Header: Authorization: Bearer <token> |
| authservice | logoutAllDevices | POST | /api/auth/logout-all-devices | Logout from all devices for the current user. | Header: Authorization: Bearer <token> |

Notes:
- All endpoints listed under `taskservice` in the collection require an Authorization header (Bearer token) in the examples. Replace with a valid JWT when calling the API.
- Base URL used in the collection: http://localhost:8080
- Paths with path parameters are shown using {param} notation.

## Run this application
```bash
git clone git@github.com:viiku/task-planner.git
docker compose up
```
