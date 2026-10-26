
; Global Variable Definitions

; Engine Data
Const UsingPak=True

Const Version=10		; type of version - 0-9 demos, 10+ full
					;	0,1 - normal demo release (1 was used as of v1.08)
					;   2 - Joerg Mad Data
					;   3 - Weasel Circus
					; 	4 - General Affiliate 4 (Currently BrainGamez)
					;   5 - General Affiliate 5 (Currently Games4Win)
					;   6 - General Affiliate 6 (Currently Search Amateur)
					; 	7 - Five Star Network Affiliate Release
					;   8 - Download-Free-Games Affiliate Release
					;	9 - Coffeebreakarcade Affiliate Release
					;	10 - normal full release
					;	11 - press demo full release (extra menu)
					; 	12 - astragon (World 8 and 9 disabled)

Global VersionNo1=10	; X.00
Const VersionNo2=0		; 0.X0
Const VersionNo3=0		; 0.0X

Const PackOverRide=1	; 1 - always use, 	-1 - never use
Const PackCleanUp=True ; delelted all tmp and x files if true

Global PackFileCount=0

Global DisplayWidth=640
Global DisplayHeight=480
Dim MyGfxModeExists(5),MyGfxModeWidth(5),MyGfxModeHeight(5)
Global MyGfxMode=1
Global MyColMode=0
Global MyWindowed=0
Global MyWindowedEd,MyColModeEd
Global OldGfxMode, OldWindowed
Global NoTimeLimit=0
Global Lockworlds=0



Global DoesSoundExist=True
Const BaseSpeed=24			; number of steps for a regular move (must be divisible by 2)

Global Music

Global Language=0	; 0 - English
					; 1 - German

; User Option Settings
Global DetailLevel=3	; 1-5


Global SecretMusic=0
; Game Variables


Global FPS, FPSCounter,FPSTime
Global ShowFPS=False
Global Tween#,TweenPeriod,TweenTime,TweenElapsed,TweenTicks
Global GamePhase=0			; 0-menu, 1-game

; Global 3D Data

Global LevelLight			; only one light used throughout game
Global CurrentLightRed,CurrentLightBlue,CurrentLightGreen
Global TargetLightRed,TargetLightGreen,TargetLightBlue
Global LightChangeSpeed
Global CurrentAmbientRed,CurrentAmbientBlue,CurrentAmbientGreen
Global TargetAmbientRed,TargetAmbientGreen,TargetAmbientBlue
Global AmbientChangeSpeed


Global Camera
Global CameraDX#,CameraDY#	; for cameraswoops: if positive, moves and reduces by step
							;					if negative, moves and adds by step
Global CameraDZ#
Global CameraDStep#=.5
Global FreeCameraX#,FreeCameraZ#


Global SaveGameStateActive=False


; -----------
; LEVEL DATA
; -----------

; 1) 3D Model

Global LevelMesh
Dim LevelSurface(50) ; maximum of 50 for entire level
Global NofLevelSurfaces


Global BackGroundPlane,BackGroundPlane2,Snow, Rain, Lightning, LightningTimer, Psyche
Global LevelRed,LevelGreen,LevelBlue,LevelYellow


; 2) Game Logic

Global LevelTimer
Global CurrentLevelScore

Global MapName$
Global MapID
Global MapStyle,MapBackground
Global MapWidth, MapHeight
Global MapTime, MapMilliTime
Dim MapTile(101,101); maximum 100*100 tiles in level
Dim MapTile2(100,100); backup

MapTile(101,101)=0

Global LevelCompleted	; 0 - NO, 1-YES, in animation, 2- yes/done and go to menu
						; 3- yes/done and restart same level, 4- yes/done and start next level
Global LevelCompletedTimer
Global BothCompleted	; bits 1-4 show if player 1-4 has completed

Global BackGroundEffect=2	; 1- walls, 2- sky/forest




; Level-Textures
; --------------
; (loaded in setup)
Global WaterTexture,  IceTexture

