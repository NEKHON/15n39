Global globalDeltaTime#
Global oldTime#
Global FPSOLDTIME%
Global FPSFRAME%=0

Function calculateDeltaTime(CURTIME#,OLDDTIME#,MINLIMIT#,MAXLIMIT#)

Local DELTATIME#

DELTATIME#=(CURTIME#-OLDDTIME#)/1000
If DELTATIME<0.016 Then DELTATIME=0.016

Return DELTATIME#

End Function	

;~IDEal Editor Parameters:
;~C#Blitz3D