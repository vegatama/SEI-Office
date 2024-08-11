import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';

class CountdownDuration {
  final CountdownUnit days;
  final CountdownUnit hours;
  final CountdownUnit minutes;
  final CountdownUnit seconds;
  final CountdownUnit milliseconds;
  final CountdownUnit microseconds;
  const CountdownDuration({
    required this.days,
    required this.hours,
    required this.minutes,
    required this.seconds,
    required this.milliseconds,
    required this.microseconds,
  });
}

class CountdownUnit {
  final int value;
  final int padding;
  final String prefix;
  final String suffix;
  final bool optional;
  final String paddingString;
  const CountdownUnit(
    this.value, {
    this.padding = 0,
    this.prefix = ' ',
    this.suffix = '',
    this.optional = false,
    this.paddingString = '0',
  });

  CountdownUnit copyWith({
    int? value,
    int? padding,
    String? prefix,
    String? suffix,
    bool? optional,
    String? paddingString,
  }) {
    return CountdownUnit(
      value ?? this.value,
      padding: padding ?? this.padding,
      prefix: prefix ?? this.prefix,
      suffix: suffix ?? this.suffix,
      optional: optional ?? this.optional,
    );
  }

  CountdownUnit pad(int padding, [String paddingString = '0']) {
    return copyWith(padding: padding, paddingString: paddingString);
  }

  CountdownUnit opt([bool optional = true]) {
    return copyWith(optional: optional);
  }

  CountdownUnit pref(String prefix) {
    return copyWith(prefix: prefix);
  }

  CountdownUnit suff(String suffix) {
    return copyWith(suffix: suffix);
  }

  @override
  String toString() {
    return '$prefix${value.toString().padLeft(padding, '0')}$suffix';
  }
}

class Countdown extends StatefulWidget {
  static List<CountdownUnit> defaultFormat(CountdownDuration duration) {
    return [
      duration.days.pad(2).suff('d').pref(':'),
      duration.hours.pad(2).suff('h').pref(':'),
      duration.minutes.pad(2).suff('m').pref(':').opt(),
      duration.seconds.pad(2).suff('s').pref(':').opt(),
    ];
  }

  final DateTime until;
  final DateTime? now;
  final List<CountdownUnit> Function(CountdownDuration duration)? format;

  const Countdown({
    required this.until,
    this.now,
    super.key,
    this.format,
  });

  @override
  State<Countdown> createState() => _CountdownState();
}

class _CountdownState extends State<Countdown>
    with SingleTickerProviderStateMixin {
  late Duration _offset;
  late Duration _remaining;
  late Ticker _ticker;

  @override
  void initState() {
    super.initState();
    DateTime now = DateTime.now();
    _offset = widget.now?.difference(now) ?? Duration.zero;
    _remaining = widget.until.difference(now.add(_offset));
    _ticker = createTicker((elapsed) {
      setState(() {
        _remaining = widget.until.difference(DateTime.now().add(_offset));
      });
    });
    _ticker.start();
  }

  @override
  void didUpdateWidget(covariant Countdown oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.now != widget.now) {
      DateTime now = DateTime.now();
      _offset = widget.now?.difference(now) ?? Duration.zero;
    }
  }

  @override
  void dispose() {
    _ticker.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    CountdownDuration duration = CountdownDuration(
      days: CountdownUnit(_remaining.inDays),
      hours: CountdownUnit(_remaining.inHours.remainder(24)),
      minutes: CountdownUnit(_remaining.inMinutes.remainder(60)),
      seconds: CountdownUnit(_remaining.inSeconds.remainder(60)),
      milliseconds: CountdownUnit(_remaining.inMilliseconds.remainder(1000)),
      microseconds: CountdownUnit(_remaining.inMicroseconds.remainder(1000)),
    );
    List<CountdownUnit> Function(CountdownDuration duration) format =
        widget.format ?? Countdown.defaultFormat;
    List<CountdownUnit> units = format(duration);
    StringBuffer buffer = StringBuffer();
    // optional unit depends on previous unit, if previous unit is optional
    // and it's value is 0, then it's not shown
    bool prevNotShown = false;
    for (int i = 0; i < units.length; i++) {
      CountdownUnit unit = units[i];
      if (unit.optional && prevNotShown) {
        continue;
      }
      if (unit.optional && unit.value == 0) {
        prevNotShown = true;
      } else {
        prevNotShown = false;
      }
      buffer.write(unit);
    }
    return Text(buffer.toString());
  }
}

class CountdownBuilder extends StatefulWidget {
  final DateTime until;
  final DateTime? now;
  final Widget Function(BuildContext context, Duration time)? builder;

  const CountdownBuilder({
    required this.until,
    this.now,
    this.builder,
  });

  @override
  State<CountdownBuilder> createState() => _CountdownBuilderState();
}

class _CountdownBuilderState extends State<CountdownBuilder>
    with SingleTickerProviderStateMixin {
  late Duration _offset;
  late Duration _remaining;
  late Ticker _ticker;

  @override
  void initState() {
    super.initState();
    DateTime now = DateTime.now();
    _offset = widget.now?.difference(now) ?? Duration.zero;
    _remaining = widget.until.difference(now.add(_offset));
    _ticker = createTicker((elapsed) {
      setState(() {
        _remaining = widget.until.difference(DateTime.now().add(_offset));
      });
    });
    _ticker.start();
  }

  @override
  void didUpdateWidget(covariant CountdownBuilder oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.now != widget.now) {
      DateTime now = DateTime.now();
      _offset = widget.now?.difference(now) ?? Duration.zero;
    }
  }

  @override
  void dispose() {
    _ticker.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return widget.builder?.call(context, _remaining) ?? Container();
  }
}
