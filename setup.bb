

Function Setup()

	For i=1 To (MilliSecs() Mod 100)
		a=Rand(0,100)
	Next
	; general Setup routines called at program start

	; Setup Graphics
	
	MyGFXModeWidth(0)=640
	MyGFXModeHeight(0)=480
	MyGFXModeWidth(1)=800
	MyGFXModeHeight(1)=600
	MyGFXModeWidth(2)=1024
	MyGFXModeHeight(2)=768
	MyGFXModeWidth(3)=1280
	MyGFXModeHeight(3)=960
	MyGFXModeWidth(4)=1280
	MyGFXModeHeight(4)=1024


	If GfxMode3DExists(MyGFXModeWidth(MyGfxMode),MyGfxModeHeight(MyGfxMode),8+MyColMode*8)=False
		MyGfxMode=0
		MyColMode=1
		;MyWindowed=0
	EndIf 
	
	
		
	DisplayWidth=MyGFXModeWidth(MyGfxMode)
	DisplayHeight=MyGfxModeHeight(MyGfxMode)
	
	OldGfxMode=MyGfxMode
	OldWindowed=MyWindowed
		

	
	Graphics3D DisplayWidth,DisplayHeight,8+MyColMode*8,MyWindowed*2

	SetBuffer BackBuffer()
	

	InitPackFile()
	
	
	
	
		; Setup Camera
	Camera=CreateCamera()
	RotateEntity Camera,0,0,0
	PositionEntity Camera,0,0,-4
	CameraRange Camera,.98,140
	CameraZoom Camera,2
	
	; Setup Lights
	LevelLight=CreateLight(1)
	LightColor LevelLight,255,255,255
	PositionEntity LevelLight,100,100,-100
	RotateEntity LevelLight,25,-25,0
	

	; Fonts and Signs
	SetupFont()

	
	splashtex=MyLoadTexture("res12.dat",1,False)
	splashsquare=CreateFlatSquare(splashtex)
	
	PositionEntity SplashSquare,-.5,.5,-1
	RotateEntity SplashSquare,-90,0,0
	
	
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"05% Complete",True,False
	Flip
	
	xsound3=MilliSecs() Mod 100
	;Print xsound3
	xsound4=Rand(0,100)

	If FileType("CustomVoices\load.wav")=1
		xsound=MyLoadSound("CustomVoices\load.wav",False)
		xsound3=1000
	Else
		xsound=MyLoadSound("newsounds\loading2.wav",False)
	EndIf
	PlayMySound(xsound,0)
	
	xsound2=MilliSecs()
	
	
	

	

	; Load Level-Textures
	
	
	
	
	WaterTexture = MyLoadTexture("graphics\water.jpg",2,False)
	

	
	Repeat
	Until MilliSecs()-xsound2>2000 Or (xsound3>=15 Or xsound4>=50)

	
	
	;Print xsound4
	If xsound3<15
	
		
		
		If xsound4<50
			If xsound>0 
				FreeSound xsound
				xsound=0
			EndIf
			xsound=MyLoadSound("newsounds\loading4.wav",False)
			PlayMySound(xsound,0)
		
		EndIf
		
		
	EndIf
	xsound2=MilliSecs()

	
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"10% Complete",True,False
	Flip

	
	ConveyorTexture1(1) = MyLoadTexture ("res14.dat",1,False)
	ConveyorTexture2(1) = MyLoadTexture ("res14.dat",1,False)

	RotateTexture ConveyorTexture2(1),270
	ConveyorTexture3(1) = MyLoadTexture ("res14.dat",1,False)

	RotateTexture ConveyorTexture3(1),180
	ConveyorTexture4(1) = MyLoadTexture ("res14.dat",1,False)

	RotateTexture ConveyorTexture4(1),90
	ConveyorTexture1(2) = MyLoadTexture ("res14.dat",1,False)

	ConveyorTexture2(2) = MyLoadTexture ("res14.dat",1,False)

	RotateTexture ConveyorTexture2(2),270
	ConveyorTexture3(2) = MyLoadTexture ("res14.dat",1,False)

	RotateTexture ConveyorTexture3(2),180
	ConveyorTexture4(2) = MyLoadTexture ("res14.dat",1,False)

	RotateTexture ConveyorTexture4(2),90
	
		
	SwitcherTexture1(1) = MyLoadTexture ("graphics\switcher.jpg",1,False)
	SwitcherTexture2(1) = MyLoadTexture ("graphics\switcher.jpg",1,False)
	RotateTexture SwitcherTexture2(1),270
	SwitcherTexture3(1) = MyLoadTexture ("graphics\switcher.jpg",1,False)
	RotateTexture SwitcherTexture3(1),180
	SwitcherTexture4(1) = MyLoadTexture ("graphics\switcher.jpg",1,False)
	RotateTexture SwitcherTexture4(1),90
	SwitcherTexture1(2) = MyLoadTexture ("graphics\switcher.jpg",1,False)
	SwitcherTexture2(2) = MyLoadTexture ("graphics\switcher.jpg",1,False)
	RotateTexture SwitcherTexture2(2),270
	SwitcherTexture3(2) = MyLoadTexture ("graphics\switcher.jpg",1,False)
	RotateTexture SwitcherTexture3(2),180
	SwitcherTexture4(2) = MyLoadTexture ("graphics\switcher.jpg",1,False)
	RotateTexture SwitcherTexture4(2),90
	
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"20% Complete",True,False
	Flip




	
	IceTexture = MyLoadTexture ("graphics\ice.jpg",1,False)
	IceCurveTexture1 = MyLoadTexture ("graphics\icecurve1.jpg",1,False)
	IceCurveTexture2 = MyLoadTexture ("graphics\icecurve2.jpg",1,False)
	IceCurveTexture3 = MyLoadTexture ("graphics\icecurve3.jpg",1,False)
	IceCurveTexture4 = MyLoadTexture ("graphics\icecurve4.jpg",1,False)
	LavaTexture=MyLoadTexture ("graphics\lava.jpg",1,False)
	
	
	
	ElectroTexture=MyLoadAnimTexture("graphics\electro.bmp",6,64,64,0,4,False)

	
	
	

	
		
		
	; Object Textures
	
	RainbowTexture=MyLoadTexture("graphics\rainbow.jpg",2,False)
	
	
		
	; Load Models
	
	PlayerMesh(1) = MyLoadMD2("graphics\stinky.md2",False)
	PlayerTexture(1) = MyLoadTexture ("graphics\stinky2.bmp",1,False)
	PlayerTextureB(1) = MyLoadTexture ("graphics\stinky2b.bmp",1,False)
	PlayerTextureC(1) = MyLoadTexture ("graphics\stinky2c.bmp",1,False)
	PlayerTextureD(1) = MyLoadTexture ("graphics\stinky2d.bmp",1,False)
	EntityTexture PlayerMesh(1),PlayerTexture(1)
	HideEntity PlayerMesh(1)
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"30% Complete",True,False
	Flip


	
	PlayerMesh(2) = MyLoadMD2("graphics\stinky.md2",False)
	PlayerTexture(2) = MyLoadTexture ("graphics\loof.bmp",1,False)
	PlayerTextureB(2) = MyLoadTexture ("graphics\loofb.bmp",1,False)
	PlayerTextureC(2) = MyLoadTexture ("graphics\loofc.bmp",1,False)
	PlayerTextureD(2) = MyLoadTexture ("graphics\loofd.bmp",1,False)
	EntityTexture PlayerMesh(2),PlayerTexture(2)
	ScaleEntity PlayerMesh(2),.9,0.7,.95
	HideEntity PlayerMesh(2)
	
	PlayerMesh(3) = MyLoadMD2("graphics\stinky.md2",False)
	PlayerTexture(3) = MyLoadTexture ("graphics\qookie2a.bmp",1,False)
	PlayerTextureB(3) = MyLoadTexture ("graphics\qookie2b.bmp",1,False)
	PlayerTextureC(3) = MyLoadTexture ("graphics\qookie2c.bmp",1,False)
	PlayerTextureD(3) = MyLoadTexture ("graphics\qookie2d.bmp",1,False)
	EntityTexture PlayerMesh(3),PlayerTexture(3)
	ScaleEntity PlayerMesh(3),1.1,.82,1.1
	HideEntity PlayerMesh(3)


	PlayerMesh(4) = MyLoadMD2("graphics\peegueb.md2",False)
	PlayerTexture(4) = MyLoadTexture ("graphics\peegue2a.bmp",1,False)
	PlayerTextureB(4) = MyLoadTexture ("graphics\peegue2b.bmp",1,False)
	PlayerTextureC(4) = MyLoadTexture ("graphics\peegue2c.bmp",1,False)
	PlayerTextureD(4) = MyLoadTexture ("graphics\peegue2d.bmp",1,False)
	EntityTexture PlayerMesh(4),PlayerTexture(4)
	ScaleEntity PlayerMesh(4),1.05,.9,1.05
	HideEntity PlayerMesh(4)
	


	
	WoodBoxMesh = MyLoadMesh("graphics\box.3ds",False)
	WoodBoxTexture = MyLoadTexture ("graphics\woodbox.bmp",1,False)
	EntityTexture WoodBoxMesh,WoodBoxTexture
