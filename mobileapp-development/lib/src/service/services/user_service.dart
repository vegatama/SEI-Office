import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:seioffice/src/service/request/employee.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/employee.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/app_service.dart';
import 'package:shared_preferences/shared_preferences.dart';

class UserService extends Service {
  ValueNotifier<UserData?> user = ValueNotifier(null);

  UserData get currentUser => user.value!;
  @override
  Future<void> init() async {
    var prefs = await SharedPreferences.getInstance();
    var email = prefs.getString('email');
    var name = prefs.getString('name');
    var nik = prefs.getString('nik');
    var employee_code = prefs.getString('employee_code');
    var token = prefs.getString('token');
    var access = prefs.getString('access');
    var create_password = prefs.getBool('create_password');
    var status = prefs.getString('status');
    var is_active = prefs.getBool('is_active');
    var employee_id = prefs.getString('employee_id');
    var nav_id = prefs.getString('nav_id');
    var avatar = prefs.getString('avatar');
    if (email != null &&
        name != null &&
        nik != null &&
        employee_code != null &&
        token != null &&
        access != null &&
        create_password != null &&
        status != null &&
        is_active != null &&
        employee_id != null &&
        nav_id != null) {
      // check token validity
      try {
        user.value = UserData(
          email: email,
          name: name,
          nik: nik,
          employee_code: employee_code,
          token: token,
          access: access,
          create_password: create_password,
          status: status,
          is_active: is_active,
          employee_id: employee_id,
          nav_id: nav_id,
          accessMap: AccessMap.fromJson(jsonDecode(access)),
          avatar: avatar,
        );
        await get<AppService>()
            .request(EmployeeProfileRequest(empcode: employee_code));
      } catch (e) {
        print(e);
        // if token is invalid, then don't set user
        await logout();
      }
    }
    debugPrint('User: ${user.value}');
  }

  Future<void> login({
    required String email,
    required String password,
  }) async {
    UserLoginResponse response = await get<AppService>()
        .request(LoginRequest(email: email, password: password));
    if (response.msg != 'SUCCESS') {
      throw response.msg;
    }
    user.value = UserData(
      email: response.email,
      name: response.name,
      nik: response.nik,
      employee_code: response.employee_code,
      token: response.token,
      access: response.access,
      create_password: response.create_password,
      status: response.status,
      is_active: response.is_active,
      employee_id: response.employee_id,
      nav_id: response.nav_id,
      accessMap: response.accessMap,
      avatar: response.avatar,
    );
    var prefs = await SharedPreferences.getInstance();
    prefs.setString('email', response.email);
    prefs.setString('name', response.name);
    prefs.setString('nik', response.nik);
    prefs.setString('employee_code', response.employee_code);
    prefs.setString('token', response.token);
    prefs.setString('access', response.access);
    prefs.setBool('create_password', response.create_password);
    prefs.setString('status', response.status);
    prefs.setBool('is_active', response.is_active);
    prefs.setString('employee_id', response.employee_id);
    prefs.setString('nav_id', response.nav_id);
    if (response.avatar != null) {
      prefs.setString('avatar', response.avatar!);
    }
  }

  Future<void> logout() async {
    debugPrint('Logging out');
    user.value = null;
    var prefs = await SharedPreferences.getInstance();
    prefs.remove('email');
    prefs.remove('name');
    prefs.remove('nik');
    prefs.remove('employee_code');
    prefs.remove('token');
    prefs.remove('access');
    prefs.remove('create_password');
    prefs.remove('status');
    prefs.remove('is_active');
    prefs.remove('employee_id');
    prefs.remove('nav_id');
    prefs.remove('avatar');
  }

  Future<void> loginWithGoogle() async {
    // TODO
  }
}

class UserData {
  final String email;
  final String name;
  final String nik;
  final String employee_code;
  final String token;
  final String access;
  final bool create_password;
  final String status;
  final bool is_active;
  final String employee_id;
  final String nav_id;
  final AccessMap accessMap;
  final String? avatar;

  const UserData({
    required this.email,
    required this.name,
    required this.nik,
    required this.employee_code,
    required this.token,
    required this.access,
    required this.create_password,
    required this.status,
    required this.is_active,
    required this.employee_id,
    required this.nav_id,
    required this.accessMap,
    required this.avatar,
  });
}
