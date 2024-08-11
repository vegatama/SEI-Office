import 'package:flutter/material.dart';
import 'package:seioffice/src/service/feature.dart';
import 'package:seioffice/src/service/localization.dart';
import 'package:seioffice/src/service/routes.dart';

final kFeatures = [
  Feature(
    colorHash: 1,
    name: (context) => context.intl.serviceAttendance,
    description: (context) => context.intl.serviceAttendanceDescription,
    routeName: kPageAttendance,
    icon: Icons.event_available,
  ),
  // Feature(
  //   colorHash: 100,
  //   name: (context) => context.intl.serviceEvents,
  //   description: (context) => context.intl.serviceEventsDescription,
  //   // routeName: kPageEvents,
  //   routeName: kPageDashboard, // not yet implemented, go to dashboard instead
  //   icon: Icons.list_alt,
  // ),
  // // calendar
  // Feature(
  //   colorHash: 300,
  //   name: (context) => context.intl.serviceCalendar,
  //   description: (context) => context.intl.serviceCalendarDescription,
  //   // routeName: kPageCalendar,
  //   routeName: kPageDashboard, // not yet implemented, go to dashboard instead
  //   icon: Icons.calendar_month,
  // ),
  // // documents
  // Feature(
  //   colorHash: 800,
  //   name: (context) => context.intl.serviceDocuments,
  //   description: (context) => context.intl.serviceDocumentsDescription,
  //   // routeName: kPageDocuments,
  //   routeName: kPageDashboard, // not yet implemented, go to dashboard instead
  //   icon: Icons.file_copy,
  // ),
  // // vehicles
  // Feature(
  //   colorHash: 1600,
  //   name: (context) => context.intl.serviceVehicles,
  //   description: (context) => context.intl.serviceVehiclesDescription,
  //   // routeName: kPageVehicles,
  //   routeName: kPageDashboard, // not yet implemented, go to dashboard instead
  //   icon: Icons.directions_car,
  // ),
  // projects
  // Feature(
  //   colorHash: 1800,
  //   name: (context) => context.intl.serviceProjects,
  //   description: (context) => context.intl.serviceProjectsDescription,
  //   // routeName: kPageProjects,
  //   routeName: kPageDashboard, // not yet implemented, go to dashboard instead
  //   icon: Icons.work,
  // ),
  // leave
  Feature(
    colorHash: 2000,
    name: (context) => context.intl.leaveAndPermission,
    description: (context) => context.intl.leaveAndPermissionDescription,
    routeName: kPageLeave,
    icon: Icons.umbrella,
  ),
];
