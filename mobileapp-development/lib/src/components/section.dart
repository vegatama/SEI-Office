import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

class ParagraphSection extends StatelessWidget {
  final Widget title;
  final Widget content;
  final Widget? description;
  final Alignment alignment;
  final double gap;

  const ParagraphSection({
    super.key,
    required this.title,
    required this.content,
    this.alignment = Alignment.centerLeft,
    this.gap = 0,
    this.description,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return IntrinsicWidth(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Align(
            alignment: alignment,
            child: DefaultTextStyle(
              style: theme.typography.sm.copyWith(
                color: theme.colorScheme.subtitleForeground,
                fontWeight: fontMedium,
              ),
              child: title,
            ),
          ),
          if (gap > 0) SizedBox(height: gap),
          Align(
            alignment: alignment,
            child: DefaultTextStyle(
              style: theme.typography.xl.copyWith(
                color: theme.colorScheme.secondaryForeground,
                fontWeight: fontSemiBold,
              ),
              child: content,
            ),
          ),
          if (description != null) ...[
            if (gap > 0) SizedBox(height: gap),
            Align(
              alignment: alignment,
              child: DefaultTextStyle(
                style: theme.typography.sm.copyWith(
                  color: theme.colorScheme.subtitleForeground,
                  fontWeight: fontMedium,
                ),
                child: description!,
              ),
            ),
          ],
        ],
      ),
    );
  }
}
