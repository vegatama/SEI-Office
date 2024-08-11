import 'dart:ui';

import 'package:flutter/material.dart';

import '../theme/theme.dart';

class PhotoPreview extends StatefulWidget {
  final Widget child;
  final VoidCallback? onSave;

  const PhotoPreview({super.key, required this.child, this.onSave});

  @override
  State<PhotoPreview> createState() => _PhotoPreviewState();
}

class _PhotoPreviewState extends State<PhotoPreview> {
  final TransformationController _controller = TransformationController();
  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Positioned.fill(
          child: BackdropFilter(
              filter: ImageFilter.blur(sigmaX: 5, sigmaY: 5),
              child: Container(color: Colors.black.withOpacity(0.5))),
        ),
        // photo preview
        Positioned.fill(
          child: InteractiveViewer(
            transformationController: _controller,
            child: widget.child,
          ),
        ),
        // close button at top right
        Positioned(
          top: 24,
          right: 24,
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              if (widget.onSave != null)
                GestureDetector(
                  onTap: widget.onSave,
                  child: const Icon(
                    Icons.save_alt,
                    color: Colors.white,
                    size: 32,
                  ),
                ),
              if (widget.onSave != null) const SizedBox(width: 16),
              GestureDetector(
                onTap: () {
                  Navigator.of(context).pop();
                },
                child: const Icon(
                  Icons.close,
                  color: Colors.white,
                  size: 32,
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

class PhotoBox extends StatelessWidget {
  final ImageProvider image;
  const PhotoBox({super.key, required this.image});

  @override
  Widget build(BuildContext context) {
    final theme = SEITheme.of(context);
    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onTap: () async {
        showDialog(
          context: context,
          builder: (context) {
            return PhotoPreview(
                child: Image(
                    image: image,
                    errorBuilder: (context, error, stackTrace) {
                      return Padding(
                        padding: const EdgeInsets.all(32.0),
                        child: Center(
                          child: Column(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Row(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  Icon(Icons.error,
                                      color: theme
                                          .colorScheme.secondaryForeground),
                                  const SizedBox(width: 8),
                                  Text(
                                    'Failed to load image',
                                    style: theme.typography.sm.copyWith(
                                      color:
                                          theme.colorScheme.secondaryForeground,
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 8),
                              Text(
                                error.toString(),
                                textAlign: TextAlign.center,
                                style: theme.typography.xs.copyWith(
                                  color: theme.colorScheme.secondaryForeground
                                      .withOpacity(0.5),
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    }));
          },
        );
      },
      child: Container(
        clipBehavior: Clip.antiAlias,
        // borderRadius: BorderRadius.circular(12),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(12),
          color: theme.colorScheme.surfaceMuted,
        ),
        child: Image(
          image: image,
          fit: BoxFit.cover,
          errorBuilder: (context, error, stackTrace) {
            return Center(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(Icons.error,
                          color: theme.colorScheme.secondaryForeground),
                      const SizedBox(width: 8),
                      Text(
                        'Failed to load image',
                        style: theme.typography.sm.copyWith(
                          color: theme.colorScheme.secondaryForeground,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 8),
                  Text(
                    error.toString(),
                    textAlign: TextAlign.center,
                    style: theme.typography.xs.copyWith(
                      color: theme.colorScheme.secondaryForeground
                          .withOpacity(0.5),
                    ),
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }
}
