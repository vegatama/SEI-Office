import 'package:flutter/material.dart';
import 'package:seioffice/src/components/shimmer.dart';

import '../theme/theme.dart';

class EmployeeCircleAvatar extends StatefulWidget {
  final double radius;
  final bool showContent;
  final String? url;
  final String? employeeName;

  const EmployeeCircleAvatar({
    super.key,
    required this.radius,
    this.url,
    this.employeeName,
    required this.showContent,
  });

  @override
  State<EmployeeCircleAvatar> createState() => _EmployeeCircleAvatarState();
}

class _EmployeeCircleAvatarState extends State<EmployeeCircleAvatar> {
  late ImageProvider? _imageProvider;

  @override
  void initState() {
    super.initState();
    _imageProvider = widget.url != null
        ? NetworkImage(
            widget.url!,
          )
        : null;
  }

  @override
  void didUpdateWidget(covariant EmployeeCircleAvatar oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.url != widget.url) {
      _imageProvider = widget.url != null
          ? NetworkImage(
              widget.url!,
            )
          : null;
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return ContentPlaceholder(
      showContent: widget.showContent,
      useBox: false,
      child: CircleAvatar(
        radius: widget.radius,
        backgroundColor: theme.colorScheme.primaryForeground,
        foregroundImage: _imageProvider,
      ),
    );
  }
}
