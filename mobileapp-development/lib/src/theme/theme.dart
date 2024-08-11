import 'package:flutter/material.dart';
import 'package:seioffice/src/components/data.dart';

class SEITheme {
  static SEITheme of(BuildContext context) {
    return Data.of(context);
  }

  final SEITypography typography;
  final SEITypography monoTypography;
  final SEIColorScheme colorScheme;
  final Brightness brightness;

  const SEITheme({
    required this.typography,
    required this.monoTypography,
    required this.colorScheme,
    this.brightness = Brightness.light,
  });

  static SEITheme lerp(SEITheme a, SEITheme b, double t) {
    return SEITheme(
      typography: SEITypography.lerp(a.typography, b.typography, t),
      monoTypography: SEITypography.lerp(a.monoTypography, b.monoTypography, t),
      colorScheme: SEIColorScheme.lerp(a.colorScheme, b.colorScheme, t),
      brightness: t < 0.5 ? a.brightness : b.brightness,
    );
  }
}

Color _hex(int hex) {
  // append 0xFF to the hex value (as it's expected to be in ARGB format)
  return Color(hex | 0xFF000000);
}

class SEIColorScheme {
  factory SEIColorScheme.defaultColorScheme() {
    return SEIColorScheme(
      primary: _hex(0x50B33F),
      secondary: _hex(0xFFFFFF),
      tertiary: _hex(0x346C2B),
      primaryForeground: _hex(0xFFFFFF),
      secondaryForeground: _hex(0x000000),
      mutedForeground: _hex(0xFFFFFF),
      muted: _hex(0x929292),
      subtitleForeground: _hex(0x49494C),
      border: _hex(0x7B7B7B),
      surfaceForeground: _hex(0xFFFFFF),
      success: _hex(0x50B33F),
      successForeground: _hex(0xFFFFFF),
      warning: _hex(0xB3AE3F),
      warningForeground: _hex(0xFFFFFF),
      destructive: _hex(0xB33F3F),
      destructiveForeground: _hex(0xFFFFFF),
      surfaceNotification: _hex(0x50B33F),
      surfaceNotificationForeground: _hex(0xFFFFFF),
      surfaceNotificationDestructive: _hex(0xde4040),
      surfaceNotificationDestructiveForeground: _hex(0xFFFFFF),
      surfaceNotificationWarning: _hex(0xB3AE3F),
      surfaceNotificationWarningForeground: _hex(0xFFFFFF),
      surfaceUnknown: _hex(0xB3703F),
      surfaceUnknownForeground: _hex(0x3C1F0A),
      surfaceWarning: _hex(0xB3AE3F),
      surfaceWarningForeground: _hex(0x4C4D0C),
      surfaceSuccess: _hex(0x50B33F),
      surfaceSuccessForeground: _hex(0x204519),
      surfaceFine: _hex(0x3FB3A5),
      surfaceFineForeground: _hex(0x094C44),
      surfaceDanger: _hex(0xB33F3F),
      surfaceDangerForeground: _hex(0x510B0B),
      surfaceMuted: _hex(0xB1B1B1),
      surfaceMutedForeground: _hex(0x5B5B5B),
      swiperTrack: _hex(0xD9D9D9),
      swiperThumb: _hex(0x50B33F),
      swiperTrackBorder: _hex(0x7B7B7B),
      swiperThumbDisabled: _hex(0x7B7B7B),
      swiperTrackSelected: _hex(0x346C2B),
      swiperForeground: _hex(0xFFFFFF),
      summaryCardIconBackground: _hex(0xE7D535),
      shadow: const Color(0x33000000),
      shimmerBase: Color(0xFFE3E3E3),
      shimmerHighlight: Color(0xFFD9D9D9),
      card: Color(0xFFFFFFFF),
      background: Color(0xFFE3E3E3),
    );
  }