;	ScaleEntity WoodBoxMesh,1.1,1.1,1.1
	HideEntity WoodBoxMesh

	SteelBoxMesh = MyLoadMesh("graphics\box.3ds",False)
	SteelBoxTexture = MyLoadTexture ("graphics\steelbox.bmp",1,False)
	EntityTexture SteelBoxMesh,SteelBoxTexture
	HideEntity SteelBoxMesh
	
	ReflectorMesh = MyLoadMesh("graphics\reflector.3ds",False)
	ReflectorTexture = MyLoadTexture ("graphics\reflector.bmp",2,False)
	EntityTexture ReflectorMesh,ReflectorTexture
	RotateMesh ReflectorMesh,-90,0,0
	HideEntity ReflectorMesh

	
	BoulderMesh = CreateSphere()
	BoulderTexture = MyLoadTexture ("res23.dat",1,False)
	EntityTexture BoulderMesh,BoulderTexture
	ScaleEntity BoulderMesh,.45,.45,.45
	HideEntity BoulderMesh
	
	GhostBoxMesh = MyLoadMesh("graphics\box.3ds",False)
	GhostBoxTexture = MyLoadTexture ("graphics\plasmabox.jpg",2,False)
	EntityTexture GhostBoxMesh,GhostBoxTexture
	HideEntity GhostBoxMesh
	
	BoomboxMesh = MyLoadMesh("graphics\barrel.3ds",False)
	RotateMesh BoomboxMesh,-90,0,0
	BoomboxTexture = MyLoadTexture ("graphics\barrel.jpg",1,False)
	EntityTexture BoomboxMesh,BoomboxTexture
	ScaleMesh WoodBoxMesh,1.1,1.1,1.1
	HideEntity BoomboxMesh
	
	PrismMesh = MyLoadMesh("graphics\prism.3ds",False)
	PrismTexture = MyLoadTexture ("graphics\prism.jpg",1,False)
	RotateMesh PrismMesh,-90,0,0
	EntityTexture PrismMesh,PrismTexture
	ScaleEntity PrismMesh,1.1,1.1,1.1
	HideEntity PrismMesh
	
	BoxerMesh = MyLoadMesh("graphics\box.3ds",False)
	BoxerTexture = MyLoadTexture ("graphics\boxer.bmp",1,False)
	PositionMesh BoxerMesh,0,0.4,0
	EntityTexture BoxerMesh,BoxerTexture
	ScaleEntity BoxerMesh,1.3,1.3,1.3
	HideEntity BoxerMesh
	
	StickyCubeTexture = MyLoadTexture ("res54.dat",1,False)
	LinkSphereRedTexture = MyLoadTexture("res58.dat",1,False)
	LinkSphereYellowTexture = MyLoadTexture("res57.dat",1,False)
	LinkSphereBlueTexture = MyLoadTexture("res55.dat",1,False)
	LinkSphereGreenTexture = MyLoadTexture("res56.dat",1,False)
	PushCannonTexture=MyLoadTexture("res53.dat",1,False)

	
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"40% Complete",True,False
	Flip




		
	CannonMesh = MyLoadMesh("graphics\scouge.3ds",False)
	CannonTexture = MyLoadTexture ("graphics\scouge3.bmp",1,False)
	;PositionMesh CannonMesh,0,0,-0.36
