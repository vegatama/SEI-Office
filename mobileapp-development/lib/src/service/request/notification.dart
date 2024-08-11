import 'package:seioffice/src/service/api.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/service/response/notification.dart';

import '../../util.dart';

class GetNotificationListRequest extends APIRequest<NotificationListResponse> {
  final String empCode;
  final int? after;

  GetNotificationListRequest({
    required this.empCode,
    this.after,
  });

  @override
  APIResponse parseResponse(JsonObject json) {
    return NotificationListResponse.fromJson(json);
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  String get path {
    return '/notification/list';
  }

  @override
  Map<String, dynamic>? get queryParameters => {
        'empCode': empCode,
        if (after != null) 'after': after,
      };
}

class GetNotificationHistoryRequest
    extends APIRequest<NotificationListResponse> {
  final String empCode;
  final int? before;

  GetNotificationHistoryRequest({
    required this.empCode,
    this.before,
  });

  @override
  APIResponse parseResponse(JsonObject json) {
    return NotificationListResponse.fromJson(json);
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  String get path {
    return '/notification/history';
  }

  @override
  Map<String, dynamic>? get queryParameters => {
        'empCode': empCode,
        if (before != null) 'before': before,
      };
}

class GetNotificationUnreadRequest
    extends APIRequest<NotificationListResponse> {
  final String empCode;

  GetNotificationUnreadRequest({
    required this.empCode,
  });

  @override
  APIResponse parseResponse(JsonObject json) {
    return NotificationListResponse.fromJson(json);
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  String get path {
    return '/notification/unread';
  }

  @override
  Map<String, dynamic>? get queryParameters => {
        'empCode': empCode,
      };
}

class NotificationMarkAsReadRequest extends APIRequest<MessageResponse> {
  final String empCode;
  final int id;

  NotificationMarkAsReadRequest({
    required this.empCode,
    required this.id,
  });

  @override
  APIResponse parseResponse(JsonObject json) {
    return MessageResponse.fromJson(json);
  }

  @override
  APIMethod get method {
    return apiGet;
  }

  @override
  String get path {
    return '/notification/read';
  }

  @override
  Map<String, dynamic> get queryParameters {
    return {
      'empCode': empCode,
      'id': id,
    };
  }
}
