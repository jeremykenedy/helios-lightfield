# Releasing

1. Update the package version name and monotonically increasing version code.
2. Finish source, settings, docs, banner, screenshots, repository metadata, and workflows.
3. Run the complete local tests, coverage, style, privacy, documentation, artifact, and device checks.
4. Perform a read-only production sweep of the exact release candidate, including README links, screenshots, license/notices, manifest, APK contents, commands, checksums, and compatibility claims.
5. Run the required CI suite against that exact commit as the final validation. Make no source, documentation, asset, workflow, or metadata changes afterward. If anything needs correction, repeat the affected local checks, sweep, and CI.
6. Verify the APK signature, package, version, DreamService declaration, permissions, and signing-certificate continuity.
7. Generate `helios-lightfield.apk.sha256` from the final signed APK. Publish both files with a SemVer tag and release notes describing features, fixes, platform evidence, commands, and upgrade path.
8. Download the published assets and verify the checksum against the published file. Keep the release key secure and backed up.

Do not overwrite a published tag. Use a new patch version for fixes after release.
