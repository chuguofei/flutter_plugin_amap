import 'package:flutter/material.dart';

/// 自定义dialog的模板
class BaseDialog extends StatelessWidget {
  const BaseDialog(
      {Key key,
      this.title,
      this.onPressed,
      this.onCancel,
      this.hiddenTitle = false,
      this.autoClose = false,
      @required this.child})
      : super(key: key);

  final String title;
  final VoidCallback onPressed;
  final VoidCallback onCancel;
  final Widget child;
  final bool hiddenTitle;
  final bool autoClose; // 自动关闭

  @override
  Widget build(BuildContext context) {
    Widget dialogTitle = Visibility(
      visible: !hiddenTitle,
      child: Padding(
        padding: const EdgeInsets.only(bottom: 8.0),
        child: Text(
          hiddenTitle ? '' : title,
          style: TextStyle(),
        ),
      ),
    );

    Widget bottomButton = Row(
      children: <Widget>[
        _DialogButton(
          text: '取消',
          onPressed: () {
            Navigator.pop(context);
            if (onCancel != null) {
              onCancel();
            }
          },
        ),
        const SizedBox(
          height: 48.0,
          width: 0.6,
          child: VerticalDivider(),
        ),
        _DialogButton(
          text: '确定',
          textColor: Colors.blue,
          onPressed: () {
            if (autoClose) Navigator.pop(context);
            onPressed();
          },
        ),
      ],
    );

    Widget body = Material(
      borderRadius: BorderRadius.circular(8.0),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          SizedBox(height: 10),
          dialogTitle,
          Flexible(child: child),
          SizedBox(height: 10),
          Divider(height: 1,),
          bottomButton,
        ],
      ),
    );

    return AnimatedPadding(
      padding: MediaQuery.of(context).viewInsets +
          const EdgeInsets.symmetric(horizontal: 10.0, vertical: 24.0),
      duration: const Duration(milliseconds: 120),
      curve: Curves.easeInCubic,
      child: MediaQuery.removeViewInsets(
        removeLeft: true,
        removeTop: true,
        removeRight: true,
        removeBottom: true,
        context: context,
        child: Center(
          child: SizedBox(
            width: 300.0,
            child: body,
          ),
        ),
      ),
    );
  }
}

class _DialogButton extends StatelessWidget {
  const _DialogButton({
    Key key,
    this.text,
    this.textColor,
    this.onPressed,
  }) : super(key: key);

  final String text;
  final Color textColor;
  final VoidCallback onPressed;

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: SizedBox(
        height: 48.0,
        child: FlatButton(
          child: Text(
            text,
          ),
          textColor: textColor,
          onPressed: onPressed,
        ),
      ),
    );
  }
}
