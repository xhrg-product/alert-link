# Security Policy

## Supported Versions

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |

## Reporting a Vulnerability

We take the security of Alert Link seriously. If you believe you have found a security vulnerability, please report it to us as described below.

**Please do NOT report security vulnerabilities through public GitHub issues.**

Instead, please send an email to [security@example.com](mailto:security@example.com) with the following information:

- Description of the vulnerability
- Steps to reproduce the issue
- Potential impact
- Suggested fix (if any)

You should receive a response within 48 hours. If for some reason you do not, please follow up via email to ensure we received your original message.

## Security Best Practices

### Webhook Security

- Keep your DingTalk webhook URL and secret confidential
- Use HTTPS for all webhook endpoints
- Validate incoming webhook requests

### Configuration

- Never commit sensitive credentials (webhook URLs, secrets, passwords) to version control
- Use environment variables or external configuration for sensitive data
- Rotate secrets regularly

### Network Security

- Restrict access to the alert endpoints using firewall rules or authentication
- Monitor for unusual alert patterns that might indicate abuse

## Acknowledgments

We appreciate responsible disclosure of security vulnerabilities.
