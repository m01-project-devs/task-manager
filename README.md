
# 🧩 Tasky – Full-Stack Task Management Application

**Tasky** is a full-stack task management platform designed to help teams and individuals organize, track, and complete their work efficiently. It provides a modern UI, secure authentication, and a cloud-ready backend — all built within a monorepo architecture.

Tasky is a web-based application designed to:
- Enable users to create and manage tasks and task boards
- Provide secure authentication with JWT-based login
- Offer a modern, user-friendly interface for task management
- Ensure data persistence with PostgreSQL
- Support both local development and Docker-based deployment

## 🚀 Features

* 🔐 **Authentication** – JWT-based login & registration (Spring Security)
* 🧾 **Boards & Tasks** – Create task boards and manage tasks with CRUD operations
* 🗑️ **Soft Delete** – Tasks and boards are never lost, only hidden
* 💌 **Email Reset** – Forgot password flow via email tokens
* ☁️ **CI/CD** – Automated build & deploy pipeline with GitHub Actions
* 🐳 **Dockerized** – Local dev and cloud environments are containerized
* 🌐 **Cloud Run Ready** – Backend deploys to Google Cloud Run; frontend to GitHub Pages

## 🧠 Tech Stack

| Layer          | Technology                     | Description                                     |
| -------------- |--------------------------------| ----------------------------------------------- |
| **Frontend**   | React 18, Vite, MUI            | Single-page app with Material Design components |
| **Backend**    | Java 21, Spring Boot 3.5       | RESTful API and authentication services         |
| **Database**   | PostgreSQL                     | Persistent relational storage                   |
| **Migrations** | Flyway                         | Versioned schema migrations                     |
| **Security**   | Spring Security + JWT          | Authentication and authorization                |
| **DevOps**     | Docker, Docker Compose         | Environment standardization                     |
| **CI/CD**      | GitHub Actions                 | Continuous integration and deployment           |
| **Cloud**      | Google Cloud Run, GitHub Pages | Production-ready hosting

## 📂 Project Structure

```
task-manager/
├── backend/                   # Spring Boot application
│   ├── src/
│   │   ├── main/java/         # Java source code
│   │   └── main/resources/    # Configuration and migrations
│   └── pom.xml
├── frontend/                  # React + Vite + MUI application
│   ├── src/
│   │   ├── components/        # React components
│   │   ├── pages/             # Page components
│   │   └── api/               # API integration
│   └── package.json
│   └── vite.config.js
├── docker-compose.local.yml   # Docker Compose configuration
├── docker-compose.cloud.yml
├── .github/
│   └── workflows/
│       ├── ci-tests.yml
│       ├── ci-static-analysis.yml
│       └── cd-deploy.yml
├── .env.dist                  # Environment variables
└── run-docker.sh              # Docker startup script
```

## ⚙️ Local Development Setup

### 1️⃣ Prerequisites

Make sure the following are installed:

