# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

## [2.1.0] - 2026-04-14

### Changed
- **FastCore Integration** — Replaced `NativeLibraryLoader` with unified `FastCore.loadLibrary()`
- **Blueprint Structure** — Reorganized project with `examples/` folder separating demos from core library
- **Documentation** — Added `COMPILE.md` for build instructions, updated `README.md`

### Removed
- `NativeLibraryLoader.java` — functionality moved to FastCore

## [2.0.0] - 2026-04-06

### Added
- **DXGI Desktop Duplication API** — Hardware-accelerated screen streaming
- **60fps+ streaming** — Matches monitor refresh rate (60-240fps)
- **C++ scaling** — Hardware-accelerated frame scaling

## [1.0.0] - 2026-04-05

### Added
- GDI BitBlt screen capture
- DirectInput mouse/keyboard control
- Basic JNI wrapper for Win32 API
- Initial Maven project structure