;	RotateEntity CannonMesh,0,0,90
	RotateEntity CannonMesh,-90,-90,0
	EntityTexture CannonMesh,CannonTexture
	HideEntity CannonMesh
	
	FireBallMesh = CreateSphere()
	EntityColor FireBallMesh,255,240,200
	EntityAlpha FireBallMesh,0.5
	;EntityShininess  FireBallMesh,1
	ScaleEntity FireballMesh,0.2,0.2,0.2
	HideEntity FireBallMesh
	
	RoundShadowTexture = MyLoadTexture ("graphics\beachballshadow.bmp",2,False)
	;RoundShadowMesh = CreateFlatSquare(RoundShadowTexture)
	;HideEntity RoundShadowMesh
	
	IronGateMesh = MyLoadMesh("graphics\gate.3ds",False)
	PositionMesh IronGateMesh,0,0.1,0
	TurnEntity IronGateMesh,-90,0,0
	ScaleEntity IronGateMesh,1,1,1.3
	HideEntity IronGateMesh
	
	MonsterMesh(0) = MyLoadMD2("graphics\coily.md2",False);CreateSphere()
	MonsterTexture(0)=MyLoadTexture ("graphics\coily.bmp",1,False) 
;	RotateMesh MonsterMesh(0),0,0,-90
	ScaleEntity MonsterMesh(0),0.3,0.2,0.3
	;RotateEntity MonsterMesh(0),0,90,0
	;EntityColor MonsterMesh(0),160,160,160
	EntityTexture MonsterMesh(0),MonsterTexture(0)
	HideEntity MonsterMesh(0)
	;MonsterShadowMesh(0) = CreateFlatSquare(RoundShadowTexture)
	;ScaleMesh MonsterShadowMesh(0),.8,1,.8
	;HideEntity MonsterShadowMesh(0)
	
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"50% Complete",True,False
	Flip

	
	
	MonsterMesh(1) = MyLoadMesh("graphics\zbot.3ds",False)	
	MonsterTexture(1)=MyLoadTexture ("graphics\zbot.bmp",1,False) 
	ScaleEntity MonsterMesh(1),0.6,0.6,0.6
	EntityTexture MonsterMesh(1),MonsterTexture(1)
	HideEntity MonsterMesh(1)
	
	MonsterMesh(3) = MyLoadMD2("graphics\bomb.md2",False)
	MonsterTexture(3)=MyLoadTexture ("graphics\bomb.bmp",1,False) 
	RotateEntity MonsterMesh(3),0,180,0
	EntityTexture MonsterMesh(3),MonsterTexture(3)
	HideEntity MonsterMesh(3)

	MonsterMesh(5) = MyLoadMesh("graphics\ufo.3ds",False)	
	MonsterTexture(5)=MyLoadTexture ("graphics\ufo.bmp",1,False) 
;	ScaleEntity MonsterMesh(5),0.6,0.6,0.6
	EntityTexture MonsterMesh(5),MonsterTexture(5)
	HideEntity MonsterMesh(5)
	
	MonsterMesh(7) = MyLoadMD2("graphics\chomper.md2",False)
	MonsterTexture(7)=MyLoadTexture ("graphics\chomper2a.bmp",1,False) 
	MonsterTexture(8)=MyLoadTexture ("graphics\chomper2b.bmp",1,False) 

;	ScaleEntity MonsterMesh(7),0.8,0.8,0.8
;	RotateMesh MonsterMesh(7),-90,0,0
	;RotateEntity MonsterMesh(7),0,180,0
	EntityTexture MonsterMesh(7),MonsterTexture(7)
	HideEntity MonsterMesh(7)

	MonsterMesh(9) = MyLoadMesh("graphics\ghost.3ds",False)	
	MonsterTexture(9)=MyLoadTexture ("graphics\ghost2.bmp",2,False) 
	ScaleEntity MonsterMesh(9),1,1,.8
	RotateMesh MonsterMesh(9),0,0,-90
	EntityTexture MonsterMesh(9),MonsterTexture(9)
	HideEntity MonsterMesh(9)
	
	

	
	;MonsterMesh(10) = MyLoadMesh("graphics\ghost.3ds",False)	
	;MonsterTexture(10)=MyLoadTexture ("graphics\rainbow.jpg",2,False) 
	;ScaleEntity MonsterMesh(10),.6,.6,.5
	;RotateMesh MonsterMesh(10),0,0,-90
	;EntityTexture MonsterMesh(10),Rainbowtexture
	;HideEntity MonsterMesh(10)






	FishMesh= MyLoadMesh("graphics\fish.x",False)
	FitMesh FishMesh,-.5,-.5,-.5,.5,.5,.5,False
	FishTexture = MyLoadTexture("graphics\fish.bmp",1,False)
	EntityTexture FishMesh,FishTexture
	HideEntity FishMesh	
	
	
	
	TransporterMesh = MyLoadMesh("graphics\box.3ds",False)
	PositionMesh TransporterMesh,0,-1,0
	ScaleMesh TransporterMesh,1,0.1,1
	TransporterTexture = MyLoadTexture ("graphics\steelbox.bmp",1,False)
	EntityTexture TransporterMesh,TransporterTexture
	HideEntity TransporterMesh
	
	TrampolineTexture = MyLoadTexture("res59.dat",1,False)

	WarperMesh=CreateSphere()
	ScaleMesh WarperMesh,.5,.5,.5
	EntityTexture WarperMesh,LavaTexture
