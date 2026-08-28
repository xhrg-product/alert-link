# Contributing to Alert Link

Thank you for your interest in contributing to Alert Link! This document provides guidelines and instructions for contributing.

## How to Contribute

### Reporting Bugs

Before creating bug reports, please check existing issues. When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce** the behavior
- **Expected vs actual behavior**
- **Screenshots** if applicable
- **Environment details** (Java version, OS, etc.)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. Please describe:

- **Use case** for the enhancement
- **Current behavior** and why it's insufficient
- **Proposed solution** or alternative considered

### Pull Requests

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass (`mvn test`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

## Development Guidelines

### Code Style

- Follow standard Java naming conventions
- Use meaningful variable and method names
- Keep methods small and focused
- Add JavaDoc comments for public APIs

### Testing

- Write unit tests for new features
- Aim for good code coverage
- Ensure tests are deterministic

### Commit Messages

Use clear and descriptive commit messages:

```
feat: add DingTalk signature support
fix: resolve alert deduplication issue
docs: update README with configuration examples
```

## Code of Conduct

- Be respectful and inclusive
- Focus on constructive feedback
- Welcome newcomers and help them learn

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.
