[WIP]

* Explain how was implemented
* Explain the architecture
* Explain how to change
* Passing through CI

## Dependencies

You can check all
the [dependencies versions](https://github.com/arturschaefer/Liveness-Android-ML-Kit/blob/main/dependencies.gradle)
, and
the [libraries](https://github.com/arturschaefer/Liveness-Android-ML-Kit/blob/main/livenesscamerax/build.gradle)
used inside the project, but keep in mind:

* minSDK 21
* targetSDKVersion and compileSDKVersion are set to 32 already
* [Fragment 1.3.6](https://developer.android.com/jetpack/androidx/releases/fragment#1.3.6)
  and [Activity 1.3.1](https://developer.android.com/jetpack/androidx/releases/activity#1.3.1) are
  used. Keep it in mind if you are using old versions of this in your code.