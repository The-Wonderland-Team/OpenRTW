Function LoadSounds()

	; Load Sound FX
	;SFXWalk=MyLoadSound("sound\walk.wav",True)
	
		
	SFXLevelLose=MyLoadSound("sound\death2.wav",False)
	;SFXLevelWin=MyLoadSound("sound\win.wav",True)
	SFXBreakAway=MyLoadSound("sound\breakaway.wav",False)
	SFXBoxPush=MyLoadSound("sound\boxpush.wav",False)
	SFXBoxSplosh=MyLoadSound("sound\splosh.wav",False)
	SFXBoxExplo=MyLoadSound("sound\boxexplo.wav",False)
	SFXTeleport=MyLoadSound("sound\teleport.wav",False)
	SFXButton=MyLoadSound("sound\button.wav",False)
	
	SFXBounce=MyLoadSound("sound\bounce.wav",False)
	SFXCoilyDeath=MyLoadSound("sound\coilydeath.wav",False)
	SFXBombExplo=MyLoadSound("sound\bombexplo.wav",False)
	
	SFXBoulderPush=MyLoadSound("sound\boulderpush.wav",False)
	SFXBoulderHit=MyLoadSound("sound\boulderhit.wav",False)
	
	SFXSingle(0)=MyLoadSound("sound\electro.wav",False)
	SFXSingle(1)=MyLoadSound("sound\fireshot.wav",False)
	SFXSingle(2)=MyLoadSound("sound\fireexplo.wav",False)
	SFXSingle(3)=MyLoadSound("sound\saucershot.wav",False)
	SFXSingle(4)=MyLoadSound("sound\saucerfly.wav",False)
	SFXSingle(5)=MyLoadSound("sound\spikeup.wav",False)
	SFXSingle(6)=MyLoadSound("sound\spikedown.wav",False)
	SFXSingle(7)=MyLoadSound("sound\reflector.wav",False)
	SFXSingle(8)=MyLoadSound("sound\chomper1.wav",False)
	SFXSingle(9)=MyLoadSound("sound\chomper1.wav",False)
	SFXSingle(10)=MyLoadSound("sound\chomper1.wav",False)
	SFXSingle(11)=MyLoadSound("sound\chomper1.wav",False)
	SFXSingle(12)=MyLoadSound("newsounds\prism1.wav",False)
	SFXSingle(13)=MyLoadSound("newsounds\boxgen1.wav",False)
	SFXSingle(14)=MyLoadSound("newsounds\mother1.wav",False)
	SFXSingle(15)=MyLoadSound("newsounds\spirit1.wav",False)
	
	SFXSingle(16)=MyLoadSound("res49.dat",False)
	
	SFXSticky=MyLoadSound("res48.dat",False)
	SFXTrampoline=MyLoadSound("res47.dat",False)
	SFXTransporter=MyLoadSound("res50.dat",False)

	SFXpBounce(1,1)=MyLoadSound("res31.dat",False)
	SFXpBounce(1,2)=MyLoadSound("res32.dat",False)
	SFXpBounce(1,3)=MyLoadSound("res33.dat",False)
	SFXpBounce(1,4)=MyLoadSound("res34.dat",False)
	SFXpBounce(2,1)=MyLoadSound("res35.dat",False)
	SFXpBounce(2,2)=MyLoadSound("res36.dat",False)
	SFXpBounce(2,3)=MyLoadSound("res37.dat",False)
	SFXpBounce(2,4)=MyLoadSound("res38.dat",False)
	SFXpBounce(3,1)=MyLoadSound("res39.dat",False)
	SFXpBounce(3,2)=MyLoadSound("res40.dat",False)
	SFXpBounce(3,3)=MyLoadSound("res41.dat",False)
	SFXpBounce(3,4)=MyLoadSound("res42.dat",False)
	SFXpBounce(4,1)=MyLoadSound("res43.dat",False)
	SFXpBounce(4,2)=MyLoadSound("res44.dat",False)
	SFXpBounce(4,3)=MyLoadSound("res45.dat",False)
	SFXpBounce(4,4)=MyLoadSound("res46.dat",False)





	
	
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"80% Complete",True,False
	Flip






	
	; stinky and loof voices
	
	SFXPlayerCoin(1)=MyLoadSound("sound\stinkycoin.wav",False)
	SFXPlayerBonus(1)=MyLoadSound("sound\stinkybonus.wav",False)
	SFXPlayerClock(1)=MyLoadSound("sound\stinkyclock.wav",False)
	If FileType("CustomVoices\stinkyend.wav")=1
		SFXPlayerEnd(1)=MyLoadSound("CustomVoices\stinkyend.wav",False)
	Else
		SFXPlayerEnd(1)=MyLoadSound("sound\stinkyend.wav",False)
	EndIf
	SFXPlayerConvey(1)=MyLoadSound("sound\stinkyconvey.wav",False)
	
	SFXPlayerCoin(2)=MyLoadSound("sound\loofcoin.wav",False)
	SFXPlayerBonus(2)=MyLoadSound("sound\loofbonus.wav",False)
	SFXPlayerClock(2)=MyLoadSound("sound\loofclock.wav",False)
	If FileType("CustomVoices\loofend.wav")=1
		SFXPlayerEnd(2)=MyLoadSound("CustomVoices\loofend.wav",False)
	Else
		SFXPlayerEnd(2)=MyLoadSound("sound\loofend.wav",False)
	EndIf
	SFXPlayerConvey(2)=MyLoadSound("sound\loofconvey.wav",False)
	
	SFXPlayerCoin(3)=MyLoadSound("newsounds\qookiecoin3.wav",False)
	SFXPlayerBonus(3)=MyLoadSound("newsounds\qookiecoin4.wav",False)
	SFXPlayerClock(3)=MyLoadSound("newsounds\qookietime1.wav",False)
	If FileType("CustomVoices\qookieend.wav")=1
		SFXPlayerEnd(3)=MyLoadSound("CustomVoices\qookieend.wav",False)
	Else
		SFXPlayerEnd(3)=MyLoadSound("newsounds\qookieend1.wav",False)
	EndIf
	SFXPlayerConvey(3)=MyLoadSound("newsounds\qookiecoin5.wav",False)
	
	SFXPlayerCoin(4)=MyLoadSound("newsounds\peeguecoin3.wav",False)
	SFXPlayerBonus(4)=MyLoadSound("newsounds\peeguetime2.wav",False)
	SFXPlayerClock(4)=MyLoadSound("newsounds\peeguetime1.wav",False)
	If FileType("CustomVoices\peegueend.wav")=1
		SFXPlayerEnd(4)=MyLoadSound("CustomVoices\peegueend.wav",False)
	Else
		SFXPlayerEnd(4)=MyLoadSound("newsounds\peegueend1.wav",False)
	EndIf
	SFXPlayerConvey(4)=MyLoadSound("newsounds\peegueconvey1.wav",False)


	


	
	SFXStinkycoin=MyLoadSound("sound\stinkycoin.wav",False)
	SFXLoofcoin=MyLoadSound("sound\Loofcoin.wav",False)
	SFXStinkyBonus=MyLoadSound("sound\StinkyBonus.wav",False)
	SFXLoofBonus=MyLoadSound("sound\LoofBonus.wav",False)
	SFXLastCoin=MyLoadSound("sound\lastcoin.wav",False)
	SFXStinkyEnd=MyLoadSound("sound\StinkyEnd.wav",False)
	SFXLoofEnd=MyLoadSound("sound\LoofEnd.wav",False)
