const kPageLogin = 'login';
const kPageDashboard = 'dashboard';
const kPageProfile = 'profile';
const kPageAttendanceDetail = 'attendance-detail';
const kPageAttendanceHistory = 'attendance-history';
const kPageAttendance = 'attendance';
const kPageAttendanceRequestsLog = 'attendance-requests-log';
// const kPageAttendanceRequestDetail = 'attendance-request-detail'; HALAMAN DETAIL TIDAK MENGGUNAKAN ROUTER
const kPageLiveAttendance = 'live-attendance';
const kPageNotification = 'notification';
const kPageAttendanceReview =
    'attendance-review'; // halaman review attendance untuk atasan
const kPageLeave = 'leave'; // halaman pengajuan cuti
const kPageLeaveDetail = 'leave-detail'; // halaman detail pengajuan cuti
const kPageLeaveHistory = 'leave-history'; // halaman riwayat cuti
const kPageLeaveReview = 'leave-review'; // halaman review cuti untuk atasan
const kPageLeaveReviewDetail =
    'leave-review-detail'; // halaman detail review cuti
const kPageAttendanceReviewDetail = 'attendance-review-detail';
// jatah cuti
const kPageLeaveQuota = 'leave-quota';
const kPageLeaveQuotaHistory = 'leave-quota-history';

// final GoRouter router = GoRouter(
//   errorBuilder: (context, state) {
//     return NotFoundPage();
//   },
//   routes: [
//     GoRoute(
//       path: '/',
//       name: kPageDashboard,
//       redirect: (context, state) {
//         final userController = ServiceRegistry.of<UserService>(context);
//         if (userController.user.value != null) {
//           return null;
//         }
//         return '/login';
//       },
//       pageBuilder: (context, state) {
//         if (ServiceRegistry.of<UserService>(context).user.value == null) {
//           return SEIPage(child: const SizedBox());
//         }
//         return SEIPage(child: DashboardPage());
//       },
//       routes: <RouteBase>[
//         GoRoute(
//           path: 'login',
//           name: kPageLogin,
//           pageBuilder: (context, state) {
//             return SEIPage(child: LoginPage());
//           },
//         ),
//         GoRoute(
//           path: 'profile',
//           name: kPageProfile,
//           pageBuilder: (context, state) {
//             return SEIPage(child: const ViewProfilePage());
//           },
//         ),
//         GoRoute(
//             path: 'attendance',
//             name: kPageAttendance,
//             pageBuilder: (context, state) {
//               return SEIPage(child: const AttendanceDashboardPage());
//             },
//             routes: [
//               GoRoute(
//                 path: 'live',
//                 name: kPageLiveAttendance,
//                 pageBuilder: (context, state) {
//                   return SEIPage(child: const LiveAttendancePage());
//                 },
//               ),
//               GoRoute(
//                 path: 'requests',
//                 name: kPageAttendanceRequestsLog,
//                 pageBuilder: (context, state) {
//                   return SEIPage(child: const AttendanceRequestsLogPage());
//                 },
//               ),
//               GoRoute(
//                 path: 'history',
//                 name: kPageAttendanceHistory,
//                 pageBuilder: (context, state) {
//                   return SEIPage(child: const AttendanceLogPage());
//                 },
//                 routes: [
//                   GoRoute(
//                     path: 'detail',
//                     name: kPageAttendanceDetail,
//                     pageBuilder: (context, state) {
//                       final queryParams = state.uri.queryParameters;
//                       final day = int.tryParse(queryParams['day'] ?? '');
//                       final month = int.tryParse(queryParams['month'] ?? '');
//                       final year = int.tryParse(queryParams['year'] ?? '');
//                       if (day == null || month == null || year == null) {
//                         return SEIPage(child: const AttendanceLogPage());
//                       }
//                       return SEIPage(
//                         child: AttendanceHistoryDetailPage(
//                           year: year,
//                           month: month,
//                           day: day,
//                         ),
//                       );
//                     },
//                   ),
//                 ],
//               ),
//             ]),
//         GoRoute(
//           path: 'notification',
//           name: kPageNotification,
//           pageBuilder: (context, state) {
//             return SEIPage(child: const NotificationPage());
//           },
//         ),
//         GoRoute(
//           path: 'cuti',
//           name: kPageLeave,
//           pageBuilder: (context, state) {
//             return SEIPage(child: const CutiDashboardPage());
//           },
//           routes: [
//             GoRoute(
//               path: 'history',
//               name: kPageLeaveHistory,
//               pageBuilder: (context, state) {
//                 return SEIPage(child: const CutiHistoryPage());
//               },
//               routes: [
//                 GoRoute(
//                   path: 'detail',
//                   name: kPageLeaveDetail,
//                   redirect: (context, state) {
//                     int? id =
//                         int.tryParse(state.uri.queryParameters['id'] ?? '');
//                     if (id == null) {
//                       return '/cuti/history';
//                     }
//                     return null;
//                   },
//                   pageBuilder: (context, state) {
//                     int? id =
//                         int.tryParse(state.uri.queryParameters['id'] ?? '');
//                     return SEIPage(child: CutiDetailPage(id: id!));
//                   },
//                 ),
//               ],
//             ),
//             GoRoute(
//               path: 'review',
//               name: kPageLeaveReview,
//               pageBuilder: (context, state) {
//                 return SEIPage(child: const CutiApprovalHistoryPage());
//               },
//               routes: [
//                 GoRoute(
//                   path: 'detail',
//                   name: kPageLeaveReviewDetail,
//                   redirect: (context, state) {
//                     int? id =
//                         int.tryParse(state.uri.queryParameters['id'] ?? '');
//                     if (id == null) {
//                       return '/cuti/review';
//                     }
//                     return null;
//                   },
//                   pageBuilder: (context, state) {
//                     int? id =
//                         int.tryParse(state.uri.queryParameters['id'] ?? '');
//                     return SEIPage(child: CutiApprovalDetailPage(id: id!));
//                   },
//                 ),
//               ],
//             ),
//             GoRoute(
//               path: 'quota',
//               name: kPageLeaveQuota,
//               pageBuilder: (context, state) {
//                 return SEIPage(child: const CutiQuotaPage());
//               },
//               routes: [
//                 GoRoute(
//                   path: 'history',
//                   name: kPageLeaveQuotaHistory,
//                   redirect: (context, state) {
//                     int? year =
//                         int.tryParse(state.uri.queryParameters['year'] ?? '');
//                     if (year == null) {
//                       return '/cuti/quota';
//                     }
//                     return null;
//                   },
//                   pageBuilder: (context, state) {
//                     int? year =
//                         int.tryParse(state.uri.queryParameters['year'] ?? '');
//                     return SEIPage(child: CutiQuotaHistoryPage(year: year!));
//                   },
//                 ),
//               ],
//             ),
//           ],
//         )
//       ],
//     )
//   ],
// );
