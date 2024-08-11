import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:seioffice/src/components/dashed.dart';
import 'package:seioffice/src/components/image.dart';
import 'package:seioffice/src/util.dart';

import '../theme/theme.dart';

class StylizedTextArea extends StatelessWidget {
  final TextField child;

  const StylizedTextArea({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final parent = Theme.of(context);
    return Theme(
      data: parent.copyWith(
        textSelectionTheme: TextSelectionThemeData(
          cursorColor: theme.colorScheme.primary,
          selectionColor: theme.colorScheme.primary.withOpacity(0.3),
          selectionHandleColor: theme.colorScheme.primary,
        ),
        textTheme: TextTheme(
          bodyLarge: theme.typography.lg.copyWith(
            color: theme.colorScheme.secondaryForeground,
            fontWeight: fontMedium,
          ),
        ),
        inputDecorationTheme: InputDecorationTheme(
          fillColor: Colors.transparent,
          filled: true,
          border: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.primary,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          disabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          enabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          errorBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.destructive,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          focusedErrorBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.destructive,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          contentPadding:
              const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
          prefixIconColor: theme.colorScheme.subtitleForeground,
          suffixIconColor: theme.colorScheme.subtitleForeground,
          hintStyle: theme.typography.lg.copyWith(
            color: theme.colorScheme.muted,
          ),
          labelStyle: theme.typography.md.copyWith(
            color: theme.colorScheme.muted,
          ),
        ),
      ),
      child: TextField(
        // copy all properties from widget.child
        controller: child.controller,
        focusNode: child.focusNode,
        decoration: (child.decoration ?? const InputDecoration()).copyWith(
          prefixIconConstraints: const BoxConstraints(
            minWidth: 64,
          ),
          suffixIconConstraints: const BoxConstraints(
            minWidth: 64,
          ),
        ),
        keyboardType: child.keyboardType,
        textInputAction: child.textInputAction,
        textCapitalization: child.textCapitalization,
        style: child.style,
        strutStyle: child.strutStyle,
        textAlign: child.textAlign,
        textAlignVertical: child.textAlignVertical,
        textDirection: child.textDirection,
        readOnly: child.readOnly,
        toolbarOptions: child.toolbarOptions,
        showCursor: child.showCursor,
        autofocus: child.autofocus,
        obscureText: child.obscureText,
        autocorrect: child.autocorrect,
        smartDashesType: child.smartDashesType,
        smartQuotesType: child.smartQuotesType,
        enableSuggestions: child.enableSuggestions,
        maxLines: child.maxLines,
        minLines: child.minLines,
        expands: child.expands,
        maxLength: child.maxLength,
        maxLengthEnforcement: child.maxLengthEnforcement,
        onChanged: child.onChanged,
        onEditingComplete: child.onEditingComplete,
        onSubmitted: child.onSubmitted,
        inputFormatters: child.inputFormatters,
        enabled: child.enabled,
        cursorWidth: child.cursorWidth,
        cursorHeight: child.cursorHeight,
        cursorRadius: child.cursorRadius,
        cursorColor: child.cursorColor,
        selectionHeightStyle: child.selectionHeightStyle,
        selectionWidthStyle: child.selectionWidthStyle,
        keyboardAppearance: child.keyboardAppearance,
        scrollPadding: child.scrollPadding,
        dragStartBehavior: child.dragStartBehavior,
        enableInteractiveSelection: child.enableInteractiveSelection,
        selectionControls: child.selectionControls,
        onTap: child.onTap,
        buildCounter: child.buildCounter,
        scrollController: child.scrollController,
        scrollPhysics: child.scrollPhysics,
        autofillHints: child.autofillHints,
        restorationId: child.restorationId,
        clipBehavior: child.clipBehavior,
        canRequestFocus: child.canRequestFocus,
        contentInsertionConfiguration: child.contentInsertionConfiguration,
        contextMenuBuilder: child.contextMenuBuilder,
        cursorErrorColor: child.cursorErrorColor,
        cursorOpacityAnimates: child.cursorOpacityAnimates,
        enableIMEPersonalizedLearning: child.enableIMEPersonalizedLearning,
        magnifierConfiguration: child.magnifierConfiguration,
        mouseCursor: child.mouseCursor,
        obscuringCharacter: child.obscuringCharacter,
        onAppPrivateCommand: child.onAppPrivateCommand,
        onTapAlwaysCalled: child.onTapAlwaysCalled,
        onTapOutside: child.onTapOutside,
        scribbleEnabled: child.scribbleEnabled,
        spellCheckConfiguration: child.spellCheckConfiguration,
        statesController: child.statesController,
        undoController: child.undoController,
      ),
    );
  }
}

/// Wraps into TextField with SEI theme
class StylizedTextField extends StatefulWidget {
  final TextField child;

