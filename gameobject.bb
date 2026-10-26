

; -------------------------------------------
; Players
; -------------------------------------------

Function CreatePlayer(i,x,y)
	; input tile position x/y
	
	PlayerExists(i)=True
	
	PlayerEntity(i)=CopyEntity(PlayerMesh(i)) 
	PositionEntity PlayerEntity(i),x+0.5,0,-(y+0.5)	
	
	RotateEntity PlayerEntity(i),0,180,0
	PlayerRotation(i)=180
	PlayerRotationGoal(i)=180

	PlayerShadowEntity(i)=CreateFlatSquare(RoundShadowTexture)
	ScaleEntity PlayerShadowEntity(i),.55,1,.55
	PositionEntity PlayerShadowEntity(i),x+.25,0.01,-(y+.25)

	PlayerX(i)=x
	PlayerY(i)=y
	PlayerX2(i)=x
	PlayerY2(i)=y
	PlayerMoving(i)=False
	
	PlayerAnimCurrent(i)=0
	PlayerAnimNew(i)=0
	PlayerIdleCounter(i)=0
	
	
	PlayerMoving(i)=False
	PlayerDead(i)=False
	PlayerDying(i)=False
	PlayerDyingTimer(i)=0	
	
	PlayerJumping(i)=False
	PlayerJumpingDistance(i)=1
	
	PlayerBlinkCounter(i)=370
	PlayerBeforeBlinkTexture(i)=PlayerTexture(i)
	
	FocusOnPlayer(i)=False
	
	PlayerView3dRotation(i)=2

	
End Function

Function CanPlayerMove(k,dx,dy)

	If PlayerDying(k)=True Then Return True

	; tests a field to see if Loofcan go there
	x=PlayerX(k)+dx
	y=PlayerY(k)+dy
	
	; edge of level
	If x<0 Or y<0 Or x>=MapWidth Or y>=MapHeight Then Return False

	
	; First check tiles
	
	Tile=MapTile(x,y)
	
	If (Tile/100=3 Or Tile/100=9) And PlayerMoving(k)=True ; moving into water/lava (such as from ice)
		KillPlayer(k)
		Return False
	EndIf
	
	If tile/100=17 And tile>1702
		; moving transporter
		Return False
	EndIf
	
	If Tile/100=0 Or Tile/100=2 Or Tile/100=3 Or Tile/100=8 Or Tile/100=9	Or (Tile/100=10 And Tile Mod 100<50)
		; Walls, Water, Cannons, Lava, Gates - Definitely Cannot Enter
		Return False
	EndIf
	
	; check other players
	For j=1 To 4
		If PlayerExists(j)=True And (BothCompleted And 2^(j-1))=0 And ((PlayerX(j)=x And PlayerY(j)=y) Or (PlayerX2(j)=x And PlayerY2(j)=y))
			Return False
		EndIf	
	Next
	
	; Finally check boxes
	For i=0 To NofBoxes-1
		If (BoxX(i)=x And BoxY(i)=y) Or (BoxX2(i)=x And BoxY2(i)=y)
			; yep found a box
			; is it moving
			If BoxMoving(i)=True
				; box moving away from Loof: no problem [LATER: or sideways at end of movement]
				If BoxMovingX(i)=dx And BoxMovingY(i)=dy 
					PlayerAnimNew(k)=1
					Return True
				EndIf				
				; box moving towards Loof: boom!
				If BoxMovingX(i)=-dx And BoxMovingY(i)=-dy
					KillPlayer(k)
					Return False
				EndIf
				; box moving sideways: just bounce Loofoff
			
				Return False
			EndIf
			; not a moving box: check if the box can be moved further
			If (MapTile(BoxX(i),BoxY(i))/100<>5 Or (MapTile(BoxX(i),BoxY(i)) Mod 100) >=8 Or ConveyorActive(1)=False) And (MapTile(BoxX(i),BoxY(i))/100<>5 Or (MapTile(BoxX(i),BoxY(i)) Mod 100) <8 Or ConveyorActive(2)=False)	; don't even check if box on moving conveyor
				If CanBoxMove(i,dx,dy)=True 
					; move it
					; check if Loofon ice, if so move box faster
					If MapTile(PlayerX(k),PlayerY(k))/100=4 Or (MapTile(PlayerX(k),PlayerY(k))/100=5 And (MapTile(PlayerX(k),PlayerY(k)) Mod 100) <8 And ConveyorActive(1)=True) Or (MapTile(PlayerX(k),PlayerY(k))/100=5 And (MapTile(PlayerX(k),PlayerY(k)) Mod 100) >=8 And ConveyorActive(2)=True)
						MoveBox(i,dx,dy,2)
						
					Else
						MoveBox(i,dx,dy,0)
					EndIf
					; and move Loof
					PlayerAnimNew(k)=2
					
					; and finally, check if it is a linksphere. If so, move all of the same type
					If BoxType(i)>=9 And BoxType(i)<=12
						For j=0 To NofBoxes-1
							If i<>j And BoxType(i)=BoxType(j) And BoxX(i)<>101
								; found other linksphere
								; don't move if already moving
								If BoxMoving(j)=False
									If (MapTile(BoxX(j),BoxY(j))/100<>5 Or (MapTile(BoxX(j),BoxY(j)) Mod 100) >=8 Or ConveyorActive(1)=False) And (MapTile(BoxX(j),BoxY(j))/100<>5 Or (MapTile(BoxX(j),BoxY(j)) Mod 100) <8 Or ConveyorActive(2)=False)	; don't even check if box on moving conveyor

										If CanBoxMove(j,dx,dy)=True
											MoveBox(j,dx,dy,0)
										EndIf
									EndIf
								EndIf
							EndIf
						Next
					EndIf
					Return True
				Else 
					; box can't be moved, so neither can Loof
					Return False
				EndIf
			Else 
				Return False
			EndIf
		EndIf
	Next
	
	;Default: Move	
	If (MapTile(PlayerX(k),PlayerY(k))/100=5 And (MapTile(PlayerX(k),PlayerY(k)) Mod 100) <8 And ConveyorActive(1)=True) Or (MapTile(PlayerX(k),PlayerY(k))/100=5 And (MapTile(PlayerX(k),PlayerY(k)) Mod 100) >=8 And ConveyorActive(2)=True) Or MapTile(PlayerX(k),PlayerY(k))/100=4

		PlayerAnimNew(k)=0
	Else
		PlayerAnimNew(k)=1
	EndIf
	Return True
		
End Function

Function MovePlayer(k,dx,dy)
	PlayerIdleCounter(k)=0
	EntityTexture PlayerEntity(k),PlayerTexture(k)
	If MD2Animating(PlayerEntity(k))=False Or PlayerAnimCurrent(k)<>PlayerAnimNew(k)
		If PlayerAnimNew(k)=0 ; nothing
			AnimateMD2 PlayerEntity(k),0,1,0,0
		Else If PlayerAnimNew(k)=1 ; normal walk
			AnimateMD2 PlayerEntity(k),1,.25,2,18
		Else If PlayerAnimNew(k)=2 ; pushing walk
			AnimateMD2 PlayerEntity(k),1,.25,19,35
		EndIf
		PlayerAnimCurrent(k)=PlayerAnimNew(k)
	EndIf

	If MapTile(PlayerX(k),Playery(k))/100=4
		;ice
		PlayMySound(SFXIce,PlayerEntity(k))
	EndIf	
		
	; Speed depends on Terrain
	If MapTile(PlayerX(k),PlayerY(k))/100=4 Or (MapTile(PlayerX(k),PlayerY(k))/100=5 And (MapTile(PlayerX(k),PlayerY(k)) Mod 100) <8 And ConveyorActive(1)=True) Or (MapTile(PlayerX(k),PlayerY(k))/100=5 And (MapTile(PlayerX(k),PlayerY(k)) Mod 100) >=8 And ConveyorActive(2)=True) ; Ice or Conveyor		
		PlayerSpeed(k)=BaseSpeed/2
	Else
		PlayerSpeed(k)=BaseSpeed
	EndIf
	
	

	PlayerMovingTimer(k)=PlayerSpeed(k)
	PlayerX2(k)=PlayerX(k)+dx
	PlayerY2(k)=PlayerY(k)+dy
	
	PlayerMoving(k)=True
	PlayerMovingX(k)=dx
	PlayerMovingY(k)=dy
	
	If dx=0
		If dy=1	
			PlayerRotationGoal(k)=180
		Else 
			PlayerRotationGoal(k)=0
		EndIf
	Else
		If dx=1
			PlayerRotationGoal(k)=270
		Else 
			PlayerRotationGoal(k)=90
		EndIf
	EndIf
	
	; now check if shadowstinker exists
	For i=0 To nofmonsters-1
		If monstertype(i)=12+k Or monstertype(i)=16+k
		 	If monstermoving(i)=False
			;	monsterspeed(i)=playerspeed(k)
				If canmonstermove(i,-dx,-dy)=True Then movemonster(i,-dx,-dy)
			EndIf
		EndIf
	Next


End Function

Function EndMovePlayer(k)
	
	OldX=PlayerX(k)
	OldY=PlayerY(k)
	OldTile=MapTile(PlayerX(k),PlayerY(k)) ;what did Loofused to stand on?
									 ; used e.g. for breakawaytiles
	PlayerX(k)=PlayerX2(k)
	PlayerY(k)=PlayerY2(k)
	
	
	If Endmoveplayertrampoline=False
		If OldTile/100=5 And ((OldTile-500) Mod 8) >=4
		
			
			; Switcher Tiles 
			
			MapTile(OldX,OldY)=MapTile(OldX,OldY)+2
			If (MapTile(OldX,OldY)-100) Mod 8 <=4 Then MapTile(OldX,OldY)=MapTile(OldX,OldY)-4
			KillSwitcher(OldX,OldY)
			CreateSwitcher(OldX,OldY,MapTile(OldX,OldY) Mod 4 + (Maptile(OldX,Oldy)>=508)*4 )
	
			
			
		EndIf
	
		
		If OldTile/100=6
			; Breakaway Tiles - look at what Loofused to stand on
			
			If OldTile=600	
				; will break away into water
				; convert logic to water
				MapTile(OldX,OldY)=300
				; and destroy that tile
				KillBreakAway(OldX,OldY)
			Else If OldTile=603
				; will break into lava
				MapTile(OldX,OldY)=900
				KillBreakAway(OldX,OldY)
			Else If OldTile=606
				; will break into nothing
				MapTile(OldX,OldY)=0
				KillBreakAway(OldX,OldY)
			Else
				; reduce by one
				KillBreakAway(OldX,OldY)
				MapTile(OldX,OldY)=OldTile-1
				CreateBreakAway(OldX,OldY,OldTile-601)
			EndIf
			
		EndIf
		If OldTile/100=11
			; Button Tiles 
			StepOffButton(OldX,OldY)
		EndIf
		; Signs
		For i=0 To 19
			If OldX=SignX(i) And OldY=SignY(i)
				If SignActive=k Then StopSign(11+i)
			EndIf
		Next 
	EndIf
	
		
	; now check what tile its standing on now
	Tile=MapTile(PlayerX(k),PlayerY(k))
	If Tile/100=4 
		
		; Ice, keep sliding if possible 
		
		If OldTile/100<>4 ; first time? Play sound
			i=Rand(0,4)
			If DoesSoundExist=True Then SoundPitch SFXSLIce(i),19000
			PlayMySound(SFXSlIce(i),PlayerEntity(k))
		EndIf
		
		
		; first, figure out what kind of ice (curved, straight) to find forward/back directions
		
		If Tile=400	; straight ice
			; straight forward
			ForwardX=PlayerMovingX(k)
			ForwardY=PlayerMovingY(k)
			BackwardX=-PlayerMovingX(k)
			BackwardY=-PlayerMovingY(k)
		Else If Tile=401 ; down-right ice
			If PlayerMovingX(k)=-1 ; coming from right
				ForwardX=0
				ForwardY=1
				BackWardX=1
				BackWardY=0
			Else				; coming from below
				ForwardX=1
				ForwardY=0
				BackWardX=0
				BackWardY=1
			EndIf
		Else If Tile=402 ; down-left ice
			If PlayerMovingX(k)=1  ; coming from left
				ForwardX=0
				ForwardY=1
				BackWardX=-1
				BackWardY=0
			Else				; coming from below
				ForwardX=-1
				ForwardY=0
				BackWardX=0
				BackWardY=1
			EndIf
		Else If Tile=403 ; up-left ice
			If PlayerMovingX(k)=1 ; coming from left
				ForwardX=0
				ForwardY=-1
				BackWardX=-1
				BackWardY=0
			Else				; coming from above
				ForwardX=-1
				ForwardY=0
				BackWardX=0
				BackWardY=-1
			EndIf
		Else If Tile=404 ; up-right ice
			If PlayerMovingX(k)=-1 ; coming from right
				ForwardX=0
				ForwardY=-1
				BackWardX=1
				BackWardY=0
			Else				; coming from above
				ForwardX=1
				ForwardY=0
				BackWardX=0
				BackWardY=-1
			EndIf
		EndIf
	

		; now do the check and movement
		If CanPlayerMove(k,ForwardX,ForwardY)=True
			MovePlayer(k,ForwardX,ForwardY)
			Return
		; can't move forward, can we move back?
		Else If CanPlayerMove(k,BackwardX,BackwardY)=True
			MovePlayer(k,BackwardX,BackwardY)
			Return
		Else
			; couldn't move further... this should never happen!
			KillPlayer(k) ;but just in case
			Return
		EndIf
		
			
	EndIf
	
	If (Tile/100=5 And (Tile Mod 100)<8 And ConveyorActive(1)=True) Or (Tile/100=5 And (Tile Mod 100)>=8 And ConveyorActive(2)=True)
		; Conveyor Belts: move if possible (no bouncing or anything,
		;	anything that collides with an immovable is killed)
		
		If Tile<>OldTile ; first time this direction
			PlayMySound(SFXPlayerConvey(k),PlayerEntity(k))
		EndIf
		
		Tile= 500+(Tile Mod 4)

		
		If Tile=500	; up
			If CanPlayerMove(k,0,-1)=True 
				MovePlayer(k,0,-1)
				Return
			Else
				KillPlayer(k)
				Return
			EndIf
		EndIf
		If Tile=502	; down
			If CanPlayerMove(k,0,1)=True 
				MovePlayer(k,0,1)
				Return
			Else
				KillPlayer(k)
				Return

			EndIf
		EndIf
		If Tile=501	; right
			If CanPlayerMove(k,1,0)=True 
				MovePlayer(k,1,0)
				Return
			Else
				KillPlayer(k)
				Return

			EndIf
		EndIf
		If Tile=503	; left
			If CanPlayerMove(k,-1,0)=True 
				MovePlayer(k,-1,0)
				Return
			Else
				KillPlayer(k)
				Return

			EndIf
		EndIf
		


	
	EndIf

	If Tile/100=11	; buttons
		StepOnButton(PlayerX(k),PlayerY(k))
	EndIf	
	
	If Tile/100=12 	; Teleporter
		PlayerMoving(k)=False	; do first to avoid 'death in water'
		If PlayerStepOnTeleporter(k,PlayerX(k),PlayerY(k),PlayerMovingX(k),PlayerMovingY(k))=True
			Return
		EndIf
	EndIf
	
	; Signs
	For i=0 To 19
		If PlayerX(k)=SignX(i) And PlayerY(k)=SignY(i)
			If FocusOnPlayer(k)=True Then StartSign(SignText$(i),8,11+i,1)
		EndIf
	Next 

	
	If Tile/100=17 ; Transporter
		If CanTransporterMove(PlayerX(k),PlayerY(k),PlayerMovingX(k),PlayerMovingY(k))
			MovePlayer(k,PlayerMovingX(k),PlayerMovingY(k))
			MoveTransporter(PlayerX(k),PlayerY(k),PlayerMovingX(k),PlayerMovingY(k))
			PlayerAnimNew(k)=0
			AnimateMD2 PlayerEntity(k),0,1,0,0
			
			PlayMySound(SFXTransporter,PlayerEntity(k))
			Return
		EndIf
	EndIf		
	
	If Tile/100=18 ; Trampoline
		PlayerJumpingDistance(k)=PlayerJumpingDistance(k)+1
		PlayerJumping(k)=True
		PlayerMoving(k)=True
		PlayerMovingTimer(k)=(BaseSpeed)*PlayerJumpingDistance(k)
		PlayerSpeed(k)=(BaseSpeed)
		PlayerJumpingSpeed(k)=PlayerMovingTimer(k)
		
		PlayMySound(SFXTrampoline,PlayerEntity(k))
		
		If playerjumpingdistance(k)=6 Or playerjumpingdistance(k)=9
			PlayMySound(SFXpBounce(k,3),PlayerEntity(k))
		Else If playerjumpingdistance(k)=12
			PlayMySound(SFXpBounce(k,4),PlayerEntity(k))
		Else
			PlayMySound(SFXpBounce(k,Rand(1,2)),PlayerEntity(k))
		EndIf
		
		AnimateMD2 PlayerEntity(k),3,0.2,36,42
		PlayerAnimNew(k)=3
		PlayerAnimCurrent(k)=3
		
		If FocusOnPlayer(k)=True And PlayerJumpingDistance(k)>2
			If KeyDown(200) Or KeyDown(72)
				PlayerMovingX(k)=0
				PlayerMovingY(k)=-1
			EndIf
			If KeyDown(208) Or KeyDown(80)
				PlayerMovingX(k)=0
				PlayerMovingY(k)=1
			EndIf
			If KeyDown(203) Or KeyDown(75)
				PlayerMovingX(k)=-1
				PlayerMovingY(k)=0
			EndIf
			If KeyDown(205) Or KeyDown(77)
				PlayerMovingX(k)=1
				PlayerMovingY(k)=0
			EndIf
		EndIf
		
		dx=PlayerMovingX(k)
		dy=PlayerMovingY(k)
		
		If dx=0
			If dy=1	
				PlayerRotationGoal(k)=180
			Else 
				PlayerRotationGoal(k)=0
			EndIf
		Else
			If dx=1
				PlayerRotationGoal(k)=270
			Else 
				PlayerRotationGoal(k)=90
			EndIf
		EndIf



		
		PlayerJumpingX(k)=PlayerX(k)+PlayerJumpingDistance(k)*PlayerMovingX(k)
		PlayerJumpingY(k)=PlayerY(k)+PlayerJumpingDistance(k)*PlayerMovingY(k)
		PlayerX(k)=101
		PlayerY(k)=101
		PlayerX2(k)=101
		PlayerY2(k)=101
		
		Return
	Else
		PlayerJumpingDistance(k)=1
	EndIf
		
			
	
	PlayerMoving(k)=False
	
	
End Function

Function ControlPlayer(k)
	;animations

	; turning into walk-direction
	; note: actual turning is taken care of in boxcontrol
	If PlayerRotation(k)<>PlayerRotationGoal(k)
		If PlayerRotation(k)<PlayerRotationGoal(k) And PlayerRotationGoal(k)-PlayerRotation(k)<180
			PlayerRotation(k)=PlayerRotation(k)+5
			
		Else If PlayerRotation(k)>PlayerRotationGoal(k) And PlayerRotation(k)-PlayerRotationGoal(k)>180
			PlayerRotation(k)=PlayerRotation(k)+5
			
	  	Else
			PlayerRotation(k)=PlayerRotation(k)-5
			
		EndIf
		If PlayerRotation(k)=360 Then PlayerRotation(k)=0
		If PlayerRotation(k)=-5 Then PlayerRotation(k)=355
	EndIf
	
	If playerjumping(k)=True
		; in-flight trampoline
		If EntityX(PlayerEntity(k))-0.5<0 Or EntityZ(PlayerEntity(k))+0.5>0 Or EntityX(PlayerEntity(k))+0.5>MapWidth Or EntityZ(PlayerEntity(k))-0.5<-MapHeight
			KillPlayer(k)
			PlayerMoving(k)=False
			PlayerJumping(k)=False
		EndIf
	EndIf
			
End Function


Function KillPlayer(k)
		
	If PlayerExists(k)=False Return
	If PlayerDying(k)=True Return
	If LevelCompleted>0 Return
	
	If PlayerExists(k)=True And (BothCompleted And 2^(k-1))>0
		Return
	EndIf


	
	If PlayerDying(1)+PlayerDying(2)+PlayerDying(3)+PlayerDying(4)+PlayerDead(1)+PlayerDead(2)+PlayerDead(3)+PlayerDead(4)=0 Then PlayMySound(SFXDeath,0)
	
	AnimateMD2 PlayerEntity(k),3,0.2,36,42
	EntityTexture PlayerEntity(k),PlayerTextureB(k)
	PlayerAnimCurrent(k)=3
		
	PlayerX(k)=0
	PlayerY(k)=0
	
	PlayerDyingTimer(k)=100
	PlayerDying(k)=True
	
	While FocusOnPlayer(k)=False
		SwitchPlayer(0)
	Wend
	
	
	
	FreeEntity PlayerShadowEntity(k)

End Function

Function SwitchPlayer(sound)
	
	; first find out if there's someone to switch to
	flag=False
	For i=1 To 4
		If focusonplayer(i)=True
			currentfocus=i
			oldfocus=i
		EndIf
		If FocusOnPlayer(i)=False And PlayerExists(i)=True And (BothCompleted And 2^(i-1))=0
			flag=True
		EndIf
	Next
	If flag=False Then Return
			
	
	If LevelTimer<160 And sound>0 Then Return	; no switching while zoom in or one already out

	
	
	
	; switches Control and Camera on Stinky and Loof
	For i=1 To 4
		CurrentFocus=CurrentFocus+1
		If CurrentFocus=5 Then CurrentFocus=1
		If FocusOnPlayer(CurrentFocus)=False And PlayerExists(CurrentFocus)=True And (BothCompleted And 2^(CurrentFocus-1))=0
			Exit
		EndIf
	Next
	
	If sound=True
		
		PlayMySound(SFXTalk(10+CurrentFocus),0)
	Else
		PlayMySound(SFXTeleport,0)
	EndIf
	
	
	FocusOnPlayer(CurrentFocus)=True
	FocusOnPlayer(OldFocus)=False
		
			
	If FocusSwitchTimer<60 Then FocusSwitchTimer=20
	; also move camera
	If view3d=False
		For i=1 To 4
			CameraDX=EntityX(PlayerEntity(currentfocus))-EntityX(Camera)
			CameraDZ=(EntityZ(PlayerEntity(currentfocus))-9)-EntityZ(Camera)
		Next
	Else
		CameraDX=EntityX(PlayerEntity(currentfocus))-EntityX(PlayerEntity(oldfocus))
		CameraDZ=EntityZ(PlayerEntity(currentfocus))-EntityZ(PlayerEntity(oldfocus))
	EndIf

		
	
	; and handle signs
	For i=11 To 30
		StopSign(i)
	Next
	CheckSigns()
	
	If view3d=True
		
		HideEntity playerentity(currentfocus)
		ShowEntity playerentity(oldfocus)
		
		playerview3drotation(oldfocus)=view3drotation
		
		view3drotation =playerview3drotation(currentfocus)
			


	EndIf
	
	
	


End Function



