# 🎓 PostgreSQL + Flyway Eğitimi (20 dakika)

## 🎯 Amaç

Öğrencilerin ilişkisel veritabanı (RDBMS) mantığını kavraması, PostgreSQL’i kullanarak veri modelini anlaması ve Flyway ile veritabanı versiyonlamayı öğrenmesi.

⏱️ **Süre:** 20 dakika

---

## 📍 Bölüm 1 (5 dk) – PostgreSQL ve RDS Mantığı

### 🔹 PostgreSQL Nedir?
PostgreSQL, **RDBMS (Relational Database Management System)**’tir.  
Veri tablolarda saklanır: `table → row → column`.  
Her tablo ilişkiler üzerinden bağlanabilir (örneğin: `User ↔ Board ↔ Todo`).

### 🔹 TODO Projesinde Neden PostgreSQL?
- Kalıcı veri depolamak için güvenilir bir çözüm  
- Güçlü ilişki desteği (foreign key, join)  
- **Open-source**, **ACID** uyumlu, güçlü constraint ve index desteği  
- Spring Boot ile kolay entegre olur  

### 🔹 ACID Nedir?
PostgreSQL gibi ilişkisel veritabanları **ACID prensiplerini** sağlar:

| Harf | Kavram | Açıklama |
|------|---------|----------|
| **A – Atomicity** | Atomiklik | Her işlem tamamen yapılır ya da hiç yapılmaz. |
| **C – Consistency** | Tutarlılık | Veritabanı her zaman geçerli bir durumda kalır (ör. constraint’ler korunur). |
| **I – Isolation** | İzolasyon | Eşzamanlı işlemler birbirini etkilemez. |
| **D – Durability** | Kalıcılık | İşlem tamamlandığında veri diske kalıcı olarak yazılır. |

💡 Bu yüzden PostgreSQL, özellikle finansal veya kritik sistemlerde tercih edilir.

### 🔹 RDS Karşılaştırması

| Tür | Örnek | Yapı | Avantaj | Dezavantaj | Kullanım Alanı |
|------|--------|--------|-------------|----------------|----------------|
| **RDS (Relational Database Service)** | PostgreSQL, MySQL, MariaDB | Tablo, sütun, satır | Veri bütünlüğü, güçlü sorgu dili (SQL), ilişkiler | Şemalı yapı, esneklik az | Finans, kullanıcı verisi, ilişkili kayıtlar |
| **NoSQL (Document)** | MongoDB, DynamoDB | JSON doküman | Esnek yapı, hızlı prototipleme | Veri tutarlılığı zayıf | Log, analitik, cache |
| **Key-Value Store** | Redis | Anahtar-değer | Çok hızlı erişim | Karmaşık sorgular yok | Oturum, cache |
| **Columnar / Wide Column** | Cassandra | Sütun bazlı | Büyük veri performansı | Karmaşık yapı | IoT, veri analizi |
| **Search / Analytics Engine** | OpenSearch, Elasticsearch | Ters indeks | Full-text search, analitik sorgular | Transaction yok, veri tutarlılığı zayıf | Log analizi, arama altyapısı |
| **Graph DB** | Neo4j | Düğümler / ilişkiler | İlişki analizi kolay | Az destekli | Sosyal ağ, öneri sistemleri |

💡 **RDS**, bu türlerin *managed* (yönetilen) versiyonudur.  
AWS RDS veya Google Cloud SQL, PostgreSQL gibi ilişkisel veritabanlarını yönetir — yedekleme, ölçekleme, bakım işlemlerini senin yerine yapar.

### SQL Örneği:
```sql
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL
);
```

---

## 📍 Bölüm 2 (7 dk) – Spring Boot Bağlantısı

### 🔹 application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todo_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=validate
```

### 🔹 @Entity Örneği
```java
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    private String email;
    private String password;
}
```

🔸 **Test:** Basit bir Repository çağrısı ya da `/api/test/db` endpoint’i ile bağlantıyı doğrula.

---

## 📍 Bölüm 3 (8 dk) – Flyway Migration

### 🔹 Problem
Veritabanı değişikliklerini manuel yapmak risklidir (örneğin tabloyu elle değiştirmek versiyon karmaşası yaratır).

### 🔹 Çözüm: Flyway
Flyway, **versiyonlu SQL dosyaları** ile veritabanını otomatik yönetir.

#### Maven dependency
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

#### Migration dosyası
`src/main/resources/db/migration/V1__create_user_table.sql`
```sql
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL
);
CREATE INDEX idx_users_email ON users(email);
```

#### Çalıştırınca:
- Flyway `flyway_schema_history` tablosunu oluşturur.  
- Migration’ı otomatik uygular.  

#### Yeni versiyon ekleme:
`V2__add_todo_table.sql`  
Rollback gerekirse, eski migration silinmez, yeni bir *fix migration* eklenir.

#### Faydalı komutlar:
```bash
mvn flyway:info     # Migration geçmişini gösterir
mvn flyway:migrate  # Eksik migration'ları uygular
```

---

## 💬 Özet – Neden Flyway?
✅ Versiyon kontrolü sağlar  
✅ Takım çalışmasında veritabanı senkronizasyonunu kolaylaştırır  
✅ CI/CD pipeline’larında otomatik migration yapılabilir

---

🧩 ChatGPT Prompt:
```
PostgreSQL + Flyway Eğitimi (20 dakika) için markdown formatında bir eğitim dökümanı hazırla. 
İçerik: 
PostgreSQL ve RDS Mantığı 
- PostgreSQL nedir, neden kullanılır 
- ACID kavramı 
- RDS vs NoSQL karşılaştırma tablosu 
- Basit CREATE TABLE örneği 
- spring-boot bağlantısı 
- @Entity örneği 
Flyway Migration
- Neden gerekli olduğu 
- Maven dependency örneği 
- V1__create_user_table.sql örneği 
- flyway_schema_history tablosu ve mvn komutları (info, migrate)
```