  const StylizedTextField({super.key, required this.child});

  @override
  State<StylizedTextField> createState() => _StylizedTextFieldState();
}

class StylizedTextFormArea extends StatefulWidget {
  final TextFormField child;

  const StylizedTextFormArea({super.key, required this.child});

  @override
  State<StylizedTextFormArea> createState() => _StylizedTextFormAreaState();
}

class _StylizedTextFormAreaState extends State<StylizedTextFormArea> {
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final parent = Theme.of(context);
    return Theme(
      data: parent.copyWith(
        textSelectionTheme: TextSelectionThemeData(
          cursorColor: theme.colorScheme.primary,
          selectionColor: theme.colorScheme.primary.withOpacity(0.3),
          selectionHandleColor: theme.colorScheme.primary,
        ),
        textTheme: TextTheme(
          bodyLarge: theme.typography.lg.copyWith(
            color: theme.colorScheme.secondaryForeground,
            fontWeight: fontMedium,
          ),
        ),
        inputDecorationTheme: InputDecorationTheme(
          fillColor: Colors.transparent,
          filled: true,
          border: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.primary,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          disabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          enabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          errorBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.destructive,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          focusedErrorBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.destructive,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(12),
          ),
          contentPadding:
              const EdgeInsets.symmetric(horizontal: 16, vertical: 16),
          prefixIconColor: theme.colorScheme.subtitleForeground,
          suffixIconColor: theme.colorScheme.subtitleForeground,
          hintStyle: theme.typography.lg.copyWith(
            color: theme.colorScheme.muted,
          ),
          labelStyle: theme.typography.md.copyWith(
            color: theme.colorScheme.muted,
          ),
        ),
      ),
      child: widget.child,
    );
  }
}

class StylizedTextFormField extends StatefulWidget {
  final TextFormField child;

  const StylizedTextFormField({super.key, required this.child});

  @override
  State<StylizedTextFormField> createState() => _StylizedTextFormFieldState();
}

class _StylizedTextFormFieldState extends State<StylizedTextFormField> {
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final parent = Theme.of(context);
    return Theme(
      data: parent.copyWith(
        textSelectionTheme: TextSelectionThemeData(
          cursorColor: theme.colorScheme.primary,
          selectionColor: theme.colorScheme.primary.withOpacity(0.3),
          selectionHandleColor: theme.colorScheme.primary,
        ),
        textTheme: TextTheme(
          bodyLarge: theme.typography.lg.copyWith(
            color: theme.colorScheme.secondaryForeground,
            fontWeight: fontMedium,
          ),
        ),
        inputDecorationTheme: InputDecorationTheme(
          fillColor: Colors.transparent,
          filled: true,
          border: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.primary,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          disabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          enabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          errorBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.destructive,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          focusedErrorBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.destructive,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          contentPadding:
              const EdgeInsets.symmetric(horizontal: 24, vertical: 18),
          prefixIconColor: theme.colorScheme.subtitleForeground,
          suffixIconColor: theme.colorScheme.subtitleForeground,
          hintStyle: theme.typography.lg.copyWith(
            color: theme.colorScheme.muted,
          ),
          labelStyle: theme.typography.md.copyWith(
            color: theme.colorScheme.muted,
          ),
        ),
      ),
      child: widget.child,
    );
  }
}

