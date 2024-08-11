import 'package:flutter/widgets.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart' as gMapPlugin;
import 'package:latlong2/latlong.dart' as latLngPlugin;
import 'package:seioffice/src/service/environment.dart';

import 'flutter_map.dart' as fMap;
import 'google_map.dart' as gMap;

class LatLng {
  final double latitude;
  final double longitude;

  const LatLng(this.latitude, this.longitude);

  @override
  String toString() {
    return 'LatLng($latitude, $longitude)';
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;

    return other is LatLng &&
        other.latitude == latitude &&
        other.longitude == longitude;
  }

  @override
  int get hashCode {
    return latitude.hashCode ^ longitude.hashCode;
  }

  gMapPlugin.LatLng _toGoogleLatLng() {
    return gMapPlugin.LatLng(latitude, longitude);
  }

  latLngPlugin.LatLng _toFlutterLatLng() {
    return latLngPlugin.LatLng(latitude, longitude);
  }
}

AbstractLocalMapController _createLocalMapController() {
  if (kMapProvider == kMapProviderFlutterMap) {
    return fMap.LocalMapController();
  } else if (kMapProvider == kMapProviderGoogle) {
    return gMap.LocalMapController();
  } else {
    throw Exception('Map provider not found');
  }
}

class LocalMapController {
  final AbstractLocalMapController _delegate = _createLocalMapController();

  void fit() {
    _delegate.fit();
  }

  void center() {
    _delegate.center();
  }
}

abstract class AbstractLocalMapController {
  void fit();
  void center();
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
    if (kMapProvider == kMapProviderFlutterMap) {
      return fMap.StaticLiteMap(
          title: title,
          center: center._toFlutterLatLng(),
          radius: radius,
          userLocation: userLocation?._toFlutterLatLng());
    } else if (kMapProvider == kMapProviderGoogle) {
      return gMap.StaticLiteMap(
          title: title,
          center: center._toGoogleLatLng(),
          radius: radius,
          userLocation: userLocation?._toGoogleLatLng());
    } else {
      throw Exception('Map provider not found');
    }
  }
}

class FullPageMap extends StatelessWidget {
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
  Widget build(BuildContext context) {
    if (kMapProvider == kMapProviderFlutterMap) {
      return fMap.FullPageMap(
          title: title,
          center: center._toFlutterLatLng(),
          radius: radius,
          userLocation: userLocation?._toFlutterLatLng());
    } else if (kMapProvider == kMapProviderGoogle) {
      return gMap.FullPageMap(
          title: title,
          center: center._toGoogleLatLng(),
          radius: radius,
          userLocation: userLocation?._toGoogleLatLng());
    } else {
      throw Exception('Map provider not found');
    }
  }
}

class LocalMapData {
  final double centerLatitude;
  final double centerLongitude;
  final double radius;

  const LocalMapData({
    required this.centerLatitude,
    required this.centerLongitude,
    required this.radius,
  });
}

class LocalMap extends StatelessWidget {
  final LocalMapController? controller;
  final double centerLatitude;
  final double centerLongitude;
  final double radius;
  final EdgeInsets? padding;
  final ValueChanged<LatLng>? onPositionChanged;
  final Future<LocalMapData> Function()? init;
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
  Widget build(BuildContext context) {
    if (kMapProvider == kMapProviderFlutterMap) {
      return fMap.LocalMap(
        controller: controller?._delegate as fMap.LocalMapController?,
        centerLatitude: centerLatitude,
        centerLongitude: centerLongitude,
        radius: radius,
        padding: padding,
        onPositionChanged: onPositionChanged == null
            ? null
            : (latLng) {
                onPositionChanged!(LatLng(latLng.latitude, latLng.longitude));
              },
        init: init,
        onDismissed: onDismissed,
      );
    } else if (kMapProvider == kMapProviderGoogle) {
      return gMap.LocalMap(
        controller: controller?._delegate as gMap.LocalMapController?,
        centerLatitude: centerLatitude,
        centerLongitude: centerLongitude,
        radius: radius,
        padding: padding,
        onPositionChanged: onPositionChanged == null
            ? null
            : (latLng) {
                onPositionChanged!(LatLng(latLng.latitude, latLng.longitude));
              },
        init: init,
        onDismissed: onDismissed,
      );
    } else {
      throw Exception('Map provider not found');
    }
  }
}
