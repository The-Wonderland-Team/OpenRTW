AppTitle "Return To Wonderland"

Include "globals.bb"


	
HidePointer()
LoadOptions()
Setup()


StartMenu(0)


; the Main Loop

WasWindowactive=True

While GameExit=False

  If GetActiveWindow()

	
	
	If GamePhase=0
		; menu
		If WasWindowActive=False
			If DoesSoundExist=True Then	ChannelVolume Music,OptionsMusicLevel
			WasWindowActive=True
		EndIf
		MenuLoop()
		
	EndIf
	If GamePhase=1
		; main game
		
		
		MainGameLoop()
		
		; main game
		If KeyDown(1)=True
			PlayMySound(SFXMenuSound(0),0)
			Repeat
			Until KeyDown(1)=False

			StartMenu(5)
		EndIf
		
		

	EndIf
  Else
	If WasWindowActive=True
		If DoesSoundExist=True Then	ChannelVolume Music,0
		WasWindowActive=False
	EndIf
	Delay 100
  EndIf
		
Wend



; and get outta here
; delete .tmp files
dir=ReadDir(CurrentDir$())
Repeat
	a$=NextFile$(dir)
	
	If Lower$(Right$(a$,4))=".tmp" Or Lower$(Right$(a$,4))=".3ds" Or Lower$(Right$(a$,2))=".x"
		
		If packcleanup=True Then DeleteFile a$
	EndIf
	
Until a$=""
CloseDir dir

ClearWorld
EndGraphics 
;PakClean()

xsound3=MilliSecs() Mod 100
If xsound3<15
	xsound=MyLoadSound("newsounds\exit3.wav",False)
	PlayMySound(xsound,0)
Else If xsound3<30
	xsound=MyLoadSound("newsounds\exit4.wav",False)
	PlayMySound(xsound,0)
Else If xsound3<60
	xsound=MyLoadSound("newsounds\exit5.wav",False)
	PlayMySound(xsound,0)
Else If xsound3<75
	xsound=MyLoadSound("newsounds\exit6.wav",False)
	PlayMySound(xsound,0)
Else 
	xsound=MyLoadSound("newsounds\exit8.wav",False)
	PlayMySound(xsound,0)
	
EndIf
xsound2=MilliSecs()




If DoesSoundExist=True And OptionsMusicLevel>0
;	If ChannelPlaying(Music)=True
	For i=1 To 100
		ChannelVolume Music,1-Float(i)/100.0
		Delay 10
	Next 
	StopChannel(Music)
;	EndIf
EndIf


;If OldGfxMode<>MyGfxMode Or OldWindowed<>MyWindowed
;	ExecFile("wonderland.exe")
;EndIf

Repeat
Until MilliSecs()-xsound2>2000
FreeSound xsound

If VersionNo1=-999
	ExecFile("http://www.midnightsynergy.com/returntowonderland/hints")
	End
EndIf
If VersionNo1=-998
	ExecFile("http://www.midnightsynergy.com/returntowonderland/levelex")
	End
EndIf
If VersionNo1=-997
	ExecFile("wdleditp.exe")
	End
EndIf
If VersionNo1=-996
	ExecFile("http://www.midnightsynergy.com")
	End
EndIf



End







Function StartLevel(Level)

	


	; Load Models
;	FreeEntity PlayerMesh(1)
;	FreeTexture PlayerTexture(1)
;	FreeTexture PlayerTextureB(1)
;	FreeTexture PlayerTextureC(1)
;	FreeTexture PlayerTextureD(1)
	
;	PlayerMesh(1) = MyLoadMD2("graphics\stinky.md2",False)
;	PlayerTexture(1) = MyLoadTexture ("graphics\stinky2.bmp",1,False)
;	PlayerTextureB(1) = MyLoadTexture ("graphics\stinky2b.bmp",1,False)
;	PlayerTextureC(1) = MyLoadTexture ("graphics\stinky2c.bmp",1,False)
;	PlayerTextureD(1) = MyLoadTexture ("graphics\stinky2d.bmp",1,False)


;	EntityTexture PlayerMesh(1),PlayerTexture(1)
	
