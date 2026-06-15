# 🚀 News Scanner - Backlog 

## 🛠️ High Priority (Technical Debt)
- [x] **Prevent Duplicates**: Implement `existsByLink` in the repository before saving in the Runner.
- [x] **Runner Control**: Implement a ``CommandLineRunner`` to fetch 10 pages in historical mode (``forcarCargaHistorica = true``) on application startup so it seeds the database immediately.
- [x] **Scheduler Delay**: Set an ``initialDelay`` of 5 minutes (300000ms) on the ``@Scheduled`` method to allow seeding to finish completely before the hourly cycle begins.
- [x] **Database Cleanup**: Create a SQL script to remove existing duplicates (identified via DBeaver).

## 🌟 News Features
- [x] **REST API**: Create `ArtigoController` to expose data.
- [x] **Search Filters**: Add endpoints to search news by keywords in the tittle.
- [x] **Metadata**: Add a `collected_at` field to track when the news was scraped.

## ⚙️ Infrastructure & Tools 
- [x] **API Documentation**: Configure Swagger/OpenAPI
- [x] **Docker Optimization**: Ensure data volumes are properly managed and back up.
    
## ⏳ Next Phase: On-Demand Historical Load (Backfilling)
- [ ] **API Endpoint**: Create a ``/api/artigos/historico`` endpoint that accepts a target date string.
- [ ] **Adaptive Loop**: Implement page iteration that dynamically checks the publication date.
- [ ] **Boundary Logic**: Ensure the stop condition uses ``ìsBefore(dataLimite)`` so that the target day's articles are fully included.
- [ ] **Swagger Validation**: Test the historical backfill with a past date (e.g., 2026-06-01) and verify database state.

## ⚙️ Continuous Improvements
- [x] Implement a professional logger ('org.slf4j.Logger') in `NewsService` to replace `System.out.println`.
- [x] Create a scheduler (`@Scheduled`) to make the scraper run automatically every X hours (tested with 10s delay).
- [x] Optimize terminal logs by disabling raw SQL execution queries (`spring.jpa.show-sql=false`).
- [x] Test and validate historical load manually via Postman API endpoint (`POST /sincronizar`).

## 📊 Future Phases (Market Analyzer & Intel)
- [ ] **Keyword Extraction**: Implements a service to parse titles/content and extract relevant market keywords.
- [ ] **Spring AI Integration**: Connect to an LLM API (OpenAI/Ollama) to perform sentiment analysis on collected news.