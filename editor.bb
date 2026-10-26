; Editor
AppTitle "Editor"

packcleanup=True
Global NofPackFiles
Dim PackFilename$(1000),PackFilePosition(1000),PackFileLength(1000)
Include "pack.bb"




Global ExitToGame=False

InitPackFile()

File=ReadFile("options.cfg")
	
If File=0
	MyWindowedEd=0
	MyColModeEd=1
	OptionsTeleport=0
Else
	OptionsSoundLevel2=ReadInt(File)
	Print optionssoundlevel2
	;OptionsSoundLevel=Float(OptionsSoundLevel2)/100.0
	OptionsMusicLevel2=ReadInt(File)
	Print optionsmusiclevel2
	;OptionsMusicLevel=Float(OptionsMusicLevel2)/100.0
	DetailLevel= ReadInt(File)
	Print detailevel
	MyGfxMode=ReadInt(File)
	Print mygfxmode
	MyColMode=ReadInt(File)
	Print mycolmode
	MyWindowed=ReadInt(File)
	Print mywindowed
	Language=ReadInt(File)
	Print language
	NoTimeLimit=ReadInt(File)
	Print notimelimit
	LockWorlds=ReadInt(File)

	MyColModeEd=ReadInt(File)
	Print mycolmodeed
	MyWindowedEd=ReadInt(File)
	Print mywindoweded
	OptionsTeleport=ReadInt(File)
	
	CloseFile(File)
EndIf



Graphics 800,600,8+8*MyColModeEd,2*MyWindowedEd



Global EditorMode=0	; 0-tiles, 1-objects

Global TileSize=32
Global WinStartX=0
Global WinStartY=0
Global WinSize=14

Global MapName$="UNTITLED"
Global MapDescription$="Untitled Level"
Global MapID


Global MapTime=300
Dim FileDir$(4000)
Dim FileDir2$(4000)
Global FileDirN
Global FileDirPos

Dim SignText$(20) ; the text for each sign
Dim SignLine$(5) ; used internally - the 5 lines for a particular sign when edited
Dim Textline$(5)

Global MapWidth=WinSize
Global MapHeight=WinSize
Global MapWidthBackup,MapHeightBackup
Dim MapTile(100,100); maximum 100*100 tiles in level
Dim MapObject(100,100)
Dim MapTileBackup(100,100)
Dim MapObjectBackup(100,100)
For i=0 To 99
	For j=0 To 99
		MapTile(i,j)=200
		MapObject(100,100)=0
	Next 
Next	
For i=1 To WinSize-2
	For j=1 To WinSize-2
		MapTile(i,j)=100
	Next 
Next

Global CurrentTile=200
Global MaxTiles=2500
Global StartTile=0

Global CurrentObject=1
Global MaxObjects=90

Dim version2objects(100)

For i=0 To 50
	Read version2objects(i)
Next
Data 0,1,2,7,8,4,3,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29
Data 5,6,41,47,42,43,44,30,31,32,33,34,35,36,37,38,39,40,9,10,11,12,45,46,48,49,50

Dim version5objects(100)

For i=0 To 50
	Read version5objects(i)
Next
Data 0,1,2,5,6,7,8,9,10,11,12,13,14,17,18,19,20,21,22,23,24,25,27,28,29,30,31,32,33,34,35,36
Data 37,38,39,40,41,42,43,44,45,47, 50,51,52, 66,70, 84,85,86,87





Dim TileCategory$(MaxTiles/100)
TileCategory$(0)="Empty"
TileCategory$(1)="Floors"
TileCategory$(2)="Walls"
TileCategory$(3)="Water"
TileCategory$(4)="Ice"
TileCategory$(5)="Conveyor"
TileCategory$(6)="Bridges"
TileCategory$(7)="Electro"
TileCategory$(8)="Cannons"
TileCategory$(9)="Lava"
TileCategory$(10)="Gates"
TileCategory$(11)="Buttons"
TileCategory$(12)="Teleporters"
TileCategory$(13)="Signs"
TileCategory$(14)="Spikes"
TileCategory$(15)="Fake Walls"
TileCategory$(16)="Box Factory"
TileCategory$(17)="Transporters"
TileCategory$(18)="Trampolines"
TileCategory$(19)="Push Cannons"
TileCategory$(20)="Sticky Cubes"
TileCategory$(21)="Link Spheres"
TileCategory$(22)="Warp Gates"
TileCategory$(23)="3D View"
TileCategory$(24)="ShadowStinks"

Dim StyleName$(10)
Global MaxMapStyles=10
Global MapStyle=2
StyleName$(0)="Cave"
StyleName$(1)="Sand"
StyleName$(2)="Wood"
StyleName$(3)="Purple"
StyleName$(4)="Castle"
StyleName$(5)="Jade"
StyleName$(6)="Spooky"
StyleName$(7)="Garden"
StyleName$(8)="Aztec"
StyleName$(9)="Custom"

Dim BackGroundName$(10)
Global MapBackground=0
Global MaxMapBackgrounds=11
BackGroundName$(0)="Sky"
BackGroundName$(1)="Forest"
BackGroundName$(2)="Walls"
BackGroundName$(3)="Stars"
BackGroundName$(4)="Flat"
BackGroundName$(5)="Water"
BackGroundName$(6)="Lava"
BackGroundName$(7)="Warp"
BackGroundName$(8)="City"
BackgroundName$(9)="Rainbow"
BackgroundName$(10)="Custom"


Dim MusicName$(10)
Global PackLevelNumber, LevelPackFile, CurrentLevelInPack
Global PackName$,PackDescription$, PackInfo$
Global PackMusic=0
Global MaxPackMusic=5
MusicName$(0)="Track 1: Welcome To Wonderland"
MusicName$(1)="Track 2: Puzzling Breeze"
MusicName$(2)="Track 3: Waterwaves"
MusicName$(3)="Track 4: Dark Caverns"
MusicName$(4)="Track 5: Bittersweet Endings"
Global MusicTrack=1


; Level-Textures
; --------------

Dim TileCategoryTexture(MaxTiles/100)
Dim DoesTileExist(MaxTiles)
For i=0 To MaxTiles-1
	DoesTileExist(i)=False
Next




SetBuffer BackBuffer()
;HidePointer

; Load the Tile-Graphics
TileCategoryTexture(0)=LoadAnimImage (pack("graphicseditor\ed-empty.bmp"),32,32,0,1)
DoesTileExist(0)=True

n=5
TileCategoryTexture(1)=LoadAnimImage (pack("graphicseditor\ed-floorv5.bmp"),32,32,0,n)
For i=1 To n
	DoesTileExist(100+i-1)=True
Next


TileCategoryTexture(2)=LoadAnimImage (pack("graphicseditor\ed-wallv5.bmp"),32,32,0,4)
DoesTileExist(200)=True
DoesTileExist(201)=True
DoesTileExist(202)=True
DoesTileExist(203)=True

TileCategoryTexture(3)=LoadAnimImage (pack("graphicseditor\ed-water.bmp"),32,32,0,1)
DoesTileExist(300)=True

TileCategoryTexture(4)=LoadAnimImage (pack("graphicseditor\ed-ice.bmp"),32,32,0,5)
DoesTileExist(400)=True
DoesTileExist(401)=True
DoesTileExist(402)=True
DoesTileExist(403)=True
DoesTileExist(404)=True


TileCategoryTexture(5)=LoadAnimImage (pack("graphicseditor\ed-conveyorv5.bmp"),32,32,0,16)
For i=500 To 515
	DoesTileExist(i)=True
Next


TileCategoryTexture(6)=LoadAnimImage (pack("graphicseditor\ed-breakawayv5.bmp"),32,32,0,9)
DoesTileExist(600)=True
DoesTileExist(601)=True
DoesTileExist(602)=True
DoesTileExist(603)=True
DoesTileExist(604)=True
DoesTileExist(605)=True
DoesTileExist(606)=True
DoesTileExist(607)=True
DoesTileExist(608)=True


TileCategoryTexture(7)=LoadAnimImage (pack("graphicseditor\ed-electro.bmp"),32,32,0,4)
DoesTileExist(700)=True
DoesTileExist(701)=True
DoesTileExist(702)=True
DoesTileExist(703)=True

TileCategoryTexture(8)=LoadAnimImage (pack("graphicseditor\ed-cannon.bmp"),32,32,0,16)
For i=800 To 815
	DoesTileExist(i)=True
Next

TileCategoryTexture(9)=LoadAnimImage (pack("graphicseditor\ed-lava.bmp"),32,32,0,1)
DoesTileExist(900)=True

TileCategoryTexture(10)=LoadAnimImage (pack("graphicseditor\ed-gatev5.bmp"),32,32,0,16)
For i=1000 To 1015
	DoesTileExist(i)=True
Next

TileCategoryTexture(11)=LoadAnimImage (pack("graphicseditor\ed-buttonsv5.bmp"),32,32,0,27)
For i=1100 To 1126
	DoesTileExist(i)=True
Next

If OptionsTeleport=0
	TileCategoryTexture(12)=LoadAnimImage (pack("graphicseditor\ed-teleportv5.bmp"),32,32,0,8)
Else
	TileCategoryTexture(12)=LoadAnimImage ("res13.dat",32,32,0,8)
EndIf
For i=1200 To 1207
	DoesTileExist(i)=True
Next

TileCategoryTexture(13)=LoadAnimImage (pack("graphicseditor\ed-signsv5.bmp"),32,32,0,20)
For i=1300 To 1319
	DoesTileExist(i)=True
Next
TileCategoryTexture(14)=LoadAnimImage (pack("graphicseditor\ed-spikes.bmp"),32,32,0,4)
For i=1400 To 1403
	DoesTileExist(i)=True
Next
TileCategoryTexture(15)=LoadAnimImage (pack("graphicseditor\ed-fakev5.bmp"),32,32,0,3)
DoesTileExist(1500)=True
DoesTileExist(1501)=True
DoesTileExist(1502)=True

TileCategoryTexture(16)=LoadAnimImage ("res63.dat",32,32,0,13)
For i=1600 To 1612
	DoesTileExist(i)=True
Next

TileCategoryTexture(17)=LoadAnimImage ("res68.dat",32,32,0,3)
For i=1700 To 1702
	DoesTileExist(i)=True
Next

