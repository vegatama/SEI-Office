import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:seioffice/src/components/button.dart';
import 'package:seioffice/src/components/header.dart';
import 'package:seioffice/src/components/page.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/response/absen.dart';
import 'package:seioffice/src/theme/theme.dart';
import 'package:seioffice/src/util.dart';

import '../../service/features.dart';

class ServiceListPage extends StatelessWidget {
  final AccessMap accessMap;
  const ServiceListPage({
    required this.accessMap,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final intl = context.intl;
    return StandardPage(
      appBar: Header(
        leading: IconButton(
          onPressed: () {
            Navigator.of(context).pop();
          },
          icon: const Icon(Icons.arrow_back),
        ),
        title: Text(context.intl.serviceTitle),
      ),
      padding: const EdgeInsets.only(top: 24, right: 24, left: 24, bottom: 32),
      children: [
        ...kFeatures
            .map((e) {
              return CardButton(
                title: Text(e.name(context)),
                subtitle: Text(e.description(context)),
                color: theme.colorScheme[e.colorHash],
                leading: Icon(e.icon),
                trailing: const Icon(Icons.arrow_forward_ios),
                onPressed: () {
                  context.pushNamed(e.routeName);
                },
              );
            })
            .toList()
            .joinSeparator(const SizedBox(height: 8)),
      ],
    );
  }
}
