
# AdventureXP Project

## Overview

This project consists of a backend (Spring Boot) and a frontend (HTML, JavaScript, CSS) served using Nginx. The application is containerized using Docker, and it provides access to the backend API and frontend static files.

---

## Docker Setup

### Prerequisites
- Docker
- System env variables: DB_USERNAME, DB_PASSWORD

### Docker Build

To build the **two** Docker images for 1) Backend + DB and 2) for the Frontend , run the following command in the project root directory (where your docker-compose and Dockerfile is located):

```bash
docker-compose up --build
```

```bash
docker build -t adventurexp-frontend .
```

---

### Docker Run

1. **Run the Backend (Spring Boot)**:
   ```bash
   docker run -d --network adventurexp-network --name adventurexp_app -p 8080:8080 adventurexp_backend
   ```

2. **Run the Frontend (Nginx)**:
   ```bash
   docker run -d --network adventurexp-network --name adventurexp_frontend -p 8081:80 adventurexp-frontend
   ```

These commands will start both the backend and frontend containers and connect them to a Docker network (`adventurexp-network`) so they can communicate with each other.

---

### Access Points

Once the containers are up and running, you can access the frontend and backend as follows:

#### Frontend (HTML Pages)
The frontend HTML pages are served by Nginx and can be accessed via the following URLs:

- **Start Page**:  
  `http://localhost:8081/html/startpage.html`

- **Booking Page**:  
  `http://localhost:8081/html/booking.html`

- **Admin Page**:  
  `http://localhost:8081/html/admin.html`

- **Service Page**:
  `http://localhost:8081/html/serviceemployee.html`



These pages interact with the backend API to fetch and manage data.

#### Backend API

The backend (Spring Boot) API is available through Nginx-proxied endpoints. You can interact with the API directly via:

- **Activity API**:  
  `http://localhost:8081/api/Activity`

---

### Notes

- **Static Files**: Static files (HTML, CSS, JS) are served from the `/usr/share/nginx/html` directory in the Nginx container.
- **Backend Communication**: The frontend makes requests to the backend through the Nginx proxy, which forwards API requests from `http://localhost:8081/api/` to the backend running on `http://adventurexp_app:8080`.
- **Docker Network**: Both frontend and backend are connected via `adventurexp-network` to ensure proper communication.

---

This should guide anyone through building, running, and accessing the Dockerized application! Let me know if you need further adjustments.