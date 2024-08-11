import 'dart:async';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/map/adaptive_map.dart' as adaptive;
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/theme/theme.dart';

class LocalMapController extends adaptive.AbstractLocalMapController {
  LocalMapState? _state;

  @override
  void fit() {
    _state?._fit();
  }

  @override
  void center() {
    _state?._center();
  }
}

class StaticLiteMap extends StatelessWidget {
  final LatLng center;
  final double radius;
  final LatLng? userLocation;
  final Widget title;

  const StaticLiteMap({
    required this.center,
    required this.radius,
    this.userLocation,
    required this.title,
  });

  static Future<void> waitForGoogleMap(GoogleMapController c) {
    return c.getVisibleRegion().then((value) {
      if (value.southwest.latitude != 0) {
        return Future.value();
      }

      return Future.delayed(const Duration(milliseconds: 100))
          .then((_) => waitForGoogleMap(c));
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          SEIPageRoute(
            builder: (context) => FullPageMap(
              center: center,
              radius: radius,
              userLocation: userLocation,
              title: title,
            ),
          ),
        );
      },
      child: AbsorbPointer(
        child: GoogleMap(
            mapToolbarEnabled: false,
            zoomControlsEnabled: false,
            myLocationButtonEnabled: false,
            rotateGesturesEnabled: false,
            myLocationEnabled: false,
            zoomGesturesEnabled: false,
            tiltGesturesEnabled: false,
            scrollGesturesEnabled: false,
            initialCameraPosition: CameraPosition(
              target: userLocation ?? center,
            ),
            cameraTargetBounds: userLocation != null
                ? CameraTargetBounds(
                    LatLngBounds(
                      southwest: LatLng(
                        min(center.latitude,
                            userLocation?.latitude ?? center.latitude),
                        min(center.longitude,
                            userLocation?.longitude ?? center.longitude),
                      ),
                      northeast: LatLng(
                        max(center.latitude,
                            userLocation?.latitude ?? center.latitude),
                        max(center.longitude,
                            userLocation?.longitude ?? center.longitude),
                      ),
                    ),
                  )
                : CameraTargetBounds.unbounded,
            onMapCreated: (controller) async {
              if (userLocation != null) {
                final bounds = LatLngBounds(
                  southwest: LatLng(
                    min(center.latitude,
                        userLocation?.latitude ?? center.latitude),
                    min(center.longitude,
                        userLocation?.longitude ?? center.longitude),
                  ),
                  northeast: LatLng(
                    max(center.latitude,
                        userLocation?.latitude ?? center.latitude),
                    max(center.longitude,
                        userLocation?.longitude ?? center.longitude),
                  ),
                );
                final cameraUpdate = CameraUpdate.newLatLngBounds(
                  bounds,
                  50,
                );
                waitForGoogleMap(controller).then((value) {
                  controller.moveCamera(cameraUpdate);
                });
              }
            },
            circles: {
              Circle(
                circleId: const CircleId('radius'),
                center: center,
                radius: radius,
                fillColor: theme.colorScheme.primary.withOpacity(0.5),
                strokeWidth: 2,
                strokeColor: theme.colorScheme.primary,
              ),
            },
            markers: {
              if (userLocation != null)
                Marker(
                  markerId: const MarkerId('user'),
                  position: userLocation!,
                  icon: BitmapDescriptor.defaultMarkerWithHue(
                      BitmapDescriptor.hueAzure),
                ),
              Marker(
                markerId: const MarkerId('center'),
                position: center,
                icon: BitmapDescriptor.defaultMarker,
              ),
            },
            polylines: {
              if (userLocation != null)
                Polyline(
                  polylineId: const PolylineId('route'),
                  points: [
                    userLocation!,
                    center,
                  ],
                  color: theme.colorScheme.primary,
                  width: 5,
                ),
            }
            // initialCameraPosition: CameraPosition(
            //   target: center,
            //   zoom: 15,
            // ),
            ),
      ),
    );
  }
}

// fullpagemap opens when staticlite map is clicked
class FullPageMap extends StatefulWidget {
  final LatLng center;
  final double radius;
  final LatLng? userLocation;
  final Widget title;

  const FullPageMap({
    required this.center,
    required this.radius,
    this.userLocation,
    required this.title,
  });