;	SFXStinkyIce=MyLoadSound("StinkyIce.wav",False)
;	SFXLoofIce=MyLoadSound("LoofIce.wav",False)
	SFXStinkyConvey=MyLoadSound("sound\StinkyConvey.wav",False)
	SFXLoofConvey=MyLoadSound("sound\LoofConvey.wav",False)
	SFXStinkyClock=MyLoadSound("sound\Stinkyclock.wav",False)
	SFXLoofClock=MyLoadSound("sound\LoofClock.wav",False)
	SFXSaucerDie=MyLoadSound("sound\SaucerDie.wav",False)
	
	UpdateWorld
	RenderWorld
	Text DisplayWidth/2,DisplayHeight*2/3,"90% Complete",True,False
	Flip


	
	
	SFXHmmm=MyLoadSound("sound\slhmmm.wav",False)
	SFXTime=MyLoadSound("sound\sltime.wav",False)
	SFXDeath=MyLoadSound("newsounds\sldeathquiet.wav",False)
	
	If FileType("CustomVoices\start1.wav")=1
		SFXTalk(0)=MyLoadSound("CustomVoices\start1.wav",False)
		For i=1 To 4
			SFXTalk(i)=SFXTalk(0)
		Next
	Else
		SFXTalk(0)=MyLoadSound("sound\getready.wav",False)
		SFXTalk(1)=MyLoadSound("sound\herewego.wav",False)
		SFXTalk(2)=MyLoadSound("sound\cmonletsgo.wav",False)
		SFXTalk(3)=MyLoadSound("sound\letsdoit.wav",False)
		SFXTalk(4)=MyLoadSOund("newsounds\gitready.wav",False)
	EndIf
	
	
	SFXTalk(5)=MyLoadSound("sound\stinkyno.wav",False)
	If FileType("CustomVoices\start2.wav")=1
		SFXTalk(6)=MyLoadSound("CustomVoices\start2.wav",False)
	Else
		SFXTalk(6)=MyLoadSound("newsounds\mynameisqookie2.wav",False)
	EndIf
	If FileType("CustomVoices\start3.wav")=1
		SFXTalk(7)=MyLoadSound("CustomVoices\start3.wav",False)
	Else
		SFXTalk(7)=MyLoadSound("newsounds\peeguespell3.wav",False)
	EndIf
	
	
	If FileType("CustomVoices\stinky.wav")=1
		SFXTalk(11)=MyLoadSound("CustomVoices\stinky.wav",False)
	Else
		SFXTalk(11)=MyLoadSOund("newsounds\charstinky.wav",False)
	EndIf
	If FileType("CustomVoices\loof.wav")=1
		SFXTalk(12)=MyLoadSound("CustomVoices\loof.wav",False)
	Else
		SFXTalk(12)=MyLoadSOund("newsounds\charloof.wav",False)
	EndIf
	If FileType("CustomVoices\qookie.wav")=1
		SFXTalk(13)=MyLoadSound("CustomVoices\qookie.wav",False)
	Else
		SFXTalk(13)=MyLoadSOund("newsounds\charqookie2.wav",False)
	EndIf
	If FileType("CustomVoices\peegue.wav")=1
		SFXTalk(14)=MyLoadSound("CustomVoices\peegue.wav",False)
	Else
		SFXTalk(14)=MyLoadSOund("newsounds\charpeegue.wav",False)
	EndIf

	

	SFXMenuSound(0)=MyLoadSound("sound\menumove.wav",False)
	SFXMenuSound(1)=MyLoadSound("sound\type.wav",False)
	For i=0 To 10
		SFXCount(i)=MyLoadSound("sound\sl"+Str$(i)+".wav",False)
	Next
	
	If Version>=10
		SFXTimerButton=MyLoadSound("sound\windup.wav",False)
		SFXIce=MyLoadSound("sound\ice.wav",False)
		For i=0 To 4
			SFXSlIce(i)=MyLoadSound("sound\slice"+Str$(i+1)+".wav",False)
		Next
		SFXLava=MyLoadSound("sound\blub.wav",False)	
	EndIf
	
	SFXLightning(0)=MyLoadSound("sound\lightning1.wav",False)
	SFXLightning(1)=MyLoadSound("sound\lightning2.wav",False)
	SFXLightning(2)=MyLoadSound("sound\lightning3.wav",False)
	SFXLightning(3)=MyLoadSound("sound\lightning4.wav",False)



	
