import 'package:seioffice/src/pages/attendance/attendance_history_detail_page.dart';
import 'package:seioffice/src/service/controller.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/routes.dart';

import '../../pages/attendance/attendance_dashboard_page.dart';
import '../../pages/attendance/attendance_log_page.dart';
import '../../pages/attendance/attendance_requests_log_page.dart';
import '../../pages/attendance/live_attendance_page.dart';

final fAttendanceController = Controller(
    name: kPageAttendance,
    path: 'attendance',
    requireAuth: true,
    requiredAccess: const AccessMap(
      absensaya: true,
    ),
    builder: (context, parameters) {
      return const AttendanceDashboardPage();
    },
    children: [
      Controller(
        name: kPageLiveAttendance,
        path: 'live',
        inherit: true,
        requiredAccess: const AccessMap(
          absensi: true,
        ),
        builder: (context, parameters) {
          return const LiveAttendancePage();
        },
      ),
      Controller(
        name: kPageAttendanceRequestsLog,
        path: 'requests',
        inherit: true,
        builder: (context, parameters) {
          return const AttendanceRequestsLogPage();
        },
      ),
      Controller(
          name: kPageAttendanceHistory,
          path: 'history',
          inherit: true,
          builder: (context, parameters) {
            return const AttendanceLogPage();
          },
          children: [
            Controller(
              name: kPageAttendanceDetail,
              path: 'detail',
              inherit: true,
              builder: (context, parameters) {
                final day = parameters.getInteger('day');
                final month = parameters.getInteger('month');
                final year = parameters.getInteger('year');
                return AttendanceHistoryDetailPage(
                  day: day,
                  month: month,
                  year: year,
                );
              },
            )
          ])
    ]);
