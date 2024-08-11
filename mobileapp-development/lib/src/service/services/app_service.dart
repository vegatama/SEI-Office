import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:seioffice/src/service/environment.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AppService extends Service {
  late ValueNotifier<SEITheme> theme;
  late ValueNotifier<Locale> locale;
  final Dio dio = Dio();
  @override
  Future<void> init() async {
    super.init();
    var prefs = await SharedPreferences.getInstance();
    ThemeMode mode = ThemeMode.values[prefs.getInt('themeMode') ?? 1];
    locale = ValueNotifier(Locale(prefs.getString('langCode') ?? 'id',
        prefs.getString('langCountryCode') ?? 'ID'));
    if (mode == ThemeMode.system) {
      mode = ThemeMode.light;
    }
    // theme = ValueNotifier(SEITheme(
    //   typography: SEITypography.defaultTypography(),
    //   monoTypography: SEITypography.monoTypography(),
    //   colorScheme: SEIColorScheme.darkColorScheme(),
    //   brightness: Brightness.dark,
    // ));
    if (mode == ThemeMode.light) {
      theme = ValueNotifier(SEITheme(
        typography: SEITypography.defaultTypography(),
        monoTypography: SEITypography.monoTypography(),
        colorScheme: SEIColorScheme.defaultColorScheme(),
        brightness: Brightness.light,
      ));
    } else {
      theme = ValueNotifier(SEITheme(
        typography: SEITypography.defaultTypography(),
        monoTypography: SEITypography.monoTypography(),
        colorScheme: SEIColorScheme.darkColorScheme(),
        brightness: Brightness.dark,
      ));
    }
  }

  ThemeMode get themeMode {
    if (theme.value.brightness == Brightness.light) {
      return ThemeMode.light;
    } else {
      return ThemeMode.dark;
    }
  }

  void toggleTheme() {
    if (theme.value.brightness == Brightness.light) {
      switchTheme(ThemeMode.dark);
    } else {
      switchTheme(ThemeMode.light);
    }
  }

  void toggleLanguage() {
    var prefs = SharedPreferences.getInstance();
    if (locale.value.languageCode == 'id') {
      locale.value = const Locale('en', 'US');
      prefs.then((value) {
        value.setString('langCode', 'en');
        value.setString('langCountryCode', 'US');
      });
    } else {
      locale.value = const Locale('id', 'ID');
      prefs.then((value) {
        value.setString('langCode', 'id');
        value.setString('langCountryCode', 'ID');
      });
    }
  }

  Future<void> switchTheme(ThemeMode mode) async {
    if ((mode == ThemeMode.light || mode == ThemeMode.system) &&
        theme.value.brightness != Brightness.light) {
      theme.value = SEITheme(
        typography: SEITypography.defaultTypography(),
        monoTypography: SEITypography.monoTypography(),
        colorScheme: SEIColorScheme.defaultColorScheme(),
        brightness: Brightness.light,
      );
      debugPrint('switching to light theme');
      final prefs = await SharedPreferences.getInstance();
      prefs.setInt('themeMode', mode.index);
    } else if (mode == ThemeMode.dark &&
        theme.value.brightness != Brightness.dark) {
      theme.value = SEITheme(
        typography: SEITypography.defaultTypography(),
        monoTypography: SEITypography.monoTypography(),
        colorScheme: SEIColorScheme.darkColorScheme(),
        brightness: Brightness.dark,
      );
      debugPrint('switching to dark theme');
      final prefs = await SharedPreferences.getInstance();
      prefs.setInt('themeMode', mode.index);
    }
  }

  Future<T> request<T extends APIResponse>(APIRequest<T> request) async {
    var userController = get<UserService>();
    var baseUrl = kAPIBaseUrl;
    assert(baseUrl.isNotEmpty, 'API base URL is not set');
    Uri uri = Uri.parse(baseUrl);
    debugPrint('Requesting: ${request.path}');
    // with path
    String normalizedPath = request.path;
    if (normalizedPath.startsWith('/')) {
      normalizedPath = normalizedPath.substring(1);
    }
    if (normalizedPath.endsWith('/')) {
      normalizedPath = normalizedPath.substring(0, normalizedPath.length - 1);
    }
    String normalizedUriPath = uri.path;
    if (normalizedUriPath.startsWith('/')) {
      normalizedUriPath = normalizedUriPath.substring(1);
    }
    if (normalizedUriPath.endsWith('/')) {
      normalizedUriPath =
          normalizedUriPath.substring(0, normalizedUriPath.length - 1);
    }
    Map<String, dynamic>? queryParameters = request.queryParameters;
    Map<String, dynamic> safeQueryParameters = {};
    if (queryParameters != null) {
      for (var key in queryParameters.keys) {
        if (queryParameters[key] != null) {
          safeQueryParameters[key] = queryParameters[key].toString();
        }
      }
    }
    uri = uri.replace(
        path: '$normalizedUriPath/$normalizedPath',
        queryParameters:
            safeQueryParameters.isNotEmpty ? safeQueryParameters : null);
    Object? body;
    if (request.isMultipart) {
      var requestBody = request.body;
      FormData formData = FormData.fromMap({
        if (requestBody != null && requestBody.isNotEmpty) ...requestBody,
      });
      body = formData;
    } else {
      body = request.body;
    }
    var response = await dio.request(
      uri.toString(),
      data: body,
      options: Options(
        validateStatus: (status) {
          return true;
        },
        method: request.method.name,
        headers: {
          ...?request.headers,
          if (userController.user.value != null)
            'Authorization': 'Bearer ${userController.user.value!.token}',
          if (request.isMultipart)
            'Content-Type': 'multipart/form-data'
          else if (body != null)
            'Content-Type': 'application/json',
        },
      ),
    );
    var data = response.data;
    try {
      if (data is! Map<String, dynamic>) {
        throw Exception('Response is not a map: $data');
      }
      var parseResponse = request.parseResponse(data);
      return parseResponse as T;
    } catch (e, s) {
      debugPrint('cannot parse: ${data}');
      debugPrint('error parsing response: $e');
      debugPrint('stacktrace: $s');
      debugPrint('response: ${data}');
      rethrow;
    }
  }
}