Function EndTrampoline(what,who)
	If what=0 ; player
		x=PlayerX(who)
		y=PlayerY(who)
	Else ; box
		x=BoxX(who)
		y=BoxY(who)
	EndIf
	
	tile=maptile(x,y)
	tile2=tile/100
	
	If what=0 
		;tiles
		If (tile2=0 Or tile2=2 Or tile2=3 Or tile2=8 Or tile2=9 Or tile2=16 Or tile2=10 Or (tile2=17 And tile>1702) Or (Tile2=10 And Tile Mod 100<50))
			TranslateEntity PlayerEntity(who),0,0.5,0
			; smack!
			killplayer(who)
			Return
		EndIf
		;boxes 
		For i=0 To nofboxes-1
			If (x=BoxX(i) And y=BoxY(i)) Or (x=BoxX2(i) And y=BoxY2(i))
				TranslateEntity PlayerEntity(who),0,0.5,0
				killplayer(who)
				killbox(i)
				Return
			EndIf
		Next
		; monsters
		For i=0 To nofmonsters-1
			If (x=MonsterX(i) And Y=MonsterY(i)) Or (x=MonsterX2(i) And y=MonsterY2(i))
				TranslateEntity PlayerEntity(who),0,0.5,0
				killplayer(who)
				killmonster(i)
				Return
			EndIf
		Next
		; other players
		For j=1 To 4
			If j<>who And PlayerExists(j)=True And (BothCompleted And 2^(j-1))=0 And ((PlayerX(j)=x And PlayerY(j)=y) Or (PlayerX2(j)=x And PlayerY2(j)=y))
				TranslateEntity PlayerEntity(who),0,0.5,0
				killplayer(who)
				killplayer(j)
				Return 
			EndIf
		Next
		; items
		For i=0 To nofitems-1
			If x=ItemX(i) And y=ItemY(i)
				pickupitem(i,who)
			EndIf
		Next
		
	EndIf
	
	If what=1 
		;tiles
		If (tile2=0 Or tile2=2 Or tile2=8 Or tile2=16 Or tile2=10 Or (tile2=17 And tile>1702) Or (Tile2=10 And Tile Mod 100<50))
			killBox(who)
			Return
		EndIf
		;boxes 
		For i=0 To nofboxes-1
			If who<>i And ((x=BoxX(i) And y=BoxY(i)) Or (x=BoxX2(i) And y=BoxY2(i)))
			
				If boxtype(i)<>8 And boxtype(who)<>8 Then killBox(who)
				killbox(i)
				Return
			EndIf
		Next
		; monsters
		For i=0 To nofmonsters-1
			If (x=MonsterX(i) And Y=MonsterY(i)) Or (x=MonsterX2(i) And y=MonsterY2(i))
				
				If monstertype(i)<>9 
					killmonster(i)
					killBox(who)
				EndIf
				Return
			EndIf
		Next
		; other players
		For j=1 To 4
			If PlayerExists(j)=True And (BothCompleted And 2^(j-1))=0 And ((PlayerX(j)=x And PlayerY(j)=y) Or (PlayerX2(j)=x And PlayerY2(j)=y))
				killbox(who)
				killplayer(j)
				Return 
			EndIf
		Next
		; items
		For i=0 To nofitems-1
			If x=ItemX(i) And y=ItemY(i)
				killbox(who)
			EndIf
		Next
		
	EndIf


End Function

; -------------------------------------------
; Boxes
; -------------------------------------------

Function CreateBox(x,y,btype)
	; input tile position x/y
	
	;btype	0-wood
	;		1-steel
	;		2/3-reflector
	;		4-boulder
	;		5-plasma
	;		6-keg
	;		7-prism
	;		8-sticky
	;		9/10/11/12-linksphere
	;		100-115-pushcannon
	
	If NofBoxes>=MaxBoxes
		Return
	EndIf
	
	If btype=0
		BoxEntity(NofBoxes)=CopyEntity(WoodBoxMesh) 
	Else If btype=1
		BoxEntity(NofBoxes)=CopyEntity(SteelBoxMesh)
	Else If btype=2 Or btype=3
		BoxEntity(NofBoxes)=CopyEntity(ReflectorMesh)
		If btype=3 Then RotateEntity BoxEntity(NofBoxes),0,90,0	
	Else If btype=4	
		BoxEntity(NofBoxes)=CopyEntity(BoulderMesh)
	Else If btype=5
		BoxEntity(NofBoxes)=CopyEntity(GhostBoxMesh)
	Else If btype=6
		BoxEntity(NofBoxes)=CopyEntity(BoomBoxMesh)
	Else If btype=7
		BoxEntity(NofBoxes)=CopyEntity(PrismMesh)
	Else If btype=8
		BoxEntity(NofBoxes)=CreateCube()
		ScaleEntity BoxEntity(NofBoxes),.45,.45,.45
		EntityTexture BoxEntity(NofBoxes),StickyCubeTexture
;		EntityColor BoxEntity(NofBoxes),255,100,255
		EntityAlpha BoxEntity(NofBoxes),.5
	Else If btype=9 
		BoxEntity(NofBoxes)=CreateSphere()
		ScaleEntity BoxEntity(NofBoxes),0.45,0.45,0.45
		EntityTexture BoxEntity(NofBoxes),LinkSphereRedTexture
	;	EntityAlpha BoxEntity(NofBoxes),0.8
	Else If btype=10 
		BoxEntity(NofBoxes)=CreateSphere()
		ScaleEntity BoxEntity(NofBoxes),0.45,0.45,0.45

		EntityTexture BoxEntity(NofBoxes),LinkSphereYellowTexture


	;	EntityAlpha BoxEntity(NofBoxes),0.8
	Else If btype=11 
		BoxEntity(NofBoxes)=CreateSphere()
		ScaleEntity BoxEntity(NofBoxes),0.45,0.45,0.45

		EntityTexture BoxEntity(NofBoxes),LinkSphereGreenTexture

	;	EntityAlpha BoxEntity(NofBoxes),0.8
	Else If btype=12 
		BoxEntity(NofBoxes)=CreateSphere()
		ScaleEntity BoxEntity(NofBoxes),0.45,0.45,0.45

		EntityTexture BoxEntity(NofBoxes),LinkSphereBlueTexture

	;	EntityAlpha BoxEntity(NofBoxes),0.8
	Else If btype>=100
		BoxEntity(NofBoxes)=CopyEntity(CannonMesh)
		EntityTexture BoxEntity(NofBoxes),PushCannonTexture
		
		If (btype-100)/4 = 0 
			dx=0
			dy=-1
		EndIf
		If (btype-100)/4 = 1 
			dx=1
			dy=0
		EndIf
		If (btype-100)/4 = 2 
			dx=0
			dy=1
		EndIf
		If (btype-100)/4 = 3 
			dx=-1
			dy=0
		EndIf
		speed=(btype Mod 4)+1
			
		BoxCannonMovingX(NofBoxes)=dx
		BoxCannonMovingY(NofBoxes)=dy
		
		BoxCannonSpeed(NofBoxes)=speed
		If BoxCannonMovingX(NofBoxes)<>0
			TurnEntity BoxEntity(NofBoxes),0,0,(90*(2-BoxCannonMovingX(NofBoxes)))
		EndIf
		If BoxCannonMovingY(NofBoxes)<>0
			TurnEntity BoxEntity(NofBoxes),0,0,(90*(BoxCannonMovingY(NofBoxes)+1))
		EndIf
			
		
		
		BoxCannonTimer(NofBoxes)=(speed+1)*CannonBaseTimer


	EndIf
	BoxType(NofBoxes)=btype
	
	If btype<100	
		PositionEntity BoxEntity(NofBoxes),x+0.5,0.4,-(y+0.5)
	Else
		PositionEntity BoxEntity(NofBoxes),x+.5,0,-y-.5	
	EndIf
	
	BoxX(NofBoxes)=x
	BoxY(NofBoxes)=y
	BoxX2(NofBoxes)=x
	BoxY2(NofBoxes)=y
	BoxMoving(NofBoxes)=False
	BoxDead(NofBoxes)=False
	BoxJumping(NofBoxes)=False
	BoxJumpingDistance(NofBoxes)=1
	
	BoxSwayingTimer(NofBoxes)=-1
	
	;BoxShadowEntity(NofBoxes)=CopyEntity(BoxShadowMesh,BoxEntity(NofBoxes))
	;ScaleEntity BoxShadowEntity(NofBoxes),2.5,1,2.5
	;TranslateEntity BoxShadowEntity(NofBoxes),-.9,-.999,.9
			
	NofBoxes=NofBoxes+1
	
	
	
	

End Function

Function CanBoxMove(i,dx,dy)
	
	; can't move if already moving
	;If BoxMoving(i)=True
	;	Return False
	;EndIf
	
	; tests a field to see if the box can go there
	x=BoxX(i)+dx
	y=BoxY(i)+dy
	
	; edge of level
	If x<0 Or y<0 Or x>=MapWidth Or y>=MapHeight Then Return False

	
	; First check tiles
	Tile=MapTile(x,y)
	If Tile/100=0 Or Tile/100=2 Or Tile/100=6	Or Tile/100=8 Or (Tile/100=10 And Tile Mod 100<50)
		; Walls, BreakAways, Cannons, Gates - Definitely Cannot Enter
		If BoxType(i)=6 Then KillBox(i)
		Return False
		
	EndIf
	
	; Now check boxes 
	; if the box we're running into is stationary, then push that away (if possible)
	; exception: wood box can't push steel box
	; if the box is moving, kill both boxes
	For j=0 To NofBoxes-1
		If ((BoxX(j)=x And BoxY(j)=y) Or (BoxX2(j)=x And BoxY2(j)=y)) And j<>i
			; yep found a box 
			; if my box is not moving: no power, can't move
			If BoxMoving(i)=False
				If BoxType(i)=6 Then KillBox(i)
				Return False
			EndIf

			; is the other box stationary and our box moving
			If BoxMoving(j)=False
				If BoxType(i)=6 Then KillBox(i)

				
				;depends on type
				If BoxType(i)=4 Or (BoxType(i)>=9 And BoxType(i)<=12); rolling boulder onto something else
					If BoxType(j)=0 Or BoxType(j)=6 ; onto wood - destroy
						KillBox(j)
						Return True
					Else If BoxType(j)=4 Or (BoxType(j)>=9 And BoxType(j)<=12); onto other ball - roll that if possible
						If CanBoxMove(j,dx,dy)=True
							If MapTile(BoxX(i),BoxY(i))/100 = 4 Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i)) Mod 100)<8 And ConveyorActive(1)=True) Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i)) Mod 100)>=8 And ConveyorActive(2)=True)
								; yes, move other box faster a
								MoveBox(j,dx,dy,2)
							Else
								MoveBox(j,dx,dy,0)
							EndIf
							Return False
						Else
							Return False
						EndIf
						

					Else	; onto anything else - stopped
						Return False
					EndIf
							
				
				; now more general cases
				Else If ((BoxType(i)>=1 And BoxType(i)<>6) And (BoxType(j)=0 Or BoxType(j)=6)) Or ((BoxType(i)=4 Or BoxType(i)=5 Or (BoxType(i)>=9 And BoxType(i)<=12)) And (BoxType(j)=1 Or BoxType(j)=2 Or BoxType(j)=3 Or BoxType(j)=7)) Or (BoxType(i)=5 And BoxType(j)=4)
					; Stronger onto Weaker
					If BoxType(j)=6 Then KillBox(j)
					; pushes or kills wood box, and definitely moves itself
					If CanBoxMove(j,dx,dy)=True
						; yes, move other box: check if on ice or conveyor
						If MapTile(BoxX(i),BoxY(i))/100 = 4 Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i)) Mod 100)<8 And ConveyorActive(1)=True) Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i)) Mod 100)>=8 And ConveyorActive(2)=True)
							; yes, move other box faster a
							MoveBox(j,dx,dy,2)
						Else
							MoveBox(j,dx,dy,0)
						EndIf
					Else
						; no, just kill other box and move on
						KillBox(j)
					EndIf
					Return True
				
				
				Else If BoxType(i)=BoxType(j) Or ((BoxType(i)=2 Or BoxType(i)=3 Or BoxType(i)=7) And (BoxType(j)=3 Or BoxType(j)=2 Or BoxType(j)=7)) Or (BoxType(i)=1 And (BoxType(j)=2 Or BoxType(j)=3 Or BoxType(j)=7)) Or (BoxType(j)=1 And (BoxType(i)=2 Or BoxType(i)=3 Or BoxType(i)=7)) Or ((BoxType(i)=1 Or BoxType(i)=2 Or BoxType(i)=3) And (BoxType(j)=4 Or (BoxType(j)>=9 And BoxType(j)<=12)))
					; same strength
					If BoxType(j)=6 Then KillBox(j)
					; pushes other box if possible, if not it can't move 
					If CanboxMove(j,dx,dy)=True
						; yes, move other box: check if on ice or conveyor
						If MapTile(BoxX(i),BoxY(i))/100 = 4 Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i)) Mod 100)<8 And ConveyorActive(1)=True) Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i)) Mod 100)>=8 And ConveyorActive(2)=True)
							; yes, move other box faster 
							MoveBox(j,dx,dy,2)
							Return True
						Else
							MoveBox(j,dx,dy,0)
							Return True
						EndIf
					Else
						; no- can't move
						Return False
						
					EndIf
					
				Else 	
					; weaker on stronger
					If BoxType(j)=6 Then KillBox(j)
					
					; can't move
					Return False
				EndIf
				
				
			Else	
				; both boxes moving
			
				; Box moving away: no interaction
				If dx=BoxMovingX(j) And dy=BoxMovingY(j)
					Return True	; no problem
				Else
					; box moving into eachother head-on or sideways
					If ((BoxType(i)>=1 And BoxType(i)<>6) And (BoxType(j)=0 Or BoxType(j)=6)) Or ((BoxType(i)=4 Or BoxType(i)=5 Or (BoxType(i)>=9 And BoxType(i)<=12)) And (BoxType(j)=1 Or BoxType(j)=2 Or BoxType(j)=3 Or BoxType(j)=7)) Or (BoxType(i)=5 And (BoxType(j)=4 Or (BoxType(j)>=9 And BoxType(j)<=12)))
		
						; Stronger onto Weaker
						KillBox(j)
						Return True ; since stronger can keep moving
					Else If BoxType(i)=BoxType(j) Or ((BoxType(i)=2 Or BoxType(i)=3 Or BoxType(i)=7) And (BoxType(j)=3 Or BoxType(j)=2 Or BoxType(j)=7)) Or (BoxType(i)=1 And (BoxType(j)=2 Or BoxType(j)=3 Or BoxType(j)=7)) Or (BoxType(j)=1 And (BoxType(i)=2 Or BoxType(i)=3 Or BoxType(i)=7)) Or ((BoxType(i)=1 Or BoxType(i)=2 Or BoxType(i)=3) And (BoxType(j)=4 Or (BoxType(j)>=9 And BoxType(j)<=12)))
						; same strength
						Killbox(i)
						Killbox(j)
						Return False
					Else
						; weaker onto stronger
						KillBox(i)
						Return False
					EndIf



				EndIf
			EndIf
			; default: can't move if box is in way, but should never get here
			Return False
		EndIf
	Next
	
	; now check monsters - stationary box can't move, moving box kills monster
	For j=0 To NofMonsters-1
		If ((MonsterX(j)=x And MonsterY(j)=y) Or (MonsterX2(j)=x And MonsterY2(j)=y)) 
			; yep found a monster 
			; if my box is not moving: no power, can't move
			If BoxMoving(i)=False
				; unless its a ghost and not a ghostbox
				If MonsterType(j)=9 And BoxType(i)<>5
					Return True
				EndIf
				Return False
			Else 
			; my box is moving: kill monster
				;unless its a ghost and not a ghostbox
				If MonsterType(j)=9 And BoxType(i)<>5
					Return True
				EndIf
				KillMonster(j)
				; kill box if boombox
				If BoxType(i)=6 Then KillBox(i)
				Return True
			EndIf
		EndIf
	Next
	
	; now check items - can't move into
	For j=0 To NofItems-1
		If ((ItemX(j)=x And ItemY(j)=y)) 
			; yep found a Item 
			Return False
		EndIf
	Next






	; default: move
	Return True
End Function

Function MoveBox(i,dx,dy,Speed)

	; move box in direction dx/dy. Speed is an override factor (x1,x2) if not zero

	; override speed?
	If Speed<>0
		; yes
		BoxSpeed(i)=BaseSpeed/Speed
		If BoxType(i)<>4
			PlayMySound(SFXBoxPush,BoxEntity(i))
		Else
			PlayMySound(SFXBoulderPush,BoxEntity(i))
		EndIf
	Else
		;no
		; Speed depends on Terrain
		If MapTile(BoxX(i),BoxY(i))/100=4 Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i)) Mod 100)<8 And ConveyorActive(1)=True) Or (MapTile(BoxX(i),BoxY(i))/100=5 And (MapTile(BoxX(i),BoxY(i)) Mod 100)>=8 And ConveyorActive(2)=True)
			 ; Ice  or conveyor
			BoxSpeed(i)=BaseSpeed/2
		Else
			If BoxType(i)<>4
				PlayMySound(SFXBoxPush,BoxEntity(i))
			Else
				PlayMySound(SFXBoulderPush,BoxEntity(i))
			EndIf
			BoxSpeed(i)=BaseSpeed
		EndIf
	EndIf
	
	BoxMovingTimer(i)=BoxSpeed(i)
	BoxX2(i)=BoxX(i)+dx
	BoxY2(i)=BoxY(i)+dy
	BoxMovingX(i)=dx
	BoxMovingY(i)=dy

	
	; Check if Stinky is in the way, if so: boom!
	For k=1 To 4
		If PlayerExists(k)=True
			If (BoxX2(i)=PlayerX(k) And BoxY2(i)=PlayerY(k)) Or (BoxX2(i)=PlayerX2(k) And BoxY2(i)=PlayerY2(k))
				;unless stinky is moving away, [LATER: or sideways at end of move]
				;(also discount conveyors, since stinky could be 'walking' against it,
				; perhaps later look at this more precisely, for now it should do)
				If PlayerMoving(k)=True And PlayerMovingX(k)=dx And PlayerMovingY(k)=dy And MapTile(PlayerX(k),PlayerY(k))/100<>5
					; no interaction
				Else
					; boom
					KillPlayer(k)
				EndIf
			EndIf
		EndIf
	Next
	

	BoxMoving(i)=True
		
End Function

