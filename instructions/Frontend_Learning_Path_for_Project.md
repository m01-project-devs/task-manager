# 🧑‍💻 Frontend Öğrenme Yolu

*(React + Vite + MUI temelinde)*

Bu belge, ekip üyelerinin **frontend teknolojilerini hızlıca öğrenmesi** için hazırlanmıştır.
Projemiz React 18, Vite ve Material UI (MUI) kullanmaktadır.

---

## 1️⃣ React 18 — Temel Kavramlar

**Amaç:** Bileşenler (components), JSX, props, state ve hooks ile çalışmayı öğrenmek.

* 📘 **Resmî Dokümantasyon:** [react.dev](https://react.dev)
* 🧑‍💻 **Tutorial:** [Intro to React – React Official Tutorial](https://react.dev/learn)
* ⚙️ **Hooks (useState, useEffect…):** [Hooks Girişi](https://react.dev/learn/hooks-intro)

**Uygulama Talimatları:**

* Fonksiyonel bileşenler oluşturun
* State kullanarak UI’yi dinamik hale getirin
* Props ile veri aktarımını deneyin

---

## 2️⃣ Vite + Proje Yapısı

**Amaç:** Vite ile React projesinin yapısını anlamak ve dev server ile çalışmayı öğrenmek.

* 🌐 **Vite Resmî Dokümantasyon:** [vitejs.dev](https://vitejs.dev/)
* ⚡ **React + Vite Başlangıç Kılavuzu:** [İlk projenizi oluşturun](https://vitejs.dev/guide/#scaffolding-your-first-vite-project)

**Proje yapısı örneği:**

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

**Uygulama Talimatları:**

* `npm install` → `npm run dev` ile dev serveri başlatın
* `src/components` ve `src/pages` klasörlerini inceleyin
* `.env` dosyası ile API_URL kullanımını test edin

---

## 3️⃣ Material UI (MUI)

**Amaç:** UI bileşenlerini kullanmayı, theme ve CssBaseline’i öğrenmek.

* 🎨 **MUI Başlangıç:** [mui.com getting started](https://mui.com/material-ui/getting-started/overview/)
* 🔘 **Bileşenler Örneği (Button, AppBar, …):** [MUI Components](https://mui.com/material-ui/react-button/)
* 🎨 **Theming & Özelleştirme:** [MUI Theming](https://mui.com/material-ui/customization/theming/)

**Uygulama Talimatları:**

* `AppBar`, `Button`, `Typography`, `Container` bileşenlerini deneyin
* `ThemeProvider` ve `CssBaseline` ekleyin
* Arka plan (background color), container boyutu ve renk değişikliklerini uygulayın

---

## 4️⃣ Environment Variables & API URL

**Amaç:** Backend URL’lerini kodda hard-coded yapmadan `.env` ile konfigüre etmek.

* 🔧 **Vite Env Dokümantasyonu:** [vitejs.dev — Env and Modes](https://vitejs.dev/guide/env-and-mode.html)

**Uygulama Talimatları:**

1. `frontend/.env` dosyasını oluşturun:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

2. React kodunda kullanın:

```js
const API_BASE = import.meta.env.VITE_API_BASE_URL;
```

3. Dev ve production ortamlarını ayırt edin — URL hard-coded olmasın

---

## 5️⃣ Projeye Özel Yapı

**Klasör yapısı:**

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

**Talimatlar:**

* Bileşenleri `components/` içine koyun
* Sayfaları (HomePage gibi) `pages/` içine koyun

---

## 🧭 Başlangıç (Getting Started)

1. Repo’yu klonlayın:

```bash
git clone <repo-url>
cd frontend
```

2. Paketleri yükleyin:

```bash
npm install
```

3. Local dev serveri başlatın:

```bash
npm run dev
```

4. `.env` oluşturup `VITE_API_URL` değerini ayarlayın → Backend ile bağlantıyı kurun

---
