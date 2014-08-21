###Support PreferenceFragment
-----

非官方PreferenceFragment兼容库，支持Android1.6及更高版本。

Unofficial PreferenceFragment compatibility layer for Android 1.6 and up.

###About the status of the library

弥补官方没有PreferenceFragment兼容库

我会一直维护它的  
I will always maintain its 

计划支持 Android L Material Design  
Plans to support Android L Material Design

---

###Demo

![二维码 Dimensional Code](https://raw.githubusercontent.com/kongnanlive/PreferenceFragment/master/download/dimensionalcode.png)

[Download Demo](https://github.com/kongnanlive/PreferenceFragment/raw/master/download/PreferenceFragmentExample-release.apk)

###Usage
----

Add the following config in your `build.gradle`:

```groovy
dependencies {
   compile 'com.daimajia.numberprogressbar:library:1.1@aar'
}
```

Use it in your own code:

```java
  import android.support.v4.preference.CheckBoxPreference;
  import android.support.v4.preference.EditTextPreference;
  import android.support.v4.preference.ListPreference;
  import android.support.v4.preference.PreferenceActivity;
  import android.support.v4.preference.PreferenceCategory;
  import android.support.v4.preference.PreferenceScreen;
  import android.support.v4.preference.SwitchPreference;
```	

```java
<PreferenceScreen xmlns:android1="http://schemas.android.com/apk/res/android"
    xmlns:android="http://schemas.android.com/apk/res-auto" >

    <PreferenceCategory
            android:title="@string/inline_preferences">
            
        <CheckBoxPreference
                android:key="checkbox_preference"
                android:title="@string/title_checkbox_preference"
                android:summary="@string/summary_checkbox_preference" />

        <SwitchPreference
                android:key="checkbox_preference"
                android:title="@string/title_switch_preference"
                android:summary="@string/summary_switch_preference" />

        <SwitchPreference
                android:key="checkbox_preference"
                android:title="@string/title_switch_preference"
                android:summary="@string/summary_switch_preference_yes_no"
                android:switchTextOn = "YES"
                android:switchTextOff = "NO"
                android:defaultValue="true" />
            
    </PreferenceCategory>
    
</PreferenceScreen>
```	

Just extend PreferenceFragment and follow the Settings developer guide like if this layer wasn't even there to begin with.

http://developer.android.com/guide/topics/ui/settings.html

### About me:

我来自孔孟之乡，六年代码生涯，略懂java、flex、php、javascript、html5、android等开发语言，热爱移动互联，目前从事Android开发

### End

这是我自己APP的截图

![alt tag](https://raw.githubusercontent.com/kongnanlive/PreferenceFragment/master/screenshots/psb.jpeg)
