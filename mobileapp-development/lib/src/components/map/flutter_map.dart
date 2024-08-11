import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:flutter_map_cancellable_tile_provider/flutter_map_cancellable_tile_provider.dart';
import 'package:geolocator/geolocator.dart';
import 'package:latlong2/latlong.dart';
import 'package:seioffice/src/components/map/adaptive_map.dart' as adaptive;
import 'package:seioffice/src/theme/color_filter_generator.dart';
import 'package:seioffice/src/theme/theme.dart';

import '../header.dart';
import '../page.dart';

class LocalMapController extends adaptive.AbstractLocalMapController {
  LocalMapState? _state;
  final MapController _mapController = MapController();

  @override
  void fit() {
    _state?._fit();
  }

  @override
  void center() {
    _state?._center();
  }
}

class LocalMap extends StatefulWidget {
  final LocalMapController? controller;
  final double centerLatitude;
  final double centerLongitude;
  final double radius;
  final EdgeInsets? padding;
  final ValueChanged<LatLng>? onPositionChanged;
  final Future<adaptive.LocalMapData> Function()? init;
  final VoidCallback? onDismissed;

  const LocalMap({
    super.key,
    this.controller,
    required this.centerLatitude,
    required this.centerLongitude,
    required this.radius,
    this.padding,
    this.onPositionChanged,
    this.init,
    this.onDismissed,
  });

  @override
  State<LocalMap> createState() => LocalMapState();
}

class LocalMapState extends State<LocalMap> {
  late LocalMapController _mapController;
  LatLng? _userLocation;
  late StreamSubscription<Position> _positionStream;

  @override
  void initState() {
    super.initState();
    _positionStream = Geolocator.getPositionStream(
      locationSettings: const LocationSettings(
        accuracy: LocationAccuracy.best,
        distanceFilter: 1,
      ),
    ).listen((position) {
      if (!mounted) return;
      setState(() {
        _userLocation = LatLng(position.latitude, position.longitude);
        widget.onPositionChanged?.call(_userLocation!);
      });
    }, onError: (error, stackTrace) {
      debugPrint('Error: $error');
      debugPrint(stackTrace);
    });
    _mapController = widget.controller ?? LocalMapController();
    _mapController._state = this;
    _fit();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) async {
      showLoading(
          context,
          hasAccess().then((value) async {
            if (!value) {
              widget.onDismissed?.call();
              return;
            }
            await _updateUserLocation();
            WidgetsBinding.instance.addPostFrameCallback((timeStamp) async {
              await _fit();
            });
            if (widget.init != null) {
              await widget.init!();
            }
            print('inited');
          }).onError((error, stackTrace) {
            print(error);
            print(stackTrace);
            widget.onDismissed?.call();
          }),
          onDismissed: widget.onDismissed);
    });
  }

  Future<void> _updateUserLocation() {
    return Geolocator.getCurrentPosition(
      desiredAccuracy: LocationAccuracy.high,
      // timeLimit: const Duration(seconds: 5),
    ).then((position) {
      if (!mounted) return;
      setState(() {
        _userLocation = LatLng(position.latitude, position.longitude);
      });
    }).onError((error, stackTrace) {
      debugPrint('position error $error');
      debugPrint(stackTrace.toString());
    });
  }

  @override
  void didUpdateWidget(covariant LocalMap oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.controller != widget.controller) {
      oldWidget.controller?._state = null;
      widget.controller?._state = this;
    }
    if (oldWidget.centerLatitude != widget.centerLatitude ||
        oldWidget.centerLongitude != widget.centerLongitude ||
        oldWidget.radius != widget.radius) {
      WidgetsBinding.instance.addPostFrameCallback((timeStamp) async {
        showLoading(
            context,
            hasAccess().then((value) async {
              await _fit();
              if (widget.init != null) {
                await widget.init!();
              }
            }));
      });
    }
  }

  // @override
  // void didUpdateWidget(covariant LocalMap oldWidget) {
  //   super.didUpdateWidget(oldWidget);
  //   if (oldWidget.controller != widget.controller) {
  //     _mapController = widget.controller ?? LocalMapController();
  //     _mapController._state = this;
  //     oldWidget.controller?._state = null;
  //   }
  // }

  @override
  void dispose() {
    _mapController._state = null;
    _positionStream.cancel();
    super.dispose();
  }

  Future<void> _fit() async {
    bool serviceEnabled;
    LocationPermission permission;
    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      return;
    }
    permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        return;
      }
    }
    if (permission == LocationPermission.deniedForever) {
      return;
    }
    final position = await Geolocator.getCurrentPosition();
    if (!mounted) return;
    setState(() {
      _userLocation = LatLng(position.latitude, position.longitude);
      _mapController._mapController.move(_userLocation!, 20);
      _mapController._mapController.fitCamera(
        CameraFit.bounds(
          bounds: LatLngBounds(
            LatLng(
              widget.centerLatitude,
              widget.centerLongitude,
            ),
            LatLng(
              position.latitude,
              position.longitude,
            ),
          ),
          minZoom: 15,
          maxZoom: 20,
          // padding: EdgeInsets.all(100),
          padding: widget.padding ?? const EdgeInsets.all(100),
        ),
      );
    });
    return;
  }

  Future<void> _center() async {
    bool serviceEnabled;
    LocationPermission permission;
    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      return;
    }
    permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        return;
      }
    }
    if (permission == LocationPermission.deniedForever) {
      return;
    }
    final position = await Geolocator.getCurrentPosition();
    if (!mounted) return;
    setState(() {
      _userLocation = LatLng(position.latitude, position.longitude);
      _mapController._mapController.move(_userLocation!, 15);
    });
    return;
  }

  Future<bool> hasAccess() async {
    bool serviceEnabled;
    LocationPermission permission;
    serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      return false;
    }
    permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        return false;
      }
    }
    if (permission == LocationPermission.deniedForever) {
      return false;
    }
    return true;
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return FlutterMap(
      mapController: _mapController._mapController,
      options: MapOptions(
        initialCenter: LatLng(widget.centerLatitude, widget.centerLongitude),
        initialZoom: 15,
        minZoom: 15,
        maxZoom: 20,
        backgroundColor: theme.colorScheme.secondary,
      ),
      children: [
        Container(
          color: theme.colorScheme.primaryForeground,
        ),
        ColorFiltered(
          colorFilter: createColorFilter(theme),
          child: TileLayer(
            tileProvider: CancellableNetworkTileProvider(),
            urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
          ),
        ),
        CircleLayer(circles: [
          CircleMarker(
            point: LatLng(widget.centerLatitude, widget.centerLongitude),
            color: theme.colorScheme.primary.withOpacity(0.5),
            borderColor: theme.colorScheme.primary,
            borderStrokeWidth: 2,
            radius: widget.radius,
            useRadiusInMeter: true,
          ),
        ]),
        MarkerLayer(
          markers: [
            if (_userLocation != null)
              Marker(
                width: 80.0,
                height: 80.0,
                point: _userLocation!,
                child: const Icon(
                  Icons.location_on,
                  color: Colors.red,
                  size: 48,
                ),
              ),
            Marker(
              width: 80.0,
              height: 80.0,
              point: LatLng(widget.centerLatitude, widget.centerLongitude),
              child: Icon(
                Icons.circle,
                color: theme.colorScheme.primary,
                size: 12,
              ),
            ),
          ],
        ),
      ],
    );
  }
}

