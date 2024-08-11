import 'dart:io';

const kFilePathRel = 'lib/src/service/localization.dart';
const kFileTargetRel = 'lib/src/service/lerped_localization.dart';

main() {
  File file = File(kFilePathRel);
  String content = file.readAsStringSync();
  List<LocalizationFunction> functions = read(content);
  for (LocalizationFunction function in functions) {
    print(generateLerpedFunction(function.name, function.isGetter,
        function.args, function.isResultNullable, function.header));
  }
  String lerpedContent = generateLerpedClass(functions);
  File target = File(kFileTargetRel);
  target.writeAsStringSync(lerpedContent);
}

String header = '''
import 'package:seioffice/src/service/localization.dart';
import 'package:flutter/material.dart';
import 'package:seioffice/src/service/string_lerper.dart';

class LerpedLocalization extends SEILocalizations {
  final SEILocalizations a;
  final SEILocalizations b;
  final double t;
  
  LerpedLocalization(this.a, this.b, this.t);

''';

String footer = '''
}
''';

String generateLerpedFunction(String functionName, bool isGetter,
    List<Param> args, bool isResultNullable, String header) {
  if (isGetter) {
    return '''
  @override
  $header {
    return lerpString(a.$functionName, b.$functionName, t)${!isResultNullable ? '!' : ''};
  }
''';
  } else {
    return '''
  @override
  $header {
    return lerpString(a.$functionName(${args.map((e) => e.createReference()).join(', ')}), b.$functionName(${args.map((e) => e.createReference()).join(', ')}), t)${!isResultNullable ? '!' : ''};
  }
''';
  }
}

String generateLerpedClass(List<LocalizationFunction> functions) {
  String result = header;
  for (LocalizationFunction function in functions) {
    result += generateLerpedFunction(function.name, function.isGetter,
        function.args, function.isResultNullable, function.header);
  }
  result += footer;
  return result;
}

class LocalizationFunction {
  final String name;
  final bool isGetter;
  final List<Param> args;
  final bool isResultNullable;
  final String header;

  LocalizationFunction(
      this.name, this.isGetter, this.args, this.isResultNullable, this.header);
}

String localizationClassHeader = 'const SEILocalizations();';

String localizationClassFooter = 'enum TimeUnits {';

List<LocalizationFunction> read(String s) {
  int index = s.indexOf(localizationClassHeader);
  int endIndex = s.indexOf(localizationClassFooter);
  print('Header: $index Footer: $endIndex');
  String sub = s.substring(index + localizationClassHeader.length, endIndex);
  List<String> lines = sub.split(';');
  List<LocalizationFunction> functions = [];
  for (String line in lines) {
    line = line.trim();
    if (line.isEmpty) {
      continue;
    }
    bool isValid = line.startsWith('String');
    if (!isValid) {
      continue;
    }
    bool isGetter = line.contains('get ');
    int firstStringIndex = line.indexOf('String');
    String subLine = line.substring(firstStringIndex + 6).trim();
    bool isResultNullable = subLine.startsWith('?');
    if (isGetter) {
      int startName = line.indexOf('get ');
      String name = line.substring(startName + 4).trim();
      functions.add(
          LocalizationFunction(name, isGetter, [], isResultNullable, line));
    } else {
      print('Non-Getter: $line');
      int startName = line.indexOf(' ');
      int endName = line.indexOf('(');
      String name = line.substring(startName + 1, endName).trim();
      int startParam = line.indexOf('(');
      int endParam = line.indexOf(')');
      String params = line.substring(startParam + 1, endParam);
      List<Param> args = parseParams(params);
      functions.add(
          LocalizationFunction(name, isGetter, args, isResultNullable, line));
    }
  }
  return functions;
}

abstract class Param {
  String createReference();
}

class SimpleParam extends Param {
  final String name;

  SimpleParam(this.name);

  @override
  String createReference() {
    return name;
  }
}

class NamedParam extends Param {
  final String name;
  NamedParam(this.name);

  @override
  String createReference() {
    return '$name: $name';
  }
}

List<Param> parseParams(String s) {
  List<Param> result = [];
  bool isNamed = false;
  String builder = '';
  print('Attempting to parse: $s');
  for (int i = 0; i < s.length; i++) {
    String c = s[i];
    print(c);
    if (c == '{') {
      print('Setting named');
      isNamed = true;
    } else if (c == '}' || c == ',') {
      int defaultValueIndex = builder.indexOf('=');
      if (defaultValueIndex != -1) {
        builder = builder.substring(0, defaultValueIndex);
      }
      List<String> parts = builder.trim().split(' ');
      builder = parts[parts.length - 1];
      if (builder.isNotEmpty) {
        print('Adding: $builder $isNamed');
        if (isNamed) {
          result.add(NamedParam(builder.trim()));
        } else {
          result.add(SimpleParam(builder.trim()));
        }
      }
      builder = '';
      if (c == '}') {
        print('Resetting named');
        isNamed = false;
      }
    } else {
      builder += c;
    }
  }
  print('Rest: ${builder.trim()}');
  int defaultValueIndex = builder.indexOf('=');
  if (defaultValueIndex != -1) {
    builder = builder.substring(0, defaultValueIndex);
  }
  List<String> parts = builder.trim().split(' ');
  builder = parts[parts.length - 1];
  if (builder.isNotEmpty) {
    print('Adding: $builder $isNamed');
    if (isNamed) {
      result.add(NamedParam(builder.trim()));
    } else {
      result.add(SimpleParam(builder.trim()));
    }
  }
  return result;
}
