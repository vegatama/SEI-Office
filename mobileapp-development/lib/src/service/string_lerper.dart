const kCodeUnitStart = 65; // A
const kCodeUnitUnitEnd = 122; // z

main() {
  String from = 'Lorem ipsum';
  String to = 'dolor sit amet consectetur adipiscing elit';
  for (double t = 0; t <= 1; t += 0.1) {
    print(lerpString(from, to, t));
  }
}

String? lerpString(String? from, String? to, double t) {
  if (from == null && to == null) {
    return null;
  }
  String a = from ?? '';
  String b = to ?? '';
  int lengthDiff = b.length - a.length;
  int newLength = (a.length + (lengthDiff * t)).toInt();
  List<int> generated = List.generate(
    newLength,
    (index) {
      double ratio = index / newLength.toDouble();
      double startT = (ratio * index) / newLength;
      double endT = (ratio * (index + 1)) / newLength;
      // rescale the t value to the range of the current index
      double lerpT = ((t - startT) / (endT - startT)).clamp(0, 1);
      // print(
      //     'lerpT: $lerpT at $index from $t with $newLength ratio $ratio and $startT $endT');
      if (index < a.length && index < b.length) {
        return lerpCodeUnit(a.codeUnitAt(index), b.codeUnitAt(index), lerpT);
      } else if (index < a.length) {
        return lerpCodeUnit(
            _getRandomCodeUnit(ratio), a.codeUnitAt(index), lerpT);
      } else {
        return lerpCodeUnit(
            _getRandomCodeUnit(ratio), b.codeUnitAt(index), lerpT);
      }
    },
  );
  return String.fromCharCodes(generated);
}

int _getRandomCodeUnit(double t) {
  return (kCodeUnitStart + (kCodeUnitUnitEnd - kCodeUnitStart) * t).toInt();
}

int lerpCodeUnit(int a, int b, double t) {
  return (a + ((b - a) * t)).toInt();
}
