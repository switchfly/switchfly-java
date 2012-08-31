/**
 * @overview
 * @date $Date: 2009-05-13 15:29:08 -0700 (Wed, 13 May 2009) $
 * @version $Revision: 44458 $
 * @author $Author: lbunselmeyer $
 * @location $HeadURL: http://sfxcode01.corp.ezrez/svn/dev/trunk/js_common/src/main/javascript/ezrez/js/ezrez.js $
 */

/**
 * The EzRez namespace
 * @namespace
 */
var EzRez = {
    revision: '$Revision: 44458 $',
    REQUIRED_PROTOTYPE: '1.6.0',
    REQUIRED_SCRIPTACULOUS: '1.8.1',
    load: function() {
        function convertVersionString(versionString) {
            var r = versionString.split('.');
            return parseInt(r[0], 10) * 100000 + parseInt(r[1], 10) * 1000 + parseInt(r[2], 10);
        }

        if (Prototype === undefined) {
            throw("ezrez.js requires the Prototype JavaScript framework >= " + EzRez.REQUIRED_PROTOTYPE);
        }

        if (convertVersionString(Prototype.Version) < convertVersionString(EzRez.REQUIRED_PROTOTYPE)) {
            throw("ezrez.js requires the Prototype JavaScript framework >= " + EzRez.REQUIRED_PROTOTYPE);
        }

        if (Scriptaculous === undefined) {
            throw("ezrez.js requires the Prototype JavaScript framework >= " + EzRez.REQUIRED_SCRIPTACULOUS);
        }

        if (convertVersionString(Scriptaculous.Version) < convertVersionString(EzRez.REQUIRED_SCRIPTACULOUS)) {
            throw("ezrez.js requires the Prototype JavaScript framework >= " + EzRez.REQUIRED_SCRIPTACULOUS);
        }

        var script = $A(document.getElementsByTagName("script")).find(function(s) {
            return s.src && (s.src.include('/js/packages/js/ezrez-') || s.src.include('/js/ezrez/js/ezrez.js'));
        });

        /**
         * @type String
         */
        this.path = (script) ? script.src.replace(/\/js\/packages\/js\/ezrez-[^\/]*\.js$/, '/js').replace('/js/ezrez/js/ezrez.js', '/js') : '';

        var matches = '$Revision: 44458 $'.match(/^\$Revision: (\d+) \$$/);

        /**
         * $Revision: 44458 $
         * @type String
         */
        this.revision = matches[1];
    }
};

EzRez.load();

/**
 * TODO: Replace with intersect when Prototype 1.6.0.3 is released.
 * Alternative array intersect method to prototype's built in method.
 * See http://prototype.lighthouseapp.com/projects/8886-prototype/tickets/240-cleanup-array-intersect-patch
 * @param other {Array}
 */
Array.prototype.intersect2 = function(other) {
    var arr = this, result = [];
    other.each(function(item) {
        if (arr.indexOf(item) !== -1 && result.indexOf(item) == -1) {
            result.push(item);
        }
    });
    return result;
};

/**
 * Add to prototype's build-in list of keycodes.
 * These are already defined in prototype.js:
 *   KEY_BACKSPACE: 8
 *   KEY_TAB:       9
 *   KEY_RETURN:   13
 *   KEY_ESC:      27
 *   KEY_LEFT:     37
 *   KEY_UP:       38
 *   KEY_RIGHT:    39
 *   KEY_DOWN:     40
 *   KEY_DELETE:   46
 *   KEY_HOME:     36
 *   KEY_END:      35
 *   KEY_PAGEUP:   33
 *   KEY_PAGEDOWN: 34
 *   KEY_INSERT:   45
 */
Object.extend(Event, {
    KEY_SHIFT: 16,
    KEY_CTRL: 17,
    KEY_ALT: 18,
    KEY_F1: 112,
    KEY_F2: 113,
    KEY_F3: 114,
    KEY_F4: 115,
    KEY_F5: 116,
    KEY_F6: 117,
    KEY_F7: 118,
    KEY_F8: 119,
    KEY_F9: 120,
    KEY_F10: 121,
    KEY_F11: 122,
    KEY_F12: 123
});

/**
 * Parse a GMT date string of the form '2008-12-22 09:00:00 GMT' and return the time in milliseconds
 * @param datestring {String} GMT date string of the form 'yyyy-mm-dd HH:mm:ss GMT'
 * @returns {Integer} milliseconds
 */
Date.parseGMT = function(datestring) {
    if (!datestring) {
        return new Date();
    }

    var matches = datestring.match(/(^\d{4})[-\/](\d{1,2})[-\/](\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2}) GMT$/);

    if (!matches) {
        return null;
    }

    if (!Object.isArray(matches) && matches.length < 8) {
        return null;
    }

    matches.shift();
    matches = matches.collect(function(match) {
        return parseInt(match, 10);
    });

    return Date.UTC(matches[0], matches[1] - 1, matches[2] - 1, matches[3], matches[4], matches[5]);
};

