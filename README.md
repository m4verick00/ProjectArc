# ArcLogbook

A native Android app written in Kotlin using Jetpack Compose, Room, Hilt, and Microsoft Graph SDK for OneDrive sync. Features a cyberpunk/hacker theme, MVVM architecture, and modules for Logbook, OSINT Automation Toolkit, and Dark Web Monitoring Dashboard.

## Features
- Cyberpunk dark theme with neon accents
- MVVM architecture
- Room database (with optional encryption)
- Hilt for dependency injection
- Microsoft Graph SDK for OneDrive backup/restore
- Biometric authentication for privacy
- Logbook, OSINT Toolkit, Dark Web Monitoring modules

## Build & Run
1. Open this project in Android Studio (Giraffe or newer recommended).
2. Connect a Pixel 7 (or emulator, API 34+).
3. Run `Build Android App` or `Run Android App` from VS Code tasks or use Android Studio's build/run buttons.

## Notes
- All data is stored locally and can be synced to OneDrive.
- For OneDrive sync, sign in with your Microsoft account in the app settings.
- Replace placeholder assets/fonts as needed for production.