;	HideEntity PlayerMesh(1)



	; Free Fonts
	For l.LetterType=Each LetterType
		FreeEntity l\Entity
		Delete l
	Next
	
	For i=1 To 4
		PlayerX(i)=0
		PlayerY(i)=0
		PlayerExists(i)=False 
		PlayerMoving(i)=False
		PlayerDead(i)=False
		PlayerDying(i)=False
		PlayerDyingTimer(i)=0	
		FocusOnPlayer(i)=False
	Next
	BothCompleted=0

	If Level=-999
		; Custom
		LoadLevel("CustomLevels\"+CurrentExtraLevelDirectory$+FileDir$(CurrentFileDir))
	Else
		LoadLevel(WorldLevelName$(Level))
	EndIf

	If view3d=False
		RotateEntity camera,90,0,0
		CameraRange Camera,.98,140
		CameraZoom Camera,2
		view3drotation=0
		view3drotation2=0
		
	Else
		RotateEntity Camera,38,180,0
		CameraRange Camera,.18,140
		CameraZoom Camera,1
		view3drotation=2
		view3drotation2=200
		For i=1 To 4
			playerview3drotation(i)=2
		Next
	EndIf
	LightColor LevelLight,-255,-255,-255
	AmbientLight 0,0,0
	
	
	CurrentLightRed=-255
	CurrentLightGreen=-255
	CurrentLightBlue=-255
	LightChangeSpeed=2
	CurrentAmbientRed=0
	CurrentAmbientGreen=0
	CurrentAmbientBlue=0
	If SignActive=0
		If LevelRed=True
			TargetLightRed=255-155*Lightning
			TargetAmbientRed=255-155*Lightning
		Else 
			TargetLightRed=100
			TargetAmbientRed=100
		EndIf
		If LevelGreen=True
			TargetLightGreen=255-155*Lightning
			TargetAmbientGreen=255-155*Lightning
		Else
			TargetLightGreen=100
			TargetAmbientGreen=100
		EndIf
		If LevelBlue=True
			TargetLightBlue=255
			TargetAmbientBlue=255
		Else
			TargetLightBlue=100
			TargetAmbientBlue=100
		EndIf
		
		
		
	EndIf
	AmbientChangeSpeed=1
	
	
	GamePhase=1

	

	FreeCameraX=0
	FreeCameraZ=0
	
	
	
	;LoadLevel(WorldLevelName$(Level))
		
	; make the level mesh
	ConstructLevel()
	
	;check if any buttons are currently depressed
	CheckButtons()
	BoxAlreadyInWater()
	
	; and other stuff
	FPScounter=0
	FPStime=MilliSecs()
	FPS=0
	
	TweenPeriod=1000/85
	TweenTime=MilliSecs()-TweenPeriod
	
	LevelCompleted=0
	LevelCompletedTimer=0
	LevelTimer=0
	
	CurrentLevelScore=0
	
	MapMilliTime=MilliSecs()
	

	StartSign(MapName$,16,10,0)
	
	FinalGateAnim=0
	For i=1 To 4
		PlayerShrink(i)=0
	Next	
	ShiftKeyPressed=0
	
	

	
	
	
	
	
End Function

Function EndLevel()

	
		
	view3d=False
	CameraRange Camera,.98,140
	CameraZoom Camera,2
	

	; clean up EVERYTHING
	
	; Level
	FreeEntity LevelMesh
	NofLevelSurfaces=0
	
	; Objects
	For i=1 To 4
		If PlayerExists(i)=True
			FreeEntity PlayerEntity(i)
			If PlayerDying(i)=False Then FreeEntity PlayerShadowEntity(i)
		EndIf
	Next
	
	
	For i=0 To NofBoxes-1
		FreeEntity BoxEntity(i)
	Next
	NofBoxes=0
	
	For i=0 To NofBoxers-1
		FreeEntity BoxerEntity(i)
	Next
	NofBoxers=0
	
	For i=0 To NofWarpers-1
		FreeEntity WarperEntity(i)
	Next
	NofWarpers=0


	
	For i=0 To NofBreakAways-1
		FreeEntity BreakAwayEntity(i)
	Next
	NofBreakAways=0
	
	For i=0 To NofSwitchers-1
		FreeEntity SwitcherEntity(i)
	Next
	NofSwitchers=0


	For i=0 To 3
		If ElectroExists(i)=True Then FreeEntity ElectroEntity(i)
	Next
	NofElectros=0
	
	For i=0 To NofSpikes-1
		FreeEntity SpikeEntity(i)
	Next
	NofSpikes=0


	For i=0 To NofCannons-1
		FreeEntity CannonEntity(i)
	Next
	NofCannons=0


	For i=0 To NofFireballs-1
		FreeEntity FireballEntity(i)
	Next
	NofFireballs=0

	For i=0 To NofLaserGates-1
		FreeEntity LaserGateEntity(i)
		FreeEntity LaserGateEntity2(i)
	Next	
	NofLasergates=0
	
	For i=0 To NofButtons-1
		FreeEntity ButtonEntity(i)
	Next
	NofButtons=0

	For i=0 To NofMonsters-1
		FreeEntity MonsterEntity(i)
		If MonsterDying(i)=False Then FreeEntity MonsterShadowEntity(i)
	Next
	NofMonsters=0
	
	For i=0 To NofFish-1
		FreeEntity FishEntity(i)
	Next
	NofFish=0
	
	For i=0 To NofPillars-1
		FreeEntity PillarEntity(i)
	Next
	NofPillars=0


	For i=0 To NofItems-1
		FreeEntity ItemEntity(i)
		If ItemType(i)=0 Or ItemType(i)=1 Or ItemType(i)=3
			FreeEntity ItemShadowEntity(i)
		EndIf
	Next
	NofItems=0
	NofCrystals=0
	NofBonusCoins=0
	
	For i=0 To NofTransporters-1
		FreeEntity TransporterEntity(i)
	Next
	NofTransporters=0

	
		
	For p.particle= Each particle
		FreeEntity p\Entity
		Delete p
	Next
	NofParticles=0
	
	
	; Style Textures
	FreeTexture FloorTexture 
	FreeTexture FloorTexture2
	FreeTexture FloorTexture3
	FreeTexture FloorTexture4 

	FreeTexture FloorTextureSign
	FreeTexture WallTextureTop1
	FreeTexture WallTextureSide1a
	FreeTexture WallTextureSide1b
	FreeTexture WallTextureSide2a
	FreeTexture WallTextureSide2b
	FreeTexture WallTextureSide3a
	FreeTexture WallTextureSide3b

	FreeTexture BreakAwayTexture1
	FreeTexture BreakAwayTexture2
	FreeTexture BreakAwayTexture3
	FreeTexture TeleportTexture(0)
	FreeTexture TeleportTexture(1)
	FreeTexture TeleportTexture(2)
	FreeTexture TeleportTexture(3)
	FreeTexture TeleportTexture(4)
	FreeTexture TeleportTexture(5)
	FreeTexture TeleportTexture(6)
	FreeTexture TeleportTexture(7)



	
	; Background effect
	If MapBackground=0 
		FreeEntity BackGroundPlane
		FreeEntity BackGroundPlane2
		FreeTexture BackGroundTexture
	EndIf
	If MapBackground=1 Or MapBackGround=7 Or MapBackGround=9 Or mapbackground=10 Or mapbackground=11
		FreeEntity BackGroundPlane
		FreeTexture BackGroundTexture
	EndIf
	If MapBackground=2 Or MapBackGround=4
		FreeEntity BackGroundPlane
	EndIf
	If MapBackground=3 Or MapBackground=8
		FreeEntity BackGroundPlane
		FreeTexture BackGroundTexture
	EndIf
	If MapBackground=5 Or MapBackGround=6
		FreeEntity BackGroundPlane
		FreeEntity BackGroundPlane2
	EndIf



	
	
	
	
	; 4, 8, 16, etc.
	
	
	; Fonts
	For l.LetterType=Each LetterType
		FreeEntity l\Entity
		Delete l
	Next
	
	
	

End Function






Function MainGameLoop()
	Repeat
		TweenElapsed=MilliSecs()-TweenTime
	Until TweenElapsed>TweenPeriod
	
	If TweenElapsed>20*TweenPeriod 
		TweenElapsed=20*TweenPeriod
		TweenTime=MilliSecs()-TweenElapsed
	EndIf

	;how many 'frames' have elapsed	
	TweenTicks=TweenElapsed/TweenPeriod
	
	
	
	;fractional remainder
	Tween#=Float(TweenElapsed Mod TweenPeriod)/Float(TweenPeriod)
	
	
	
	

	
	
	
	For k=1 To TweenTicks
		Tweentime=Tweentime+Tweenperiod
		If k=Tweenticks 
			CaptureWorld
			
		EndIf

		
		; do the game in here
		UpdateGame()		
		
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
;Color 255,255,255 : Text 0,DisplayHeight-48,"Total Videomemory : " + TotalVidMem() + " - Available Video Memory : " + AvailVidMem()
;Text 0,DisplayHeight-32,"Active Textures : "+ActiveTextures()
;Text 0,DisplayHeight-16,"Tris Rendered : "+TrisRendered()

;RuntimeStats


	
	


	

	; fps calculation
	fpscounter=fpscounter+1
	If fpscounter=100
		fpscounter=0
		fps=(MilliSecs()-fpstime)
		fpstime=MilliSecs()
		fps=100000/(fps)
	
	EndIf

	
	; Data Display
	
;	DisplayText(Str$(FreeCameraX)+" "+Str$(MonsterY(1))+" "+Str$(MonsterX2(1))+" "+Str$(MonsterY2(1))+" "+Str$(BoxX(0))+" "+Str$(BoxY(0)),0,3,1,9)	
	; Crystals
	If NofCrystals>0 Or LevelTimer Mod 40<20
		;If NofCrystals>=10
		If Language=0
			DisplayText("Coins: "+Str$(NofTotalCrystals-NofCrystals),0,0,1,0)
		Else If Language=1
			DisplayText("Taler: "+Str$(NofTotalCrystals-NofCrystals),0,0,1,0)
		Else If Language=2
			DisplayText("Sous:  "+Str$(NofTotalCrystals-NofCrystals),0,0,1,0)

		EndIf
		;Else 
	;		DisplayText("Coins: 0"+Str$(NofTotalCrystals-NofCrystals),0,0,1,0)
		;EndIf
		If Language=0
			If NofTotalCrystals-NofCrystals>=10
				DisplayText(" of "+Str$(NofTotalCrystals),9,0,1,5)
			Else 
				DisplayText(" of "+Str$(NofTotalCrystals),8,0,1,5)
			EndIf
		Else If Language=1
			If NofTotalCrystals-NofCrystals>=10
				DisplayText(" von "+Str$(NofTotalCrystals),9,0,1,5)
			Else 
				DisplayText(" von "+Str$(NofTotalCrystals),8,0,1,5)
			EndIf
		Else If Language=2
			If NofTotalCrystals-NofCrystals>=10
				DisplayText(" de "+Str$(NofTotalCrystals),9,0,1,5)
			Else 
				DisplayText(" de "+Str$(NofTotalCrystals),8,0,1,5)
			EndIf

		EndIf
	Else
		DisplayText(" ",0,0,1,0)
		DisplayText(" ",0,0,1,5)

	EndIf
	If NofTotalBonusCoins>0 And (NofBonusCoins>0 Or LevelTimer Mod 40<20)
		DisplayText("Bonus: "+Str$(noftotalbonuscoins-nofbonuscoins)+" of "+Str$(noftotalbonuscoins),0,1,1,6)
	Else
		displayText("",0,0,1,6)
	EndIf
	
	
	
	
	
	; Time
	If LevelCompleted=0 And NoTimeLimit=0
		i=MilliSecs()
		MapTimeElapsed=i-MapMilliTime
		
		If MapTimeElapsed>=1000
			MapTime=MapTime-1
			If MapTime=30 Then PlayMySound(SFXTime,0)
			For j=0 To 10
				If MapTime=j Then PlayMySound(SFXCount(j),0)
			Next
			MapMilliTime=i
		EndIf
		If MapTime<0 
			For i=1 To 4
				If PlayerExists(i) Then KillPlayer(i)
			Next
			MapTime=0
		EndIf
	EndIf
	MapTimeMinutes=MapTime / 60
	MapTimeSeconds=MapTime Mod 60
	MapTimeString$=Str$(MapTimeMinutes)+":"
	If MapTimeSeconds<10
		MapTimeString$=MapTimeString$+"0"+Str$(MapTimeSeconds)
	Else
		MapTimeString$=MapTimeString$+Str$(MapTimeSeconds)
	EndIf
	DisplayText(MapTimeString$,50-Len(MapTimeString$),0,1,1)

	; Fps (optional)
	If ShowFPS=True
		DisplayText("FPS: "+Str$(fps),0,2,1,3)
	Else
		DisplayText("",0,2,1,3)
	EndIf
	
	
	
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next
	
	
	
	
	
			
;	Text 500,20, "FPS: "+Str$(StinkyMovingX)
;	Text 500,40, "FPS: "+Str$(SFXElectroVolume)

	
	;If EntityY(Camera,True)>50
	;	EndLevel()
	;	StartMenu(0)
	;EndIf
	
	
	Flip
	
	If KeyHit(66) Then
		SaveBuffer(FrontBuffer(),"screenshot.bmp")
	End If



	
	If LevelCompleted=2 ; totally over, but restart menu
		EndLevel()
		If CurrentLevel<>-999 
			CurrentLevel=CurrentLevel+1
			StartMenu(4)
		Else
			StartMenu(14)
		EndIf
	Else If LevelCompleted=3 	; restart same level
		EndLevel()
		StartLevel(CurrentLevel)
	EndIf


	
End Function




Function UpdateGame()

	For i=0 To 16
		SFXSingleVolume(i)=0 ; used to figure out the loudest of the "single" sounds
	Next
	
		
	
	
	
	If KeyDown(56) And KeyDown(45) 
		; Alt-X + cursor keys - Camera Cheat Control
		If KeyDown(72) 
			;If EntityZ(Camera)<-1-9.5
				TranslateEntity Camera,0,0,.1	;forward
			;	FreeCameraZ=FreeCameraZ+.1
			;EndIf
		EndIf
		If KeyDown(80) 
			;If EntityZ(Camera)>-(MapHeight-1)-9.5
				TranslateEntity Camera,0,0,-.1	;backward
			;	FreeCameraZ=FreeCameraZ-.1
			;EndIf
		EndIf
		If KeyDown(75)
			;If EntityX(Camera)>1
				TranslateEntity Camera,-.1,0,0	;left
			;	FreeCameraX=FreeCameraX-.1
			;EndIf
		EndIf
		If KeyDown(77)
			;If EntityX(Camera)<MapWidth-1
				TranslateEntity Camera,.1,0,0	;right
			;	FreeCameraX=FreeCameraX+.1
			;EndIf
		EndIf
		If KeyDown(73)
			;If EntityX(Camera)>1
				TranslateEntity Camera,0,.1,0	;up
			;	FreeCameraX=FreeCameraX-.1
			;EndIf
		EndIf
		If KeyDown(81)
			;If EntityX(Camera)<MapWidth-1
				TranslateEntity Camera,0,-.1,0	;down
			;	FreeCameraX=FreeCameraX+.1
			;EndIf
		EndIf
		If KeyDown(71)
			TurnEntity Camera,1,0,0
		EndIf
		If KeyDown(79)
			TurnEntity Camera,-1,0,0
		EndIf
	
	
	Else If (KeyDown(42) Or KeyDown(54)) And view3d=False 
		; shift key is pressed - Camera control
		If ShiftKeyPressed=False
			For i=11 To 30
				StopSign(i)
			Next
		EndIf
		
		ShiftKeyPressed=True
		
	
		If KeyDown(200) Or KeyDown(72) 
			If EntityZ(Camera)<-1-9.5
				TranslateEntity Camera,0,0,.1	;forward
				FreeCameraZ=FreeCameraZ+.1
			EndIf
		EndIf
		If KeyDown(208) Or KeyDown(80) 
			If EntityZ(Camera)>-(MapHeight-1)-9.5
				TranslateEntity Camera,0,0,-.1	;backward
				FreeCameraZ=FreeCameraZ-.1
			EndIf
		EndIf
		If KeyDown(75) Or KeyDown(203)
			If EntityX(Camera)>1
				TranslateEntity Camera,-.1,0,0	;left
				FreeCameraX=FreeCameraX-.1
			EndIf
		EndIf
		If KeyDown(77) Or KeyDown(205)
			If EntityX(Camera)<MapWidth-1
				TranslateEntity Camera,.1,0,0	;right
				FreeCameraX=FreeCameraX+.1
			EndIf
		EndIf
		
			;If KeyDown(73) TranslateEntity Camera,0,.1,0	;up
		;If KeyDown(81) TranslateEntity Camera,0,-.1,0	;down
			
	Else
		If ShiftKeyPressed=True
			CheckSigns()
		EndIf
		
		ShiftKeyPressed=False
		If FreeCameraX<>0 Or FreeCameraZ<>0
			TranslateEntity Camera,-FreeCameraX,0,-FreeCameraZ
			FreeCameraX=0
			FreeCameraZ=0
		EndIf
		; no shift - stinky control
		; first check if we accept any input?
		If PlayerDying(1)+PlayerDying(2)+PlayerDying(3)+PlayerDying(4)=0 And PlayerDead(1)+PlayerDead(2)+PlayerDead(3)+PlayerDead(4)=0 And LevelCompleted=0 And FocusSwitchTimer<20
		 
		 If view3d=False
			NormalKeyboardControl()
		 Else
			View3dKeyboardControl()
		 EndIf
		
		 
				
	   EndIf
	 
	
	
	   ; Return - Switch Stinky & Loof
	   If (KeyDown(28) Or KeyDown(156)) And FocusSwitchTimer=0
		  ;n switching While somebody is dead
		  k=0
		  For i=1 To 4
		  	If PlayerExists(i)=True And (PlayerDying(i)=True Or PlayerDead(i)=True)
				k=1
		  	EndIf
		  Next
			
		  If k=0 Then SwitchPlayer(1)
	   EndIf
	EndIf
	If FocusSwitchTimer>0
		FocusSwitchTimer=FocusSwitchTimer-1
	EndIf

	
	; Stinky Walking Animation End?
	; needs to be between Move and Endmove (i.e.here) to prevent animation from stopping if a new tile
	; is reached (and hence endmove is called)
	For i=1 To 4
		If PlayerExists(i)=True And PlayerMoving(i)=False And PlayerAnimCurrent(i)<3
			AnimateMD2 PlayerEntity(i),0,1,0,0
			PlayerAnimNew(i)=0
			PlayerAnimCurrent(i)=0
		EndIf
		
		If (PlayerTexture(i)=PlayerBeforeBlinkTexture(i)) And PlayerExists(i)=True
			PlayerBlinkCounter(i)=PlayerBlinkCounter(i)-1
			If PlayerBlinkCounter(i)>20 And PlayerBlinkCounter(i)<30
				EntityTexture PlayerEntity(i),PlayerTextureD(i)
				PlayerIdleCounter(i)=PlayerIdleCounter(i)-1
			EndIf
			If PlayerBlinkCounter(i)=>10 And PlayerBlinkCounter(i)<20
				EntityTexture PlayerEntity(i),PlayerTexture(i)
				PlayerIdleCounter(i)=PlayerIdleCounter(i)-1
			EndIf
			If PlayerBlinkCounter(i)>0 And PlayerBlinkCounter(i)<10
				EntityTexture PlayerEntity(i),PlayerTextureD(i)
				PlayerIdleCounter(i)=PlayerIdleCounter(i)-1
			EndIf
			If PlayerBlinkCounter(i)<=0
				EntityTexture PlayerEntity(i),PlayerTexture(i)
				PlayerBlinkCounter(i)=500+Rand(0,500)
			EndIf
		EndIf
	

		PlayerIdleCounter(i)=PlayerIdleCounter(i)+1
		
	
		If PlayerIdleCounter(i)=900 And PlayerExists(i)=True
			i2=Rand(0,100)
			j=Rand(0,100)
			If i2<50
				AnimateMD2 PlayerEntity(i),0,1,0,0
				PlayerAnimNew(i)=0
				PlayerAnimCurrent(i)=0
				PlayerIdleCounter(i)=600
				If j<60
					EntityTexture PlayerEntity(i),PlayerTexture(i)
					PlayerBeforeBlinkTexture(i)=PlayerTexture(i)
				Else If j<95
					EntityTexture PlayerEntity(i),PlayerTextureC(i)
					PlayerBeforeBlinkTexture(i)=PlayerTextureC(i)
				Else 
					EntityTexture PlayerEntity(i),PlayerTextureD(i)
					PlayerBeforeBlinkTexture(i)=PlayerTextureD(i)
				EndIf
	
			Else If i2<60
				AnimateMD2 PlayerEntity(i),1,0.1,105,147
				PlayerAnimNew(i)=7
				PlayerAnimCurrent(i)=7
				PlayerIdleCounter(i)=-300
				If j<50
					EntityTexture PlayerEntity(i),PlayerTexture(i)
					PlayerBeforeBlinkTexture(i)=PlayerTexture(i)
				Else If j<95
					EntityTexture PlayerEntity(i),PlayerTextureC(i)
					PlayerBeforeBlinkTexture(i)=PlayerTextureC(i)
				Else 
					EntityTexture PlayerEntity(i),PlayerTextureD(i)
					PlayerBeforeBlinkTexture(i)=PlayerTextureD(i)
				EndIf
	
			Else If i2<65
				AnimateMD2 PlayerEntity(i),1,0.1,62,104
				PlayerAnimCurrent(i)=6
				PlayerAnimNew(i)=6
				PlayerIdleCounter(i)=-300
				If j<50
					EntityTexture PlayerEntity(i),PlayerTexture(i)
					PlayerBeforeBlinkTexture(i)=PlayerTexture(i)
				Else 
					EntityTexture PlayerEntity(i),PlayerTextureC(i)
					PlayerBeforeBlinkTexture(i)=PlayerTextureC(i)
				EndIf
	
			Else 
				AnimateMD2 PlayerEntity(i),1,0.2,53,61
				PlayerAnimCurrent(i)=5
				PlayerAnimCurrent(i)=5
				PlayerIdleCounter(i)=-300
				If j<45
					EntityTexture PlayerEntity(i),PlayerTexture(i)
					PlayerBeforeBlinkTexture(i)=PlayerTexture(i)
				Else If j<95
					EntityTexture PlayerEntity(i),PlayerTextureC(i)
					PlayerBeforeBlinkTexture(i)=PlayerTextureC(i)
				Else 
					EntityTexture PlayerEntity(i),PlayerTextureD(i)
					PlayerBeforeBlinkTexture(i)=PlayerTextureD(i)
				EndIf
	
			EndIf
			
		EndIf

		; Stinky dying animation
		If PlayerDying(i)=True And PlayerDead(i)=False And PlayerExists(i)=True
			TranslateEntity PlayerEntity(i),0,Float(PlayerDyingTimer(i))/2000.0,0
			
			oldx#=EntityX(PlayerEntity(i))
			oldy#=EntityY(PlayerEntity(i))
			oldz#=EntityZ(PlayerEntity(i))
			TurnEntity PlayerEntity(i),4,6,0
					
			TranslateEntity Camera,0,Float(PlayerDyingTimer(i))/2000.0,0
	
			PlayerDyingTimer(i)=PlayerDyingTimer(i)-1
			If PlayerDyingTimer(i)=0
				PlayerDyingTimer(i)=320
				;CameraFogColor camera,0,0,0
				PlayerDead(i)=True
				
				k=0
				For j=1 To 4
					If PlayerExists(j)=True And PlayerDead(j)=True And i<>j
						k=1
					EndIf
				Next
				If k=0
					
					ClassicDeathSound=ClassicDeathSound+1
					If sfxLevelLose>0 FreeSound sfxlevellose
					k2=MilliSecs() Mod 100
					k3=MilliSecs()*Rand(7,37) Mod 100
					
					If ClassicDeathSound<3 Or k3<75
						SFXLevelLose=MyLoadSound("sound\death2.wav",False)
					Else If k2<30
						SFXLevelLose=MyLoadSound("newsounds\death-2b.wav",False)
					Else If k2<60
						SFXLevelLose=MyLoadSound("newsounds\death-2c.wav",False)
					Else If k2<85
						SFXLevelLose=MyLoadSound("newsounds\death-2d.wav",False)
					Else If k2<90
						SFXLevelLose=MyLoadSound("newsounds\death2-spa.wav",False)
					Else If k2<95
						SFXLevelLose=MyLoadSound("newsounds\death2-ger.wav",False)
					Else 
						SFXLevelLose=MyLoadSound("newsounds\death2-fra.wav",False)
					EndIf
					
					
					PlayMySound(SFXLevelLose,0)
					
					
				EndIf
				
				HideEntity PlayerEntity(i)
				; and explode
				For i2=0 To 30*DetailLevel	
					; angle of particle
					angle#=Rnd(0,359)
					speed#=Rnd(.03,.05)
					dx#=speed*Sin(angle) 
					dz#=speed*Cos(angle)
					CreateParticle(0,Rand(5,8),.2,EntityX(PlayerEntity(i)),EntityY(PlayerEntity(i)),EntityZ(PlayerEntity(i)),dx,Rnd(-.001,.001),dz,3,0,200)
				Next
			EndIf
		EndIf
		If PlayerDead(i)=True
			; fade out
			TargetLightRed=-255
			TargetLightGreen=-255
			TargetLightBlue=-255
			LightChangeSpeed=2
			TargetAmbientRed=0
			TargetAmbientGreen=0
			TargetAmbientBlue=0
			AmbientChangeSpeed=1
	
			
			PlayerDyingTimer(i)=PlayerDyingTimer(i)-1
			
			If PlayerDyingTimer(i)<=0
				; and done - restart level
				
				LevelCompleted=3
			EndIf
		EndIf
	
	Next

	
	
	

	
	
	
	
	
	
	If LevelTimer<160
		; fade in
	;	If view3d=False
			TranslateEntity Camera,0,-10.0/160.0,-10.0/160.0
	;	Else
	;		TranslateEntity Camera,0,-10.0/160.0,10.0/160.0
	;	EndIf
		TurnEntity Camera,-35.0/160.0,0,0
		
		If leveltimer>150 And view3d=True
			For i=1 To 4
				If FocusOnPlayer(i)=True And playerexists(i)=True
					HideEntity playerentity(i)
				EndIf
			Next
		EndIf
						
	EndIf

	

			
		
		
	For i=1 To 4
		;standing on conveyor... check if path has been cleared and movement can be made
		If PlayerExists(i)=True And PlayerMoving(i)=False And ((MapTile(PlayerX(i),PlayerY(i))/100=5 And (MapTile(PlayerX(i),PlayerY(i))) Mod 100 <8 And ConveyorActive(1)=True) Or (MapTile(PlayerX(i),PlayerY(i))/100=5 And (MapTile(PlayerX(i),PlayerY(i))) Mod 100 >=8 And ConveyorActive(2)=True))
			
			
			If MapTile(PlayerX(i),PlayerY(i)) Mod 4 = 0 
				If CanPlayerMove(i,0,-1)=True Then MovePlayer(i,0,-1)
			EndIf
			If MapTile(PlayerX(i),PlayerY(i)) Mod 4 = 2 
		 		If CanPlayerMove(i,0,1)=True Then MovePlayer(i,0,1)
					
			EndIf
			If MapTile(PlayerX(i),PlayerY(i)) Mod 4 =1 
				
				If CanPlayerMove(i,1,0)=True Then MovePlayer(i,1,0)
			EndIf
			If MapTile(PlayerX(i),PlayerY(i)) Mod 4 = 3
				If CanPlayerMove(i,-1,0)=True Then 	MovePlayer(i,-1,0)
					
			EndIf
			
		EndIf
	Next
	


	; --------------
	;   BOXES
	; --------------
	For i=0 To NofBoxes-1
		;standing on conveyor... check if path has been cleared and movement can be made
		If BoxMoving(i)=False And ((MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i))) Mod 100<8 And ConveyorActive(1)=True) Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i))) Mod 100 >=8 And ConveyorActive(2)=True))

			If MapTile(BoxX(i),BoxY(i)) Mod 4 = 0
				If CanBoxMove(i,0,-1)=True Then MoveBox(i,0,-1,0)
			EndIf
			If MapTile(BoxX(i),BoxY(i)) Mod 4 = 2
				If CanBoxMove(i,0,1)=True Then MoveBox(i,0,1,0)
			EndIf
			If MapTile(BoxX(i),BoxY(i)) Mod 4 = 1
				If CanBoxMove(i,1,0)=True Then MoveBox(i,1,0,0)
			EndIf
			If MapTile(BoxX(i),BoxY(i)) Mod 4 = 3
				If CanBoxMove(i,-1,0)=True Then MoveBox(i,-1,0,0)
			EndIf
		EndIf
	Next
	
	; -------------------
	; 	MONSTER CONTROL
	; -------------------
	ControlFireballs()
	For i=1 To 4
		ControlPlayer(i)
	Next
	; Box Swaying
	ControlBoxes()
	; ElectroTiles
	ControlElectroTiles()
	; Cannons & Fireballs
	ControlCannons()
	; Spikes
	ControlSpikes()
	; Timer Buttons
	ControlTimerButtons()
	; Monsters
	ControlMonsters()
	; Items
	ControlItems()
	; Fish
	ControlFish()
	; Particles
	ControlParticles()
	ControlFireballs()
	
	ControlBoxers()
	ControlWarpers()
	
	
	
	; OBJECT MOVEMENTS
	; ----------------
	For i=1 To 4
		If PlayerExists(i)=True And PlayerMoving(i)=True And PlayerDead(i)=False And PlayerDying(i)=False
			TranslateEntity PlayerEntity(i),PlayerMovingX(i)/Float(PlayerSpeed(i)),0,-PlayerMovingY(i)/Float(PlayerSpeed(i))
			TranslateEntity PlayerShadowEntity(i),PlayerMovingX(i)/Float(PlayerSpeed(i)),0,-PlayerMovingY(i)/Float(PlayerSpeed(i))
			If FocusOnPlayer(i)=True
				TranslateEntity Camera,PlayerMovingX(i)/Float(PlayerSpeed(i)),0,-PlayerMovingY(i)/Float(PlayerSpeed(i))
			EndIf
			If PlayerJumping(i)=True
				TranslateEntity PlayerEntity(i),0,-.2*Cos(180.0/Float(PlayerJumpingSpeed(i))*Float(PlayerMovingTimer(i))),0
			EndIf
			PlayerMovingTimer(i)=PlayerMovingTimer(i)-1
		EndIf
		
	Next
	
	For i=0 To NofTransporters-1
		If TransporterMoving(i)=True
			TranslateEntity TransporterEntity(i),TransporterMovingX(i)/Float(TransporterSpeed(i)),0,-TransporterMovingY(i)/Float(TransporterSpeed(i))
			TransporterMovingTimer(i)=TransporterMovingTimer(i)-1
			
		EndIf
	Next



	For i=0 To NofBoxes-1
		If BoxMoving(i)=True
			TranslateEntity BoxEntity(i),BoxMovingX(i)/Float(BoxSpeed(i)),0,-BoxMovingY(i)/Float(BoxSpeed(i))
			BoxMovingTimer(i)=BoxMovingTimer(i)-1
			If BoxJumping(i)=True
				TranslateEntity BoxEntity(i),0,-.2*Cos(180.0/Float(BoxJumpingSpeed(i))*Float(BoxMovingTimer(i))),0
			EndIf

			
		EndIf
	Next
	For i=0 To NofMonsters-1
		If MonsterMoving(i)=True
			TranslateEntity MonsterEntity(i),MonsterMovingX(i)/Float(MonsterSpeed(i)),0,-MonsterMovingY(i)/Float(MonsterSpeed(i))
			TranslateEntity MonsterShadowEntity(i),MonsterMovingX(i)/Float(MonsterSpeed(i)),0,-MonsterMovingY(i)/Float(MonsterSpeed(i))
			MonsterMovingTimer(i)=MonsterMovingTimer(i)-1
			
		EndIf
		
	Next
	


	
	; MOVEMENT END?
	; ------------
	
	For i=0 To NofTransporters-1 ;(do transporter first, since player might need to land on it as it travels with player)
		If TransporterMoving(i)=True And TransporterMovingTimer(i)=0
			EndMoveTransporter(i)
		EndIf
	Next
	
	For i=1 To 4
		
		If PlayerMoving(i)=True And PlayerMovingTimer(i)=0 And PlayerExists(i)=True
			EndMovePlayerTrampoline=False
			If PlayerJumping(i)=True
				PlayerJumping(i)=False
				PlayerX2(i)=PlayerJumpingX(i)
				PlayerY2(i)=PlayerJumpingY(i)
				PlayerX(i)=PlayerX2(i)
				PlayerY(i)=PlayerY2(i)
				AnimateMD2 PlayerEntity(i),0,1,0,0
				PlayerAnimNew(i)=3
				TranslateEntity PlayerEntity(i),0,-EntityY(PlayerEntity(i)),0
				EndMovePlayerTrampoline=True
				EndTrampoline(0,i)
			EndIf
			EndMovePlayer(i)
		EndIf
	Next
	
	

	
	For i=0 To NofBoxes-1
		If BoxMoving(i)=True And BoxMovingTimer(i)=0
			EndMovePlayerTrampoline=False
			If BoxJumping(i)=True
				BoxJumping(i)=False
				BoxX2(i)=BoxJumpingX(i)
				BoxY2(i)=BoxJumpingY(i)
				BoxX(i)=BoxX2(i)
				BoxY(i)=BoxY2(i)
				If BoxType(i)<100
					TranslateEntity BoxEntity(i),0,-EntityY(BoxEntity(i))+0.4,0
				Else
					TranslateEntity BoxEntity(i),0,-EntityY(BoxEntity(i)),0
				EndIf
				EndMovePlayerTrampoline=True
				EndTrampoline(1,i)
			EndIf

			EndMoveBox(i)
		EndIf
	Next
	For i=0 To NofMonsters-1
		If MonsterMoving(i)=True And MonsterMovingTimer(i)=0
			EndMoveMonster(i)
		EndIf
	Next



	
	
	
	; OBJECT CLEANUP
	; --------------
	CleanUpBoxes()
	CleanUpFireballs()
	CleanUpMonsters()
	CleanUpItems()
	
	
		
	


		
		
	; ---------------
	; SPECIAL EFFECTS
	; ---------------
	
	; Camera Scroll?
	; e.g. after switch between stinky and loof
	If CameraDX>=CameraDStep
		TranslateEntity Camera,CameraDStep,0,0
		CameraDX=CameraDX-CameraDStep
	Else If CameraDX<=-CameraDStep
		TranslateEntity Camera,-CameraDStep,0,0
		CameraDX=CameraDX+CameraDStep
	Else
		TranslateEntity Camera,CameraDX,0,0
		CameraDX=0
	EndIf
	If CameraDZ>=CameraDStep
		TranslateEntity Camera,0,0,CameraDStep
		CameraDZ=CameraDZ-CameraDStep
	Else If CameraDZ<=-CameraDStep
		TranslateEntity Camera,0,0,-CameraDStep
		CameraDZ=CameraDZ+CameraDStep
	Else
		TranslateEntity Camera,0,0,CameraDZ
		CameraDZ=0
	EndIf

	


		
	
	; scroll water texture
	Watertexu#=Watertexu#+0.001
	Watertexv#=Watertexv#+0.0005
	;Watertexr#=Watertexr#+1
	If Watertexu#=1.0 Then Watertexu#=0.0
	If Watertexv#=1.0 Then Watertexv#=0.0	
	;If Watertexr#=360.0 Then Watertexr#=0.0
	PositionTexture WaterTexture,Watertexu#,Watertexv#
	If Version>=10 	PositionTexture LavaTexture,Watertexu#,Watertexv#
	PositionTexture RainbowTexture,Watertexu,WaterTexv
	PositionTexture GhostBoxTexture,Watertexu,WaterTexv
	RotateTexture GhostBoxTexture,watertexv*360.0
	If MapBackGround=0 ; scroll sky
		PositionEntity BackGroundPlane,WaterTexu*40,-50,WaterTexv*40
		PositionEntity BackGroundPlane2,WaterTexu*20,-60,WaterTexv*20

	EndIf
	If MapBackGround=9 Or mapbackground=10 ; scroll sky
		PositionEntity BackGroundPlane,WaterTexu*40,-50,WaterTexv*40
	;	PositionEntity BackGroundPlane2,WaterTexu*20,-60,WaterTexv*20

	EndIf

	If MapBackGround=7 ; warp sphere
		PositionTexture BackgroundTexture,Watertexu,WaterTexv
	EndIf
