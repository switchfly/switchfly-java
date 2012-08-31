if (EzRez === undefined) {throw ("ezrez-app.js requires ezrez.js");}
if (EzRez.Util === undefined) {throw ("ezrez-app.js requires ezrez-util.js");}
if (EzRez.UI === undefined) {throw ("ezrez-app.js requires ezrez-ui.js");}
EzRez.App = {};
EzRez.App.Messenger = new function() {
    var b = $H();
    this.begin = function(e, c) {
        var d = $H({onEnd: null, keep: false}).merge(c).toObject();
        EzRez.Util.Profiler.start("Message " + e + ".");
        b.set(e, {data: new Array(), ended: false, onEnd: d.onEnd, keep: d.keep});
    };
    this.write = function(c, d) {
        var e = b.get(c);
        if (e === undefined) {return;}
        if (e.ended) {return;}
        e.data.push(d);
        b.set(c, e);
    };
    var a = function(d, c) {
        var e = b.get(d);
        if (c === undefined) {c = false;}
        if (e === undefined) {return;}
        if (!e.ended) {return;}
        if (!c && !e.keep) {b.unset(d);}
        return e.data;
    };
    this.read = function(d, c) {return a(d, c);};
    this.end = function(c) {
        var e = b.get(c);
        e.ended = true;
        if (e.onEnd != null) {
            var d = a(c, e.keep);
            e.onEnd(d);
        }
        EzRez.Util.Profiler.stop("Message " + c + ".");
    };
};