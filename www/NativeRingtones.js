var exec = require('cordova/exec');

function RingtoneManager() {
    // https://developer.android.com/reference/android/media/AudioAttributes
    this.USAGE_MEDIA = 1;
    this.USAGE_UNKNOWN = 0;
    this.USAGE_VOICE_COMMUNICATION = 2;
    this.USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;
    this.USAGE_ALARM = 4;
    this.USAGE_NOTIFICATION = 5;
    this.USAGE_NOTIFICATION_RINGTONE = 6;
    this.USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
    this.USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
    this.USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
    this.USAGE_NOTIFICATION_EVENT = 10;
    this.USAGE_ASSISTANCE_ACCESSIBILITY = 11;
    this.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
    this.USAGE_ASSISTANCE_SONIFICATION = 13;
    this.USAGE_GAME = 14;
    this.USAGE_ASSISTANT = 16;
}


/**
 * Get the ringtone list of the device
 *
 * @param {Object}
 *            Set the ringtone list to the attribute ringtoneList of the object
 */
RingtoneManager.prototype.getRingtone = function (successCallback, errorCallback, ringtoneType) {
    exec(successCallback, errorCallback, "NativeRingtones", "get", [ringtoneType]);
};

RingtoneManager.prototype.playRingtone = function (ringtoneUri, playOnce, volume, usage, successCallback, errorCallback) {
    if (!successCallback) {
        successCallback = function (success) { };
    }

    if (!errorCallback) {
        errorCallback = function (error) { };
    }

    if (typeof playOnce == "undefined") {
        playOnce = true;
    }

    if (typeof volume == "undefined") {
        volume = 100;
    } else {
        if (volume < 0) {
            volume = -1;
        } else if (volume > 100) {
            volume = 100;
        }
    }

    if (typeof usage == "undefined") {
        usage = this.USAGE_NOTIFICATION_RINGTONE;
    }

    exec(successCallback, errorCallback, "NativeRingtones", "play", [ringtoneUri, playOnce, volume, usage]);
};

RingtoneManager.prototype.stopRingtone = function (successCallback, errorCallback) {
    if (!successCallback) {
        successCallback = function (success) { };
    }
    if (!errorCallback) {
        errorCallback = function (error) { };
    }

    exec(successCallback, errorCallback, "NativeRingtones", "stop", []);
};

module.exports = new RingtoneManager();
