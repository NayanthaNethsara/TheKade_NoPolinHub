# TheKade - NoPolinHub

NoPolinHub is a centralized system built with a **microservice
architecture** to reduce queues (polin) in Sri Lanka.\
It supports multiple government services like train ticket booking,
motor department queue booking, and other related services.

## 🚀 Features

- Citizen and Admin user types\
- Train ticket booking system\
- Motor department queue booking\
- Scalable microservice architecture\
- PostgreSQL as the database\
- Authentication and Authorization with JWT\
- API Gateway support

## 🏗️ Tech Stack

- **Backend**: Spring Boot (Microservices)
- **Database**: PostgreSQL
- **Frontend**: React / Next.js (optional future integration)
- **Containerization**: Docker, Docker Compose
- **Security**: JWT-based authentication
- **Other Tools**: Maven, GitHub Actions (CI/CD ready)

## 📂 Project Structure

    TheKade_NoPolinHub/
    │── backend
        |── api-gateway/
        │── appointment-service/
        │── auth-service/
    │── frontend
    │── docker-compose.yml
    │── README.md

## 🐳 Running with Docker

Build and run services:

```sh
docker compose up --build
```

Stop services:

```sh
docker compose down
```

## 🗄️ Database

PostgreSQL is used for persistence. To export the DB:

```sh
pg_dump -U <username> -d <database> -F c -f dump.sql
```

## 👥 Team

- Developed by **The Kade** team
- For Sri Lanka government service digitalization projects
