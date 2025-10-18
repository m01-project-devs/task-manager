## **CONTRIBUTING.md**

### 🧭 Overview

Thank you for your interest in contributing!\
This project follows a clean and collaborative development workflow.\
All contributions---bug fixes, new features, improvements, or
documentation---are welcome.

Please take a moment to read the following guidelines before making
changes.

------------------------------------------------------------------------

### ⚙️ Project Structure

    /backend     → Spring Boot 3 (Java 25) REST API  
    /frontend    → React 18 + Vite + Material UI  
    /docker      → Docker Compose configurations for local and cloud environments

------------------------------------------------------------------------

### 🧱 1. Setup Instructions

**Clone the repository**

``` bash
git clone https://github.com/m01-project-devs/task-manager.git
cd task-manager
```

**Backend**

``` bash
cd backend
./mvnw spring-boot:run
```

**Frontend**

``` bash
cd frontend
npm install
npm run dev
```

**Optional -- Run via Docker**

``` bash
docker-compose -f docker-compose.local.yml up
```

------------------------------------------------------------------------

### 🌿 2. Branching & Workflow

1.  Always create a new branch from `main`:

    ``` bash
    git checkout -b feature/<feature-name>
    ```

2.  Use clear, concise commit messages:

        feat: add JWT token validation
        fix: correct todo deletion logic
        refactor: improve API response structure

3.  Push your branch and open a **Pull Request (PR)** once changes are
    ready.

4.  Each PR should be reviewed and approved before merging into `main`.

------------------------------------------------------------------------

### 🧪 3. Testing

**Backend Tests**

``` bash
mvn test
```

**Frontend Tests (optional)**

``` bash
npm run test
```

✅ All tests **must pass** before submitting a PR.

------------------------------------------------------------------------

### 🧹 4. Code Style & Conventions

**Backend (Java / Spring Boot)** - Follow standard Spring Boot layering
(`controller`, `service`, `repository`). - Use `@Valid` for request
validation and handle exceptions globally. - Keep business logic
isolated from controllers.

**Frontend (React / Vite)** - Prefer **functional components** and
hooks. - Use **Material UI theme** for consistent design. - Keep
components small and reusable. - Follow standard naming conventions
(camelCase for variables, PascalCase for components).

------------------------------------------------------------------------

### 🧩 5. Pull Requests

Each PR should include: - A short summary of the change. - The related
issue number (if applicable). - Clear description of what has been
added, fixed, or improved. - Verification that the code has been tested
locally.

PRs that fail CI/CD checks or automated tests will not be merged.

------------------------------------------------------------------------

### 🔒 6. Security & Environment Variables

Do not commit any sensitive data or `.env` files.\
All secrets and credentials (e.g., API keys, database passwords) must be
managed via: - Local `.env.local` file (not committed) - GitHub Secrets
for CI/CD

------------------------------------------------------------------------

### 🧭 7. Commit & Merge Rules

-   The `main` branch is **protected** and can only receive changes via
    PRs.
-   All PRs require at least one code review approval.
-   Passing CI checks (build + test + static analysis) is mandatory
    before merge.

------------------------------------------------------------------------

### 🙌 8. Code Review Principles

We aim for clean, maintainable, and readable code.\
When reviewing or commenting on PRs: - Be constructive and specific. -
Avoid personal tone; focus on the code. - Encourage consistency and best
practices.

------------------------------------------------------------------------

### 🏁 9. Continuous Integration

All commits and PRs trigger GitHub Actions workflows: 1.
**ci-tests.yml** --- runs build and test pipelines.\
2. **ci-static-analysis.yml** --- performs static code quality checks
   (e.g., SonarCloud).\
3. **cd-deploy.yml** --- deploys to Google Cloud Run & GitHub Pages upon
   successful merge.

------------------------------------------------------------------------

### 💬 10. Contact

If you have questions, suggestions, or encounter issues, open a GitHub
Issue or start a discussion thread.\
Your contributions help improve the stability, quality, and
maintainability of the project.
