package com.koleff.kare_android.common

import kotlin.math.PI

object DegreeUtils {

    fun toRadian(degree: Float): Float{
        return degree * (PI / 180f).toFloat()
    }

    fun toDegree(radian: Float): Float{
        return radian * (180f / PI).toFloat()
    }
}