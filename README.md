# 🔗 Java URL Shortener

A scalable and high‑performance **URL Shortener** built using **Java**, with **PostgreSQL** for persistent storage and **Redis** for caching. This project is designed to efficiently generate, store, and resolve shortened URLs while handling high read traffic with low latency.


## Features

* Generate short URLs for long URLs
* Fast redirection using Redis caching
* Persistent storage with PostgreSQL
* Cache‑aside strategy for optimal performance
* Thread‑safe and production‑ready architecture
* URL hit count tracking *(optional if implemented)*
* RESTful API design


## Tech Stack

* **Backend:** Java (Spring Boot)
* **Database:** PostgreSQL
* **Cache:** Redis
* **Build Tool:** Maven / Gradle
* **API Style:** REST
* **ORM:** Hibernate / JPA

## System Architecture

1. **Create Short URL**

   * Long URL is stored in PostgreSQL
   * Generated short code is cached in Redis

2. **Redirect Flow**

   * Check Redis for short code
   * If cache miss → fetch from PostgreSQL and update Redis
   * Redirect user to original URL




## Configure Environment
Update application.properties to local postgres server and update redis cache server in the main entry point.

Update `application.yml` / `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener
spring.datasource.username=postgres
spring.datasource.password=password

spring.redis.host=localhost
spring.redis.port=6379
```

---

Contributions are welcome! Feel free to fork the repository and submit a pull request.

---

⭐ If you find this project useful, consider giving it a star on GitHub!
