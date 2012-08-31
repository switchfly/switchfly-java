if (EzRez === undefined) {
    throw('ezrez-util.js requires ezrez.js');
}

/**
 * The EzRez Util namespace
 * @namespace
 */
EzRez.Util = {};

/**
 * Simple utility to mock firebug console methods
 * @namespace
 */
EzRez.Util.MockFBConsole = function() {
    this.debug = function() {
    };
    this.log = function() {
    };
    this.info = function() {
    };
    this.warn = function() {
    };
    this.error = function() {
    };
    this.assert = function() {
    };
    this.dir = function() {
    };
    this.dirxml = function() {
    };
    this.group = function() {
    };
    this.groupEnd = function() {
    };
    this.time = function() {
    };
    this.timeEnd = function() {
    };
    this.count = function() {
    };
    this.trace = function() {
    };
    this.profile = function() {
    };
    this.profileEnd = function() {
    };
};

// If no firebug console, then mock it
try {
    console.info('console.info test');
    console.dir({test: 'console.dir test'});
} catch (e) {
    window.console = new EzRez.Util.MockFBConsole();
}

/**
 * Debugger utility.  See http://www.timdown.co.uk/log4javascript/log4javascript-current/docs/manual.html.
 * @namespace
 */
EzRez.Util.Debug = new function() {
    var _disabled_buffer = [];
    var _log4js = log4javascript.getLogger();

    var _popUpAppender = new log4javascript.PopUpAppender();
    _popUpAppender.setFocusPopUp(false);
    _popUpAppender.setComplainAboutPopUpBlocking(false);
    _popUpAppender.setUseOldPopUp(true);
    _popUpAppender.setReopenWhenClosed(false);
    _log4js.addAppender(_popUpAppender);
    log4javascript.setEnabled(false);

    var _buffer = function(level, message, exception) {
        if (log4javascript.isEnabled()) {
            return;
        }

        _disabled_buffer.push({lvl: level, msg: message, ex: exception });
    };

    /**
     * Logs a message and optionally an error at level TRACE.
     * @param message {String}
     * @param [exception] {Exception}
     * @methodOf EzRez.Util.Debug
     */
    this.trace = function(message, exception) {
        _buffer('trace', message, exception);
        _log4js.trace(message, exception);
    };

    /**
     * Logs a message and optionally an error at level DEBUG.
     * @param message {String}
     * @param [exception] {Exception}
     * @methodOf EzRez.Util.Debug
     */
    this.debug = function(message, exception) {
        _buffer('trace', message, exception);
        _log4js.debug(message, exception);
    };

    /**
     * Logs a message and optionally an error at level INFO.
     * @param message {String}
     * @param [exception] {Exception}
     * @methodOf EzRez.Util.Debug
     */
    this.info = function(message, exception) {
        _buffer('info', message, exception);
        _log4js.info(message, exception);
    };

    /**
     * Logs a message and optionally an error at level WARN.
     * @param message {String}
     * @param [exception] {Exception}
     * @methodOf EzRez.Util.Debug
     */
    this.warn = function(message, exception) {
        _buffer('warn', message, exception);
        _log4js.warn(message, exception);
    };

    /**
     * Logs a message and optionally an error at level ERROR.
     * @param message {String}
     * @param [exception] {Exception}
     * @methodOf EzRez.Util.Debug
     */
    this.error = function(message, exception) {
        _buffer('error', message, exception);
        _log4js.error(message, exception);
    };

    /**
     * Logs a message and optionally an error at level FATAL.
     * @param message {String}
     * @param [exception] {Exception}
     * @methodOf EzRez.Util.Debug
     */
    this.fatal = function(message, exception) {
        _buffer('fatal', message, exception);
        _log4js.fatal(message, exception);
    };

    /**
     * Enables or disables all logging, depending on enabled.
     * @param enabled {Boolean}
     * @methodOf EzRez.Util.Debug
     */
    this.setEnabled = function(enabled) {
        log4javascript.setEnabled(enabled);

        // Add logs that occured while logging was disabled
        if (enabled && _disabled_buffer.length > 0) {
            _disabled_buffer.each(function(log) {
                this[log.lvl](log.msg, log.ex);
            }, this);
            _disabled_buffer = [];
        }
    };

    /**
     * Open the popup window.
     * @methodOf EzRez.Util.Debug
     */
    this.openPopup = function() {
        _popUpAppender.setReopenWhenClosed(true);
        _log4js.debug('Debugging enabled.');
        _popUpAppender.setReopenWhenClosed(false);
    };

    /**
     * Close the popup window.
     * @methodOf EzRez.Util.Debug
     */
    this.closePopup = function() {
        _popUpAppender.close();
    };

};

