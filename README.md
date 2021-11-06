# Liveness CameraX

An easy way to consume results from [CameraX](https://developer.android.com/training/camerax)
and [Firebase Face Detection](https://developers.google.com/ml-kit/vision/face-detection) as an API. With simple steps, you can challenge your user before sinding pictures to your services.

## Installation

### Configure root build.gradle (jitpack.io):

```
allprojects {  
    repositories {  
        ...  
        maven { url 'https://jitpack.io' }  
    }  
}  
```

### Add dependencie to your project build.gradle:

```
dependencies {
  implementation 'implementation 'com.github.arturschaefer:liveness-camerax-android:TAG'
}
```

## Start to use

You can start and listen the result passing its `Context` and a callback listener.

```
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.btnLiveness.setOnClickListener {
            LivenessEntryPoint.startLiveness(context = this) { result ->
                result.createdByUser?.let { photoResult ->
                    // Photo taken by the user
                }
                result.createdBySteps?.let { listOfPhotos ->
                    // List of photos taken by the user
                }
                result.error?.let { error ->
                    // Handle errors
                }
            }
        }
    }
}
```
## Customization

You can customize all the assets and dimensions. It's only required to create the resources with the same keys in your project.

## Challenges to request

There are several steps to challenge the user, and you can check it inside [StepLiveness](https://github.dev/arturschaefer/liveness-camerax-android/blob/50f0023f3b247cdd6146f489db21d7e7008d7201/livenesscamerax/src/main/java/com/schaefer/livenesscamerax/domain/model/StepLiveness.kt#L1). They are:

* STEP_LUMINOSITY: Luminosity captured in real-time. We get the number of white pixels in every
  frame captured to define the luminosity.
* STEP_SMILE: We consider a smiling probability to pass it. Some libraries called it happiness
  probability too.ç
* STEP_BLINK: After both eyes opened, we checked if they blinked. Use it carefully, because you need
  to think about accessibility.
* STEP_HEAD_FRONTAL, STEP_HEAD_LEFT, STEP_HEAD_RIGHT: Each step references the head's position
  inside an Euler's graphic. These heads steps could be confunsing to the user.

## How does its work

We have a preset list of steps to configure our liveness. After the user completes each step, a
picture will be saved automatically on the device. When the user passes all the challenges we will
show a button on the UI. It will allow him to take a picture and finalize the process. After the
user finalizes the process you'll receive as a result the picture taken by the user and a list of
files automatically created.

## Common examples

In scenarios when a selfie is required to improve security, we can use this library to check the user's liveness before
taking the picture. It's a way to avoid several frauds and check pictures integrity before being
validated on the server side, reducing costs and saving our time. The bests examples to try this
library are SignUp, device authorization, or even password reset.


## Why it is built like that

With this kind of result, which is a list of photos in Base, you can choose what picture show to the user on UI as a result. I
recommend that he takes for this. The most common reason to do this is to require confirmation
before send to your backend. With the list of files automatically taken you can compare all photos
and validate if he is the same picture in all of those photos.


### Constraints

We have a constraint about the number of faces recognized at the same time. It's only possible to
proceed with a single face recognized. In the happiest scenario, your user shouldn't have any
problem. It is important to know about the worst-case scenario. When a second face is recognized we
delete all the completed steps and start over. It means the completed steps and taken pictures will
be canceled/removed, and your user needs to pass through the challenges again.

### Libary tech stuff

It's simple like that to use.
For more technical information you can check the [README.md]() inside the library.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process
for submitting pull requests to us.

## Authors

See also the list
of [contributors](https://github.com/arturschaefer/Liveness-Android-ML-Kit/graphs/contributors) who
participated in this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for
details
