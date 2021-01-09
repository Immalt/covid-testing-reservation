# Covid testing reservation

This is a simple case-study app, purposed for managing a simple registration system for COVID-19 testing stations. The users are able to register for a specific time-slot and are notified by email about their appointments.

## Services

These are the services that form the app.

### Grafana

Available on http://localhost:5000 with name `admin`, password `admin`

### Jaeger

Present but broken

### Kafka

Communicating on port 9092, used internally

### Mailer

MailHog interface available on http://127.0.0.1:1080/

### Mailing service

REST API available on http://localhost:1002, Swagger UI on http://localhost:1002/swagger-ui

Responsible for sending out email notifications and confirmations.

### Personal data service

REST API available on http://localhost:1000, Swagger UI on http://localhost:1000/swagger-ui

Responsible for handling of personal data and applications.

### Prometheus

Responsible for collecting metrics.

### Static server service

Serves a (placeholder) SPA on http://localhost:1003

### Time slots service

REST API available on http://localhost:1001, Swagger UI on http://localhost:1001/swagger-ui

Responsible for managing available time slots for testing.

### Zookeeper

The Apache ZooKeeper instance.