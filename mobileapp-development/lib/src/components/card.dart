import 'package:flutter/material.dart';
import 'package:seioffice/src/components/data.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

class ActionIcon extends StatelessWidget {
  final IconData icon;
  final Color? color;
  final EdgeInsets? padding;

  const ActionIcon({
    super.key,
    required this.icon,
    this.color,
    this.padding,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final surface = Data.of<SurfaceData>(context);
    return Container(
      decoration: BoxDecoration(
        color: theme.colorScheme.secondary,
        borderRadius: BorderRadius.circular(6),
      ),
      padding: padding ?? const EdgeInsets.all(6),
      child: Icon(
        icon,
        color: color ?? surface.color,
        size: 32,
      ),
    );
  }
}

class ActionCard extends StatelessWidget {
  final Widget? leading;
  final Widget child;
  final Widget? description;
  final Widget? title;
  final Widget? trailing;
  final VoidCallback? onPressed;
  final Color? backgroundColor;

  const ActionCard({
    super.key,
    this.leading,
    required this.child,
    this.description,
    this.trailing,
    this.onPressed,
    this.title,
    this.backgroundColor,
  });

  @override
  Widget build(BuildContext context) {
    // use muted color if onPressed is null
    final theme = SEITheme.of(context);
    return GestureDetector(
      onTap: () {
        if (onPressed != null) {
          onPressed!();
          Feedback.forTap(context);
        }
      },
      child: Data(
        data: SurfaceData(
          color: onPressed == null
              ? theme.colorScheme.muted
              : backgroundColor ?? theme.colorScheme.primary,
          foreground: theme.colorScheme.primaryForeground,
        ),
        child: Container(
          decoration: BoxDecoration(
            color: onPressed == null
                ? theme.colorScheme.muted
                : backgroundColor ?? theme.colorScheme.primary,
            borderRadius: BorderRadius.circular(12),
          ),
          clipBehavior: Clip.antiAlias,
          padding: EdgeInsets.only(
              left: leading == null ? 18 : 12, right: 12, top: 12, bottom: 12),
          child: Row(
            children: [
              if (leading != null)
                IconTheme.merge(
                    data: const IconThemeData(size: 24), child: leading!),
              if (leading != null) const SizedBox(width: 12),
              Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    if (title != null)
                      DefaultTextStyle(
                        style: theme.typography.sm.copyWith(
                          color: theme.colorScheme.primaryForeground,
                        ),
                        child: title!,
                      ),
                    DefaultTextStyle(
                      style: theme.typography.md.copyWith(
                        fontWeight: fontSemiBold,
                        color: theme.colorScheme.primaryForeground,
                      ),
                      child: child,
                    ),
                    if (description != null)
                      DefaultTextStyle(
                        style: theme.typography.xs.copyWith(
                          color: theme.colorScheme.primaryForeground,
                        ),
                        child: description!,
                      ),
                  ],
                ),
              ),
              if (trailing != null) const SizedBox(width: 12),
              if (trailing != null)
                IconTheme.merge(
                  data: IconThemeData(
                    color: theme.colorScheme.primaryForeground,
                    size: 24,
                  ),
                  child: trailing!,
                ),
            ],
          ),
        ),
      ),
    );
  }
}

class ProgressiveCard extends StatelessWidget {
  final Widget? title;
  final Widget child;
  final Widget? description;
  final Widget? trailing;
  final Color? inactiveColor;
  final Color? activeColor;
  final double progress;

  const ProgressiveCard({
    super.key,
    this.title,
    required this.child,
    this.description,
    this.trailing,
    this.inactiveColor,
    this.activeColor,
    required this.progress,
  });

