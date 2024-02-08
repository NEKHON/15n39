Type celestial

Field mainPivot%
Field body%
Field surface%

Field radius#

Field id%
Field name$

Field soiRadius%
Field freefallSpeed#

End Type

Function createCelestial(id%,name$,radius%,soi%,mcR%,mcG%,mcB%,displacement1#,displacement2#,j)

cel.celestial=New celestial

cel\mainPivot=CreatePivot(worldPivot)
cel\id = id
cel\soiRadius=soi%

cel\radius=radius
cel\radius=cel\radius*radiusMultiplier

cel\body=CreateSphere(64,cel\mainPivot)
ScaleEntity cel\body,cel\radius*2,cel\radius*2,cel\radius*2

cel\surface=GetSurface(cel\body,CountSurfaces(cel\body))

For i=0 To CountVertices(cel\surface)-1
EntityFX cel\body,2

x=Rand(0,j)


Select x

Case 1 ; highlands
VertexCoords(cel\surface,i,VertexX(cel\surface,i)+displacement1,VertexY(cel\surface,i)+displacement1,VertexZ(cel\surface,i)+displacement1)
VertexColor(cel\surface,i,mcR+30,mcG+30,mcB+30)
Case 2 ; lowlands
VertexCoords(cel\surface,i,VertexX(cel\surface,i)+displacement2,VertexY(cel\surface,i)+displacement2,VertexZ(cel\surface,i)+displacement2)
VertexColor(cel\surface,i,mcR-20,mcG-20,mcB-20)

Default ; midlands
VertexCoords(cel\surface,i,VertexX(cel\surface,i),VertexY(cel\surface,i),VertexZ(cel\surface,i))
VertexColor(cel\surface,i,mcR,mcG,mcB)
End Select	



Next

Return	cel\mainPivot

End Function
;~IDEal Editor Parameters:
;~C#Blitz3D