  @override
  State<FullPageMap> createState() => _FullPageMapState();
}

class _FullPageMapState extends State<FullPageMap> {
  final Completer<GoogleMapController> _controller = Completer();
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
      body: Stack(
        children: [
          GoogleMap(
            mapToolbarEnabled: false,
            zoomControlsEnabled: false,
            myLocationButtonEnabled: false,
            rotateGesturesEnabled: true,
            myLocationEnabled: false,
            zoomGesturesEnabled: true,
            tiltGesturesEnabled: true,
            scrollGesturesEnabled: true,
            initialCameraPosition: CameraPosition(
              target: widget.userLocation ?? widget.center,
              zoom: 15,
            ),
            onMapCreated: (controller) async {
              _controller.complete(controller);
              if (widget.userLocation != null) {
                final bounds = LatLngBounds(
                  southwest: LatLng(
                    min(
                        widget.center.latitude,
                        widget.userLocation?.latitude ??
                            widget.center.latitude),
                    min(
                        widget.center.longitude,
                        widget.userLocation?.longitude ??
                            widget.center.longitude),
                  ),
                  northeast: LatLng(
                    max(
                        widget.center.latitude,
                        widget.userLocation?.latitude ??
                            widget.center.latitude),
                    max(
                        widget.center.longitude,
                        widget.userLocation?.longitude ??
                            widget.center.longitude),
                  ),
                );
                final cameraUpdate = CameraUpdate.newLatLngBounds(
                  bounds,
                  50,
                );
                StaticLiteMap.waitForGoogleMap(controller).then((value) {
                  controller.moveCamera(cameraUpdate);
                });
              }
            },
            circles: {
              Circle(
                circleId: const CircleId('radius'),
                center: widget.center,
                radius: widget.radius,
                fillColor: theme.colorScheme.primary.withOpacity(0.5),
                strokeWidth: 2,
                strokeColor: theme.colorScheme.primary,
              ),
            },
            markers: {
              if (widget.userLocation != null)
                Marker(
                  markerId: const MarkerId('user'),
                  position: widget.userLocation!,
                  icon: BitmapDescriptor.defaultMarkerWithHue(
                      BitmapDescriptor.hueAzure),
                ),
              Marker(
                markerId: const MarkerId('center'),
                position: widget.center,
                icon: BitmapDescriptor.defaultMarker,
              ),
            },
            polylines: {
              if (widget.userLocation != null)
                Polyline(
                  polylineId: const PolylineId('route'),
                  points: [
                    widget.userLocation!,
                    widget.center,
                  ],
                  color: theme.colorScheme.primary,
                  width: 5,
                ),
            },
          ),
          Positioned(
            bottom: 16,
            left: 16,
            child: Row(
              children: [
                FloatingActionButton(
                  heroTag: 'focus-center',
                  onPressed: () {
                    final controller = _controller.future;
                    controller.then((value) {
                      value.animateCamera(
                          CameraUpdate.newLatLngZoom(widget.center, 18));
                    });
                  },
                  child: const Icon(Icons.center_focus_strong),
                ),
                if (widget.userLocation != null) ...[
                  const SizedBox(width: 16),
                  FloatingActionButton(
                    heroTag: 'focus-user',
                    onPressed: () {
                      final controller = _controller.future;
                      controller.then((value) {
                        value.animateCamera(CameraUpdate.newLatLngZoom(
                            widget.userLocation!, 18));
                      });
                    },
                    child: const Icon(Icons.gps_fixed),
                  ),
                  const SizedBox(width: 16),
                  // focus on both user and center
                  FloatingActionButton(
                    heroTag: 'focus-all',
                    onPressed: () {
                      final controller = _controller.future;
                      controller.then((value) {
                        final bounds = LatLngBounds(
                          southwest: LatLng(
                            min(widget.center.latitude,
                                widget.userLocation!.latitude),
                            min(widget.center.longitude,
                                widget.userLocation!.longitude),
                          ),
                          northeast: LatLng(
                            max(widget.center.latitude,
                                widget.userLocation!.latitude),
                            max(widget.center.longitude,
                                widget.userLocation!.longitude),
                          ),
                        );
                        value.animateCamera(CameraUpdate.newLatLngBounds(
                          bounds,
                          50,
                        ));
                      });
                    },
                    child: const Icon(Icons.zoom_out_map),
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class LocalMap extends StatefulWidget {
  final LocalMapController? controller;
  final double centerLatitude;
  final double centerLongitude;
  final double radius;
  final EdgeInsets? padding;
  final void Function(LatLng position)? onPositionChanged;
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
  final Completer<GoogleMapController> _controller = Completer();
  LatLng? _userLocation;
  late StreamSubscription<Position> _positionSubscription;
  late double _radius;
  late double _centerLatitude;
  late double _centerLongitude;

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
  void initState() {
    super.initState();
    _radius = widget.radius;
    _centerLatitude = widget.centerLatitude;
    _centerLongitude = widget.centerLongitude;
    widget.controller?._state = this;
    _positionSubscription = Geolocator.getPositionStream(
      locationSettings: const LocationSettings(
        accuracy: LocationAccuracy.high,
        distanceFilter: 1,
      ),
    ).listen((position) {
      debugPrint('position changed $position');
      if (widget.onPositionChanged != null) {
        widget
            .onPositionChanged!(LatLng(position.latitude, position.longitude));
        if (!mounted) return;
        setState(() {
          _userLocation = LatLng(position.latitude, position.longitude);
        });
      }
    }, onError: (error, stackTrace) {
      debugPrint('position error $error');
      debugPrint(stackTrace.toString());
    });
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
      _centerLatitude = widget.centerLatitude;
      _centerLongitude = widget.centerLongitude;
      _radius = widget.radius;
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

  @override
  void dispose() {
    widget.controller?._state = null;
    _positionSubscription.cancel();
    super.dispose();
  }

  Future<void> _fit() async {
    final controller = await _controller.future;
    if (!mounted) return;
    final position = _userLocation;
    if (position == null) {
      return;
    }
    setState(() {
      double minLatitude = min(position.latitude, _centerLatitude);
      double maxLatitude = max(position.latitude, _centerLatitude);
      double minLongitude = min(position.longitude, _centerLongitude);
      double maxLongitude = max(position.longitude, _centerLongitude);
      controller.animateCamera(CameraUpdate.newLatLngBounds(
        LatLngBounds(
          southwest: LatLng(minLatitude, minLongitude),
          northeast: LatLng(maxLatitude, maxLongitude),
        ),
        100,
      ));
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
    final controller = await _controller.future;
    if (!mounted) return;
    setState(() {
      // _userLocation = LatLng(position.latitude, position.longitude);
      // _mapController._mapController.move(_userLocation!, 15);
      controller.animateCamera(CameraUpdate.newLatLng(
        LatLng(position.latitude, position.longitude),
      ));
    });
    return;
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GoogleMap(
      padding: widget.padding ?? EdgeInsets.zero,
      mapToolbarEnabled: false,
      zoomControlsEnabled: false,
      myLocationButtonEnabled: false,
      circles: {
        Circle(
          circleId: const CircleId('radius'),
          center: LatLng(_centerLatitude, _centerLongitude),
          radius: _radius,
          fillColor: theme.colorScheme.primary.withOpacity(0.5),
          strokeWidth: 2,
          strokeColor: theme.colorScheme.primary,
        ),
      },
      markers: {
        if (_userLocation != null)
          Marker(
            markerId: const MarkerId('user'),
            position: _userLocation!,
            icon: BitmapDescriptor.defaultMarkerWithHue(
                BitmapDescriptor.hueAzure),
          ),
        Marker(
          markerId: const MarkerId('center'),
          position: LatLng(_centerLatitude, _centerLongitude),
          icon: BitmapDescriptor.defaultMarker,
        ),
      },
      polylines: {
        if (_userLocation != null)
          Polyline(
            polylineId: const PolylineId('route'),
            points: [
              _userLocation!,
              LatLng(_centerLatitude, _centerLongitude),
            ],
            color: theme.colorScheme.primary,
            width: 5,
          ),
      },
      initialCameraPosition: CameraPosition(
        target: LatLng(_centerLatitude, _centerLongitude),
        zoom: 15,
      ),
      onMapCreated: (controller) async {
        _controller.complete(controller);
      },
    );
  }
}
