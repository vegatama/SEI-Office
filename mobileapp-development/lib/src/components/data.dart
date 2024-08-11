import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';

class _RefreshableParentData {
  final Future<void> Function() onRefresh;

  const _RefreshableParentData({
    required this.onRefresh,
  });
}

class RefreshableParent extends StatelessWidget {
  static Future<void> dispatchRefresh(BuildContext context) async {
    final data = Data.maybeOf<_RefreshableParentData>(context);
    if (data != null) {
      await data.onRefresh();
    }
  }

  final Widget child;
  final Future<void> Function() onRefresh;

  const RefreshableParent({
    super.key,
    required this.child,
    required this.onRefresh,
  });

  @override
  Widget build(BuildContext context) {
    return Data<_RefreshableParentData>(
      data: _RefreshableParentData(onRefresh: onRefresh),
      child: child,
    );
  }
}

/// Data widget yang memberikan akses data ke widget turunannya
class Data<T> extends InheritedWidget {
  final T data;

  const Data({
    super.key,
    required this.data,
    required super.child,
  });

  static T of<T>(BuildContext context) {
    final widget = context.dependOnInheritedWidgetOfExactType<Data<T>>();
    if (widget == null) {
      throw Exception('Data of type $T not found');
    }
    return widget.data;
  }

  static T? maybeOf<T>(BuildContext context) {
    return context.dependOnInheritedWidgetOfExactType<Data<T>>()?.data;
  }

  @override
  bool updateShouldNotify(Data<T> oldWidget) {
    return oldWidget.data != data;
  }
}

/// Data widget yang memberikan akses data yang dapat diubah ke widget turunannya
class ListenableData<T> extends InheritedWidget {
  final ValueListenable<T> data;

  const ListenableData({
    super.key,
    required this.data,
    required super.child,
  });

  static T of<T>(BuildContext context) {
    final widget =
        context.dependOnInheritedWidgetOfExactType<ListenableData<T>>();
    if (widget == null) {
      throw Exception('ListenableData of type $T not found');
    }
    return widget.data.value;
  }

  @override
  bool updateShouldNotify(ListenableData<T> oldWidget) {
    return oldWidget.data != data;
  }
}

/// Widget yang digunakan untuk membangun widget berdasarkan data yang diberikan
class DataWatcher<T> extends StatelessWidget {
  final Widget? child;
  final Widget Function(BuildContext context, T data, Widget? child) builder;

  const DataWatcher({
    super.key,
    required this.builder,
    this.child,
  });

  @override
  Widget build(BuildContext context) {
    final listenable = ListenableData.of<T>(context);
    return builder(context, listenable, child);
  }
}
