# Learning Management System (LMS)

A full-stack Learning Management System built with React, Spring Boot, and MySQL.

## 🚀 Features

- **Role-based Authentication**: Admin, Instructor, and Student roles with JWT security
- **Course Management**: Create, update, and manage courses with multimedia content
- **Lesson Management**: Support for text, PDF, video, and audio lessons
- **Enrollment System**: Students can enroll in courses and track progress
- **File Upload**: AWS S3 integration for multimedia content storage
- **Responsive UI**: Modern, mobile-friendly interface with Tailwind CSS

## 🛠️ Tech Stack

### Backend
- Spring Boot 3.x
- Spring Data JPA
- Spring Security with JWT
- MySQL Database
- Maven

### Frontend
- React 18
- Tailwind CSS
- Axios for API calls
- React Router for navigation

### Deployment
- Frontend: Vercel
- Backend: Railway
- Database: MySQL (Cloud)

## 📁 Project Structure

```
lms-project/
├── backend/                 # Spring Boot backend
├── frontend/               # React frontend
├── database/               # Database schema and migrations
├── docs/                   # Documentation
└── README.md
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.6+

### Backend Setup
1. Navigate to `backend/` directory
2. Update `application.properties` with your database credentials
3. Run: `mvn spring-boot:run`

### Frontend Setup
1. Navigate to `frontend/` directory
2. Install dependencies: `npm install`
3. Start development server: `npm start`

### Database Setup
1. Create MySQL database: `lms_db`
2. Run the schema from `database/schema.sql`
3. Sample data will be loaded automatically

## 🔐 Default Credentials

- **Admin**: admin/ Admin@123
- **Instructor**: instructor1/ Instructor@123
- **Student**: student1 / Student@123

## 📚 API Documentation

The backend provides REST APIs for:
- Authentication (`/api/auth/*`)
- User management (`/api/users/*`)
- Course management (`/api/courses/*`)
- Lesson management (`/api/lessons/*`)
- Enrollment management (`/api/enrollments/*`)

## 🚀 Deployment

### Frontend (Vercel)
1. Connect your GitHub repository to Vercel
2. Set build command: `npm run build`
3. Set output directory: `build`

### Backend (Render)
1. Connect your GitHub repository to Render
2. Set build command: `mvn clean package`
3. Set start command: `java -jar target/lms-backend-0.0.1-SNAPSHOT.jar`
4. Add environment variables for database and JWT configuration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request




