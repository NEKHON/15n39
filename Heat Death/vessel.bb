; Ships

Const shipsMaxThrusters%=4

Type vessel

; Apereance

Field body%

; Controling

Field id% ; Ship id
Field active% ; does ship active and controled or not

Field shipmode%=0

; Physics

Field mass%

Field velocity#[2] ; blitz array with XYZ velocity
Field torque#[2] ; blitz array with XYZ rotation speed

Field acceleration#[2] ; blitz array with XYZ acceleration speed
Field momentum#[2] ; blitz array with XYZ torque momentum
Field gyropower#

Field throttlesens# ; throttle sensetivity

Field throttle# ; from 0.0 to 1.0, ship throttle. ( accel = (enginepower * throttle) )
 
End Type

Type thruster 

Field connectedShipId%
Field body% ; thruster visual
Field radius% ; thruster radius, for collision

Field gimbalAngle%[1] ; gimbal angle of thruster, XYZ
Field power# ; thruster power
 

Field doesforward% ; can be used for frontal accel?
Field doesreverse% ; can be used for reverse accel?
Field doesvtol% ; can be used for vtol accel?

Field thrusteractive%

End Type 


Function vesselUpdate(deltaTime#)

For this.vessel = Each vessel

If EntityPick(this\body,4) Then End()


If this\active>0 Then ; Controling

PositionEntity shipPoint,EntityX(this\body),EntityY(this\body),EntityZ(this\body)

currentShip=this\body

; chunks

If EntityX(this\body)>chunkSize Then TranslateEntity this\body,-chunkSize,0,0 TranslateEntity worldPivot,-chunkSize,0,0
If EntityX(this\body)<-chunkSize Then TranslateEntity this\body,chunkSize,0,0 TranslateEntity worldPivot,chunkSize,0,0

If EntityY(this\body)>chunkSize Then TranslateEntity this\body,0,-chunkSize,0 TranslateEntity worldPivot,0,-chunkSize,0
If EntityY(this\body)<-chunkSize Then TranslateEntity this\body,0,chunkSize,0 TranslateEntity worldPivot,0,chunkSize,0

If EntityZ(this\body)>chunkSize Then TranslateEntity this\body,0,0,-chunkSize TranslateEntity worldPivot,-0,0,-chunkSize
If EntityZ(this\body)<-chunkSize Then TranslateEntity this\body,0,0,chunkSize TranslateEntity worldPivot,0,0,chunkSize


this\throttle#=this\throttle+((this\throttlesens#*SignalThrottle)*deltaTime#)

If this\throttle>1 Then this\throttle=1
If this\throttle<0 Then this\throttle=0

displayThrottle=this\throttle*100

; modes

If KeyHit(2) Then this\shipmode=0 
If KeyHit(3) Then this\shipmode=1
If KeyHit(4) Then this\shipmode=2

; CONTROL THRUSTERS

For this2.thruster=Each thruster

If this2\connectedShipId=this\id Then

; modes

Select this\shipmode
Case 0
	If this2\doesforward >0 Then this2\thrusteractive=1 RotateEntity this2\body,90,0,0
	If this2\doesreverse>0 Then this2\thrusteractive=0 RotateEntity this2\body,90,0,0

Case 1
	If this2\doesreverse>0 Then this2\thrusteractive=1 RotateEntity this2\body,-90,0,0
	If this2\doesforward>0 Then this2\thrusteractive=0 RotateEntity this2\body,90,0,0
Case 2
	If this2\doesvtol>0 Then this2\thrusteractive=1 RotateEntity this2\body,0,0,0
End Select

; tform

If this2\thrusteractive>0 Then
TurnEntity this2\body,this2\gimbalAngle[0]*SignalGimbalX,0,-this2\gimbalAngle[1]*SignalGimbalZ
TFormVector 0,(this2\power/this\mass)*this\throttle,0,this2\body,0


this\acceleration[0]=this\acceleration[0]+TFormedX()
this\acceleration[1]=this\acceleration[1]+TFormedY()
this\acceleration[2]=this\acceleration[2]+TFormedZ()

If this\throttle>0 Then


this3.particle=New particle
this3\entity=CreateSprite(this2\body)
;EntityTexture this3\entity,brush
ScaleSprite this3\entity,0.5,0.5
EntityColor this3\entity,Rand(0,255),0,Rand(0,255)
this3\velocity[1]=-40*this\throttle
this3\velocity[0]=Rand(-6,6)
this3\velocity[2]=Rand(-6,6)
PositionEntity this3\entity,0,-1,0
this3\lifetime=Rand(10,20)
 

End If 

TFormVector 0,(this2\power/this\mass)*this\throttle,0,this2\body,this\body

If this2\doesreverse>0 Then 
this\momentum[1] = this\momentum[1] - TFormedX()
Else 
this\momentum[1] = this\momentum[1] + TFormedX()
End If 

If this2\doesreverse>0 Then 
this\momentum[0] = this\momentum[0] - TFormedY()
Else 
this\momentum[0] = this\momentum[0] + TFormedY()
End If 


displayYawAccel#=this\momentum[1]

this\momentum[1] = this\momentum[1] + -SignalGyroY*this\gyropower
this\momentum[0] = this\momentum[0] + SignalGyroX*this\gyropower
this\momentum[2] = this\momentum[2] + (KeyDown(16)-KeyDown(18))*this\gyropower

If KeyHit(57) Then this\torque[0]=0 this\torque[1]=0 this\torque[2]=0 this\velocity[0]=0 this\velocity[1]=0 this\velocity[2]=0

End If

End If 

Next

End If

; PHYSICS

displayaccelx=EntityX(this\body)
displayaccely=EntityY(this\body)
displayaccelz=EntityZ(this\body)

For i=0 To 2
this\velocity[i]=this\velocity[i]+(this\acceleration[i]*deltaTime) ; acceleration 

this\velocity[i]=this\velocity[i]+(globalPush[i]*deltaTime) ; push by graity
Next

For i=0 To 2
this\torque[i]=this\torque[i]+(this\momentum[i]*deltaTime)
Next



TurnEntity this\body,this\torque[0]*deltaTime,this\torque[1]*deltaTime,this\torque[2]*deltaTime
TranslateEntity this\body,this\velocity[0]*deltaTime,this\velocity[1]*deltaTime,this\velocity[2]*deltaTime

For i=0 To 2
this\acceleration#[i]=0
Next

For i=0 To 2
this\momentum[i]=0
Next





 
Next 

End Function


; CREATE TEST SHIP!!!!

; create ship ---

this.vessel = New vessel

this\body = CreateCube()
RotateMesh this\body,0,45,0
ScaleMesh this\body,2,0.4,2
EntityColor this\body,60,60,60

TurnEntity this\body,0,90,0

cameraPivot=CreatePivot(this\body)
camera=CreateCamera(cameraPivot)
CameraRange camera,0.5,100000
MoveEntity camera,0,3,-25

this\id=0
this\active=1
this\throttlesens=0.5
this\gyropower#=2
this\mass=430

; thrusters

this2.thruster = New thruster ; Frontal Left, Backward + Vtol

this2\connectedShipId=0
this2\body=CreateCylinder(6,1,this\body)
EntityColor this2\body,100,100,100
PositionEntity this2\body,-2.5,0,2.5

this2\radius%=1
this2\gimbalAngle[0]=15
this2\gimbalAngle[1]=15

this2\power=13000
this2\doesreverse%=1
this2\doesvtol=1

this2.thruster = New thruster ; Frontal Right, Backward + Vtol

this2\connectedShipId=0
this2\body=CreateCylinder(6,1,this\body)
EntityColor this2\body,100,100,100
PositionEntity this2\body,2.5,0,2.5

this2\radius%=1
this2\gimbalAngle[0]=15
this2\gimbalAngle[1]=15

this2\power=13000
this2\doesreverse%=1
this2\doesvtol=1

this2.thruster = New thruster ; Backward Left, Forward + Vtol

this2\connectedShipId=0
this2\body=CreateCylinder(6,1,this\body)
EntityColor this2\body,100,100,100
PositionEntity this2\body,-2.5,0,-2.5

this2\radius%=1
this2\gimbalAngle[0]=15
this2\gimbalAngle[1]=15

this2\power=13000
this2\doesforward%=1
this2\doesvtol=1

this2.thruster = New thruster ; Backward Right, Forrdward + Vtol

this2\connectedShipId=0
this2\body=CreateCylinder(6,1,this\body)
EntityColor this2\body,100,100,100
PositionEntity this2\body,2.5,0,-2.5

this2\radius%=1
this2\gimbalAngle[0]=15
this2\gimbalAngle[1]=15

this2\power=13000
this2\doesforward=1
this2\doesvtol=1
;~IDEal Editor Parameters:
;~C#Blitz3D