import 'package:flutter/material.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

class Header extends StatelessWidget {
  final Widget? leading;
  final Widget title;
  final Widget? subtitle;
  final List<Widget>? actions;
  final bool small;

  const Header({
    super.key,
    this.leading,
    required this.title,
    this.subtitle,
    this.actions,
    this.small = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return IconTheme.merge(
      data: IconThemeData(
        color: theme.colorScheme.secondaryForeground,
        size: small ? 20 : 24,
      ),
      child: IntrinsicWidth(
        child: Row(
          children: [
            if (leading != null) leading!,
            if (leading != null) const SizedBox(width: 12),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                  if (subtitle != null)
                    DefaultTextStyle.merge(
                      style: theme.typography.sm.copyWith(
                        color: theme.colorScheme.subtitleForeground,
                        fontWeight: fontMedium,
                      ),
                      child: subtitle!,
                    ),
                  DefaultTextStyle.merge(
                    style: (small ? theme.typography.md : theme.typography.xl)
                        .copyWith(
                      fontWeight: fontMedium,
                      color: theme.colorScheme.secondaryForeground,
                    ),
                    child: title,
                  ),
                ],
              ),
            ),
            if (actions != null) const SizedBox(width: 12),
            if (actions != null) ...actions!,
          ],
        ),
      ),
    );
  }
}