Function EndMoveBox(i)
	Oldx=BoxX(i)
	OldY=BoxY(i)
	
	
	BoxX(i)=BoxX2(i)
	BoxY(i)=BoxY2(i)
	
	If endmoveplayertrampoline=False
	
		; stepped off button
		If MapTile(Oldx,Oldy)/100=11
			StepOffButton(Oldx,Oldy)
		EndIf
		
		OldTile=MapTile(Oldx,Oldy)
		
		If OldTile/100=5 And ((OldTile-100) Mod 8) >=4
			; Switcher Tiles 
			
			MapTile(OldX,OldY)=MapTile(OldX,OldY)+2
			If (MapTile(OldX,OldY)-100) Mod 8 <=4 Then MapTile(OldX,OldY)=MapTile(OldX,OldY)-4
			KillSwitcher(OldX,OldY)
			CreateSwitcher(OldX,OldY,MapTile(OldX,OldY) Mod 4 + (Maptile(OldX,Oldy)>=508)*4 )
	
			
			
		EndIf
	EndIf

	
	stickyflag=False
	; if it is a sticky-cube, check neighbours
	If boxtype(i)=8
		For j=0 To nofboxes-1
			If i<>j And boxtype(j)=8 And boxmoving(j)=False
				If (boxx(j)=Boxx(i)-1 And boxy(j)=Boxy(i)) Or (boxx(j)=Boxx(i)+1 And boxy(j)=Boxy(i)) Or (boxx(j)=Boxx(i) And boxy(j)=Boxy(i)-1) Or (boxx(j)=Boxx(i) And boxy(j)=Boxy(i)+1)
					; yep found one
					;remove both
					PlayMySound(SFXSticky,BoxEntity(i))
					MapTile(BoxX(j),BoxY(j))=200
					MapTile(BoxX(i),BoxY(i))=200
					ScaleEntity BoxEntity(i),.5,.5,.5
					EntityAlpha BoxEntity(i),1
					ScaleEntity BoxEntity(j),.5,.5,.5
					EntityAlpha BoxEntity(j),1
					Boxmoving(i)=False
					stickyflag=True
				EndIf
			EndIf
		Next
	EndIf
	If stickyflag=True Then Return

	

		
	; now check what it's on (for some depends on what type of box)
	Tile=MapTile(BoxX(i),BoxY(i))
	
	If Tile/100=3 ;Water
		PlayMySound (SFXBoxSplosh,BoxEntity(i))
		If BoxType(i)=0 Or BoxType(i)=6; wood box
			; convert the water into walkable floor
			MapTile(BoxX(i),BoxY(i))=100
			
			; Create Water Ripple
			CreateParticle(1,4,1,BoxX(i)-.05,-.2,-BoxY(i)+.05,0,0,0,0,0,180)
		
			; drop box and 'remove it'
			TranslateEntity BoxEntity(i),0,-.9,0
			BoxSwayX(i)=BoxX(i)
			BoxSwayY(i)=BoxY(i)

			BoxX(i)=101
			BoxX2(i)=101
			BoxSwayingTimer(i)=0
			BoxMoving(i)=False
			Return
		Else If BoxType(i)=5 ; ghostbox
			KillBox(i)
			Return
		Else ; steel box/reflector/boulder
			; does it already have a steelbox?
			If Tile=399
				; yes
				; convert the water into walkable floor
				MapTile(BoxX(i),BoxY(i))=100
				
				; Create Water Ripple
				CreateParticle(1,4,1,BoxX(i)-.01,-.2,-BoxY(i)+.01,0,0,0,0,0,180)

		
				; drop box and 'remove it'
				TranslateEntity BoxEntity(i),0,-.9,0
				BoxX(i)=101
				BoxX2(i)=101
				BoxMoving(i)=False
				Return
			Else
				; no
				MapTile(BoxX(i),BoxY(i))=399
				
				; Create Water Ripple
				CreateParticle(1,4,1,BoxX(i)-.01,-.2,-BoxY(i)+.01,0,0,0,0,0,180)

				; drop box two levels and 'remove it'
				TranslateEntity BoxEntity(i),0,-1.9,0
				BoxX(i)=101
				BoxX2(i)=101
				BoxMoving(i)=False
				Return
			EndIf
		EndIf
	EndIf
	If Tile/100=4 ; Ice
		; Ice, keep sliding if possible
		; first, figure out forward/backward direction for straight or curved ice
		If Tile=400	; straight ice
			; straight forward
			ForwardX=BoxMovingX(i)
			ForwardY=BoxMovingY(i)
			BackwardX=-BoxMovingX(i)
			BackwardY=-BoxMovingY(i)
		Else If Tile=401 ; down-right ice
			If BoxMovingX(i)=-1 ; coming from right
				ForwardX=0
				ForwardY=1
				BackWardX=1
				BackWardY=0
			Else				; coming from below
				ForwardX=1
				ForwardY=0
				BackWardX=0
				BackWardY=1
			EndIf
		Else If Tile=402 ; down-left ice
			If BoxMovingX(i)=1  ; coming from left
				ForwardX=0
				ForwardY=1
				BackWardX=-1
				BackWardY=0
			Else				; coming from below
				ForwardX=-1
				ForwardY=0
				BackWardX=0
				BackWardY=1
			EndIf
		Else If Tile=403 ; up-left ice
			If BoxMovingX(i)=1 ; coming from left
				ForwardX=0
				ForwardY=-1
				BackWardX=-1
				BackWardY=0
			Else				; coming from above
				ForwardX=-1
				ForwardY=0
				BackWardX=0
				BackWardY=-1
			EndIf
		Else If Tile=404 ; up-right ice
			If BoxMovingX(i)=-1 ; coming from right
				ForwardX=0
				ForwardY=-1
				BackWardX=1
				BackWardY=0
			Else				; coming from above
				ForwardX=1
				ForwardY=0
				BackWardX=0
				BackWardY=-1
			EndIf
		EndIf
		
		If CanBoxMove(i,ForwardX,ForwardY)=True
			MoveBox(i,ForwardX,ForwardY,0)
			Return
		; can't move forward, can we move back?
		Else If CanBoxMove(i,BackWardX,BackWardY)=True
			MoveBox(i,BackWardX,BackWardY,0)
			Return
		Else
			; couldn't move further... should never happen, but kill just in case
			KillBox(i)
			Return
		EndIf
	EndIf

	If (Tile/100=5 And (Tile Mod 100)<8 And ConveyorActive(1)=True) Or (Tile/100=5 And (Tile Mod 100)>=8 And ConveyorActive(2)=True)		
		; Conveyor Belts: move if possible (no bouncing or anything)
		
		Tile= 500 + (Tile Mod 4)
	
		
		; none of that happened, just do conveyor as usual
		If Tile=500	; up
			If CanBoxMove(i,0,-1)=True 
				MoveBox(i,0,-1,0)
				Return
			Else
				KillBox(i)
				Return
			EndIf
		EndIf
		If Tile=502	; down
			If CanBoxMove(i,0,1)=True 
				MoveBox(i,0,1,0)
				Return
			Else
				KillBox(i)

				Return
			EndIf
		EndIf
		If Tile=501	; right
			If CanBoxMove(i,1,0)=True 
				MoveBox(i,1,0,0)
				Return
			Else
				KillBox(i)

				Return

			EndIf
		EndIf
		If Tile=503	; left
			If CanBoxMove(i,-1,0)=True 
				MoveBox(i,-1,0,0)
				Return
			Else
				KillBox(i)

				Return

			EndIf
		EndIf
		


	EndIf
	
	If Tile/100=9 ;Lava
		If BoxType(i)<>1 ; wood box/reflector/boulder/ghost
			; destroy
			KillBox(i)
			Return
		Else If BoxType(i)=1 ; steel box
			PlayMySound(SFXLava,BoxEntity(i))
			; does it already have a steelbox?
			If Tile=999
				; yes
				; convert the water into walkable floor
				MapTile(BoxX(i),BoxY(i))=100
		
				; drop box and 'remove it'
				TranslateEntity BoxEntity(i),0,-.9,0
				BoxX(i)=101
				BoxX2(i)=101
				BoxMoving(i)=False
				Return
			Else
				; no
				MapTile(BoxX(i),BoxY(i))=999
				; drop box two levels and 'remove it'
				TranslateEntity BoxEntity(i),0,-1.9,0
				BoxX(i)=101
				BoxX2(i)=101
				BoxMoving(i)=False
				Return
			EndIf
		EndIf

	
	EndIf
	
	If Tile/100=11 ;buttons
		StepOnButton(BoxX(i),BoxY(i))
		
	EndIf

	If Tile/100=12 ;teleporter
		If BoxStepOnTeleporter(i,BoxX(i),BoxY(i),BoxMovingX(i),BoxMovingY(i))=True
			Return
		EndIf
	EndIf
	
	
	
	
	If Tile/100=17 ; Transporter
		If CanTransporterMove(BoxX(i),BoxY(i),BoxMovingX(i),BoxMovingY(i))
			MoveBox(i,BoxMovingX(i),BoxMovingY(i),0)
			MoveTransporter(BoxX(i),BoxY(i),BoxMovingX(i),BoxMovingY(i))
			PlayMySound(SFXTransporter,BoxEntity(i))
			Return
		EndIf
	EndIf	
	
	If Tile/100=18 ; Trampoline
		BoxJumpingDistance(i)=BoxJumpingDistance(i)+1
		BoxJumping(i)=True
		BoxMoving(i)=True
		BoxMovingTimer(i)=(BaseSpeed)*BoxJumpingDistance(i)
		BoxSpeed(i)=(BaseSpeed)
		BoxJumpingSpeed(i)=BoxMovingTimer(i)
		
				
		dx=BoxMovingX(i)
		dy=BoxMovingY(i)
		PlayMySound(SFXTrampoline,BoxEntity(i))
				
		BoxJumpingX(i)=BoxX(i)+BoxJumpingDistance(i)*BoxMovingX(i)
		BoxJumpingY(i)=BoxY(i)+BoxJumpingDistance(i)*BoxMovingY(i)
		BoxX(i)=101
		BoxY(i)=101
		BoxX2(i)=101
		BoxY2(i)=101
		
		Return
	Else
		BoxJumpingDistance(i)=1
	EndIf
	
	
	
	; boulders - keep moving!
	If BoxType(i)=4 Or (BoxType(i)>=9 And BoxType(i)<=12)
		If CanBoxMove(i,BoxMovingX(i),BoxMovingY(i))=True
			MoveBox(i,BoxMovingX(i),BoxMovingY(i),0)
			Return
		Else
			PlayMySound(SFXBoulderHit,BoxEntity(i))
		EndIf
	EndIf
	

	
	BoxMoving(i)=False

				
End Function

Function ControlBoxes()


	


	For k=1 To 4
		PlayerOn(k)=False; flag, is stinky/loof on a box?
	Next
	
	; just the swaying in the water
	For i=0 To NofBoxes-1
		
		If boxjumping(i)=True
			; in-flight trampoline
			If EntityX(BoxEntity(i))-0.5<0 Or EntityZ(BoxEntity(i))+0.5>0 Or EntityX(BoxEntity(i))+0.5>MapWidth Or EntityZ(BoxEntity(i))-0.5<-MapHeight
				KillBox(i)
				BoxMoving(i)=False
				BoxJumping(i)=False
			EndIf
		EndIf
	
	
		; swaying boxes
		If BoxSwayingTimer(i)>=0
			RotateEntity BoxEntity(i),5*Cos(BoxSwayingTimer(i)),0,5*Sin(BoxSwayingTimer(i))
			BoxSwayingTimer(i)=BoxSwayingTimer(i)+1
			If BoxSwayingTimer(i)=360 Then BoxSwayingTimer(i)=0
			
			; and check if stinky is standing on it
			For k=1 To 4
				;PlayerOn(k)=False
				If PlayerExists(k)=True
					If PlayerDying(k)=False And PlayerDead(k)=False And PlayerMoving(k)=False And PlayerX(k)=BoxSwayX(i) And PlayerY(k)=BoxSwayY(i)
						RotateEntity PlayerEntity(k),0,0,0
		
						TurnEntity PlayerEntity(k),5*Cos(BoxSwayingTimer(i)),0,5*Sin(BoxSwayingTimer(i))
						TurnEntity PlayerEntity(k),0,PlayerRotation(k),0
						PlayerOn(k)=True
					EndIf
				EndIf
			Next
			
						
			
		EndIf
		; rolling boulders
		If BoxType(i)=4 Or (BoxType(i)>=9 And BoxType(i)<=12) And BoxMoving(i)=True
			If BoxMovingX(i)>0 Then TurnEntity BoxEntity(i),0,0,-360/BoxSpeed(i)
			If BoxMovingX(i)<0 Then TurnEntity BoxEntity(i),0,0,360/BoxSpeed(i)
			If BoxMovingY(i)>0 Then TurnEntity BoxEntity(i),-360/BoxSpeed(i),0,0
			If BoxMovingY(i)<0 Then TurnEntity BoxEntity(i),360/BoxSpeed(i),0,0
		EndIf
		; ghostboxes
		If BoxType(i)=5
			size#=.9-+.1*Sin(watertexu*2880.0)
			ScaleEntity BoxEntity(i),size,size,size
		EndIf
		;boomboxes - check if any monsters around
		
		If BoxType(i)=6
			Flag=False
			If BoxMoving(i)=False Or (BoxMoving(i)=True And BoxMovingTimer(i)>=BoxSpeed(i)/2)
				x2=BoxX(i)
				y2=BoxY(i)
			Else
				x2=BoxX2(i)
				y2=BoxY2(i)
			EndIf
			
			For k=0 To NofMonsters-1
				If MonsterDead(k)=False And MonsterDying(k)=False

					If MonsterMoving(k)=False Or (MonsterMoving(k)=True And MonsterMovingTimer(k)>=MonsterSpeed(k)/2)
						xsb=MonsterX(k)
						ysb=MonsterY(k)
					Else
						xsb=MonsterX2(k)
						ysb=MonsterY2(k)
					EndIf
				
				
					For x3=-1 To 1 Step 2
						
						x=x2+x3
						y=y2
				
					
						If x=xsb And y=ysb
							KillMonster(k)
							Flag=True				
						EndIf
													
						x=x2
						y=y2+x3
				
						If x=xsb And y=ysb
							KillMonster(k)
							Flag=True				
						EndIf
							
					Next
				EndIf
			Next
			
			If Flag=True 
				KillBox(i)
				Return
			EndIf
		EndIf
		
		;boxcannons
		If BoxType(i)>=100
			BoxCannonTimer(i)=BoxCannonTimer(i)-1
			If boxmoving(i)=False
				If BoxCannonTimer(i)<=0 And NofFireBalls<25
					;fire!
					PlaySingleSound(BoxX(i),BoxY(i),1)
					CreateFireBall(BoxX(i)+BoxCannonMovingX(i),BoxY(i)+BoxCannonMovingY(i),BoxCannonMovingX(i),BoxCannonMovingY(i))
				
					BoxCannonTimer(i)=(BoxCannonSpeed(i)+1)*CannonBaseTimer
				EndIf
			;Else
			;	BoxCannonTimer(i)=(BoxCannonSpeed(i)+1)*CannonBaseTimer
			EndIf
		EndIf
		



		
		
	Next
	
	
	
	For k=1 To 4
		IceRotation=0
		If PlayerExists(k)=True And PlayerOn(k)=False And PlayerDying(k)=False And PlayerDead(k)=False
			; on no box, return to normal orientation just in case
			If MapTile(PlayerX(k),PlayerY(k))/100=4 IceRotation=-15	
			RotateEntity PlayerEntity(k),IceRotation,PlayerRotation(k),0
		EndIf
	Next
	
		
	
End Function


Function KillBox(i)
	BoxDead(i)=True
	
	If BoxType(i)=7
		PlayMySound(SFXSingle(12),BoxEntity(i))
	Else
		PlayMySound(SFXBoxExplo,BoxEntity(i))
	EndIf
	
	; create explosion of particles
	For j=1 To 7*DetailLevel
		If BoxType(i)=0 
			CreateParticle(0,100,.15,EntityX(BoxEntity(i)),EntityY(BoxEntity(i)),EntityZ(BoxEntity(i)),Rnd(-.04,.04),Rnd(.01,.06),Rnd(-.04,.04),Rnd(-10,10),.001,60*DetailLevel)
		Else If BoxType(i)=6
			CreateParticle(0,0,.15,EntityX(BoxEntity(i)),EntityY(BoxEntity(i)),EntityZ(BoxEntity(i)),Rnd(-.04,.04),Rnd(.01,.06),Rnd(-.04,.04),Rnd(-10,10),.001,60*DetailLevel)
		Else
			CreateParticle(0,101,.15,EntityX(BoxEntity(i)),EntityY(BoxEntity(i)),EntityZ(BoxEntity(i)),Rnd(-.04,.04),Rnd(.01,.06),Rnd(-.04,.04),Rnd(-10,10),.001,60*DetailLevel)
		EndIf
	Next
	
	
	; BOOMBOX - Kill everything around it
	If BoxType(i)=6
		If BoxMoving(i)=False Or (BoxMoving(i)=True And BoxMovingTimer(i)>=BoxSpeed(i)/2)
			x2=BoxX(i)
			y2=BoxY(i)
		Else
			x2=BoxX2(i)
			y2=BoxY2(i)
		EndIf

		For x3=-1 To 1 Step 2
			
			x=x2+x3
			y=y2
			For j=0 To NofBoxes-1
				If BoxDead(j)=False 
					If (x=BoxX(j) Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j))
						KillBox(j)
					EndIf
				EndIf
			Next
			For j=0 To NofMonsters-1
				If MonsterDead(j)=False And MonsterDying(j)=False
					If (x=MonsterX(j) Or x=MonsterX2(j)) And (y=MonsterY(j) Or y=MonsterY2(j))
						If MonsterType(j)<>9 Then KillMonster(j)
					EndIf
				EndIf
			Next
			
			For k=1 To 4
				If PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False
					If (x=PlayerX(k) Or x=PlayerX2(k)) And (y=PlayerY(k) Or y=PlayerY2(k))
						KillPlayer(k)
					EndIf
				EndIf
			Next
			
			x=x2
			y=y2+x3
			For j=0 To NofBoxes-1
				If BoxDead(j)=False 
					If (x=BoxX(j) Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j))
						KillBox(j)
					EndIf
				EndIf
			Next
			For j=0 To NofMonsters-1
				If MonsterDead(j)=False And MonsterDying(j)=False
					If (x=MonsterX(j) Or x=MonsterX2(j)) And (y=MonsterY(j) Or y=MonsterY2(j))
						If MonsterType(j)<>9 Then KillMonster(j)
					EndIf
				EndIf
			Next
			For k=1 To 4
				If PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False
					If (x=PlayerX(k) Or x=PlayerX2(k)) And (y=PlayerY(k) Or y=PlayerY2(k))
						KillPlayer(k)
					EndIf
				EndIf
			Next


				

					
		
		Next
	EndIf

	
	; stepped off button?
	If MapTile(BoxX(i),BoxY(i))/100=11
		StepOffButton(BoxX(i),BoxY(i))
	EndIf


End Function

Function CleanUpBoxes()
	; removes any dead boxes
	For i=0 To NofBoxes-1
		If BoxDead(i)=True
			; yep, get rid of it by overwriting it with later boxes
			FreeEntity BoxEntity(i)
			
			If i<NofBoxes-1 ;unless it is the last one
				For j=i To NofBoxes-1
					BoxEntity(j)=BoxEntity(j+1)
					BoxType(j)=BoxType(j+1)
					BoxX(j)=BoxX(j+1)
					BoxY(j)=BoxY(j+1)
					BoxX2(j)=BoxX2(j+1)
					BoxY2(j)=BoxY2(j+1)
					BoxMoving(j)=BoxMoving(j+1)
					BoxMovingX(j)=BoxMovingX(j+1)
					BoxMovingY(j)=BoxMovingY(j+1)
					BoxMovingTimer(j)=BoxMovingTimer(j+1)
					BoxSpeed(j)=BoxSpeed(j+1)
					BoxDead(j)=BoxDead(j+1)
					BoxSwayingTimer(j)=BoxSwayingTimer(j+1)
					BoxSwayX(j)=BoxSwayX(j+1)
					BoxSwayY(j)=BoxSwayY(j+1)
					BoxJumping(j)=BoxJumping(j+1)
					BoxJumpingX(j)=BoxJumpingX(j+1)
					BoxJumpingY(j)=BoxJumpingY(j+1)
					BoxJumpingSpeed(j)=BoxJumpingSpeed(j+1)
					BoxJumpingDistance(j)=BoxJumpingDistance(j+1)

					BoxCannonMovingX(j)=BoxCannonMovingX(j+1)
					BoxCannonMovingY(j)=BoxCannonMovingY(j+1)
					BoxCannonTimer(j)=BoxCannonTimer(j+1)
					BoxCannonSpeed(j)=BoxCannonSpeed(j+1)


					
				Next
				i=i-1

			EndIf
			NofBoxes=NofBoxes-1
		EndIf
	Next
End Function


Function CreateBoxer(x,y,btype)
	; input tile position x/y
	
	BoxerEntity(NofBoxers)=CopyEntity(BoxerMesh)
	
	BoxerType(NofBoxers)=btype
	
		
	PositionEntity BoxerEntity(NofBoxers),x+0.5,0,-(y+0.5)	
	
	BoxerX(NofBoxers)=x
	BoxerY(NofBoxers)=y
	
	
	BoxerTimer(NofBoxers)=0
	
		
	NofBoxers=NofBoxers+1
	


End Function

Function ControlBoxers()
	For i=0 To NofBoxers-1
		If BoxerTimer(i)=0
			; waiting - look if no more boxes
			
			Flag=False
			For j=0 To NofBoxes-1
				If BoxType(j)=BoxerType(i) And EntityY(BoxEntity(j))>-.2 And (BoxType(j)<>8 Or MapTile(BoxX(j),BoxY(j))<>200)
					Flag=True
				EndIf
			Next
			If Flag=False And NofBoxes<MaxBoxes-10
				; no more - create!
				BoxerTimer(i)=1
				PlaySingleSound(BoxerX(i),BoxerY(i),13)
				
				For k=0 To 3
					dx=0
					dy=0
					CreateBox(BoxerX(i),BoxerY(i),BoxerType(i))
					If k=0 Then dx=1
					If k=1 Then dx=-1
					If k=2 Then dy=1
					If k=3 Then dy=-1
					If CanBoxMove(NofBoxes-1,dx,dy)=True
						MoveBox(NofBoxes-1,dx,dy,0)
					Else	
						BoxDead(NofBoxes-1)=True
					EndIf
				Next
			EndIf
			
			
		Else 
			BoxerTimer(i)=BoxerTimer(i)+1
			
			If BoxerTimer(i)<50
				ScaleEntity BoxerEntity(i),1.3,(60.0-BoxerTimer(i))/60.0*1.3,1.3
			Else If BoxerTimer(i)<51
			
			Else If BoxerTimer(i)<100
				ScaleEntity BoxerEntity(i),1.3,(BoxerTimer(i)-40.0)/60.0*1.3,1.3

			Else If BoxerTimer(i)=100
				BoxerTimer(i)=0
			EndIf
		EndIf
				
	
	Next

End Function

Function CreateWarper(x,y,btype)
	; input tile position x/y
	
	WarperEntity(NofWarpers)=CopyEntity(WarperMesh)
	
	If btype=12 Or btype=13
		btype=btype+1 ; skip mothership
	EndIf
	If btype=3 Or btype=4 ; switch kabooms
		btype=7-btype
	EndIf
	WarperType(NofWarpers)=btype
	
		
	PositionEntity WarperEntity(NofWarpers),x+0.5,0.5,-(y+0.5)	
	
	WarperX(NofWarpers)=x
	WarperY(NofWarpers)=y
	
	
	WarperTimer(NofWarpers)=0
	
		
	NofWarpers=NofWarpers+1
	


End Function

Function ControlWarpers()
	For i=0 To NofWarpers-1
		If WarperTimer(i)=0
			; waiting - look if no more boxes
			
			Flag=False
			For j=0 To NofMonsters-1
				If MonsterType(j)=WarperType(i) 
					Flag=True
				EndIf
			Next
			If Flag=False And NofMonsters<MaxMonsters-10
				; no more - create!
				WarperTimer(i)=1
				PlaySingleSound(WarperX(i),WarperY(i),16)
				
				For k=0 To 3
					
					For k=0 To 3
						
						CreateMonster(WarperX(i),WarperY(i),WarperType(i),k)
						;MonsterData(NofMonsters-1,9)=3
					Next
				
				Next
			EndIf
			
			
		Else 
			WarperTimer(i)=WarperTimer(i)+1
			
			If WarperTimer(i)<40
				ScaleEntity WarperEntity(i),1+Float(WarperTimer(i))/20.0,1+Float(WarperTimer(i))/20.0,1+Float(WarperTimer(i))/20.0
			Else If WarperTimer(i)<200
				ScaleEntity WarperEntity(i),1+Float(200-WarperTimer(i))/80.0,1+Float(200-WarperTimer(i))/80.0,1+Float(200-WarperTimer(i))/80.0
			Else If WarperTimer(i)=200
				WarperTimer(i)=0
			EndIf
		EndIf
				
	
	Next

End Function


; -------------------------------------------
; Break Away Tiles
; -------------------------------------------

Function CreateBreakAway(x,y,btype)
	; input tile position x/y
	BreakAwayEntity(NofBreakAways)=CopyEntity(BreakAwayMesh) 
	If btype Mod 3=0
		EntityTexture BreakAwayEntity(NofBreakAways),BreakAwayTexture1
	Else If btype Mod 3=1
		EntityTexture BreakAwayEntity(NofBreakAways),BreakAwayTexture2
	Else If btype Mod 3=2
		EntityTexture BreakAwayEntity(NofBreakAways),BreakAwayTexture3
	EndIf
	BreakAwayType(NofBreakAways)=btype
	
	PositionEntity BreakAwayEntity(NofBreakAways),x,0,-y	
	
	BreakAwayX(NofBreakAways)=x
	BreakAwayY(NofBreakAways)=y
		
	NofBreakAways=NofBreakAways+1
	
	
End Function

Function KillBreakAway(x,y)
	; destroys the tile mesh at position x/y
	For i=0 To NofBreakAways-1
		If BreakAwayX(i)=x And BreakAwayY(i)=y
			; found it
			PlayMySound(SFXBreakAway,BreakAwayEntity(i))
		
			; move up remaining tiles
			FreeEntity BreakAwayEntity(i)
			
			If i<NofBreakAways-1 ;unless it is the last one
				For j=i To NofBreakAways-1
					BreakAwayEntity(j)=BreakAwayEntity(j+1)
					BreakAwayType(j)=BreakAwayType(j+1)
					BreakAwayX(j)=BreakAwayX(j+1)
					BreakAwayY(j)=BreakAwayY(j+1)
					
				Next
				i=i-1

			EndIf
			NofBreakAways=NofBreakAways-1
	
			
		EndIf
	Next 
End Function



; -------------------------------------------
; Switcher Tiles
; -------------------------------------------

Function CreateSwitcher(x,y,btype)
	; input tile position x/y
	SwitcherEntity(NofSwitchers)=CopyEntity(SwitcherMesh) 
	If btype Mod 4=0
		EntityTexture SwitcherEntity(NofSwitchers),SwitcherTexture1(btype / 4 +1)
	Else If btype Mod 4=1
		EntityTexture SwitcherEntity(NofSwitchers),SwitcherTexture2(btype / 4 +1)
	Else If btype Mod 4=2
		EntityTexture SwitcherEntity(NofSwitchers),SwitcherTexture3(btype / 4 +1)
	Else If btype Mod 4=3
		EntityTexture SwitcherEntity(NofSwitchers),SwitcherTexture4(btype / 4 +1)
	EndIf
	SwitcherType(NofSwitchers)=btype
	
	PositionEntity SwitcherEntity(NofSwitchers),x,0.02,-y	
	
	SwitcherX(NofSwitchers)=x
	SwitcherY(NofSwitchers)=y
		
	NofSwitchers=NofSwitchers+1
	
	