TileCategoryTexture(18)=LoadAnimImage ("res67.dat",32,32,0,1)
DoesTileExist(1800)=True

TileCategoryTexture(19)=LoadAnimImage ("res64.dat",32,32,0,16)
For i=1900 To 1915
	DoesTileExist(i)=True
Next


TileCategoryTexture(20)=LoadAnimImage ("res66.dat",32,32,0,1)
For i=2000 To 2000
	DoesTileExist(i)=True
Next


TileCategoryTexture(21)=LoadAnimImage ("res65.dat",32,32,0,4)
For i=2100 To 2103
	DoesTileExist(i)=True
Next

TileCategoryTexture(22)=LoadAnimImage ("res62.dat",32,32,0,12)
For i=2200 To 2211
	DoesTileExist(i)=True
Next

TileCategoryTexture(23)=LoadAnimImage ("res61.dat",32,32,0,1)
For i=2300 To 2300
	DoesTileExist(i)=True
Next

TileCategoryTexture(24)=LoadAnimImage ("res60.dat",32,32,0,8)
For i=2400 To 2407
	DoesTileExist(i)=True
Next










HidePointer



; Other Graphics
Global MouseTexture=LoadImage(pack("graphicseditor\mouse.bmp"))
ResizeImage MouseTexture,TileSize,TileSize

Global ArrowTexture=LoadAnimImage(pack("graphicseditor\arrows.bmp"),32,32,0,8)
Global ObjectTexture=LoadAnimImage(pack("graphicseditor\objectsv5b.bmp"),16,16,0,MaxObjects)
MaxObjects=88

	
Global Maske=LoadImage(pack("graphicseditor\maske.bmp"))
Global MenuBkg=LoadImage(pack("graphicseditor\menubkg.bmp"))


; Font

Dim FontTex(5)
FontTex(0)=LoadAnimImage(pack("graphicseditor\fontpiece1.bmp"),16,32,0,16)
FontTex(1)=LoadAnimImage(pack("graphicseditor\fontpiece2.bmp"),16,32,0,16)
FontTex(2)=LoadAnimImage(pack("graphicseditor\fontpiece3.bmp"),16,32,0,16)
FontTex(3)=LoadAnimImage(pack("graphicseditor\fontpiece4.bmp"),16,32,0,16)
FontTex(4)=LoadAnimImage(pack("graphicseditor\fontpiece5.bmp"),16,32,0,16)
FontTex(5)=LoadAnimImage(pack("graphicseditor\fontpiece6.bmp"),16,32,0,16)


	
Global GamePhase=6

Global Cursorblink=0
Global CursorX,CursorY

Global CursorKeyPressed

Global BlockMode=0
Global BlockX,BlockY
	
Global UndoAvailable=0
Global MouseButtonInUse=False	
	
Global Running=True
; create level and level-pack directories
If FileType("customlevels")<>2
	CreateDir "CustomLevels"
EndIf

Global HouseDirTrue=0
Global HouseDir$=""
Global ModelDirTrue=0
Global ModelDir$=""
Global TextureDirTrue=0
Global TextureDir$=""
Global BackgroundDirTrue=0
Global BackgroundDir$=""

	
While Running

  If GetActiveWindow()

	If GamePhase=1
		MainEditorLoop()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				GamePhase=8
			EndIf
		Else
			CursorKeyPressed=False
		EndIf

	EndIf
	If GamePhase=2
		EditFileNameLoop()
	EndIf
	If GamePhase=3
		EditNameLoop()
	EndIf
	If GamePhase=4
		EditSignLoop()
	EndIf
	If GamePhase=5
		LoadLevelLoop()
		If KeyDown(1)=True And CursorKeyPressed=False
			CursorKeyPressed=True
			GamePhase=1
		EndIf

	EndIf
	If GamePhase=6
		MainMenuLoop()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				GamePhase=7
			EndIf
		Else
			CursorKeyPressed=False
		EndIf

	EndIf
	If GamePhase=7
		ExitMenuLoop()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				GamePhase=6
			EndIf
		Else
			CursorKeyPressed=False
		EndIf


	EndIf
	If GamePhase=8
		ExitEditorLoop()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				GamePhase=1
			EndIf
		Else
			CursorKeyPressed=False
		EndIf


	EndIf
	If GamePhase=9
		ExitToGameLoop()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				GamePhase=6
			EndIf
		Else
			CursorKeyPressed=False
		EndIf


	EndIf
	If GamePhase=10
		WipeLoop()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				GamePhase=1
			EndIf
		Else
			CursorKeyPressed=False
		EndIf


	EndIf
	
	If GamePhase=11
		EditPackFileNameLoop()
	EndIf
	If GamePhase=12
		EditPackNameLoop()
	EndIf
	If GamePhase=13
		PackMusicSelect()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				GamePhase=6
			EndIf
		Else
			CursorKeyPressed=False
		EndIf

	EndIf
	If GamePhase=14
		PackLevelNumberSelect()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				GamePhase=6
			EndIf
		Else
			CursorKeyPressed=False
		EndIf

	EndIf
	If GamePhase=15
		PackLevelSelect()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				CloseFile levelpackfile
				DeleteFile "levelpacks/"+packname$+".lpk"
				gamephase=6
			EndIf
		Else
			CursorKeyPressed=False
		EndIf

	EndIf
	If GamePhase=16
		PackCompletedLoop()
		If KeyDown(1)=True 
			If CursorKeyPressed=False
				CursorKeyPressed=True
				CloseFile levelpackfile
				DeleteFile "levelpacks/"+packname$+".lpk"
				gamephase=6
			EndIf
		Else
			CursorKeyPressed=False
		EndIf

	EndIf
	If GamePhase=17
		EditPackInfoLoop()
	EndIf
	If GamePhase=18
		ManualMenuLoop()
	EndIf
	
	If GamePhase=19
		EditHouseDirLoop()
	EndIf
	If GamePhase=20
		EditModelDirLoop()
	EndIf
	If GamePhase=21
		EditTextureDirLoop()
	EndIf
	If GamePhase=22
		EditBackgroundDirLoop()
	EndIf







  Else

	Delay 100
  EndIf








	

Wend

dir=ReadDir(CurrentDir$())
Repeat
	a$=NextFile$(dir)
	
	If Lower$(Right$(a$,4))=".tmp" Or Lower$(Right$(a$,4))=".3ds" Or Lower$(Right$(a$,2))=".x"
		
		DeleteFile a$
	EndIf
	
Until a$=""
CloseDir dir



; and go to Wonderland
If ExitToGame=True Then ExecFile ("wdlgamep.exe")

End

Function MainEditorLoop()

	If (KeyDown(29) Or KeyDown(157)) And KeyDown(64) Then MaxObjects=90
	
	If (KeyDown(42) Or KeyDown(54)) And KeyDown(35)
		Repeat
		Until GetKey()=0 And KeyDown(42)=False And KeyDown(54)=False And KeyDown(35)=False
		GamePhase=19
		CursorBlink=0
		CursorX=0
		CursorY=0
		Delay 200
		Return
	EndIf
	
	
	If (KeyDown(42) Or KeyDown(54)) And KeyDown(50)
		Repeat
		Until GetKey()=0 And KeyDown(42)=False And KeyDown(54)=False And KeyDown(50)=False
		GamePhase=20
		CursorBlink=0
		CursorX=0
		CursorY=0
		Delay 200
		Return
	EndIf
	If (KeyDown(42) Or KeyDown(54)) And KeyDown(20)
		Repeat
		Until GetKey()=0 And KeyDown(42)=False And KeyDown(54)=False And KeyDown(20)=False
		GamePhase=21
		CursorBlink=0
		CursorX=0
		CursorY=0
		Delay 200
		Return
	EndIf

	If (KeyDown(42) Or KeyDown(54)) And KeyDown(34)
		Repeat
		Until GetKey()=0 And KeyDown(42)=False And KeyDown(54)=False And KeyDown(34)=False
		GamePhase=22
		CursorBlink=0
		CursorX=0
		CursorY=0
		Delay 200
		Return
	EndIf




	; maske
	Cls
	DrawImage Maske,0,0
	; board
	For i=0 To WinSize-1
		For j=0 To WinSize-1
			DrawImage TileCategoryTexture(MapTile(WinStartX+i,WinStartY+j)/100),i*TileSize+48,j*TileSize+48,(MapTile(WinStartX+i,WinStartY+j) Mod 100)
			If MapObject(WinStartX+i,WinStartY+j)<>0
				DrawImage ObjectTexture,i*32+48+8,j*32+48+8,MapObject(WinStartX+i,WinStartY+j)
			EndIf
		Next
	Next
	; dividor lines
;	For i=1 To WinSize-1
;		Color 0,0,0
;		Line 16+i*32,16,16+i*32,16+(WinSize)*32
;		Line 16,16+i*32,16+(WinSize)*32,16+i*32
;	Next

	; arrows around board
	For i=0 To WinSize-1
		; up?
		If WinStartY>0
			DrawImage ArrowTexture,i*32+48,16,0
		Else
			DrawImage ArrowTexture,i*32+48,16,4
		EndIf
		; left?
		If WinStartX>0
			DrawImage ArrowTexture,16,i*32+48,3
		Else
			DrawImage ArrowTexture,16,i*32+48,7
		EndIf
		; down?
		If WinStartY<MapHeight-WinSize
			DrawImage ArrowTexture,i*32+48,16+(WinSize+1)*32,2
		Else
			DrawImage ArrowTexture,i*32+48,16+(WinSize+1)*32,6
		EndIf
		; right?
		If WinStartX<MapWidth-WinSize
			DrawImage ArrowTexture,16+(WinSize+1)*32,i*32+48,1
		Else
			DrawImage ArrowTexture,16+(WinSize+1)*32,i*32+48,5
		EndIf
	Next 
					
	
	; Name
	If Len(MapDescription$)<=15
		MyText(Mapdescription$,544+4+(15-Len(Mapdescription$))*8,12)
	Else
		mytext(Left$(mapdescription$,12)+"...",544+4,12)
	EndIf
