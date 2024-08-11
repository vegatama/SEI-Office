import 'package:seioffice/src/service/services/app_service.dart';
import 'package:seioffice/src/service/services/user_service.dart';
import 'package:seioffice/src/service/request/absen.dart';
import 'package:seioffice/src/service/request/attendance.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/util.dart';

import '../response/attendance.dart';

class AttendanceService extends Service {
  Future<AttendanceDashboardData> getDashboardData(DateTime from) async {
    final appController = get<AppService>();
    final userController = get<UserService>();
    final response = await appController.request(
      AttendanceHistoryRequest(
        employee_code: userController.currentUser.employee_code,
        from: LocalDate.fromDateTime(from),
      ),
    );
    final history = response.data;
    final now = DateTime.now();
    final month = now.month;
    final year = now.year;
    final absenResponse = await appController.request(
      AbsenSayaRequest(
          employee_code: userController.currentUser.employee_code,
          year: year,
          month: month),
    );
    history.sort((a, b) {
      DateTime aDate = DateTime(a.year, a.month, a.day);
      DateTime bDate = DateTime(b.year, b.month, b.day);
      return bDate.compareTo(aDate);
    });
    final requests =
        await appController.request(AttendanceApprovalRequestsRequest(
      employee_code: userController.currentUser.employee_code,
      showAll: false,
    ));
    // only take 2 requests
    return AttendanceDashboardData(
      totalLate: absenResponse.total_lupa_check,
      totalShortWorkHour: absenResponse.total_kurang_jam,
      totalForgotCheckOut: absenResponse.total_terlambat,
      history: history,
      requests: requests.requests.take(2).toList(),
    );
  }

  Future<TodayAttendanceResponse> getAttendanceData(DateTime date) async {
    final appController = get<AppService>();
    final userController = get<UserService>();
    final response = await appController.request(
      AttendanceAtRequest(
        employeeCode: userController.currentUser.employee_code,
        date: LocalDate.fromDateTime(date),
      ),
    );
    return response;
  }

  Future<AttendanceHistoryResponse> getHistory(
      {DateTime? to, DateTime? from}) async {
    final appController = get<AppService>();
    final userController = get<UserService>();
    final response = await appController.request(
      AttendanceHistoryRequest(
        employee_code: userController.currentUser.employee_code,
        from: from != null ? LocalDate.fromDateTime(from) : null,
        to: to != null ? LocalDate.fromDateTime(to) : null,
      ),
    );
    return response;
  }

  Future<AttendanceRequestsResponse> getApprovalHistory([int? lastId]) {
    final appController = get<AppService>();
    final userController = get<UserService>();
    return appController.request(AttendanceApprovalRequestsRequest(
      employee_code: userController.currentUser.employee_code,
      from: lastId,
    ));
  }

  Future<void> confirmApproval(int id) async {
    final appController = get<AppService>();
    final userController = get<UserService>();
    await appController.request(AttendanceConfirmRequest(
      empCode: userController.currentUser.employee_code,
      id: id,
      approve: true,
    ));
  }

  Future<void> rejectApproval(int id) async {
    final appController = get<AppService>();
    final userController = get<UserService>();
    await appController.request(AttendanceConfirmRequest(
      empCode: userController.currentUser.employee_code,
      id: id,
      approve: false,
    ));
  }
}

class AttendanceDashboardData {
  final int totalLate;
  final int totalShortWorkHour;
  final int totalForgotCheckOut;
  final List<AttendanceData> history;
  final List<EmployeeCheckInApproval> requests;

  AttendanceDashboardData({
    required this.totalLate,
    required this.totalShortWorkHour,
    required this.totalForgotCheckOut,
    required this.history,
    required this.requests,
  });
}