;depending on style:
Global FloorTexture,FloorTexture2,FloorTexture3,FloorTexture4
Global FloorTextureSign
Global WallTextureTop1
Global WallTextureSide1a,WallTextureSide1b,WallTextureSide2a,WallTextureSide2b,WallTextureSide3a,WallTextureSide3b


;global for all styles:
Global IceCurveTexture1, IceCurveTexture2, IceCurveTexture3, IceCurveTexture4
Dim ConveyorTexture1(2), ConveyorTexture2(2), ConveyorTexture3(2), ConveyorTexture4(2)
Dim SwitcherTexture1(2), SwitcherTexture2(2), SwitcherTexture3(2), SwitcherTexture4(2)
Global BreakAwayTexture1, BreakAwayTexture2, BreakAwayTexture3
Global ElectroTexture
Global LavaTexture
Dim TeleportTexture(8)



Global BackGroundTexture,BackGroundTexture2

; Texture Effects
; ---------------

Global Watertexu#=0.0
Global Watertexv#=0.0
Dim conveyortex#(2)
Conveyortex#(1)=0.0
Conveyortex#(2)=0.0




; -------
; OBJECTS
; -------

; Stinky-Data
; -----------

Dim PlayerMesh(4) ; the mesh
Dim PlayerExists(4)
Dim FocusOnPlayer(4) ; true (controlling this character) or false (controlling another)
Global FocusSwitchTimer

Dim PlayerEntity(4)
Dim PlayerShadowEntity(4)
Dim PlayerTexture(4),PlayerTextureB(4),PlayerTextureC(4),PlayerTextureD(4)

Dim PlayerX(4),PlayerY(4) ; current tile position
Dim PlayerX2(4),PlayerY2(4) ; tile goal (if moving)

Dim PlayerMoving(4) ; true or false
Dim PlayerMovingX(4),PlayerMovingY(4) ;-1/0/+1 depending on direction of movement
Dim PlayerMovingTimer(4) ; for how many frames will player keep moving?
Dim PlayerSpeed(4) ; the 'reset' value for MovingTimer

Dim PlayerRotationGoal(4), PlayerRotation(4) ; which way is turning/turned

Dim PlayerDying(4), PlayerDyingTimer(4)
Dim PlayerDead(4) ; true or false

Dim PlayerShrink(4)
Dim PlayerAnimCurrent(4),PlayerAnimNew(4); new is set, current is what's currently happening
	; new/current is used to switch animations already in progress, e.g. walking/pushing
							; 0 - none			(frame 1)
							; 1 - walking		(frames 2-18)
							; 2 - pushing walk	(frames 19-32)
							; 3 - dying
							; 4 - waving idle
							; 5 - tapping idle
							; 6 - bouncing idle
							; 7 - bouncing idle v2

Dim PlayerIdleCounter(4),PlayerBlinkCounter(4),PlayerBeforeBlinkTexture(4)
Dim PlayerOn(4) ; on a box in water?
Dim xsbomb(4), ysbomb(4); used For kaboom

Global View3d=False
Global View3drotation, view3drotation2
Dim PlayerView3drotation(4)


; Box-Data	(IF UPDATED, ALSO INCLUDE IN CLEANUPBOXES())
; --------

Global WoodBoxMesh, SteelBoxMesh, GhostBoxMesh, ReflectorMesh, BoulderMesh, BoomboxMesh, PrismMesh		; the meshes 
Global WoodBoxTexture,SteelBoxTexture, GhostBoxTexture, ReflectorTexture, BoulderTexture, BoomboxTexture, PrismTexture
Global StickyCubeTexture, LinkSphereRedTexture,	LinkSphereYellowTexture, LinkSphereBlueTexture, LinkSphereGreenTexture, PushCannonTexture
;Global BoxShadowTexture, BoxShadowMesh

Global NofBoxes=0
Const MaxBoxes=400

