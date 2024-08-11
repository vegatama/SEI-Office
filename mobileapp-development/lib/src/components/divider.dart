import 'package:flutter/material.dart';
import 'package:seioffice/src/theme/theme.dart';

class ContentDivider extends StatelessWidget {
  final Widget child;

  const ContentDivider({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return DefaultTextStyle(
      style: theme.typography.md.copyWith(
        color: theme.colorScheme.muted,
      ),
      child: IntrinsicWidth(
          child: Row(
        children: [
          Expanded(
            child: Divider(
              color: theme.colorScheme.muted,
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            child: child,
          ),
          Expanded(
            child: Divider(
              color: theme.colorScheme.muted,
            ),
          ),
        ],
      )),
    );
  }
}
