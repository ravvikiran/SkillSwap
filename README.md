# SkillSwap 🤝

A hyperlocal neighborhood skill exchange platform for Android.

## Concept
People list skills they can offer and skills they need. The app matches neighbors and uses a time-credit system (1 hour of help = 1 credit) to keep exchanges fair.

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material Design 3 (Material You)
- **Architecture:** MVVM + Clean Architecture (domain/data/ui layers)
- **DI:** Hilt
- **Backend:** Firebase (Auth, Firestore, Cloud Messaging, Storage)
- **Maps:** Google Maps SDK + Location Services
- **Image Loading:** Coil

## Project Structure
```
app/src/main/java/com/skillswap/app/
├── di/                  # Dependency injection modules
├── domain/
│   ├── model/           # Data models (User, Skill, TimeCredit, etc.)
│   └── repository/      # Repository interfaces
├── data/
│   └── repository/      # Firebase repository implementations
├── ui/
│   ├── theme/           # Material 3 theme (colors, typography)
│   ├── navigation/      # Nav graph, routes, bottom nav
│   └── screens/
│       ├── auth/        # Welcome, Sign In, Sign Up
│       ├── onboarding/  # Location setup, Skills setup
│       ├── home/        # Home feed
│       └── profile/     # Profile view
├── MainActivity.kt
├── MainViewModel.kt
└── SkillSwapApplication.kt
```

## Setup

1. **Clone the repo**

2. **Firebase Setup:**
   - Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
   - Enable Authentication (Google + Email/Password)
   - Create a Firestore database
   - Download `google-services.json` and place it in `app/`

3. **Google Maps:**
   - Get a Maps API key from [Google Cloud Console](https://console.cloud.google.com)
   - Replace `YOUR_MAPS_API_KEY` in `AndroidManifest.xml`

4. **Build & Run:**
   - Open in Android Studio Hedgehog or later
   - Sync Gradle
   - Run on device/emulator (API 26+)

## MVP Features (Phase 1)
- [x] Onboarding (Google/email auth + neighborhood setup)
- [x] Profile (skills offered/needed, trust score)
- [ ] Discovery feed (nearby listings, category filters)
- [ ] Smart matching (mutual needs)
- [ ] In-app messaging
- [ ] Time-credit wallet
- [ ] Ratings & reviews

## Design Principles
- Material You dynamic color theming
- Dark mode from day one
- Accessible (proper contrast, touch targets, screen reader labels)
- Clean whitespace, smooth animations
- Bottom navigation (Home, Discover, Messages, Wallet, Profile)