End Function

Function KillSwitcher(x,y)
	; destroys the tile mesh at position x/y
	For i=0 To NofSwitchers-1
		If SwitcherX(i)=x And SwitcherY(i)=y
					
			; move up remaining tiles
			FreeEntity SwitcherEntity(i)
			
			If i<NofSwitchers-1 ;unless it is the last one
				For j=i To NofSwitchers-1
					SwitcherEntity(j)=SwitcherEntity(j+1)
					SwitcherType(j)=SwitcherType(j+1)
					SwitcherX(j)=SwitcherX(j+1)
					SwitcherY(j)=SwitcherY(j+1)
					
				Next
				i=i-1

			EndIf
			NofSwitchers=NofSwitchers-1
	
			
		EndIf
	Next 
End Function


; -------------------------------------------
; Electro Tiles
; -------------------------------------------

; CreateElectro is in level.bb
Function ControlElectroTiles()
	
	For i=0 To 3
		; movement/animation of meshes

		If ElectroExists(i)=True
			; turn on/off
			ElectroTimer(i)=ElectroTimer(i)-1
			If ElectroTimer(i)=ElectroBaseTimer
				; turn on
				MoveEntity ElectroEntity(i),0,0.02,0
				;PlayElectroSound(ElectroEntity(i))
			EndIf
			If ElectroTimer(i)=-1
				; turn off
				ElectroTimer(i)=(i+2)*ElectroBaseTimer
				MoveEntity ElectroEntity(i),0,-0.02,0
			EndIf
			
			; and animate them all
			EntityTexture ElectroEntity(i),ElectroTexture,(ElectroTimer(i) Mod 16)/4
		;	EntityAlpha ElectroEntity(i),0.5
		
			; collision
			; check if _anything_ touches it if on
			If ElectroTimer(i)<=ElectroBaseTimer
				For x=0 To MapWidth-1
					For y=0 To MapHeight-1
						If ElectroMap(x,y)=i
							; Player?
							For k=1 To 4
								If (PlayerExists(k)=True)
									If PlayerMoving(k)=True And PlayerMovingTimer(k)<PlayerSpeed(k)/2
										If (x=PlayerX2(k)) And (y=PlayerY2(k))
											KillPlayer(k)
										EndIf
									Else
										If (x=PlayerX(k)) And (y=PlayerY(k))
											KillPlayer(k)
										EndIf
									EndIf
								EndIf
							Next
				
							; Boxes?
							For j=0 To NofBoxes-1
								If (x=BoxX(j)	Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j))
									KillBox(j)
								EndIf
							Next
							; Monsters? - except saucers/ghosts!
							For j=0 To NofMonsters-1
								If (x=MonsterX(j)	Or x=MonsterX2(j)) And (y=MonsterY(j) Or y=MonsterY2(j)) And (MonsterType(j)<>5 And MonsterType(j)<>6 And MonsterType(j)<>9)
									KillMonster(j)
								EndIf
							Next
							; Sound
							If ElectroTimer(i)=ElectroBaseTimer
								PlaySingleSound(x,y,0)
							EndIf

						EndIf
					Next
				Next
			EndIf	
		EndIf	
	
		
				

	Next
	
	
End Function

; -------------------------------------------
; Spikes
; -------------------------------------------

Function CreateSpike(x,y,speed)
	; input tile position x/y
	
	SpikeEntity(NofSpikes)=CopyEntity(SpikeMesh) 
	
		
	SpikeType(NofSpikes)=speed
	PositionEntity SpikeEntity(NofSpikes),x+.5,-.5,-y-.5	
	
	SpikeX(NofSpikes)=x
	SpikeY(NofSpikes)=y
	
	SpikeTimer(NofSpikes)=SpikeBaseTimer*(speed+3)
		
	NofSpikes=NofSpikes+1
	
	
	
End Function

Function ControlSpikes()
	For i=0 To NofSpikes-1
		SpikeTimer(i)=SpikeTimer(i)-1
		x=SpikeX(i)
		y=SpikeY(i)
		; move up/down
		If SpikeTimer(i)>=SpikeBaseTimer And SpikeTimer(i)<SpikeBaseTimer+20
			MoveEntity SpikeEntity(i),0,.05,0
		Else If SpikeTimer(i)<20
			MoveEntity SpikeEntity(i),0,-.05,0
		EndIf
		If SpikeTimer(i)=0
			SpikeTimer(i)=SpikeBaseTimer*(SpikeType(i)+3)
		EndIf
		
		; check for collisions
		If SpikeTimer(i)>5 And SpikeTimer(i)<SpikeBaseTimer+5
		
			; Player?
			For k=1 To 4
				If (PlayerExists(k)=True)
					If PlayerMoving(k)=True And PlayerMovingTimer(k)<PlayerSpeed(k)/2
						If (x=PlayerX2(k)) And (y=PlayerY2(k))
							KillPlayer(k)
						EndIf
					Else
						If (x=PlayerX(k)) And (y=PlayerY(k))
							KillPlayer(k)
						EndIf
					EndIf
				EndIf
			Next

			
			; Boxes?
			For j=0 To NofBoxes-1
				If (x=BoxX(j)	Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j))
					KillBox(j)
				EndIf
			Next
			; Monsters? - except ghosts!
			For j=0 To NofMonsters-1
				If (x=MonsterX(j)	Or x=MonsterX2(j)) And (y=MonsterY(j) Or y=MonsterY2(j)) And (MonsterType(j)<>9)
					KillMonster(j)
				EndIf
			Next
			

	
		EndIf
		; Sound up
		If SpikeTimer(i)=SpikeBaseTimer+20
			PlaySingleSound(x,y,6)
		EndIf
		; Sound down
		If SpikeTimer(i)=10 
			PlaySingleSound(x,y,5)
		EndIf
		
	
	Next

End Function


; -------------------------------------------
; Cannons & Fireballs
; -------------------------------------------

Function CreateCannon(x,y,speed,dx,dy)
	; input tile position x/y
	
	; what 0-normal, 1-pushable (called from createbox
	
	CannonEntity(NofCannons)=CopyEntity(CannonMesh) 
		
	CannonMovingX(NofCannons)=dx
	CannonMovingY(NofCannons)=dy
	
	CannonSpeed(NofCannons)=speed
	If CannonMovingX(NofCannons)<>0
		TurnEntity CannonEntity(NofCannons),0,0,(90*(2-CannonMovingX(NofCannons)))
	EndIf
	If CannonMovingY(NofCannons)<>0
		TurnEntity CannonEntity(NofCannons),0,0,(90*(CannonMovingY(NofCannons)+1))
	EndIf
	PositionEntity CannonEntity(NofCannons),x+.5,0,-y-.5	
	
	CannonX(NofCannons)=x
	CannonY(NofCannons)=y
	
	CannonTimer(NofCannons)=(speed+1)*CannonBaseTimer
		
	NofCannons=NofCannons+1
	
	
	
End Function

Function ControlCannons()
	For i=0 To NofCannons-1
		CannonTimer(i)=CannonTimer(i)-1
		If CannonTimer(i)<=0 And NofFireBalls<25
			;fire!
			PlaySingleSound(CannonX(i),CannonY(i),1)
			CreateFireBall(CannonX(i)+CannonMovingX(i),CannonY(i)+CannonMovingY(i),CannonMovingX(i),CannonMovingY(i))
		
			CannonTimer(i)=(CannonSpeed(i)+1)*CannonBaseTimer
		EndIf
		

	Next
End Function


Function CreateFireball(x,y,dx,dy)
	
	; input tile position x/y
	If NofFireballs>=MaxFireBalls Then Return
	
	FireballEntity(NofFireballs)=CopyEntity(FireballMesh) 
	
	FireBallMovingX(NofFireballs)=dx
	FireBallMovingY(NofFireballs)=dy
	
	If FireballMovingX(NofFireballs)<>0
		TurnEntity FireballEntity(NofFireballs),0,-(90*(2-FireballMovingX(NofFireballs))),0	
	EndIf
	If FireballMovingY(NofFireballs)<>0
		TurnEntity FireballEntity(NofFireballs),0,-(90*(FireballMovingY(NofFireballs)+1)),0	
	EndIf

	
	PositionEntity FireballEntity(NofFireballs),x+0.5,0.5,-y-0.5	
	
	FireballX(NofFireballs)=x
	FireballY(NofFireballs)=y
	FireballX2(NofFireballs)=x;+dx
	FireballY2(NofFireballs)=y;+dy
	
	; first check reflectors
	For j=0 To NofBoxes-1
		If (BoxType(j)=2 Or BoxType(j)=3) And (x=BoxX(j) Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j))
			If BoxMoving(j)=True
				FireBallMovingTimer(NofFireballs)=FireBallSpeed
				FireBallDead(NofFireballs)=False
				NofFireballs=NofFireballs+1
				KillFireBall(NofFireballs)
				Return
			EndIf
			If BoxType(j)=2	
				k=FireBallMovingY(NofFireballs) 
				FireBallMovingY(NofFireballs)=-FireBallMovingX(NofFireballs)
				FireBallMovingX(NofFireballs)=-k
			EndIf
			If BoxType(j)=3
				k=FireBallMovingY(NofFireballs) 
				FireBallMovingY(NofFireballs)=FireBallMovingX(NofFireballs)
				FireBallMovingX(NofFireballs)=k
			EndIf
			If BoxType(j)=7
				PlaySingleSound(BoxX(j),BoxY(j),7)
				CreateFireBall(BoxX(j),BoxY(j),FireBallMovingY(i),FireBallMovingX(i))
				CreateFireBall(BoxX(j),BoxY(j),-FireBallMovingY(i),-FireBallMovingX(i))
				KillBox(j)
			EndIf

		EndIf
	Next

	FireballX2(NofFireballs)=x+FireBallMovingX(NofFireballs)
	FireballY2(NofFireballs)=y+FireBallMovingY(NofFireballs)

	

	
	FireBallMovingTimer(NofFireballs)=FireBallSpeed
	FireBallDead(NofFireballs)=False
	
		
	NofFireballs=NofFireballs+1
	
	
	
End Function
Function ControlFireballs()

	For i=0 To NofFireBalls-1
	    If FireBallDead(i)=False
			
			TranslateEntity FireBallEntity(i),Float(FireBallMovingX(i))/Float(FireBallSpeed),0,-Float(FireBallMovingY(i))/Float(FireBallSpeed)
			
			If FireBallMovingTimer(i) Mod 6 =0
				CreateParticle(0,0,0.2,EntityX(FireBallEntity(i)),EntityY(FireBallEntity(i)),EntityZ(FireBallEntity(i)),0,Rnd(-.01,.01),Rnd(-.01,.01),Rnd(-1,1),0,FireBallSpeed*2)
			EndIf
			
			FireBallMovingTimer(i)=FireBallMovingTimer(i)-1
			If FireBallMovingTimer(i)=0
				;reached next tile
				If FireBallX2(i)<=0 Or FireBallY2(i)<=0 Or FireBallX2(i)>=Mapwidth-1 Or FireBallY2(i)>=MapHeight-1
					KillFireBall(i)
				EndIf
				
				;first check if hit reflector - change direction
				For j=0 To NofBoxes-1
					If (BoxType(j)=2 Or BoxType(j)=3 Or BoxType(j)=7) And (FireBallX2(i)=BoxX(j) Or FireBallX2(i)=BoxX2(j)) And (FireBallY2(i)=BoxY(j) Or FireBallY2(i)=BoxY2(j))
						If BoxType(j)=2	
							PlaySingleSound(BoxX(j),BoxY(j),7)
							k=FireBallMovingY(i) 
							FireBallMovingY(i)=-FireBallMovingX(i)
							FireBallMovingX(i)=-k
						EndIf
						If BoxType(j)=3
							PlaySingleSound(BoxX(j),BoxY(j),7)
							k=FireBallMovingY(i) 
							FireBallMovingY(i)=FireBallMovingX(i)
							FireBallMovingX(i)=k
						EndIf
						If BoxType(j)=7
							PlaySingleSound(BoxX(j),BoxY(j),7)
							CreateFireBall(BoxX(j),BoxY(j),FireBallMovingY(i),FireBallMovingX(i))
							CreateFireBall(BoxX(j),BoxY(j),-FireBallMovingY(i),-FireBallMovingX(i))
							KillBox(j)
						
						EndIf
					EndIf
				Next
						
				FireBallMovingTimer(i)=FireBallSpeed
				
				FireballX(i)=FireballX2(i)
				FireballY(i)=FireballY2(i)
				FireballX2(i)=FireballX(i)+FireBallMovingX(i)
				FireballY2(i)=FireballY(i)+FireBallMovingY(i)
				
				
				
			EndIf
			; check for collision 
			If CanFireBallMove(i)=False
				KillFireball(i)
			EndIf
		EndIf
	
	
	Next


End Function

Function CanFireBallMove(i)
	
	; pick either first or second half of movement
	If FireBallMovingTimer(i)>=FireBallSpeed/2
		x=FireBallX(i)
		y=FireBallY(i)
	Else
		x=FireBallX2(i)
		y=FireBallY2(i)
	EndIf
	
	; check tiles 
	Tile=MapTile(x,y)
	If Tile/100=2 Or Tile/100=8 Or (Tile/100=10 And Tile Mod 100<50)
		;can't move into walls, cannons, gates
		Return False
	EndIf
	
	
	
	; check Boxes
	For j=0 To NofBoxes-1
		If BoxMoving(j)=False Or (BoxMoving(j)=True And BoxMovingTimer(j)>=BoxSpeed(j)/2)
			x2=BoxX(j)
			y2=BoxY(j)
		Else
			x2=BoxX2(j)
			y2=BoxY2(j)
		EndIf

		If x=x2 And y=y2
			If BoxType(j)=0 Or BoxType(j)=6
				KillBox(j)
			EndIf
			
			If (BoxType(j)=2 Or BoxType(j)=3 Or BoxType(j)=7) And BoxMoving(j)=False
				Return True
			EndIf

			Return False
		EndIf
	Next 
	
	; check Monsters
	For i=0 To NofMonsters-1
		If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
			x2=MonsterX(i)
			y2=MonsterY(i)
		Else
			x2=MonsterX2(i)
			y2=MonsterY2(i)
		EndIf

		If x=x2 And y=y2 And MonsterType(i)<>9 And (MonsterType(i)<>12 Or (MonsterData(i,3)>300 And MonsterData(i,3)<400))
			KillMonster(i)
			Return False
		EndIf
	Next 
	
	; check Items
	For i=0 To NofItems-1
		If x=ItemX(i) And y=ItemY(i)
			Return False
		EndIf
	Next 

	; check Player
	For k=1 To 4
		If PlayerExists(k)=True
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
				Return False
			EndIf
		EndIf
	Next
	
	

	

	Return True


End Function

Function KillFireball(i)

	FireballDead(i)=True
	PlaySingleSound(FireBallX(i),FireBallY(i),2)
	

End Function

Function CleanUpFireballs()
	; removes any dead Fireballs
	For i=0 To NofFireballs-1
		If FireballDead(i)=True
			; yep, get rid of it by overwriting it with later Fireballs
			FreeEntity FireballEntity(i)
			
			If i<NofFireballs-1 ;unless it is the last one
				For j=i To NofFireballs-1
					FireballEntity(j)=FireballEntity(j+1)
					FireballX(j)=FireballX(j+1)
					FireballY(j)=FireballY(j+1)
					FireballX2(j)=FireballX2(j+1)
					FireballY2(j)=FireballY2(j+1)
					FireballMovingX(j)=FireballMovingX(j+1)
					FireballMovingY(j)=FireballMovingY(j+1)
					FireballMovingTimer(j)=FireballMovingTimer(j+1)
					FireballDead(j)=FireballDead(j+1)
				Next
				i=i-1

			EndIf
			NofFireballs=NofFireballs-1
		EndIf
	Next



End Function

; -----------------------
; Laser Gates
; -----------------------
Function CreateLaserGate(x,y,col,dir)
		
	LaserGateActive(col)=True
	
	If col=0
	; iron gate - load
		LaserGateEntity(NofLaserGates)=CopyEntity(IronGateMesh)
		If dir=1 Then TurnEntity LaserGateEntity(NofLaserGates),0,0,90
	Else
	; create the mesh if laser gate
		LaserGateEntity(NofLaserGates)=CreateMesh() 
		cyl=CreateCylinder (6,False) ; an individual cylinder
		ScaleMesh cyl,0.05,0.5,0.05
		RotateMesh cyl,0,0,90
		PositionMesh cyl,0,.25,0.0
		AddMesh cyl,LaserGateEntity(NofLaserGates)
		PositionMesh cyl,0,-.375,.2165
		AddMesh cyl,LaserGateEntity(NofLaserGates)
		PositionMesh cyl,0,0,-.433
		AddMesh cyl,LaserGateEntity(NofLaserGates)
		FreeEntity cyl
		If dir=1 Then RotateEntity LaserGateEntity(NofLaserGates),0,90,0
	EndIf
	
	
	
	PositionEntity LaserGateEntity(NofLaserGates),x+.5,0.5,-y-.5
	If col=0 Then PositionEntity LaserGateEntity(NofLaserGates),x+.5,0,-y-.5


	LaserGateX(NofLaserGates)=x
	LaserGateY(NofLaserGates)=y
	LaserGateCol(NofLaserGates)=col

	;EntityTexture LaserGateEntity(NofLaserGates),LaserGateTexture(col)
	If col >0 Then EntityAlpha LaserGateEntity(NofLaserGates),0.5
	
	If col=0
		EntityColor LaserGateEntity(NofLaserGates),128,128,128
	Else If col=1
		EntityColor LaserGateEntity(NofLaserGates),255,0,255
	Else If col=2
		EntityColor LaserGateEntity(NofLaserGates),255,255,0
	Else If col=3
		EntityColor LaserGateEntity(NofLaserGates),0,255,0
	Else If col=4
		EntityColor LaserGateEntity(NofLaserGates),0,255,255
	Else If col=5
		EntityColor LaserGateEntity(NofLaserGates),255,55,55
	Else If col=6
		EntityColor LaserGateEntity(NofLaserGates),50,57,255
	Else If col=7
		EntityColor LaserGateEntity(NofLaserGates),255,255,200



	EndIf


	
	; Create the emmiter mesh
	LaserGateEntity2(NofLaserGates)=CreateMesh()
	If col=0
		HideEntity LaserGateEntity2(NofLaserGates)
	Else
		cyl=CreateCylinder (6,True) ; an individual cylinder
		ScaleMesh cyl,0.08,0.03,0.08
		RotateMesh cyl,0,0,90
	
		PositionMesh cyl,-.47,.25,0.0
		AddMesh cyl,LaserGateEntity2(NofLaserGates)
		PositionMesh cyl,.47*2,0,0.0
		AddMesh cyl,LaserGateEntity2(NofLaserGates)
	
		PositionMesh cyl,-.47*2,-.375,.2165
		AddMesh cyl,LaserGateEntity2(NofLaserGates)
		PositionMesh cyl,.47*2,0,0
		AddMesh cyl,LaserGateEntity2(NofLaserGates)
	
		PositionMesh cyl,-.47*2,0,-.433
		AddMesh cyl,LaserGateEntity2(NofLaserGates)
		PositionMesh cyl,.47*2,0,0
		AddMesh cyl,LaserGateEntity2(NofLaserGates)
		FreeEntity cyl
		
		If dir=1 Then RotateEntity LaserGateEntity2(NofLaserGates),0,90,0
		
		PositionEntity LaserGateEntity2(NofLaserGates),x+.5,0.5,-y-.5
	
		EntityTexture LaserGateEntity2(NofLaserGates),SteelBoxTexture
	EndIf



	
	NofLaserGates=NofLaserGates+1
	
	
End Function


Function ToggleLaserGate(n,AlwaysOff)
	; goes through the map and turns on/off all gates of that type
	; if AlwaysOff=True then we always turn off the gate, never on.
	For i=0 To MapWidth-1
		For j=0 To MapHeight-1
		
			If MapTile(i,j)/100=10 And ((MapTile(i,j)-1000) Mod 50)/2=n
				; got one, toggle it:
				If MapTile(i,j) Mod 100 <50 Or AlwaysOff=True
					If MapTile(i,j) Mod 100 < 50
						MapTile(i,j)=MapTile(i,j)+50
						LaserGateActive(n)=False
						For k=0 To NofLaserGates-1
							If LaserGateX(k)=i And LaserGateY(k)=j
								If MapTile(i,j) Mod 50 <2 
									; final gate, start anim
									FinalGateAnim=60;
								Else
									; laser gate,just hide
									HideEntity LaserGateEntity(k)
								EndIf
							EndIf
						Next
					EndIf

				Else
					MapTile(i,j)=MapTile(i,j)-50
					LaserGateActive(n)=True
					For k=0 To NofLaserGates-1
						If LaserGateX(k)=i And LaserGateY(k)=j
							ShowEntity LaserGateEntity(k)
						EndIf
					Next


					; lasergate is reactivated: check and destroy anything in its path
					
					;Player
					For k=1 To 4
						If PlayerExists(k)
							If (PlayerX(k)=i Or PlayerX2(k)=i) And (PlayerY(k)=j Or PlayerY2(k)=j)
								KillPlayer(k)
							EndIf
						EndIf
					Next

					; Boxes
					For k=0 To NofBoxes-1
						If (BoxX(k)=i Or BoxX2(k)=i) And (BoxY(k)=j Or BoxY2(k)=j)
							KillBox(k)
						EndIf
					Next
					; Fireballs 
					For k=0 To NofFireballs-1
						If (FireballX(k)=i Or FireballX2(k)=i) And (FireballY(k)=j Or FireballY2(k)=j)
							KillFireball(k)
						EndIf
					Next
					; Monsters
					For k=0 To NofMonsters-1
						If (MonsterX(k)=i Or MonsterX2(k)=i) And (MonsterY(k)=j Or MonsterY2(k)=j)
							If MonsterType(k)<>9 Then KillMonster(k)
						EndIf
					Next


					
				EndIf
			EndIf
		Next
	Next
				

End Function

; -------------------------------------------
; Buttons
; -------------------------------------------

Function CreateButton(x,y,btype)
	; input tile position x/y
	
	
	
	ButtonEntity(NofButtons)=CreateMesh()
	Surface=CreateSurface(ButtonEntity(NofButtons))
	
	
	AddVertex(Surface,-.5,0,.5, 0+(btype Mod 4)*.25,.125+(btype/4)*.125)
	AddVertex(Surface,.5,0,.5, 0.25+(btype Mod 4)*.25,0.125+(btype/4)*.125)
	AddVertex(Surface,.5,0,-.5, 0.25+(btype Mod 4)*.25,0+(btype/4)*.125)
	AddVertex(Surface,-.5,0,-.5, 0+(btype Mod 4)*.25,0+(btype/4)*.125)
	AddTriangle(Surface,0,1,2)
	AddTriangle(Surface,0,2,3)
	UpdateNormals(ButtonEntity(NofButtons))


	EntityTexture ButtonEntity(NofButtons),ButtonOnTexture
	

	
	ScaleEntity ButtonEntity(NofButtons),0.8,1,0.8
		
	ButtonType(NofButtons)=btype
	
	PositionEntity ButtonEntity(NofButtons),x+0.5,0.01,-y-0.5	
	
	ButtonX(NofButtons)=x
	ButtonY(NofButtons)=y
	
	ButtonTimer(NofButtons)=0
	ButtonActive(NofButtons)=False	
	
	NofButtons=NofButtons+1
	
	
	
	
	
