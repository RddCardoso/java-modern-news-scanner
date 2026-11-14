# 🚀 News Scanner - Backlog & Roadmap

## 🛠️ High Priority (Technical Debt)
- [x] **Prevent Duplicates**: Implement `existsByLink` in the repository before saving in the Runner.
- [x] **Runner Control**: Implement a ``CommandLineRunner`` to fetch initial data on application startup.
- [x] **Scheduler Delay**: Set an ``initialDelay`` of 5 minutes (``300000ms``) on the ``@Scheduled`` method to allow seeding to finish.
- [x] **Database Cleanup**: Create unique constraints and idempotent ingestion logic.

## 📰 News Features
- [x] **REST API**: Create `ArticleController` to expose data.
- [x] **Search Filters**: Add endpoints to search news by keywords in the title.
- [x] **Metadata**: Add a `collected_at` field to track collection timestamps.
- [x] **RSS Migration:** Transition from fragile HTML scraping to atomic RSS parsing.

## ⚙️ Infrastructure & Tools 
- [x] **API Documentation**: Configure Swagger/OpenAPI.
- [x] **Docker Optimization**: Ensure PostgresSQL volumes are properly managed.
    
## ⏳ Next Phase: On-Demand Historical Load (Backfilling)
- [x] **API Endpoint**: Create a ``/api/articles/historical`` endpoint accepting a target date string (`YYYY-MM-DD`).
- [x] **Adaptive Parsing**: Implement target date comparison logic.
- [x] **Boundary Logic**: Ensure historical stop conditions are met safely.

## ⚙️ Continuous Improvements
- [x] **Professional Logger:** Implement ``org.slf4j.Logger`` in `NewsService` to replace `System.out.println`.
- [x] **Job scheduler:** Create a scheduler (`@Scheduled`) to make the scraper run automatically every hour.
- [x] **SQL Log Optimization:** Optimize terminal logs by disabling raw SQL execution queries (`spring.jpa.show-sql=false`).
- [x] **Manual Validation:** Test and validate historical load manually via Swagger/Postman (`POST /api/articles/sync`).

## 🧪 Testing & Quality Assurance
-[ ] **Unit & Integration Tests**: Write comprehensive tests using JUnit 5, Mockito and Spring Boot Test. 
-[ ] **Database Testing**: Integrate Testcontainers to run real PostgreSQL instances in integration tests.

## ⚡ Event-Driven Architecture & Scalability
-[ ] **Apache Kafka**: Integrate a Kafka producer to publish newly scraped articles to a message topic.
-[ ] **Consumer Microservice**: Create a downstream consumer service to process events asynchronously.

## ☁️ Cloud, DevOps & CI/CD
-[ ] **CI/CD Pipeline**: Configure GitHub Actions to automate builds, run tests, and check code quality on every push.
-[ ] **AWS Deployment**: Containerize the application and deploy it to AWS (e.g., ECS/EKS or an EC2 instance with RDS). 

## 📊 Future Phases (Market Analyzer & Intel)
-[ ] **Keyword Extraction**: Implements a service to parse titles/content and extract relevant market keywords.
-[ ] **Spring AI Integration**: Connect to an LLM API (OpenAI/Ollama) to perform sentiment analysis on collected news.