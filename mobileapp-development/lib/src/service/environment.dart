/// Environment variables
/// Lihat di file .env/{environment}.env
const kEnv = String.fromEnvironment('env', defaultValue: kEnvUnknown);
const kAPIBaseUrl = String.fromEnvironment('api_base_url');
const kAPIKey = String.fromEnvironment('api_key');
const kGoogleClientId = String.fromEnvironment('google_client_id');
const kGoogleClientSecret = String.fromEnvironment('google_client_secret');
const kGoogleRedirectUri = String.fromEnvironment('google_redirect_uri');
const kGoogleMapApiKey = String.fromEnvironment('google_map_api_key');
const kMapProvider = String.fromEnvironment('map_provider');

const kEnvDev = 'dev';
const kEnvProd = 'prod';
const kEnvUnknown = 'unknown';

const kMapProviderGoogle = 'GOOGLE_MAP';
const kMapProviderFlutterMap = 'FLUTTER_MAP';
