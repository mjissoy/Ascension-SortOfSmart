# Runic Path
### Realms:
1. Trace Sensing
2. Rune Knowing
3. Script Visualization
4. Pattern Enlightenment
5. Rune Breath
6. Soul Inscription
7. Living Script
8. Dao Seed
9. Origin Spark
10. Infinite Script  

Higher realms would unlock deeper runes and more rune slots and time for casting.  

End Goal for Path: Over a hundred Runes and a flexible Runic Sequence Casting system 
like Ars Nouveau or Hex Casting or Iron's Spells and Spellbooks

---

## Core Gameplay Loop:
>Discover rune → Understand rune → Record rune → Combine rune → Cast formula → Refine formula

#### Runic Path Entry Conditions
- At least Major Realm 1 in any major path
- A discovered rune source, tome, tablet, or teacher
- Maybe a "First Inscription" breakthrough ritual???


#### Types of Runes
1. Source Runes: Core runes that are found in the natural world around you, describing the order of all things
2. Intent Runes: Action based runes that describe what things do and how they do it
3. Form Runes: Runes that outline the fundamental shapes of all things
4. Modifier Runes: Sub Runes born from different aspects, that speak of the features something possesses

### Splitting Runes
Runes would be denoted using common tags, and split further using more tags, E.g. a block like deepslate may have a rune from the common Stones tag, and then another deeper rune for being deepslate, and to observe the deeper rune you would need a higher level of Runic Path.  
Example names: Surface Runes, Deep Runes, Hidden Runes etc.  
Higher realms would expose deeper runes.  

### Archetypes of Runic Path Players:
Soft archetypes that would be enforced slightly or pushed with custom breakthroughs and runes one finds?  

**Inner Rune Master** → Focuses on carving runes onto the body, soul, bones, meridians, or inner sea.  
These players would use  sequences mainly around the self, such as
Passive buffs, Self-healing, Resistance, Stat boosts, Movement buffs, 
Temporary transformation effects, Lower range, higher survivability etc.

**Outer Rune Masters** → Focuses on writing commands onto reality itself.  
These players would use  sequences mainly around exterior events and actions, such as 
Walls, Fields, Traps, Projectile  sequences, 
Crowd control, Terrain manipulation, Higher range etc.


---

## Learning a Rune: 
### States of Learning
Unseen Rune → The Player has no idea that this Rune exists.  
Glimpsed Rune → The Player has seen the Rune, but they have a low understanding of it, barely scratching the surface.   
Observed Rune → The Player can see the Rune, but they cannot cast a spell with this Rune, not having fully understood it.  
Known Rune → The Player knows this Rune and can use it.

### How To Learn
1. Tomes and Tablets which store a rune or two in them, and when used by the Player they learn said Rune. 15 or 20.
2. Runic Sight Skill that lets Players see the runes in things they are looking at via Raycasting. Losing focus disrupts the learning. Grows stronger with more Runes learned.

### Runic Sight Progression
0 known runes:
No passive sight. Must learn from tomes/tablets.

10 known runes:
Unlock Runic Sight active skill.

25 known runes:
Common mob/entity runes become visible.

40 known runes:
Runic Sight evolves into Runic Visualization passive, making all known and seen runes slightly visible at all times.

75+ known runes:
Complex formulas, spell residues, traps, and hidden inscriptions become visible.
---

## Runic Casting System:
- Player becomes slowed or rooted
- Cannot use items or use other active skills
- Nearby enemies will be slowed significantly
- Opens a Runic Casting UI
- Player has limited time to input rune sequence
- Have to cast a runic spell to exit the casting UI
- If no spell is cast when the time runs out, also get hit by the backlash

### Realm Increase Bonuses
Realm 1: 2 rune slots, 3 seconds  
Realm 2: 2 rune slots, 4 seconds  
Realm 3: 3 rune slots, 5 seconds  
Realm 4: 3 rune slots, 6 seconds  
Realm 5: 4 rune slots, 7 seconds  
Realm 6: 4 rune slots, 8 seconds  
Realm 7: 5 rune slots, 9 seconds  
Realm 8: 5 rune slots, 10 seconds  
Realm 9: 6 rune slots, 11 seconds  
Realm 10: 7 rune slots, 12 seconds  

### Failing in Rune Casting
- "The Runes do not align" action bar message
- Small backlash damage
- Brief nausea/confusion
- Consumes a little qi

### Mastery of Runic Sequences 
Casting the same Runic Sequence over and over would increase its mastery, with grades like Unstable, Stable, Mastered, Perfected? 
This is an optional function that may not be implemented

### Runic Items
#### Runic Codex
A tome created by a Runic Path Player which stores all their known runes, 
and the percentages of their learning of other runes, 
as well as runic  sequences they have used before that works.  It would also let players arrange 
favourite  sequences, and if they hold the Codex when opening the UI, instead of manually making 
the runic sequence from scratch, they could select a favourite sequence immediately, but there 
are slot limitations to them, like 3 or 4 slots max.  


#### Runic Brush
A Brush item, with different types/grades, which would give more runic slots for casting and also benefit certain runes.  
A Player would have to hold the Brush in their Main Hand when activating the Runic Casting UI for the effect to happen.  
```
Examples: Needs work.

Stone-Tip Brush:  
Stone formulas gain stability or duration, +1 slot.

Flame-Hair Brush:  
Flame formulas gain potency, +1 slot.

Soulhair Brush:  
Soul formulas cast faster and +2 slots.

Blood-Ink Brush:  
Can replace one missing rune in a blood/self formula, but costs health, +1 slot.
```

---

### Starting Code:

- 20 Runes
- Seven Sequences
- A Rough UI (EasyGUI?)
- 1 Basic Brush and Codex
- An Observation System
- A Couple Runic Tomes

## All Implemented Classes and Systems
RunicRune