* [Node.js 20+](https://nodejs.org)
* [Java 21+](https://adoptium.net)
* [Docker Desktop](https://www.docker.com)
* [Git](https://git-scm.com)

### 2️⃣ Clone the Repository

```bash
git clone https://github.com/m01-project-devs/task-manager.git
cd task-manager
```

### 3️⃣ Environment Variables

**Important: All credentials must be in `.env` file only (never committed to Git)**

1. Copy the template and add your credentials:
   ```bash
   cp .env.dist .env
   ```

2. Edit `.env` and replace `CHANGE_ME` with actual values
3. Set `DB_HOST` based on your development mode:
   - Backend in Docker: `DB_HOST=postgres`
   - Backend running locally: `DB_HOST=localhost:5432`

The `.env` file contains helpful comments explaining each setup.

### 4️⃣ Run Locally

### Option 1: All Services in Docker (Full Docker Setup)

1. Ensure `.env` has `DB_HOST=postgres`
2. Run all services:
   ```bash
   ./run-docker.sh all
   ```

3. Access:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - Database: localhost:5432

### Option 2: Only Database in Docker (Local Backend + Frontend)

1. Update `.env` to set `DB_HOST=localhost:5432`
2. Run only PostgreSQL:
   ```bash
   ./run-docker.sh db
   ```

3. Run backend locally (in debug mode):
   ```bash
   cd backend
   ./mvnw spring-boot:run
   # Or run from your IDE with debug configuration
   ```

4. Run frontend locally:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

### Option 3: Database + Backend in Docker (Local Frontend)

1. Ensure `.env` has `DB_HOST=postgres`
2. Run PostgreSQL and backend:
   ```bash
   ./run-docker.sh backend-db
   ```

3. Run frontend locally (in debug mode):
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

### Option 4: Database + Frontend in Docker (Local Backend)

1. Update `.env` to set `DB_HOST=localhost:5432`
2. Run PostgreSQL and frontend:
   ```bash
   ./run-docker.sh frontend-db
   ```

3. Run backend locally (in debug mode):
   ```bash
   cd backend
   ./mvnw spring-boot:run
   # Or run from your IDE with debug configuration
   ```

## Quick Reference
| Scenario | Docker Services | Local Services | DB_HOST in .env | Command |
|----------|----------------|----------------|-----------------|---------|
| **Full Docker** | postgres + backend + frontend | none | `postgres` | `./run-docker.sh all` |
| **DB Only** | postgres | backend + frontend | `localhost:5432` | `./run-docker.sh db` |
| **DB + Backend** | postgres + backend | frontend | `postgres` | `./run-docker.sh backend-db` |
| **DB + Frontend** | postgres + frontend | backend | `localhost:5432` | `./run-docker.sh frontend-db` |
| **No Docker** | none | postgres + backend + frontend | `localhost:5432` | Manual setup |

### Option 5: Local Development (No Docker)

#### Backend

1. Install and run PostgreSQL locally on port 5432
2. Update `.env` to set `DB_HOST=localhost:5432`
3. Navigate to backend directory:
   ```bash
   cd backend
   ```


4. Build and run the backend:
   ```bash
   ./mvnw clean spring-boot:run
   ```

   The backend will start on http://localhost:8080

#### Frontend

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will start on [http://localhost:3000](http://localhost:3000)

## Database Setup

Database migrations are handled automatically by Flyway on application startup. No manual setup is required.

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

## Stopping the Application

If running with Docker Compose:
```bash
docker-compose -f docker-compose.yml down
```

## ☁️ Deployment (CI/CD Overview)

GitHub Actions automates the following:

1. **`ci-tests.yml`** – Runs tests on pull requests to `main`
2. **`cd-deploy.yml`** – On merge to main:

   * Builds Docker image
   * Deploys backend to Cloud Run
   * Deploys frontend to GitHub Pages

> Secrets used: `GCP_SA_KEY`, `DB_URL`, `DB_USER`, `DB_PASS`, `JWT_SECRET`

---

## 🧪 Testing

* **Backend:** JUnit 5 + Mockito
* **Frontend:** Jest + React Testing Library
* **Static Analysis:** SonarCloud
* **Code Coverage Goal:** ≥70% core modules

---

## 🧰 Development Workflow

This project uses a simple branching model:

* All work is done in **feature branches**.
* Merge requests (Pull Requests) go directly into **main**.
* CI/CD automatically validates and deploys after merge.

### Example Workflow

```bash
git checkout -b feature/DEV-1-add-task-endpoint
# make changes
git add .
git commit -m "DEV-1 Add task creation endpoint"
git push origin feature/DEV-1-add-task-endpoint
```

Then open a Pull Request into `main`.

> ⚠️ Direct pushes to `main` are disabled — all changes must come through a PR.

---

## 📜 License

MIT License – see [`LICENSE`](LICENSE) for details.

## Support

For issues or questions, please open an issue in the repository.


