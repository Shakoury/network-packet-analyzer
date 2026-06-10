FROM python:3.11-slim

WORKDIR /app

# System dependencies
RUN apt-get update && apt-get install -y \
    nodejs npm \
    libpcap-dev \
    && rm -rf /var/lib/apt/lists/*

# Install backend dependencies
COPY backend/requirements.txt ./backend/
RUN pip install --no-cache-dir -r backend/requirements.txt

# Install frontend dependencies
COPY frontend/package*.json ./frontend/
WORKDIR /app/frontend
RUN npm install --production
WORKDIR /app

# Copy application files
COPY backend/ ./backend/
COPY frontend/src ./frontend/src
COPY frontend/index.html ./frontend/
COPY frontend/vite.config.js ./frontend/

# Build frontend
RUN cd frontend && npm run build

# Expose ports
EXPOSE 5000 3000

# Create startup script
RUN mkdir -p /app/logs

# Start backend (frontend is served as static files)
CMD ["python", "backend/main.py"]