  factory SEIColorScheme.darkColorScheme() {
    // use Color(0xFF<color>) instead
    return const SEIColorScheme(
      primary: Color(0xFF50B33F),
      secondary: Color(0xFF1E1E1E),
      tertiary: Color(0xFF346C2B),
      primaryForeground: Color(0xFFFFFFFF),
      secondaryForeground: Color(0xFFFFFFFF),
      mutedForeground: Color(0xFFFFFFFF),
      muted: Color(0xFF525252),
      subtitleForeground: Color(0xFFCACAD3),
      border: Color(0xFF7B7B7B),
      surfaceForeground: Color(0xFFFFFFFF),
      success: Color(0xFF50B33F),
      successForeground: Color(0xFFFFFFFF),
      warning: Color(0xFFB3AE3F),
      warningForeground: Color(0xFFFFFFFF),
      destructive: Color(0xFFB33F3F),
      destructiveForeground: Color(0xFFFFFFFF),
      surfaceNotification: Color(0xFF50B33F),
      surfaceNotificationForeground: Color(0xFFFFFFFF),
      surfaceNotificationDestructive: Color(0xffde4040),
      surfaceNotificationDestructiveForeground: Color(0xFFFFFFFF),
      surfaceNotificationWarning: Color(0xFFB3AE3F),
      surfaceNotificationWarningForeground: Color(0xFFFFFFFF),
      surfaceUnknown: Color(0xFFB3703F),
      surfaceUnknownForeground: Color(0xFF3C1F0A),
      surfaceWarning: Color(0xFFB3AE3F),
      surfaceWarningForeground: Color(0xFF4C4D0C),
      surfaceSuccess: Color(0xFF50B33F),
      surfaceSuccessForeground: Color(0xFF204519),
      surfaceFine: Color(0xFF3FB3A5),
      surfaceFineForeground: Color(0xFF094C44),
      surfaceDanger: Color(0xFFB33F3F),
      surfaceDangerForeground: Color(0xFF510B0B),
      surfaceMuted: Color(0xFFB1B1B1),
      surfaceMutedForeground: Color(0xFF5B5B5B),
      swiperTrack: Color(0xFFD9D9D9),
      swiperThumb: Color(0xFF50B33F),
      swiperTrackBorder: Color(0xFF7B7B7B),
      swiperThumbDisabled: Color(0xFF7B7B7B),
      swiperTrackSelected: Color(0xFF346C2B),
      swiperForeground: Color(0xFFFFFFFF),
      summaryCardIconBackground: Color(0xFFE7D535),
      shadow: Color(0x33000000),
      shimmerBase: Color(0xFF1E1E1E),
      shimmerHighlight: Color(0xFF2E2E2E),
      card: Color(0xFF2E2E2E),
      background: Color(0xFF1E1E1E),
    );
  }

  const SEIColorScheme({
    required this.background,
    required this.card,
    required this.primary,
    required this.secondary,
    required this.tertiary,
    required this.primaryForeground,
    required this.secondaryForeground,
    required this.mutedForeground,
    required this.muted,
    required this.subtitleForeground,
    required this.border,
    required this.surfaceForeground,
    required this.success,
    required this.successForeground,
    required this.warning,
    required this.warningForeground,
    required this.destructive,
    required this.destructiveForeground,
    required this.surfaceNotification,
    required this.surfaceNotificationForeground,
    required this.surfaceNotificationDestructive,
    required this.surfaceNotificationDestructiveForeground,
    required this.surfaceNotificationWarning,
    required this.surfaceNotificationWarningForeground,
    required this.surfaceUnknown,
    required this.surfaceUnknownForeground,
    required this.surfaceWarning,
    required this.surfaceWarningForeground,
    required this.surfaceSuccess,
    required this.surfaceSuccessForeground,
    required this.surfaceFine,
    required this.surfaceFineForeground,
    required this.surfaceDanger,
    required this.surfaceDangerForeground,
    required this.surfaceMuted,
    required this.surfaceMutedForeground,
    required this.swiperTrack,
    required this.swiperThumb,
    required this.swiperThumbDisabled,
    required this.swiperTrackBorder,
    required this.swiperTrackSelected,
    required this.swiperForeground,
    required this.summaryCardIconBackground,
    required this.shadow,
    required this.shimmerBase,
    required this.shimmerHighlight,
  });

