if (EzRez === undefined) {
    throw('ezrez-app.js requires ezrez.js');
}

if (EzRez.Util === undefined) {
    throw('ezrez-app.js requires ezrez-util.js');
}

if (EzRez.UI === undefined) {
    throw('ezrez-app.js requires ezrez-ui.js');
}

/**
 * The EzRez App namespace
 * @namespace
 */
EzRez.App = {};

/**
 * EzRez.App.Messenger: Global utility used to transfer data (aka messages)
 * from the server side to the client application within the HTML body of
 * the document.
 * @namespace
 * @example
 * <pre><code>
 * EzRez.App.Messenger.begin('Hotel-Data');
 * EzRez.App.Messenger.write('Hotel-Data', {name: 'Hotel A', rating: 3.5});
 * EzRez.App.Messenger.write('Hotel-Data', {name: 'Hotel B', rating: 2.0});
 * EzRez.App.Messenger.write('Hotel-Data', {name: 'Hotel C', rating: 4.5});
 * EzRez.App.Messenger.end('Hotel-Data');
 *
 * var hotel_data = EzRez.App.Messenger.read('Hotel-Data');
 * </code></pre>
 */
EzRez.App.Messenger = new function() {
    var _data = $H();

    /**
     * Begin a message.  Note: A message cannot be written to until it has begun.
     * @methodOf EzRez.App.Messenger
     * @param name {String} Message name
     * @param [cfg] {Object} Message paramters
     * @param [cfg.onEnd] {Function} Callback to call once the message has ended.
     * @param [cfg.keep=false] {Boolean} Specifies whether or not to keep the message after it has been read.
     */
    this.begin = function(name, cfg) {
        var config = $H({
            onEnd: null,
            keep: false
        }).merge(cfg).toObject();

        EzRez.Util.Profiler.start('Message ' + name + '.');

        _data.set(name, {
            data: new Array(),
            ended: false,
            onEnd: config.onEnd,
            keep: config.keep
        });
    };

    /**
     * Write a message.  Successive calls will append the data to an array of previous values.  Note: A message
     * cannot be written to until it has begun.
     * @methodOf EzRez.App.Messenger
     * @param name {String} Message name
     * @param value {Array} Array of message values
     */
    this.write = function(name, value) {

        var msg = _data.get(name);

        if (msg === undefined) {
            return;
        }

        if (msg.ended) {
            return;
        }

        msg.data.push(value);
        _data.set(name, msg);
    };

    var _read = function(name, keep) {
        var msg = _data.get(name);

        if (keep === undefined) {
            keep = false;
        }

        if (msg === undefined) {
            return;
        }

        if (!msg.ended) {
            return;
        }

        if (!keep && !msg.keep) {
            _data.unset(name);
        }

        return msg.data;
    };

    /**
     * Read a message.  Note: A message cannot be read until it has ended.  Also, by default
     * a message will be deleted after being read.
     * @methodOf EzRez.App.Messenger
     * @param name {String} Message name
     * @param keep {Boolean} Specifies whether or not to keep the message after it has been read.
     * @return {Mixed} Returns either an Array of values, one value, or undefined.
     */
    this.read = function(name, keep) {
        return _read(name, keep);
    };

    /**
     * End a message.  Note: A message cannot be read until it has ended.  Also, by default
     * @methodOf EzRez.App.Messenger
     * @param name {String} Message name
     */
    this.end = function(name) {
        var msg = _data.get(name);
        msg.ended = true;

        if (msg.onEnd != null) {
            var data = _read(name, msg.keep);
            msg.onEnd(data);
        }

        EzRez.Util.Profiler.stop('Message ' + name + '.');
    };
};
