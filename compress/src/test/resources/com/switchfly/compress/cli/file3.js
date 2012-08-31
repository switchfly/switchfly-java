/**
 * The EzRez.UI.GMap
 *
 */

if (EzRez === undefined) {
    throw('ezrez-ui-gmap.js requires ezrez.js');
}

if (EzRez.Util === undefined) {
    throw('ezrez-ui-gmap.js requires ezrez-util.js');
}

if (EzRez.UI === undefined) {
    throw('ezrez-ui-gmap.js requires ezrez-ui.js');
}

(function() {
    // Private Static Properties
    var _compatible = true;
    var _debug = true;

    var _console = function(msg, e) {
        if (!_debug) {
            return false;
        }
        EzRez.Util.Debug.info(msg, e);
    };

    var _compatibilityCheck = function() {
        try {
            if (GBrowserIsCompatible === undefined) {
                return false;
            }

            return GBrowserIsCompatible();
        } catch (e) {
            _console('Error: google maps api is not loaded.', e);
            return false;
        }
    };

    if (!_compatibilityCheck()) {
        _compatible = false;
        _console('Warning: Cannot run google maps.');
    }

    /**
     * A UI component to create a Google Map from semantic html.
     * @class
     * @param cfg {Object} Configuration parameters
     * @param cfg.canvas_id {String} ID of the map canvas element
     * @param [cfg.info_id] {String} ID of the element containing the gmarker information.  See example for semantic markup.
     * @param [cfg.default_zoom=1] {Integer} Default zoom level
     * @param [cfg.default_center={lat: 20, lng: 0}] {Object} Default location to center the map in decimal format.
     * @param [cfg.geo_min_dec_figs=3] {Integer}
     * @param [cfg.map_options] {GMapOptions} Map options.  See http://code.google.com/apis/maps/documentation/reference.html#GMapOptions.
     * @param [cfg.info_window_opts] {ExtInfoWindowOptions} ExtInfoWindow Options.
     * See http://gmaps-utility-library.googlecode.com/svn/trunk/extinfowindow/release/docs/reference.html.
     * @param [cfg.setup] {Function} Callback to run after google map has been initialized.  The callback takes one argument: the
     * GMap2 instance used within EzRez.UI.GMap.
     * @param [cfg.parseComplete] {Function} Callback to run after parsing and geocoding the gmarker info within the cfg.info_id element
     * @example
     * <pre><code>
     * <script>
     *   var HotelsMap = new EzRez.UI.GMap({
     *       canvas_id: 'gmap_canvas',
     *       info_id: 'gmap_info',
     *       setup: function(map) {
     *           map.addControl(new GSmallMapControl());
     *           map.addControl(new GMapTypeControl());
     *           map.enableContinuousZoom();
     *           map.enableGoogleBar();
     *       },
     *       parseComplete: function(){
     *           alert('Map Complete!');
     *       }
     *   });
     * </script>
     * <div id="gmap_canvas"></div>
     * <div id="gmap_info" style="display: none;">
     *   <div class="gmarker">
     *      <div class="address">1101 Van Ness Ave, San Francisco, California, 94109</div>
     *       <div class="lat">37.7859</div>
     *       <div class="lng">-122.4203</div>
     *       <div class="title">Cathedral Hill Hotel</div>
     *       <div class="label">3</div>
     *       <div class="info_window">
     *           <h3>Cathedral Hill Hotel</h3>
     *           1101 Van Ness Ave, San Francisco, California, 94109
     *       </div>
     *   </div>
     *   <div class="gmarker">
     *       <div class="address">2550 Van Ness Ave, San Francisco, California, 94109</div>
     *       <div class="lat">37.7992</div>
     *       <div class="lng">-122.4217</div>
     *       <div class="title">Heritage Marina Hotel</div>
     *       <div class="label">7</div>
     *       <div class="info_window">
     *           <h3>Heritage Marina Hotel</h3>
     *           2550 Van Ness Ave, San Francisco, California, 94109
     *       </div>
     *   </div>
     * </div>
     * </code></pre>
     */
    EzRez.UI.GMap = function(cfg) {
        var _addresses;
        var _markers;
        var _default_zoom;
        var _map;
        var _geo;
        var _geo_min_dec_figs;
        var _map_saved_center;
        var _map_saved_zoom;
        var _centered;
        var _info_window_opts;
        var _secure;
        var _canvas_id;
        var _default_center;
        var _info_id;

        var config = $H({
            canvas_id: null,
            info_id: null,
            width: null,
            height: null,
            default_zoom: 1,
            geo_min_dec_figs: 3,
            map_options: null,
            info_window_opts: null,
            setup: function(map) {
            },
            parseComplete: function() {
            },
            default_center: {lat: 20, lng: 0}
        }).merge(cfg).toObject();

        _addresses = [];
        _markers = [];
        _centered = false;
        _map = null;
        _geo = null;
        _geo_min_dec_figs = config.geo_min_dec_figs;
        _info_window_opts = config.info_window_opts;
        _secure = ( location.protocol == 'https' );
        _canvas_id = config.canvas_id;
        _info_id = config.info_id;
        _default_zoom = config.default_zoom;
        _default_center = config.default_center;
        _map_saved_center = null;
        _map_saved_zoom = null;

        var _clearMarkers = function() {
            _addresses.clear();
            _markers.clear();
            _map.clearOverlays();
            _console('Cleared Map Overlays.');
        };

        /**
         * Add a geo cordinate to the map.
         * @methodOf EzRez.UI.GMap
         * @param lat {Float} Latitude in decimal format
         * @param lng {Float} Longitude in decimal format
         * @param [cfg] {Object} Configration parameters
         * @param [cfg.zoom] {Integer} Zoom level.  Defaults to the default_zoom set at instantiation.
         * @param [cfg.title] {String} Title to show on mouseover
         * @param [cfg.label] {Integer} Label to show within the gmarker
         * @param [cfg.geo_min_dec_figs] {Integer} Defaults to the geo_min_dec_figs set at instantiation.
         * @param [cfg.info_window_html] {String} HTML to show within the gmarker info window
         * @param [cfg.info_window_opts] {ExtInfoWindowOptions} ExtInfoWindow Options.
         * See http://gmaps-utility-library.googlecode.com/svn/trunk/extinfowindow/release/docs/reference.html.
         */
        this.addGeoCord = function(lat, lng, cfg) {
            if (!_compatible) {
                return;
            }

            var config = $H({
                zoom: _default_zoom,
                info_window_html: null,
                info_window_opts: _info_window_opts,
                title: '',
                geo_min_dec_figs: _geo_min_dec_figs,
                label: null
            }).merge(cfg).toObject();

            if (lat.length === 0 || lng.length === 0) {
                _console('Missing geo cordinates.');
                return null;
            }

            var digitsfilter = function(number_str, dec_figs) {
                if (config.geo_min_dec_figs <= 0) {
                    return false;
                }

                number_str = String(number_str);

                var pattern = /\.([0-9]+)/;
                var matches = number_str.match(pattern);
                return ( matches[1].length < dec_figs );
            };

            if (digitsfilter(lat, _geo_min_dec_figs) || digitsfilter(lng, _geo_min_dec_figs)) {
                _console('Inaccurate geo cordinates: less than ' + _geo_min_dec_figs + ' decimal figuress');
                return null;
            }

            var marker;

            if (config.label !== null) {
                marker = new LabeledMarker(new GLatLng(lat, lng),
                    { title: config.title, 'labelText': config.label, "labelOffset": new GSize(-5, -29) });
            } else {
                marker = new GMarker(new GLatLng(lat, lng), { title: config.title });
            }

            _map.addOverlay(marker);
            _console('Placed location (' + lat + ',' + lng + ') on the map.');

            if (config.info_window_html !== null) {
                GEvent.addListener(marker, "click", function() {
                    marker.openExtInfoWindow(_map, "info_window_ext", config.info_window_html, config.info_window_opts);
                });
            }

            _markers.push(marker);

            return marker;
        };

        /**
         * Add an address to the map.  This method will perform a geocoder call to the google api to determine the geocordinates of the given address.
         * The onComplete callback will fire after geoocder request has completed and the address has been added to the map.
         * @methodOf EzRez.UI.GMap
         * @param address {String} Address
         * @param cfg {Object} Configuration parameters
         * @param [cfg.lat] {Float} Default latitude (decimal format) to use in case geocoding fails.
         * @param [cfg.lng] {Float} Default longitude (decimal format) to use in case geocoding fails.
         * @param [cfg.title] {String} Title to show on mouseover of the gmarker.
         * @param [cfg.label] {Integer} Label to show within the gmarker
         * @param [cfg.zoom] {Integer} Zoom level.  Defaults to the default_zoom set at instantiation.
         * @param [cfg.geo_min_dec_figs] {Integer} Defaults to the geo_min_dec_figs set at instantiation.
         * @param [cfg.info_window_html] {String} HTML to show within the gmarker info window
         * @param [cfg.info_window_opts] {ExtInfoWindowOptions} ExtInfoWindow Options.
         * See http://gmaps-utility-library.googlecode.com/svn/trunk/extinfowindow/release/docs/reference.html.
         * @param onComplete {Function} The onComplete callback will fire after geoocder request has completed and the address has been added to the map.
         */
        this.addAddress = function(address, cfg, onComplete) {
            if (!_compatible) {
                return;
            }

            address = address || '';

            var config = $H({
                lat: '',
                lng: '',
                title: '',
                zoom: _default_zoom,
                label: null,
                info_window_html: null,
                info_window_opts: _info_window_opts
            }).merge(cfg).toObject();

            // Disable geo code accuracy filtering
            if (config.info_window_opts === null) {
                config.info_window_opts = {geo_min_dec_figs: 0};
            } else {
                config.info_window_opts.geo_min_dec_figs = 0;
            }

            if (onComplete === undefined) {
                onComplete = function() {
                };
            }

            if (address.length === 0) {
                this.addGeoCord(config.lat, config.lng, config);
                onComplete();
                return;
            }

            var save = function(marker, addressdetails) {

                var value = {adddress: address, marker: marker};
                value = Object.extend(value, config);

                if (addressdetails !== null || addressdetails !== undefined) {
                    value = Object.extend(value, addressdetails);
                }

                _addresses.push(value);
            };

            _geo.getLocations(address, function(result) {

                var status = {};
                status[200] = "Success";
                status[400] = "Bad Request";
                status[500] = "Server Error";
                status[601] = "Missing Query or Missing Address";
                status[602] = "Unknown Address";
                status[603] = "Unavailable Address";
                status[604] = "Unknown Directions";
                status[610] = "Bad Key";
                status[620] = "Too Many Queries";

                var marker = {};

                if (result.Status.code != G_GEO_SUCCESS) {
                    marker = this.addGeoCord(config.lat, config.lng, config);
                    save(marker, null);
                    _console('ClientGeocoder Request Error: ' + result.Status.code + ' - ' + status[result.Status.code] + ' (' + address + ').');
                    onComplete();
                    return;
                }

                _console('Address Accuracy = ' + result.Placemark[0].AddressDetails.Accuracy + '.');

                if (result.Placemark[0].AddressDetails.Accuracy < 7) {
                    _console('Address Accuracy < 7: mapping geo cordinates.');
                    marker = this.addGeoCord(config.lat, config.lng, config);

                    if (marker !== null) {
                        save(marker, null);
                        onComplete();
                        return;
                    }
                }

                _console('Address Accuracy >= 7: mapping address (' + address + ').');

                var p = result.Placemark[0].Point.coordinates;
                marker = this.addGeoCord(p[1], p[0], config);

                if (marker === null) {
                    return false;
                }

                save(marker, result.Placemark[0].AddressDetails);

                _console('Placed address (' + address + ') on the map.');

                onComplete();
            }.bind(this));
        };

        /**
         * Parse DOMElement for gmarker information to add to the map
         * @methodOf EzRez.UI.GMap
         * @param [cfg.info_id] {String} ID of the element containing the gmarker information.  See example for semantic markup.
         * @param [cfg.elements] {Array} An array of DOMElements to parse
         * @param [cfg] {Object} Configuration parameters
         * @param [cfg.clear_markers=false] {Boolean} Clear existing gmarkers on the map
         * @param [cfg.parseComplete] {Function} The callback will fire after geoocder request has completed and the
         * addresses have been added to the map.
         */
        this.parse = function(cfg) {
            if (!_compatible) {
                return;
            }

            var config = $H({
                info_id: null,
                elements: [],
                clear_markers: false,
                parseComplete: function() {
                }
            }).merge(cfg).toObject();

            var gmarkers = [];

            if (config.clear_markers) {
                _clearMarkers();
            }

            if (config.elements.length > 0) {
                gmarkers = config.elements;
            } else {
                var info = $(config.info_id);
                if (!info) {
                    return;
                }
                _info_id = config.info_id;
                gmarkers = info.select('.gmarker');
            }

            var location_index = 0;

            gmarkers.each(function(loc) {
                var address = '';
                var loc_info = {};

                loc.select('.address').each(function(div) {
                    address = div.innerHTML.strip();
                });

                loc.select('.lat').each(function(div) {
                    loc_info.lat = div.innerHTML.strip();
                });

                loc.select('.lng').each(function(div) {
                    loc_info.lng = div.innerHTML.strip();
                });

                loc.select('.title').each(function(div) {
                    loc_info.title = div.innerHTML.strip();
                });

                loc.select('.label').each(function(div) {
                    loc_info.label = div.innerHTML.strip();
                });

                loc.select('.info_window').each(function(div) {
                    loc_info.info_window_html = div.innerHTML.strip();
                });

                this.addAddress(address, loc_info, function() {
                    if (gmarkers.length === location_index + 1) {
                        _console('==== Done Loading Map ====');
                        _map.checkResize();
                        this.zoomAll();
                        config.parseComplete(_markers, _addresses);
                    }

                    location_index++;
                }.bind(this));

            }.bind(this));
        };

        /**
         * Zoom around all gmarkers on the map
         * @methodOf EzRez.UI.GMap
         */
        this.zoomAll = function() {
            if (!_compatible) {
                return;
            }
            if (_markers.size() === 0) {
                return;
            }
            ;
            if (_markers.size() === 1) {
                this.center(_markers.first().getLatLng(), _default_zoom);
            }
            ;

            var lats = [];
            var lngs = [];
            var zbox = {};

            _markers.each(function(m) {
                var latlng = m.getLatLng();
                lats.push(latlng.lat());
                lngs.push(latlng.lng());
            });

            lats.sort();
            lngs.sort();

            // Decimal Degrees
            // Lat:  90N => + 90,  90S => - 90
            // Lng: 180W => -180, 180E => +180
            var latS = lats.first();
            var latN = lats.last();
            var lngW = lngs.first();
            var lngE = lngs.last();

            zbox.sw = new GLatLng(latS, lngW);
            zbox.ne = new GLatLng(latN, lngE);
            zbox.se = new GLatLng(latS, lngE);
            zbox.nw = new GLatLng(latN, lngW);

            var zoomArea = new GPolyline([zbox.nw, zbox.ne, zbox.se, zbox.sw, zbox.nw]);

            zbox.bounds = zoomArea.getBounds();
            zbox.zoom = _map.getBoundsZoomLevel(zbox.bounds);
            zbox.center = zbox.bounds.getCenter();

            this.center(zbox.center, zbox.zoom - 1);

            return zbox;

        };

        /**
         * Center the map at the specified location and zoom level
         * @methodOf EzRez.UI.GMap
         * @param center {Object}
         * @param center.lat {Float} Latitude in decimal format
         * @param center.lng {Float} Longitude in decimal format
         * @param zoom {Integer}
         */
        this.center = function(center, zoom) {
            if (!_compatible) {
                return;
            }

            _map.setCenter(center, zoom);
            _map.checkResize();

            _map_saved_center = center;
            _map_saved_zoom = zoom;
            _centered = true;
        };

        /**
         * Clear all the markers on the map
         * @methodOf EzRez.UI.GMap
         */
        this.clearMarkers = function() {
            _clearMarkers();
        };

        /**
         * Useful if the map is initialized within a hidden (display:none) div.
         * Call FixCenter when showing the map to fix the center alignment.
         * @methodOf EzRez.UI.GMap
         * @param [zoomall] {Boolean=false} Zoom around all gmarkers on the map
         */
        this.fixCenter = function(zoomall) {
            if (!_compatible) {
                return;
            }
            if (_map_saved_center === null || _map_saved_zoom === null) {
                return;
            }

            if (zoomall === undefined) {
                zoomall = false;
            }

            _map.checkResize();
            if (!zoomall) {
                this.center(_map_saved_center, _map_saved_zoom);
            } else {
                this.zoomAll();
            }
        };

        /**
         * When using SSL proxy, this method fixes the google links to point back to google.
         * @methodOf EzRez.UI.GMap
         */
        this.fixGoogleLinks = function() {
            if (!_compatible) {
                return;
            }
            if (!_secure) {
                return;
            }

            // remove the secure proxy url
            var replace = function(link) {
                link.href = link.href.replace(/^https:\/\/[^\?]+\?/, '');
            };

            $(_canvas_id).select('img[src*="poweredby.png"]').each(function(img) {
                // Url is updated by gmap whenever the map center is changed,
                // so we need to use event handles to fix it after each update
                // (for most browsers) and onclick (for ie).

                var link = img.up('a');

                replace(link);

                // For IE
                link.observe('click', function(evt) {
                    replace(link);
                });

                // For the rest
                link.observe('DOMAttrModified', function(evt) {
                    replace(link);
                });
            });

            $(_canvas_id).select('a[href*="terms_maps.html"]').each(function(link) {
                // much easier
                replace(link);
            });
        };

        /**
         * A wrapper for GEvent.addListener: Adds a listener to the map for map event.
         * See http://code.google.com/apis/maps/documentation/events.html.
         * @param evt {String} Google Map event
         * @param listener {GEventListener} Event listener.
         * See http://code.google.com/apis/maps/documentation/reference.html#GEventListener.
         */
        this.addListener = function(evt, listener) {
            if (!_compatible) {
                return;
            }
            GEvent.addListener(_map, evt, listener);
        };

        /**
         * Getter method for the GMap2 instantce
         * @methodOf EzRez.UI.GMap
         * @returns {GMap2} GMap2 instantce
         */
        this.getMap = function() {
            return _map;
        };

        /**
         * Getter method for the GClientGeocoder instance
         * @methodOf EzRez.UI.GMap
         * @returns {GClientGeocoder} GClientGeocoder instance
         */
        this.getGeo = function() {
            return _geo;
        };

        /**
         * Getter method of the addresses hash
         * @methodOf EzRez.UI.GMap
         * @returns {Hash} Hash of addresses that have been added to the map
         */
        this.getAddresses = function() {
            return _addresses;
        };

        /**
         * Getter method of the gmarkers hash
         * @methodOf EzRez.UI.GMap
         * @returns {Hash} Hash of gmarkers that have been added to the map
         */
        this.getMarkers = function() {
            return _markers;
        };

        /**
         * Wrapper method for GBrowserIsCompatible.  See http://code.google.com/apis/maps/documentation/reference.html#GBrowserIsCompatible.
         * @methodOf EzRez.UI.GMap
         * @returns {Boolean} GBrowserIsCompatible
         */
        this.isCompatible = function() {
            return _compatible;
        };

        _map = new GMap2(document.getElementById(_canvas_id), config.map_options);
        _geo = new GClientGeocoder();

        var map_init = function() {
            //this.Center( new GLatLng( 20, 0 ), 1 );
            _map.enableScrollWheelZoom();
            _map.enableContinuousZoom();
            config.setup(_map);

            this.fixGoogleLinks();

            if ($(_info_id) !== undefined) {
                this.parse({info_id: _info_id, parseComplete: config.parseComplete});
            }
        }.bind(this);

        if (typeof(_default_center) == 'string') {
            _geo.getLocations(_default_center, function(result) {
                if (result.Status.code != G_GEO_SUCCESS) {
                    return;
                }

                var p = result.Placemark[0].Point.coordinates;
                this.center(new GLatLng(p[1], p[0]), _default_zoom);
                map_init();

            }.bind(this));
        } else {
            this.center(new GLatLng(_default_center.lat, _default_center.lng), _default_zoom);
            map_init();
        }
    };

    /**
     * Wrapper method for GBrowserIsCompatible.  See http://code.google.com/apis/maps/documentation/reference.html#GBrowserIsCompatible.
     * @static
     * @returns {Boolean} GBrowserIsCompatible
     */
    EzRez.UI.GMap.isCompatible = function() {
        return _compatible;
    };

    /**
     * Wrapper method for GUnload.  See http://code.google.com/apis/maps/documentation/reference.html#GBrowserIsCompatible.
     * @static
     */
    EzRez.UI.GMap.unload = function() {
        if (!_compatible) {
            return;
        }
        GUnload();
    };
})();
