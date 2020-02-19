# AEDMapping_Mobile


AEDMapping mobile application has been developed to assist patients in case of cardiac arrest. 
Minimum API level is 23.


### Features
- Location of the nearest defibrillators
- Addition of new defibrillators
- CPR Training
- Management of Citizen Responders

### Mobile Side Tech Stack
- [Butterknife](https://jakewharton.github.io/butterknife/) - Field and method binding for Android views
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java
- [Material Design](https://material.io/develop/android/) - Build beautiful, usable products using Material Components for Android
- [Google Sign-in](https://developers.google.com/identity/sign-in/android/start-integrating) - Start Integrating Google Sign-In into Your Android App
- [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk/intro) - Maps SDK for Android 
- [Adobe XD](https://www.adobe.com/tr/products/xd.html) - Let’s XD together.

#### build.gradle
```gradle
dependencies {
        classpath 'com.android.tools.build:gradle:3.5.1'
        classpath 'com.jakewharton:butterknife-gradle-plugin:10.2.1'
        classpath 'com.google.gms:google-services:4.3.2'
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
```
#### build.gradle(module)
```gradle
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'androidx.appcompat:appcompat:1.0.2'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'com.google.android.gms:play-services-auth:17.0.0'
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    implementation 'com.squareup.retrofit2:retrofit:2.6.2'
    implementation 'com.squareup.retrofit2:converter-gson:2.6.2'
    implementation 'com.jakewharton:butterknife:10.2.1'
    implementation 'com.google.android.material:material:1.2.0-alpha03'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.0.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.1'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.0'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.1.1'
}
```
#### gradle.properties
```
GOOGLE_MAPS_API_KEY=<YOUR_API_KEY>
```
#### res/values/strings.xml
```xml
...
<string name="google_maps_key"><!--YOUR GOOGLE MAPS API FOR ANDROID KEY--></string>
<string name="google_client_id"><!--YOUR SIGN IN WITH GOOGLE KEY--></string>
...
```
#### Constant.java
```java
public static final String YOUR_PHP_WEBSITE_LINK="";
```

### GIFs

![Add/Update/Report Defibrillator](/images/add-update-report-defibrillator.gif)
![Learning CPR with Embedded Video](/images/learning-cpr-with-embedded-video.gif)
![Responders](/images/responders.gif)


### Screenshots
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/signin.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/googlesignin.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/signup.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/locationpermission.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/showall.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/selectlocation.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/adddefib.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/update.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/report.jpeg" width=300>
<img src="https://github.com/cnkay/aedmapping_mobile/blob/master/images/cpr.jpeg" width=300>


License
----

GNU General Public License v3.0


