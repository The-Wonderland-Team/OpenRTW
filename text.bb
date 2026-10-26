

Function SetupFont()

	Modus=256+2

	FontTex(0)=LoadAnimTexture("res15.dat",2,16,32,0,16)
	If FontTex(0)=0 
		Modus=256+6
		FontTex(0)=MyLoadAnimTexture("res15.dat",6,16,32,0,16,False)

	EndIf
	FontTex(1)=MyLoadAnimTexture("res16.dat",Modus,16,32,0,16,False)
	FontTex(2)=MyLoadAnimTexture("res17.dat",Modus,16,32,0,16,False)
	FontTex(3)=MyLoadAnimTexture("res18.dat",Modus,16,32,0,16,False)
	FontTex(4)=MyLoadAnimTexture("res19.dat",Modus,16,32,0,16,False)
	FontTex(5)=MyLoadAnimTexture("res20.dat",Modus,16,32,0,16,False)

	
	
	
	
End Function

Function AddLetter(Ascii,x#,y#,Alpha#,MyTag)

	l.LetterType = New LetterType
	
	l\Entity = CreateSprite(Camera)
	ScaleSprite l\Entity,.01,.02
	If view3d=True
		TranslateEntity l\Entity,-.5+x*.02+.01,0.375-y*.04-.02,0.5
	Else
		TranslateEntity l\Entity,-.5+x*.02+.01,0.375-y*.04-.02,1
	EndIf
	EntityTexture l\Entity,FontTex((Ascii-32)/16),(Ascii-32) Mod 16
	EntityAlpha l\Entity,Alpha
	
	HideEntity l\Entity
	
	l\Tag=MyTag
	l\Active=False

End Function

Function DisplayText(MyText$,x#,y#,Alpha#,MyTag)
	
	; First go through existing letters and erase all with the same Tag
	For l.LetterType = Each LetterType
		If l\Tag=MyTag
			FreeEntity(l\Entity)
			Delete l
		EndIf
	Next
	
	; Now add the line
	For i=1 To Len(MyText$)
		AddLetter(Asc(Mid$(MyText$,i,1)),x+i-1,y,Alpha#,MyTag)
	Next 

End Function

Function CenterText(MyText$,y#,Alpha#,MyTag)
	
	x#=25.0-Len(MyText$)/2.0
	
	; First go through existing letters and erase all with the same Tag
	For l.LetterType = Each LetterType
		If l\Tag=MyTag
			FreeEntity(l\Entity)
			Delete l
		EndIf
	Next
	
	; Now add the line
	For i=1 To Len(MyText$)
		AddLetter(Asc(Mid$(MyText$,i,1)),x+i-1,y,Alpha#,MyTag)
	Next 

End Function


Function StartSign(MyText$,y#,MyTag,Alpha#)

	; First go through existing letters and erase all with the same Tag
	For l.LetterType = Each LetterType
		If l\Tag=MyTag
			FreeEntity(l\Entity)
			Delete l
		EndIf
	Next
	
	If mytext$="" Then Return
	
	; Now Parse the text and add the lines 
	j=1
	While(j<Len(MyText$))
		If Mid$(MyText$,j,1)="#" Or j=Len(MyText$)
			; display the line
			For i=1 To j-1
				AddLetter(Asc(Mid$(MyText$,i,1)),25.0-Float(j-1)/2.0+i-1,y,Alpha,MyTag)
			Next 
			y=y+1
			; and delete and reset that line
			MyText$=Right$(MyText$,Len(MyText$)-j)
			j=0
		EndIf
		j=j+1
	Wend
	; display final line
	
	For i=1 To j
		AddLetter(Asc(Mid$(MyText$,i,1)),25.0-Float(j)/2.0+i-1,y,Alpha,MyTag)
	Next 
	
	; light
	If MyTag>=11 And MyTag<=30
		For k=1 To 4
			If FocusOnPlayer(k)=True
				SignActive=k
			EndIf
		Next
		
		;LightColor LevelLight,0,0,0
		;AmbientLight 100,100,100
		TargetLightRed=0
		TargetLightGreen=0
		TargetLightBlue=0
		LightChangeSpeed=10
		If LevelRed=True
			TargetAmbientRed=100
		Else
			TargetAmbientRed=100
		EndIf
		If LevelGreen=True
			TargetAmbientGreen=100
		Else
			TargetAmbientGreen=100
		EndIf
		If levelblue=True
			TargetAmbientBlue=100
		Else
			TargetAmbientBlue=100
		EndIf
		AmbientChangeSpeed=5
		
	EndIf



End Function
Function ControlSigns()
	; handles blanding in and out of levelname sign
	
	If LevelTimer>800 Return
	
	
	If LevelTimer<200
		For l.LetterType=Each LetterType
			If l\Tag=10
				EntityAlpha l\Entity,Float(LevelTimer)/200.0
			EndIf
			
		Next
	Else If LevelTimer>600 And LevelTimer<800
		For l.LetterType=Each LetterType
			If l\Tag=10
				EntityAlpha l\Entity,1.0-Float(LevelTimer-600)/200.0
			EndIf
		Next
	Else If LevelTimer=800
		StopSign(10)
	EndIf

	
End Function

Function StopSign(MyTag)
	For l.LetterType=Each LetterType
		If l\Tag=MyTag
			FreeEntity l\Entity
			Delete l
		EndIf
	Next
	
	; light
	If MyTag>=11 And MyTag<=30
	
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

	
		SignActive=0


		LightChangeSpeed=10

		AmbientChangeSpeed=5

	EndIf

End Function


Function CheckSigns()

	For k=1 To 4
		If FocusOnPlayer(k)=True
			; see if stinky on signs
			For i=0 To 19
				If PlayerX(k)=SignX(i) And PlayerY(k)=SignY(i)
					StartSign(SignText$(i),8,11+i,1)
				EndIf
			Next
		EndIf
	Next
End Function