Attribute VB_Name = "Gravity"
Sub Gravity()
    
    'x(0,) = position
    'x(1,) = velocity
    'x(2,) = acceleration
    'x(3,0) = mass
    Dim x(3, 1) As Double
    Dim y(3, 1) As Double
    Dim floor As Integer
    Dim t, tMax As Integer
    Dim vel As Double
    
    tMax = 2000
    floor = 300
    Rows(floor).Interior.color = RGB(255, 0, 0)
    
    'initial positions
    x(0, 0) = 150
    x(0, 1) = 200
    y(0, 0) = 150
    y(0, 1) = 150
    
    'initial velocities
    x(1, 0) = 0.1
    x(1, 1) = 0
    y(1, 0) = 0
    y(1, 1) = 0
    
    'accelerations are calculated dynamically
    
    'masses
    x(3, 0) = 10
    y(3, 0) = 10000
    
    For vel = 0.55 To 0.65 Step 0.02
        x(0, 0) = 150
        x(0, 1) = 200
        y(0, 0) = 150
        y(0, 1) = 150
        
        x(1, 0) = vel
        x(1, 1) = 0
        y(1, 0) = 0
        y(1, 1) = 0
        For t = 1 To tMax
            'remove the last dot
    '        Call Plot(x(0, 0), x(0, 1), floor, RGB(255, 255, 255))
    '        Call Plot(y(0, 0), y(0, 1), floor, RGB(255, 255, 255))
            
            Call GravityStep(x(), y())
            'plot the new dot
            Call Plot(x(0, 0), x(0, 1), floor, RGB(0, 255, 0))
            Call Plot(y(0, 0), y(0, 1), floor, RGB(0, 0, 255))
                 
            If t Mod 10 = 0 Then
                Sleep 1 'show motion
            End If
        Next t
    Next vel
    
End Sub

'moves both particles by one step based on gravitational force
Sub GravityStep(ByRef x() As Double, y() As Double)
    'law of gravity in vector form
    'F = -G*(m1 * m2)/(r^2) * rHat,
    'where rHat is the unit vector from particle 1 to particle 2
    'rHat = (r2 - r1)/|r2 - r1|
    
    Dim forceFactor As Double 'the non-vector part of the force
    Dim forceVector(1) As Double '2D force vector
    Const G As Double = 0.001 'gravitational constant
    Dim r As Double 'distance between particles
    Dim rHat(1) As Double 'unit vector from particle 1 to particle 2
    
    r = WorksheetFunction.Power(((y(0, 0) - x(0, 0)) ^ 2) + ((y(0, 1) - x(0, 1)) ^ 2), 1 / 2)
    
    'set rHat
    rHat(0) = (y(0, 0) - x(0, 0)) / r
    rHat(1) = (y(0, 1) - x(0, 1)) / r
    
    'forceVector represents the force on x exerted by y
    forceFactor = -G * (x(3, 0) * y(3, 0)) / (r ^ 2) 'scalar force
    forceVector(0) = forceFactor * rHat(0)
    forceVector(1) = forceFactor * rHat(1)
    
    'set particle accelerations
    'F = ma ==> a = F/m
    x(2, 0) = -forceVector(0) / x(3, 0)
    x(2, 1) = -forceVector(1) / x(3, 0)
    y(2, 0) = forceVector(0) / y(3, 0)
    y(2, 1) = forceVector(1) / y(3, 0)
    
    'set velocities
    'vNew = vOld + a
    x(1, 0) = x(1, 0) + x(2, 0)
    x(1, 1) = x(1, 1) + x(2, 1)
    y(1, 0) = y(1, 0) + y(2, 0)
    y(1, 1) = y(1, 1) + y(2, 1)
    
    'set positions
    'pNew = pOld + v
    x(0, 0) = x(0, 0) + x(1, 0)
    x(0, 1) = x(0, 1) + x(1, 1)
    y(0, 0) = y(0, 0) + y(1, 0)
    y(0, 1) = y(0, 1) + y(1, 1)
End Sub