/**
 * Parse a date string of the form '2008-12-22 09:00:00' and return the time in milliseconds
 * @param datestring {String} GMT date string of the form 'yyyy-mm-dd HH:mm:ss'
 * @returns {Integer} milliseconds
 */
Date.parseJSON = function(datestring) {
    if (!datestring) {
        return new Date();
    }

    var matches = datestring.match(/(^\d{4})[-\/](\d{1,2})[-\/](\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2}) GMT$/);

    if (!matches) {
        return null;
    }

    if (!Object.isArray(matches) && matches.length < 8) {
        return null;
    }

    matches.shift();
    matches = matches.collect(function(match) {
        return parseInt(match, 10);
    });

    return new Date(matches[0], matches[1] - 1, matches[2], matches[3], matches[4], matches[5]).getTime();
};

/**
 * Calculate the difference in days between two date objects.
 * @param date2 {Date}
 * @returns {Integer} Difference in days
 */
Date.prototype.diffInDays = function(date2) {
    var diff = ( date2.getTime() - this.getTime() ) / ( 1000 * 60 * 60 * 24 );
    return Math.ceil(diff);
};

/**
 * Determines if the passed date is before this date
 * @param d {Date}
 * @returns {boolean}
 */
Date.prototype.isBefore = function(d) {
    return this.getTime() < d.getTime();
};

/**
 * Determines if the passed date is after this date
 * @param d {Date}
 * @returns {boolean}
 */
Date.prototype.isAfter = function(d) {
    return this.getTime() > d.getTime();
};

/**
 * Date Format 1.2.2
 * (c) 2007-2008 Steven Levithan <stevenlevithan.com>
 * MIT license
 * Includes enhancements by Scott Trenda <scott.trenda.net> and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Returns a formatted version of the date.
 * @param [mask='default'] {String} Format mask
 * @param [utc=false] {Boolean} Set to UTC time
 * @returns {String} formatted version of the given date.
 * @example
 * <pre><code>
 *  d           Day of the month as digits; no leading zero for single-digit days.
 *  dd          Day of the month as digits; leading zero for single-digit days.
 *  ddd         Day of the week as a three-letter abbreviation.
 *  dddd        Day of the week as its full name.
 *  m           Month as digits; no leading zero for single-digit months.
 *  mm          Month as digits; leading zero for single-digit months.
 *  mmm         Month as a three-letter abbreviation.
 *  mmmm        Month as its full name.
 *  yy          Year as last two digits; leading zero for years less than 10.
 *  yyyy        Year represented by four digits.
 *  h           Hours; no leading zero for single-digit hours (12-hour clock).
 *  hh          Hours; leading zero for single-digit hours (12-hour clock).
 *  H           Hours; no leading zero for single-digit hours (24-hour clock).
 *  HH          Hours; leading zero for single-digit hours (24-hour clock).
 *  M           Minutes; no leading zero for single-digit minutes.
 *  MM          Minutes; leading zero for single-digit minutes.
 *  s           Seconds; no leading zero for single-digit seconds.
 *  ss          Seconds; leading zero for single-digit seconds.
 *  l or L      Milliseconds. l gives 3 digits. L gives 2 digits.
 *  t           Lowercase, single-character time marker string: a or p.
 *  tt          Lowercase, two-character time marker string: am or pm.
 *  T           Uppercase, single-character time marker string: A or P.
 *  TT          Uppercase, two-character time marker string: AM or PM.
 *  Z           US timezone abbreviation, e.g. EST or MDT. With non-US timezones or in the Opera browser, the GMT/UTC offset is returned, e.g. GMT-0500
 *  o           GMT/UTC timezone offset, e.g. -0500 or +0230.
 *  S           The date's ordinal suffix (st, nd, rd, or th). Works well with d.
 *  '…' or "…"  Literal character sequence. Surrounding quotes are removed.
 *  UTC:        Must be the first four characters of the mask. Converts the date from local time to UTC/GMT/Zulu time before applying the mask. The "UTC:" prefix is removed.
 *
 *  default         ddd mmm dd yyyy HH:MM:ss        Sat Jun 09 2007 17:46:21
 *  shortDate       m/d/yy                          6/9/07
 *  mediumDate      mmm d, yyyy                     Jun 9, 2007
 *  longDate        mmmm d, yyyy                    June 9, 2007
 *  fullDate        dddd, mmmm d, yyyy              Saturday, June 9, 2007
 *  shortTime       h:MM TT                         5:46 PM
 *  mediumTime      h:MM:ss TT                      5:46:21 PM
 *  longTime        h:MM:ss TT Z                    5:46:21 PM EST
 *  isoDate         yyyy-mm-dd                      2007-06-09
 *  isoTime         HH:MM:ss                        17:46:21
 *  isoDateTime     yyyy-mm-dd'T'HH:MM:ss           2007-06-09T17:46:21
 *  isoUtcDateTime  UTC:yyyy-mm-dd'T'HH:MM:ss'Z'    2007-06-09T22:46:21Z
 * </code></pre>
 */
