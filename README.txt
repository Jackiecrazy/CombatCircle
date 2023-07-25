
Features:
- encirclement. mobs attempt to surround you when giving chase, preventing single-file kiting.
    - when circling, certain mobs will prefer certain points relative to the player
- ordered attacks. mobs queue up their attacks while staying out of your range to perform specific moves once they gain permission to attack.
    - archers will try to only shoot when they have los
- avoidance. mobs avoid other mobs that are currently attacking. Attacks can be defined to cause or not cause collateral damage
    - on large aoe attacks, mobs nearby attempt to move out of the way.
- combat moves. json-defined mob moves with windup, damage, and cooldown frames, and *maybe* animation
    - should include both melee and ranged moves wherever possible
- a "stun" or "counter" attack the player can use to put enemies out of combat for a few moments, so the player can attack other enemies in the circle
- a "dodge" move for the player to quickly travel into, out of, or within the circle
- "push" and "pull" moves that let the player move enemies into and out of the circle
- data-defined special mobs that grant special behavior when locked onto a target:
    - "clerics" hang back to provide buffs like increasing attack allocation points, incentivizing you to attack them first
    - "captains" ignore attack allocation to add pressure.
    - "commanders" reorganize enemies into certain structures.
        -