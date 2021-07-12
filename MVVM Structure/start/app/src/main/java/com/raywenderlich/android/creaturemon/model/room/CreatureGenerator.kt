package com.raywenderlich.android.creaturemon.model.room

import com.raywenderlich.android.creaturemon.model.Creature
import com.raywenderlich.android.creaturemon.model.CreatureAttributes

class CreatureGenerator {

    fun generateCreature(attributes: CreatureAttributes, name: String = "", drawable: Int = 0 ) : Creature{
        val hitPoints = 5 * attributes.intelligence + 3 * attributes.strength + 4 * attributes.endurance
        return Creature(attributes, hitPoints, name, drawable)
    }
}