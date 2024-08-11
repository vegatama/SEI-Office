import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/dialog.dart';
import 'package:seioffice/src/components/divider.dart';
import 'package:seioffice/src/components/input.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/theme/theme.dart';

import '../../service/services/app_service.dart';
import '../../service/environment.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final FocusNode _emailFocus = FocusNode();
  final FocusNode _passwordFocus = FocusNode();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final GoogleSignIn _googleSignIn = GoogleSignIn(
    serverClientId: kGoogleClientId,
    scopes: [
      'email',
    ],
  );
  bool _passwordVisible = false;

  @override
  void initState() {
    super.initState();
    _emailController.addListener(() {
      setState(() {});
    });
    _passwordController.addListener(() {
      setState(() {});
    });
  }

  void dispatchLogin() {
    showLoading(
        context,
        getService<UserService>()
            .login(
                email: _emailController.text,
                password: _passwordController.text)
            .onError((error, stackTrace) {
          if (kDebugMode) {
            print('error: $error');
            print(stackTrace);
          }
          showDialog(
            context: context,
            builder: (context) {
              return PopupDialog(
                content: Text(intl.loginDialogFailedMessage),
                title: Text(intl.loginDialogFailedTitle),
                actions: [
                  PrimaryButton(
                    child: Text(intl.buttonOk),
                    onPressed: () {
                      context.pop();
                    },
                  )
                ],
              );
            },
          );
        }).then((value) {
          context.goNamed(kPageDashboard);
        }));
  }

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final appController = getService<AppService>();
    return Scaffold(
      backgroundColor: theme.colorScheme.secondary,
      body: SingleChildScrollView(
        child: Container(
          padding: EdgeInsets.symmetric(horizontal: 32, vertical: 48),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Center(
                child: Image.asset(
                  'lib/assets/images/logo_green.png',
                  height: 200,
                ),
              ),
              SizedBox(
                height: 24,
              ),
              Text(intl.loginTitle,
                  style: theme.typography.x5l.copyWith(
                    fontWeight: FontWeight.bold,
                    color: theme.colorScheme.secondaryForeground,
                  )),
              Text(intl.loginSubtitle,
                  style: theme.typography.xl.copyWith(
                    color: theme.colorScheme.subtitleForeground,
                  )),
              SizedBox(height: 24),
              StylizedTextField(
                child: TextField(
                  autofillHints: const [AutofillHints.email],
                  decoration: InputDecoration(
                    hintText: intl.loginHintEmail,
                    prefixIcon: Icon(Icons.email),
                  ),
                  controller: _emailController,
                  focusNode: _emailFocus,
                  onSubmitted: (value) {
                    _passwordFocus.requestFocus();
                  },
                ),
              ),
              SizedBox(
                height: 12,
              ),
              StylizedTextField(
                child: TextField(
                  focusNode: _passwordFocus,
                  obscureText: !_passwordVisible,
                  controller: _passwordController,
                  autofillHints: const [AutofillHints.password],
                  onSubmitted: (value) {
                    dispatchLogin();
                  },
                  decoration: InputDecoration(
                    hintText: intl.loginHintPassword,
                    prefixIcon: Icon(Icons.lock),
                    floatingLabelBehavior: FloatingLabelBehavior.never,
                    suffixIcon: IconButton(
                      padding: EdgeInsets.zero,
                      onPressed: () {
                        setState(() {
                          _passwordVisible = !_passwordVisible;
                        });
                      },
                      icon: Icon(_passwordVisible
                          ? Icons.visibility_off
                          : Icons.visibility),
                    ),
                  ),
                ),
              ),
              SizedBox(height: 48),
              PrimaryButton(
                onPressed: _emailController.text.isEmpty ||
                        _passwordController.text.isEmpty
                    ? null
                    : dispatchLogin,
                child: Text(intl.loginButtonLogin, textAlign: TextAlign.center),
              ),
              SizedBox(height: 12),
              Padding(
                padding: EdgeInsets.symmetric(horizontal: 24),
                child: ContentDivider(child: Text(intl.loginDividerOr)),
              ),
              SizedBox(height: 12),
              SecondaryButton(
                leading: Image.asset('lib/assets/images/logo_google.png'),
                child: Text(intl.loginButtonGoogle),
                onPressed: () {
                  _googleSignIn.signIn().then((account) {
                    print(account?.email);
                  });
                },
              ),
              const SizedBox(height: 48),
              Center(
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Button(
                      onPressed: () {
                        appController.toggleTheme();
                      },
                      decoration: BoxDecoration(),
                      foreground: theme.colorScheme.primary,
                      trailing: theme.brightness == Brightness.dark
                          ? Icon(Icons.light_mode)
                          : Icon(Icons.dark_mode),
                      child: Text(intl.loginChangeTheme),
                    ),
                    Button(
                      onPressed: () {
                        appController.toggleLanguage();
                      },
                      decoration: BoxDecoration(),
                      foreground: theme.colorScheme.primary,
                      leading: Icon(Icons.language),
                      child: Text(intl.loginChangeLanguage),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
