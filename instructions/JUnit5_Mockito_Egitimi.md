# 🧩 JUnit 5 + Mockito Eğitimi (30 dakika)

🎯 **Amaç:**  
Backend kodunu test edebilmek, hataları erken fark etmek, CI pipeline’da merge öncesi kalite kontrolü sağlamak.

⏱️ **Süre:** 30 dakika  

---

## 📍 Bölüm 1 (5 dk) – Neden Test?

- “Çalışıyor gibi görünen” kod yerine **doğrulanmış kod**.
- Gerçek projede CI pipeline (GitHub Actions) testleri geçmeden **merge** yapılamaz.
- **JUnit 5:** Java için modern test framework’ü (Spring Boot Starter Test ile gelir).
- **Mockito:** bağımlılıkları izole ederek unit testleri kolaylaştırır.

**Test türleri:**

| Tür | Amaç | Kapsam | Örnek |
|-----|------|--------|-------|
| Unit Test | Tek sınıfın davranışı | İzole | `UserService` |
| Integration Test | Katmanların birlikte çalışması | Geniş | `Controller + DB` |
| End-to-End Test | Gerçek sistem akışı | Tüm sistem | `API + DB + Auth` |

---

## 📍 Bölüm 2 (10 dk) – Basit Unit Test (Mockito ile)

**Dependency:**  
`spring-boot-starter-test` (JUnit 5 + Mockito dahil)

**Servis örneği:**

```java
@Service
public class CalculatorService {
    public int add(int a, int b) { return a + b; }
}
```

**Test sınıfı (JUnit 5 + Mockito):**

```java
@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    private final CalculatorService calculator = new CalculatorService();

    @Test
    void add_shouldReturnCorrectSum() {
        int result = calculator.add(2, 3);
        assertEquals(5, result);
    }
}
```

**Komut:**
```bash
mvn test
```

✅ Başarılı test → “BUILD SUCCESS”  
❌ Hatalı beklenen değer → “AssertionFailedError”

---

## 📍 Bölüm 3 (15 dk) – Controller Testleri (Integration via MockMvc)

Controller testleri genellikle iki şekilde yapılır:

### 🔹 1. Hafif Integration Test – Sadece Web Katmanı

```java
@WebMvcTest(UserController.class)
class UserControllerWebLayerTest {

    @Autowired MockMvc mockMvc;

    @Test
    void shouldReturn200() throws Exception {
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk());
    }
}
```

🧠 Bu test: sadece `UserController` ve ilgili web katmanını yükler.  
Repository veya servisleri mocklamak gerekir.

---

### 🔹 2. Tam Integration Test – Base Class ile (Gerçek Context)

Gerçek sistem bileşenleriyle test için `@SpringBootTest` + `@AutoConfigureMockMvc` kullanılır.  
Bu yapı, projede genellikle **BaseIntegrationTest** üzerinden kalıtılır.

```java
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {
    @Autowired protected MockMvc mockMvc;
}
```

Kullanımı:

```java
class UserControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk());
    }
}
```

➡️ **Fark:**  
- `@WebMvcTest`: yalnızca web katmanı, hızlı.  
- `@SpringBootTest`: tüm context (DB, security dahil), yavaş ama tam sistem testi.  
CI pipeline’da kritik endpoint’ler bu seviyede test edilir.

---

🧩 ChatGPT Prompt:
```
JUnit 5 + Mockito eğitimi (30 dakika) için markdown formatında bir eğitim dokümanı hazırla.

İçerik:

Amaç: backend kodunu test edebilmeyi, hataları erken fark etmeyi öğretmek
Bölüm 1 (5 dk): neden test yazılır, JUnit 5 ve Mockito tanıtımı, test türleri tablosu (unit vs integration vs end-to-end)
Bölüm 2 (10 dk): basit unit test örneği (CalculatorService, @ExtendWith(MockitoExtension.class)), mvn test çıktısı açıklaması
Bölüm 3 (15 dk): controller testleri — @WebMvcTest ve @SpringBootTest + @AutoConfigureMockMvc farkını örnek kodlarla açıkla, base integration test yaklaşımını göster
```
