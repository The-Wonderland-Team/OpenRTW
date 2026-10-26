
Function MenuSetup()

	; Select Style
	MenuBkgStyle=1
	
	
	; Load Stylespecific Data
	Select MenuBkgStyle
	
	Case 0		; old style clouds
		
		MenuTexture(0)=MyLoadTexture("graphics\stars.jpg",1,False)
		ScaleTexture MenuTexture(0),20,20
		MenuTexture(1)=MyLoadTexture("graphics\stars.jpg",1,False)
		ScaleTexture MenuTexture(1),20,20
		
		MenuEntity(0) = CreatePlane()
		RotateEntity MenuEntity(0),-45,0,0
		PositionEntity MenuEntity(0),0,0,20
		EntityTexture MenuEntity(0),MenuTexture(0)	
		
		MenuEntity(1) = CreatePlane()
		RotateEntity MenuEntity(1),-45,0,0
		PositionEntity MenuEntity(1),0,0,10
		EntityTexture MenuEntity(1),MenuTexture(1)	
		EntityAlpha MenuEntity(1),.5
		
	Case 1		; Sphere of Stars
		CameraFogMode Camera,1
		CameraFogRange Camera,90,128
		CameraFogColor Camera,0,0,0;128,175,255
	
		MenuTexture(0)=MyLoadTexture("res30.dat",1,False)
		ScaleTexture MenuTexture(0),.125,.125
		MenuEntity(0)=CreateCylinder(64,True)
		PositionEntity MenuEntity(0),0,0,0
		FlipMesh MenuEntity(0)
		ScaleEntity MenuEntity(0),128,128,128
	;	RotateEntity MenuEntity(0),-90,0,0
		EntityFX MenuEntity(0),8
		EntityTexture MenuEntity(0),MenuTexture(0)
		
		
		MenuTexture(1)=MyLoadTexture("graphics\titlewater.jpg",1,False)
		
		ScaleTexture MenuTexture(1),20,20
		
		MenuEntity(1)=CreatePlane()
		
		;RotateEntity MenuEntity(1),-90,0,0
		PositionEntity MenuEntity(1),0,-5.2,0
		
		EntityTexture MenuEntity(1),MenuTexture(1)
		
		
		MenuEntity(2)=CreatePlane()
		

		MenuTexture(2)=MyLoadTexture("graphics\titlewater.jpg",1,False)
		

		ScaleTexture MenuTexture(2),10,10
		

		;RotateEntity MenuEntity(1),-90,0,0
		PositionEntity MenuEntity(2),0,-5,0
		

		EntityTexture MenuEntity(2),MenuTexture(2)
		

		EntityAlpha MenuEntity(2),.5
		

	
		
		MenuTexture(3)=MyLoadTexture("graphics\titlemountains.jpg",1,False)
		
		


		ScaleTexture MenuTexture(3),10,10
		MenuEntity(3)=LoadTerrain("res9.dat")
		PositionEntity MenuEntity(3),-128,-5.3,-128
		EntityTexture MenuEntity(3),MenuTexture(3)
		TerrainDetail MenuEntity(3),4000,True
		ScaleEntity MenuEntity(3),1,25,1
		
		MenuEntity(4)=CreateSphere(32)
		PositionEntity MenuEntity(4),0,-11,128
		EntityColor MenuEntity(4),255,255,0
		ScaleEntity MenuEntity(4),5,5,5
		EntityFX MenuEntity(4),9
		
		
		
	
		For i=10 To 59
			MenuEntity(i)=CreateSprite()
			EntityTexture MenuEntity(i),ParticleTexture,3
			ScaleSprite MenuEntity(i),.6,.6
			PositionEntity MenuEntity(i),-50,-50,-50
		Next 
		
		MenuEntity(60)=CreateSphere(64)
		ScaleEntity MenuEntity(60),2,40,2
		RotateEntity MenuEntity(60),90,0,0
		FlipMesh MenuEntity(60)
		PositionEntity MenuEntity(60),0,0,39
		EntityTexture MenuEntity(60),RainbowTexture
		HideEntity MenuEntity(60)
		
		
	

			
	End Select
	

	
	; And General Menu Data
	Logotex1=MyLoadTexture("res51.dat",4,False)
	Logo1 = CreateFlatSquare(logotex1)
	RotateEntity Logo1,-90,0,0
	PositionEntity Logo1,-1,1,3
	EntityAlpha Logo1,.85
	Logotex2=MyLoadTexture("res52.dat",4,False)
	Logo2 = CreateFlatSquare(logotex2)
	RotateEntity Logo2,-90,0,0
	PositionEntity Logo2,0,1,3
	EntityAlpha Logo2,.85
			
	MenuSelector = CreateSprite (Camera)
	MenuSelectortex = MyLoadTexture("graphics\menubar.jpg",1,False)
	ScaleSprite MenuSelector,.35,.02
	TranslateEntity MenuSelector,-.5+20*.02+.01,0.375-(-2000)*.04-.02,0
	EntityTexture MenuSelector,MenuSelectortex
	EntityAlpha MenuSelector,.6
	
	WorldLevelTexture=MyLoadTexture("graphics\levelsigns2.bmp",4,False)
	WorldLevelTexture2=MyLoadTexture("graphics\levelsigns2b.bmp",4,False)
	WorldLevelTexture3=MyLoadTexture("graphics\levelperfect.bmp",4,False)


End Function	




