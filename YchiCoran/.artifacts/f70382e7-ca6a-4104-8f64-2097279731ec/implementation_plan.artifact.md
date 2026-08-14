# Fix Duplicate Resources Error

The build fails with a "Duplicate resources" error due to multiple definitions of the `ic_launcher_background` color resource in the `:app` module. Specifically, it is defined in both `res/values/colors.xml` and `res/values/ic_launcher_background.xml`, and also has an empty definition in `res/values-night/colors.xml`.

## Proposed Changes

### Resource Cleanup

#### [MODIFY] [colors.xml](file:///D:/produkt/YchiCoran/app/src/main/res/values/colors.xml)
- Set the correct value for `ic_launcher_background` (#0E854B).

#### [MODIFY] [colors.xml](file:///D:/produkt/YchiCoran/app/src/main/res/values-night/colors.xml)
- Remove the redundant/empty `ic_launcher_background` definition.

#### [DELETE] [ic_launcher_background.xml](file:///D:/produkt/YchiCoran/app/src/main/res/values/ic_launcher_background.xml)
- Remove this redundant file as its content is moved to `colors.xml`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:packageDebugResources` to verify that the duplication error is resolved.