class _StylizedTextFieldState extends State<StylizedTextField> {
  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    final parent = Theme.of(context);
    return Theme(
      data: parent.copyWith(
        textSelectionTheme: TextSelectionThemeData(
          cursorColor: theme.colorScheme.primary,
          selectionColor: theme.colorScheme.primary.withOpacity(0.3),
          selectionHandleColor: theme.colorScheme.primary,
        ),
        textTheme: TextTheme(
          bodyLarge: theme.typography.lg.copyWith(
            color: theme.colorScheme.secondaryForeground,
            fontWeight: fontMedium,
          ),
        ),
        inputDecorationTheme: InputDecorationTheme(
          fillColor: Colors.transparent,
          filled: true,
          border: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.primary,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          disabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          enabledBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.muted,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          errorBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.destructive,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          focusedErrorBorder: OutlineInputBorder(
            borderSide: BorderSide(
              color: theme.colorScheme.destructive,
              width: 2,
            ),
            borderRadius: BorderRadius.circular(50),
          ),
          contentPadding:
              const EdgeInsets.symmetric(horizontal: 24, vertical: 18),
          prefixIconColor: theme.colorScheme.subtitleForeground,
          suffixIconColor: theme.colorScheme.subtitleForeground,
          hintStyle: theme.typography.lg.copyWith(
            color: theme.colorScheme.muted,
          ),
          labelStyle: theme.typography.md.copyWith(
            color: theme.colorScheme.muted,
          ),
        ),
      ),
      child: TextField(
        // copy all properties from widget.child
        controller: widget.child.controller,
        focusNode: widget.child.focusNode,
        decoration:
            (widget.child.decoration ?? const InputDecoration()).copyWith(
          prefixIconConstraints: const BoxConstraints(
            minWidth: 64,
          ),
          suffixIconConstraints: const BoxConstraints(
            minWidth: 64,
          ),
        ),
        keyboardType: widget.child.keyboardType,
        textInputAction: widget.child.textInputAction,
        textCapitalization: widget.child.textCapitalization,
        style: widget.child.style,
        strutStyle: widget.child.strutStyle,
        textAlign: widget.child.textAlign,
        textAlignVertical: widget.child.textAlignVertical,
        textDirection: widget.child.textDirection,
        readOnly: widget.child.readOnly,
        toolbarOptions: widget.child.toolbarOptions,
        showCursor: widget.child.showCursor,
        autofocus: widget.child.autofocus,
        obscureText: widget.child.obscureText,
        autocorrect: widget.child.autocorrect,
        smartDashesType: widget.child.smartDashesType,
        smartQuotesType: widget.child.smartQuotesType,
        enableSuggestions: widget.child.enableSuggestions,
        maxLines: widget.child.maxLines,
        minLines: widget.child.minLines,
        expands: widget.child.expands,
        maxLength: widget.child.maxLength,
        maxLengthEnforcement: widget.child.maxLengthEnforcement,
        onChanged: widget.child.onChanged,
        onEditingComplete: widget.child.onEditingComplete,
        onSubmitted: widget.child.onSubmitted,
        inputFormatters: widget.child.inputFormatters,
        enabled: widget.child.enabled,
        cursorWidth: widget.child.cursorWidth,
        cursorHeight: widget.child.cursorHeight,
        cursorRadius: widget.child.cursorRadius,
        cursorColor: widget.child.cursorColor,
        selectionHeightStyle: widget.child.selectionHeightStyle,
        selectionWidthStyle: widget.child.selectionWidthStyle,
        keyboardAppearance: widget.child.keyboardAppearance,
        scrollPadding: widget.child.scrollPadding,
        dragStartBehavior: widget.child.dragStartBehavior,
        enableInteractiveSelection: widget.child.enableInteractiveSelection,
        selectionControls: widget.child.selectionControls,
        onTap: widget.child.onTap,
        buildCounter: widget.child.buildCounter,
        scrollController: widget.child.scrollController,
        scrollPhysics: widget.child.scrollPhysics,
        autofillHints: widget.child.autofillHints,
        restorationId: widget.child.restorationId,
        clipBehavior: widget.child.clipBehavior,
        canRequestFocus: widget.child.canRequestFocus,
        contentInsertionConfiguration:
            widget.child.contentInsertionConfiguration,
        contextMenuBuilder: widget.child.contextMenuBuilder,
        cursorErrorColor: widget.child.cursorErrorColor,
        cursorOpacityAnimates: widget.child.cursorOpacityAnimates,
        enableIMEPersonalizedLearning:
            widget.child.enableIMEPersonalizedLearning,
        magnifierConfiguration: widget.child.magnifierConfiguration,
        mouseCursor: widget.child.mouseCursor,
        obscuringCharacter: widget.child.obscuringCharacter,
        onAppPrivateCommand: widget.child.onAppPrivateCommand,
        onTapAlwaysCalled: widget.child.onTapAlwaysCalled,
        onTapOutside: widget.child.onTapOutside,
        scribbleEnabled: widget.child.scribbleEnabled,
        spellCheckConfiguration: widget.child.spellCheckConfiguration,
        statesController: widget.child.statesController,
        undoController: widget.child.undoController,
      ),
    );
  }
}

