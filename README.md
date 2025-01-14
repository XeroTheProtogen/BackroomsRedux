[REPOSITORY NO LONGER IN USE, GO TO PROJECT REDUX INSTEAD]

# Backrooms Redux
[IMPORTANT] Due to an issue with Limlib Fabric, you won't see any new pieces on a level unless you create a new world or reset the data for that dimension (Unverified) altogether.
[NOTE] All credits for any 3rd party code, assets and information used can be found at the bottom of the readme, alongside anyone whose contributed to Backrooms Redux directly (Thanks for supporting me btw :>)

## Introduction
> If you're not careful and you noclip out of reality in the wrong areas, you'll end up in the Backrooms, where it's nothing but the stink of old moist carpet,
> the madness of mono-yellow, the endless background noise of fluorescent lights at maximum hum-buzz, and approximately six hundred million square miles of randomly segmented empty rooms to be trapped in
God save you if you hear something wandering around nearby, because it sure as hell has heard you. - Unknown (If you know who said this, ping me on the discord)

Backrooms Redux is a mod that seeks to continue the legacy of Forameus' "Faithful Backrooms" & LudoCrypt's "The Corners", 
while being unique with a sanity and noclip system, interconnective with built-in addon support, and supportive of its community through guides on making addons, using LimLib and more!

## Levels
>…normal levels form the framework of the Backrooms and vary greatly in size, danger, and habitability.
>Typical levels—for whatever value of "typical" can be expected from the Backrooms—tend to be infinite in size (though "bounded" levels are not unheard of)
>and often provoke a feeling of uncanny familiarity to those happening upon them.
>The laws of physics and nature as we know them cannot be relied on in the depths of the Backrooms; nevertheless, some of the more hospitable and stable levels are home to thriving communities,
>and some inhabitants have managed to survive within even the most dangerous levels. - The Backrooms Wikidot

Backrooms Redux intends to add levels 0-11, with levels being released in waves of three. Sublevels may be added, but that could easily change.

### Current levels:
- [ ] Wave 1: "The Beginning"
  - [x] Level 0, The Tutorial
  - [ ] Level 1, Habitable Zone
  - [ ] Level 2, Abandoned Utility Halls AKA Pipe Dreams
  - [ ] The Hub
        
- [ ] Wave 2: "Electric Interlim"
  - [ ] Level 3, Electrical Station
  - [ ] Level 4, Abandoned Office
  - [ ] Level 5, Terror Hotel
        
- [ ] Wave 3: "Deeper Waters"
  - [ ] Level 6, Lights Out
  - [ ] Level 7, Thalassophobia
  - [ ] Level 8, Cave System

- [ ] Wave 4: "An Endless Journey Truly Begins"
  - [ ] Level 9, The Suburbs
  - [ ] Level 10, The Bumper Crop AKA Fields of Wheat
  - [ ] Level 11, The Endless City

## Entities
TODO

## Addon Support
Backrooms Redux is designed with addon support in mind, and as such, we've provided the following 
tools to allow for the safe modification of levels in this mod and any addons making use
of this system.

### PieceArraySingleton
A singleton object used to register and retrieve PieceManagers. It grants addon makers
the ability to register new pieces to a level's piece pool, and the option to allow
addon support for the addon's piece pools during mod initialization.
**More Documentation Coming Soon**

### PieceManager
The objects stored in PieceArraySingleton, its job is to store 
and retrieve data relating to piece names and piece pool processors.
**More Documentation Coming Soon**

### PieceProcessor
A functional interface that can be used in chunk generators
to add procedural block modifications, it can be implemented into a chunk generator
if addon support is desired for the level. 
**More Documentation Coming Soon**

## Credits
### Supporters
To those who've supported my modding journey up to this point, 
I thank you greatly for helping me enjoy doing stuff like this.

### Project Redux
The people who helped me develop this mod directly, thank you for contributing 
and assisting in the development of this dream project of mine.
Whether you were a texture or model designer who made assets or a programmer who helped with developing the internals of the mod,
Thank you :)

Texture Designers: TB (Made textures for some of the blocks in Level 1)

### Dependencies
Liminal Library by Ludocrypt.
Liminal Library Fabric by V-Fast & Clomclem.
Cardinal Components by the Ladysnake group.
Geckolib by Gecko, Tslat, DerToaster, and any other creators not mentioned

### Third-party assets
Light hum sound originally provided by Zapsplat.

### Backrooms stories
Canon used is the Backrooms Wikidot.
