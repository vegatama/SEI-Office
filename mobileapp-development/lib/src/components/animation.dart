import 'dart:math';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:seioffice/src/components/data.dart';
import 'package:seioffice/src/service/lerped_localization.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/app_service.dart';
import 'package:seioffice/src/theme/theme.dart';

class AnimatedSEILogoBackground extends StatelessWidget {
  const AnimatedSEILogoBackground({super.key});

  @override
  Widget build(BuildContext context) {
    final appController = context.getService<AppService>();
    // USE BRIGHTNESS FROM APP CONTROLLER INSTEAD TO PREVENT DELAY
    return ValueListenableBuilder(
        valueListenable: appController.theme,
        builder: (context, theme, _) {
          return AnimatedSwitcher(
            duration: const Duration(milliseconds: 150),
            transitionBuilder: (child, animation) {
              return FadeTransition(
                opacity: animation,
                child: child,
              );
            },
            child: theme.brightness == Brightness.dark
                ? Image.asset(
                    'lib/assets/images/sei_logo_bg_dark.png',
                    key: ValueKey('dark_logo'),
                  )
                : Image.asset(
                    'lib/assets/images/sei_logo_bg.png',
                    key: ValueKey('light_logo'),
                  ),
          );
        });
  }
}

class SEILocalizationsTween extends Tween<SEILocalizations> {
  SEILocalizationsTween({super.begin, super.end});

  @override
  SEILocalizations lerp(double t) {
    return LerpedLocalization(begin!, end!, t);
  }
}

class SEIThemeTween extends Tween<SEITheme> {
  SEIThemeTween({super.begin, super.end});

  @override
  SEITheme lerp(double t) {
    return SEITheme.lerp(begin!, end!, t);
  }
}

class AnimatedSEITheme extends ImplicitlyAnimatedWidget {
  final SEITheme data;
  final Widget child;

  const AnimatedSEITheme({
    required this.data,
    required this.child,
    super.curve,
    super.duration = const Duration(milliseconds: 150),
    super.key,
  });

  @override
  _AnimatedSEIThemeState createState() => _AnimatedSEIThemeState();
}

class _AnimatedSEIThemeState extends AnimatedWidgetBaseState<AnimatedSEITheme> {
  SEIThemeTween? _data;

  @override
  void forEachTween(TweenVisitor<dynamic> visitor) {
    _data = visitor(_data, widget.data,
            (dynamic value) => SEIThemeTween(begin: value as SEITheme))!
        as SEIThemeTween;
  }

  @override
  Widget build(BuildContext context) {
    return Data<SEITheme>(
      data: _data!.evaluate(animation),
      child: widget.child,
    );
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder description) {
    super.debugFillProperties(description);
    description.add(DiagnosticsProperty<SEIThemeTween>('data', _data,
        showName: false, defaultValue: null));
  }
}

class AnimatedSEILocalizations extends ImplicitlyAnimatedWidget {
  final SEILocalizations data;
  final Widget child;

  const AnimatedSEILocalizations({
    required this.data,
    required this.child,
    super.curve,
    super.duration = const Duration(milliseconds: 150),
    super.key,
  });

  @override
  _AnimatedSEILocalizationsState createState() =>
      _AnimatedSEILocalizationsState();
}

class _AnimatedSEILocalizationsState
    extends AnimatedWidgetBaseState<AnimatedSEILocalizations> {
  SEILocalizationsTween? _data;

  @override
  void forEachTween(TweenVisitor<dynamic> visitor) {
    _data = visitor(
            _data,
            widget.data,
            (dynamic value) =>
                SEILocalizationsTween(begin: value as SEILocalizations))!
        as SEILocalizationsTween;
  }

  @override
  Widget build(BuildContext context) {
    return Data<SEILocalizations>(
      data: _data!.evaluate(animation),
      child: widget.child,
    );
  }

  @override
  void debugFillProperties(DiagnosticPropertiesBuilder description) {
    super.debugFillProperties(description);
    description.add(DiagnosticsProperty<SEILocalizationsTween>('data', _data,
        showName: false, defaultValue: null));
  }
}