;	If MapBackground=9 ; rainbow world
;		PositionTexture FloorTexture,Watertexu,WaterTexv
;		RotateTexture FloorTexture,WaterTexu*360
;		PositionTexture WallTextureTop1,Watertexu,WaterTexv
;		PositionTexture WallTextureSide1a,Watertexu,WaterTexv
;		PositionTexture WallTextureSide1b,Watertexu,WaterTexv
;		PositionTexture WallTextureSide2a,Watertexu,WaterTexv
;		PositionTexture WallTextureSide2b,Watertexu,WaterTexv
;		PositionTexture WallTextureSide3a,Watertexu,WaterTexv
;		PositionTexture WallTextureSide3b,Watertexu,WaterTexv
;		RotateTexture WallTextureTop1,WaterTexu*1440
;		RotateTexture WallTextureSide1a,-WaterTexu*360
;		RotateTexture WallTextureSide1b,WaterTexu*720
;
;	EndIf

	For i=0 To NofLaserGates-1
		If LaserGateActive(LaserGateCol(i))=True And LaserGateCol(i)>0
			TurnEntity LaserGateEntity(i),1,0,0
			TurnEntity LaserGateEntity2(i),1,0,0

		EndIf
	Next
	
	
	For i=1 To 2
		; scroll conveyor textures
		If ConveyorActive(i)=True
			conveyortex(i)=conveyortex(i)+(1.0/BaseSpeed)*2
			If conveyortex(i)>=1.0 Then conveyortex(i)=conveyortex(i)-1
		EndIf
		PositionTexture ConveyorTexture1(i),0,1.0-conveyortex(i)
		PositionTexture ConveyorTexture2(i),0,1.0-conveyortex(i)
		PositionTexture ConveyorTexture3(i),0,1.0-conveyortex(i)
		PositionTexture ConveyorTexture4(i),0,1.0-conveyortex(i)
		PositionTexture SwitcherTexture1(i),0,1.0-conveyortex(i)
		PositionTexture SwitcherTexture2(i),0,1.0-conveyortex(i)
		PositionTexture SwitcherTexture3(i),0,1.0-conveyortex(i)
		PositionTexture SwitcherTexture4(i),0,1.0-conveyortex(i)
	Next

	
	; Level Completed Zoom
	If LevelCompleted=1
		LevelCompletedTimer=LevelCompletedTimer+1
	
		TranslateEntity Camera,0,10.0/512.0,10.0/512.0
		TurnEntity Camera,45.0/512.0,0,0
		If LevelCompletedTimer=300
			LevelCompleted=2
		EndIf
		
		TargetLightRed=0
		TargetLightGreen=0
		TargetLightBlue=0
		LightChangeSpeed=2
		TargetAmbientRed=0
		TargetAmbientGreen=0
		TargetAmbientBlue=0
		AmbientChangeSpeed=1


		
	EndIf
	
	; Final Gate Anim
	If FinalGateAnim>0
		FinalGateAnim=FinalGateAnim-1
		For k=0 To NofLaserGates-1
			If LaserGateCol(k)=0 
				TranslateEntity LaserGateEntity(k),0,-0.016,0
				If FinalGateAnim=0
					HideEntity LaserGateEntity(k)
				EndIf
			EndIf
		Next
	EndIf
	
	; Stinky/Loof Shrink in Rainbowgate
	For i=1 To 4
		If PlayerShrink(i)>0
			shrinkfactor#=Float(PlayerShrink(i))/60.0
			PlayerShrink(i)=PlayerShrink(i)-1
			ScaleEntity PlayerEntity(i),shrinkfactor,shrinkfactor,shrinkfactor
			If PlayerShrink(i)=0 Then HideEntity PlayerEntity(i)
		EndIf
	Next
		
		
		
	; single sounds (electro, cannon)
	For i=0 To 16
		If SFXSingleVolume(i)>0
			SoundVolume SFXSingle(i),OptionsSoundLevel*SFXSingleVolume(i)
			PlaySound SFXSingle(i)
		EndIf
	Next
	
	; Signs
	ControlSigns()
	
	; Weather		
	If Snow=True
		If LevelTimer Mod (1+(5-DetailLevel)*(5-DetailLevel))=0
			CreateParticle(0,10,.05,Rnd(-2,MapWidth+2),Rnd(5,10),-Rnd(-2,MapHeight+2),.01,-Rnd(.02,.03),-.01,Rnd(-.5,.25),0,10000)
			
		EndIf
	EndIf
	
	If Rain=True
		If LevelTimer Mod (1+(5-DetailLevel)*(5-DetailLevel))=0
			CreateParticle(0,11,.2,EntityX(Camera)+Rnd(-10,10),Rnd(5,10),EntityZ(Camera)+Rnd(1,10),0,-.15,0,0,0,10000)
			
		EndIf
	EndIf
	
	If Lightning=True And LightningTimer=0 And PlayerDying(1)+PlayerDying(2)+PlayerDying(3)+PlayerDying(4)=0 And SignActive=0 And Rand(0,150)=1
		LightningTimer=1
		If Rand(0,3)=1 Then PlayMySound(SFXLightning(Rand(0,3)),0)
	EndIf
	If LightningTimer>0 And PlayerDying(1)+PlayerDying(2)+PlayerDying(3)+PlayerDying(4)=0 And SignActive=0
		If LightningTimer=1 Or LightningTimer=30 Or LightningTimer=60
			; start it
			If LevelRed=True
				TargetLightRed=255
				TargetAmbientRed=255
			Else
				TargetLightRed=100
				TargetAmbientRed=100
			EndIf
			If LevelGreen=True
				TargetLightGreen=255
				TargetAmbientGreen=255
			Else
				TargetLightGreen=100
				TargetAmbientGreen=100
			EndIf
			If LevelBlue=True
				TargetLightBlue=255
				TargetAmbientBlue=255
			Else
				TargetLightBlue=100
				TargetAmbientBlue=100
			EndIf
			LightChangeSpeed=Rand(20,40)
			AmbientChangeSpeed=Rand(20,30)
		EndIf
		If LightningTimer=10 Or LightningTimer=35 Or LightningTimer=65
			; go down
			If LevelRed=True
				TargetLightRed=100
				TargetAmbientRed=100
			Else
				TargetLightRed=100
				TargetAmbientRed=100
			EndIf
			If LevelGreen=True
				TargetLightGreen=100
				TargetAmbientGreen=100
			Else
				TargetLightGreen=100
				TargetAmbientGreen=100
			EndIf
			If LevelBlue=True
				TargetLightBlue=255
				TargetAmbientBlue=255
			Else
				TargetLightBlue=100
				TargetAmbientBlue=100
			EndIf

					
			LightChangeSpeed=Rand(5,10)
		
			AmbientChangeSpeed=Rand(2,5)
		EndIf
		LightningTimer=LightningTimer+1
		If LightningTimer=100
			LightningTimer=0
		EndIf
			
				
	EndIf
	
	If Psyche=True And PlayerDying(1)+PlayerDying(2)+PlayerDying(3)+PlayerDying(4)=0 And SignActive=0 And Rand(0,100)=1
		If LevelRed=True
			TargetLightRed=Rand(0,255)

			TargetAmbientRed=Rand(100,255)
		Else
			TargetLightRed=100
			TargetAmbientRed=100
		EndIf
		If LevelGreen=True
			TargetLightGreen=Rand(0,255)

			TargetAmbientGreen=Rand(100,255)
		Else
			TargetLightGreen=100
			TargetAmbientGreen=100
		EndIf
		If LevelBlue=True
			TargetLightBlue=Rand(0,255)

			TargetAmbientBlue=Rand(100,255)
		Else
			TargetLightBlue=100
			TargetAmbientBlue=100
		EndIf
		LightChangeSpeed=1
		
		AmbientChangeSpeed=1
	EndIf
		


	


	; Lights
	If CurrentLightRed<TargetLightRed-LightChangeSpeed
		CurrentLightRed=CurrentLightRed+LightChangeSpeed
	Else If CurrentLightRed<TargetLightRed
		CurrentLightRed=TargetLightRed
	EndIf
	If CurrentLightRed>TargetLightRed+LightChangeSpeed
		CurrentLightRed=CurrentLightRed-LightChangeSpeed
	Else If CurrentLightRed>TargetLightRed
		CurrentLightRed=TargetLightRed
	EndIf
	If CurrentLightGreen<TargetLightGreen-LightChangeSpeed
		CurrentLightGreen=CurrentLightGreen+LightChangeSpeed
	Else If CurrentLightGreen<TargetLightGreen
		CurrentLightGreen=TargetLightGreen
	EndIf
	If CurrentLightGreen>TargetLightGreen+LightChangeSpeed
		CurrentLightGreen=CurrentLightGreen-LightChangeSpeed
	Else If CurrentLightGreen>TargetLightGreen
		CurrentLightGreen=TargetLightGreen
	EndIf
	If CurrentLightBlue<TargetLightBlue-LightChangeSpeed
		CurrentLightBlue=CurrentLightBlue+LightChangeSpeed
	Else If CurrentLightBlue<TargetLightBlue
		CurrentLightBlue=TargetLightBlue
	EndIf
	If CurrentLightBlue>TargetLightBlue+LightChangeSpeed
		CurrentLightBlue=CurrentLightBlue-LightChangeSpeed
	Else If CurrentLightBlue>TargetLightBlue
		CurrentLightBlue=TargetLightBlue
	EndIf
		If CurrentAmbientRed<TargetAmbientRed-AmbientChangeSpeed
		CurrentAmbientRed=CurrentAmbientRed+AmbientChangeSpeed
	Else If CurrentAmbientRed<TargetAmbientRed
		CurrentAmbientRed=TargetAmbientRed
	EndIf
	If CurrentAmbientRed>TargetAmbientRed+AmbientChangeSpeed
		CurrentAmbientRed=CurrentAmbientRed-AmbientChangeSpeed
	Else If CurrentAmbientRed>TargetAmbientRed
		CurrentAmbientRed=TargetAmbientRed
	EndIf
	If CurrentAmbientGreen<TargetAmbientGreen-AmbientChangeSpeed
		CurrentAmbientGreen=CurrentAmbientGreen+AmbientChangeSpeed
	Else If CurrentAmbientGreen<TargetAmbientGreen
		CurrentAmbientGreen=TargetAmbientGreen
	EndIf
	If CurrentAmbientGreen>TargetAmbientGreen+AmbientChangeSpeed
		CurrentAmbientGreen=CurrentAmbientGreen-AmbientChangeSpeed
	Else If CurrentAmbientGreen>TargetAmbientGreen
		CurrentAmbientGreen=TargetAmbientGreen
	EndIf
	If CurrentAmbientBlue<TargetAmbientBlue-AmbientChangeSpeed
		CurrentAmbientBlue=CurrentAmbientBlue+AmbientChangeSpeed
	Else If CurrentAmbientBlue<TargetAmbientBlue
		CurrentAmbientBlue=TargetAmbientBlue
	EndIf
	If CurrentAmbientBlue>TargetAmbientBlue+AmbientChangeSpeed
		CurrentAmbientBlue=CurrentAmbientBlue-AmbientChangeSpeed
	Else If CurrentAmbientBlue>TargetAmbientBlue
		CurrentAmbientBlue=TargetAmbientBlue
	EndIf
	
	LightColor LevelLight,CurrentLightRed,CurrentLightGreen,CurrentLightBlue
	AmbientLight CurrentAmbientRed,CurrentAmbientGreen,CurrentAmbientBlue
	
	
		
	LevelTimer=LevelTimer+1
	
	
	
	If Version<>11
		;Cheats (CTRL + F-Keys)
		If (KeyDown(29) Or KeyDown(157)) 
			If KeyDown(63) 	ShowFPS=True ;F5
			If KeyDown(64) 	ShowFPS=False ;F6
			If KeyDown(67)	;F9
				LevelCompleted=1
				LevelCompletedTimer=0
				If Language=0
					ex$="WELL DONE!!! LEVEL COMPLETED!!!"
					ex$=ex$+"#... NOT! :) # "
				Else If language=2
					ex$="BRAVO!!!#NIVEAU COMPLETE!!!#...PAS VRAIMENT! :)# #TRICHE...TRICHE...TRICHE"
				Else 
					ex$="CHEAT...CHEAT...CHEAT"
				EndIf
				CurrentLevelScore=1
				If Language<>2
					ex$=ex$+"#TOTAL SCORE: "+Str$(CurrentLevelScore)
							
				EndIf
				If CurrentLevel<>-999
					If CurrentLevelScore>CurrentPlayerLevelScore(CurrentLevel) 
						CurrentPlayerLevelScore(CurrentLevel)=CurrentLevelScore
					EndIf
				EndIf
				
				StartSign(ex$,7,2,1)
				
				SavePlayer()
			EndIf
		EndIf
	
	
	
	EndIf
	
	

