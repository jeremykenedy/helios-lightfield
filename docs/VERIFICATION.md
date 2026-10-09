# Device verification

## Emulator checks

| Device | OS/API | Resolution | Verified |
|---|---|---:|---|
| Google Android TV emulator (`sdk_google_atv64_arm64`, emulator-5570) | Android 12 / API 31 | 1920x1080 | APK installed and launched; animated preview rendered and changed between captures 3 seconds apart; settings screen opened; provider schema and settings queried; a supported speed update applied and reset. The DreamService idle activation route is unavailable in this image. |

The emulator was not changed from its original system state: screensaver component `com.google.android.backdrop/.Backdrop`, enabled `1`, activate-on-sleep `0`, and screen-off timeout `1800000` ms. Helios Lightfield was uninstalled after testing.

| Capture | SHA-256 |
|---|---|
| [Animated preview](screenshots/helios-lightfield-preview.png) | `f2524954914c18fe8560a550d1dca2ec0808bdb0adc599fe1ea991e016c872d5` |
| [Settings screen](screenshots/settings-android-tv.png) | `2bf0e082625cec352808f5a44565165c591b17ee3c0c532b11f5fc0851c5aceb` |

The DreamService service metadata and app-owned settings provider are verified in the built APK. Automatic DreamService activation remains unverified because this emulator image does not expose the screensaver settings route. The emulator does not establish physical-device behavior, native 4K composition, thermal behavior, or Fire OS idle startup.

## Physical device matrix

| Platform | Status | Test request |
|---|---|---|
| Fire TV | Untested on physical hardware | We are looking for a Fire TV owner to test install, screensaver selection and idle activation, remote settings, update, uninstall, and sleep/wake behavior. Please report model, Fire OS/API, display resolution, steps, and results through the [issue tracker](https://github.com/jeremykenedy/helios-lightfield/issues). |
| Android TV | Untested on physical hardware | We are looking for an Android TV owner to test install, screensaver selection and idle activation, remote settings, update, uninstall, and sleep/wake behavior. Please report model, Android version/API, display resolution, steps, and results through the [issue tracker](https://github.com/jeremykenedy/helios-lightfield/issues). |
| Google TV | Untested on physical hardware | We are looking for a Google TV owner to test install, ambient display selection and idle activation, remote settings, update, uninstall, and sleep/wake behavior. Please report model, Android version/API, display resolution, steps, and results through the [issue tracker](https://github.com/jeremykenedy/helios-lightfield/issues). |

No physical TV has been used for testing. A device emulator does not establish Fire OS behavior, native 4K output, thermal behavior, or vendor-specific idle activation.
