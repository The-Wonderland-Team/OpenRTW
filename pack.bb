Function InitPackFile()

	listfile=ReadFile("res1.dat")
	i=0
	Repeat
		PackFileName$(i)=Lower$(ReadString$(listfile))
		PackFilePosition(i)=ReadInt(listfile)
		PackFileLength(i)=ReadInt(listfile)
		i=i+1
	Until Eof(listfile)
	
	NofPackfiles=i
	
	CloseFile(listfile)
				



End Function

Function Pack$(ex$)

	GotitFlag=False

	If Lower$(Right$(ex$,4))=".3ds"
		suffix$=".3ds"
	Else If Lower$(Right$(ex$,2))=".x"
		suffix$=".x"
	Else
		suffix$=".tmp"
	EndIf
	

	; delete .tmp files
	dir=ReadDir(CurrentDir$())
	Repeat
		a$=NextFile$(dir)
		If Lower$(Right$(a$,4))=".tmp" Or Lower$(Right$(a$,4))=".3ds" Or Lower$(Right$(a$,2))=".x"
			If packcleanup=True Then DeleteFile a$
		EndIf
	Until a$=""
	CloseDir dir
	
	For j=0 To NofPackFiles-1
	
	;	Print ex$+" "+packfilename$(j)
	;	Delay 100
		
		If Lower$(ex$)=packfilename$(j)
	;		Delay 1000
			; got it
			GotitFlag=True
			infile=ReadFile("res2.dat")
			outfile=WriteFile(Str$(packfilecount)+suffix$)
			SeekFile (infile,PackFilePosition(j))
			For k=1 To PackFileLength(j)
				WriteByte outfile,ReadByte(infile)
			Next 
			CloseFile(infile)
			CloseFile(outfile)
			Exit 
		EndIf
			
	Next 
	

	
	packfilecount=packfilecount+1
	
	
	;Print ex$
	;Print Str$(packfilecount-1)+suffix$
	;Print "------"
	;Delay 500
	
	If GotitFlag=True
		Return Str$(packfilecount-1)+suffix$
	Else
		Return ex$
	EndIf
	
End Function