Dim BoxEntity(MaxBoxes);, BoxShadowEntity(MaxBoxes)
Dim BoxType(MaxBoxes)	; 0-wood, 1-steel, 2,3-reflector, 4-ball, 5-ghostbox, 6-boom, 7-prism

Dim BoxX(MaxBoxes),BoxY(MaxBoxes)		; current tile position
Dim BoxX2(MaxBoxes),BoxY2(MaxBoxes)		; tile goal (if moving)

Dim BoxMoving(MaxBoxes)			; true or false
Dim BoxMovingX(MaxBoxes),BoxMovingY(MaxBoxes)	; -1/0/+1 depending on direction of movement
Dim BoxMovingTimer(MaxBoxes)	; for how many frames will Stinky keep moving?
Dim BoxSpeed(MaxBoxes)			; the 'reset' value for MovingTimer
Dim BoxDead(MaxBoxes)			; true or false

Dim BoxSwayingTimer(MaxBoxes);		-1 if not swaying, otherwise through motion 0-359
Dim BoxSwayX(MaxBoxes),BoxSwayY(MaxBoxes)
Dim BoxCannonMovingX(MaxBoxes),BoxCannonMovingY(MaxBoxes),BoxCannonTimer(MaxBoxes),BoxCannonSpeed(MaxBoxes)

; Conveyors
; ---------

Dim ConveyorActive(2)			; true or false

; Break-Away Tiles 
; ----------------

Global BreakAwayMesh

Global NofBreakAways=0
Const MaxBreakAways=400

Dim BreakAwayEntity(MaxBreakAways)
Dim BreakAwayX(MaxBreakAways),BreakAwayY(MaxBreakAways)
Dim BreakAwayType(MaxBreakAways) ; 0-almost broken, 1-2steps, 2-3steps

; Switcher Tiles 
; ----------------

Global SwitcherMesh

Global NofSwitchers=0
Const MaxSwitchers=400

Dim SwitcherEntity(MaxSwitchers)
Dim SwitcherX(MaxSwitchers),SwitcherY(MaxSwitchers)
Dim SwitcherType(MaxSwitchers) ; 0-up, 1-right, 2-down, 3-left



; Electro Tiles 
; -------------

Global ElectroMesh	; a single square

Global NofElectros=0
Const MaxElectros=400

Dim ElectroMap(100,100)

Dim ElectroEntity(4)
Dim ElectroX(MaxElectros),ElectroY(MaxElectros)
Dim ElectroExists(4)
Dim ElectroType(MaxElectros) ; 1,2,3 - (+1)*BaseTimer for timer
Dim ElectroTimer(4) ; current timer (countdown until less than basetimer- then turns on)
Const ElectroBaseTimer=BaseSpeed*2.5

; Spikes 
; -------------

Global SpikeMesh	
Global SpikeTexture

Global NofSpikes=0
Const MaxSpikes=400

Dim SpikeMap(100,100)

Dim SpikeEntity(MaxSpikes)
Dim SpikeX(MaxSpikes),SpikeY(MaxSpikes)
Dim SpikeType(MaxSpikes) ; 0,1,2,3 
Dim SpikeTimer(MaxSpikes) ; current timer (countdown until less than basetimer- then turns on)
Const SpikeBaseTimer=BaseSpeed*3


; Cannons & Fireballs
; -------------------

Global CannonMesh
Global CannonTexture

Global NofCannons=0
Const MaxCannons=400

Dim CannonEntity(MaxCannons)
Dim CannonX(MaxCannons),CannonY(MaxCannons)
Dim CannonMovingX(MaxCannons),CannonMovingY(MaxCannons)
Dim CannonSpeed(MaxCannons) ; 1,2,3 - (+1)*BaseTimer for timer
Dim CannonTimer(MaxCannons) ; current timer (countdown until less than basetimer- then turns on)
Const CannonBaseTimer=BaseSpeed*5

Global FireballMesh

Global NofFireballs=0
Const MaxFireballs=200