;	MyText (Mapname$+".LEV",544+8+(15-Len(Mapname$)-4)*8,12)
	; Size
	MySize$=Str$(Mapwidth)+"  X  "+Str$(MapHeight)
	MyText (Mysize$,544+4+(15-Len(MySize$))*8,48)
	;If Mapwidth>14
		DrawImage ArrowTexture,544+20,44,3
	;Else
	;	DrawImage ArrowTexture,544+20,44,7
	;EndIf
	;If Mapwidth<99
		DrawImage ArrowTexture,544+20+4*16,44,1
	;Else
	;	DrawImage ArrowTexture,544+20+4*16,44,5
	;EndIf
	
	;If Mapheight>14
		DrawImage ArrowTexture,544+20+7*16,44,3
	;Else
	;	DrawImage ArrowTexture,544+20+7*16,44,7
	;EndIf
	;If Mapheight<99
		DrawImage ArrowTexture,544+20+11*16,44,1
	;Else
	;	DrawImage ArrowTexture,544+20+11*16,44,5
	;EndIf


	
	; Time
	If MapTime>10
		DrawImage ArrowTexture,544,88,3
	Else
		DrawImage ArrowTexture,544,88,7
	EndIf
	If MapTime<900
		DrawImage ArrowTexture,544+216,88,1
	Else
		DrawImage ArrowTexture,544+216,88,5
	EndIf

	MySize$="Time: "+Str$(MapTime)
	MyText (Mysize$,544+4+(15-Len(MySize$))*8,92)

	
	; Style
	MySize$="Style: "+Stylename$(MapStyle)
	MyText (Mysize$,544+4+(15-Len(MySize$))*8,124)
	MySize$="Bkg: "+BackgroundName$(MapBackground)
	MyText (Mysize$,544+4+(15-Len(MySize$))*8,156)
	
	
	
		

	
	; Category Button
	DrawImage ArrowTexture,544,196,3
	DrawImage ArrowTexture,544+216,196,1
	MySize$=TileCategory$(CurrentTile/100)
	MyText (Mysize$,544+4+(15-Len(MySize$))*8,200)
	
	; Category Tiles
	Tile=(CurrentTile/100)*100+StartTile*5
	i=0
	j=0
	While DoesTileExist(Tile)=True
		DrawImage TileCategoryTexture(Tile/100),544+12+i*TileSize,228+j*TileSize,Tile Mod 100
		; and Rectangle around it if it's the currenttile
		If Tile=CurrentTile And EditorMode=0
			Color 255,255,0
			Rect 544+12+i*TileSize,228+j*TileSize,TileSize,TileSize,0
			Color 0,0,0
			Rect 544+12+i*TileSize+1,228+j*TileSize+1,TileSize-2,TileSize-2,0
		EndIf
		i=i+1
		If i=7
			i=0
			j=j+1
		EndIf
		If j=4 Then Exit 
		Tile=Tile+1
	Wend
	; Category Tiles Arrows
	;For i=0 To 6
	;	If StartTile=0 
	;		DrawImage ArrowTexture,544+12+i*32,228,4
	;	Else
	;		DrawImage ArrowTexture,544+12+i*32,228,0
	;	EndIf
	;
	;	If DoesTileExist((CurrentTile/100)*100+(StartTile+2)*5)=True
	;		DrawImage ArrowTexture,544+12+i*32,228+96,2
	;	Else
	;		DrawImage ArrowTexture,544+12+i*32,228+96,6
	;	EndIf
	;Next
		
		
	
	; Objects
	
	MySize$="Objects"
	MyText (Mysize$,544+4+(15-Len(MySize$))*8,368)

	
	i=0
	j=0
	While i+j*15<MaxObjects
		DrawImage ObjectTexture,544+4+i*16,392+j*16,i+j*15
		; and Rectangle around it if it's the currentobject
		If i+j*15=CurrentObject And EditorMode=1
			Color 255,255,0
			Rect 544+4+i*16,392+j*16,16,16,0
			Color 0,0,0
			Rect 544+4+i*16+1,392+j*16+1,16-2,16-2,0
		EndIf

		i=i+1
		If i=15
			i=0
			j=j+1
		EndIf
	Wend
	
	; Music
	MyText("Music Track: "+Str$(musictrack),544+4+8,506)
	
	; Load/Save/Exit Buttons
	
	Color 255,255,0
	
	Mytext("Load",512,558)
	Mytext("Save",512+15*8,558)
	Mytext("Blck",512-15*8*4,558)
	Mytext("Wipe",512-15*8*3,558)
	Mytext("Brdr",512-15*8*2,558)
	If UndoAvailable=1
		Mytext("Undo",512-15*8*1,558)
	EndIf

	ex$=""
	If housedirtrue=True
		ex$=ex$+"H:"+housedir$+" "
	EndIf
	If modeldirtrue=True
		ex$=ex$+"M:"+modeldir$+" "
	EndIf
	If texturedirtrue=True
		ex$=ex$+"T:"+texturedir$+" "
	EndIf
	If backgrounddirtrue=True
		ex$=ex$+"B:"+backgrounddir$+" "
	EndIf
	Mytext(ex$,0,580) 


	; BlockMode?
	If BlockMode>0
		Color 255,255,0
		Rect 7,548,114,39,0
		If BlockMode=2
			BlockX2=WinStartX+(MouseX()-48)/TileSize
			BlockY2=WinStartY+(MouseY()-48)/TileSize
			
			If BlockX<BlockX2
				BstartX=BlockX
				BendX=BlockX2
			Else
				BstartX=BlockX2
				BendX=BlockX
			EndIf
			If BlockY<BlockY2
				BstartY=BlockY
				BendY=BlockY2
			Else
				BstartY=BlockY2
				BendY=BlockY
			EndIf
			If BStartX-WinStartX<0 Then BStartX=WinStartX
			If BStartY-WinStartY<0 Then BstartY=WinStartY
			If BendX-WinStartX>13 Then BendX=winstartx+13
			If Bendy-winstarty>13 Then bendy=winstarty+13
			
			For i=BStartX To BEndX
				For j=BStartY To BEndY
					DrawImage TileCategoryTexture(CurrentTile/100),(i-WinStartX)*32+48,(j-WinStartY)*32+48,CurrentTile Mod 100
				Next
			Next
		EndIf

