# CryptoWatcher

CryptoWatcher is an application that allows users to manage their portfolios, track cryptocurrencies, and perform profit/loss analysis. This project uses an API to fetch live cryptocurrency prices and keeps users' portfolios up to date.

## Features

- User registration and authentication (with JWT)
- Create and manage portfolios
- Fetch live cryptocurrency prices via API
- Calculate profit/loss
- Redis integration for caching

## Setup

### Requirements

- Java 11 or higher
- MySQL Database
- Redis
- Maven

### Steps

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/yunusordek/crypto-watcher.git
    cd crypto-watcher/CryptoWatcher
    ```

2. **Set Up the Database**:
    Create a database in MySQL and update the database connection settings in the `src/main/resources/application.properties` file:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/crypto_watcher
    spring.datasource.username=root
    spring.datasource.password=mysql
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```

3. **Set Up Redis**:
    Make sure Redis is running locally and update the Redis connection settings in the `application.properties` file:
    ```properties
    spring.redis.host=localhost
    spring.redis.port=6379
    ```

4. **Install Dependencies and Run the Project**:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

## Usage

### API Endpoints

#### User Services

- **User Registration**: `POST /auth/register`
    ```json
    {
        "username": "example",
        "password": "password",
        "email": "example@example.com"
    }
    ```

- **User Login**: `POST /auth/login`
    ```json
    {
        "username": "example",
        "password": "password"
    }
    ```

#### Portfolio Services

- **Create Portfolio**: `POST /portfolio/create`
    ```json
    {
        "name": "My Portfolio",
        "userId": 1
    }
    ```

- **Add Coin to Portfolio**: `POST /portfolio/add`
    ```json
    {
        "portfolioId": 1,
        "cryptoSymbol": "bitcoin",
        "amount": 1.5,
        "purchasePrice": 30000
    }
    ```

- **List Coins in Portfolio**: `GET /portfolio/{portfolioId}/items`

- **Calculate Portfolio Value**: `GET /portfolio/{portfolioId}/value`

- **Calculate Portfolio Profit/Loss**: `GET /portfolio/{portfolioId}/profit`

### Redis Integration

#### What is Redis?

Redis is an open-source, in-memory data structure store. It is commonly used for caching, session management, and fast data access.

#### Using Redis

In this project, Redis is used to store user sessions and tokens. Configure the Redis connection in the `application.properties` file:
```properties
spring.redis.host=localhost
spring.redis.port=6379