End Function

Function StepOnButton(x,y)

	; first figure out which button was pushed
	For i=0 To NofButtons-1
		If x=ButtonX(i) And y=ButtonY(i)
			; got it!
			
			
			; Activate if necessary
			If ButtonActive(i)=False 
				ActivateButton(i,0)
				PlayMySound(SFXButton,0)
			EndIf
			
						
			If ButtonType(i) Mod 3 = 2 		; timer
				ButtonTimer(i)=ButtonBaseTimer
				PlayMySound(SFXTimerButton,0)
				For j=0 To NofButtons-1
					If ButtonType(j)=ButtonType(i) And j<>i 
						ActivateButton(j,0)
						ButtonTimer(j)=ButtonBaseTimer
					EndIf
				Next
			EndIf					
					
		EndIf
	Next
	
		
End Function

Function StepOffButton(x,y)

	; first figure out which button was stepped off
	For i=0 To NofButtons-1
		If x=ButtonX(i) And y=ButtonY(i)
			; got it!
			
			; Act/DeActivate if flip switch
			If ButtonType(i) Mod 3 =1 
				If ButtonActive(i)=True And CheckButtonType(ButtonType(i))=False
					DeActivateButton(i,0)
				EndIf
				
			EndIf
			
				
					
		EndIf
	Next
	
		
End Function

Function CheckButtons()
	; checks all buttons if something is on it and activates (done at start of level)
	For i=0 To NofButtons-1
		; take each button and check if something is on it
		If CheckButton(i)=True Then StepOnButton(ButtonX(i),ButtonY(i))
	Next
End Function
Function CheckButtonType(btype)
	; checks a particular type of button (used for stepping off round buttons)
	For i=0 To NofButtons-1
		If ButtonType(i)=btype
			If CheckButton(i)=True
				Return True
			EndIf
		EndIf
	Next
	Return False
End Function
Function CheckButton(i)
	; check a particular button (used by the above functions)
	
	; player
	For k=1 To 4
		If Playerexists(k)=True
			If (playerX(k)=ButtonX(i) And playerY(k)=ButtonY(i) And playerDead(k)=False)
				Return True
			EndIf
		EndIf
	Next
	
	; boxes
	For j=0 To NofBoxes-1
		If ButtonX(i)=BoxX(j) And ButtonY(i)=BoxY(j) And BoxDead(j)=False
			Return True
		EndIf
	Next
	; monsters
	For j=0 To Nofmonsters-1
		If ButtonX(i)=monsterX(j) And ButtonY(i)=monsterY(j) And monsterdying(j)=False
			Return True
		EndIf
	Next

	Return False
End Function
Function ActivateButton(i,iteration)

	ButtonActive(i)=True

	;TranslateEntity ButtonEntity(i),0,-0.019,0
	EntityTexture ButtonEntity(i),ButtonOffTexture
	
		
	; what kind (i.e color/transporter) of button
	If ButtonType(i)/3 = 0  ; transporter
		;If ConveyorActive=True
			ConveyorActive(1)=False
		;Else 
		;	ConveyorActive=True
		;EndIf
	Else If ButtonType(i)/3=1
		ConveyorActive(2)=False
	Else ; lasergate
		ToggleLaserGate(ButtonType(i)/3-1,True)
	EndIf
	
	If iteration=0
		; and activate all other buttons of the same type
		For j=0 To NofButtons-1
			If ButtonType(j)=ButtonType(i) And j<>i
				;ActivateButton(j,1)
				ButtonActive(j)=True
				EntityTexture ButtonEntity(j),ButtonOffTexture

			EndIf
		Next
	EndIf


End Function

Function DeActivateButton(i,iteration)

	ButtonActive(i)=False

;	If ButtonType(i)/3=0 
		EntityTexture ButtonEntity(i),ButtonOnTexture
;	Else
;		If ButtonType(i)/3=1
;			EntityColor ButtonEntity(i),255,0,255
;		Else If ButtonType(i)/3=2
;			EntityColor ButtonEntity(i),255,255,0
;		Else If ButtonType(i)/3=3
;			EntityColor ButtonEntity(i),0,255,0
;		Else If ButtonType(i)/3=4
;			EntityColor ButtonEntity(i),0,255,255
;		EndIf

		;EntityTexture ButtonEntity(i),LaserGateTexture(ButtonType(i)/3)
;	EndIf
	
	; what kind (i.e color/transporter) of button
	If ButtonType(i)/3 = 0 ; transporter
		;If ConveyorActive=True
		;	ConveyorActive=False
		;Else 
			ConveyorActive(1)=True
	Else If buttontype(i)/3=1
		ConveyorActive(2)=True
		;EndIf
	Else  ; lasergate
	
		
		ToggleLaserGate(ButtonType(i)/3-1,False)
				
		
	EndIf
	
	
	If iteration=0
		; and activate all other buttons of the same type
		For j=0 To NofButtons-1
			If ButtonType(j)=ButtonType(i)
				;DeActivateButton(j,1)
				ButtonActive(j)=False
				EntityTexture ButtonEntity(j),ButtonOnTexture

			EndIf
		Next
	EndIf


End Function

Function ControlTimerButtons()
	
	For i=0 To NofButtons-1
		If ButtonType(i) Mod 3 = 2 And ButtonActive(i)=True
			; ticking down
			ButtonTimer(i)=ButtonTimer(i)-1
			RotateEntity ButtonEntity(i),0,(ButtonTimer(i)*90)/ButtonBaseTimer,0
			If ButtonTimer(i)<=0
				RotateEntity ButtonEntity(i),0,0,0
				ButtonActive(i)=False
				DeActivateButton(i,0)
			EndIf
		EndIf
	Next
End Function

; -------------------------------------------
; Teleporters
; -------------------------------------------

Function PlayerStepOnTeleporter(k,x,y,dx,dy)
	
	; returns true if movement was possible
	
	ttype=MapTile(x,y)-1200
	x2=-1
	; first find out where the other teleporter is located
	For i=0 To MapWidth-1
		For j=0 To MapHeight-1
			If MapTile(i,j)-1200=ttype And (x<>i Or y<>j)
				; found it
				x2=i
				y2=j
			EndIf
		Next
	Next
	If x2=-1
		; found nothing - shouldn't happen though
		Return False
	EndIf
	
	; i/j is now the new teleporter position
	; move Stinky to new position and check four directions of movement
	PlayerX(k)=x2
	PlayerY(k)=y2
	PositionEntity PlayerEntity(k),x2+0.5,0,-(y2+0.5)
	PositionEntity PlayerShadowEntity(k),x2+0.2,0.01,-(y2+0.2)

	
	If CanPlayerMove(k,dx,dy)=True
		MovePlayer(k,dx,dy)
	Else If CanPlayerMove(k,-dx,-dy)=True
		MovePlayer(k,-dx,-dy)
	Else If CanPlayerMove(k,dy,dx)=True
		MovePlayer(k,dy,dx)
	Else If CanPlayerMove(k,-dy,-dx)=True
		MovePlayer(k,-dy,-dx)
	Else 
		; couldn't move anywhere - move back
		PlayerX(k)=x
		PlayerY(k)=y
	
		PositionEntity PlayerEntity(k),x+0.5,0,-(y+0.5)
		PositionEntity PlayerShadowEntity(k),x+0.2,0.01,-(y+0.2)	
		
		PlayMySound(SFXHmmm,0)
		Return False

	EndIf

	PlayMySound(SFXTeleport,0)
	If FocusOnPlayer(k)=True	
		If view3d=False
			For i=1 To 4
				CameraDX=EntityX(PlayerEntity(k))-EntityX(Camera)
				CameraDZ=(EntityZ(PlayerEntity(k))-9)-EntityZ(Camera)
			Next
		Else
			CameraDX=EntityX(PlayerEntity(k))-x-0.5
			CameraDZ=EntityZ(PlayerEntity(k))+y+0.5
		EndIf

	EndIf
	
	Return True
	

End Function


Function BoxStepOnTeleporter(i,x,y,dx,dy)
	
	; returns true if movement was possible
	
	ttype=MapTile(x,y)-1200
	x2=-1
	; first find out where the other teleporter is located
	For i2=0 To MapWidth-1
		For j=0 To MapHeight-1
			If MapTile(i2,j)-1200=ttype And (x<>i2 Or y<>j)
				; found it
				x2=i2
				y2=j
			EndIf
		Next
	Next
	If x2=-1
		; found nothing - shouldn't happen though
		Return False
	EndIf
	
	; i/j is now the new teleporter position
	; move Box to new position and check four directions of movement
	BoxX(i)=x2
	BoxY(i)=y2
	PositionEntity BoxEntity(i),x2+0.5,0.4,-(y2+0.5)	
	BoxMoving(i)=False
	If CanBoxMove(i,dx,dy)=True
		MoveBox(i,dx,dy,0)
	Else If CanBoxMove(i,-dx,-dy)=True
		MoveBox(i,-dx,-dy,0)
	Else If CanBoxMove(i,dy,dx)=True
		MoveBox(i,dy,dx,0)
	Else If CanBoxMove(i,-dy,-dx)=True
		MoveBox(i,-dy,-dx,0)
	Else 
		; couldn't move anywhere - move back
		BoxX(i)=x
		BoxY(i)=y
	
		PositionEntity BoxEntity(i),x+0.5,0.4,-(y+0.5)	
		
		Return False

	EndIf
	
	; and move the box already a little bit so it is ahead of stinky and
	; 	can be pushed if stinky follows
	TranslateEntity BoxEntity(i),BoxMovingX(i)/Float(BoxSpeed(i)),0,-BoxMovingY(i)/Float(BoxSpeed(i))
	BoxMovingTimer(i)=BoxMovingTimer(i)-1
	
	Return True
	

End Function

; -------------------------------------------
; Monsters
; -------------------------------------------

Function CreateMonster(x,y,btype,dir)
	
	; btype	0 - coily
	;		1,2 - z-bot
	;		3,4 - bomber
	;		5,6 - saucer
	;		7,8	- chomper
	;		9   - ghost
	;		10  - rainbow spirit
	;		11  - broken zbot
	;		12  - mothership
	;		13/14/15/16 - shadow stinkers
	;		17/18/19/20 - fire stinkers
	
	; First the Mesh
	If btype=2 Or btype=4 Or btype=6 Or btype=8	; left/right variations: same mesh
		MonsterEntity(NofMonsters)=CopyEntity(MonsterMesh(btype-1)) 
	Else If btype=10 ; rainbow spirit
		MonsterEntity(NofMonsters)=CopyEntity(MonsterMesh(9))
		ScaleEntity MonsterEntity(NofMonsters),.6,.6,.6
	Else If btype=11 ; broken zbot
		MonsterEntity(NofMonsters)=CopyEntity(MonsterMesh(1))
	Else If btype=12 ; mothership
		MonsterEntity(NofMonsters)=CopyEntity(MonsterMesh(5))
		ScaleEntity MonsterEntity(NofMonsters),5,5,5
	Else If btype=13 Or btype=17 ;shadowstinkers
		MonsterEntity(NofMonsters)=CopyEntity(playermesh(1))
	Else If btype=14 Or btype=18 ;shadowstinkers
		MonsterEntity(NofMonsters)=CopyEntity(playermesh(2))
	Else If btype=15 Or btype=19 ;shadowstinkers
		MonsterEntity(NofMonsters)=CopyEntity(playermesh(3))
	Else If btype=16 Or btype=20 ;shadowstinkers
		MonsterEntity(NofMonsters)=CopyEntity(playermesh(4))

	Else
		MonsterEntity(NofMonsters)=CopyEntity(MonsterMesh(btype)) 
	EndIf
	If btype=8 EntityTexture MonsterEntity(NofMonsters),MonsterTexture(8)
	If btype=10 EntityTexture MonsterEntity(NofMonsters),Rainbowtexture
	MonsterShadowEntity(NofMonsters)=CreateFlatSquare(RoundShadowTexture)
	PositionMesh MonsterShadowEntity(NofMonsters),-.5,0,-.5
	
	; Position of Mesh/Shadow	
	If btype=0
		PositionEntity MonsterEntity(NofMonsters),x+0.5,0.4,-(y+0.5)
		TurnEntity MonsterEntity(NofMonsters),0,Rand(1,300),0
		ScaleMesh MonsterShadowEntity(NofMonsters),.6,1,.6
	Else If btype=1 Or btype=2 Or btype=11
		PositionEntity MonsterEntity(NofMonsters),x+.5,.3,-(y+.5)
		RotateEntity MonsterEntity(NofMonsters),-90,0,0
		ScaleMesh MonsterShadowEntity(NofMonsters),.4,1,.4
	Else If btype=3 Or btype=4
		PositionEntity MonsterEntity(NofMonsters),x+0.5,0,-(y+0.5)
		ScaleMesh MonsterShadowEntity(NofMonsters),.7,1,.7
	Else If btype=5 Or btype=6
		PositionEntity MonsterEntity(NofMonsters),x+.5,.3,-(y+.5)
		RotateEntity MonsterEntity(NofMonsters),-90,0,0
		ScaleMesh MonsterShadowEntity(NofMonsters),.7,1,.7
	Else If btype=7 Or btype=8
		PositionEntity MonsterEntity(NofMonsters),x+0.5,0.4,-(y+0.5)
		ScaleMesh MonsterShadowEntity(NofMonsters),.6,1,.6
	Else If btype=9
		PositionEntity MonsterEntity(NofMonsters),x+0.5,0,-(y+0.5)
		ScaleMesh MonsterShadowEntity(NofMonsters),.7,1,.7
	Else If btype=10
		PositionEntity MonsterEntity(NofMonsters),x+0.5,.5,-(y+0.5)
		ScaleMesh MonsterShadowEntity(NofMonsters),.7,1,.7
	Else If btype=12
		PositionEntity MonsterEntity(NofMonsters),x+.5,-1.8,-(y+.5)
		RotateEntity MonsterEntity(NofMonsters),-90,0,0
	Else If btype>=13 And btype<=20
		PositionEntity MonsterEntity(NofMonsters),x+0.5,0,-(y+0.5)	

	EndIf
	PositionEntity MonsterShadowEntity(NofMonsters),x+.5,0.01,-y-.0
	If btype=1 Or btype=2 Or btype=11
		PositionEntity MonsterShadowEntity(NofMonsters),x+.5,0.01,-y-.3
	EndIf
	If btype=9 Or btype=10 Or btype=12 HideEntity MonsterShadowEntity(NofMonsters)
	
	
	MonsterX(NofMonsters)=x
	MonsterY(NofMonsters)=y
	MonsterX2(NofMonsters)=x
	MonsterY2(NofMonsters)=y
	MonsterMoving(NofMonsters)=False
	MonsterType(NofMonsters)=btype
	
	; direction
	If btype<13 Or btype>20 ; no turning for shadowstinkers
		If dir=0
			MonsterMovingX(NofMonsters)=0
			MonsterMovingY(NofMonsters)=-1
			If btype=3 Or btype=4 
				TurnEntity MonsterEntity(NofMonsters),0,180,0
			Else If btype>0
				TurnEntity MonsterEntity(NofMonsters),0,0,-90
			EndIf
		Else If dir=1
			MonsterMovingX(NofMonsters)=1
			MonsterMovingY(NofMonsters)=0
			If btype=3 Or btype=4 
				TurnEntity MonsterEntity(NofMonsters),0,90,0
			EndIf
		Else If dir=2
			MonsterMovingX(NofMonsters)=0
			MonsterMovingY(NofMonsters)=1
			If btype>0
				TurnEntity MonsterEntity(NofMonsters),0,0,90
			EndIf
		Else 
			MonsterMovingX(NofMonsters)=-1
			MonsterMovingY(NofMonsters)=0
			If btype=3 Or btype=4 
				TurnEntity MonsterEntity(NofMonsters),0,-90,0
			Else If btype>0
				TurnEntity MonsterEntity(NofMonsters),0,0,180
			EndIf
		EndIf
	EndIf
	If btype=0  ;coily - no dir
		MonsterMovingX(NofMonsters)=0
		MonsterMovingY(NofMonsters)=0
	EndIf
	
	MonsterDead(NofMonsters)=False
	MonsterDying(NofMonsters)=False
	MonsterDyingTimer(NofMonsters)=0
	
	If btype=0	
		MonsterSpeed(NofMonsters)=BaseSpeed*2
	Else If btype=1 Or btype=2 Or btype=10 
		MonsterSpeed(NofMonsters)=BaseSpeed*2/3 
	Else If btype=3 Or btype=4 
		MonsterSpeed(NofMonsters)=BaseSpeed*3/2 
	Else If btype=5 Or btype=6 Or btype=7 Or btype=8 Or btype=9
		MonsterSpeed(NofMonsters)=BaseSpeed*2
	Else If btype=11 Or (btype>=13 And btype<=20)
		MonsterSpeed(NofMonsters)=BaseSpeed
	EndIf
	
	; Special Data
	For i=0 To 9
		MonsterData(NofMonsters,i)=0
	Next
	
	If btype=0
		MonsterData(NofMonsters,0)=-MonsterSpeed(NofMonsters)/2	; Bouncing Timer
		MonsterData(NofMonsters,1)=Rand(-16,16)	; turning speed (*8)
	Else If btype=1 Or btype=2 Or btype=5 Or btype=6 Or btype=7 Or btype=8 Or btype=9 Or btype=10 Or btype=11
		;MonsterData(NofMonsters,1)  RobotCurrentRotation (for turning while walking)
		If MonsterMovingX(NofMonsters)=0
			If MonsterMovingY(NofMonsters)=1	
				MonsterData(NofMonsters,1)=180
			Else 
				MonsterData(NofMonsters,1)=0
			EndIf
		Else
			If MonsterMovingX(NofMonsters)=1
				MonsterData(NofMonsters,1)=270
			Else 
				MonsterData(NofMonsters,1)=90
			EndIf
		EndIf
	Else If btype>=13 And btype<=20
		MonsterData(NofMonsters,1)=0
		MonsterData(NofMonsters,3)=0
		If btype<=16
			EntityTexture MonsterEntity(NofMonsters),WaterTexture
		Else
			EntityTexture MonsterEntity(NofMonsters),lavaTexture
		EndIf

	
	Else If btype=3 Or btype=4
		MonsterData(NofMonsters,0)=0 ; logic: turning timer to switch to straight
		
		;MonsterData(NofMonsters,1)  RobotCurrentRotation (for turning while walking)
		If MonsterMovingX(NofMonsters)=0
			If MonsterMovingY(NofMonsters)=1	
				MonsterData(NofMonsters,1)=180
			Else 
				MonsterData(NofMonsters,1)=0
			EndIf
		Else
			If MonsterMovingX(NofMonsters)=1
				MonsterData(NofMonsters,1)=270
			Else 
				MonsterData(NofMonsters,1)=90
			EndIf
		EndIf
		
		AnimateMD2 MonsterEntity(NofMonsters),1,.4,1,24
	
	
	EndIf
	If btype=7 Or btype=8	
		AnimateMD2 MonsterEntity(NofMonsters),2,.3,0,6
	EndIf

	NofMonsters=NofMonsters+1
	
	

End Function

Function ControlMonsters()

	For i=0 To NofMonsters-1
		
		If MonsterType(i)=0
			ControlBeachBall(i)
		Else If MonsterType(i)=1 Or MonsterType(i)=2
			ControlRobot(i)
		Else If MonsterType(i)=3 Or MonsterType(i)=4
			ControlBomber(i)
		Else If MonsterType(i)=5 Or MonsterType(i)=6
			ControlSaucer(i)
		Else If MonsterType(i)=7 Or MonsterType(i)=8
			ControlChomper(i)
		Else If MonsterType(i)=9
			ControlGhost(i)
		Else If MonsterType(i)=10
			ControlSpirit(i)
		Else If MonsterType(i)=11
			ControlBrokenZbot(i)
		Else If MonsterType(i)=12
			ControlMotherShip(i)
		Else If MonsterType(i)>=13 And MonsterType(i)<=20
			ControlShadowStinker(i)
		EndIf
	Next 

End Function

Function ControlBeachBall(i)


	; first check if dying first
	If MonsterDying(i)=True
		; yep, do a deflation animation
		
		; shrink it
		ScaleEntity MonsterEntity(i),.4*Float(MonsterDyingTimer(i))/Float(BaseSpeed*4),.38,.4*Float(MonsterDyingTimer(i))/Float(BaseSpeed*4)
		
		; move it
		MoveEntity MonsterEntity(i),0,.1,0
		If MonsterDyingTimer(i)>BaseSpeed*3
			TurnEntity MonsterEntity(i),0,0,4		
		Else
			TurnEntity MonsterEntity(i),0,0,-4
		EndIf
		
		; finally, remove entity if all gone
		MonsterDyingTimer(i)=MonsterDyingTimer(i)-1
		If MonsterDyingTimer(i)=0
			MonsterDead(i)=True
		EndIf
	
		Return ; don't do anything else
	EndIf

	; animation
	; Turn
	TurnEntity MonsterEntity(i),0,Float(MonsterData(i,1))/8.0,0
	
	; Bounce
	If MonsterData(i,0)>-MonsterSpeed(i)/2
		TranslateEntity MonsterEntity(i),0,Float(MonsterData(i,0))/500.0,0
		TranslateEntity MonsterShadowEntity(i),Float(MonsterData(i,0))/1000.0,0,-Float(MonsterData(i,0))/1000.0
		
		ScaleEntity MonsterEntity(i),.5,.5-.4*Float(Abs(MonsterData(i,0)))/Float(MonsterSpeed(i)/2),.5

		MonsterData(i,0)=MonsterData(i,0)-1
	EndIf
	
	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
		x=MonsterX(i)
		y=MonsterY(i)
	Else
		x=MonsterX2(i)
		y=MonsterY2(i)
	EndIf

	
	; check collision with Loof
	For k=1 To 4
		If (PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False)
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
			EndIf
		EndIf
	Next




	; now check if movement pattern is to be changed
	If MonsterMoving(i)=True Return

	dir=Rand(-1,1)
	If dir=0
		If Rand(100)<50
			dir=-1
		Else
			dir=1
		EndIf
		If CanMonsterMove(i,0,dir)=True 
			If DoesSoundExist= True Then SoundPitch SFXBounce,20000+Rand(0,4000)
			PlayMySound(SFXBounce,MonsterEntity(i))
			MoveMonster(i,0,dir)
			MonsterData(i,0)=MonsterSpeed(i)/2-1
		EndIf
	Else
		If CanMonsterMove(i,dir,0)=True 
			If DoesSoundExist= True Then SoundPitch SFXBounce,20000+Rand(0,4000)
			PlayMySound(SFXBounce,MonsterEntity(i))
			MoveMonster(i,dir,0)
			MonsterData(i,0)=MonsterSpeed(i)/2-1
		EndIf
	EndIf