;		If MouseX()>7 And MouseX()<121 And MouseY()>548 And MouseY()<587 
	EndIf

		
	

	; show mouse cursor
	Color 100,100,100
	;Text 0,0,Str$(MouseX())+" "+Str$(MouseY())+" "+Str$(CurrentTile)
	DrawImage MouseTexture,MouseX(),MouseY()
	Flip
	
	; Keyboard shortcuts
		If KeyDown(22)=True And undoavailable=1
			undoavailable=0
			MapWidth=MapWidthBackup
			MapHeight=MapHeightBackup
			For i=0 To MapWidth-1
				For j=0 To MapHeight-1
					MapTile(i,j)=MapTileBackup(i,j)
					MapObject(i,j)=MapObjectBackup(i,j)
				Next
			Next

					
		
			Repeat
			Until KeyDown(22)=False
		EndIf
		If KeyDown(48)
			If BlockMode>0 
				BlockMode=0
			Else
				BlockMode=1
			EndIf
		
			Repeat
			Until KeyDown(48)=False
		EndIf

	
	; now do mouse input - left mouse button
	If MouseDown(1)=True
		; clicked inside the window?
		If MouseX()>=48 And MouseY()>=48 And MouseX()<48+WinSize*TileSize And MouseY()<48+WinSize*TileSize
			If BlockMode=0
				If EditorMode=0
					BackupLevel()
					MapTile(WinStartX+(MouseX()-48)/TileSize,WinStartY+(MouseY()-48)/TileSize)=CurrentTile
					If CurrentTile/100=13
						;signs: enter sign
						CursorX=0
						CursorBlink=0
						CursorY=0
						Repeat
						Until GetKey()=0
						GamePhase=4
						
	;					Cls
	;					Print "Enter Sign Text (use '#' to separate lines):"
	;					SignText$(CurrentTile-1300)=Input$(":")
					
					EndIf
				Else If EditorMode=1
					BackupLevel()
					MapObject(WinStartX+(MouseX()-48)/TileSize,WinStartY+(MouseY()-48)/TileSize)=CurrentObject
				EndIf
			Else If BlockMode=1
				BlockX=WinStartX+(MouseX()-48)/TileSize
				BlockY=WinStartY+(MouseY()-48)/TileSize
				BlockMode=2
				While MouseDown(1)=True
				Wend
			Else If BlockMode=2
				BackupLevel()
				BlockX2=WinStartX+(MouseX()-48)/TileSize
				BlockY2=WinStartY+(MouseY()-48)/TileSize
				
				If BlockX<BlockX2
					BstartX=BlockX
					BendX=BlockX2
				Else
					BstartX=BlockX2
					BendX=BlockX
				EndIf
				If BlockY<BlockY2
					BstartY=BlockY
					BendY=BlockY2
				Else
					BstartY=BlockY2
					BendY=BlockY
				EndIf
				If BStartX-WinStartX<0 Then BStartX=WinStartX
				If BStartY-WinStartY<0 Then BstartY=WinStartY
				If BendX-WinStartX>13 Then BendX=winstartx+13
				If Bendy-winstarty>13 Then bendy=winstarty+13
				
				For i=BStartX To BEndX
					For j=BStartY To BEndY
						MapTile(i,j)=CurrentTile 
					Next
				Next

				
				BlockMode=1			
				While MouseDown(1)=True
				Wend
			EndIf
		EndIf
		; border scrolling
		If MouseX()>=48 And MouseX()<48+WinSize*32 And MouseY()>16 And MouseY()<48 And WinStartY>0
			WinStartY=WinStartY-1
			Delay(100)
		EndIf
		If MouseY()>=48 And MouseY()<48+WinSize*32 And MouseX()>16 And MouseX()<48  And WinStartX>0
			WinStartX=WinStartX-1
			Delay(100)
		EndIf
		If MouseX()>=48 And MouseX()<48+WinSize*32 And MouseY()>48+WinSize*32 And MouseY()<80+WinSize*32 And WinStartY<MapHeight-WinSize
			WinStartY=WinStartY+1
			Delay(100)
		EndIf
			If MouseY()>=48 And MouseY()<48+WinSize*32 And MouseX()>48+WinSize*32 And MouseX()<80+WinSize*32 And WinStartX<MapWidth-WinSize
			WinStartX=WinStartX+1
			Delay(100)
		EndIf
		
		

		; level name
		If MouseX()>544 And MouseY()>12 And MouseY()<44
			Repeat
			Until GetKey()=0
			GamePhase=3
			CursorBlink=0
			CursorX=0
			CursorY=0
			Delay 200
		EndIf
		
		; map size - increase
		If MouseX()>=564 And MouseY()>=44 And MouseX()<564+32 And MouseY()<44+32 And MapWidth<99
			BackupLevel()
			MapWidth=MapWidth+1
			For i=MapWidth-1 To 1 Step -1
				For j=0 To MapHeight-1
					MapTile(i,j)=MapTile(i-1,j)
					MapObject(i,j)=MapObject(i-1,j)
				Next
			Next
			For i=0 To MapHeight-1
				If i>0 And i<MapHeight-1 Then MapTile(1,i)=100
				MapTile(0,i)=200
			Next 
			Delay(100)
		EndIf
		If MouseX()>=564+112 And MouseY()>=44 And MouseX()<564+112+32 And MouseY()<44+32 And MapHeight<99
			BackupLevel()

			MapHeight=MapHeight+1
			For i=MapHeight-1 To 1 Step -1
				For j=0 To MapWidth-1
					MapTile(j,i)=MapTile(j,i-1)
					MapObject(j,i)=MapObject(j,i-1)
				Next
			Next
			For i=0 To MapWidth-1
				If i>0 And i<MapWidth-1 Then MapTile(i,1)=100
				MapTile(i,0)=200
			Next 

			Delay(100)
		EndIf
		If MouseX()>=564+64 And MouseY()>=44 And MouseX()<564+96 And MouseY()<44+32 And MapWidth<99
			BackupLevel()

			MapWidth=MapWidth+1
			For i=1 To MapHeight-2
				MapTile(MapWidth-2,i)=100
				MapTile(MapWidth-1,i)=200
			Next 
			Delay(100)
		EndIf
		If MouseX()>=564+112+64 And MouseY()>=44 And MouseX()<564+112+96 And MouseY()<44+32 And MapHeight<99
			BackupLevel()

			MapHeight=MapHeight+1
			For i=1 To MapWidth-2
				MapTile(i,MapHeight-2)=100
				
				MapTile(i,MapHeight-1)=200
			Next

			Delay(100)
		EndIf

			
		
		; time
		If MouseX()>544 And MouseX()<544+32 And MouseY()>88 And MouseY()<88+32 And MapTime>10
			MapTime=MapTime-10
			Delay 75
		EndIf
		If MouseX()>760 And MouseX()<760+32 And MouseY()>88 And MouseY()<88+32 And MapTime<900
			MapTime=MapTime+10
			Delay 75
		EndIf
		; Style
		If MouseX()>544 And MouseX()<792 And MouseY()>120 And MouseY()<152
			MapStyle=MapStyle+1
			If MapStyle=MaxMapStyles Then MapStyle=0
			Delay 200
		EndIf
		; background
		If MouseX()>544 And MouseX()<792 And MouseY()>152 And MouseY()<184
			MapBackground=MapBackground+1
			If MapBackground=MaxMapBackgrounds Then MapBackground=0
			Delay 200
		EndIf



			


		; tile category button?
		If MouseX()>544 And MouseX()<544+32 And MouseY()>196 And MouseY()<196+32
			EditorMode=0
			CurrentTile=((CurrentTile/100)-1)*100
			If CurrentTile=-100
				CurrentTile=0
				While DoesTileExist(CurrentTile+100)=True
					CurrentTile=CurrentTile+100
				Wend
			EndIf
			StartTile=0
			While MouseDown(1)=True
			Wend
		EndIf
		If MouseX()>760 And MouseX()<760+32 And MouseY()>196 And MouseY()<196+32
			EditorMode=0
			CurrentTile=((CurrentTile/100)+1)*100
			If DoesTileExist(CurrentTile)=False Then CurrentTile=0
			StartTile=0
			While MouseDown(1)=True
			Wend
		EndIf
		; category tiles?
		If MouseX()>544+12 And MouseX()<780 And MouseY()>=228 And MouseY()<228+4*TileSize
			Tile=(CurrentTile/100)*100+StartTile*7+(MouseX()-544-12)/TileSize+((MouseY()-228)/TileSize)*7
			If DoesTileExist(Tile)=True
				EditorMode=0
				CurrentTile=Tile
			EndIf
		EndIf
		
		
 
		
		; objects?
		If MouseX()>548 And MouseX()<788 And MouseY()>=392 And MouseY()<392+9*16
			Obj=(MouseX()-548)/16+((MouseY()-392)/16)*15
			If Obj<MaxObjects
				EditorMode=1
				BlockMode=0
				CurrentObject=Obj
			EndIf
		EndIf
		
		; music?
		If MouseX()>548 And MouseX()<788 And MouseY()>=504 And MouseY()<534
			musictrack=musictrack+1
			If musictrack=7 Then musictrack=1
			While MouseDown(1)=True
			Wend

		EndIf
		
		; block
		If MouseX()>7 And MouseX()<121 And MouseY()>548 And MouseY()<587 
			While MouseDown(1)=True
			Wend
			If BlockMode>0 
				BlockMode=0
			Else
				BlockMode=1
			EndIf
			
			
		EndIf

			
		; wipe
		If MouseX()>127 And MouseX()<241 And MouseY()>548 And MouseY()<587 
			While MouseDown(1)=True
			Wend

			GamePhase=10
		EndIf
		
		; border
		If MouseX()>247 And MouseX()<361 And MouseY()>548 And MouseY()<587 
			While MouseDown(1)=True
			Wend
			MouseButtonInUse=False
			BackUpLevel()


			For i=0 To MapWidth-1
				MapTile(i,0)=CurrentTile
				MapTile(i,MapHeight-1)=CurrentTile
			Next
			For i=0 To MapHeight-1
				MapTile(0,i)=CurrentTile
				MapTile(MapWidth-1,i)=CurrentTile
			Next
		EndIf
		
		; undo
		If MouseX()>367 And MouseX()<481 And MouseY()>548 And MouseY()<587 And undoavailable=1
		
			While MouseDown(1)=True
			Wend
			
			undoavailable=0
			MapWidth=MapWidthBackup
			MapHeight=MapHeightBackup
			For i=0 To MapWidth-1
				For j=0 To MapHeight-1
					MapTile(i,j)=MapTileBackup(i,j)
					MapObject(i,j)=MapObjectBackup(i,j)
				Next
			Next
			
			
		EndIf



		
		; load
		If MouseX()>489 And MouseX()<601 And MouseY()>548 And MouseY()<587 
			While MouseDown(1)=True
			Wend
			LoadLevel()
		EndIf
		; save
		If MouseX()>608 And MouseX()<720 And MouseY()>548 And MouseY()<587 
			While MouseDown(1)=True
			Wend
			Repeat
			Until GetKey()=0
			GamePhase=2
			If mapname$="UNTITLED" Then mapname$=""
			CursorX=Len(Mapname$)
			CursorBlink=0
			CursorY=0
		EndIf
		
		; exit
		If MouseX()>753 And MouseX()<795 And MouseY()>548 And MouseY()<587 
			While MouseDown(1)=True
			Wend

			GamePhase=8
		EndIf
	EndIf
	
	; now do mouse input - right mouse button
	If MouseDown(2)=True
		
	
		
		
		; map size - reduce
		If MouseX()>=564 And MouseY()>=44 And MouseX()<564+32 And MouseY()<44+32 And MapWidth>14
			BackupLevel()

			MapWidth=MapWidth-1
			For i=0 To MapWidth-1
				For j=0 To MapHeight-1
					MapTile(i,j)=MapTile(i+1,j)
					MapObject(i,j)=MapObject(i+1,j)
				Next
			Next
			For i=0 To MapHeight-1
				MapTile(0,i)=200
			Next
			If Winstartx>Mapwidth-14 Then winstartx=winstartx-1
			Delay(100)
		EndIf
		If MouseX()>=564+112 And MouseY()>=44 And MouseX()<564+112+32 And MouseY()<44+32 And MapHeight>14
			BackupLevel()

			MapHeight=MapHeight-1
			For i=0 To MapHeight-1
				For j=0 To MapWidth-1
					MapTile(j,i)=MapTile(j,i+1)
					MapObject(j,i)=MapObject(j,i+1)
				Next
			Next
			For i=0 To MapWidth-1
				MapTile(i,0)=200
			Next

			
			If Winstarty>Mapheight-14 Then winstarty=winstarty-1

			Delay(100)
		EndIf

		
		
		If MouseX()>=564+64 And MouseY()>=44 And MouseX()<564+96 And MouseY()<44+32 And MapWidth>14
			BackupLevel()


			MapWidth=MapWidth-1
			If WinStartX>MapWidth-WinSize Then WinStartX=WinStartX-1
			For i=0 To MapHeight-1
				MapTile(MapWidth-1,i)=200
			Next 
			Delay(100)
		EndIf
		If MouseX()>=564+112+64 And MouseY()>=44 And MouseX()<564+112+96 And MouseY()<44+32 And MapHeight>14
			BackupLevel()


			MapHeight=MapHeight-1
			If WinStartY>MapHeight-WinSize Then WinStartY=WinStartY-1
			For i=0 To MapWidth-1
				MapTile(i,MapHeight-1)=200
			Next
			Delay(100)
		EndIf
		
			
	
	EndIf
	If MouseDown(1)=False And MouseDown(2)=False
		MouseButtonInUse=False
	EndIf

		

End Function

Function EditFileNameLoop()
	
	
	For i=0 To 4
		Textline$(i)=""
	Next

	
	Cls
	Color 100,100,100
	Rect 0,32,800,30,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,63,800,63
	
	
	
	MyText("Enter Level Filename (do not include '.LV6'):",0,4)
	
	
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,38)
	
	ex$=MapName$
	MyText(ex$,0,36)
	Textline$(0)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36)

	
	;ascii keys
	askey=GetKey()
	If askey>=97 And askey<=122 Then askey=askey-32
	If ((askey>=48 And askey<=57) Or (askey>=65 And askey<=90))And Len(Textline$(0))<15 

		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	

	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			SaveLevel()
			GamePhase=1
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf

	
	mapname$=textline$(0)
	
	DrawImage MouseTexture,MouseX(),MouseY()

	
	Flip

	
	
