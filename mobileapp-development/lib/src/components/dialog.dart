import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:seioffice/src/theme/theme.dart';

import '../util.dart';

class PopupDialog extends StatelessWidget {
  final Widget? icon;
  final Widget? title;
  final Widget content;
  final List<Widget>? actions;

  const PopupDialog({
    Key? key,
    this.icon,
    this.title,
    required this.content,
    this.actions,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Center(
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 24),
        constraints: const BoxConstraints(minWidth: 300),
        decoration: BoxDecoration(
          color: theme.colorScheme.secondary,
          borderRadius: BorderRadius.circular(12),
          boxShadow: [
            BoxShadow(
              color: theme.colorScheme.shadow,
              blurRadius: 8,
              offset: const Offset(0, 4),
            ),
          ],
        ),
        child: IconTheme(
          data: IconThemeData(
            color: theme.colorScheme.secondaryForeground,
            size: 64,
          ),
          child: DefaultTextStyle(
            style: theme.typography.lg.copyWith(
              color: theme.colorScheme.secondaryForeground,
            ),
            child: IntrinsicWidth(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      if (icon != null) icon!,
                      if (icon != null) const SizedBox(width: 16),
                      Expanded(
                        child: IntrinsicWidth(
                          child: Column(
                            mainAxisSize: MainAxisSize.min,
                            crossAxisAlignment: CrossAxisAlignment.stretch,
                            children: [
                              if (title != null)
                                DefaultTextStyle.merge(
                                  style: theme.typography.xxl.copyWith(
                                    fontWeight: fontMedium,
                                  ),
                                  child: title!,
                                ),
                              if (title != null) const SizedBox(height: 8),
                              content,
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                  if (actions != null) const SizedBox(height: 24),
                  if (actions != null)
                    Wrap(
                      alignment: WrapAlignment.end,
                      spacing: 8,
                      runSpacing: 8,
                      children: actions!,
                    ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
