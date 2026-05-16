# 🚀 News Scanner - Backlog 

## 🛠️ High Priority (Technical Debt)
- [x] **Prevent Duplicates**: Implement `existsByLink` in the repository before saving in the Runner.
- [ ] **Runner Control**: Implement logic to prevent the scraper from running unnecessarily on every restart.
- [ ] **Database Cleanup**: Create a SQL script to remove existing duplicates (identified via DBeaver).

## 🌟 News Features
- [ ] **REST API**: Create `ArtigoController` to expose data.
- [ ] **Search Filters**: Add endpoints to search news by keywords in the tittle.
- [ ] **Metadata**: Add a `collected_at` field to track when the news was scraped.

## ⚙️ Infrastructure & Tools 
- [ ] **API Documentation**: Configure Swagger/OpenAPI
- [ ] **Docker Optimization**: Ensure data volumes are properly managed and back up.

## ⏳ Next Phase: Incremental Synchronization by Date
- [ ] **HTML Mapping**: Identify where Pplware hides the publication date in  the DOM (e.g., `<time>` tar or metadata).
- [ ] **Entity Evolution**: Add a `dataPublicacao` field (`LocalDate` or `LocalDateTime`) to the `Artigo` class.
- [ ] **Control Parameter**: Allow `NewsService` to accept a `date Limit` (e.g., fetch only since last Friday or `last X days`).
- [ ] **Dynamic Stop (Break)**: Change the page loop to automatically stop reading when it hits the first news article with a date older than the defined limit.

## ⚙️ Continuous Improvements
- [ ] Implement a professional logger ('org.slf4j.Logger') in `NewsService` to replace `System.out.println`.
- [ ] Create a scheduler (`@Scheduled`) to make the scraper run automatically every X hours.