End Function

Function EditHouseDirLoop()
	
	
	For i=0 To 4
		Textline$(i)=""
	Next

	
	Cls
	Color 100,100,100
	Rect 0,32,800,30,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,63,800,63
	
	
	
	MyText("Enter Name Of House Directory:",0,4)
	
	
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,38)
	
	ex$=HouseDir$
	MyText(ex$,0,36)
	Textline$(0)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36)

	
	;ascii keys
	askey=GetKey()
	If askey>=97 And askey<=122 Then askey=askey-32
	If ((askey>=48 And askey<=57) Or (askey>=65 And askey<=90))And Len(Textline$(0))<15 

		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	

	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			If Textline$(0)<>""
				HouseDir$=Textline$(0)
				HouseDirTrue=1
			Else
				HouseDirTrue=0
			EndIf
			GamePhase=1
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf

	housedir$=textline$(0)
	
	DrawImage MouseTexture,MouseX(),MouseY()

	
	Flip

	
	
End Function

Function EditModelDirLoop()
	
	
	For i=0 To 4
		Textline$(i)=""
	Next

	
	Cls
	Color 100,100,100
	Rect 0,32,800,30,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,63,800,63
	
	
	
	MyText("Enter Name Of Model Directory:",0,4)
	
	
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,38)
	
	ex$=ModelDir$
	MyText(ex$,0,36)
	Textline$(0)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36)

	
	;ascii keys
	askey=GetKey()
	If askey>=97 And askey<=122 Then askey=askey-32
	If ((askey>=48 And askey<=57) Or (askey>=65 And askey<=90))And Len(Textline$(0))<15 

		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	

	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			If Textline$(0)<>""
				ModelDir$=Textline$(0)
				ModelDirTrue=1
			Else
				ModelDirTrue=0
			EndIf

			GamePhase=1
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf
	
	DrawImage MouseTexture,MouseX(),MouseY()

	modeldir$=textline$(0)
	
	Flip

	
	
End Function

Function EditTextureDirLoop()
	
	
	For i=0 To 4
		Textline$(i)=""
	Next

	
	Cls
	Color 100,100,100
	Rect 0,32,800,30,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,63,800,63
	
	
	
	MyText("Enter Name Of Texture Directory:",0,4)
	
	
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,38)
	
	ex$=TextureDir$
	MyText(ex$,0,36)
	Textline$(0)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36)

	
	;ascii keys
	askey=GetKey()
	If askey>=97 And askey<=122 Then askey=askey-32
	If ((askey>=48 And askey<=57) Or (askey>=65 And askey<=90))And Len(Textline$(0))<15 

		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	

	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			If Textline$(0)<>""
				TextureDir$=Textline$(0)
				TextureDirTrue=1
			Else
				TextureDirTrue=0
			EndIf

			GamePhase=1
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf
	
	DrawImage MouseTexture,MouseX(),MouseY()

	texturedir$=textline$(0)
	
	Flip

	
	
End Function

Function EditBackgroundDirLoop()
	
	
	For i=0 To 4
		Textline$(i)=""
	Next

	
	Cls
	Color 100,100,100
	Rect 0,32,800,30,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,63,800,63
	
	
	
	MyText("Enter Name Of Background Directory:",0,4)
	
	
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,38)
	
	ex$=BackgroundDir$
	MyText(ex$,0,36)
	Textline$(0)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36)

	
	;ascii keys
	askey=GetKey()
	If askey>=97 And askey<=122 Then askey=askey-32
	If ((askey>=48 And askey<=57) Or (askey>=65 And askey<=90))And Len(Textline$(0))<15 

		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	

	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			If Textline$(0)<>""
				BackgroundDir$=Textline$(0)
				BackgroundDirTrue=1
			Else
				BackgroundDirTrue=0
			EndIf

			GamePhase=1
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf
	
	DrawImage MouseTexture,MouseX(),MouseY()

	Backgrounddir$=textline$(0)
	
	Flip

	
	
End Function




Function EditNameLoop()

	
	For i=0 To 4
		Textline$(i)=""
	Next

	Cls
	Color 100,100,100
	Rect 0,32,800,62,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,95,800,95
		
		
	MyText("Enter Level Name (Maximum Two Lines):",0,4)
	
	MyText("Use Cursor Keys, Delete, and BackSpace to Edit.",0,200-96)
	mytext("Use Cursor Up/Down to move between Lines.",0,200-64)
	MyText("Press 'Enter' or 'Esc' when done.",0,200-32)
	
	
	ex$=mapdescription$
	j=36
	k=0
	For i=1 To Len(ex$)
		If Mid$(ex$,i,1)="#"
			Textline$(k)=Left$(ex$,i-1)
			k=k+1
			MyText(Left$(ex$,i-1),0,j)
			j=j+32
			ex$=Right$(ex$,Len(ex$)-i)
			i=0
		EndIf
	Next
	MyText(ex$,0,j)
	Textline$(k)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36+CursorY*32)

	
	;ascii keys
	askey=GetKey()
	If (askey>=32 And askey<=122 And askey<>35 And askey<>36 And askey<>37) And Len(Textline$(CursorY))<50 
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	Else If (KeyDown(200) Or KeyDown(72)) And cursory>0
		If CursorKeyPressed=False
			cursorblink=0
			cursory=cursory-1
			If cursorx>Len(textline$(cursory)) Then cursorx=Len(textline$(cursory))
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(208) Or KeyDown(80)) And cursory<1
		If CursorKeyPressed=False
			cursorblink=0
			cursory=cursory+1
			If cursorx>Len(textline$(cursory)) Then cursorx=Len(textline$(cursory))
			CursorKeyPressed=True
		EndIf




	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			GamePhase=1
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf


	mapdescription$=textline$(0)
	If textline$(1)<>""
		mapdescription$=mapdescription$+"#"+textline$(1)
	EndIf
	
	
	Flip
	
End Function

Function EditSignLoop()
	
	
	
	Cls
	Color 100,100,100
	Rect 0,32,800,126,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,159,800,159
		
		
	MyText("Text for Sign $"+Str$(currenttile-1299)+" (Maximum Four Lines):",0,4)
	
	MyText("Use Cursor Keys, Delete, and BackSpace to Edit.",0,264-96)
	mytext("Use Cursor Up/Down to move between Lines.",0,264-64)
	MyText("Press 'Enter' or 'Esc' when done.",0,264-32)
	
	For i=0 To 3
		Textline$(i)=""
	Next
	
	ex$=signtext$(currenttile-1300)
	j=36
	k=0
	For i=1 To Len(ex$)
		If Mid$(ex$,i,1)="#"
			Textline$(k)=Left$(ex$,i-1)
			k=k+1
			MyText(Left$(ex$,i-1),0,j)
			j=j+32
			ex$=Right$(ex$,Len(ex$)-i)
			i=0
		EndIf
	Next
	MyText(ex$,0,j)
	Textline$(k)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36+CursorY*32)

	For i=1 To 3
		If textline$(i)=" " Then textline$(i)=""
	Next
	;ascii keys
	askey=GetKey()
	If (askey>=32 And askey<=122 And askey<>35 And askey<>36 And askey<>37) And Len(Textline$(CursorY))<50 
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	Else If (KeyDown(200) Or KeyDown(72)) And cursory>0
		If CursorKeyPressed=False
			cursorblink=0
			cursory=cursory-1
			If cursorx>Len(textline$(cursory)) Then cursorx=Len(textline$(cursory))
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(208) Or KeyDown(80)) And cursory<3
		If CursorKeyPressed=False
			cursorblink=0
			cursory=cursory+1
			If cursorx>Len(textline$(cursory)) Then cursorx=Len(textline$(cursory))
			CursorKeyPressed=True
		EndIf




	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			GamePhase=1
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf
	
	SignText$(currenttile-1300)=""
	For i=0 To 3
		If TextLine$(i)="" Then TextLine$(i)=" "
		SignText$(currenttile-1300)=SignText$(currenttile-1300)+TextLine$(i)+"#"
	Next
	If TextLine$(4)="" Then TextLine$(4)=" "
	SignText$(currenttile-1300)=SignText$(currenttile-1300)+TextLine$(4)

	
	Flip
	


End Function


