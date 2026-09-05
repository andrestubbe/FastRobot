# Changelog

All notable changes to this project will be documented in this file.

## [0.1.1] - 2026-09-04

### Added
- **FastImage Ecosystem Integration**:
  - Direct bridge methods to `FastImage 0.1.2`:
    - `robot.captureImage(Rectangle rect)`: Direct capture to off-heap `FastImage` buffer.
    - `robot.captureImage(int x, int y, int w, int h)`: Primitive bounds capture to `FastImage`.
    - `robot.getFrameImage()`: Wrap internal capture frame into `FastImage` with zero heap allocations.
- **Modern Build Chain**:
  - Automated detection of Visual Studio 2026 and 2022 Community via `vswhere.exe`.
  - Automatic deployment of `fastrobot.dll` into resources and target directory.
- Standardized project README and Maven dependency chain.

## [0.1.0] - 2026-05-23

### Added
- Initial release
- Standardized FastJava ecosystem module
