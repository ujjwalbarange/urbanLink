---
name: Urban Link Civic System
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#434655'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#737686'
  outline-variant: '#c3c6d7'
  surface-tint: '#0053db'
  primary: '#004ac6'
  on-primary: '#ffffff'
  primary-container: '#2563eb'
  on-primary-container: '#eeefff'
  inverse-primary: '#b4c5ff'
  secondary: '#006c49'
  on-secondary: '#ffffff'
  secondary-container: '#6cf8bb'
  on-secondary-container: '#00714d'
  tertiary: '#784b00'
  on-tertiary: '#ffffff'
  tertiary-container: '#996100'
  on-tertiary-container: '#ffeedd'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b4c5ff'
  on-primary-fixed: '#00174b'
  on-primary-fixed-variant: '#003ea8'
  secondary-fixed: '#6ffbbe'
  secondary-fixed-dim: '#4edea3'
  on-secondary-fixed: '#002113'
  on-secondary-fixed-variant: '#005236'
  tertiary-fixed: '#ffddb8'
  tertiary-fixed-dim: '#ffb95f'
  on-tertiary-fixed: '#2a1700'
  on-tertiary-fixed-variant: '#653e00'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
  canvas-bg: '#F8FAFC'
  surface-subtle: '#F1F5F9'
  surface-glass: rgba(255, 255, 255, 0.88)
  glass-border: rgba(226, 232, 240, 0.80)
  primary-dark: '#1D4ED8'
  text-primary: '#0F172A'
  text-secondary: '#64748B'
  text-muted: '#94A3B8'
  emergency-red: '#EF4444'
  emergency-bg: '#FEF2F2'
  emergency-border: '#FCA5A5'
  success-bg: '#ECFDF5'
  warning-bg: '#FFFBEB'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 34px
    fontWeight: '800'
    lineHeight: 40px
  display-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '800'
    lineHeight: 34px
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 30px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '700'
    lineHeight: 26px
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 18px
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '700'
    lineHeight: 16px
    letterSpacing: 0.04em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '700'
    lineHeight: 14px
    letterSpacing: 0.06em
  mono-ticket:
    fontFamily: Space Grotesk
    fontSize: 13px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.02em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-sm: 0.75rem
  margin: 1rem
  margin-lg: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.25rem
  space-xl: 1.75rem
  space-2xl: 2.5rem
---

## Brand & Style

The design system establishes a high-trust, responsive civic infrastructure interface tailored for urban municipal citizens across metropolitan India. It balances state-level authority with human warmth, transforming bureaucratic interactions into immediate, frictionless actions. 

The aesthetic is a hybrid of Modern Glassmorphism and Tactile Depth. Pure white glass surfaces layer gently over warm slate-tinted canvas backdrops with subtle 1px translucent rims and soft dual-axis ambient shadows. Primary actions feature an engineered, tactile presence with microscopic inner top bevel highlights that invite touch, while status indicators project crisp, high-visibility civic legitimacy. The interface feels clean, rapid, transparent, and effortlessly dependable under direct sunlight or low-bandwidth mobile conditions.

## Colors

The palette balances authoritative civic clarity with accessibility standards:

- **Primary (`#2563EB` & `#1D4ED8`)**: Urban Link Blue represents municipal reliability, administrative integrity, and functional navigation.
- **Secondary (`#10B981`)**: Civic Emerald signifies resolved tickets, validated citizen IDs, and positive confirmations.
- **Tertiary (`#F59E0B`)**: Civic Amber signals ongoing municipal work, pending evaluations, and moderate priority alerts.
- **Emergency Crimson (`#EF4444`)**: Reserved strictly for rapid emergency dispatches, direct SOS calls to 112, and dangerous civic hazards.
- **Neutral Foundation (`#0F172A`, `#64748B`, `#F8FAFC`)**: Canvas uses cool off-white `#F8FAFC` to reduce glare, punctuated by deep navy `#0F172A` for absolute text legibility and `#64748B` for secondary operational metadata.

## Typography

The typography uses Plus Jakarta Sans across headlines, interface labels, and readable body text, selected for its contemporary geometry, generous counters, and legibility at compact mobile scale. 

All ticket reference IDs, tracking codes (e.g., `NAG-2026-750001`), and real-time metric counters use Space Grotesk to provide technical precision and instant character disambiguation between numerals and Latin capitals. Section headings use small uppercase tracking (`letter-spacing: 0.06em`) with medium gray slate weights (`#64748B`) to anchor dense civic views.

## Layout & Spacing

The layout is built on an adaptive fluid mobile-first architecture engineered primarily for touchscreens, standardizing on a 4-pixel base rhythm.