Function LoadLevel()
	
	; count files and setup load
	Dir=ReadDir("customlevels")
	n=0
	; first count all files
	Repeat
		File$=Upper$(NextFile$(Dir))
		If File$="" Then Exit
		If Upper$(Right$(File$,4))=".LEV" Or Upper$(Right$(File$,4))=".LV5" Or Upper$(Right$(File$,4))=".LV6"

		
			File2=ReadFile("CustomLevels\"+File$)
			a=0
			For i=1 To 20
				a=a+ReadByte(file2)
			Next
			CloseFile(File2)
			
						
			If a=1380
			
				; sort
				If n=0 
					n2=0
				Else
					For i=0 To n-1
						If File$<=FileDir$(i)
							For j=n To i+1 Step -1
								FileDir$(j)=FileDir$(j-1)
								FileDir2$(j)=FileDir2$(j-1)
							Next
							n2=i
							Exit 
						EndIf
					Next
					If i=n
						n2=n
					EndIf
				EndIf
	
				FileDir$(n2)=File$
				
				File2=ReadFile("customlevels\"+FileDir$(n2))
				
				ex$=ReadString$(File2) 	; actually jsut the "level file" message
				If Right$(ex$,1)="2"
					mapfileversion=2
				Else If Right$(ex$,1)="3"
					mapfileversion=3
				Else If Right$(ex$,1)="4"
					mapfileversion=4
				Else If Right$(ex$,1)="5" 
					mapfileversion=5
				Else If Right$(ex$,1)="6"
					mapfileversion=6
				Else
					mapfileversion=1
				EndIf
				ex$=ReadString$(File2) ; level filename
				If mapfileversion>=3 Then ux=ReadInt(file2)
				FileDir2$(n2)=ReadString$(File2)
				CloseFile File2
				
				n=n+1
			EndIf
			
		EndIf
		If n=>4000
			Cls
			Print "_____"
			Print "TOO MANY LEVEL FILES!"
			Delay 5000
			End
		EndIf
	Forever
	

	FileDirN=n
	FileDirPos=0
	CloseDir(Dir)
	
	GamePhase=5
	
End Function



Function LoadLevelLoop()
	
	Keypress=GetKey()
	; keyboard - starting letter
	If (Keypress>64 And Keypress<91) Or (Keypress>96 And Keypress<123) 
		a$=Upper$(Chr$(keypress))
		For i=0 To FileDirN
			If a$<=FileDir$(i)
				Exit
			EndIf
		Next
		
		
	
		If FileDirN-i>=15
			FileDirPos=i
		Else
			FileDirPos=FileDirN-15
		EndIf
	EndIf
	
	Cls
	mytext("Select Level File To Load:                [CANCEL]",0,0)
	For i=0 To 25
		If FileDirPos>0
			DrawImage ArrowTexture,i*32,32,0
		Else
			DrawImage ArrowTexture,i*32,32,4
		EndIf
		If FileDirN-FileDirPos>15
			DrawImage ArrowTexture,i*32,64+15*32,2
		Else
			DrawImage ArrowTexture,i*32,64+15*32,6
		EndIf
	Next
	For i=0 To 14
		If i+FileDirPos<FileDirN
			MyText(Left$(FileDir$(i+FileDirPos),Len(FileDir$(i+FileDirPos))-4),0,64+i*32)
			MyText(FileDir2$(i+FileDirPos),15*16,64+i*32)
		EndIf
	Next
	DrawImage MouseTexture,MouseX(),MouseY()

	Flip
	
	
	
	If MouseDown(1)=True
		If MouseX()>600 And MouseY()<32 
			While MouseDown(1)=True
			Wend

			GamePhase=1
		EndIf
		If MouseY()>=32 And MouseY()<64 And FileDirPos>0
			FileDirPos=FileDirPos-1
			Delay 100
		EndIf
		If MouseY()>=64+15*32 And MouseY()<64+15*32+32 And FileDirN-FileDirPos>15
			FileDirPos=FileDirPos+1
			Delay 100
		EndIf
		If MouseY()>64 And MouseY()<64+15*32
			n=(MouseY()-64)/32+FileDirPos
			If n<FileDirN
				UndoAvailable=0
				While MouseDown(1)=True
				Wend
				
				
		
				ClearLevel()
			
				MapName$=Upper$(Left$(FileDir$(n),Len(FileDir$(n))-4))
				File=ReadFile("customlevels\"+FileDir$(n))
	
				
				MapName$=ReadString$(File) 	; actually jsut the "level file" message
				; except it has version info
				If Right$(mapname$,1)="2"
					mapfileversion=2
				Else If Right$(mapname$,1)="3"
					mapfileversion=3
				Else If Right$(mapname$,1)="4"
					mapfileversion=4
				Else If Right$(mapname$,1)="5" 
					mapfileversion=5
				Else If Right$(mapname$,1)="6"
					mapfileversion=6
				Else
					mapfileversion=1
				EndIf
				MapName$=ReadString$(File)
				If mapfileversion>=3 Then MapID=ReadInt(File)
				MapDescription$=ReadString$(File)
				
				If mapfileversion>=5
					housedirtrue=ReadInt(file)
					housedir$=ReadString$(File)
					modeldirtrue=ReadInt(File)
					modeldir$=ReadString$(File)
					

				Else
					housedirtrue=0
					housedir$=""
					modeldirtrue=0
					modeldir$=""
				EndIf
				If mapfileversion=6
					texturedirtrue=ReadInt(file)
					texturedir$=ReadString$(File)
					backgrounddirtrue=ReadInt(File)
					backgrounddir$=ReadString$(File)
					

				Else
					texturedirtrue=0
					texturedir$=""
					backgrounddirtrue=0
					backgrounddir$=""
				EndIf

				
				MapTime=ReadInt(File)
				MapStyle=ReadInt(File)
				MapBackground=ReadInt(File)
				MapWidth=ReadInt(File)
				MapHeight=ReadInt(File)
				For j=0 To MapHeight-1
					For i=0 To MapWidth-1
						MapTile(i,j)=ReadInt(File)
						If mapfileversion<5
							; old version to v5
							; buttons
							If maptile(i,j)>=1103 And maptile(i,j)<=1114
								maptile(i,j)=maptile(i,j)+3
							EndIf
						EndIf
						
					Next
				Next
				For j=0 To MapHeight-1
					For i=0 To MapWidth-1
						MapObject(i,j)=ReadInt(File)
						If mapfileversion=1
							; oldest version : re-sort objects
							mapobject(i,j)=version2objects(mapobject(i,j))
							; and sort again to update to newest version
							mapobject(i,j)=version5objects(mapobject(i,j))

						Else If mapfileversion<5
							; Return To Wonderland v1.xx version
							mapobject(i,j)=version5objects(mapobject(i,j))
						EndIf
					Next
				Next
				If mapfileversion<5
					For j=0 To 4
						SignText$(j)=ReadString$(File)
					Next
				Else
					For j=0 To 19
						SignText$(j)=ReadString$(File)
					Next
				EndIf
				MusicTrack=ReadInt(File)
				
				CloseFile(File)
				WinStartX=0
				WinStartY=0
				
				While MouseDown(1)=True
				Wend
	

			
			
				GamePhase=1
			EndIf
		EndIf
	EndIf
			
			
		
	
End Function

Function ClearLevel()

	TextureDirTrue=0
	BackgroundDirTrue=0

	
	EditorMode=0	; 0-tiles, 1-objects
	
	HouseDirTrue=0
	HouseDir$=""
	ModelDirTrue=0
	ModelDir$=""
	
	
	WinStartX=0
	WinStartY=0
	
	
	MapName$="UNTITLED"
	MapDescription$="Untitled Level"
	
	
	MapTime=300
	
	
	MapWidth=WinSize
	MapHeight=WinSize
	
	For i=0 To 99
		For j=0 To 99
			MapTile(i,j)=200
			MapObject(i,j)=0
		Next 
	Next	
	For i=1 To WinSize-2
		For j=1 To WinSize-2
			MapTile(i,j)=100
		Next 
	Next
	
	CurrentTile=200
	
	CurrentObject=1
	MapStyle=2
	MapBackground=0
	BlockMode=0
	
	For i=0 To 19
		SignText$(i)=""
	Next

End Function

Function MainMenuLoop()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("Return To Wonderland Platinum",10)
	CenterText ("-----------------------------",32)
	CenterText ("Level Editor Open Source v10.00",64)
	CenterText ("===========================",96)
	CenterText ("MAIN MENU",150)
	CenterText ("New Level",200)
	CenterText ("Load Level",232)
	CenterText ("Online Manual",264)
	CenterText ("Exit to Windows",296)
	CenterText ("Return To Game",328)
	CenterText ("Use The Mouse To Select Your Menu Option",500)
	CenterText ("Copyright (C) 2026 Midnight Synergy",568)

		
	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=5 Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-12*16,196+WhichMenuOption*32,24*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		If WhichMenuOption=0
			UndoAvailable=0
			ClearLevel()
			GamePhase=1
		Else If WhichMenuOption=1
			ClearLevel()
			LoadLevel()
		Else If WhichMenuOption=2
			GamePhase=18
		Else If WhichMenuOption=3
			GamePhase=7
		Else If WhichMenuOption=4
			GamePhase=9
		EndIf
	EndIf
	Flip


End Function
Function ExitMenuLoop()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("Return To Wonderland Platinum",10)
	CenterText ("-----------------------------",32)
	CenterText ("Level Editor v3.00",64)
	CenterText ("===========================",96)


	CenterText ("EXIT TO WINDOWS?",150)
	CenterText ("Yes, Please",200)
	CenterText ("No, Thanks!",232)
	CenterText ("Use The Mouse To Select Your Menu Option",500)

	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=2 Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-12*16,196+WhichMenuOption*32,24*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		If WhichMenuOption=0
			End
		Else If WhichMenuOption=1
			GamePhase=6
		EndIf
	EndIf

	
	Flip


End Function
Function ManualMenuLoop()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("Return To Wonderland Platinum",10)
	CenterText ("-----------------------------",32)
	CenterText ("Level Editor v3.00",64)
	CenterText ("===========================",96)


	CenterText ("CONNECT TO INTERNET FOR ONLINE MANUAL?",150)
	CenterText ("Yes, Please",200)
	CenterText ("No, Thanks!",232)
	CenterText ("Use The Mouse To Select Your Menu Option",500)

	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=2 Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-12*16,196+WhichMenuOption*32,24*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		If WhichMenuOption=0
			ExecFile("http://www.midnightsynergy.com/returntowonderland/editormanual")
			End
		Else If WhichMenuOption=1
			GamePhase=6
		EndIf
	EndIf

	
	Flip


End Function

Function ExitEditorLoop()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("Return To Wonderland Platinum",10)
	CenterText ("-----------------------------",32)
	CenterText ("Level Editor v3.00",64)
	CenterText ("===========================",96)


	CenterText ("EXIT EDITOR                            ",150)
	CenterText ("Yes, Please",200)
	CenterText ("No, Thanks!",232)
	CenterText ("Use The Mouse To Select Your Menu Option",500)
	
	If CursorBlink<15
		CenterText ("            (did you rememberto save)?",150)
	EndIf
	cursorblink=cursorblink+1
	If cursorblink>=30 cursorblink=0


	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=2 Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-12*16,196+WhichMenuOption*32,24*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		If WhichMenuOption=0
			GamePhase=6
		Else If WhichMenuOption=1
			GamePhase=1
		EndIf
	EndIf

	
	Flip


End Function

Function ExitToGameLoop()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("Return To Wonderland Platinum",10)
	CenterText ("-----------------------------",32)
	CenterText ("Level Editor v3.00",64)
	CenterText ("===========================",96)


	CenterText ("RETURN TO GAME?",150)
	CenterText ("Yes, Please",200)
	CenterText ("No, Thanks!",232)
	CenterText ("Use The Mouse To Select Your Menu Option",500)

	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=2 Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-12*16,196+WhichMenuOption*32,24*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		If WhichMenuOption=0
			Running=False
			ExitToGame=True
		Else If WhichMenuOption=1
			GamePhase=6
		EndIf
	EndIf

	
	Flip


End Function

Function WipeLoop()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("Return To Wonderland Platinum",10)
	CenterText ("-----------------------------",32)
	CenterText ("Level Editor v3.00",64)
	CenterText ("===========================",96)


	CenterText ("WIPE LEVEL?",150)
	CenterText ("Yes, Please",200)
	CenterText ("No, Thanks!",232)
	CenterText ("Use The Mouse To Select Your Menu Option",500)

	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=2 Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-12*16,196+WhichMenuOption*32,24*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		If WhichMenuOption=0
			MouseButtonInUse=False
			BackUpLevel()
			For i=0 To 99
				For j=0 To 99
					MapTile(i,j)=CurrentTile
					MapObject(i,j)=0
				Next 
			Next	
			GamePhase=1
		Else If WhichMenuOption=1
			GamePhase=1
		EndIf
	EndIf

	
	Flip


End Function

Function EditPackFileNameLoop()

	
	For i=0 To 4
		Textline$(i)=""
	Next

	
	Cls
	Color 100,100,100
	Rect 0,32,800,30,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,63,800,63
	
	
	
	MyText("Enter LevelPack FileName (do not include *.LPK):",0,4)
	
	
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,38)
	
	ex$=PackName$
	MyText(ex$,0,36)
	Textline$(0)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36)

	
	;ascii keys
	askey=GetKey()
	If askey>=97 And askey<=122 Then askey=askey-32
	If ((askey>=48 And askey<=57) Or (askey>=65 And askey<=90))And Len(Textline$(0))<15 

		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	

	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			packname$=textline$(0)
			cursorx=0
			PackDescription=""
			Repeat
			Until GetKey()=0
			GamePhase=12
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf

	
	packname$=textline$(0)
	
	DrawImage MouseTexture,MouseX(),MouseY()

	
	Flip

	
	
End Function



Function EditPackNameLoop()


	For i=0 To 4
		Textline$(i)=""
	Next

	
	Cls
	Color 100,100,100
	Rect 0,32,800,30,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,63,800,63
	
	
	
	MyText("Enter LevelPack Title:",0,4)
	
	
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,38)
	
	ex$=PackDescription$
	MyText(ex$,0,36)
	Textline$(0)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36)

	
	;ascii keys
	askey=GetKey()
	If (askey>=32 And askey<=122 And askey<>35 And askey<>36 And askey<>37) And Len(Textline$(0))<50
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	

	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			packdescription$=textline$(0)
			cursorx=0
			PackInfo=""
			Repeat
			Until GetKey()=0

			GamePhase=17
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf

	
	packdescription$=textline$(0)
	
	DrawImage MouseTexture,MouseX(),MouseY()

	
	Flip

	
	