class PhotoUploadInput extends StatelessWidget {
  final ValueChanged<XFile?> onPhotoSelected;
  final ImageProvider? image;
  const PhotoUploadInput(
      {super.key, required this.onPhotoSelected, this.image});

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onTap: () async {
        if (image == null) {
          final pickedImage =
              await ImagePicker().pickImage(source: ImageSource.camera);
          if (pickedImage != null) {
            onPhotoSelected(pickedImage);
          }
        } else {
          showDialog(
            context: context,
            builder: (context) {
              return PhotoPreview(child: Image(image: image!));
            },
          );
        }
      },
      child: image != null
          ? ClipRRect(
              borderRadius: BorderRadius.circular(12),
              child: Stack(
                children: [
                  Image(
                    image: image!,
                    fit: BoxFit.cover,
                  ),
                  Positioned(
                    bottom: 24,
                    right: 24,
                    child: Container(
                      decoration: BoxDecoration(
                        color: theme.colorScheme.secondary,
                        borderRadius: BorderRadius.circular(50),
                        boxShadow: [
                          BoxShadow(
                            color: theme.colorScheme.secondaryForeground
                                .withOpacity(0.3),
                            blurRadius: 8,
                            spreadRadius: 2,
                          ),
                        ],
                      ),
                      padding: const EdgeInsets.all(8),
                      child: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          IconButton(
                            onPressed: () async {
                              final pickedImage = await ImagePicker()
                                  .pickImage(source: ImageSource.camera);
                              if (pickedImage != null) {
                                onPhotoSelected(pickedImage);
                              }
                            },
                            icon: Icon(
                              Icons.edit,
                              color: theme.colorScheme.secondaryForeground,
                            ),
                          ),
                          const SizedBox(width: 8),
                          IconButton(
                            onPressed: () {
                              onPhotoSelected(null);
                            },
                            icon: Icon(
                              Icons.delete,
                              color: theme.colorScheme.destructive,
                            ),
                          ),
                        ],
                      ),
                    ),
                  )
                ],
              ),
            )
          : Dashed(
              thickness: 2,
              borderRadius: BorderRadius.circular(12),
              color: theme.colorScheme.muted,
              child: Padding(
                padding: const EdgeInsets.all(48.0),
                child: Center(
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(
                        Icons.add_a_photo_outlined,
                        size: 24,
                        color: theme.colorScheme.muted,
                      ),
                      const SizedBox(width: 8),
                      Text(
                        'Tambahkan Foto',
                        style: theme.typography.md.copyWith(
                          fontWeight: fontMedium,
                          color: theme.colorScheme.muted,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
    );
  }
}
