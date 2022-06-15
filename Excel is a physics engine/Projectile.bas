Attribute VB_Name = "Projectile"
'vertical position of a falling object over time
Sub Projectile()
    'equations of motion
    'a = g
    'v = g*t + vInitial
    'x = (1/2)*g*t^2 + vInitial*t + xInitial
    
    Dim t, tMax As Integer
    Dim G, V, x, vInitial, xInitial, floor As Double
    Dim address As String
    Dim rowIndex, colIndex As Integer
    
    tMax = 500
    G = -0.01
    vInitial = 1.5
    xInitial = 100
    floor = 300
    Rows(floor).Interior.color = RGB(255, 0, 0)
    
    For t = 1 To tMax
        'x represents the "actual" height above the floor
        x = (1 / 2) * G * (t ^ 2) + vInitial * t + xInitial
        x = WorksheetFunction.Max(x, 0)
        
        rowIndex = WorksheetFunction.Max(-x + floor, 1)
        colIndex = WorksheetFunction.Max(t, 1)
        address = Cells(rowIndex, colIndex).address()
        Range(address).Interior.color = RGB(0, 0, 0)
    Next t
    
End Sub

'"true" 2D projectile path, where x and y are functions of t
Sub Projectile2()
    Dim t, tMax As Integer
    Dim x(1) As Double 'position vector
    Dim V(1) As Double 'velocity vector
    Dim address As String
    Dim floor As Integer
    Dim rowIndex, colIndex As Integer
    
    x(0) = 1
    x(1) = 200
    V(0) = 0.7
    V(1) = 1.2
    tMax = 1000
    floor = 300
    Rows(floor).Interior.color = RGB(255, 0, 0)
    
    For t = 1 To tMax
        Call Step(x(), V())
        
        rowIndex = WorksheetFunction.Max(-x(1) + floor, 1)
        colIndex = x(0)
        address = Cells(rowIndex, colIndex).address()
        Range(address).Interior.color = RGB(0, 0, 0)
        If t Mod 10 = 0 Then Sleep 1 'show motion
    Next t
    
End Sub

'takes position and velocity vectors and returns the next next step
Sub Step(ByRef x() As Double, ByRef V() As Double)
    Const G As Double = -0.01 'downward acceleration
    
    x(0) = x(0) + V(0)
    x(1) = WorksheetFunction.Max(x(1) + V(1), 0) 'restrict to non-negative vertical position
    
    'bouncing effect
    If x(1) = 0 Then
        V(1) = -0.5 * V(1)
    End If
    
    V(1) = V(1) + G 'apply acceleration
End Sub
