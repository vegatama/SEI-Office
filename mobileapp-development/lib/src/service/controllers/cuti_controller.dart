import 'package:seioffice/src/pages/cuti/cuti_approval_detail_page.dart';
import 'package:seioffice/src/pages/cuti/cuti_approval_history_page.dart';
import 'package:seioffice/src/pages/cuti/cuti_dashboard_page.dart';
import 'package:seioffice/src/pages/cuti/cuti_quota_history_page.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/routes.dart';

import '../../pages/cuti/cuti_detail_page.dart';
import '../../pages/cuti/cuti_history_page.dart';
import '../../pages/cuti/cuti_quota_page.dart';
import '../controller.dart';

final fCutiController = Controller(
  name: kPageLeave,
  path: 'cuti',
  requiredAccess: const AccessMap(
    izincuti: true,
  ),
  builder: (context, parameters) {
    return const CutiDashboardPage();
  },
  children: [
    Controller(
      name: kPageLeaveHistory,
      path: 'history',
      inherit: true,
      builder: (context, parameters) {
        return const CutiHistoryPage();
      },
      children: [
        Controller(
          name: kPageLeaveDetail,
          path: 'detail',
          inherit: true,
          builder: (context, parameters) {
            int id = parameters.getInteger('id');
            return CutiDetailPage(id: id);
          },
        ),
        Controller(
          name: kPageLeaveReview,
          path: 'review',
          inherit: true,
          builder: (context, parameters) {
            return const CutiApprovalHistoryPage();
          },
          children: [
            Controller(
              name: kPageLeaveReviewDetail,
              path: 'detail',
              inherit: true,
              builder: (context, parameters) {
                int id = parameters.getInteger('id');
                return CutiApprovalDetailPage(id: id);
              },
            ),
          ],
        ),
        Controller(
          name: kPageLeaveQuota,
          path: 'quota',
          inherit: true,
          builder: (context, parameters) {
            return const CutiQuotaPage();
          },
          children: [
            Controller(
              name: kPageLeaveQuotaHistory,
              path: 'history',
              inherit: true,
              builder: (context, parameters) {
                int year = parameters.getInteger('year');
                return CutiQuotaHistoryPage(year: year);
              },
            )
          ],
        ),
      ],
    ),
  ],
);
