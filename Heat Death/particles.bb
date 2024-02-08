; PARTICLES TYPE, PARTICLES FUNCTION

; --- TYPE
Type particle

Field entity% ; Entity
Field velocity%[2] ; Array, XYZ elocity

Field lifetime% ; In frames

End Type

Function particlesUpdate(deltaTime#)

For this3.particle=Each particle

MoveEntity this3\entity,this3\velocity[0]*deltaTime,this3\velocity[1]*deltaTime,this3\velocity[2]*deltaTime
RotateSprite this3\entity,Rand(0,360)

this3\lifetime=this3\lifetime-1 : If this3\lifetime<1 Then FreeEntity this3\entity Delete this3

Next	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D