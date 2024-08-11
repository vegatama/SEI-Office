import 'package:flutter/material.dart';
import 'package:seioffice/src/components/data.dart';
import 'package:seioffice/src/service/services.dart';

class DefaultServiceDataRegistry extends StatefulWidget {
  final Widget child;
  final VoidCallback onInit;

  const DefaultServiceDataRegistry({
    super.key,
    required this.child,
    required this.onInit,
  });

  @override
  State<DefaultServiceDataRegistry> createState() =>
      _DefaultServiceDataRegistryState();
}

class _DefaultServiceDataRegistryState
    extends State<DefaultServiceDataRegistry> {
  final ServiceRegistry _registry = ServiceRegistry();
  late Future<void> _initialization;
  @override
  void initState() {
    super.initState();
    for (var service in services) {
      debugPrint('Registering service ${service.runtimeType}');
      _registry.registerTyped(service.type, service.builder());
    }
    _initialization = _registry._dispatchInit().then((_) {
      widget.onInit();
    }).onError((error, stackTrace) {
      debugPrint('Error initializing services: $error');
      debugPrintStack(stackTrace: stackTrace);
      return Future.error(error!, stackTrace);
    });
  }

  @override
  void dispose() {
    _registry._dispatchDispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: _initialization,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          if (snapshot.hasError) {
            return const SizedBox();
          }
          return Data(
            data: _registry,
            child: widget.child,
          );
        }
        return const SizedBox();
      },
    );
  }
}

class ServiceRegistry {
  static T of<T extends Service>(BuildContext context) {
    return Data.of<ServiceRegistry>(context).get<T>();
  }

  final Map<Type, Service> _services = {};

  void register<T extends Service>(T service) {
    _services[T] = service;
    service._registry = this;
  }

  void registerTyped(Type type, Service service) {
    _services[type] = service;
    service._registry = this;
  }

  T get<T extends Service>() {
    var service = _services[T];
    assert(service != null, 'Service of type $T not found');
    return service as T;
  }

  Future<void> _dispatchInit() async {
    for (var service in _services.values) {
      try {
        await service.init();
        debugPrint('Service ${service.runtimeType} initialized');
      } catch (e, stackTrace) {
        debugPrint('Error initializing service ${service.runtimeType}: $e');
        debugPrintStack(stackTrace: stackTrace);
        rethrow;
      }
    }
  }

  void _dispatchDispose() {
    for (var service in _services.values) {
      service.dispose();
    }
  }
}

extension ServiceStateExtension on State {
  T getService<T extends Service>() {
    return ServiceRegistry.of<T>(context);
  }
}

extension ServiceContextExtension on BuildContext {
  T getService<T extends Service>() {
    return ServiceRegistry.of<T>(this);
  }
}

extension ServiceStatelessWidgetExtension on StatelessWidget {
  T getService<T extends Service>(BuildContext context) {
    return ServiceRegistry.of<T>(context);
  }
}

abstract class Service {
  static T of<T extends Service>(BuildContext context) {
    return ServiceRegistry.of<T>(context);
  }

  late ServiceRegistry _registry;

  @protected
  T get<T extends Service>() {
    return _registry.get<T>();
  }

  Future<void> init() async {}
  void dispose() {}
}

class ServiceFactory<T extends Service> {
  final T Function() builder;

  ServiceFactory(this.builder);

  Type get type => T;
}