/**
 * The EzRez.Util.Env.  Taken from From http://www.quirksmode.org/js/detect.html.
 * @namespace
 */
EzRez.Util.Env = new function() {
    var _versionSearchString = '';

    var _dataBrowser = [
        {
            string: navigator.userAgent,
            subString: "OmniWeb",
            versionSearch: "OmniWeb/",
            identity: "OmniWeb"
        },
        {
            string: navigator.vendor,
            subString: "Apple",
            identity: "Safari"
        },
        {
            prop: window.opera,
            identity: "Opera"
        },
        {
            string: navigator.vendor,
            subString: "iCab",
            identity: "iCab"
        },
        {
            string: navigator.vendor,
            subString: "KDE",
            identity: "Konqueror"
        },
        {
            string: navigator.userAgent,
            subString: "Firefox",
            identity: "Firefox"
        },
        {
            string: navigator.vendor,
            subString: "Camino",
            identity: "Camino"
        },
        {
            // for newer Netscapes (6+)
            string: navigator.userAgent,
            subString: "Netscape",
            identity: "Netscape"
        },
        {
            string: navigator.userAgent,
            subString: "MSIE",
            identity: "Explorer",
            versionSearch: "MSIE"
        },
        {
            string: navigator.userAgent,
            subString: "Gecko",
            identity: "Mozilla",
            versionSearch: "rv"
        },
        {
            // for older Netscapes (4-)
            string: navigator.userAgent,
            subString: "Mozilla",
            identity: "Netscape",
            versionSearch: "Mozilla"
        }
    ];

    var _dataOS = [
        {
            string: navigator.platform,
            subString: "Win",
            identity: "Windows"
        },
        {
            string: navigator.platform,
            subString: "Mac",
            identity: "Mac"
        },
        {
            string: navigator.platform,
            subString: "Linux",
            identity: "Linux"
        }
    ];

    var _searchString = function(data) {
        var dataString = false;
        var dataProp = false;
        for (var i = 0; i < data.length; i++) {
            dataString = data[i].string;
            dataProp = data[i].prop;
            _versionSearchString = data[i].versionSearch || data[i].identity;
            if (dataString) {
                if (dataString.indexOf(data[i].subString) != -1) {
                    return data[i].identity;
                }
            } else if (dataProp) {
                return data[i].identity;
            }
        }
    };

    var _searchVersion = function(dataString) {
        var index = dataString.indexOf(_versionSearchString);
        if (index == -1) {
            return;
        }
        return parseFloat(dataString.substring(index + _versionSearchString.length + 1));
    };

    var _browser = _searchString(_dataBrowser) || "An unknown browser";
    var _version = _searchVersion(navigator.userAgent) || _searchVersion(navigator.appVersion) || "an unknown version";
    var _OS = _searchString(_dataOS) || "an unknown OS";

    /**
     * Get a string of the browser, version, and operating system.
     * @methodOf EzRez.Util.Env
     * @returns {String}
     */
    this.toString = function() {
        return _browser + ' ' + _version + ' ' + _OS;
    };

    /**
     * Get the browser name.
     * @methodOf EzRez.Util.Env
     * @returns {String}
     */
    this.getBrowser = function() {
        return _browser;
    };

    /**
     * Get the browser version.
     * @methodOf EzRez.Util.Env
     * @returns {String}
     */
    this.getVersion = function() {
        return _version;
    };

    /**
     * Get the operating system.
     * @methodOf EzRez.Util.Env
     * @returns {String}
     */
    this.getOS = function() {
        return _OS;
    };

    /**
     * Is the current browser IE6?
     * @methodOf EzRez.Util.Env
     * @returns {boolean}
     */
    this.isIE6 = function() {
        var browserVersion = _browser + _version;
        return browserVersion.include("Explorer6");
    };
};