End Function

Function EditPackInfoLoop()
	
	
	
	Cls
	Color 100,100,100
	Rect 0,32,800,126,1
		
	Color 255,255,255
	Line 0,31,800,31
	Line 0,159,800,159
		
		
	MyText("Description and Info (Maximum Four Lines):",0,4)
	
	MyText("Use Cursor Keys, Delete, and BackSpace to Edit.",0,264-96)
	mytext("Use Cursor Up/Down to move between Lines.",0,264-64)
	MyText("Press 'Enter' or 'Esc' when done.",0,264-32)
	
	
	ex$=PackInfo$
	j=36
	k=0
	For i=1 To Len(ex$)
		If Mid$(ex$,i,1)="#"
			Textline$(k)=Left$(ex$,i-1)
			k=k+1
			MyText(Left$(ex$,i-1),0,j)
			j=j+32
			ex$=Right$(ex$,Len(ex$)-i)
			i=0
		EndIf
	Next
	MyText(ex$,0,j)
	Textline$(k)=ex$
	
	CursorBlink=CursorBlink+1
	If CursorBlink>60 CursorBlink=0
	
	If CursorBlink<30 MyText("_",CursorX*16,36+CursorY*32)

	For i=1 To 3
		If textline$(i)=" " Then textline$(i)=""
	Next
	;ascii keys
	askey=GetKey()
	If (askey>=32 And askey<=122 And askey<>35 And askey<>36 And askey<>37) And Len(Textline$(CursorY))<50 
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0 And Len(Textline$(cursory))>0
				textline$(cursory)=Chr$(askey)+Textline$(cursory)
			Else If cursorx<Len(textline$(cursory))
				Textline$(cursorY)=Left$(Textline$(cursory),cursorx)+Chr$(askey)+Right$(Textline$(cursory),Len(Textline$(cursory))-cursorx)	
			Else
				Textline$(cursorY)=Textline$(cursory)+Chr$(askey)
			EndIf
			CursorKeyPressed=True
			cursorx=cursorx+1
			
		EndIf
			
		
	; backspace
	Else If KeyDown(14) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=Len(textline$(cursory))
				textline$(cursory)=Left$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx-1)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx)
			EndIf
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	; delete
	Else If KeyDown(211) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			If cursorx=0
				textline$(cursory)=Right$(textline$(cursory),Len(textline$(cursory))-1)
			Else 
				textline$(cursory)=Left$(textline$(cursory),cursorx)+Right$(textline$(cursory),Len(textline$(cursory))-cursorx-1)
			EndIf
			
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf


		
	; cursor keys
	Else If (KeyDown(75) Or KeyDown(203)) And cursorx>0
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx-1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf

	Else If (KeyDown(77) Or KeyDown(205)) And cursorx<Len(textline$(cursory))
		If CursorKeyPressed=False
			cursorblink=0
			cursorx=cursorx+1
			CursorKeyPressed=5
		Else
			cursorkeypressed=cursorkeypressed-1
			
		EndIf
	Else If (KeyDown(200) Or KeyDown(72)) And cursory>0
		If CursorKeyPressed=False
			cursorblink=0
			cursory=cursory-1
			If cursorx>Len(textline$(cursory)) Then cursorx=Len(textline$(cursory))
			CursorKeyPressed=True
		EndIf
	Else If (KeyDown(208) Or KeyDown(80)) And cursory<3
		If CursorKeyPressed=False
			cursorblink=0
			cursory=cursory+1
			If cursorx>Len(textline$(cursory)) Then cursorx=Len(textline$(cursory))
			CursorKeyPressed=True
		EndIf




	
		
	; enter	
	Else If (KeyDown(28) Or KeyDown(156)) Or KeyDown(1)
		If CursorKeyPressed=False
			GamePhase=13
			CursorKeyPressed=True
		EndIf	
	Else
		CursorKeyPressed=False
	EndIf
	
	PackInfo$=""
	For i=0 To 3
		If TextLine$(i)="" Then TextLine$(i)=" "
		PackInfo$=PackInfo$+TextLine$(i)+"#"
	Next
	If TextLine$(4)="" Then TextLine$(4)=" "
	PackInfo$=PackInfo$+TextLine$(4)

	
	Flip
	


End Function




Function PackMusicSelect()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("Select Music for LevelPack",0)
	CenterText ("--------------------------",32)
	
	For i=0 To MaxPackMusic-1
		CenterText (MusicName$(i),200+i*32)
	Next
	
		
	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=MaxPackMusic Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-16*16,196+WhichMenuOption*32,32*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		PackMusic=WhichMenuOption
		GamePhase=14
		
	EndIf
	Flip