Date.prototype.format = function(mask, utc) {
    var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g, timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g, timezoneClip = /[^-+\dA-Z]/g, pad = function(val,
        len) {
        val = String(val);
        len = len || 2;
        while (val.length < len) {
            val = "0" + val;
        }
        return val;
    };

    var dateFormat = function(date, mask, utc) {
        var dF = dateFormat;

        // You can't provide utc if you skip other args (use the "UTC:" mask prefix)
        if (arguments.length == 1 && (typeof date == "string" || date instanceof String) && !/\d/.test(date)) {
            mask = date;
            date = undefined;
        }

        // Passing date through Date applies Date.parse, if necessary
        date = date ? new Date(date) : new Date();
        if (isNaN(date)) {
            throw new SyntaxError("invalid date");
        }

        mask = String(dF.masks[mask] || mask || dF.masks["default"]);

        // Allow setting the utc argument via the mask
        if (mask.slice(0, 4) == "UTC:") {
            mask = mask.slice(4);
            utc = true;
        }

        var _ = utc ? "getUTC" : "get", d = date[_ + "Date"](), D = date[_ + "Day"](), m = date[_ + "Month"](), y = date[_ +
            "FullYear"](), H = date[_ + "Hours"](), M = date[_ + "Minutes"](), s = date[_ + "Seconds"](), L = date[_ + "Milliseconds"](), o = utc ?
            0 : date.getTimezoneOffset(), flags = {
            d: d,
            dd: pad(d),
            ddd: dF.i18n.dayNames[D],
            dddd: dF.i18n.dayNames[D + 7],
            m: m + 1,
            mm: pad(m + 1),
            mmm: dF.i18n.monthNames[m],
            mmmm: dF.i18n.monthNames[m + 12],
            yy: String(y).slice(2),
            yyyy: y,
            h: H % 12 || 12,
            hh: pad(H % 12 || 12),
            H: H,
            HH: pad(H),
            M: M,
            MM: pad(M),
            s: s,
            ss: pad(s),
            l: pad(L, 3),
            L: pad(L > 99 ? Math.round(L / 10) : L),
            t: H < 12 ? "a" : "p",
            tt: H < 12 ? "am" : "pm",
            T: H < 12 ? "A" : "P",
            TT: H < 12 ? "AM" : "PM",
            Z: utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
            o: (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
            S: ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
        };

        return mask.replace(token, function($0) {
            return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
        });
    };

    // Some common format strings
    dateFormat.masks = {
        "default": "ddd mmm dd yyyy HH:MM:ss",
        shortDate: "m/d/yy",
        mediumDate: "mmm d, yyyy",
        longDate: "mmmm d, yyyy",
        fullDate: "dddd, mmmm d, yyyy",
        shortTime: "h:MM TT",
        mediumTime: "h:MM:ss TT",
        longTime: "h:MM:ss TT Z",
        isoDate: "yyyy-mm-dd",
        isoTime: "HH:MM:ss",
        isoDateTime: "yyyy-mm-dd'T'HH:MM:ss",
        isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
    };

    // Internationalization strings
    dateFormat.i18n = {
        dayNames: [
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
        ],
        monthNames: [
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "January", "February", "March", "April", "May",
            "June", "July", "August", "September", "October", "November", "December"
        ]
    };

    return dateFormat(this, mask, utc);
};

/**
 * Rounds numbers to the given decimal place
 * @param number {Float} Number to round
 * @param decimal {Integer} Decimal place to round to
 * @returns {Float} Rounded number
 * @example
 *   Math.roundToDecimal(101.123,2) // 101.12
 *   Math.roundToDecimal(101.125,2) // 101.13
 *   Math.roundToDecimal(101.13,1)  // 101.1
 *   Math.roundToDecimal(101.15,1)  // 101.2
 */
Math.roundToDecimal = function(number, decimal) {
    if (!Object.isNumber(number) || !Object.isNumber(decimal) || decimal < 1) {
        return number;
    }
    decimal = Math.round(decimal);
    return Math.round(number * Math.pow(10, decimal)) / Math.pow(10, decimal);
};

/* Fix IE6 image caching */
try {
    document.execCommand("BackgroundImageCache", false, true);
} catch (err) {
}
