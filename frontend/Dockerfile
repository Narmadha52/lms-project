# Use Node 18 LTS
FROM node:18-alpine

WORKDIR /app

# Copy package files and install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy source code
COPY . .

# Build React app
RUN npm run build

# Install lightweight server to serve static files
RUN npm install -g serve

# Expose port 3000
EXPOSE 3000

# Start React app
CMD ["serve", "-s", "build", "-l", "3000"]
