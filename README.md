Facebook Login Library
=========
This sample project will show how to use [Facebook Android SDK][facebook sdk] to login and logout from the social network using a custom button and saving the data you might need (in this project we only save user full name and mail address) using the library we created to make an easier implementation of Facebook Android SDK.

Setup the project
--

- Download Facebook Android SDK from [Developer site][facebook sdk] and uncompress it
- Download the *library* folder and add it as a Module of your app
- Once the module has been added to your project, Facebook SDK will most probably lack a few dependencies, that are contained in its *library* subfolder: to overcome this issue, simply open *facebook/build.gradle* file in your project and paste inside the content of the one you can find in this repository in the same location, that will actually add those dependencies to the SDK.

**Note**: if you experience issues with `minSdkVersion` or `targetSdkVersion`, simply change them according to your *app/build.gradle* file.

Usage
--

This library comes with a `FacebookLoginFragment` and a `FacebookLoginActivity` ready to be extended: by doing this, you will be asked to implement the following methods:
- `onSessionOpen()`: this method is called every time a **Session Opening is completed** (for instance, this method will be called on *successfull login*), including during the `onResume()` method of your `Fragment` or `Activity` if your app is called from links.
- `onSessionClose()`: this method is called every time a **Session Closing is completed** (for instance, this method will be called on *successfull logout*), including during the `onResume()` method of your `Fragment` or `Activity` if your app is called from links.
- `onSessionEvent()`: this method is called every time a **Session Change is completed** (but the Session is not yet _opened nor closed_), including during the `onResume()` method of your `Fragment` or `Activity` if your app is called from links.
- `onSessionNullResume()`: this method is called during the `onResume()` phase of your `Fragment` or `Activity` if your app is called from links and the Facebook Session is null. (*Note*: one case in which this method can be invoked is when your app is launched from a link but the user did not logged in for the first time yet)

Requesting Login and Logout
--

Once you successfully extended `FacebookLoginFragment` or `FacebookLoginActivity`, you will have the possibility to invoke the login and logout methods
- `public Session login(String... profiles) throws FacebookHelperException`
- `public void logout() throws FacebookHelperException`

**Note**: both methods will throw a `FacebookHelperException` in case the `Fragment`is not attached or it has not been created. 
Refer to the [sample][sample] project in order to have a clear look on how to use this library or [download the APK][apk] from this repository.

License
--
This library is provided as is under the terms of [MIT License][license]

Thanks
--
- StackOverflow user [Rikin Prajapati][hint_logout] for hints about the logout method
- StackOverflow User [Cristiana214][hint_gradle] for hints about the Gradle issue

[facebook sdk]:https://developers.facebook.com/resources/facebook-android-sdk-current.zip
[hint_logout]:http://stackoverflow.com/questions/14328148/how-to-programmatically-logout-from-facebook-sdk-3-0-without-using-facebook-logi/18584885#18584885
[hint_gradle]:http://stackoverflow.com/questions/24466921/android-studio-0-8-1-how-to-use-facebook-sdk/24573831#24573831
[sample]:https://github.com/txtr/Facebook-Login-Sample/tree/master/app
[apk]:https://github.com/txtr/Facebook-Login-Sample/blob/master/txtr_flh.apk
[license]:https://github.com/txtr/Facebook-Login-Sample/blob/master/LICENSE.txt