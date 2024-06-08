package jackiecrazy.combatcircle.move;
/*
An action is a single json element with an action ID, a "condition", and several "arguments". It may inherit or refer to a "template" action json for faster definition.
Arguments, such as "vector", "duration", or "damage interrupt threshold", only affect the current action, and not its sub-actions.
A condition parameter accepts a list of conditions, such as "within x blocks of player", "has X kills", or "is named Johnny".
Actions have "tags" that tell the in-game moveset compiler what it does, such as "movement", "damage" or "spawn".

A move consists of a list of conditions and a single timer action, such as "stand still", "move to this location", or "jump with this velocity".
There is also a special "jump to" move. This is only read by the moveset, and causes it to advance to the move indicated by the index. This interrupts all other actions.
Each timer action exposes several further action lists, such as "while moving", "upon landing", and "on collision". Other actions, such as "project box of size X", "play particle sequence", or "spawn an entity", link to them here.


Sub-actions of an action are evaluated simultaneously but not concurrently, and sub-actions override parent actions. For example, a piglin brute with both "hold up a shield" and "stand still" actions linked to a single movement base will wait once "stand still" evaluates to true, but will hold up a shield regardless of whether it is moving or standing.
Sub-actions of actions will inherit the last source of proxy. For example, the evoker summoning a zombie defined with a set of subsequent moves will cause the zombie, and not the evoker, to execute these moves.
All sub-actions will be interrupted when their parent action on that proxy ends. This protocol is for sanity reasons.

A moveset consists of several moves, an optional central condition list, as well as a priority and a weight parameter.
A moveset is evaluated from top to bottom. The first move is executed first, and after completion the second move is executed, etc.

A mob may be defined with a list of movesets. Condition-less movesets will be chosen from randomly with the help of the weight parameter. Conditional movesets override conditionless movesets.
 */

/*
 * actions are generated by action factories, which have their own registry.
 * When scouring the json file for an action, the action type is grabbed as an id, which is compared against the action registry and resolved into an action factory.
 * The action factory created the desired new action and feeds it the provided Json object.
 * To save overhead these are stored per entity type and the entity only holds an integer list of behavior they should pull up.
 * The action has a single perform() method that executes the action given the mob and target.
 *
 * The wrapper holds an array of currently executing timer actions, which are collectively ticked and performed.
 * Expiry or jump removes the action from the queue. If expired, the next action is slotted in.
 * If jumped, all other actions are reset and cleared from the queue, then the jumped action is slotted in.
 * timer actions possess a trigger list. When an event occurs, the executing moveset list is queried for matching triggers.
 * the last one takes precedence as it's the most recent one to be added, the attached moveset is passed by wrapper into the executing moveset queue as well
 * todo how does this system handle parent timer action ending?
 *  uhhhh when the parent timer expires it's responsible for propagating an expiry to all its children? Kinda messy ngl
 * triggers: fall, on being hit, on take (posture) damage, stun, gain effect, on fire, die, emit sculk event?, entering block?, stab?
 * todo repeat action with delay of x ticks
 * can we categorize this as just a special action?
 */

/*
implementation notes:
encirclement: safe distance, avoidance vector towards specific entity types, slot placement priority, "cover" (whatever that is, I'm still not sure)
chasing: boid behavior, smooth transition to encirclement
actions: ID, condition, arguments, (template), tags, sub-actions, proxy inheritance
move: "movement" action
moveset: moves, conditions, weight, global cooldown
mob: movesets, any special tags, encirclement

 */