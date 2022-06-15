VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
END
Attribute VB_Name = "Particle"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False
'Class module Particle

Private Position(1) As Double
Private Velocity(1) As Double
Private Acceleration(1) As Double
Private Mass As Double

Public Function GetPosition()
    GetPosition = Position
End Function

Public Sub SetPosition(x As Double, y As Double)
    Position(0) = x
    Position(1) = y
End Sub

Public Function GetVelocity()
    GetVelocity = Velocity
End Function

Public Sub SetVelocity(x As Double, y As Double)
    Velocity(0) = x
    Velocity(1) = y
End Sub

Public Function GetAcceleration()
    GetAcceleration = Acceleration
End Function

Public Sub SetAcceleration(x As Double, y As Double)
    Acceleration(0) = x
    Acceleration(1) = y
End Sub

Public Function GetMass()
    GetMass = Mass
End Function

Public Sub SetMass(m As Double)
    Mass = m
End Sub
