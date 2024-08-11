import 'package:flutter/widgets.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/service.dart';
import 'package:seioffice/src/service/services/user_service.dart';

class NoQueryParameterException implements Exception {
  final String key;

  NoQueryParameterException(this.key);

  @override
  String toString() {
    return 'No query parameter with key $key';
  }
}

class QueryParameterList {
  final List<String> _params;

  QueryParameterList() : _params = [];

  QueryParameterList.fromList(List<String> list) : _params = list;

  String? _get(int index) {
    if (index >= _params.length || index < 0) {
      return null;
    }
    return _params[index];
  }

  int getInteger(int index) {
    String? value = _get(index);
    if (value == null) {
      throw NoQueryParameterException(index.toString());
    }
    int? result = int.tryParse(value);
    if (result == null) {
      throw NoQueryParameterException(index.toString());
    }
    return result;
  }

  int? getOptionalInteger(int index) {
    String? value = _get(index);
    if (value == null) {
      return null;
    }
    return int.tryParse(value);
  }

  String getString(int index) {
    String? value = _get(index);
    if (value == null) {
      throw NoQueryParameterException(index.toString());
    }
    return value;
  }

  String? getOptionalString(int index) {
    return _get(index);
  }

  double getDouble(int index) {
    String? value = _get(index);
    if (value == null) {
      throw NoQueryParameterException(index.toString());
    }
    double? result = double.tryParse(value);
    if (result == null) {
      throw NoQueryParameterException(index.toString());
    }
    return result;
  }

  double? getOptionalDouble(int index) {
    String? value = _get(index);
    if (value == null) {
      return null;
    }
    return double.tryParse(value);
  }

  bool getBoolean(int index) {
    String? value = _get(index);
    if (value == null) {
      throw NoQueryParameterException(index.toString());
    }
    if (value == 'true') {
      return true;
    } else if (value == 'false') {
      return false;
    }
    throw NoQueryParameterException(index.toString());
  }

  bool? getOptionalBoolean(int index) {
    String? value = _get(index);
    if (value == null) {
      return null;
    }
    if (value == 'true') {
      return true;
    } else if (value == 'false') {
      return false;
    }
    return null;
  }

  int get length => _params.length;
}

class QueryParameters {
  final Map<String, List<String>> _params;

  QueryParameters() : _params = {};

  QueryParameters.fromMap(Map<String, List<String>> map) : _params = map;

  String? _getFirst(String key) {
    List<String>? values = _params[key];
    if (values == null) {
      return null;
    }
    return values.firstOrNull;
  }

  QueryParameterList getList(String key) {
    List<String>? values = _params[key];
    if (values == null) {
      return QueryParameterList();
    }
    return QueryParameterList.fromList(values);
  }

  int getInteger(String key, [int? defaultValue]) {
    String? value = _getFirst(key);
    if (value == null) {
      if (defaultValue != null) {
        return defaultValue;
      }
      throw NoQueryParameterException(key);
    }
    int? result = int.tryParse(value);
    if (result == null) {
      throw NoQueryParameterException(key);
    }
    return result;
  }

  int? getOptionalInteger(String key) {
    String? value = _getFirst(key);
    if (value == null) {
      return null;
    }
    return int.tryParse(value);
  }

  String getString(String key, [String? defaultValue]) {
    String? value = _getFirst(key);
    if (value == null) {
      if (defaultValue != null) {
        return defaultValue;
      }
      throw NoQueryParameterException(key);
    }
    return value;
  }

  String? getOptionalString(String key) {
    return _getFirst(key);
  }

  double getDouble(String key, [double? defaultValue]) {
    String? value = _getFirst(key);
    if (value == null) {
      if (defaultValue != null) {
        return defaultValue;
      }
      throw NoQueryParameterException(key);
    }
    double? result = double.tryParse(value);
    if (result == null) {
      throw NoQueryParameterException(key);
    }
    return result;
  }

  double? getOptionalDouble(String key) {
    String? value = _getFirst(key);
    if (value == null) {
      return null;
    }
    return double.tryParse(value);
  }

