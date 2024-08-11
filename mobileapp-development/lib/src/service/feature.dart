import 'dart:math';

import 'package:flutter/material.dart';
import 'package:seioffice/src/service/localization.dart';

import 'features.dart';

const kFeatureGroupSize = 3;
const kFeatureVisibleGroupCount = 2;
List<List<Feature>> get kGroupedFeatures {
  final List<List<Feature>> result = [];
  for (var i = 0; i < kFeatures.length; i += kFeatureGroupSize) {
    int end = min(i + kFeatureGroupSize, kFeatures.length);
    result.add(kFeatures.sublist(i, end));
  }
  return result.sublist(0, min(kFeatureVisibleGroupCount, result.length));
}

class Feature {
  final int colorHash;
  final MessageBuilder name;
  final MessageBuilder description;
  final String routeName;
  final IconData icon;

  const Feature({
    required this.colorHash,
    required this.name,
    required this.description,
    required this.routeName,
    required this.icon,
  });
}
