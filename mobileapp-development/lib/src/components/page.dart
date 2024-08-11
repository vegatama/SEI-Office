import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/swiped_tab_bar.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/theme/theme.dart';

import '../util.dart';

class PlainScrollConfiguration extends MaterialScrollBehavior {
  const PlainScrollConfiguration();

  @override
  Set<PointerDeviceKind> get dragDevices => {
        PointerDeviceKind.touch,
        PointerDeviceKind.mouse,
      };

  @override
  Widget buildOverscrollIndicator(
      BuildContext context, Widget child, ScrollableDetails details) {
    return child;
  }
}

class StandardPage extends StatelessWidget {
  final Widget appBar;
  final List<Widget>? children;
  final Widget Function(BuildContext context, int index)? builder;
  final int? itemCount;
  final Future<void> Function()? onRefresh;
  final EdgeInsetsGeometry? padding;
  final Widget? footer;
  final PreferredSizeWidget? bottom;

  const StandardPage({
    super.key,
    required this.appBar,
    this.children,
    this.itemCount,
    this.onRefresh,
    this.padding,
    this.builder,
    this.footer,
    this.bottom,
  }) : assert(children != null || builder != null,
            'slivers or delegate must not be null');

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return NonStackedSafeArea(
      child: ScrollConfiguration(
        behavior: PlainScrollConfiguration(),
        child: Scaffold(
          backgroundColor: theme.colorScheme.secondary,
          body: NestedScrollView(
            floatHeaderSlivers: true,
            clipBehavior: Clip.none,
            headerSliverBuilder: (context, innerBoxIsScrolled) {
              return [
                SliverOverlapAbsorber(
                  handle:
                      NestedScrollView.sliverOverlapAbsorberHandleFor(context),
                  sliver: SliverAppBar(
                    title: BackdropFilter(
                      filter: ImageFilter.blur(sigmaX: 5, sigmaY: 5),
                      child: Container(
                        height: 72 + mediaPadding.top,
                        padding: const EdgeInsets.symmetric(vertical: 8),
                        child: Align(
                          alignment: Alignment.bottomCenter,
                          child: SizedBox(
                            width: double.infinity,
                            child: appBar,
                          ),
                        ),
                      ),
                    ),
                    // tab pane
                    bottom: bottom,
                    automaticallyImplyLeading: false,
                    toolbarHeight: 72 + mediaPadding.top,
                    surfaceTintColor: Colors.transparent,
                    backgroundColor:
                        theme.colorScheme.secondary.withOpacity(0.65),
                    floating: false,
                    pinned: true,
                  ),
                ),
              ];
            },
            body: Builder(builder: (context) {
              Widget child = Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Expanded(
                    child: CustomScrollView(
                      slivers: [
                        SliverOverlapInjector(
                          handle:
                              NestedScrollView.sliverOverlapAbsorberHandleFor(
                                  context),
                        ),
                        SliverPadding(
                          padding: padding ?? EdgeInsets.zero,
                          sliver: SliverList(
                            delegate: SliverChildBuilderDelegate(
                              childCount: children?.length ?? itemCount,
                              (context, index) {
                                if (builder != null) {
                                  return builder!(context, index);
                                }
                                return children![index];
                              },
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                  if (footer != null) footer!,
                ],
              );
              if (onRefresh != null) {
                child = RefreshIndicator(
                  onRefresh: onRefresh!,
                  edgeOffset: 72 + mediaPadding.top,
                  displacement: 24,
                  child: child,
                );
              }
              return child;
            }),
          ),
        ),
      ),
    );
  }
}

class NonStackedSafeArea extends StatelessWidget {
  final Widget child;

  const NonStackedSafeArea({
    super.key,
    required this.child,
  });

  @override
  Widget build(BuildContext context) {
    print('MediaPadding: ${MediaQuery.of(context).padding}');
    return Stack(
      clipBehavior: Clip.none,
      children: [
        Positioned.fill(
          child: MediaQuery.removePadding(
            context: context,
            removeTop: true,
            removeBottom: true,
            removeLeft: true,
            removeRight: true,
            child: child,
          ),
        ),
      ],
    );
  }
}

class ScaffoldStandardPage extends StatelessWidget {
  final Widget appBar;
  final Widget child;
  final Future<void> Function()? onRefresh;
  final EdgeInsetsGeometry? padding;
  final Widget? footer;

  const ScaffoldStandardPage({
    super.key,
    required this.appBar,
    required this.child,
    this.onRefresh,
    this.padding,
    this.footer,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return NonStackedSafeArea(
      child: ScrollConfiguration(
        behavior: const PlainScrollConfiguration(),
        child: Scaffold(
          backgroundColor: theme.colorScheme.background,
          body: NestedScrollView(
            floatHeaderSlivers: true,
            clipBehavior: Clip.none,
            headerSliverBuilder: (context, innerBoxIsScrolled) {
              return [
                SliverOverlapAbsorber(
                  handle:
                      NestedScrollView.sliverOverlapAbsorberHandleFor(context),
                  sliver: SliverAppBar(
                    flexibleSpace: FlexibleSpaceBar(
                      background: DefaultTextStyle.merge(
                        style: TextStyle(
                          color: innerBoxIsScrolled
                              ? theme.colorScheme.secondaryForeground
                              : theme.colorScheme.primaryForeground,
                        ),
                        child: IconTheme(
                          data: IconThemeData(
                            color: innerBoxIsScrolled
                                ? theme.colorScheme.secondaryForeground
                                : theme.colorScheme.primaryForeground,
                          ),
                          child: AnimatedContainer(
                            padding: const EdgeInsets.symmetric(horizontal: 18),
                            color: innerBoxIsScrolled
                                ? theme.colorScheme.secondary.withOpacity(0.65)
                                : Colors.transparent,
                            duration: const Duration(milliseconds: 100),
                            width: double.infinity,
                            height: 72 + mediaPadding.top,
                            child: BackdropFilter(
                              filter: ImageFilter.blur(
                                  sigmaX: innerBoxIsScrolled ? 5 : 0,
                                  sigmaY: innerBoxIsScrolled ? 5 : 0),
                              child: Padding(
                                padding: mediaPadding,
                                child: Container(
                                  height: 72,
                                  padding:
                                      const EdgeInsets.symmetric(vertical: 8),
                                  child: Align(
                                    alignment: Alignment.bottomCenter,
                                    child: SizedBox(
                                      width: double.infinity,
                                      child: appBar,
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                    automaticallyImplyLeading: false,
                    toolbarHeight: 72 + mediaPadding.top,
                    surfaceTintColor: Colors.transparent,
                    backgroundColor: Colors.transparent,
                    // backgroundColor: innerBoxIsScrolled
                    //     ? theme.colorScheme.secondary.withOpacity(0.65)
                    //     : Colors.transparent,
                    floating: false,
                    pinned: true,
                  ),
                ),
              ];
            },
            body: Builder(builder: (context) {
              Widget child = Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Expanded(
                    child: Padding(
                      padding: padding ?? EdgeInsets.zero,
                      child: this.child,
                    ),
                  ),
                  if (footer != null) footer!,
                ],
              );
              if (onRefresh != null) {
                child = RefreshIndicator(
                  onRefresh: onRefresh!,
                  edgeOffset: 72 + mediaPadding.top,
                  displacement: 24,
                  child: child,
                );
              }
              return child;
            }),
          ),
        ),
      ),
    );
  }
}

class TabbedPage extends StatefulWidget {
  final Widget appBar;
  final List<List<Widget>>? children;
  final Widget Function(BuildContext context, int index, int tab)? builder;
  final List<int>? itemCount;
  final Future<void> Function()? onRefresh;
  final EdgeInsetsGeometry? padding;
  final Widget? footer;
  final List<Widget> tabs;
  final bool showTabBar;

  const TabbedPage({
    super.key,
    required this.appBar,
    this.children,
    this.itemCount,
    this.onRefresh,
    this.padding,
    this.builder,
    this.footer,
    required this.tabs,
    this.showTabBar = true,
  }) : assert(children != null || builder != null,
            'slivers or delegate must not be null');

  @override
  State<TabbedPage> createState() => _TabbedPageState();
}

class _TabbedPageState extends State<TabbedPage>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;

  // filled in later after build
  GlobalKey _bottomBarKey = GlobalKey();

  @override
  void initState() {
    super.initState();
    _tabController = TabController(
      length: (widget.children?.length ?? widget.itemCount!.length),
      vsync: this,
    );
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return NonStackedSafeArea(
      child: ScrollConfiguration(
        behavior: PlainScrollConfiguration(),
        child: Scaffold(
          backgroundColor: theme.colorScheme.secondary,
          body: NestedScrollView(
            floatHeaderSlivers: true,
            clipBehavior: Clip.none,
            headerSliverBuilder: (context, innerBoxIsScrolled) {
              return [
                SliverOverlapAbsorber(
                  handle:
                      NestedScrollView.sliverOverlapAbsorberHandleFor(context),
                  sliver: SliverAppBar(
                    title: BackdropFilter(
                      filter: ImageFilter.blur(sigmaX: 5, sigmaY: 5),
                      child: Container(
                        height: 72 + mediaPadding.top,
                        padding: const EdgeInsets.symmetric(vertical: 8),
                        child: Align(
                          alignment: Alignment.bottomCenter,
                          child: SizedBox(
                            width: double.infinity,
                            child: widget.appBar,
                          ),
                        ),
                      ),
                    ),
                    // tab pane
                    bottom: widget.showTabBar
                        ? PreferredSize(
                            preferredSize: const Size.fromHeight(48),
                            child: ClipRect(
                              child: Stack(
                                children: [
                                  Positioned.fill(
                                    child: BackdropFilter(
                                      filter: ImageFilter.blur(
                                          sigmaX: 5, sigmaY: 5),
                                      child: Container(),
                                    ),
                                  ),
                                  Material(
                                    color: Colors.transparent,
                                    child: TabBar(
                                      controller: _tabController,
                                      tabs: widget.tabs,
                                      labelColor:
                                          theme.colorScheme.secondaryForeground,
                                      unselectedLabelColor:
                                          theme.colorScheme.secondaryForeground,
                                      indicatorColor: theme.colorScheme.primary,
                                      indicatorSize: TabBarIndicatorSize.tab,
                                      indicatorWeight: 3,
                                      labelStyle: theme.typography.md.copyWith(
                                        fontWeight: fontSemiBold,
                                      ),
                                      unselectedLabelStyle:
                                          theme.typography.md.copyWith(
                                        fontWeight: fontRegular,
                                      ),
                                      overlayColor: MaterialStateProperty.all(
                                        theme.colorScheme.primary
                                            .withOpacity(0.1),
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          )
                        : null,
                    automaticallyImplyLeading: false,
                    toolbarHeight: 72 + mediaPadding.top,
                    surfaceTintColor: Colors.transparent,
                    backgroundColor:
                        theme.colorScheme.secondary.withOpacity(0.65),
                    floating: false,
                    pinned: true,
                  ),
                ),
              ];
            },
            body: Builder(builder: (context) {
              return TabBarView(
                controller: _tabController,
                children: [
                  for (int i = 0; i < widget.tabs.length; i++)
                    Builder(
                      builder: (context) {
                        Widget child = Column(
                          crossAxisAlignment: CrossAxisAlignment.stretch,
                          children: [
                            Expanded(
                              child: CustomScrollView(
                                slivers: [
                                  SliverOverlapInjector(
                                    handle: NestedScrollView
                                        .sliverOverlapAbsorberHandleFor(
                                            context),
                                  ),
                                  SliverPadding(
                                    padding: widget.padding ?? EdgeInsets.zero,
                                    sliver: SliverList(
                                      delegate: SliverChildBuilderDelegate(
                                        // childCount: widget.children?.length ??
                                        //     widget.itemCount,
                                        childCount:
                                            widget.children?[i].length ??
                                                widget.itemCount![i],
                                        (context, index) {
                                          if (widget.builder != null) {
                                            return widget.builder!(
                                                context, index, i);
                                          }
                                          return widget.children![i][index];
                                        },
                                      ),
                                    ),
                                  ),
                                  SliverToBoxAdapter(
                                    child: SizedBox(
                                      height:
                                          _bottomBarKey.currentContext != null
                                              ? _bottomBarKey.currentContext!
                                                  .findRenderObject()!
                                                  .paintBounds
                                                  .height
                                              : 0,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        );
                        if (widget.onRefresh != null) {
                          child = RefreshIndicator(
                            onRefresh: widget.onRefresh!,
                            edgeOffset: 72 +
                                48 +
                                mediaPadding
                                    .top, // 72 is appbar height, 48 is tab height
                            displacement: 24,
                            child: child,
                          );
                        }
                        return child;
                      },
                    ),
                ],
              );
            }),
          ),
          bottomNavigationBar: widget.footer == null
              ? null
              : Stack(
                  key: _bottomBarKey,
                  alignment: Alignment.bottomCenter,
                  children: [
                    Positioned.fill(
                      child: ClipRect(
                        child: BackdropFilter(
                          filter: ImageFilter.blur(sigmaX: 5, sigmaY: 5),
                          child: Container(
                            color:
                                theme.colorScheme.secondary.withOpacity(0.65),
                          ),
                        ),
                      ),
                    ),
                    SizedBox(
                      width: double.infinity,
                      child: widget.footer!,
                    ),
                  ],
                ),
          extendBody: true,
          extendBodyBehindAppBar: true,
        ),
      ),
    );
  }
}

class StackedPage extends StatelessWidget {
  final Widget appBar;
  final Widget? flexibleSpace;
  final Widget body;

  const StackedPage({
    super.key,
    required this.appBar,
    required this.body,
    this.flexibleSpace,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return ScrollConfiguration(
      behavior: ScrollConfiguration.of(context).copyWith(dragDevices: {
        PointerDeviceKind.touch,
        PointerDeviceKind.mouse,
      }),
      child: Scaffold(
          backgroundColor: theme.colorScheme.secondary,
          body: Stack(
            children: [
              Positioned.fill(
                child: body,
              ),
              Positioned(
                top: 0,
                left: 0,
                right: 0,
                child: Padding(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 12, vertical: 24),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(
                            vertical: 8, horizontal: 18),
                        decoration: BoxDecoration(
                          color: theme.colorScheme.secondary,
                          borderRadius: BorderRadius.circular(8),
                          boxShadow: [
                            BoxShadow(
                              color: theme.colorScheme.shadow,
                              blurRadius: 20,
                              offset: const Offset(0, 4),
                            ),
                          ],
                        ),
                        child: SizedBox(
                          width: double.infinity,
                          child: appBar,
                        ),
                      ),
                      if (flexibleSpace != null) const SizedBox(height: 12),
                      if (flexibleSpace != null) flexibleSpace!,
                    ],
                  ),
                ),
              ),
            ],
          )),
    );
  }
}

enum BottomSheetState {
  full(1),
  expanded(0.5),
  collapsed(0);

  final double value;

  const BottomSheetState(this.value);
}

class StackedBottomSheetController extends ChangeNotifier {
  BottomSheetState _state;

  StackedBottomSheetController(
      {BottomSheetState initialState = BottomSheetState.expanded})
      : _state = initialState;

  BottomSheetState get state => _state;

  set state(BottomSheetState value) {
    if (value == _state) {
      return;
    }
    _state = value;
    notifyListeners();
  }
}

class StackedBottomSheet extends StatefulWidget {
  final Widget? appBar;
  final Widget? flexibleSpace;
  final Widget body;
  final Widget bottomSheet;
  final double expandedHeight;
  final double collapsedHeight;
  final Widget? floatingActionButton;
  final Widget? fullPage;
  final StackedBottomSheetController? controller;

  const StackedBottomSheet({
    super.key,
    this.appBar,
    required this.body,
    required this.bottomSheet,
    this.expandedHeight = 400,
    this.collapsedHeight = 60,
    this.floatingActionButton,
    this.fullPage,
    this.flexibleSpace,
    this.controller,
  });

  @override
  State<StackedBottomSheet> createState() => _StackedBottomSheetState();
}

class _StackedBottomSheetState extends State<StackedBottomSheet>
    with SingleTickerProviderStateMixin {
  late final AnimationController _controller;
  // 0 - is collapsed, 0.5 is expanded, 1 is full page
  late double _dragStart;
  bool _isAnimating = false;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 300),
      value: widget.controller?.state.value ?? 0.5,
    );
    _controller.addListener(() {
      if (widget.controller != null && !_isAnimating) {
        double value = _controller.value;
        if (value == 0) {
          widget.controller!.state = BottomSheetState.collapsed;
        } else if (value == 0.5) {
          widget.controller!.state = BottomSheetState.expanded;
        } else if (value == 1) {
          widget.controller!.state = BottomSheetState.full;
        }
      }
    });
    if (widget.controller != null) {
      widget.controller!.addListener(_listener);
    }
  }

  void _listener() {
    if (widget.controller != null) {
      // animate to state
      _isAnimating = true;
      _controller.animateTo(widget.controller!.state.value).then((value) {
        _isAnimating = false;
      });
    }
  }

  @override
  void didUpdateWidget(covariant StackedBottomSheet oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (widget.controller != oldWidget.controller) {
      oldWidget.controller?.removeListener(_listener);
      widget.controller?.addListener(_listener);
    }
    if (widget.fullPage == null) {
      if (_controller.value == 1) {
        _controller.animateTo(0.5);
      }
    }
  }

  @override
  void dispose() {
    _controller.dispose();
    if (widget.controller != null) {
      widget.controller!.removeListener(_listener);
    }
    super.dispose();
  }

  double calculateHeight(double maxHeight) {
    double value = _controller.value;
    if (value > 0.5 && value <= 1) {
      if (widget.fullPage == null) {
        value = 0.5;
      }
      // normalize value from 0.5 - 1
      double normalized = (value - 0.5) * 2;
      return lerpDouble(widget.expandedHeight, maxHeight, normalized)!;
    } else {
      return widget.expandedHeight;
    }
  }

  double calculateViewportHeight(double maxHeight) {
    double value = _controller.value;
    if (value >= 0 && value <= 0.5) {
      // normalize value from 0 - 0.5
      double normalized = value * 2;
      return lerpDouble(
          widget.collapsedHeight, widget.expandedHeight, normalized)!;
    } else {
      return widget.expandedHeight;
    }
  }

  double calculatePosition() {
    double value = _controller.value;
    if (value >= 0 && value < 0.5) {
      double normalized = value * 2;
      return lerpDouble(0, -(widget.expandedHeight - widget.collapsedHeight),
          1 - normalized)!;
    }
    return 0;
  }

  void increaseHeight(double delta, double maxHeight) {
    // delta is in pixel, normalize it first based on the current value
    // if value is 0.5-1, then normalize it to 0.5-1
    // if value is 0-0.5, then normalize it to 0-0.5
    double value = _controller.value;
    if (value >= 0 && value <= 0.5) {
      _controller.value -=
          (delta / (widget.expandedHeight - widget.collapsedHeight)) / 2;
    } else if (value > 0.5 && value <= 1) {
      _controller.value -= delta / (maxHeight - widget.expandedHeight) / 2;
    }
    if (widget.fullPage == null && _controller.value > 0.5) {
      _controller.value = 0.5;
    }
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return Scaffold(
      body: LayoutBuilder(builder: (context, constraints) {
        double maxHeight = constraints.maxHeight;
        // crossfade from expanded to full page
        return Stack(
          children: [
            // Positioned(
            //   top: 96,
            //   left: 0,
            //   right: 0,
            //   bottom: widget.expandedHeight,
            //   child: widget.body,
            // ),
            AnimatedBuilder(
              animation: _controller,
              builder: (context, child) {
                return Positioned(
                  top: 0,
                  left: 0,
                  right: 0,
                  bottom: 0,
                  child: widget.body,
                );
              },
            ),
            if (widget.appBar != null)
              Positioned(
                top: mediaPadding.top,
                left: 0,
                right: 0,
                child: Padding(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 12, vertical: 24),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(
                            vertical: 8, horizontal: 18),
                        decoration: BoxDecoration(
                          color: theme.colorScheme.secondary,
                          borderRadius: BorderRadius.circular(8),
                          boxShadow: [
                            BoxShadow(
                              color: theme.colorScheme.shadow,
                              blurRadius: 20,
                              offset: const Offset(0, 4),
                            ),
                          ],
                        ),
                        child: SizedBox(
                          width: double.infinity,
                          child: widget.appBar,
                        ),
                      ),
                      if (widget.flexibleSpace != null)
                        const SizedBox(height: 12),
                      if (widget.flexibleSpace != null) widget.flexibleSpace!,
                    ],
                  ),
                ),
              ),
            AnimatedBuilder(
                animation: _controller,
                child: Column(
                  children: [
                    Container(
                      width: 84,
                      height: 4,
                      decoration: BoxDecoration(
                        color: theme.colorScheme.muted,
                        borderRadius: BorderRadius.circular(2),
                      ),
                    ),
                    const SizedBox(height: 12),
                    Expanded(child: widget.bottomSheet),
                  ],
                ),
                builder: (context, child) {
                  double lowPoint = 0.75;
                  double highPoint = 1;
                  var controllerValue = _controller.value;
                  double crossfade =
                      (controllerValue - lowPoint) / (highPoint - lowPoint);
                  crossfade = crossfade.clamp(0, 1);
                  return AnimatedPositioned(
                    duration: const Duration(milliseconds: 50),
                    bottom: calculatePosition(),
                    left: 0,
                    right: 0,
                    child: Padding(
                        padding: EdgeInsets.symmetric(
                            horizontal: 12 * (1 - crossfade)),
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          crossAxisAlignment: CrossAxisAlignment.stretch,
                          children: [
                            if (widget.floatingActionButton != null)
                              Row(
                                mainAxisAlignment: MainAxisAlignment.end,
                                children: [
                                  widget.floatingActionButton!,
                                ],
                              ),
                            if (widget.floatingActionButton != null)
                              const SizedBox(height: 12),
                            GestureDetector(
                              onVerticalDragStart: (details) {
                                _dragStart = controllerValue;
                              },
                              onVerticalDragUpdate: (details) {
                                // _controller.value -=
                                //     details.delta.dy / maxHeight;
                                increaseHeight(details.delta.dy, maxHeight);
                              },
                              onVerticalDragEnd: (details) {
                                double value = controllerValue;
                                if (value > 0.75) {
                                  _controller.animateTo(1,
                                      curve: Curves.easeOut);
                                } else if (value > 0.25 && value <= 0.75) {
                                  _controller.animateTo(0.5,
                                      curve: Curves.easeOut);
                                } else if (value <= 0.25) {
                                  _controller.animateTo(0,
                                      curve: Curves.easeOut);
                                }
                              },
                              child: AnimatedContainer(
                                duration: const Duration(milliseconds: 50),
                                height: calculateHeight(maxHeight),
                                decoration: BoxDecoration(
                                  color: theme.colorScheme.secondary,
                                  borderRadius: BorderRadius.only(
                                    topLeft:
                                        Radius.circular(8 * (1 - crossfade)),
                                    topRight:
                                        Radius.circular(8 * (1 - crossfade)),
                                  ),
                                  boxShadow: [
                                    BoxShadow(
                                      color: theme.colorScheme.shadow,
                                      blurRadius: 20,
                                      offset: const Offset(0, -4),
                                    ),
                                  ],
                                ),
                                child: Stack(
                                  children: [
                                    Positioned.fill(
                                      child: Offstage(
                                        offstage: crossfade == 1 &&
                                            widget.fullPage != null,
                                        child: Opacity(
                                          opacity: widget.fullPage != null
                                              ? (1 - crossfade)
                                              : 1,
                                          child: Padding(
                                            padding: EdgeInsets.only(
                                                top: 8, left: 18, right: 18),
                                            child: child,
                                          ),
                                        ),
                                      ),
                                    ),
                                    if (widget.fullPage != null)
                                      Positioned.fill(
                                        child: Offstage(
                                          offstage: crossfade == 0,
                                          child: IgnorePointer(
                                            ignoring: crossfade != 1,
                                            child: Opacity(
                                              opacity: crossfade,
                                              child: NotificationListener(
                                                onNotification: (notification) {
                                                  if (notification
                                                      is OverscrollNotification) {
                                                    increaseHeight(
                                                        -notification
                                                            .overscroll,
                                                        maxHeight);
                                                  }
                                                  if (notification
                                                      is ScrollEndNotification) {
                                                    double value =
                                                        controllerValue;
                                                    if (value > 0.75) {
                                                      _controller.animateTo(1,
                                                          curve:
                                                              Curves.easeOut);
                                                    } else if (value > 0.25 &&
                                                        value <= 0.75) {
                                                      _controller.animateTo(0.5,
                                                          curve:
                                                              Curves.easeOut);
                                                    } else if (value <= 0.25) {
                                                      _controller.animateTo(0,
                                                          curve:
                                                              Curves.easeOut);
                                                    }
                                                  }
                                                  return false;
                                                },
                                                child: widget.fullPage!,
                                              ),
                                            ),
                                          ),
                                        ),
                                      ),
                                  ],
                                ),
                              ),
                            ),
                          ],
                        )),
                  );
                }),
          ],
        );
      }),
    );
  }
}

class ActionFeedbackPage extends StatelessWidget {
  final Widget icon;
  final Widget title;
  final Widget? subtitle;
  final List<Widget> action;
  final Color? backgroundColor;

  const ActionFeedbackPage({
    super.key,
    required this.icon,
    required this.title,
    this.subtitle,
    required this.action,
    this.backgroundColor,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return Scaffold(
      backgroundColor: backgroundColor ?? theme.colorScheme.primary,
      body: Padding(
        padding: const EdgeInsets.all(48.0) + mediaPadding,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            IconTheme(
              data: IconThemeData(
                size: 145,
                color: theme.colorScheme.primaryForeground,
              ),
              child: icon,
            ),
            const SizedBox(height: 24),
            DefaultTextStyle.merge(
              textAlign: TextAlign.center,
              style: theme.typography.xxl.copyWith(
                fontWeight: fontSemiBold,
                color: theme.colorScheme.primaryForeground,
              ),
              child: title,
            ),
            if (subtitle != null) const SizedBox(height: 4),
            if (subtitle != null)
              DefaultTextStyle.merge(
                textAlign: TextAlign.center,
                style: theme.typography.md.copyWith(
                  color: theme.colorScheme.primaryForeground,
                ),
                child: subtitle!,
              ),
            const SizedBox(height: 96),
            ...action,
          ],
        ),
      ),
    );
  }
}

Future<T> showLoading<T>(BuildContext context, Future<T> future,
    {VoidCallback? onDismissed}) {
  Future<dynamic> pushFuture = Navigator.of(context).push<T>(LoaderRoute(
    future: future,
    context: context,
    builder: (context) => const LoaderPage(),
  ));
  if (pushFuture is Future<MessageDataHolder?>) {
    pushFuture = pushFuture.handleErrorMessage(context);
  }
  pushFuture.whenComplete(() {
    if (onDismissed != null) {
      onDismissed();
    }
  });
  return future;
}

class LoaderRoute<T> extends DialogRoute<T> {
  final Future<T> future;
  final VoidCallback? onDismissed;

  LoaderRoute({
    required this.future,
    required BuildContext context,
    required super.builder,
    this.onDismissed,
  }) : super(context: context) {
    future.whenComplete(() {
      var of = Navigator.of(context);
      of.removeRoute(this);
    });
  }

  @override
  Duration get transitionDuration => const Duration(milliseconds: 300);

  @override
  bool get barrierDismissible => false;

  @override
  Color get barrierColor => Colors.black.withOpacity(0.5);

  @override
  String get barrierLabel => 'Memuat...';
}

class LoaderPage extends StatelessWidget {
  const LoaderPage({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return Center(
      child: Container(
        padding: const EdgeInsets.all(24) + mediaPadding,
        decoration: BoxDecoration(
          color: theme.colorScheme.secondary,
          borderRadius: BorderRadius.circular(12),
          boxShadow: [
            BoxShadow(
              color: theme.colorScheme.shadow,
              blurRadius: 20,
              offset: const Offset(0, 4),
            ),
          ],
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            CircularProgressIndicator(
              valueColor:
                  AlwaysStoppedAnimation(theme.colorScheme.secondaryForeground),
            ),
            const SizedBox(height: 24),
            DefaultTextStyle(
              style: theme.typography.md.copyWith(
                color: theme.colorScheme.secondaryForeground,
                decoration: TextDecoration.none,
              ),
              child: Text(intl(context).loading),
            ),
          ],
        ),
      ),
    );
  }
}

// page with slide transition from right to left
class SEIPage extends CustomTransitionPage {
  SEIPage({super.key, required super.child, required String name})
      : super(
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            final theme = SEITheme.of(context);
            return BackdropFilter(
              filter: ImageFilter.blur(
                  sigmaX: 3 * animation.value, sigmaY: 3 * animation.value),
              child: Container(
                color: Colors.black.withOpacity(animation.value),
                child: SlideTransition(
                  position: Tween<Offset>(
                    begin: const Offset(1, 0),
                    end: Offset.zero,
                  ).animate(CurvedAnimation(
                    parent: animation,
                    curve: Curves.easeInOut,
                  )),
                  child: Container(
                    color: theme.colorScheme.secondary,
                    child: ScaleTransition(
                      scale: Tween<double>(
                        begin: 1,
                        end: 0.9,
                      ).animate(CurvedAnimation(
                        parent: secondaryAnimation,
                        curve: Curves.easeInOut,
                      )),
                      child: child,
                    ),
                  ),
                ),
              ),
            );
          },
        );
}

class ProgressivePage extends StatefulWidget {
  final Widget? appBar;
  final List<List<Widget>>? children;
  final Widget Function(BuildContext context, int index, int tab)? builder;
  final List<int>? itemCount;
  final Future<void> Function()? onRefresh;
  final EdgeInsets? padding;
  final Widget? footer;
  final bool showTabBar;
  final PageController? controller;

  const ProgressivePage({
    super.key,
    this.appBar,
    this.children,
    this.itemCount,
    this.onRefresh,
    this.padding,
    this.builder,
    this.footer,
    this.showTabBar = true,
    this.controller,
  }) : assert(children != null || builder != null,
            'slivers or delegate must not be null');

  @override
  State<ProgressivePage> createState() => _ProgressivePageState();
}

class _ProgressivePageState extends State<ProgressivePage>
    with SingleTickerProviderStateMixin {
  late PageController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = widget.controller ?? PageController();
    _tabController.addListener(_update);
  }

  @override
  void didUpdateWidget(covariant ProgressivePage oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (widget.controller != oldWidget.controller) {
      oldWidget.controller?.removeListener(_update);
      widget.controller?.addListener(_update);
    }
  }

  void _update() {
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final mediaPadding = MediaQuery.paddingOf(context);
    return NonStackedSafeArea(
      child: ScrollConfiguration(
        behavior: PlainScrollConfiguration(),
        child: Scaffold(
          backgroundColor: theme.colorScheme.secondary,
          body: NestedScrollView(
            floatHeaderSlivers: true,
            clipBehavior: Clip.none,
            headerSliverBuilder: (context, innerBoxIsScrolled) {
              return [
                if (widget.appBar != null)
                  SliverOverlapAbsorber(
                    handle: NestedScrollView.sliverOverlapAbsorberHandleFor(
                        context),
                    sliver: SliverAppBar(
                      title: BackdropFilter(
                        filter: ImageFilter.blur(sigmaX: 5, sigmaY: 5),
                        child: Container(
                          height: 72 + mediaPadding.top,
                          padding: const EdgeInsets.symmetric(
                              vertical: 8, horizontal: 8),
                          child: Align(
                            alignment: Alignment.bottomCenter,
                            child: SizedBox(
                              width: double.infinity,
                              child: widget.appBar,
                            ),
                          ),
                        ),
                      ),
                      // tab pane
                      automaticallyImplyLeading: false,
                      toolbarHeight: 72 + mediaPadding.top,
                      surfaceTintColor: Colors.transparent,
                      backgroundColor:
                          theme.colorScheme.secondary.withOpacity(0.65),
                      floating: false,
                      pinned: true,
                    ),
                  ),
              ];
            },
            body: Builder(builder: (context) {
              return PageView(
                controller: _tabController,
                children: [
                  for (int i = 0;
                      i < (widget.children?.length ?? widget.itemCount!.length);
                      i++)
                    Builder(
                      builder: (context) {
                        Widget child = Column(
                          crossAxisAlignment: CrossAxisAlignment.stretch,
                          children: [
                            Expanded(
                              child: CustomScrollView(
                                slivers: [
                                  if (widget.appBar != null)
                                    SliverOverlapInjector(
                                      handle: NestedScrollView
                                          .sliverOverlapAbsorberHandleFor(
                                              context),
                                    ),
                                  SliverPadding(
                                    padding:
                                        (widget.padding ?? EdgeInsets.zero) +
                                            const EdgeInsets.only(
                                                bottom: 96 + 24 * 2),
                                    sliver: SliverList(
                                      delegate: SliverChildBuilderDelegate(
                                        // childCount: widget.children?.length ??
                                        //     widget.itemCount,
                                        childCount:
                                            widget.children?[i].length ??
                                                widget.itemCount![i],
                                        (context, index) {
                                          if (widget.builder != null) {
                                            return widget.builder!(
                                                context, index, i);
                                          }
                                          return widget.children![i][index];
                                        },
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                            if (widget.footer != null) widget.footer!,
                          ],
                        );
                        if (widget.onRefresh != null) {
                          child = RefreshIndicator(
                            onRefresh: widget.onRefresh!,
                            edgeOffset: 72 + mediaPadding.top,
                            displacement: 24,
                            child: child,
                          );
                        }
                        return child;
                      },
                    ),
                ],
              );
            }),
          ),
          extendBodyBehindAppBar: true,
          extendBody: true,
          bottomNavigationBar: SwipedTabBar(
            selectedIndex: _tabController.hasClients
                ? _tabController.page?.round() ?? 0
                : 0,
            totalTabs: (widget.children?.length ?? widget.itemCount!.length),
            onTabChanged: (value) {
              _tabController.animateToPage(value,
                  duration: Duration(milliseconds: 300),
                  curve: Curves.easeInOut);
            },
          ),
        ),
      ),
    );
  }
}

class SEIPageRoute extends PageRouteBuilder {
  final String name;
  final WidgetBuilder builder;

  SEIPageRoute({required this.builder, this.name = '?'})
      : super(
          pageBuilder: (context, animation, secondaryAnimation) =>
              builder(context),
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            final theme = SEITheme.of(context);
            return BackdropFilter(
              filter: ImageFilter.blur(
                  sigmaX: 3 * animation.value, sigmaY: 3 * animation.value),
              child: Container(
                color: Colors.black.withOpacity(animation.value),
                child: SlideTransition(
                  position: Tween<Offset>(
                    begin: const Offset(1, 0),
                    end: Offset.zero,
                  ).animate(CurvedAnimation(
                    parent: animation,
                    curve: Curves.easeInOut,
                  )),
                  child: Container(
                    color: theme.colorScheme.secondary,
                    child: ScaleTransition(
                      scale: Tween<double>(
                        begin: 1,
                        end: 0.9,
                      ).animate(CurvedAnimation(
                        parent: secondaryAnimation,
                        curve: Curves.easeInOut,
                      )),
                      child: child,
                    ),
                  ),
                ),
              ),
            );
          },
        );
}