;	EntityColor WarperMesh,255,100,100
	EntityAlpha WarperMesh,.7
	HideEntity WarperMesh
	
	
	If xsound3<15 And xsound4>=50
	
		If xsound>0 
			FreeSound xsound
			xsound=0
		EndIf
		If xsound4>=75
			xsound=MyLoadSound("newsounds\loading3.wav",False)
			PlayMySound(xsound,0)
		Else 
			xsound=MyLoadSound("newsounds\loading5.wav",False)
			PlayMySound(xsound,0)
		EndIf
		xsound2=MilliSecs()
	EndIf
	

	
	
	; Particles
	; just two types: sprite (0) and flatsquare (1)
	ParticleMesh(0)=CreateSprite()
	ParticleMesh(1)=CreateFlatSquare(WaterTexture) ;placeholder texture
	HideEntity ParticleMesh(0)
	HideEntity ParticleMesh(1)
	; and Textures
	ParticleTexture=MyLoadAnimTexture ("res21.dat",2,32,32,0,12,False)
	ParticleTexture2=MyLoadAnimTexture ("res22.dat",4,32,32,0,2,False)
	; 0-cannonball, 100-woodsplinter, 101-steelsplinter, 3-rainbow star, 4-water ripple
	; 5,6,7,8 - colour flares, 9-"250"

	

	

		
	; Construct Models
	BreakAwayMesh=CreateFlatSquare(RainbowTexture) ; for now, since its textured later
	HideEntity BreakAwayMesh
	
	SwitcherMesh=CreateFlatSquare(RainbowTexture) ; for now, since its textured later
	HideEntity SwitcherMesh
	
	ElectroMesh=CreateFlatSquare(ElectroTexture)
	HideEntity ElectroMesh
	
	SpikeMesh = MyLoadMesh("graphics\spikes.3ds",False)
	RotateMesh SpikeMesh,-90,0,0
	SpikeTexture = MyLoadTexture ("graphics\spikes.jpg",1,False)
	EntityTexture SpikeMesh,SpikeTexture
	HideEntity SpikeMesh
	
	




	
	;crystal
	ItemMesh(0)=MyLoadMesh("graphics\coin.3ds",False)
	ScaleEntity ItemMesh(0),.8,.8,0.5
	ItemTexture(0)=MyLoadTexture("graphics\coin2.bmp",2,False)
	ItemShadowTexture(0)=MyLoadTexture("graphics\coinshadow.bmp",2,False)
	ItemShadowMesh(0)=CreateFlatSquare(ItemShadowTexture(0))
	PositionMesh ItemShadowMesh(0),-0.5,0,0.5
	ScaleEntity ItemShadowMesh(0),.6,.6,0.1
	EntityAlpha ItemShadowMesh(0),.5
	HideEntity ItemShadowMesh(0)
		
	EntityTexture ItemMesh(0),ItemTexture(0)
	HideEntity ItemMesh(0)
	;endgate
	ItemMesh(1)=CreateSphere()
	ScaleEntity ItemMesh(1),0.5,0.5,0.5
	EntityTexture ItemMesh(1),RainbowTexture
	HideEntity ItemMesh(1)
	;time
	ItemMesh(2)=MyLoadMesh("graphics\clock.3ds",False)
	ItemTexture(2)=MyLoadTexture("graphics\clock.bmp",1,False)
	EntityTexture itemmesh(2),itemtexture(2)
	RotateMesh itemmesh(2),-90,0,0
;	ScaleEntity ItemMesh(2),.2,.2,.2
	HideEntity ItemMesh(2)
	;money
	ItemMesh(3)=MyLoadMesh("graphics\coin.3ds",False)
	ScaleEntity ItemMesh(3),.8,.8,.5
	ItemTexture(3)=MyLoadTexture("graphics\coin.jpg",1,False)
	EntityTexture ItemMesh(3),ItemTexture(3)
	HideEntity ItemMesh(3)
	

	ButtonOnTexture=MyLoadTexture("graphics\buttonv5.bmp",4,False)
	ButtonOffTexture=MyLoadTexture("graphics\buttonoffv5.bmp",4,False)

	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"60% Complete",True,False
	Flip


	
	
	PillarMesh(0)=MyLoadMesh("graphics\pillar.3ds",False)
	PillarTexture(0)=MyLoadTexture("graphics\pillar1.jpg",1,False)
	EntityTexture PillarMesh(0),PillarTexture(0)
	RotateEntity PillarMesh(0),-90,0,0
	HideEntity PillarMesh(0)

	PillarMesh(1)=MyLoadMesh("graphics\pyramid.3ds",False)
	PillarTexture(1)=MyLoadTexture("graphics\pillar2.jpg",1,False)
	EntityTexture PillarMesh(1),PillarTexture(1)
	RotateEntity PillarMesh(1),-90,0,0
	HideEntity PillarMesh(1)

	PillarMesh(2)=MyLoadMesh("graphics\spike.3ds",False)
	PillarTexture(2)=MyLoadTexture("graphics\pillar2.jpg",1,False)
	EntityTexture PillarMesh(2),PillarTexture(2)
	RotateEntity PillarMesh(2),-90,0,0
	HideEntity PillarMesh(2)
	
	PillarMesh(3)=MyLoadMesh("graphics\fountain.3ds",False)
	PillarTexture(3)=MyLoadTexture("graphics\fountain.jpg",1,False)
	EntityTexture PillarMesh(3),PillarTexture(3)
	RotateMesh PillarMesh(3),-90,0,0
	PositionMesh PillarMesh(3),0,.5,0
	ScaleMesh PillarMesh(3),1,.8,1
	HideEntity PillarMesh(3)
	


	
	PillarMesh(4)=MyLoadMesh("graphics\pyramid2.3ds",False)
	PillarTexture(4)=MyLoadTexture("graphics\pyramid.jpg",1,False)
	EntityTexture PillarMesh(4),PillarTexture(4)
	RotateMesh PillarMesh(4),-90,0,0
	PositionMesh PillarMesh(4),0,.5,0
	ScaleMesh PillarMesh(4),3,3,3
	HideEntity PillarMesh(4)
	
	PillarMesh(5)=MyLoadMesh("graphics\pyramid2.3ds",False)