Dim FireballEntity(MaxFireballs)
Dim FireballX(MaxFireballs),FireballY(MaxFireballs)
Dim FireballX2(MaxFireballs),FireballY2(MaxFireballs)
Dim FireballMovingX(MaxFireballs),FireballMovingY(MaxFireballs)
Dim FireballMovingTimer(MaxFireballs)
Dim FireballDead(MaxFireballs)
Const FireballSpeed=BaseSpeed

; LaserGates
; ----------

;Global LaserGateMesh
Global IronGateMesh
;Dim LaserGateTexture(5)
Global FinalGateAnim

Global NofLaserGates=0
Const MaxLaserGates=400

Dim LaserGateEntity(MaxLaserGates), LaserGateEntity2(MaxLaserGates)
Dim LaserGateX(MaxLaserGates),LaserGateY(MaxLaserGates)
Dim LaserGateCol(MaxLaserGates)

;Dim LaserGateEntity(5)	; final (0) and one per colour 1/2/3/4
;Dim LaserGateExists(5)	; true or false if the mesh exists
Dim LaserGateActive(9)	; true or false in regards to buttons

; Buttons 
; -------------

Global ButtonOnTexture,ButtonOffTexture

Global NofButtons=0
Const MaxButtons=400

Dim ButtonEntity(MaxButtons)
Dim ButtonX(MaxButtons),ButtonY(MaxButtons)
Dim ButtonType(MaxButtons) ; 0/1/2 + Color*3
Dim ButtonActive(MaxButtons) ; True of False (as in On or Off)
Dim ButtonTimer(MaxButtons) ; current timer for timer buttons (countdown until zero - then turns off)
Const ButtonBaseTimer=BaseSpeed*15

; Monsters	
; --------

Dim MonsterMesh(12)		; the meshes (slimer, bomber,...)
Dim MonsterTexture(12)
;Dim MonsterShadowMesh(5)

Global NofMonsters=0
Const MaxMonsters=600

Dim MonsterEntity(MaxMonsters)
Dim MonsterShadowEntity(MaxMonsters)

Dim MonsterType(MaxMonsters)	; 0-coily, 1/2-zbot, 3/4-kaboom, 5/6-saucer, 7/8-chomper, 9-ghost
								; 10-rainbow spirit, 11-broken z-bot, 12-mother ship

Dim MonsterX(MaxMonsters),MonsterY(MaxMonsters)		; current tile position
Dim MonsterX2(MaxMonsters),MonsterY2(MaxMonsters)		; tile goal (if moving)

Dim MonsterMoving(MaxMonsters)			; true or false
Dim MonsterMovingX(MaxMonsters),MonsterMovingY(MaxMonsters)	; -1/0/+1 depending on direction of movement
Dim MonsterMovingTimer(MaxMonsters)	; for how many frames will Stinky keep moving?
Dim MonsterSpeed(MaxMonsters)			; the 'reset' value for MovingTimer
Dim MonsterDying(MaxMonsters)
Dim MonsterDyingTimer(MaxMonsters)
Dim MonsterDead(MaxMonsters)			; true or false

Dim MonsterData(MaxMonsters,10)			; various uses

; Fish
; -----

Global FishMesh, FishTexture
Global NofFish=0
Const MaxFish=200

Dim FishEntity(MaxFish)
Dim FishTimer#(MaxFish), FishSpeed#(MaxFish)
Dim FishX(MaxFish), FishY(MaxFish)

; Pillars
; -------
Dim PillarMesh(30), PillarTexture(30)
Global NofPillars=0
Const MaxPillars=600
Dim PillarEntity(MaxPillars)
Dim PillarType(MaxPillars)
Global HouseStyle=1
Dim HouseTexture(4)

; BoxFactory
; -------
Global BoxerMesh,BoxerTexture
Global NofBoxers=0
Const MaxBoxers=200
Dim BoxerEntity(MaxBoxers)
Dim BoxerType(MaxBoxers)
Dim BoxerTimer(MaxBoxers)
Dim BoxerX(MaxBoxers)
Dim BoxerY(MaxBoxers)