/**
 *
 * @param message
 * @param cfg
 */
EzRez.Util.RemoteLog = function(message, cfg) {
    var config = $H({
        type: 'information',
        file: 'clientapp_javascript',
        complete: function() {
        }
    }).merge(cfg).toObject();

    var _url = '/js_logger.cfm';
    var _type = config.type;
    var _file = config.file;
    var _complete = config.complete;
    var _status = false;
    var _params;

    var _tpl = new Template('<log>' + '<message>#{msg}</message>' + '<environment>#{env}</environment>' + '<location>#{loc}</location>' + '</log>');

    var _makeParams = function(message) {
        var log = {
            msg: message,
            env: navigator.userAgent,
            loc: location.href
        };

        _params = {
            log_content: _tpl.evaluate(log),
            log_type: _type,
            log_file: _file
        };
    };

    this.getPost = function() {
        return _params;
    };

    this.getStatus = function() {
        return _status;
    };

    this.post = function(message) {
        _makeParams(message);

        var ajax = new Ajax.Request(_url, {
            method: 'post',
            parameters: _params,
            onSuccess: function(transport) {
                _status = ( transport.responseText.strip() == '1' );
                _complete(_status);
            },
            onException: function(transport, exception) {
                _status = false;
                _complete(_status);
                throw exception;
            },
            onFailure: function() {
                _status = false;
                _complete(_status);
            }
        });
    };

    this.post(message);
};

EzRez.Util.Exception = function(cfg) {
    var config = $H({
        name: 'EzrezException',
        message: '',
        remotelog: true
    }).merge(cfg).toObject();

    var _tostring = config.name + ': "' + config.message + '"';
    var _remotelog = config.remotelog;
    var _log;

    this.name = config.name;
    this.message = config.message;

    this.toString = function() {
        return _tostring;
    };

    if (_remotelog) {
        _log = new EzRez.Util.RemoteLog(_tostring, {type: 'error'});
    }
};

/**
 * EzRez.Util.Profiler is profiler utility to measure code performance.
 * @namespace
 */
