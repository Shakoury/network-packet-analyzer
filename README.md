# Network Packet Analyzer - Android Edition

A powerful, educational Android packet capture and analysis tool that displays raw network packets without Android restrictions. Designed for security researchers, educators, and developers to analyze network traffic at the packet level.

## ⚠️ Important Notice

**This tool is for educational and authorized security analysis purposes only.**

### Prerequisites
- Android device with **root access** (rooted device)
- Android 6.0 (API 23) or higher
- Proper authorization to monitor network traffic on the target device/network

### Ethical Usage Requirements
- Only use on devices you own or have explicit written permission to analyze
- Never use for unauthorized network surveillance or interception
- Respect privacy laws and regulations in your jurisdiction
- This tool must only be used for:
  - Educational learning
  - Personal network security analysis
  - Authorized penetration testing
  - Network debugging and development

## Features

✅ **Raw Packet Capture** - Capture all network packets without Android restrictions
✅ **Low-Level Access** - C/C++ JNI implementation for direct packet access
✅ **Real-time Display** - View packets as they traverse the network
✅ **Packet Analysis** - Decode and analyze network protocols (TCP, UDP, ICMP, DNS, HTTP)
✅ **Hex Dump View** - Display raw packet data in hexadecimal format
✅ **Protocol Filtering** - Filter packets by protocol type, IP, port
✅ **Statistics** - Network traffic statistics and summaries
✅ **Export Functionality** - Save captured packets in PCAP format
✅ **Real-time Visualization** - Traffic graphs and statistics

## Quick Start

```bash
# Clone repository
git clone https://github.com/Shakoury/network-packet-analyzer.git
cd network-packet-analyzer

# Build APK
./gradlew clean build

# Install on rooted device
./gradlew installDebug
```

## Installation

For detailed setup instructions, see [SETUP.md](docs/SETUP.md)

## Technical Stack

- **Frontend**: Kotlin, Android Framework, Material Design 3
- **Networking**: C/C++ (JNI) for raw socket packet capture
- **Low-Level Access**: Raw sockets, netlink sockets, kernel interfaces
- **Data Storage**: SQLite for packet history
- **Analysis**: Protocol parsing and decoding (TCP/IP stack)

## How It Works

This application uses a multi-layered architecture:

1. **Android UI Layer** - Kotlin-based user interface
2. **Service Layer** - Background packet capture service
3. **JNI Bridge** - Communication between Kotlin and native code
4. **Native Layer** - C/C++ implementation for raw packet capture
5. **Kernel Level** - Direct Linux socket access

For detailed architecture, see [ARCHITECTURE.md](docs/ARCHITECTURE.md)

## Supported Protocols

- **Layer 2**: Ethernet, ARP
- **Layer 3**: IPv4, IPv6, ICMP
- **Layer 4**: TCP, UDP, IGMP
- **Layer 7**: DNS, HTTP/HTTPS, SSL/TLS, FTP, SSH, Telnet

## Security & Legal Compliance

### ✅ Authorized Uses
- Analyze traffic on your own device
- Educational learning and network research
- Authorized penetration testing (with written permission)
- Network debugging during development
- Security analysis of your own infrastructure

### ❌ Prohibited Uses
- Unauthorized network surveillance
- Intercepting others' network traffic without consent
- Commercial use without proper licensing
- Violating computer fraud and abuse laws
- Privacy violations or GDPR violations

## Legal Disclaimer

**IMPORTANT**: The authors and contributors of this project assume **NO LIABILITY** for improper or illegal use of this tool.

Users are **SOLELY RESPONSIBLE** for:
- Ensuring compliance with all applicable laws and regulations
- Obtaining proper authorization before analyzing network traffic
- Respecting the privacy and security of others
- Understanding the legal consequences of unauthorized network monitoring

By using this tool, you agree that:
1. You will only use it on devices/networks you own or have explicit permission to monitor
2. You understand and accept legal responsibilities
3. You will comply with all applicable laws (CFAA, GDPR, local laws, etc.)

## Documentation

- **[Setup Guide](docs/SETUP.md)** - Installation and configuration
- **[Architecture](docs/ARCHITECTURE.md)** - System design overview
- **[Packet Format](docs/PACKET_FORMAT.md)** - Network packet reference
- **[Troubleshooting](docs/SETUP.md#troubleshooting)** - Common issues and solutions

## Project Structure

```
network-packet-analyzer/
├── app/
│   ├── src/main/
│   │   ├── cpp/              # Native C/C++ code
│   │   ├── java/             # Kotlin/Java source
│   │   └── res/              # Resources
│   └── build.gradle.kts
├── docs/                      # Documentation
├── build.gradle.kts
├── settings.gradle.kts
├── LICENSE
└── README.md
```

## License

MIT License - See [LICENSE](LICENSE) file

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## Support

For issues and questions:
- Check [documentation](docs/)
- Review [existing issues](https://github.com/Shakoury/network-packet-analyzer/issues)
- Open a new issue with details

---

**Remember**: This is a powerful tool requiring root access. Use responsibly and ethically. Always comply with applicable laws and regulations in your jurisdiction.
