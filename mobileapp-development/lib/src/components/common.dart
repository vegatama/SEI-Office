import 'package:flutter/material.dart';
import 'package:seioffice/src/components/dashed.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

class FieldGroup extends StatelessWidget {
  final Widget label;
  final Widget child;

  const FieldGroup({
    super.key,
    required this.label,
    required this.child,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        label,
        const SizedBox(height: 8),
        child,
      ],
    );
  }
}

class FieldLabel extends StatelessWidget {
  final String label;
  final bool important;

  const FieldLabel({
    super.key,
    required this.label,
    this.important = false,
  });

  @override
  Widget build(BuildContext context) {
    if (important) {
      return DefaultTextStyle.merge(
          style: SEITheme.of(context).typography.lg.copyWith(
                fontWeight: fontMedium,
                color: SEITheme.of(context).colorScheme.secondaryForeground,
              ),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Text(label),
              const Text(
                '*',
                style: TextStyle(
                  color: Colors.red,
                ),
              ),
            ],
          ));
    }
    return DefaultTextStyle.merge(
      style: SEITheme.of(context).typography.md.copyWith(
            fontWeight: fontMedium,
            color: SEITheme.of(context).colorScheme.secondaryForeground,
          ),
      child: Text(label),
    );
  }
}

class EmptyListText extends StatelessWidget {
  final String message;
  const EmptyListText({
    super.key,
    required this.message,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(48),
      child: Center(
        child: Text(
          message,
          style: SEITheme.of(context).typography.md.copyWith(
                color: SEITheme.of(context).colorScheme.subtitleForeground,
              ),
        ),
      ),
    );
  }
}

class LabeledIcon extends StatelessWidget {
  final Widget? trailing;
  final Widget icon;
  final Widget? leading;
  final IconThemeData? iconTheme;
  const LabeledIcon({
    super.key,
    this.trailing,
    required this.icon,
    this.leading,
    this.iconTheme,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Row(
      children: [
        if (leading != null) leading!,
        if (leading != null) const SizedBox(width: 8),
        IconTheme(
          data: IconThemeData(
            color: theme.colorScheme.primary,
          ).merge(iconTheme),
          child: icon,
        ),
        if (trailing != null) const SizedBox(width: 8),
        if (trailing != null) trailing!,
      ],
    );
  }
}

class Section extends StatelessWidget {
  final Widget title;
  final List<Widget> children;
  final double? gap;
  final WidgetBuilder? divider;

  const Section({
    super.key,
    required this.title,
    required this.children,
    this.gap,
    this.divider,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return IntrinsicWidth(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          DefaultTextStyle.merge(
            style: theme.typography.sm.copyWith(
              color: theme.colorScheme.subtitleForeground,
              fontWeight: fontMedium,
            ),
            child: title,
          ),
          SizedBox(height: gap ?? 8),
          // ...joinWidgets(
          //     children,
          //     Divider(
          //       color: theme.colorScheme.border,
          //     ))
          ...joinWidgets(
            children,
            divider != null
                ? divider!(context)
                : Divider(color: theme.colorScheme.border),
          ),
        ],
      ),
    );
  }
}

class DistanceVisualization extends StatelessWidget {
  final String distance;

  const DistanceVisualization({
    super.key,
    required this.distance,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return IntrinsicWidth(
      child: IconTheme.merge(
        data: IconThemeData(
          size: 32,
          color: theme.colorScheme.secondaryForeground,
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Row(
              children: [
                Icon(
                  Icons.accessibility,
                ),
                const SizedBox(width: 8),
                Expanded(
                    child: DashedLine(
                  color: theme.colorScheme.border,
                )),
                const SizedBox(width: 8),
                Icon(
                  Icons.apartment,
                ),
              ],
            ),
            const SizedBox(height: 8),
            DefaultTextStyle.merge(
              style: theme.typography.md.copyWith(
                fontWeight: fontSemiBold,
                color: theme.colorScheme.secondaryForeground,
              ),
              textAlign: TextAlign.center,
              child: Text(distance),
            ),
          ],
        ),
      ),
    );
  }
}

class AbstractChip extends StatelessWidget {
  final Widget child;
  final Color? backgroundColor;
  final Color? borderColor;
  final Color? textColor;

  const AbstractChip({
    super.key,
    required this.child,
    this.backgroundColor,
    this.borderColor,
    this.textColor,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Container(
      decoration: BoxDecoration(
        color: backgroundColor,
        border: Border.all(
          color: borderColor ?? Colors.transparent,
          width: 2,
        ),
        borderRadius: BorderRadius.circular(24),
      ),
      padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 24),
      child: DefaultTextStyle.merge(
        style: theme.typography.lg.copyWith(
          color: textColor,
          fontWeight: fontMedium,
        ),
        child: child,
      ),
    );
  }
}

class ToggledChip extends StatelessWidget {
  final Widget child;
  final bool selected;
  final ValueChanged<bool>? onChanged;

  const ToggledChip({
    super.key,
    required this.child,
    required this.selected,
    this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      onTap: () {
        onChanged?.call(!selected);
      },
      child: AbstractChip(
        backgroundColor: selected ? theme.colorScheme.primary : null,
        borderColor: selected ? null : theme.colorScheme.primary,
        textColor: selected
            ? theme.colorScheme.primaryForeground
            : theme.colorScheme.primary,
        child: child,
      ),
    );
  }
}

class ChipButtonGroup extends StatelessWidget {
  final List<Widget> children;

  const ChipButtonGroup({
    super.key,
    required this.children,
  });

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      clipBehavior: Clip.none,
      child: Row(
        children: joinWidgets(
          children,
          const SizedBox(
            width: 4,
          ),
        ).toList(),
      ),
    );
  }
}
