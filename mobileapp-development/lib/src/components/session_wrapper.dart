import 'dart:async';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:jwt_decoder/jwt_decoder.dart';
import 'package:seioffice/src/service/routes.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/user_service.dart';

class SessionWrapper extends StatefulWidget {
  final Widget child;
  final GoRouter router;

  const SessionWrapper({super.key, required this.child, required this.router});

  @override
  State<SessionWrapper> createState() => _SessionWrapperState();
}

class _SessionWrapperState extends State<SessionWrapper> {
  UserService? _userController;
  Timer? _activeTimer;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _userController?.user.removeListener(_onUserChange);
    _userController = getService<UserService>();
    _userController?.user.addListener(_onUserChange);
    _onUserChange();
  }

  void _onUserChange() {
    _activeTimer?.cancel();
    UserData? user = _userController?.user.value;
    if (user != null) {
      final token = user.token;
      DateTime expiration = JwtDecoder.getExpirationDate(token);
      if (expiration.isBefore(DateTime.now())) {
        debugPrint('Token has expired');
        _userController?.logout().then((value) {
          WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
            widget.router.goNamed(kPageLogin);
          });
        });
      } else {
        var duration = expiration.difference(DateTime.now());
        debugPrint('Token expires at $expiration ($duration)');
        _activeTimer = Timer(duration, () {
          debugPrint('Token has expired');
          _userController?.logout().then((value) {
            WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
              widget.router.goNamed(kPageLogin);
            });
          });
        });
      }
    } else {
      _activeTimer?.cancel();
      WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
        widget.router.goNamed(kPageLogin);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return widget.child;
  }
}
