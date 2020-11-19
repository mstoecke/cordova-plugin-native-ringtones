# This plugin is a fork of [cordova-plugin-native-ringtones](https://github.com/TongZhangzt/cordova-plugin-native-ringtones)

### cordova-plugin-native-ringtones-ef [![npm version](https://badge.fury.io/js/cordova-plugin-native-ringtones-ef.svg)](https://badge.fury.io/js/cordova-plugin-native-ringtones-ef)

Plugin for the [Cordova](https://cordova.apache.org) framework to get the native ringtone list.

The plugin helps get the native ringtones list on Android or IOS devices. And you can also use this plugin to play or stop the native ringtones and custom ringtones(added in the www folder).

## Supported Platforms
- __iOS__ 
- __Android__ 

## Installation
The plugin can be installed via Cordova-CLI and is publicly available on [NPM](https://www.npmjs.com/package/cordova-plugin-native-ringtones).

Execute from the projects root folder:

    $ cordova plugin add cordova-plugin-native-ringtones

Or install a specific version:

    $ cordova plugin add cordova-plugin-native-ringtones-ef

Or install the latest head version:

    $ cordova plugin add https://github.com/EltonFaust/cordova-plugin-native-ringtones

## Usage
The plugin creates the object `cordova.plugins.NativeRingtones` and is accessible after the *deviceready* event has been fired.

You can call the function getRingtone to get the ringtone list. There are two/three parameters for this function:  
1. successCallback function: The cordova plugin will pass the ringtone list by the `success` object and the ringtone list is put in an array, each object in this array represent a ringtone (with name, url and category).  
2. errorCallback function: The function that will be called if the getRingtone failed.  
3. (just for Android) An string value to indicate the ringtone type. There are three kinds of ringtones for `Android`: 'notification', 'alarm', 'ringtone'. The default value is *'notification'*.

```js
document.addEventListener('deviceready', function () {
    cordova.plugins.NativeRingtones.getRingtone(
        function(ringtones) {
            //An object array contains all the ringtones, including the systems default ringtone at the beginning
            console.log(ringtones);
        },
        function(err) {
            alert(err);
        }, 'alarm'
    );
}, false);
```

You can call the function playRingtone or stopRingtone to play or stop a ringtone by passing the URI of this ringtone. This plugin allow you to play both native ringtones and custom ringtones.

```js
document.addEventListener('deviceready', function () {
    // if set to false, it will keep looping until call of stopRingtone (default: true)
    var playOnce = false;
    // ringtone volume, value between 0 and 100 (default: 100)
    var volume = 50;
    // Android only: play ringtone as type (default: cordova.plugins.NativeRingtones.USAGE_NOTIFICATION_RINGTONE)
    var usage = cordova.plugins.NativeRingtones.USAGE_ALARM;

    // params playOnce, volume, usage are not required
    cordova.plugins.NativeRingtones.playRingtone(
        "/System/Library/Audio/UISounds/Modern/calendar_alert_chord.caf",
        playOnce, volume, usage
    );

    window.setTimeout(function(){
        // if is set to false playOnce param for playRingtone method, call stopRingtone to stop playing
        cordova.plugins.NativeRingtones.stopRingtone();
    }, 5000);
}, false);
```

## License

This software is released under the [Apache 2.0 License](http://opensource.org/licenses/Apache-2.0).