Function StartMenu(Menu)

	CurrentMenuOption=0

	; General Stuff

	; Camera & Lights
	If Menu<>5 ;  no changes for in-game Menu
		RotateEntity Camera,0,0,0
		PositionEntity Camera,0,0,0
		
	;	PositionEntity LevelLight,0,0,-100
	;	RotateEntity LevelLight,25,0,0

		; Entities
		If Menu<>12
			For i=0 To 59
				If MenuEntity(i)<>0 ShowEntity MenuEntity(i)
			Next
		Else 
			For i=0 To 9
				If MenuEntity(i)<>0 HideEntity MenuEntity(i)
			Next
			For i=10 To 60
				If MenuEntity(i)<>0 ShowEntity MenuEntity(i)
			Next
		EndIf
	EndIf
	
	
	If Menu<>1 And Menu<>0
		HideEntity Logo1
		HideEntity Logo2
	Else
		ShowEntity Logo1
		ShowEntity Logo2
		
	EndIf
	
	If Menu<>4 And Menu<>5	And Menu<>12 And Menu<>16 ShowEntity MenuSelector

	
	
	; Special Stuff	
	If Menu=0
		HidePointer()
		MenuTimer=-500
		
		
		LightColor LevelLight,0,0,0
		AmbientLight 0,0,0;255,255,255	
		; Start Music
		If DoesSoundExist=True 
			StopChannel(Music)
			Music=PlayMusic("res7.dat")
			ChannelVolume music,OptionsMusicLevel
		EndIf

	Else If Menu=1 	; Main Menu
		; Start Music
		If DoesSoundExist=True And CurrentMenu<>7 And CurrentMenu<>9 And CurrentMenu<>10 And CurrentMenu<>0 And CurrentMenu<>15
			StopChannel(Music)
			Music=PlayMusic("res7.dat")
			ChannelVolume music,OptionsMusicLevel
		EndIf

		
		

	Else If Menu=2	; Player Select Screen
				
		; Music
		If DoesSoundExist=True
			If ChannelPlaying(Music)=True
				StopChannel(Music)
			EndIf
		EndIf
					
		; Special Stuff: Collect Names
		; for the select player name, check first if there are any players at all
		; if not go directly to enter player name
		
		; but first check if that directory exitst
		If FileType("players")<>2 Then CreateDir("players")
		
		Dir=ReadDir("players")
		PlayerNameN=0
		; first count all files
		Repeat
			FileName$=NextFile$(Dir)
			If FileName$="" Then Exit
			If Upper$(Right$(FileName$,4))=".PLA" 
				PlayerName$(PlayerNameN)=Left$(FileName$,Len(FileName$)-4)
				PlayerNameN=PlayerNameN+1
			EndIf
			If PlayerNameN=10 Then Exit
		Forever
		
		CloseDir(Dir)

		; no players exist - go to create player
		If PlayerNameN=0 
		 	StartMenu(3)
			Return
		EndIf
		
		; Yes, there are players - get their Levelscompleted and Score
		For i=0 To PlayerNameN-1
		
			File=ReadFile("players/"+Lower$(PlayerName$(i))+".pla")
			ex$=ReadString$(File)
			If ex$<>"Stinky & Loof Player File v2"
				; uh oh! old player file
				PlayerNameN=PlayerNameN-1
				
				CloseFile(File)
				CopyFile "players/"+Lower$(PlayerName$(i))+".pla","players/"+Lower$(PlayerName$(i))+".old"
				DeleteFile "players/"+Lower$(PlayerName$(i))+".pla"
				i=i-1
				If PlayerNameN=0
					StartMenu(3)
					Return
				EndIf
			Else
				ex$=ReadString$(File) ; name
				ix=ReadInt(File) ; ID
				ix=ReadInt(File) ; world
				PlayerLevels(i)=ReadInt(File)
				PLayerScore(i)=ReadInt(File)
				
				
				For i23=0 To PlayGroundLevel-1
					ReadInt(File)
				Next
				
				PlayerExtraLevels(i)=ReadInt(File)
	
				
	
				
				
				CloseFile(File)
			EndIf
			
		Next

	
	Else If Menu=3; Enter New Player Name
		
		; Music
		If DoesSoundExist=True
			If ChannelPlaying(Music)=True
				StopChannel(Music)
			EndIf
		EndIf
		
		; Special Stuff
		CurrentPlayerName$=""	
	
	Else If Menu=4; Level Selector
	

		SaveGameStateActive=False
	
		If CurrentLevel=-999 Then CurrentLevel=CurrentLevelBackup
		
		; Music
		If CurrentMenu=13 And DoesSoundExist=True
			StartLevelMusic()
		EndIf
			
		; Special Stuff
		
		; add the squares for the levels
		; how many levels?
		CurrentWorldLevelN=0
		Repeat
			If WorldLevelName$(CurrentWorld*10+CurrentWorldLevelN)="" Then Exit
			CurrentWorldLevelN=CurrentWorldLevelN+1
		Until CurrentWorldLevelN=10
		
		NextWorldFlag=0
		
		For j=WhichWorldPack*10 To CurrentWorld
			For i=0 To 9
				If WorldLevelName$(j*10+i)<>"" And CurrentPlayerLevelScore(j*10+i)=0 Then NextWorldFlag=1
			Next
		Next
			
		; create the entities
		
		For i=0 To CurrentWorldLevelN+Sgn(CurrentWorld Mod 10)
		
			; create the flat squares for each level
			
			
			
			CurrentWorldLevelEntity(i)=CreateMesh()
			Brush=CreateBrush()
			
			; perfect
			If i<CurrentWorldLevelN And CurrentPlayerLevelScore(CurrentWorld*10+i)>0 And CurrentPlayerLevelPerfect(CurrentWorld*10+i)>0
				BrushTexture Brush,WorldLevelTexture3	
			Else
			; other
				BrushTexture Brush,WorldLevelTexture
			EndIf
			Surface=CreateSurface(CurrentWorldLevelEntity(i),Brush)

			If i=CurrentWorldLevelN
				; forward level
				; open
				If NextWorldFlag=0 Or ((CurrentWorld Mod 10)<9-WhichWorldPack And LockWorlds=1) ;!!!!! change for world packs
					AddVertex(Surface,-1,0,1, .5,-.5)
					AddVertex(Surface,1,0,1, 1,-.5)
					AddVertex(Surface,1,0,-1, 1,0)
					AddVertex(Surface,-1,0,-1, .5,0)
					If Language=0
						CurrentWorldLevelName$(i,0)="The Gateway Is Open"
						CurrentWorldLevelName$(i,1)="Press 'Enter' To Continue"
						CurrentWorldLevelName$(i,2)=""
					Else If Language=1
						CurrentWorldLevelName$(i,0)="Das Tor ist geoeffnet."
						CurrentWorldLevelName$(i,1)="'Enter' zum fortsetzen."
						CurrentWorldLevelName$(i,2)=""
					Else If Language=2
						CurrentWorldLevelName$(i,0)="La Grille est ouverte."
						CurrentWorldLevelName$(i,1)="Tape 'Entree' pour continuer."
						CurrentWorldLevelName$(i,2)=""

					EndIf
				Else
					AddVertex(Surface,-1,0,1, 0,-1)
					AddVertex(Surface,1,0,1, .5,-1)
					AddVertex(Surface,1,0,-1, .5,-.5)
					AddVertex(Surface,-1,0,-1, 0,-.5)
					If Language=0
						CurrentWorldLevelName$(i,0)="The Gateway is Closed"
						CurrentWorldLevelName$(i,1)="Complete all Levels in this World"
						CurrentWorldLevelName$(i,2)="in order to open this Gate"
					Else If Language=1
						CurrentWorldLevelName$(i,0)="Das Tor ist geschlossen."
						CurrentWorldLevelName$(i,1)="Beende erst alle Levels dieser Welt"
						CurrentWorldLevelName$(i,2)="um es zu oeffnen."
					Else If Language=2
						CurrentWorldLevelName$(i,0)="La Grille est fermee."
						CurrentWorldLevelName$(i,1)="Termine tous les niveaux de ce monde"
						CurrentWorldLevelName$(i,2)="pour ouvrir cette Grille."


					EndIf

				EndIf
			Else If i=CurrentWorldLevelN+1
				; backward level
				AddVertex(Surface,-1,0,1, 0,-.5)
				AddVertex(Surface,1,0,1, .5,-.5)
				AddVertex(Surface,1,0,-1, .5,0)
				AddVertex(Surface,-1,0,-1, 0,0)
				If Language=0
					CurrentWorldLevelName$(i,0)="Gateway is Open"
					CurrentWorldLevelName$(i,1)="Press 'Enter' to Travel Back"
					CurrentWorldLevelName$(i,3)=""
				Else If Language=1
					CurrentWorldLevelName$(i,0)="Das Tor ist geoeffnet."
					CurrentWorldLevelName$(i,1)="'Enter' zum umkehren."
					CurrentWorldLevelName$(i,3)=""
				Else If Language=2
					CurrentWorldLevelName$(i,0)="La Grille est ouverte."
					CurrentWorldLevelName$(i,1)="Tape 'Entree' pour revenir en arriere."
					CurrentWorldLevelName$(i,3)=""


				EndIf
			Else 
				; normal levels
				If CurrentPlayerLevelScore(CurrentWorld*10+i)=0
					; not completed
					AddVertex(Surface,-1,0,1, 0,-1)
					AddVertex(Surface,1,0,1, .5,-1)
					AddVertex(Surface,1,0,-1, .5,-.5)
					AddVertex(Surface,-1,0,-1, 0,-.5)
				Else
					If CurrentPlayerLevelPerfect(CurrentWorld*10+i)=0
						; completed
						AddVertex(Surface,-1,0,1, .5,-1)
						AddVertex(Surface,1,0,1, 1,-1)
						AddVertex(Surface,1,0,-1, 1,-.5)
						AddVertex(Surface,-1,0,-1, .5,-.5)
					Else
						; perfect!
						AddVertex(Surface,-1,0,1, 0,-1)
						AddVertex(Surface,1,0,1, 1,-1)
						AddVertex(Surface,1,0,-1, 1,0)
						AddVertex(Surface,-1,0,-1, 0,0)
					EndIf
				EndIf
			EndIf
			AddTriangle(Surface,0,1,2)
			AddTriangle(Surface,0,2,3)
	
			UpdateNormals(CurrentWorldLevelEntity(i))
			
			RotateEntity CurrentWorldLevelEntity(i),-90,0,0

			FreeBrush Brush
						
			ScaleEntity CurrentWorldLevelEntity(i),.15,.15,.15
			
			If i<CurrentWorldLevelN
				If CurrentWorldLevelN>5
					TranslateEntity CurrentWorldLevelEntity(i),-.8+(i Mod 5)*.4,.6-Floor(i/5)*.35,4
				Else
					TranslateEntity CurrentWorldLevelEntity(i),-.8+(i Mod 5)*.4,.425,4
				EndIf
			Else If i=CurrentWorldLevelN+1
				TranslateEntity CurrentWorldLevelEntity(i),-1.3,.425,4
			Else
				TranslateEntity CurrentWorldLevelEntity(i),1.3,.425,4
			EndIf
			EntityFX CurrentWorldLevelEntity(i),1
			
			

			



			; and get the level name
			If i<CurrentWorldLevelN
				; normal world
				If WorldLevelName$(CurrentWorld*10+i)="res10.dat"
					File=MyReadFile("res10.dat",False)
				Else
					File=MyReadFile("levels\"+WorldLevelName$(CurrentWorld*10+i),False)
				EndIf
				ex$=ReadString$(File) 	
				ex$=ReadString$(File)
				ix=ReadInt(File)
				ex$=ReadString$(File)
				CloseFile(File)
				If Language=1
					Restore GermanText
					; extra math is in there to chop of 5 levels if demo, so that it
					; gets the correct demo titles
					j2=0
					If Version<10 And CurrentWorld=1 Then j2=5
					For j=j2 To CurrentWorld*10+i
						Read ex$
						Read ux$
						Read ux$
						Read ux$
						Read ux$
						Read ux$
					Next
				EndIf
				If Language=2
					Restore FrenchText
					; extra math is in there to chop of 5 levels if demo, so that it
					; gets the correct demo titles
					j2=0
					If Version<10 And CurrentWorld=1 Then j2=5
					For j=j2 To CurrentWorld*10+i
						Read ex$
						Read ux$
						Read ux$
						Read ux$
						Read ux$
						Read ux$
					Next
				EndIf

							
				j=1
				k=0
				While(j<Len(ex$))
					If Mid$(ex$,j,1)="#" 
						CurrentWorldLevelName$(i,k)=Left$(ex$,j-1)
						k=k+1
						ex$=Right$(ex$,Len(ex$)-j)
						j=0
					EndIf
					j=j+1
				Wend
				CurrentWorldLevelName$(i,k)=ex$
				If k<2 Then CurrentWorldLevelName$(i,2)=""
				If k<1 Then CurrentWorldLevelName$(i,1)=""
			EndIf
			
								
			
		Next
		
		; do Entity #13 - the cursror
		CurrentWorldLevelEntity(12)=CreateMesh()
		Brush=CreateBrush()
		BrushTexture Brush,WorldLevelTexture2
		Surface=CreateSurface(CurrentWorldLevelEntity(12),Brush)
		AddVertex(Surface,-1,0,1, 0,0)
		AddVertex(Surface,1,0,1, 1,0)
		AddVertex(Surface,1,0,-1, 1,1)
		AddVertex(Surface,-1,0,-1, 0,1)
		AddTriangle(Surface,0,1,2)
		AddTriangle(Surface,0,2,3)
		UpdateNormals(CurrentWorldLevelEntity(12))
		FreeBrush Brush
		
		RotateEntity CurrentWorldLevelEntity(12),-90,0,0
		ScaleEntity CurrentWorldLevelEntity(12),.16,.16,.16
		EntityFX CurrentWorldLevelEntity(12),1

			
		
		UpdateWorld
		
		

	
	Else If Menu=5; In Game Menu
		; Camera - No Change
		
		; Signs
		StopSign(2)
		For i=11 To 30
			StopSign(i)
		Next
		
		; Lights
		LightColor LevelLight,0,0,0
		AmbientLight 100,100,100
		
		
		; LevelTimer - No Change
		
	
		
	
		
	
		
	Else If Menu=12	; End of Game
		
		LevelTimer=0
	;	MenuTimer=12000
		
		;RotateEntity Camera,90,0,0
		;HideEntity MenuEntity(4)
		
		
	;	CameraClsColor Camera,170,170,255
	;	CameraFogColor Camera,170,170,255
		;CameraRange Camera,.98,10000
		;CameraFogRange Camera,1000,10000
		;CameraFogMode Camera,1

		LightColor LevelLight,255,255,255
		AmbientLight 255,255,255

		
		
		; Music
		If DoesSoundExist=True; And CurrentMenu<>7 And CurrentMenu<>9 And CurrentMenu<>10
			StopChannel(Music)
			Music=PlayMusic("res4.dat")
			ChannelVolume music,OptionsMusicLevel
		EndIf

	Else If menu=13
		LevelTimer=0
		StopChannel Music
		Extralevelmusictrack=0
		CurrentFileDir=0
		
	Else If menu=14 ; load custom level
	
		SaveGameStateActive=False
		
		Repeat
			;
		Until GetKey()=0

		; clear directory 
		For i=0 To 5000; FileDirN
			FileDir$(i)=""
			FileDir2$(i)=""
			FileDirID(i)=0
			filedirid2(i)=0
			FileDirScore(i)=0
			FileDirPerfect(i)=0
		Next
		FileDirN=0
		FileDirPos=0

		; count files and setup load
		Dir=ReadDir("CustomLevels\"+CurrentExtraLevelDirectory$)
		n=0
		; first count all files
		Repeat
			DirFile$=Upper$(NextFile$(Dir))
			If DirFile$="" Then Exit
			If (Upper$(Right$(DirFile$,4))=".LEV" Or Upper$(Right$(DirFile$,4))=".LV5" Or Upper$(Right$(DirFile$,4))=".LV6") And FileType("CustomLevels\"+CurrentExtraLevelDirectory$+DirFile$)=1
			
								
				File2=ReadFile("CustomLevels\"+CurrentExtraLevelDirectory$+DirFile$)
				a=0
				For i=1 To 20
					a=a+ReadByte(file2)
				Next
				CloseFile(File2)
				
				
								
				If a=1380
					
						
					File2=ReadFile("CustomLevels\"+CurrentExtraLevelDirectory$+DirFile$)
					ex$=ReadString$(File2) 	; actually jsut the "level file" messag
										
					ex$=ReadString$(File2) ; level filename
					FileDirIDHolder=ReadInt(file2) ; id
					
				
			
					; sort
					If n=0 
						n2=0
					Else
						For i=0 To n-1
							If (CustomSortByName=True And DirFile$<=FileDir$(i)) ;Or (CustomSortByName=False And FirDirIDHolder>FileDirID(i))
								For j=n To i+1 Step -1
									FileDir$(j)=FileDir$(j-1)
									FileDir2$(j)=FileDir2$(j-1)
									FileDirID(j)=FileDirID(j-1)
									FileDirID2(j)=FileDirID2(j-1)
									FileDirScore(j)=FileDirScore(j-1)
									FileDirPerfect(j)=FileDirPerfect(j-1)
	
								Next
								n2=i
								Exit 
							EndIf
						Next
						If i=n
							n2=n
						EndIf
					EndIf
		
					FileDir$(n2)=DirFile$
				
					FileDirID(n2)=FileDirIDHolder ; id
					
					
					
					FileDir2$(n2)=ReadString$(File2) ; description
					For i=1 To Len(filedir2$(n2))
						filedirid2(n2)=filedirid2(n2)+Asc(Mid$(filedir2$(n2),i,1))^((i Mod 3)+1)
					Next
					For i=1 To Len(dirfile$)
						filedirid2(n2)=filedirid2(n2)+Asc(Mid$(ex$,i,1))^((i Mod 3)+1)
					Next

					CloseFile File2
					; Score
					filedirscore(n2)=0
					For k=0 To CurrentPlayerExtraLevels-1
						If CurrentPlayerExtraLevelID(k)=FiledirID(n2); And CurrentPlayerExtraLevelID2(k)=FiledirID2(n2)
							FileDirScore(n2)=CurrentPlayerExtraLevelScore(k)
							FileDirPerfect(n2)=CurrentPlayerExtraLevelPerfect(k)
							Exit
						Else 
							FileDirScore(n2)=0
							FileDirPerfect(n2)=0
						EndIf
					Next		
					
					n=n+1
				EndIf
								
			EndIf
			If n=>4000
				Cls
				Print "_____"
				Print "TOO MANY LEVEL FILES! SORT INTO DIRECTORIES!"
				Delay 5000
				End
			EndIf
			
			
		Forever
		
		
	
		FileDirN=n
		
		CloseDir(Dir)
		
		; now add directories and "Back" button
		Dir=ReadDir("CustomLevels\"+CurrentExtraLevelDirectory$)
		; first count all files
		DirN=0
		Repeat
			DirFile$=Upper$(NextFile$(Dir))
			If DirFile$="" Then Exit
			If DirFile$<>"." And DirFile$<>".." And FileType("CustomLevels\"+CurrentExtraLevelDirectory$+DirFile$)=2
				; directory
				;For j=FileDirN To 1 Step -1
				;	FileDir$(j)=FileDir$(j-1)
				;	FileDir2$(j)=FileDir2$(j-1)
				;	FileDirID(j)=FileDirID(j-1)
				;	FileDirScore(j)=FileDirScore(j-1)
				;Next
				
				
				; sort
				If DirN=0 
					n2=0
					For j=FileDirN To 1 Step -1
						FileDir$(j)=FileDir$(j-1)
						FileDir2$(j)=FileDir2$(j-1)
						FileDirID(j)=FileDirID(j-1)
						FileDirID2(j)=FileDirID2(j-1)
						FileDirScore(j)=FileDirScore(j-1)
						FileDirPerfect(j)=FileDirPerfect(j-1)

					Next

				Else
					For i=0 To DirN
						If DirFile$<=FileDir$(i) Or i=DirN
							For j=FileDirN To i+1 Step -1
								FileDir$(j)=FileDir$(j-1)
								FileDir2$(j)=FileDir2$(j-1)
								FileDirID(j)=FileDirID(j-1)
								filedirid2(j)=filedirid2(j-1)
								FileDirScore(j)=FileDirScore(j-1)
								FileDirPerfect(j)=FileDirPerfect(j-1)


							Next
							n2=i
							Exit 
						EndIf
					Next
					;If i=FileDirN
					;	n2=FileDirN
					;EndIf
				EndIf
				FileDir$(n2)=DirFile$
				FileDirID(n2)=0; id
				filedirid2(n2)=0
				FileDirScore(n2)=-2
				FileDir2$(n2)="(Folder)" ; description
				FileDirPerfect(n2)=0


						
				
				
				FileDirN=FileDirN+1
				DirN=DirN+1
			EndIf
		Forever
		CloseDir(Dir)
		
		For j=FileDirN To 1 Step -1
			FileDir$(j)=FileDir$(j-1)
			FileDir2$(j)=FileDir2$(j-1)
			FileDirID(j)=FileDirID(j-1)
			FileDirID2(j)=FileDirID2(j-1)
			FileDirScore(j)=FileDirScore(j-1)
			FileDirPerfect(j)=FileDirPerfect(j-1)

		Next
		FileDir$(0)=".."
		FileDir2$(0)="(Go Back)"
		FileDirID(0)=0
		filedirid2(0)=0
		FileDirScore(0)=-1
		FileDirPerfect(0)=0
		FileDirN=FileDirN+1
		
		; and finally place menuoption according to filedirpos
		If CurrentFileDir>14
			
			filedirpos=CurrentFileDir-14
			currentmenuoption=14
		Else
			currentmenuoption=currentfiledir
		EndIf
		
	Else If Menu=16	; End of Game Deluxe
		
		LevelTimer=0
	;	MenuTimer=12000
		
		;RotateEntity Camera,90,0,0
		;HideEntity MenuEntity(4)
		
		
	;	CameraClsColor Camera,170,170,255
	;	CameraFogColor Camera,170,170,255
		;CameraRange Camera,.98,10000
		;CameraFogRange Camera,1000,10000
		;CameraFogMode Camera,1
		
		PositionEntity Camera,0,3,-10
		RotateEntity Camera,10,0,0

		LightColor LevelLight,255,255,255
		AmbientLight 255,255,255

		
		
		; Music
		If DoesSoundExist=True; And CurrentMenu<>7 And CurrentMenu<>9 And CurrentMenu<>10
			StopChannel(Music)
			Music=PlayMusic("res7.dat")
			ChannelVolume music,OptionsMusicLevel
		EndIf

			


	EndIf
	
	CurrentMenu=Menu
	
	GamePhase=0
		
	TweenPeriod=1000/85
	TweenTime=MilliSecs()-TweenPeriod


End Function

Function MenuLoop()
	
;	If CurrentMenu<>5 Then LevelTimer=LevelTimer+1

	If CurrentMenu=0 Then MenuLoop0
	If CurrentMenu=1 Then MenuLoop1
	If CurrentMenu=2 Then MenuLoop2
	If CurrentMenu=3 Then MenuLoop3
	If CurrentMenu=4 Then MenuLoop4
	If CurrentMenu=5 Then MenuLoop5
	If CurrentMenu=6 Then MenuLoop6
	If CurrentMenu=7 Then MenuLoop7
	If CurrentMenu=9 Then MenuLoop9
	If CurrentMenu=10 Then MenuLoop10
	If CurrentMenu=12 Then MenuLoop12
	If CurrentMenu=13 Then MenuLoop13
	If CurrentMenu=14 Then MenuLoop14
	If CurrentMenu=15 Then MenuLoop15
	If CurrentMenu=16 Then MenuLoop16


	





	If KeyHit(66) Then
		SaveBuffer(FrontBuffer(),"screenshot.bmp")
	End If


	
End Function

Function MenuLoop0()

	; Credit Text
	
	If (MenuTimer Mod 20000)<8500
		CenterText("",9,1,50)
		CenterText("",9,1,51)
	Else If (MenuTimer Mod 20000)<9500
		CenterText("A Midnight Synergy Game",7.5,1,50)
		CenterText("",9,1,51)
	Else If (MenuTimer Mod 20000)<10500
		CenterText("Program, Visuals, and SFX",7.5,1,50)
		CenterText("Patrick Maidorn",8.5,1,51)
	Else If (MenuTimer Mod 20000)<11500
		CenterText("Music",7.5,1,50)
		CenterText("Jonne 'Purple Motion' Valtonen",8.5,1,51)
	Else If (MenuTimer Mod 20000)<12500
		CenterText("Additional 3D Modeling",7.5,1,50)
		CenterText("Danny Hawrysio",8.5,1,51)
	Else If (MenuTimer Mod 20000)<13500
		CenterText("Software Development Consultant",7.5,1,50)
		CenterText("Claire Polster",8.5,1,51)
	Else If (MenuTimer Mod 20000)<14500
		CenterText("And Many Thanks",7.5,1,50)
		CenterText("To The Entire Wonderland Community!",8.5,1,51)


	EndIf
	; Story Text
	If MenuTimer<0
		CenterText("And now",15,1,55)
		CenterText("our journey continues...",16,1,56)
		
	Else If MenuTimer<1500
		CenterText("",9,1,55)
		CenterText("",9,1,56)
	Else If MenuTimer<2500
		CenterText("As a new day",15,1,55)
		CenterText("dawns over Wonderland,",16,1,56)
		CenterText("",9,1,57)
	Else If MenuTimer<3500
		CenterText("its inhabitants wake up",15,1,55)
		CenterText("to troubling news.",16,1,56)
		CenterText("",9,1,57)
	Else If MenuTimer<4500
		CenterText("The Rainbow Coins,",15,1,55)
		CenterText("whose magic keeps Wonderland safe,",16,1,56)
		
	Else If MenuTimer<5500
		CenterText("have been scattered",15,1,56)
		CenterText("to the far corners of the world!",16,1,55)
	Else If MenuTimer<6500
		CenterText("Once again,",15,1,56)
		CenterText("Wonderland's existence is at risk.",16,1,57)
	Else If MenuTimer<6700
		CenterText("And, once again,",15,1,56)
		CenterText("",16,1,57)
	Else If MenuTimer<6900
		CenterText("And, once again,",15,1,56)
		CenterText("Stinky and Loof",16,1,57)
	Else If MenuTimer<7500
		CenterText("And, once again,",15,1,56)
		CenterText("Stinky and Loof",16,1,57)
		CenterText("must save the day!",17,1,58)
	Else If MenuTimer<8500
		CenterText("",9,1,55)
		CenterText("",9,1,56)
		CenterText("",9,1,57)
	Else
		CenterText("(Press Space Key To Start)",16,1,57)
	



	EndIf
		



	
		

	; turn on text and signs
	If MenuTimer<0
		For l.LetterType=Each LetterType
			If l\Active=False
				l\Active=True
				If (MenuTimer)<-300 EntityAlpha l\Entity,Float(500.0+(MenuTimer))/200.0
				If (MenuTimer)>-200 EntityAlpha l\Entity,-Float((MenuTimer))/200.0
	
				ShowEntity l\Entity
				
			EndIf
		Next

	Else If menutimer>500
		For l.LetterType=Each LetterType
			If l\Active=False
				l\Active=True
				If (MenuTimer-500) Mod 1000<200 EntityAlpha l\Entity,Float((MenuTimer-500) Mod 200)/200.0
				If (MenuTimer-500) Mod 1000>800 EntityAlpha l\Entity,1.0-Float((MenuTimer-500) Mod 200)/200.0
	
				ShowEntity l\Entity
				
			EndIf
		Next
	EndIf
	Repeat
		TweenElapsed=MilliSecs()-TweenTime
	Until TweenElapsed>TweenPeriod
	;how many 'frames' have elapsed	
	TweenTicks=TweenElapsed/TweenPeriod
	;fractional remainder
	Tween#=Float(TweenElapsed Mod TweenPeriod)/Float(TweenPeriod)
	For k=1 To TweenTicks
		Tweentime=Tweentime+Tweenperiod
		If k=Tweenticks 
			CaptureWorld
		EndIf
	
		; do the updates in here
		MenuBkgUpdate()
	


		UpdateWorld
	
	Next
	RenderWorld tween
	
	Flip
	
	If KeyDown(1) Or KeyDown(57) Or KeyDown(28)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			If MenuTimer<0 Then MenuTimer=0
			StartMenu(1)
		EndIf
	EndIf
	
	


End Function

Function MenuLoop1()
	
	; Display 
	
	DisplayText("v"+Str$(VersionNo1)+"."+Str$(VersionNo2)+Str$(VersionNo3),44,0,1,50)
	If Language=0
		CenterText("START GAME",9,1,51)
		CenterText("OPTIONS",10,1,52)
		CenterText("ONLINE SOLUTIONS",11,1,53)
		CenterText("LEVEL EXCHANGE",12,1,54)
		CenterText("LEVEL EDITOR",13,1,55)
		CenterText("EXIT TO WINDOWS",14,1,56)
	Else If Language=1
		CenterText("SPIEL STARTEN",9,1,51)
		CenterText("OPTIONEN",10,1,52)
		CenterText("ONLINE HILFE",11,1,53)
		CenterText("ONLINE FORUM",12,1,54)
		CenterText("LEVEL EDITOR",13,1,55)
		CenterText("SPIEL BEENDEN",14,1,56)
	Else If Language=2
		CenterText("JOUER",9,1,51)
		CenterText("OPTIONS",10,1,52)
		CenterText("SOLUCES EN LIGNE",11,1,53)
		CenterText("FORUM EN LIGNE",12,1,54)
		CenterText("LEVEL EDITOR",13,1,55)
		CenterText("SORTIE VERS WINDOWS",14,1,56)
	EndIf
	; Use Cursor Keys
	If LevelTimer Mod 200 <100
		If Language=0
			CenterText("Use Cursor Keys and Enter to Select",17,1,57)
		Else If Language=1
			CenterText("Benutze die Pfeiltasten und zur Auswahl 'Enter'",17,1,57)
		Else If Language=2
			CenterText("Pour choisir, utilise les fleches et 'Entree'",17,1,57)
		EndIf

	Else 
		CenterText("",17,1,57)
	EndIf
	
	; General Update Routine	
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(9+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,25*.012,.02
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next
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
	
		; do the updates in here
		MenuBkgUpdate()
		UpdateWorld
	
	Next
	RenderWorld tween
	Flip

	; Input Etc
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)		
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=5
			CursorKeyPressed=True
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=6 Then CurrentMenuOption=0
			CursorKeyPressed=True
		EndIf
	
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CursorKeyPressed=True
			If CurrentMenuOption=0
				EndMenu()
				StartMenu(2)
				Return
			EndIf
			If CurrentMenuOption=1
				EndMenu()
				StartMenu(9)
				Return
			EndIf
			If CurrentMenuOption=2
				EndMenu()
				VersionNo1=-999
				StartMenu(15)
			EndIf
			If CurrentMenuOption=3
				EndMenu()
				VersionNo1=-998
				StartMenu(15)
			EndIf
			If CurrentMenuOption=4
				EndMenu()
				VersionNo1=-997
				GameExit=True
			EndIf
			If CurrentMenuOption=5
				EndMenu()
				StartMenu(10)
			EndIf
			
			
		EndIf
	
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			StartMenu(10)
			

		EndIf
	Else
		CursorKeyPressed=False
	EndIf
	
	If Version>=10
		If KeyDown(2) And KeyDown(3) And KeyDown(4) 
			EndMenu()
			CursorKeyPressed=True
			StartMenu(12)
		EndIf
		
		
	EndIf
	

End Function

Function MenuLoop2()
	

	; Display Static Text
	If Language=0
		CenterText( "SELECT PLAYER",4,1,50)
		CenterText( "----------------------------------------------",5,1,51)
		DisplayText("Name             Levels  Custom    Total Score",2,6,1,52)
		
		DisplayText("CREATE NEW PLAYER",2,7,1,53)
	Else If Language=1
		CenterText("SPIELERAUSWAHL",4,1,50)
		CenterText("------------------------------",5,1,51)
		DisplayText("Name             Levels Punkte",10,6,1,52)
		
		DisplayText("NEUER SPIELER",10,7,1,53)
	Else If Language=2
		CenterText("CHOISIS LE JOUEUR",4,1,50)
		CenterText("------------------------------",5,1,51)
		DisplayText("Nom              Niveau  Score",10,6,1,52)
		
		DisplayText("CREER NOUVEAU JOUEUR",10,7,1,53)
	EndIf
	If PlayerNameN>0
		For i=0 To PlayerNameN-1
			DisplayText(Upper$(PlayerName$(i)),2,8+i,1,54+4*i)
			DisplayText(PlayerLevels(i),25-Len(Str$(PlayerLevels(i))),8+i,1,55+4*i)
			DisplayText(PlayerExtraLevels(i),33-Len(Str$(PlayerExtraLevels(i))),8+i,1,57+4*i)
			DisplayText(PlayerScore(i),48-Len(Str$(PlayerScore(i))),8+i,1,56+4*i)
		Next 
	EndIf
	CenterText("----------------------------------------------",8+PlayerNameN,1,55+4*PlayerNameN)

	
		
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(7+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,48.5*.012,.02
	

	
	
	
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



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

		
		; do the updates in here
		MenuBkgUpdate()		
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip

	
	
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=PlayerNameN
			CursorKeyPressed=True
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=PlayerNameN+1 Then CurrentMenuOption=0
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CursorKeyPressed=True
			If CurrentMenuOption=0 
				; create new player
				If PlayerNameN<10
					EndMenu()
					StartMenu(3)
					Return
				Else
					; no more room - maybe play a sound fx here?
				EndIf
			Else
				; select existing player
				EndMenu()
				LoadPlayer(PlayerName$(CurrentMenuOption-1))
				StartMenu(13)				
				Return
			EndIf
		EndIf
	Else If KeyDown(211)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			If CurrentMenuOption>0
				Player2BDeleted=CurrentMenuOption-1
				EndMenu()
				StartMenu(6)
				Return
			EndIf
		EndIf
		
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			StartMenu(1)
			Return
		EndIf
				
	Else
		CursorKeyPressed=False
	EndIf
	
			
			 

End Function

Function MenuLoop3()
	

	; Display Static Text
	If Language=0
		CenterText("NEW PLAYER - PLEASE ENTER NAME:",4,1,50)
		CenterText("-------------------------------",5,1,51)
	Else If Language=1
		CenterText("SPIELER - BITTE NAMEN EINGEBEN:",4,1,50)
		CenterText("-------------------------------",5,1,51)
	Else If Language=2
		CenterText("NOUVEAU JOUEUR - S.V.P. ECRIS TON NOM:",4,1,50)
		CenterText("--------------------------------------",5,1,51)

	EndIf
	
	CenterText(CurrentPlayerName$,7,1,52)
	If LevelTimer Mod 50<25
		DisplayText(":",25+Len(CurrentPlayerName$)/2.0,7,1,53)
	Else
		DisplayText(" ",25+Len(CurrentPlayerName$)/2.0,7,1,53)
	EndIf
	
	
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.375-Float(-2)*.04-.013,1.001
	
		
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



	
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

		
		; do the updates in here
		
		MenuBkgUpdate()		
		
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip
	
	;ascii keys
	askey=GetKey()
	If askey>=97 Then askey=askey-32
	If (askey>=65 And askey<=90) And Len(CurrentPlayerName$)<15
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(1),0)	
			CurrentPlayerName$=CurrentPlayerName$+Chr$(askey)
			CursorKeyPressed=True
		EndIf
	Else If (askey=32) And Len(CurrentPlayerName$)<15
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(1),0)	
			CurrentPlayerName$=CurrentPlayerName$+" "
			CursorKeyPressed=True
		EndIf

		
		
	; backspace
	Else If KeyDown(14) And Len(CurrentPlayerName$)>0
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(1),0)	
			CurrentPlayerName$=Left$(CurrentPlayerName$,Len(CurrentPlayerName$)-1)
			CursorKeyPressed=True
		EndIf
	
	; enter	
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			PlayMySound(SFXLastCoin,0)	
		
			CreateNewPlayer()
			
			CursorKeyPressed=True
			EndMenu()
			StartMenu(13)
			Return
		EndIf
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			StartMenu(2)
			Return
		EndIf
	
	Else
		CursorKeyPressed=False
	EndIf
	
	

		
			 

End Function

Function MenuLoop4()

	; *******************
	
	; FIRST: load up the world data, i.e. each levelname, score, world name
	; *******************
	DisplayText(CurrentPlayerName$,0,0,1,61)
	AstragonLess=0
	If Version=12 And CurrentWorld>7
		AstragonLess=2
	EndIf
	If CurrentWorld<10
		ex$="CLASSIC LEVELS "
		WorldAdjust=0
	Else If CurrentWorld<20
		ex$="TUTORIAL LEVELS "
		WorldAdjust=10
	Else If CurrentWorld<30
		ex$="DELUXE LEVELS "
		WorldAdjust=20
	Else If CurrentWorld<40
		ex$="KIDS LEVELS "
		WorldAdjust=30
	EndIf

		
	CenterText("",15,1,63)

	If Language=0
		CenterText(ex$+"WORLD "+Str$(CurrentWorld+1-AstragonLess-WorldAdjust),2,1,50)
	Else If Language=1
		CenterText("WELT  "+Str$(CurrentWorld+1-AstragonLess-WorldAdjust),2,1,50)
	Else If Language=2
		CenterText("MONDE  "+Str$(CurrentWorld+1-AstragonLess-WorldAdjust),2,1,50)

	EndIf
	j=0
	CenterText(WorldName$(CurrentWorld,Language),3,1,51)
	For i=WorldAdjust*10 To WorldAdjust*10+99
		If CurrentPlayerLevelScore(i)>0
			j=j+CurrentPlayerLevelScore(i)
		EndIf
	Next
	If Language=0
		DisplayText("TOTAL SCORE:",38,0,1,52)
	Else If Language=1
		DisplayText("     PUNKTE:",38,0,1,52)
	Else If Language=2
		DisplayText("SCORE TOTAL:",38,0,1,52)

	EndIf
	DisplayText(Str$(j),50-Len(Str$(j)),1,1,53)	
	
			
	
	For i=0 To 2
		CenterText(CurrentWorldLevelName$(CurrentLevel -CurrentWorld*10,i),10.5+i,1,55+i)
	Next
	If CurrentLevel - CurrentWorld*10<CurrentWorldLevelN
		; place cursor
		If CurrentWorldLevelN>5
			PositionEntity CurrentWorldLevelEntity(12),-.8+(CurrentLevel Mod 5)*.4,.6-Floor((CurrentLevel Mod 10)/5)*.35,3.99
		Else
			PositionEntity CurrentWorldLevelEntity(12),-.8+(CurrentLevel Mod 5)*.4,.425,3.99
		EndIf
		; and text
		CenterText("Level "+Str$((CurrentLevel Mod 10)+1),9.5,1,54)
		If CurrentPlayerLevelScore(CurrentLevel)=0
			If(LevelTimer Mod 160 < 120)
				If Language=0
					CenterText("Not Completed Yet",13,1,58)
					CenterText("Press 'Enter' to try this Level.",14,1,59)
				Else If Language=1
					CenterText("Noch nicht komplett.",13,1,58)
					CenterText("'Enter' um das Level zu starten.",14,1,59)
				Else If Language=2
					CenterText("Pas encore complete.",13,1,58)
					CenterText("Tape 'Entree' pour essayer ce niveau.",14,1,59)
	
				EndIf
			Else
				DisplayText("",0,0,1,58)
				DisplayText("",0,0,1,59)
			EndIf
			If Language=0
				DisplayText("Use Cursor Keys to select different Levels.",4,18,1,60)
			Else If Language=1
				DisplayText("Benutze die Pfeiltasten zur Levelauswahl.",5,18,1,60)
			Else If Language=2
				DisplayText("Utilise les fleches pour choisir les niveaux.",3,18,1,60)
	
			EndIf
		Else
			If Language=0
				CenterText("Completed!",13,1,58)
				CenterText("Score: "+Str$(CurrentPlayerLevelScore(CurrentLevel)),14,1,59)
				If(LevelTimer Mod 160 < 120)
					DisplayText("Replay Levels to try and beat your score anytime.",0.5,18,1,60)
				Else
					DisplayText("",0,0,1,60)
				EndIf
				If CurrentPlayerLevelPerfect(CurrentLevel)=1
					CenterText("All Bonus Coins Found!",15,1,63)
				EndIf
			Else If Language=1
				CenterText("Beendet!",13,1,58)
				CenterText("Punkte: "+Str$(CurrentPlayerLevelScore(CurrentLevel)),14,1,59)
				If(LevelTimer Mod 160 < 120)
					DisplayText("Levels koennen jederzeit nochmal gespielt werden.",0.5,18,1,60)
				Else
					DisplayText("",0,0,1,60)
				EndIf
				
			Else If Language=2
				CenterText("Reussi!",13,1,58)
				CenterText("Score: "+Str$(CurrentPlayerLevelScore(CurrentLevel)),14,1,59)
				If(LevelTimer Mod 160 < 120)
					DisplayText("      Rejoue les niveaux n'importe quand.      ",1,18,1,60)
				Else
					DisplayText("",0,0,1,60)
				EndIf

			EndIf
		EndIf	
	Else If CurrentLevel - CurrentWorld*10=CurrentWorldLevelN
		PositionEntity CurrentWorldLevelEntity(12),1.3,.425,3.99
		If Language=0
			CenterText("Next World",9.5,1,54)
		Else If language=1
			CenterText("Zur naechsten Welt!",9.5,1,54)
		Else If language=2
			CenterText("Monde suivant",9.5,1,54)


		EndIf
		DisplayText("",1,1,1,58)
		DisplayText("",1,1,1,59)
		DisplayText("",1,1,1,60)
	Else 
		PositionEntity CurrentWorldLevelEntity(12),-1.3,.425,3.99
		If Language=0
			CenterText("Previous World",9.5,1,54)
		Else If language=1
			CenterText("Zur vorherigen Welt!",9.5,1,54)
		Else If language=2
			CenterText("Monde precedant",9.5,1,54)

		EndIf

		
		DisplayText("",1,1,1,58)
		DisplayText("",1,1,1,59)
		DisplayText("",1,1,1,60)

	EndIf
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next

	
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
	Tween#=Float(TweenElapsed Mod TweenPeriod)/Float(Tweenperiod)
	
	For k=1 To TweenTicks
		Tweentime=Tweentime+Tweenperiod
		If k=Tweenticks 
			CaptureWorld
			
		EndIf
		leveltimer=leveltimer+1

		
		; do the updates in here
		
		MenuBkgUpdate()
		TurnEntity CurrentWorldLevelEntity(12),0,3,0
	;	If CurrentPlayerLevelPerfect(CurrentLevel)=1
	;		ScaleEntity CurrentWorldLevelEntity((CurrentLevel Mod 10)),.15+.05*Sin(leveltimer Mod 360),.15+.05*Sin(leveltimer Mod 360),.15+.05*Sin(leveltimer Mod 360)
	;	EndIf
		
		
		UpdateWorld
	
	Next

	RenderWorld tween
	
;	Color 255,255,255 : Text 0,DisplayHeight-48,"Total Videomemory : " + TotalVidMem() + " - Available Video Memory : " + AvailVidMem()
;Text 0,DisplayHeight-32,"Active Textures : "+ActiveTextures()
;Text 0,DisplayHeight-16,"Tris Rendered : "+TrisRendered()
	
	Flip
	
	
	; enter	
	If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			CursorKeyPressed=True
			
			If CurrentLevel -CurrentWorld*10<CurrentWorldLevelN
				; enter a level?
				; also get rid of level entities
				For i=0 To CurrentWorldLevelN+Sgn(CurrentWorld Mod 10)
					FreeEntity CurrentWorldLevelEntity(i)
				Next
				FreeEntity CurrentWorldLevelEntity(12)
				EndMenu()
				
				StartLevel(CurrentLevel)
;				PlayMySound(SFXTalk(Rand(0,4)),0)
				Return
			Else If CurrentLevel -CurrentWorld *10=CurrentWorldLevelN
				; Next World
				NextWorldFlag=0
				For j=WhichWorldPack*10 To CurrentWorld
					For i=0 To 9 
						If WorldLevelName$(j*10+i)<>"" And CurrentPlayerLevelScore(j*10+i)=0 Then NextWorldFlag=1
					Next
				Next

				If NextWorldFlag=0 Or (CurrentWorld Mod 10<9-WhichWorldPack And LockWorlds=1) ; CHANGE FOR DIFFERENT PACKS !!!!!!!!!!!!!!!!!!!!!!

					
					; also get rid of level entities
					For i=0 To CurrentWorldLevelN+Sgn(CurrentWorld Mod 10)
						FreeEntity CurrentWorldLevelEntity(i)
					Next
					FreeEntity CurrentWorldLevelEntity(12)
					;onward and upward!
					CurrentWorld=CurrentWorld+1
					If CurrentWorld=7 And Version=12
						CurrentWorld=9
					EndIf

					EndMenu()
					
					CurrentLevel=CurrentWorld*10
					
					If CurrentWorld=10	; CHANGE FOR DIFFERENT PACKS !!!!!!!!!!!!!!!!!!!!!!
						; end of game
						StartMenu(12)
						Return
					EndIf
					If CurrentWorld=12	; CHANGE FOR DIFFERENT PACKS !!!!!!!!!!!!!!!!!!!!!!
						; end of game
						StartMenu(1)
						Return
					EndIf

					If CurrentWorld=28	; CHANGE FOR DIFFERENT PACKS !!!!!!!!!!!!!!!!!!!!!!
						; end of game
						StartMenu(16)
						Return
					EndIf
 
					
					StartLevelMusic()
					
					StartMenu(4)
					Return
				Else
					PlayMySound(SFXTalk(5),0)
				EndIf
			Else 
				; Previous World
				; also get rid of level entities
				For i=0 To CurrentWorldLevelN+Sgn(CurrentWorld Mod 10)
					FreeEntity CurrentWorldLevelEntity(i)
				Next
				FreeEntity CurrentWorldLevelEntity(12)
				CurrentWorld=CurrentWorld-1
				If CurrentWorld=8 And Version=12
					CurrentWorld=6
				EndIf
				EndMenu()
				
				CurrentLevel=CurrentWorld*10
				
				StartLevelMusic()

				StartMenu(4)
				Return
			EndIf
			
			Return
		EndIf
	; cursor keys
	Else If KeyDown(75) Or KeyDown(203)
		If CursorKeyPressed=False 
			PlayMySound(SFXMenuSound(0),0)	
			
			CursorKeyPressed=True
			CurrentLevel=CurrentLevel-1
		
			If CurrentLevel<CurrentWorld*10
				CurrentLevel=CurrentWorld*10+CurrentWorldLevelN+Sgn(CurrentWorld Mod 10)
			EndIf
		EndIf
	
	Else If KeyDown(77) Or KeyDown(205)

		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			
			CursorKeyPressed=True
			CurrentLevel=CurrentLevel+1
	
			If CurrentLevel>=CurrentWorld*10+CurrentWorldLevelN +1+Sgn(CurrentWorld Mod 10)
				CurrentLevel=CurrentWorld*10
			EndIf
		EndIf
	Else If KeyDown(72) Or KeyDown(200)
		If CursorKeyPressed=False And CurrentWorldLevelN>5 
			PlayMySound(SFXMenuSound(0),0)	
			
			CursorKeyPressed=True
			CurrentLevel=CurrentLevel-5
		
			If CurrentLevel<CurrentWorld*10
				CurrentLevel=CurrentWorld*10+CurrentWorldLevelN-1
			EndIf
		EndIf
	Else If KeyDown(80) Or KeyDown(208)
		If CursorKeyPressed=False And CurrentWorldLevelN>5
			PlayMySound(SFXMenuSound(0),0)	
			
			CursorKeyPressed=True
			CurrentLevel=CurrentLevel+5
		
			If CurrentLevel>CurrentWorld*10+CurrentWorldLevelN-1
				CurrentLevel=CurrentWorld*10+CurrentWorldLevelN-1
			EndIf
		EndIf
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			; also get rid of level entities
			For i=0 To CurrentWorldLevelN+Sgn(CurrentWorld  Mod 10)
				FreeEntity CurrentWorldLevelEntity(i)
			Next
			FreeEntity CurrentWorldLevelEntity(12)
			EndMenu()
			StartMenu(13)
			Return
		EndIf
	Else
		CursorKeyPressed=False
	EndIf
	
	
		
		



End Function

Function MenuLoop5()

	
	If Language=0
	
		If CurrentMenuOption=0
			CenterText("== RESTART THIS LEVEL ==",7,1,52)
		Else
			CenterText("RESTART THIS LEVEL",7,1,52)
		EndIf
		
		If CurrentMenuOption=1
			CenterText("== SELECT NEW LEVEL ==",8,1,53)
		Else
			CenterText("SELECT NEW LEVEL",8,1,53)
		EndIf
		
		If CurrentMenuOption=2
			CenterText("== EXIT GAME ==",9,1,54)
		Else
			CenterText("EXIT GAME",9,1,54)
		EndIf
		
		If CurrentMenuOption=3
			CenterText("== RESUME GAME ==",10,1,55)
		Else
			CenterText("RESUME GAME",10,1,55)
		EndIf
		
	;	If CurrentMenuOption=4
	;		CenterText("== SAVE GAME STATE ==",12,1,56)
	;	Else
	;		CenterText("SAVE GAME STATE",12,1,56)
	;	EndIf
	;	If SaveGameStateActive=True
	;		If CurrentMenuOption=5
	;			CenterText("== LOAD GAME STATE ==",13,1,57)
	;		Else
	;			CenterText("LOAD GAME STATE",13,1,57)
	;		EndIf
	;	EndIf


	Else If Language=1
		If CurrentMenuOption=0
			CenterText("== LEVEL NEU STARTEN ==",7,1,52)
		Else
			CenterText("LEVEL NEU STARTEN",7,1,52)
		EndIf
		
		If CurrentMenuOption=1
			CenterText("== NEUES LEVEL ==",8,1,53)
		Else
			CenterText("NEUES LEVEL",8,1,53)
		EndIf
		
		If CurrentMenuOption=2
			CenterText("== SPIEL BEENDEN ==",9,1,54)
		Else
			CenterText("SPIEL BEENDEN",9,1,54)
		EndIf
		
		If CurrentMenuOption=3
			CenterText("== SPIEL FORTSETZEN ==",10,1,55)
		Else
			CenterText("SPIEL FORTSETZEN",10,1,55)
		EndIf
		
	Else If Language=2
		If CurrentMenuOption=0
			CenterText("== RECOMMENCER CE NIVEAU ==",7,1,52)
		Else
			CenterText("RECOMMENCER CE NIVEAU",7,1,52)
		EndIf
		
		If CurrentMenuOption=1
			CenterText("== CHOISIR UN AUTRE NIVEAU ==",8,1,53)
		Else
			CenterText("CHOISIR UN AUTRE NIVEAU",8,1,53)
		EndIf
		
		If CurrentMenuOption=2
			CenterText("== SORTIE DU JEU ==",9,1,54)
		Else
			CenterText("SORTIE DU JEU",9,1,54)
		EndIf
		
		
			If CurrentMenuOption=3
				CenterText("== CONTINUER A JOUER ==",10,1,55)
			Else
				CenterText("CONTINUER A JOUER",10,1,55)
			EndIf
		
	EndIf

	
	
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next




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
	
		
		;UpdateWorld
	
	Next
		
	RenderWorld tween
	
;	Color 255,255,255 : Text 0,DisplayHeight-48,"Total Videomemory : " + TotalVidMem() + " - Available Video Memory : " + AvailVidMem()
;Text 0,DisplayHeight-32,"Active Textures : "+ActiveTextures()
;Text 0,DisplayHeight-16,"Tris Rendered : "+TrisRendered()
	
	Flip
	
	
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=False	
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=3;4+SaveGameStateActive
			CursorKeyPressed=True
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=4 Then CurrentMenuOption=0
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(28) Or KeyDown(156))
	
		While (KeyDown(28) Or KeyDown(156))
		Wend
		
	
		; Clear This Menu
		For l.LetterType=Each LetterType
			FreeEntity l\Entity
			Delete l
		Next
		LightColor LevelLight,255,255,255
		AmbientLight 255,255,255
		
		If CurrentMenuOption=0
			For k=1 To 4
				If PlayerExists(k)=True Then KillPlayer(k)
			Next
			GamePhase=1
		Else If CurrentMenuOption=1
			; Back to Menu
			EndLevel()
			If CurrentLevel=-999
				StartMenu(14)
			Else
				StartMenu(4)
			EndIf
			
		Else If CurrentMenuOption=2
			; Exit Game
			EndLevel()
			StartMenu(1)
		
		Else If CurrentMenuOption=3
			; Resume Game
					
			GamePhase=1
			CheckSigns()
			
		Else If CurrentMenuOption=4
			SaveGameState()
			GamePhase=1
			CheckSigns()
		Else If CurrentMenuOption=5
			LoadGameState()
			GamePhase=1
			CheckSigns()
	
		EndIf

	Else If KeyDown(1)
	
		While KeyDown(1)
		Wend
		
		; Esc: Back to Menu
		For l.LetterType=Each LetterType
			FreeEntity l\Entity
			Delete l
		Next
		LightColor LevelLight,255,255,255
		AmbientLight 255,255,255
		
		
		GamePhase=1
		CheckSigns()

		
	Else
		CursorKeyPressed=False
	EndIf

		
	

End Function
Function MenuLoop6()

	; Display Static Text
	If Language=0
		CenterText("CONFIRM PLAYER DELETION?",4,1,50)
		CenterText("------------------------",5,1,51)
		CenterText("(backup is stored in PLAYER directory)",6,1,52)
		
		CenterText("YES",8,1,53)
		CenterText("NO",9,1,54)
	Else If Language=1
		CenterText("SPIELER LOESCHEN?",4,1,50)
		CenterText("-----------------",5,1,51)
		CenterText("(Backup im Player Directory)",6,1,52)
		
		CenterText("Ja",8,1,53)
		CenterText("Nein",9,1,54)
	Else If Language=2
		CenterText("CONFIRMEZ L'EFFACEMENT DU JOUER?",4,1,50)
		CenterText("-------------------------------",5,1,51)
		CenterText("La sauvegarde est stockee dans le dossier PLAYERS",6,1,52)
		
		CenterText("OUI",8,1,53)
		CenterText("NON",9,1,54)

	EndIf
	
	
	
		
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(8+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,4.5*.012,.02
	

	
	
	
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



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

		
		; do the updates in here
		
		LevelTimer=LevelTimer+1
		PositionTexture MenuBackGroundTex,Float(LevelTimer Mod 5120)/5120.0, -Float(LevelTimer Mod 1024)/1024.0
		PositionTexture MenuBackGroundTex2,Float(LevelTimer Mod 5120)/5120.0, -Float(LevelTimer Mod 1024)/1024.0
	
		
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip

	
	
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=1
			CursorKeyPressed=True
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=2 Then CurrentMenuOption=0
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CursorKeyPressed=True
			If CurrentMenuOption=0 
				; delete
				CopyFile "players/"+PlayerName$(Player2BDeleted)+".pla","players/"+PlayerName$(Player2BDeleted)+".bak"
				DeleteFile "players/"+PlayerName$(Player2BDeleted)+".pla"
			EndIf
			EndMenu()
			StartMenu(2)
		EndIf
	Else
		CursorKeyPressed=False
	EndIf


	
End Function

Function MenuLoop7()

	; Display Static Text
	
	If Version>=10
		i=1
	Else 
		i=0
	EndIf
	
	If Language=0
		CenterText("Welcome to the Playground",2+i,1,50)
		CenterText("-------------------------",3+i,1,51)
		CenterText("The Playground offers a collection",5+i,1,52)
		CenterText("of levels specifically designed for",6+i,1,53)
		CenterText("very young or less experienced players.",7+i,1,54)
		CenterText("Rather than placing the emphasis",8+i,1,55)
		CenterText("on progressively challenging trials,",9+i,1,56)
		CenterText("the Playground levels focus on exploration,",10+i,1,57)
		CenterText("discovery, and simple puzzles.",11+i,1,58)
	Else If Language=1
		CenterText("Beginner Levels",2+i,1,50)
		CenterText("---------------",3+i,1,51)
		CenterText("Diese Levels bieten eine Auswahl an",5+i,1,52)
		CenterText("speziell entworfenen Levels fuer sehr",6+i,1,53)
		CenterText("Junge oder unerfahrene Spieler.",7+i,1,54)
		CenterText("Im Gegensatz zu den Herausforderungen",8+i,1,55)
		CenterText("der anderen Levels, konzentrieren sich",9+i,1,56)
		CenterText("die Beginner Levels auf das Training,",10+i,1,57)
		CenterText("Entdeckung und einfache Aufgaben.",11+i,1,58)
	Else If Language=2
		CenterText("BINVENUE AU TERRAIN DE JEU",2+i,1,50)
		CenterText("--------------------------",3+i,1,51)
		CenterText("Le terrain de jeu offre une collection",5+i,1,52)
		CenterText("de niveaux specialement penses pour les",6+i,1,53)
		CenterText("tres jeunes ou les joueurs moins experimentes.",7+i,1,54)
		CenterText("Au lieu de mettre l'emphase sur",8+i,1,55)
		CenterText("des tentatives de plus en plus exigentes,",9+i,1,56)
		CenterText("ces niveaux visent l'exploration, la",10+i,1,57)
		CenterText("decouverte et les casse-tetes simples.",11+i,1,58)

	EndIf
	
	
	
	
	If Version>=10
		If Language=0 Then CenterText("Also contains Five Bonus Levels.",13+i,1,59)

		i=1
		If Language=0
			CenterText("HERE WE GO!",15+i,1,63)
			CenterText("NO THANKS",16+i,1,64)
		Else If Language=1
			CenterText("LOS GEHTS!",15+i,1,63)
			CenterText("NEIN DANKE",16+i,1,64)
		Else If Language=2
			CenterText("ALLONS-Y",15+i,1,63)
			CenterText("NON MERCI",16+i,1,64)


		EndIf
	Else 
		If Language=0
			If Leveltimer Mod 300 < 250
				CenterText("The Playground is not available in the free",13,1,59)
				CenterText("demo version. Please register to access",14,1,60)
				CenterText("these and all other levels.",15,1,62)
			Else
				CenterText("",13,1,59)
				CenterText("",14,1,60)
				CenterText("",15,1,62)
			EndIf
			CenterText("PRESS ENTER",17,1,63)
		Else If Language=1
			If Leveltimer Mod 300 < 250
				CenterText("Die Beginner Levels sind nicht in der",13,1,59)
				CenterText("Demo erhaeltlich. Bitte kaufen Sie die",14,1,60)
				CenterText("Vollversion um diese Levels zu spielen.",15,1,62)
			Else
				CenterText("",13,1,59)
				CenterText("",14,1,60)
				CenterText("",15,1,62)
			EndIf
			CenterText("'ENTER'",17,1,63)
		Else If Language=2
			If Leveltimer Mod 300 < 250
				CenterText("Le terrain de jeu n'est pas disponible",13,1,59)
				CenterText("avec la version demo gratuit. S.V.P",14,1,60)
				CenterText("enregistrez-vous pour y acceder.",15,1,62)
			Else
				CenterText("",13,1,59)
				CenterText("",14,1,60)
				CenterText("",15,1,62)
			EndIf
			CenterText("TAPE 'ENTREE'",17,1,63)

		EndIf
		i=4
	EndIf


	
	
	
	
		
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(15+i+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,11.5*.012,.02
	

	
	
	
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



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

		
		; do the updates in here
		
		LevelTimer=LevelTimer+1
		PositionTexture MenuBackGroundTex,Float(LevelTimer Mod 5120)/5120.0, -Float(LevelTimer Mod 1024)/1024.0
		PositionTexture MenuBackGroundTex2,Float(LevelTimer Mod 5120)/5120.0, -Float(LevelTimer Mod 1024)/1024.0
	
		
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip

	
	
	If (KeyDown(200) Or KeyDown(72)) And Version>=10
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=1
			CursorKeyPressed=True
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) And Version>=10
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=2 Then CurrentMenuOption=0
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CursorKeyPressed=True
			If CurrentMenuOption=0 And Version>=10
				PlayMySound(SFXTalk(1),0)
				EndMenu()
				StartMenu(8)
			Else
				EndMenu()
				StartMenu(1)
			EndIf
			
		EndIf
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			StartMenu(1)
		EndIf

	Else
		CursorKeyPressed=False
	EndIf


	
End Function

Function MenuLoop9()

	If Language=0	
		If OptionsSoundLevel=1
			OptionsSoundText$="Sound Volume: High"
		Else If OptionsSoundLevel=.6
			OptionsSoundText$="Sound Volume: Medium"
		Else If OptionsSoundLevel=.2
			OptionsSoundText$="Sound Volume: Low"
		Else If OptionsSoundLevel=0
			OptionsSoundText$="No Sound"
		EndIf
		If OptionsMusicLevel=1
			OptionsMusicText$="Music Volume: High"
		Else If OptionsMusicLevel=.7
			OptionsMusicText$="Music Volume: Medium"
		Else If OptionsMusicLevel=.4
			OptionsMusicText$="Music Volume: Low"
		Else If OptionsMusicLevel=0
			OptionsMusicText$="No Music"
		EndIf
		OptionsResText$="Resolution: "+Str$(MyGfxModeWidth(MyGfxMode))+"x"+Str$(MyGfxModeHeight(MyGfxMode))
		If DetailLevel=5
			OptionsDetailText$="Detail Level: High"
		Else If DetailLevel=3
			OptionsDetailText$="Detail Level: Medium"
		Else If DetailLevel=1
			OptionsDetailText$="Detail Level: Low"
		EndIf
		If MyWindowed=0
			OptionsWindowedText$="Full Screen"
		Else
			OptionsWindowedText$="Windowed"
		EndIf
		If NoTimeLimit=1
			OptionsTimeLimitText$="No Time Limit"
		Else
			OptionsTimeLimitText$="Timed Mode"
		EndIf
		If LockWorlds=0
			LockWorldsText$="Normal World Progression"
		Else
			LockWorldsText$="All Worlds Unlocked"
		EndIf
	
		OptionsLanguageText$="English"
		
		If OptionsTeleport=0
			TeleportText$="Teleport: Pent"
		Else
			TeleportText$="Teleport: Star"
		EndIf
		
		If OPtionsSwitchPlayerSound=1
			SwitchText$="Switch SFX: New"
		Else
			SwitchText$="Switch SFX: Classic"
		EndIf
		

		
	
	
		; Options Menu
		
			
		CenterText("OPTIONS",5,1,50)
		CenterText("-------",6,1,51)
		CenterText(OptionsSoundText$,8,1,52)
		CenterText(OptionsMusicText$,9,1,53)
		;CenterText(OptionsResText$,10,1,54)
		CenterText(OptionsDetailText$,10,1,55)
		;CenterText(OptionsWindowedText$,12,1,56)
		;CenterText(OPtionsLanguageText$,13,1,58)
		CenterText(OPtionsTimeLimitText$,11,1,60)
		CenterText(LockWorldsText$,12,1,54)
		CenterText(TeleportText$,13,1,56)
		;CenterText(SwitchText$,14,1,61)

		CenterText("Visit Us Online",14,1,59)
		CenterText("MAIN MENU",15,1,57)
		
	Else If Language=1
	
		OptionsLanguageText$="Deutsch"
	
		If OptionsSoundLevel=1
			OptionsSoundText$="Sound: Laut"
		Else If OptionsSoundLevel=.6
			OptionsSoundText$="Sound: Mittel"
		Else If OptionsSoundLevel=.2
			OptionsSoundText$="Sound: Leise"
		Else If OptionsSoundLevel=0
			OptionsSoundText$="Kein Sound"
		EndIf
		If OptionsMusicLevel=1
			OptionsMusicText$="Musik: Laut"
		Else If OptionsMusicLevel=.7
			OptionsMusicText$="Musik: Mittel"
		Else If OptionsMusicLevel=.4
			OptionsMusicText$="Musik: Leise"
		Else If OptionsMusicLevel=0
			OptionsMusicText$="Keine Musik"
		EndIf
		OptionsResText$="Aufloesung: "+Str$(MyGfxModeWidth(MyGfxMode))+"x"+Str$(MyGfxModeHeight(MyGfxMode))
		If DetailLevel=5
			OptionsDetailText$="Detaillevel: Hoch"
		Else If DetailLevel=3
			OptionsDetailText$="Detaillevel: Mittel"
		Else If DetailLevel=1
			OptionsDetailText$="Detaillevel: Niedrig"
		EndIf
		If MyWindowed=0
			OptionsWindowedText$="Vollbild"
		Else
			OptionsWindowedText$="Fenster"
		EndIf
		If NoTimeLimit=1
			OptionsTimeLimitText$="Kein Zeitlimit"
		Else
			OptionsTimeLimitText$="Timer Modus"
		EndIf

	
	
	
		; Options Menu
		
			
		CenterText("OPTIONEN",5,1,50)
		CenterText("--------",6,1,51)
		CenterText(OptionsSoundText$,8,1,52)
		CenterText(OptionsMusicText$,9,1,53)
		CenterText(OptionsResText$,10,1,54)
		CenterText(OptionsDetailText$,11,1,55)
		CenterText(OptionsWindowedText$,12,1,56)
		CenterText(OPtionsLanguageText$,13,1,58)
		CenterText(OPtionsTimeLimitText$,14,1,60)

		CenterText("Unsere Webseite",15,1,59)
		CenterText("HAUPTMENUE",16,1,57)
	Else If Language=2
	
		OptionsLanguageText$="Francais"
	
		If OptionsSoundLevel=1
			OptionsSoundText$="Niveau du Son: 3"
		Else If OptionsSoundLevel=.6
			OptionsSoundText$="Niveau du Son: 2"
		Else If OptionsSoundLevel=.2
			OptionsSoundText$="Niveau du Son: 1"
		Else If OptionsSoundLevel=0
			OptionsSoundText$="Niveau du Son: 0"
		EndIf
		If OptionsMusicLevel=1
			OptionsMusicText$="Niveau de la Musique: 3"
		Else If OptionsMusicLevel=.7
			OptionsMusicText$="Niveau de la Musique: 2"
		Else If OptionsMusicLevel=.4
			OptionsMusicText$="Niveau de la Musique: 1"
		Else If OptionsMusicLevel=0
			OptionsMusicText$="Niveau de la Musique: 0"
		EndIf
		OptionsResText$="Resolution: "+Str$(MyGfxModeWidth(MyGfxMode))+"x"+Str$(MyGfxModeHeight(MyGfxMode))
		If DetailLevel=5
			OptionsDetailText$="Detail: Haut"
		Else If DetailLevel=3
			OptionsDetailText$="Detail: Medium"
		Else If DetailLevel=1
			OptionsDetailText$="Detail: Bas"
		EndIf
		If MyWindowed=0
			OptionsWindowedText$="Plein Ecran"
		Else
			OptionsWindowedText$="En Fenetre"
		EndIf
		If NoTimeLimit=1
			OptionsTimeLimitText$="Pas de Chrono"
		Else
			OptionsTimeLimitText$="Mode Chrono"
		EndIf

	
	
	
		; Options Menu
		
			
		CenterText("OPTIONS",5,1,50)
		CenterText("-------",6,1,51)
		CenterText(OptionsSoundText$,8,1,52)
		CenterText(OptionsMusicText$,9,1,53)
		CenterText(OptionsResText$,10,1,54)
		CenterText(OptionsDetailText$,11,1,55)
		CenterText(OptionsWindowedText$,12,1,56)
		CenterText(OPtionsLanguageText$,13,1,58)
		CenterText(OPtionsTimeLimitText$,14,1,60)
		CenterText("Notre Site Web",15,1,59)
		CenterText("Menu Principal",16,1,57)

	EndIf
	
	
		
	
		
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(8+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,22*.012,.02
	
	
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



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

		
		; do the updates in here
		
		LevelTimer=LevelTimer+1
		MenuBkgUpdate()		
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip

	If KeyDown(2)
		SecretMusic=1
;		PlaySound(sfxteleport);

	EndIf
	If KeyDown(11) And SecretMusic>300 And DoesSoundExist=True
		SecretMusic=0
		StopChannel(music)
		;CopyFile "res11.dat","res11.ogg"
		blabla=MyLoadSound("newsounds\song1c.wav",False)
		PlaySound(blabla)
	;	Delay 1000
	;	DeleteFile "res11.ogg"
;		PlaySound(sfxteleport)
	EndIf
	If SecretMusic>0 Then SecretMusic=SecretMusic+1
	
	
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=7
			CursorKeyPressed=True
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=8 Then CurrentMenuOption=0
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CursorKeyPressed=True
			If CurrentMenuOption=0 
				If OptionsSoundLevel=1
					OptionsSoundLevel=.6
					
				Else If OptionsSoundLevel=.6
					OptionsSoundLevel=.2
				Else If OptionsSoundLevel=.2
					OptionsSoundLevel=0
				Else
					OptionsSoundLevel=1
				EndIf
			Else If CurrentMenuOption=1 
				If OptionsMusicLevel=1
					OptionsMusicLevel=.7
					
				Else If OptionsMusicLevel=.7
					OptionsMusicLevel=.4
				Else If OptionsMusicLevel=.4
					OptionsMusicLevel=0
				Else
					OptionsMusicLevel=1
				EndIf
				If DoesSoundExist=True Then	ChannelVolume Music,OptionsMusicLevel
			;Else If CurrentMenuOption=2 
			;	Repeat
			;		MyGfxMode=MyGfxMode+1
			;	Until MyGfxModeExists(MyGfxMode)=1 Or MyGfxMode=4
			;	If MyGfxMode=4
			;		MyGfxMode=0
			;	EndIf

			Else If CurrentMenuOption=2 
				If DetailLevel=5
					DetailLevel=3
					
				Else If DetailLevel=3
					DetailLevel=1
				Else 
					DetailLevel=5
				EndIf
				
			;Else If CurrentMenuOption=4
			;	If MyWindowed=0
			;		MyWindowed=1
			;	Else
			;		MyWindowed=0
			;	EndIf
			;Else If CurrentMenuOption=5
			;	Language=Language+1
			;	If Language=3 Then Language=0
			Else If  CurrentMenuOption=3
				NoTimeLimit=1-NoTimeLimit
			Else If CurrentMenuOption=4
				LockWorlds=1-LockWorlds
			Else If CurrentMenuOption=5
				OptionsTeleport=1-OptionsTeleport
			Else If CurrentMenuOption=6
				EndMenu()
				; go to site!
				VersionNo1=-996
				StartMenu(15)
				
			Else If CurrentMenuOption=7
				EndMenu()
				SaveOptions()
				StartMenu(1)
			
			EndIf
			
		EndIf
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			SaveOptions()
			StartMenu(1)
		EndIf

	Else
		CursorKeyPressed=False
	EndIf


	
End Function

Function MenuLoop10()

	; Display Static Text
	
	If Language=0	
		CenterText("Exit to Windows?",7,1,50)
		CenterText("----------------",8,1,51)
		CenterText("SURE THING",10,1,52)
		CenterText("NO THANKS",11,1,53)
	Else If Language=1
		CenterText("Wonderland Beenden?",7,1,50)
		CenterText("--------------------",8,1,51)
		CenterText("JA BITTE",10,1,52)
		CenterText("NEIN DANKE",11,1,53)
	Else If Language=2
		CenterText("Sortie vers Windows?",7,1,50)
		CenterText("--------------------",8,1,51)
		CenterText("BIEN SUR",10,1,52)
		CenterText("NON MERCI",11,1,53)

	EndIf
	
	
	
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(10+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,11.5*.012,.02
	

	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



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

		
		; do the updates in here
		
		LevelTimer=LevelTimer+1
		MenuBkgUpdate()
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip

	
	
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=1
			CursorKeyPressed=True
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=2 Then CurrentMenuOption=0
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CursorKeyPressed=True
			If CurrentMenuOption=0 
				If Version>=10
					EndMenu()
					GameExit=True
				Else 
					EndMenu()
					StartMenu(11)
				EndIf
			Else
				EndMenu()
				StartMenu(1)
			EndIf
			
		EndIf
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			If Version>=10
				EndMenu()
				GameExit=True
			Else 
				EndMenu()
				StartMenu(11)
			EndIf
		EndIf

	Else
		CursorKeyPressed=False
	EndIf


	
End Function

Function MenuLoop12()

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
	
	
	; text in here
	
	t2=leveltimer Mod 7800
	
	If t2<6000
		centertext("Congratulations!!!",2,1,52)
		centertext("You have completed",4,1,53)
		centertext("Return To Wonderland",5,1,50)
		centertext("Thank you for playing!",7,1,51)
		centertext("Special Thanks from",15,1,54)
		
		If t2<600
			centertext("Stinky!",16,1,55)
		Else If t2<1200
			centertext("Loof!",16,1,55)
		Else If t2<1800
			centertext("Scouge!",16,1,55)
		Else If t2<2400
			centertext("Coily!",16,1,55)
		Else If t2<3000
			centertext("Kaboom!",16,1,55)
		Else If t2<3600
			centertext("Z-Bot!",16,1,55)
		Else If t2<4200
			centertext("The Flying Saucer!",16,1,55)
		Else If t2<4800
			centertext("Red Chomper!",16,1,55)
		Else If t2<5400
			centertext("Gold Chomper!",16,1,55)
		Else 
			centertext("and Ghost!",16,1,55)
		EndIf
	Else
		centertext("Well Done!",6,1,50)
		centertext("We hope you enjoyed the Game!",8,1,51)
		centertext("Best Wishes from your Friends at",10,1,52)
		centertext("Midnight Synergy",11,1,53)
		centertext("www.midnightsynergy.com",13,1,54)
		centertext("",14,1,55)
	EndIf


	
		
	
	
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next

	
	
	
	For k=1 To TweenTicks
		Tweentime=Tweentime+Tweenperiod
		If k=Tweenticks 
			CaptureWorld
			
		EndIf

		LevelTimer=LevelTimer+1
		
		EndBkgUpdate()
		;MoveEntity Camera,0,0,-.1

			
		
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	
	

	
	
	
	
	
	
	
	Flip
	
	
	If KeyHit(1) 
		If CursorKeyPressed=False
			EndMenu()
			StartMenu(1)
			CursorKeyPressed=True
		EndIf
	Else
		CursorKeyPressed=False
	End If


	
	
	

End Function

Function MenuLoop13()

	; Display Static Text
	
	
	CenterText("SELECT LEVEL PACK",2,1,50)
	CenterText("-----------------",3,1,51)
	
	CenterText("Tutorial Levels",7,1,52)
	CenterText("Classic Levels",8,1,53)
	CenterText("Deluxe Levels",9,1,54)
	;CenterText("Kids Levels",10,1,55)
	CenterText("Custom Levels",10,1,56)

	
	If LevelTimer<50 Then	LevelTimer=LevelTimer+1
	
	If CurrentMenuOption=0
		CenterText("A set of twenty tutorial levels:",19.0-Float(LevelTimer)/10.0,1,57)
		CenterText("Learn the ins and outs of basic gameplay",20.0-Float(LevelTimer)/10.0,1,58)
		CenterText("and practice your Wonderland skills",21.0-Float(LevelTimer)/10.0,1,59)
		CenterText("with a set of easy levels.",22.0-Float(LevelTimer)/10.0,1,60)
	Else If CurrentMenuOption=1
		CenterText("Take Stinky and Loof on and adventure",19.0-Float(LevelTimer)/10.0,1,57)
		CenterText("through ten worlds and eighty-five",20.0-Float(LevelTimer)/10.0,1,58)
		CenterText("diverse levels of increasing difficulty.",21.0-Float(LevelTimer)/10.0,1,59)
		CenterText("",22.0-Float(LevelTimer)/20.0,1,60)
	Else If CurrentMenuOption=2
		CenterText("Fifty levels featuring new gameplay elements",19.0-Float(LevelTimer)/10.0,1,57)
		CenterText("and a brand new story: Strange Rainbow",20.0-Float(LevelTimer)/10.0,1,58)
		CenterText("Spirits have appeared in Wonderland. Can Stinky,",21.0-Float(LevelTimer)/10.0,1,59)
		CenterText("Loof, and their new friends solve the mystery?",22.0-Float(LevelTimer)/10.0,1,60)
;	Else If CurrentMenuOption=3
;		CenterText("Thirty easy to play levels for",19.0-Float(LevelTimer)/10.0,1,57)
;		CenterText("young and/or novice players.",20.0-Float(LevelTimer)/10.0,1,58)
;		CenterText("Explore a Wonderland of fun!",21.0-Float(LevelTimer)/10.0,1,59)
;		CenterText("",22.0-Float(LevelTimer)/20.0,1,60)
	Else If CurrentMenuOption=3
		CenterText("Hundreds of custom levels created by other",19.0-Float(LevelTimer)/10.0,1,57)
		CenterText("Return To Wonderland players.",20.0-Float(LevelTimer)/10.0,1,58)
		CenterText("Visit the online community and add more",21.0-Float(LevelTimer)/10.0,1,59)
		CenterText("free custom levels anytime!",22.0-Float(LevelTimer)/10.0,1,60)
	EndIf

		
	
	
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(7+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,21.5*.012,.02
	

	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



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

		
		; do the updates in here
		
;		LevelTimer=LevelTimer+1
		MenuBkgUpdate()
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip

	
	
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=3
			CursorKeyPressed=True
			LevelTimer=0
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=4 Then CurrentMenuOption=0
			CursorKeyPressed=True
			LevelTimer=0
		EndIf
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			;PlayMySound(SFXLastCoin,0)	
			CursorKeyPressed=True
			EndMenu()
			If CurrentMenuOption<3
				WhichWorldPack=CurrentMenuOption
				If WhichWorldPack<2 Then WhichWorldPack=1-WhichWorldPack ; switch 0/1
				CurrentWorld=CurrentWorldPack(WhichWorldPack)
				CurrentLevel=CurrentWorld*10
				
			
				StartMenu(4)
			
			Else If CurrentMenuOption=3
				StartMenu(14)
			
			EndIf
			
		EndIf
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			StartMenu(1)
			
		EndIf

	Else
		CursorKeyPressed=False
	EndIf


	
End Function


Function MenuLoop14()

	; Display Static Text
	
	
	;DisplayText("Select A Custom Level To Play.",0,0,1,50)
	DisplayText("FILENAME:      LEVELNAME:                 SCORE:",1,1,1,51)
	DisplayText("------------------------------------------------",1,2,1,52)
	
	For i=0 To 14
		j=FileDirPos+i
		If j<FileDirN
			If Upper$(Right$(FileDir$(j),4))=".LEV" Or Upper$(Right$(FileDir$(j),4))=".LV5" Or Upper$(Right$(FileDir$(j),4))=".LV6"
				DisplayText(Left$(Upper$(Left$(FileDir$(j),Len(Filedir$(j))-4)),14),1,i+3,1,100+i*4)
			Else 
				DisplayText(Left$(Upper$(Filedir$(j)),14),1,i+3,1,100+i*4)
			EndIf
			If Len(Filedir2$(j))<27
				DisplayText(Filedir2$(j),16,i+3,1,101+i*4)
			Else
				DisplayText(Left$(Filedir2$(j),24)+"...",16,i+3,1,101+i*4)
			EndIf
			If FileDirScore(j)=0
				DisplayText("-----",44,i+3,1,102+i*4)
			Else If FileDirScore(j)>0
				DisplayText(Left$(Str$(FileDirScore(j)),5),44,i+3,1,102+i*4)
			EndIf
			If FileDirPerfect(j)=1
				DisplayText("*",49,i+3,1,103+i*4)
			Else	
				DisplayText("",49,i+3,1,103+i*4)
			EndIf
		EndIf
	Next
		
	
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(3+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,50*.012,.02
	

	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



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

		
		; do the updates in here
		
		LevelTimer=LevelTimer+1
		MenuBkgUpdate()
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip

	Keypress=GetKey()
	; keyboard - starting letter
	If (Keypress>64 And Keypress<91) Or (Keypress>96 And Keypress<123) 
		a$=Upper$(Chr$(keypress))
		For i=0 To FileDirN
			If a$<=FileDir$(i) And FileDirScore(i)>=0
				Exit
			EndIf
		Next
		
		If i>=FileDirN Then i=FileDirN-1
	
		If FileDirN-i>=15
			FileDirPos=i
			CurrentMenuOption=0
		Else
			FileDirPos=FileDirN-15
			If FileDirPos<0 
				FileDirPos=0
				CurrentMenuOption=i
			Else
				CurrentMenuOption=15-(FileDirN-i)
			EndIf
		EndIf
		
	EndIf

	
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=0 Or (CursorKeyPressed>5 And CursorKeyPressed Mod 2 = 0)
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 
				CurrentMenuOption=0
				If FileDirPos>0
					FileDirPos=FileDirPos-1
				EndIf
			EndIf
			CursorKeyPressed=CursorKeyPressed+1
		Else 
			CursorKeyPressed=CursorKeyPressed+1
			;If CursorKeyPressed>=2 Then CursorKeyPressed=0
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=0 Or (CursorKeyPressed>5 And CursorKeyPressed Mod 2 = 0)

			PlayMySound(SFXMenuSound(0),0)	
			If CurrentMenuOption<FileDirN-1 Then CurrentMenuOption=CurrentMenuOption+1
							
			If CurrentMenuOption=15
				CurrentMenuOption=14
				If FileDirPos+15<FileDirN
					FileDirPos=FileDirPos+1
				EndIf
			EndIf
			CursorKeyPressed=CursorKeyPressed+1

		Else 
			CursorKeyPressed=CursorKeyPressed+1
			;If CursorKeyPressed>=2 Then CursorKeyPressed=0
		EndIf

	Else If (KeyDown(201))
		; page up
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			FileDirPos=FileDirPos-15
			If FileDirPos<0 
				CurrentMenuOption=CurrentMenuOption+FileDirPos
				FileDirPos=0
				If CurrentMenuOption<0 Then CurrentMenuOption=0
			EndIf
	
			CursorKeyPressed=True
		EndIf

		
	
	Else If (KeyDown(209))
		; page down
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			If FileDirN>15
				FileDirPos=FileDirPos+15
				If FileDirPos+15>=FileDirN
					FileDirPos=FileDirN-15 
					CurrentMenuOption=14
				EndIf
			Else
				CurrentMenuOption=FileDirN-1
			EndIf
	
			CursorKeyPressed=True
		EndIf

		

	
	
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			;PlayMySound(SFXLastCoin,0)	
			CursorKeyPressed=True
			
			
			; back 
			If FileDirScore(CurrentMenuOption+FileDirPos)=-1
				EndMenu()
				If CurrentExtraLevelDirectory$=""
					StartMenu(13)
					PlayMySound(SFXMenuSound(0),0)
				Else
					;chop off one directory
					Repeat
						CurrentExtraLevelDirectory$=Left$(CurrentExtraLevelDirectory$,Len(CurrentExtraLevelDirectory$)-1)
					Until Right$(CurrentExtraLevelDirectory$,1)="\" Or CurrentExtraLevelDirectory$=""
					CurrentFileDir=0
					StartMenu(14)
					PlayMySound(SFXMenuSound(0),0)		
				EndIf
			EndIf
			; directory
			If FileDirScore(CurrentMenuOption+FileDirPos)=-2
				
				EndMenu()
				CurrentExtraLevelDirectory$=CurrentExtraLevelDirectory$+FileDir$(CurrentMenuOption+FileDirPos)+"\"
				CurrentFileDir=0
				StartMenu(14)
				PlayMySound(SFXMenuSound(0),0)
			EndIf
			
			; level
			If FileDirScore(CurrentMenuOption+FileDirPos)>=0 And (Upper$(Right$(FileDir$(CurrentMenuOption+FileDirPos),4))=".LEV" Or Upper$(Right$(FileDir$(CurrentMenuOption+FileDirPos),4))=".LV5" Or Upper$(Right$(FileDir$(CurrentMenuOption+FileDirPos),4))=".LV6")
				
				EndMenu()
				If CurrentLevel<>-999 Then CurrentLevelBackup=CurrentLevel
				CurrentLevel=-999
				CurrentFileDir=CurrentMenuOption+FileDirPos

				i=extralevelmusictrack
				
				


				StartLevel(-999)
				
								
				If ExtraLevelMusicTrack<>i				
					StopChannel Music
					If ExtraLevelMusicTrack=1
						Music=PlayMusic("res4.dat")
					Else If ExtraLevelMusicTrack=2
						Music=PlayMusic("res3.dat")
					Else If ExtraLevelMusicTrack=3
						Music=PlayMusic("res5.dat")
					Else If ExtraLevelMusicTrack=4
						Music=PlayMusic("res6.dat")
					Else If ExtraLevelMusicTrack=5
						Music=PlayMusic("res7.dat")
					Else 
						Music=PlayMusic("res8.dat")
					EndIf
					If ExtraLevelMusicTrack=1 Or ExtraLevelMusicTrack=5
						ChannelVolume music,OptionsMusicLevel/2.0
					Else
						ChannelVolume music,OptionsMusicLevel
					EndIf
				
				EndIf


					
				;PlayMySound(SFXTalk(Rand(0,4)),0)
				Return
			EndIf

			
		EndIf
	Else If KeyDown(59)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			CustomSortByName=True
			StartMenu(14)
		EndIf
	Else If KeyDown(60)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			CustomSortByName=False
			StartMenu(14)
		EndIf

	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			
			StartMenu(13)
			
		EndIf

	Else
		CursorKeyPressed=False
	EndIf


	
End Function

Function MenuLoop15()

	; Display Static Text
	
	If Language=0	
		CenterText("Go Online?",4,1,50)
		CenterText("----------",5,1,51)
		CenterText("This option will exit Return to Wonderland",7,1,52)
		CenterText("and connect you to an Internet Website.",8,1,53)
		
		CenterText("PROCEED",10,1,54)
		CenterText("NO THANKS",11,1,55)
	Else If Language=1
		CenterText("Wonderland Beenden?",7,1,50)
		CenterText("--------------------",8,1,51)
		CenterText("JA BITTE",10,1,52)
		CenterText("NEIN DANKE",11,1,53)
	Else If Language=2
		CenterText("Sortie vers Windows?",7,1,50)
		CenterText("--------------------",8,1,51)
		CenterText("BIEN SUR",10,1,52)
		CenterText("NON MERCI",11,1,53)

	EndIf
	
	
	
	PositionEntity MenuSelector,-.5+24.4*.02+.01,0.372-Float(10+CurrentMenuOption)*.044+.027,1.1
	ScaleSprite MenuSelector,11.5*.012,.02
	

	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next



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

		
		; do the updates in here
		
		LevelTimer=LevelTimer+1
		MenuBkgUpdate()
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	Flip

	
	
	If (KeyDown(200) Or KeyDown(72)) 
		If CursorKeyPressed=False		
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption-1
			If CurrentMenuOption=-1 Then CurrentMenuOption=1
			CursorKeyPressed=True
		EndIf

	Else If (KeyDown(208) Or KeyDown(80)) 
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CurrentMenuOption=CurrentMenuOption+1
			If CurrentMenuOption=2 Then CurrentMenuOption=0
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(28) Or KeyDown(156))
		If CursorKeyPressed=False
			PlayMySound(SFXMenuSound(0),0)	
			CursorKeyPressed=True
			If CurrentMenuOption=0 
				If Version>=10
					EndMenu()
					GameExit=True
				EndIf
			Else
				EndMenu()
				VersionNo1=1
				StartMenu(1)
			EndIf
			
		EndIf
	Else If KeyDown(1)
		If CursorKeyPressed=False
			CursorKeyPressed=True
			EndMenu()
			VersionNo1=1
			StartMenu(1)
		EndIf

	Else
		CursorKeyPressed=False
	EndIf


	
End Function


Function MenuLoop16()

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
	
	
	; text in here
	
	

	If LevelTimer<1000
		centertext("",1,1,52)
		centertext("",2,1,53)
		centertext("",3,1,50)
	Else If LevelTimer<2000
		centertext("",1,1,52)
		centertext("And so,",2,1,53)
		centertext("",3,1,50)
	Else If LevelTimer<3000
		centertext("",1,1,52)
		centertext("after a long journey",2,1,53)
		centertext("",3,1,50)
	Else If LevelTimer<4000
		centertext("the Rainbow Spirits",1,1,52)
		centertext("returned to the",2,1,53)
		centertext("Rainbow World",3,1,50)
	Else If LevelTimer<5000
		centertext("and balance",1,1,52)
		centertext("was once again restored",2,1,53)
		centertext("in Wonderland.",3,1,50)
	Else If LevelTimer<6000
		centertext("",1,1,52)
		centertext("What happened next?",2,1,53)
		centertext("",3,1,50)
	Else If LevelTimer<7000
		centertext("Mayor Peegue returned to",1,1,52)
		centertext("Wondertown and helped fellow",2,1,53)
		centertext("Wonderlanders rebuild their city.",3,1,50)
	Else If LevelTimer<8000
		centertext("Qookie went back to the cottage,",1,1,52)
		centertext("made friends with the wild wood chompers,",2,1,53)
		centertext("and shared some giant mushrooms with them.",3,1,50)
	Else If LevelTimer<9000
		centertext("Stinky and Loof",1,1,52)
		centertext("briefly considered retirement.",2,1,53)
		centertext("",3,1,50)
	Else If LevelTimer<10000
		centertext("But another adventure",1,1,52)
		centertext("was sure to be",2,1,53)
		centertext("just around the corner.",3,1,50)
	Else If LevelTimer<11000
		centertext("",1,1,52)
		centertext("",2,1,53)
		centertext("",3,1,50)
	Else If LevelTimer<12000
		centertext("THE END",1,1,52)
		centertext("",2,1,53)
		centertext("Thank you for playing!",3,1,50)

	



	EndIf

		
	
		


	
		
	
	
	; turn on text and signs
	For l.LetterType=Each LetterType
		If l\Active=False
			l\Active=True
			ShowEntity l\Entity
		EndIf
	Next

	
	
	
	For k=1 To TweenTicks
		Tweentime=Tweentime+Tweenperiod
		If k=Tweenticks 
			CaptureWorld
			
		EndIf


		EndBkgUpdateDeluxe()
		LevelTimer=LevelTimer+1
		
		
		;MoveEntity Camera,0,0,-.1

			
		
		
		UpdateWorld
	
	Next
		
	RenderWorld tween
	
	
	

	
	
	
	
	
	
	
	Flip
	
	
	If KeyHit(1) 
		If CursorKeyPressed=False
			EndMenu()
			FreeTexture BackGroundTexture
			FreeTexture BackGroundTexture2
			For i=0 To 49
				ScaleSprite MenuEntity(i+10),.6,.6
			Next
			For i=62 To 66
				If MenuEntity(i)<>0 HideEntity MenuEntity(i)
			Next
	

			StartMenu(1)
			CursorKeyPressed=True
		EndIf
	Else
		CursorKeyPressed=False
	End If


	
	
	

End Function



Function EndMenu()

	
	HideEntity Logo1
	HideEntity Logo2
	HideEntity MenuSelector
	For i=0 To 61
		If MenuEntity(i)<>0 HideEntity MenuEntity(i)
	Next
	
	; Fonts
	For l.LetterType=Each LetterType
		FreeEntity l\Entity
		Delete l
	Next



End Function
Function StartLevelMusic()

	StopChannel Music

	If CurrentWorld+1=1 Or CurrentWorld+1=5 Or CurrentWorld+1=23 Or CurrentWorld+1=25 Or CurrentWorld+1=12
		Music=PlayMusic("res5.dat")	
	Else If CurrentWorld+1=3 Or CurrentWorld+1=9 Or CurrentWorld+1=27
		Music=PlayMusic("res8.dat")
	Else If CurrentWorld+1=2 Or CurrentWorld+1=6 Or CurrentWorld+1=10 Or CurrentWorld+1=21 Or CurrentWorld+1=24 Or CurrentWorld+1=11
		Music=PlayMusic("res3.dat")
	Else If CurrentWorld+1=8 Or CurrentWorld+1=28
		Music=PlayMusic("res4.dat")
		
;	If CurrentWorld+1=4 Or CurrentWorld+1=9
;		Music=PlayMusic("res6.dat")
;	Else If (CurrentWorld+1=2 And Version>=10) Or CurrentWorld+1=6 Or CurrentWorld+1=10 
;		Music=PlayMusic("res5.dat")
;	Else If CurrentWorld+1=8 
;		Music=PlayMusic("res4.dat")
	Else 
		Music=PlayMusic("res6.dat")
	EndIf
	
	If CurrentWorld+1=8 Or CurrentWorld+1=28
		ChannelVolume music,OptionsMusicLevel/2.0
	Else
		ChannelVolume music,OptionsMusicLevel
	EndIf


End Function
Function CreateNewPlayer()
	; reset all data
	CurrentWorld=0
	For i=0 To PlayGroundLevel-1
		CurrentPlayerLevelScore(i)=0
	Next
	
	CurrentPlayerID=0
	For i=1 To Len(CurrentPlayername$)
		CurrentPlayerID=CurrentPlayerID+Asc(Mid$(currentplayername$,i,1))
	Next
	CurrentPlayerID=CurrentPlayerID+MilliSecs()
		
	; and create new player file on disk
	If CurrentPlayerName$="" Then CurrentPlayerName$="ANONYMOUS"
	
	File=ReadFile("players/"+Lower$(CurrentPlayerName$)+".pla")

	If File=0
		;CloseFile(File)
	Else
		CloseFile(File)
		CopyFile "players/"+Lower$(CurrentPlayerName$)+".pla","players/"+Lower$(CurrentPlayerName$)+".bak"
	EndIf

	
	File=WriteFile("players/"+Lower$(CurrentPlayerName$)+".pla")
	
	;*********************** LATER:

	; Need error handling if file dne
	
	;*****************************

	j=0
	WriteString(File,"Stinky & Loof Player File v2")	
	WriteString(File,CurrentPlayerName$)
	WriteInt (File,CurrentPlayerID)
	WriteInt (File,CurrentWorld)
	WriteInt (File,j)		; # of levels completed
	WriteInt (File,j)		; total score
	For i=0 To PlayGroundLevel-1
		WriteInt(File,CurrentPlayerLevelScore(i))
	Next
	
	WriteInt File,0		;extra levels completed, but there are none at first
	CurrentPlayerExtraLevels=0
	
	For i=0 To 3
		CurrentWorldPack(i)=i*10
		WriteInt File,CurrentWorldPack(i)
	Next
	
	

	CloseFile(File)
	
	CurrentLevel=CurrentWorld*10
	
	

End Function
Function LoadPlayer(FileName$)
	File=ReadFile("players/"+Lower$(FileName$)+".pla")
	
	;*********************** LATER:

	; Need error handling if file dne
	
	;*****************************

	CurrentPlayerName$=ReadString$(File)	; just the "player file" message
	CurrentPlayerName$=ReadString$(File)
	CurrentPlayerID=ReadInt(File)
	CurrentWorld= ReadInt(File)
	j=ReadInt(File)		; # of levels completed - don't need these at this stage
	j=ReadInt(File)		; total score
	For i=0 To PlayGroundLevel-1
		CurrentPlayerLevelScore(i)=ReadInt(File)
	Next
	
	CurrentPlayerExtraLevels=ReadInt(File)
	i=0
	While i<CurrentPlayerExtraLevels
		CurrentPlayerExtraLevelID(i)=ReadInt(File)
		CurrentPlayerExtraLevelID2(i)=ReadInt(File)
		CurrentPlayerExtraLevelScore(i)=ReadInt(File)
		i=i+1
	Wend
	
	For i=0 To PlayGroundLevel-1
		CurrentPlayerLevelPerfect(i)=ReadInt(File)
	Next
	
	i=0
	While i<CurrentPlayerExtraLevels
		CurrentPlayerExtraLevelPerfect(i)=ReadInt(File)
		i=i+1
	Wend

	
	For i=0 To 3
		CurrentWorldPack(i)=ReadInt(File)
		If i>0 And CurrentWorldPack(i)=0
			CurrentWorldPack(i)=i*10
		EndIf
	Next


	
	CloseFile(File)
	
	

	
	CurrentLevel=CurrentWorld*10
	
	
	
End Function

Function SavePlayer()
	; saves teh current player
	
	File=WriteFile("players/"+Lower$(CurrentPlayerName$)+".pla")
	
	If File=0
		Return
	EndIf

	
	
	j=0
	j2=0
	WriteString(File,"Stinky & Loof Player File v2")	
	WriteString(File,CurrentPlayerName$)
	WriteInt (File,CurrentPlayerID)
	WriteInt (File,CurrentWorld)
	
	For i=0 To PlayGroundLevel-1
		If CurrentPlayerLevelScore(i)>0
			j=j+1
			j2=j2+CurrentPlayerLevelScore(i)
		EndIf
	Next
	i=0
	While i<CurrentPlayerExtraLevels
		j2=j2+CurrentPlayerExtraLevelScore(i)
		i=i+1
	Wend
				
	WriteInt (File,j)		; # of levels completed
	WriteInt (File,j2)		; total score
	For i=0 To PlayGroundLevel-1

		WriteInt(File,CurrentPlayerLevelScore(i))
	Next
	
	WriteInt File,CurrentPlayerExtraLevels
	
	i=0
	While i<CurrentPlayerExtraLevels
		WriteInt File,CurrentPlayerExtraLevelID(i)
		WriteInt File,CurrentPlayerExtraLevelID2(i)
		WriteInt File,CurrentPlayerExtraLevelScore(i)
		i=i+1
	Wend
	
	For i=0 To PlayGroundLevel-1

		WriteInt(File,CurrentPlayerLevelPerfect(i))
	Next
	
	i=0
	While i<CurrentPlayerExtraLevels
		WriteInt File,CurrentPlayerExtraLevelPerfect(i)
		i=i+1
	Wend
	
	If CurrentWorld<10
		CurrentWorldPack(0)=CurrentWorld
	Else If CurrentWorld<20
		CurrentWorldPack(1)=CurrentWorld
	Else If CurrentWorld<30
		CurrentWorldPack(2)=CurrentWorld
	Else If CurrentWorld<40
		CurrentWorldPack(3)=CurrentWorld
	EndIf
	
	For i=0 To 3
		WriteInt(File,CurrentWorldPack(i))
	Next





	CloseFile(File)
	
	
	
End Function

Function MenuBkgUpdate()

	MenuTimer=MenuTimer+1
	
	If MenuBkgStyle=0
		PositionTexture MenuTexture(0),Float(LevelTimer Mod 5120)/5120.0, -Float(LevelTimer Mod 1024)/1024.0
		PositionTexture MenuTexture(1),Float(LevelTimer Mod 5120)/5120.0, -Float(LevelTimer Mod 1024)/1024.0
		RotateTexture MenuTexture(0),(Float(LevelTimer Mod 5120)/5120.0)*360

	Else If MenuBkgStyle=1
		;TurnEntity Camera,0,-.005,0
		
		
		If MenuTimer>0
			For i=0 To 3
				TurnEntity MenuEntity(i),0,.005,0
			Next
			PositionEntity MenuEntity(3),-Cos(45.0+MenuTimer*360.0/72000.0)*Sin(45.0)*256.0,-5.3,-Sin(45.0+MenuTimer*360.0/72000.0)*Sin(45.0)*256.0
			
			PositionTexture MenuTexture(0),-Float(MenuTimer Mod 4000)/4000.0, Float(MenuTimer Mod 100000)/100000.0
			PositionTexture MenuTexture(1),Float(MenuTimer Mod 24000)/24000.0, -Float(MenuTimer Mod 6000)/6000.0
			PositionTexture MenuTexture(2),Float(MenuTimer Mod 12000)/12000.0, -Float(MenuTimer Mod 3000)/3000.0
			
			
			PositionEntity MenuEntity(2),0,-5+.15*Sin(Float(MenuTimer Mod 720)/2.0),0
	
		;	If MenuTimer<2560
		;		LightColor LevelLight,MenuTimer /10,MenuTimer /20,MenuTimer /20
		;		AmbientLight MenuTimer/10,MenuTimer/20,MenuTimer/20
		;	Else If MenuTimer<5120
		;		LightColor LevelLight,255,MenuTimer /20,MenuTimer /20
		;		AmbientLight 255,MenuTimer /20,MenuTimer /20
		;	Else
		;		LightColor LevelLight,255,255,255
		;		AmbientLight 255,255,255
		;	EndIf


			If CurrentMenu=12
				PositionEntity MenuEntity(4),-128*Sin(MenuTimer *360.0/100000.0),-10.5+40.0,128*Cos(MenuTimer *360.0/100000.0)
				LightColor LevelLight,255,255,255
				AmbientLight 255,255,255
			Else If MenuTimer <6400
				LightColor LevelLight,MenuTimer /25,MenuTimer /50,MenuTimer /50
				AmbientLight MenuTimer /50,MenuTimer /50,MenuTimer /50

				CameraFogColor Camera,128*MenuTimer /12800,175*MenuTimer /12800,255*MenuTimer /12800
				EntityColor MenuEntity(4),255,MenuTimer*200/6400,0
				PositionEntity MenuEntity(4),-128*Sin(MenuTimer *360.0/100000.0),-10.5+MenuTimer *40.0/12800.0,128*Cos(MenuTimer *360.0/100000.0)
		
			Else If MenuTimer <12800
				LightColor LevelLight,255,MenuTimer /50,MenuTimer /50
				AmbientLight MenuTimer /50,MenuTimer /50,MenuTimer /50
		;		CameraFogColor Camera,128*MenuTimer /12800,175*MenuTimer /12800,255*MenuTimer /12800
				PositionEntity MenuEntity(4),-128*Sin(MenuTimer *360.0/100000.0),-10.5+MenuTimer *40.0/12800.0,128*Cos(MenuTimer *360.0/100000.0)
			Else
		;		LightColor LevelLight,255,255,255
				PositionEntity MenuEntity(4),-128*Sin(MenuTimer *360.0/100000.0),-10.5+40.0,128*Cos(MenuTimer *360.0/100000.0)
				LightColor LevelLight,255,255,255
				AmbientLight 255,255,255
 
			
			EndIf
			
			
			For i=0 To 49
				PositionEntity MenuEntity(i+10),40.0*Sin((((MenuTimer-125.0*i)/50.0)) Mod 360)+13*Sin(35*i),15-5*Cos((((MenuTimer-125.0*i)/20.0)) Mod 360)-2*Cos(47*i),-80*Cos((((MenuTimer-125.0*i)/50.0)) Mod 360)+17*Sin(51*i)
				

				RotateSprite MenuEntity(i+10),menutimer + i*47
			Next

		EndIf
	
	EndIf
	
	

End Function

Function EndBkgUpdate()

	;If LevelTimer>=1440 Leveltimer=0
	
	TurnEntity MenuEntity(60),0,0.1,0
	PositionTexture Rainbowtexture,Float(LevelTimer)/1000.0,Float(LevelTimer)/2000.0
	
	; star movements (in steps of Leveltimer Mod 720)
	Select EndBkgStyle
	Case 0
		
		For i=0 To 49
			PositionEntity MenuEntity(i+10),(3+Sin(Leveltimer))*Cos(i*36),(3+Sin(Leveltimer))*Sin(i*36),i*5-((LevelTimer-i*5) Mod 250)
			RotateSprite MenuEntity(i+10),leveltimer*3+i*47

		Next
			
	
	
	
	
	End Select
	
	t2=leveltimer Mod 7800
	
	If t2=5;stinky
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(PlayerMesh(1))
	 	EntityTexture MenuEntity(61),PlayerTexture(1)
		PositionEntity MenuEntity(61),0,0,100
		TurnEntity MenuEntity(61),20,180,0
		AnimateMD2 MenuEntity(61),1,0.1,44,52
		
	EndIf
	If t2=605;loof
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(PlayerMesh(2))
	 	EntityTexture MenuEntity(61),PlayerTexture(2)
		;PositionMesh MenuEntity(61),0,-0.5,0
		PositionEntity MenuEntity(61),0,0,100
		TurnEntity MenuEntity(61),0,180,0
		AnimateMD2 MenuEntity(61),2,.1,38,44
		
	EndIf
	
	If t2=1205;scouge 
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(CannonMesh)
	 	;EntityTexture MenuEntity(61),LoofTexture
		PositionEntity MenuEntity(61),0,0,100
		TurnEntity MenuEntity(61),0,0,180
		;AnimateMD2 MenuEntity(61),1,0.5,40,43
		
	EndIf

	
	If t2=1805;coily
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(MonsterMesh(0))
	 	;EntityTexture MenuEntity(61),LoofTexture
		PositionEntity MenuEntity(61),0,0,100
		;TurnEntity MenuEntity(61),-20,180,0
		;AnimateMD2 MenuEntity(61),1,0.5,40,43
		
	EndIf
	
	If t2=2405;kaboom
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(MonsterMesh(3))
	 	;EntityTexture MenuEntity(61),LoofTexture
		PositionEntity MenuEntity(61),0,0,100
		;TurnEntity MenuEntity(61),-20,180,0
		;AnimateMD2 MenuEntity(61),1,0.5,40,43
		
	EndIf
	
	If t2=3005;z-bot
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(MonsterMesh(1))
	 	;EntityTexture MenuEntity(61),LoofTexture
		PositionEntity MenuEntity(61),0,0,100
		TurnEntity MenuEntity(61),-90,0,0
		;AnimateMD2 MenuEntity(61),1,0.5,40,43
		
	EndIf
	
	If t2=3605;saucer
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(MonsterMesh(5))
	 	;EntityTexture MenuEntity(61),LoofTexture
		PositionEntity MenuEntity(61),0,0,100
		TurnEntity MenuEntity(61),90,0,0
		;AnimateMD2 MenuEntity(61),1,0.5,40,43
		
	EndIf
	
	If t2=4205;
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(MonsterMesh(7))
	 	;EntityTexture MenuEntity(61),LoofTexture
		PositionEntity MenuEntity(61),0,0,100
		TurnEntity MenuEntity(61),10,180,0
		AnimateMD2 MenuEntity(61),2,.3,0,6

		
	EndIf
	
	If t2=4805
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(MonsterMesh(7))
	 	EntityTexture MenuEntity(61),MonsterTexture(8)
		PositionEntity MenuEntity(61),0,0,100
		TurnEntity MenuEntity(61),10,180,0
		AnimateMD2 MenuEntity(61),2,.3,0,6

		
	EndIf
	
	If t2=5405
		If MenuEntity(61)<>0 FreeEntity MenuEntity(61)
		MenuEntity(61)=CopyEntity(MonsterMesh(9))
	 	;EntityTexture MenuEntity(61),LoofTexture
		PositionEntity MenuEntity(61),0,0,100
		;TurnEntity MenuEntity(61),90,0,0
		;AnimateMD2 MenuEntity(61),1,0.5,40,43
		
	EndIf








	
	
	t=leveltimer Mod 600
	
	If t2>610 And t2<1200
		TurnEntity menuentity(61),0,0,-2
	EndIf
	If t2>1810 And t2<2400
		TurnEntity menuentity(61),1,2,-2
	EndIf
	If t2>3010 And t2<3600
		TurnEntity menuentity(61),0,0,-3
	EndIf
	If t2>3610 And t2<4200
		TurnEntity menuentity(61),0,-3,0
	EndIf
	If t2>5410 And t2<6000
		TurnEntity menuentity(61),2,-3,1
	EndIf




	
	If t>=10	And t2<6000	
		PositionEntity MenuEntity(61),0,-.6,98.0-Float(t)/6.0
		If t=10
			EntityAlpha MenuEntity(61),1
		EndIf
		If t>550
			EntityAlpha MenuEntity(61),(600.0-Float(t))/50.0
		EndIf
	EndIf

		
	

End Function

Function EndBkgUpdateDeluxe()

	;If LevelTimer>=1440 Leveltimer=0
	
	TurnEntity MenuEntity(60),0,0.1,0
	PositionTexture Rainbowtexture,Float(LevelTimer)/1000.0,Float(LevelTimer)/2000.0
	
	; star movements (in steps of Leveltimer Mod 720)
	Select EndBkgStyle
	Case 0
		
		For i=0 To 49
			If i>0 
				ScaleSprite MenuEntity(i+10),.4,.4
			Else 
				ScaleSprite MenuEntity(i+10),1,1
			EndIf
			PositionEntity MenuEntity(i+10),24*Cos(Float(LevelTimer-i*16.0)/16.0),(8*Sin(Float(LevelTimer-i*16.0)/16.0)+Float(i)/40.0*Sin(i*57+i*i*15)),25+Float(i)/100.0;  (20*Sin(leveltimer+Float(i)*3.6))*Cos((Float(Leveltimer)+Float(i)*4.0*7.2)/4.0),0.4,(20+12*Sin(leveltimer+Float(i)*3.6))*Sin((Float(Leveltimer)+Float(i)*7.2*4.0)/4.0)
			RotateSprite MenuEntity(i+10),leveltimer+i*47

		Next
		
			
	
	
	
	
	End Select
	
	If LevelTimer=0
		; create 4 chars, planet, sky
		For i=61 To 66
			If MenuEntity(i)<>0 FreeEntity MenuEntity(i)
			MenuEntity(i)=0
		Next
		For i=1 To 4
			MenuEntity(60+i)=CopyEntity(PlayerMesh(i))
		 	EntityTexture MenuEntity(60+i),PlayerTexture(i)
			PositionEntity MenuEntity(60+i),-3.75+i*1.5,0,0
			TurnEntity MenuEntity(60+i),20,180+25-i*10,0
			AnimateMD2 MenuEntity(60+i),1,Rnd(0.04,0.10),44,52
		Next
		MenuEntity(65)=CreatePlane()
		BackGroundTexture=MyLoadTexture ("graphics\greenfloor1.jpg",1,False)
		EntityTexture MenuEntity(65),BackGroundTexture
		ScaleTexture Backgroundtexture,3,3
		TranslateEntity MenuEntity(65),0,0,0
		
		MenuEntity(66)=CreatePlane()
		BackGroundTexture2=MyLoadTexture ("graphics\stars.jpg",1,False)
		
		ScaleTexture BackGroundTexture2,10,10
		
		EntityTexture MenuEntity(66),BackGroundTexture2
		RotateEntity MenuEntity(66),-90,0,0
		TranslateEntity MenuEntity(66),0,0,30
		
		

		
	EndIf
	
	PositionTexture BackGroundTexture2,Float(LevelTimer Mod 10000)/10000.0,Float(LevelTimer Mod 20000)/20000.0
		
	
	;TranslateEntity Camera,0,.001,-.01
	



	

End Function

Function LoadOptions()
	File=ReadFile("options.cfg")
	
	If File=0
		OptionsSoundLevel=1
		OptionsMusicLevel=1
		DetailLevel=3
		MyGfxMode=1
		MyColMode=1
		MyWindowed=0
		Language=0
		NoTimeLimit=0
		LockWorlds=0
		OptionsTeleport=0
		Return
	EndIf
	
	;*********************** LATER:

	; Need error handling if file dne
	
	;*****************************

	;ex$=ReadString$(File)
	OptionsSoundLevel2=ReadInt(File)
	OptionsSoundLevel=Float(OptionsSoundLevel2)/100.0
	OptionsMusicLevel2=ReadInt(File)
	OptionsMusicLevel=Float(OptionsMusicLevel2)/100.0
	DetailLevel= ReadInt(File)
	MyGfxMode=ReadInt(File)
	MyColMode=ReadInt(File)
	MyWindowed=ReadInt(File)
	Language=ReadInt(File)
	NoTimeLimit=ReadInt(File)
	LockWorlds=ReadInt(File)
	MyColModeEd=ReadInt(File)
	MyWindowedEd=ReadInt(File)
	OptionsTeleport=ReadInt(File)
	

	CloseFile(File)
	
	
	
	
	
		
	
End Function


Function SaveOptions()
	; saves teh current options
	
	File=WriteFile("options.cfg")
	
	If File=0
		Return
	EndIf
	
	
	;*********************** LATER:

	; Need error handling if file dne
	
	;*****************************
	
	j=0
	j2=0
	
	
	
	;WriteString(File,"Wonderland Options File")	
	WriteInt File,Int(OptionsSoundLevel*100)
	WriteInt File,Int(OptionsMusicLevel*100)
	WriteInt File,DetailLevel
	WriteInt File,MyGfxMode
	WriteInt File,MyColMode
	WriteInt File,MyWindowed
	WriteInt File,Language
	WriteInt File,NoTimeLimit
	WriteInt File,LockWorlds
	WriteInt File,MyColModeEd
	WriteInt File,MyWindowedEd
	WriteInt File,OptionsTeleport
	

	
	CloseFile(File)
	
	
	
	
	
End Function

Function SaveGameState()
	
	file=WriteFile("savestate.wld")
	If file=0 Return
	
	WriteInt File,MapWidth
	WriteInt File,MapHeight
	For j=0 To MapHeight-1
		For i=0 To MapWidth-1
			WriteInt File,MapTile(i,j)
		Next
	Next
	WriteInt File,Leveltimer
	WriteInt File,CurrentLevelScore
	WriteInt File,LevelCompleted
	WriteInt File,LevelCompletedTimer
	WriteInt File,BothCompleted
	WriteInt File,MapTime
	WriteInt File,MapMilliTime
	WriteInt File,FocusSwitchTimer
	;WriteInt File,
	;WriteInt File,
	;WriteInt File,
	
	;camera
	WriteFloat File,CameraDX#
	WriteFloat File,CameraDY#	
	WriteFloat File,CameraDZ#
	WriteFloat File,CameraDStep#
	WriteFloat File,FreeCameraX#
	WriteFloat File,FreeCameraZ#
	WriteFloat File,EntityX(Camera,True)
	WriteFloat File,EntityY(Camera,True)
	WriteFloat File,EntityZ(Camera,True)

	
	
	;player
	For k=1 To 4
		WriteInt File,PlayerExists(k)
		WriteInt File,FocusOnPlayer(k)
	Next

	
	
	CloseFile file
	SaveGameStateActive=True

End Function
Function LoadGameState()

	file=ReadFile("savestate.wld")
	If file=0 Return
	

	MapWidth=ReadInt(File)
	MapHeight=ReadInt(File)
	For j=0 To MapHeight-1
		For i=0 To MapWidth-1
			MapTile(i,j)=ReadInt(File)
		Next
	Next
	Leveltimer=ReadInt(File)
	CurrentLevelScore=ReadInt(File)
	LevelCompleted=ReadInt(File)
	LevelCompletedTimer=ReadInt(File)
	BothCompleted=ReadInt(File)
	MapTime=ReadInt(File)
	MapMilliTime=ReadInt(File)
	FocusSwitchTimer=ReadInt(File)
	;=ReadInt(File)
	;=ReadInt(File)
	;=ReadInt(File)
	
	;camera
	CameraDX#=ReadFloat(File)
	CameraDY#=ReadFloat(File)	
	CameraDZ#=ReadFloat(File)
	CameraDStep#=ReadFloat(File)
	FreeCameraX#=ReadFloat(File)
	FreeCameraZ#=ReadFloat(File)
	PositionEntity Camera,ReadFloat(File),ReadFloat(File),ReadFloat(File);,True
	
	
	;player 
	For k=1 To 4 
		PlayerExists(k)=ReadInt(File)
		FocusOnPlayer(k)=ReadInt(File)
	Next
	
	



	
	CloseFile file


End Function