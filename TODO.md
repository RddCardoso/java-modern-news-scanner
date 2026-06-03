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
- [ ] **Docker Optimization**: Ensure data volumes are properly managed and back up.
    
## ⏳ Next Phase: Incremental Synchronization by Date
- [x] **HTML Mapping**: Identify where Pplware hides the publication date in  the DOM (e.g., `<time>` tar or metadata).
- [x] **Entity Evolution**: Add a `dataPublicacao` field (`LocalDate` or `LocalDateTime`) to the `Artigo` class.
- [x] **Control Parameter**: Allow `NewsService` to accept a `date Limit` (e.g., fetch only since last Friday or `last X days`).
- [x] **Dynamic Stop (Break)**: Change the page loop to automatically stop reading when it hits the first news article with a date older than the defined limit.

## ⚙️ Continuous Improvements
- [x] Implement a professional logger ('org.slf4j.Logger') in `NewsService` to replace `System.out.println`.
- [x] Create a scheduler (`@Scheduled`) to make the scraper run automatically every X hours (tested with 10s delay).
- [x] Optimize terminal logs by disabling raw SQL execution queries (`spring.jpa.show-sql=false`).
- [x] Test and validate historical load manually via Postman API endpoint (`POST /sincronizar`).

## 📊 Future Phases (Market Analyzer & Intel)
- [ ] **Keyword Extraction**: Implements a service to parse titles/content and extract relevant market keywords.
- [ ] **Spring AI Integration**: Connect to an LLM API (OpenAI/Ollama) to perform sentiment analysis on collected news.