# SkillSwap 🤝

A hyperlocal neighborhood skill exchange platform for Android.

## Concept

People list skills they can offer and skills they need. The app matches neighbors and uses a time-credit system (1 hour of help = 1 credit) to keep exchanges fair.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material Design 3 (Material You) |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Backend | Firebase (Auth, Firestore, Cloud Messaging, Storage) |
| Maps | Google Maps SDK + Fused Location Provider |
| Image Loading | Coil |
| Async | Kotlin Coroutines + Flow |

## Project Structure

```
app/src/main/java/com/skillswap/app/
├── di/                  # Hilt dependency injection modules
├── domain/
│   ├── model/           # Data models (User, Skill, LatLng, TimeCredit, Review)
│   └── repository/      # Repository interfaces (pure Kotlin, no Android deps)
├── data/
│   └── repository/      # Firebase implementations
├── ui/
│   ├── theme/           # Material 3 theme (colors, typography, dynamic color)
│   ├── navigation/      # Nav graph, routes, bottom navigation
│   └── screens/
│       ├── auth/        # Welcome, Sign In, Sign Up
│       ├── onboarding/  # Location setup, Skills setup
│       ├── home/        # Home feed with category filters
│       └── profile/     # Profile view with skills & trust score
├── MainActivity.kt      # Entry point, edge-to-edge, splash screen
├── MainViewModel.kt     # Auth state observation
└── SkillSwapApplication.kt  # Hilt application
```

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34 (min SDK 26)

### Setup

1. **Clone the repo**
   ```bash
   git clone https://github.com/yourusername/SkillSwap.git
   cd SkillSwap
   ```

2. **Firebase Setup**
   - Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
   - Enable Authentication → Sign-in providers: Google + Email/Password
   - Create a Cloud Firestore database
   - Download `google-services.json` and place it in `app/`

3. **Google Maps**
   - Enable "Maps SDK for Android" in [Google Cloud Console](https://console.cloud.google.com)
   - Create an API key
   - Replace `YOUR_MAPS_API_KEY` in `app/src/main/AndroidManifest.xml`

4. **Build & Run**
   - Open in Android Studio (it will auto-download the Gradle wrapper)
   - Wait for Gradle sync to complete
   - Run on device/emulator (API 26+)
   
   Or from command line (after Android Studio generates the wrapper):
   ```bash
   ./gradlew assembleDebug
   ```

## Features

### MVP (Phase 1) — Current
- [x] Onboarding flow (Google/Email auth → Location setup → Skills setup)
- [x] Profile screen (photo, name, neighborhood, trust score, credits, skills)
- [x] Home feed with category filters (UI ready, data placeholder)
- [x] Material You dynamic color theming
- [x] Dark mode support
- [x] Bottom navigation (Home, Discover, Messages, Wallet, Profile)
- [x] Accessible UI (semantic headings, content descriptions, touch targets)

### Phase 2 — Planned
- [ ] Discovery feed with real Firestore queries + distance filtering
- [ ] Smart matching algorithm (mutual skill needs)
- [ ] In-app messaging (Firestore chat collections)
- [ ] Time-credit wallet (earn/spend/transaction history)
- [ ] Ratings & reviews after swaps
- [ ] Push notifications (Firebase Cloud Messaging)
- [ ] Profile photo upload

## Architecture

The app follows **MVVM with Clean Architecture** principles:

- **Domain layer** — Pure Kotlin. Models and repository interfaces with no Android or Firebase dependencies.
- **Data layer** — Firebase implementations of repository interfaces. Easily swappable.
- **UI layer** — Jetpack Compose screens + ViewModels. One-way data flow via StateFlow.
- **DI** — Hilt provides all dependencies. Single `AppModule` for the MVP.

## Configuration

| File | Purpose | Committed? |
|------|---------|-----------|
| `google-services.json` | Firebase config with API keys | ❌ gitignored |
| `local.properties` | Android SDK path | ❌ gitignored |
| `AndroidManifest.xml` | Maps API key placeholder | ✅ (replace key) |

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE) for details.
