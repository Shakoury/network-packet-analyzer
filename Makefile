.PHONY: install dev prod test clean help

help:
	@echo "Network Packet Analyzer - Build Commands"
	@echo "========================================="
	@echo ""
	@echo "Available targets:"
	@echo "  make install      - Install all dependencies"
	@echo "  make dev          - Start development servers"
	@echo "  make prod         - Build and run production"
	@echo "  make test         - Run tests"
	@echo "  make clean        - Clean build artifacts"
	@echo "  make docker-build - Build Docker image"
	@echo "  make docker-run   - Run Docker container"

install:
	@echo "📦 Installing dependencies..."
	cd backend && pip install -r requirements.txt
	cd frontend && npm install
	@echo "✅ Installation complete"

dev:
	@echo "🚀 Starting development servers..."
	@echo "Backend: http://localhost:5000"
	@echo "Frontend: http://localhost:5173"
	@echo ""
	@echo "Press Ctrl+C to stop"
	@echo ""
	@(cd backend && python main.py) & (cd frontend && npm run dev)

prod:
	@echo "🏗️  Building for production..."
	cd frontend && npm run build
	@echo "✅ Build complete"
	@echo "🚀 Starting production server..."
	cd backend && gunicorn --workers 4 --bind 0.0.0.0:5000 main:app

test:
	@echo "🧪 Running tests..."
	cd backend && python -m pytest tests/ -v

clean:
	@echo "🧹 Cleaning build artifacts..."
	find . -type d -name __pycache__ -exec rm -rf {} + 2>/dev/null || true
	find . -type f -name "*.pyc" -delete 2>/dev/null || true
	cd frontend && rm -rf node_modules dist build 2>/dev/null || true
	rm -rf backend/*.db 2>/dev/null || true
	@echo "✅ Clean complete"

docker-build:
	@echo "🐳 Building Docker image..."
	docker build -t network-packet-analyzer:latest .
	@echo "✅ Docker image built"

docker-run:
	@echo "🚀 Running Docker container..."
	docker run -it -p 5000:5000 -p 3000:3000 network-packet-analyzer:latest

lint:
	@echo "🔍 Running linter..."
	cd backend && python -m pylint api/ models.py main.py || true
	cd frontend && npm run lint || true