;	PillarTexture(4)=MyLoadTexture("graphics\pyramid.jpg",1,False)
	EntityTexture PillarMesh(5),PillarTexture(4)
	RotateMesh PillarMesh(5),-90,0,0
	PositionMesh PillarMesh(5),0,.5,0
	ScaleMesh PillarMesh(5),1,1,1
	HideEntity PillarMesh(5)
	
	PillarMesh(6)=MyLoadMesh("graphics\box.3ds",False)
	;PillarTexture(6)=MyLoadTexture("graphics\pyramid.jpg",1,False)
	EntityTexture PillarMesh(6),WoodBoxTexture
	;RotateMesh PillarMesh(6),-90,0,0
	PositionMesh PillarMesh(6),0,.5,0
	ScaleMesh PillarMesh(6),3,3,3
	HideEntity PillarMesh(6)
	
	PillarMesh(7)=MyLoadMesh("graphics\lamp.3ds",False)
	PillarTexture(7)=MyLoadTexture("graphics\lamp.jpg",1,False)
	EntityTexture PillarMesh(7),PillarTexture(7)
	RotateMesh PillarMesh(7),-90,0,0
	PositionMesh PillarMesh(7),0,.5,0
	ScaleMesh PillarMesh(7),1.5,1.5,1.5
	HideEntity PillarMesh(7)
	
		
	PillarMesh(8)=MyLoadMesh("graphics\house.3ds",False)
	HouseTexture(1)=MyLoadTexture("graphics\house1.jpg",1,False)
	HouseTexture(2)=MyLoadTexture("graphics\house2.jpg",1,False)
	HouseTexture(3)=MyLoadTexture("graphics\house3.jpg",1,False)
	HouseTexture(4)=MyLoadTexture("graphics\house4.jpg",1,False)
	;EntityTexture PillarMesh(8),PillarTexture(8)
	RotateMesh PillarMesh(8),-90,0,0
	PositionMesh PillarMesh(8),0,1,0
	ScaleMesh PillarMesh(8),1.5,1.5,1.5
	HideEntity PillarMesh(8)

	



	
	PillarMesh(16)=MyLoadMesh("graphics\tree.3ds",False)
	PillarTexture(16)=MyLoadTexture("graphics\tree.jpg",1,False)
	EntityTexture PillarMesh(16),PillarTexture(16)
	RotateEntity PillarMesh(16),-90,0,0
	TurnEntity PillarMesh(16),0,0,90
	ScaleMesh PillarMesh(16),1,1,1.5
	HideEntity PillarMesh(16)
	PillarMesh(17)=MyLoadMesh("graphics\tree.3ds",False)
	;PillarTexture(16)=MyLoadTexture("graphics\tree.jpg",1,False)
	EntityTexture PillarMesh(17),PillarTexture(16)
	RotateEntity PillarMesh(17),-90,0,0
	TurnEntity PillarMesh(17),0,0,90
	ScaleMesh PillarMesh(17),.4,.4,1.6
	HideEntity PillarMesh(17)
	
	
	
	PillarMesh(20)=MyLoadMesh("graphics\tree.3ds",False)
	PillarTexture(20)=MyLoadTexture("graphics\shroom.jpg",1,False)
	EntityTexture PillarMesh(20),PillarTexture(20)
	RotateEntity PillarMesh(20),-90,0,0
	ScaleMesh PillarMesh(20),.3,.3,.3
	HideEntity PillarMesh(20)
	PillarMesh(21)=MyLoadMesh("graphics\tree.3ds",False)
;	PillarTexture(20)=MyLoadTexture("graphics\shroom.jpg",1,False)
	EntityTexture PillarMesh(21),PillarTexture(20)
	RotateEntity PillarMesh(21),-90,0,0
	ScaleMesh PillarMesh(21),1,1,1
	HideEntity PillarMesh(21)
	
	PillarMesh(18)=MyLoadMesh("graphics\pine.3ds",False)
	PillarTexture(18)=MyLoadTexture("graphics\pine1.jpg",1,False)
	EntityTexture PillarMesh(18),PillarTexture(18)
	;RotateEntity PillarMesh(20),-90,0,0
	;caleMesh PillarMesh(20),.3,.3,.3
	HideEntity PillarMesh(18)
	
	PillarMesh(19)=MyLoadMesh("graphics\pine.3ds",False)
	PillarTexture(19)=MyLoadTexture("graphics\pine2.jpg",1,False)
	EntityTexture PillarMesh(19),PillarTexture(19)
	;RotateEntity PillarMesh(20),-90,0,0
	;caleMesh PillarMesh(20),.3,.3,.3
	HideEntity PillarMesh(19)


	

	
	
	PillarMesh(22)=MyLoadMesh("graphics\flower.3ds",False)
	PillarTexture(22)=MyLoadTexture("graphics\flower.jpg",1,False)
	EntityTexture PillarMesh(22),PillarTexture(22)
	RotateMesh PillarMesh(22),-90,0,0
;	PositionMesh PillarMesh(5),0,1,0
	ScaleMesh PillarMesh(22),.02,.02,.02
	HideEntity PillarMesh(22)
	
	

	PillarTexture(23)=MyLoadTexture("graphics\stinkystatue.jpg",1,False)
	
	PillarMesh(25)=MyLoadMesh("graphics\snowman.3ds",False)
	PillarTexture(25)=MyLoadTexture("graphics\snowman.jpg",1,False)
	EntityTexture PillarMesh(25),PillarTexture(25)
