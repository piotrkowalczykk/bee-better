
<div align="center">
<img src="https://raw.githubusercontent.com/piotrkowalczykk/be-better/refs/heads/main/logobb.png" alt="Logo">

---
<center>
<h4> A simple web application designed to track fitness progress.
</center>

<p align="center">
  <img alt="Static Badge" src="https://img.shields.io/badge/license-MIT-pink">
  <img alt="Static Badge" src="https://img.shields.io/badge/version-1.0.0-blue?logo=version">
  <img alt="Static Badge" src="https://img.shields.io/badge/status-completed-green?logo=version">
</p>

<img src="https://raw.githubusercontent.com/piotrkowalczykk/be-better/refs/heads/main/preview.gif" alt="App Preview" width="100%">
</div>


## 📋 Table of Contents
* [Key Features](#-key-features)
* [Technologies](#-technologies)
* [API Endpoints Documentation](#-api-endpoints-documentation)
* [License](#-license)


## 🛠 Key Features

### Security and Authorization
* **JWT Authentication:** Secure communication between React and Spring Boot using access tokens.
* **Role-Based Access Control (RBAC):** Distinct permissions for users and administrators.
* **Email Verification:** Registration system requiring email confirmation via MailHog.
* **Secure Password Reset:** Automated SMTP-based workflow for safe password recovery and updates.
* **Secure Password Hashing:** Utilizing BCrypt for robust credential storage.

### Workout and Training Management
* **Custom Exercise Creation:** Users can define and add their own specific exercises to a personalized library.
* **Workout Plan Builder:** Ability to design comprehensive, long-term training plans from scratch.
* **Training Day Scheduling:** Organize workouts by assigning specific routines to different days of the week.
* **Exercise Logging:** Real-time tracking of sets, reps, and weight for every movement during a session.
* **Routine Management:** Create, edit, and save reusable routines for quick access during training.
* **Training History:** Detailed archive of all past workouts, exercises performed, and dates.
* **1RM Estimation:** Automatically calculates your theoretical one-rep max based on sub-maximal efforts.

### Technical Core (Backend & Database)
* **RESTful Web Services:** Built with Spring Boot for high-performance and scalable API architecture.
* **Relational Data Persistence:** Structured data management using PostgreSQL for complex workout relationships.
* **SMTP Integration:** Reliable email delivery service specifically for account verification and password resets.
* **Spring Data JPA:** Efficient database communication and ORM management.

### Frontend & User Experience
* **Reactive Interface:** Built with React to provide a fast, app-like experience without unnecessary page reloads.
* **Mobile-First Design:** Fully responsive layout optimized for use on smartphones directly at the gym.
* **Real-time UI Feedback:** Immediate validation of inputs to ensure accurate workout data entry.

## 🤖 Technologies
<p align="center"> <img src="https://img.shields.io/badge/Java-ff9100?style=for-the-badge&logo=coffeescript&labelColor=black" alt="Java"> <img src="https://img.shields.io/badge/Spring_Boot-%236DB33F?style=for-the-badge&logo=springboot&logoColor=white&labelColor=black" alt="Spring Boot"> <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white&labelColor=black" alt="Spring Security"> <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white&labelColor=black" alt="Hibernate"> <img src="https://img.shields.io/badge/Lombok-%23bc060c?style=for-the-badge&logo=lombok&logoColor=white&labelColor=black" alt="Lombok"> <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white&labelColor=black" alt="Maven"> <img src="https://img.shields.io/badge/react-%2361DAFB?style=for-the-badge&logo=react&logoColor=white&labelColor=black" alt="React"> <img src="https://img.shields.io/badge/postgresql-%234169E1?style=for-the-badge&logo=postgresql&logoColor=white&labelColor=black" alt="PostgreSQL"> <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white&labelColor=000000" alt="JWT"> <img src="https://img.shields.io/badge/MailHog-blue?style=for-the-badge&logo=maildotru&logoColor=white&labelColor=black" alt="MailHog"> <img src="https://img.shields.io/badge/html5-%23E34F26?style=for-the-badge&logo=html5&logoColor=white&labelColor=black" alt="HTML5"> <img src="https://img.shields.io/badge/css-%231572B6?style=for-the-badge&logo=css3&logoColor=white&labelColor=black" alt="CSS3"> <img src="https://img.shields.io/badge/javascript-%23F7DF1E?style=for-the-badge&logo=javascript&logoColor=white&labelColor=black" alt="JavaScript"> </p>

## 📰 API Endpoints Documentation

### CustomerController Endpoints

| Method | Endpoint | Parameters | Response |
|--------|---------|------------|----------|
| GET | `/customer/articles` | `UserDetails` (@AuthenticationPrincipal) | List of all articles (`List<ArticlesResponse>`) |
| GET | `/customer/articles/{articleId}` | `articleId: Long`, `UserDetails` | Article details (`ArticleDetailsResponse`) |
| POST | `/customer/add-routine` | `AddRoutineRequest` (@RequestBody), `UserDetails` | Add new routine (`RoutineResponse`) |
| GET | `/customer/routines` | `UserDetails` | List of all routines (`List<RoutineResponse>`) |
| GET | `/customer/routines/{id}` | `id: Long`, `UserDetails` | Routine details (`RoutineResponse`) |
| PUT | `/customer/routines/{id}` | `id: Long`, `UpdateRoutineRequest` (@RequestBody), `UserDetails` | Update routine (`RoutineResponse`) |
| DELETE | `/customer/routines/{id}` | `id: Long`, `UserDetails` | Delete routine (`RoutineResponse`) |
| POST | `/customer/routines/log` | `LogRoutineRequest` (@RequestBody), `UserDetails` | Log routine (`RoutineLogResponse`) |
| GET | `/customer/routines/logs` | `date: LocalDate` (@RequestParam), `UserDetails` | Routine logs for a day (`List<RoutineLogResponse>`) |
| POST | `/customer/add-day` | `AddDayRequest` (@RequestBody), `UserDetails` | Add day (`DayResponse`) |
| GET | `/customer/days` | `UserDetails` | List of all days (`List<DayResponse>`) |
| GET | `/customer/days/{id}` | `id: Long`, `UserDetails` | Day details (`DayResponse`) |
| PUT | `/customer/days/{id}` | `id: Long`, `UpdateDayRequest` (@RequestBody), `UserDetails` | Update day (`DayResponse`) |
| DELETE | `/customer/days/{id}` | `id: Long`, `UserDetails` | Delete day (`DayResponse`) |
| GET | `/customer/days/today` | `date: LocalDate` (@RequestParam), `UserDetails` | Day for a given date (`DayResponse`) |
| POST | `/customer/add-exercise` | `AddExerciseRequest` (@ModelAttribute), `UserDetails` | Add exercise (`ExerciseResponse`) |
| GET | `/customer/exercises` | `UserDetails` | List of exercises (`List<ExerciseResponse>`) |
| GET | `/customer/exercises/{id}` | `id: Long`, `UserDetails` | Exercise details (`ExerciseResponse`) |
| PUT | `/customer/exercises/{id}` | `id: Long`, `UpdateExerciseRequest` (@RequestBody), `UserDetails` | Update exercise (`ExerciseResponse`) |
| DELETE | `/customer/exercises/{id}` | `id: Long`, `UserDetails` | Delete exercise (`ExerciseResponse`) |
| GET | `/customer/exercises/equipment` | `UserDetails` | List of equipment (`List<Equipment>`) |
| GET | `/customer/workouts` | `UserDetails` | All workout logs (`List<WorkoutLog>`) |
| GET | `/customer/workouts/by-date` | `date: LocalDate` (@RequestParam), `UserDetails` | Workout for a specific day (`WorkoutDayResponse`) |
| POST | `/customer/workouts/log-workout` | `LogWorkoutRequest` (@RequestBody), `UserDetails` | Log workout (`WorkoutLog`) |
| PUT | `/customer/workouts/{id}` | `id: Long`, `UserDetails` | Delete workout log (No Content) |
| GET | `/customer/workouts/logs/{exerciseId}/1rm` | `exerciseId: Long`, `from: LocalDate` (optional), `to: LocalDate` (optional), `UserDetails` | Max weight for exercise (`List<Exercise1RmResponse>`) |
| GET | `/customer/workouts/logs/exercises` | `UserDetails` | All logged exercises (`List<Exercise>`) |

---

### AuthController Endpoints

| Method | Endpoint | Parameters | Response |
|--------|---------|------------|----------|
| POST | `/auth/register` | `RegisterRequest` (@RequestBody) | Register user (`RegisterResponse`) |
| POST | `/auth/login` | `LoginRequest` (@RequestBody) | Login (`LoginResponse`) |
| POST | `/auth/validate-email` | `ValidateEmailRequest` (@RequestBody) | Validate email (`ValidateEmailResponse`) |
| POST | `/auth/resend-email-verification-code` | `ResendVerificationCodeRequest` (@RequestBody) | Resend verification code (`ResendVerificationCodeResponse`) |
| POST | `/auth/reset-password` | `ResetPasswordRequest` (@RequestBody) | Reset password (`ResetPasswordResponse`) |
| POST | `/auth/send-reset-password-code` | `SendResetPasswordCodeRequest` (@RequestBody) | Send reset password code (`SendResetPasswordCodeResponse`) |

---

### AdminController Endpoints

| Method | Endpoint | Parameters | Response |
|--------|---------|------------|----------|
| GET | `/admin/articles` | `UserDetails` (@AuthenticationPrincipal) | List all articles (`List<ArticleDetailsResponse>`) |
| POST | `/admin/articles` | `ArticleRequest` (@ModelAttribute), `UserDetails` | Add article (`ArticleDetailsResponse`) |
| PUT | `/admin/articles/{articleId}` | `articleId: Long`, `ArticleRequest` (@ModelAttribute), `UserDetails` | Update article (`ArticleDetailsResponse`) |
| DELETE | `/admin/articles/{articleId}` | `articleId: Long`, `UserDetails` | Delete article (`ArticleDetailsResponse`) |

## 🧾 License
Available under the [MIT license](https://github.com/piotrkowalczykk/be-better/blob/main/LICENSE)
