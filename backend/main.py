from flask import Flask, jsonify, request
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
from dotenv import load_dotenv
import os

load_dotenv()

app = Flask(__name__)
CORS(app)

# Database Configuration
app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL', 'sqlite:///analyzer.db')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

# Import blueprints
from api.routes import api_bp
from api.capture import capture_bp
from api.threat import threat_bp

# Register blueprints
app.register_blueprint(api_bp, url_prefix='/api')
app.register_blueprint(capture_bp, url_prefix='/api/capture')
app.register_blueprint(threat_bp, url_prefix='/api/threat')

@app.route('/health', methods=['GET'])
def health():
    return jsonify({'status': 'healthy', 'version': '1.0.0'})

@app.errorhandler(404)
def not_found(error):
    return jsonify({'error': 'Not Found'}), 404

@app.errorhandler(500)
def internal_error(error):
    return jsonify({'error': 'Internal Server Error'}), 500

if __name__ == '__main__':
    debug = os.getenv('DEBUG', 'False') == 'True'
    port = int(os.getenv('PORT', 5000))
    app.run(debug=debug, host='0.0.0.0', port=port)
