# SecureVault 🔐

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green)
![License](https://img.shields.io/badge/license-MIT-blue)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen)

A secure, enterprise-grade document management system built with Spring Boot that provides end-to-end encryption, OAuth2 authentication, and granular access control for file storage and sharing.

## 🌟 Features

- **🔒 End-to-End Encryption**: Files are encrypted using AES-256-GCM encryption before storage
- **🔐 OAuth2 Authentication**: Secure authentication via Asgardeo OIDC provider
- **👥 Access Control**: Role-based permissions (Owner, Editor, Viewer) for documents
- **📁 Document Management**: Upload, download, and organize files with full metadata
- **🏢 Multi-tenancy**: Support for groups and organizational structures
- **📊 Audit Logging**: Comprehensive audit trails for all document operations
- **🎨 Modern UI**: Responsive web interface built with Thymeleaf and modern CSS
- **⚡ Performance**: Lazy loading for large file handling

## 🚀 Quick Start

### Prerequisites

- Java 21 or higher
- MySQL 8.0+
- Maven 3.9+
- Asgardeo account (for OAuth2)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/securevault.git
   cd securevault
   ```

2. **Configure Environment Variables**
   
   Create a `.env` file or set the following environment variables:
   ```bash
   # Application
   SPRING_APPLICATION_NAME=securevault
   SERVER_PORT=8080
   
   # Database Configuration
   DATASOURCE_URL=jdbc:mysql://localhost:3306/<YOUR DB NAME>
   DATASOURCE_USER=your_db_user
   DATASOURCE_PASSWORD=your_db_password
   
   # Asgardeo OAuth2 Configuration
   ASGARDEO_CLIENT_ID=your_client_id
   ASGARDEO_CLIENT_SECRET=your_client_secret
   ASGARDEO_BASE_URL=https://api.asgardeo.io/t/your_org
   ASGARDEO_REDIRECT_URI=http://localhost:8080/login/oauth2/code/asgardeo
   ASGARDEO_USERINFO_URI=https://api.asgardeo.io/t/your_org/oauth2/userinfo
   ASGARDEO_ORG_PATH=/t/your_org
   ASGARDEO_USERNAME=username
   
   # Encryption (Generate a 256-bit key and encode in Base64)
   BASE64_ENCODED_256_BIT_KEY_HERE=your_base64_encoded_256_bit_key
   ```

3. **Generate Encryption Key**
   ```bash
   # Generate a 256-bit key for AES encryption
   openssl rand -base64 32
   ```

4. **Build and Run**
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

5. **Access the Application**
   
   Navigate to `http://localhost:8080` in your browser.

## 🏗️ Architecture

### Technology Stack

- **Backend**: Spring Boot 3.5.7, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Database**: MySQL with JPA/Hibernate
- **Authentication**: OAuth2/OIDC with Asgardeo
- **Encryption**: AES-256-GCM
- **Build Tool**: Maven

### Project Structure

```
src/
├── main/
│   ├── java/com/example/securevault/
│   │   ├── config/           # Security and application configuration
│   │   ├── controller/       # REST controllers and web endpoints
│   │   ├── entity/          # JPA entities and data models
│   │   ├── repository/      # Data access layer
│   │   └── service/         # Business logic layer
│   └── resources/
│       ├── static/          # CSS, JavaScript, and static assets
│       └── templates/       # Thymeleaf templates
└── test/                    # Unit and integration tests
```

### Core Entities

- **Document**: Encrypted file storage with metadata
- **User**: User profiles linked to OAuth2 identity
- **DocumentAcl**: Access control lists for fine-grained permissions
- **Group/GroupMember**: Organizational structure support
- **AccessRequest**: Workflow for requesting document access
- **AuditLog**: Comprehensive audit trail

## 🔒 Security Features

### Encryption
- **Algorithm**: AES-256-GCM (Galois/Counter Mode)
- **Key Management**: Environment-based key configuration
- **IV Generation**: Cryptographically secure random IV per document
- **At-Rest Encryption**: All document content encrypted before database storage

### Authentication & Authorization
- **OAuth2/OIDC**: Integration with Asgardeo identity provider
- **Session Management**: Spring Security session handling
- **Role-Based Access**: Document-level permissions (Owner, Editor, Viewer)
- **Group Support**: Organizational access control

### Security Headers
- CSRF protection enabled
- Secure session cookies
- Content Security Policy headers

## 🚀 Deployment

### Docker Deployment

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/securevault-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
# Build the application
./mvnw clean package

# Build Docker image
docker build -t securevault:latest .

# Run with Docker Compose
docker-compose up -d
```

### Production Considerations

1. **Database**: Use managed MySQL service (AWS RDS, Google Cloud SQL)
2. **Key Management**: Use cloud key management services (AWS KMS, Azure Key Vault)
3. **Load Balancing**: Deploy behind a load balancer with SSL termination
4. **Monitoring**: Implement application monitoring (Prometheus, Grafana)
5. **Backup**: Regular database backups and disaster recovery planning

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

```

## 📝 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# File upload limits
spring.servlet.multipart.max-file-size=25MB
spring.servlet.multipart.max-request-size=25MB

# Database connection pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5

# JPA/Hibernate settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

## 📊 Monitoring & Observability

- **Health Checks**: Spring Boot Actuator endpoints
- **Metrics**: Custom metrics for document operations
- **Logging**: Structured logging with correlation IDs
- **Audit Trail**: Complete audit log for compliance requirements

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding standards and Spring Boot best practices
- Write unit tests for new features
- Update documentation for API changes
- Ensure security best practices are followed

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [Wiki](https://github.com/your-org/securevault/wiki)
- **Issue Tracker**: [GitHub Issues](https://github.com/your-org/securevault/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-org/securevault/discussions)

## 🚧 Roadmap

- [ ] Multi-cloud storage support (AWS S3, Azure Blob)
- [ ] Advanced audit reporting dashboard
- [ ] Document versioning and change tracking
- [ ] Mobile application support
- [ ] Advanced search and indexing
- [ ] Webhook integration for external systems
- [ ] LDAP/Active Directory integration
- [ ] Document workflow approval system

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) - Application framework
- [Asgardeo](https://wso2.com/asgardeo/) - Identity and access management
- [Thymeleaf](https://www.thymeleaf.org/) - Template engine
- [MySQL](https://www.mysql.com/) - Database system

---

**SecureVault** - *Secure File Management Made Simple* 🔐✨
