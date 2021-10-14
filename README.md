# Liveness CameraX

This project intends to provide an easy way to get proof of life before getting pictures with [CameraX](https://developer.android.com/training/camerax) and [Firebase Face Detection](https://developers.google.com/ml-kit/vision/face-detection).

## Common usages

In scenarios when a selfie is required, we can use this library to check the user's liveness before taking the picture.
It's a way to avoid several frauds and check pictures' integrity before being validated on the server side, reducing costs and saving our time.
The bests examples to try this library are SignUp, device authorization, or even password reset.

## How does its work

We have a preset list of steps to configure our liveness. After the user completes each step, a picture will be saved automatically on the device.
When the user passes all the challenges we will show a button on the UI. It will allow him to take a picture and finalize the process.
After the user finalizes the process you'll receive as a result the picture taken by the user and a list of files automatically created.

### Why it is built like that

With this kind of result you can choose what picture shows to the user on UI as a result, we recommend that he takes for this. The most common reason to do this is to require confirmation before send to your backend.
With the list of files automatically taken you can compare all photos and validate if he is the same picture in all of those.

### Possible steps

There are several steps to challenge the user. They are:
* STEP_LUMINOSITY: Luminosity captured in real-time. We get the number of white pixels in every frame captured to define the luminosity.
* STEP_HEAD_FRONTAL, STEP_HEAD_LEFT, STEP_HEAD_RIGHT: Each step references the head's position inside an Euler's graphic.
* STEP_SMILE: We consider a smiling probability to pass it. Some libraries called it happiness probability too.
* STEP_BLINK: After both eyes opened, we checked if they blinked. Use it carefully, because you need to think about accessibility.

### Constraints

We have a constraint about the number of faces recognized at the same time. It's only possible to proceed with a single face recognized.
In the happiest scenario, your user shouldn't have any problem.
It is important to know about the worst-case scenario.  When a second face is recognized we delete all the completed steps and start over.
It means the completed steps and taken pictures will be canceled/removed, and your user needs to pass through the challenges again.

## Dependencies

You can check all the [dependencies versions](https://github.com/arturschaefer/Liveness-Android-ML-Kit/blob/main/dependencies.gradle), and the [libraries](https://github.com/arturschaefer/Liveness-Android-ML-Kit/blob/main/livenesscamerax/build.gradle) used inside the project, but keep in mind:
* minSDK 19 it's the minimal version required to use Firebase ML Kit.
* targetSDKVersion and compileSDKVersion are set to 31 already
* [Fragment 1.3.6](https://developer.android.com/jetpack/androidx/releases/fragment#1.3.6) and [Activity 1.3.1](https://developer.android.com/jetpack/androidx/releases/activity#1.3.1) are used. Keep it in mind if you are using old versions of this in your code.

### How to use it
[WIP]

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

See also the list of [contributors](https://github.com/arturschaefer/Liveness-Android-ML-Kit/graphs/contributors) who participated in this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details
