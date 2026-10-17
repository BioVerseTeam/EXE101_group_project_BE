# 🧬 BioVerse Backend — Developer Guide

> **Tài liệu dành cho lập trình viên mới tham gia dự án.** Đọc kỹ trước khi viết bất kỳ dòng code nào.

---

## 📋 Mục lục

1. [Tổng quan dự án](#1-tổng-quan-dự-án)
2. [Tech Stack & Công cụ](#2-tech-stack--công-cụ)
3. [Cấu trúc thư mục](#3-cấu-trúc-thư-mục)
4. [Kiến trúc module (Package)](#4-kiến-trúc-module-package)
5. [Cơ sở dữ liệu](#5-cơ-sở-dữ-liệu)
6. [Các tính năng hiện có](#6-các-tính-năng-hiện-có)
7. [API Endpoints tổng hợp](#7-api-endpoints-tổng-hợp)
8. [Bên thứ ba tích hợp](#8-bên-thứ-ba-tích-hợp)
9. [Bảo mật & Xác thực](#9-bảo-mật--xác-thực)
10. [Cấu hình môi trường](#10-cấu-hình-môi-trường)
11. [Deploy & Docker](#11-deploy--docker)
12. [Quy tắc phát triển](#12-quy-tắc-phát-triển)

---

## 1. Tổng quan dự án

**BioVerse** là nền tảng học tập STEM tương tác dành cho học sinh THCS (lớp 6–9). Backend cung cấp REST API cho:

- Xem và tương tác với **mô hình sinh học 3D** (GLB/GLTF)
- Làm và nộp **bài thi trắc nghiệm** với chấm điểm tự động
- **AI Chatbot** hỗ trợ học tập sinh học (powered by OpenRouter/GPT-4o-mini)
- **Đăng ký gói cước & thanh toán** qua cổng PayOS
- **Hệ thống huy hiệu STEM** và **streak học tập** hàng ngày
- **Phương trình hóa học tương tác**

| Thông tin | Giá trị |
|---|---|
| Tên dự án | `EXE101_Bioverse` |
| Phiên bản Java | 21 |
| Framework | Spring Boot 4.1.0 |
| Database | PostgreSQL 16 |
| Cache | Redis 7 |
| Port mặc định | 8080 |
| URL production | https://bioverse.eraidev.id.vn |

---

## 2. Tech Stack & Công cụ

### Backend Core

| Công nghệ | Phiên bản | Vai trò |
|---|---|---|
| Spring Boot | 4.1.0 | Core framework |
| Spring Security | (managed) | Authentication & Authorization |
| Spring Data JPA | (managed) | ORM & Repository pattern |
| Spring Data Redis | (managed) | Cache & Session |
| Spring Mail | (managed) | Gửi email OTP |
| Spring Validation | (managed) | Validate request DTO |
| Spring Web MVC | (managed) | REST API |
| Thymeleaf | (managed) | Template email HTML |

### Database & Migration

| Công nghệ | Phiên bản | Vai trò |
|---|---|---|
| PostgreSQL | 16 | Cơ sở dữ liệu chính |
| Flyway | (managed) | Database migration tự động |
| Redis | 7-alpine | Cache, token blacklist, OTP |

### Thư viện hỗ trợ

| Thư viện | Phiên bản | Vai trò |
|---|---|---|
| Lombok | (managed) | Giảm boilerplate code |
| MapStruct | 1.5.5.Final | Entity to DTO mapping |
| JJWT | 0.12.6 | Tạo và xác thực JWT |
| SpringDoc OpenAPI | 3.0.3 | Swagger UI |

### Build & Deploy

| Công nghệ | Vai trò |
|---|---|
| Maven 3.9 | Build tool |
| Docker (multi-stage) | Containerization |
| Docker Compose | Local dev environment |

---

## 3. Cấu trúc thư mục

```
EXE101_group_project_BE/
├── pom.xml                          # Maven dependencies & build config
├── Dockerfile                       # Multi-stage build: Maven -> JRE Alpine
├── docker-compose.yml               # Local dev: PostgreSQL + Redis
├── docker-compose.prod.yml          # Production deployment
├── .env                             # Biến môi trường thực (KHÔNG commit)
├── .env.example                     # Template biến môi trường
├── .env.prod.example                # Template cho production
│
├── src/
│   ├── main/
│   │   ├── java/com/example/exe101_bioverse/
│   │   │   ├── Exe101BioverseApplication.java   # Entry point Spring Boot
│   │   │   ├── ai/          # Module AI Chat (OpenRouter)
│   │   │   ├── auth/        # Module xác thực & quản lý người dùng
│   │   │   ├── badge/       # Module huy hiệu STEM
│   │   │   ├── common/      # Config, Exception, Response chung
│   │   │   ├── exam/        # Module đề thi & làm bài thi
│   │   │   ├── model/       # Module mô hình 3D & phòng lab
│   │   │   ├── storage/     # Module lưu trữ file (Cloudflare R2)
│   │   │   ├── streak/      # Module streak học tập hàng ngày
│   │   │   └── subscription/ # Module gói cước & thanh toán PayOS
│   │   │
│   │   └── resources/
│   │       ├── application.properties           # Config chính
│   │       ├── application-local.properties     # Profile: Local Docker DB
│   │       ├── application-supabase.properties  # Profile: Supabase Cloud DB
│   │       ├── mock_data.sql                    # Dữ liệu mẫu v1
│   │       ├── mock_data_v2.sql                 # Dữ liệu mẫu v2
│   │       ├── db/migration/                    # Flyway SQL migrations (V1-V21)
│   │       └── templates/                       # Thymeleaf email templates
│   │
│   └── test/                        # Unit & integration tests
│
├── scripts/                         # Script tiện ích (push DB to Supabase,...)
├── deploy/                          # File cấu hình deploy production
│
├── DATABASE_CONCEPTUAL_DESIGN.md    # Thiết kế DB conceptual
├── EXAM_API_DOCUMENTATION.md        # Tài liệu API thi cử chi tiết
├── EXAM_ENTITY_DOCUMENTATION.md     # Tài liệu entity thi cử
└── PROJECT_DOCUMENTATION.md         # Tài liệu tổng quát dự án
```

---

## 4. Kiến trúc module (Package)

Mỗi module tuân theo kiến trúc phân lớp nhất quán:

```
<module>/
├── controller/    # REST Controller — nhận request, trả response
├── service/       # Business logic (interface + impl/)
├── repository/    # Spring Data JPA Repository
├── entity/        # JPA Entity (ánh xạ tới bảng DB)
├── dto/
│   ├── request/   # DTO nhận từ client
│   └── response/  # DTO trả về cho client
├── mapper/        # MapStruct mapper (Entity <-> DTO)
└── enums/         # Enum của module
```

### Module `auth` — Xác thực & Quản lý người dùng

Xử lý toàn bộ luồng đăng ký, đăng nhập, OTP, JWT và quản trị người dùng.

**Entities:** `User`, `Role`, `UserSession`

**Controllers:**
- `AuthController` — `/api/auth/**` — đăng ký, đăng nhập, OTP, refresh, logout
- `ProfileController` — `/api/users/me` — xem/cập nhật profile, đổi mật khẩu
- `AdminUserController` — `/api/admin/users` — quản lý danh sách user
- `AdminRoleController` — `/api/admin/roles` — quản lý role
- `AdminDeskController` — `/api/admin/desk` — thống kê dashboard admin

**Services quan trọng:**
- `JwtService` — Tạo/xác thực Access Token & Refresh Token (HS256)
- `OtpService` — Quản lý OTP 6 số qua Redis (TTL, retry limit, cooldown)
- `MailService` — Gửi email OTP qua Brevo SMTP + Thymeleaf template
- `TokenBlacklistService` — Blacklist access token đã logout vào Redis

---

### Module `exam` — Đề thi & Làm bài

Quản lý ngân hàng câu hỏi, đề thi, chấm điểm tự động.

**Entities:** `Exam`, `Question`, `Answer`, `ExamQuestion`, `ExamAttempt`, `AttemptAnswer`, `Grade`, `Subject`, `Semester`, `QuestionImage`, `AnswerImage`

**Controllers:**
- `ExamController` — `/api/exams/**` — CRUD đề thi, nhân bản, kéo câu từ ngân hàng
- `QuestionController` — `/api/questions/**` — ngân hàng câu hỏi
- `StudentExamController` — `/api/student/**` — làm bài anti-cheat, nộp bài, lịch sử
- `GradeController`, `SemesterController`, `SubjectController` — danh mục học
- `MediaController` — upload ảnh câu hỏi/đáp án lên Cloudinary

---

### Module `model` — Mô hình 3D & Lab

Quản lý thư viện mô hình sinh học 3D và phòng thí nghiệm ảo.

**Entities:** `BioModel`, `BioModelCategory`, `BioLab`, `ReactionEquation`

**Controllers:**
- `BioModelController` — `/api/models/**` — danh mục, chi tiết, featured, labs
- `AdminBioModelController` — `/api/admin/models/**` — CRUD model 3D
- `AdminCategoryController` — `/api/admin/categories/**` — quản lý danh mục
- `AdminLabController` — `/api/admin/labs/**` — quản lý lab
- `ReactionController` — `/api/reactions/**` — phương trình hóa học
- `AdminReactionController` — `/api/admin/reactions/**` — quản lý phương trình

---

### Module `subscription` — Gói cước & Thanh toán

Quản lý gói Premium và tích hợp thanh toán PayOS.

**Entities:** `Plan`, `Subscription`, `Payment`

**Controllers:**
- `PlanController` — `/api/plans` — xem danh sách gói cước
- `UserSubscriptionController` — `/api/subscriptions/**` — xem gói hiện tại
- `PaymentController` — `/api/payments/**` — checkout, webhook PayOS
- `AdminPlanController` — `/api/admin/plans/**` — quản lý gói
- `AdminSubscriptionController` — `/api/admin/subscriptions/**`
- `AdminPaymentController` — `/api/admin/payments/**`

---

### Module `ai` — AI Chatbot

Tích hợp AI hỗ trợ học tập qua OpenRouter (GPT-4o-mini).

**Controllers:**
- `AiChatController` — `/api/ai/**` — chat, lịch sử hội thoại, xóa hội thoại

**Clients:**
- `OpenRouterClient` — HTTP client gọi OpenRouter API

---

### Module `badge` — Huy hiệu STEM

Hệ thống huy hiệu thưởng thành tích học tập.

**Entities:** `StemBadge`

**Controllers:**
- `BadgeController` — `/api/badges` — xem huy hiệu
- `AdminBadgeController` — `/api/admin/badges/**` — quản lý huy hiệu

---

### Module `streak` — Streak học tập

Theo dõi chuỗi ngày đăng nhập/học tập liên tiếp.

**Entities:** `UserStreak`

> Streak được cập nhật tự động khi user gọi `GET /api/users/me`

---

### Module `storage` — Lưu trữ File

Proxy file 3D model từ Cloudflare R2 tới Frontend.

**Controllers:**
- `ModelAssetController` — `/api/assets/models/**` — serve file từ R2

---

### Module `common` — Dùng chung

| Sub-package | Nội dung |
|---|---|
| `config/` | `SecurityConfig`, `RedisConfig`, `FirebaseConfig`, `Swagger`, `MailTemplateConfig`, `DotEnvLoader` |
| `exception/` | Global exception handler |
| `response/` | `ApiResponse<T>`, `PageResponse<T>` — wrapper chuẩn cho tất cả API |
| `seed/` | Seed dữ liệu mẫu khi khởi động (bật/tắt qua `SEED_ENABLED`) |

---

## 5. Cơ sở dữ liệu

### Hệ thống DB

| Hệ thống | Công nghệ | Mục đích |
|---|---|---|
| Database chính | PostgreSQL 16 | Toàn bộ dữ liệu nghiệp vụ |
| Cache | Redis 7 | OTP, Token Blacklist, AI conversation history |

### Profiles kết nối DB

| Profile | Mô tả | Khi nào dùng |
|---|---|---|
| `local` | Docker PostgreSQL local (port 5434) | Phát triển trên máy tính |
| `supabase` | Supabase Cloud PostgreSQL | Staging / Production |

Cấu hình qua: `SPRING_PROFILES_ACTIVE=local` hoặc `SPRING_PROFILES_ACTIVE=supabase`

### Database Migration (Flyway)

> Tất cả thay đổi schema PHẢI tạo file `Vn__*.sql` mới. **KHÔNG** sửa file đã apply.

| Migration | Nội dung |
|---|---|
| `V1__init_schema.sql` | Schema ban đầu |
| `V2__user_auth_schema.sql` | Bảng users, roles, user_sessions |
| `V3__subscription_schema.sql` | Bảng plans, subscriptions |
| `V4__payment_schema.sql` | Bảng payments |
| `V5__content_taxonomy_schema.sql` | Môn học, danh mục |
| `V6__chemicals_schema.sql` | Hóa chất |
| `V7__experiments_schema.sql` | Thí nghiệm |
| `V8__bio_models_schema.sql` | Mô hình 3D |
| `V9__exam_enhancement_schema.sql` | Cải tiến đề thi |
| `V10__shared_objects_schema.sql` | Shared DB objects (PostgreSQL enums) |
| `V11__roles_table.sql` | Bảng roles |
| `V12__seed_plans.sql` | Seed gói cước mặc định |
| `V13__users_grade.sql` | Thêm cột grade cho users |
| `V14__add_featured_and_metadata_to_bio_models.sql` | Metadata model 3D |
| `V15__user_streaks.sql` | Bảng user_streaks |
| `V16__model_categories_and_labs.sql` | Danh mục và labs |
| `V17__reaction_equations.sql` | Phương trình hóa học |
| `V18__seed_more_reactions.sql` | Seed ~200 phương trình hóa học |
| `V19__exam_class_semester_subject_schema.sql` | Schema thi cử đầy đủ |
| `V20__seed_exam_data_khtn_grade_6_to_9.sql` | Seed đề thi KHTN lớp 6-9 (~1.2MB) |
| `V21__stem_badges_schema_and_seed.sql` | Schema và seed huy hiệu STEM |

### Sơ đồ quan hệ (ERD tóm tắt)

```
users ─── roles
  │
  ├── user_sessions     (refresh tokens, device tracking)
  ├── user_streaks      (streak học tập hàng ngày)
  ├── subscriptions ─── plans
  └── payments ─────── plans

exam ─────────────── subjects
  │
  ├── exam_questions ── questions ── answers
  │                         │            │
  │                    question_images  answer_images
  │
  └── exam_attempts
       └── attempt_answers

bio_models ─── bio_model_categories
bio_labs
reaction_equations

stem_badges

ai_conversations ─── ai_messages
```

### Các bảng chính

| Bảng | Module | Mô tả |
|---|---|---|
| `users` | auth | Tài khoản người dùng (grade, role, status, firebase_uid) |
| `roles` | auth | Phân quyền (ADMIN, STUDENT, TEACHER...) |
| `user_sessions` | auth | Refresh token sessions (device tracking) |
| `user_streaks` | streak | Streak đăng nhập hàng ngày |
| `plans` | subscription | Gói cước (FREE, MONTHLY, YEARLY) |
| `subscriptions` | subscription | Gói đang dùng của user |
| `payments` | subscription | Lịch sử thanh toán PayOS |
| `exam` | exam | Đề thi |
| `questions` | exam | Ngân hàng câu hỏi |
| `answers` | exam | Đáp án (có flag is_correct) |
| `exam_questions` | exam | Mapping đề thi với câu hỏi (kèm điểm số, thứ tự) |
| `exam_attempts` | exam | Lần làm bài của học sinh |
| `attempt_answers` | exam | Đáp án học sinh chọn |
| `subjects` | exam | Môn học |
| `grades` | exam | Lớp học (6-9) |
| `semesters` | exam | Học kỳ |
| `bio_models` | model | Mô hình 3D (JSONB: classification, annotations) |
| `bio_model_categories` | model | Danh mục mô hình |
| `bio_labs` | model | Phòng lab ảo |
| `reaction_equations` | model | Phương trình hóa học (JSONB: chemx_json) |
| `stem_badges` | badge | Huy hiệu STEM (criteria_type, reward_xp) |

---

## 6. Các tính năng hiện có

### Xác thực & Quản lý tài khoản

- **Đăng ký 2 bước:** Nhập email -> Xác thực OTP (6 số, TTL 10 phút, tối đa 5 lần thử)
- **Đăng nhập:** Email + Password -> JWT Access Token (1h) + Refresh Token (7 ngày)
- **Refresh Token:** Cấp lại access token khi hết hạn
- **Quên mật khẩu:** OTP -> Reset Token -> Đặt mật khẩu mới
- **Đổi mật khẩu:** Xác thực OTP trước khi đổi (bảo mật cao hơn)
- **Đa thiết bị:** Session-based, hỗ trợ logout thiết bị hiện tại và logout-all
- **Blacklist JWT:** Access token sau khi logout được lưu vào Redis cho đến khi hết hạn
- **Firebase SSO:** Trường `firebase_uid` hỗ trợ đăng nhập Google (Firebase Auth)

### Hồ sơ người dùng

- Xem profile (đồng thời kích hoạt streak check-in tự động)
- Cập nhật thông tin: tên, avatar, ngày sinh, giới tính, số điện thoại
- Admin quản lý role và trạng thái tài khoản (ACTIVE/BANNED)

### Mô hình 3D sinh học

- Thư viện model GLB/GLTF (sinh học, hóa học, vật lý)
- Lọc theo khối lớp (6-9), danh mục, môn học, từ khóa tìm kiếm
- Phân trang catalog
- Truy cập theo ID hoặc slug (URL-friendly)
- Model "featured" được hiển thị trên trang chủ
- Tracking `views_count` mỗi lần xem
- JSONB metadata: `classification`, `annotations`, `fun_facts`, `default_rotation`, `camera_position`
- Admin: CRUD đầy đủ, upload thumbnail lên Cloudinary

### Phòng lab & Phương trình hóa học

- Danh sách lab 3D đang mở
- ~200+ phương trình hóa học tương tác (format ChemX JSON)
- Admin: quản lý lab và phương trình

### Hệ thống thi cử

- **Ngân hàng câu hỏi:** CRUD câu hỏi trắc nghiệm, upload ảnh minh họa (Cloudinary)
- **Quản lý đề thi:** Tạo đề, kéo câu từ ngân hàng, sắp xếp thứ tự, phân bổ điểm
- **Nhân bản đề thi:** Duplicate nhanh đề thi có sẵn
- **Anti-Cheat Paper:** Trộn thứ tự câu hỏi và đáp án ngẫu nhiên cho mỗi lần thi
- **Chấm điểm tự động:** Server-side grading, tính XP
- **Lịch sử thi:** Xem lại bài làm, đáp án đúng, giải thích
- **Danh mục:** Quản lý môn học, lớp, học kỳ
- **Dữ liệu mẫu:** Seed đề thi KHTN lớp 6-9 (migration V20)

### AI Chatbot học tập

- Chat với AI về sinh học (context-aware, nhớ lịch sử hội thoại)
- Lưu lịch sử hội thoại theo `conversation_id`
- Phân trang danh sách cuộc trò chuyện
- Xem lại tin nhắn trong hội thoại
- Xóa hội thoại
- Cấu hình: model `openai/gpt-4o-mini`, temperature 0.3, max 8 messages history, max 800 tokens
- Guardrails: lọc nội dung không phù hợp

### Gói cước & Thanh toán

- Xem danh sách gói cước (FREE / MONTHLY / YEARLY)
- Tạo link thanh toán QR code qua PayOS
- Webhook tự động cập nhật trạng thái payment
- Hủy đơn thanh toán đang chờ
- Đồng bộ trạng thái từ PayOS
- Admin: quản lý gói, xem đơn hàng
- Flag `SUBSCRIPTION_ENFORCE_QUOTA=false` — tạm thời mở miễn phí toàn bộ tính năng

### Huy hiệu STEM

- Hệ thống badge theo tiêu chí học tập (Science, Technology, Engineering, Math)
- Tiêu chí: `ALWAYS_UNLOCKED`, điểm số, streak, số bài thi...
- Thưởng XP khi đạt badge
- Admin: CRUD huy hiệu, set criteria

### Streak học tập

- Theo dõi chuỗi ngày học liên tiếp
- Ghi nhận tự động khi user gọi `GET /api/users/me`
- `currentStreak`: Streak hiện tại
- `longestStreak`: Chuỗi dài nhất từ trước đến nay
- Tự động reset nếu bỏ qua >= 1 ngày

### Lưu trữ File

- Upload ảnh câu hỏi/đáp án lên **Cloudinary**
- Serve file 3D model từ **Cloudflare R2** (proxy qua backend để bảo vệ URL)

---

## 7. API Endpoints tổng hợp

> Swagger UI: `http://localhost:8080/swagger-ui/index.html`
>
> Legend: OPEN = không cần JWT | AUTH = cần JWT | ADMIN = cần Role ADMIN

### Auth — `/api/auth`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| POST | `/register` | OPEN | Đăng ký, gửi OTP |
| POST | `/verify-register` | OPEN | Xác thực OTP, tạo tài khoản |
| POST | `/resend-otp` | OPEN | Gửi lại OTP |
| POST | `/login` | OPEN | Đăng nhập |
| POST | `/refresh` | OPEN | Refresh access token |
| POST | `/forgot-password` | OPEN | Gửi OTP quên mật khẩu |
| POST | `/verify-reset-otp` | OPEN | Xác thực OTP reset |
| POST | `/reset-password` | OPEN | Đặt mật khẩu mới |
| POST | `/logout` | AUTH | Logout thiết bị hiện tại |
| POST | `/logout-all` | AUTH | Logout tất cả thiết bị |

### Profile — `/api/users/me`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/` | AUTH | Lấy profile + check-in streak |
| PATCH | `/` | AUTH | Cập nhật profile |
| POST | `/password/otp` | AUTH | Gửi OTP đổi mật khẩu |
| POST | `/password` | AUTH | Đổi mật khẩu sau khi có OTP |

### Bio Models — `/api/models`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/featured` | OPEN | Model phổ biến cho trang chủ |
| GET | `/catalog` | OPEN | Danh mục có lọc & phân trang |
| GET | `/detail/{id}` | OPEN | Chi tiết theo ID |
| GET | `/slug/{slug}` | OPEN | Chi tiết theo slug |
| GET | `/categories` | OPEN | Danh sách categories |
| GET | `/labs` | OPEN | Danh sách labs |

### Exam — `/api/exams`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/` | OPEN | Danh sách / catalog đề thi |
| POST | `/` | OPEN | Tạo đề thi mới |
| GET | `/summary` | OPEN | Thống kê tổng đề thi |
| GET | `/{id}` | OPEN | Chi tiết đề thi |
| PUT | `/{id}` | OPEN | Cập nhật đề thi |
| DELETE | `/{id}` | OPEN | Xóa đề thi |
| POST | `/{id}/duplicate` | OPEN | Nhân bản đề thi |
| GET | `/{id}/builder` | OPEN | Nạp data đề thi cho editor |
| PUT | `/{id}/questions/reorder` | OPEN | Sắp xếp lại câu hỏi |
| POST | `/{id}/questions` | OPEN | Thêm câu hỏi vào đề |
| DELETE | `/{id}/questions/{qId}` | OPEN | Xóa câu khỏi đề |
| POST | `/{id}/questions/pick-from-bank` | OPEN | Kéo câu từ ngân hàng |

### Student Exam — `/api/student`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/exams/{id}/paper` | OPEN | Lấy đề thi anti-cheat (đã trộn) |
| POST | `/exams/{id}/submit` | AUTH (optional) | Nộp bài, chấm điểm tự động |
| GET | `/exam-attempts/{id}` | AUTH (optional) | Xem lại bài làm chi tiết |
| GET | `/exam-attempts/my-history` | AUTH (optional) | Lịch sử thi cá nhân |

### AI Chat — `/api/ai`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| POST | `/chat` | OPEN | Gửi tin nhắn (guest hoặc authenticated) |
| GET | `/conversations` | AUTH | Danh sách hội thoại (phân trang) |
| GET | `/conversations/{id}/messages` | AUTH | Tin nhắn trong hội thoại |
| DELETE | `/conversations/{id}` | AUTH | Xóa hội thoại |

### Payment — `/api/payments`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| POST | `/checkout` | AUTH | Tạo link thanh toán PayOS |
| GET | `/{orderCode}` | AUTH | Trạng thái đơn hàng |
| POST | `/{orderCode}/cancel` | AUTH | Hủy đơn hàng |
| POST | `/payos/webhook` | OPEN | Webhook nhận từ PayOS |

### Plans — `/api/plans`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/` | OPEN | Danh sách gói cước |

### Badges — `/api/badges`

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/` | OPEN | Danh sách huy hiệu STEM |

### Admin — `/api/admin/**`

> Tất cả yêu cầu Role: `ADMIN`

Bao gồm đầy đủ CRUD cho: Users, Roles, Models, Categories, Labs, Reactions, Plans, Subscriptions, Payments, Badges.

---

## 8. Bên thứ ba tích hợp

### OpenRouter — AI Gateway

| Thuộc tính | Giá trị |
|---|---|
| Vai trò | Cổng gọi AI model (dễ đổi sang model khác) |
| URL | `https://openrouter.ai/api/v1` |
| Model mặc định | `openai/gpt-4o-mini` |
| Biến ENV | `OPENROUTER_API_KEY` |
| File | `OpenRouterClient.java` |
| Timeout | 30 phút (AI inference chậm) |
| Tài liệu | https://openrouter.ai/docs |

---

### Firebase Admin SDK — Google SSO

| Thuộc tính | Giá trị |
|---|---|
| Vai trò | Xác thực token Firebase, quản lý user Google SSO |
| Phiên bản | `firebase-admin:9.9.0` |
| File cấu hình | `FirebaseConfig.java` |
| Biến ENV | `FIREBASE_SERVICE_ACCOUNT_PATH` |
| File JSON | `firebase/serviceAccountKey.json` (KHÔNG commit git) |
| Dashboard | https://console.firebase.google.com |

---

### PayOS — Cổng thanh toán

| Thuộc tính | Giá trị |
|---|---|
| Vai trò | Tạo link thanh toán QR code cho gói Premium |
| Phiên bản | `payos-java:2.0.1` |
| Nhà phát triển | Việt Nam |
| Biến ENV | `PAYOS_CLIENT_ID`, `PAYOS_API_KEY`, `PAYOS_CHECKSUM_KEY` |
| Callback ENV | `PAYOS_RETURN_URL`, `PAYOS_CANCEL_URL` |
| Webhook | `POST /api/payments/payos/webhook` |
| Dashboard | https://my.payos.vn |

---

### Cloudinary — Lưu trữ ảnh

| Thuộc tính | Giá trị |
|---|---|
| Vai trò | Upload và lưu ảnh câu hỏi/đáp án thi |
| Phiên bản | `cloudinary-http44:1.39.0` |
| Biến ENV | Cloudinary URL hoặc cloud_name/api_key/api_secret |
| Dashboard | https://cloudinary.com |

---

### Cloudflare R2 — Lưu trữ file 3D

| Thuộc tính | Giá trị |
|---|---|
| Vai trò | Lưu trữ file GLB model 3D, backend proxy tới FE |
| SDK | `software.amazon.awssdk:s3:2.31.78` (R2 tương thích S3 API) |
| Bucket mặc định | `bio3d-models` |
| Biến ENV | `R2_ACCOUNT_ID`, `R2_ACCESS_KEY_ID`, `R2_SECRET_ACCESS_KEY`, `R2_BUCKET_NAME` |
| Endpoint | `GET /api/assets/models/**` |
| Dashboard | https://dash.cloudflare.com |

---

### Brevo — SMTP Email

| Thuộc tính | Giá trị |
|---|---|
| Vai trò | Gửi email OTP đăng ký, quên mật khẩu, đổi mật khẩu |
| Host | `smtp-relay.brevo.com` (port 587) |
| Biến ENV | `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM` |
| Template | Thymeleaf HTML email trong `resources/templates/` |
| Lưu ý | `MAIL_FROM` phải là sender đã verify trên Brevo |
| Dashboard | https://app.brevo.com |

---

### Supabase — Cloud Database

| Thuộc tính | Giá trị |
|---|---|
| Vai trò | PostgreSQL cloud cho staging/production |
| Pool | Session pooler port 5432 — KHÔNG dùng Transaction pooler port 6543 |
| Biến ENV | `SUPABASE_DB_HOST`, `SUPABASE_DB_PORT`, `SUPABASE_DB_USERNAME`, `SUPABASE_DB_PASSWORD` |
| Profile | `SPRING_PROFILES_ACTIVE=supabase` |
| Dashboard | https://supabase.com/dashboard |

---

## 9. Bảo mật & Xác thực

### Luồng xác thực JWT

```
Client                    Backend                     Redis
  │                          │                           │
  ├─POST /login ────────────►│                           │
  │                          ├─ Verify password          │
  │                          ├─ Create UserSession       │
  │◄── JWT(1h) + RefreshToken┤                           │
  │                          │                           │
  ├─GET /api/* (Bearer JWT)─►│                           │
  │                          ├─ JwtAuthenticationFilter  │
  │                          ├─ Check blacklist ─────────►
  │                          │◄── not blacklisted ───────┤
  │◄──────── Response ───────┤                           │
  │                          │                           │
  ├─POST /logout ───────────►│                           │
  │                          ├─ Blacklist JWT ───────────►
  │                          ├─ Delete UserSession       │
```

### Cơ chế OTP (Redis)

```
POST /register
  -> otp:{email}           (TTL: 600s) — Mã OTP
  -> otp:cooldown:{email}  (TTL: 60s)  — Chống spam gửi lại
  -> otp:attempts:{email}              — Đếm lần thử (max: 5)
```

### Phân quyền

| Role | Quyền truy cập |
|---|---|
| `ADMIN` | Toàn quyền, bao gồm `/api/admin/**` |
| `STUDENT` | API công khai + profile + payment + AI chat |
| Guest | API công khai (models, plans, badges, exam paper, AI chat) |

### Endpoints công khai (không cần JWT)

- `/api/auth/**` — Xác thực
- `/api/models/**` — Xem model 3D
- `/api/reactions/**` — Xem phương trình
- `/api/plans/**` — Xem gói cước
- `/api/badges/**` — Xem huy hiệu
- `/api/ai/chat` — Chat AI (guest)
- `/api/payments/payos/webhook` — Webhook PayOS
- `/swagger-ui/**`, `/v3/api-docs/**` — Swagger

---

## 10. Cấu hình môi trường

### Thiết lập môi trường local

```bash
# Bước 1: Copy file env template
cp .env.example .env

# Bước 2: Khởi động PostgreSQL (port 5434) và Redis (port 6380)
docker compose up -d

# Bước 3: Điền các API key vào file .env
# - OPENROUTER_API_KEY     : https://openrouter.ai
# - MAIL_USERNAME/PASSWORD : https://app.brevo.com -> Settings -> SMTP
# - R2_*                   : https://dash.cloudflare.com
# - PAYOS_*                : https://my.payos.vn
# - Firebase               : serviceAccountKey.json -> firebase/

# Bước 4: Chạy ứng dụng
./mvnw spring-boot:run

# Bước 5: Mở Swagger
# http://localhost:8080/swagger-ui/index.html
```

### Biến ENV bắt buộc

| Biến | Ví dụ | Bắt buộc cho |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `local` hoặc `supabase` | Luôn luôn |
| `JWT_SECRET` | Chuỗi >= 32 ký tự | Luôn luôn |
| `REDIS_PASSWORD` | `123456` | Luôn luôn |
| `OPENROUTER_API_KEY` | `sk-or-v1-...` | Tính năng AI |
| `MAIL_USERNAME` | SMTP login Brevo | Gửi OTP email |
| `MAIL_PASSWORD` | SMTP key xsmtpsib-... | Gửi OTP email |
| `MAIL_FROM` | `noreply@domain.com` | Gửi OTP email |
| `PAYOS_CLIENT_ID` | ID từ PayOS | Thanh toán |
| `PAYOS_API_KEY` | API key PayOS | Thanh toán |
| `PAYOS_CHECKSUM_KEY` | Checksum key PayOS | Thanh toán |
| `R2_ACCOUNT_ID` | Cloudflare Account ID | File 3D model |
| `R2_ACCESS_KEY_ID` | R2 Access Key | File 3D model |
| `R2_SECRET_ACCESS_KEY` | R2 Secret Key | File 3D model |

### Biến ENV tùy chọn

| Biến | Mặc định | Mô tả |
|---|---|---|
| `SEED_ENABLED` | `true` | Seed dữ liệu mẫu khi khởi động |
| `SUBSCRIPTION_ENFORCE_QUOTA` | `false` | false = mở miễn phí toàn bộ |
| `CORS_ALLOWED_ORIGINS` | `https://bioverse.eraidev.id.vn` | CORS production |

---

## 11. Deploy & Docker

### Local Development

```bash
# Khởi động PostgreSQL (port 5434) + Redis (port 6380)
docker compose up -d

# Dừng containers
docker compose down

# Xem logs real-time
docker compose logs -f
```

### Production Build (Docker)

Multi-stage Dockerfile:
- **Stage 1:** Maven 3.9 + JDK 21 -> Build JAR
- **Stage 2:** JRE 21 Alpine -> Run JAR (image gọn nhẹ hơn)

```bash
# Build Docker image
docker build -t bioverse-be .

# Chạy container với env file
docker run --env-file .env -p 8080:8080 bioverse-be
```

### Production Deploy

```bash
# Deploy bằng production compose file
docker compose -f docker-compose.prod.yml up -d
```

### CI/CD

- GitHub Actions: xem thư mục `.github/`

---

## 12. Quy tắc phát triển

### Convention bắt buộc

1. **Module mới:** Tạo package riêng, tuân theo cấu trúc `controller/service/repository/entity/dto/mapper/enums`
2. **API response:** Luôn dùng `ApiResponse<T>` wrapper — `ApiResponse.success(data)` hoặc `ApiResponse.success(data, message)`
3. **Database:** Tạo Flyway migration mới `V{n+1}__description.sql` — **không** sửa file đã apply
4. **DTO mapping:** Dùng MapStruct — không map thủ công trong service
5. **Validation:** Khai báo annotation trong DTO với `jakarta.validation`, không validate trong service
6. **Pagination:** Dùng `PageResponse<T>` wrapper cho tất cả danh sách phân trang

### Những điều cần tránh

- `spring.jpa.hibernate.ddl-auto=validate` — JPA chỉ validate schema, không tự tạo/sửa bảng
- Flyway chạy tự động khi khởi động — migration SQL sai sẽ làm app không start được
- Không commit file `.env`, `serviceAccountKey.json`, `target/` lên git
- Redis timeout ngắn (2s) — code cần handle `RedisConnectionException`
- AI inference timeout 30 phút — tránh gọi AI trong vòng lặp hoặc background job ngắn

### Checklist khi thêm tính năng mới

- [ ] Tạo migration SQL nếu cần schema mới
- [ ] Tạo Entity JPA với annotation đúng
- [ ] Tạo Repository interface
- [ ] Tạo DTO request/response
- [ ] Tạo MapStruct mapper
- [ ] Implement Service (interface + impl trong `impl/`)
- [ ] Tạo Controller với route phù hợp
- [ ] Cập nhật `SecurityConfig` nếu cần phân quyền đặc biệt
- [ ] Thêm Swagger `@Tag`, `@Operation` annotation
- [ ] Test với Swagger UI tại http://localhost:8080/swagger-ui/index.html

---

*Cập nhật lần cuối: 2026-10-05 | BioVerse Backend Team*
