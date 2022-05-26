package com.isit322.plant_tracker.data class RGeoData(
    val plus_code: PlusCode,
    val results: List<Result>,
    val status: String
)

data class PlusCode(
    val compound_code: String,
    val global_code: String
)

data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val plus_code: PlusCodeX,
    val types: List<String>
)

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

data class Geometry(
    val bounds: Bounds,
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)

data class PlusCodeX(
    val compound_code: String,
    val global_code: String
)

data class Bounds(
    val northeast: Northeast,
    val southwest: Southwest
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Viewport(
    val northeast: NortheastX,
    val southwest: SouthwestX
)

data class Northeast(
    val lat: Double,
    val lng: Double
)

data class Southwest(
    val lat: Double,
    val lng: Double
)

data class NortheastX(
    val lat: Double,
    val lng: Double
)

data class SouthwestX(
    val lat: Double,
    val lng: Double
)