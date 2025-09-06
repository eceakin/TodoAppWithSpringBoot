
```markdown
# 🚀 TodoApp - Spring Boot & React Full Stack Uygulaması

Modern, güvenli ve ölçeklenebilir bir yapılacaklar listesi uygulaması.   Spring Boot backend ve React frontend ile geliştirilmiş tam kapsamlı bir todo uygulaması.

## 📋 Özellikler

### 🔐 Kimlik Doğrulama & Yetkilendirme
- JWT tabanlı güvenli kimlik doğrulama
- Rol tabanlı erişim kontrolü
- Şifre hash'leme (BCrypt)
- Otomatik token yenileme

### 📝 Todo Yönetimi
- Kişiselleştirilmiş todo listeleri oluşturma
- Alt görevler (todo items) ekleme/düzenleme/silme
- Öncelik seviyeleri (Low, Medium, High, Urgent)
- Tamamlanma durumu takibi
- Tarih bazlı filtreleme (due date)

### 👥 Kullanıcı Yönetimi
- Kullanıcı kaydı ve girişi
- Profil yönetimi
- Kişiselleştirilmiş todo erişimi

## 🛠️ Teknoloji Stack'i

### Backend
- **Java 21** - Programlama dili
- **Spring Boot 3.5.5** - Framework
- **Spring Security** - Güvenlik
- **Spring Data JPA** - Veritabanı erişimi
- **PostgreSQL** - Veritabanı
- **JWT** - Token tabanlı kimlik doğrulama
- **Lombok** - Boilerplate kod azaltma
- **ModelMapper** - DTO mapping
- **Validation API** - Veri validasyonu

### Frontend
- **React** - UI Framework
- **JavaScript** - Programlama dili
- **Axios** - HTTP istemcisi
- **CSS3** - Styling

## 📦 Kurulum ve Çalıştırma

### Ön Gereksinimler
- Java 21 JDK
- Maven 3.6+
- PostgreSQL 12+
- Node.js 16+
- npm veya yarn

### Backend Kurulumu

1. **Repository'yi klonlayın**
```bash
git clone <repository-url>
cd todoAppSpring
```

2. **Veritabanını ayarlayın**
```sql
CREATE DATABASE todoAppSpring;
CREATE USER todo_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE todoAppSpring TO todo_user;
```

3. **Application properties'i yapılandırın**
`src/main/resources/application.properties` dosyasını düzenleyin:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todoAppSpring
spring.datasource.username=todo_user
spring.datasource.password=your_password
```

4. **Backend'i başlatın**
```bash
mvn clean install
mvn spring-boot:run
```

Backend http://localhost:8080 adresinde çalışacaktır.

### Frontend Kurulumu

1. **Frontend dizinine gidin**
```bash
cd frontend
```

2. **Bağımlılıkları yükleyin**
```bash
npm install
```

3. **Frontend'i başlatın**
```bash
npm start
```

Frontend http://localhost:3000 adresinde çalışacaktır.

## 🗃️ Veritabanı Yapısı

### Tablolar
- **users** - Kullanıcı bilgileri
- **todo_lists** - Todo listeleri
- **todo_items** - Todo öğeleri

### İlişkiler
- `User` (1) ↔ (N) `TodoList`
- `TodoList` (1) ↔ (N) `TodoItem`

## 📡 API Endpoints

### 🔐 Kimlik Doğrulama
- `POST /api/auth/register` - Yeni kullanıcı kaydı
- `POST /api/auth/login` - Kullanıcı girişi
- `GET /api/auth/me` - Mevcut kullanıcı bilgileri

### 📋 Todo Listeleri
- `GET /api/todolists` - Tüm listeleri getir
- `POST /api/todolists` - Yeni liste oluştur
- `GET /api/todolists/{id}` - ID ile liste getir
- `PUT /api/todolists/{id}` - Liste güncelle
- `DELETE /api/todolists/{id}` - Liste sil
- `GET /api/todolists/search?title={title}` - Liste ara

### ✅ Todo Öğeleri
- `GET /api/todoitems?listId={listId}` - Liste öğelerini getir
- `POST /api/todoitems` - Yeni öğe oluştur
- `PUT /api/todoitems/{id}` - Öğe güncelle
- `DELETE /api/todoitems/{id}` - Öğe sil
- `PATCH /api/todoitems/{id}/complete` - Öğeyi tamamla/geri al

### 👥 Kullanıcılar
- `GET /api/users/me` - Profil bilgileri
- `PUT /api/users/me` - Profili güncelle

## 🔧 Yapılandırma

### JWT Yapılandırması
```properties
todoAppSpring.jwt.secret=your-jwt-secret-key
todoAppSpring.jwt.expiration=86400000 # 24 saat
```

### CORS Yapılandırması
```properties
todoAppSpring.cors.allowed-origins=http://localhost:3000
todoAppSpring.cors.allowed-methods=GET,POST,PUT,DELETE,PATCH,OPTIONS
```

## 🚀 Production Deployment

### Docker ile Deployment
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/todoAppSpring-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/todoAppSpring
export SPRING_DATASOURCE_USERNAME=your-db-user
export SPRING_DATASOURCE_PASSWORD=your-db-password
export JWT_SECRET=your-production-jwt-secret
```

## 🤝 Katkıda Bulunma

1. Forklayın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit edin (`git commit -m 'Add amazing feature'`)
4. Push edin (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun


## 👨‍💻 Geliştirici

**Ece Akın**
- Email: akinecee3535@hotmail.com
- GitHub: [@eceakin](https://github.com/eceakin)


---
