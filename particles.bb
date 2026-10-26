

Function CreateParticle(btype,btex,bsize#,x#,y#,z#,dx#,dy#,dz#,dr#,ddy#,timer)
	
	
	; only 200 max
	If NofParticles>=100*DetailLevel Then Return
	
	p.particle=New particle
	p\x=x
	p\y=y
	p\z=z
	p\r=0
	p\dx=dx	
	p\dy=dy
	p\dz=dz
	p\dr=dr
	p\ddy=ddy
	p\timer=timer
	p\timer2=timer
	p\Entity=CopyEntity(ParticleMesh(btype))
	If btex<100
		EntityTexture p\Entity,ParticleTexture,btex
	Else
		EntityTexture p\Entity,ParticleTexture2,btex-100
	EndIf
	
	If btype=0 ; sprite
		ScaleSprite p\Entity,bsize,bsize
	Else	; flatsquare
		ScaleEntity p\Entity,bsize,1,bsize
	EndIf
	
	
	PositionEntity p\Entity,x,y,z
	
	
	
	
	
	NofParticles=NofParticles+1
	
	
	
	

End Function

Function ControlParticles()

	
	
	For p.particle = Each particle
		
		; move it
		TranslateEntity p\Entity,p\dx,p\dy,p\dz
		
		; gravity?
		p\dy=p\dy-p\ddy
		; turn it
		If p\dr<>0
			RotateSprite p\Entity,p\r
			p\r=p\r+p\dr
		EndIf
		
		p\Timer=p\Timer-1

		
		;phase out
		EntityAlpha p\Entity,Float(p\Timer)/Float(p\Timer2)
		
		;water ripple: scale
		If p\dx=0 And p\dy=0 And p\dz=0
			ScaleEntity p\Entity,2-Float(p\Timer)/Float(p\Timer2),1,2-Float(p\Timer)/Float(p\Timer2)
			TranslateEntity p\Entity,-(0.5/p\Timer2),0,(0.5/p\Timer2)
		EndIf
		
		If p\Timer=0 Or EntityY(p\Entity)<-.1 
			FreeEntity p\Entity
			Delete p
			NofParticles=NofParticles-1
			
		EndIf
				
	Next
	
	
End Function