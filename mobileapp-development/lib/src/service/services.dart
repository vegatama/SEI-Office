import 'package:seioffice/src/service/services/app_service.dart';
import 'package:seioffice/src/service/services/attendance_service.dart';
import 'package:seioffice/src/service/services/notification_service.dart';
import 'package:seioffice/src/service/service.dart';

import 'services/user_service.dart';

final List<ServiceFactory> services = [
  ServiceFactory<AppService>(() => AppService()),
  ServiceFactory<UserService>(() => UserService()),
  ServiceFactory<NotificationService>(() => NotificationService()),
  ServiceFactory<AttendanceService>(() => AttendanceService()),
];
