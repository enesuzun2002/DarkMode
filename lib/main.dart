import 'package:dark_mode/theme.dart';
import 'package:flutter/material.dart';

import 'package:flutter/services.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'DarkMode',
      themeMode: ThemeMode.system,
      theme: lightTheme,
      darkTheme: darkTheme,
      debugShowCheckedModeBanner: false,
      home: const MainScreen(),
    );
  }
}

class MainScreen extends StatefulWidget {
  const MainScreen({Key? key}) : super(key: key);

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  @override
  Widget build(BuildContext context) {
    var darkModeChannel = const MethodChannel('enesmuratuzun.com/darkMode');
    late int darkMode;
    return Scaffold(
      body: InkWell(
        // To disable ripple effect
        splashColor: Theme.of(context).colorScheme.background,
        highlightColor: Theme.of(context).colorScheme.background,
        onTap: () async {
          darkMode = await darkModeChannel.invokeMethod('getDarkMode');
          if (darkMode == 1) {
            await darkModeChannel.invokeMethod('setDarkMode', {'darkMode': 2});
            darkMode = await darkModeChannel.invokeMethod('getDarkMode');
          } else {
            await darkModeChannel.invokeMethod('setDarkMode', {'darkMode': 1});
            darkMode = await darkModeChannel.invokeMethod('getDarkMode');
          }
          setState(() {});
        },
        child: FutureBuilder<dynamic>(
            future: darkModeChannel.invokeMethod("getDarkMode"),
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                darkMode = snapshot.data;
                return Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Column(
                        children: [
                          Icon(
                            darkMode == 2
                                ? Icons.dark_mode_outlined
                                : Icons.light_mode_outlined,
                            size: 100.0,
                          ),
                          const SizedBox(
                            height: 10.0,
                          ),
                          Text(darkMode == 2 ? "Dark Mode" : "Light Mode"),
                        ],
                      ),
                    ],
                  ),
                );
              } else {
                return const Center(
                  child: CircularProgressIndicator(),
                );
              }
            }),
      ),
    );
  }
}
