import 'package:flutter/material.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

class RippleButton extends StatelessWidget {
  final Widget child;
  final VoidCallback? onPressed;
  const RippleButton({
    super.key,
    required this.child,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    const double rippleOffset = 50;
    return GestureDetector(
        onTap: onPressed,
        child: Stack(
          clipBehavior: Clip.none,
          children: [
            Positioned(
              top: -rippleOffset,
              left: -rippleOffset,
              right: -rippleOffset,
              bottom: -rippleOffset,
              child: Center(
                child: RippleAnimation(
                  color: theme.colorScheme.primary,
                  opacity: 0.5,
                  minRadius: 64 + 26,
                  maxRadius: 64 + 150 + 26,
                  thickness: 20,
                  animateOpacity: true,
                  animateThickness: true,
                  duration: const Duration(seconds: 1),
                ),
              ),
            ),
            Positioned(
              top: -rippleOffset,
              left: -rippleOffset,
              right: -rippleOffset,
              bottom: -rippleOffset,
              child: Center(
                child: RippleAnimation(
                  animationOffset: 0.25,
                  color: theme.colorScheme.primary,
                  opacity: 0.5,
                  minRadius: 64 + 26,
                  maxRadius: 64 + 150 + 26,
                  thickness: 20,
                  animateOpacity: true,
                  animateThickness: true,
                  duration: const Duration(seconds: 1),
                ),
              ),
            ),
            RepeatedAnimationBuilder.noChild(
                duration: const Duration(seconds: 1),
                curve: Curves.easeInOut,
                builder: (context, value) {
                  // make ping pong
                  if (value > 0.5) {
                    value = 1 - value;
                  }
                  return Transform.scale(
                    scale: 1 + value * 0.1,
                    child: Container(
                      decoration: BoxDecoration(
                        color: onPressed != null
                            ? theme.colorScheme.primary
                            : theme.colorScheme.muted,
                        shape: BoxShape.circle,
                      ),
                      padding: const EdgeInsets.all(26),
                      child: IconTheme(
                        data: IconThemeData(
                          size: 64,
                          color: onPressed != null
                              ? theme.colorScheme.primaryForeground
                              : theme.colorScheme.mutedForeground,
                        ),
                        child: child,
                      ),
                    ),
                  );
                }),
          ],
        ));
  }
}

class PrimaryButton extends StatelessWidget {
  final Widget? leading;
  final Widget? trailing;
  final Widget child;
  final VoidCallback? onPressed;
  final Color? color;
  const PrimaryButton({
    super.key,
    this.leading,
    this.trailing,
    required this.child,
    this.onPressed,
    this.color,
  });
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Button(
      leading: leading,
      trailing: trailing,
      onPressed: onPressed,
      decoration: BoxDecoration(
        color: onPressed != null
            ? color ?? theme.colorScheme.primary
            : theme.colorScheme.muted,
        borderRadius: BorderRadius.circular(50),
      ),
      foreground: theme.colorScheme.primaryForeground,
      child: child,
    );
  }
}

class SecondaryButton extends StatelessWidget {
  final Widget? leading;
  final Widget? trailing;
  final Widget child;
  final VoidCallback? onPressed;
  const SecondaryButton({
    super.key,
    this.leading,
    this.trailing,
    required this.child,
    this.onPressed,
  });
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Button(
      leading: leading,
      trailing: trailing,
      onPressed: onPressed,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(50),
        border: Border.all(
          color: onPressed != null
              ? theme.colorScheme.primary
              : theme.colorScheme.muted,
          width: 2,
        ),
      ),
      foreground: onPressed != null
          ? theme.colorScheme.primary
          : theme.colorScheme.muted,
      child: child,
    );
  }
}

class TertiaryButton extends StatelessWidget {
  final Widget? leading;
  final Widget? trailing;
  final Widget child;
  final VoidCallback? onPressed;
  final Color? color;
  const TertiaryButton({
    super.key,
    this.leading,
    this.trailing,
    required this.child,
    this.onPressed,
    this.color,
  });
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Button(
      leading: leading,
      trailing: trailing,
      onPressed: onPressed,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(50),
        border: Border.all(
          color: onPressed != null
              ? color ?? theme.colorScheme.primaryForeground
              : theme.colorScheme.muted,
          width: 2,
        ),
      ),
      foreground: onPressed != null
          ? color ?? theme.colorScheme.primaryForeground
          : theme.colorScheme.muted,
      child: child,
    );
  }
}

