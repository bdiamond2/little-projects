Attribute VB_Name = "NBodyGravity"
Sub Main()
    Dim sun As New Particle
    Dim earth As New Particle
    Dim moon As New Particle
'    Dim bodies() As New particle
    Dim floor As Integer
    Dim t, tMax As Long
    
    '===SET YOUR PARAMETERS HERE===
    tMax = 100 'tMax = 100 translates to ~10 seconds
    
    Call sun.SetMass(3000)
    Call earth.SetMass(10)
    Call moon.SetMass(1)
    
    Call sun.SetPosition(250, 250)
    Call earth.SetPosition(650, 250)
    Call moon.SetPosition(655, 250)
    
    Call sun.SetVelocity(0, 0)
    Call earth.SetVelocity(0, -0.01)
    Call moon.SetVelocity(0, -0.02)
    '===============================
    
    floor = 300
    Rows(floor).Interior.color = RGB(255, 0, 0)
    tMax = tMax * 1000
    
    For t = 1 To tMax
        Call NBodyGravityStep(earth, sun)
        Call NBodyGravityStep(earth, moon)
        
        Call NBodyGravityStep(sun, earth)
        Call NBodyGravityStep(sun, moon)
        
        Call NBodyGravityStep(moon, earth)
        Call NBodyGravityStep(moon, sun)
        
        
        'plot initial point and once then every 25
        If t < 10 Or t Mod 25 = 0 Then
            Call SheetFunctions.Plot(earth.GetPosition(0), earth.GetPosition(1), floor, RGB(0, 255, 0))
            Call SheetFunctions.Plot(sun.GetPosition(0), sun.GetPosition(1), floor, RGB(0, 0, 255))
            Call SheetFunctions.Plot(moon.GetPosition(0), moon.GetPosition(1), floor, RGB(255, 0, 255))
        End If
                
    Next t
    
End Sub

'This function takes two particles, "moving" and "stationary", and applies gravitational
'force onto the moving particle exerted by the stationary particle.
Sub NBodyGravityStep(mov As Particle, still As Particle)
    'law of gravity in vector form
    'F = G*(m1 * m2)/(r^2) * rHat,
    'where rHat is the unit vector from particle 1 to particle 2
    'rHat = (r2 - r1)/|r2 - r1|
    
    Dim forceFactor As Double 'the non-vector part of grav force
    Dim forceVector(1) As Double '2D force vector
    Const G As Double = 0.0001 'gravitational constant (not the real life one)
    Dim r As Double 'distance between particles
    Dim rHat(1) As Double 'unit vector from particle 1 to particle 2
     
    r = WorksheetFunction.Power(((still.GetPosition(0) - mov.GetPosition(0)) ^ 2) + ((still.GetPosition(1) - mov.GetPosition(1)) ^ 2), 1 / 2)
    
    'set new acceleration
    'account for r near zero (so we don't divide by ~0 and get insane acceleration)
    If r >= 0.5 Then
        Call mov.SetAcceleration(forceVector(0) / mov.GetMass, forceVector(1) / mov.GetMass)
        'set rHat
        rHat(0) = (still.GetPosition(0) - mov.GetPosition(0)) / r
        rHat(1) = (still.GetPosition(1) - mov.GetPosition(1)) / r
        
        'forceVector represents the force on mov exerted by still
        forceFactor = G * (mov.GetMass * still.GetMass) / (r ^ 2) 'scalar force
        forceVector(0) = forceFactor * rHat(0) 'x component
        forceVector(1) = forceFactor * rHat(1) 'y component
        
        'set particle accelerations
        'F = ma ==> a = F/m
        Call mov.SetAcceleration(forceVector(0) / mov.GetMass, forceVector(1) / mov.GetMass)
    Else
        forceVector(0) = 0 'x component
        forceVector(1) = 0 'y component
    End If
    
'    'set new velocity
'    'vNew = vOld + a
    Call mov.SetVelocity(mov.GetVelocity(0) + mov.GetAcceleration(0), mov.GetVelocity(1) + mov.GetAcceleration(1))
    
'    'set new position
'    'pNew = pOld + v
    Call mov.SetPosition(mov.GetPosition(0) + mov.GetVelocity(0), mov.GetPosition(1) + mov.GetVelocity(1))
    
    
End Sub
