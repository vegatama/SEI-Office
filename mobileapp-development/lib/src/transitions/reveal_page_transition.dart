import 'dart:math';

import 'package:flutter/material.dart';

class RevealPageRoute<T> extends PageRoute<T> {
  final WidgetBuilder builder;
  final Alignment? alignment;
  final Offset? offset;
  final double? minRadius;
  final double? maxRadius;

  RevealPageRoute({
    required this.builder,
    this.alignment,
    this.offset,
    this.minRadius,
    this.maxRadius,
    super.settings,
  });

  @override
  Color get barrierColor => Colors.black.withOpacity(0.5);

  @override
  String get barrierLabel => '';

  @override
  bool get maintainState => true;

  @override
  Duration get transitionDuration => const Duration(milliseconds: 300);

  @override
  Animation<double> createAnimation() {
    return CurvedAnimation(
      parent: super.createAnimation(),
      curve: Curves.easeInOut,
    );
  }

  @override
  Widget buildPage(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation) {
    return builder(context);
  }

  @override
  Widget buildTransitions(BuildContext context, Animation<double> animation,
      Animation<double> secondaryAnimation, Widget child) {
    return RevealPageAnimation(
      alignment: alignment,
      offset: offset,
      minRadius: minRadius,
      maxRadius: maxRadius,
      animation: animation,
      child: child,
    );
  }
}

class RevealPageTransitions extends PageTransitionsBuilder {
  final Alignment? alignment;
  final Offset? offset;
  final double? minRadius;
  final double? maxRadius;

  const RevealPageTransitions({
    this.alignment,
    this.offset,
    this.minRadius,
    this.maxRadius,
  });

  @override
  Widget buildTransitions<T>(
      PageRoute<T> route,
      BuildContext context,
      Animation<double> animation,
      Animation<double> secondaryAnimation,
      Widget child) {
    return RevealPageAnimation(
      alignment: alignment,
      offset: offset,
      minRadius: minRadius,
      maxRadius: maxRadius,
      animation: animation,
      child: child,
    );
  }
}

class RevealPageAnimation extends StatelessWidget {
  final Alignment? alignment;
  final Offset? offset;
  final double? minRadius;
  final double? maxRadius;
  final Widget child;
  final Animation<double> animation;

  const RevealPageAnimation({
    super.key,
    this.alignment,
    this.offset,
    this.minRadius,
    this.maxRadius,
    required this.child,
    required this.animation,
  });

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: animation,
      builder: (context, child) {
        return ClipPath(
          clipper: RevealClipper(
            progress: animation.value,
            alignment: alignment,
            offset: offset,
            minRadius: minRadius,
            maxRadius: maxRadius,
          ),
          child: child,
        );
      },
      child: child,
    );
  }
}

class RevealClipper extends CustomClipper<Path> {
  final double progress;
  final Alignment? alignment;
  final Offset? offset;
  final double? minRadius;
  final double? maxRadius;

  RevealClipper({
    required this.progress,
    this.alignment,
    this.offset,
    this.minRadius,
    this.maxRadius,
  });

  @override
  Path getClip(Size size) {
    final Offset center =
        alignment?.alongSize(size) ?? offset ?? size.center(Offset.zero);
    final minRadius = this.minRadius ?? 0;
    final maxRadius = this.maxRadius ?? getMaxRadius(size, center);
    return Path()
      ..addOval(
        Rect.fromCircle(
          center: center,
          radius: lerpDouble(minRadius, maxRadius, progress),
        ),
      );
  }

  @override
  bool shouldReclip(covariant RevealClipper oldClipper) {
    return oldClipper.progress != progress ||
        oldClipper.alignment != alignment ||
        oldClipper.offset != offset ||
        oldClipper.minRadius != minRadius ||
        oldClipper.maxRadius != maxRadius;
  }

  static double getMaxRadius(Size size, Offset center) {
    final w = max(center.dx, size.width - center.dx);
    final h = max(center.dy, size.height - center.dy);
    return sqrt(w * w + h * h);
  }

  static double lerpDouble(double a, double b, double t) {
    return a * (1 - t) + b * t;
  }
}