End Function

Function NormalKeyboardControl()

	;select direction of walking 
		
	
		 ; up
		 If KeyDown(200) Or KeyDown(72)
			For i=1 To 4
				If FocusOnPlayer(i)=True
					If PlayerMoving(i)=False And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)>=8 Or ConveyorActive(1)=False) And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)<8 Or ConveyorActive(2)=False)
						If CanPlayerMove(i,0,-1)=True	
							MovePlayer(i,0,-1)
						Else If MapTile(PlayerX(i),PlayerY(i))/100=17 And MapTile(PlayerX(i),PlayerY(i))<1703
							; on transporter
							If CanTransporterMove(PlayerX(i),PlayerY(i),0,-1)
								MovePlayer(i,0,-1)
								MoveTransporter(PlayerX(i),PlayerY(i),0,-1)
								PlayerAnimNew(i)=0
							Else If  PlayerY(i)>0
								If MapTile(PlayerX(i)+0,PlayerY(i)-1)/100=17 And MapTile(PlayerX(i)+0,PlayerY(i)-1)<1703
									For m=0 To nofboxes-1
										If boxmoving(m)=False And playerx(i)+0=boxx(m) And PlayerY(i)-1=boxy(m)
											; box on transporter found
											
											If cantransportermove(PlayerX(i)+0,PlayerY(i)-1,0,-1)
												MoveBox(m,0,-1,0)
												MoveTransporter(PlayerX(i)+0,PlayerY(i)-1,0,-1)
											EndIf
										EndIf
									Next
								EndIf
					
								
							EndIf
						Else If PLayerY(i)>0
							
							;special case: check if a box on transporter is next
							If MapTile(PlayerX(i)+0,PlayerY(i)-1)/100=17 And MapTile(PlayerX(i)+0,PlayerY(i)-1)<1703
								
								For m=0 To nofboxes-1
									If boxmoving(m)=False And playerx(i)+0=boxx(m) And PlayerY(i)-1=boxy(m)
										; box on transporter found
										
										If cantransportermove(PlayerX(i)+0,PlayerY(i)-1,0,-1)
											MoveBox(m,0,-1,0)
											MoveTransporter(PlayerX(i)+0,PlayerY(i)-1,0,-1)
										EndIf
									EndIf
								Next
							EndIf
					
									
						EndIf
					EndIf
				EndIf
			Next
		 EndIf
		 ; down
		 If KeyDown(208) Or KeyDown(80)
			For i=1 To 4
				If FocusOnPlayer(i)=True
					If PlayerMoving(i)=False And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)>=8 Or ConveyorActive(1)=False) And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)<8 Or ConveyorActive(2)=False)
						If CanPlayerMove(i,0,1)=True	
							MovePlayer(i,0,1)
						Else If MapTile(PlayerX(i),PlayerY(i))/100=17 And MapTile(PlayerX(i),PlayerY(i))<1703
							; on transporter
							If CanTransporterMove(PlayerX(i),PlayerY(i),0,1)
								MovePlayer(i,0,1)
								MoveTransporter(PlayerX(i),PlayerY(i),0,1)
								PlayerAnimNew(i)=0
							Else If PlayerY(i)<MapHeight-1
								If MapTile(PlayerX(i)+0,PlayerY(i)+1)/100=17 And MapTile(PlayerX(i)+0,PlayerY(i)+1)<1703
									For m=0 To nofboxes-1
										If boxmoving(m)=False And playerx(i)+0=boxx(m) And PlayerY(i)+1=boxy(m)
											; box on transporter found
											
											If cantransportermove(PlayerX(i)+0,PlayerY(i)+1,0,+1)
												MoveBox(m,0,+1,0)
												MoveTransporter(PlayerX(i)+0,PlayerY(i)+1,0,+1)
											EndIf
										EndIf
									Next
								EndIf
					
								
							EndIf
						Else If PlayerY(i)<MapHeight-1
							
							;special case: check if a box on transporter is next
							If MapTile(PlayerX(i)+0,PlayerY(i)+1)/100=17 And MapTile(PlayerX(i)+0,PlayerY(i)+1)<1703
								
								For m=0 To nofboxes-1
									If boxmoving(m)=False And playerx(i)+0=boxx(m) And PlayerY(i)+1=boxy(m)
										; box on transporter found
										
										If cantransportermove(PlayerX(i)+0,PlayerY(i)+1,0,+1)
											MoveBox(m,0,+1,0)
											MoveTransporter(PlayerX(i)+0,PlayerY(i)+1,0,+1)
										EndIf
									EndIf
								Next
							EndIf
					
									
						EndIf


					EndIf
				EndIf
			Next
		 EndIf
		 ; left
		 If  KeyDown(203) Or KeyDown(75)
			For i=1 To 4
				If FocusOnPlayer(i)=True
					If PlayerMoving(i)=False And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)>=8 Or ConveyorActive(1)=False) And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)<8 Or ConveyorActive(2)=False)
						If CanPlayerMove(i,-1,0)=True	
							MovePlayer(i,-1,0)
						Else If MapTile(PlayerX(i),PlayerY(i))/100=17 And MapTile(PlayerX(i),PlayerY(i))<1703
							; on transporter
							If CanTransporterMove(PlayerX(i),PlayerY(i),-1,0)
								MovePlayer(i,-1,0)
								MoveTransporter(PlayerX(i),PlayerY(i),-1,0)
								PlayerAnimNew(i)=0
							Else If PlayerX(i)>0
								If MapTile(PlayerX(i)-1,PlayerY(i)-0)/100=17 And MapTile(PlayerX(i)-1,PlayerY(i)-0)<1703
									For m=0 To nofboxes-1
										If boxmoving(m)=False And playerx(i)-1=boxx(m) And PlayerY(i)-0=boxy(m)
											; box on transporter found
											
											If cantransportermove(PlayerX(i)-1,PlayerY(i)-0,-1,-0)
												MoveBox(m,-1,-0,0)
												MoveTransporter(PlayerX(i)-1,PlayerY(i)-0,-1,-0)
											EndIf
										EndIf
									Next
								EndIf
					
								
							EndIf
						Else If PlayerX(i)>0
							
							;special case: check if a box on transporter is next
							If MapTile(PlayerX(i)-1,PlayerY(i)-0)/100=17 And MapTile(PlayerX(i)-1,PlayerY(i)-0)<1703
								
								For m=0 To nofboxes-1
									If boxmoving(m)=False And playerx(i)-1=boxx(m) And PlayerY(i)-0=boxy(m)
										; box on transporter found
										
										If cantransportermove(PlayerX(i)-1,PlayerY(i)-0,-1,-0)
											MoveBox(m,-1,-0,0)
											MoveTransporter(PlayerX(i)-1,PlayerY(i)-0,-1,-0)
										EndIf
									EndIf
								Next
							EndIf
					
									
						EndIf


					EndIf
				EndIf
			Next
		 EndIf
		 ; right
		 If KeyDown(205) Or KeyDown(77)
			For i=1 To 4
				If FocusOnPlayer(i)=True
				  
					If PlayerMoving(i)=False And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)>=8 Or ConveyorActive(1)=False) And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)<8 Or ConveyorActive(2)=False)
						If CanPlayerMove(i,1,0)=True	
							MovePlayer(i,1,0)
						Else If MapTile(PlayerX(i),PlayerY(i))/100=17 And MapTile(PlayerX(i),PlayerY(i))<1703
							; on transporter
							If CanTransporterMove(PlayerX(i),PlayerY(i),1,0)
								MovePlayer(i,1,0)
								MoveTransporter(PlayerX(i),PlayerY(i),1,0)
								PlayerAnimNew(i)=0
							Else If PlayerX(i)<MapWidth-1
								If MapTile(PlayerX(i)+1,PlayerY(i)-0)/100=17 And MapTile(PlayerX(i)+1,PlayerY(i)-0)<1703
									For m=0 To nofboxes-1
										If boxmoving(m)=False And playerx(i)+1=boxx(m) And PlayerY(i)+0=boxy(m)
											; box on transporter found
											
											If cantransportermove(PlayerX(i)+1,PlayerY(i)-0,+1,-0)
												MoveBox(m,+1,-0,0)
												MoveTransporter(PlayerX(i)+1,PlayerY(i)-0,+1,-0)
											EndIf
										EndIf
									Next
								EndIf
					
								
							EndIf
						Else If PlayerX(i)<MapWidth-1
							
							;special case: check if a box on transporter is next
							If MapTile(PlayerX(i)+1,PlayerY(i)-0)/100=17 And MapTile(PlayerX(i)+1,PlayerY(i)-0)<1703
								
								For m=0 To nofboxes-1
									If boxmoving(m)=False And playerx(i)+1=boxx(m) And PlayerY(i)-0=boxy(m)
										; box on transporter found
										
										If cantransportermove(PlayerX(i)+1,PlayerY(i)-0,+1,-0)
											MoveBox(m,+1,-0,0)
											MoveTransporter(PlayerX(i)+1,PlayerY(i)-0,+1,-0)
										EndIf
									EndIf
								Next
							EndIf
					
									
						EndIf


					EndIf
				 
				
			  EndIf
			Next
		 EndIf