  @override
  Widget build(BuildContext context) {
    var theme = SEITheme.of(context);
    return Container(
      decoration: BoxDecoration(
        color: inactiveColor ?? theme.colorScheme.tertiary,
        borderRadius: BorderRadius.circular(12),
      ),
      clipBehavior: Clip.antiAlias,
      child: Stack(
        children: [
          Positioned.fill(
            child: FractionallySizedBox(
              alignment: Alignment.centerLeft,
              widthFactor: progress,
              heightFactor: 1,
              child: Container(
                color: activeColor ?? theme.colorScheme.primary,
              ),
            ),
          ),
          Container(
            padding: const EdgeInsets.all(24),
            child: Row(
              children: [
                Expanded(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      if (title != null)
                        DefaultTextStyle(
                          style: theme.typography.xl.copyWith(
                            fontWeight: fontSemiBold,
                            color: theme.colorScheme.primaryForeground,
                          ),
                          child: title!,
                        ),
                      DefaultTextStyle(
                        style: theme.typography.x6l.copyWith(
                          fontWeight: fontBold,
                          color: theme.colorScheme.primaryForeground,
                        ),
                        child: child,
                      ),
                      if (description != null)
                        DefaultTextStyle(
                          style: theme.typography.xl.copyWith(
                            color: theme.colorScheme.primaryForeground,
                          ),
                          child: description!,
                        ),
                    ],
                  ),
                ),
                if (trailing != null) const SizedBox(width: 24),
                if (trailing != null)
                  IconTheme.merge(
                    data: IconThemeData(
                      color: theme.colorScheme.primaryForeground,
                      size: 48,
                    ),
                    child: trailing!,
                  ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class StatsCard extends StatelessWidget {
  final Widget? title;
  final Widget child;
  final Widget? description;
  final Widget? icon;
  final Color? backgroundColor;

  const StatsCard({
    super.key,
    this.title,
    required this.child,
    this.description,
    this.icon,
    this.backgroundColor,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    double iconSize = 100;
    return IntrinsicHeight(
      child: Container(
        clipBehavior: Clip.antiAlias,
        decoration: BoxDecoration(
          color: backgroundColor ?? theme.colorScheme.primary,
          borderRadius: BorderRadius.circular(12),
        ),
        padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 18),
        child: Stack(
          clipBehavior: Clip.none,
          children: [
            Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                if (title != null)
                  DefaultTextStyle(
                    style: theme.typography.md.copyWith(
                      fontWeight: fontSemiBold,
                      color: theme.colorScheme.primaryForeground,
                    ),
                    child: title!,
                  ),
                Expanded(
                  child: Align(
                    alignment: Alignment.bottomLeft,
                    child: DefaultTextStyle(
                      style: theme.typography.x6l.copyWith(
                        fontWeight: fontBold,
                        color: theme.colorScheme.primaryForeground,
                      ),
                      child: child,
                    ),
                  ),
                ),
                if (description != null)
                  DefaultTextStyle(
                    style: theme.typography.xl.copyWith(
                      color: theme.colorScheme.primaryForeground,
                    ),
                    child: description!,
                  ),
              ],
            ),
            if (icon != null)
              Positioned(
                bottom: -(iconSize / 3),
                right: -(iconSize / 3),
                child: IconTheme.merge(
                  data: IconThemeData(
                    color: theme.colorScheme.primaryForeground.withOpacity(0.5),
                    size: iconSize,
                  ),
                  child: icon!,
                ),
              ),
          ],
        ),
      ),
    );
  }
}

class AlertCard extends StatelessWidget {
  final Widget? title;
  final Widget content;
  final Widget? details;
  final Widget icon;
  final Color color;
  final Color foregroundColor;

  const AlertCard({
    super.key,
    this.title,
    required this.content,
    this.details,
    required this.icon,
    required this.color,
    required this.foregroundColor,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Container(
      decoration: BoxDecoration(
        color: color,
        borderRadius: BorderRadius.circular(12),
      ),
      clipBehavior: Clip.antiAlias,
      child: Stack(
        children: [
          Positioned(
            bottom: -86 / 4,
            right: -86 / 4,
            child: IconTheme(
              data: IconThemeData(
                color: theme.colorScheme.primaryForeground.withOpacity(0.3),
                size: 86,
              ),
              child: icon,
            ),
          ),
          Container(
              padding: EdgeInsets.symmetric(vertical: 18, horizontal: 24),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  if (title != null)
                    DefaultTextStyle(
                      style: theme.typography.sm.copyWith(
                        fontWeight: fontMedium,
                        color: theme.colorScheme.primaryForeground,
                      ),
                      child: title!,
                    ),
                  DefaultTextStyle(
                    style: theme.typography.xl.copyWith(
                      fontWeight: fontBold,
                      color: foregroundColor,
                    ),
                    child: content,
                  ),
                  if (details != null)
                    DefaultTextStyle(
                      style: theme.typography.xs.copyWith(
                        fontWeight: fontMedium,
                        color: theme.colorScheme.primaryForeground,
                      ),
                      child: details!,
                    ),
                ],
              ))
        ],
      ),
    );
  }
}

class NotificationCard extends StatelessWidget {
  final Widget? icon;
  final Widget child;
  final Widget? subtitle;
  final Widget? trailing;
  final Color? backgroundColor;
  final bool dropShadow;
  final Color? foregroundColor;

  const NotificationCard({
    super.key,
    this.icon,
    required this.child,
    this.subtitle,
    this.trailing,
    this.backgroundColor,
    this.foregroundColor,
    this.dropShadow = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Container(
      decoration: BoxDecoration(
        color: backgroundColor ?? theme.colorScheme.primary,
        borderRadius: BorderRadius.circular(12),
        boxShadow: dropShadow
            ? [
                BoxShadow(
                  color: theme.colorScheme.shadow,
                  blurRadius: 12,
                  offset: const Offset(0, 4),
                ),
              ]
            : null,
      ),
      padding: EdgeInsets.symmetric(vertical: 12, horizontal: 18),
      child: IntrinsicWidth(
        child: IconTheme.merge(
          data: IconThemeData(
            color: foregroundColor ?? theme.colorScheme.primaryForeground,
          ),
          child: Row(
            children: [
              if (icon != null)
                IconTheme.merge(
                    data: IconThemeData(
                      size: 42,
                      color: foregroundColor ??
                          theme.colorScheme.primaryForeground,
                    ),
                    child: icon!),
              if (icon != null) const SizedBox(width: 18),
              Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    DefaultTextStyle.merge(
                      style: theme.typography.md.copyWith(
                        fontWeight: fontSemiBold,
                        color: foregroundColor ??
                            theme.colorScheme.primaryForeground,
                      ),
                      child: child,
                    ),
                    if (subtitle != null)
                      DefaultTextStyle.merge(
                        style: theme.typography.sm.copyWith(
                          color: foregroundColor ??
                              theme.colorScheme.primaryForeground,
                        ),
                        child: subtitle!,
                      ),
                  ],
                ),
              ),
              if (trailing != null) const SizedBox(width: 18),
              if (trailing != null)
                IconTheme.merge(
                    data: IconThemeData(
                        size: 32,
                        color: foregroundColor ??
                            theme.colorScheme.primaryForeground),
                    child: trailing!),
            ],
          ),
        ),
      ),
    );
  }
}

class SummaryCard extends StatelessWidget {
  final Widget icon;
  final Widget title;
  final Widget content;
  const SummaryCard({
    super.key,
    required this.icon,
    required this.title,
    required this.content,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: theme.colorScheme.muted,
          width: 1,
        ),
      ),
      padding: const EdgeInsets.all(8).copyWith(right: 24),
      child: IntrinsicWidth(
        child: IntrinsicHeight(
          child: Row(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              AspectRatio(
                aspectRatio: 1,
                child: Container(
                  alignment: Alignment.center,
                  decoration: BoxDecoration(
                    color: theme.colorScheme.summaryCardIconBackground,
                    borderRadius: BorderRadius.circular(8),
                  ),
                  padding: const EdgeInsets.all(12),
                  child: IconTheme(
                    data: IconThemeData(
                      color: theme.colorScheme.primaryForeground,
                      size: 45,
                    ),
                    child: icon,
                  ),
                ),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    DefaultTextStyle.merge(
                      style: theme.typography.lg.copyWith(
                        fontWeight: fontBold,
                        color: theme.colorScheme.secondaryForeground,
                      ),
                      child: title,
                    ),
                    const SizedBox(height: 2),
                    DefaultTextStyle.merge(
                      style: theme.typography.md.copyWith(
                        color: theme.colorScheme.secondaryForeground,
                      ),
                      child: content,
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class CardGroupItem extends StatelessWidget {
  final Widget title;
  final Widget child;
  final bool selected;

  const CardGroupItem({
    super.key,
    required this.title,
    required this.child,
    this.selected = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Container(
      decoration: BoxDecoration(
        color:
            selected ? theme.colorScheme.primary : theme.colorScheme.secondary,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: selected ? theme.colorScheme.primary : theme.colorScheme.muted,
          width: 2,
        ),
      ),
      padding: const EdgeInsets.all(24),
      child: IntrinsicWidth(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            DefaultTextStyle(
              style: theme.typography.xl.copyWith(
                fontWeight: fontBold,
                color: selected
                    ? theme.colorScheme.primaryForeground
                    : theme.colorScheme.muted,
              ),
              child: title,
            ),
            const SizedBox(height: 8),
            DefaultTextStyle(
              style: theme.typography.xl.copyWith(
                color: selected
                    ? theme.colorScheme.primaryForeground
                    : theme.colorScheme.muted,
              ),
              child: child,
            ),
          ],
        ),
      ),
    );
  }
}

class CardGroupSelection<T> extends StatefulWidget {
  final Widget Function(BuildContext context, T item, bool selected) builder;
  final List<T> items;
  final Set<T> selected;
  final ValueChanged<Set<T>> onChanged;
  final double gap;

  const CardGroupSelection({
    required this.builder,
    required this.items,
    required this.selected,
    required this.onChanged,
    this.gap = 8,
  });

  factory CardGroupSelection.single({
    required Widget Function(BuildContext context, T item, bool selected)
        builder,
    required List<T> items,
    required T? selected,
    required ValueChanged<T?> onChanged,
    double gap = 8,
  }) {
    return CardGroupSelection(
      builder: builder,
      items: items,
      selected: selected == null ? {} : {selected},
      onChanged: (selected) {
        if (selected.isNotEmpty) {
          onChanged(selected.last);
        } else {
          onChanged(null);
        }
      },
      gap: gap,
    );
  }

  @override
  State<CardGroupSelection<T>> createState() => _CardGroupSelectionState<T>();
}

class _CardGroupSelectionState<T> extends State<CardGroupSelection<T>> {
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      mainAxisSize: MainAxisSize.min,
      children: [
        for (var i = 0; i < widget.items.length; i++)
          GestureDetector(
            onTap: () {
              final item = widget.items[i];
              if (widget.selected.contains(item)) {
                widget.onChanged(Set.from(widget.selected)..remove(item));
              } else {
                widget.onChanged(Set.from(widget.selected)..add(item));
              }
              Feedback.forTap(context);
            },
            child: Container(
              margin: EdgeInsets.only(top: i == 0 ? 0 : widget.gap),
              child: widget.builder(
                context,
                widget.items[i],
                widget.selected.contains(widget.items[i]),
              ),
            ),
          ),
      ],
    );
  }
}

class EmployeeCard extends StatelessWidget {
  final Widget avatar;
  final Widget name;
  final Widget jobTitle;
  final bool selected;

  const EmployeeCard({
    super.key,
    required this.avatar,
    required this.name,
    required this.jobTitle,
    this.selected = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    // [      ] [Job Title (small)]
    // [Avatar] [Name (large)]
    // [      ]
    return Container(
      decoration: BoxDecoration(
        color:
            selected ? theme.colorScheme.primary : theme.colorScheme.secondary,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(
          color: selected ? theme.colorScheme.primary : theme.colorScheme.muted,
          width: 2,
        ),
      ),
      padding: const EdgeInsets.all(16),
      child: IntrinsicWidth(
        child: IntrinsicHeight(
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              AspectRatio(
                aspectRatio: 1,
                child: avatar,
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    DefaultTextStyle(
                      style: theme.typography.md.copyWith(
                        color: selected
                            ? theme.colorScheme.primaryForeground
                            : theme.colorScheme.muted,
                      ),
                      child: jobTitle,
                    ),
                    DefaultTextStyle(
                      style: theme.typography.xl.copyWith(
                        fontWeight: fontSemiBold,
                        color: selected
                            ? theme.colorScheme.primaryForeground
                            : theme.colorScheme.muted,
                      ),
                      child: name,
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