End Function


Function PlayMySound(bsound,bentity)
	If OptionsSoundLevel=0 Return
	If DoesSoundExist=False Return
	; if no distance specified
	If bentity=0 
		SoundVolume bsound,OptionsSoundLevel
		PlaySound(bsound)
		
		Return
	EndIf

	; calculate distance from sound
	TranslateEntity Camera,0,-10,9
	distance#=EntityDistance (Camera,bentity)
	TranslateEntity Camera,0,10,-9

	a#=3.0 ; minimum radius
	b#=9.0 ; maximum radius
	
	volume#=(b-distance)/(b-a)
	
	If OptionsSoundLevel*volume<=0 Then Return
	
	If volume>1 Then volume=1
	
	
	SoundVolume(bsound,OptionsSoundLevel*volume)
	PlaySound(bsound)	
	


	
End Function

Function PlaySingleSound(x,y,bType)

	; 0-electro
	; 1-cannon
	; 2-fireball
	; 3-saucershot
	; 4-saucerfly
	; 5-spikeup
	; 6-spikedown
	; 7-fireball reflected

	If OptionsSoundLevel=0 Return
	If DoesSoundExist=False Then Return
	
	If x=-999 
		SFXSingleVolume(btype)=1.0
		Return
	EndIf

	; just checks volume... at the end plays highest volume sound in UpdateGame

	bentity=CreatePivot()
	TranslateEntity bentity,x+.5,0,-y-0.5
	
	; calculate distance from sound
	TranslateEntity Camera,0,-10,9
	distance#=EntityDistance (Camera,bentity)
	TranslateEntity Camera,0,10,-9

	a#=3.0 ; minimum radius
	b#=9.0 ; maximum radius
	
	volume#=(b-distance)/(b-a)
	
	If volume<=0 Then 
		FreeEntity bentity
		Return
	EndIf
	
	If volume>1 Then volume=1
	
	; now, is it bigger?
	
	If volume>SFXSingleVolume(btype) Then SFXSingleVolume(btype)=volume
	
	FreeEntity bentity
	
End Function