class StaticLiteMap extends StatelessWidget {
  final LatLng center;
  final double radius;
  final LatLng? userLocation;
  final Widget title;

  const StaticLiteMap({
    super.key,
    required this.center,
    required this.radius,
    this.userLocation,
    required this.title,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      onTap: () {
        Navigator.push(context, SEIPageRoute(
          builder: (context) {
            return FullPageMap(
              title: title,
              center: center,
              radius: radius,
              userLocation: userLocation,
            );
          },
        ));
      },
      child: AbsorbPointer(
        child: FlutterMap(
          options: MapOptions(
            initialCenter: center,
            initialZoom: 15,
            minZoom: 15,
            maxZoom: 20,
            backgroundColor: theme.colorScheme.secondary,
          ),
          children: [
            Container(
              color: theme.colorScheme.primaryForeground,
            ),
            ColorFiltered(
              colorFilter: createColorFilter(theme),
              child: TileLayer(
                tileProvider: CancellableNetworkTileProvider(),
                urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
              ),
            ),
            CircleLayer(circles: [
              CircleMarker(
                point: center,
                color: theme.colorScheme.primary.withOpacity(0.5),
                borderColor: theme.colorScheme.primary,
                borderStrokeWidth: 2,
                radius: radius,
                useRadiusInMeter: true,
              ),
            ]),
            MarkerLayer(
              markers: [
                if (userLocation != null)
                  Marker(
                    width: 80.0,
                    height: 80.0,
                    point: userLocation!,
                    child: const Icon(
                      Icons.location_on,
                      color: Colors.red,
                      size: 48,
                    ),
                  ),
                Marker(
                  width: 80.0,
                  height: 80.0,
                  point: center,
                  child: Icon(
                    Icons.circle,
                    color: theme.colorScheme.primary,
                    size: 12,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

// A full page of a static lite map, but with FloatingActionButton to center the map
class FullPageMap extends StatefulWidget {
  final LatLng center;
  final double radius;
  final LatLng? userLocation;
  final Widget title;
  const FullPageMap({
    super.key,
    required this.center,
    required this.radius,
    this.userLocation,
    required this.title,
  });

  @override
  State<FullPageMap> createState() => _FullPageMapState();
}

class _FullPageMapState extends State<FullPageMap> {
  late MapController _mapController;

  @override
  void initState() {
    super.initState();
    _mapController = MapController();
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return StackedPage(
      appBar: Header(
        title: widget.title,
        leading: IconButton(
          onPressed: () {
            Navigator.pop(context);
          },
          icon: const Icon(Icons.arrow_back),
        ),
      ),
      body: Stack(children: [
        FlutterMap(
          mapController: _mapController,
          options: MapOptions(
            initialCenter: widget.center,
            initialZoom: 15,
            minZoom: 15,
            maxZoom: 20,
            backgroundColor: theme.colorScheme.secondary,
          ),
          children: [
            Container(
              color: theme.colorScheme.primaryForeground,
            ),
            ColorFiltered(
              colorFilter: createColorFilter(theme),
              child: TileLayer(
                tileProvider: CancellableNetworkTileProvider(),
                urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
              ),
            ),
            CircleLayer(circles: [
              CircleMarker(
                point: widget.center,
                color: theme.colorScheme.primary.withOpacity(0.5),
                borderColor: theme.colorScheme.primary,
                borderStrokeWidth: 2,
                radius: widget.radius,
                useRadiusInMeter: true,
              ),
            ]),
            MarkerLayer(
              markers: [
                if (widget.userLocation != null)
                  Marker(
                    width: 80.0,
                    height: 80.0,
                    point: widget.userLocation!,
                    child: const Icon(
                      Icons.location_on,
                      color: Colors.red,
                      size: 48,
                    ),
                  ),
                Marker(
                  width: 80.0,
                  height: 80.0,
                  point: widget.center,
                  child: Icon(
                    Icons.circle,
                    color: theme.colorScheme.primary,
                    size: 12,
                  ),
                ),
              ],
            ),
          ],
        ),
        Positioned(
          bottom: 16,
          left: 16,
          child: Row(
            children: [
              FloatingActionButton(
                heroTag: 'focus-center',
                onPressed: () {
                  // final controller = _controller.future;
                  // controller.then((value) {
                  //   value.animateCamera(
                  //       CameraUpdate.newLatLngZoom(widget.center, 18));
                  // });
                  _mapController.move(widget.center, 18);
                },
                child: const Icon(Icons.center_focus_strong),
              ),
              if (widget.userLocation != null) ...[
                const SizedBox(width: 16),
                FloatingActionButton(
                  heroTag: 'focus-user',
                  onPressed: () {
                    _mapController.move(widget.userLocation!, 18);
                  },
                  child: const Icon(Icons.gps_fixed),
                ),
                const SizedBox(width: 16),
                // focus on both user and center
                FloatingActionButton(
                  heroTag: 'focus-all',
                  onPressed: () {
                    // final controller = _controller.future;
                    // controller.then((value) {
                    //   final bounds = LatLngBounds(
                    //     southwest: LatLng(
                    //       min(widget.center.latitude,
                    //           widget.userLocation!.latitude),
                    //       min(widget.center.longitude,
                    //           widget.userLocation!.longitude),
                    //     ),
                    //     northeast: LatLng(
                    //       max(widget.center.latitude,
                    //           widget.userLocation!.latitude),
                    //       max(widget.center.longitude,
                    //           widget.userLocation!.longitude),
                    //     ),
                    //   );
                    //   value.animateCamera(CameraUpdate.newLatLngBounds(
                    //     bounds,
                    //     50,
                    //   ));
                    // });
                    _mapController.fitCamera(
                      CameraFit.bounds(
                        bounds: LatLngBounds(
                          widget.center,
                          widget.userLocation!,
                        ),
                        minZoom: 15,
                        maxZoom: 20,
                        padding: const EdgeInsets.all(100),
                      ),
                    );
                  },
                  child: const Icon(Icons.zoom_out_map),
                ),
              ],
            ],
          ),
        ),
      ]),
    );
  }
}

ColorFilter createColorFilter(SEITheme theme) {
  return (theme.brightness == Brightness.dark
          ? ColorFilterGenerator()
              .applyContrast(0.7)
              .applyInvert()
              .applyColorize(theme.colorScheme.primary.invert)
              .applySaturate(0.5)
          : ColorFilterGenerator().applyColorize(theme.colorScheme.primary))
      .generate();
}
