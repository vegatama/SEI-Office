import 'package:flutter/material.dart';
import 'package:seioffice/src/theme/theme.dart';

class Swiper extends StatefulWidget {
  final ValueChanged<bool>? onSwiped;
  final Duration swipeDebounceDuration;
  final Duration swipeRevertDuration;
  final double threshold;

  const Swiper({
    super.key,
    this.onSwiped,
    this.swipeDebounceDuration = const Duration(milliseconds: 300),
    this.swipeRevertDuration = const Duration(milliseconds: 300),
    this.threshold = 0.75,
  });

  @override
  State<Swiper> createState() => _SwiperState();
}

class _SwiperState extends State<Swiper> with SingleTickerProviderStateMixin {
  late AnimationController _swipeController;
  bool swiped = false;

  @override
  void initState() {
    super.initState();
    _swipeController = AnimationController(
      vsync: this,
    );
  }

  @override
  void dispose() {
    _swipeController.dispose();
    super.dispose();
  }

  void _revert() {
    _swipeController.animateTo(0, duration: widget.swipeRevertDuration);
  }

  bool get _enabled => widget.onSwiped != null;

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return Container(
      height: 56,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(50),
        border: Border.all(
          color: theme.colorScheme.swiperTrackBorder,
          width: 2,
        ),
      ),
      padding: const EdgeInsets.all(2),
      child: LayoutBuilder(builder: (context, constraints) {
        return ListenableBuilder(
            listenable: _swipeController,
            builder: (context, _) {
              return GestureDetector(
                onHorizontalDragStart: !_enabled
                    ? null
                    : (details) {
                        // stop the animation
                        _swipeController.stop();
                      },
                onHorizontalDragUpdate: !_enabled
                    ? null
                    : (details) {
                        if (_swipeController.value >= 1) {
                          return;
                        }
                        _swipeController.value = (_swipeController.value +
                                details.primaryDelta! / constraints.maxWidth)
                            .clamp(0.0, 1.0);
                      },
                onHorizontalDragEnd: (details) {
                  if (_swipeController.value > widget.threshold) {
                    _swipeController.animateTo(1,
                        duration: widget.swipeRevertDuration);
                    // wait for debounce, and then revert back
                    Future.delayed(widget.swipeDebounceDuration, () {
                      _revert();
                      if (swiped) {
                        swiped = false;
                        widget.onSwiped?.call(false);
                      }
                    });
                    if (!swiped) {
                      swiped = true;
                      widget.onSwiped?.call(true);
                    }
                  } else {
                    _revert();
                    if (swiped) {
                      swiped = false;
                      widget.onSwiped?.call(false);
                    }
                  }
                },
                child: Container(
                  decoration: BoxDecoration(
                    color: theme.colorScheme.swiperTrack,
                    borderRadius: BorderRadius.circular(50),
                  ),
                  clipBehavior: Clip.antiAlias,
                  child: Stack(
                    children: [
                      // Swiper Selected Track
                      Positioned(
                        top: 0,
                        left: 0,
                        bottom: 0,
                        right: (constraints.maxWidth - 96) *
                            (1 - _swipeController.value), // width is 96
                        child: Container(
                          decoration: BoxDecoration(
                            color: _enabled
                                ? theme.colorScheme.swiperTrackSelected
                                : theme.colorScheme.swiperTrack,
                            // borderRadius: BorderRadius.circular(50),
                            borderRadius: const BorderRadius.only(
                              topRight: Radius.circular(50),
                              bottomRight: Radius.circular(50),
                            ),
                          ),
                        ),
                      ),
                      // Swiper Thumb
                      Positioned(
                        top: 0,
                        bottom: 0,
                        left: (constraints.maxWidth - 96) *
                            _swipeController.value, // width is 96
                        child: Container(
                          width: 96,
                          decoration: BoxDecoration(
                            color: _enabled
                                ? theme.colorScheme.swiperThumb
                                : theme.colorScheme.muted,
                            borderRadius: BorderRadius.circular(50),
                          ),
                          child: Center(
                            child: Icon(
                              Icons.double_arrow,
                              color: theme.colorScheme.swiperForeground,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              );
            });
      }),
    );
  }
}
