# 🧩 Tasky – Full-Stack Task Management Application

**Tasky** is a full-stack task management platform designed to help teams and individuals organize, track, and complete their work efficiently. It provides a modern UI, secure authentication, and a cloud-ready backend — all built within a monorepo architecture.

---

## 🚀 Features

* 🔐 **Authentication** – JWT-based login & registration (Spring Security)
* 🧾 **Boards & Tasks** – Create task boards and manage tasks with CRUD operations
* 🗑️ **Soft Delete** – Tasks and boards are never lost, only hidden
* 💌 **Email Reset** – Forgot password flow via email tokens
* ☁️ **CI/CD** – Automated build & deploy pipeline with GitHub Actions
* 🐳 **Dockerized** – Local dev and cloud environments are containerized
* 🌐 **Cloud Run Ready** – Backend deploys to Google Cloud Run; frontend to GitHub Pages

---

## 🧠 Tech Stack

| Layer          | Technology                     | Description                                     |
| -------------- | ------------------------------ | ----------------------------------------------- |
| **Frontend**   | React 18, Vite, MUI            | Single-page app with Material Design components |
| **Backend**    | Java 25, Spring Boot 3.5       | RESTful API and authentication services         |
| **Database**   | PostgreSQL                     | Persistent relational storage                   |
| **Migrations** | Flyway                         | Versioned schema migrations                     |
| **Security**   | Spring Security + JWT          | Authentication and authorization                |
| **DevOps**     | Docker, Docker Compose         | Environment standardization                     |
| **CI/CD**      | GitHub Actions                 | Continuous integration and deployment           |
| **Cloud**      | Google Cloud Run, GitHub Pages | Production-ready hosting                        |

---

## 📂 Project Structure

```
task-manager/
├── backend/                # Spring Boot application
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/               # React + Vite + MUI application
│   ├── src/
│   ├── package.json
│   └── vite.config.js
│
├── docker-compose.local.yml
├── docker-compose.cloud.yml
├── .github/
│   └── workflows/
│       ├── ci-tests.yml
│       ├── ci-static-analysis.yml
│       └── cd-deploy.yml
│
├── .env.dist
├── .gitignore
└── README.md
```

---

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

Duplicate the `.env.dist` file and fill in real values:

```bash
cp .env.dist .env
```

Then, inside `/frontend/.env.dist`:

```
VITE_API_BASE_URL=http://localhost:8080/api
```

### 4️⃣ Run Locally with Docker

```bash
docker-compose -f docker-compose.local.yml up --build
```

### 5️⃣ Manual Run (without Docker)

**Backend:**

```bash
cd backend
./mvnw spring-boot:run
```

**Frontend:**

```bash
cd frontend
npm install
npm run dev
```

Access app: [http://localhost:3000](http://localhost:3000)

---

## ☁️ Deployment (CI/CD Overview)

GitHub Actions automates the following:

1. **`ci-tests.yml`** – Runs tests on pull requests to `main`
2. **`ci-static-analysis.yml`** – Runs SonarCloud / code quality checks
3. **`cd-deploy.yml`** – On merge to main:

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
git checkout -b feature/add-task-endpoint
# make changes
git add .
git commit -m "feat(api): add task creation endpoint"
git push origin feature/add-task-endpoint
```

Then open a Pull Request into `main`.

> ⚠️ Direct pushes to `main` are disabled — all changes must come through a PR.

---

## 🧱 Contributing

Contributions are welcome! Please read the [CONTRIBUTING.md](CONTRIBUTING.md) file for branch, PR, and coding standards.

---

## 🧑‍🤝‍🧑 Contributors

| Name          | Role                           |
| ------------- | ------------------------------ |
| Yeager Ereren | Backend Developer / Maintainer |
| Team Members  | Frontend & DevOps              |

---

## 📜 License

MIT License – see [`LICENSE`](LICENSE) for details.

---