End Function

Function ControlRobot(i)
	; first check if dying first
	If MonsterDying(i)=True
		MonsterDead(i)=True
	
		Return ; don't do anything else
	EndIf

	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
		x=MonsterX(i)
		y=MonsterY(i)
	Else
		x=MonsterX2(i)
		y=MonsterY2(i)
	EndIf
	
	; check collision with Loof
	For k=1 To 4
		If (PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False)
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
			EndIf
		EndIf
	Next


	; turning find goal
	If MonsterMovingX(i)=0
		If MonsterMovingY(i)=1	
			RobotRotationGoal=90
		Else 
			RobotRotationGoal=270
		EndIf
	Else
		If MonsterMovingX(i)=1
			RobotRotationGoal=0
		Else 
			RobotRotationGoal=180
		EndIf
	EndIf
	; turning into walk-direction
	If MonsterData(i,1)<>RobotRotationGoal
		If MonsterData(i,1)<RobotRotationGoal And RobotRotationGoal-MonsterData(i,1)<180
			MonsterData(i,1)=MonsterData(i,1)+6
		Else If MonsterData(i,1)>RobotRotationGoal And MonsterData(i,1)-RobotRotationGoal>180
			MonsterData(i,1)=MonsterData(i,1)+6
	  	Else
			MonsterData(i,1)=MonsterData(i,1)-6
		EndIf
		If MonsterData(i,1)=360 Then MonsterData(i,1)=0
		If MonsterData(i,1)=-6 Then MonsterData(i,1)=354
	EndIf

	RotateEntity MonsterEntity(i),-90,0,MonsterData(i,1)



	; now check if movement pattern is to be changed
	If MonsterMoving(i)=True Return

	; default - move to left/right
	If MonsterMovingX(i)=0 And MonsterMovingY(i)=0
		If MonsterType(i)=1
			MonsterMovingX(i)=-1
		Else 
			MonsterMovingX(i)=1
		EndIf
	EndIf
	
	; can it continue in current direction?
	If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
		MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
	Else
		;no, need to change direction
		If MonsterType(i)=1 ; left-handed movement
			; Try a turn to the left first
			k=MonsterMovingX(i)
			MonsterMovingX(i)=MonsterMovingY(i)
			MonsterMovingY(i)=-k
			If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
				MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
			Else
				; try a turn to the right next
				MonsterMovingX(i)=-MonsterMovingX(i)
				MonsterMovingY(i)=-MonsterMovingY(i)
				If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
					MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
				Else
					; try backing up last
					k=MonsterMovingX(i)
					MonsterMovingX(i)=-MonsterMovingY(i)
					MonsterMovingY(i)=k
					If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
						MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
					EndIf
				EndIf
			EndIf
		Else ; right-handed movement
			; Try a turn to the right first
			k=MonsterMovingX(i)
			MonsterMovingX(i)=-MonsterMovingY(i)
			MonsterMovingY(i)=k
			If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
				MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
			Else
				; try a turn to the right next
				MonsterMovingX(i)=-MonsterMovingX(i)
				MonsterMovingY(i)=-MonsterMovingY(i)
				If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
					MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
				Else
					; try backing up last
					k=MonsterMovingX(i)
					MonsterMovingX(i)=MonsterMovingY(i)
					MonsterMovingY(i)=-k
					If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
						MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
					EndIf
				EndIf
			EndIf
		EndIf

	EndIf
	
	



								
	


End Function


Function ControlSaucer(i)

	; first check if dying first
	If MonsterDying(i)=True
		; yep, do a deflation animation
		
		; shrink it
	;	ScaleEntity MonsterEntity(i),.4*Float(MonsterDyingTimer(i))/Float(BaseSpeed*4),.38,.4*Float(MonsterDyingTimer(i))/Float(BaseSpeed*4)
		
		; move it
		MoveEntity MonsterEntity(i),0,.01,.005
		If MonsterDyingTimer(i)>BaseSpeed*3
			TurnEntity MonsterEntity(i),-1,1,4		
		Else
			TurnEntity MonsterEntity(i),1,-1,-4
		EndIf
		
		; finally, remove entity if all gone
		MonsterDyingTimer(i)=MonsterDyingTimer(i)-1
		If MonsterDyingTimer(i)=0
			For j=1 To 7*DetailLevel
				CreateParticle(0,0,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.04,.04),Rnd(.01,.06),Rnd(-.04,.04),Rnd(-10,10),.001,60*DetailLevel)
				CreateParticle(0,5,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.06,.06),Rnd(.03,.09),Rnd(-.06,.06),Rnd(-10,10),.002,60*DetailLevel)
			Next
			PlayMySound(SFXBombExplo,MonsterEntity(i))
			MonsterDead(i)=True
		EndIf
	
		Return ; don't do anything else
	EndIf

	

	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
		x=MonsterX(i)
		y=MonsterY(i)
	Else
		x=MonsterX2(i)
		y=MonsterY2(i)
	EndIf

	; check collision with Loof
	For k=1 To 4
		If (PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False)
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
			EndIf
		EndIf
	Next
	
	

	RotateEntity MonsterEntity(i),-90,0,MonsterData(i,1)
	MonsterData(i,1)=MonsterData(i,1)+1
	If MonsterData(i,1)>=360 Then MonsterData(i,1)=0
	
	If MonsterData(i,2)<=0
		PlaySingleSound(MonsterX(i),MonsterY(i),4)
		MonsterData(i,2)=100
	Else
		MonsterData(i,2)=MonsterData(i,2)-1
	EndIf

	If MonsterData(i,3)>=0
		MonsterData(i,3)=MonsterData(i,3)-1
	EndIf
	If MonsterData(i,3)<0
		MonsterData(i,3)=0
	EndIf

	; now check if movement pattern is to be changed
	If MonsterMoving(i)=True Return
	
	
	If NofFireBalls<25 And MonsterData(i,9)=0

		; check if Stinky and Loof in Sight, if so - fire!
			
		
		For k=1 To 4
			If PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False
				If MonsterX(i)=PlayerX(k) Or MonsterX(i)=PlayerX2(k)
					If MonsterY(i)>PlayerY(k) 
						Tile=MapTile(MonsterX(i),MonsterY(i)-1)
						If Tile/100<>2 And Tile/100<>8 And (Tile/100<>10 Or Tile Mod 100>=50) And MonsterData(i,3)=0
		
							PlaySingleSound(MonsterX(i),MonsterY(i),3)
							CreateFireBall(MonsterX(i),MonsterY(i)-1,0,-1)
							MonsterData(i,3)=30
		
							
						EndIf
					Else If MonsterY(i)<PlayerY(k)
						Tile=MapTile(MonsterX(i),MonsterY(i)+1)
						If Tile/100<>2 And Tile/100<>8 And (Tile/100<>10 Or Tile Mod 100>=50) And MonsterData(i,3)=0
		
							PlaySingleSound(MonsterX(i),MonsterY(i),3)
							CreateFireBall(MonsterX(i),MonsterY(i)+1,0,1)
							MonsterData(i,3)=30
		
							
						EndIf
					EndIf
				Else If MonsterY(i)=PlayerY(k) Or MonsterY(i)=PlayerY2(k)
					If MonsterX(i)>PlayerX (k)
						Tile=MapTile(MonsterX(i)-1,MonsterY(i))
						If Tile/100<>2 And Tile/100<>8 And (Tile/100<>10 Or Tile Mod 100>=50) And MonsterData(i,3)=0
		
							PlaySingleSound(MonsterX(i),MonsterY(i),3)
							CreateFireBall(MonsterX(i)-1,MonsterY(i),-1,0)
							MonsterData(i,3)=30
		
							
						EndIf
					Else If MonsterX(i)<PlayerX(k)
						Tile=MapTile(MonsterX(i)+1,MonsterY(i))
						If Tile/100<>2 And Tile/100<>8 And (Tile/100<>10 Or Tile Mod 100>=50) And MonsterData(i,3)=0
		
							PlaySingleSound(MonsterX(i),MonsterY(i),3)
							CreateFireBall(MonsterX(i)+1,MonsterY(i),1,0)
							MonsterData(i,3)=30
		
							
						EndIf
					EndIf
				EndIf
			EndIf
		Next

	EndIf	

			


	; default - move to left/right
	If MonsterMovingX(i)=0 And MonsterMovingY(i)=0
		If MonsterType(i)=1
			MonsterMovingX(i)=-1
		Else 
			MonsterMovingX(i)=1
		EndIf
	EndIf
	
	; can it continue in current direction?
	If MonsterData(i,9)>0
		; created from mothership - 3 free moves
		MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
		MonsterData(i,9)=MonsterData(i,9)-1
		If MonsterX2(i)<0 Or MonsterY2(i)<0 Or MonsterX2(i)>=MapWidth Or MonsterY2(i)>=MapHeight 
			KillMonster(i)
			Return 
		EndIf
	Else If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
		MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
	Else
		;no, need to change direction
		If MonsterType(i)=5 ; left-handed movement
			; Try a turn to the left first
			k=MonsterMovingX(i)
			MonsterMovingX(i)=MonsterMovingY(i)
			MonsterMovingY(i)=-k
			If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
				MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
			Else
				; try a turn to the right next
				MonsterMovingX(i)=-MonsterMovingX(i)
				MonsterMovingY(i)=-MonsterMovingY(i)
				If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
					MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
				Else
					; try backing up last
					k=MonsterMovingX(i)
					MonsterMovingX(i)=-MonsterMovingY(i)
					MonsterMovingY(i)=k
					If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
						MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
					EndIf
				EndIf
			EndIf
		Else ; right-handed movement
			; Try a turn to the right first
			k=MonsterMovingX(i)
			MonsterMovingX(i)=-MonsterMovingY(i)
			MonsterMovingY(i)=k
			If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
				MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
			Else
				; try a turn to the right next
				MonsterMovingX(i)=-MonsterMovingX(i)
				MonsterMovingY(i)=-MonsterMovingY(i)
				If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
					MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
				Else
					; try backing up last
					k=MonsterMovingX(i)
					MonsterMovingX(i)=MonsterMovingY(i)
					MonsterMovingY(i)=-k
					If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
						MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
					EndIf
				EndIf
			EndIf
		EndIf

	EndIf
	
	



								
	


End Function





Function ControlBomber(i)

	If MonsterDead(i)=True Return
	
	;TurnEntity MonsterEntity(i),0,1,0
	
	Flag=False
	
	; check for stinky/loof around it
	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
		x2=MonsterX(i)
		y2=MonsterY(i)
	Else
		x2=MonsterX2(i)
		y2=MonsterY2(i)
	EndIf
	
	For k=1 To 4
		If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
			xsbomb(k)=PlayerX(k)
			ysbomb(k)=PlayerY(k)
		Else
			xsbomb(k)=PlayerX2(k)
			ysbomb(k)=PlayerY2(k)
		EndIf
	Next
	
	For x3=-1 To 1 Step 2
		
		x=x2+x3
		y=y2

		For k=1 To 4
			If Playerexists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False
				If x=xsbomb(k) And y=ysbomb(k)
					KillPlayer(k)
					Flag=True				
				EndIf
			EndIf
		Next
				
		x=x2
		y=y2+x3

		For k=1 To 4
			If Playerexists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False
				If x=xsbomb(k) And y=ysbomb(k)
					KillPlayer(k)
					Flag=True				
				EndIf
			EndIf
		Next


		
	Next
	If Flag=True 
		KillMonster(i)
		Return
	EndIf



	; turning find goal
	If MonsterMovingX(i)=0
		If MonsterMovingY(i)=1	
			RobotRotationGoal=180
		Else 
			RobotRotationGoal=0
		EndIf
	Else
		If MonsterMovingX(i)=1
			RobotRotationGoal=270
		Else 
			RobotRotationGoal=90
		EndIf
	EndIf
	; turning into walk-direction
	If MonsterData(i,1)<>RobotRotationGoal
		If MonsterData(i,1)<RobotRotationGoal And RobotRotationGoal-MonsterData(i,1)<180
			MonsterData(i,1)=MonsterData(i,1)+3
		Else If MonsterData(i,1)>RobotRotationGoal And MonsterData(i,1)-RobotRotationGoal>180
			MonsterData(i,1)=MonsterData(i,1)+3
	  	Else
			MonsterData(i,1)=MonsterData(i,1)-3
		EndIf
		If MonsterData(i,1)=360 Then MonsterData(i,1)=0
		If MonsterData(i,1)=-3 Then MonsterData(i,1)=357
	EndIf

	RotateEntity MonsterEntity(i),0,MonsterData(i,1),0






	; now check if movement pattern is to be changed
	If MonsterMoving(i)=True Return

	; default - move to right
	If MonsterMovingX(i)=0 And MonsterMovingY(i)=0
		MonsterMovingX(i)=1
	EndIf
	
	OldX=MonsterMovingX(i)
	OldY=MonsterMovingY(i)
	
	If MonsterType(i)=3	;left-handed movement
		
		; turn right
		k=MonsterMovingX(i)
		MonsterMovingX(i)=-MonsterMovingY(i)
		MonsterMovingY(i)=k
		If MonsterData(i,0)<4 And CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
			MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
			MonsterData(i,0)=MonsterData(i,0)+1
		Else
			MonsterData(i,0)=0
			; can't turn left, try continuing
			k=MonsterMovingX(i)
			MonsterMovingX(i)=MonsterMovingY(i)
			MonsterMovingY(i)=-k
			If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
				MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
			Else
				; can't, try turning left
				k=MonsterMovingX(i)
				MonsterMovingX(i)=MonsterMovingY(i)
				MonsterMovingY(i)=-k
				If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
					MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
				Else
					; can't, try backing up
					k=MonsterMovingX(i)
					MonsterMovingX(i)=MonsterMovingY(i)
					MonsterMovingY(i)=-k
					If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
						MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
					Else
						; can't go anywhere - boom!
						KillMonster(i)
						Return
						
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf

	
	If MonsterType(i)=4 ; right-handed movement
		
		; turn left
		k=MonsterMovingX(i)
		MonsterMovingX(i)=MonsterMovingY(i)
		MonsterMovingY(i)=-k
		If MonsterData(i,0)<4 And CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
			MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
			MonsterData(i,0)=MonsterData(i,0)+1
		Else
			MonsterData(i,0)=0
			; can't turn left, try continuing
			k=MonsterMovingX(i)
			MonsterMovingX(i)=-MonsterMovingY(i)
			MonsterMovingY(i)=k
			If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
				MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
			Else
				; can't, try turning right
				k=MonsterMovingX(i)
				MonsterMovingX(i)=-MonsterMovingY(i)
				MonsterMovingY(i)=k
				If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
					MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
				Else
					; can't, try backing up
					k=MonsterMovingX(i)
					MonsterMovingX(i)=-MonsterMovingY(i)
					MonsterMovingY(i)=k
					If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True
						MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
					Else
						; can't go anywhere - boom!
						KillMonster(i)
						Return
						
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	
	

		


End Function

Function ControlChomper(i)

	; first check if dying first
	If MonsterDying(i)=True
		MonsterDead(i)=True
	
		Return ; don't do anything else
	EndIf

	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
		x=MonsterX(i)
		y=MonsterY(i)
	Else
		x=MonsterX2(i)
		y=MonsterY2(i)
	EndIf
	
	; check collision with Loof
	For k=1 To 4
		If (PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False)
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
			EndIf
		EndIf
	Next


	
	; turning find goal
	If MonsterMovingX(i)=0
		If MonsterMovingY(i)=1	
			RobotRotationGoal=90
		Else 
			RobotRotationGoal=270
		EndIf
	Else
		If MonsterMovingX(i)=1
			RobotRotationGoal=0
		Else 
			RobotRotationGoal=180
		EndIf
	EndIf
	; turning into walk-direction
	If MonsterData(i,1)<>RobotRotationGoal
		If MonsterData(i,1)<RobotRotationGoal And RobotRotationGoal-MonsterData(i,1)<180
			MonsterData(i,1)=MonsterData(i,1)+6
		Else If MonsterData(i,1)>RobotRotationGoal And MonsterData(i,1)-RobotRotationGoal>180
			MonsterData(i,1)=MonsterData(i,1)+6
	  	Else
			MonsterData(i,1)=MonsterData(i,1)-6
		EndIf
		If MonsterData(i,1)=360 Then MonsterData(i,1)=0
		If MonsterData(i,1)=-6 Then MonsterData(i,1)=354
	EndIf

	RotateEntity MonsterEntity(i),0,-90-MonsterData(i,1),0

	
	
	;at the end of move?
	If MonsterMoving(i)=True Return
	
	

	
	;get closest distance
	
	For k=1 To 4
		If PlayerDead(k)=True Or PlayerDying(k)=True
			Return
		EndIf
	Next
	
	SoundPitch SFXSingle(8),Rand(19000,25000)
	
	;ok, first figure out who to go after
	If MonsterType(i)=7
		playerpicked=1
	Else
		playerpicked=2
	EndIf
	For k=1 To 4
		; is that player around?
		If (BothCompleted And 2^(playerpicked-1))=0 And PlayerExists(playerpicked)=True
			; yep - take it!
			Exit
		EndIf
		playerpicked=Playerpicked+1
		If PlayerPicked=5 Then PlayerPicked=1
	Next
			
	If Abs(PlayerX(PlayerPicked)-MonsterX(i))>=Abs(PlayerY(PlayerPicked)-MonsterY(i))
			; try left/right
			
			; right
			If PlayerX(PlayerPicked)>MonsterX(i)
				If CanMonsterMove(i,1,0) 
					MoveMonster(i,1,0)
					
					PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))

				Else
					; blocked: try up/down
					If PlayerY(PlayerPicked)>MonsterY(i)
						If CanMonsterMove(i,0,1)
							MoveMonster(i,0,1)
							PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
						EndIf
					Else If PlayerY(PlayerPicked)<MonsterY(i)
						If CanMonsterMove(i,0,-1) 
							MoveMonster(i,0,-1)
							PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
						EndIf

					EndIf
				EndIf
			Else 
			; left
				If CanMonsterMove(i,-1,0) 
					MoveMonster(i,-1,0)
					PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))

				Else
					; blocked: try up/down
					If PlayerY(PlayerPicked)>MonsterY(i)
						If CanMonsterMove(i,0,1) 
							MoveMonster(i,0,1)
							PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
						EndIf

					Else If PlayerY(PlayerPicked)<MonsterY(i)
						If CanMonsterMove(i,0,-1) 
							MoveMonster(i,0,-1)
							PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
						EndIf

					EndIf
	
				EndIf
			EndIf
		Else
			; try up/down first
			
			; down
			If PlayerY(PlayerPicked)>MonsterY(i)
				If CanMonsterMove(i,0,1) 
					MoveMonster(i,0,1)
					PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
					
				Else
					; blocked: try left/right
					If PlayerX(PlayerPicked)>MonsterX(i)
						If CanMonsterMove(i,1,0) 
							MoveMonster(i,1,0)
							PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
						EndIf

					Else If PlayerX(PlayerPicked)<MonsterX(i)
						If CanMonsterMove(i,-1,0) 
							MoveMonster(i,-1,0)
							PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
						EndIf

					EndIf
				EndIf
			Else 
			; up
				If CanMonsterMove(i,0,-1) 
					MoveMonster(i,0,-1)
					PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))

				Else
					; blocked: try left/right
					If PlayerX(PlayerPicked)>MonsterX(i)
						If CanMonsterMove(i,1,0) 
							MoveMonster(i,1,0)
							PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
						EndIf

					Else If PlayerX(PlayerPicked)<MonsterX(i)
						If CanMonsterMove(i,-1,0) 
							MoveMonster(i,-1,0)
							PlaySingleSound(MonsterX(i),MonsterY(i),Rand(8,11))
						EndIf

					EndIf
	
	
				EndIf
			EndIf
	
		EndIf
		
		

	
	

End Function

Function ControlGhost(i)

	; first check if dying first
	If MonsterDying(i)=True
		MonsterDead(i)=True
	
		Return ; don't do anything else
	EndIf

	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
		x=MonsterX(i)
		y=MonsterY(i)
	Else
		x=MonsterX2(i)
		y=MonsterY2(i)
	EndIf

	; check collision with Loof
	For k=1 To 4
		If (PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False)
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
			EndIf
		EndIf
	Next




	; turning find goal
	If MonsterMovingX(i)=0
		If MonsterMovingY(i)=1	
			RobotRotationGoal=90
		Else 
			RobotRotationGoal=270
		EndIf
	Else
		If MonsterMovingX(i)=1
			RobotRotationGoal=0
		Else 
			RobotRotationGoal=180
		EndIf
	EndIf
	; turning into walk-direction
	If MonsterData(i,1)<>RobotRotationGoal
		If MonsterData(i,1)<RobotRotationGoal And RobotRotationGoal-MonsterData(i,1)<180
			MonsterData(i,1)=MonsterData(i,1)+6
		Else If MonsterData(i,1)>RobotRotationGoal And MonsterData(i,1)-RobotRotationGoal>180
			MonsterData(i,1)=MonsterData(i,1)+6
	  	Else
			MonsterData(i,1)=MonsterData(i,1)-6
		EndIf
		If MonsterData(i,1)=360 Then MonsterData(i,1)=0
		If MonsterData(i,1)=-6 Then MonsterData(i,1)=354
	EndIf

	RotateEntity MonsterEntity(i),-90,0,MonsterData(i,1)

	
	
	;at the end of move?
	If MonsterMoving(i)=True Return

	
	; try a random sideways turn
	If Rand(0,100)<50 
		If Rand(0,100)<50
			If 	CanMonsterMove(i,MonsterMovingY(i),MonsterMovingX(i))=True
				MoveMonster(i,MonsterMovingY(i),MonsterMovingX(i))
				Return
			EndIf
		Else
			If 	CanMonsterMove(i,-MonsterMovingY(i),-MonsterMovingX(i))=True
				MoveMonster(i,-MonsterMovingY(i),-MonsterMovingX(i))
				Return
			EndIf
		EndIf
	EndIf
			
	; if not continue along straight line if possible
	If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True 
		MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
	Else
		; or if stuck try a random direction
		If Rand(0,100)<50
			If Rand(0,100)<50
				If CanMonsterMove(i,1,0)=True Then MoveMonster(i,1,0)
			Else
				If CanMonsterMove(i,-1,0)=True Then MoveMonster(i,-1,0)
			EndIf
		Else
			If Rand(0,100)<50
				If CanMonsterMove(i,0,1)=True Then MoveMonster(i,0,1)
			Else
				If CanMonsterMove(i,0,-1)=True Then MoveMonster(i,0,-1)
			EndIf
		EndIf
	EndIf
		

End Function

