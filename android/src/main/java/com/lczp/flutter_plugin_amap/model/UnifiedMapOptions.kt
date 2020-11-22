package com.lczp.flutter_plugin_amap.model

import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.model.MyLocationStyle

/**
 * @author guofei
 * @date 2020/11/22 12:55
 */
class UnifiedMapOptions(
        private val mapType: Int = AMap.MAP_TYPE_NORMAL,
        private val myLocationStyle: Int = MyLocationStyle.LOCATION_TYPE_SHOW,
        private val rotateGesturesEnabled: Boolean = true,
        private val scrollGesturesEnabled: Boolean = true,
        private val scaleControlsEnabled: Boolean = false,
        private val compassEnabled: Boolean = false,
        private val tiltGesturesEnabled: Boolean = true,
        private val zoomControlsEnabled: Boolean = false,
        private val zoomGesturesEnabled: Boolean = true,
        private val myLocationEnabled: Boolean = false,
        private val setMyLocationButtonEnabled: Boolean = false) {

    fun getMyLocationEnable(): Boolean {
        return myLocationEnabled
    }

    fun getMyLocationStyle(): Int {
        return myLocationStyle
    }

    fun getMyLocationButtonEnabled(): Boolean {
        return setMyLocationButtonEnabled
    }

    fun toAMapOptions(): AMapOptions {
        return AMapOptions()
                .mapType(mapType)
                .zoomControlsEnabled(zoomControlsEnabled)
                .zoomGesturesEnabled(zoomGesturesEnabled)
                .rotateGesturesEnabled(rotateGesturesEnabled)
                .tiltGesturesEnabled(tiltGesturesEnabled)
                .scaleControlsEnabled(scaleControlsEnabled)
                .scrollGesturesEnabled(scrollGesturesEnabled)
                .compassEnabled(compassEnabled)
    }

}