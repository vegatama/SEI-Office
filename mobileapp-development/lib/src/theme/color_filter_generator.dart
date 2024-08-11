import 'dart:math';

import 'package:flutter/painting.dart';

class ColorFilterGenerator {
  static const List<double> identity = [
    1,
    0,
    0,
    0,
    0,
    0,
    1,
    0,
    0,
    0,
    0,
    0,
    1,
    0,
    0,
    0,
    0,
    0,
    1,
    0,
  ];
  final List<double> _matrix;

  ColorFilterGenerator() : _matrix = identity;
  ColorFilterGenerator.from(List<double> matrix) : _matrix = List.from(matrix);

  ColorFilterGenerator _concat(List<double> matrix) {
    final List<double> result = List.filled(20, 0);
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 5; j++) {
        double value = 0;
        for (int k = 0; k < 4; k++) {
          value += _matrix[i * 5 + k] * matrix[k * 5 + j];
        }
        result[i * 5 + j] = value;
        if (j == 4) {
          result[i * 5 + j] += _matrix[i * 5 + 4];
        }
      }
    }
    return ColorFilterGenerator.from(result);
  }

  ColorFilterGenerator applyHue(double hue) {
    final List<double> matrix = List.from(identity);
    final double cosA = cos(hue * pi / 180);
    final double sinA = sin(hue * pi / 180);
    matrix[0] = cosA + (1 - cosA) / 3;
    matrix[1] = 1 / 3 * (1 - cosA) - sqrt(1 / 3) * sinA;
    matrix[2] = 1 / 3 * (1 - cosA) + sqrt(1 / 3) * sinA;
    matrix[5] = 1 / 3 * (1 - cosA) + sqrt(1 / 3) * sinA;
    matrix[6] = cosA + 1 / 3 * (1 - cosA);
    matrix[7] = 1 / 3 * (1 - cosA) - sqrt(1 / 3) * sinA;
    matrix[10] = 1 / 3 * (1 - cosA) - sqrt(1 / 3) * sinA;
    matrix[11] = 1 / 3 * (1 - cosA) + sqrt(1 / 3) * sinA;
    matrix[12] = cosA + 1 / 3 * (1 - cosA);
    return _concat(matrix);
  }

  ColorFilterGenerator applySaturate(double saturation) {
    final List<double> matrix = List.from(identity);
    final double invSat = 1 - saturation;
    final double invLumR = invSat * 0.299;
    final double invLumG = invSat * 0.587;
    final double invLumB = invSat * 0.114;
    matrix[0] = invLumR + saturation;
    matrix[1] = invLumG;
    matrix[2] = invLumB;
    matrix[5] = invLumR;
    matrix[6] = invLumG + saturation;
    matrix[7] = invLumB;
    matrix[10] = invLumR;
    matrix[11] = invLumG;
    matrix[12] = invLumB + saturation;
    return _concat(matrix);
  }

  ColorFilterGenerator applyColorReplacement(Color from, Color to) {
    final List<double> matrix = List.from(identity);
    double oR = from.red / 255;
    double oG = from.green / 255;
    double oB = from.blue / 255;
    double nR = to.red / 255;
    double nG = to.green / 255;
    double nB = to.blue / 255;
    matrix[0] = nR - oR;
    matrix[6] = nG - oG;
    matrix[12] = nB - oB;
    matrix[4] = oR * (1 - nR) + nR * 255;
    matrix[9] = oG * (1 - nG) + nG * 255;
    matrix[14] = oB * (1 - nB) + nB * 255;
    return _concat(matrix);
  }

  ColorFilterGenerator applyColorize(Color color) {
    HSVColor hsv = HSVColor.fromColor(color);
    return applyHue(hsv.hue)
        .applySaturate(hsv.saturation)
        .applyBrightness(hsv.value);
  }

  ColorFilterGenerator applyContrast(double contrast) {
    final List<double> matrix = List.from(identity);
    final double t = (1 - contrast) / 2;
    matrix[0] = contrast;
    matrix[1] = 0;
    matrix[2] = 0;
    matrix[5] = 0;
    matrix[6] = contrast;
    matrix[7] = 0;
    matrix[10] = 0;
    matrix[11] = 0;
    matrix[12] = contrast;
    matrix[4] = t;
    matrix[9] = t;
    matrix[14] = t;
    return _concat(matrix);
  }

  ColorFilterGenerator applyBrightness(double brightness) {
    final List<double> matrix = List.from(identity);
    matrix[4] = brightness;
    matrix[9] = brightness;
    matrix[14] = brightness;
    return _concat(matrix);
  }

  ColorFilterGenerator applyInvert() {
    final List<double> matrix = List.from(identity);
    matrix[0] = -1;
    matrix[6] = -1;
    matrix[12] = -1;
    matrix[18] = 1;
    // add 255 to the result to invert the color
    matrix[4] = 255;
    matrix[9] = 255;
    matrix[14] = 255;
    return _concat(matrix);
  }

  ColorFilterGenerator applyGreyscale() {
    final List<double> matrix = List.from(identity);
    matrix[0] = 0.2126;
    matrix[1] = 0.7152;
    matrix[2] = 0.0722;
    matrix[5] = 0.2126;
    matrix[6] = 0.7152;
    matrix[7] = 0.0722;
    matrix[10] = 0.2126;
    matrix[11] = 0.7152;
    matrix[12] = 0.0722;
    return _concat(matrix);
  }

  ColorFilterGenerator applySepia() {
    final List<double> matrix = List.from(identity);
    matrix[0] = 0.393;
    matrix[1] = 0.769;
    matrix[2] = 0.189;
    matrix[5] = 0.349;
    matrix[6] = 0.686;
    matrix[7] = 0.168;
    matrix[10] = 0.272;
    matrix[11] = 0.534;
    matrix[12] = 0.131;
    return _concat(matrix);
  }

  ColorFilterGenerator applyOpacity(double opacity) {
    final List<double> matrix = List.from(identity);
    matrix[18] = opacity;
    return _concat(matrix);
  }

  ColorFilter generate() {
    print('Generated matrix:');
    for (int i = 0; i < 4; i++) {
      print(
          '${_matrix[i * 5]} ${_matrix[i * 5 + 1]} ${_matrix[i * 5 + 2]} ${_matrix[i * 5 + 3]} ${_matrix[i * 5 + 4]}');
    }
    return ColorFilter.matrix(_matrix);
  }
}

extension ColorExtension on Color {
  Color get invert => Color.fromARGB(255, 255 - red, 255 - green, 255 - blue);
}
