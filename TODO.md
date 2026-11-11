# 🚀 News Scanner - Backlog 

## 🛠️ High Priority (Technical Debt)
- [x] **Prevent Duplicates**: Implement `existsByLink` in the repository before saving in the Runner.
- [x] **Runner Control**: Implement a ``CommandLineRunner`` to fetch pages in historical mode (``force = true``) on application startup so it seeds the database immediately.
- [x] **Scheduler Delay**: Set an ``initialDelay`` of 5 minutes (``300000ms``) on the ``@Scheduled`` method to allow seeding to finish completely before the hourly cycle begins.
- [x] **Database Cleanup**: Create a SQL script to remove existing duplicates (identified via DBeaver).

## 🌟 News Features
- [x] **REST API**: Create `ArticleController` to expose data.
- [x] **Search Filters**: Add endpoints to search news by keywords in the tittle.
- [x] **Metadata**: Add a `collected_at` field to track when the news was scraped.

## ⚙️ Infrastructure & Tools 
- [x] **API Documentation**: Configure Swagger/OpenAPI
- [x] **Docker Optimization**: Ensure data volumes are properly managed and back up.
    
## ⏳ Next Phase: On-Demand Historical Load (Backfilling)
- [x] **API Endpoint**: Create a ``/api/articles/historical`` endpoint that accepts a target date string.
- [x] **Adaptive Loop**: Implement page iteration that dynamically checks the publication date.
- [x] **Boundary Logic**: Ensure the stop condition uses target date comparison so that the target day's articles are included.
- [x] **Swagger Validation**: Test the historical backfill with a past date (e.g., ``2026-06-01``) and verify database.

## ⚙️ Continuous Improvements
- [x] Implement a professional logger (``org.slf4j.Logger``) in `NewsService` to replace `System.out.println`.
- [x] Create a scheduler (`@Scheduled`) to make the scraper run automatically every hour.
- [x] Optimize terminal logs by disabling raw SQL execution queries (`spring.jpa.show-sql=false`).
- [x] Test and validate historical load manually via Swagger/Postman (`POST /api/articles/sync`).

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
- [ ] **Keyword Extraction**: Implements a service to parse titles/content and extract relevant market keywords.
- [ ] **Spring AI Integration**: Connect to an LLM API (OpenAI/Ollama) to perform sentiment analysis on collected news.