End Function
Function PackLevelNumberSelect()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("Select Number of Levels",0)
	CenterText ("-----------------------",32)
	
	For i=0 To 9
		CenterText (Str$(i+1),200+i*32)
	Next
	
		
	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=10 Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-12*16,196+WhichMenuOption*32,24*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		PackLevelNumber=WhichMenuOption+1
		
		; count files and setup load
		Dir=ReadDir("customlevels")
		n=0
		; first count all files
		Repeat
			File$=Upper$(NextFile$(Dir))
			If File$="" Then Exit
			If Upper$(Right$(File$,4))=".LEV"
				
					; sort
				If n=0 
					n2=0
				Else
					For i=0 To n-1
						If File$<=FileDir$(i)
							For j=n To i+1 Step -1
								FileDir$(j)=FileDir$(j-1)
								FileDir2$(j)=FileDir2$(j-1)
							Next
							n2=i
							Exit 
						EndIf
					Next
					If i=n
						n2=n
					EndIf
				EndIf
	
			
				FileDir$(n2)=File$
							
				File2=ReadFile("customlevels\"+FileDir$(n2))
				ex$=ReadString$(File2) 	; actually jsut the "level file" message
				ex$=ReadString$(File2)
				FileDir2$(n2)=ReadString$(File2)
				CloseFile File2
				
				n=n+1
			EndIf
			If n=>4000
				Cls
				Print "_____"
				Print "TOO MANY LEVEL FILES!"
				Delay 5000
				End
			EndIf
		Forever
		
		
		
		FileDirN=n
		FileDirPos=0
		CloseDir(Dir)

		LevelPackFile=WriteFile("levelpacks\"+PackName$+".lpk")	
		WriteString LevelPackFile,"Wonderland Level Pack"
		WriteString LevelPackFile,packname$
		WriteString LevelPackFile,packdescription$
		WriteString LevelPackFile,packinfo$
		WriteInt	LevelPackFile,packmusic
		WriteInt	LevelPackFile,packlevelnumber
		CurrentLevelinPack=0
		GamePhase=15
		
	EndIf
	Flip


End Function

Function PackLevelSelect()

	Keypress=GetKey()
	; keyboard - starting letter
	If (Keypress>64 And Keypress<91) Or (Keypress>96 And Keypress<123) 
		a$=Upper$(Chr$(keypress))
		For i=0 To FileDirN
			If a$<=FileDir$(i)
				Exit
			EndIf
		Next
	
		If FileDirN-i>=15
			FileDirPos=i
		Else
			FileDirPos=FileDirN-15
		EndIf
	EndIf

	
	Cls
	mytext("Select Level For Position "+Str$(CurrentLevelInPack+1)+":              [CANCEL]",0,0)
	For i=0 To 25
		If FileDirPos>0
			DrawImage ArrowTexture,i*32,32,0
		Else
			DrawImage ArrowTexture,i*32,32,4
		EndIf
		If FileDirN-FileDirPos>15
			DrawImage ArrowTexture,i*32,64+15*32,2
		Else
			DrawImage ArrowTexture,i*32,64+15*32,6
		EndIf
	Next
	For i=0 To 14
		If i+FileDirPos<FileDirN
			MyText(Left$(FileDir$(i+FileDirPos),Len(FileDir$(i+FileDirPos))-4),0,64+i*32)
			MyText(FileDir2$(i+FileDirPos),15*16,64+i*32)
		EndIf
	Next
	DrawImage MouseTexture,MouseX(),MouseY()

	Flip
	
	If MouseDown(1)=True
		If MouseX()>600 And MouseY()<32 
			While MouseDown(1)=True
			Wend
			CloseFile(LevelPackFile)
			DeleteFile "levelpacks\"+PackName$+".lpk"
			GamePhase=6
		EndIf
		If MouseY()>=32 And MouseY()<64 And FileDirPos>0
			FileDirPos=FileDirPos-1
			Delay 100
		EndIf
		If MouseY()>=64+15*32 And MouseY()<64+15*32+32 And FileDirN-FileDirPos>15
			FileDirPos=FileDirPos+1
			Delay 100
		EndIf
		If MouseY()>64 And MouseY()<64+15*32
			n=(MouseY()-64)/32+FileDirPos
			If n<FileDirN
				UndoAvailable=0
				While MouseDown(1)=True
				Wend
		
				; Level Selected: Load and Add it To LPK File
				
				; Loading
				ClearLevel()
			
				MapName$=Upper$(Left$(FileDir$(n),Len(FileDir$(n))-4))
				File=ReadFile("customlevels\"+FileDir$(n))
				MapName$=ReadString$(File) 	; actually jsut the "level file" message
				MapName$=ReadString$(File)
									
				MapDescription$=ReadString$(File)
				MapTime=ReadInt(File)
				MapStyle=ReadInt(File)
				MapBackground=ReadInt(File)
				MapWidth=ReadInt(File)
				MapHeight=ReadInt(File)
				For j=0 To MapHeight-1
					For i=0 To MapWidth-1
						MapTile(i,j)=ReadInt(File)
					Next
				Next
				For j=0 To MapHeight-1
					For i=0 To MapWidth-1
						MapObject(i,j)=ReadInt(File)
					Next
				Next
				
				For j=0 To 4
					SignText$(j)=ReadString$(File)
				Next
			
				CloseFile(File)
				WinStartX=0
				WinStartY=0
				
				; Adding to LPK
				
				WriteString(LevelPackFile,"Stinky & Loof Level File")	
				WriteString(LevelPackFile,MapName$)
				WriteString(LevelPackFile,MapDescription$)
				WriteInt(LevelPackFile,MapTime)
				WriteInt(LevelPackFile,MapStyle)
				WriteInt(LevelPackFile,MapBackground)
				WriteInt(LevelPackFile,MapWidth)
				WriteInt(LevelPackFile,MapHeight)
				For j=0 To MapHeight-1
					For i=0 To MapWidth-1
						WriteInt(LevelPackFile,MapTile(i,j))
					Next
				Next
				For j=0 To MapHeight-1
					For i=0 To MapWidth-1
						WriteInt(LevelPackFile,MapObject(i,j))
					Next
				Next
				For j=0 To 4
					WriteString(LevelPackFile,SignText$(j))
				Next

				
				
				While MouseDown(1)=True
				Wend
	
				CurrentLevelInPack=CurrentLevelInPack+1
				If CurrentLevelInPack=PackLevelNumber
					CloseFile LevelPackFile
					GamePhase=16
				EndIf
			
			
				
			EndIf
		EndIf
	EndIf
			
			
		
	
End Function

Function PackCompletedLoop()
	Cls
	DrawImage MenuBkg,0,0
	CenterText ("LevelPack Has Been Created",0)
	CenterText ("--------------------------",32)
	CenterText ("FileName: "+Str$(packname$),64)
	CenterText (Str$(packdescription$),96)
	centertext ("--------------------------------------------------",128)
	
	CenterText ("Return to Main Menu",200)
	
	DrawImage MouseTexture,MouseX(),MouseY()
	
	WhichMenuOption=(MouseY()-200)/32
	If WhichMenuOption>=1 Then WhichMenuOption=-1
	
	If WhichMenuOption>=0 
		Color 255,255,0
		Rect 400-12*16,196+WhichMenuOption*32,24*16,32,0
	EndIf
	
	If MouseDown(1)=True And WhichMenuOption>=0
		While MouseDown(1)=True
		Wend
		GamePhase=6
		
	EndIf

	
	Flip


End Function






Function BackupLevel()

	If MouseButtonInUse=True Then Return

	MapWidthBackup=MapWidth
	MapHeightBackup=MapHeight
	For i=0 To MapWidth-1
		For j=0 To MapHeight-1
			MapTileBackup(i,j)=MapTile(i,j)
			MapObjectBackup(i,j)=MapObject(i,j)
		Next
	Next
	UndoAvailable=1
	MouseButtonInUse=True
End Function

	


Function SaveLevel()

	CheckLevel()

	File=WriteFile("customlevels/"+MapName$+".LV6")
	
	
	; Get the ID
	cd$=CurrentDate$()
	ct$=CurrentTime$()
	ms=MilliSecs()
	ID=0
	; Hours
	ex$=Left$(ct$,2)
	i=val(ex$)
	ID=ID+i
	; Days
	ex$=Left$(cd$,2)
	i=val(ex$)
	ID=ID+i*24
	; Months
	ex$=Mid$(cd$,4,3)
	If ex$="Jan" Then i=0
	If ex$="Feb" Then i=1
	If ex$="Mar" Then i=2
	If ex$="Apr" Then i=3
	If ex$="May" Then i=4
	If ex$="Jun" Then i=5
	If ex$="Jul" Then i=6
	If ex$="Aug" Then i=7
	If ex$="Sep" Then i=8
	If ex$="Oct" Then i=9
	If ex$="Nov" Then i=10
	If ex$="Dec" Then i=11
	ID=ID+i*24*31
	;Years
	ex$=Right$(cd$,4)
	i=(val(ex$)-2003) Mod 10
	ID=ID+i*10000
	;Millisecs
	i=ms Mod 1000
	ID=ID+i*100000
	;Minutes/3
	ex$=Mid$(ct$,4,2)
	i=(val(ex$)/3) Mod 21
	ID=ID+i*100000000
	
	
	
	WriteString(File,"Stinky & Loof Level File v6")	
	WriteString(File,MapName$)
	WriteInt(File,ID)
	WriteString(File,MapDescription$)
	WriteInt(File,HouseDirTrue)
	WriteString(File,HouseDir$)
	WriteInt(File,ModelDirTrue)
	WriteString(File,ModelDir$)
	WriteInt(File,TextureDirTrue)
	WriteString(File,TextureDir$)
	WriteInt(File,BackgroundDirTrue)
	WriteString(File,BackgroundDir$)

	
	WriteInt(File,MapTime)
	WriteInt(File,MapStyle)
	WriteInt(File,MapBackground)
	WriteInt(File,MapWidth)
	WriteInt(File,MapHeight)
	For j=0 To MapHeight-1
		For i=0 To MapWidth-1
			WriteInt(File,MapTile(i,j))
		Next
	Next
	For j=0 To MapHeight-1
		For i=0 To MapWidth-1
			WriteInt(File,MapObject(i,j))
		Next
	Next
	For j=0 To 19
		WriteString(File,SignText$(j))
	Next
	WriteInt(File,MusicTrack)
	
	CloseFile(File)
	
	

End Function

Function MyText(tex$,x,y)
	For ex=1 To Len(tex$)
		Ascii=Asc(Mid$(tex$,ex,1))
		DrawImage FontTex((Ascii-32)/16),x,y,(ascii-32) Mod 16
		x=x+16
	Next

End Function
Function CenterText(tex$,y)
	For ex=1 To Len(tex$)
		Ascii=Asc(Mid$(tex$,ex,1))
		DrawImage FontTex((Ascii-32)/16),(50-Len(tex$))*8+(ex-1)*16,y,(ascii-32) Mod 16
	
	Next

End Function

Function val(ex$)
	j=0
	k=1
	For i=1 To Len(ex$)
		j=j+(Asc(Mid$(ex$,Len(ex$)+1-i,1))-48)*k
		k=k*10
	Next
	Return j
End Function

Function checklevel()

	chars=0
	; stinky/multiple stinkys?
	stinkys=0
	For j=0 To MapHeight-1
		For i=0 To MapWidth-1
			If MapObject(i,j)=1 Then stinkys=stinkys+1
		Next
		chars=chars+stinkys
	Next
	
	If stinkys>1
		stinkys=0
		For j=0 To MapHeight-1
			For i=0 To MapWidth-1
				If MapObject(i,j)=1
					If stinkys=0
						stinkys=1
					Else
						mapobject(i,j)=0
					EndIf
				EndIf
			Next
		Next
	EndIf
	; loof multiples?
	stinkys=0
	For j=0 To MapHeight-1
		For i=0 To MapWidth-1
			If MapObject(i,j)=2 Then stinkys=stinkys+1
		Next
		chars=chars+stinkys

	Next
	If stinkys>1
		stinkys=0
		For j=0 To MapHeight-1
			For i=0 To MapWidth-1
				If MapObject(i,j)=2
					If stinkys=0
						stinkys=1
					Else
						mapobject(i,j)=0
					EndIf
				EndIf
			Next
		Next
	EndIf
	; qookie multiples?
	stinkys=0
	For j=0 To MapHeight-1
		For i=0 To MapWidth-1
			If MapObject(i,j)=3 Then stinkys=stinkys+1
		Next
		chars=chars+stinkys

	Next
	If stinkys>1
		stinkys=0
		For j=0 To MapHeight-1
			For i=0 To MapWidth-1
				If MapObject(i,j)=3
					If stinkys=0
						stinkys=1
					Else
						mapobject(i,j)=0
					EndIf
				EndIf
			Next
		Next
	EndIf
	; peegue multiples?
	stinkys=0
	For j=0 To MapHeight-1
		For i=0 To MapWidth-1
			If MapObject(i,j)=4 Then stinkys=stinkys+1
		Next
		chars=chars+stinkys

	Next
	If stinkys>1
		stinkys=0
		For j=0 To MapHeight-1
			For i=0 To MapWidth-1
				If MapObject(i,j)=4
					If stinkys=0
						stinkys=1
					Else
						mapobject(i,j)=0
					EndIf
				EndIf
			Next
		Next
	EndIf

	; no characters?
	If chars=0
		MapObject(1,1)=1
	EndIf

	
 


End Function


	