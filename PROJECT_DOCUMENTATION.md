# TÀI LIỆU DỰ ÁN BIOVERSE BACKEND (EXE101)

> **Tên dự án:** Bioverse - Nền tảng học tập Khoa học Tự nhiên trực quan 3D cho học sinh THCS  
> **Mã dự án:** EXE101_Bioverse / `EXE101_group_project_BE`  
> **Phiên bản:** 0.0.1-SNAPSHOT  
> **Ngôn ngữ & Nền tảng:** Java 21 LTS | Spring Boot 4.1.0  
> **Cơ sở dữ liệu:** PostgreSQL 16 (Local / Supabase) | Redis 7 (In-memory Cache, Token Blacklist & OTP)  
> **Lưu trữ 3D & Thumbnail:** Cloudflare R2 (S3-compatible Object Storage)  
> **AI Service:** OpenRouter API (`openai/gpt-4o-mini`) + Firebase Firestore (Lịch sử hội thoại)  
> **Email Service:** Brevo SMTP Relay / Gmail SMTP + Thymeleaf Template Engine  
> **Triển khai & CI/CD:** Docker, Docker Compose, Cloudflare Zero Trust Tunnel, Harbor Registry, GitHub Actions  

---

## MỤC LỤC
1. [Tổng quan dự án](#1-tổng-quan-dự-án)
2. [Ngăn xếp công nghệ (Tech Stack)](#2-ngăn-xếp-công-nghệ-tech-stack)
3. [Cấu trúc mã nguồn (Project Structure)](#3-cấu-trúc-mã-nguồn-project-structure)
4. [Mô hình dữ liệu & Database Migrations (V1 → V20)](#4-mô-hình-dữ-liệu--database-migrations)
5. [Quy chuẩn API & Bảng mã lỗi](#5-quy-chuẩn-api--bảng-mã-lỗi)
6. [Danh sách API Endpoints chi tiết](#6-danh-sách-api-endpoints-chi-tiết)
   - [6.1. Xác thực & Tài khoản (Authentication)](#61-xác-thực--tài-khoản-authentication)
   - [6.2. Hồ sơ cá nhân & Đổi mật khẩu (User Profile)](#62-hồ-sơ-cá-nhân--đổi-mật-khẩu-user-profile)
   - [6.3. Chuỗi học tập (User Streak)](#63-chuỗi-học-tập-user-streak)
   - [6.4. Bảng điều khiển Quản trị viên (Admin Desk & Census)](#64-bảng-điều-khiển-quản-trị-viên-admin-desk--census)
   - [6.5. Quản trị Người dùng & Vai trò (Admin Users & Roles)](#65-quản-trị-người-dùng--vai-trò-admin-users--roles)
   - [6.6. Quản trị Mô hình 3D, Loại mẫu & Phòng Lab (Admin Models, Categories, Labs)](#66-quản-trị-mô-hình-3d-loại-mẫu--phòng-lab-admin-models-categories-labs)
   - [6.7. Quản trị Phương trình Hoá học 3D (Admin Reaction Equations)](#67-quản-trị-phương-trình-hoá-học-3d-admin-reaction-equations)
   - [6.8. Mô hình 3D & Phòng Lab công khai (Public Bio Models & Labs)](#68-mô-hình-3d--phòng-lab-công-khai-public-bio-models--labs)
   - [6.9. Phương trình Hoá học công khai (Public Reactions)](#69-phương-trình-hoá-học-công-khai-public-reactions)
   - [6.10. Lưu trữ & Streaming Asset 3D (R2 Storage Proxy)](#610-lưu-trữ--streaming-asset-3d-r2-storage-proxy)
   - [6.11. Trợ lý Trí tuệ nhân tạo (AI Tutor & Conversations)](#611-trợ-lý-trí-tuệ-nhân-tạo-ai-tutor--conversations)
   - [6.12. Hệ thống Khối lớp, Học kỳ, Môn học & Đề thi (Exam Taxonomy)](#612-hệ-thống-khối-lớp-học-kỳ-môn-học--đề-thi-exam-taxonomy)
7. [Các cơ chế kỹ thuật cốt lõi (Core Mechanics)](#7-các-cơ-chế-kỹ-thuật-cốt-lõi-core-mechanics)
   - [Bảo mật & Phân quyền (Stateless JWT & Redis Blacklist)](#bảo-mật--phân-quyền-stateless-jwt--redis-blacklist)
   - [Xác thực OTP Email đa tầng với Redis](#xác-thực-otp-email-đa-tầng-với-redis)
   - [AI Guardrails & Multi-turn Chat cá nhân hóa (Firestore + OpenRouter)](#ai-guardrails--multi-turn-chat-cá-nhân-hóa-firestore--openrouter)
   - [Streaming 3D Models qua R2 Reverse Proxy](#streaming-3d-models-qua-r2-reverse-proxy)
   - [Mô phỏng Hoá học 3D với .chemx Keyframe Animation](#mô-phỏng-hoá-học-3d-với-chemx-keyframe-animation)
   - [Quản trị động cấu trúc Lab 3D và Specimen Categories](#quản-trị-động-cấu-trúc-lab-3d-và-specimen-categories)
   - [Thống kê người dùng Admin Census & Snapshot](#thống-kê-người-dùng-admin-census--snapshot)
   - [Tự động nạp cấu hình thông minh với DotEnvLoader](#tự-động-nạp-cấu-hình-thông-minh-với-dotenvloader)
   - [Thuật toán Điểm danh Daily Streak](#thuật-toán-điểm-danh-daily-streak)
8. [Hướng dẫn Cài đặt & Vận hành (Deployment & Setup)](#8-hướng-dẫn-cài-đặt--vận-hành-deployment--setup)
   - [Yêu cầu tiên quyết](#yêu-cầu-tiên-quyết)
   - [Biến môi trường (.env) chi tiết](#biến-môi-trường-env-chi-tiết)
   - [Chạy môi trường Local Development](#chạy-môi-trường-local-development)
   - [Chạy kết nối Supabase Cloud](#chạy-kết-nối-supabase-cloud)
   - [Triển khai Production với Docker & Cloudflare Tunnel](#triển-khai-production-với-docker--cloudflare-tunnel)
   - [Quy trình CI/CD với GitHub Actions & Harbor](#quy-trình-cicd-với-github-actions--harbor)
   - [Dữ liệu mẫu khởi tạo (Seed Accounts & Data)](#dữ-liệu-mẫu-khởi-tạo-seed-accounts--data)

---

## 1. Tổng quan dự án

**Bioverse** là giải pháp công nghệ giáo dục (EdTech) hỗ trợ giảng dạy và học tập môn **Khoa học Tự nhiên (KHTN)** cho học sinh cấp Trung học Cơ sở tại Việt Nam (từ Lớp 6 đến Lớp 9), bao gồm ba phân môn: **Sinh học, Vật lý và Hóa học**.

Hệ sinh thái Bioverse giải quyết các rào cản học tập truyền thống bằng cách:
- Cung cấp **kho mô hình 3D sinh học và dụng cụ thí nghiệm tương tác trực quan**, cho phép học sinh xoay, phóng to, mổ xẻ cấu trúc sinh vật và tế bào trực tiếp trên trình duyệt.
- Tích hợp **phòng thí nghiệm tương tác 3D (Phòng Lab)** và **phòng mô phỏng phản ứng hoá học 3D (.chemx keyframe animations)** giúp học sinh quan sát chuyển động phân tử và phản ứng trực quan.
- Tích hợp **Gia sư ảo AI (BioVerse AI Tutor)** có bộ lọc an toàn chuyên sâu (AI Guardrails) giúp giải đáp thắc mắc lý thuyết, giải bài tập và nhắc nhở an toàn phòng thí nghiệm phù hợp với tâm lý lứa tuổi học sinh THCS; hỗ trợ đồng bộ phiên hội thoại vào Firebase Firestore theo từng người dùng.
- Cung cấp **hệ thống đề kiểm tra trắc nghiệm, câu hỏi ôn luyện** chuẩn hóa theo Khối lớp (6-9), Học kỳ (Giữa kỳ, Cuối kỳ) và Môn học kèm đáp án và hình ảnh minh họa chi tiết.
- Tăng tính gắn kết qua cơ chế **Gamification - Chuỗi ngày học liên tục (Daily Streak)**.
- Trang bị **Bảng điều khiển Quản trị (Admin Desk & Census)** tổng hợp thời gian thực thông tin mô hình, file R2, thống kê biểu đồ tăng trưởng học sinh 12 tháng qua.

---

## 2. Ngăn xếp công nghệ (Tech Stack)

| Thành phần | Công nghệ / Thư viện | Vai trò |
| :--- | :--- | :--- |
| **Runtime & Language** | Java 21 LTS | Ngôn ngữ chính, tận dụng Virtual Threads, Pattern Matching và hiệu năng hiện đại |
| **Framework** | Spring Boot 4.1.0, Spring MVC, Spring Security | Xây dựng RESTful API, quản lý Dependency Injection và bảo mật |
| **ORM / Data Access** | Spring Data JPA, Hibernate, PostgreSQL Driver | Tương tác cơ sở dữ liệu quan hệ, tự động map entity |
| **Database Migration** | Flyway Core 10+ (`db/migration`) | Quản lý vòng đời và phiên bản schema database tự động (20 migrations) |
| **Primary Database** | PostgreSQL 16 (Hỗ trợ Local & Supabase) | Lưu trữ người dùng, quyền, đề thi, câu hỏi, mô hình sinh học, phản ứng hóa học |
| **In-Memory Cache & OTP** | Redis 7 (`spring-boot-starter-data-redis`) | Quản lý mã OTP, Blacklist Token, phiên đăng ký tạm; hỗ trợ URI `REDIS_URL` |
| **Object Storage** | Cloudflare R2 (`software.amazon.awssdk:s3` v2) | Lưu trữ file mô hình 3D (.glb, .gltf, textures, binary) và thumbnail crop |
| **AI Integration** | OpenRouter API (`openai/gpt-4o-mini`) | Động cơ suy luận ngôn ngữ tự nhiên cho AI Tutor |
| **AI State / History** | Firebase Admin SDK 9.9.0 (Firestore) | Lưu trữ lịch sử hội thoại AI chat phân tách theo từng người dùng và guest |
| **Email Service** | Brevo SMTP Relay / Gmail SMTP + Thymeleaf Engine | Gửi mã OTP đăng ký và khôi phục mật khẩu qua giao diện HTML responsive |
| **Object Mapping & Tools** | MapStruct 1.5.5, Lombok | Chuyển đổi giữa Entity và DTO tốc độ cao khi biên dịch |
| **API Documentation** | SpringDoc OpenAPI 3.0.3 (Swagger UI) | Tự động sinh tài liệu và giao diện test API tương tác tại `/swagger-ui/index.html` |
| **Environment Loader** | DotEnvLoader (Custom component) | Tự động tìm nạp biến môi trường từ các file `.env` cấp cha/con linh hoạt |
| **Container & CI/CD** | Docker, Docker Compose, Harbor Registry, GitHub Actions | Đóng gói tự động, build multi-stage image và deploy qua SSH lên VPS |
| **Network & Gateway** | Cloudflare Zero Trust Tunnel | Proxy bảo mật truy cập từ domain `bioverse.eraidev.id.vn` vào backend nội bộ |

---

## 3. Cấu trúc mã nguồn (Project Structure)

```
EXE101_group_project_BE/
├── .env.example                       # Mẫu cấu hình biến môi trường
├── .env.prod.example                  # Mẫu cấu hình môi trường Production
├── Dockerfile                         # Build Docker đa giai đoạn (Eclipse Temurin 21)
├── docker-compose.yml                 # Khởi chạy Postgres (5434) & Redis (6380) local
├── docker-compose.prod.yml            # Khởi chạy Backend (8088) + Redis network nội bộ
├── pom.xml                            # Quản lý dependencies Maven
├── deploy/
│   └── cloudflared-bioverse.yml.example # Cấu hình Cloudflare Tunnel ingress
├── .github/
│   └── workflows/
│       └── ci-cd.yml                  # Pipeline CI/CD: build image -> Harbor -> deploy VPS
├── scripts/
│   └── push-db-to-supabase.sh         # Script hỗ trợ đồng bộ schema sang Supabase
└── src/
    ├── main/
    │   ├── java/com/example/exe101_bioverse/
    │   │   ├── Exe101BioverseApplication.java   # Main entry point Spring Boot
    │   │   ├── ai/                              # PHÂN HỆ GIA SƯ ẢO AI & HỘI THOẠI
    │   │   │   ├── client/OpenRouterClient.java # Client HTTP gọi OpenRouter API
    │   │   │   ├── controller/AiChatController.java # Chat & quản lý lịch sử hội thoại
    │   │   │   ├── dto/                         # AiChatRequest, AiChatResponse, ChatMessageResponse...
    │   │   │   ├── guardrail/                   # Regex guardrails kiểm duyệt an toàn câu hỏi
    │   │   │   ├── repository/                  # Firebase Firestore conversation repository
    │   │   │   └── service/                     # AiChatService, AiGuardrailService, System Prompt
    │   │   ├── auth/                            # PHÂN HỆ XÁC THỰC, HỒ SƠ & QUẢN TRỊ NGƯỜI DÙNG
    │   │   │   ├── controller/
    │   │   │   │   ├── AuthController.java      # Đăng ký, đăng nhập, OTP, làm mới token, đăng xuất
    │   │   │   │   ├── ProfileController.java   # Hồ sơ cá nhân /api/users/me, đổi mật khẩu OTP
    │   │   │   │   ├── AdminDeskController.java # Bảng điều khiển Admin tổng hợp (Desk snapshot)
    │   │   │   │   ├── AdminUserController.java # Quản trị tài khoản học sinh & quản trị viên
    │   │   │   │   └── AdminRoleController.java # Quản trị danh sách vai trò phân quyền
    │   │   │   ├── dto/                         # DTOs Request/Response cho Auth, User, Role, Census
    │   │   │   ├── entity/                      # User, Role, UserSession
    │   │   │   ├── enums/                       # UserStatus, GenderType, OtpPurpose
    │   │   │   ├── mapper/                      # UserMapper, RoleMapper (MapStruct)
    │   │   │   ├── repository/                  # UserRepository, RoleRepository, UserSessionRepository
    │   │   │   ├── security/                    # JwtAuthenticationFilter, UserPrincipal, CustomUserDetailsService
    │   │   │   └── service/                     # AuthService, JwtService, OtpService, MailService, AdminDeskService...
    │   │   ├── common/                          # CẤU HÌNH & HẠ TẦNG DÙNG CHUNG
    │   │   │   ├── config/                      # SecurityConfig, RedisConfig, FirebaseConfig, Swagger, DotEnvLoader
    │   │   │   ├── exception/                   # AppException, ErrorCode (1000 - 1511), GlobalExceptionHandler
    │   │   │   ├── response/                    # ApiResponse, PageResponse
    │   │   │   └── seed/DataSeeder.java         # Khởi tạo tài khoản mẫu mặc định (Admin, 2 Students)
    │   │   ├── exam/                            # PHÂN HỆ KHỐI LỚP, HỌC KỲ, MÔN HỌC & ĐỀ THI
    │   │   │   ├── controller/                  # Grade, Semester, Subject, Exam, Question, Answer, Images
    │   │   │   ├── dto/                         # DTOs Request/Response cho taxonomy đề thi
    │   │   │   ├── entity/                      # ClassEntity, Semester, Subject, Exam, Question, Answer, Images...
    │   │   │   ├── enums/                       # ExamType, QuestionType, AnswerType
    │   │   │   ├── mapper/                      # Mappers cho Exam, Question, Answer
    │   │   │   ├── repository/                  # Repositories JPA cho taxonomy & câu hỏi
    │   │   │   └── service/                     # GradeService, SemesterService, SubjectService, ExamService...
    │   │   ├── model/                           # PHÂN HỆ MÔ HÌNH 3D, LAB & PHẢN ỨNG HÓA HỌC
    │   │   │   ├── controller/
    │   │   │   │   ├── BioModelController.java       # Catalog, Featured, Detail theo ID/Slug, Categories, Labs
    │   │   │   │   ├── ReactionController.java       # Phương trình hoá học xem công khai
    │   │   │   │   ├── AdminBioModelController.java  # Quản trị model, toggle featured, upload thumbnail R2
    │   │   │   │   ├── AdminCategoryController.java  # Quản trị loại mẫu catalog
    │   │   │   │   ├── AdminLabController.java       # Quản trị danh mục phòng Lab 3D
    │   │   │   │   └── AdminReactionController.java  # Quản trị cấu hình phản ứng .chemx
    │   │   │   ├── dto/                         # Request/Response models, categories, labs, reactions
    │   │   │   ├── entity/                      # BioModel, BioModelCategory, BioLab, ReactionEquation
    │   │   │   ├── repository/                  # BioModelRepository, Category, Lab, Reaction repositories
    │   │   │   ├── service/                     # BioModelService, AdminBioModelService, LabService, ReactionService
    │   │   │   └── util/SlugUtil.java           # Tiện ích chuyển đổi chuỗi tiếng Việt thành slug URL
    │   │   ├── storage/                         # PHÂN HỆ LƯU TRỮ CLOUDFLARE R2
    │   │   │   ├── controller/ModelAssetController.java # Reverse Proxy streaming file 3D (.glb/.gltf)
    │   │   │   ├── dto/ModelAssetResponse.java
    │   │   │   └── service/R2StorageService.java# Tải danh sách, upload thumbnail, streaming nhị phân
    │   │   └── streak/                          # PHÂN HỆ ĐIỂM DANH NGÀY HỌC (STREAK)
    │   │       ├── controller/StreakController.java # Lấy streak hiện tại và thực hiện check-in
    │   │       ├── dto/response/StreakResponse.java
    │   │       ├── entity/UserStreak.java
    │   │       ├── repository/UserStreakRepository.java
    │   │       └── service/StreakService.java
    │   └── resources/
    │       ├── application.properties           # Cấu hình chính (Port, JPA, Timeout, Mail, R2, AI, Redis)
    │       ├── application-local.properties     # Cấu hình kết nối Docker Postgres nội bộ (5434)
    │       ├── application-supabase.properties  # Cấu hình kết nối Supabase Session Pooler (5432)
    │       ├── db/migration/                    # 20 file Flyway Migration (V1 -> V20)
    │       └── templates/mail/otp.html          # Mẫu giao diện Email gửi mã OTP responsive
    └── test/
        └── java/com/example/exe101_bioverse/    # Unit & Integration Tests
```

---

## 4. Mô hình dữ liệu & Database Migrations

Hệ thống quản lý cơ sở dữ liệu phiên bản tự động bằng **Flyway Migration** (`src/main/resources/db/migration`). Toàn bộ 20 migrations được đánh số thứ tự tuần tự:

| File Migration | Tên Schema | Nội dung chi tiết |
| :--- | :--- | :--- |
| **`V1__init_schema.sql`** | Init Exam & Question | Bảng cơ bản `exam`, `question`, `exam_question` |
| **`V2__user_auth_schema.sql`** | User & Auth | Bảng `users`, `user_sessions`, ENUMs (`user_role`, `user_status`, `gender_type`) |
| **`V3__subscription_schema.sql`** | Subscription Plans | Bảng `plans`, `user_subscriptions` (gói cước tháng, quý, năm) |
| **`V4__payment_schema.sql`** | Payment Schema | Bảng `payments` (tích hợp thanh toán PayOS orderCode, transactionId, trạng thái) |
| **`V5__content_taxonomy_schema.sql`** | Content Taxonomy | Phân cấp bài học: `grade_levels` (Lớp 6-9) → `subjects` → `chapters` → `lessons` |
| **`V6__chemicals_schema.sql`** | Chemicals Library | Bảng `chemicals`, `chemical_reactions` (tính chất hóa học, khối lượng phân tử, màu sắc) |
| **`V7__experiments_schema.sql`** | Virtual Experiments | Bảng `experiments`, `experiment_steps`, `experiment_equipment`, `experiment_scene_objects` |
| **`V8__bio_models_schema.sql`** | Bio Models | Bảng `bio_models` (tên khoa học, phân loại học, môi trường sống, đặc điểm, 3D asset path) |
| **`V9__exam_enhancement_schema.sql`** | Exam Enhancement | Bổ sung `subject_id`, `grade_level_id`, `duration_minutes`, `total_score` cho bảng `exam` |
| **`V10__shared_objects_schema.sql`** | Shared 3D Assets | Bảng `shared_3d_objects` (thư viện đồ dùng thí nghiệm: ống nghiệm, đèn cồn, cốc đong...) |
| **`V11__roles_table.sql`** | Dynamic Roles | Tách bảng `roles` riêng (`STUDENT`, `ADMIN`), liên kết khóa ngoại `users.role_id` |
| **`V12__seed_plans.sql`** | Seed Subscription | Khởi tạo dữ liệu gói học viên: Gói Tháng (49k), Gói Quý (119k), Gói Năm (399k) |
| **`V13__users_grade.sql`** | User Grade | Bổ sung cột `grade` (giới hạn từ lớp 6 đến 9) cho bảng `users` |
| **`V14__add_featured_and_metadata_to_bio_models.sql`** | Bio Models Metadata | Bổ sung `slug`, `is_featured`, `views_count`, `grade`, `subject`, `badge_text`, `action_text` |
| **`V15__user_streaks.sql`** | Daily Streak | Bảng `user_streaks` theo dõi chuỗi ngày học liên tục, kỷ lục dài nhất, ngày điểm danh cuối |
| **`V16__model_categories_and_labs.sql`** | Model Categories & Labs | Bảng `bio_model_categories` (loại mẫu catalog) & `bio_labs` (phòng lab 3D gắn với nhân mẫu) |
| **`V17__reaction_equations.sql`** | Reaction Equations | Bảng `reaction_equations` chứa dữ liệu JSONB `.chemx` mô phỏng phản ứng hoá học theo keyframe |
| **`V18__seed_more_reactions.sql`** | Seed Reactions | Nạp sẵn hàng loạt hoạt cảnh phản ứng: quang hợp, hô hấp tế bào, trung hòa axit - bazơ... |
| **`V19__exam_class_semester_subject_schema.sql`** | Exam Taxonomy | Hệ thống phân loại đề thi: `exam_classes`, `exam_semesters`, `exam_subjects` |
| **`V20__seed_exam_data_khtn_grade_6_to_9.sql`** | Seed Exam Data | Nạp 16 bộ đề thi mẫu KHTN đầy đủ cho Lớp 6, 7, 8, 9 (Giữa kỳ & Cuối kỳ) kèm câu hỏi, đáp án, ảnh |

---

## 5. Quy chuẩn API & Bảng mã lỗi

### 5.1. Cấu trúc Phản hồi chuẩn (`ApiResponse<T>`)
Tất cả các API trả về phản hồi định dạng JSON đồng nhất:
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
    "items": [ ... ],
    "page": 0,
    "size": 20,
    "totalElements": 45,
    "totalPages": 3
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
| **`1009`** | `500 Internal Error` | `ROLE_NOT_CONFIGURED` | Vai trò mặc định chưa được khởi tạo trong hệ thống |
| **`1010`** | `400 Bad Request` | `OTP_INVALID` | Mã OTP không đúng |
| **`1011`** | `400 Bad Request` | `OTP_EXPIRED` | Mã OTP đã hết hạn hoặc chưa từng được gửi |
| **`1012`** | `429 Too Many Requests` | `OTP_RESEND_TOO_SOON` | Chưa hết thời gian chờ cooldown gửi lại OTP |
| **`1013`** | `429 Too Many Requests` | `OTP_MAX_ATTEMPTS` | Nhập sai OTP quá 5 lần |
| **`1014`** | `400 Bad Request` | `PENDING_REGISTRATION_EXPIRED` | Phiên đăng ký tạm trong Redis đã hết hạn |
| **`1015`** | `401 Unauthorized` | `INVALID_RESET_TOKEN` | Phiên đặt lại mật khẩu không hợp lệ hoặc hết hạn |
| **`1016`** | `500 Internal Error` | `EMAIL_SEND_FAILED` | Không thể gửi email qua máy chủ SMTP |
| **`1017`** | `403 Forbidden` | `CANNOT_MODIFY_OWN_ACCOUNT` | Không thể tự đổi quyền hoặc khóa chính tài khoản mình |
| **`1018`** | `409 Conflict` | `LAST_ACTIVE_ADMIN` | Không thể khóa hoặc hạ quyền của Admin hoạt động cuối cùng |
| **`1019`** | `404 Not Found` | `ROLE_NOT_FOUND` | Không tìm thấy vai trò |
| **`1020`** | `409 Conflict` | `ROLE_CODE_ALREADY_EXISTS` | Mã vai trò đã tồn tại trong hệ thống |
| **`1021`** | `409 Conflict` | `ROLE_IN_USE` | Không thể xóa vai trò đang được gán cho người dùng |
| **`1022`** | `403 Forbidden` | `SYSTEM_ROLE_PROTECTED` | Không thể xóa hoặc đổi mã vai trò hệ thống (`ADMIN`, `STUDENT`) |
| **`1400`** | `400 Bad Request` | `INVALID_DATA` | Dữ liệu đầu vào vi phạm ràng buộc validation |
| **`1403`** | `403 Forbidden` | `ACCESS_DENIED` | Không có quyền truy cập tài nguyên |
| **`1410`** | `400 Bad Request` | `INVALID_FILE` | Định dạng file tải lên không hợp lệ |
| **`1411`** | `400 Bad Request` | `FILE_TOO_LARGE` | Kích thước file vượt quá giới hạn cho phép |
| **`1501`** | `404 Not Found` | `MODEL_NOT_FOUND` | Không tìm thấy mô hình 3D |
| **`1502`** | `503 Service Unavailable` | `STORAGE_NOT_CONFIGURED` | Chưa cấu hình thông số Cloudflare R2 |
| **`1503`** | `502 Bad Gateway` | `STORAGE_ERROR` | Lỗi kết nối hoặc truyền tải dữ liệu từ Cloudflare R2 |
| **`1504`** | `404 Not Found` | `CATEGORY_NOT_FOUND` | Không tìm thấy loại mẫu catalog |
| **`1505`** | `409 Conflict` | `CATEGORY_NAME_EXISTS` | Tên loại mẫu đã tồn tại |
| **`1506`** | `404 Not Found` | `LAB_NOT_FOUND` | Không tìm thấy phòng Lab |
| **`1507`** | `409 Conflict` | `LAB_CODE_EXISTS` | Mã phòng Lab đã tồn tại |
| **`1508`** | `403 Forbidden` | `SYSTEM_LAB_PROTECTED` | Không thể xóa hoặc đổi mã phòng Lab hệ thống |
| **`1509`** | `404 Not Found` | `REACTION_NOT_FOUND` | Không tìm thấy phương trình hoá học |
| **`1510`** | `409 Conflict` | `REACTION_CODE_EXISTS` | Mã phương trình hoá học đã tồn tại |
| **`1511`** | `403 Forbidden` | `SYSTEM_REACTION_PROTECTED` | Không thể ẩn hoặc đổi mã phương trình hoá học hệ thống |

---

## 6. Danh sách API Endpoints chi tiết

### 6.1. Xác thực & Tài khoản (Authentication)
**Prefix:** `/api/auth`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Public | Bắt đầu quy trình đăng ký, lưu tạm Redis và gửi OTP về email (bắt buộc grade 6-9) |
| `POST` | `/api/auth/verify-register` | Public | Xác thực mã OTP, tạo bản ghi tài khoản học sinh và trả về cặp JWT tokens |
| `POST` | `/api/auth/resend-otp` | Public | Gửi lại mã OTP (tuân thủ giới hạn cooldown 60 giây) |
| `POST` | `/api/auth/forgot-password` | Public | Yêu cầu gửi OTP khôi phục mật khẩu đến email |
| `POST` | `/api/auth/verify-reset-otp` | Public | Xác thực mã OTP quên mật khẩu và nhận `resetToken` dùng 1 lần (TTL 10 phút) |
| `POST` | `/api/auth/reset-password` | Public | Đặt lại mật khẩu mới thông qua `resetToken` |
| `POST` | `/api/auth/login` | Public | Đăng nhập bằng Email và Password, trả về Access Token & Refresh Token |
| `POST` | `/api/auth/refresh` | Public | Cấp lại Access Token mới từ Refresh Token hợp lệ |
| `POST` | `/api/auth/logout` | Authenticated | Đăng xuất phiên hiện tại, đưa Access Token vào Redis Blacklist |
| `POST` | `/api/auth/logout-all` | Authenticated | Đăng xuất toàn bộ thiết bị: thu hồi mọi session và vô hiệu hóa token tương ứng |

### 6.2. Hồ sơ cá nhân & Đổi mật khẩu (User Profile)
**Prefix:** `/api/users/me`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/users/me` | Authenticated | Lấy thông tin tài khoản hiện tại và tự động ghi nhận điểm danh streak ngày hôm nay |
| `PATCH` | `/api/users/me` | Authenticated | Cập nhật hồ sơ (họ tên, ngày sinh, giới tính, khối lớp). Không đổi email/password tại đây |
| `POST` | `/api/users/me/password/otp` | Authenticated | Gửi mã OTP xác nhận đến email của người dùng trước khi đổi mật khẩu |
| `POST` | `/api/users/me/password` | Authenticated | Đổi mật khẩu sau khi nhập đúng OTP; thu hồi các phiên đăng nhập cũ |

### 6.3. Chuỗi học tập (User Streak)
**Prefix:** `/api/users/me/streak`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/users/me/streak` | Authenticated | Lấy trạng thái chuỗi học tập (current streak, longest streak) mà không ghi nhận điểm danh |
| `POST` | `/api/users/me/streak/check-in` | Authenticated | Ghi nhận lượt học hôm nay, tính toán cộng dồn chuỗi liên tục theo giờ Việt Nam |

### 6.4. Bảng điều khiển Quản trị viên (Admin Desk & Census)
**Prefix:** `/api/admin/desk`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/desk` | Role `ADMIN` | Tải một lượt toàn bộ dữ liệu snapshot: 20 models gần nhất, 20 users gần nhất, danh sách file R2, trạng thái kết nối R2 và thống kê Census (tổng học sinh, tăng trưởng theo tuần/tháng, biểu đồ 12 tháng) |

### 6.5. Quản trị Người dùng & Vai trò (Admin Users & Roles)
**Prefix:** `/api/admin` (Yêu cầu vai trò `ADMIN`)

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/users` | Role `ADMIN` | Danh sách người dùng (tìm kiếm `q`, lọc theo `status`, `role`, phân trang) |
| `GET` | `/api/admin/users/{id}` | Role `ADMIN` | Xem chi tiết thông tin một người dùng |
| `PATCH` | `/api/admin/users/{id}` | Role `ADMIN` | Cập nhật hồ sơ, vai trò, trạng thái khóa/mở khóa tài khoản (bảo vệ tài khoản admin cuối) |
| `GET` | `/api/admin/roles` | Role `ADMIN` | Danh sách tất cả vai trò trong hệ thống (có phân trang và tìm kiếm) |
| `GET` | `/api/admin/roles/{id}` | Role `ADMIN` | Xem chi tiết một vai trò theo ID |
| `POST` | `/api/admin/roles` | Role `ADMIN` | Tạo vai trò mới (yêu cầu mã vai trò không trùng lặp) |
| `PATCH` | `/api/admin/roles/{id}` | Role `ADMIN` | Sửa tên, mô tả hoặc mã vai trò (cấm sửa vai trò hệ thống) |
| `DELETE` | `/api/admin/roles/{id}` | Role `ADMIN` | Xóa vai trò (không thể xóa vai trò hệ thống hoặc vai trò đang gán cho người dùng) |

### 6.6. Quản trị Mô hình 3D, Loại mẫu & Phòng Lab (Admin Models, Categories, Labs)
**Prefix:** `/api/admin` (Yêu cầu vai trò `ADMIN`)

#### A. Quản lý Mô hình 3D (`/api/admin/models`)
| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/models` | Role `ADMIN` | Danh sách mô hình 3D (lọc theo từ khóa `q`, `isFeatured`, `isActive`, phân trang) |
| `GET` | `/api/admin/models/{id}` | Role `ADMIN` | Xem chi tiết thông tin kỹ thuật của mô hình 3D |
| `POST` | `/api/admin/models` | Role `ADMIN` | Tạo mới một mô hình 3D |
| `PUT` | `/api/admin/models/{id}` | Role `ADMIN` | Cập nhật toàn bộ thông tin mô hình 3D |
| `PATCH` | `/api/admin/models/{id}/featured` | Role `ADMIN` | Bật/tắt ghim nổi bật lên trang chủ kèm chỉ số `sortOrder` |
| `GET` | `/api/admin/models/assets` | Role `ADMIN` | Liệt kê các file 3D (.glb, .gltf) hiện có trên bucket Cloudflare R2 |
| `POST` | `/api/admin/models/thumbnail` | Role `ADMIN` | Upload ảnh thumbnail đã crop lên R2 (`multipart/form-data`, hỗ trợ JPG, PNG, WebP ≤ 8MB) |
| `DELETE` | `/api/admin/models/{id}` | Role `ADMIN` | Xóa mềm mô hình 3D (chuyển `isActive = false`, tắt cờ featured) |

#### B. Quản lý Loại mẫu Catalog (`/api/admin/model-categories`)
| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/model-categories` | Role `ADMIN` | Danh sách loại mẫu catalog (kể cả đang ẩn) sắp xếp theo `sortOrder` |
| `POST` | `/api/admin/model-categories` | Role `ADMIN` | Tạo loại mẫu mới kèm tên và slug duy nhất |
| `PATCH` | `/api/admin/model-categories/{id}` | Role `ADMIN` | Cập nhật thông tin loại mẫu, thứ tự hiển thị, môn học hoặc trạng thái ẩn/hiện |
| `DELETE` | `/api/admin/model-categories/{id}` | Role `ADMIN` | Ẩn loại mẫu (`isActive = false`) |

#### C. Quản lý Phòng Lab 3D (`/api/admin/labs`)
| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/labs` | Role `ADMIN` | Danh sách phòng Lab 3D (kể cả đang ẩn) |
| `POST` | `/api/admin/labs` | Role `ADMIN` | Tạo phòng Lab mới (mã `code`, tên, mô tả, thứ tự) |
| `PATCH` | `/api/admin/labs/{id}` | Role `ADMIN` | Chỉnh sửa cấu hình phòng Lab |
| `DELETE` | `/api/admin/labs/{id}` | Role `ADMIN` | Ẩn phòng Lab (bảo vệ các phòng Lab hệ thống mặc định) |

### 6.7. Quản trị Phương trình Hoá học 3D (Admin Reaction Equations)
**Prefix:** `/api/admin/reactions` (Yêu cầu vai trò `ADMIN`)

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/reactions` | Role `ADMIN` | Danh sách phương trình hoá học (kể cả đang ẩn) |
| `POST` | `/api/admin/reactions` | Role `ADMIN` | Thêm phương trình mới từ nội dung JSON `.chemx` keyframes |
| `PATCH` | `/api/admin/reactions/{id}` | Role `ADMIN` | Sửa tiêu đề, mô tả, nhãn khối lớp, nội dung animation hoặc trạng thái |
| `DELETE` | `/api/admin/reactions/{id}` | Role `ADMIN` | Ẩn phương trình (không thể xóa hoặc ẩn phương trình hệ thống cốt lõi) |

### 6.8. Mô hình 3D & Phòng Lab công khai (Public Bio Models & Labs)
**Prefix:** `/api/models`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/models/featured` | Public | Lấy danh sách mô hình 3D nổi bật hiển thị ở trang chủ (sắp xếp theo `sortOrder`) |
| `GET` | `/api/models/catalog` | Public | Danh mục mô hình có phân trang, bộ lọc theo `grade` (6-9), `category`, `subject`, tìm kiếm `q` |
| `GET` | `/api/models/detail/{id}` | Public | Xem thông tin chi tiết mô hình theo ID (tự động tăng `views_count`) |
| `GET` | `/api/models/slug/{slug}` | Public | Xem chi tiết mô hình theo đường dẫn slug thân thiện SEO (tự động tăng `views_count`) |
| `GET` | `/api/models/categories` | Public | Lấy danh sách tên thể loại mô hình đang mở (tùy chọn lọc theo `subject`) |
| `GET` | `/api/models/labs` | Public | Lấy danh sách phòng Lab 3D đang hoạt động cho học sinh truy cập |

### 6.9. Phương trình Hoá học công khai (Public Reactions)
**Prefix:** `/api/reactions`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/reactions` | Public | Lấy danh sách phương trình hoá học 3D đang kích hoạt cho học sinh |
| `GET` | `/api/reactions/{code}` | Public | Lấy chi tiết một phương trình kèm dữ liệu JSON animation `.chemx` theo mã `code` |

### 6.10. Lưu trữ & Streaming Asset 3D (R2 Storage Proxy)
**Prefix:** `/api/models`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/models` hoặc `/api/models/assets` | Public | Liệt kê các key asset mô hình 3D đang có trên Cloudflare R2 |
| `GET` / `HEAD` | `/api/models/{*objectKey}` | Public | Reverse streaming tải file nhị phân (.glb, .gltf, textures, thumbnails) trực tiếp từ R2 về client |

> *Lưu ý:* Các từ khóa đặt trước (`assets`, `catalog`, `categories`, `featured`, `labs`, `detail`, `slug`) được bảo vệ để không nhầm lẫn với objectKey của file 3D.

### 6.11. Trợ lý Trí tuệ nhân tạo (AI Tutor & Conversations)
**Prefix:** `/api/ai`

| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/ai/chat` | Public / Auth | Gửi câu hỏi cho Gia sư AI (hỗ trợ cả khách và học sinh đã đăng nhập để lưu vào hồ sơ) |
| `GET` | `/api/ai/conversations` | Authenticated | Lấy danh sách các cuộc hội thoại của học sinh (phân trang dựa trên cursor, mặc định 20 cuộc) |
| `GET` | `/api/ai/conversations/{id}/messages` | Authenticated | Lấy toàn bộ lịch sử tin nhắn của một cuộc hội thoại cụ thể thuộc sở hữu của học sinh |
| `DELETE` | `/api/ai/conversations/{id}` | Authenticated | Xóa một cuộc hội thoại khỏi tài khoản học sinh |

**Cấu trúc Request Body Chat (`/api/ai/chat`):**
```json
{
  "conversationId": "uuid-string-or-null",
  "question": "Trùng roi xanh di chuyển bằng cách nào và có những đặc điểm quang hợp gì?"
}
```

### 6.12. Hệ thống Khối lớp, Học kỳ, Môn học & Đề thi (Exam Taxonomy)

#### A. Quản lý Khối lớp (`/api/classes`)
| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/classes` | Public | Tạo khối lớp mới (grade: 6, 7, 8, 9) |
| `GET` | `/api/classes` | Public | Lấy danh sách tất cả khối lớp |
| `GET` | `/api/classes/{id}` | Public | Lấy chi tiết khối lớp theo ID |
| `GET` | `/api/classes/grade/{grade}` | Public | Lấy chi tiết khối lớp theo số lớp (6, 7, 8, 9) |
| `PUT` | `/api/classes/{id}` | Public | Cập nhật thông tin khối lớp |
| `DELETE` | `/api/classes/{id}` | Public | Xóa khối lớp |

#### B. Quản lý Học kỳ (`/api/semesters`)
| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/semesters` | Public | Tạo học kỳ mới liên kết khối lớp (`classId`) |
| `GET` | `/api/semesters` | Public | Lấy danh sách tất cả học kỳ |
| `GET` | `/api/semesters/{id}` | Public | Lấy chi tiết học kỳ theo ID |
| `GET` | `/api/semesters/class/{classId}` | Public | Lấy danh sách học kỳ thuộc một khối lớp |
| `PUT` | `/api/semesters/{id}` | Public | Cập nhật thông tin học kỳ |
| `DELETE` | `/api/semesters/{id}` | Public | Xóa học kỳ |

#### C. Quản lý Môn học theo học kỳ (`/api/subjects`)
| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/subjects` | Public | Tạo môn học mới liên kết học kỳ (`semesterId`) |
| `GET` | `/api/subjects` | Public | Lấy danh sách tất cả môn học |
| `GET` | `/api/subjects/{id}` | Public | Lấy chi tiết môn học theo ID |
| `GET` | `/api/subjects/semester/{semesterId}` | Public | Lấy danh sách môn học thuộc một học kỳ |
| `PUT` | `/api/subjects/{id}` | Public | Cập nhật thông tin môn học |
| `DELETE` | `/api/subjects/{id}` | Public | Xóa môn học |

#### D. Quản lý Đề thi, Câu hỏi, Đáp án & Hình ảnh
| Method | Endpoint | Quyền | Mô tả chức năng |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/exams` | Public | Tạo mới bài kiểm tra (có thể gắn `subjectId`) |
| `GET` | `/api/exams` | Public | Lấy danh sách tất cả bài kiểm tra |
| `GET` | `/api/exams/subject/{subjectName}` | Public | Lọc đề thi theo tên môn học (Sinh học, Hóa học, Vật lý) |
| `GET` | `/api/exams/type/{type}` | Public | Lọc đề thi theo loại (15 phút, 1 tiết, giữa kỳ, cuối kỳ) |
| `GET` | `/api/exams/name/{name}` | Public | Tìm đề thi theo tiêu đề |
| `GET` | `/api/exams/code/{code}` | Public | Tìm đề thi theo mã đề duy nhất |
| `POST` | `/api/questions` | Public | Tạo câu hỏi mới |
| `GET` | `/api/questions/{id}` | Public | Lấy chi tiết câu hỏi theo ID |
| `GET` | `/api/questions/exam/{examId}` | Public | Lấy tất cả câu hỏi thuộc về một đề thi |
| `GET` | `/api/questions/type/{questionType}` | Public | Lọc câu hỏi theo dạng (SINGLE_CHOICE, MULTIPLE_CHOICE...) |
| `POST` | `/api/exam-questions` | Public | Gán câu hỏi vào đề thi kèm thứ tự câu và điểm số |
| `GET` | `/api/exam-questions/exam/{examId}` | Public | Lấy cấu trúc đề thi và danh sách câu hỏi gán kèm điểm |
| `GET` | `/api/exam-questions/exam/{examId}/question/{questionId}` | Public | Lấy chi tiết một câu hỏi cụ thể trong cấu trúc đề thi |
| `POST` | `/api/answers` | Public | Tạo đáp án lựa chọn cho câu hỏi |
| `GET` | `/api/answers/question/{questionId}` | Public | Lấy danh sách đáp án của một câu hỏi |
| `POST` | `/api/question-images` | Public | Đính kèm hình ảnh minh họa cho câu hỏi |
| `GET` | `/api/question-images/question/{questionId}` | Public | Lấy danh sách ảnh minh họa của câu hỏi |
| `POST` | `/api/answer-images` | Public | Đính kèm hình ảnh minh họa cho đáp án |
| `GET` | `/api/answer-images/answer/{answerId}` | Public | Lấy danh sách ảnh minh họa của đáp án |

---

## 7. Các cơ chế kỹ thuật cốt lõi (Core Mechanics)

### Bảo mật & Phân quyền (Stateless JWT & Redis Blacklist)
- Hệ thống áp dụng mô hình xác thực phi trạng thái (**Stateless Authentication**) sử dụng `JwtAuthenticationFilter` chạy trước `UsernamePasswordAuthenticationFilter`.
- Access Token có thời hạn ngắn (mặc định 1 giờ), được ký mã hóa bằng thuật toán `HS256` với khóa bí mật tối thiểu 32 ký tự.
- Khi người dùng đăng xuất (`/api/auth/logout`), Access Token được đưa vào Redis Blacklist (`token_blacklist:{jti}`) với TTL bằng thời gian còn lại của token để hủy hiệu lực ngay lập tức.
- Khi người dùng chọn đăng xuất khỏi tất cả thiết bị (`/api/auth/logout-all`), hệ thống xóa toàn bộ session trong bảng `user_sessions` và đưa jti vào Redis blacklist.

### Xác thực OTP Email đa tầng với Redis
- Quy trình đăng ký tài khoản không tạo ngay bản ghi trong bảng `users` nhằm chống spam rác dữ liệu.
- Thông tin đăng ký được lưu tạm trong Redis (`pending_registration:{email}`) kèm mã OTP 6 chữ số ngẫu nhiên có TTL 10 phút.
- Giao diện thư điện tử HTML được render qua template engine Thymeleaf chuyên nghiệp.
- Áp dụng cơ chế bảo vệ kép:
  - **Cooldown:** Giới hạn gửi lại mã OTP tối thiểu 60 giây (`otp_resend_cooldown:{email}`).
  - **Brute-force limit:** Khóa phiên nếu nhập sai mã OTP quá 5 lần (`otp_attempts:{email}`).

### AI Guardrails & Multi-turn Chat cá nhân hóa (Firestore + OpenRouter)
Phân hệ AI Tutor sử dụng mô hình ngôn ngữ lớn thông qua OpenRouter API kết hợp cơ chế kiểm duyệt nội dung nhiều tầng:
1. **Kiểm tra đầu vào:** Giới hạn tối đa 1200 ký tự cho mỗi câu hỏi.
2. **Bộ lọc Guardrails (`AiGuardrailServiceImpl`):** Sử dụng các mẫu Regex phát hiện và từ chối các nhóm nội dung không phù hợp:
   - *Ngoài phạm vi:* Lập trình máy tính, làm văn phân tích, tiền điện tử, chính trị, game hack.
   - *Nguy hiểm lý - hóa:* Chế tạo thuốc nổ, vũ khí, khí độc, bẫy điện gây giật, ma túy.
   - *Rủi ro y tế / sinh học:* Tự chẩn đoán bệnh tật, kê đơn thuốc, nuôi cấy vi khuẩn nguy hiểm.
   - *Tiêu cực & bạo lực:* Tự hại, khiêu dâm, bạo lực, gian lận thi cử.
3. **Cơ chế Safe Academic Answer:** Đối với các câu hỏi học thuật chứa từ khóa nhạy cảm (như "axit clohidric", "phản ứng cháy", "vi khuẩn"), hệ thống không từ chối thô bạo mà tự động bổ sung cờ an toàn vào System Prompt: chỉ giải thích lý thuyết thuần túy và luôn nhắc nhở nguyên tắc an toàn thí nghiệm dưới sự hướng dẫn của thầy cô.
4. **Lưu trữ đa tầng ngữ cảnh:** Tích hợp Firebase Firestore để lưu trữ lịch sử cuộc hội thoại theo từng người dùng (nếu đã đăng nhập) hoặc guest. Hệ thống tự động gửi kèm 8 tin nhắn gần nhất để AI duy trì mạch giao tiếp tự nhiên.
5. **Quản lý hội thoại cá nhân:** Hỗ trợ xem danh sách hội thoại theo cơ chế phân trang con trỏ (cursor pagination), đọc lại tin nhắn cũ và xóa hội thoại qua các endpoint `/api/ai/conversations/**`.

### Streaming 3D Models qua R2 Reverse Proxy
- Để bảo mật bucket Cloudflare R2 nội bộ và tránh các lỗi liên quan đến CORS trên trình duyệt, Backend đóng vai trò làm **Reverse Streaming Proxy**.
- Endpoint `GET /api/models/{*objectKey}` sử dụng Spring `StreamingResponseBody` để truyền tải trực tiếp dòng dữ liệu nhị phân từ R2 về trình duyệt client mà không cần lưu tạm vào bộ nhớ RAM máy chủ.
- Hỗ trợ đầy đủ phương thức `HEAD` và các header HTTP quan trọng (`Content-Length`, `Content-Type`, `Content-Disposition`), cho phép thư viện Three.js / Babylon.js phía Frontend tải mô hình 3D mượt mà.

### Mô phỏng Hoá học 3D với .chemx Keyframe Animation
- Thay vì chỉ cung cấp phương trình chữ hoặc hình tĩnh, Bioverse xây dựng định dạng hoạt cảnh nguyên tử hoá học theo thời gian thực `.chemx`.
- Dữ liệu `.chemx` lưu dưới dạng `JSONB` trong PostgreSQL bảng `reaction_equations`, quy định tọa độ `x, y, z`, điện tích, ký hiệu nguyên tử và các liên kết hoá học (covalent, ionic...) tại từng mốc thời gian (timestamp).
- Admin có thể tạo, chỉnh sửa hoặc cập nhật phản ứng thông qua `/api/admin/reactions`, cho phép Frontend render tương tác 3D chân thực bài học Hóa học THCS.

### Quản trị động cấu trúc Lab 3D và Specimen Categories
- Khắc phục tình trạng hardcode danh mục ở Frontend: Toàn bộ danh mục mẫu vật (`bio_model_categories`) và phòng thí nghiệm ảo (`bio_labs`) đều được quản trị động bằng cơ sở dữ liệu.
- Phân biệt giữa các đối tượng mặc định của hệ thống (`is_system = true`, được bảo vệ không thể xóa) và đối tượng tùy chỉnh do Admin bổ sung.
- Bổ sung tiện ích `SlugUtil` tự động chuẩn hóa chuỗi tiếng Việt có dấu thành slug chuẩn SEO (hỗ trợ chuyển đổi cả chữ `đ`/`Đ`).

### Thống kê người dùng Admin Census & Snapshot
- Endpoint `/api/admin/desk` tổng hợp dữ liệu tốc độ cao phục vụ dashboard:
  - Thống kê tổng số học sinh, số tài khoản đang bị khóa, số tài khoản mới trong tuần và trong tháng.
  - Phân tích tăng trưởng người dùng theo từng tháng trong vòng 12 tháng gần nhất (`MonthBucket`).
  - Kiểm tra kết nối dịch vụ lưu trữ Cloudflare R2 (`r2Ok`).

### Tự động nạp cấu hình thông minh với DotEnvLoader
- Hỗ trợ component `DotEnvLoader` tự động quét và phân tích file `.env` từ thư mục làm việc hiện tại hoặc các cấp thư mục cha/con lồng nhau mà không phụ thuộc vào cách IDE khởi chạy.
- Hỗ trợ kết nối Redis qua chuỗi kết nối duy nhất `REDIS_URL` (hỗ trợ cả giao thức `redis://` và `rediss://` có mã hóa TLS như Upstash hoặc Redis Cloud).

### Thuật toán Điểm danh Daily Streak
- Mỗi người dùng sở hữu một bản ghi `UserStreak` liên kết với tài khoản.
- Tính toán dựa trên múi giờ Việt Nam (`Asia/Ho_Chi_Minh`):
  - Khi người dùng đăng nhập hoặc bấm điểm danh (`check-in`):
    - Nếu đã điểm danh trong ngày hôm nay: Không thay đổi số ngày liên tiếp.
    - Nếu điểm danh vào ngày kế tiếp (liên tục): Tăng `current_streak` thêm 1. Cập nhật `longest_streak` nếu vượt qua kỷ lục cũ.
    - Nếu bị ngắt quãng quá 1 ngày: Đặt lại `current_streak = 1`, giữ nguyên `longest_streak`.

---

## 8. Hướng dẫn Cài đặt & Vận hành (Deployment & Setup)

### Yêu cầu tiên quyết
- **Java Development Kit (JDK):** Phiên bản 21 trở lên (Khuyến nghị Eclipse Temurin 21)
- **Apache Maven:** 3.9+ (hoặc sử dụng wrapper `./mvnw` / `mvnw.cmd`)
- **Docker & Docker Compose:** Đã cài đặt và đang chạy dịch vụ Docker engine
- **Tài nguyên dịch vụ bên ngoài:**
  - Khóa API OpenRouter (`OPENROUTER_API_KEY`)
  - Tài khoản SMTP gửi mail (Khuyến nghị Brevo SMTP hoặc Gmail App Password)
  - File khóa riêng tư Firebase: `firebase/serviceAccountKey.json`
  - Bucket Cloudflare R2 lưu trữ 3D Models (`R2_ACCOUNT_ID`, `R2_ACCESS_KEY_ID`, `R2_SECRET_ACCESS_KEY`)

### Biến môi trường (.env) chi tiết
Tạo file `.env` tại thư mục gốc của dự án dựa theo mẫu chuẩn:

```env
# Chọn môi trường: 'local' (Docker Postgres) hoặc 'supabase' (Postgres Cloud)
SPRING_PROFILES_ACTIVE=supabase

# Cấu hình OpenRouter AI
OPENROUTER_API_KEY=sk-or-v1-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# Cấu hình Cloudflare R2 (Lưu trữ 3D Models và Thumbnails)
R2_ACCOUNT_ID=your_cloudflare_account_id
R2_ACCESS_KEY_ID=your_r2_access_key_id
R2_SECRET_ACCESS_KEY=your_r2_secret_access_key
R2_BUCKET_NAME=bio3d-models

# Chuỗi bí mật JWT (Tối thiểu 32 ký tự)
JWT_SECRET=BioverseJwtSecretKeyThatIsAtLeastThirtyTwoCharsLong!
JWT_ACCESS_EXPIRATION_MS=3600000
JWT_REFRESH_EXPIRATION_MS=604800000

# CORS Origins (phân tách bởi dấu phẩy, localhost luôn được bật tự động)
CORS_ALLOWED_ORIGINS=https://bioverse.eraidev.id.vn

# Redis Cache (Local Docker port 6380 hoặc REDIS_URL)
REDIS_HOST=localhost
REDIS_PORT=6380
REDIS_USERNAME=
REDIS_PASSWORD=123456
REDIS_SSL=false
REDIS_TIMEOUT=2s

# Tự động tạo tài khoản mẫu khi khởi động ứng dụng
SEED_ENABLED=true

# Cấu hình gửi Mail OTP qua Brevo SMTP Relay
MAIL_HOST=smtp-relay.brevo.com
MAIL_PORT=587
MAIL_USERNAME=your-brevo-smtp-login
MAIL_PASSWORD=xsmtpsib-your-brevo-smtp-key
MAIL_FROM=noreply@your-verified-domain.com
MAIL_FROM_NAME=BioVerse
APP_PUBLIC_URL=https://bioverse.eraidev.id.vn

# Cấu hình tham số OTP
OTP_LENGTH=6
OTP_TTL_SECONDS=600
OTP_RESEND_COOLDOWN_SECONDS=60
OTP_MAX_ATTEMPTS=5

# Cấu hình PostgreSQL Local (Môi trường local, khớp docker-compose.yml)
LOCAL_DB_HOST=localhost
LOCAL_DB_PORT=5434
LOCAL_DB_NAME=bioverse
LOCAL_DB_USERNAME=bioverse
LOCAL_DB_PASSWORD=123456
LOCAL_DB_SSLMODE=disable
LOCAL_DB_POOL_SIZE=10

# Cấu hình PostgreSQL Supabase (Môi trường supabase, dùng Session Pooler cổng 5432)
SUPABASE_DB_HOST=aws-0-ap-southeast-1.pooler.supabase.com
SUPABASE_DB_PORT=5432
SUPABASE_DB_NAME=postgres
SUPABASE_DB_USERNAME=postgres.YOUR_PROJECT_REF
SUPABASE_DB_PASSWORD=YOUR_DATABASE_PASSWORD
SUPABASE_DB_SSLMODE=require
SUPABASE_DB_POOL_SIZE=5
```

### Chạy môi trường Local Development

1. **Khởi chạy Database & Redis bằng Docker Compose:**
   ```bash
   docker compose up -d
   ```
   *Lệnh này khởi chạy Postgres cục bộ trên cổng `5434` và Redis trên cổng `6380`.*

2. **Khởi chạy ứng dụng Spring Boot:**
   ```bash
   # Windows PowerShell
   .\mvnw.cmd spring-boot:run

   # Linux / macOS
   ./mvnw spring-boot:run
   ```
   *Flyway sẽ tự động quét thư mục `db/migration` và chạy các bước nâng cấp schema từ V1 đến V20.*

3. **Kiểm tra tài liệu Swagger UI:**
   Truy cập trình duyệt tại địa chỉ:  
   `http://localhost:8080/swagger-ui/index.html`

### Chạy kết nối Supabase Cloud
Để ứng dụng chuyển sang sử dụng cơ sở dữ liệu đám mây Supabase:
1. Đặt trong file `.env`: `SPRING_PROFILES_ACTIVE=supabase`.
2. Điền đầy đủ thông số `SUPABASE_DB_*` từ Dashboard của Supabase (lưu ý: sử dụng cổng **5432** - Session Pooler, **không** dùng Transaction Pooler cổng 6543).
3. Khởi động lại ứng dụng: Flyway sẽ tự động đồng bộ toàn bộ bảng và dữ liệu seed lên Supabase.

### Triển khai Production với Docker & Cloudflare Tunnel
Dự án cung cấp sẵn cấu hình `docker-compose.prod.yml` chạy ứng dụng Backend (cổng 8088 nội bộ) và Redis trong cùng một mạng Docker riêng:

```bash
# 1. Build hình ảnh Docker của ứng dụng
docker compose -f docker-compose.prod.yml build

# 2. Khởi chạy hệ thống ở chế độ nền
docker compose -f docker-compose.prod.yml up -d

# 3. Theo dõi log hoạt động của backend
docker compose -f docker-compose.prod.yml logs -f backend
```

**Cấu hình Cloudflare Tunnel Ingress:**
- Trên Cloudflare Zero Trust Dashboard → Networks → Tunnels:
  - **Rule 1 (Backend API):**
    - Path: `/api` (và tùy chọn `/swagger-ui`, `/v3/api-docs`)
    - Service: `http://host.docker.internal:8088`
  - **Rule 2 (Frontend Catch-all):**
    - Path: để trống / `*`
    - Service: `http://127.0.0.1:3000` (hoặc cổng chạy Frontend)

### Quy trình CI/CD với GitHub Actions & Harbor
Quy trình tích hợp và triển khai liên tục được cấu hình tự động tại `.github/workflows/ci-cd.yml`:
1. Khi có sự kiện `push` lên nhánh `main`:
   - GitHub Actions kích hoạt job `build-image`: build Dockerfile đa giai đoạn và push image lên Harbor Registry (`registry.eraidev.id.vn/bioverse/bioverse-backend`).
2. Kích hoạt job `deploy`:
   - SSH an toàn vào VPS máy chủ dưới tài khoản `bioverse`.
   - Kéo image mới nhất từ Harbor và khởi chạy lại container thông qua `docker compose -f docker-compose.prod.yml pull && docker compose up -d`.

### Dữ liệu mẫu khởi tạo (Seed Accounts & Data)
Khi khởi động ứng dụng với thiết lập `SEED_ENABLED=true`, hệ thống sẽ tự động kiểm tra và tạo 3 tài khoản mặc định phục vụ cho việc kiểm thử:

| Loại tài khoản | Email đăng nhập | Mật khẩu mặc định | Vai trò | Ghi chú |
| :--- | :--- | :--- | :---: | :--- |
| **Quản trị viên** | `admin@bioverse.com` | `Admin@123456` | `ADMIN` | Toàn quyền quản trị nội dung, người dùng, R2 và phòng lab |
| **Học sinh 1** | `student@bioverse.com` | `Student@123456` | `STUDENT` | Học sinh Lớp 8 |
| **Học sinh 2** | `student2@bioverse.com` | `Student@123456` | `STUDENT` | Học sinh Lớp 7 |

---

*Tài liệu được cập nhật tự động và đồng bộ chính xác theo toàn bộ mã nguồn thực tế của dự án Bioverse Backend.*
