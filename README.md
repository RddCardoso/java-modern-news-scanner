# Java News Scanner & Market Analyzer

A high-performance news aggregator built with **Java 21**, designed to filter and track specific topics (Tech,AI, and Finance) across multiple sources.

## 🚀 Overview
This project serves as the backbone for a future **Stock Market Sentiment Analyzer**. It currently scrapes real-time data, processes it using modern Java Streams, and filters relevant information based on specific keywords.

## 🛠 Tech Stack
- **Language:** Java 21 (utilizing Records for clean data modeling)
- **Framework:** Spring Boot 3 (JPA/Hibernate)
- **Database:** PostgreSQL (running in Docker)
- **Library:** Jsoup (for robust HTML parsing)
- **Architecture:** Functional Programming approach with Java Streams API

## ⚙️ Setup & How to Run
### 1. Start the Database
Ensure Docker is running and execute the following in your terminal:
````bash 
docker-compose up -d
````

### 2. Configuration 
The application connects to the database using the following credentials (defined in docker-compose.yml):
- **Host:** ``localhost:5432``
- **Database:** ``news_scraper_db``
- **User:** ``utilizador_java``

### 3. Run the Application
Run the ``Main.java`` class from your IDE. The scraper will fetch data and store in the ``artigos`` tables.

## 📊 Data Visualization
You can inspect the data using DBeaver or the PostgresSQL terminal:
````bash
psql -U utilizador_java -d news_scraper_db
````

## 💡 Key Features
- **Modern Pipeline:** Uses Java Streams for efficient data transformation and filtering.
- **Clean Architecture:** Implements Java Records to ensure data immutability.
- **Case-Insensitive Filtering:** Advanced filtering logic to capture keywords regardless of formatting.

## 📖 How it Wotks
The core logic utilizes a professional-grade pipeline:
1. **Selection:** Targets specific CSS selectors to isolate relevant headlines.
2. **Mapping:** converts raw HTML elements into immutable 'com.newsscanner.Artigo' objects.
3. **Filtering:** Applies logic to isolate high-value topics like AI and Java.
4. **Collection:** Bundles processed data into a clean, usable List.



## 🎯 Roadmap
- [x] Implement database persistence (SQL).
- [ ] Prevent duplicate news entries (Idempotency).
- [ ] Create a REST API to expose data via JSON. 
- [ ] Add financial news sources (Yahoo Finance, CNBC),
- [ ] Integrate a Machine Learning model for Sentiment Analysis.
- [ ] Create a web dashboard for data visualization.