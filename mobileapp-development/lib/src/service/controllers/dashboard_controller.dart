import 'package:seioffice/src/pages/profile/profile_page.dart';

import '../../pages/dashboard/dashboard_page.dart';
import '../../pages/dashboard/notification_page.dart';
import '../controller.dart';
import '../routes.dart';

final fDashboardController = Controller(
  name: kPageDashboard,
  path: '',
  requireAuth: true,
  builder: (context, parameters) {
    return const DashboardPage();
  },
  children: [
    Controller(
      name: kPageNotification,
      path: 'notification',
      inherit: true,
      builder: (context, parameters) {
        return const NotificationPage();
      },
    ),
    Controller(
      name: kPageProfile,
      path: 'profile',
      builder: (context, parameters) {
        return const ProfilePage();
      },
    )
  ],
);
