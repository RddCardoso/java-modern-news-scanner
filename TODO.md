# 🚀 News Scanner - Backlog 

## 🛠️ High Priority (Technical Debt)
- [ ] **Prevent Duplicates**: Implement 'existsByLink' in the repository before saving in the Runner.
- [ ] **Runner Control**: Implement logic to prevent the scraper from running unnecessarily on every restart.
- [ ] **Database Cleanup**: Create a SQL script to remove existing duplicates (identified via DBeaver).

## 🌟 News Features
- [ ] **REST API**: Create 'ArtigoController' to expose data.
- [ ] **Search Filters**: Add endpoints to search news by keywords in the tittle.
- [ ] **Metadata**: Add a 'collected_at' field to track when the news was scraped.

## ⚙️ Infrastructure & Tools 
- [ ] **API Documentation**: Configure Swagger/OpenAPI
- [ ] **Docker Optimization**: Ensure data volumes are proprerly managed and back up.