import '../../pages/dashboard/login.dart';
import '../controller.dart';
import '../routes.dart';

final fLoginController = Controller(
  name: kPageLogin,
  path: 'login',
  builder: (context, parameters) {
    return const LoginPage();
  },
);