EzRez.Util.Profiler = new function() {
    var _now = new Date();
    var _start = _now.getTime();
    var _divid = 'profiler_report';

    var _spans = $H();

    var _toHTML = function() {
        var html = '<ul>';
        var tpl = new Template('<li class="profiler_span">' + '<div class="span_details">(#{index}) #{name}: #{start}, #{end}, #{dur}</div>' +
            '<div class="span_bar" style="left:#{left}px; width: #{width}px;"> </div>' + '</li>');

        _spans.each(function(span) {
            span.value.left = span.value.start / 50;
            span.value.width = span.value.dur / 50;
            html += tpl.evaluate(span.value);
        });
        html += '</ul>';

        return html;
    };

    var _toXML = function() {
        var xml = '<profile_report>';
        var tpl = new Template('<span>' + '<index>#{index}</index>' + '<name>#{name}</name>' + '<start>#{start}</start>' + '<end>#{end}</end>' +
            '<duration>#{dur}</duration>' + '</span>');

        _spans.each(function(span) {
            xml += tpl.evaluate(span.value);
        });
        xml += '</profile_report>';

        return xml;
    };

    var _toJSON = function() {
        return _spans.toJSON();
    };

    var _getDiff = function() {
        var ctime = new Date();
        return ctime.getTime() - _start;
    };

    var _logReport = function() {
        var report_str = _toJSON();
        var log = new EzRez.Util.RemoteLog(report_str, {file: 'js_profile'});
    };

    var _renderReport = function() {
        if ($(_divid)) {
            return;
        }

        _toHTML();

        var report_str = '<h3>Profiler Report</h3>' + _toHTML();
        report_str += "<a class='log' href='#'>(Log)</a>";

        var div = new Element('div', { 'id': _divid, 'style': 'display: none;'}).update(report_str);
        document.body.appendChild(div);
    };

    var _onF2 = function(evt) {
        if (evt.keyCode != Event.KEY_F2) {
            return;
        }

        _renderReport(_divid);

        $$('#' + _divid + ' a.log')[0].observe('click', function(evt) {
            _logReport();
            Event.stop(evt);
            return false;
        });

        $(_divid).toggle();
    };

    var _stop = function(name) {
        var span = _spans.get(name);

        if (!span) {
            span = {
                index: _spans.size() + 1,
                name: name,
                start: 0
            };
        }

        span.stop = function() {
        };
        span.end = _getDiff();
        span.dur = span.end - span.start;
        _spans.set(name, span);
        EzRez.Util.Debug.info('#{index}) #{name}: #{start}, #{end}, #{dur}'.interpolate(span));

        return span;
    };

    /**
     * Get the initial start time in millisecons
     * @returns {Int}
     */
    this.getStartTime = function() {
        return _start;
    };

    /**
     * Start timing a span
     * @param name {String} span name
     * @returns {Span} Object of span information: index, name, start, end, dur, and a stop() method.
     */
    this.start = function(name) {
        var span = {
            index: _spans.size() + 1,
            name: name,
            start: _getDiff(),
            end: null,
            dur: null
        };

        span.stop = function() {
            _stop(name);
        };
        _spans.set(name, span);

        return span;
    };

    /**
     * Stop timing a span
     * @param name {String} span name
     * @returns {Span} Object of span information: index, name, start, end, dur, and a stop() method.
     */
    this.stop = function(name) {
        return _stop(name);
    };

    /**
     * Create a report and append it to document.body.  Press F2 to activate.
     */
    this.report = function() {
        _renderReport();
    };

    /**
     * Remotely log a report to the js_profile server log
     */
    this.logReport = function() {
        _logReport();
    };

    /**
     * Clear all profile spans
     */
    this.clearAll = function() {
        _spans = $H();
    };

    /**
     * Get a span
     * @param name {String} Span name
     * @returns {Span} Object of span information: index, name, start, end, dur, and a stop() method.
     */
    this.getSpan = function(name) {
        return _spans.get(name);
    };

    /**
     * Get all spans
     * @returns {Hash}
     */
    this.getSpans = function() {
        return _spans;
    };

    /**
     * Return a HTML formated string of span measurements
     */
    this.toHTML = function() {
        return _toHTML();
    };

    /**
     * Return a XML formated string of span measurements
     */
    this.toXML = function() {
        return _toXML();
    };

    /**
     * Return a JSON formated string of span measurements
     */
    this.toJSON = function() {
        return _toJSON();
    };

    //    document.observe('dom:loaded', function(evt) {
    //        this.stop('dom:load');
    //    }.bindAsEventListener(this));

    //    Event.observe(window, 'load', function(evt) {
    //        this.stop('window:load');
    //    }.bindAsEventListener(this));

    document.observe('keydown', _onF2);
};

/**
 * Utility to create, read, and erase cookies.  Adopted from http://www.quirksmode.org/js/cookies.html
 * @namespace
 */