  final Color primary;
  final Color secondary;
  final Color tertiary;
  final Color card;
  final Color primaryForeground;
  final Color secondaryForeground;
  final Color mutedForeground;
  final Color muted;
  final Color subtitleForeground;
  final Color border;
  final Color shadow;
  final Color surfaceForeground;
  // untuk halaman check-in, check-out dsb
  final Color success;
  final Color successForeground;
  final Color warning;
  final Color warningForeground;
  final Color destructive;
  final Color destructiveForeground;
  // untuk halaman notifikasi
  final Color surfaceNotification;
  final Color surfaceNotificationForeground;
  final Color surfaceNotificationDestructive;
  final Color surfaceNotificationDestructiveForeground;
  final Color surfaceNotificationWarning;
  final Color surfaceNotificationWarningForeground;
  // untuk halaman riwayat absensi
  final Color surfaceUnknown;
  final Color surfaceUnknownForeground;
  final Color surfaceWarning;
  final Color surfaceWarningForeground;
  final Color surfaceSuccess;
  final Color surfaceSuccessForeground;
  final Color surfaceFine;
  final Color surfaceFineForeground;
  final Color surfaceDanger;
  final Color surfaceDangerForeground;
  final Color surfaceMuted;
  final Color surfaceMutedForeground;
  final Color swiperTrack;
  final Color swiperThumb;
  final Color swiperThumbDisabled;
  final Color swiperTrackBorder;
  final Color swiperTrackSelected;
  final Color swiperForeground;
  final Color summaryCardIconBackground;
  final Color shimmerBase;
  final Color shimmerHighlight;
  final Color background;
  // Surface Colors
  Color generateSurfaceColor(int seed) {
    double randomHue = (((seed) / 360) % 360);
    double saturation = 150 / 255;
    double randomLightness = 130 / 255;
    return HSLColor.fromAHSL(1, randomHue, saturation, randomLightness)
        .toColor();
  }

  Color operator [](dynamic anything) {
    int hashCode = anything == null ? 0 : anything.hashCode;
    return generateSurfaceColor(hashCode);
  }

  static SEIColorScheme lerp(SEIColorScheme a, SEIColorScheme b, double t) {
    return SEIColorScheme(
      primary: Color.lerp(a.primary, b.primary, t)!,
      secondary: Color.lerp(a.secondary, b.secondary, t)!,
      tertiary: Color.lerp(a.tertiary, b.tertiary, t)!,
      card: Color.lerp(a.card, b.card, t)!,
      primaryForeground:
          Color.lerp(a.primaryForeground, b.primaryForeground, t)!,
      secondaryForeground:
          Color.lerp(a.secondaryForeground, b.secondaryForeground, t)!,
      mutedForeground: Color.lerp(a.mutedForeground, b.mutedForeground, t)!,
      muted: Color.lerp(a.muted, b.muted, t)!,
      subtitleForeground:
          Color.lerp(a.subtitleForeground, b.subtitleForeground, t)!,
      border: Color.lerp(a.border, b.border, t)!,
      shadow: Color.lerp(a.shadow, b.shadow, t)!,
      surfaceForeground:
          Color.lerp(a.surfaceForeground, b.surfaceForeground, t)!,
      success: Color.lerp(a.success, b.success, t)!,
      successForeground:
          Color.lerp(a.successForeground, b.successForeground, t)!,
      warning: Color.lerp(a.warning, b.warning, t)!,
      warningForeground:
          Color.lerp(a.warningForeground, b.warningForeground, t)!,
      destructive: Color.lerp(a.destructive, b.destructive, t)!,
      destructiveForeground:
          Color.lerp(a.destructiveForeground, b.destructiveForeground, t)!,
      surfaceNotification:
          Color.lerp(a.surfaceNotification, b.surfaceNotification, t)!,
      surfaceNotificationForeground: Color.lerp(
          a.surfaceNotificationForeground, b.surfaceNotificationForeground, t)!,
      surfaceNotificationDestructive: Color.lerp(
          a.surfaceNotificationDestructive,
          b.surfaceNotificationDestructive,
          t)!,
      surfaceNotificationDestructiveForeground: Color.lerp(
          a.surfaceNotificationDestructiveForeground,
          b.surfaceNotificationDestructiveForeground,
          t)!,
      surfaceNotificationWarning: Color.lerp(
          a.surfaceNotificationWarning, b.surfaceNotificationWarning, t)!,
      surfaceNotificationWarningForeground: Color.lerp(
          a.surfaceNotificationWarningForeground,
          b.surfaceNotificationWarningForeground,
          t)!,
      surfaceUnknown: Color.lerp(a.surfaceUnknown, b.surfaceUnknown, t)!,
      surfaceUnknownForeground: Color.lerp(
          a.surfaceUnknownForeground, b.surfaceUnknownForeground, t)!,
      surfaceWarning: Color.lerp(a.surfaceWarning, b.surfaceWarning, t)!,
      surfaceWarningForeground: Color.lerp(
          a.surfaceWarningForeground, b.surfaceWarningForeground, t)!,
      surfaceSuccess: Color.lerp(a.surfaceSuccess, b.surfaceSuccess, t)!,
      surfaceSuccessForeground: Color.lerp(
          a.surfaceSuccessForeground, b.surfaceSuccessForeground, t)!,
      surfaceFine: Color.lerp(a.surfaceFine, b.surfaceFine, t)!,
      surfaceFineForeground:
          Color.lerp(a.surfaceFineForeground, b.surfaceFineForeground, t)!,
      surfaceDanger: Color.lerp(a.surfaceDanger, b.surfaceDanger, t)!,
      surfaceDangerForeground:
          Color.lerp(a.surfaceDangerForeground, b.surfaceDangerForeground, t)!,
      surfaceMuted: Color.lerp(a.surfaceMuted, b.surfaceMuted, t)!,
      surfaceMutedForeground:
          Color.lerp(a.surfaceMutedForeground, b.surfaceMutedForeground, t)!,
      swiperTrack: Color.lerp(a.swiperTrack, b.swiperTrack, t)!,
      swiperThumb: Color.lerp(a.swiperThumb, b.swiperThumb, t)!,
      swiperThumbDisabled:
          Color.lerp(a.swiperThumbDisabled, b.swiperThumbDisabled, t)!,
      swiperTrackBorder:
          Color.lerp(a.swiperTrackBorder, b.swiperTrackBorder, t)!,
      swiperTrackSelected:
          Color.lerp(a.swiperTrackSelected, b.swiperTrackSelected, t)!,
      swiperForeground: Color.lerp(a.swiperForeground, b.swiperForeground, t)!,
      summaryCardIconBackground: Color.lerp(
          a.summaryCardIconBackground, b.summaryCardIconBackground, t)!,
      shimmerBase: Color.lerp(a.shimmerBase, b.shimmerBase, t)!,
      shimmerHighlight: Color.lerp(a.shimmerHighlight, b.shimmerHighlight, t)!,
      background: Color.lerp(a.background, b.background, t)!,
    );
  }
}

