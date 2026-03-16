# RESTo API
Simple restaurant table reservation and recommendation system.

## Features
* View restaurant plan with all tables
* Filter available tables by date, time and number of guests
* Get a recommended table based on party size and preferences

## Tech stack
### Backend
* Java 25
* Spring Boot
* Spring Data JPA
* Uses H2 as the database

### Frontend
* React
* HTML5 Canvas for restaurant layout visualization

## Prerequisites
* Java 25
* Node.js + npm
* Gradle
* An IDE or code editor (e.g., IntelliJ IDEA)


## Installation

### Backend (Spring Boot)
1. Clone the repository:
   ```bash
   git clone https://github.com/palllaura/restoapi.git
   cd restoapi

2. Open the project in your IDE.

3. Build and run the backend:
   ```bash
   ./gradlew bootRun
4. The backend server will start at:
   http://localhost:8080

### Frontend (React + Canvas)
1. Navigate to the frontend folder:
   ```bash
   cd frontend
2. Install dependencies:
   ```bash
   npm install
3. Start the development server:
   ```bash
   npm run dev
4. The frontend will be available at:
   http://localhost:5173

### Database (H2)
The database starts automatically when the backend application runs.
H2 console will be available at: http://localhost:8080/h2-console

## Notes

* The restaurant layout and initial reservations are generated automatically for demonstration purposes.
* This project focuses on core functionality and a simple recommendation algorithm.
