Graphics3D 1200,800,32,2
SetBuffer BackBuffer()
SeedRnd MilliSecs()
; Versions...

Const gameVersion$="Ver.0.1"
Include "deltatime.bb"
Include "apptitle.bb"

; signals

Global SignalThrottle%=0

Global SignalGimbalX%=0

Global SignalGimbalZ%=0

Global SignalRoll%=0

Global SignalGyroX%=0
Global SignalGyroY%=0

; display

Global displayThrottle#
Global displayGimbal[2]

Global displayaccelx#=0
Global displayaccely#=0
Global displayaccelz#=0

Global displayYawAccel#

Global chunkSize%=1000

Global gfxCursor=LoadImage("2d/cursor.png")

Include "vessel.bb" ; SPACESHIP FUNCTIONS, TYPES
Include "particles.bb" ; PARTICLES TYPES, FUNCTION 
Include "celestials.bb" ; CELESTIAL BODIES TYPES, FUNCTIONS

Global currentShip
Global globalPush#[2] ; keyboard dont work, rename in future

Global radiusMultiplier=1

Global worldPivot=CreatePivot()
light=CreateLight(2,worldPivot)
LightRange light,1400

Global celTatro = createCelestial(0,"196 TATRO-1",450,13000,116,80,30,0.001,-0.008,3) ; Medium Rocky Planet
MoveEntity celTatro,0,0,2000


fpstimer=CreateTimer(120)
;AmbientLight 0,0,4

Global shipPoint=CreatePivot()

Repeat ; GAME CYCLE
WaitTimer fpsTIMER

; Delta Time

globalDeltaTime=(MilliSecs()-oldTime)/1000 
oldTime#=MilliSecs()
If globalDeltaTime<0.01 Then globalDeltaTime=0.01
If globalDeltaTime>0.1 Then globalDeltaTime=0.1

If MouseDown(2)
TurnEntity cameraPivot,MouseYSpeed(),-MouseXSpeed(),KeyDown(71)KeyDown(73)
MoveEntity camera,0,0,MouseZSpeed()
RotateEntity cameraPivot,EntityPitch(cameraPivot),EntityYaw(cameraPivot),0

If EntityPitch(cameraPivot)>85 Then RotateEntity cameraPivot,85,EntityYaw(cameraPivot),0
If EntityPitch(cameraPivot)<-85 Then RotateEntity cameraPivot,-85,EntityYaw(cameraPivot),0
End If 



; control signals
SignalThrottle=KeyDown(44)-KeyDown(45)

SignalGyroY=MouseXSpeed():SignalGyroX=MouseYSpeed()

;SignalGDDDDDDDyroX=MouseYSpeed()

;SignalGyroY=MouseXSpeed()

SignalGimbalX%=KeyDown(17)-KeyDown(31)

SignalGimbalZ%=KeyDown(30)-KeyDown(32)		

SignalRoll%=KeyDown(18)-KeyDown(16)

; update ships

vesselUpdate(globalDeltaTime#)
particlesUpdate(globalDeltaTime)


; render worlds


For i=0 To 2
globalPush[i]=0
Next

For cel.celestial=Each celestial
UpdateNormals(cel\body)

EntityFX cel\body,2

If KeyDown(57) Then EntityFX cel\body,1+8+64

If EntityDistance(currentShip,cel\body)<cel\soiRadius

gforce# = Abs(6.8 * ((4300 * 8700000) / (EntityDistance(currentShip,cel\body)^2)))
TFormPoint 0,0,1,shipPoint,cel\body

globalPush[0] = globalPush[0] - TFormedX()
globalPush[1] = globalPush[1] - TFormedY()
globalPush[2] = globalPush[2] - TFormedZ()

orbitalPeriod% = EntityDistance(currentShip,cel\body)-cel\radius
End If 

Next

UpdateWorld()

RenderWorld()

MoveMouse 200,200

FPSFRAME=FPSFRAME+1

If MilliSecs()>FPSOLDTIME+1000 Then
FPS=FPSFRAME
FPSFRAME=0
FPSOLDTIME%=MilliSecs()
End If 

; 2d graphic

Color 200,200,200

Text 50,20,"fps: "+FPS+" deltatime: "+globalDeltaTime
Text 50,40,"local pos:" +Ceil(displayaccelx)+" "+Ceil(displayaccely)+" "+Ceil(displayaccelz)

Text 50,70,"global pos:"+EntityX(worldPivot)+" "+EntityY(worldPivot)+" "+EntityZ(worldPivot)
;Text 50,83,"tris rendered: "+TrisRendered()

Text 300,10,globalPush[0]+" "+globalPush[1]+" "+globalPush[2]
Text 90,10,Ceil(orbitalPeriod)
Color 255,0,0

Rect 10,400,5,displayThrottle
Text 20,390+displayThrottle,Int(displayThrottle)

DrawImage gfxCursor,GraphicsWidth()/2-32,GraphicsHeight()/2-32+60

Flip(0)
Cls

If FPS>60 Then Delay 1


Until KeyHit(1)

ClearWorld()
FreeImage gfxCursor
EndGraphics()
End 


;~IDEal Editor Parameters:
;~C#Blitz3D