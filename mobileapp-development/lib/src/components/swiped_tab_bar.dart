import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/theme/theme.dart';

class SwipedTabBar extends StatefulWidget {
  final int selectedIndex;
  final int totalTabs;
  final ValueChanged<int> onTabChanged;

  const SwipedTabBar({
    Key? key,
    required this.selectedIndex,
    required this.totalTabs,
    required this.onTabChanged,
  }) : super(key: key);

  @override
  State<SwipedTabBar> createState() => _SwipedTabBarState();
}

class _SwipedTabBarState extends State<SwipedTabBar> {
  final TimelineTween _timelineTween = TimelineTween([
    Timeline(
      duration: Duration(milliseconds: 300),
      curve: Curves.easeInCubic,
    ),
    Timeline(
      duration: Duration(milliseconds: 700),
      curve: Curves.bounceOut,
      startValue: 1,
      endValue: 0,
    ),
  ]);
  bool swiped = false;

  @override
  void didUpdateWidget(covariant SwipedTabBar oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.selectedIndex != widget.selectedIndex) {
      swiped = true;
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      onHorizontalDragEnd: (details) {
        if (details.primaryVelocity! > 0) {
          if (widget.selectedIndex > 0) {
            widget.onTabChanged(widget.selectedIndex - 1);
          }
        } else {
          if (widget.selectedIndex < widget.totalTabs - 1) {
            widget.onTabChanged(widget.selectedIndex + 1);
          }
        }
      },
      child: Container(
        height: 96 + 24 * 2,
        child: Stack(
          alignment: Alignment.center,
          children: [
            Positioned.fill(
              child: BackdropFilter(
                filter: ImageFilter.blur(sigmaX: 5, sigmaY: 5),
                child: Container(
                  color: theme.colorScheme.secondary.withOpacity(0.65),
                ),
              ),
            ),
            AnimatedPositioned(
              left: widget.selectedIndex > 0 ? -128 / 2 : -128,
              duration: const Duration(milliseconds: 300),
              curve: Curves.easeInOut,
              child: GestureDetector(
                onTap: () {
                  if (widget.selectedIndex > 0) {
                    widget.onTabChanged(widget.selectedIndex - 1);
                  }
                },
                child: Container(
                  decoration: BoxDecoration(
                    color: theme.colorScheme.primary,
                    borderRadius: BorderRadius.only(
                      topRight: Radius.circular(96 / 2),
                      bottomRight: Radius.circular(96 / 2),
                    ),
                  ),
                  child: Transform.translate(
                    offset: Offset(128 / 4, 0),
                    child: Icon(Icons.arrow_back_ios,
                        color: theme.colorScheme.primaryForeground),
                  ),
                  width: 128,
                  height: 96,
                ),
              ),
            ),
            AnimatedPositioned(
              duration: const Duration(milliseconds: 300),
              curve: Curves.easeInOut,
              right:
                  widget.selectedIndex < widget.totalTabs - 1 ? -128 / 2 : -128,
              child: Container(
                decoration: BoxDecoration(
                  color: theme.colorScheme.primary,
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(96 / 2),
                    bottomLeft: Radius.circular(96 / 2),
                  ),
                ),
                child: Transform.translate(
                  offset: Offset(-128 / 4, 0),
                  child: Icon(Icons.arrow_forward_ios,
                      color: theme.colorScheme.primaryForeground),
                ),
                width: 128,
                height: 96,
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                for (int i = 0; i < widget.totalTabs; i++)
                  Container(
                    width: 24,
                    height: 24,
                    margin: const EdgeInsets.symmetric(horizontal: 8),
                    decoration: BoxDecoration(
                      color: i == widget.selectedIndex
                          ? theme.colorScheme.primary
                          : theme.colorScheme.muted,
                      shape: BoxShape.circle,
                    ),
                  )
              ],
            ),
            if (!swiped)
              RepeatedAnimationBuilder.noChild(
                  builder: (context, value) {
                    return Positioned(
                      top: 24 - _timelineTween.lerp(value) * 18,
                      child: Text('Swipe ke kiri/kanan',
                          style: theme.typography.lg.copyWith(
                            color: theme.colorScheme.subtitleForeground,
                          )),
                    );
                  },
                  duration: _timelineTween.duration),
          ],
        ),
      ),
    );
  }
}
