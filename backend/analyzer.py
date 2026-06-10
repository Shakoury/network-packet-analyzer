from models import db, Packet, Flow, Alert
from api.capture import capture_instance
from api.threat import detector
import time
from datetime import datetime, timedelta

class PacketAnalyzer:
    
    @staticmethod
    def build_flows():
        """Aggregate packets into flows"""
        packets = Packet.query.all()
        flow_dict = {}
        
        for packet in packets:
            flow_key = (packet.src_ip, packet.dst_ip, packet.protocol)
            
            if flow_key not in flow_dict:
                flow = Flow(
                    src_ip=packet.src_ip,
                    dst_ip=packet.dst_ip,
                    protocol=packet.protocol,
                    start_time=packet.timestamp
                )
                db.session.add(flow)
                db.session.flush()
                flow_dict[flow_key] = flow
            else:
                flow = flow_dict[flow_key]
            
            flow.packet_count += 1
            flow.total_bytes += packet.payload_size
            flow.end_time = packet.timestamp
        
        db.session.commit()
    
    @staticmethod
    def analyze_all_packets():
        """Analyze all packets for threats"""
        packets = Packet.query.filter(Packet.id.notin_(
            db.session.query(Alert.packet_id)
        )).all()
        
        threat_count = 0
        for packet in packets:
            alerts, risk_score = detector.analyze_packet(packet)
            
            if risk_score > 30:
                threat_count += 1
            
            for alert_data in alerts:
                alert = Alert(
                    packet_id=packet.id,
                    alert_type=alert_data['type'],
                    severity=alert_data['severity'],
                    risk_score=risk_score,
                    description=alert_data['description']
                )
                db.session.add(alert)
        
        db.session.commit()
        return threat_count
    
    @staticmethod
    def get_protocol_distribution():
        """Get distribution of protocols"""
        from sqlalchemy import func
        
        distribution = db.session.query(
            Packet.protocol,
            func.count(Packet.id).label('count')
        ).group_by(Packet.protocol).all()
        
        return {proto: count for proto, count in distribution}
    
    @staticmethod
    def get_top_ips(limit=10):
        """Get top source and destination IPs"""
        from sqlalchemy import func
        
        src_ips = db.session.query(
            Packet.src_ip,
            func.count(Packet.id).label('count')
        ).group_by(Packet.src_ip).order_by(func.count(Packet.id).desc()).limit(limit).all()
        
        dst_ips = db.session.query(
            Packet.dst_ip,
            func.count(Packet.id).label('count')
        ).group_by(Packet.dst_ip).order_by(func.count(Packet.id).desc()).limit(limit).all()
        
        return {
            'source_ips': [{'ip': ip, 'count': count} for ip, count in src_ips],
            'dest_ips': [{'ip': ip, 'count': count} for ip, count in dst_ips]
        }
    
    @staticmethod
    def get_timeline_data(hours=24):
        """Get packet timeline data"""
        from sqlalchemy import func
        
        cutoff = datetime.utcnow() - timedelta(hours=hours)
        
        timeline = db.session.query(
            func.strftime('%Y-%m-%d %H:00', Packet.timestamp).label('hour'),
            func.count(Packet.id).label('count')
        ).filter(Packet.timestamp >= cutoff).group_by(
            func.strftime('%Y-%m-%d %H:00', Packet.timestamp)
        ).all()
        
        return [{'time': hour, 'count': count} for hour, count in timeline]

analyzer = PacketAnalyzer()
