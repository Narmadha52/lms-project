# Deployment Guide

This guide will help you deploy the Learning Management System (LMS) to production.

## Prerequisites

- MySQL database (local or cloud)
- Vercel account (for frontend)
- Railway/Render account (for backend)
- AWS account (for file storage - optional)

## Backend Deployment

### Option 1: Railway

1. **Connect Repository**
   - Go to [Railway](https://railway.app)
   - Connect your GitHub repository
   - Select the `backend` folder

2. **Configure Environment Variables**
   ```
   DATABASE_URL=jdbc:mysql://your-mysql-host:3306/lms_db?useSSL=true&serverTimezone=UTC
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   JWT_SECRET=your-super-secret-jwt-key-here
   CORS_ORIGINS=https://your-frontend-url.vercel.app
   AWS_ACCESS_KEY_ID=your_aws_key
   AWS_SECRET_ACCESS_KEY=your_aws_secret
   ```

3. **Deploy**
   - Railway will automatically build and deploy your application
   - Note the generated URL for your backend

### Option 2: Render

1. **Create Web Service**
   - Go to [Render](https://render.com)
   - Create a new Web Service
   - Connect your GitHub repository
   - Set build command: `cd backend && mvn clean package -DskipTests`
   - Set start command: `cd backend && java -jar target/lms-backend-0.0.1-SNAPSHOT.jar`

2. **Configure Environment Variables**
   - Same as Railway configuration

3. **Deploy**
   - Render will build and deploy your application

### Option 3: AWS EC2

1. **Launch EC2 Instance**
   - Launch an Ubuntu 20.04+ instance
   - Install Java 17 and MySQL

2. **Setup Application**
   ```bash
   # Clone repository
   git clone <your-repo-url>
   cd lms-project/backend
   
   # Build application
   ./mvnw clean package -DskipTests
   
   # Run application
   java -jar target/lms-backend-0.0.1-SNAPSHOT.jar
   ```

3. **Configure Nginx** (optional)
   ```nginx
   server {
       listen 80;
       server_name your-domain.com;
       
       location / {
           proxy_pass http://localhost:8080;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
       }
   }
   ```

## Frontend Deployment

### Vercel

1. **Connect Repository**
   - Go to [Vercel](https://vercel.com)
   - Import your GitHub repository
   - Set root directory to `frontend`

2. **Configure Environment Variables**
   ```
   REACT_APP_API_URL=https://your-backend-url.railway.app/api
   ```

3. **Deploy**
   - Vercel will automatically build and deploy your React app
   - Note the generated URL for your frontend

### Netlify

1. **Connect Repository**
   - Go to [Netlify](https://netlify.com)
   - Connect your GitHub repository
   - Set build command: `cd frontend && npm run build`
   - Set publish directory: `frontend/build`

2. **Configure Environment Variables**
   ```
   REACT_APP_API_URL=https://your-backend-url.railway.app/api
   ```

3. **Deploy**
   - Netlify will build and deploy your application

## Database Setup

### Local MySQL

1. **Install MySQL**
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install mysql-server
   
   # macOS
   brew install mysql
   ```

2. **Create Database**
   ```sql
   CREATE DATABASE lms_db;
   CREATE USER 'lms_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON lms_db.* TO 'lms_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Run Schema**
   ```bash
   mysql -u lms_user -p lms_db < database/schema.sql
   ```

### Cloud MySQL

1. **AWS RDS**
   - Create MySQL RDS instance
   - Configure security groups
   - Note connection details

2. **Google Cloud SQL**
   - Create Cloud SQL instance
   - Configure authorized networks
   - Note connection details

3. **PlanetScale**
   - Create database
   - Get connection string
   - Configure environment variables

## Environment Variables Reference

### Backend
- `DATABASE_URL`: MySQL connection string
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT tokens
- `JWT_EXPIRATION`: JWT token expiration time (milliseconds)
- `CORS_ORIGINS`: Allowed CORS origins (comma-separated)
- `AWS_ACCESS_KEY_ID`: AWS access key
- `AWS_SECRET_ACCESS_KEY`: AWS secret key
- `MAIL_USERNAME`: Email username
- `MAIL_PASSWORD`: Email password

### Frontend
- `REACT_APP_API_URL`: Backend API URL

## Post-Deployment

1. **Test API Endpoints**
   - Verify authentication works
   - Test course creation and enrollment
   - Check file upload functionality

2. **Configure Domain** (optional)
   - Point custom domain to Vercel/Netlify
   - Update CORS origins in backend

3. **Monitor Performance**
   - Set up logging and monitoring
   - Configure error tracking
   - Monitor database performance

## Troubleshooting

### Common Issues

1. **CORS Errors**
   - Check CORS_ORIGINS environment variable
   - Ensure frontend URL is included

2. **Database Connection Issues**
   - Verify database credentials
   - Check network connectivity
   - Ensure database is accessible

3. **JWT Token Issues**
   - Verify JWT_SECRET is set
   - Check token expiration settings

4. **File Upload Issues**
   - Verify AWS credentials
   - Check S3 bucket permissions
   - Ensure CORS is configured on S3 bucket

### Logs

- **Railway**: Check logs in Railway dashboard
- **Render**: Check logs in Render dashboard
- **Vercel**: Check function logs in Vercel dashboard
- **EC2**: Check application logs in `/var/log/`

## Security Considerations

1. **Environment Variables**
   - Never commit secrets to repository
   - Use strong, unique passwords
   - Rotate secrets regularly

2. **Database Security**
   - Use strong passwords
   - Enable SSL connections
   - Restrict network access

3. **API Security**
   - Implement rate limiting
   - Use HTTPS in production
   - Validate all inputs

4. **File Storage**
   - Configure S3 bucket policies
   - Use signed URLs for file access
   - Implement virus scanning

## Scaling

1. **Database**
   - Use read replicas for read-heavy workloads
   - Implement database connection pooling
   - Consider database sharding for large datasets

2. **Backend**
   - Use load balancers
   - Implement horizontal scaling
   - Use CDN for static assets

3. **Frontend**
   - Use CDN for global distribution
   - Implement caching strategies
   - Optimize bundle size

