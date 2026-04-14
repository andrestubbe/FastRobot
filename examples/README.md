# FastXXX Examples

Self-contained example projects demonstrating FastXXX usage.

## Structure

```
examples/
├── 00-basic-usage/          # ⬅️ START HERE - Minimal usage
│   ├── pom.xml              # Standalone Maven project
│   └── src/main/java/...    # Example code
├── 10-advanced/             # Advanced usage
└── 20-ui-demo/              # (Add more examples here)
```

## Running Examples

Each example is a standalone Maven project:

```bash
cd examples/00-basic-usage
mvn compile exec:java
```

## Why Root-Level examples/?

- **Not part of the library** - Examples are standalone mini-projects
- **Not tests** - Examples are tutorials, not JUnit tests
- **Easy to run** - Each has its own `pom.xml` and main class
- **Copy-paste friendly** - Users can copy an example as a starter template
- **SEO-friendly** - Each example can have its own README

## Adding New Examples

1. Create folder: `examples/XX-my-example/` (use number prefix for sorting)
2. Add `pom.xml` with dependency on `fastXXX`
3. Add source code in `src/main/java/...`
4. Add README explaining the example
