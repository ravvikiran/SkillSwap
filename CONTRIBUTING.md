# Contributing to SkillSwap

Thanks for considering contributing to SkillSwap! Here's how to get started.

## Development Setup

1. **Prerequisites:**
   - Android Studio Hedgehog (2023.1.1) or later
   - JDK 17
   - Android SDK 34

2. **Firebase:**
   - Create your own Firebase project
   - Add `google-services.json` to `app/` (this file is gitignored)
   - Enable Authentication (Google + Email/Password providers)
   - Create a Firestore database in test mode

3. **Google Maps:**
   - Enable Maps SDK for Android in Google Cloud Console
   - Add your API key to `AndroidManifest.xml`

## Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use MVVM architecture with clean architecture layers
- Keep composables small and focused
- All UI strings should be in `strings.xml` for localization readiness

## Branch Strategy

- `main` — stable, release-ready code
- `develop` — integration branch for features
- `feature/*` — individual feature branches

## Pull Request Process

1. Fork the repo and create your branch from `develop`
2. Make your changes with clear, atomic commits
3. Ensure the app builds without errors
4. Update the README if needed
5. Submit a PR with a clear description of changes

## Architecture

```
domain/     → Pure Kotlin models and repository interfaces (no Android deps)
data/       → Firebase implementations of repository interfaces
di/         → Hilt dependency injection modules
ui/         → Compose screens, ViewModels, theme, navigation
```

## Reporting Issues

- Use GitHub Issues
- Include device info, Android version, and steps to reproduce
- Screenshots or recordings are helpful