  bool getBoolean(String key, [bool? defaultValue]) {
    String? value = _getFirst(key);
    if (value == null) {
      if (defaultValue != null) {
        return defaultValue;
      }
      throw NoQueryParameterException(key);
    }
    if (value == 'true') {
      return true;
    } else if (value == 'false') {
      return false;
    }
    throw NoQueryParameterException(key);
  }

  bool? getOptionalBoolean(String key) {
    String? value = _getFirst(key);
    if (value == null) {
      return null;
    }
    if (value == 'true') {
      return true;
    } else if (value == 'false') {
      return false;
    }
    return null;
  }
}

class Controller {
  final String name;
  final String path;
  final bool inherit;
  final bool? requireAuth;
  final AccessMap? requiredAccess;
  final Widget Function(BuildContext context, QueryParameters parameters)
      builder;

  final List<Controller> children;

  const Controller({
    required this.name,
    required this.path,
    this.inherit = false,
    this.requireAuth,
    this.requiredAccess,
    required this.builder,
    this.children = const [],
  });
}

void _debugPrintControllers(
    Map<Controller, Controller> parents, List<Controller> controllers) {
  String debugBuildPath(Controller controller) {
    final parent = parents[controller];
    String path = parent == null
        ? controller.path.isEmpty
            ? ''
            : '/${controller.path}'
        : '${debugBuildPath(parent)}/${controller.path}';
    return path;
  }

  _debugDeepPrintControllers(controllers, debugBuildPath);
}

void _debugDeepPrintControllers(
    List<Controller> controllers, String Function(Controller) debugBuildPath) {
  for (final controller in controllers) {
    debugPrint(
        'Building route for ${controller.name} (${debugBuildPath(controller)})');
    _debugDeepPrintControllers(controller.children, debugBuildPath);
  }
}

void _fillInParents(Map<Controller, Controller> parents, Controller parent) {
  for (final child in parent.children) {
    parents[child] = parent;
    _fillInParents(parents, child);
  }
}

GoRouter buildRouter(List<Controller> controllers) {
  Map<Controller, Controller> parents = {};
  for (final controller in controllers) {
    _fillInParents(parents, controller);
  }
  _debugPrintControllers(parents, controllers);
  final routes = controllers.map((controller) {
    return _buildChildRoute(parents, controller);
  }).toList();
  return GoRouter(
    routes: routes,
  );
}

bool _requireAuth(Map<Controller, Controller> parentMap, Controller child) {
  final parent = parentMap[child];
  return child.requireAuth ??
      (child.inherit
          ? parent != null
              ? _requireAuth(parentMap, parent)
              : false
          : false);
}

bool _hasAccess(Controller? parent, Controller child, AccessMap userAccess) {
  AccessMap? requiredAccess =
      child.requiredAccess ?? (child.inherit ? parent?.requiredAccess : null);
  if (requiredAccess == null) {
    return true;
  }
  return requiredAccess.hasAccess(userAccess);
}

GoRoute _buildChildRoute(
    Map<Controller, Controller> parentMap, Controller child) {
  final parent = parentMap[child];
  return GoRoute(
    name: child.name,
    path: parent == null ? '/${child.path}' : child.path,
    // path: '/${child.path}',
    redirect: (context, state) {
      if (_requireAuth(parentMap, child)) {
        final userController = ServiceRegistry.of<UserService>(context);
        if (userController.user.value == null) {
          return '/login';
        }
        final user = userController.currentUser;
        if (!_hasAccess(parent, child, user.accessMap)) {
          return '/forbidden';
        }
      }
      return null;
    },
    pageBuilder: (context, state) {
      return SEIPage(
        name: child.name,
        key: ValueKey(child.name),
        child: Builder(
          builder: (context) {
            return child.builder(
                context, QueryParameters.fromMap(state.uri.queryParametersAll));
          },
        ),
      );
    },
    routes: child.children.map((controller) {
      return _buildChildRoute(parentMap, controller);
    }).toList(),
  );
}
