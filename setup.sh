#!/bin/bash
set -e

echo "🌐 Network Packet Analyzer - Setup Script"
echo "========================================"

# Check Python
if ! command -v python3 &> /dev/null; then
    echo "❌ Python 3 is required but not installed"
    exit 1
fi

# Check Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is required but not installed"
    exit 1
fi

echo "✅ Python $(python3 --version) and Node.js $(node --version) found"

# Install backend dependencies
echo ""
echo "📦 Installing backend dependencies..."
cd backend
pip install -r requirements.txt
cd ..

# Install frontend dependencies
echo ""
echo "📦 Installing frontend dependencies..."
cd frontend
npm install
cd ..

echo ""
echo "✅ Installation complete!"
echo ""
echo "🚀 To start the application:"
echo "   1. Terminal 1 - Backend:  cd backend && python main.py"
echo "   2. Terminal 2 - Frontend: cd frontend && npm run dev"
echo ""
echo "Or use Makefile: make dev"
