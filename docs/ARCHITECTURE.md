# Architecture

Helios Lightfield is a native Android application containing a `DreamService`, settings activity, preview activity, and app-owned settings provider. The DreamService uses an OpenGL ES 2.0 fragment shader to render animated elliptical light strands on a dark field. The shader draws a bounded number of luminous filaments with independently adjustable motion, density, glow, and palette values. A single full-screen quad is reused; no media decoder, network access, wake lock, or background service is used. Frame animation derives from elapsed time and allocates no per-frame geometry.

A single four-vertex full-screen strip is created once. The fragment shader computes animated elliptical light strands from elapsed time, keeping motion independent of frame count and avoiding per-frame allocations. The GL surface switches to `RENDERMODE_WHEN_DIRTY` when the dream stops. The app uses no video decoding, wake lock, runtime network access, or background service.

`LightOptions` resolves persisted settings and random choices into bounded renderer values. `SettingsValues` validates updates through the content provider. Both are pure Java and covered by line and branch tests. Android lifecycle, shaders, and rendered pixels require the emulator checks documented in [verification](VERIFICATION.md).

The separate Python installer retrieves only this repository's release APK and checksum after a user starts it, verifies HTTPS GitHub release assets, trusted redirects, file size, and SHA-256, then invokes ADB. It does not change system screensaver or power settings.