class SEITypography {
  final TextStyle xs;
  final TextStyle sm;
  final TextStyle md;
  final TextStyle lg;
  final TextStyle xl;
  final TextStyle xxl;
  final TextStyle x3l;
  final TextStyle x4l;
  final TextStyle x5l;
  final TextStyle x6l;

  const SEITypography({
    required this.xs,
    required this.sm,
    required this.md,
    required this.lg,
    required this.xl,
    required this.xxl,
    required this.x3l,
    required this.x4l,
    required this.x5l,
    required this.x6l,
  });

  factory SEITypography.fromTextStyle(TextStyle textStyle) {
    return SEITypography(
      xs: textStyle.copyWith(fontSize: 12),
      sm: textStyle.copyWith(fontSize: 14),
      md: textStyle.copyWith(fontSize: 16),
      lg: textStyle.copyWith(fontSize: 18),
      xl: textStyle.copyWith(fontSize: 20),
      xxl: textStyle.copyWith(fontSize: 24),
      x3l: textStyle.copyWith(fontSize: 28),
      x4l: textStyle.copyWith(fontSize: 32),
      x5l: textStyle.copyWith(fontSize: 36),
      x6l: textStyle.copyWith(fontSize: 40),
    );
  }

  factory SEITypography.defaultTypography() {
    return SEITypography.fromTextStyle(const TextStyle(
      fontFamily: 'GeistSans',
      letterSpacing: -0.41,
    ));
  }

  factory SEITypography.monoTypography() {
    return SEITypography.fromTextStyle(const TextStyle(
      fontFamily: 'GeistMono',
      letterSpacing: -0.41,
    ));
  }

  static SEITypography lerp(SEITypography a, SEITypography b, double t) {
    return SEITypography(
      xs: TextStyle.lerp(a.xs, b.xs, t)!,
      sm: TextStyle.lerp(a.sm, b.sm, t)!,
      md: TextStyle.lerp(a.md, b.md, t)!,
      lg: TextStyle.lerp(a.lg, b.lg, t)!,
      xl: TextStyle.lerp(a.xl, b.xl, t)!,
      xxl: TextStyle.lerp(a.xxl, b.xxl, t)!,
      x3l: TextStyle.lerp(a.x3l, b.x3l, t)!,
      x4l: TextStyle.lerp(a.x4l, b.x4l, t)!,
      x5l: TextStyle.lerp(a.x5l, b.x5l, t)!,
      x6l: TextStyle.lerp(a.x6l, b.x6l, t)!,
    );
  }
}