End Function

Function view3dkeyboardcontrol()

 	walkdir=-1
	walkdiradd=view3drotation
		 
	If KeyDown(200) Or KeyDown(72) Then walkdir=(0+walkdiradd) Mod 4
	If KeyDown(208) Or KeyDown(80) Then walkdir=(2+walkdiradd) Mod 4
		
;	If KeyDown(205) Or KeyDown(77) Then walkdir=(1+walkdiradd) Mod 4
;	If KeyDown(203) Or KeyDown(75) Then walkdir=(3+walkdiradd) Mod 4
	
	
	
	 ; up
		 If walkdir=0
			For i=1 To 4
				If FocusOnPlayer(i)=True
					If PlayerMoving(i)=False And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)>=8 Or ConveyorActive(1)=False) And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)<8 Or ConveyorActive(2)=False)
						If CanPlayerMove(i,0,-1)=True	
							MovePlayer(i,0,-1)
						Else If MapTile(PlayerX(i),PlayerY(i))/100=17 And MapTile(PlayerX(i),PlayerY(i))<1703
							; on transporter
							If CanTransporterMove(PlayerX(i),PlayerY(i),0,-1)
								MovePlayer(i,0,-1)
								MoveTransporter(PlayerX(i),PlayerY(i),0,-1)
								PlayerAnimNew(i)=0
							Else If  PlayerY(i)>0
								If MapTile(PlayerX(i)+0,PlayerY(i)-1)/100=17 And MapTile(PlayerX(i)+0,PlayerY(i)-1)<1703
									For m=0 To nofboxes-1
										If boxmoving(m)=False And playerx(i)+0=boxx(m) And PlayerY(i)-1=boxy(m)
											; box on transporter found
											
											If cantransportermove(PlayerX(i)+0,PlayerY(i)-1,0,-1)
												MoveBox(m,0,-1,0)
												MoveTransporter(PlayerX(i)+0,PlayerY(i)-1,0,-1)
											EndIf
										EndIf
									Next
								EndIf
					
								
							EndIf
						Else If PLayerY(i)>0
							
							;special case: check if a box on transporter is next
							If MapTile(PlayerX(i)+0,PlayerY(i)-1)/100=17 And MapTile(PlayerX(i)+0,PlayerY(i)-1)<1703
								
								For m=0 To nofboxes-1
									If boxmoving(m)=False And playerx(i)+0=boxx(m) And PlayerY(i)-1=boxy(m)
										; box on transporter found
										
										If cantransportermove(PlayerX(i)+0,PlayerY(i)-1,0,-1)
											MoveBox(m,0,-1,0)
											MoveTransporter(PlayerX(i)+0,PlayerY(i)-1,0,-1)
										EndIf
									EndIf
								Next
							EndIf
					
									
						EndIf
					EndIf
				EndIf
			Next
		 EndIf
		 ; down
		 If walkdir=2
			For i=1 To 4
				If FocusOnPlayer(i)=True
					If PlayerMoving(i)=False And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)>=8 Or ConveyorActive(1)=False) And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)<8 Or ConveyorActive(2)=False)
						If CanPlayerMove(i,0,1)=True	
							MovePlayer(i,0,1)
						Else If MapTile(PlayerX(i),PlayerY(i))/100=17 And MapTile(PlayerX(i),PlayerY(i))<1703
							; on transporter
							If CanTransporterMove(PlayerX(i),PlayerY(i),0,1)
								MovePlayer(i,0,1)
								MoveTransporter(PlayerX(i),PlayerY(i),0,1)
								PlayerAnimNew(i)=0
							Else If PlayerY(i)<MapHeight-1
								If MapTile(PlayerX(i)+0,PlayerY(i)+1)/100=17 And MapTile(PlayerX(i)+0,PlayerY(i)+1)<1703
									For m=0 To nofboxes-1
										If boxmoving(m)=False And playerx(i)+0=boxx(m) And PlayerY(i)+1=boxy(m)
											; box on transporter found
											
											If cantransportermove(PlayerX(i)+0,PlayerY(i)+1,0,+1)
												MoveBox(m,0,+1,0)
												MoveTransporter(PlayerX(i)+0,PlayerY(i)+1,0,+1)
											EndIf
										EndIf
									Next
								EndIf
					
								
							EndIf
						Else If PlayerY(i)<MapHeight-1
							
							;special case: check if a box on transporter is next
							If MapTile(PlayerX(i)+0,PlayerY(i)+1)/100=17 And MapTile(PlayerX(i)+0,PlayerY(i)+1)<1703
								
								For m=0 To nofboxes-1
									If boxmoving(m)=False And playerx(i)+0=boxx(m) And PlayerY(i)+1=boxy(m)
										; box on transporter found
										
										If cantransportermove(PlayerX(i)+0,PlayerY(i)+1,0,+1)
											MoveBox(m,0,+1,0)
											MoveTransporter(PlayerX(i)+0,PlayerY(i)+1,0,+1)
										EndIf
									EndIf
								Next
							EndIf
					
									
						EndIf


					EndIf
				EndIf
			Next
		 EndIf
		 ; left
		 If  walkdir=3
			For i=1 To 4
				If FocusOnPlayer(i)=True
					If PlayerMoving(i)=False And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)>=8 Or ConveyorActive(1)=False) And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)<8 Or ConveyorActive(2)=False)
						If CanPlayerMove(i,-1,0)=True	
							MovePlayer(i,-1,0)
						Else If MapTile(PlayerX(i),PlayerY(i))/100=17 And MapTile(PlayerX(i),PlayerY(i))<1703
							; on transporter
							If CanTransporterMove(PlayerX(i),PlayerY(i),-1,0)
								MovePlayer(i,-1,0)
								MoveTransporter(PlayerX(i),PlayerY(i),-1,0)
								PlayerAnimNew(i)=0
							Else If PlayerX(i)>0
								If MapTile(PlayerX(i)-1,PlayerY(i)-0)/100=17 And MapTile(PlayerX(i)-1,PlayerY(i)-0)<1703
									For m=0 To nofboxes-1
										If boxmoving(m)=False And playerx(i)-1=boxx(m) And PlayerY(i)-0=boxy(m)
											; box on transporter found
											
											If cantransportermove(PlayerX(i)-1,PlayerY(i)-0,-1,-0)
												MoveBox(m,-1,-0,0)
												MoveTransporter(PlayerX(i)-1,PlayerY(i)-0,-1,-0)
											EndIf
										EndIf
									Next
								EndIf
					
								
							EndIf
						Else If PlayerX(i)>0
							
							;special case: check if a box on transporter is next
							If MapTile(PlayerX(i)-1,PlayerY(i)-0)/100=17 And MapTile(PlayerX(i)-1,PlayerY(i)-0)<1703
								
								For m=0 To nofboxes-1
									If boxmoving(m)=False And playerx(i)-1=boxx(m) And PlayerY(i)-0=boxy(m)
										; box on transporter found
										
										If cantransportermove(PlayerX(i)-1,PlayerY(i)-0,-1,-0)
											MoveBox(m,-1,-0,0)
											MoveTransporter(PlayerX(i)-1,PlayerY(i)-0,-1,-0)
										EndIf
									EndIf
								Next
							EndIf
					
									
						EndIf


					EndIf
				EndIf
			Next
		 EndIf
		 ; right
		 If walkdir=1
			For i=1 To 4
				If FocusOnPlayer(i)=True
				  
					If PlayerMoving(i)=False And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)>=8 Or ConveyorActive(1)=False) And (MapTile(PlayerX(i),PlayerY(i))/100<>5 Or (MapTile(PlayerX(i),PlayerY(i)) Mod 100)<8 Or ConveyorActive(2)=False)
						If CanPlayerMove(i,1,0)=True	
							MovePlayer(i,1,0)
						Else If MapTile(PlayerX(i),PlayerY(i))/100=17 And MapTile(PlayerX(i),PlayerY(i))<1703
							; on transporter
							If CanTransporterMove(PlayerX(i),PlayerY(i),1,0)
								MovePlayer(i,1,0)
								MoveTransporter(PlayerX(i),PlayerY(i),1,0)
								PlayerAnimNew(i)=0
							Else If PlayerX(i)<MapWidth-1
								If MapTile(PlayerX(i)+1,PlayerY(i)-0)/100=17 And MapTile(PlayerX(i)+1,PlayerY(i)-0)<1703
									For m=0 To nofboxes-1
										If boxmoving(m)=False And playerx(i)+1=boxx(m) And PlayerY(i)+0=boxy(m)
											; box on transporter found
											
											If cantransportermove(PlayerX(i)+1,PlayerY(i)-0,+1,-0)
												MoveBox(m,+1,-0,0)
												MoveTransporter(PlayerX(i)+1,PlayerY(i)-0,+1,-0)
											EndIf
										EndIf
									Next
								EndIf
					
								
							EndIf
						Else If PlayerX(i)<MapWidth-1
							
							;special case: check if a box on transporter is next
							If MapTile(PlayerX(i)+1,PlayerY(i)-0)/100=17 And MapTile(PlayerX(i)+1,PlayerY(i)-0)<1703
								
								For m=0 To nofboxes-1
									If boxmoving(m)=False And playerx(i)+1=boxx(m) And PlayerY(i)-0=boxy(m)
										; box on transporter found
										
										If cantransportermove(PlayerX(i)+1,PlayerY(i)-0,+1,-0)
											MoveBox(m,+1,-0,0)
											MoveTransporter(PlayerX(i)+1,PlayerY(i)-0,+1,-0)
										EndIf
									EndIf
								Next
							EndIf
					
									
						EndIf


					EndIf
				 
				
			  EndIf
			Next
		 EndIf

	