EzRez.Util.Cookie = new function() {

    var _cookie_txt = '';

    /**
     * Create a cookie
     * @methodOf EzRez.Util.Cookie
     * @param name {String} Cookie name
     * @param value {String} Cookie value
     * @param [hours] {Object} Duration in hours. Defaults to the duration of the session
     */
    this.create = function(name, value, hours) {
        var expires = "";
        var encoded = encodeURIComponent(value);
        var date = new Date();

        if (hours) {
            date.setTime(date.getTime() + (hours * 60 * 60 * 1000));
            expires = "; expires=" + date.toGMTString();
        }

        _cookie_txt = name + "=" + encoded + expires + "; path=/";

        document.cookie = _cookie_txt;
    };

    /**
     * Read a cookie
     * @methodOf EzRez.Util.Cookie
     * @param name {String} Cookie name
     * @return Cookie value
     */
    this.read = function(name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        var c;
        for (var i = 0; i < ca.length; i++) {
            c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1, c.length);
            }
            if (c.indexOf(nameEQ) == 0) {
                return decodeURIComponent(c.substring(nameEQ.length, c.length));
            }
        }
        return null;
    };

    /**
     * Erase a cookie
     * @methodOf EzRez.Util.Cookie
     * @param name {String} Cookie name
     */
    this.erase = function(name) {
        this.create(name, "", -1);
    };

    /**
     * Erase all cookies
     * @methodOf EzRez.Util.Cookie
     */
    this.eraseAll = function() {
        var ca = document.cookie.split(';');
        ca.each(function(c) {
            var name = c.strip().split('=')[0];
            this.erase(name);
        }.bind(this));
    };

    this.getCookieText = function() {
        return _cookie_txt;
    };
};

/**
 * EzRez.Util.Indexer creates search indexes from an array of objects.  This is great for searching a large array of objects.
 * @class
 * @param cfg {Object} Configuration parameters
 * @param cfg.index_data {Array} Array of data objects
 * @param cfg.id_property {String} Name of the primary lookup or ID property of each data object
 * @param [property_list='all'] {String} Comma separated list of properties to index.  Creates an index of each
 * property. Default ia 'all', which uses all properties
 * @example
 * <pre>
 *   var hotels = [
 *       {
 *           id: 1,
 *           name: 'Hotel A',
 *           price: 512.00,
 *           rating: 4.5,
 *           neighorhoodId: 110
 *       },{
 *           id: 2,
 *           name: 'Hotel B',
 *           price: 345.00,
 *           rating: 3.5,
 *           neighorhoodId: 200
 *       },{
 *           id: 3,
 *           name: 'Hotel C',
 *           price: 110.00,
 *           rating: 2.0,
 *           neighorhoodId: 110
 *       }
 *   ];
 *
 *   var hotelIndexes = new EzRez.Util.Indexer({
 *       index_data: hotels,
 *       id_property: 'id',
 *       property_list: ['neighorhoodId', 'rating']
 *   });
 *
 *   var neighorhoods = hotelIndexes.filter('neighorhoodId',110);
 *   // neighorhoods = [1,3];
 * </pre>
 */
