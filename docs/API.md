# SecureVault API Documentation

## Overview
SecureVault provides a REST API for secure document management with end-to-end encryption, OAuth2 authentication, and role-based access control.

**Base URL:** `http://localhost:8080`  
**Authentication:** OAuth2/OIDC (Asgardeo)  
**Content-Type:** `application/json` (unless specified)

## Authentication

All API endpoints require OAuth2 authentication via Asgardeo. Users must be authenticated through the web interface before accessing API endpoints.

## Endpoints

### Documents

#### Upload Document
```http
POST /documents
Content-Type: multipart/form-data
```

**Parameters:**
- `file` (required): The file to upload

**Response:**
- Redirects to `/dashboard` with success/error flash attributes

**Example:**
```bash
curl -X POST http://localhost:8080/documents \
  -F "file=@document.pdf" \
  --cookie "JSESSIONID=your_session_id"
```

---

#### List User Documents
```http
GET /documents
```

**Response:**
- Returns HTML page with user's documents list

---

#### Get Document Metadata
```http
GET /documents/{id}
```

**Parameters:**
- `id` (path): Document ID

**Response:**
```json
{
  "id": 1,
  "name": "document.pdf",
  "contentType": "application/pdf",
  "size": 1024,
  "uploadedAt": "2024-01-01T10:00:00Z",
  "ownerId": "user123"
}
```

**Example:**
```bash
curl http://localhost:8080/documents/1 \
  --cookie "JSESSIONID=your_session_id"
```

---

#### Download Document
```http
GET /documents/{id}/download
```

**Parameters:**
- `id` (path): Document ID

**Response:**
- Binary file content with appropriate headers
- `Content-Disposition: attachment; filename="document.pdf"`
- `Content-Type`: Original file MIME type

**Example:**
```bash
curl http://localhost:8080/documents/1/download \
  --cookie "JSESSIONID=your_session_id" \
  -o downloaded_file.pdf
```

---

### User Files

#### My Files Page
```http
GET /my-files
```

**Response:**
- Returns HTML page with user's files

---

### Authentication & Dashboard

#### Landing Page
```http
GET /
```

**Response:**
- Returns landing page HTML

---

#### Dashboard
```http
GET /dashboard
```

**Response:**
- Returns dashboard HTML with user documents and statistics

## Error Responses

### Common HTTP Status Codes

- `200 OK` - Request successful
- `302 Found` - Redirect response
- `400 Bad Request` - Invalid request parameters
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Access denied
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Error Format
```json
{
  "error": "Error message description",
  "timestamp": "2024-01-01T10:00:00Z",
  "path": "/documents/1"
}
```

## Security Features

### Encryption
- **Algorithm:** AES-256-GCM
- **Key Management:** Environment-based configuration
- **At-Rest:** All document content encrypted before storage

### Access Control
- **Authentication:** OAuth2/OIDC via Asgardeo
- **Authorization:** User can only access their own documents
- **Session Management:** Spring Security session handling

### Security Headers
- CSRF protection enabled
- Secure session cookies
- Content Security Policy headers

## Rate Limits

No explicit rate limits are currently implemented. Standard Spring Boot connection pool limits apply.

## File Upload Limits

- **Max File Size:** 25MB
- **Max Request Size:** 25MB
- **Supported Formats:** All file types supported

## Examples

### Complete Upload Flow
```bash
# 1. Authenticate via web interface first
# 2. Upload document
curl -X POST http://localhost:8080/documents \
  -F "file=@important_document.pdf" \
  --cookie "JSESSIONID=your_session_id"

# 3. List documents (returns HTML)
curl http://localhost:8080/documents \
  --cookie "JSESSIONID=your_session_id"

# 4. Get document metadata
curl http://localhost:8080/documents/1 \
  --cookie "JSESSIONID=your_session_id"

# 5. Download document
curl http://localhost:8080/documents/1/download \
  --cookie "JSESSIONID=your_session_id" \
  -o downloaded_document.pdf
```

## Environment Configuration

Required environment variables:
```bash
# Database
DATASOURCE_URL=jdbc:mysql://localhost:3306/securevault
DATASOURCE_USER=your_db_user
DATASOURCE_PASSWORD=your_db_password

# OAuth2
ASGARDEO_CLIENT_ID=your_client_id
ASGARDEO_CLIENT_SECRET=your_client_secret
ASGARDEO_BASE_URL=https://api.asgardeo.io/t/your_org

# Encryption
BASE64_ENCODED_256_BIT_KEY_HERE=your_base64_encoded_key
```

## Support

For issues and questions:
- **GitHub Issues:** [Project Issues](https://github.com/your-org/securevault/issues)
- **Documentation:** [Project Wiki](https://github.com/your-org/securevault/wiki)