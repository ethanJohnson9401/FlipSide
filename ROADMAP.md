# FlipSide Roadmap

FlipSide is a personal catalog and discovery app for physical media, beginning with records, CDs, and cassettes. The product should make it easy to record what someone owns, track what they want, discover related releases, and understand their collection as it grows.

## Product vision

FlipSide should feel like a well-organized personal record shop: fast to browse, enjoyable to explore, and trustworthy enough to become the long-term home for a physical media collection.

The central product principles are:

- **Fast to capture:** adding an item should require very little typing.
- **Easy to browse:** users should be able to find items using familiar physical-world concepts such as crates, shelves, formats, and storage locations.
- **Useful over time:** collection history, spending, condition, and wishlist data should remain valuable as the collection grows.
- **Local and portable:** users should be able to keep, export, and restore their own data.
- **Personal discovery:** recommendations should reflect what a collector actually owns and wants.

## Current foundation

The current implementation already includes the core building blocks:

- Local media-item storage with SQLite/SQLDelight
- Collection and wishlist states
- Manual media entry
- Editing and deleting media items
- Moving wishlist items into the collection
- Media formats including vinyl, CD, and cassette
- Release metadata such as year, edition, condition, notes, price, and cover art
- MusicBrainz-powered search
- Cached search results
- Local recommendation scoring
- Home, collection, wishlist, detail, edit, add, and recommendation screens

The roadmap below focuses on making those capabilities reliable, easier to use, and more distinctive.

## Roadmap at a glance

| Phase | Theme | Primary outcome | Priority |
|---|---|---|---|
| 1 | Core MVP stability | A dependable personal catalog | P0 |
| 2 | Discovery and entry | Faster, more accurate adding | P0 |
| 3 | Collection browsing | Strong search, filters, and statistics | P0 |
| 4 | Milk Crate View | A physical-media-native way to browse and organize | P1 |
| 5 | Smart recommendations | Recommendations users understand and trust | P1 |
| 6 | Backup and portability | Safe export, import, and recovery | P0 |
| 7 | Polish and release readiness | A stable, accessible, release-quality app | P1 |

## Phase 1: Core MVP stability

### Goal

Make the collection and wishlist workflows reliable enough for everyday use.

### Features

- Validate required fields such as title and format.
- Validate numeric input for release year and purchase price.
- Normalize blank optional fields consistently.
- Add clear loading, empty, success, and error states.
- Add a confirmation step before deleting an item.
- Ensure moving an item from wishlist to collection preserves its metadata.
- Prevent accidental duplicate additions when the same release is entered twice.
- Preserve collection data after app restarts.
- Handle database errors without losing unsaved form data.
- Add stable database migrations for future schema changes.

### Quality work

- Unit tests for `MediaRepository` operations.
- Database tests for insert, update, delete, ownership, and wishlist transitions.
- Tests for blank-field normalization and invalid values.
- UI tests for the main add, edit, move, and delete flows.

### Acceptance criteria

- A user can add, edit, move, and delete items without data loss.
- Invalid forms explain what needs to be corrected.
- The collection and wishlist are correct after restarting the app.
- Destructive actions are intentional and recoverable where possible.

## Phase 2: Discovery and entry

### Goal

Make adding an item quick while keeping the resulting metadata useful.

### Features

- Debounced MusicBrainz search.
- Search cancellation when the query changes.
- Better handling for timeouts, rate limits, and no results.
- More accurate artist, release, year, country, status, and format mapping.
- Clear indication of which fields came from external metadata.
- Cover-art preview with replace and remove actions.
- Support for external release identifiers.
- Optional barcode or catalog-number entry when supported.
- More media formats over time, such as DVD, Blu-ray, VHS, and books if the product scope expands.
- A quick-add mode for users who only want title, artist, and format.

### Acceptance criteria

- A user can search for a release, select it, review the metadata, and add it with minimal editing.
- Search failures never block manual entry.
- Cover art is optional and does not prevent saving.
- The app avoids unnecessary repeated network requests.

## Phase 3: Collection browsing

### Goal

Make the collection useful at small and large sizes.

### Features

- Search across title, artist, edition, notes, and identifiers.
- Filters for format, artist, release year, condition, and ownership state.
- Sort by title, artist, date added, release year, purchase price, and condition.
- Clear indication of the active sort and filters.
- One-tap filter reset.
- Grid and list display modes.
- Recently added section.
- Favorites or pinned items.
- Collection statistics:
  - total item count
  - count by format
  - count by artist
  - total purchase cost
  - average price
  - release-year distribution
  - condition breakdown
- Optional price totals with an explicit distinction between purchase cost and estimated value.

### Acceptance criteria

- Users can find a known item quickly without scrolling through the entire collection.
- Sorting and filtering can be combined.
- The selected view and filters are clear and consistent.
- Statistics update when the collection changes.

## Phase 4: Milk Crate View

### Goal

Give FlipSide a distinctive browsing mode inspired by the way physical media is actually stored and explored.

### Product concept

Milk Crate View is a collection display and organization mode that presents media as groups of physical crates rather than only as a flat list or grid.

### Version 1: virtual crates

- Display the collection in crate-style sections.
- Create virtual crates automatically from the active sort order.
- Support configurable crate sizes, such as 25, 50, or 100 items.
- Show compact spine cards or album-cover cards inside each crate.
- Allow browsing one crate at a time or viewing all crates together.
- Keep normal search and filtering available.
- Make the current sort visible, for example “Crates sorted by artist.”

