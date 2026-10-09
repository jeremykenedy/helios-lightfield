# Configuration

Open Helios Lightfield from the TV launcher or device screensaver settings. The remote-friendly settings page includes a full-screen preview. Values persist locally and apply the next time a screensaver session starts.

| Setting | Default | Choices |
|---|---|---|
| Motion speed | Slow | Slow, Medium, Fast, Random |
| Light strands | Balanced | Few, Balanced, Many, Random |
| Glow intensity | Luminous | Soft, Luminous, Intense, Random |
| Color palette | Solar amber | Solar amber, Polar blue, Random |
| Randomize all settings each start | Off | Off, On |

Each setting supports Random independently. Enabling global randomization selects all four settings again when the DreamService starts. Changes apply on the next screensaver session.

The app's settings provider exposes its schema and current values at:

- `content://com.jeremykenedy.helioslightfield.settings/schema`
- `content://com.jeremykenedy.helioslightfield.settings/settings`

The schema cursor has `key`, `title`, `type`, `default`, `choices`, and `randomAllowed` columns. The settings cursor has `key` and `value`. To update one value, call `ContentResolver.update()` on `/settings` with `ContentValues` containing `key` and `value`. The provider rejects unknown keys and values. `randomize_all` uses a boolean string; other settings use strings from the listed choices.

These app-specific controls are available through this activity and provider. The Fire TV UI's screensaver picker selects installed dreams but does not currently expose a generic settings editor.
