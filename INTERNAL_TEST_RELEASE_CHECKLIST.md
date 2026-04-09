# Internal Test Release Readiness Checklist

## Completed in this branch
- App naming normalized to **Calories Tracker**.
- Package namespace and application id set to `com.caloriesapp`.
- Launcher icon uses a clean adaptive vector placeholder (no raster temp assets).
- Manifest includes baseline production-friendly defaults:
  - `android:exported` explicitly set on launcher activity
  - app label/icon/roundIcon/supported RTL/theme configured
  - backup and extraction rules referenced
- SDK levels set for modern Play expectations:
  - `minSdk = 24`
  - `targetSdk = 35`
  - `compileSdk = 35`
- Debug behavior scoped to `debug` buildType via suffixes only.
- Release build has minification enabled and proguard baseline configured.
- In-app Privacy Note screen added for local-only data disclosure.

## Before uploading to Google Play Internal Testing
- Generate upload keystore and configure Play App Signing.
- Build an AAB: `./gradlew :app:bundleRelease`.
- Fill Play Console Data safety section (local-only processing declaration as applicable).
- Provide screenshots, feature graphic, and final listing copy.
- Run quick smoke tests on at least one phone + one emulator.
