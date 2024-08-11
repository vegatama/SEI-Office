import 'package:seioffice/src/util.dart';

import '../api.dart';

class NotificationListResponse extends APIResponse {
  final String message;
  final List<NotificationDto> notifications;

  NotificationListResponse.fromJson(Map<String, dynamic> json)
      : message = json.getString('message')!,
        notifications = (json['notifications'] as List)
            .map((e) => NotificationDto.fromJson(e))
            .toList();
}

class NotificationDto {
  final int id;
  final DateTime timestamp;
  final String typeName;
  final List<dynamic> data;
  final String message;
  final bool isRead;

  NotificationDto.fromJson(Map<String, dynamic> json)
      : id = json.getInt('id')!,
        timestamp = DateTime.parse(json.getString('timestamp')!),
        typeName = json.getString('typeName')!,
        data = json['data'],
        message = json.getString('message')!,
        isRead = json.getBool('read')!;

  @override
  String toString() {
    return 'NotificationDto{id: $id, timestamp: $timestamp, typeName: $typeName, data: $data, message: $message, isRead: $isRead}';
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is NotificationDto &&
          runtimeType == other.runtimeType &&
          id == other.id;

  @override
  int get hashCode => id.hashCode;
}
