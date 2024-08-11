import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/animation.dart';
import 'package:seioffice/src/components/session_wrapper.dart';
import 'package:seioffice/src/service/controller.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/app_service.dart';

import '../service/controllers.dart';
import '../service/localization.dart';

/// Widget utama untuk aplikasi
class SEIOfficeApp extends StatefulWidget {
  const SEIOfficeApp({super.key});

  @override
  State<SEIOfficeApp> createState() => _SEIOfficeAppState();
}

class _SEIOfficeAppState extends State<SEIOfficeApp> {
  late GoRouter _router;

  @override
  void initState() {
    super.initState();
    _router = buildRouter(controllers);
  }

  @override
  Widget build(BuildContext context) {
    AppService appController = getService<AppService>();
    return ValueListenableBuilder(
        valueListenable: appController.theme,
        builder: (context, theme, child) {
          var themeData = ThemeData(
            useMaterial3: true,
            colorScheme:
                ColorScheme.fromSeed(seedColor: theme.colorScheme.primary),
            textTheme: theme.brightness == Brightness.dark
                ? Typography.material2018().white
                : Typography.material2018().black,
            datePickerTheme: DatePickerThemeData(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
            ),
            timePickerTheme: TimePickerThemeData(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(12),
              ),
            ),
            floatingActionButtonTheme: FloatingActionButtonThemeData(
              backgroundColor: theme.colorScheme.secondary,
              foregroundColor: theme.colorScheme.secondaryForeground,
              focusColor: Colors.transparent,
              splashColor: Colors.transparent,
              hoverColor: Colors.transparent,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(50),
              ),
            ),
          );
          return ValueListenableBuilder(
              valueListenable: appController.locale,
              builder: (context, locale, child) {
                return MaterialApp.router(
                  title: 'SEI Office',
                  routerConfig: _router,
                  themeAnimationDuration: Duration.zero,
                  themeMode: appController.themeMode,
                  theme: themeData,
                  debugShowCheckedModeBanner: false,
                  darkTheme: themeData,
                  locale: locale,
                  color: theme.colorScheme.primary,
                  supportedLocales: const [
                    Locale('id', 'ID'),
                    Locale('en', 'US'),
                  ],
                  localeResolutionCallback: (locale, supportedLocales) {
                    if (locale != null) {
                      for (var supportedLocale in supportedLocales) {
                        if (supportedLocale.languageCode ==
                                locale.languageCode &&
                            supportedLocale.countryCode == locale.countryCode) {
                          return supportedLocale;
                        }
                      }
                    }
                    return supportedLocales.first;
                  },
                  localizationsDelegates: const [
                    SEILocalizations.delegate,
                    GlobalMaterialLocalizations.delegate,
                    GlobalCupertinoLocalizations.delegate,
                    GlobalWidgetsLocalizations.delegate,
                  ],
                  builder: (context, child) {
                    return Container(
                      color: theme.colorScheme.primary,
                      child: AnimatedSEITheme(
                        data: theme,
                        child: DefaultTextStyle.merge(
                          style: theme.typography.md.copyWith(
                            color: theme.colorScheme.secondaryForeground,
                          ),
                          child: AnimatedSEILocalizations(
                            data: context.intl,
                            duration: Duration(milliseconds: 500),
                            // duration: Duration(seconds: 5),
                            child: SessionWrapper(
                              router: _router,
                              child: child ?? Container(),
                            ),
                          ),
                        ),
                      ),
                    );
                  },
                );
              });
        });
  }
}