- **Mobile Viewports (< 640px)**: A 4-column layout utilizing `margin: 1rem` (16px) with an inner `gutter: 0.75rem` (12px). Quick report buttons align into a two-column grid (`grid-cols-2`) for rapid thumb selection.
- **Tablet / Desktop Expandability (≥ 640px)**: Reflows smoothly to an 8-to-12 column container capped at a centered maximum width of 640px for single-handed civic utilities or 960px for analytical municipal dashboards.
- **Vertical Hierarchy**: Strict separation between contextual modules using `space-lg` (20px) to `space-xl` (28px). Bottom navigation and the floating voice input bar use absolute anchoring with safe-area padding compensation (`env(safe-area-inset-bottom) + 16px`).

## Elevation & Depth

Visual hierarchy is delivered via structured glassmorphism combined with soft ambient light fields:

- **Level 0 (Canvas Base)**: Matte, anti-glare flat fill `#F8FAFC`.
- **Level 1 (Cards & Department Modules)**: Pure white translucent surface (`rgba(255, 255, 255, 0.90)`), supported by `backdrop-filter: blur(12px)`, a delicate 1px border (`rgba(226, 232, 240, 0.80)`), and an ambient drop shadow: `0 4px 16px -2px rgba(15, 23, 42, 0.04), 0 2px 4px -1px rgba(15, 23, 42, 0.02)`.
- **Level 2 (Active Sheets, Floating Dock & Voice Bar)**: Elevated `rgba(255, 255, 255, 0.96)` with `backdrop-filter: blur(16px)` paired with a dual shadow system: `0 12px 32px -4px rgba(15, 23, 42, 0.08), 0 4px 8px -2px rgba(15, 23, 42, 0.03)`.
- **Tactile Component Depth**: Primary action buttons feature a 1px inner top highlight (`inset 0 1px 0 rgba(255, 255, 255, 0.25)`) and a bottom drop rim (`0 2px 4px rgba(37, 99, 235, 0.25)`), creating a responsive, physical sensation under finger presses.
- **Emergency Elevation**: Emergency panels use a soft crimson tint (`#FEF2F2`) framed with a high-contrast soft red ring (`#FCA5A5`) to ensure instant isolation from neutral administrative elements.

## Shapes

The system implements a friendly, approachable curvature profile with clear functional separation:

- **Primary Interactive Cards**: Standardized at `rounded-2xl` (16px to 20px) for departmental selection grids, ticket overview cards, and modal dialogs.
- **Pills & Status Indicators**: Full rounded pill geometries (`border-radius: 9999px`) for ticket status chips (`RESOLVED`, `IN PROGRESS`), emergency trigger triggers, and bottom floating action pills.
- **Inputs & Voice Bar**: Standard text fields and search inputs feature a rounded capsule form (24px to 28px) to soften the municipal data-entry burden.

## Components

### Buttons
- **Primary Civic Action**: `#2563EB` solid fill, transitioning to `#1D4ED8` on press. Subtle inner top highlight border, white text (`label-lg`), 12px vertical padding, 20px horizontal padding, 16px border-radius.
- **Emergency Button**: Full `#EF4444` crimson pill with high-contrast `#FFFFFF` bold label and integrated pulsing icon indicator.
- **Secondary / Ghost**: Pure white surface with 1px `#E2E8F0` border, deep navy `#0F172A` text, and slight hover wash of `#F1F5F9`.

### Status Chips & Pills
- Small, uppercase font (`label-sm`).
- **Resolved / Verified**: Background `#ECFDF5`, text `#059669`, subtle border `#A7F3D0`.
- **In-Progress / Medium Priority**: Background `#FFFBEB`, text `#D97706`, subtle border `#FDE68A`.
- **High Priority / Critical**: Background `#FEF2F2`, text `#DC2626`, subtle border `#FECACA`.

### Department / Category Cards
- White glass substrate (`rgba(255, 255, 255, 0.88)`), 16px padding, 20px radius.
- Includes a prominent 32px 3D/colored icon or civic insignia, bold title (`headline-sm`), and a 2-line slate caption (`body-sm`) describing jurisdiction.
- Active states feature a gentle scale reduction (`scale(0.98)`) and `#2563EB` border glow.

### Voice & Assistance Bar
- Floating capsule pinned above the mobile dock.
- Houses a text placeholder (`Describe your problem...`) in `#64748B`, with an adjacent floating circular microphone button (`#2563EB`).
- When recording, the mic pulses with an ambient ring: `0 0 0 8px rgba(37, 99, 235, 0.20)`.

### Ticket Lists & Activity Tiles
- Bordered glass card housing top status strip: Monospaced reference ID (`Space Grotesk`) at the left, aligned with colored status pill on the right.
- Descriptive snippet capped at two lines with ellipsis truncation, followed by timestamp and ward location in `#64748B`.

### Form Fields & Inputs
- Enclosed with soft `#F1F5F9` background or translucent white with 1.5px `#E2E8F0` outline.
- Smooth transition on focus: outline becomes `#2563EB` with an ambient glow of `0 0 0 3px rgba(37, 99, 235, 0.12)`.