Function ControlSpirit(i)

	; first check if dying first
	If MonsterDying(i)=True
		MonsterDead(i)=True
	
		Return ; don't do anything else
	EndIf

	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
		x=MonsterX(i)
		y=MonsterY(i)
	Else
		x=MonsterX2(i)
		y=MonsterY2(i)
	EndIf

	; check collision with Loof
	For k=1 To 4
		If (PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False)
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
			EndIf
		EndIf
	Next




	; turning find goal
	If MonsterMovingX(i)=0
		If MonsterMovingY(i)=1	
			RobotRotationGoal=90
		Else 
			RobotRotationGoal=270
		EndIf
	Else
		If MonsterMovingX(i)=1
			RobotRotationGoal=0
		Else 
			RobotRotationGoal=180
		EndIf
	EndIf
	; turning into walk-direction
	If MonsterData(i,1)<>RobotRotationGoal
		If MonsterData(i,1)<RobotRotationGoal And RobotRotationGoal-MonsterData(i,1)<180
			MonsterData(i,1)=MonsterData(i,1)+6
		Else If MonsterData(i,1)>RobotRotationGoal And MonsterData(i,1)-RobotRotationGoal>180
			MonsterData(i,1)=MonsterData(i,1)+6
	  	Else
			MonsterData(i,1)=MonsterData(i,1)-6
		EndIf
		If MonsterData(i,1)=360 Then MonsterData(i,1)=0
		If MonsterData(i,1)=-6 Then MonsterData(i,1)=354
	EndIf

	RotateEntity MonsterEntity(i),-90,0,MonsterData(i,1)

	;If Rand(0,500)<5
	;	CreateParticle(0,3,0.1,MonsterX(i)+.5,0.5,-(MonsterY(i)+.5),Rnd(-.01,.01),Rnd(.02,.035),Rnd(-.01,.01),Rnd(-5,5),.0005,180)
	;EndIf
	
	;at the end of move?
	If MonsterMoving(i)=True Return

	
	If Rand(0,100)<50
		lefty=1
	Else
		lefty=-1
	EndIf
			
	; try to continue along straight line if possible
	If CanMonsterMove(i,MonsterMovingX(i),MonsterMovingY(i))=True 
		MoveMonster(i,MonsterMovingX(i),MonsterMovingY(i))
	
	; try a random sideways turn
	
	Else If	CanMonsterMove(i,MonsterMovingY(i)*lefty,MonsterMovingX(i)*lefty)=True
		MoveMonster(i,MonsterMovingY(i)*lefty,MonsterMovingX(i)*lefty)
	Else If	CanMonsterMove(i,-MonsterMovingY(i)*lefty,-MonsterMovingX(i)*lefty)=True
		MoveMonster(i,-MonsterMovingY(i)*lefty,-MonsterMovingX(i)*lefty)
	; backwards
	Else If CanMonsterMove(i,-MonsterMovingX(i),-MonsterMovingY(i))=True 
		MoveMonster(i,-MonsterMovingX(i),-MonsterMovingY(i))
	EndIf
					
	


End Function

Function ControlBrokenZbot(i)

	; first check if dying first
	If MonsterDying(i)=True
		MonsterDead(i)=True
	
		Return ; don't do anything else
	EndIf

	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
		x=MonsterX(i)
		y=MonsterY(i)
	Else
		x=MonsterX2(i)
		y=MonsterY2(i)
	EndIf

	; check collision with Loof
	For k=1 To 4
		If (PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False)
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
			EndIf
		EndIf
	Next





	MonsterData(i,1)=MonsterData(i,1)+3
	
	If MonsterData(i,1)>=360 Then MonsterData(i,1)=0
	
	RotateEntity MonsterEntity(i),-50,60*Sin(MonsterData(i,1)*2),MonsterData(i,1)

	

	
	;at the end of move?
	If MonsterMoving(i)=True Return

	; pick random direction
	If Rand(0,100)<50
		lefty=1
	Else
		lefty=-1
	EndIf
			
	If Rand(0,100)<50
		
		If CanMonsterMove(i,lefty,0)=True 
			MoveMonster(i,lefty,0)
		EndIf
	Else
		If CanMonsterMove(i,0,lefty)=True 
			MoveMonster(i,0,lefty)
		EndIf

	EndIf
End Function

Function ControlMotherShip(i)
	
	RotateEntity MonsterEntity(i),-90,0,MonsterData(i,1)/2
	MonsterData(i,1)=MonsterData(i,1)+1
	If MonsterData(i,1)>=720 Then MonsterData(i,1)=0
	
	If MonsterData(i,2)<=0
		PlaySingleSound(MonsterX(i),MonsterY(i),4)
		MonsterData(i,2)=400
	Else
		MonsterData(i,2)=MonsterData(i,2)-1
	EndIf
	
	If MonsterData(i,3)=0 ; not currently creating
		; check if any ufos around
		Flag=False
		For j=0 To NofMonsters-1
			If MonsterType(j)=5 Or MonsterType(j)=6
				Flag=True
			EndIf
		Next
		; no, start creating
		If Flag=False
			PlaySingleSound(-999,-1,14)
			MonsterData(i,3)=1
		EndIf
	EndIf
	If MonsterData(i,3)>0
		
		If MonsterData(i,3)<=300
			TranslateEntity MonsterEntity(i),0,.01,0
		Else If MonsterData(i,3)<=350
			; do nothing
		Else If MonsterData(i,3)<=351
			; create
			For k=0 To 3
				CreateMonster(MonsterX(i),MonsterY(i),Rand(5,6),k)
				MonsterData(NofMonsters-1,9)=3
			Next
			
		Else If MonsterData(i,3)<=400
			; do nothing
		Else If MonsterData(i,3)<=700
			TranslateEntity MonsterEntity(i),0,-.01,0
		Else If MonsterData(i,3)<=800
			; do nothing
		Else
			MonsterData(i,3)=-1
		EndIf
		MonsterData(i,3)=MonsterData(i,3)+1
	EndIf
	
		



End Function


Function ControlShadowStinker(i)

	
	If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>MonsterSpeed(i)/2)
		x=MonsterX(i)
		y=MonsterY(i)
	Else
		x=MonsterX2(i)
		y=MonsterY2(i)
	EndIf
	
	; check collision with players
	For k=1 To 4
		If (PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False)
			If PlayerMoving(k)=False Or (PlayerMoving(k)=True And PlayerMovingTimer(k)>=PlayerSpeed(k)/2)
				x2=PlayerX(k)
				y2=PlayerY(k)
			Else
				x2=PlayerX2(k)
				y2=PlayerY2(k)
			EndIf
			If x=x2 And y=y2
				KillPlayer(k)
			EndIf
		EndIf
	Next


	
	; turning find goal
	If MonsterMovingX(i)=0
		If MonsterMovingY(i)=1	
			RobotRotationGoal=90
		Else 
			RobotRotationGoal=270
		EndIf
	Else
		If MonsterMovingX(i)=1
			RobotRotationGoal=0
		Else 
			RobotRotationGoal=180
		EndIf
	EndIf
	; turning into walk-direction
	If MonsterData(i,1)<>RobotRotationGoal
		If MonsterData(i,1)<RobotRotationGoal And RobotRotationGoal-MonsterData(i,1)<180
			MonsterData(i,1)=MonsterData(i,1)+6
		Else If MonsterData(i,1)>RobotRotationGoal And MonsterData(i,1)-RobotRotationGoal>180
			MonsterData(i,1)=MonsterData(i,1)+6
	  	Else
			MonsterData(i,1)=MonsterData(i,1)-6
		EndIf
		If MonsterData(i,1)=360 Then MonsterData(i,1)=0
		If MonsterData(i,1)=-6 Then MonsterData(i,1)=354
	EndIf

	RotateEntity MonsterEntity(i),0,-90-MonsterData(i,1),0
	
	If MonsterData(i,3)>=0
		MonsterData(i,3)=MonsterData(i,3)-1
	EndIf
	If MonsterData(i,3)<0
		MonsterData(i,3)=0
	EndIf


	If NofFireBalls<25 And MonsterType(i)>16; And MonsterMovingTimer(i)=0

		; check if Stinky and Loof in Sight, if so - fire!
			
		
		For k=1 To 4
			If PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False
				If MonsterX(i)=PlayerX(k) Or MonsterX(i)=PlayerX2(k)
					If MonsterY(i)>PlayerY(k) 
						Tile=MapTile(MonsterX(i),MonsterY(i)-1)
						If Tile/100<>2 And Tile/100<>8 And (Tile/100<>10 Or Tile Mod 100>=50) And MonsterData(i,3)=0
		
							PlaySingleSound(MonsterX(i),MonsterY(i),3)
							If MonsterMoving(i)=False Or MonsterMovingTimer(i)>=MonsterSpeed(i)/2
								CreateFireBall(MonsterX(i),MonsterY(i)-1,0,-1)
							Else
								CreateFireBall(MonsterX2(i),MonsterY2(i)-1,0,-1)
							EndIf
							MonsterData(i,3)=30
		
							
						EndIf
					Else If MonsterY(i)<PlayerY(k)
						Tile=MapTile(MonsterX(i),MonsterY(i)+1)
						If Tile/100<>2 And Tile/100<>8 And (Tile/100<>10 Or Tile Mod 100>=50) And MonsterData(i,3)=0
		
							PlaySingleSound(MonsterX(i),MonsterY(i),3)
							If MonsterMoving(i)=False Or MonsterMovingTimer(i)>=MonsterSpeed(i)/2
								CreateFireBall(MonsterX(i),MonsterY(i)+1,0,1)
							Else
								CreateFireBall(MonsterX2(i),MonsterY2(i)+1,0,1)

							EndIf

							MonsterData(i,3)=30
		
							
						EndIf
					EndIf
				Else If MonsterY(i)=PlayerY(k) Or MonsterY(i)=PlayerY2(k)
					If MonsterX(i)>PlayerX (k)
						Tile=MapTile(MonsterX(i)-1,MonsterY(i))
						If Tile/100<>2 And Tile/100<>8 And (Tile/100<>10 Or Tile Mod 100>=50) And MonsterData(i,3)=0
		
							PlaySingleSound(MonsterX(i),MonsterY(i),3)
							If MonsterMoving(i)=False Or MonsterMovingTimer(i)>=MonsterSpeed(i)/2
								CreateFireBall(MonsterX(i)-1,MonsterY(i),-1,0)
							Else
								CreateFireBall(MonsterX2(i)-1,MonsterY2(i),-1,0)
							EndIf

							MonsterData(i,3)=30
		
							
						EndIf
					Else If MonsterX(i)<PlayerX(k)
						Tile=MapTile(MonsterX(i)+1,MonsterY(i))
						If Tile/100<>2 And Tile/100<>8 And (Tile/100<>10 Or Tile Mod 100>=50) And MonsterData(i,3)=0
		
							PlaySingleSound(MonsterX(i),MonsterY(i),3)
							If MonsterMoving(i)=False Or MonsterMovingTimer(i)>=MonsterSpeed(i)/2
								CreateFireBall(MonsterX(i)+1,MonsterY(i),1,0)
							Else
								CreateFireBall(MonsterX2(i)+1,MonsterY2(i),1,0)

							EndIf

							MonsterData(i,3)=30
		
							
						EndIf
					EndIf
				EndIf
			EndIf
		Next

	EndIf	

	
	
	

End Function





Function CanMonsterMove(i,dx,dy)
	; defaults to true, but checks if anything is in the way
	
	x=MonsterX(i)+dx
	y=MonsterY(i)+dy
	
	; edge of level
	If x<0 Or y<0 Or x>=MapWidth Or y>=MapHeight Then Return False

	
	Tile=MapTile(x,y)
	; can only walk on floor, electrotiles, open gates, buttons
	If Tile/100 = 1 Or Tile/100=7 Or (Tile/100=10 And Tile Mod 100>=50) Or Tile/100=11 Or Tile/100=13
		;ok
	Else If (tile/100=3 Or tile/100=9) And (MonsterType(i)=1 Or MonsterType(i)=2 Or MonsterType(i)=11)
		;also ok - robot can walk into water (but will then explode)
	Else If (tile/100=3 Or tile/100=4 Or tile/100=5 Or tile/100=6 Or tile/100=9) And (MonsterType(i)=5 Or MonsterType(i)=6 Or MonsterType(i)=10)
		;also ok - saucers can go anywhere
	Else If (tile/100=4 Or tile/100=5 Or tile/100=6) And (MonsterType(i)=9)
		;ghosts go anywhere except over water/lava
	Else If tile/100=10 And MonsterType(i)=9
		;ghost can also go through closed gates
	Else If tile/100=17 And (MonsterType(i)=5 Or MonsterType(i)=6 Or MonsterType(i)=10)
		;spirits and UFOs can go over transporters
	Else
		Return False
	EndIf
	
	; ghost has no problem with objects
	If MonsterType(i)=9 
		For j=0 To NofBoxes-1
			; other than ghostboxes
			If (x=BoxX(j) Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j)) And BoxType(j)=5
				Return False
			EndIf
		Next
		Return True
	EndIf

	
	; now check boxes - can't move into any
	If monstertype(i)<13 Or monstertype(i)>20
		For j=0 To NofBoxes-1
			If (x=BoxX(j) Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j))
				Return False
			EndIf
		Next
	Else ; shadowstinkers can move and push
		
		For j=0 To NofBoxes-1
			If (BoxX(j)=x And BoxY(j)=y) Or (BoxX2(j)=x And BoxY2(j)=y)
				; yep found a box
				; is it moving
				If BoxMoving(j)=True
					; box moving away from Loof: no problem [LATER: or sideways at end of movement]
					If BoxMovingX(j)=dx And BoxMovingY(j)=dy 
						
									
					; box moving towards Loof: boom!
					Else If BoxMovingX(j)=-dx And BoxMovingY(j)=-dy
						Killbox(j)
						;Return False
					Else
						; box moving sideways: just bounce Loofoff
						Return False
					EndIf
					
				
				EndIf
				; not a moving box: check if the box can be moved further
				If (MapTile(BoxX(j),BoxY(j))/100<>5 Or (MapTile(BoxX(j),BoxY(j)) Mod 100) >=8 Or ConveyorActive(1)=False) And (MapTile(BoxX(j),BoxY(j))/100<>5 Or (MapTile(BoxX(j),BoxY(j)) Mod 100) <8 Or ConveyorActive(2)=False)	; don't even check if box on moving conveyor
					If CanBoxMove(j,dx,dy)=True 
						; move it
						; check if Loofon ice, if so move box faster
						MoveBox(j,dx,dy,0)
						
												
						; and finally, check if it is a linksphere. If so, move all of the same type
						If BoxType(j)>=9 And BoxType(j)<=12
							For k=0 To NofBoxes-1
								If j<>k And BoxType(j)=BoxType(k) And BoxX(j)<>101
									; found other linksphere
									; don't move if already moving
									If BoxMoving(k)=False
										If (MapTile(BoxX(k),BoxY(k))/100<>5 Or (MapTile(BoxX(k),BoxY(k)) Mod 100) >=8 Or ConveyorActive(1)=False) And (MapTile(BoxX(k),BoxY(k))/100<>5 Or (MapTile(BoxX(k),BoxY(k)) Mod 100) <8 Or ConveyorActive(2)=False)	; don't even check if box on moving conveyor
	
											If CanBoxMove(k,dx,dy)=True
												MoveBox(k,dx,dy,0)
											EndIf
										EndIf
									EndIf
								EndIf
							Next
						EndIf
						;Return True
					Else 
						; box can't be moved, so neither can Loof
						Return False
					EndIf
				Else 
					Return False
				EndIf
			EndIf
		Next
	
	EndIf
	
	; now check other monsters - can't move into any
	For j=0 To NofMonsters-1
		If (x=MonsterX(j) Or x=MonsterX2(j)) And (y=MonsterY(j) Or y=MonsterY2(j)) And i<>j
			Return False
		EndIf
	Next
	
	; now check items - can't move into any
	For j=0 To NofItems-1
		If x=ItemX(j) And y=ItemY(j)
			; except a rainbow spirit
			If ItemType(j)=0 And (MonsterType(i)=10 Or (MonsterType(i)>=13 And MonsterType(i)<=20))
				CreateParticle(0,3,0.3,ItemX(j)+.5,1,-ItemY(j)-.2,0,.02,0,Rnd(4,8),0,120)
				PlayMySound(SFXSingle(15),MonsterEntity(i))
				NofCrystals=NofCrystals-1
				; and open door if none left
				If NofCrystals=0
					PlayMySound(SFXLastCoin,0)
					ToggleLaserGate(0,True)
				EndIf
				KillItem(j)
			Else If ItemType(j)=3 And MonsterType(i)>=13 And MonsterType(i)<=20
				nofbonuscoins=nofbonuscoins-1
		
				PlayMySound(SFXSingle(15),MonsterEntity(i))

				
				CurrentLevelScore=CurrentLevelScore+250
				CreateParticle(0,9,0.5,ItemX(j)+.5,1,-ItemY(j)-.2,0,.02,0,0,0,120)
				KillItem(j)

			Else
				Return False
			EndIf
		EndIf
	Next


	
	
	
	Return True
	
End Function

Function MoveMonster(i,dx,dy)

	

	MonsterMovingTimer(i)=MonsterSpeed(i)
	MonsterX2(i)=MonsterX(i)+dx
	MonsterY2(i)=MonsterY(i)+dy
	MonsterMovingX(i)=dx
	MonsterMovingY(i)=dy

	


	MonsterMoving(i)=True

	

End Function

Function EndMoveMonster(i)

	Oldx=MonsterX(i)
	OldY=MonsterY(i)

	
	
	MonsterX(i)=MonsterX2(i)
	MonsterY(i)=MonsterY2(i)
	
	; check old tile
	If MapTile(Oldx,Oldy)/100=11 And MonsterType(i)<>5 And MonsterType(i)<>6 And MonsterType(i)<>9;buttons
		StepOffButton(Oldx,Oldy)
	EndIf

	MonsterMoving(i)=False
	
	; check new tile
	If MapTile(MonsterX(i),MonsterY(i))/100=11 And MonsterType(i)<>5 And MonsterType(i)<>6 And MonsterType(i)<>9;buttons
		StepOnButton(MonsterX(i),MonsterY(i))
	EndIf
	If (MapTile(MonsterX(i),MonsterY(i))/100=3 Or MapTile(MonsterX(i),MonsterY(i))/100=9) And (MonsterType(i)=1 Or MonsterType(i)=2 Or MonsterType(i)=11); water
		KillMonster(i)
	EndIf

	
End Function

Function KillMonster(i)

	

	MonsterDying(i)=True

	; stepped off button
	If MapTile(MonsterX(i),MonsterY(i))/100=11 And MonsterType(i)<>5 And MonsterType(i)<>6 And MonsterType(i)<>9
		StepOffButton(MonsterX(i),MonsterY(i))
	EndIf

	; sound + bonus?
	If MonsterType(i)=0
		PlayMySound(SFXCoilyDeath,MonsterEntity(i))
		CurrentLevelScore=CurrentLevelScore+250
		CreateParticle(0,9,0.5,MonsterX(i)+.5,1,-MonsterY(i)-.2,0,.01,0,0,0,120)
	Else
		If MonsterType(i)<>5 And MonsterType(i)<>6 Then PlayMySound(SFXBombExplo,MonsterEntity(i))
		CurrentLevelScore=CurrentLevelScore+250
		CreateParticle(0,9,0.5,MonsterX(i)+.5,1,-MonsterY(i)-.2,0,.01,0,0,0,120)
	EndIf


	; Remove the entity logically first, actual death is taken care of later
	
	If MonsterShadowEntity(i)>0
		FreeEntity MonsterShadowEntity(i)
		MonsterShadowEntity(i)=0
	EndIf

	;TurnEntity MonsterEntity(i),0,0,Rand(-30,30) 
	;If MonsterType(i)=0
		MonsterDyingTimer(i)=BaseSpeed*4
	;Else
	;	MonsterDyingTimer(i)=BaseSpeed*3
	;EndIf
	If MonsterType(i)=5 Or MonsterType(i)=6
		; saucer
		SoundPitch SFXSaucerDie,16000
		PlayMySound(SFXSaucerDie,MonsterEntity(i))

		For j=1 To 3*DetailLevel
			CreateParticle(0,0,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.04,.04),Rnd(.01,.06),Rnd(-.04,.04),Rnd(-10,10),.001,60*DetailLevel)
		;	CreateParticle(0,5,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.06,.06),Rnd(.03,.09),Rnd(-.06,.06),Rnd(-10,10),.002,60*DetailLevel)

		Next
	Else If MonsterType(i)=12
		; mother ship - big explo
		MonsterDead(i)=True
		SoundPitch SFXSaucerDie,10000
		PlayMySound(SFXSaucerDie,MonsterEntity(i))

		; create explosion of particles
		For j=1 To 14*DetailLevel
			CreateParticle(0,0,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.14,.14),Rnd(.01,.06),Rnd(-.14,.14),Rnd(-10,10),.0005,120*DetailLevel)
			CreateParticle(0,5,0.1,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.26,.26),Rnd(.08,.2),Rnd(-.26,.26),Rnd(-30,30),.002,120*DetailLevel)

		Next
		For j=1 To 14*DetailLevel
			CreateParticle(0,0,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.04,.04),Rnd(.01,.06),Rnd(-.04,.04),Rnd(-10,10),.0005,120*DetailLevel)
			CreateParticle(0,5,0.1,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.06,.06),Rnd(.08,.2),Rnd(-.06,.06),Rnd(-30,30),.002,120*DetailLevel)

		Next

	
	Else If MonsterType(i)>2 And MonsterType(i)<>11
		; blow up immediately
		MonsterDead(i)=True
		; create explosion of particles
		For j=1 To 7*DetailLevel
			CreateParticle(0,0,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.04,.04),Rnd(.01,.06),Rnd(-.04,.04),Rnd(-10,10),.001,60*DetailLevel)
			CreateParticle(0,5,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.06,.06),Rnd(.03,.09),Rnd(-.06,.06),Rnd(-10,10),.002,60*DetailLevel)

		Next
	
	
		


	
	Else If MonsterType(i)>0
		; blow up immediately
		MonsterDead(i)=True
		; create explosion of particles
		For j=1 To 14*DetailLevel
			;CreateParticle(0,0,0.2,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.04,.04),Rnd(.01,.06),Rnd(-.04,.04),Rnd(-10,10),.001,60*DetailLevel)
			CreateParticle(0,5,0.1,EntityX(MonsterEntity(i)),EntityY(MonsterEntity(i)),EntityZ(MonsterEntity(i)),Rnd(-.16,.16),Rnd(.08,.2),Rnd(-.16,.16),Rnd(-30,30),.004,60*DetailLevel)

		Next
	EndIf

	
	; BOMBER - Kill everything around it
	If MonsterType(i)=3 Or MonsterType(i)=4
		If MonsterMoving(i)=False Or (MonsterMoving(i)=True And MonsterMovingTimer(i)>=MonsterSpeed(i)/2)
			x2=MonsterX(i)
			y2=MonsterY(i)
		Else
			x2=MonsterX2(i)
			y2=MonsterY2(i)
		EndIf

		For x3=-1 To 1 Step 2
			
			x=x2+x3
			y=y2
			For j=0 To NofBoxes-1
				If BoxDead(j)=False 
					If (x=BoxX(j) Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j))
						KillBox(j)
					EndIf
				EndIf
			Next
			For j=0 To NofMonsters-1
				If MonsterDead(j)=False And MonsterDying(j)=False
					If (x=MonsterX(j) Or x=MonsterX2(j)) And (y=MonsterY(j) Or y=MonsterY2(j))
						If MonsterType(j)<>9 Then KillMonster(j)
					EndIf
				EndIf
			Next
			
			For k=1 To 4
				If PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False
					If (x=PlayerX(k) Or x=PlayerX2(k)) And (y=PlayerY(k) Or y=PlayerY2(k))
						KillPlayer(k)
					EndIf
				EndIf
			Next
			
			x=x2
			y=y2+x3
			For j=0 To NofBoxes-1
				If BoxDead(j)=False 
					If (x=BoxX(j) Or x=BoxX2(j)) And (y=BoxY(j) Or y=BoxY2(j))
						KillBox(j)
					EndIf
				EndIf
			Next
			For j=0 To NofMonsters-1
				If MonsterDead(j)=False And MonsterDying(j)=False
					If (x=MonsterX(j) Or x=MonsterX2(j)) And (y=MonsterY(j) Or y=MonsterY2(j))
						If MonsterType(j)<>9 Then KillMonster(j)
					EndIf
				EndIf
			Next
			For k=1 To 4
				If PlayerExists(k)=True And PlayerDying(k)=False And PlayerDead(k)=False
					If (x=PlayerX(k) Or x=PlayerX2(k)) And (y=PlayerY(k) Or y=PlayerY2(k))
						KillPlayer(k)
					EndIf
				EndIf
			Next


				

					
		
		Next
	EndIf

	
	
	MonsterX(i)=0
	MonsterY(i)=0
	MonsterX2(i)=0
	MonsterY2(i)=0
	MonsterMoving(i)=False

	
