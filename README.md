# Java News Scanner & Market Analyzer

A high-performance news aggregator built with **Java 21**, designed to scrape, process, and persist technology and financial articles across multiple sources

## 🚀 Overview
This project serves as the backbone for a future **Stock Market Sentiment Analyzer**. It automatically scrapes real-time data using Jsoup, processes articles with modern Java Streams, handles historical backfills, prevents duplicates, and exposes a fully documented REST API.

## 🛠 Tech Stack
- **Language:** Java 21 (immutability and modern features)
- **Framework:** Spring Boot 3.4.0 (JPA/Hibernate, Web, Scheduling)
- **Database:** PostgreSQL 16 (running in Docker)
- **Web Scraping:** Jsoup (for robust HTML parsing)
- **API Documentation** OpenAPI 3 / Swagger UI
- **Logging:** SLF4J (structured logging)

## ⚙️ Setup & How to Run
### 1. Start the Database
Ensure Docker is running and execute the following in your terminal:
````bash 
docker-compose up -d
````

### 2. Configuration 
The application connects to PostgreSQL using these defaults:
- **Host:** ``localhost:5432``
- **Database:** ``news_scraper_db``
- **User:** ``utilizador_java``

### 3. Run the Application
Run the ``Main.java`` class from your IDE or via Maven:
````bash
mvn spring-boot:run
````
Upon startup, the ``InitialLoadRunner`` automatically seeds the database with the latest articles if empty.

## 📊 Data Visualization
You can inspect the data using DBeaver or the PostgresSQL terminal:
````bash
psql -U utilizador_java -d news_scraper_db
````

## 💡 Key Features
- **Smart Stop & Deduplication:** Prevents duplicate storage using link checks and halts scraping dynamically when existing articles are found.
- **Scheduled Scraping:** Background worker (``NewsScheduler``) executes automatic runs every hour (with a 5-minute initial startup delay).
- **Historical Backfill:** Allows on-demand scraping retroactively from a specified target date (``YYYY-MM-DD``).
- **REST API & Swagger:** Exposes paginated endpoints for querying articles and triggering manual runs, fully documented via Swagger UI (``/swagger-ui.html``).

## 🔌 API Endpoints
### Articles
- ``GET /api/articles`` - Get paginated list of articles. Supports title filtering (``?title=java&page=0&size=10``).

### Management & Triggers
- ``POST /api/articles/sync?pages=10&force=true`` - Trigger manual scraping for a specified number of pages.
- ``POST /api/articles/historical?sinceDate=2026-06-01`` - Trigger historical backfill since a target date.

## 🎯 Roadmap
- [x] Implement database persistence with Spring Data JPA.
- [x] Prevent duplicate news entries (Idempotency).
- [x] Create a paginated REST API (``/api/articles``).
- [x] Implement a background job scheduler (``@Scheduled``).
- [x] Implement on-demand historical backfill endpoint.
- [x] Integrate Swagger / OpenAPI documentation.
- [ ] Add financial news sources (Yahoo Finance, CNBC),
- [ ] Integrate LLM sentiment analysis (Spring AI/Ollama).
- [ ] Build a web dashboard for data visualization.