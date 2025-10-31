# 🚀 Docker Eğitimi (10 Dakika)

## 🎯 Amaç

Docker’ın ne olduğunu, neden kullanıldığını ve projelerde nasıl kullanıldığını öğrenmek.  
Bu eğitim sonunda:

- Docker ve container kavramlarını anlayacak,
- Basit bir Java uygulamasını containerize edebilecek,
- `docker-compose` kavramını tanıyor olacaksın.

⏱️ Süre: 10 dakika

---

## 📍 Bölüm 1 (3 dk) – Docker Nedir?

### 🧩 Temel Tanım:
Docker, uygulamanın tüm çalışma ortamını (kod, kütüphane, bağımlılıklar, sistem ayarları) tek bir paket (container) içinde toplar.  
> “Bir yerde çalışıyorsa, her yerde çalışır!”

| Klasik Yaklaşım | Docker Yaklaşımı |
|------------------|------------------|
| Uygulama + sistem kurulumu gerekir | Tüm ortam tek image’ta hazır |
| “Bende çalışıyor ama sende çalışmıyor” | Aynı image her yerde aynı sonucu verir |

### 🧠 Kavramlar:
- **Image:** Uygulamanın ve ortamının hazır halidir (örneğin bir kalıp).  
- **Container:** Image’ın çalışan halidir (örneğin kalıptan çıkan ürün).  
- **Dockerfile:** Image nasıl oluşturulacak, onu tanımlar.  
- **Docker Hub:** Hazır imajların (örneğin openjdk, nginx) bulunduğu depo.  
- **Port:** Container içindeki servise dışarıdan erişebilmemizi sağlar (örn. 8080).  
- **Volume:** Verilerin container silinse bile kalıcı olmasını sağlar.

---

## 📍 Bölüm 2 (5 dk) – Uygulamanın Containerize Edilmesi

### 🛠️ 1. Dockerfile Nedir?
`Dockerfile`, Docker’a **nasıl bir image oluşturacağını** adım adım anlatır.  
Aşağıda “todo-app” örneği için **en yaygın ve sade** Java tabanlı bir Dockerfile bulunuyor:

```dockerfile
# 1️⃣ Temel image: OpenJDK 17 (Java çalışma ortamı)
FROM openjdk:17-jdk-slim

# 2️⃣ Container içindeki çalışma dizini
WORKDIR /app

# 3️⃣ Jar dosyamızı kopyalıyoruz (önce maven build yapılmalı)
COPY target/*.jar app.jar

# 4️⃣ Uygulamanın dış dünyaya açılacağı port
EXPOSE 8080

# 5️⃣ Container başlarken çalışacak komut
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 💡 Açıklama:
- `FROM` → hangi taban ortamdan başlayacağımızı belirtir.  
- `WORKDIR` → komutların çalışacağı dizindir.  
- `COPY` → yerel dosyayı container içine kopyalar.  
- `EXPOSE` → container içindeki portu dış dünyaya açar.  
- `ENTRYPOINT` → container başladığında çalışacak ana komutu belirtir.

---

### 🧩 2. Build ve Run Komutları

```bash
# 1️⃣ Jar oluştur
mvn clean package -DskipTests

# 2️⃣ Docker image oluştur
docker build -t todo-app .

# 3️⃣ Container çalıştır
docker run -p 8080:8080 todo-app
```

> `-p 8080:8080` → Container’daki 8080 portunu bilgisayarımızdaki 8080’e yönlendirir.

🌐 **Test:**
Tarayıcıdan kontrol et:  
`http://localhost:8080/api/test/db`

---

## 📍 Bölüm 3 (2 dk) – Docker Compose Kavramı

### 🧩 Compose Nedir?
Bir projede genelde sadece uygulama olmaz, veritabanı (Postgres, Redis vs.) da gerekir.  
Her servisi tek tek `docker run` ile başlatmak yerine, **tek bir YAML dosyasında** tanımlarız.

#### Örnek `docker-compose.yml`
```yaml
version: "3.8"
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: todo
      POSTGRES_USER: todo
      POSTGRES_PASSWORD: secret
```

### 💡 Komutlar:
```bash
# Tüm servisi ayağa kaldır
docker compose up --build

# Arka planda çalıştırmak için
docker compose up -d
```

> Artık tek komutla uygulama + veritabanı birlikte ayağa kalkar.  
> CI/CD ortamlarında da genellikle bu yapı kullanılır.

---

## 🧠 Ekstra: Bilinmesi Gereken Temel Kavramlar

| Kavram | Açıklama |
|--------|-----------|
| **Image** | Uygulama ve ortamın paketlenmiş hali |
| **Container** | Image’ın çalışan hali |
| **Dockerfile** | Image oluşturmak için talimat dosyası |
| **Volume** | Verilerin kalıcı olması için kullanılan disk alanı |
| **Network** | Container’ların birbirini görmesini sağlar |
| **Compose** | Birden fazla container’ı tek dosyada yönetmemizi sağlar |
| **Registry / Hub** | Image’ların saklandığı depo (örneğin Docker Hub) |

---

🧩 ChatGPT Prompt:
```
🚀 Bana 10 dakikalık bir "Docker Eğitimi" hazırla.

🎯 İçerik:
- Docker’ın ne olduğu, neden kullanıldığı ve projelerde nasıl çalıştığı.
- "todo-app" adında basit bir Java uygulamasının containerize edilme örneği.
- `Dockerfile`, `Image`, `Container`, `Volume`, `Port`, `Docker Hub`, `docker-compose` kavramlarının açıklamaları.
- En yaygın ve sade bir Java Dockerfile örneği dahil olsun.
- `docker build`, `docker run` ve `docker compose up` komutlarının kullanımı adım adım anlatılsın.
- 3 bölüm halinde yapılandır: “Docker Nedir”, “Containerize Etme”, “Compose Kavramı”.
```