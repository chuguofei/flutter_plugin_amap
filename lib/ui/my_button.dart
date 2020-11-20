import 'package:flutter/material.dart';

/**
 * @author: guofei
 * @ProjectName: flutter_plugin_amap
 * @Package: ui
 * @ClassName: button
 * @date: 2020/11/20 15:40
 * @Description:
 */

class MyButton extends StatelessWidget {
  final String text;
  final VoidCallback onTap;

  MyButton({this.text, this.onTap});

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: this.onTap,
      child: Container(
        height: 40,
        margin: EdgeInsets.symmetric(vertical: 5, horizontal: 5),
        decoration: BoxDecoration(
          border:
              Border.all(color: Colors.blueAccent.withAlpha(60), width: 1.0),
          borderRadius: BorderRadius.all(
            Radius.circular(5),
          ),
        ),
        child: Container(
          margin: EdgeInsets.all(8),
          child: Text(text),
        ),
      ),
    );
  }
}