### Version 2: named crates

- Create custom crates with names such as “Jazz,” “To Sell,” “Favorites,” or “Unsorted.”
- Rename, reorder, archive, and delete crates without deleting media items.
- Assign an item to one or more crates.
- Store crate membership locally.
- Support a default “Unsorted” crate for newly added items.

### Version 3: physical storage

- Optional storage-location fields such as room, shelf, cabinet, or crate number.
- Show a physical location on the item detail page.
- Filter the collection by storage location.
- Support a printable or exportable crate inventory.

### UX principles

- The feature should be useful without requiring manual organization.
- Users should never lose an item by deleting or reorganizing a crate.
- The normal list and grid views remain available.
- The crate metaphor should improve browsing rather than become a second complicated database.

### Acceptance criteria

- A user can switch between Grid, List, and Milk Crate View.
- Existing collection filters continue to work in Milk Crate View.
- Virtual crates require no setup.
- Named crates can be created and reorganized without changing ownership data.
- Moving an item between crates does not duplicate or delete it.

## Phase 5: Smart recommendations

### Goal

Make recommendations feel relevant, transparent, and controllable.

### Features

- Improve artist and creator parsing.
- Exclude owned and wishlisted releases more reliably.
- Explain each recommendation, for example “Because you own 4 releases by this artist.”
- Add direct “Save to wishlist” actions.
- Add “Not interested” or dismiss actions.
- Group recommendations into categories:
  - More from artists you own
  - Related releases
  - Similar formats
  - Earlier or later releases
  - Discover something outside your usual collection
- Cache recommendations and provide an intentional refresh action.
- Avoid recommending the same release repeatedly.
- Track dismissed recommendation keys locally.

### Acceptance criteria

- Recommendations do not include owned items.
- Wishlist items are either excluded or clearly labeled.
- Each recommendation has a comprehensible reason.
- A user can save or dismiss a recommendation directly.
- Offline use falls back gracefully to cached candidates.

## Phase 6: Backup and portability

### Goal

Make users comfortable trusting FlipSide with a long-term collection.

### Features

- Export collection and wishlist to CSV.
- Export the full dataset to JSON.
- Import CSV and JSON with a preview before changes are applied.
- Define duplicate matching rules using identifiers and normalized title/artist/format keys.
- Show import errors by row rather than failing the entire import.
- Add database backup and restore.
- Add a clear “last backup” indicator.
- Document the export format.
- Support schema migrations without silently discarding fields.

### Acceptance criteria

- A user can export all collection data without losing optional fields.
- A user can restore an export into a fresh installation.
- Imports provide a summary of added, updated, skipped, and invalid rows.
- Existing data is backed up before an import changes it.

## Phase 7: Polish and release readiness

### Goal

Turn the working product into a consistent, accessible, and maintainable release.

### Features and quality work

- Consistent typography, spacing, colors, and component behavior.
- Responsive layouts for phones, tablets, and desktop-sized windows.
- Accessibility review for contrast, labels, touch targets, and screen readers.
- Keyboard navigation where relevant.
- Performance testing with hundreds and thousands of items.
- Image loading, caching, and failure handling improvements.
- Network retry and rate-limit behavior.
- Onboarding for first-time users.
- Settings screen for search, display, backup, and data preferences.
- About screen with attribution for external metadata and cover art sources.
- Release build configuration and installation documentation.

### Acceptance criteria

- The app remains responsive with a large collection.
- Core workflows work offline after data has been saved locally.
- Accessibility issues do not block basic collection management.
- A new user understands how to add their first item without instruction.

## Recommended delivery order

The recommended order is:

1. Core data safety and workflow reliability
2. Local search, filters, and sorting
3. Export and restore
4. Faster metadata-assisted adding
5. Milk Crate View, beginning with virtual crates
6. Recommendation quality and explanations
7. Named crates and physical storage locations
8. Visual polish, accessibility, and release packaging

This order protects the user’s data first, improves everyday utility next, and then adds FlipSide’s more distinctive experiences.

## Suggested first milestone

### Milestone: Collection MVP complete

The first milestone is complete when a user can:

- Add an item manually or from MusicBrainz.
- Edit all important metadata.
- Browse collection and wishlist items.
- Search, filter, and sort the collection.
- Move an item from wishlist to collection.
- Delete an item with confirmation.
- Restart the app without losing data.
- Export and restore their collection.

## Suggested second milestone

### Milestone: Collector browsing experience

The second milestone is complete when a user can:

- Switch between Grid, List, and Milk Crate View.
- Browse automatic virtual crates.
- Create and name custom crates.
- Assign items to crates.
- See useful collection statistics.
- Find an item by title, artist, format, or storage location.

## Open product decisions

These decisions should be made before implementing the later phases:

- Is FlipSide focused only on music media, or will it expand to movies, books, and games?
- Should an item be allowed in multiple named crates?
- Is a crate primarily a browsing category, a physical storage location, or both?
- Should estimated market value be included, or only purchase price?
- Should user data remain entirely local, or is optional account sync planned?
- Which platforms are release targets first?

## North-star outcome

FlipSide succeeds when a collector can open the app, see their physical collection represented naturally, add a new find in seconds, locate anything they own, and discover their next want without feeling like they are maintaining a spreadsheet.