If KeyDown(203) Or KeyDown(75)


  If view3drotation*100=view3drotation2
	; view3d
	view3drotation=view3drotation-1
	If view3drotation=-1 Then view3drotation=3
		
  EndIf
EndIf
If KeyDown(205) Or KeyDown(77)

  If view3drotation*100=view3drotation2
	; view3d
	view3drotation=view3drotation+1
	If view3drotation=4 Then view3drotation=0
		
  EndIf
EndIf

				
				
	  ; camera turning
	   pitch#=EntityPitch(camera)
	   roll#=EntityRoll(camera)
	   If view3drotation*100<>view3drotation2
			If view3drotation2<view3drotation*100 And view3drotation*100-view3drotation2<200
				view3drotation2=view3drotation2+2
				
				TurnEntity camera,-pitch,0,-roll
				TurnEntity camera,0,-1.8,0
				TurnEntity camera,pitch,0,roll
				
				
			Else If view3drotation2>view3drotation*100 And view3drotation2-view3drotation*100>200
				view3drotation2=view3drotation2+2
				TurnEntity camera,-pitch,0,-roll
				TurnEntity camera,0,-1.8,0
				TurnEntity camera,pitch,0,roll

	
				
		  	Else
				view3drotation2=view3drotation2-2
				TurnEntity camera,-pitch,0,-roll
				TurnEntity camera,0,+1.8,0
				TurnEntity camera,pitch,0,roll

	
				
			EndIf
			If view3drotation2=400 Then view3drotation2=0
			If view3drotation2=-2 Then view3drotation2=398
	
	   EndIf

End Function


; Function Collections
;Include "d:\blitz3d\terapak\bones2.bb"
Include "level.bb"
Include "setup.bb"
Include "gameobject.bb"
Include "text.bb"
Include "particles.bb"
Include "sound.bb"
Include "menu.bb"
Include "error.bb"
Include "pack.bb"