class Timeline {
  final Duration duration;
  final Curve curve;
  final double startValue;
  final double endValue;

  Timeline({
    required this.duration,
    this.curve = Curves.linear,
    this.startValue = 0.0,
    this.endValue = 1.0,
  });
}

class TimelineTween {
  final List<Timeline> timelines;

  TimelineTween(this.timelines);

  Duration get duration {
    Duration maxDuration = Duration.zero;
    for (var timeline in timelines) {
      maxDuration += timeline.duration;
    }
    return maxDuration;
  }

  double lerp(double value, {double min = 0, double max = 1}) {
    // normalize value
    value = (value - min) / (max - min);
    Duration maxDuration = duration;
    // find the timeline that contains the current duration
    Duration current = Duration.zero;
    for (var timeline in timelines) {
      double startBound = current.inMilliseconds / maxDuration.inMilliseconds;
      double endBound = (current + timeline.duration).inMilliseconds /
          maxDuration.inMilliseconds;
      if (value >= startBound && value <= endBound) {
        double t = (value - startBound) / (endBound - startBound);
        return lerpDouble(timeline.startValue, timeline.endValue,
            timeline.curve.transform(t));
      }
      current += timeline.duration;
    }
    // throw Exception('Invalid value $value');
    return 1;
  }

  static double lerpDouble(double a, double b, double t) {
    return a + (b - a) * t;
  }
}

class TimelineAnimationBuilder extends StatelessWidget {
  final TimelineTween timeline;
  final Widget Function(BuildContext context, double value, Widget? child)
      builder;
  final Widget? child;

  const TimelineAnimationBuilder({
    required this.timeline,
    required this.builder,
    this.child,
    Key? key,
  }) : super(key: key);

  factory TimelineAnimationBuilder.noChild({
    required TimelineTween timeline,
    required Widget Function(BuildContext context, double value) builder,
    Key? key,
  }) {
    return TimelineAnimationBuilder(
      timeline: timeline,
      builder: (context, value, child) => builder(context, value),
      key: key,
    );
  }

  @override
  Widget build(BuildContext context) {
    if (child != null) {
      return RepeatedAnimationBuilder(
        duration: timeline.duration,
        builder: (context, value, child) {
          return builder(context, timeline.lerp(value), child);
        },
        child: child!,
      );
    } else {
      return RepeatedAnimationBuilder.noChild(
        duration: timeline.duration,
        builder: (context, value) {
          return builder(context, timeline.lerp(value), null);
        },
      );
    }
  }
}

class RepeatedAnimationBuilder extends StatefulWidget {
  final Widget Function(BuildContext context, double value, Widget child)?
      builder;
  final Widget Function(BuildContext context, double value)? noChildBuilder;

  final Duration duration;
  final Curve curve;
  final double start;
  final double end;
  final Widget? child;
  final bool reverse;

  const RepeatedAnimationBuilder({
    Key? key,
    required this.builder,
    required this.duration,
    this.curve = Curves.linear,
    this.start = 0.0,
    this.end = 1.0,
    required Widget this.child,
    this.reverse = false,
  })  : noChildBuilder = null,
        super(key: key);

  const RepeatedAnimationBuilder.noChild({
    super.key,
    required Widget Function(BuildContext context, double value) builder,
    required this.duration,
    this.curve = Curves.linear,
    this.start = 0.0,
    this.end = 1.0,
    this.reverse = false,
  })  : builder = null,
        child = null,
        noChildBuilder = builder;

  @override
  State<RepeatedAnimationBuilder> createState() =>
      _RepeatedAnimationBuilderState();
}

class _RepeatedAnimationBuilderState extends State<RepeatedAnimationBuilder>
    with SingleTickerProviderStateMixin {
  late final AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: widget.duration,
      vsync: this,
    );
    _controller.repeat();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _controller,
      builder: (context, child) {
        final value = lerpDouble(widget.start, widget.end,
            widget.reverse ? 1 - _controller.value : _controller.value);
        if (widget.builder != null) {
          return widget.builder!(context, value, child!);
        } else {
          return widget.noChildBuilder!(context, value);
        }
      },
      child: widget.child,
    );
  }

  static double lerpDouble(double a, double b, double t) {
    return a + (b - a) * t;
  }
}

