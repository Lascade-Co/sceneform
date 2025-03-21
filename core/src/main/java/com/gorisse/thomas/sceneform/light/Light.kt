package com.gorisse.thomas.sceneform.light

import com.google.android.filament.EntityInstance
import com.google.android.filament.IndirectLight
import com.google.android.filament.LightManager
import com.gorisse.thomas.sceneform.*

/**
 * Default directional light intensity.
 */
const val defaultMainLightIntensity = 5000.0f
/**
 * Default sunny day sun directional light intensity
 */
const val sunnyDayMainLightIntensity = 100_000.0f

typealias Light = Int
typealias LightInstance = Int

/**
 * @see LightManager.getInstance
 */
val Light.instance @EntityInstance get() : LightInstance = Filament.lightManager.getInstance(this)

/**
 * @see LightManager.Builder.build
 */
fun LightManager.Builder.build(): Light =
    Filament.entityManager.create().apply {
        build(Filament.engine, this)
    }

/**
 * @see LightManager.getType
 */
val Light.type: LightManager.Type
    get() = Filament.lightManager.getType(instance)

/**
 * @see LightManager.getPosition
 * @see LightManager.setPosition
 */
var Light.position: Direction
    get() = FloatArray(3).apply {
        Filament.lightManager.getPosition(instance, this)
    }.toPosition()
    set(value) = Filament.lightManager.setPosition(instance, value.x, value.y, value.z)

/**
 * @see LightManager.getDirection
 * @see LightManager.setDirection
 */
var Light.direction: Direction
    get() = FloatArray(3).apply {
        Filament.lightManager.getDirection(instance, this)
    }.toDirection()
    set(value) = Filament.lightManager.setDirection(instance, value.x, value.y, value.z)

/**
 * @see LightManager.getIntensity
 * @see LightManager.setIntensity
 */
var Light.intensity: Float
    get() = Filament.lightManager.getIntensity(instance)
    set(value) = Filament.lightManager.setIntensity(instance, value)

/**
 * @see LightManager.getColor
 * @see LightManager.setColor
 */
var Light.color: Color
    get() = FloatArray(3).apply {
        Filament.lightManager.getColor(instance, this)
    }.toColor()
    set(value) = Filament.lightManager.setColor(instance, value.r, value.g, value.b)

/**
 * @see LightManager.isShadowCaster
 * @see LightManager.setShadowCaster
 */
var Light.isShadowCaster: Boolean
    get() = Filament.lightManager.isShadowCaster(instance)
    set(value) = Filament.lightManager.setShadowCaster(instance, value)

/**
 * @see LightManager.getFalloff
 * @see LightManager.setFalloff
 */
var Light.falloff: Float
    get() = Filament.lightManager.getFalloff(instance)
    set(value) = Filament.lightManager.setFalloff(instance, value)


/**
 * @see LightManager.getSunHaloFalloff
 * @see LightManager.setSunHaloFalloff
 */
var Light.sunHaloFalloff: Float
    get() = Filament.lightManager.getSunHaloFalloff(instance)
    set(value) = Filament.lightManager.setSunHaloFalloff(instance, value)

/**
 * @see LightManager.getSunHaloSize
 * @see LightManager.setSunHaloSize
 */
var Light.sunHaloSize: Float
    get() = Filament.lightManager.getSunHaloSize(instance)
    set(value) = Filament.lightManager.setSunHaloSize(instance, value)

/**
 * @see LightManager.getSunAngularRadius
 * @see LightManager.setSunAngularRadius
 */
var Light.sunAngularRadius: Float
    get() = Filament.lightManager.getSunAngularRadius(instance)
    set(value) = Filament.lightManager.setSunAngularRadius(instance, value)

/**
 * @see LightManager.getInnerConeAngle
 * @see LightManager.setSpotLightCone
 */
var Light.innerConeAngle: Float
    get() = Filament.lightManager.getInnerConeAngle(instance)
    set(value) = Filament.lightManager.setSpotLightCone(instance, value, outerConeAngle)

/**
 * @see LightManager.getOuterConeAngle
 * @see LightManager.setSpotLightCone
 */
var Light.outerConeAngle: Float
    get() = Filament.lightManager.getOuterConeAngle(instance)
    set(value) = Filament.lightManager.setSpotLightCone(instance, innerConeAngle, value)

// TODO: We need a clone on the Filament side in order to copy all values
fun Light.clone() = LightManager.Builder(type)
    .castShadows(isShadowCaster)
    .position(position.x, position.y, position.z)
    .direction(direction.x, direction.y, direction.z)
    .intensity(intensity)
    .color(color.r, color.g, color.b)
    .falloff(falloff)
    .sunHaloFalloff(sunHaloFalloff)
    .sunHaloSize(sunHaloSize)
    .sunAngularRadius(sunAngularRadius)
    .spotLightCone(innerConeAngle, outerConeAngle)
    .build()

/**
 * Destroys a Light and frees all its associated resources.
 */
@JvmOverloads
fun Light.destroy() {
    Filament.lightManager.destroy(this)
}

/**
 * @see IndirectLight.Builder.build
 */
fun IndirectLight.Builder.build(): IndirectLight = build(Filament.engine)

/**
 * Destroys an IndirectLight and frees all its associated resources.
 */
fun IndirectLight.destroy() {
    Filament.engine.destroyIndirectLight(this)
}