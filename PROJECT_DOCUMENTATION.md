# TÀI LIỆU DỰ ÁN BIOVERSE BACKEND (EXE101)

> **Tên dự án:** Bioverse - Nền tảng học tập Khoa học Tự nhiên trực quan 3D cho học sinh THCS  
> **Mã dự án:** EXE101_Bioverse / `EXE101_group_project_BE`  
> **Phiên bản:** 0.0.1-SNAPSHOT  
> **Ngôn ngữ & Nền tảng:** Java 21 LTS | Spring Boot 4.1.0  
> **Cơ sở dữ liệu:** PostgreSQL 16 (Local / Supabase) | Redis 7 (In-memory & OTP)  
> **Lưu trữ 3D:** Cloudflare R2 (S3-compatible)  
> **AI Service:** OpenRouter API (`openai/gpt-4o-mini`) + Firebase Firestore  

---

## MỤC LỤC
1. [Tổng quan dự án](#1-tổng-quan-dự-án)
2. [Ngăn xếp công nghệ (Tech Stack)](#2-ngăn-xếp-công-nghệ-tech-stack)
3. [Cấu trúc mã nguồn (Project Structure)](#3-cấu-trúc-mã-nguồn-project-structure)
4. [Mô hình dữ liệu & Database Migrations](#4-mô-hình-dữ-liệu--database-migrations)
5. [Quy chuẩn API & Bảng mã lỗi](#5-quy-chuẩn-api--bảng-mã-lỗi)
6. [Danh sách API Endpoints chi tiết](#6-danh-sách-api-endpoints-chi-tiết)
   - [6.1. Xác thực & Tài khoản (Authentication)](#61-xác-thực--tài-khoản-authentication)
   - [6.2. Hồ sơ cá nhân (User Profile)](#62-hồ-sơ-cá-nhân-user-profile)
   - [6.3. Chuỗi học tập (User Streak)](#63-chuỗi-học-tập-user-streak)
   - [6.4. Quản trị Người dùng & Vai trò (Admin)](#64-quản-trị-người-dùng--vai-trò-admin)
   - [6.5. Mô hình 3D Sinh học (Bio Models)](#65-mô-hình-3d-sinh-học-bio-models)
   - [6.6. Lưu trữ & Streaming Asset 3D (R2 Storage)](#66-lưu-trữ--streaming-asset-3d-r2-storage)
   - [6.7. Trợ lý Trí tuệ nhân tạo (AI Tutor)](#67-trợ-lý-trí-tuệ-nhân-tạo-ai-tutor)
   - [6.8. Hệ thống Bài thi & Câu hỏi (Exam & Question)](#68-hệ-thống-bài-thi--câu-hỏi-exam--question)
7. [Các cơ chế kỹ thuật cốt lõi (Core Mechanics)](#7-các-cơ-chế-kỹ-thuật-cốt-lõi-core-mechanics)
   - [Bảo mật & Phân quyền (Security & JWT)](#bảo-mật--phân-quyền-security--jwt)
   - [Xác thực OTP Email với Redis](#xác-thực-otp-email-với-redis)
   - [AI Guardrails & Prompt Engineering](#ai-guardrails--prompt-engineering)
   - [Streaming 3D Models qua R2 Proxy](#streaming-3d-models-qua-r2-proxy)
   - [Thuật toán Điểm danh Daily Streak](#thuật-toán-điểm-danh-daily-streak)
8. [Hướng dẫn Cài đặt & Vận hành (Deployment & Setup)](#8-hướng-dẫn-cài-đặt--vận-hành-deployment--setup)
   - [Biến môi trường (.env)](#biến-môi-trường-env)
   - [Chạy môi trường Local Development](#chạy-môi-trường-local-development)
   - [Chạy kết nối Supabase Cloud](#chạy-kết-nối-supabase-cloud)
   - [Triển khai Production với Docker](#triển-khai-production-với-docker)
   - [Dữ liệu mẫu khởi tạo (Seed Accounts)](#dữ-liệu-mẫu-khởi-tạo-seed-accounts)

---

## 1. Tổng quan dự án

**Bioverse** là giải pháp công nghệ giáo dục (EdTech) hỗ trợ giảng dạy và học tập môn **Khoa học Tự nhiên (KHTN)** cho học sinh cấp Trung học Cơ sở tại Việt Nam (từ Lớp 6 đến Lớp 9), bao gồm ba phân môn: **Sinh học, Vật lý và Hóa học**.

Hệ sinh thái Bioverse giải quyết các rào cản học tập truyền thống bằng cách:
- Cung cấp **kho mô hình 3D sinh học và dụng cụ thí nghiệm tương tác trực quan**, cho phép học sinh xoay, phóng to, mổ xẻ cấu trúc sinh vật và tế bào trực tiếp trên trình duyệt.
- Tích hợp **Gia sư ảo AI (BioVerse AI Tutor)** có bộ lọc an toàn chuyên sâu (AI Guardrails) giúp giải đáp thắc mắc lý thuyết, giải bài tập và nhắc nhở an toàn phòng thí nghiệm phù hợp với tâm lý lứa tuổi học sinh THCS.
- Cung cấp **hệ thống đề kiểm tra trắc nghiệm, câu hỏi ôn luyện** theo chuẩn phân loại khối lớp và môn học.
- Tăng tính gắn kết qua cơ chế **Gamification - Chuỗi ngày học liên tục (Daily Streak)**.

---

## 2. Ngăn xếp công nghệ (Tech Stack)

| Thành phần | Công nghệ / Thư viện | Vai trò |
| :--- | :--- | :--- |
| **Runtime & Language** | Java 21 LTS | Ngôn ngữ chính, tận dụng Virtual Threads và tính năng hiện đại |
| **Framework** | Spring Boot 4.1.0, Spring MVC, Spring Security | Xây dựng RESTful API và bảo mật |
| **ORM / Data Access** | Spring Data JPA, Hibernate, PostgreSQL Driver | Tương tác cơ sở dữ liệu quan hệ |
| **Database Migration** | Flyway Core 10+ (`db/migration`) | Quản lý vòng đời và phiên bản schema database tự động |
| **Primary Database** | PostgreSQL 16 (Hỗ trợ Local & Supabase) | Lưu trữ người dùng, quyền, đề thi, mô hình sinh học, thanh toán |
| **In-Memory Cache** | Redis 7 (`spring-boot-starter-data-redis`) | Quản lý mã xác thực OTP, Blacklist Token, phiên đăng ký tạm |
| **Object Storage** | Cloudflare R2 (`software.amazon.awssdk:s3` v2) | Lưu trữ file mô hình 3D (.glb, .gltf, textures, binary assets) |
| **AI Integration** | OpenRouter API (`openai/gpt-4o-mini`) | Động cơ suy luận ngôn ngữ tự nhiên cho AI Tutor |
| **AI State / History** | Firebase Admin SDK 9.9.0 (Firestore) | Lưu trữ lịch sử hội thoại AI chat theo phiên người dùng |
| **Email Service** | Spring Boot Mail + Thymeleaf Engine | Gửi mã OTP đăng ký và khôi phục mật khẩu qua giao diện HTML |
| **Object Mapping** | MapStruct 1.5.5, Lombok | Chuyển đổi giữa Entity và DTO không làm giảm hiệu năng |
| **API Documentation** | SpringDoc OpenAPI 3.0.3 (Swagger UI) | Tự động sinh tài liệu và giao diện test API tương tác |
| **Container & CI/CD** | Docker (Multi-stage build), Docker Compose | Đóng gói ứng dụng chạy cục bộ và triển khai Production |

---

## 3. Cấu trúc mã nguồn (Project Structure)

```
EXE101_group_project_BE/
├── .env.example                       # Mẫu cấu hình biến môi trường
├── .env.prod.example                  # Mẫu cấu hình môi trường Production
├── Dockerfile                         # Build Docker đa giai đoạn (Eclipse Temurin 21)
├── docker-compose.yml                 # Khởi chạy Postgres (5434) & Redis (6380) local
├── docker-compose.prod.yml            # Khởi chạy Backend + Redis network nội bộ
├── pom.xml                            # Quản lý dependencies Maven
├── scripts/
│   └── push-db-to-supabase.sh         # Script hỗ trợ đồng bộ schema sang Supabase
└── src/
    ├── main/
    │   ├── java/com/example/exe101_bioverse/
    │   │   ├── Exe101BioverseApplication.java   # Main entry point
    │   │   ├── ai/                              # Phân hệ Gia sư ảo AI
    │   │   │   ├── client/OpenRouterClient.java # Client HTTP gọi OpenRouter API
    │   │   │   ├── controller/AiChatController.java
    │   │   │   ├── dto/                         # AiChatRequest, AiChatResponse, ChatMessage
    │   │   │   ├── guardrail/                   # Bộ lọc regex kiểm tra an toàn câu hỏi
    │   │   │   ├── repository/                  # Firebase conversation repository
    │   │   │   └── service/                     # Xử lý chat, xây dựng Prompt hệ thống
    │   │   ├── auth/                            # Phân hệ Xác thực & Người dùng
    │   │   │   ├── controller/                  # Auth, Profile, AdminUser, AdminRole
    │   │   │   ├── dto/                         # Request/Response cho Auth, Role, User
    │   │   │   ├── entity/                      # User, Role, UserSession
    │   │   │   ├── enums/                       # UserStatus, GenderType, OtpPurpose
    │   │   │   ├── mapper/                      # UserMapper, RoleMapper (MapStruct)
    │   │   │   ├── repository/                  # UserRepository, RoleRepository, UserSessionRepository
    │   │   │   ├── security/                    # JwtAuthenticationFilter, UserPrincipal, CustomUserDetailsService
    │   │   │   └── service/                     # AuthService, JwtService, OtpService, MailService...
    │   │   ├── common/                          # Cấu hình & Hạ tầng dùng chung
    │   │   │   ├── config/                      # SecurityConfig, RedisConfig, FirebaseConfig, Swagger...
    │   │   │   ├── exception/                   # AppException, ErrorCode, GlobalExceptionHandler
    │   │   │   ├── response/                    # ApiResponse, PageResponse
    │   │   │   └── seed/DataSeeder.java         # Khởi tạo tài khoản mặc định (Admin, Student)
    │   │   ├── exam/                            # Phân hệ Bài thi & Câu hỏi
    │   │   │   ├── controller/                  # Exam, Question, Answer, ExamQuestion, Images
    │   │   │   ├── dto/                         # Request/Response đề thi, câu hỏi, đáp án
    │   │   │   ├── entity/                      # Exam, Question, Answer, ExamQuestion, Images
    │   │   │   ├── enums/                       # ExamType, QuestionType, AnswerType
    │   │   │   ├── mapper/                      # Mapper cho Exam & Question
    │   │   │   ├── repository/                  # Repositories JPA
    │   │   │   └── service/                     # Nghiệp vụ tạo đề, quản lý câu hỏi
    │   │   ├── model/                           # Phân hệ Mô hình 3D Sinh học (BioModel)
    │   │   │   ├── controller/                  # BioModelController, AdminBioModelController
    │   │   │   ├── dto/                         # Create/UpdateModelRequest, ModelDetailResponse
    │   │   │   ├── entity/BioModel.java         # Entity BioModel
    │   │   │   ├── repository/BioModelRepository.java
    │   │   │   └── service/                     # BioModelService, AdminBioModelService
    │   │   ├── storage/                         # Phân hệ Lưu trữ Cloudflare R2
    │   │   │   ├── controller/ModelAssetController.java # Proxy streaming file .glb/.gltf
    │   │   │   ├── dto/ModelAssetResponse.java
    │   │   │   └── service/R2StorageService.java
    │   │   └── streak/                          # Phân hệ Điểm danh ngày học (Streak)
    │   │       ├── controller/StreakController.java
    │   │       ├── dto/response/StreakResponse.java
    │   │       ├── entity/UserStreak.java
    │   │       ├── repository/UserStreakRepository.java
    │   │       └── service/StreakService.java
    │   └── resources/
    │       ├── application.properties           # Cấu hình chính (Port, JPA, Timeout, Mail, R2, AI)
    │       ├── application-local.properties     # Cấu hình kết nối Docker Postgres nội bộ
    │       ├── application-supabase.properties  # Cấu hình kết nối Supabase Session Pooler
    │       ├── db/migration/                    # 15 file Flyway Migration (V1 -> V15)
    │       ├── templates/mail/otp.html          # Mẫu giao diện Email gửi mã OTP
    │       ├── mock_data.sql                    # Dữ liệu kiểm thử đề thi & câu hỏi
    │       └── mock_data_v2.sql                 # Dữ liệu mô hình sinh học bổ sung
    └── test/
        └── java/com/example/exe101_bioverse/    # Unit & Integration Tests (UserStreakTest...)
```

---

## 4. Mô hình dữ liệu & Database Migrations

Dự án áp dụng cơ chế quản lý cơ sở dữ liệu phiên bản tự động bằng **Flyway Migration** (`src/main/resources/db/migration`):

| File Migration | Tên Schema | Nội dung chi tiết |
| :--- | :--- | :--- |
| **`V1__init_schema.sql`** | Init Exam & Question | Bảng `exam`, `question`, `exam_question` |
| **`V2__user_auth_schema.sql`** | User & Auth | Bảng `users`, `user_sessions`, ENUMs (`user_role`, `user_status`, `gender_type`) |
| **`V3__subscription_schema.sql`** | Subscription Plans | Bảng `plans`, `user_subscriptions` (gói cước tháng, quý, năm) |
| **`V4__payment_schema.sql`** | Payment Schema | Bảng `payments` (tích hợp PayOS orderCode, transactionId, trạng thái giao dịch) |
| **`V5__content_taxonomy_schema.sql`** | Content Taxonomy | Phân cấp nội dung: `grade_levels` (Lớp 6-9) → `subjects` → `chapters` → `lessons` |
| **`V6__chemicals_schema.sql`** | Chemicals Library | Bảng `chemicals`, `chemical_reactions` (tính chất hóa học, khối lượng phân tử, màu sắc, model 3D) |
| **`V7__experiments_schema.sql`** | Virtual Experiments | Bảng `experiments`, `experiment_steps`, `experiment_equipment`, `experiment_scene_objects` |
| **`V8__bio_models_schema.sql`** | Bio Models | Bảng `bio_models` (tên khoa học, phân loại học, môi trường sống, đặc điểm, 3D asset path) |
| **`V9__exam_enhancement_schema.sql`** | Exam Enhancement | Bổ sung `subject_id`, `grade_level_id`, `duration_minutes`, `total_score` cho bảng `exam` |
| **`V10__shared_objects_schema.sql`** | Shared 3D Assets | Bảng `shared_3d_objects` (thư viện đồ dùng thí nghiệm: ống nghiệm, đèn cồn, cốc đong...) |
| **`V11__roles_table.sql`** | Dynamic Roles | Tách bảng `roles` riêng (`STUDENT`, `ADMIN`), liên kết `users.role_id` khóa ngoại |
| **`V12__seed_plans.sql`** | Seed Subscription | Khởi tạo dữ liệu các gói học viên: Gói Tháng (49k), Gói Quý (119k), Gói Năm (399k) |
| **`V13__users_grade.sql`** | User Grade | Bổ sung trường `grade` (giới hạn từ lớp 6 đến 9) cho bảng `users` |
| **`V14__add_featured_and_metadata_to_bio_models.sql`** | Bio Models Metadata | Thêm `slug`, `is_featured`, `views_count`, `grade`, `subject`, `badge_text`, `action_text` |
| **`V15__user_streaks.sql`** | Daily Streak | Bảng `user_streaks` theo dõi chuỗi ngày học liên tục, kỷ lục dài nhất, ngày điểm danh cuối |

---

## 5. Quy chuẩn API & Bảng mã lỗi

### 5.1. Cấu trúc Response chuẩn (`ApiResponse<T>`)
Tất cả các API trả về phản hồi định dạng JSON thống nhất:
```json
{
  "code": 1000,
  "message": "Thành công",
  "data": { ... }
}
```

Đối với danh sách phân trang (`PageResponse<T>`):
```json
{
  "code": 1000,
  "message": "Thành công",
  "data": {
    "content": [ ... ],
    "page": 0,
    "size": 10,
    "totalElements": 45,
    "totalPages": 5,
    "first": true,
    "last": false
  }
}
```

### 5.2. Bảng mã lỗi hệ thống (`ErrorCode`)

| Mã Code | HTTP Status | Định danh lỗi | Mô tả chi tiết |
| :---: | :---: | :--- | :--- |
| **`1000`** | `200 OK` | `SUCCESS` | Thao tác thành công |
| **`9999`** | `500 Internal Error` | `UNCATEGORIZED` | Lỗi máy chủ không xác định |
| **`1001`** | `404 Not Found` | `USER_NOT_FOUND` | Không tìm thấy người dùng |
| **`1002`** | `409 Conflict` | `EMAIL_ALREADY_EXISTS` | Email đã được đăng ký trong hệ thống |
| **`1003`** | `409 Conflict` | `PHONE_ALREADY_EXISTS` | Số điện thoại đã được đăng ký |
| **`1004`** | `401 Unauthorized` | `INVALID_CREDENTIALS` | Email hoặc mật khẩu không chính xác |
| **`1005`** | `401 Unauthorized` | `INVALID_REFRESH_TOKEN` | Refresh token không hợp lệ hoặc đã hết hạn |
| **`1006`** | `403 Forbidden` | `ACCOUNT_NOT_ACTIVE` | Tài khoản đang bị khóa hoặc chưa kích hoạt |
| **`1007`** | `401 Unauthorized` | `INVALID_ACCESS_TOKEN` | Token JWT không hợp lệ hoặc sai chữ ký |
| **`1008`** | `401 Unauthorized` | `MISSING_ACCESS_TOKEN` | Thiếu access token trong Header Authorization |
| **`1010`** | `400 Bad Request` | `OTP_INVALID` | Mã OTP không đúng |
| **`1011`** | `400 Bad Request` | `OTP_EXPIRED` | Mã OTP đã hết hạn hoặc chưa từng được gửi |
| **`1012`** | `429 Too Many Requests` | `OTP_RESEND_TOO_SOON` | Chưa hết thời gian chờ cooldown gửi lại OTP |
| **`1013`** | `429 Too Many Requests` | `OTP_MAX_ATTEMPTS` | Nhập sai OTP quá 5 lần |
| **`1014`** | `400 Bad Request` | `PENDING_REGISTRATION_EXPIRED` | Phiên đăng ký tạm đã hết hạn |
| **`1015`** | `401 Unauthorized` | `INVALID_RESET_TOKEN` | Phiên đặt lại mật khẩu không hợp lệ |
| **`1016`** | `500 Internal Error` | `EMAIL_SEND_FAILED` | Không thể gửi email qua máy chủ SMTP |
| **`1017`** | `403 Forbidden` | `CANNOT_MODIFY_OWN_ACCOUNT` | Không thể tự đổi quyền hoặc khóa chính tài khoản mình |
| **`1018`** | `409 Conflict` | `LAST_ACTIVE_ADMIN` | Không thể khóa/xóa quản trị viên cuối cùng |
| **`1019`** | `404 Not Found` | `ROLE_NOT_FOUND` | Không tìm thấy vai trò |
| **`1400`** | `400 Bad Request` | `INVALID_DATA` | Dữ liệu đầu vào vi phạm validation |
| **`1403`** | `403 Forbidden` | `ACCESS_DENIED` | Không có quyền truy cập tài nguyên |
| **`1501`** | `404 Not Found` | `MODEL_NOT_FOUND` | Không tìm thấy file hoặc thông tin mô hình 3D |
| **`1502`** | `503 Service Unavailable` | `STORAGE_NOT_CONFIGURED` | Chưa cấu hình bucket Cloudflare R2 |
| **`1503`** | `502 Bad Gateway` | `STORAGE_ERROR` | Lỗi đường truyền tải file từ Cloudflare R2 |

---

## 6. Danh sách API Endpoints chi tiết

### 6.1. Xác thực & Tài khoản (Authentication)
**Prefix:** `/api/auth`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Public | Gửi thông tin đăng ký và nhận OTP qua email |
| `POST` | `/api/auth/verify-register` | Public | Xác thực OTP đăng ký, lưu user vào DB và nhận JWT token |
| `POST` | `/api/auth/resend-otp` | Public | Gửi lại mã OTP (tuân thủ cooldown 60 giây) |
| `POST` | `/api/auth/forgot-password` | Public | Yêu cầu OTP khôi phục mật khẩu gửi về email |
| `POST` | `/api/auth/verify-reset-otp` | Public | Xác thực OTP quên mật khẩu và nhận `resetToken` |
| `POST` | `/api/auth/reset-password` | Public | Đặt lại mật khẩu mới kèm theo `resetToken` |
| `POST` | `/api/auth/login` | Public | Đăng nhập bằng email/password, nhận Access & Refresh Token |
| `POST` | `/api/auth/refresh` | Public | Cấp lại Access Token mới từ Refresh Token hợp lệ |
| `POST` | `/api/auth/logout` | Authenticated | Đăng xuất phiên làm việc hiện tại và thu hồi token |
| `POST` | `/api/auth/logout-all` | Authenticated | Đăng xuất khỏi toàn bộ thiết bị đang đăng nhập |

### 6.2. Hồ sơ cá nhân (User Profile)
**Prefix:** `/api/users/me`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/users/me` | Authenticated | Lấy thông tin cá nhân của người dùng hiện tại |
| `PATCH` | `/api/users/me` | Authenticated | Cập nhật hồ sơ cá nhân (họ tên, ngày sinh, giới tính, lớp) |
| `POST` | `/api/users/me/password/otp` | Authenticated | Gửi OTP về email để xác nhận đổi mật khẩu |
| `POST` | `/api/users/me/password` | Authenticated | Thực hiện đổi mật khẩu sau khi nhập mã OTP |

### 6.3. Chuỗi học tập (User Streak)
**Prefix:** `/api/users/me/streak`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/users/me/streak` | Authenticated | Xem chuỗi ngày học hiện tại, kỷ lục chuỗi dài nhất |
| `POST` | `/api/users/me/streak/check-in`| Authenticated | Thực hiện điểm danh ngày học (tăng streak nếu hợp lệ) |

### 6.4. Quản trị Người dùng & Vai trò (Admin)
**Prefix:** `/api/admin` (Yêu cầu vai trò `ADMIN`)

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/users` | Role `ADMIN` | Xem danh sách người dùng (phân trang, lọc theo vai trò, trạng thái) |
| `GET` | `/api/admin/users/{id}` | Role `ADMIN` | Xem chi tiết thông tin một người dùng |
| `PATCH` | `/api/admin/users/{id}` | Role `ADMIN` | Khóa/Mở khóa hoặc thay đổi vai trò của người dùng |
| `GET` | `/api/admin/roles` | Role `ADMIN` | Xem danh sách tất cả các vai trò phân quyền |
| `GET` | `/api/admin/roles/{id}` | Role `ADMIN` | Xem chi tiết một vai trò |
| `POST` | `/api/admin/roles` | Role `ADMIN` | Tạo vai trò mới |
| `PATCH` | `/api/admin/roles/{id}` | Role `ADMIN` | Cập nhật thông tin vai trò |
| `DELETE` | `/api/admin/roles/{id}` | Role `ADMIN` | Xóa vai trò (không áp dụng với role hệ thống) |

### 6.5. Mô hình 3D Sinh học (Bio Models)
**Prefix:** `/api/models` & `/api/admin/models`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/models/featured` | Public | Lấy danh sách các mô hình 3D nổi bật hiển thị ở trang chủ |
| `GET` | `/api/models/catalog` | Public | Tra cứu danh mục mô hình (phân trang, lọc theo khối lớp, chuyên mục) |
| `GET` | `/api/models/detail/{id}` | Public | Lấy chi tiết thông tin khoa học của mô hình theo ID |
| `GET` | `/api/models/slug/{slug}` | Public | Lấy chi tiết mô hình theo đường dẫn slug thân thiện |
| `GET` | `/api/models/categories` | Public | Lấy danh sách các danh mục mô hình (Động vật, Thực vật, Vi sinh...) |
| `GET` | `/api/admin/models` | Role `ADMIN` | Danh sách quản lý mô hình (phân trang dành cho Admin) |
| `GET` | `/api/admin/models/{id}` | Role `ADMIN` | Xem chi tiết thông tin kỹ thuật mô hình |
| `POST` | `/api/admin/models` | Role `ADMIN` | Thêm mới mô hình 3D |
| `PUT` | `/api/admin/models/{id}` | Role `ADMIN` | Chỉnh sửa toàn bộ thông tin mô hình |
| `PATCH`| `/api/admin/models/{id}/featured` | Role `ADMIN` | Bật/tắt ghim nổi bật mô hình lên trang chủ |
| `DELETE`| `/api/admin/models/{id}` | Role `ADMIN` | Xóa mô hình khỏi hệ thống |

### 6.6. Lưu trữ & Streaming Asset 3D (R2 Storage)
**Prefix:** `/api/models`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/models` | Public | Liệt kê các key asset mô hình đang có trên bucket Cloudflare R2 |
| `GET` / `HEAD` | `/api/models/{*objectKey}` | Public | Proxy streaming file 3D (.glb, .gltf, textures) trực tiếp từ R2 về client |

### 6.7. Trợ lý Trí tuệ nhân tạo (AI Tutor)
**Prefix:** `/api/ai`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/ai/chat` | Public | Gửi câu hỏi, chạy bộ lọc Guardrail và nhận câu trả lời từ AI Tutor |

**Cấu trúc Request Body:**
```json
{
  "conversationId": "uuid-string-or-null",
  "question": "Trùng roi xanh di chuyển bằng cách nào?"
}
```

### 6.8. Hệ thống Bài thi & Câu hỏi (Exam & Question)

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/exams` | Public | Tạo mới một bài kiểm tra |
| `GET` | `/api/exams` | Public | Lấy danh sách tất cả bài kiểm tra |
| `GET` | `/api/exams/subject/{subjectName}` | Public | Lọc đề thi theo tên môn học (Sinh học, Hóa học, Vật lý) |
| `GET` | `/api/exams/type/{type}` | Public | Lọc đề thi theo thể loại (15 phút, 1 tiết, học kỳ) |
| `GET` | `/api/exams/code/{code}` | Public | Tìm đề thi theo mã đề |
| `POST` | `/api/questions` | Public | Thêm mới một câu hỏi |
| `GET` | `/api/questions/{id}` | Public | Lấy chi tiết câu hỏi theo ID |
| `GET` | `/api/questions/exam/{examId}` | Public | Lấy toàn bộ danh sách câu hỏi nằm trong một đề thi |
| `POST` | `/api/exam-questions` | Public | Gán câu hỏi vào đề thi kèm thứ tự câu và điểm số |
| `GET` | `/api/exam-questions/exam/{examId}` | Public | Lấy cấu trúc liên kết câu hỏi - điểm số của đề thi |
| `POST` | `/api/answers` | Public | Tạo các đáp án lựa chọn cho câu hỏi |
| `GET` | `/api/answers/question/{questionId}` | Public | Lấy danh sách đáp án của một câu hỏi |
| `POST` | `/api/question-images` | Public | Đính kèm hình ảnh minh họa cho câu hỏi |
| `POST` | `/api/answer-images` | Public | Đính kèm hình ảnh minh họa cho đáp án |

---

## 7. Các cơ chế kỹ thuật cốt lõi (Core Mechanics)

### Bảo mật & Phân quyền (Security & JWT)
- Hệ thống áp dụng cấu chế xác thực phi trạng thái (**Stateless Authentication**) sử dụng `JwtAuthenticationFilter` chạy trước `UsernamePasswordAuthenticationFilter`.
- Access Token có thời hạn ngắn (mặc định 1 giờ), được mã hóa bằng thuật toán `HS256` với khóa bí mật tối thiểu 32 ký tự.
- Khi người dùng đăng xuất, Access Token được đưa vào Redis Blacklist (`token_blacklist:{jti}`) với TTL bằng thời gian còn lại của token để hủy hiệu lực ngay lập tức.
- Refresh Token được quản lý trong cơ sở dữ liệu (`user_sessions`) và hỗ trợ tính năng **Logout All** giúp thu hồi quyền truy cập trên toàn bộ thiết bị.

### Xác thực OTP Email với Redis
- Quy trình đăng ký tài khoản không tạo ngay bản ghi trong bảng `users` nhằm chống spam rác dữ liệu.
- Thông tin đăng ký được lưu tạm trong Redis (`pending_registration:{email}`) kèm mã OTP 6 chữ số ngẫu nhiên có TTL 10 phút.
- Giao diện thư điện tử HTML được render qua template engine Thymeleaf chuyên nghiệp.
- Áp dụng cơ chế bảo vệ kép:
  - **Cooldown:** Giới hạn gửi lại mã OTP tối thiểu 60 giây (`otp_resend_cooldown:{email}`).
  - **Brute-force limit:** Khóa phiên nếu nhập sai mã OTP quá 5 lần (`otp_attempts:{email}`).

### AI Guardrails & Prompt Engineering
Phân hệ AI Tutor sử dụng mô hình ngôn ngữ lớn thông qua OpenRouter API kết hợp cơ chế kiểm duyệt nội dung nhiều tầng:
1. **Kiểm tra đầu vào:** Giới hạn tối đa 1200 ký tự cho mỗi câu hỏi.
2. **Bộ lọc Guardrails (`AiGuardrailServiceImpl`):** Sử dụng các mẫu Regex phát hiện và từ chối các nhóm nội dung không phù hợp:
   - *Ngoài phạm vi:* Lập trình máy tính, làm văn phân tích, tiền điện tử, chính trị, game hack.
   - *Nguy hiểm lý - hóa:* Chế tạo thuốc nổ, vũ khí, khí độc, bẫy điện gây giật, ma túy.
   - *Rủi ro y tế / sinh học:* Tự chẩn đoán bệnh tật, kê đơn thuốc, nuôi cấy vi khuẩn nguy hiểm.
   - *Tiêu cực & bạo lực:* Tự hại, khiêu dâm, bạo lực, gian lận thi cử.
3. **Cơ chế Safe Academic Answer:** Đối với các câu hỏi học thuật chứa từ khóa nhạy cảm (như "axit clohidric", "phản ứng cháy", "vi khuẩn"), hệ thống không từ chối thô bạo mà tự động bổ sung cờ an toàn vào System Prompt: chỉ giải thích lý thuyết thuần túy và luôn nhắc nhở nguyên tắc an toàn thí nghiệm dưới sự hướng dẫn của thầy cô.
4. **Lưu trữ ngữ cảnh:** Tích hợp Firebase Firestore để lưu trữ lịch sử cuộc hội thoại, tự động gửi kèm 8 tin nhắn gần nhất để AI duy trì mạch giao tiếp tự nhiên.

### Streaming 3D Models qua R2 Proxy
- Để bảo mật bucket Cloudflare R2 nội bộ và tránh các lỗi liên quan đến CORS trên trình duyệt, Backend đóng vai trò làm **Reverse Streaming Proxy**.
- Endpoint `GET /api/models/{*objectKey}` sử dụng Spring `StreamingResponseBody` để truyền tải trực tiếp dòng dữ liệu nhị phân từ R2 về trình duyệt client mà không cần lưu tạm vào bộ nhớ RAM máy chủ.
- Hỗ trợ đầy đủ phương thức `HEAD` và các header HTTP quan trọng (`Content-Length`, `Content-Type`, `Content-Disposition`), cho phép thư viện Three.js / Babylon.js phía Frontend tải mô hình 3D mượt mà.

### Thuật toán Điểm danh Daily Streak
- Mỗi người dùng sở hữu một bản ghi `UserStreak` liên kết với tài khoản.
- Khi người dùng đăng nhập hoặc bấm điểm danh (`check-in`):
  - Nếu đã điểm danh trong ngày hôm nay: Không thay đổi số ngày liên tiếp.
  - Nếu điểm danh vào ngày kế tiếp (liên tục): Tăng `current_streak` thêm 1. Cập nhật `longest_streak` nếu vượt qua kỷ lục cũ.
  - Nếu bị ngắt quãng quá 1 ngày: Đặt lại `current_streak = 1`, giữ nguyên `longest_streak`.

---

## 8. Hướng dẫn Cài đặt & Vận hành (Deployment & Setup)

### Yêu cầu tiên quyết
- **Java Development Kit (JDK):** Phiên bản 21 trở lên
- **Apache Maven:** 3.9+ (hoặc sử dụng wrapper `./mvnw`)
- **Docker & Docker Compose:** Đã cài đặt và đang chạy dịch vụ Docker
- **Tài nguyên bên ngoài:**
  - Khóa API OpenRouter (`OPENROUTER_API_KEY`)
  - Tài khoản Gmail bật 2FA và tạo Mật khẩu ứng dụng (App Password)
  - File khóa dịch vụ Firebase: `firebase/serviceAccountKey.json`

### Biến môi trường (.env)
Tạo file `.env` tại thư mục gốc của dự án dựa theo mẫu dưới đây:

```env
# Chọn môi trường: 'local' (Docker Postgres) hoặc 'supabase' (Postgres Cloud)
SPRING_PROFILES_ACTIVE=local

# Cấu hình OpenRouter AI
OPENROUTER_API_KEY=sk-or-v1-your-openrouter-api-key

# Cấu hình Cloudflare R2 (Lưu trữ 3D Models)
R2_ACCOUNT_ID=your_cloudflare_account_id
R2_ACCESS_KEY_ID=your_r2_access_key
R2_SECRET_ACCESS_KEY=your_r2_secret_key
R2_BUCKET_NAME=bio3d-models
R2_ENDPOINT=https://your_account_id.r2.cloudflarestorage.com

# Chuỗi bí mật JWT (Tối thiểu 32 ký tự)
JWT_SECRET=BioverseJwtSecretKeyThatIsAtLeastThirtyTwoCharsLong!
JWT_ACCESS_EXPIRATION_MS=3600000
JWT_REFRESH_EXPIRATION_MS=604800000

# Redis Cache (Local Docker port 6380)
REDIS_HOST=localhost
REDIS_PORT=6380
REDIS_PASSWORD=123456
REDIS_SSL=false
REDIS_TIMEOUT=2s

# Cho phép tự động tạo tài khoản mẫu khi khởi động
SEED_ENABLED=true

# Cấu hình gửi Mail OTP qua Gmail SMTP
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password-16-chars
MAIL_FROM=your-email@gmail.com
MAIL_FROM_NAME=Bioverse

# Cấu hình tham số OTP
OTP_LENGTH=6
OTP_TTL_SECONDS=600
OTP_RESEND_COOLDOWN_SECONDS=60
OTP_MAX_ATTEMPTS=5

# Cấu hình PostgreSQL Local (Khớp với docker-compose.yml)
LOCAL_DB_HOST=localhost
LOCAL_DB_PORT=5434
LOCAL_DB_NAME=bioverse
LOCAL_DB_USERNAME=bioverse
LOCAL_DB_PASSWORD=123456
LOCAL_DB_SSLMODE=disable
LOCAL_DB_POOL_SIZE=10

# Cấu hình PostgreSQL Supabase (Dùng Session Pooler port 5432)
SUPABASE_DB_HOST=aws-0-ap-southeast-1.pooler.supabase.com
SUPABASE_DB_PORT=5432
SUPABASE_DB_NAME=postgres
SUPABASE_DB_USERNAME=postgres.your_project_ref
SUPABASE_DB_PASSWORD=your_db_password
SUPABASE_DB_SSLMODE=require
SUPABASE_DB_POOL_SIZE=5
```

### Chạy môi trường Local Development

1. **Khởi chạy Database & Redis bằng Docker Compose:**
   ```bash
   docker compose up -d
   ```
   *Lệnh này sẽ khởi chạy Postgres trên cổng `5434` và Redis trên cổng `6380`.*

2. **Khởi chạy ứng dụng Spring Boot:**
   ```bash
   # Sử dụng Maven Wrapper trên Windows PowerShell
   .\mvnw.cmd spring-boot:run
   ```
   Flyway sẽ tự động quét thư mục `db/migration` và chạy các bước nâng cấp schema từ V1 đến V15.

3. **Kiểm tra tài liệu Swagger UI:**
   Truy cập trình duyệt tại địa chỉ:
   `http://localhost:8080/swagger-ui/index.html`

### Chạy kết nối Supabase Cloud
Để ứng dụng chuyển sang sử dụng cơ sở dữ liệu đám mây Supabase:
1. Đổi giá trị trong file `.env`: `SPRING_PROFILES_ACTIVE=supabase`.
2. Điền đầy đủ thông số `SUPABASE_DB_*` từ Dashboard của Supabase (sử dụng cổng **5432** - Session Pooler, không dùng Transaction Pooler cổng 6543).
3. Khởi động lại ứng dụng: Flyway sẽ tự động tạo bảng và áp dụng dữ liệu hạt giống lên Supabase.

### Triển khai Production với Docker
Dự án cung cấp sẵn cấu hình `docker-compose.prod.yml` chạy ứng dụng Backend và Redis trong cùng một mạng riêng:

```bash
# 1. Build hình ảnh Docker của ứng dụng
docker compose -f docker-compose.prod.yml build

# 2. Khởi chạy toàn bộ hệ thống
docker compose -f docker-compose.prod.yml up -d

# 3. Theo dõi nhật ký hoạt động (logs)
docker compose -f docker-compose.prod.yml logs -f backend
```

### Dữ liệu mẫu khởi tạo (Seed Accounts)
Khi khởi động ứng dụng với thiết lập `SEED_ENABLED=true`, hệ thống sẽ tự động kiểm tra và tạo 3 tài khoản mặc định phục vụ cho việc kiểm thử:

| Loại tài khoản | Email đăng nhập | Mật khẩu mặc định | Vai trò | Ghi chú |
| :--- | :--- | :--- | :---: | :--- |
| **Quản trị viên** | `admin@bioverse.com` | `Admin@123456` | `ADMIN` | Toàn quyền quản trị nội dung & người dùng |
| **Học sinh 1** | `student@bioverse.com` | `Student@123456` | `STUDENT` | Học sinh Lớp 8 |
| **Học sinh 2** | `student2@bioverse.com` | `Student@123456` | `STUDENT` | Học sinh Lớp 7 |

---

*Tài liệu được cập nhật tự động dựa trên mã nguồn và cấu trúc thực tế của dự án Bioverse.*
