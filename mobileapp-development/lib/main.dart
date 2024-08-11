import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_native_splash/flutter_native_splash.dart';
import 'package:google_maps_flutter_android/google_maps_flutter_android.dart';
import 'package:google_maps_flutter_platform_interface/google_maps_flutter_platform_interface.dart';
import 'package:seioffice/src/components/app.dart';
import 'package:seioffice/src/service/environment.dart';
import 'package:seioffice/src/service/service.dart';

/// Entry point untuk aplikasi
late final String kGoogleMapDarkStyle;
void main() async {
  WidgetsBinding binding = WidgetsFlutterBinding.ensureInitialized();
  FlutterNativeSplash.preserve(widgetsBinding: binding);
  // Pastikan environment variable sudah di-set
  // Cek di file .env/{environment}.env
  assert(kEnv != kEnvUnknown, 'Please set the environment variable');
  debugPrint('Environment: $kEnv');
  runApp(DefaultServiceDataRegistry(
    child: const SEIOfficeApp(),
    onInit: () async {
      if (kMapProvider == kMapProviderGoogle) {
        kGoogleMapDarkStyle =
            await rootBundle.loadString('lib/assets/maps/dark.json');
        final mapsImplementation = GoogleMapsFlutterPlatform.instance;
        if (mapsImplementation is GoogleMapsFlutterAndroid) {
          debugPrint('using Android View Surface');
          mapsImplementation.useAndroidViewSurface = true;
          // try {
          //   await mapsImplementation
          //       .initializeWithRenderer(AndroidMapRenderer.latest);
          // } catch (e) {
          //   debugPrint('Failed to initialize Google Maps: $e');
          // }
        }
      }
      FlutterNativeSplash.remove();
    },
  ));
}
