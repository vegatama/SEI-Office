import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:seioffice/src/theme/theme.dart';

class Steps extends StatelessWidget {
  final List<Widget> children;
  final double gap;
  final int? currentStep;
  final bool lastFailed;
  final bool reverse;
  const Steps({
    super.key,
    required this.children,
    this.gap = 24,
    this.currentStep,
    this.lastFailed = false,
    this.reverse = false,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);

    Color getStepColor(int step) {
      if (reverse) {
        step = children.length - 1 - step;
      }
      if (currentStep == null) {
        if (lastFailed && step == children.length - 1) {
          return theme.colorScheme.destructive;
        }
        return theme.colorScheme.primary;
      }
      if (step < currentStep!) {
        if (lastFailed && step == currentStep! - 1) {
          return theme.colorScheme.destructive;
        }
        return theme.colorScheme.primary;
      } else {
        return theme.colorScheme.muted;
      }
    }

    return IntrinsicWidth(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          for (var i = 0; i < children.length; i++)
            IntrinsicHeight(
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Stack(
                    clipBehavior: Clip.none,
                    alignment: Alignment.center,
                    children: [
                      if (i != 0)
                        Positioned(
                          bottom: 20 + gap / 2,
                          top: 0,
                          child: Container(
                            width: 2,
                            color: theme.colorScheme.muted,
                          ),
                        ),
                      if (i != children.length - 1)
                        Positioned(
                          bottom: 0,
                          top: 20 + gap / 2,
                          child: Container(
                            width: 2,
                            color: theme.colorScheme.muted,
                          ),
                        ),
                      Container(
                        width: 20,
                        height: 20,
                        decoration: BoxDecoration(
                          color: theme.colorScheme.secondary,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: getStepColor(i),
                            width: 2,
                          ),
                        ),
                        padding: EdgeInsets.all(2),
                        child: Container(
                          decoration: BoxDecoration(
                            color: getStepColor(i),
                            borderRadius: BorderRadius.circular(12),
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(width: 12),
                  // children[i],
                  Expanded(
                    child: Padding(
                      padding: EdgeInsets.symmetric(vertical: gap / 2),
                      child: children[i],
                    ),
                  ),
                ],
              ),
            ),
        ],
      ),
    );
  }
}