End Function

Function CleanUpMonsters()
	; removes any dead boxes
	For i=0 To NofMonsters-1
		If MonsterDead(i)=True
			; yep, get rid of it by overwriting it with later boxes
			FreeEntity MonsterEntity(i)
			
			If i<NofMonsters-1 ;unless it is the last one
				For j=i To NofMonsters-1
					MonsterEntity(j)=MonsterEntity(j+1)
					MonsterShadowEntity(j)=MonsterShadowEntity(j+1)
					MonsterType(j)=MonsterType(j+1)
					MonsterX(j)=MonsterX(j+1)
					MonsterY(j)=MonsterY(j+1)
					MonsterX2(j)=MonsterX2(j+1)
					MonsterY2(j)=MonsterY2(j+1)
					MonsterMoving(j)=MonsterMoving(j+1)
					MonsterMovingX(j)=MonsterMovingX(j+1)
					MonsterMovingY(j)=MonsterMovingY(j+1)
					MonsterMovingTimer(j)=MonsterMovingTimer(j+1)
					MonsterSpeed(j)=MonsterSpeed(j+1)
					MonsterDying(j)=MonsterDying(j+1)
					MonsterDyingTimer(j)=MonsterDyingTimer(j+1)
					MonsterDead(j)=MonsterDead(j+1)
					For k=0 To 9
						MonsterData(j,k)=MonsterData(j+1,k)
					Next
				Next
				i=i-1

			EndIf
			NofMonsters=NofMonsters-1
		EndIf
	Next
End Function

; -------------------------------------------
; Items
; -------------------------------------------

Function CreateItem(x,y,btype)

	; input tile position x/y
	
	
	ItemEntity(NofItems)=CopyEntity(ItemMesh(btype)) 
	
	ItemType(NofItems)=btype
	
	PositionEntity ItemEntity(NofItems),x+.5,0.5,-(y+.5)
	
	ItemX(NofItems)=x
	ItemY(NofItems)=y
	ItemDead(NofItems)=False
	
	
	
	
	If btype=0 ; crystal
		;EntityTexture ItemEntity(NofItems),RainbowTexture
		NofCrystals=NofCrystals+1
		ItemShadowEntity(NofItems)=CopyEntity(ItemShadowMesh(btype))
		PositionEntity ItemShadowEntity(NofItems),x+.6,0.01,-(y+.6)
	EndIf	
	If btype=1 ; endgate
		ItemShadowEntity(NofItems)=CreateFlatSquare(RoundShadowTexture)
		PositionEntity ItemShadowEntity(NofItems),x,0.01,-(y)
	EndIf	
	If btype=3 ; bonus
		NofBonusCoins=NofBonusCoins+1
		;EntityTexture ItemEntity(NofItems),RainbowTexture
		ItemShadowEntity(NofItems)=CopyEntity(ItemShadowMesh(0))
		PositionEntity ItemShadowEntity(NofItems),x+.5,0.01,-(y+.55)
	EndIf	

	If btype=2
		ItemData(NofItems)=Rand(360)
	EndIf
	
	NofItems=NofItems+1

	

End Function

Function ControlItems()

	For i=0 To NofItems-1
	
		; general control
		If ItemType(i)=0	; coin - rotate
			TurnEntity ItemEntity(i),0,-2,0
			TurnEntity ItemShadowEntity(i),0,-2,0
			;size#=.8+.2*Sin(EntityYaw(ItemEntity(i)))
			;ScaleEntity ItemEntity(i),.8*size,.8*size,.5*size

		EndIf
		
		If ItemType(i)=1	; exit - rotate/pulsate
			TurnEntity ItemEntity(i),0,-1,0
			size#=.4+.1*Sin(EntityYaw(ItemEntity(i)))
			ScaleEntity ItemEntity(i),size,size/2,size
			
			ScaleEntity ItemShadowEntity(i),size*2-.2,size,size*2-.2
			PositionEntity ItemShadowEntity(i),ItemX(i)+.6-size,0.01,-ItemY(i)-.6+size
			
			; and create rainbowstar particles
			If Rand(0,100)<5
			
				;px#=ItemX(i)+.5
				;py#=0.5
				;pz#=-(ItemY(i)+.5)
				;pdx#=Rnd(-.008,.008)
				;pdy#=Rnd(.02,.03)
				;pdz#=Rnd(-.008,.008)
				;pddy#=.0005
				;For j=1 To 180
			;		px=px+pdx
			;		py=py+pdy
			;		pz=pz+pdz
			;		pdy=pdy-pddy
			;	Next
			;	CreateParticle(3,px,py,pz,-pdx,Abs(pdy),-pdz,Rnd(-5,5),pddy,180)
				CreateParticle(0,3,0.1,ItemX(i)+.5,0.5,-(ItemY(i)+.5),Rnd(-.01,.01),Rnd(.02,.035),Rnd(-.01,.01),Rnd(-5,5),.0005,180)
			EndIf
			
			
		EndIf
		
		If ItemType(i)=2	; time - rotate
			If (watertexu+itemdata(i)/1000.0) Mod .5>=.4 
				RotateEntity ItemEntity(i),0,itemdata(i)-watertexv*2000.0,10*Sin(watertexu*36000.0)
			Else
				RotateEntity ItemEntity(i),0,itemdata(i)-watertexv*2000.0,0
			EndIf
			;TurnEntity ItemShadowEntity(i),0,-2,0
			;size#=.8+.2*Sin(EntityYaw(ItemEntity(i)))
			;ScaleEntity ItemEntity(i),.8*size,.8*size,.5*size

		EndIf
		
		If ItemType(i)=3	; bonus coin - rotate
			TurnEntity ItemEntity(i),0,0,-2
			;TurnEntity ItemShadowEntity(i),0,-2,0
			;size#=.8+.2*Sin(EntityYaw(ItemEntity(i)))
			;ScaleEntity ItemEntity(i),.8*size,.8*size,.5*size

		EndIf
		
		


			
		
		; check if they are picked up (if moving stinky is halfway on its way to the tile)
		; do this last since items might be gone after killitem
		For k=1 To 4
			If playerexists(k)=True
				If ItemX(i)=PlayerX2(k) And ItemY(i)=PlayerY2(k) 
					If PlayerMoving(k)=True And PlayerMovingTimer(k)<PlayerSpeed(k)/2 And (BothCompleted And 2^(k-1))=0
						; yep, pick up
						PickUpItem(i,k)
						Return
					EndIf
				EndIf
			EndIf
		Next
		
		
	
	Next
End Function

Function PickUpItem(i,who)
	; Who is player 1-4
	
	If ItemType(i)=0	; crystal
		
		PlayMySound(SFXPlayerCoin(who),0)
		
		CreateParticle(0,3,0.3,ItemX(i)+.5,1,-ItemY(i)-.2,0,.02,0,Rnd(4,8),0,120)

		NofCrystals=NofCrystals-1
		; and open door if none left
		If NofCrystals=0
			PlayMySound(SFXLastCoin,0)
			ToggleLaserGate(0,True)
		EndIf
	EndIf
	If ItemType(i)=1	; the end
		
		
		PlayerShrink(who)=60
		PlayMySound(SFXPlayerEnd(who),0)
		HideEntity PlayerShadowEntity(who)
		BothCompleted=BothCompleted+2^(who-1)
	
		; is noone else around? --> end level
		flag=True
		For k=1 To 4
			If PlayerExists(k)=True And (BothCompleted And 2^(k-1))=0
				flag=False
			EndIf
		Next
		If flag=True
			LevelCompleted=1
			LevelCompletedTimer=0
			If Language=0
				a=Rand(0,100)
				If a<20
					ex$="WELL DONE!!! LEVEL COMPLETED!!!"
				Else If a<40
					ex$="CONGRATULATIONS!!! LEVEL COMPLETED!!!"
				Else If a<60
					ex$="EXCELLENT!!! LEVEL COMPLETED!!!"
				Else If a<80
					ex$="GOOD JOB!!! LEVEL COMPLETED!!!"
				Else 
					ex$="NICELY DONE!!! LEVEL COMPLETED!!!"
				EndIf
				If MapTime<1 Then MapTime=1
				If NoTimeLimit=1 Then MapTime=1
				ex$=ex$+"# #BONUS SCORE: "+Str$(CurrentLevelScore)+"    TIME BONUS: "+Str$(MapTime*10)
				CurrentLevelScore=CurrentLevelScore+MapTime*10
				ex$=ex$+"#TOTAL SCORE: "+Str$(CurrentLevelScore)
				
				If CurrentLevel<>-999
					If CurrentPlayerLevelScore(CurrentLevel)>0 
						If CurrentLevelScore>CurrentPlayerLevelScore(CurrentLevel)
							ex$=ex$+"#!!! NEW LEVEL RECORD !!!"
						Else
							ex$=ex$+"#NO NEW RECORD."
						EndIf
					EndIf
				Else
					; custom level - has it been played?
					For k=0 To CurrentPlayerExtraLevels-1
						If CurrentPlayerExtraLevelID(k)=FileDirID(CurrentFileDir); And CurrentPlayerExtraLevelID2(k)=FileDirID2(CurrentFileDir)
							; yes played it
							
							If CurrentLevelScore>CurrentPlayerExtraLevelScore(k)
								ex$=ex$+"#!!! NEW LEVEL RECORD !!!"
								CurrentPlayerExtraLevelScore(k)=CurrentLevelScore
								
								If CurrentPlayerExtraLevelPerfect(k)=0
									If NofTotalBonusCoins=0 Or NofBonusCoins=0
										Print "YEA"
										Delay 1000
										CurrentPlayerExtraLevelPerfect(k)=1
									EndIf
								EndIf
																
							Else
								ex$=ex$+"#NO NEW RECORD."
							EndIf
							k=200000
						EndIf
					Next
					If k<200000
						; no, first time
						CurrentPlayerExtraLevelScore(CurrentPlayerExtraLevels)=CurrentLevelScore
						CurrentPlayerExtraLevelID(CurrentPlayerExtraLevels)=FileDirID(CurrentFileDir)
						CurrentPlayerExtraLevelID2(CurrentPlayerExtraLevels)=FileDirID2(CurrentFileDir)
						
						If CurrentPlayerExtraLevelPerfect(CurrentPlayerExtraLevels)=0
							If NofTotalBonusCoins=0 Or NofBonusCoins=0
								CurrentPlayerExtraLevelPerfect(CurrentPlayerExtraLevels)=1
							EndIf
						EndIf
						
						CurrentPlayerExtraLevels=CurrentPlayerExtraLevels+1
					EndIf
					
				EndIf
				
			Else If Language=1
				ex$="GUT GEMACHT!!! LEVEL BEENDET!!!"
				If MapTime<1 Then MapTime=1
				If NoTimeLimit=1 Then MapTime=1

				ex$=ex$+"# #BONUS PUNKTE: "+Str$(CurrentLevelScore)+"    ZEIT BONUS: "+Str$(MapTime*10)
				CurrentLevelScore=CurrentLevelScore+MapTime*10
				ex$=ex$+"#PUNKTE INSGESAMMT: "+Str$(CurrentLevelScore)
				If CurrentLevel<>-999
					If CurrentPlayerLevelScore(CurrentLevel)>0 
						If CurrentLevelScore>CurrentPlayerLevelScore(CurrentLevel)
							ex$=ex$+"#!!! NEUER LEVEL REKORD !!!"
						Else
							ex$=ex$+"#LEIDER KEIN NEUER REKORD."
						EndIf
					EndIf
				Else
				
				EndIf

			Else If Language=2
				ex$="BIEN REUSSI!!! NIVEAU COMPLETE!!!"
				If MapTime<1 Then MapTime=1
				If NoTimeLimit=1 Then MapTime=1

				ex$=ex$+"# #SCORE BONUS: "+Str$(CurrentLevelScore)+"    SCORE TEMPS: "+Str$(MapTime*10)
				CurrentLevelScore=CurrentLevelScore+MapTime*10
				ex$=ex$+"#SCORE TOTAL: "+Str$(CurrentLevelScore)
				If CurrentLevel<>-999
					If CurrentPlayerLevelScore(CurrentLevel)>0 
						If CurrentLevelScore>CurrentPlayerLevelScore(CurrentLevel)
							ex$=ex$+"#!!! NOUVEAU RECORD POUR CE NIVEAU !!!"
						Else
							ex$=ex$+"#PAS DE NOUVEAU RECORD."
						EndIf
					EndIf
				Else
				
				EndIf



			
			
			EndIf
			If CurrentLevel<>-999
				
				If CurrentLevelScore>CurrentPlayerLevelScore(CurrentLevel) 
					; store new score only if score is better 
					CurrentPlayerLevelScore(CurrentLevel)=CurrentLevelScore
				EndIf
				If CurrentPlayerLevelPerfect(CurrentLevel)=0
					If NofTotalBonusCoins=0 Or NofBonusCoins=0
						CurrentPlayerLevelPerfect(CurrentLevel)=1
						
					EndIf
				EndIf
			Else
			
			EndIf
			
			StartSign(ex$,7,2,1)
			
			SavePlayer()



		Else 
			FocusSwitchTimer=60
			SwitchPlayer(0)
		EndIf
		
	EndIf
	If ItemType(i)=2	; time
		MapTime=MapTime+30
		
		PlayMySound(SFXPlayerClock(who),0)
		

	EndIf
	If ItemType(i)=3	; score
		nofbonuscoins=nofbonuscoins-1
		
		PlayMySound(SFXPlayerBonus(who),0)
		
		CurrentLevelScore=CurrentLevelScore+250
		CreateParticle(0,9,0.5,ItemX(i)+.5,1,-ItemY(i)-.2,0,.02,0,0,0,120)
	EndIf
		
	
	; and remove
	If ItemType(i)<>1 KillItem(i)			
End Function

Function KillItem(i)

	ItemDead(i)=True
	
End Function

Function CleanUpItems()
	; removes any dead boxes
	For i=0 To NofItems-1
		If ItemDead(i)=True
			; yep, get rid of it by overwriting it with later boxes
			FreeEntity ItemEntity(i)
			If ItemType(i)=0 Or ItemType(i)=1 Or ItemType(i)=3
				FreeEntity ItemShadowEntity(i)
			EndIf
			
			If i<NofItems-1 ;unless it is the last one
				For j=i To NofItems-1
					ItemEntity(j)=ItemEntity(j+1)
					ItemShadowEntity(j)=ItemShadowEntity(j+1)
					ItemType(j)=ItemType(j+1)
					ItemX(j)=ItemX(j+1)
					ItemY(j)=ItemY(j+1)
					ItemDead(j)=ItemDead(j+1)
					ItemData(j)=ItemData(j+1)
					
				Next
				i=i-1

			EndIf
			NofItems=NofItems-1
		EndIf
	Next
End Function

Function CreateFish(x,y)

	FishEntity(NofFish)=CopyEntity(FishMesh)
	
	;ScaleEntity FishEntity(NofFish),14,14,14 
		
	PositionEntity FishEntity(NofFish),x+.75,-0.5,-(y+.4)+2
	
	FishTimer(NofFish)=0.0
	FishX(NofFish)=x
	FishY(NofFish)=y
	FishSpeed(NofFish)=Rnd(.5,1.5)
	If Rand(0,100)<50
		; other way
		FishSpeed(NofFish)=-FishSpeed(NofFish)
		TurnEntity FishEntity(NofFish),0,180,0
	EndIf
	
	NofFish=NofFish+1

	

End Function

Function ControlFish()
	For i=0 To NofFish-1
		If FishSpeed(NofFish)>0
			FishTimer(i)=FishTimer(i)+FishSpeed(i)
			If FishTimer(i)>=360 Then FishTimer(i)=FishTimer(i)-360
			PositionEntity FishEntity(i),FishX(i)+.75-1.5*Sin(FishTimer(i)),-0.5,-(FishY(i)+.4)+1.5*Cos(FishTimer(i))
			TurnEntity FishEntity(i),0,FishSpeed(i),0 
		Else
			FishTimer(i)=FishTimer(i)-FishSpeed(i)
			If FishTimer(i)>=360 Then FishTimer(i)=FishTimer(i)-360
			PositionEntity FishEntity(i),FishX(i)+.75+1.5*Sin(FishTimer(i)),-0.5,-(FishY(i)+.4)+1.5*Cos(FishTimer(i))
			TurnEntity FishEntity(i),0,FishSpeed(i),0 
		EndIf
	Next 
		
	
End Function

Function CreatePillar(x,y,btype)

	If btype>=12 And btype<16 ; house style
		HouseStyle=btype-11
		Return
	EndIf
		
	If btype>=9 And btype<=11 ; rotated house
		PillarEntity(NofPillars)=CopyEntity(PillarMesh(8))
	Else If btype=23 Or btype=24 Or btype=30; stinky statue
		PillarEntity(NofPillars)=CopyEntity(PlayerMesh(1))
		If btype=23
			EntityTexture PillarEntity(NofPillars),PillarTexture(23)
		Else If btype=24
			EntityTexture PillarEntity(NofPillars),Rainbowtexture
		Else If btype=30
			EntityTexture PillarEntity(NofPillars),PillarTexture(30)
		EndIf
			
	Else
		PillarEntity(NofPillars)=CopyEntity(PillarMesh(btype))
	EndIf
	If btype>=8 And btype<=11 ; house texture
		EntityTexture PillarEntity(NofPillars),HouseTexture(HouseStyle)
	EndIf
	If btype=8 ; rotate house
		TurnEntity PillarEntity(NofPillars),0,90,0
	Else If btype=10
		TurnEntity PillarEntity(NofPillars),0,-90,0
	Else If btype=11
		TurnEntity PillarEntity(NofPillars),0,180,0
	EndIf
	If btype=22 ; rotate flower
		TurnEntity PillarEntity(NofPillars),0,Rand(-180,180),0
	EndIf
	
	
	;ScaleEntity FishEntity(NofFish),14,14,14 
		
	PositionEntity PillarEntity(NofPillars),x+.5,0,-(y+.5)
	If btype=23 Or btype=24 Or btype=30
		TranslateEntity PillarEntity(NofPillars),0,.9,0
		TurnEntity PillarEntity(NofPillars),0,180,0
	EndIf
	PillarType(NofPillars)=btype
	
	NofPillars=NofPillars+1

	

End Function

Function CreateTransporter(x,y)
	; input tile position x/y
	
	If NofTransporters>=MaxTransporters
		Return
	EndIf
	
	
	TransporterEntity(NofTransporters)=CopyEntity(TransporterMesh)
		
		
	PositionEntity TransporterEntity(NofTransporters),x+0.5,0.0,-(y+0.5)	
	
	TransporterX(NofTransporters)=x
	TransporterY(NofTransporters)=y
	TransporterX2(NofTransporters)=x
	TransporterY2(NofTransporters)=y
	TransporterMoving(NofTransporters)=False
	TransporterMovingX(k)=0
	TransporterMovingY(k)=0
	TransporterMovingTimer(NofTransporters)=0
		
	NofTransporters=NofTransporters+1
	
	
	
	

End Function

Function CanTransporterMove(x,y,dx,dy)
	
	;k is player standing on
	
	; first, check if tile is out of bounds
	x2=x+dx
	y2=y+dy
	
	; edge of level
	If x2<0 Or y2<0 Or x2>=MapWidth Or y2>=MapHeight Then Return False
	
	; check tiles - must match (e.g. water to water)
	If maptile(x,y)=1700 And maptile(x2,y2)<>300 Then Return False
	If maptile(x,y)=1701 And maptile(x2,y2)<>0 Then Return False
	If maptile(x,y)=1702 And maptile(x2,y2)<>900 Then Return False
	
	; check boxes and exit gate
	For i=0 To nofboxes-1
		If BoxX(i)=x2 And BoxY(i)=y2 Then Return False
		If boxX2(i)=x2 And BoxY2(i)=y2 Then Return False
	Next
	; cannot move beyond exit gate
	For i=0 To nofitems-1
		If ItemX(i)=x And ItemY(i)=y And ItemType(i)=1 Then Return False
	Next
	; if there's a box on it it also cannot move into exit gate
	For i=0 To nofitems-1
		If ItemX(i)=x2 And ItemY(i)=y2 And ItemType(i)=1 
			For k=0 To nofboxes-1
				If (boxx(k)=x Or boxx2(k)=x) And (boxy(k)=y Or boxy2(k)=y)
					Return False
				EndIf
			Next
		EndIf
	Next

	


	Return True

End Function

Function MoveTransporter(x,y,dx,dy)

	

	For i=0 To NofTransporters-1
		If x=TransporterX(i) And y=TransporterY(i)
			k=i
		EndIf
	Next	
	

	TransporterMovingTimer(k)=Basespeed
	TransporterSpeed(k)=Basespeed
	TransporterX2(k)=TransporterX(k)+dx
	TransporterY2(k)=TransporterY(k)+dy

	TransporterMovingX(k)=dx
	TransporterMovingY(k)=dy

	
	TransporterMoving(k)=True
	
	MapTile(x,y)=MapTile(x,y)+3
	MapTile(TransporterX2(k),TransporterY2(k))=MapTile(x,y)
	


End Function
Function EndMoveTransporter(k)
	
	tile=MapTile(TransporterX2(k),TransporterY2(k))-3
	MapTile(TransporterX2(k),TransporterY2(k))=tile
	If tile=1700 Then MapTile(TransporterX(k),TransporterY(k))=300
	If tile=1701 Then MapTile(TransporterX(k),TransporterY(k))=0
	If tile=1702 Then MapTile(TransporterX(k),TransporterY(k))=900
	
	TransporterX(k)=TransporterX2(k)
	TransporterY(k)=TransporterY2(k)
	TransporterMoving(k)=False
	TransporterMovingX(k)=0
	TransporterMovingY(k)=0


End Function


	