;	RotateMesh PillarMesh(25),-90,0,0
;	PositionMesh PillarMesh(5),0,1,0
;	ScaleMesh PillarMesh(22),.02,.02,.02
	HideEntity PillarMesh(25)

	
	
	
	PillarTexture(30)=MyLoadTexture("graphics\stinkystatue2.jpg",1,False)



	
	



	
	

	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"70% Complete",True,False
	Flip


	

	
	LoadSounds()
	
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"100% Complete",True,False
	Flip

	
	

	MenuSetup()	
	
	If FileType("CustomLevels")=0
		CreateDir("CustomLevels")
	EndIf
	If FileType("CustomModels")=0
		CreateDir("CustomModels")
	EndIf

	
	LightColor LevelLight,255,0,255

	FreeTexture Splashtex
	FreeEntity Splashsquare
	
	Repeat
	Until MilliSecs()-xsound2>3000

	
	If xsound>0 
		FreeSound xsound
		xsound=0
	EndIf
	
	



	
	
End Function


Function LoadStyleTextures()

	; Note that all textures have to be assigned, since they will all be freed at the end
	
	
	
	If MapBackground=10 
		MapStyle=-1
			; Rainbow World
		FloorTexture = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		FloorTexture4 = MyLoadTexture ("graphics\rainbow.jpg",1,False)

		FloorTextureSign = MyLoadTexture ("graphics\rainbow.jpg",1,False)

	
		WallTextureTop1 = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		
		WallTextureSide1a = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		WallTextureSide2b = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\rainbow.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\rainbow.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		
		
		TeleportTexture(0)=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		TeleportTexture(1)=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		TeleportTexture(2)=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		TeleportTexture(3)=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		TeleportTexture(4)=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		TeleportTexture(5)=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		TeleportTexture(6)=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		TeleportTexture(7)=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		
	EndIf
	
	
	;MapStyle =Rand(0,3)	
	If MapStyle=0
		; Style 0 - Cave
		

		FloorTexture = MyLoadTexture ("graphics\cavefloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\cavefloor4.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\cavefloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\cavefloor2.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf
	
		WallTextureTop1 = MyLoadTexture ("graphics\cavewalltop1.jpg",1,False)
		FloorTextureSign = MyLoadTexture ("graphics\cavefloorsign.jpg",1,False)

		
		WallTextureSide1a = MyLoadTexture ("graphics\cavewallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\cavewallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\cavewallside2a.jpg",1,False)
		WallTextureSide2b = MyLoadTexture ("graphics\cavewallside2b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\cavewallside3a.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\cavewallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\cavebreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\cavebreak2.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\cavebreak3.jpg",1,False)
		
		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\cavetele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\cavetele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\cavetele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\cavetele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\cavetele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\cavetele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\cavetele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\cavetele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf



		
		
	EndIf
	
	If MapStyle=1
		; Style 1 - Sand
		FloorTexture = MyLoadTexture ("graphics\sandfloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\sandfloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\sandfloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\gardenfloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf

		

		FloorTextureSign = MyLoadTexture ("graphics\sandfloorsign.jpg",1,False)

	
		WallTextureTop1 = MyLoadTexture ("graphics\sandwalltop1.jpg",1,False)
		
		WallTextureSide1a = MyLoadTexture ("graphics\sandwallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\sandwallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\sandwallside2a.jpg",1,False)
		WallTextureSide2b = MyLoadTexture ("graphics\sandwallside2b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\sandwallside3a.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\sandwallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\sandbreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\sandbreak2.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\sandbreak3.jpg",1,False)
		
		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\sandtele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\sandtele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\sandtele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\sandtele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\sandtele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\sandtele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\sandtele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\sandtele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf


		
		
	EndIf
	
	If MapStyle=2
		; Style 2 - Wood
		FloorTexture = MyLoadTexture ("graphics\woodfloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\woodfloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\woodfloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\woodfloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf

		FloorTextureSign = MyLoadTexture ("Graphics\woodfloorsign.jpg",1,False)

	
		WallTextureTop1 = MyLoadTexture ("graphics\woodwalltop1.jpg",1,False)
		
		WallTextureSide1a = MyLoadTexture ("graphics\woodwallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\woodwallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\woodwallside2a.jpg",1,False)
		WallTextureSide2b =MyLoadTexture ("graphics\woodwallside2b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\woodwallside3a.jpg",1,False)
		WallTextureSide3b =MyLoadTexture ("graphics\woodwallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\woodbreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\woodbreak2.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\woodbreak3.jpg",1,False)
		
		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\woodtele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\woodtele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\woodtele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\woodtele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\woodtele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\woodtele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\woodtele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\woodtele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf

		


		
		
	EndIf

	If MapStyle=3
		; Style 3 - Metal
		FloorTexture = MyLoadTexture ("graphics\metalfloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\metalfloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\metalfloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\metalfloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf


		FloorTextureSign = MyLoadTexture ("graphics\metalfloorsign.jpg",1,False)
	
		WallTextureTop1 = MyLoadTexture ("graphics\metalwalltop1.jpg",1,False)
		
		WallTextureSide1a = MyLoadTexture ("graphics\metalwallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\metalwallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\metalwallside1a.jpg",1,False)
		WallTextureSide2b = MyLoadTexture ("graphics\metalwallside1b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\metalwallside3a.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\metalwallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\metalbreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\metalbreak2.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\metalbreak3.jpg",1,False)
		
		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\metaltele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\metaltele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\metaltele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\metaltele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\metaltele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\metaltele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\metaltele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\metaltele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf



		
		

		
		
	EndIf
	
	If MapStyle=4
		; Style 4 - Castle
		FloorTexture = MyLoadTexture ("graphics\castlefloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\castlefloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\castlefloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\castlefloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf


	
		WallTextureTop1 = MyLoadTexture ("graphics\castlewalltop1.jpg",1,False)
		FloorTextureSign = MyLoadTexture ("graphics\castlefloorsign.jpg",1,False)


		
		WallTextureSide1a = MyLoadTexture ("graphics\castlewallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\castlewallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("res24.dat",1,False)
		WallTextureSide2b = MyLoadTexture ("res25.dat",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\castlewallside3a.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\castlewallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\castlebreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\castlebreak1.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\castlebreak1.jpg",1,False)
		
		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\castletele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\castletele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\castletele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\castletele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\castletele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\castletele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\castletele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\castletele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf


	EndIf
	
	If MapStyle=5
		; Style 5 - Jade
		FloorTexture = MyLoadTexture ("graphics\greenfloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\greenfloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\greenfloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\greenfloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf


	
	
		WallTextureTop1 = MyLoadTexture ("graphics\greenwalltop1.jpg",1,False)
		FloorTextureSign = MyLoadTexture ("graphics\greenfloorsign.jpg",1,False)


		
		WallTextureSide1a = MyLoadTexture ("graphics\greenwallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\greenwallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\greenwallside1a.jpg",1,False)
		WallTextureSide2b = MyLoadTexture ("graphics\greenwallside1b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\greenwallside1a.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\greenwallside1b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\greenbreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\greenbreak2.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\greenbreak3.jpg",1,False)
		
		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\greentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\greentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\greentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\greentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\greentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\greentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\greentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\greentele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf


	EndIf
	
	If MapStyle=6
		; Style 6 - Spooky
		

		FloorTexture = MyLoadTexture ("graphics\spookyfloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\spookyfloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\spookyfloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\spookyfloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf

	
		WallTextureTop1 = MyLoadTexture ("graphics\spookywalltop1.jpg",1,False)
		FloorTextureSign = MyLoadTexture ("graphics\spookyfloorsign.jpg",1,False)

		
		WallTextureSide1a = MyLoadTexture ("graphics\spookywallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\spookywallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\spookywallside2a.jpg",1,False)
		WallTextureSide2b = MyLoadTexture ("graphics\spookywallside2b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\spookywallside3a.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\spookywallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\spookybreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\spookybreak2.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\spookybreak3.jpg",1,False)
		
		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\spookytele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\spookytele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\spookytele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\spookytele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\spookytele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\spookytele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\spookytele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\spookytele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf
		
		
	EndIf
	
	If MapStyle=7
		; Style 7 - Garden
		

		FloorTexture = MyLoadTexture ("graphics\gardenfloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\gardenfloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\gardenfloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\gardenfloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf


	
		WallTextureTop1 = MyLoadTexture ("graphics\gardenwalltop1.jpg",1,False)
		FloorTextureSign = MyLoadTexture ("graphics\gardenfloorsign.jpg",1,False)

		
		WallTextureSide1a = MyLoadTexture ("graphics\gardenwallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\gardenwallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\gardenwallside1a.jpg",1,False)
		WallTextureSide2b = MyLoadTexture ("graphics\gardenwallside1b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\gardenwallside3a.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\gardenwallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\castlebreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\castlebreak1.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\castlebreak1.jpg",1,False)

		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\gardentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gardentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gardentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gardentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gardentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gardentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gardentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gardentele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf
		
		
	EndIf
	
	If MapStyle=8
		; Style 8 - Aztec
		

		FloorTexture = MyLoadTexture ("graphics\aztecfloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\aztecfloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\aztecfloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\aztecfloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf


	
		WallTextureTop1 = MyLoadTexture ("graphics\aztecwalltop1.jpg",1,False)
		FloorTextureSign = MyLoadTexture ("graphics\aztecfloorsign.jpg",1,False)

		
		WallTextureSide1a = MyLoadTexture ("graphics\aztecwallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\aztecwallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\aztecwallside2a.jpg",1,False)
		WallTextureSide2b = MyLoadTexture ("graphics\aztecwallside2b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\aztecwallside3a.jpg",1,False)
		WallTextureSide3b = MyLoadTexture ("graphics\aztecwallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\aztecbreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\aztecbreak2.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\aztecbreak3.jpg",1,False)

		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\aztectele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\aztectele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\aztectele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\aztectele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\aztectele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\aztectele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\aztectele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\aztectele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf


		
		
	EndIf
	
	If MapStyle=9
		; Style 9 - custom
		
		nocustomtex=True
		
		If FileType("customtextures\"+customtexdir$+"\floor1.jpg")=1
			If FileType("customtextures\"+customtexdir$+"\floor2.jpg")=1
				If FileType("customtextures\"+customtexdir$+"\floor3.jpg")=1
					If FileType("customtextures\"+customtexdir$+"\floor4.jpg")=1
						If FileType("customtextures\"+customtexdir$+"\walltop.jpg")=1
							If FileType("customtextures\"+customtexdir$+"\sign.jpg")=1
								If FileType("customtextures\"+customtexdir$+"\wallside1a.jpg")=1
									If FileType("customtextures\"+customtexdir$+"\wallside1b.jpg")=1
										If FileType("customtextures\"+customtexdir$+"\wallside2a.jpg")=1
											If FileType("customtextures\"+customtexdir$+"\wallside2b.jpg")=1
												If FileType("customtextures\"+customtexdir$+"\wallside3a.jpg")=1
													If FileType("customtextures\"+customtexdir$+"\wallside3b.jpg")=1
														If FileType("customtextures\"+customtexdir$+"\break.jpg")=1


															nocustomtex=False
														EndIf
													EndIf
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
		
		If nocustomtex=True
			MapStyle=2
		Else
		
			FloorTexture = MyLoadTexture ("customtextures\"+customtexdir$+"\floor1.jpg",1,False)
			FloorTexture2 = MyLoadTexture ("customtextures\"+customtexdir$+"\floor2.jpg",1,False)
			FloorTexture3 = MyLoadTexture ("customtextures\"+customtexdir$+"\floor3.jpg",1,False)
			
			If snow=0
				FloorTexture4 = MyLoadTexture ("customtextures\"+customtexdir$+"\floor4.jpg",1,False)
				
			Else
				FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
			EndIf
	
	
		
			WallTextureTop1 = MyLoadTexture ("customtextures\"+customtexdir$+"\walltop.jpg",1,False)
			FloorTextureSign = MyLoadTexture ("customtextures\"+customtexdir$+"\sign.jpg",1,False)
	
			
			WallTextureSide1a = MyLoadTexture ("customtextures\"+customtexdir$+"\wallside1a.jpg",1,False)
			WallTextureSide1b = MyLoadTexture ("customtextures\"+customtexdir$+"\wallside1b.jpg",1,False)
			WallTextureSide2a = MyLoadTexture ("customtextures\"+customtexdir$+"\wallside2a.jpg",1,False)
			WallTextureSide2b = MyLoadTexture ("customtextures\"+customtexdir$+"\wallside2b.jpg",1,False)
			WallTextureSide3a = MyLoadTexture ("customtextures\"+customtexdir$+"\wallside3a.jpg",1,False)
			WallTextureSide3b = MyLoadTexture ("customtextures\"+customtexdir$+"\wallside3b.jpg",1,False)
	
			
			BreakAwayTexture1=MyLoadTexture ("customtextures\"+customtexdir$+"\break.jpg",1,False)
			BreakAwayTexture2=MyLoadTexture ("customtextures\"+customtexdir$+"\break.jpg",1,False)
			BreakAwayTexture3=MyLoadTexture ("customtextures\"+customtexdir$+"\break.jpg",1,False)

		
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		
		EndIf

		
		
	EndIf
	
	If MapStyle=2
		; Style 2 - Wood
		FloorTexture = MyLoadTexture ("graphics\woodfloor1.jpg",1,False)
		FloorTexture2 = MyLoadTexture ("graphics\woodfloor2.jpg",1,False)
		FloorTexture3 = MyLoadTexture ("graphics\woodfloor3.jpg",1,False)
		If snow=0
			FloorTexture4 = MyLoadTexture ("graphics\woodfloor4.jpg",1,False)
		Else
			FloorTexture4 = MyLoadTexture ("graphics\icefloor.jpg",1,False)
		EndIf

		FloorTextureSign = MyLoadTexture ("Graphics\woodfloorsign.jpg",1,False)

	
		WallTextureTop1 = MyLoadTexture ("graphics\woodwalltop1.jpg",1,False)
		
		WallTextureSide1a = MyLoadTexture ("graphics\woodwallside1a.jpg",1,False)
		WallTextureSide1b = MyLoadTexture ("graphics\woodwallside1b.jpg",1,False)
		WallTextureSide2a = MyLoadTexture ("graphics\woodwallside2a.jpg",1,False)
		WallTextureSide2b =MyLoadTexture ("graphics\woodwallside2b.jpg",1,False)
		WallTextureSide3a = MyLoadTexture ("graphics\woodwallside3a.jpg",1,False)
		WallTextureSide3b =MyLoadTexture ("graphics\woodwallside3b.jpg",1,False)

		
		BreakAwayTexture1=MyLoadTexture ("graphics\woodbreak1.jpg",1,False)
		BreakAwayTexture2=MyLoadTexture ("graphics\woodbreak2.jpg",1,False)
		BreakAwayTexture3=MyLoadTexture ("graphics\woodbreak3.jpg",1,False)
		
		If OptionsTeleport=0
			TeleportTexture(0)=MyLoadTexture ("graphics\woodtele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\woodtele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\woodtele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\woodtele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\woodtele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\woodtele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\woodtele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\woodtele7.jpg",1,False)
		Else
			TeleportTexture(0)=MyLoadTexture ("graphics\gentele0.jpg",1,False)
			TeleportTexture(1)=MyLoadTexture ("graphics\gentele1.jpg",1,False)
			TeleportTexture(2)=MyLoadTexture ("graphics\gentele2.jpg",1,False)
			TeleportTexture(3)=MyLoadTexture ("graphics\gentele3.jpg",1,False)
			TeleportTexture(4)=MyLoadTexture ("graphics\gentele4.jpg",1,False)
			TeleportTexture(5)=MyLoadTexture ("graphics\gentele5.jpg",1,False)
			TeleportTexture(6)=MyLoadTexture ("graphics\gentele6.jpg",1,False)
			TeleportTexture(7)=MyLoadTexture ("graphics\gentele7.jpg",1,False)
		EndIf

		


		
		
	EndIf







	;MapBackground=6;Rand(0,3)		
	; Background
	If MapBackground=0
		;sky
		BackGroundTexture=MyLoadTexture ("res27.dat",1,False)
		ScaleTexture BackGroundTexture,50,50
	EndIf
	If MapBackground=1
		;forest
		BackGroundTexture=MyLoadTexture ("graphics\grass4.jpg",1,False)
		ScaleTexture BackGroundTexture,50,50
	EndIf
	
	If MapBackground=5 Or MapBackGround=6
		;water/lava
	
	EndIf
	If MapBackground=7
		;sky
		BackGroundTexture=MyLoadTexture ("graphics\warp.jpg",1,False)
		ScaleTexture BackGroundTexture,.25,.25
	EndIf
	
	
	If MapBackground=11
		
		;custom
		If FileType("custombackgrounds\"+custombackdir$+"\back.jpg")=1
			BackGroundTexture=MyLoadTexture ("custombackgrounds\"+custombackdir$+"\back.jpg",1,False)
			ScaleTexture BackGroundTexture,50,50
		Else
			MapBackGround=3
		EndIf
	EndIf	
	
	If MapBackground=3
		;stars
		BackGroundTexture=MyLoadTexture ("graphics\stars.jpg",1,False)
		ScaleTexture BackGroundTexture,50,50
	EndIf

	
	If MapBackground=8
		;stars
		BackGroundTexture=MyLoadTexture ("graphics\city.jpg",1,False)
		ScaleTexture BackGroundTexture,50,50
	EndIf	


	If MapBackground=9 Or MapBackground=10
		;stars
		BackGroundTexture=MyLoadTexture ("graphics\rainbow.jpg",1,False)
		ScaleTexture BackGroundTexture,50,50
	EndIf





	
	



End Function