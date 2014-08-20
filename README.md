Facebook Login Sample
=========
This sample project will show how to use [Facebook Android SDK][facebook sdk] to login and logout from the social network using a custom button and saving the data you might need (in this project we only save user full name and mail address).

Setup the project
--

- Download Facebook Android SDK from [Developer site][facebook sdk] and uncompress it
- In your Android Studio project, add a new module that pointing to the *facebook* subfolder inside the SDK directory
- Once the module has been added to your project, Facebook SDK will most probably lack a few dependencies, that are contained in its *library* subfolder: to overcome this issue, simply open *facebook/build.gradle* file in your project and paste inside the content of the one you can find in this repository in the same location, that will actually add those dependencies to the SDK.
- Sync your Gradle file and start coding.
---
> **Note**: if you experience issues with *minSdkVersion* or *targetSdkVersion*, simply change them according to your *app/build.gradle* file.

---
Thanks
--
- StackOverflow user [Rikin Prajapati][hint_logout] for hints about the logout method
- StackOverflow User [Cristiana214][hint_gradle] for hints about the Gradle issue

[facebook sdk]:https://developers.facebook.com/resources/facebook-android-sdk-current.zip
[hint_logout]:http://stackoverflow.com/questions/14328148/how-to-programmatically-logout-from-facebook-sdk-3-0-without-using-facebook-logi/18584885#18584885
[hint_gradle]:http://stackoverflow.com/questions/24466921/android-studio-0-8-1-how-to-use-facebook-sdk/24573831#24573831