class GhostButton extends StatelessWidget {
  final Widget? leading;
  final Widget? trailing;
  final Widget child;
  final VoidCallback? onPressed;
  const GhostButton({
    super.key,
    this.leading,
    this.trailing,
    required this.child,
    this.onPressed,
  });
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Button(
      leading: leading,
      trailing: trailing,
      onPressed: onPressed,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(50),
      ),
      foreground: onPressed != null
          ? theme.colorScheme.primary
          : theme.colorScheme.muted,
      child: child,
    );
  }
}

class Button extends StatefulWidget {
  final Widget? leading;
  final Widget? trailing;
  final Widget child;
  final VoidCallback? onPressed;
  final Decoration decoration;
  final Color foreground;
  final EdgeInsets? padding;
  final TextStyle? textStyle;
  const Button({
    super.key,
    this.leading,
    this.trailing,
    required this.child,
    this.onPressed,
    required this.decoration,
    required this.foreground,
    this.textStyle,
    this.padding,
  });

  @override
  State<Button> createState() => _ButtonState();
}

class _ButtonState extends State<Button> {
  bool pointerDown = false;
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
        // onTap: widget.onPressed,
        onTap: () {
          if (widget.onPressed != null) {
            Feedback.forTap(context);
            widget.onPressed!();
          }
        },
        onTapDown: (_) {
          setState(() {
            pointerDown = true;
          });
        },
        onTapUp: (_) {
          setState(() {
            pointerDown = false;
          });
        },
        onTapCancel: () {
          setState(() {
            pointerDown = false;
          });
        },
        child: FocusableActionDetector(
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 100),
            // padding: const EdgeInsets.symmetric(
            //   vertical: 16,
            //   horizontal: 24,
            // ),
            constraints: const BoxConstraints(minWidth: 100),
            padding: widget.padding ??
                const EdgeInsets.symmetric(vertical: 16, horizontal: 24),
            decoration: widget.decoration,
            child: AnimatedScale(
              scale: pointerDown ? 0.95 : 1,
              duration: const Duration(milliseconds: 50),
              curve: Curves.easeOut,
              child: IconTheme.merge(
                data: IconThemeData(
                  color: widget.foreground,
                ),
                child: DefaultTextStyle.merge(
                  textAlign: TextAlign.center,
                  style: widget.textStyle ??
                      theme.typography.lg.copyWith(
                        fontWeight: fontMedium,
                        color: widget.foreground,
                      ),
                  child: IntrinsicWidth(
                    child: Row(
                      children: [
                        if (widget.leading != null) widget.leading!,
                        if (widget.leading != null) const SizedBox(width: 12),
                        Expanded(
                          child: widget.child,
                        ),
                        if (widget.trailing != null) const SizedBox(width: 12),
                        if (widget.trailing != null) widget.trailing!,
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ),
        ));
  }
}

class PrimaryIconButton extends StatefulWidget {
  final Widget icon;
  final Widget label;
  final VoidCallback? onPressed;
  final Color color;
  const PrimaryIconButton({
    super.key,
    required this.icon,
    required this.label,
    required this.color,
    this.onPressed,
  });

  @override
  State<PrimaryIconButton> createState() => _PrimaryIconButtonState();
}

class _PrimaryIconButtonState extends State<PrimaryIconButton> {
  bool pointerDown = false;
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      // onTap: widget.onPressed,
      onTap: () {
        if (widget.onPressed != null) {
          Feedback.forTap(context);
          widget.onPressed!();
        }
      },
      onTapDown: (_) {
        setState(() {
          pointerDown = true;
        });
      },
      onTapUp: (_) {
        setState(() {
          pointerDown = false;
        });
      },
      onTapCancel: () {
        setState(() {
          pointerDown = false;
        });
      },
      child: Container(
        constraints: const BoxConstraints(
          minWidth: 100,
          minHeight: 100,
        ),
        padding: const EdgeInsets.all(12),
        decoration: BoxDecoration(
          color:
              widget.onPressed != null ? widget.color : theme.colorScheme.muted,
          borderRadius: BorderRadius.circular(12),
        ),
        child: AnimatedScale(
          scale: pointerDown ? 0.85 : 1,
          duration: const Duration(milliseconds: 50),
          curve: Curves.easeOut,
          child: DefaultTextStyle.merge(
            style: theme.typography.md.copyWith(
              color: theme.colorScheme.primaryForeground,
            ),
            child: IconTheme.merge(
              data: IconThemeData(
                color: theme.colorScheme.primaryForeground,
                size: 42,
              ),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  widget.icon,
                  const SizedBox(height: 4),
                  widget.label,
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class CardButton extends StatelessWidget {
  final Widget? trailing;
  final Widget? leading;
  final Widget title;
  final Widget subtitle;
  final VoidCallback? onPressed;
  final Color color;

  const CardButton({
    super.key,
    this.trailing,
    this.leading,
    required this.title,
    required this.subtitle,
    required this.color,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      onTap: onPressed,
      child: Container(
        padding: const EdgeInsets.all(24),
        decoration: BoxDecoration(
          color: onPressed != null ? color : theme.colorScheme.muted,
          borderRadius: BorderRadius.circular(12),
        ),
        child: IconTheme.merge(
          data: IconThemeData(
            color: theme.colorScheme.primaryForeground,
            size: 42,
          ),
          child: IntrinsicWidth(
            child: Row(
              children: [
                if (leading != null) leading!,
                if (leading != null) const SizedBox(width: 18),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      DefaultTextStyle.merge(
                        style: theme.typography.xl.copyWith(
                          fontWeight: fontSemiBold,
                          color: theme.colorScheme.primaryForeground,
                        ),
                        child: title,
                      ),
                      DefaultTextStyle.merge(
                        style: theme.typography.sm.copyWith(
                          fontWeight: fontMedium,
                          color: theme.colorScheme.primaryForeground,
                        ),
                        child: subtitle,
                      ),
                    ],
                  ),
                ),
                if (trailing != null) const SizedBox(width: 18),
                if (trailing != null) trailing!,
              ],
            ),
          ),
        ),
      ),
    );
  }
}

class SectionButton extends StatelessWidget {
  final Widget? icon;
  final Widget child;
  final VoidCallback? onPressed;

  const SectionButton({
    super.key,
    this.icon,
    required this.child,
    this.onPressed,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onTap: onPressed,
      child: IntrinsicWidth(
        child: DefaultTextStyle.merge(
          style: theme.typography.md.copyWith(
            color: onPressed != null
                ? theme.colorScheme.secondaryForeground
                : theme.colorScheme.muted,
            fontWeight: fontMedium,
          ),
          child: IconTheme(
            data: IconThemeData(
              color: onPressed != null
                  ? theme.colorScheme.subtitleForeground
                  : theme.colorScheme.muted,
              size: 24,
            ),
            child: Row(
              children: [
                if (icon != null) icon!,
                if (icon != null) const SizedBox(width: 12),
                Expanded(
                  child: child,
                ),
                const SizedBox(width: 12),
                IconButton(
                  icon: const Icon(
                    Icons.chevron_right,
                  ),
                  onPressed: onPressed,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

/// ChipButton, same as PrimaryButton, but with less padding
class ChipButton extends StatelessWidget {
  final Widget? leading;
  final Widget? trailing;
  final Widget child;
  final VoidCallback? onPressed;
  const ChipButton({
    super.key,
    this.leading,
    this.trailing,
    required this.child,
    this.onPressed,
  });
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Button(
      leading: leading,
      trailing: trailing,
      onPressed: onPressed,
      padding: const EdgeInsets.symmetric(vertical: 9, horizontal: 24),
      decoration: BoxDecoration(
        color: onPressed != null
            ? theme.colorScheme.primary
            : theme.colorScheme.muted,
        borderRadius: BorderRadius.circular(50),
      ),
      foreground: onPressed != null
          ? theme.colorScheme.primaryForeground
          : theme.colorScheme.mutedForeground,
      textStyle: theme.typography.md.copyWith(
        fontWeight: fontMedium,
        color: onPressed != null
            ? theme.colorScheme.primaryForeground
            : theme.colorScheme.mutedForeground,
      ),
      child: child,
    );
  }
}