; Transporters
; -------------

Global TransporterMesh,TransporterTexture
Global NofTransporters=0
Const MaxTransporters=200
Dim TransporterEntity(MaxTransporters)
Dim TransporterX(MaxTransporters),TransporterX2(MaxTransporters)
Dim TransporterY(MaxTransporters),TransporterY2(MaxTransporters)
Dim TransporterMovingX(MaxTransporters),TransporterMovingY(MaxTransporters)
Dim TransporterMoving(MaxTransporters),TransporterMovingTimer(MaxTransporters),TransporterSpeed(MaxTransporters)


; Trampolines
; -------------

Global TrampolineTexture
Dim PlayerJumping(4),PlayerJumpingSpeed(4),PlayerJumpingDistance(4)
Dim PlayerJumpingX(4),PlayerJumpingY(4)
Dim BoxJumping(MaxBoxes),BoxJumpingSpeed(MaxBoxes), BoxJumpingDistance(MaxBoxes)
Dim BoxJumpingX(MaxBoxes),BoxJumpingY(MaxBoxes)
Global endmoveplayertrampoline

; Warpers
;---------------
Global WarperMesh,WarperTexture
Global NofWarpers=0
Const MaxWarpers=200
Dim WarperEntity(MaxWarpers)
Dim WarperType(MaxWarpers)
Dim WarperTimer(MaxWarpers)
Dim WarperX(MaxWarpers)
Dim WarperY(MaxWarpers)

; Shadows
; -------

;Global ;RoundShadowMesh,
Global RoundShadowTexture

; Items	
; --------

Dim ItemMesh(5), ItemShadowMesh(5)		; the meshes (crystal, bonus, timer, ...)
Dim ItemTexture(5), ItemShadowTexture(5)

Global RainbowTexture

Global NofItems=0
Const MaxItems=400

Global NofCrystals=0 ; for main gate control
Global NofTotalCrystals
Global NofBonusCoins
Global NofTotalBonusCoins

Dim ItemEntity(MaxItems), ItemShadowEntity(MaxItems)
Dim ItemType(MaxItems)	; 0-crystal

Dim ItemX(MaxItems),ItemY(MaxItems)		; current tile position
Dim ItemDead(MaxItems)
Dim ItemData(MaxItems) ; currently just used as offset for clocks

; Signs
;----------