class ConstantRebuild extends StatefulWidget {
  final WidgetBuilder builder;

  const ConstantRebuild({
    required this.builder,
    Key? key,
  }) : super(key: key);

  @override
  State<ConstantRebuild> createState() => _ConstantRebuildState();
}

class _ConstantRebuildState extends State<ConstantRebuild>
    with SingleTickerProviderStateMixin {
  late Ticker _ticker;

  @override
  void initState() {
    super.initState();
    _ticker = createTicker((elapsed) {
      setState(() {});
    });
    _ticker.start();
  }

  @override
  void dispose() {
    _ticker.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return widget.builder(context);
  }
}

class AnimatedPendingIcon extends StatefulWidget {
  @override
  State<AnimatedPendingIcon> createState() => _AnimatedPendingIconState();
}

class _AnimatedPendingIconState extends State<AnimatedPendingIcon>
    with SingleTickerProviderStateMixin {
  late final AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(seconds: 3),
      vsync: this,
    )..repeat();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _controller,
      builder: (context, child) {
        return Transform.rotate(
          angle: _controller.value * 2 * pi,
          child: child,
        );
      },
      child: const Icon(
        Icons.hourglass_empty_outlined,
      ),
    );
  }
}

class RippleAnimation extends StatelessWidget {
  final double minRadius;
  final double maxRadius;
  final Duration duration;
  final double thickness;
  final double opacity;
  final bool animateThickness;
  final bool animateOpacity;
  final Color color;
  final double animationOffset;
  final Curve curve;

  const RippleAnimation({
    this.minRadius = 0,
    this.maxRadius = 100,
    this.duration = const Duration(milliseconds: 500),
    this.thickness = 0,
    this.opacity = 1,
    this.animateThickness = false,
    this.animateOpacity = false,
    required this.color,
    this.animationOffset = 0,
    this.curve = Curves.easeInOut,
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return RepeatedAnimationBuilder.noChild(
      curve: curve,
      builder: (context, value) {
        if (animationOffset > 0) {
          value = (value + animationOffset) % 1;
        }
        return Container(
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            border: Border.all(
              color: color.withOpacity(
                animateOpacity ? (1 - value) * opacity : opacity,
              ),
              width: animateThickness ? (1 - value) * thickness : thickness,
            ),
          ),
          width: minRadius + (maxRadius - minRadius) * value,
          height: minRadius + (maxRadius - minRadius) * value,
        );
      },
      duration: duration,
    );
  }
}

class AnimatedValue extends StatefulWidget {
  final double value;
  final Widget Function(BuildContext context, double value, Widget? child)
      builder;
  final Duration duration;
  final Curve curve;
  final Widget? child;

  const AnimatedValue({
    required this.value,
    required this.builder,
    this.duration = const Duration(milliseconds: 300),
    this.curve = Curves.easeInOut,
    this.child,
  });

  @override
  State<AnimatedValue> createState() => _AnimatedValueState();
}

class _AnimatedValueState extends State<AnimatedValue>
    with SingleTickerProviderStateMixin {
  late final AnimationController _controller;
  late double _oldValue;
  late double _newValue;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: widget.duration,
      vsync: this,
    );
    _oldValue = widget.value;
    _newValue = widget.value;
  }

  @override
  void didUpdateWidget(covariant AnimatedValue oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.value != widget.value) {
      _oldValue = _newValue;
      _newValue = widget.value;
      _controller.reset();
      _controller.forward();
    }
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: _controller,
      builder: (context, child) {
        final value = lerpDouble(_oldValue, _newValue, _controller.value);
        return widget.builder(context, value, child);
      },
      child: widget.child,
    );
  }

  static double lerpDouble(double a, double b, double t) {
    return a + (b - a) * t;
  }
}