EzRez.Util.Indexer = function(cfg) {
    var config = $H({
        index_data: null,
        id_property: '',
        property_list: 'all'
    }).merge(cfg).toObject();

    var _indexes = $H();
    var _index_data = config.index_data;
    var _id_property = config.id_property;
    var _property_list = config.property_list;

    config.index_data = null;

    var _createIndexes = function() {
        var data_item;

        if (_property_list === 'all') {
            data_item = _index_data.first();
            var property;
            for (property in data_item) {
                if (data_item[property] && property !== _id_property) {
                    _indexes.set(property, $H());
                }
            }

            return;
        }

        _property_list.each(function(property) {
            _indexes.set(property, $H());
        });

    };

    var _buildIndex = function(index, look_up, id) {
        if (Object.isArray(look_up)) {
            look_up.each(function(look_up_item) {
                _buildIndex(index, look_up_item, id);
            });

            return;
        }

        var matches = index.get(look_up);
        if (!matches) {
            index.set(look_up, [id]);
        } else {
            matches.push(id);
        }
    };

    var _buildIndexes = function() {
        _index_data.each(function(data_item, data_index) {
            data_item.master_index = data_index;
            _indexes.each(function(index) {
                if (data_item[index.key]) {
                    _buildIndex(index.value, data_item[index.key], data_item);
                }
            });
        });

        //        _indexes.each(function(index) {
        //            console.dir(index.value.toObject());
        //        });
    };

    var _unload = function() {
        _indexes = null;
        _index_data = null;
    };

    /**
     * Search a given index for a given value
     * @param index_name {String} Index name: same as the propertry name
     * @param index_lookup {String} look up value
     * @returns {Array} Array of data objects
     */
    this.filter = function(index_name, index_lookup) {
        var index = _indexes.get(index_name);

        if (!index) {
            return [];
        }

        var values = index.get(index_lookup);

        if (!values) {
            return [];
        }

        return values.pluck(_id_property);
    };

    /**
     * Search a given index for several values and return the union of matches
     * @param index_name {String} Index name: same as the propertry name
     * @param index_lookup {Array} look up values
     * @returns {Array} Array of data objects
     */
    this.filterUnion = function(index_name, index_lookups) {
        var index = _indexes.get(index_name);
        var results = [];
        var temp;

        if (!index) {
            return results;
        }

        index_lookups.each(function(lookup) {
            temp = index.get(lookup);
            if (temp) {
                results = results.concat(temp);
            }
        });

        results = results.sortBy(function(data_item) {
            return data_item.master_index;
        });

        return results.pluck(_id_property);
    };

    /**
     * Search a given index for several values and return the intersection of matches
     * @param index_name {String} Index name: same as the propertry name
     * @param index_lookup {Array} look up values
     * @returns {Array} Array of data objects
     */
    this.filterIntersect = function(index_name, index_lookups) {
        var index = _indexes.get(index_name);
        var results = null;
        var temp = null;

        if (!index) {
            return results;
        }

        index_lookups.each(function(lookup) {
            temp = index.get(lookup);

            if (!temp) {
                return;
            }

            if (!results) {
                results = temp;
                return;
            }

            if ([results.length, temp.length].include(0)) {
                results = [];
                throw $break;
            }

            results = results.intersect2(temp);
        });

        if (!results) {
            return [];
        }

        return results.pluck(_id_property);
    };

    /**
     * Get an index
     * @param index_name {String} Index name: same as the propertry name
     * @returns {Hash} Hash of indexes
     */
    this.getIndex = function(index_name) {
        return _indexes.get(index_name);
    };

    /**
     * Get all indexes
     */
    this.getIndexes = function() {
        return _indexes;
    };

    /**
     * Delete all indexes
     */
    this.deleteIndexes = function() {
        _indexes = $H();
    };

    /**
     * Delete an index
     * @param index_name {String} Index name: same as the propertry name
     */
    this.deleteIndex = function(index_name) {
        _indexes.unset(index_name);
    };

    /**
     * Unload all data
     */
    this.unload = function() {
        _unload();
    };

    Event.observe(window, 'unload', _unload);

    _createIndexes();
    _buildIndexes();
};

EzRez.Util.Omniture = new function() {

    this.transmit = function() {
        try {
            if (!s) {
                return;
            }
            var s_code = s.t();
            if (s_code) {
                document.write(s_code);
            }
        } catch (e) { /* ignore */
        }
    };
};

EzRez.Util.Locales = new function() {
    var _dateLocales = $H();

    this.setDateLocale = function(locale, info) {
        _dateLocales.set(locale, info);
    };

    this.get = function(locale) {
        return _dateLocales.get(locale);
    };
};
