import 'package:flutter/material.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:shimmer/shimmer.dart';

class ContentPlaceholder extends StatelessWidget {
  final bool showContent;
  final Widget child;
  final bool useBox;

  const ContentPlaceholder({
    super.key,
    this.showContent = false,
    required this.child,
    this.useBox = true,
  });

  @override
  Widget build(BuildContext context) {
    if (showContent) {
      return child;
    }
    final theme = SEITheme.of(context);
    if (!useBox) {
      return Shimmer.fromColors(
        baseColor: theme.colorScheme.shimmerBase.withOpacity(0.25),
        highlightColor: theme.colorScheme.shimmerHighlight.withOpacity(0.5),
        child: child,
      );
    }
    return Shimmer.fromColors(
      baseColor: theme.colorScheme.shimmerBase.withOpacity(0.25),
      highlightColor: theme.colorScheme.shimmerHighlight.withOpacity(0.5),
      child: Stack(
        children: [
          Positioned(
            top: 0,
            left: 0,
            right: 2,
            bottom: 2,
            child: Container(
              decoration: BoxDecoration(
                color: theme.colorScheme.shimmerBase,
                borderRadius: BorderRadius.circular(12),
              ),
            ),
          ),
          Visibility(
            visible: false,
            maintainState: true,
            maintainAnimation: true,
            maintainSize: true,
            child: child,
          ),
        ],
      ),
    );
  }
}
