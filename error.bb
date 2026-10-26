

Function MyLoadTexture (ex$,Flag,Pak)


	i=0
	If PackOverride=0
		If Pak=False 
			; load the file from disk normally
			i=LoadTexture(ex$,Flag)
		Else
			i=LoadTexture(pack(ex$),Flag)
		EndIf
	Else 
		If PackOverRide=-1 ; never use pack
			i=LoadTexture(ex$,Flag)
		Else
			i=LoadTexture(pack(ex$),Flag)
			If i=0
				i=LoadTexture(ex$,Flag)
			EndIf
			
		EndIf
	EndIf
	

	
	If i=0
		; didn't find it
		GlobalError("Couldn't load Texture: "+ex$+", Flag "+Str$(Flag))
	EndIf

	Return i
	
End Function

Function MyLoadAnimTexture(ex$,Flag,Width,Height,Firs,Count,Pak)

	i=0
	If PackOverride=0
		If Pak=False 
			; load the file from disk normally
			i=LoadAnimTexture(ex$,Flag,Width,Height,Firs,Count)
		Else
			i=LoadAnimTexture(pack(ex$),Flag,Width,Height,Firs,Count)
		EndIf
	Else 
		If PackOverRide=-1 ; never use pack
			i=LoadAnimTexture(ex$,Flag,Width,Height,Firs,Count)
		Else
			i=LoadAnimTexture(pack(ex$),Flag,Width,Height,Firs,Count)
			If i=0
				i=LoadAnimTexture(ex$,Flag,Width,Height,Firs,Count)
			EndIf
			
		EndIf
	EndIf
	

	If i=0
		GlobalError("Couldn't load AnimTexture: "+ex$)
	EndIf
	
	Return i

End Function

Function MyReadFile(ex$,pak)

	i=0
	If Lower$(Left$(ex$,12)="levels\bonus") And Right$(ex$,4)=".lev"
		i=ReadFile(Right$(ex$,10))
		If i=0
			GlobalError("Couldn't load Bonus Level - Install files in same directory!")
		EndIf
		Return i

	EndIf
	If PackOverride=0
		If Pak=False 
			; load the file from disk normally
			i=ReadFile(ex$)
		Else
			i=ReadFile(Pack(ex$))
			
		EndIf
	Else 
		If PackOverRide=-1 ; never use pack
			i=ReadFile(ex$)
		Else
			i=ReadFile(Pack(ex$))
			If i=0
				i=ReadFile(ex$)
			EndIf
						
		EndIf
	EndIf
	

	If i=0
		GlobalError("Couldn't load File: "+ex$)
	EndIf
	
	Return i

	
End Function

Function MyLoadMD2(ex$,Pak)


	i=0
	If PackOverride=0
		If Pak=False 
			; load the file from disk normally
			i=LoadMD2(ex$)
		Else
			i=LoadMD2(Pack(ex$))
		EndIf
	Else 
		If PackOverRide=-1 ; never use pack
			i=LoadMD2(ex$)
		Else
			i=LoadMD2(Pack(ex$))
			If i=0
				i=LoadMD2(ex$)
			EndIf
					
		EndIf
	EndIf
	

	If i=0
		GlobalError("Couldn't load MD2: "+ex$)
	EndIf

	
	Return i
End Function

Function MyLoadMesh(ex$,Pak)

	i=0
	If PackOverride=0
		If Pak=False 
			; load the file from disk normally
			i=LoadMesh(ex$)
		Else
			i=LoadMesh(Pack(ex$))
		EndIf
	Else 
		If PackOverRide=-1 ; never use pack
			i=LoadMesh(ex$)
		Else
			i=LoadMesh(pack(ex$))
			If i=0
				i=LoadMesh(ex$)
			EndIf
		
		EndIf
	EndIf
	

	If i=0
		GlobalError("Couldn't load Mesh: "+ex$)
	EndIf


	
	Return i
End Function

Function MyLoadSound(ex$,Pak)
	
	i=0
	If PackOverride=0
		If Pak=False 
			; load the file from disk normally
			i=LoadSound(ex$)
		Else
			i=LoadSound(Pack(ex$))
			
		EndIf
	Else 
		If PackOverRide=-1 ; never use pack
			i=LoadSound(ex$)
		Else
			i=LoadSound(Pack(ex$))
			If i=0
				i=LoadSound(ex$)
			EndIf
						
		EndIf
	EndIf
	

	If i=0
		DoesSoundExist=False
	EndIf
	
	Return i
End Function





	
Function GlobalError(Tex$)
	Print ""
	Print "-----"
	Print "ERROR"
	Print "-----"
	Print tex$
	Print 
	Print "Press Esc to exit."
	Repeat
	Until KeyDown(1)=False
	Repeat
	Until KeyDown(1)=True
	End
End Function