Dim SignX(20),SignY(20),SignText$(20)
Global SignActive;	0-none,1-on stinky,2-on loof (only for level-signs, i.e. #11-#30)

; Particles
; ----------

Type Particle
	Field Entity
	Field x#
	Field y#
	Field z#
	Field r#
	Field dx#
	Field dy#
	Field dz#
	Field dr#
	Field ddy#
	Field Timer
	Field Timer2
End Type

Dim ParticleMesh(5)		; various types of particles; 0-sprite, 1-flatsquare	
Global ParticleTexture, ParticleTexture2	; and textures (as animtexture, second is non-alpha)

Global NofParticles=0

; Letter Sprites
; --------------

Type LetterType
	Field Entity
	Field Tag		; 0-rainbow coins, 1-time, 2-level completed msg, 3-fps, 4-score, 5-total coins
					; 10 - levelname sign, 11-15 sign #1-#5
					; 50+ used for menus
	Field Active	; true or false

End Type


; -------------
; SOUND EFFECTS
; -------------

Global ClassicDeathSound=0

Global SFXTeleport
Global SFXDeath, SFXLevelLose, SFXButton, SFXTimerButton, SFXLevelWin, SFXHmmm, SFXTime
Global SFXLastCoin
Global SFXIce,SFXLava
Global SFXBoxPush, SFXBoxSplosh, SFXBoxExplo, SFXBreakAway, SFXBounce, SFXCoilyDeath, SFXBombExplo
Global SFXBoulderPush,SFXBoulderHit
Global SFXSaucerDie
Dim SFXLightning(3)

Dim SFXPBounce(4,4)

Dim SFXPlayerConvey(4),SFXPlayerCoin(4),SFXPlayerBonus(4),SFXPlayerEnd(4),SFXPlayerClock(4)

Dim SFXTalk(20), SFXCount(10), SFXSlice(5)

Dim SFXSingle(16)	; 0-electro,1-cannonfire,2-cannonimpact
Dim SFXSingleVolume#(16)

Global SFXSticky,SFXTrampoline,SFXTransporter

Dim SFXMenuSound(10) ;0-menuselect,1-type



; ------------
; VARIOUS DATA
; ------------


; ------------
;     MENUS
; ------------

Global GameExit=False	; true if exit to windows
Global CurrentMenu
Global CurrentMenuOption

Global MenuSelector; the underlying bar for selected menu option
Global MenuSelectortex

Global CursorKeyPressed, ShiftKeyPressed ; (in game, also used in end sequence)

Global Player2BDeleted
Global OptionsSoundLevel#=1.0
Global OptionsMusicLevel#=1.0
Global OptionsTeleport=0
Global OPtionsSwitchPlayerSound=1

Global MenuStyle=-1
Global MenuBkg=-1	; apparantly have nothing to do with menus, but used in MapStyle etc

Global MenuBkgStyle
Global MenuTimer#=0.0
Dim MenuEntity(80)
Dim MenuTexture(20)


; 0ld stuff - to be removed
Global MenuBackground, MenuBackGround2; Entities for planes in background
Global MenuBackgroundtex, MenuBackGroundTex2; associated textures
Global Logo1, Logo2, Logotex1,Logotex2; the Stinky& Loof Logo
; end of old stuff

                                         

; --------------
;     PLAYERS
; --------------
Dim PlayerName$(10)
Dim PlayerLevels(10)
Dim PlayerScore(10)	
Dim PlayerExtraLevels(10)
Global PlayerNameN	; how many players currently on disk (max 10)
Global CurrentPlayerName$
Global CurrentPlayerScore
Global CurrentPlayerID
Dim CurrentPlayerLevelScore(500) ; 0 means not completed, otherwise score of that level
					; 0-9 world 1, 10-19 world 2, etc. 
Dim CurrentPlayerLevelPerfect(500) ; 0 or 1
Global CurrentPlayerExtraLevels
Dim CurrentPlayerExtraLevelID(30000)
Dim CurrentPlayerExtraLevelID2(30000)
Dim CurrentPlayerExtraLevelScore(30000)
Dim CurrentPlayerExtraLevelPerfect(30000)
Global ExtraLevelMusicTrack=1


Dim FontTex(6)			; the texture anim file for 32*32 font

Global LevelNameImage,LevelNameImageBack
Global LevelNameTexture1,LevelNameTexture2
Global LevelNameEntity1,LevelNameEntity2	


; Pack File Data

Global NofPackFiles
Dim PackFilename$(1000),PackFilePosition(1000),PackFileLength(1000)

Dim pakfiles$(1000)			
Dim paklocate(1000)			
Dim PakSize(1000)			

Dim AddPack$(1000)
Dim AddPackSize(1000)
Dim AddPackOffset(1000)

Global AddPackCount
Global AddPackHeaderMask
Global AddPackKey
Global AddFileTrack

Global PackCount			;	Items in the pack file.
Global key					;	The Encrytion Key
Global Packname$			; 	The Default is DATA.PAK
Global exeoffset			;	For use when Data is packed into an .EXE
Global pakAPND$				; 	Sets up the Name to append to the start of the unpacked filenames

; WORLDS

Global CurrentWorld			; 	What world are you in now
Dim CurrentWorldPack(5)			; For the various 'packs' 0-3
Global WhichWorldPack
Global CurrentLevel, CurrentLevelBackup			; 	Level currently selected

Global CurrentWorldLevelN	; 	How many levels in this world?
Dim CurrentWorldLevelEntity(13)	; the squares for each level in the selector screen
								; up to 10 levels, + back/forward. #13 is always cursor
Global WorldLevelTexture;	a single texture containing the four symbols (no,yes,back,forward)
Global WorldLevelTexture2;	the cursor
Global WorldLevelTexture3;  perfect symbol

Dim WorldLevelName$(500)		; filenames for each level
Dim WorldName$(40,10)				; descriptorname for each world
Dim CurrentWorldLevelName$(12,3); descriptorname for each level in world (3 lines)
								;	(loaded at start of levelselectorscreen)

Restore WorldLevelNames
;For j=0 To 2 ; languages
	For i=0 To 39
		Read WorldName$(i,0)
	Next
;Next

For i=0 To 499
	Read WorldLevelName$(i)
Next
If Version<10
	For i=10 To 14
		WorldLevelName$(i)="demo"+Str$(i-9)+".lev"
	Next 
	For i=15 To 19
		WorldLevelName$(i)=""
	Next
EndIf

Global PlaygroundLevel=400	;first level of The PlayGround


; Custom Levels/Worlds

Dim FileDir$(5000)
Dim FileDir2$(5000)
Dim FileDirID(5000)
Dim FileDirID2(5000)
Dim FileDirScore(5000)
Dim FileDirPerfect(5000)
Global FileDirPos, FileDirN
Global CurrentExtraLevelDirectory$=""
Global CurrentFileDir ; the number

Global CustomSortByName=True

Global HouseDirTrue=0
Global HouseDir$=""
Global ModelDirTrue=0
Global ModelDir$=""
Global customtexdir$,custombackdir$

.WorldLevelNames
; Worlds
Data "BACK IN THE SWING OF THINGS","TEMPLE OF FIRE","THE HAUNTED CASTLE","ROBOT CITY","THE SECRET GARDEN","ATLANTIS","UNDERWONDERWORLD","RETRO GAMING, WONDERLAND-STYLE","AT THE EDGE OF SPACE AND TIME","FULL CIRCLE"
Data "TUTORIAL SET 1","TUTORIAL SET 2","TUT3","ROBOT CITY","THE SECRET GARDEN","ATLANTIS","UNDERWONDERWORLD","RETRO GAMING, WONDERLAND-STYLE","AT THE EDGE OF SPACE AND TIME","FULL CIRCLE"
Data "IT'S A MYSTERY","UNDER THE FOGGY MOUNTAINS","QOOKIE'S COTTAGE","WONDERTOWN","AND THEN THERE WERE THREE","RESCUE IN SPACE","SECRET PASSAGES","THE RAINBOW WORLD","AT THE EDGE OF SPACE And TIME","FULL CIRCLE"
Data "KIDS1","KIDS2","THE HAUNTED CASTLE","ROBOT CITY","THE SECRET GARDEN","ATLANTIS","UNDERWONDERWORLD","RETRO GAMING, WONDERLAND-STYLE","AT THE EDGE OF SPACE AND TIME","FULL CIRCLE"

;Data "UEBUNGSGELAENDE","DER WASSERPALAST","WINTER IM WONDERLAND","DIE SAELE IM GEBIRGE","DACH DER WELT","WUESTENSTAEDTE","IM WELTRAUM","STINKY & LOOF","IM HERZEN DES VULKANS","DER JADE-TEMPEL"
;Data "L'ENTRAINEMENT","LE PALAIS D'EAU","L'HIVER A WONDERLAND","LES GALERIES DE LA CAVERNE","LE SOMMET DU MONDE","LES VILLES DU DESERT","PERDU DANS L'ESPACE","STINKY & LOOF CONFONDUS","AU COEUR DU VOLCAN","LE TEMPLE DE JADE"

; Levels	
; Back in the Swing of Things
Data "intro.lev","reflectors.lev","somereflection.lev","cleartheway.lev","rollingstones.lev","ldrescue.lev","letsgetrolling.lev","relayrace.lev","",""
; Temple of Fire
Data "hotcoals.lev","chomper.lev","onemeoneyou.lev","cckohmy.lev","boulderpanic.lev","firepit.lev","remotecontrol.lev","chompatbit.lev","repetition.lev","arena.lev"
; The Haunted Castle
Data "notinviting.lev","traps.lev","gettingspooky.lev","notsofast.lev","pendulum.lev","chasms.lev","hostile.lev","rooftop.lev","",""
; Robot City
Data "starlightlounge.lev","closeencounters.lev","multifunctionre.lev","catchme.lev","switchintro.lev","switchbait.lev","floatingplat.lev","sharpshooter.lev","loadingdock.lev","zeta.lev"
; The Secret Garden
Data "gardenentrance.lev","mushrooms.lev","hiddensurprises.lev","foresttrees.lev","whatsinblock.lev","","","","",""
; Atlantis
Data "sinking.lev","pacifist.lev","bridge.lev","binarythinking.lev","guide.lev","littledetour.lev","iceage.lev","","",""
; Underwonderworld
Data "darkcaverns.lev","firingsquad.lev","greedy2.lev","ringoffire.lev","spiral2.lev","dontmove.lev","plumbing.lev","hotstuff.lev","ticktock.lev","shielding.lev"
; Retro Gaming, Wonderland-Style
Data "retropacman.lev","spaceinvaders.lev","pong.lev","digdug.lev","qbert.lev","frogger.lev","centipede.lev","missile.lev","",""
; At the Edge of Space and Time
Data "res10.dat","rimrunner.lev","doorways.lev","serpentine.lev","outoftime.lev","housemirrors.lev","ghostbusting.lev","chainreaction.lev","hopskipjump.lev",""
; Full Circle
Data "endbegin.lev","marblemaze.lev","longshot.lev","knightsmove.lev","anotherway.lev","beyondtheriver.lev","firestorm.lev","math.lev","522.lev","hurrah.lev"

; Beginner
Data "tut1.lv5","tut2.lv5","tut3.lv5","tut4.lv5","tut5.lv5","tut6.lv5","tut7.lv5","tut8.lv5","tut9.lv5","tut10.lv5"
Data "tut11.lv5","tut12.lv5","tut13.lv5","tut14.lv5","tut15.lv5","tut16.lv5","tut17.lv5","tut18.lv5","tut19.lv5","tut20.lv5"
Data "intro.lev","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""

; Deluxe
Data "mystery1.lv5","mystery2.lv5","mystery3.lv5","mystery4.lv5","mystery5.lv5","","","","",""
Data "cave1.lv5","cave2.lv5","cave3.lv5","cave4.lv5","cave5.lv5","cave6.lv5","cave7.lv5","cave8.lv5","cave9.lv5","cave10.lv5"
Data "qookie1.lv5","qookie2.lv5","qookie3.lv5","qookie4.lv5","qookie5.lv5","qookie6.lv5","qookie7.lv5","qookie8.lv5","",""
Data "city1.lv5","city2.lv5","city3.lv5","city4.lv5","city5.lv5","","","","",""
Data "three1.lv5","three2.lv5","three3.lv5","three4.lv5","three5.lv5","","","","",""
Data "ufo1.lv5","ufo2.lv5","ufo3.lv5","ufo4.lv5","ufo5.lv5","ufo6.lv5","ufo7.lv5","ufo8.lv5","ufo9.lv5",""
Data "temple1.lv5","temple2.lv5","temple3.lv5","temple4.lv5","temple5.lv5","temple6.lv5","temple7.lv5","temple8.lv5","",""
Data "rainbow.lv5","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""

; Kids
Data "intro.lev","","","","","","","","",""
Data "intro.lev","","","","","","","","",""
Data "intro.lev","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""

; The PlayGround - World #40
Data "play1.lev","play2.lev","play3.lev","play4.lev","play5.lev","bonus1.lev","bonus2.lev","bonus3.lev","bonus4.lev","bonus5.lev"
Data "theend.lev","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""
Data "","","","","","","","","",""

.GermanText

.FrenchText