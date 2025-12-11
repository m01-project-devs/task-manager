# 🧑‍💻 Frontend Learning Path

*(Based on React + Vite + MUI)*

This document is prepared to help team members **quickly learn frontend technologies**.  
Our project uses React 18, Vite, and Material UI (MUI).

---

## 1️⃣ React 18 — Basic Concepts

**Goal:** Learn to work with components, JSX, props, state, and hooks.

* 📘 **Official Documentation:** [react.dev](https://react.dev)
* 🧑‍💻 **Tutorial:** [Intro to React – React Official Tutorial](https://react.dev/learn)
* ⚙️ **Hooks (useState, useEffect…):** [Hooks Introduction](https://react.dev/learn/hooks-intro)

**Practical Instructions:**

* Create functional components  
* Make the UI dynamic using state  
* Practice passing data with props  

---

## 2️⃣ Vite + Project Structure

**Goal:** Understand the structure of a React project created with Vite and learn to work with the dev server.

* 🌐 **Vite Official Documentation:** [vitejs.dev](https://vitejs.dev/)
* ⚡ **React + Vite Starter Guide:** [Create your first Vite project](https://vitejs.dev/guide/#scaffolding-your-first-vite-project)

**Example project structure:**

```
frontend/
  ├── src/
  │     ├── components/
  │     ├── pages/
  │     ├── App.jsx
  │     └── main.jsx
  ├── .env
  ├── package.json
  └── README.md
```

**Practical Instructions:**

* Run `npm install` → `npm run dev` to start the dev server  
* Review the `src/components` and `src/pages` folders  
* Test using `.env` to configure API_URL  

---

## 3️⃣ Material UI (MUI)

**Goal:** Learn using UI components, theming, and CssBaseline.

* 🎨 **MUI Getting Started:** [mui.com getting started](https://mui.com/material-ui/getting-started/overview/)
* 🔘 **Component Examples (Button, AppBar, …):** [MUI Components](https://mui.com/material-ui/react-button/)
* 🎨 **Theming & Customization:** [MUI Theming](https://mui.com/material-ui/customization/theming/)

**Practical Instructions:**

* Try `AppBar`, `Button`, `Typography`, `Container` components  
* Add `ThemeProvider` and `CssBaseline`  
* Apply background color, container sizing, and color customizations  

---

## 4️⃣ Environment Variables & API URL

**Goal:** Configure backend URLs using `.env` instead of hard-coding them.

* 🔧 **Vite Env Documentation:** [vitejs.dev — Env and Modes](https://vitejs.dev/guide/env-and-mode.html)

**Practical Instructions:**

1. Create `frontend/.env`:

```env
VITE_API_URL=http://localhost:8080/api
```

2. Use it in React:

```js
const API_BASE = import.meta.env.VITE_API_URL;
```

3. Separate dev and production environments — avoid hard-coded URLs.

---

## 5️⃣ Project-Specific Structure

**Folder structure:**

```
frontend/
  ├── src/
  │     ├── components/
  │     ├── pages/
  │     ├── App.jsx
  │     └── main.jsx
  ├── .env
  ├── package.json
  └── README.md
```

**Instructions:**

* Place components inside the `components/` folder
* Place pages (e.g., HomePage) inside the `pages/` folder

---

## 🧭 Getting Started

1. Clone the repository:

```bash
git clone <repo-url>
cd frontend
```

2. Install packages:

```bash
npm install
```

3. Start the local dev server:

```bash
npm run dev
```

4. Create `.env` and set the `VITE_API_URL` → connect to backend

---
