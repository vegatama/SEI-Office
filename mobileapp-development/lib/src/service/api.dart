import 'package:seioffice/src/util.dart';

enum APIMethod {
  GET,
  POST,
  PUT,
  DELETE,
}

const apiGet = APIMethod.GET;
const apiPost = APIMethod.POST;
const apiPut = APIMethod.PUT;
const apiDelete = APIMethod.DELETE;

abstract class APIRequest<T extends APIResponse> {
  String get path;
  APIMethod get method;
  Map<String, String>? get headers => null;
  Map<String, dynamic>? get queryParameters => null;
  Map<String, dynamic>? get body => null;
  bool get isMultipart => false;
  APIResponse parseResponse(JsonObject json);
}

abstract class APIResponse with MessageDataHolder {
  String? get msg => null;
  String? get message => null;

  @override
  String? get errorMessage {
    return filterSuccessMessage(message ?? msg ?? kSuccessMessage);
  }
}
